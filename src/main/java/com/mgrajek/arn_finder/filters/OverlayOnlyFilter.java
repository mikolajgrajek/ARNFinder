package com.mgrajek.arn_finder.filters;

import com.mgrajek.arn_finder.domain.ARNMatch;
import com.mgrajek.arn_finder.domain.ARNMatchedGroup;
import com.mgrajek.arn_finder.domain.ARNMatchedResult;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class OverlayOnlyFilter {
  private Map<Integer, MatchedARNEntries> index = new HashMap<>();
  private ARNMatchedResult result;

  public OverlayOnlyFilter(ARNMatchedResult result) {
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
          it.remove();
          continue nextMatch;
        }
      }
      if (group.size() == 0) {
        itGroup.remove();
      }
    }
  }

}

