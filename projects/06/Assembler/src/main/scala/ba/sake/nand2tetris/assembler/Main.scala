package ba.sake.nand2tetris.assembler

import scala.io.Source
import java.io.File
import java.io.PrintWriter
import java.nio.charset.StandardCharsets

object Main {

  def main(args: Array[String]): Unit = {

    if (args.length < 1) {
      println("You must provide a file name!")
      return
    }

    val fileName = args(0)
    if (fileName == null || fileName.trim.isEmpty || fileName.length <= 4) {
      println("You must provide a file name!")
      return
    }

    val (name, ext) = fileName.splitAt(fileName.length - 4)
    println("NAME: "+name)
    println("ext: "+ext)
    if (ext != ".asm") {
      println("File must have '.asm' extension!")
      return
    }

    val inputFile = new File(fileName)
    val lines = Source.fromFile(inputFile).getLines.toVector
    val outputFile = new File(name + ".hack")
    val outputWriter = new PrintWriter(outputFile, StandardCharsets.UTF_8.name)

    val symbolTable = new SymbolTable()
    val parser = new Parser(symbolTable)
    val codeGenerator = new CodeGenerator(symbolTable, outputWriter)

    val instructions = parser.parse(lines)
    val code = codeGenerator.generate(instructions)

    outputWriter.close()
  }

}