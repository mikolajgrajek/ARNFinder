package com.mgrajek.arn_finder.finder;

import com.mgrajek.arn_finder.NucleotydesMask;
import com.mgrajek.arn_finder.Sequence;
import com.mgrajek.arn_finder.domain.ARNMatch;
import com.mgrajek.arn_finder.domain.ARNMatchedGroup;
import com.mgrajek.arn_finder.domain.ARNMatchedResult;
import com.mgrajek.arn_finder.filters.OverlayFilter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class SimpleCombinationsFinder {
  private final int maxMutations;
  private NucleotydesMask nucleotydesMask;
  private final Map<String, ARNMatchedGroup> groupByKey = new HashMap<>();
  private int minTripletsMatched;
  private boolean overlayedNotPossible;

  public SimpleCombinationsFinder(int maxMutations, NucleotydesMask nucleotydesMask) {
    this.maxMutations = maxMutations;
    this.nucleotydesMask = nucleotydesMask;
  }

  public List<ARNMatchedGroup> findPossibleARNCombinations(Sequence nucleotide) {
    final List<Integer> startPoints = findAllStarts(nucleotide.getSequence());
    int counter = 0;
    for (Integer startIndex : startPoints) {
      findAllMatches(nucleotide, startIndex);
      counter++;
      if (counter % 250_000 == 0) {
        System.out.println(counter + " processed.");
      }
    }
    return new ArrayList<>(groupByKey.values());
  }

  private List<Integer> findAllStarts(String sequence) {
    log.info("Searching for possible start points of ARN motifs...");
    Set<Integer> resultSet = new HashSet<>();
    findAll(sequence, resultSet, 'A');
    findAll(sequence, resultSet, 'G');
    final List<Integer> result = new ArrayList<>(resultSet);
    Collections.sort(result);
    log.info("Found {} possible start points for ARN motifs.", result.size());
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

  private void findAllMatches(Sequence nucleotide, Integer startIndex) {
    SingleSequenceMatchFinder finder = new SingleSequenceMatchFinder(nucleotide, startIndex, maxMutations, nucleotydesMask);
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
    log.info("Found " + result.size() + " matches. ");
    filterByMinLength(result);
    log.info("Filtered by minimal ANR motifs repetitions ({}): remained {} matches.", minTripletsMatched, result.size());

    filterOverlayed(result);
    log.info("Filtered out overlayed: remained {} matches.", result.size());
    return result;
  }

  private void filterOverlayed(ARNMatchedResult result) {
    if (!overlayedNotPossible) {
      return;
    }
    new OverlayFilter(result).filter();
  }

  private void filterByMinLength(ARNMatchedResult result) {
    for (Iterator<ARNMatchedGroup> it = result.getGroups().iterator(); it.hasNext(); ) {
      ARNMatchedGroup next = it.next();
      if (next.getMatchedLength() < minTripletsMatched) {
        it.remove();
      }
    }
  }

  public void setMinTripletsMatched(int minTripletsMatched) {
    this.minTripletsMatched = minTripletsMatched;
  }

  public void setOverlayedNotPossible(boolean overlayedNotPossible) {
    this.overlayedNotPossible = overlayedNotPossible;
  }
}
