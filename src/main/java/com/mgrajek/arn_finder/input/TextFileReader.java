package com.mgrajek.arn_finder.input;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.log4j.Log4j2;

import org.apache.commons.io.FileUtils;

import com.mgrajek.arn_finder.Nucleotide;

@Log4j2
public class TextFileReader {
  public static List<Nucleotide> loadInputFile(File fileName) throws IOException {
    final List<Nucleotide> result = new ArrayList<>();
    final List<String> strings = FileUtils.readLines(fileName, "UTF-8");
    int lineNumber = 0;
    for (String line : strings) {
      lineNumber++;
      if (line == null || line.trim().equals("")) {
        continue;
      }
      final int index = line.indexOf(':');
      if (index == -1) {
        log.error("Malformed line nr {} - no ':' separator: {}", lineNumber, line);
        System.exit(1);
      }
      final Nucleotide of = Nucleotide.of(line.substring(0, index).trim(), line.substring(index + 1).trim().toUpperCase());
      result.add(of);
    }

    return result;
  }

}
