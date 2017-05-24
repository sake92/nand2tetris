package ba.sake.nand2tetris.compiler

import java.io.File
import java.io.PrintWriter
import java.io.Writer
import java.nio.charset.StandardCharsets

class VMGenerator(outWriter: Writer) {
  import VMGenerator._

  // writing to file
  private val vmWriter = VMWriter(outWriter)

  // state of generator
  private var currentClass: String = _
  private var currentSubroutine: String = _

  private val currentClassST = new SymbolTable()
  private var currentSubroutineST: SymbolTable = _

  private var labelCounter = 0

  def generate(c: AST.ClassDec): Unit = {
    currentClass = c.className.name
    labelCounter = 0
    c.classVarDecs.foreach(handleClassVarDec)
    c.subroutineDecs.foreach(handlesubroutineDec)
  }

  private def handleClassVarDec(vd: AST.ClassVarDec) = {
    val fieldKind = vd.fieldType match {
      case AST.Static => SymbolTable.SymbolKinds.STATIC
      case AST.Field => SymbolTable.SymbolKinds.FIELD
    }
    val fields = vd.varName +: vd.varNames
    fields.foreach { f =>
      currentClassST.add(f.name, vd.tpe.name, fieldKind) // add to class' ST
    }
  }

  private def handlesubroutineDec(sd: AST.SubroutineDec) = {
    setCurrentSubroutine(sd.subroutineName.name)
    // instantiate new symbol table for this function/method
    // and populate it
    currentSubroutineST = new SymbolTable()
    if (sd.subroutineType == AST.Method) {
      currentSubroutineST.add("this", currentClass, SymbolTable.SymbolKinds.ARG) // this == arg[0]
    }
    handleParameterList(sd.parameterList)

    handleSubroutineBody(sd)
  }

  // only UPDATE THE SYMBOL TABLE!
  // arguments are PUSHED BY THE CALLER !
  private def handleParameterList(pl: AST.ParameterList) = {
    pl.params.foreach {
      case (tpe, AST.VarName(paramName)) =>
        currentSubroutineST.add(paramName, tpe.name, SymbolTable.SymbolKinds.ARG)
    }
  }

  private def handleSubroutineBody(sd: AST.SubroutineDec) = {
    val sb = sd.subroutineBody
    sb.varDecs.foreach(handleVarDec)
    // the VM will push the LOCAL VARIABLES for us, initialized to zeros !!! :)

    val varsCount = currentSubroutineST.varCount(SymbolTable.SymbolKinds.VAR)
    vmWriter.writeFunction(currentSubroutine, varsCount)

    // if it's a method, the first argument will be "this" pointer of current OBJECT
    sd.subroutineType match {
      case AST.Method => {
        vmWriter.writePush(VMWriter.SegmentTypes.ARG, 0)
        vmWriter.writePop(VMWriter.SegmentTypes.POINTER, 0)
      }
      case AST.Constructor => {
        // must ALLOCATE MEMORY for object (fields.size much.. :D)
        val numFields = currentClassST.varCount(SymbolTable.SymbolKinds.FIELD)
        vmWriter.writePush(VMWriter.SegmentTypes.CONST, numFields)
        vmWriter.writeCall("Memory.alloc", 1)
        vmWriter.writePop(VMWriter.SegmentTypes.POINTER, 0) // this = ADR[MemAlloc]
      }
      case _ => // proceed normally if a function
    }

    handleStatements(sb.statements)
  }

  // only UPDATE THE SYMBOL TABLE!
  private def handleVarDec(vd: AST.VarDec) = {
    val vars = vd.varName +: vd.varNames
    vars.foreach { v =>
      currentSubroutineST.add(v.name, vd.tpe.name, SymbolTable.SymbolKinds.VAR)
    }
  }

  private def handleStatement(st: AST.Statement): Unit = st match {
    case s: AST.LetStatement => handleLetStatement(s)
    case s: AST.IfStatement => handleIfStatement(s)
    case s: AST.WhileStatement => handleWhileStatement(s)
    case s: AST.DoStatement => handleDoStatement(s)
    case s: AST.ReturnStatement => handleReturnStatement(s)
  }

  private def handleStatements(statements: Seq[AST.Statement]): Unit = {
    statements.foreach(handleStatement)
  }

  private def handleLetStatement(s: AST.LetStatement) = {
    val varName = s.varName.name
    val sym = getSymbol(varName).getOrElse(err(s"Symbol '${varName}' not defined"))
    s.maybeIndexExpr match {
      case Some(indexExpr) =>
        /* 1. calculate array element address */
        // push arrName and index, and ADD them
        // arr[index] => arr + index
        vmWriter.writePush(symKind2VMSegment(sym.kind), sym.index)
        handleExpression(indexExpr)
        vmWriter.writeArithmetic(VMWriter.ArithmeticInstructions.ADD)

        /* 2. calculate RESULT of expression */
        handleExpression(s.expr) // push result on stack
        vmWriter.writePop(VMWriter.SegmentTypes.TEMP, 0) // remember RESULT in temp

        /* 3. write back to array element */
        vmWriter.writePop(VMWriter.SegmentTypes.POINTER, 1) // pop (arr + index) into "that"
        vmWriter.writePush(VMWriter.SegmentTypes.TEMP, 0) // pop result value into *(base+index)
        vmWriter.writePop(VMWriter.SegmentTypes.THAT, 0) // THAT IS USED for ARRAY, THIS for OBJECTs
      case None =>
        handleExpression(s.expr) // push result on stack
        vmWriter.writePop(symKind2VMSegment(sym.kind), sym.index) // pop into variable
    }
  }

  private def handleIfStatement(s: AST.IfStatement) = {
    val labelIfTrue = newLabel("IF_TRUE")
    val labelIfFalse = newLabel("IF_FALSE")

    handleExpression(s.condition)
    vmWriter.writeArithmetic(VMWriter.ArithmeticInstructions.NOT)
    vmWriter.writeIf(labelIfFalse)

    // IF THEN    
    handleStatements(s.ifTrueStatements)
    vmWriter.writeGoto(labelIfTrue) // goto end..
    // ELSE    
    vmWriter.writeLabel(labelIfFalse)
    s.ifFalseStatements.foreach(handleStatements)

    vmWriter.writeLabel(labelIfTrue) // end
  }

  private def handleWhileStatement(s: AST.WhileStatement) = {
    val labelWhileStart = newLabel("WHILE_START")
    val labelWhileEnd = newLabel("WHILE_END")

    vmWriter.writeLabel(labelWhileStart)
    handleExpression(s.condition)
    vmWriter.writeArithmetic(VMWriter.ArithmeticInstructions.NOT)
    vmWriter.writeIf(labelWhileEnd)

    handleStatements(s.statements) // WHILE statements
    vmWriter.writeGoto(labelWhileStart) // goto begin and check condition again
    vmWriter.writeLabel(labelWhileEnd)
  }

  private def handleDoStatement(s: AST.DoStatement) = {
    handleSubroutineCall(s.subroutineCall)
    vmWriter.writePop(VMWriter.SegmentTypes.TEMP, 0) // convention, POP void to temp, throw away..
  }

  private def handleReturnStatement(s: AST.ReturnStatement) = {
    s.expr match {
      case Some(e) => handleExpression(e)
      case None => vmWriter.writePush(VMWriter.SegmentTypes.CONST, 0) // convention, void returns 0
    }
    vmWriter.writeReturn()
  }

  /* expression */
  private def handleExpression(expr: AST.Expression): Unit = {
    handleTerm(expr.term)
    for ((op, term) <- expr.opTerms) {
      handleTerm(term)
      handleOp(op)
    }
  }

  private def handleExpressionList(exprs: Seq[AST.Expression]): Unit = {
    exprs.foreach(handleExpression)
  }

  private def handleTerm(term: AST.Term): Unit = {
    term match {
      case AST.IntegerConstantTerm(AST.IntConst(int)) => {
        vmWriter.writePush(VMWriter.SegmentTypes.CONST, int)
      }
      case AST.StringConstantTerm(AST.StringConst(str)) => {
        vmWriter.writePush(VMWriter.SegmentTypes.CONST, str.length)
        vmWriter.writeCall("String.new", 1)
        str.foreach { c =>
          vmWriter.writePush(VMWriter.SegmentTypes.CONST, c.toInt)
          vmWriter.writeCall("String.appendChar", 2)
        }
      }
      case AST.KeywordConstantTerm(kwd) => kwd match {
        case AST.TrueConstant => {
          vmWriter.writePush(VMWriter.SegmentTypes.CONST, 0)
          vmWriter.writeArithmetic(VMWriter.ArithmeticInstructions.NOT)
        }
        case AST.FalseConstant => vmWriter.writePush(VMWriter.SegmentTypes.CONST, 0)
        case AST.NullConstant => vmWriter.writePush(VMWriter.SegmentTypes.CONST, 0)
        case AST.ThisConstant => vmWriter.writePush(VMWriter.SegmentTypes.POINTER, 0)
      }
      case AST.VarNameTerm(varName) => {
        val sym = getSymbol(varName.name).getOrElse(err(s"Symbol '${varName.name}' not defined"))
        vmWriter.writePush(symKind2VMSegment(sym.kind), sym.index)
      }
      case AST.ArrayTerm(arrName, expr) => {
        val sym = getSymbol(arrName.name).getOrElse(err(s"Symbol '${arrName.name}' not defined"))
        vmWriter.writePush(symKind2VMSegment(sym.kind), sym.index)
        handleExpression(expr)
        vmWriter.writeArithmetic(VMWriter.ArithmeticInstructions.ADD)
        vmWriter.writePop(VMWriter.SegmentTypes.POINTER, 1)
        vmWriter.writePush(VMWriter.SegmentTypes.THAT, 0)
      }
      case AST.SubroutineCallTerm(subroutineCall) => {
        handleSubroutineCall(subroutineCall)
      }
      case AST.ParensExprTerm(expr) => {
        handleExpression(expr)
      }
      case AST.UnaryOpTerm(unaryOp, t) => {
        handleTerm(t)
        unaryOp match {
          case AST.Negate => vmWriter.writeArithmetic(VMWriter.ArithmeticInstructions.NEG)
          case AST.Invert => vmWriter.writeArithmetic(VMWriter.ArithmeticInstructions.NOT)
        }
      }
    }
  }

  private def getSymbol(name: String): Option[SymbolTable.Symbol] =
    currentSubroutineST.get(name).orElse(currentClassST.get(name))

  private def handleSubroutineCall(subroutineCall: AST.SubroutineCall) = subroutineCall match {
    // this is a call of METHOD IN THIS CLASS
    // static functions should have FULL CLASS NAME! e.g. Main.bla() !!!
    case AST.SubroutineCallNormal(AST.SubroutineName(subName), expressions) => {
      vmWriter.writePush(VMWriter.SegmentTypes.POINTER, 0) // push "this"
      handleExpressionList(expressions.expressions)
      val fullSubName = currentClass + "." + subName
      vmWriter.writeCall(fullSubName, expressions.expressions.length + 1)
    }
    case AST.SubroutineCallPrefixed(classOrVarName, AST.SubroutineName(subName), expressions) => {
      getSymbol(classOrVarName.name) match {
        case Some(obj) => {
          // push whatever "this" is.. !?
          vmWriter.writePush(symKind2VMSegment(obj.kind), obj.index)
          handleExpressionList(expressions.expressions)
          val fullMethodName = obj.tpe + "." + subName
          vmWriter.writeCall(fullMethodName, expressions.expressions.length + 1)
        }
        case None => { // function
          handleExpressionList(expressions.expressions)
          val fullFunctionName = classOrVarName.name + "." + subName
          vmWriter.writeCall(fullFunctionName, expressions.expressions.length)
        }
      }
    }
  }

  private def handleOp(op: AST.BinaryOp) = {
    import VMWriter.ArithmeticInstructions._
    op match {
      case AST.Plus => vmWriter.writeArithmetic(ADD)
      case AST.Minus => vmWriter.writeArithmetic(SUB)
      case AST.Multiply => vmWriter.writeCall("Math.multiply", 2) // * and / are OS calls... 
      case AST.Divide => vmWriter.writeCall("Math.divide", 2)
      case AST.And => vmWriter.writeArithmetic(AND)
      case AST.Or => vmWriter.writeArithmetic(OR)
      case AST.LessThan => vmWriter.writeArithmetic(LT)
      case AST.GreaterThan => vmWriter.writeArithmetic(GT)
      case AST.EqualTo => vmWriter.writeArithmetic(EQ)
    }
  }

  // translates SYMBOL KIND to VM SEGMENT type, 1:1 ... :)
  private def symKind2VMSegment(kind: SymbolTable.SymbolKinds.Kind): VMWriter.SegmentTypes.Type = {
    import SymbolTable.SymbolKinds._
    kind match {
      case STATIC => VMWriter.SegmentTypes.STATIC
      case FIELD => VMWriter.SegmentTypes.THIS
      case ARG => VMWriter.SegmentTypes.ARG
      case VAR => VMWriter.SegmentTypes.LOCAL
    }
  }

  private def setCurrentSubroutine(subName: String) =
    currentSubroutine = currentClass + "." + subName

  /**
   * generates a globally unique label. <br>
   * Reset on every new class..
   */
  private def newLabel(prefix: String) = {
    labelCounter += 1
    currentSubroutine + "_" + prefix + "_" + labelCounter
  }

  def close(): Unit = {
    outWriter.close()
  }
}

object VMGenerator {

  // print to file
  def apply(outputFile: File): VMGenerator = {
    val outWriter = new PrintWriter(outputFile, StandardCharsets.UTF_8.name)
    new VMGenerator(outWriter)
  }

  // print to a Writer, e.g. a string for debugging
  def apply(outWriter: Writer): VMGenerator = {
    new VMGenerator(outWriter)
  }

  // print to console
  def apply(): VMGenerator = {
    val outWriter = new PrintWriter(System.out)
    new VMGenerator(outWriter)
  }

  def err(msg: String) = {
    throw new RuntimeException(msg)
  }
}
