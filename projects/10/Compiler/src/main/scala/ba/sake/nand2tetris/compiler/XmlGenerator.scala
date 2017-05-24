package ba.sake.nand2tetris.compiler

import java.io.File
import java.io.PrintWriter
import java.io.Writer
import java.nio.charset.StandardCharsets
import org.apache.commons.lang3.StringEscapeUtils

class XmlGenerator(outWriter: Writer) {

  // I ignored some of keywords and symbols in the parser.. :D
  // so I insert them manually here... :/

  private def write(str: String) = outWriter.write(str)
  private def writeLn(str: String) = outWriter.write(str + "\n")

  def generate(c: AST.ClassDec): Unit = {
    writeLn("<class>")
    handleKeyword("class")
    handleIdentifier(c.className.name)
    handleSymbol("{")
    c.classVarDecs.foreach(handleClassVarDec)
    c.subroutineDecs.foreach(handlesubroutineDec)
    handleSymbol("}")
    writeLn("</class>")
  }

  private def handleClassVarDec(vd: AST.ClassVarDec) = {
    writeLn("<classVarDec>")
    handleKeyword(vd.fieldType) // field,static
    handleType(vd.tpe) // int,boolean,char,ClassName
    handleVarName(vd.varName) // one var name is mandatory
    for (vn <- vd.varNames) {
      handleSymbol(",")
      handleVarName(vn)
    }
    handleSymbol(";")
    writeLn("</classVarDec>")
  }

  private def handlesubroutineDec(sd: AST.SubroutineDec) = {
    writeLn("<subroutineDec>")
    handleKeyword(sd.subroutineType)
    handleVoidOrType(sd.voidOrType)
    handleIdentifier(sd.subroutineName.name)
    handleSymbol("(")
    handleParameterList(sd.parameterList)
    handleSymbol(")")
    handleSubroutineBody(sd.subroutineBody)
    writeLn("</subroutineDec>")
  }

  private def handleParameterList(pl: AST.ParameterList) = {
    writeLn("<parameterList>")
    for (((paramType, paramName), i) <- pl.params.zipWithIndex) {
      handleType(paramType)
      handleVarName(paramName)
      if (i < pl.params.size - 1)
        handleSymbol(",")
    }
    writeLn("</parameterList>")
  }

  private def handleSubroutineBody(sb: AST.SubroutineBody) = {
    writeLn("<subroutineBody>")
    handleSymbol("{")
    sb.varDecs.foreach(handleVarDec)
    handleStatements(sb.statements)
    handleSymbol("}")
    writeLn("</subroutineBody>")
  }

  private def handleType(tpe: AST.Type) = tpe match {
    case AST.ClassName(name) => handleIdentifier(name)
    case kwdType: AST.KeywordType => handleKeyword(kwdType)
  }

  private def handleVarDec(vd: AST.VarDec) = {
    writeLn("<varDec>")
    handleKeyword("var")
    handleType(vd.tpe)
    handleVarName(vd.varName)
    for (vn <- vd.varNames) {
      handleSymbol(",")
      handleVarName(vn)
    }
    handleSymbol(";")
    writeLn("</varDec>")
  }

  private def handleStatement(st: AST.Statement): Unit = st match {
    case s: AST.LetStatement => handleLetStatement(s)
    case s: AST.IfStatement => handleIfStatement(s)
    case s: AST.WhileStatement => handleWhileStatement(s)
    case s: AST.DoStatement => handleDoStatement(s)
    case s: AST.ReturnStatement => handleReturnStatement(s)
  }

  private def handleStatements(statements: Seq[AST.Statement]): Unit = {
    writeLn("<statements>")
    statements.foreach(handleStatement)
    writeLn("</statements>")
  }

  private def handleLetStatement(s: AST.LetStatement) = {
    writeLn("<letStatement>")
    handleKeyword("let")
    handleVarName(s.varName)
    s.maybeIndexExpr match {
      case Some(indexExpr) => {
        handleSymbol("[")
        handleExpression(indexExpr)
        handleSymbol("]")
      }
      case None =>
    }
    handleSymbol("=")
    handleExpression(s.expr)
    handleSymbol(";")
    writeLn("</letStatement>")
  }

  private def handleIfStatement(s: AST.IfStatement) = {
    writeLn("<ifStatement>")
    handleKeyword("if")
    handleSymbol("(")
    handleExpression(s.condition)
    handleSymbol(")")
    handleSymbol("{")
    handleStatements(s.ifTrueStatements)
    handleSymbol("}")
    s.ifFalseStatements match {
      case Some(statements) => {
        handleKeyword("else")
        handleSymbol("{")
        handleStatements(statements)
        handleSymbol("}")
      }
      case None =>
    }
    writeLn("</ifStatement>")
  }

  private def handleWhileStatement(s: AST.WhileStatement) = {
    writeLn("<whileStatement>")
    handleKeyword("while")
    handleSymbol("(")
    handleExpression(s.condition)
    handleSymbol(")")
    handleSymbol("{")
    handleStatements(s.statements)
    handleSymbol("}")
    writeLn("</whileStatement>")
  }

  private def handleDoStatement(s: AST.DoStatement) = {
    writeLn("<doStatement>")
    handleKeyword("do")
    handleSubroutineCall(s.subroutineCall)
    handleSymbol(";")
    writeLn("</doStatement>")
  }

  private def handleReturnStatement(s: AST.ReturnStatement) = {
    writeLn("<returnStatement>")
    handleKeyword("return")
    s.expr.foreach(handleExpression)
    handleSymbol(";")
    writeLn("</returnStatement>")
  }

  /* expression */
  private def handleExpression(expr: AST.Expression): Unit = {
    writeLn("<expression>")
    handleTerm(expr.term)
    for ((op, term) <- expr.opTerms) {
      handleOp(op)
      handleTerm(term)
    }
    writeLn("</expression>")
  }
  private def handleExpressionList(exprs: Seq[AST.Expression]): Unit = {
    writeLn("<expressionList>")
    for ((e, i) <- exprs.zipWithIndex) {
      handleExpression(e)
      if (i < exprs.size - 1)
        handleSymbol(",")
    }
    writeLn("</expressionList>")
  }

  private def handleTerm(term: AST.Term): Unit = {
    writeLn("<term>")
    term match {
      case AST.IntegerConstantTerm(AST.IntConst(int)) => {
        writeLn(s"<integerConstant> ${int} </integerConstant>")
      }
      case AST.StringConstantTerm(AST.StringConst(str)) => {
        val strEscaped = escape(str) // it could contain any character, except " or \n
        writeLn(s"<stringConstant> ${strEscaped} </stringConstant>")
      }
      case AST.KeywordConstantTerm(kwd) => {
        handleKeyword(kwd)
      }
      case AST.VarNameTerm(varName) => {
        handleVarName(varName)
      }
      case AST.ArrayTerm(arrName, expr) => {
        handleVarName(arrName)
        handleSymbol("[")
        handleExpression(expr)
        handleSymbol("]")
      }
      case AST.SubroutineCallTerm(subroutineCall) => {
        handleSubroutineCall(subroutineCall)
      }
      case AST.ParensExprTerm(expr) => {
        handleSymbol("(")
        handleExpression(expr)
        handleSymbol(")")
      }
      case AST.UnaryOpTerm(unaryOp, t) => {
        handleSymbol(unaryOp)
        handleTerm(t)
      }
    }
    writeLn("</term>")
  }

  private def handleSubroutineCall(subroutineCall: AST.SubroutineCall) = subroutineCall match {
    case AST.SubroutineCallNormal(AST.SubroutineName(subName), expressions) => {
      handleIdentifier(subName)
      handleSymbol("(")
      handleExpressionList(expressions.expressions)
      handleSymbol(")")
    }
    case AST.SubroutineCallPrefixed(classOrVarName, AST.SubroutineName(subName), expressions) => {
      handleClassNameOrVarName(classOrVarName)
      handleSymbol(".")
      handleIdentifier(subName)
      handleSymbol("(")
      handleExpressionList(expressions.expressions)
      handleSymbol(")")
    }
  }

  private def handleOp(op: AST.BinaryOp) = {
    handleSymbol(op)
  }

  /* keyword */
  private def handleKeyword(kwd: AST.KeywordTerminal): Unit =
    handleKeyword(kwd.raw)

  private def handleKeyword(kwd: String) =
    writeLn(s"<keyword> ${kwd} </keyword>")

  /* symbol */
  private def handleSymbol(sym: AST.SymbolTerminal): Unit =
    handleSymbol(sym.raw)
  private def handleSymbol(sym: String): Unit = {
    val symEscaped = escape(sym)
    writeLn(s"<symbol> ${symEscaped} </symbol>")
  }

  private def handleIdentifier(ident: String) =
    writeLn(s"<identifier> ${ident} </identifier>")

  private def handleVarName(varName: AST.VarName) =
    handleIdentifier(varName.name)

  private def handleVoidOrType(vOrT: AST.VoidOrType) = vOrT match {
    case AST.Void => handleKeyword("void")
    case t: AST.Type => handleType(t)
  }

  private def handleClassNameOrVarName(classNameOrVarName: AST.ClassNameOrVarName) = classNameOrVarName match {
    case AST.ClassName(name) => handleIdentifier(name)
    case AST.VarName(name) => handleIdentifier(name)
  }

  private def escape(raw: String): String = {
    StringEscapeUtils.escapeXml11(raw)
  }

  def close(): Unit = {
    outWriter.close()
  }
}

object XmlGenerator {

  // print to file
  def apply(outputFile: File): XmlGenerator = {
    val outWriter = new PrintWriter(outputFile, StandardCharsets.UTF_8.name)
    new XmlGenerator(outWriter)
  }

  // print to a Writer, e.g. a string for debugging
  def apply(outWriter: Writer): XmlGenerator = {
    new XmlGenerator(outWriter)
  }

  // print to console
  def apply(): XmlGenerator = {
    val outWriter = new PrintWriter(System.out)
    new XmlGenerator(outWriter)
  }
}
