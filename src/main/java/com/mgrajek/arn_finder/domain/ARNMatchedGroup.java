package com.mgrajek.arn_finder.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ARNMatchedGroup implements Comparable {
  private int matchedLength;
  private int mutationsCount;
  private List<ARNMatch> matches = new ArrayList<>();

  @SuppressWarnings("NullableProblems")
  @Override
  public int compareTo(Object o) {
    if (o == null) {
      return -1;
    }
    ARNMatchedGroup other = (ARNMatchedGroup) o;
    if (matchedLength > other.matchedLength) {
      return -1;
    } else if (matchedLength < other.matchedLength) {
      return 1;
    }

    int compare = Integer.compare(matchedLength, other.matchedLength);
    if (compare == 0) {
      compare = Integer.compare(mutationsCount, other.mutationsCount);
    }
    return compare;
  }

  public void add(ARNMatch singleMatch) {
    matches.add(singleMatch);
  }
}
