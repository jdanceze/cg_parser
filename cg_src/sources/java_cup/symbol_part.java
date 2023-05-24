package java_cup;
/* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/symbol_part.class */
public class symbol_part extends production_part {
    protected symbol _the_symbol;

    public symbol_part(symbol sym, String lab) throws internal_error {
        super(lab);
        if (sym == null) {
            throw new internal_error("Attempt to construct a symbol_part with a null symbol");
        }
        this._the_symbol = sym;
    }

    public symbol_part(symbol sym) throws internal_error {
        this(sym, null);
    }

    public symbol the_symbol() {
        return this._the_symbol;
    }

    @Override // java_cup.production_part
    public boolean is_action() {
        return false;
    }

    public boolean equals(symbol_part other) {
        return other != null && super.equals((production_part) other) && the_symbol().equals(other.the_symbol());
    }

    @Override // java_cup.production_part
    public boolean equals(Object other) {
        if (!(other instanceof symbol_part)) {
            return false;
        }
        return equals((symbol_part) other);
    }

    @Override // java_cup.production_part
    public int hashCode() {
        return super.hashCode() ^ (the_symbol() == null ? 0 : the_symbol().hashCode());
    }

    @Override // java_cup.production_part
    public String toString() {
        if (the_symbol() != null) {
            return String.valueOf(super.toString()) + the_symbol();
        }
        return String.valueOf(super.toString()) + "$$MISSING-SYMBOL$$";
    }
}
