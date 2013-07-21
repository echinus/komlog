package com.twock.proxytest.map;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;

/**
 * @author Chris Pearson
 */
@Component
public class JsonMapReader {
  public ParsedJsonMapResponse parseJson(String json) {
    try {
      return new Gson().fromJson(json, ParsedJsonMapResponse.class);
    } catch(RuntimeException e) {
      throw e;
    } catch(Exception e) {
      throw new RuntimeException("Failed to parse map response", e);
    }
  }
}
