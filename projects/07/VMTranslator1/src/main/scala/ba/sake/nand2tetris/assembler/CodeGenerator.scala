package ba.sake.nand2tetris.assembler

import java.io.PrintWriter

final class CodeGenerator(outWriter: PrintWriter) {

  var instructionCounter = 0 // debug comments..
  var jumpCounter = 0 // every JMP something instruction must have unique jump labels

  // 7.2.2 in spec
  val TrueValue = -1
  val FalseValue = 0

  def generate(instructions: Seq[I.Instruction], fileName: String) = {
    instructions.foreach { vmInstruction =>

      outWriter.write(s"// instruction no. $instructionCounter")
      instructionCounter += 1

      val assemblerInstruction: String = vmInstruction match {
        // arithmetical and logical instructions
        case I.AddInstruction() => operation((first, second) => s"$first+$second")
        case I.SubInstruction() => operation((first, second) => s"$first-$second")
        case I.AndInstruction() => operation((first, second) => s"$first&$second")
        case I.OrInstruction() => operation((first, second) => s"$first|$second")

        case I.EqInstruction() => operationWithJump("JEQ")
        case I.GtInstruction() => operationWithJump("JGT")
        case I.LtInstruction() => operationWithJump("JLT")

        case I.NegInstruction() => // neg (0-x == -x)
          s"""
          |${decrStack()}
          |@${S.SP}
          |A=M
          |D=0
          |M=D-M
          |""".stripMargin
        case I.NotInstruction() => // invert bits
          s"""
          |${decrStack()}
          |@${S.SP}
          |A=M             // indirect access...
          |M=!M
          |""".stripMargin

        /* stack instructions */
        // PUSH
        case I.PushInstruction(S.ConstSegment(), const) => // push constant 5
          s"""
            |@$const       // load constant
            |D=A           // store const in D
            |@${S.SP}      // get stack location
            |A=M           // indirect access!
            |M=D           // put const in stack
            |""".stripMargin
        case I.PushInstruction(S.OffsetSegment(base), index) =>
          s"""
            |@$base
            |D=A
            |@$index
            |A=D+A         // here we DIRECTLY SUM base(offset) and index(offset from base address...)
            |D=M           // now in D is the value of segment[index]
            |
            |@${S.SP}      // get stack location
            |A=M           // indirect access!
            |M=D           // put segment[index] on stack
            |""".stripMargin
        case I.PushInstruction(S.NormalSegment(base), index) => // push constant 5
          s"""
            |@$base
            |D=M           // indirect access...
            |
            |@$index
            |A=D+A
            |D=M           // now in D is the value of segment[index]
            |
            |@${S.SP}      // get stack location
            |A=M           // indirect access!
            |M=D           // put segment[index] on stack
            |""".stripMargin

        // POP
        case I.PopInstruction(S.ConstSegment(), const) => // pop constant 5
          "" // no-operation !!!? makes no sense to POP something INTO a CONST.. :D
        case I.PopInstruction(S.OffsetSegment(base), index) =>
            s"""
            |@$base
            |D=A
            |@$index
            |D=D+A         // address is now set
            |@${S.TEMP2}
            |M=D           // store address in TEMP2 register, temporarily
            |
            |${decrStack()}
            |@${S.SP}
            |A=M           // indirect access...
            |D=M           // store in D
            |
            |@${S.TEMP2}
            |A=M           // indirect
            |M=D           // store to M[TEMP2], that is segment[index]
            |
            |${decrStack()} // implementation detail, ignore this.. xD
            |""".stripMargin
        case I.PopInstruction(S.NormalSegment(base), index) => // push constant 5
          s"""
            |@$base
            |D=M           // indirect access...
            |@$index
            |D=D+A         // address is now set
            |@${S.TEMP2}
            |M=D           // store address in TEMP2 register, temporarily
            |
            |${decrStack()}
            |@${S.SP}
            |A=M           // indirect access...
            |D=M           // store in D
            |
            |@${S.TEMP2}
            |A=M           // indirect
            |M=D           // store to M[TEMP2], that is segment[index]
            |
            |${decrStack()} // implementation detail, ignore this.. xD
            |""".stripMargin

      }

      outWriter.write(assemblerInstruction)
      outWriter.write(incrStack() + "\n\n") // incr stack pointer after every instruction
    }
  }

  /** M - first operand
    * D - second operand
    * M - result
    */
  private def operation(op: (String, String) => String): String = {
    val opResult = "M=" + op("M", "D") // e.g. M=M+D
    s"""
            |${decrStack()}
            |@${S.SP}
            |A=M             // indirect access...
            |D=M             // store first argument in D
            |
            |${decrStack()}
            |@${S.SP}
            |A=M
            |$opResult        // do the operation and store in second arg
          |""".stripMargin
  }

  private def operationWithJump(condition: String): String = {
    val res = s"""
      |${decrStack()}
      |@${S.SP}
      |A=M             // indirect access...
      |D=M             // store first argument in D
      |
      |${decrStack()}
      |@${S.SP}
      |A=M
      |D=M-D           // subtract second argument and store in D
      |
      |@TRUE$jumpCounter
      |D;$condition // if D is true, jump to true
      |@${S.SP}
      |A=M
      |M=$FalseValue                // else it is false and goto end
      |@END$jumpCounter
      |0;JMP
      |
      |(TRUE$jumpCounter)
      |@${S.SP}
      |A=M
      |M=$TrueValue
      |(END$jumpCounter)
      |""".stripMargin
    jumpCounter += 1
    res
  }

  private def decrStack(): String = {
    s"@${S.SP}" + "\n" + s"M=M-1"
  }

  private def incrStack(): String = {
    s"@${S.SP}" + "\n" + s"M=M+1"
  }

}