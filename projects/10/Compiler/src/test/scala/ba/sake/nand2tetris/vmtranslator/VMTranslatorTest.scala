package ba.sake.nand2tetris.vmtranslator

import java.io.StringWriter

/**
 * It is hard to write automatic tests for VM program.
 * So we just print'em to console here, copy-paste to a file and check against CPU emulator... :/
 */
object VMTranslatorTest {

  val vmProgram =
    """
function SimpleFunction.test 2
push local 0
push local 1
add
not
push argument 0
add
push argument 1
sub
return

      """

  def main(args: Array[String]): Unit = {
    val parser = Parser(vmProgram)
    val codeGenerator = CodeGenerator() // write to console

    val fileName = "test"
    codeGenerator.writeBootstrap(fileName)

    var instruction: Option[I.Instruction] = null
    while ({ instruction = parser.next(); instruction.isDefined }) {
      codeGenerator.write(instruction.get, fileName)
    }

    parser.close()
    codeGenerator.close()
  }

}