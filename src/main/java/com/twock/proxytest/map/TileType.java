package com.twock.proxytest.map;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Chris Pearson
 */
public enum TileType {
  BOG(0),
  GRASSLAND(10),
  LAKE(11),
  WOODS(20),
  HILLS(30),
  MOUNTAINS(40),
  PLAIN(50),
  GOBLIN_CAMP(51);

  private final int id;

  private TileType(int id) {
    this.id = id;
  }

  @JsonValue
  public int getId() {
    return id;
  }
}
