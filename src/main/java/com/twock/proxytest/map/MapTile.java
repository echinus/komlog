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

  @Override
  public boolean equals(Object o) {
    if(this == o) return true;
    if(o == null || getClass() != o.getClass()) return false;

    MapTile mapTile = (MapTile)o;

    if(misted != mapTile.misted) return false;
    if(orgTileLevel != mapTile.orgTileLevel) return false;
    if(tileAllianceId != mapTile.tileAllianceId) return false;
    if(tileBlockId != mapTile.tileBlockId) return false;
    if(tileCityId != mapTile.tileCityId) return false;
    if(tileId != mapTile.tileId) return false;
    if(tileLevel != mapTile.tileLevel) return false;
    if(tileProvinceId != mapTile.tileProvinceId) return false;
    if(tileUserId != mapTile.tileUserId) return false;
    if(xCoord != mapTile.xCoord) return false;
    if(yCoord != mapTile.yCoord) return false;
    if(cityName != null ? !cityName.equals(mapTile.cityName) : mapTile.cityName != null) return false;
    if(race != mapTile.race) return false;
    if(tileAlliance != null ? !tileAlliance.equals(mapTile.tileAlliance) : mapTile.tileAlliance != null) return false;
    if(tileImgName != null ? !tileImgName.equals(mapTile.tileImgName) : mapTile.tileImgName != null) return false;
    if(tileType != mapTile.tileType) return false;
    if(tileUser != null ? !tileUser.equals(mapTile.tileUser) : mapTile.tileUser != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = (int)(tileId ^ (tileId >>> 32));
    result = 31 * result + xCoord;
    result = 31 * result + yCoord;
    result = 31 * result + (tileType != null ? tileType.hashCode() : 0);
    result = 31 * result + orgTileLevel;
    result = 31 * result + tileLevel;
    result = 31 * result + tileCityId;
    result = 31 * result + (cityName != null ? cityName.hashCode() : 0);
    result = 31 * result + (race != null ? race.hashCode() : 0);
    result = 31 * result + tileUserId;
    result = 31 * result + (tileUser != null ? tileUser.hashCode() : 0);
    result = 31 * result + tileAllianceId;
    result = 31 * result + (tileAlliance != null ? tileAlliance.hashCode() : 0);
    result = 31 * result + tileProvinceId;
    result = 31 * result + (int)(tileBlockId ^ (tileBlockId >>> 32));
    result = 31 * result + (tileImgName != null ? tileImgName.hashCode() : 0);
    result = 31 * result + (misted ? 1 : 0);
    return result;
  }
}
