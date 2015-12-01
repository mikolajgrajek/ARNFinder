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

  @Option(name = "-minMatchLength", usage = "minimum length of matched sequence. Default 2")
  int minMatchLength = 2;

  @Option(name = "-mask", usage = "mask used to match nucleotydes. Default ARN. Possible AAN, AGG")
  String mask = "ARN";

  @Option(name = "-overlapFilter", usage = "Removes overlapped sequences. In not specified - overlapped ARNs will be found.")
  boolean overlapFilter = false;

  public NucleotydesMask getNucleotydesMask() {
    switch (mask) {
      case "ARN":
        return NucleotydesMask.ARN;
      case "AAN":
        return NucleotydesMask.AAN;
      case "AGG":
        return NucleotydesMask.AGG;
      default:
        throw new RuntimeException("cannot parse mask: " + mask);
    }
  }
}
