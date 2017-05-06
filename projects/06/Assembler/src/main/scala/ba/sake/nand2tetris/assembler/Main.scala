package ba.sake.nand2tetris.assembler

import scala.io.Source
import java.io.File
import java.io.PrintWriter
import java.nio.charset.StandardCharsets
import java.io.BufferedReader
import java.io.FileReader
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.io.RandomAccessFile

object Main {

  def main(args: Array[String]): Unit = {

    val res = handleArgs(args)
    if (res.isEmpty) return
    val Some((fileName, name)) = res

    val inputFile = new File(fileName)
    val outputFile = new File(name + ".hack")
    val outWriter = new PrintWriter(outputFile, StandardCharsets.UTF_8.name)

    val symbolTable = new SymbolTable()
    val parser = new Parser(inputFile, symbolTable)
    val codeGenerator = new CodeGenerator(outWriter, symbolTable)

    var instruction: Option[I.RealInstruction] = null
    while ({ instruction = parser.next(); instruction.isDefined }) {
      codeGenerator.write(instruction.get)
    }

    outWriter.close()
  }

  private def handleArgs(args: Array[String]): Option[(String, String)] = {
    if (args.length < 1) {
      println("You must provide a file name!")
      None
    } else {
      val fileName = args(0)
      if (fileName == null || fileName.trim.isEmpty || fileName.length <= 4) {
        println("You must provide a file name!")
        None
      }

      val (name, ext) = fileName.splitAt(fileName.length - 4)
      println("NAME: " + name)
      println("ext: " + ext)
      if (ext != ".asm") {
        println("File must have '.asm' extension!")
        None
      } else {
        Option(fileName, name)
      }
    }

  }

}