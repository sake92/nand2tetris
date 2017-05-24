package ba.sake.nand2tetris.compiler

class SymbolTable {
  import SymbolTable._

  // map of all symbols defined in this table
  private var symbols: Map[String, Symbol] = Map()
  private var counters: Map[SymbolKinds.Kind, Int] = Map().withDefaultValue(0)

  def add(name: String, tpe: String, kind: SymbolKinds.Kind): Symbol = {
    val currentCount = counters(kind)
    val newSymbol = Symbol(name, tpe, kind, currentCount)
    symbols += (name -> newSymbol)
    counters = counters.updated(kind, currentCount + 1)
    newSymbol
  }

  def get(symbolName: String): Option[Symbol] = {
    symbols.get(symbolName)
  }

  def varCount(kind: SymbolKinds.Kind): Int = {
    counters(kind)
  }
}

object SymbolTable {

  case class Symbol(name: String, tpe: String, kind: SymbolKinds.Kind, index: Int)

  object SymbolKinds extends Enumeration {
    type Kind = Value
    val STATIC, FIELD, ARG, VAR = Value
  }
}
