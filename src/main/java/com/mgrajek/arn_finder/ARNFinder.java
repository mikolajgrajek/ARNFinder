package com.mgrajek.arn_finder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import lombok.extern.log4j.Log4j2;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import com.mgrajek.arn_finder.domain.ARNMatchedResult;
import com.mgrajek.arn_finder.finder.SimpleCombinationsFinder;
import com.mgrajek.arn_finder.input.TextFileReader;
import com.mgrajek.arn_finder.output.HtmlOutputPrinter;

@Log4j2
public class ARNFinder {
  public static void main(String... args) {
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
    final List<Nucleotide> nucleotides;
    try {
      nucleotides = TextFileReader.loadInputFile(inputFile);
    } catch (Exception e) {
      log.error("Cannot load inputFile: " + inputFile, e);
      return;
    }
    if (nucleotides.isEmpty()) {
      log.error("No data in inputFile: {}", inputFile);
      System.exit(1);
    }
    SimpleCombinationsFinder simpleCombinationsFinder = new SimpleCombinationsFinder(cmdLineArgs.maxMutations);
    for (Nucleotide nucleotide : nucleotides) {
      simpleCombinationsFinder.findPossibleARNCombinations(nucleotide);
    }

    ARNMatchedResult arnMatchedResult = simpleCombinationsFinder.getMatchResult();

    try {
      File outputFile = new File(inputFile.getParentFile(), cmdLineArgs.outputFileName);
      HtmlOutputPrinter.printToFile(arnMatchedResult, outputFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
