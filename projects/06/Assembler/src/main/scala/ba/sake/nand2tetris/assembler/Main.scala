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
    
    val Some((inputFile, outputFile)) = res

    val symbolTable = new SymbolTable()
    val parser = Parser(inputFile, symbolTable)
    val codeGenerator = CodeGenerator(outputFile, symbolTable)

    var instruction: Option[I.RealInstruction] = null
    while ({ instruction = parser.next(); instruction.isDefined }) {
      codeGenerator.write(instruction.get)
    }

    parser.close()
    codeGenerator.close()
  }

  /**
   * @return Optional tuple (inputFile, outputFile)
   */
  private def handleArgs(args: Array[String]): Option[(File, File)] = {
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
        val inputFile = new File(fileName)
        Option((inputFile, new File(inputFile.getParent, name + ".hack")))
      }
    }
  }

}