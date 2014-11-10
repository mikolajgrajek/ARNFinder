package com.mgrajek.arn_finder;

import lombok.Data;

@Data
public class Nucleotide {
  private String name;
  private String sequence;

  public static Nucleotide of(String name, String sequence) {
    Nucleotide result = new Nucleotide();
    result.name = name;
    result.sequence = sequence;
    return result;
  }

  public int length() {
    return sequence.length();
  }
}
