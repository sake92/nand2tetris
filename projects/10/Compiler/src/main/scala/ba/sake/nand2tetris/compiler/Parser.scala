package ba.sake.nand2tetris.compiler

import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStreamReader
import java.nio.file.Files

object AST {

  sealed trait ClassNameOrVarName

  /* It is a pity I don't know how to make this stuff easier... :/ */
  sealed trait VoidOrType
  sealed trait Type extends VoidOrType
  case class ClassName(name: String) extends Type with ClassNameOrVarName

  case class VarName(name: String) extends ClassNameOrVarName
  case class SubroutineName(name: String)

  /* TERMINALS: keyword, symbol, integerConstant, stringConstant, or identifier */

  /* KEYWORDS */
  sealed trait KeywordTerminal { def raw: String }

  // keywords as constants
  sealed trait KeywordConstant extends KeywordTerminal
  case object TrueConstant extends KeywordConstant { def raw = "true" }
  case object FalseConstant extends KeywordConstant { def raw = "false" }
  case object NullConstant extends KeywordConstant { def raw = "true" }
  case object ThisConstant extends KeywordConstant { def raw = "this" }

  // keywords that represent a type
  sealed trait KeywordType extends KeywordTerminal with Type
  case object Int extends KeywordType { def raw = "int" }
  case object Char extends KeywordType { def raw = "char" }
  case object Boolean extends KeywordType { def raw = "boolean" }

  // keywords that represent field type
  sealed trait KeywordFieldType extends KeywordTerminal
  case object Static extends KeywordFieldType { def raw = "static" }
  case object Field extends KeywordFieldType { def raw = "field" }

  // keywords that represent subroutine type
  sealed trait SubroutineType extends KeywordTerminal
  case object Constructor extends SubroutineType { def raw = "constructor" }
  case object Function extends SubroutineType { def raw = "function" }
  case object Method extends SubroutineType { def raw = "method" }

  case object Void extends KeywordTerminal with VoidOrType { def raw = "void" } // this one is special :p

  // These are "useless"/"meaingless"/"throw-away" keywords
  /*case class Keyword(raw: String) extends KeywordTerminal
  val kwdClass = Keyword("class")
  val kwdVar = Keyword("var")
  val kwdLet = Keyword("let")
  val kwDo = Keyword("do")
  val kwdIf = Keyword("if")
  val kwdElse = Keyword("else")
  val kwdWhile = Keyword("while")
  val kwdReturn = Keyword("return")*/

  /* SYMBOLS */
  sealed trait SymbolTerminal { def raw: String }

  sealed trait UnaryOp extends SymbolTerminal
  case object Negate extends UnaryOp { def raw = "-" }
  case object Invert extends UnaryOp { def raw = "~" }

  sealed trait BinaryOp extends SymbolTerminal
  case object Plus extends BinaryOp { def raw = "+" }
  case object Minus extends BinaryOp { def raw = "-" }
  case object Multiply extends BinaryOp { def raw = "*" }
  case object Divide extends BinaryOp { def raw = "/" }
  case object And extends BinaryOp { def raw = "&" }
  case object Or extends BinaryOp { def raw = "|" }
  case object LessThan extends BinaryOp { def raw = "<" }
  case object GreaterThan extends BinaryOp { def raw = ">" }
  case object EqualTo extends BinaryOp { def raw = "=" }

  // use-once symbols
  /*case class Symbol(raw: String) extends SymbolTerminal
  val symLeftBrace = Symbol("{")
  val symRightBrace = Symbol("}")
  val symLeftParen = Symbol("(")
  val symRightParen = Symbol(")")
  val symLeftBracket = Symbol("[")
  val symRightBracket = Symbol("]")
  val symDot = Symbol(".")
  val symComma = Symbol(",")
  val symSemicolon = Symbol(";")*/

  case class IntConst(value: Int)
  case class StringConst(value: String)

  /* NON-TERMINALS */
  /* class, classVarDec, subroutineDec, parameterList,
   * subroutineBody, varDec,
   * statements, whileSatement, ifStatement, returnStatement,
   * letStatement, doStatement, expression, term, expressionList
   */

  sealed trait SubroutineCall
  case class SubroutineCallNormal(subName: SubroutineName, expressions: ExpressionList) extends SubroutineCall
  case class SubroutineCallPrefixed(classOrVarName: ClassNameOrVarName, subName: SubroutineName, expressions: ExpressionList) extends SubroutineCall

  case class Expression(term: Term, opTerms: Seq[(BinaryOp, Term)])
  case class ExpressionList(expressions: Seq[Expression])

  sealed trait Term
  case class IntegerConstantTerm(int: IntConst) extends Term
  case class StringConstantTerm(str: StringConst) extends Term
  case class KeywordConstantTerm(kwd: KeywordConstant) extends Term
  case class VarNameTerm(varName: VarName) extends Term
  case class ArrayTerm(arrName: VarName, expr: Expression) extends Term
  case class SubroutineCallTerm(subroutineCall: SubroutineCall) extends Term
  case class ParensExprTerm(expr: Expression) extends Term
  case class UnaryOpTerm(unaryOp: UnaryOp, term: Term) extends Term

  // statements
  sealed trait Statement
  case class LetStatement(varName: VarName, maybeIndexExpr: Option[Expression], expr: Expression) extends Statement
  case class IfStatement(condition: Expression, ifTrueStatements: Seq[Statement], ifFalseStatements: Option[Seq[Statement]]) extends Statement
  case class WhileStatement(condition: Expression, statements: Seq[Statement]) extends Statement
  case class DoStatement(subroutineCall: SubroutineCall) extends Statement
  case class ReturnStatement(expr: Option[Expression]) extends Statement

  case class VarDec(tpe: Type, varName: VarName, varNames: Seq[VarName])

  case class ParameterList(params: Seq[(Type, VarName)])
  case class SubroutineBody(varDecs: Seq[VarDec], statements: Seq[Statement])

  case class ClassVarDec(fieldType: KeywordFieldType, tpe: Type, varName: VarName, varNames: Seq[VarName])

  case class SubroutineDec(subroutineType: SubroutineType, voidOrType: VoidOrType, subroutineName: SubroutineName,
                           parameterList: ParameterList, subroutineBody: SubroutineBody)

  case class ClassDec(className: ClassName, classVarDecs: Seq[ClassVarDec], subroutineDecs: Seq[SubroutineDec])

}

/** The parser. */
class Parser(br: BufferedReader) {

  import Parser._

  def next(): Option[ /*I.Instruction*/ String] = {
    None
    /*Option(br.readLine()) match {
      case None => { // end of input
        br.close()
        None
      }
      case Some(line) => FinalP.parse(line).get.value match {
        case Some(i: I.Instruction) => Option(i)
        case _ => next() // if not an instruction, skip
      }
    }*/
  }

  def close(): Unit = {
    br.close()
  }
}

/** The companion object. A bit easier to read when separated from the class... :D
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

  val White = fastparse.WhitespaceApi.Wrapper {
    import fastparse.all._
    val NL = P("\r\n" | "\n")
    val WS = P(" " | "\t" | NL)

    /* This is shamelessly stolen from here: 
     * https://github.com/lihaoyi/fastparse/blob/master/scalaparse/shared/src/main/scala/scalaparse/syntax/Literals.scala#L52 
     * :sadface:
     * */
    val CommentChunk = P(CharsWhile(c => c != '/' && c != '*') | !"*/" ~ AnyChar)
    val MultilineComment: P0 = P("/*" ~/ CommentChunk.rep ~ "*/")

    val SameLineCharChunks = P(CharsWhile(c => c != '\n' && c != '\r') | !NL ~ AnyChar)
    val LineComment = P("//" ~ SameLineCharChunks.rep ~ &(NL | End))
    val Comment: P0 = P(MultilineComment | LineComment)

    val IGNORE = P(WS | Comment)
    NoTrace(IGNORE.rep)
  }
  import fastparse.noApi._
  import White._

  // since we use WHITESPACE API, a few notes:
  // methods "~" and "rep" take an implpicit parameter that defines whitespace to ignore
  // methods "~~" and "repX" are NORMAL ones... :D explicit that is

  // PRO-TIP
  // ALWAYS PUT BRACKETS AROUND OPTIONAL PARSERSSSSSSS!
  // e.g. z~((a~b)?) and NOT like z~(a~b)?

  val Letter = P(CharPred(_.isLetter))
  val Digit = P(CharPred(_.isDigit))
  val IdentifierFirst = P(Letter | "." | "_" | "$" | ":") // symbol must begin with NON-DIGIT
  val Identifier = P(IdentifierFirst ~~ (IdentifierFirst | Digit).repX)!
  val Number = P(Digit.rep(min = 1)).!.map(_.toInt) // captured Int number

  /* LEXICAL ELEMENTS */
  val kwdClass = P("class") //.!.map(_ => AST.kwdClass)
  val kwdVar = P("var") //.!.map(_ => AST.kwdVar)
  val kwdLet = P("let") //.!.map(_ => AST.kwdLet)
  val kwdDo = P("do") //.!.map(_ => AST.kwDo)
  val kwdIf = P("if") //.!.map(_ => AST.kwdIf)
  val kwdElse = P("else") //.!.map(_ => AST.kwdElse)
  val kwdWhile = P("while") //.!.map(_ => AST.kwdWhile)
  val kwdReturn = P("return") //.!.map(_ => AST.kwdReturn)
  val kwdConstructor = P("constructor").!.map(_ => AST.Constructor)
  val kwdFunction = P("function").!.map(_ => AST.Function)
  val kwdMethod = P("method").!.map(_ => AST.Method)
  val kwdField = P("field").!.map(_ => AST.Field)
  val kwdStatic = P("static").!.map(_ => AST.Static)
  val kwdInt = P("int").!.map(_ => AST.Int)
  val kwdChar = P("char").!.map(_ => AST.Char)
  val kwdBoolean = P("boolean").!.map(_ => AST.Boolean)
  val kwdVoid = P("void").!.map(_ => AST.Void)
  val kwdTrue = P("true").!.map(_ => AST.TrueConstant)
  val kwdFalse = P("false").!.map(_ => AST.FalseConstant)
  val kwdNull = P("null").!.map(_ => AST.NullConstant)
  val kwdThis = P("this").!.map(_ => AST.ThisConstant)

  val symLeftBrace = P("{") //.!.map(_ => AST.symLeftBrace)
  val symRightBrace = P("}") //.!.map(_ => AST.symRightBrace)
  val symLeftParen = P("(") //.!.map(_ => AST.symLeftParen)
  val symRightParen = P(")") //.!.map(_ => AST.symRightParen)
  val symLeftBracket = P("[") //.!.map(_ => AST.symLeftBracket)
  val symRightBracket = P("]") //.!.map(_ => AST.symRightBracket)
  val symDot = P(".") //.!.map(_ => AST.symDot)
  val symComma = P(",") //.!.map(_ => AST.symComma)
  val symSemicolon = P(";") //.!.map(_ => AST.symSemicolon)
  val symPlus = P("+").!.map(_ => AST.Plus)
  val symMinus = P("-").!.map(_ => AST.Minus)
  val symMultiply = P("*").!.map(_ => AST.Multiply)
  val symDivide = P("/").!.map(_ => AST.Divide)
  val symAnd = P("&").!.map(_ => AST.And)
  val symOr = P("|").!.map(_ => AST.Or)
  val symLessThan = P("<").!.map(_ => AST.LessThan)
  val symGreaterThan = P(">").!.map(_ => AST.GreaterThan)
  val symEqual = P("=").!.map(_ => AST.EqualTo)

  val symNegate = P("-").!.map(_ => AST.Negate)
  val symTilda = P("~").!.map(_ => AST.Invert)

  ///////////////////////
  /*val KEYWORD: P[AST.KeywordTerminal] = P(kwdClass | kwdConstructor | kwdFunction | kwdMethod | kwdField |
    kwdStatic | kwdVar | kwdInt | kwdChar | kwdBoolean | kwdVoid | kwdTrue | kwdFalse |
    kwdNull | kwdThis | kwdLet | kwdDo | kwdIf | kwdElse | kwdWhile | kwdReturn)

  val SYMBOL: P[AST.SymbolTerminal] = P(symLeftBrace | symRightBrace | symLeftParen | symRightParen | symLeftBracket | symRightBracket |
    symDot | symComma | symSemicolon | symPlus | symMinus | symMultiply | symDivide |
    symAnd | symOr | symLessThan | symGreaterThan | symEqual | symTilda)

    */

  val INTEGER_CONSTANT = P(Number).map(AST.IntConst)
  val STRING_CONSTANT = P("\"" ~ CharPred(c => c != '"' && c != '\r' && c != '\n').rep.! ~ "\"").map(AST.StringConst)

  val CLASS_NAME = P(Identifier).map(AST.ClassName)
  val SUBROUTINE_NAME = P(Identifier).map(AST.SubroutineName)
  val VAR_NAME = P(Identifier).map(AST.VarName)
  val TYPE: P[AST.Type] = P(kwdInt | kwdChar | kwdBoolean | CLASS_NAME)

  /* EXPRESSIONS */
  val KEYWORD_CONSTANT: P[AST.KeywordConstant] = P(kwdTrue | kwdFalse | kwdNull | kwdThis)
  val UNARY_OP: P[AST.UnaryOp] = P(symNegate | symTilda)
  val OP: P[AST.BinaryOp] = P(symPlus | symMinus | symMultiply | symDivide | symAnd | symOr | symLessThan | symGreaterThan | symEqual)

  val arrayTerm = P(VAR_NAME ~ symLeftBracket ~/ EXPRESSION ~ symRightBracket).map(AST.ArrayTerm.tupled)
  val parenExprTerm = P(symLeftParen ~/ EXPRESSION ~ symRightParen).map(AST.ParensExprTerm)
  val subCallTerm = P(SUBROUTINE_CALL).map(AST.SubroutineCallTerm)
  val unaryOpTerm = P(UNARY_OP ~ TERM)
  val varNameTerm = P(VAR_NAME).map(AST.VarNameTerm)
  val intConstTerm = P(INTEGER_CONSTANT).map(AST.IntegerConstantTerm)
  val strConstTerm = P(STRING_CONSTANT).map(AST.StringConstantTerm)
  val kwdConstTerm = P(KEYWORD_CONSTANT).map(AST.KeywordConstantTerm)
  val TERM: P[AST.Term] = P(arrayTerm | subCallTerm | parenExprTerm |
    varNameTerm | intConstTerm | strConstTerm | kwdConstTerm
  )

  val EXPRESSION: P[AST.Expression] = P(TERM ~ (OP ~ TERM).rep).map(AST.Expression.tupled)
  val EXPRESSION_LIST = P((EXPRESSION ~ (symComma ~ EXPRESSION).rep) ?).map { res =>
    val exprs = res.toList.flatMap { t => Seq(t._1) ++ t._2 }
    AST.ExpressionList(exprs)
  }

  val classNameOrVarName: P[AST.ClassNameOrVarName] = P(CLASS_NAME | VAR_NAME)
  val subroutineCallNormal = P(SUBROUTINE_NAME ~ symLeftParen ~ EXPRESSION_LIST ~ symRightParen).map(AST.SubroutineCallNormal.tupled)
  val subroutineCallPrefixed = P(classNameOrVarName ~ symDot ~ SUBROUTINE_NAME ~ symLeftParen ~ EXPRESSION_LIST ~ symRightParen).
    map(AST.SubroutineCallPrefixed.tupled)
  val SUBROUTINE_CALL: P[AST.SubroutineCall] = P(subroutineCallNormal | subroutineCallPrefixed)

  /* STATEMENTS */
  val LET_STATEMENT: P[AST.LetStatement] = P(kwdLet ~ VAR_NAME ~ ((symLeftBracket ~ EXPRESSION ~ symRightBracket)?) ~
    symEqual ~ EXPRESSION ~ symSemicolon).map {
    case (varName, maybeIndex, _, expr) => AST.LetStatement(varName, maybeIndex, expr)
  }

  val IF_STATEMENT = P(kwdIf ~ symLeftParen ~ EXPRESSION ~ symRightParen ~ symLeftBrace ~ STATEMENTS ~ symRightBrace ~
    ((kwdElse ~ symLeftBrace ~ STATEMENTS ~ symRightBrace)?)).map(AST.IfStatement.tupled)

  val WHILE_STATEMENT = P(kwdWhile ~ symLeftParen ~ EXPRESSION ~ symRightParen ~
    symLeftBrace ~ STATEMENTS ~ symRightBrace).map(AST.WhileStatement.tupled)

  val DO_STATEMENT = P(kwdDo ~ SUBROUTINE_CALL ~ symSemicolon).map(AST.DoStatement)

  val RETURN_STATEMENT = P(kwdReturn ~ (EXPRESSION?) ~ symSemicolon).map(AST.ReturnStatement)

  val STATEMENT: P[AST.Statement] = P(LET_STATEMENT | IF_STATEMENT | WHILE_STATEMENT | DO_STATEMENT | RETURN_STATEMENT)
  val STATEMENTS: P[Seq[AST.Statement]] = P(STATEMENT.rep)

  /* PROGRAM STRUCTURE */
  val VAR_DEC = P(kwdVar ~ TYPE ~ VAR_NAME ~ (symComma ~ VAR_NAME).rep ~ symSemicolon).map(AST.VarDec.tupled)

  val PARAMETER_LIST = P(((TYPE ~ VAR_NAME) ~ (symComma ~ TYPE ~ VAR_NAME).rep)?).map { res =>
    val params = res.toList.flatMap { t => Seq((t._1, t._2)) ++ t._3 }
    AST.ParameterList(params)
  }

  val fieldType: P[AST.KeywordFieldType] = P(kwdStatic | kwdField)
  val CLASS_VAR_DEC = P(fieldType ~ TYPE ~ VAR_NAME ~ (symComma ~ VAR_NAME).rep ~ symSemicolon).map(AST.ClassVarDec.tupled)

  val SUBROUTINE_BODY = P(symLeftBrace ~ VAR_DEC.rep ~ STATEMENTS ~ symRightBrace).map(AST.SubroutineBody.tupled)

  val subroutineType: P[AST.SubroutineType] = P(kwdConstructor | kwdFunction | kwdMethod)
  val voidOrType: P[AST.VoidOrType] = P(kwdVoid | TYPE)
  val SUBROUTINE_DEC = P(subroutineType ~ voidOrType ~ SUBROUTINE_NAME ~ symLeftParen ~ PARAMETER_LIST ~ symRightParen ~ SUBROUTINE_BODY).map(AST.SubroutineDec.tupled)

  val CLASS_DEC = P(kwdClass ~ CLASS_NAME ~ symLeftBrace ~ CLASS_VAR_DEC.rep ~ SUBROUTINE_DEC.rep ~ symRightBrace).map(AST.ClassDec.tupled)

}
