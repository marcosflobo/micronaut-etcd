package io.micronaut.etcd.util;

import java.io.Serializable;
import javax.inject.Singleton;

@Singleton
public class DummyObject implements Serializable {

  private String field1;
  private int field2;

  public DummyObject(String field1, int field2) {
    this.field1 = field1;
    this.field2 = field2;
  }

  public String getField1() {
    return field1;
  }

  public void setField1(String field1) {
    this.field1 = field1;
  }

  public int getField2() {
    return field2;
  }

  public void setField2(int field2) {
    this.field2 = field2;
  }
}
