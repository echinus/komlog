package com.twock.proxytest.map;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

/**
 * @author Chris Pearson
 */
@Component
public class JsonMapReader {
  public ParsedJsonMapResponse parseJson(String json) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.readValue(json, ParsedJsonMapResponse.class);
    } catch(RuntimeException e) {
      throw e;
    } catch(Exception e) {
      throw new RuntimeException("Failed to parse map response", e);
    }
  }
}
