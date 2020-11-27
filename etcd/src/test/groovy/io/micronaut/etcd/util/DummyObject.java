package io.micronaut.etcd.util;

import java.io.Serializable;
import javax.inject.Singleton;

/**
 * Class to be used for testing.
 */
@Singleton
public class DummyObject implements Serializable {

  private String field1;
  private int field2;

  /**
   * Constructor.
   * @param field1
   * @param field2
   */
  public DummyObject(String field1, int field2) {
    this.field1 = field1;
    this.field2 = field2;
  }

  /**
   *
   * @return the field1
   */
  public String getField1() {
    return field1;
  }

  /**
   * Sets the field1.
   * @param field1
   */
  public void setField1(String field1) {
    this.field1 = field1;
  }

  /**
   *
   * @return returns field2
   */
  public int getField2() {
    return field2;
  }

  /**
   * Sets field 2.
   * @param field2
   */
  public void setField2(int field2) {
    this.field2 = field2;
  }
}
