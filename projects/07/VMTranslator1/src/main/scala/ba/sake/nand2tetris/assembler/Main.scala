package ba.sake.nand2tetris.assembler

import scala.io.Source
import java.io.File
import java.io.PrintWriter
import java.nio.charset.StandardCharsets

object Main {
  
  def main(args: Array[String]): Unit = {
    val program = """
push constant 111
push constant 333
push constant 888
pop static 8
pop static 3
pop static 1
push static 3
push static 1
sub
push static 8
add

      """
    val lines = program.lines.toVector
    val parser = new Parser()
    val outputWriter = new PrintWriter( System.out)
    val codeGenerator = new CodeGenerator(outputWriter)
    
    val instructions = parser.parse(lines)
    codeGenerator.generate(instructions, "BLA")
    outputWriter.close()
  }

  def main2(args: Array[String]): Unit = {

    val res = handleArgs(args)
    if (res.isEmpty) return
    val Some((fileName, name)) = res

    val inputFile = new File(fileName)
    val lines = Source.fromFile(inputFile).getLines.toVector
    val outputFile = new File(name + ".asm")
    val outputWriter = new PrintWriter(outputFile, StandardCharsets.UTF_8.name)

    val parser = new Parser()
    val codeGenerator = new CodeGenerator(outputWriter)

    val instructions = parser.parse(lines)
    val code = codeGenerator.generate(instructions, fileName)

    outputWriter.close()
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
      if (ext != ".vm") {
        println("File must have '.vm' extension!")
        None
      } else {
        Option(fileName, name)
      }
    }

  }

}