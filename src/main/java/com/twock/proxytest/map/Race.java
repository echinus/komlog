package com.twock.proxytest.map;

import com.fasterxml.jackson.annotation.JsonValue;

/**
* @author Chris Pearson
*/
public enum Race {
  ELF1(0),
  DWARF(1),
  ELF2(2);

  private final int id;

  private Race(int id) {
    this.id = id;
  }

  @JsonValue
  public int getId() {
    return id;
  }
}
