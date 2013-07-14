package com.twock.proxytest.map;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Chris Pearson
 */
@Entity
public class Alliance {
  private long allianceId;
  private String name;
  private long might;

  @Id
  public long getAllianceId() {
    return allianceId;
  }

  public void setAllianceId(long allianceId) {
    this.allianceId = allianceId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getMight() {
    return might;
  }

  public void setMight(long might) {
    this.might = might;
  }

  @Override
  public String toString() {
    return "Alliance{" +
      "allianceId=" + allianceId +
      ", name='" + name + '\'' +
      ", might=" + might +
      '}';
  }
}
