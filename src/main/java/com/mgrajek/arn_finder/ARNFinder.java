package com.mgrajek.arn_finder;

import com.mgrajek.arn_finder.domain.ARNMatchedResult;
import com.mgrajek.arn_finder.filters.OverlayOnlyFilter;
import com.mgrajek.arn_finder.finder.SimpleCombinationsFinder;
import com.mgrajek.arn_finder.input.GbGenomeReader;
import com.mgrajek.arn_finder.input.TextFileReader;
import com.mgrajek.arn_finder.output.HtmlOutputPrinter;
import lombok.extern.log4j.Log4j2;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Log4j2
public class ARNFinder {
  public static void main(String... args) throws IOException {
    CmdLineArgs cmdLineArgs = new CmdLineArgs();
    final CmdLineParser parser = new CmdLineParser(cmdLineArgs);
    try {
      parser.parseArgument(args);
    } catch (CmdLineException e) {
      log.error("Wrong command line params.");
      parser.printUsage(System.out);
    }

    final File inputFile = new File(cmdLineArgs.fileName);
    if (!inputFile.exists()) {
      log.error("inputFile doesn't exists: {}", inputFile);
      return;
    }
    SimpleCombinationsFinder simpleCombinationsFinder = new SimpleCombinationsFinder(cmdLineArgs.maxMutations);
    boolean genoFileLoaded = false;
    if (inputFile.getName().toLowerCase().endsWith("txt")) {
      final List<Nucleotide> nucleotides = loadNucleotides(inputFile);
      if (nucleotides == null) return;
      for (Nucleotide nucleotide : nucleotides) {
        simpleCombinationsFinder.findPossibleARNCombinations(nucleotide);
      }
    } else if (inputFile.getName().toLowerCase().endsWith("gb")) {
      Genome genome = GbGenomeReader.loadGenome(inputFile);
      simpleCombinationsFinder.findPossibleARNCombinations(genome);
      genoFileLoaded = true;
    }

    log.info("Min ARN match length set to: " + cmdLineArgs.minMatchLength);
    simpleCombinationsFinder.setMinTripletsMatched(cmdLineArgs.minMatchLength);
    log.info("ARN OverlayFilter: " + cmdLineArgs.overlayFilter);
    simpleCombinationsFinder.setOverlayedNotPossible(cmdLineArgs.overlayFilter);

    ARNMatchedResult arnMatchedResult = simpleCombinationsFinder.getMatchResult();

    if (genoFileLoaded) {
      log.info("Overlayed stats");
      arnMatchedResult.printStats(new File(inputFile.getParentFile(), "output_stats_overlayed.txt"));

      log.info("Non-Overlayed stats");
      new OverlayOnlyFilter(arnMatchedResult).filter();
      arnMatchedResult.printStats(new File(inputFile.getParentFile(), "output_stats_non_overlayed.txt"));

    } else {
      try {
        File outputFile = new File(inputFile.getParentFile(), cmdLineArgs.outputFileName);
        HtmlOutputPrinter.printToFile(arnMatchedResult, outputFile);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }


  private static List<Nucleotide> loadNucleotides(File inputFile) {
    final List<Nucleotide> nucleotides;
    try {
      nucleotides = TextFileReader.loadInputFile(inputFile);
    } catch (Exception e) {
      log.error("Cannot load inputFile: " + inputFile, e);
      return null;
    }
    if (nucleotides.isEmpty()) {
      log.error("No data in inputFile: {}", inputFile);
      System.exit(1);
    }
    return nucleotides;
  }

}
