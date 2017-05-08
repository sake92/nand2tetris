package ba.sake.nand2tetris.vmtranslator

import java.io.File

object Main {

  def main(args: Array[String]): Unit = {

    val res = handleArgs(args)
    if (res.isEmpty) return
    val Some((fileName, name)) = res

    val inputFile = new File(fileName)
    val outputFile = new File(name + ".hack")

    val parser = Parser(inputFile)
    val codeGenerator = CodeGenerator(outputFile)

    var instruction: Option[I.Instruction] = null
    while ({ instruction = parser.next(); instruction.isDefined }) {
      codeGenerator.write(instruction.get, name)
    }

    parser.close()
    codeGenerator.close()
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