package com.mgrajek.arn_finder.input;

import com.mgrajek.arn_finder.Genome;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class GbGenomeReader {
  private static final Pattern SPLIT_PATTERN = Pattern.compile("\\s+");

  public static Genome loadGenome(File file) throws IOException {
    boolean genomeStart = false;
    int lineIndex = 0;
    final StringBuilder sb = new StringBuilder();
    for (String line : FileUtils.readLines(file)) {
      lineIndex++;
      if (line.startsWith("ORIGIN")) {
        genomeStart = true;
        continue;
      }
      if (line.startsWith("//")) {
        break;
      }
      if (!genomeStart) {
        continue;
      }
      String[] split = SPLIT_PATTERN.split(line.trim());
      if (split[0] == null || split[0].equals("")) {
        throw new RuntimeException("Empty String! at line: " + lineIndex);
      }
      final int index = Integer.parseInt(split[0]);
      if (index != sb.length() + 1) {
        throw new RuntimeException("Genome indexing mismatch: file index: " + index + " readed data: " + sb.length() + " at line: " + lineIndex);
      }
      for (int i = 1; i < split.length; i++) {
        sb.append(split[i].toUpperCase());
      }
    }
    return Genome.of(sb.toString());
  }
}
