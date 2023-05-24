package java_cup;

import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/production_part.class */
public abstract class production_part {
    protected String _label;

    public abstract boolean is_action();

    public production_part(String lab) {
        this._label = lab;
    }

    public String label() {
        return this._label;
    }

    public boolean equals(production_part other) {
        if (other == null) {
            return false;
        }
        if (label() != null) {
            return label().equals(other.label());
        }
        return other.label() == null;
    }

    public boolean equals(Object other) {
        if (!(other instanceof production_part)) {
            return false;
        }
        return equals((production_part) other);
    }

    public int hashCode() {
        if (label() == null) {
            return 0;
        }
        return label().hashCode();
    }

    public String toString() {
        if (label() != null) {
            return String.valueOf(label()) + ":";
        }
        return Instruction.argsep;
    }
}
