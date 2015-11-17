package com.mgrajek.arn_finder.filters;

import com.mgrajek.arn_finder.domain.ARNMatch;

import java.util.ArrayList;
import java.util.List;

class MatchedARNEntries {
  List<ARNMatch> indexedMatches = new ArrayList<>();

  public void addMatch(ARNMatch match) {
    indexedMatches.add(match);
  }

  public boolean hasBetterMatches(ARNMatch match) {
    for (ARNMatch prev : indexedMatches) {
      if (prev.getMatchedSize() > match.getMatchedSize() && prev.getUsedMutations() <= match.getUsedMutations()) {
        return true;
      }
    }
    return false;
  }
}
