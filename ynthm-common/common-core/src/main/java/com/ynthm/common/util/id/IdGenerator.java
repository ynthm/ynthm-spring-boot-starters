package com.ynthm.common.util.id;


/**
 * @author Ethan Wang
 */
public class IdGenerator {

  private final Sequence sequence;

  public IdGenerator() {
    this.sequence = new Sequence(null);
  }

  public IdGenerator(long workerId, long dataCenterId) {
    this.sequence = new Sequence(workerId, dataCenterId);
  }

  public Long nextId() {
    return sequence.nextId();
  }
}
