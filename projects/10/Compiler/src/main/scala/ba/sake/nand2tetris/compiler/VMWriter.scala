package ba.sake.nand2tetris.compiler

import java.io.File
import java.io.PrintWriter
import java.io.Writer
import java.nio.charset.StandardCharsets

class VMWriter(outWriter: Writer) {
  import VMWriter._

  private def write(str: String) = {
    outWriter.write(str)
    outWriter.flush()
  }
  private def writeLn(str: String) = write(str + "\n")

  def writePush(segment: SegmentTypes.Type, index: Int): Unit =
    writeLn(s"push ${segment.toString.toLowerCase} ${index}")

  def writePop(segment: SegmentTypes.Type, index: Int): Unit =
    writeLn(s"pop ${segment.toString.toLowerCase} ${index}")

  def writeArithmetic(instruction: ArithmeticInstructions.Instruction): Unit =
    writeLn(instruction.toString.toLowerCase)

  def writeLabel(label: String): Unit =
    writeLn(s"label ${label}")

  def writeGoto(label: String): Unit =
    writeLn(s"goto ${label}")

  def writeIf(label: String): Unit =
    writeLn(s"if-goto ${label}")

  def writeCall(name: String, numArgs: Int): Unit =
    writeLn(s"call ${name} ${numArgs}")

  def writeFunction(name: String, numLocals: Int): Unit =
    writeLn(s"function ${name} ${numLocals}")

  def writeReturn(): Unit =
    writeLn("return")
}

object VMWriter {

  object SegmentTypes extends Enumeration {
    type Type = Value
    val CONST = Value("constant")
    val ARG = Value("argument")
    val LOCAL, STATIC, THIS, THAT, POINTER, TEMP = Value
  }

  object ArithmeticInstructions extends Enumeration {
    type Instruction = Value
    val ADD, SUB, NEG, EQ, GT, LT, AND, OR, NOT = Value
  }

  // print to file
  def apply(outputFile: File): VMWriter = {
    val outWriter = new PrintWriter(outputFile, StandardCharsets.UTF_8.name)
    new VMWriter(outWriter)
  }

  // print to a Writer, e.g. a string for debugging
  def apply(outWriter: Writer): VMWriter = {
    new VMWriter(outWriter)
  }

  // print to console
  def apply(): VMWriter = {
    val outWriter = new PrintWriter(System.out)
    new VMWriter(outWriter)
  }
}
