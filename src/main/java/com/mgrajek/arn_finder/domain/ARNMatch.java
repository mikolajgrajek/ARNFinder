package com.mgrajek.arn_finder.domain;

import com.mgrajek.arn_finder.Nucleotide;
import com.mgrajek.arn_finder.finder.MatchedNucleotide;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ARNMatch implements Comparable {
  private Nucleotide nucleotide;
  private int startIndex;
  private int usedMutations;
  private List<MatchedNucleotide> matched = new ArrayList<>();

  private ARNMatch(ARNMatch arnMatch) {
    this.nucleotide = arnMatch.nucleotide;
    this.startIndex = arnMatch.startIndex;
    this.usedMutations = arnMatch.usedMutations;
    this.matched.addAll(arnMatch.matched);
  }

  public ARNMatch(Nucleotide nucleotide, int startIndex) {
    this.nucleotide = nucleotide;
    this.startIndex = startIndex;
    final String sequence = nucleotide.getSequence();

    switch (sequence.charAt(startIndex)) {
      case 'A':
        matched.add(MatchedNucleotide.A);
        break;
      case 'G':
      case 'U':
      case 'T':
      case 'C':
        matched.add(MatchedNucleotide.M);
        usedMutations++;
        break;
    }
    switch (sequence.charAt(startIndex + 1)) {
      case 'A':
      case 'G':
        matched.add(MatchedNucleotide.R);
        break;
      case 'U':
      case 'T':
      case 'C':
        matched.add(MatchedNucleotide.M);
        usedMutations++;
        break;
    }
    matched.add(MatchedNucleotide.N);
  }

  @SuppressWarnings("NullableProblems")
  @Override
  public int compareTo(Object o) {
    if (o == null) {
      return -1;
    }
    Nucleotide other = (Nucleotide) o;
    return nucleotide.getName().compareTo(other.getName());
  }

  /**
   * Used by Freemarker template.
   */
  @SuppressWarnings("UnusedDeclaration")
  public String getName() {
    return nucleotide.getName();
  }

  /**
   * Used by Freemarker template.
   */
  @SuppressWarnings("UnusedDeclaration")
  public String getHtmlSequence() {
    int endIndex = getEndIndex();
    if (endIndex == nucleotide.length()) {
      endIndex--;
    }
    StringBuilder sb = new StringBuilder();
    sb.append(nucleotide.getSequence().substring(0, startIndex));
    sb.append("<span class='matched'>");
    sb.append(nucleotide.getSequence().substring(startIndex, endIndex + 1));
    sb.append("</span>");
    if (endIndex < nucleotide.getSequence().length()) {
      sb.append(nucleotide.getSequence().substring(endIndex + 1));
    }
    return sb.toString();
  }

  public String getGroupKey() {
    return matched.size() + "_" + usedMutations;
  }

  public ARNMatch copy() {
    return new ARNMatch(this);
  }

  public boolean isValid(int maxMutations) {
    if (matched.size() == 3 && matched.get(0) == MatchedNucleotide.M) {
      return false;
    }
    //noinspection RedundantIfStatement
    if (usedMutations > maxMutations) {
      return false;
    }
    return true;
  }

  public ARNMatch merge(ARNMatch nextTriplet) {
    usedMutations += nextTriplet.usedMutations;
    matched.addAll(nextTriplet.matched);
    return this;
  }

  public String getMatchedSequence() {
    return nucleotide.getSequence().substring(startIndex, startIndex + matched.size());
  }

  public int getEndIndex() {
    return startIndex + matched.size() - 1;
  }

  public int getMatchedSize() {
    return matched.size();
  }

  @Override
  public String toString() {
    return "[" + nucleotide.getName() + "]:L" + (matched.size() / 3) + "M" + usedMutations + " " + getMatchedSequence() + " {" + getMatchPattern() + "}";
  }

  private String getMatchPattern() {
    final StringBuilder sb = new StringBuilder();
    for (MatchedNucleotide m : matched) {
      sb.append(m.name());
    }
    return sb.toString();
  }
}
