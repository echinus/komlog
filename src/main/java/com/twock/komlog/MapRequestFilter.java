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
  private static final int SQUARE_SIZE = 3;
  private static final boolean REWRITING = false;
  @Inject
  private Charset utf8;

  @Override
  public void filter(HttpRequest httpRequest) {
    if(!HttpMethod.POST.equals(httpRequest.getMethod()) || !httpRequest.getUri().endsWith("/fetchMapTiles.php")) {
      return;
    }
    try {
/*
22:34:05.459 [New I/O worker #1] DEBUG com.twock.komlog.MapRequestFilter - Updated decoded request data: blocks=bl_315_bt_180%2Cbl_315_bt_185%2Cbl_315_bt_190%2Cbl_315_bt_195%2Cbl_315_bt_200%2Cbl_320_bt_180%2Cbl_320_bt_185%2Cbl_320_bt_190%2Cbl_320_bt_195%2Cbl_320_bt_200%2Cbl_325_bt_180%2Cbl_325_bt_185%2Cbl_325_bt_190%2Cbl_325_bt_195%2Cbl_325_bt_200%2Cbl_330_bt_180%2Cbl_330_bt_185%2Cbl_330_bt_190%2Cbl_330_bt_195%2Cbl_330_bt_200%2Cbl_335_bt_180%2Cbl_335_bt_185%2Cbl_335_bt_190%2Cbl_335_bt_195%2Cbl_335_bt_200&lang=en&kabam_id=114380120&naid=112785252&access_token=c07c2fb8-bdb9-4c24-982e-1afc291d7f13&mobileid=775584a764de3392371de27fadd1272b&platformid=203&become_user_id=&become_password=&debug=0&gver=6.0.1&gameSlot=20&race=1&gameNumber=1375219936&gameKey=f74fa828e2521e9eee77a7d03875e4b3
22:34:05.662 [New I/O worker #1] DEBUG com.twock.komlog.MapFilter - Filtering POST http://www72.hobbitmobile.com/ajax/fetchMapTiles.php [Content-Type=application/x-www-form-urlencoded, User-Agent=Dalvik/1.6.0 (Linux; U; Android 4.1.2; GT-I9300 Build/JZO54K), Host=www72.hobbitmobile.com, Connection=Keep-Alive, Accept-Encoding=gzip, Content-Length=541] data=YmxvY2tzPWJsXzMxNV9idF8xODUlMmNibF8zMTVfYnRfMTgwJTJjYmxfMzE1X2J0XzE3NSUyY2JsXzMxMF9idF8xODUlMmNibF8zMTBfYnRfMTgwJTJjYmxfMzEwX2J0XzE3NSZjaGFuZ2VkPSZsYW5nPWVuJmthYmFtX2lkPTExNDM4MDEyMCZuYWlkPTExMjc4NTI1MiZhY2Nlc3NfdG9rZW49YzA3YzJmYjgtYmRiOS00YzI0LTk4MmUtMWFmYzI5MWQ3ZjEzJm1vYmlsZWlkPTc3NTU4NGE3NjRkZTMzOTIzNzFkZTI3ZmFkZDEyNzJiJnBsYXRmb3JtaWQ9MjAzJmJlY29tZV91c2VyX2lkPSZiZWNvbWVfcGFzc3dvcmQ9JmRlYnVnPTAmZ3Zlcj02LjAuMSZnYW1lU2xvdD0yMCZyYWNlPTEmZ2FtZU51bWJlcj0xMzc1MjE5OTM2JmdhbWVLZXk9Zjc0ZmE4MjhlMjUyMWU5ZWVlNzdhN2QwMzg3NWU0YjM%3d&vcs=6.0.1

22:34:51.687 [New I/O worker #3] DEBUG com.twock.komlog.MapFilter - Filtering POST http://www72.hobbitmobile.com/ajax/fetchMapTiles.php [Content-Type=application/x-www-form-urlencoded, User-Agent=Dalvik/1.6.0 (Linux; U; Android 4.1.2; GT-I9300 Build/JZO54K), Host=www72.hobbitmobile.com, Connection=Keep-Alive, Accept-Encoding=gzip, Content-Length=541] data=YmxvY2tzPWJsXzMxNV9idF8xODUlMmNibF8zMTVfYnRfMTgwJTJjYmxfMzE1X2J0XzE3NSUyY2JsXzMxMF9idF8xODUlMmNibF8zMTBfYnRfMTgwJTJjYmxfMzEwX2J0XzE3NSZjaGFuZ2VkPSZsYW5nPWVuJmthYmFtX2lkPTExNDM4MDEyMCZuYWlkPTExMjc4NTI1MiZhY2Nlc3NfdG9rZW49YzA3YzJmYjgtYmRiOS00YzI0LTk4MmUtMWFmYzI5MWQ3ZjEzJm1vYmlsZWlkPTc3NTU4NGE3NjRkZTMzOTIzNzFkZTI3ZmFkZDEyNzJiJnBsYXRmb3JtaWQ9MjAzJmJlY29tZV91c2VyX2lkPSZiZWNvbWVfcGFzc3dvcmQ9JmRlYnVnPTAmZ3Zlcj02LjAuMSZnYW1lU2xvdD0yNiZyYWNlPTEmZ2FtZU51bWJlcj0xMzc1MjE5OTQyJmdhbWVLZXk9NzIyNzBiYmRlNDkyYWUwMWRhOWZkZjc1OTA0ODlhOTE%3d&vcs=6.0.1

      XStream xStream = new XStream();
      String xml = xStream.toXML(httpRequest);
      FileWriter fetchRequest = new FileWriter("fetchRequest.xml");
      fetchRequest.write(xml);
      fetchRequest.close();

22:46:36.757 [New I/O worker #4] DEBUG com.twock.komlog.MapRequestFilter - OLD: data=YmxvY2tzPWJsXzMzMF9idF8xNzUlMmNibF8zMzBfYnRfMTcwJTJjYmxfMzMwX2J0XzE2NSZjaGFuZ2VkPSZsYW5nPWVuJmthYmFtX2lkPTExNDM4MDEyMCZuYWlkPTExMjc4NTI1MiZhY2Nlc3NfdG9rZW49YzA3YzJmYjgtYmRiOS00YzI0LTk4MmUtMWFmYzI5MWQ3ZjEzJm1vYmlsZWlkPTc3NTU4NGE3NjRkZTMzOTIzNzFkZTI3ZmFkZDEyNzJiJnBsYXRmb3JtaWQ9MjAzJmJlY29tZV91c2VyX2lkPSZiZWNvbWVfcGFzc3dvcmQ9JmRlYnVnPTAmZ3Zlcj02LjAuMSZnYW1lU2xvdD02NiZyYWNlPTEmZ2FtZU51bWJlcj0xMzc1MjE5OTgyJmdhbWVLZXk9NWI5YjI3MzBhMmIxNmFiNjFkZmJjNDE3NmZkYzk3YTc%3d&vcs=6.0.1
blocks=bl_330_bt_175%2cbl_330_bt_170%2cbl_330_bt_165&changed=&lang=en&kabam_id=114380120&naid=112785252&access_token=c07c2fb8-bdb9-4c24-982e-1afc291d7f13&mobileid=775584a764de3392371de27fadd1272b&platformid=203&become_user_id=&become_password=&debug=0&gver=6.0.1&gameSlot=66&race=1&gameNumber=1375219982&gameKey=5b9b2730a2b16ab61dfbc4176fdc97a7

22:46:36.758 [New I/O worker #4] DEBUG com.twock.komlog.MapRequestFilter - NEW: data=YmxvY2tzPWJsXzMzMF9idF8xNzAlMkNibF8zMzBfYnRfMTc1JTJDYmxfMzMwX2J0XzE4MCUyQ2JsXzMzMF9idF8xODUlMkNibF8zMzBfYnRfMTkwJTJDYmxfMzM1X2J0XzE3MCUyQ2JsXzMzNV9idF8xNzUlMkNibF8zMzVfYnRfMTgwJTJDYmxfMzM1X2J0XzE4NSUyQ2JsXzMzNV9idF8xOTAlMkNibF8zNDBfYnRfMTcwJTJDYmxfMzQwX2J0XzE3NSUyQ2JsXzM0MF9idF8xODAlMkNibF8zNDBfYnRfMTg1JTJDYmxfMzQwX2J0XzE5MCUyQ2JsXzM0NV9idF8xNzAlMkNibF8zNDVfYnRfMTc1JTJDYmxfMzQ1X2J0XzE4MCUyQ2JsXzM0NV9idF8xODUlMkNibF8zNDVfYnRfMTkwJTJDYmxfMzUwX2J0XzE3MCUyQ2JsXzM1MF9idF8xNzUlMkNibF8zNTBfYnRfMTgwJTJDYmxfMzUwX2J0XzE4NSUyQ2JsXzM1MF9idF8xOTAmbGFuZz1lbiZrYWJhbV9pZD0xMTQzODAxMjAmbmFpZD0xMTI3ODUyNTImYWNjZXNzX3Rva2VuPWMwN2MyZmI4LWJkYjktNGMyNC05ODJlLTFhZmMyOTFkN2YxMyZtb2JpbGVpZD03NzU1ODRhNzY0ZGUzMzkyMzcxZGUyN2ZhZGQxMjcyYiZwbGF0Zm9ybWlkPTIwMyZiZWNvbWVfdXNlcl9pZD0mYmVjb21lX3Bhc3N3b3JkPSZkZWJ1Zz0wJmd2ZXI9Ni4wLjEmZ2FtZVNsb3Q9NjYmcmFjZT0xJmdhbWVOdW1iZXI9MTM3NTIxOTk4MiZnYW1lS2V5PTViOWIyNzMwYTJiMTZhYjYxZGZiYzQxNzZmZGM5N2E3&vcs=6.0.1
blocks=bl_330_bt_170%2Cbl_330_bt_175%2Cbl_330_bt_180%2Cbl_330_bt_185%2Cbl_330_bt_190%2Cbl_335_bt_170%2Cbl_335_bt_175%2Cbl_335_bt_180%2Cbl_335_bt_185%2Cbl_335_bt_190%2Cbl_340_bt_170%2Cbl_340_bt_175%2Cbl_340_bt_180%2Cbl_340_bt_185%2Cbl_340_bt_190%2Cbl_345_bt_170%2Cbl_345_bt_175%2Cbl_345_bt_180%2Cbl_345_bt_185%2Cbl_345_bt_190%2Cbl_350_bt_170%2Cbl_350_bt_175%2Cbl_350_bt_180%2Cbl_350_bt_185%2Cbl_350_bt_190&lang=en&kabam_id=114380120&naid=112785252&access_token=c07c2fb8-bdb9-4c24-982e-1afc291d7f13&mobileid=775584a764de3392371de27fadd1272b&platformid=203&become_user_id=&become_password=&debug=0&gver=6.0.1&gameSlot=66&race=1&gameNumber=1375219982&gameKey=5b9b2730a2b16ab61dfbc4176fdc97a7


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
      log.debug("OLD: {}", httpRequest.getContent().toString(utf8));
      log.debug("NEW: {}", channelBuffer.toString(utf8));
      if(REWRITING) {
        httpRequest.setContent(channelBuffer);
      }
    } catch(Exception e) {
      log.debug("Failed to decode base64: {}", httpRequest.getContent().toString(), e);
    }
  }

  private String replace1(String data) throws UnsupportedEncodingException {
    String base64data = URLDecoder.decode(data, "UTF-8");
    String decoded = Base64.decode(ChannelBuffers.copiedBuffer(base64data.getBytes(utf8))).toString(utf8);
    String replacement = replace2(decoded);
    String encoded = Base64.encode(ChannelBuffers.copiedBuffer(replacement.getBytes(utf8)), false).toString(utf8);
    String result = encoded.replaceAll("=","%3d");
    log.debug("OLD url enc: {}", data);
    log.debug("OLD base64:  {}", base64data);
    log.debug("OLD decoded: {}", decoded);
    log.debug("NEW decoded: {}", replacement);
    log.debug("NEW base64:  {}", encoded);
    log.debug("NEW url enc: {}", result);
    return result;
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
    parameters.put("changed", Arrays.asList(""));

    QueryStringEncoder encoder = new QueryStringEncoder("", utf8);
    for(Map.Entry<String, List<String>> entry : parameters.entrySet()) {
      for(String parameterValue : entry.getValue()) {
        encoder.addParam(entry.getKey(), parameterValue);
      }
    }
    String rewrittenDecoded = encoder.toString().substring(1).replaceAll("%2C", "%2c");
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
