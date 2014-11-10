package com.mgrajek.arn_finder.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Data;

@Data
public class ARNMatchedResult {
  private List<ARNMatchedGroup> groups = new ArrayList<>();

  public void sort() {
    Collections.sort(groups, (o1, o2) -> o1.compareTo(o2));
  }

}
