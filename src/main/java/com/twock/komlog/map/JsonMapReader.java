package com.twock.komlog.map;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;

/**
 * @author Chris Pearson
 */
@Component
public class JsonMapReader {
  public ParsedJsonMapResponse parseJson(String json) {
    try {
      ParsedJsonMapResponse response = new Gson().fromJson(json, ParsedJsonMapResponse.class);
      // construct alliances
      Map<Integer, Alliance> cachedAlliances = new HashMap<Integer, Alliance>();
      for(MapTile tile : response.getData().values()) {
        Alliance alliance = cachedAlliances.get(tile.getTileAllianceId());
        if(alliance == null && tile.getTileAllianceId() != 0L) {
          String allianceId = "a" + tile.getTileAllianceId();
          long might = response.getAllianceMights().get(allianceId);
          String name = response.getAllianceNames().get(allianceId);
          alliance = new Alliance();
          alliance.setAllianceId(tile.getTileAllianceId());
          alliance.setMight(might);
          alliance.setName(name);
          cachedAlliances.put(tile.getTileAllianceId(), alliance);
        }
        tile.setTileAlliance(alliance);
        // construct users
        String userId = "u" + tile.getTileUserId();
        UserInfo info = response.getUserInfo().get(userId);
        if(info != null) {
          info.setId(tile.getTileUserId());
          tile.setTileUser(info);
        }
      }
      return response;
    } catch(RuntimeException e) {
      throw e;
    } catch(Exception e) {
      throw new RuntimeException("Failed to parse map response", e);
    }
  }
}
