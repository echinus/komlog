package com.twock.komlog.map;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.google.gson.annotations.SerializedName;

/**
 * @author Chris Pearson
 */
@Entity
public class UserInfo {
  private long id;
  private Date lastUpdated = new Date();
  @SerializedName("n")
  private String name;
  @SerializedName("t")
  private int level;
  @SerializedName("m")
  private long might;
  private String s;
  private int w;
  @SerializedName("a")
  private int allianceId;
  private int i;
  @SerializedName("r")
  private Race race;

  @Id
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Date getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public long getMight() {
    return might;
  }

  public void setMight(long might) {
    this.might = might;
  }

  public String getS() {
    return s;
  }

  public void setS(String s) {
    this.s = s;
  }

  public int getW() {
    return w;
  }

  public void setW(int w) {
    this.w = w;
  }

  public int getAllianceId() {
    return allianceId;
  }

  public void setAllianceId(int allianceId) {
    this.allianceId = allianceId;
  }

  public int getI() {
    return i;
  }

  public void setI(int i) {
    this.i = i;
  }

  public Race getRace() {
    return race;
  }

  public void setRace(Race race) {
    this.race = race;
  }

  @Override
  public String toString() {
    return "UserInfo{" +
      "id=" + id +
      ", lastUpdated=" + lastUpdated +
      ", name='" + name + '\'' +
      ", level=" + level +
      ", might=" + might +
      ", s='" + s + '\'' +
      ", w=" + w +
      ", allianceId=" + allianceId +
      ", i=" + i +
      ", race=" + race +
      '}';
  }
}
