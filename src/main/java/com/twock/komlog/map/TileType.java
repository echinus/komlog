package com.twock.komlog.map;

import com.google.gson.annotations.SerializedName;

/**
 * @author Chris Pearson
 */
public enum TileType {
  @SerializedName("0")
  BOG,
  @SerializedName("10")
  GRASSLAND,
  @SerializedName("11")
  LAKE,
  @SerializedName("20")
  WOODS,
  @SerializedName("30")
  HILLS,
  @SerializedName("40")
  MOUNTAINS,
  @SerializedName("50")
  PLAIN,
  @SerializedName("51")
  GOBLIN_CAMP
}
