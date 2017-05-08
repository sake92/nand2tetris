package ba.sake.nand2tetris.vmtranslator

import java.io.File
import java.io.PrintWriter
import java.io.Writer
import java.nio.charset.StandardCharsets

final class CodeGenerator(outWriter: Writer) {

  var instructionCounter = 0 // debug comments..
  var jumpCounter = 0 // every JMP something instruction must have unique jump labels

  // 7.2.2 in spec
  val TrueValue = -1
  val FalseValue = 0

  def write(instruction: I.Instruction, fileName: String): Unit = {

    outWriter.write(s"/// ${instruction.raw} ///\n")
    instructionCounter += 1

    val assemblerInstruction: String = instruction match {
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
      case I.PushInstruction(_, S.ConstSegment(_), const) => // push constant 5
        s"""|@$const
            |D=A
            |@SP
            |A=M
            |M=D
            |$incrStack
            |""".stripMargin
      case I.PushInstruction(_, segment, index) =>
        val (accessType, base) = segment match {
          case S.OffsetSegment(_, b) => ("D=A", b) // direct
          case S.NormalSegment(_, b) => ("D=M", b) // indirect (pointer)
          case S.StatSegment(_) => ("D=M", fileName + "." + index)
        }
        s"""|@$base
            |$accessType
            |@$index
            |A=D+A
            |D=M
            |
            |@SP
            |A=M
            |M=D
            |$incrStack
            |""".stripMargin

      // POP
      case I.PopInstruction(_, S.ConstSegment(_), const) => // pop constant 5
        "" // no-operation !!!? makes no sense to POP something INTO a CONST.. :D
      case I.PopInstruction(_, segment, index) =>
        val (accessType, base) = segment match { // (segment: @unchecked)
          case S.OffsetSegment(_, b) => ("D=A", b) // direct
          case S.NormalSegment(_, b) => ("D=M", b) // indirect (pointer)
          case S.StatSegment(_) => ("D=M", fileName + "." + index)
        }
        s"""|@$base
            |$accessType
            |@$index
            |D=D+A
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

      case I.LabelInstruction(_, label) =>
        s"""|($label)
            |
            |""".stripMargin
      case I.GotoInstruction(_, label) =>
        s"""|@$label
            |0;JMP
            |
            |""".stripMargin
      case I.IfGotoInstruction(_, label) =>
        s"""|$decrStack
            |@SP
            |A=M
            |D=M
            |
            |@$label
            |D;JNE
            |
            |""".stripMargin

      // TODO popravit ovo incrStack.. :D
    }

    outWriter.write(assemblerInstruction)
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
        |
        |$decrStack
        |@SP
        |A=M
        |
        |$opResult
        |
        |$incrStack
        |""".stripMargin
  }

  private def operationWithJump(condition: String): String = {
    val res =
      s"""|$decrStack
          |@SP
          |A=M
          |D=M
          |
          |$decrStack
          |@SP
          |A=M
          |D=M-D
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
    jumpCounter += 1
    res
  }

  private val decrStack: String =
    """|// SP--
       |@SP
       |M=M-1
       |""".stripMargin

  private val incrStack: String =
    """|// SP++
       |@SP
       |M=M+1
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
