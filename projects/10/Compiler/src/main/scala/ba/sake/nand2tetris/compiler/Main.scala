package ba.sake.nand2tetris.compiler

import java.io.File
import java.io.FilenameFilter
import java.nio.file.Files
import scala.collection.JavaConverters._

object Main {

  def main(args: Array[String]): Unit = {

    val res = handleArgs(args)
    if (res.isEmpty) return

    for ((input, outputXML, outputVM) <- res.get) {
      println("*************************")

      // 1. parse input
      println("INPUT FILE: " + input.getAbsolutePath)
      val parser = Parser(Files.readAllLines(input.toPath).asScala.mkString("\n"))
      val result = parser.analyze()

      // 2. always generat VM files
      println("OUTPUT VM FILE: " + outputVM.getAbsolutePath)
      val vmGenerator = VMGenerator(outputVM)
      vmGenerator.generate(result)
      vmGenerator.close()

      // 3. optionally generate XML
      // if you pass second param, there'll be no XML code generated :)
      if (args.length <= 1 || args(1) == null) {
        println("OUTPUT XML FILE: " + outputXML.getAbsolutePath)
        val xmlGenerator = XmlGenerator(outputXML)
        xmlGenerator.generate(result)
        xmlGenerator.close()
      }
    }

  }

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
          name.toLowerCase().endsWith(".jack")
        }
      }
    )
  }

  // (inputFile, outputFile)
  private def handleFile(file: File): Option[Seq[(File, File, File)]] = {
    val ext = file.getName.takeRight(5)
    if (ext != ".jack") {
      println("File must have a '.jack' extension!")
      None
    } else {
      Option(Seq(file2ResultTuple(file)))
    }
  }

  /** @return (input, outputXml, outputVM) */
  private def file2ResultTuple(f: File): (File, File, File) = {
    val name = f.getName.dropRight(5)
    val outXML = new File(f.getParent, name + ".xml")
    val outVM = new File(f.getParent, name + ".vm")
    (f, outXML, outVM)
  }

}