package com.mgrajek.arn_finder;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

class CmdLineArgs {
  @Option(name = "-maxMutations", usage = "maximum mutations allowed in ARN match. Default: 3")
  int maxMutations = 3;

  @Argument(usage = "path to file. Default: input.txt")
  String fileName = "input.txt";

  @Option(name = "-outputFile", usage = "html output file. Default: output.html")
  String outputFileName = "output.html";
}
