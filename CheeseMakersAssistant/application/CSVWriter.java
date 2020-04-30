package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Helper to write CSV files
 */
public class CSVWriter {
  private PrintWriter writer;

  /**
   * Create CSVWriter
   *
   * @param file file to write to
   * @throws FileNotFoundException if the file is not found
   */
  public CSVWriter(File file) throws FileNotFoundException {
    writer = new PrintWriter(file);
  }

  /**
   * Write a row
   *
   * @param row the row to write
   */
  public void writeRow(String[] row) {
    writer.println(Stream.of(row).collect(Collectors.joining(",")));
  }

  /**
   * Close the writer
   */
  public void close() {
    writer.close();
  }
}
