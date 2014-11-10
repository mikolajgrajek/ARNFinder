package com.mgrajek.arn_finder.finder;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.mgrajek.arn_finder.Nucleotide;
import com.mgrajek.arn_finder.domain.ARNMatch;

class SingleSequenceMatchFinder {

  private final Nucleotide nucleotide;
  private final int matchIndex;
  private final int maxMutations;
  private final Map<String, ARNMatch> matchByIndexesKey = new HashMap<>();

  public SingleSequenceMatchFinder(Nucleotide nucleotide, int matchIndex, int maxMutations) {
    this.nucleotide = nucleotide;
    this.matchIndex = matchIndex;
    this.maxMutations = maxMutations;
  }

  Collection<ARNMatch> findSequences() {
    if (matchIndex + 1 >= nucleotide.length()) {
      return Collections.emptyList();
    }
    final ARNMatch match = new ARNMatch(nucleotide, matchIndex);
    findPossible(match);
    return matchByIndexesKey.values();
  }

  private void findPossible(ARNMatch matchCandidate) {
    if (matchCandidate.isValid(maxMutations)) {
      addMatchToResults(matchCandidate);
    } else if (matchCandidate.getMatchedSize() > 3) {
      return;
    }
    final ARNMatch match = matchRight(nucleotide, matchCandidate);
    if (match != null) {
      findPossible(match);
    }
  }

  private void addMatchToResults(ARNMatch matchCandidate) {
    String key = matchCandidate.getStartIndex() + "_" + matchCandidate.getEndIndex();
    final ARNMatch oldMatch = matchByIndexesKey.get(key);
    if (oldMatch == null || oldMatch.getUsedMutations() > matchCandidate.getUsedMutations()) {
      matchByIndexesKey.put(key, matchCandidate);
    }
  }

  private ARNMatch matchRight(Nucleotide nucleotide, ARNMatch matchCandidate) {
    if (matchCandidate.getEndIndex() + 3 >= nucleotide.length()) {
      return null;
    }
    ARNMatch nextTriplet = new ARNMatch(nucleotide, matchCandidate.getEndIndex() + 1);
    return matchCandidate.copy().merge(nextTriplet);
  }

}
