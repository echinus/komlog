package com.twock.proxytest.map;

import java.util.Collection;

/**
 * @author Chris Pearson
 */
public class ParsedMapEntities {
  private Collection<Alliance> alliances;
  private Collection<UserInfo> users;
  private Collection<MapTile> mapTiles;

  public Collection<Alliance> getAlliances() {
    return alliances;
  }

  public void setAlliances(Collection<Alliance> alliances) {
    this.alliances = alliances;
  }

  public Collection<UserInfo> getUsers() {
    return users;
  }

  public void setUsers(Collection<UserInfo> users) {
    this.users = users;
  }

  public Collection<MapTile> getMapTiles() {
    return mapTiles;
  }

  public void setMapTiles(Collection<MapTile> mapTiles) {
    this.mapTiles = mapTiles;
  }
}
