package com.twock.komlog;

import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.Base64Encoder;
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
  @Inject
  private Charset utf8;

  @Override
  public void filter(HttpRequest httpRequest) {
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
//      httpRequest.setContent(channelBuffer);
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
    Map<String,List<String>> parameters = decoder.getParameters();
    log.debug("Parameters: {}", parameters);

    List<String> blocks = parameters.get("blocks");
    String[] blockNames = blocks.get(0).split(",");
    log.debug("Querying blocks {}", blocks);
    for (int x = 0; x < 10; x++) {

    }

    QueryStringEncoder encoder = new QueryStringEncoder("", utf8);
    for(Map.Entry<String, List<String>> entry : parameters.entrySet()) {
      for(String parameterValue : entry.getValue()) {
        encoder.addParam(entry.getKey(), parameterValue);
      }
    }

/*
    Pattern blocks = Pattern.compile("blocks=(bl_\\d+_bt_\\d+,?)");
    Matcher matcher = blocks.matcher(decoded);
    if(matcher.matches()) {
      StringBuffer sb = new StringBuffer();
      matcher.appendReplacement(sb, "blocks=");
      for(int bl = 1; bl < 10; bl++) {
        for(int bt = 1; bt < 10; bt++) {
          if(bl > 1 || bt > 1) {
            sb.append(',');
          }
          sb.append("bl_").append(bl * 5).append("_bt_").append(bt * 5);
        }
      }
      matcher.appendTail(sb);
      log.debug("Reformed request to {}", sb.toString());
      httpRequest.setContent(Base64.encode(ChannelBuffers.copiedBuffer(URLEncoder.encode(sb.toString(), "UTF-8").getBytes(Charset.forName("UTF-8")))));
    }
*/
    return decoded;
  }
}
