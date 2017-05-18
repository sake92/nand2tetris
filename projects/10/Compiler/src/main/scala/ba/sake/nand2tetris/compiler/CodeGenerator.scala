package ba.sake.nand2tetris.compiler

import java.io.File
import java.io.PrintWriter
import java.io.Writer
import java.nio.charset.StandardCharsets

class CodeGenerator(outWriter: Writer) {

  def close(): Unit = {
    outWriter.close()
  }
}

object CodeGenerator {

  // print to file
  def apply(outputFile: File): CodeGenerator = {
    val outWriter = new PrintWriter(outputFile, StandardCharsets.UTF_8.name)
    new CodeGenerator(outWriter)
  }

  // print to a Writer, e.g. a string for debugging
  def apply(outWriter: Writer): CodeGenerator = {
    new CodeGenerator(outWriter)
  }

  // print to console
  def apply(): CodeGenerator = {
    val outWriter = new PrintWriter(System.out)
    new CodeGenerator(outWriter)
  }
}
