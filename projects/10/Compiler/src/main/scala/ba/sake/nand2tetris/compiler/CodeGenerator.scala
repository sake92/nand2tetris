package ba.sake.nand2tetris.compiler

import java.io.File
import java.io.PrintWriter
import java.io.Writer
import java.nio.charset.StandardCharsets

class CodeGenerator(outWriter: Writer) {

  // every JMP something instruction must have unique jump label
  private var jumpCounter = 0
  // every FUNCTION CALL must have a unique label, so we can return to it
  private var returnCounter = 0

  // must keep track of current function, to generate proper labels, f$myLabel
  private var currentFunction = ""

  // 7.2.2 in spec
  val TrueValue = -1
  val FalseValue = 0

  def write(instruction: I.Instruction, fileName: String): Unit = {
    outWriter.write(s"/// ${instruction.raw} ///\n")
    val assemblerInstruction = translate(instruction, fileName)
    outWriter.write(assemblerInstruction)
  }

  def writeBootstrap(fileName: String): Unit = {
    val bootstrapCode =
      s"""|////////// BOOTSTRAP CODE //////////
          |@256
          |D=A
          |@SP
          |M=D
          |
          |""".stripMargin
    outWriter.write(bootstrapCode)

    val callSysInit = I.FunctionCallInstruction("call Sys.init 0", "Sys.init", 0)
    write(callSysInit, fileName)

    outWriter.write("\n////////// BOOTSTRAP CODE END //////////\n\n")
  }

  private def translate(instruction: I.Instruction, fileName: String): String = {
    instruction match {
      // arithmetical and logical instructions
      case _: I.AddInstruction => operation(_ + "+" + _)
      case _: I.SubInstruction => operation(_ + "-" + _)
      case _: I.AndInstruction => operation(_ + "&" + _)
      case _: I.OrInstruction => operation(_ + "|" + _)

      case _: I.EqInstruction => operationWithJump("JEQ")
      case _: I.GtInstruction => operationWithJump("JGT")
      case _: I.LtInstruction => operationWithJump("JLT")

      case _: I.NegInstruction => // neg (0-x == -x)
        s"""|$decrStack
            |@SP
            |A=M
            |D=0
            |M=D-M
            |$incrStack
            |""".stripMargin
      case _: I.NotInstruction => // invert bits
        s"""|$decrStack
            |@SP
            |A=M
            |M=!M
            |$incrStack
            |""".stripMargin

      /* stack instructions */
      // PUSH
      case I.PushInstruction(_, segment, index) =>
        val resInD = segment match {
          case S.ConstSegment(_) =>
            s"""|@$index
                |D=A
                |""".stripMargin
          case S.OffsetSegment(_, b) =>
            s"""|@$b
                |D=A
                |@$index
                |A=D+A
                |D=M
                |""".stripMargin
          case S.NormalSegment(_, b) =>
            s"""|@$b
                |D=M       // indirect, pointer
                |@$index
                |A=D+A
                |D=M
                |""".stripMargin
          case S.StatSegment(_) =>
            val base = fileName + "." + index
            s"""|@$base
                |D=M       // indirect, pointer, VARIABLE! :)
                |""".stripMargin
        }
        s"""|$resInD
            |@SP
            |A=M
            |M=D
            |$incrStack
            |""".stripMargin

      // POP
      case I.PopInstruction(_, S.ConstSegment(_), const) => // pop constant 5
        "" // no-operation !!!? makes no sense to POP something INTO a CONST.. :D
      case I.PopInstruction(_, segment, index) =>
        val adrInD = segment match { // (segment: @unchecked)
          case S.OffsetSegment(_, b) =>
            s"""|@$b
                |D=A
                |@$index
                |D=D+A""".stripMargin
          case S.NormalSegment(_, b) =>
            s"""|@$b
                |D=M
                |@$index
                |D=D+A
                |""".stripMargin
          case S.StatSegment(_) =>
            val base = fileName + "." + index
            s"""|@$base
                |D=A
                |""".stripMargin
        }
        s"""|$adrInD
            |@R13
            |M=D
            |
            |$decrStack
            |@SP
            |A=M
            |D=M
            |
            |@R13
            |A=M
            |M=D
            |
            |""".stripMargin

      /* Program flow instructions */
      // page 164 function$label !!!!!
      case I.LabelInstruction(_, label) =>
        s"""|(${currentFunction}$$${label})
            |
            |""".stripMargin
      case I.GotoInstruction(_, label) =>
        s"""|@${currentFunction}$$${label}
            |0;JMP
            |
            |""".stripMargin
      case I.IfGotoInstruction(_, label) =>
        s"""|$decrStack
            |@SP
            |A=M
            |D=M
            |
            |@${currentFunction}$$${label}
            |D;JNE
            |
            |""".stripMargin

      /* Function Calling instructions */
      // MUST SEE PAGE 163 !!! :D
      case I.FunctionDeclarationInstruction(_, name, numLocalVars) =>
        currentFunction = name // SET CURRENT FUNCTION VARIABLE
        def pushedArgs = for (i <- 0 until numLocalVars.toInt) yield (translate(I.PushInstruction("push local 0", S.ConstantSegment, 0), fileName))
        s"""|($name)
            |${pushedArgs.mkString}
            |""".stripMargin

      case I.FunctionCallInstruction(_, name, numArgs) =>
        val returnLabel = s"RETURN_$returnCounter"
        returnCounter += 1

        val pushLabel =
          s"""|@$returnLabel
              |D=A
              |@SP
              |A=M
              |M=D
              |$incrStack
              |""".stripMargin
        // NOT A NORMAL PUSH !!! :D
        // we push te VALUE of the POINTER, not some variable TO POINTER...
        val pushLcl = pushSegmentOnStack("LCL")
        val pushArg = pushSegmentOnStack("ARG")
        val pushThis = pushSegmentOnStack("THIS")
        val pushThat = pushSegmentOnStack("THAT")

        s"""|$pushLabel
            |
            |$pushLcl
            |$pushArg
            |$pushThis
            |$pushThat
            |
            |// ARG = SP-n-5
            |@SP
            |D=M
            |@5
            |D=D-A
            |@$numArgs
            |D=D-A
            |@ARG
            |M=D
            |
            |// LCL = SP
            |@SP
            |D=M
            |@LCL
            |M=D
            |
            |// goto f
            |@$name
            |0;JMP
            |
            |($returnLabel)
            |""".stripMargin

      case I.ReturnInstruction(_) =>
        def popArg = translate(I.PopInstruction(s"pop ARG", S.ArgumentSegment, 0), fileName)
        // seg = *(FRAME - minus) // FRAME is R7
        // NOTEEEEEEEEEEEEEEE
        // watch out not to CONFLICT THESE TEMPORARY REGISTERSSSSSSs
        // e.g. R13 is used for POPPPPPPPP
        def frameRestore(seg: String, minus: Int) =
          s"""|@R7
              |D=M
              |@$minus
              |A=D-A
              |D=M
              |
              |@$seg
              |M=D
              |""".stripMargin
        s"""|// FRAME = LCL       // FRAME  is a temporary variable R7
            |@LCL
            |D=M
            |@R7
            |M=D
            |
            |// RET = *(FRAME-5)  // store RETURN-address in a temp var R14
            |@5
            |A=D-A
            |D=M
            |@R14
            |M=D
            |
            |// *ARG = pop()      // Reposition the return value for the caller
            |$popArg
            |
            |// SP = ARG+1        // Restore SP of the caller
            |@ARG
            |D=M
            |@SP
            |M=D+1
            |
            |// THAT = *(FRAME-1) // Restore THAT of the caller
            |${frameRestore("THAT", 1)}
            |// THIS = *(FRAME-2) // Restore THIS of the caller
            |${frameRestore("THIS", 2)}
            |// ARG = *(FRAME-3)  // Restore ARG of the caller
            |${frameRestore("ARG", 3)}
            |// LCL = *(FRAME-4)  // Restore LCL of the caller
            |${frameRestore("LCL", 4)}
            |
            |// goto RET // Goto return-address (in the caller’s code)
            |@R14
            |A=M
            |0;JMP
            |
            |""".stripMargin
    }
  }

  /**
   * M - first operand
   * D - second operand
   * M - result
   */
  private def operation(op: (String, String) => String): String = {
    val opResult = "M=" + op("M", "D") // e.g. M=M+D
    s"""|$decrStack
        |@SP
        |A=M
        |D=M
        |$decrStack
        |@SP
        |A=M
        |$opResult
        |$incrStack
        |""".stripMargin
  }

  private def operationWithJump(condition: String): String = {
    jumpCounter += 1
    s"""|$decrStack
          |@SP
          |A=M
          |D=M
          |$decrStack
          |@SP
          |A=M
          |D=M-D          // D is condition for jump
          |
          |@TRUE$jumpCounter
          |D;$condition
          |@SP
          |A=M
          |M=$FalseValue
          |@END$jumpCounter
          |0;JMP
          |
          |(TRUE$jumpCounter)
          |@SP
          |A=M
          |M=$TrueValue
          |(END$jumpCounter)
          |
          |$incrStack
          |""".stripMargin
  }

  private val decrStack: String =
    """|@SP            // SP--
       |M=M-1
       |""".stripMargin

  private val incrStack: String =
    """|@SP            // SP++
       |M=M+1
       |""".stripMargin

  private def pushSegmentOnStack(seg: String): String =
    s"""|// pushing segment $seg on the stack !
        |@$seg
        |D=M
        |@SP
        |A=M
        |M=D
        |$incrStack
        |""".stripMargin

  def close(): Unit = {
    outWriter.close()
  }
}

object CodeGenerator {

  // print to file
  def apply(outputFile: File): CodeGenerator = {
    val outWriter = new PrintWriter(outputFile, StandardCharsets.UTF_8.name)
    new CodeGenerator(outWriter)
  }

  // print to a Writer, e.g. a string for debugging
  def apply(outWriter: Writer): CodeGenerator = {
    new CodeGenerator(outWriter)
  }

  // print to console
  def apply(): CodeGenerator = {
    val outWriter = new PrintWriter(System.out)
    new CodeGenerator(outWriter)
  }
}
