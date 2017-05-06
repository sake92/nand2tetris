package ba.sake.nand2tetris.assembler

import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import fastparse.all._

/** Instructions AST */
object I {

  sealed trait Instruction
  sealed trait RealInstruction extends Instruction // A or C

  // not a REAL instruction, used just to point to the next instruction
  // DOESN't EXTEND the Instruction trait
  case class SymbolInstruction(symbol: String) extends Instruction // (something)

  case class AInstruction(constOrSymbol: Either[Int, String]) extends RealInstruction // a instruction: @123 or @something
  object AInstruction {
    def apply(const: Int): AInstruction = AInstruction(Left(const))
    def apply(symbol: String): AInstruction = AInstruction(Right(symbol))
  }

  /** Could be dest=comp or jump or dest=comp;jump */
  case class Destination(expr: String, machineCode: String)
  case class Computation(expr: String, machineCode: String)
  case class Jump(expr: String, machineCode: String)

  case class CInstruction(dest: Destination, comp: Computation, jump: Jump) extends RealInstruction

  object CInstruction {
    val destinationsMap = Map(("" -> "000"), ("M" -> "001"), ("D" -> "010"), ("MD" -> "011"),
      ("A" -> "100"), ("AM" -> "101"), ("AD" -> "110"), ("AMD" -> "111"))

    val computationsMapNotA = Map(("0" -> "101010"), ("1" -> "111111"), ("-1" -> "111010"),
      ("D" -> "001100"), ("A" -> "110000"), ("!D" -> "001101"), ("!A" -> "110001"),
      ("-D" -> "001111"), ("-A" -> "110011"), ("D+1" -> "011111"), ("A+1" -> "110111"),
      ("D-1" -> "001110"), ("A-1" -> "110010"), ("D+A" -> "000010"), ("D-A" -> "010011"),
      ("A-D" -> "000111"), ("D&A" -> "000000"), ("D|A" -> "010101"))
    val computationsMapA = Map(
      ("M" -> "110000"),
      ("!M" -> "110001"), ("-M" -> "110011"), ("M+1" -> "110111"), ("M-1" -> "110010"),
      ("D+M" -> "000010"), ("D-M" -> "010011"), ("M-D" -> "000111"), ("D&M" -> "000000"), ("D|M" -> "010101")
    )
    val computationsMap = computationsMapNotA ++ computationsMapA

    val jumpsMap = Map(("" -> "000"), ("JGT" -> "001"), ("JEQ" -> "010"), ("JGE" -> "011"),
      ("JLT" -> "100"), ("JNE" -> "101"), ("JLE" -> "110"), ("JMP" -> "111"))

    def destFromExpression(expr: String): Destination = Destination(expr, destinationsMap(expr))

    def compFromExpression(expr: String): Computation = {
      computationsMapNotA.get(expr) match {
        case Some(v) => Computation(expr, "0" + v) // a=0
        case None => computationsMapA.get(expr) match {
          case Some(v) => Computation(expr, "1" + v) // a=1
          case None => throw new RuntimeException("Expression not allowed: " + expr)
        }
      }
    }

    def jumpFromExpression(expr: String): Jump = Jump(expr, jumpsMap(expr))

  }
}

/** The parser. */
class Parser(inputFile: File, symbolTable: SymbolTable) {
  import Parser._

  // FIRST PASS
  buildSymbolTable()

  private val br = Files.newBufferedReader(inputFile.toPath)

  def next(): Option[I.RealInstruction] = {
    Option(br.readLine()) match {
      case None => { // end of input
        br.close()
        None
      }
      case Some(line) => FinalP.parse(line).get.value match {
        case Some(i: I.RealInstruction) => Option(i)
        case _ => next() // if not A or C, skip
      }
    }
  }

  private def buildSymbolTable(): Unit = {
    val br = Files.newBufferedReader(inputFile.toPath)
    var line: String = null
    var realInstructionsCounter = 0
    try {
      while ({ line = br.readLine(); line != null }) {
        FinalP.parse(line).get.value match {
          case Some(I.SymbolInstruction(symbol)) => symbolTable.add(symbol, realInstructionsCounter)
          case Some(_) => realInstructionsCounter += 1
          case _ => // if empty line, or just a comment
        }
      }
    } finally {
      br.close()
    }
  }

}

object Parser {
  val Newline = P(StringIn("\r\n", "\n"))
  val Whitespace = P(" " | "\t" | Newline)
  val Comment = P("//" ~ AnyChar.rep ~ End)
  val Letter = P(CharPred(_.isLetter))
  val Digit = P(CharPred(_.isDigit))
  val Number: P[Int] = Digit.rep(min = 1).!.map(_.toInt) // captured Int number
  val LetterOrDigit = P(CharPred(_.isLetterOrDigit))
  val Symbol = P(!Digit ~ (LetterOrDigit | "." | "_" | "$" | ":").rep)! // must begin with NOT-DIGIT

  // Symbol pseudo-instruction
  val SymbolInstruction = P("(" ~ Symbol.! ~ ")").map(s => I.SymbolInstruction(s))

  // A instruction: @123 or @something
  val AInstruction = P("@" ~ (Symbol.!.map(I.AInstruction.apply) | Number.map(I.AInstruction.apply)))

  // C instruction: dest=comp;jump
  // destination possibilities
  val dest = StringIn(I.CInstruction.destinationsMap.keys.toSeq: _*).!.map(I.CInstruction.destFromExpression)
  // computation possibilities
  val comp = StringIn(I.CInstruction.computationsMap.keys.toSeq: _*).!.map(I.CInstruction.compFromExpression)
  // jump options
  val jump = StringIn(I.CInstruction.jumpsMap.keys.toSeq: _*).!.map(I.CInstruction.jumpFromExpression)

  // expression: dest=comp;jump  
  // P[(Option[I.Destination], I.Computation, Option[I.Jump]) .map ...
  val CInstruction = ((dest ~ "=").? ~ comp ~ (";" ~ jump).?).map {
    case (d, computation, j) =>
      val destination = d.getOrElse(I.CInstruction.destFromExpression(""))
      val jump = j.getOrElse(I.CInstruction.jumpFromExpression(""))
      I.CInstruction(destination, computation, jump)
  }

  /////// final stuff
  val Instruction: P[I.Instruction] = SymbolInstruction | AInstruction | CInstruction
  val FinalP = Whitespace.rep ~ Instruction.? ~ Whitespace.rep ~ Comment.?
}