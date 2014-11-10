package com.mgrajek.arn_finder.finder;

import java.util.*;

import com.mgrajek.arn_finder.Nucleotide;
import com.mgrajek.arn_finder.domain.ARNMatch;
import com.mgrajek.arn_finder.domain.ARNMatchedGroup;
import com.mgrajek.arn_finder.domain.ARNMatchedResult;

public class SimpleCombinationsFinder {
  private final int maxMutations;
  private final Map<String, ARNMatchedGroup> groupByKey = new HashMap<>();

  public SimpleCombinationsFinder(int maxMutations) {
    this.maxMutations = maxMutations;
  }

  public List<ARNMatchedGroup> findPossibleARNCombinations(Nucleotide nucleotide) {
    final List<Integer> startPoints = findAllStarts(nucleotide.getSequence());
    for (Integer startIndex : startPoints) {
      findAllMatches(nucleotide, startIndex);
    }
    return new ArrayList<>(groupByKey.values());
  }

  private List<Integer> findAllStarts(String sequence) {
    Set<Integer> resultSet = new HashSet<>();
    findAll(sequence, resultSet, 'A');
    findAll(sequence, resultSet, 'G');
    final List<Integer> result = new ArrayList<>(resultSet);
    Collections.sort(result);
    return result;
  }

  private void findAll(String sequence, Set<Integer> resultSet, char element) {
    int startIndex = sequence.indexOf(element, 0);
    while (startIndex != -1) {
      if (startIndex > 0) {
        resultSet.add(startIndex - 1);
      }
      resultSet.add(startIndex);
      startIndex = sequence.indexOf(element, startIndex + 1);
    }
  }

  private void findAllMatches(Nucleotide nucleotide, Integer startIndex) {
    SingleSequenceMatchFinder finder = new SingleSequenceMatchFinder(nucleotide, startIndex, maxMutations);
    Collection<ARNMatch> singleMatch = finder.findSequences();
    if (singleMatch.isEmpty()) {
      return;
    }
    for (ARNMatch arnMatch : singleMatch) {
      final String groupKey = arnMatch.getGroupKey();
      ARNMatchedGroup group = groupByKey.get(groupKey);
      if (group == null) {
        group = new ARNMatchedGroup();
        group.setMatchedLength(arnMatch.getMatchedSize() / 3);
        group.setMutationsCount(arnMatch.getUsedMutations());
        groupByKey.put(groupKey, group);
      }
      group.add(arnMatch);
    }
  }

  public ARNMatchedResult getMatchResult() {
    ARNMatchedResult result = new ARNMatchedResult();
    result.setGroups(new ArrayList<>(groupByKey.values()));
    result.sort();
    return result;
  }
}
