package java_cup.runtime;
/* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/runtime/SymbolFactory.class */
public interface SymbolFactory {
    Symbol newSymbol(String str, int i, Symbol symbol, Symbol symbol2, Object obj);

    Symbol newSymbol(String str, int i, Symbol symbol, Symbol symbol2);

    Symbol newSymbol(String str, int i, Symbol symbol, Object obj);

    Symbol newSymbol(String str, int i, Object obj);

    Symbol newSymbol(String str, int i);

    Symbol startSymbol(String str, int i, int i2);
}
