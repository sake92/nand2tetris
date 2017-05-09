package ba.sake.nand2tetris.vmtranslator

import java.io.File
import java.io.FilenameFilter

object Main {

  def main(args: Array[String]): Unit = {

    val res = handleArgs(args)
    if (res.isEmpty) return

    val (inputFiles, outputFile) = res.get

    println("INPUT FILES: ")
    println(inputFiles.mkString("\n"))

    println("OUTPUT FILE: ")
    println(outputFile)

    val codeGenerator = CodeGenerator(outputFile)

    if (args.length <= 1 || args(1) == null) // if you pass second param, there'll be no bootstrap code generated :)
      codeGenerator.writeBootstrap("BOOTSTRAP")

    for (f <- inputFiles) writeCode(f, codeGenerator)

    codeGenerator.close()
  }

  private def writeCode(f: File, codeGenerator: CodeGenerator) = {
    val parser = Parser(f)
    var instruction: Option[I.Instruction] = null
    while ({ instruction = parser.next(); instruction.isDefined }) {
      codeGenerator.write(instruction.get, f.getName.dropRight(3)) // must drop ".vm"
    }
    parser.close()
  }

  /**
   * @return Optional tuple (inputFiles, outputFile)
   */
  private def handleArgs(args: Array[String]): Option[(Seq[File], File)] = {
    if (args.length < 1) {
      println("You must provide a file(*.vm)/folder name!")
      None
    } else {
      val fileName = args(0)
      if (fileName == null || fileName.trim.isEmpty) {
        println("You must provide a file(*.vm)/folder name!")
        None
      } else {
        val fileOrFolder = new File(fileName)
        if (!fileOrFolder.exists) {
          println("File/folder does not exist!")
          None
        } else if (fileOrFolder.isFile) {
          handleFile(fileOrFolder)
        } else {
          // one folder - one HACK file! :) inside that folder!
          Option((handleFolder(fileOrFolder), new File(fileOrFolder, fileOrFolder.getName + ".asm")))
        }
      }
    }
  }

  private def handleFolder(folder: File): Seq[File] = {
    folder.listFiles(
      new FilenameFilter() {
        def accept(dir: File, name: String): Boolean = {
          name.toLowerCase().endsWith(".vm");
        }
      }
    )
  }

  // (inputFile, outputFile)
  private def handleFile(file: File): Option[(Seq[File], File)] = {
    val fileName = file.getName
    val (name, ext) = fileName.splitAt(fileName.length - 3)
    if (ext != ".vm") {
      println("File must have a '.vm' extension!")
      None
    } else {
      Option(Seq(file), new File(file.getParent, name + ".asm"))
    }
  }

}