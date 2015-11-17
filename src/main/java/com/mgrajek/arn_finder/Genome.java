package com.mgrajek.arn_finder;

import lombok.Data;

@Data
public class Genome implements Sequence {
  private String sequence;

  public static Genome of(String sequence) {
    Genome result = new Genome();
    result.sequence = sequence;
    return result;
  }

  public int length() {
    return sequence.length();
  }

  @Override
  public String getName() {
    return "";
  }
}
