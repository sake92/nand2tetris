package ba.sake.nand2tetris.compiler

import java.io.File
import java.io.FilenameFilter

object Main {

  def main(args: Array[String]): Unit = {

    val res = handleArgs(args)
    if (res.isEmpty) return

    val files = res.get

    println("INPUT FILES: ")
    println(files.map(_._1.getAbsolutePath).mkString("\n"))

    println("OUTPUT VM FILES: ")
    println(files.map(_._2.getAbsolutePath).mkString("\n"))

    println("OUTPUT XML FILES: ")
    println(files.map(_._3.getAbsolutePath).mkString("\n"))

    // TODO
    // da GENERATOR prima SEKVENCU PISAČA U FAJLOVE!!! :D
    // tipa XML, VM ... :D

    /*for ((input, outputXML, outputVM) <- files) {
      // if you pass second param, there'll be no XML code generated :)
      if (args.length <= 1 || args(1) == null) {
        codeGenerator.writeBootstrap("BOOTSTRAP")
      }
      
      val codeGenerator = CodeGenerator(outputFile)
      writeCode(f, codeGenerator)
    }

    codeGenerator.close()*/
  }

  /*private def writeCode(f: File, codeGenerator: CodeGenerator) = {
    val parser = Parser(f)
    var instruction: Option[I.Instruction] = null
    while ({ instruction = parser.next(); instruction.isDefined }) {
      codeGenerator.write(instruction.get, f.getName.dropRight(3)) // must drop ".vm"
    }
    parser.close()
  }*/

  /**
   * @return Optional list of tuples (input, outputXML, outputVM)
   */
  private def handleArgs(args: Array[String]): Option[Seq[(File, File, File)]] = {
    if (args.length < 1) {
      println("You must provide a file(*.jack)/folder name!")
      None
    } else {
      val fileName = args(0)
      if (fileName == null || fileName.trim.isEmpty) {
        println("You must provide a file(*.jack)/folder name!")
        None
      } else {
        val fileOrFolder = new File(fileName)
        if (!fileOrFolder.exists) {
          println("File/folder does not exist!")
          None
        } else if (fileOrFolder.isFile) {
          handleFile(fileOrFolder)
        } else {
          val fileTuples = handleFolder(fileOrFolder).map(file2ResultTuple)
          Option(fileTuples)
        }
      }
    }
  }

  private def handleFolder(folder: File): Seq[File] = {
    folder.listFiles(
      new FilenameFilter() {
        def accept(dir: File, name: String): Boolean = {
          name.toLowerCase().endsWith(".jack");
        }
      }
    )
  }

  // (inputFile, outputFile)
  private def handleFile(file: File): Option[Seq[(File, File, File)]] = {
    val ext = file.getName.dropRight(5)
    if (ext != ".vm") {
      println("File must have a '.jack' extension!")
      None
    } else {
      Option(Seq(file2ResultTuple(file)))
    }
  }

  /** @return (input, outputXml, outputVM) */
  private def file2ResultTuple(f: File): (File, File, File) = {
    val outVM = new File(f.getParent, f.getName + ".vm")
    val outXML = new File(f.getParent, f.getName + ".xml")
    (f, outVM, outXML)
  }

}