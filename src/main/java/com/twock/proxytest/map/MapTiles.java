package com.twock.proxytest.map;

import java.util.Iterator;
import java.util.Map;

/**
 * @author Chris Pearson
 */
public class MapTiles {
  private Map<String, Long> allianceMights;
  private Map<String, String> allianceNames;
  private boolean ok;
  private Map<String, DataTile> data;
  private Map<String, UserInfo> userInfo;

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder().append("MapTiles{").append("allianceMights=").append(allianceMights).append(", allianceNames=").append(allianceNames).append(", ok=").append(ok);
    sb.append(", data=");
    appendMapContent(sb, data);
    sb.append(", userInfo=");
    appendMapContent(sb, userInfo);
    return sb.append('}').toString();
  }

  private void appendMapContent(StringBuilder sb, Map<String, ?> map) {
    if(map.size() > 1) {
      sb.append("\n\t");
    }
    for(Iterator<? extends Map.Entry<String, ?>> iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
      Map.Entry<String, ?> entry = iterator.next();
      sb.append(entry.getKey()).append(" = ").append(entry.getValue().toString());
      if(iterator.hasNext()) {
        sb.append("\n\t");
      }
    }
  }

  public Map<String, Long> getAllianceMights() {
    return allianceMights;
  }

  public void setAllianceMights(Map<String, Long> allianceMights) {
    this.allianceMights = allianceMights;
  }

  public Map<String, String> getAllianceNames() {
    return allianceNames;
  }

  public void setAllianceNames(Map<String, String> allianceNames) {
    this.allianceNames = allianceNames;
  }

  public boolean isOk() {
    return ok;
  }

  public void setOk(boolean ok) {
    this.ok = ok;
  }

  public Map<String, DataTile> getData() {
    return data;
  }

  public void setData(Map<String, DataTile> data) {
    this.data = data;
  }

  public Map<String, UserInfo> getUserInfo() {
    return userInfo;
  }

  public void setUserInfo(Map<String, UserInfo> userInfo) {
    this.userInfo = userInfo;
  }
}
