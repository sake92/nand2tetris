package ba.sake.nand2tetris.assembler

import fastparse.all._

/** Segments */
object S {

  final val SP = 0

  final val LCL = 1
  final val ARG = 2

  // pointer 0
  final val THIS = 3
  // pointer 1
  final val THAT = 4

  // R5-R12 temps
  // I only used TEMP2 because tests use it also... :D
  final val TEMP1 = 5
  final val TEMP2 = 6

  // general-purpose registers
  // how do we map/use these in VM?
  final val R13 = 13
  final val R14 = 14
  final val R15 = 15

  final val STATIC = 16 // base address for static variables

  sealed trait MemorySegment
  final case class NormalSegment(baseAddress: Long) extends MemorySegment // indirect access:  M[M[base]+index]
  final case class OffsetSegment(baseAddress: Long) extends MemorySegment // direct access:    M[base+index]
  final case class ConstSegment() extends MemorySegment // const:            index

  val ThisSegment = NormalSegment(THIS)
  val ThatSegment = NormalSegment(THAT)

  val LocalSegment = OffsetSegment(LCL)
  val ArgumentSegment = OffsetSegment(ARG)
  val PointerSegment = OffsetSegment(THIS) // this=0, that=1 !?  
  val TempSegment = OffsetSegment(TEMP1)
  val StaticSegment = OffsetSegment(STATIC)

  val ConstantSegment = ConstSegment() // no base address, just a dummy

  /*
pošto
@nešto alocira
novu varijablu,
iskoristićemo to
kao hop caku

statička varijabla
static 3 
npr u fajlu abc.vm
biće prevedena kao
@abc.3
   */
}

/** Instructions AST */
object I {

  sealed trait Instruction
  /* Arithmetic and logical instructions */
  final case class AddInstruction() extends Instruction
  final case class SubInstruction() extends Instruction
  final case class AndInstruction() extends Instruction
  final case class OrInstruction() extends Instruction

  final case class EqInstruction() extends Instruction
  final case class GtInstruction() extends Instruction
  final case class LtInstruction() extends Instruction

  final case class NegInstruction() extends Instruction
  final case class NotInstruction() extends Instruction

  /* Memory access instructions */
  final case class PushInstruction(segment: S.MemorySegment, index: Long) extends Instruction
  final case class PopInstruction(segment: S.MemorySegment, index: Long) extends Instruction

  //////// Part 2
  /* Program flow instructions */
  final case class LabelInstruction(label: String) extends Instruction // function-scoped
  final case class GotoInstruction(label: String) extends Instruction // function-scoped, jump unconditional
  final case class IfGotoInstruction(label: String) extends Instruction // function-scoped, jump conditional (topmost value on the stack)

  /* Function Calling instructions */
  final case class FunctionDeclarationInstruction(name: String, numLocalVars: Long) extends Instruction
  final case class FunctionCallInstruction(name: String, numLocalVars: Long) extends Instruction
  final case class ReturnInstruction() extends Instruction

}

/** The parser. */
final class Parser() {

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
  val AddInstruction = P("add").!.map(_ => I.AddInstruction())
  val SubInstruction = P("sub").!.map(_ => I.SubInstruction())
  val NegInstruction = P("neg").!.map(_ => I.NegInstruction())
  val EqInstruction = P("eq").!.map(_ => I.EqInstruction())
  val GtInstruction = P("gt").!.map(_ => I.GtInstruction())
  val LtInstruction = P("lt").!.map(_ => I.LtInstruction())
  val AndInstruction = P("and").!.map(_ => I.AndInstruction())
  val OrInstruction = P("or").!.map(_ => I.OrInstruction())
  val NotInstruction = P("not").!.map(_ => I.NotInstruction())

  val PushInstruction = P("push" ~ Whitespace.rep(min = 1) ~ Segment ~ Whitespace.rep(min = 1) ~ Number).map {
    case (segment, index) => I.PushInstruction(segment, index)
  }
  val PopInstruction = P("pop" ~ Whitespace.rep(min = 1) ~ Segment ~ Whitespace.rep(min = 1) ~ Number).map {
    case (segment, index) => I.PopInstruction(segment, index)
  }

  val LabelInstruction = P("label" ~ Whitespace.rep(min = 1) ~ Symbol!).map(I.LabelInstruction.apply)
  val GotoInstruction = P("goto" ~ Whitespace.rep(min = 1) ~ Symbol!).map(I.GotoInstruction.apply)
  val IfGotoInstruction = P("if-goto" ~ Whitespace.rep(min = 1) ~ Symbol!).map(I.IfGotoInstruction.apply)

  val Instruction: P[I.Instruction] = AddInstruction | SubInstruction | NegInstruction |
    EqInstruction | GtInstruction | LtInstruction | AndInstruction |
    OrInstruction | NotInstruction | PushInstruction | PopInstruction |
    LabelInstruction | GotoInstruction | IfGotoInstruction

  val FinalP = Whitespace.rep ~ Instruction.? ~ Whitespace.rep ~ Comment.?

  /** @param lines List of lines of an .asm file
    * @return List of instructions, A and C only!
    */
  def parse(lines: Seq[String]): Seq[I.Instruction] = {
    val allInstructions = lines.flatMap { i =>
      val res = FinalP.parse(i)
      res.get.value
    }

    allInstructions
  }

}