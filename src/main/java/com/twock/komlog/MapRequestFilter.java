package com.twock.komlog;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.base64.Base64;
import org.jboss.netty.handler.codec.http.*;
import org.littleshoot.proxy.HttpRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Chris Pearson
 */
@Component
public class MapRequestFilter implements HttpRequestFilter {
  private static final Logger log = LoggerFactory.getLogger(MapRequestFilter.class);
  private static final Pattern REQUEST_DATA = Pattern.compile("(?<=data=)(.*?)(?=&)");
  private static final int SQUARE_SIZE = 10;
  private static final boolean REWRITING = true;
  @Inject
  private Charset utf8;

  @Override
  public void filter(HttpRequest httpRequest) {
    if (!REWRITING) {
      return;
    }
    if(!HttpMethod.POST.equals(httpRequest.getMethod()) || !httpRequest.getUri().endsWith("/fetchMapTiles.php")) {
      return;
    }
    try {
/*
      XStream xStream = new XStream();
      String xml = xStream.toXML(httpRequest);
      FileWriter fetchRequest = new FileWriter("fetchRequest.xml");
      fetchRequest.write(xml);
      fetchRequest.close();
*/

      String text = httpRequest.getContent().toString(utf8);
      Matcher requestMatcher = REQUEST_DATA.matcher(text);
      if(!requestMatcher.find()) {
        log.warn("Unable to find data element in request data: {}", text);
        return;
      }
      StringBuffer sb = new StringBuffer();
      requestMatcher.appendReplacement(sb, replace1(requestMatcher.group(1)));
      requestMatcher.appendTail(sb);
      ChannelBuffer channelBuffer = ChannelBuffers.copiedBuffer(sb.toString().getBytes(utf8));
      httpRequest.setContent(channelBuffer);
    } catch(Exception e) {
      log.debug("Failed to decode base64: {}", httpRequest.getContent().toString(), e);
    }
  }

  private String replace1(String data) throws UnsupportedEncodingException {
    String base64data = URLDecoder.decode(data, "UTF-8");
    String decoded = Base64.decode(ChannelBuffers.copiedBuffer(base64data.getBytes(utf8))).toString(utf8);
    String replacement = replace2(decoded);
    String encoded = Base64.encode(ChannelBuffers.copiedBuffer(replacement.getBytes(utf8))).toString(utf8);
    return URLEncoder.encode(encoded, "UTF-8");
  }

  private String replace2(String decoded) {
    log.debug("Processing decoded request data {}", decoded);
    QueryStringDecoder decoder = new QueryStringDecoder(decoded, utf8, false);
    Map<String, List<String>> parameters = decoder.getParameters();
    log.debug("Parameters: {}", parameters);

    List<String> blocks = parameters.get("blocks");
    String[] blockNames = blocks.get(0).split(",");
    Integer[] extents = getExtents(blockNames);
    log.debug("Querying blocks {}", blocks);
    StringBuilder sb = new StringBuilder();
    int xStart = (int)Math.max(0.0, Math.min(800.0 - SQUARE_SIZE * 5.0, Math.round((double)(extents[0] + extents[1]) / 10.0) * 5));
    int yStart = (int)Math.max(0.0, Math.min(800.0 - SQUARE_SIZE * 5.0, Math.round((double)(extents[2] + extents[3]) / 10.0) * 5));
    for(int x = 0; x < SQUARE_SIZE; x++) {
      for(int y = 0; y < SQUARE_SIZE; y++) {
        if(y > 0 || x > 0) {
          sb.append(",");
        }
        sb.append("bl_").append(xStart + 5 * x).append("_bt_").append(yStart + 5 * y);
      }
    }
    log.debug("Overriding with block query for {}", sb);
    parameters.put("blocks", Arrays.asList(sb.toString()));
    parameters.put("changed", Collections.<String>emptyList());

    QueryStringEncoder encoder = new QueryStringEncoder("", utf8);
    for(Map.Entry<String, List<String>> entry : parameters.entrySet()) {
      for(String parameterValue : entry.getValue()) {
        encoder.addParam(entry.getKey(), parameterValue);
      }
    }
    String rewrittenDecoded = encoder.toString().substring(1);
    log.debug("Updated decoded request data: {}", rewrittenDecoded);
    return rewrittenDecoded;
  }

  private Integer[] getExtents(String[] blockNames) {
    Integer[] result = new Integer[4];
    for(String blockName : blockNames) {
      String[] split = blockName.split("_");
      int x = Integer.parseInt(split[1]);
      int y = Integer.parseInt(split[3]);
      if(result[0] == null || x < result[0]) {
        result[0] = x;
      }
      if(result[1] == null || x > result[1]) {
        result[1] = x;
      }
      if(result[2] == null || y < result[2]) {
        result[2] = y;
      }
      if(result[3] == null || y > result[3]) {
        result[3] = y;
      }
    }
    return result;
  }
}
