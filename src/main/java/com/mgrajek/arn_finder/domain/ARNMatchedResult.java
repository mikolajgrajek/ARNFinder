package com.mgrajek.arn_finder.domain;

import lombok.Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

@Data
public class ARNMatchedResult {
  private List<ARNMatchedGroup> groups = new ArrayList<>();


  public void sort() {
    Collections.sort(groups, (o1, o2) -> o1.compareTo(o2));
  }

  public int size() {
    int totalSize = 0;
    for (ARNMatchedGroup group : groups) {
      totalSize += group.size();
    }
    return totalSize;
  }

  public static ARNMatchedResult from(List<ARNMatch> matchedUnique) {
    Map<String, ARNMatchedGroup> groupByKey = new HashMap<>();
    for (ARNMatch match : matchedUnique) {
      ARNMatchedGroup group = groupByKey.get(match.getGroupKey());
      if (group == null) {
        group = new ARNMatchedGroup();
        group.setMatchedLength(match.getMatchedSize());
        group.setMutationsCount(match.getUsedMutations());
        groupByKey.put(match.getGroupKey(), group);
      }
      group.add(match);
    }
    ARNMatchedResult result = new ARNMatchedResult();
    result.setGroups(new ArrayList<>(groupByKey.values()));
    result.sort();
    return result;
  }

  public void printStats(File outputFile) {
    System.out.println("Matches size: " + size());
    for (ARNMatchedGroup group : getGroups()) {
      System.out.println("L" + group.getMatchedLength() + "M" + group.getMutationsCount() + ": " + group.size());
    }
    if (outputFile != null) {
      try (PrintWriter pw = new PrintWriter(outputFile)) {
        for (ARNMatchedGroup group : getGroups()) {
          for (ARNMatch match : group.getMatches()) {
            pw.write("L" + match.getMatchedSize() + "M" + match.getUsedMutations() + ": " + match.getStartIndex() + ".." + match.getEndIndex());
          }
        }
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }
  }
}
