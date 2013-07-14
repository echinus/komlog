package com.twock.proxytest.map;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Chris Pearson
 */
@Entity
public class UserInfo {
  private Date lastUpdated = new Date();
  private String name;
  private int level;
  private long might;
  private String s;
  private int w;
  private int allianceId;
  private int i;
  private Race race;

  @Override
  public String toString() {
    return "UserInfo{" +
      "lastUpdated=" + lastUpdated +
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

  public Date getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  @JsonProperty("n")
  @Id
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @JsonProperty("t")
  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  @JsonProperty("m")
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

  @JsonProperty("a")
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

  @JsonProperty("r")
  public Race getRace() {
    return race;
  }

  public void setRace(Race race) {
    this.race = race;
  }
}
