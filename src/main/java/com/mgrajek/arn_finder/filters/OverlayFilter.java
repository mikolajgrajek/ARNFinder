package com.mgrajek.arn_finder.filters;

import com.mgrajek.arn_finder.domain.ARNMatch;
import com.mgrajek.arn_finder.domain.ARNMatchedGroup;
import com.mgrajek.arn_finder.domain.ARNMatchedResult;

import java.util.*;

public class OverlayFilter {
  private Map<Integer, MatchedARNEntries> index = new HashMap<>();
  private ARNMatchedResult result;

  public OverlayFilter(ARNMatchedResult result) {
    this.result = result;
  }

  public void filter() {
    for (Iterator<ARNMatchedGroup> itGroup = result.getGroups().iterator(); itGroup.hasNext(); ) {
      ARNMatchedGroup group = itGroup.next();
      nextMatch:
      for (Iterator<ARNMatch> it = group.getMatches().iterator(); it.hasNext(); ) {
        ARNMatch match = it.next();
        for (int i = match.getStartIndex(); i < match.getEndIndex(); i++) {
          MatchedARNEntries betterMatches = index.get(i);
          if (betterMatches == null) {
            betterMatches = new MatchedARNEntries();
            index.put(i, betterMatches);
            betterMatches.addMatch(match);
            continue;
          }
          if (betterMatches.hasBetterMatches(match)) {
            it.remove();
            continue nextMatch;
          }
          betterMatches.addMatch(match);
        }
      }
      if (group.size() == 0) {
        itGroup.remove();
      }
    }
  }

}

