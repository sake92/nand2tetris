package ba.sake.nand2tetris.assembler

import java.io.File
import java.nio.charset.StandardCharsets
import java.io.Writer
import java.io.PrintWriter

class CodeGenerator(outWriter: Writer, symbolTable: SymbolTable) {

  def write(instruction: I.RealInstruction): Unit = {
    val res: String = instruction match {
      case I.AInstruction(Left(const)) =>
        paddedAInstruction(const)
      case I.AInstruction(Right(symbolName)) => {
        symbolTable.get(symbolName) match {
          case Some(value) =>
            paddedAInstruction(value)
          case _ => {
            val varAddress = symbolTable.getNextVarAddress()
            symbolTable.add(symbolName, varAddress)
            paddedAInstruction(varAddress)
          }
        }
      }
      case I.CInstruction(dest, comp, jump) => {
        "111" + comp.machineCode + dest.machineCode + jump.machineCode
      }
    }
    outWriter.write(res + "\n")
  }

  private def paddedAInstruction(decimalValue: Int): String = {
    val binaryValue = decimalValue.toBinaryString
    val padZeros = "0" * (15 - binaryValue.length)
    "0" + padZeros + binaryValue
  }

  def close(): Unit = {
    outWriter.close()
  }
}

object CodeGenerator {

  // print to file
  def apply(outputFile: File, symbolTable: SymbolTable): CodeGenerator = {
    val outWriter = new PrintWriter(outputFile, StandardCharsets.UTF_8.name)
    new CodeGenerator(outWriter, symbolTable)
  }

  // print to a Writer, e.g. a string for debugging
  def apply(outWriter: Writer, symbolTable: SymbolTable): CodeGenerator = {
    new CodeGenerator(outWriter, symbolTable)
  }

  // print to console
  def apply(symbolTable: SymbolTable): CodeGenerator = {
    val outWriter = new PrintWriter(System.out)
    new CodeGenerator(outWriter, symbolTable)
  }
}
