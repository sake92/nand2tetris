package ba.sake.nand2tetris.assembler

import org.scalatest.FlatSpec
import java.io.PrintWriter
import java.io.StringWriter

class AssemblerTest extends FlatSpec {

  for ((testName, input, expected) <- TestData.tests) {
    "Assembler" should s"parse $testName" in {
      checkOutput(testName, input, expected)
    }
  }

  def checkOutput(testName: String, input: String, expected: String): Unit = {
    val symbolTable = new SymbolTable()
    val parser = Parser(input, symbolTable)

    val out = new StringWriter() // write to string
    val codeGenerator = CodeGenerator(out, symbolTable)
    var instruction: Option[I.RealInstruction] = null
    while ({ instruction = parser.next(); instruction.isDefined }) {
      codeGenerator.write(instruction.get)
    }

    val result = out.getBuffer.toString
    assert(result == expected, testName)

    parser.close()
    codeGenerator.close()
  }

}