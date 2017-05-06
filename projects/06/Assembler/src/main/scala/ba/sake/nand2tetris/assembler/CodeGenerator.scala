package ba.sake.nand2tetris.assembler

import java.io.PrintWriter
import java.io.File
import java.nio.charset.StandardCharsets

class CodeGenerator(outWriter: PrintWriter, symbolTable: SymbolTable) {

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

}