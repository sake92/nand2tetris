package ba.sake.nand2tetris.assembler

import java.io.PrintWriter

final class CodeGenerator(symbolTable: SymbolTable, outWriter: PrintWriter) {

  private def paddedAInstruction(decimalValue: Int): String = {
    val binaryValue = decimalValue.toBinaryString
    val padZeros = "0" * (15 - binaryValue.length)
    "0" + padZeros + binaryValue
  }

  def generate(instructions: Seq[I.Instruction]) = {
    instructions.foreach {
      case I.AInstruction(Left(const)) => {
        val i = paddedAInstruction(const) + "\n"
        outWriter.write(i)
      }
      case I.AInstruction(Right(symbolName)) => {
        symbolTable.get(symbolName) match {
          case Some(value) => {
            val i = paddedAInstruction(value) + "\n"
            outWriter.write(i)
          }
          case _ => {
            val varAddress = symbolTable.getNextVarAddress()
            symbolTable.add(symbolName, varAddress)
            val i = paddedAInstruction(varAddress) + "\n"
            outWriter.write(i)
            //throw new RuntimeException("Symbol not defined: " + symbolName)
          }
        }
      }
      case I.CInstruction(dest, comp, jump) => {
        val i = "111" + comp.machineCode + dest.machineCode + jump.machineCode + "\n"
        outWriter.write(i)
      }
      case _ => // no operation
    }
  }

}