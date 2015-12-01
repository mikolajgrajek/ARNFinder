package com.mgrajek.arn_finder.finder;

import com.mgrajek.arn_finder.Nucleotide;
import com.mgrajek.arn_finder.NucleotydesMask;
import com.mgrajek.arn_finder.domain.ARNMatchedGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class SimpleCombinationsFinderTest {

  @Test
  public void testFindPossibleARNCombinations() throws Exception {
    SimpleCombinationsFinder finder = new SimpleCombinationsFinder(0, NucleotydesMask.ARN);
    final List<ARNMatchedGroup> result = finder.findPossibleARNCombinations(Nucleotide.of("TEST", "AGC"));
    Assert.assertEquals(1, result.size());
    Assert.assertEquals("AGC", result.get(0).getMatches().get(0).getMatchedSequence());
  }

  @Test
  public void testFindPossibleARNCombinationsWithU() throws Exception {
    SimpleCombinationsFinder finder = new SimpleCombinationsFinder(0, NucleotydesMask.ARN);
    final List<ARNMatchedGroup> result = finder.findPossibleARNCombinations(Nucleotide.of("TEST", "AGU"));
    Assert.assertEquals(1, result.size());
    Assert.assertEquals("AGU", result.get(0).getMatches().get(0).getMatchedSequence());
  }

  @Test
  public void testFindPossibleARNCombinationsWithT() throws Exception {
    SimpleCombinationsFinder finder = new SimpleCombinationsFinder(0, NucleotydesMask.ARN);
    final List<ARNMatchedGroup> result = finder.findPossibleARNCombinations(Nucleotide.of("TEST", "AGT"));
    Assert.assertEquals(1, result.size());
    Assert.assertEquals("AGT", result.get(0).getMatches().get(0).getMatchedSequence());
  }

  @Test
  public void doubleFind() throws Exception {
    SimpleCombinationsFinder finder = new SimpleCombinationsFinder(0, NucleotydesMask.ARN);
    final List<ARNMatchedGroup> result = finder.findPossibleARNCombinations(Nucleotide.of("TEST", "AGCAGC"));
    Assert.assertEquals(2, result.size());
    Assert.assertEquals("AGC", result.get(0).getMatches().get(0).getMatchedSequence());
    Assert.assertEquals("AGC", result.get(0).getMatches().get(1).getMatchedSequence());
    Assert.assertEquals("AGCAGC", result.get(1).getMatches().get(0).getMatchedSequence());
  }

  @Test
  public void doubleFindWithMutation() throws Exception {
    SimpleCombinationsFinder finder = new SimpleCombinationsFinder(1, NucleotydesMask.ARN);
    final List<ARNMatchedGroup> result = finder.findPossibleARNCombinations(Nucleotide.of("TEST", "AGCCGC"));
    Assert.assertEquals(2, result.size());
    Assert.assertEquals("AGC", result.get(0).getMatches().get(0).getMatchedSequence());
    Assert.assertEquals("AGCCGC", result.get(1).getMatches().get(0).getMatchedSequence());
  }

  @Test
  public void doubleFindWithT() throws Exception {
    SimpleCombinationsFinder finder = new SimpleCombinationsFinder(0, NucleotydesMask.ARN);
    final List<ARNMatchedGroup> result = finder.findPossibleARNCombinations(Nucleotide.of("TEST", "AGCAGC"));
    Assert.assertEquals(2, result.size());
    Assert.assertEquals("AGC", result.get(0).getMatches().get(0).getMatchedSequence());
    Assert.assertEquals("AGC", result.get(0).getMatches().get(1).getMatchedSequence());
    Assert.assertEquals("AGCAGC", result.get(1).getMatches().get(0).getMatchedSequence());
  }

}