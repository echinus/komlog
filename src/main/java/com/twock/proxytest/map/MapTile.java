package com.twock.proxytest.map;

import javax.persistence.*;

/**
 * @author Chris Pearson
 */
@Entity
public class MapTile {
  private long tileId;
  private int xCoord;
  private int yCoord;
  private TileType tileType;
  private int orgTileLevel;
  private int tileLevel;
  private int tileCityId;
  private String cityName;
  private Race race;
  private int tileUserId;
  private UserInfo tileUser;
  private int tileAllianceId;
  private Alliance tileAlliance;
  private int tileProvinceId;
  private long tileBlockId;
  private String tileImgName;
  private boolean misted;

  @Id
  public long getTileId() {
    return tileId;
  }

  public void setTileId(long tileId) {
    this.tileId = tileId;
  }

  public int getxCoord() {
    return xCoord;
  }

  public void setxCoord(int xCoord) {
    this.xCoord = xCoord;
  }

  public int getyCoord() {
    return yCoord;
  }

  public void setyCoord(int yCoord) {
    this.yCoord = yCoord;
  }

  public TileType getTileType() {
    return tileType;
  }

  public void setTileType(TileType tileType) {
    this.tileType = tileType;
  }

  public int getOrgTileLevel() {
    return orgTileLevel;
  }

  public void setOrgTileLevel(int orgTileLevel) {
    this.orgTileLevel = orgTileLevel;
  }

  public int getTileLevel() {
    return tileLevel;
  }

  public void setTileLevel(int tileLevel) {
    this.tileLevel = tileLevel;
  }

  public int getTileCityId() {
    return tileCityId;
  }

  public void setTileCityId(int tileCityId) {
    this.tileCityId = tileCityId;
  }

  public String getCityName() {
    return cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  public Race getRace() {
    return race;
  }

  public void setRace(Race race) {
    this.race = race;
  }

  @Transient
  public int getTileUserId() {
    return tileUserId;
  }

  public void setTileUserId(int tileUserId) {
    this.tileUserId = tileUserId;
  }

  @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
  public UserInfo getTileUser() {
    return tileUser;
  }

  public void setTileUser(UserInfo tileUser) {
    this.tileUser = tileUser;
  }

  @Transient
  public int getTileAllianceId() {
    return tileAllianceId;
  }

  public void setTileAllianceId(int tileAllianceId) {
    this.tileAllianceId = tileAllianceId;
  }

  @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
  public Alliance getTileAlliance() {
    return tileAlliance;
  }

  public void setTileAlliance(Alliance tileAlliance) {
    this.tileAlliance = tileAlliance;
  }

  public int getTileProvinceId() {
    return tileProvinceId;
  }

  public void setTileProvinceId(int tileProvinceId) {
    this.tileProvinceId = tileProvinceId;
  }

  public long getTileBlockId() {
    return tileBlockId;
  }

  public void setTileBlockId(long tileBlockId) {
    this.tileBlockId = tileBlockId;
  }

  public String getTileImgName() {
    return tileImgName;
  }

  public void setTileImgName(String tileImgName) {
    this.tileImgName = tileImgName;
  }

  public boolean isMisted() {
    return misted;
  }

  public void setMisted(boolean misted) {
    this.misted = misted;
  }

  @Override
  public String toString() {
    return "MapTile{" +
      "tileId=" + tileId +
      ", xCoord=" + xCoord +
      ", yCoord=" + yCoord +
      ", tileType=" + tileType +
      ", orgTileLevel=" + orgTileLevel +
      ", tileLevel=" + tileLevel +
      ", tileCityId=" + tileCityId +
      ", cityName='" + cityName + '\'' +
      ", race=" + race +
      ", tileUserId=" + tileUserId +
      ", tileAllianceId=" + tileAllianceId +
      ", tileAlliance=" + tileAlliance +
      ", tileProvinceId=" + tileProvinceId +
      ", tileBlockId=" + tileBlockId +
      ", tileImgName='" + tileImgName + '\'' +
      ", misted=" + misted +
      '}';
  }
}
