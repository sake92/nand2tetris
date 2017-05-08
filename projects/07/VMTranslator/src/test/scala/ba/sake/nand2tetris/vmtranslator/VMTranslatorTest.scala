package ba.sake.nand2tetris.vmtranslator

import java.io.StringWriter

/**
 * It is hard to write automatic tests for VM program.
 * So we just print'em to console here, copy-paste to a file and check against CPU emulator... :/
 */
object VMTranslatorTest {

  val vmProgram =
    """
push constant 0    
pop local 0         // initializes sum = 0
label LOOP_START
push argument 0    
push local 0
add
pop local 0	        // sum = sum + counter
push argument 0
push constant 1
sub
pop argument 0      // counter--
push argument 0
if-goto LOOP_START  // If counter > 0, goto LOOP_START
push local 0

      """

  def main(args: Array[String]): Unit = {
    val parser = Parser(vmProgram)
    val codeGenerator = CodeGenerator() // write to console

    var instruction: Option[I.Instruction] = null
    while ({ instruction = parser.next(); instruction.isDefined }) {
      codeGenerator.write(instruction.get, "test")
    }

    parser.close()
    codeGenerator.close()
  }

}