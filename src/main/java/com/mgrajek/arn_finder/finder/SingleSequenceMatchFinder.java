package com.mgrajek.arn_finder.finder;

import com.mgrajek.arn_finder.NucleotydesMask;
import com.mgrajek.arn_finder.Sequence;
import com.mgrajek.arn_finder.domain.ARNMatch;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class SingleSequenceMatchFinder {

  private final Sequence nucleotide;
  private final int matchIndex;
  private final int maxMutations;
  private NucleotydesMask nucleotydesMask;
  private final Map<String, ARNMatch> matchByIndexesKey = new HashMap<>();

  public SingleSequenceMatchFinder(Sequence nucleotide, int matchIndex, int maxMutations, NucleotydesMask nucleotydesMask) {
    this.nucleotide = nucleotide;
    this.matchIndex = matchIndex;
    this.maxMutations = maxMutations;
    this.nucleotydesMask = nucleotydesMask;
  }

  Collection<ARNMatch> findSequences() {
    if (matchIndex + 2 >= nucleotide.length()) {
      return Collections.emptyList();
    }
    final ARNMatch match = new ARNMatch(nucleotide, matchIndex, nucleotydesMask);
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
    String key = matchCandidate.getStartIndex() + ".." + matchCandidate.getEndIndex();
    final ARNMatch oldMatch = matchByIndexesKey.get(key);
    if (oldMatch == null || oldMatch.getUsedMutations() > matchCandidate.getUsedMutations()) {
      matchByIndexesKey.put(key, matchCandidate);
    }
  }

  private ARNMatch matchRight(Sequence nucleotide, ARNMatch matchCandidate) {
    if (matchCandidate.getEndIndex() + 3 >= nucleotide.length()) {
      return null;
    }
    ARNMatch nextTriplet = new ARNMatch(nucleotide, matchCandidate.getEndIndex() + 1, nucleotydesMask);
    return matchCandidate.copy().merge(nextTriplet);
  }

}
