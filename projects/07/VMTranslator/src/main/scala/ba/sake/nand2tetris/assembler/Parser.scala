package ba.sake.nand2tetris.vmtranslator

import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStreamReader
import java.nio.file.Files

import fastparse.all._

/** Segments */
object S {

  /* LCL, ARG, THIS, THAT, TEMP are PREDEFINED in their IMPLEMENTATION of the Assembler !!! */

  sealed abstract class MemorySegment {
    def raw: String
  }
  /** Normal means a POINTER! :D */
  case class NormalSegment(raw: String, baseAddress: String) extends MemorySegment // indirect access:  M[M[base]+index]
  case class OffsetSegment(raw: String, baseAddress: String) extends MemorySegment // direct access:    M[base+index]
  case class StatSegment(raw: String) extends MemorySegment
  case class ConstSegment(raw: String) extends MemorySegment // const: index

  // IMPORTANT, see page 142 !!!!!
  val LocalSegment = NormalSegment("local", "LCL")
  val ArgumentSegment = NormalSegment("argument", "ARG")
  val ThisSegment = NormalSegment("this", "THIS")
  val ThatSegment = NormalSegment("that", "THAT")

  val PointerSegment = OffsetSegment("pointer", "THIS") // this=0, that=1 !?
  val TempSegment = OffsetSegment("temp", "R5")

  val ConstantSegment = ConstSegment("constant") // no base address, just a dummy

  val StaticSegment = StatSegment("static")
}

/** Instructions AST */
object I {

  sealed abstract class Instruction {
    def raw: String
  }
  /* Arithmetic and logical instructions */
  case class AddInstruction(raw: String) extends Instruction
  case class SubInstruction(raw: String) extends Instruction
  case class AndInstruction(raw: String) extends Instruction
  case class OrInstruction(raw: String) extends Instruction

  case class EqInstruction(raw: String) extends Instruction
  case class GtInstruction(raw: String) extends Instruction
  case class LtInstruction(raw: String) extends Instruction

  case class NegInstruction(raw: String) extends Instruction
  case class NotInstruction(raw: String) extends Instruction

  /* Memory access instructions */
  case class PushInstruction(raw: String, segment: S.MemorySegment, index: Long) extends Instruction
  case class PopInstruction(raw: String, segment: S.MemorySegment, index: Long) extends Instruction

  //////// Part 2
  /* Program flow instructions */
  case class LabelInstruction(raw: String, label: String) extends Instruction // function-scoped
  case class GotoInstruction(raw: String, label: String) extends Instruction // function-scoped, jump unconditional
  case class IfGotoInstruction(raw: String, label: String) extends Instruction // function-scoped, jump conditional (topmost value on the stack)

  /* Function Calling instructions */
  case class FunctionDeclarationInstruction(raw: String, name: String, numLocalVars: Long) extends Instruction
  case class FunctionCallInstruction(raw: String, name: String, numArgs: Long) extends Instruction
  case class ReturnInstruction(raw: String) extends Instruction
}

/** The parser. */
class Parser(br: BufferedReader) {

  import Parser._

  def next(): Option[I.Instruction] = {
    Option(br.readLine()) match {
      case None => { // end of input
        br.close()
        None
      }
      case Some(line) => FinalP.parse(line).get.value match {
        case Some(i: I.Instruction) => Option(i)
        case _ => next() // if not an instruction, skip
      }
    }
  }

  def close(): Unit = {
    br.close()
  }
}

/**
 * The companion object. A bit easier to read when separated from the class... :D
 * Contains "static" stuff.
 */
object Parser {

  def apply(inputFile: File): Parser = {
    val br = Files.newBufferedReader(inputFile.toPath())
    new Parser(br)
  }

  def apply(inputString: String): Parser = {
    val br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(inputString.getBytes)))
    new Parser(br)
  }

  val Newline = P(StringIn("\r\n", "\n"))
  val Whitespace = P(" " | "\t")
  val Comment = P("//" ~ AnyChar.rep ~ End)
  val Letter = P(CharPred(_.isLetter))
  val Digit = P(CharPred(_.isDigit))
  val Number = Digit.rep(min = 1).!.map(_.toLong) // captured number
  val LetterOrDigit = P(CharPred(_.isLetterOrDigit))
  val Symbol = P(!Digit ~ (LetterOrDigit | "." | "_" | "$" | ":").rep)! // must begin with NOT-DIGIT

  // SEGMENTS
  val ArgumentSegment = P("argument").!.map(_ => S.ArgumentSegment)
  val LocalSegment = P("local").!.map(_ => S.LocalSegment)
  val StaticSegment = P("static").!.map(_ => S.StaticSegment)
  val ConstantSegment = P("constant").!.map(_ => S.ConstantSegment)
  val ThisSegment = P("this").!.map(_ => S.ThisSegment)
  val ThatSegment = P("that").!.map(_ => S.ThatSegment)
  val PointerSegment = P("pointer").!.map(_ => S.PointerSegment)
  val TempSegment = P("temp").!.map(_ => S.TempSegment)

  val Segment: P[S.MemorySegment] = ArgumentSegment | LocalSegment | StaticSegment | ConstantSegment |
    ThisSegment | ThatSegment | PointerSegment | TempSegment

  // INSTRUCTIONS
  val AddInstruction = P("add").!.map(I.AddInstruction.apply)
  val SubInstruction = P("sub").!.map(I.SubInstruction.apply)
  val NegInstruction = P("neg").!.map(I.NegInstruction.apply)
  val EqInstruction = P("eq").!.map(I.EqInstruction.apply)
  val GtInstruction = P("gt").!.map(I.GtInstruction.apply)
  val LtInstruction = P("lt").!.map(I.LtInstruction.apply)
  val AndInstruction = P("and").!.map(I.AndInstruction.apply)
  val OrInstruction = P("or").!.map(I.OrInstruction.apply)
  val NotInstruction = P("not").!.map(I.NotInstruction.apply)

  val PushInstruction = P("push" ~ Whitespace.rep(min = 1) ~ Segment ~ Whitespace.rep(min = 1) ~ Number).map {
    case (segment, index) => I.PushInstruction(s"push ${segment.raw} $index", segment, index)
  }
  val PopInstruction = P("pop" ~ Whitespace.rep(min = 1) ~ Segment ~ Whitespace.rep(min = 1) ~ Number).map {
    case (segment, index) => I.PopInstruction(s"pop ${segment.raw} $index", segment, index)
  }

  val LabelInstruction = P("label" ~ Whitespace.rep(min = 1) ~ Symbol).map {
    label => I.LabelInstruction(s"label $label", label)
  }
  val GotoInstruction = P("goto" ~ Whitespace.rep(min = 1) ~ Symbol).map {
    label => I.GotoInstruction(s"goto $label", label)
  }
  val IfGotoInstruction = P("if-goto" ~ Whitespace.rep(min = 1) ~ Symbol).map {
    label => I.IfGotoInstruction(s"if-goto $label", label)
  }

  val FunctionDeclarationInstruction = P("function" ~ Whitespace.rep(min = 1) ~ Symbol ~ Whitespace.rep(min = 1) ~ Number).map {
    case (name, numLocalVars) => I.FunctionDeclarationInstruction(s"function $name $numLocalVars", name, numLocalVars)
  }
  val FunctionCallInstruction = P("call" ~ Whitespace.rep(min = 1) ~ Symbol ~ Whitespace.rep(min = 1) ~ Number).map {
    case (name, numArgs) => I.FunctionCallInstruction(s"call $name $numArgs", name, numArgs)
  }
  val ReturnInstruction = P("return").map(_ => I.ReturnInstruction("return"))

  val Instruction: P[I.Instruction] = AddInstruction | SubInstruction | NegInstruction |
    EqInstruction | GtInstruction | LtInstruction | AndInstruction |
    OrInstruction | NotInstruction | PushInstruction | PopInstruction |
    LabelInstruction | GotoInstruction | IfGotoInstruction |
    FunctionDeclarationInstruction | FunctionCallInstruction | ReturnInstruction

  val FinalP = Whitespace.rep ~ Instruction.? ~ Whitespace.rep ~ Comment.?
}