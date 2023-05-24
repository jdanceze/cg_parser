package java_cup.runtime;
/* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/runtime/DefaultSymbolFactory.class */
public class DefaultSymbolFactory implements SymbolFactory {
    @Override // java_cup.runtime.SymbolFactory
    public Symbol newSymbol(String name, int id, Symbol left, Symbol right, Object value) {
        return new Symbol(id, left, right, value);
    }

    @Override // java_cup.runtime.SymbolFactory
    public Symbol newSymbol(String name, int id, Symbol left, Object value) {
        return new Symbol(id, left, value);
    }

    @Override // java_cup.runtime.SymbolFactory
    public Symbol newSymbol(String name, int id, Symbol left, Symbol right) {
        return new Symbol(id, left, right);
    }

    public Symbol newSymbol(String name, int id, int left, int right, Object value) {
        return new Symbol(id, left, right, value);
    }

    public Symbol newSymbol(String name, int id, int left, int right) {
        return new Symbol(id, left, right);
    }

    @Override // java_cup.runtime.SymbolFactory
    public Symbol startSymbol(String name, int id, int state) {
        return new Symbol(id, state);
    }

    @Override // java_cup.runtime.SymbolFactory
    public Symbol newSymbol(String name, int id) {
        return new Symbol(id);
    }

    @Override // java_cup.runtime.SymbolFactory
    public Symbol newSymbol(String name, int id, Object value) {
        return new Symbol(id, value);
    }
}
