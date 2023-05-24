package soot.util;
/* loaded from: gencallgraphv3.jar:soot/util/NumberedString.class */
public final class NumberedString implements Numberable {
    private final String s;
    private volatile int number;

    public NumberedString(String s) {
        this.s = s;
    }

    @Override // soot.util.Numberable
    public final void setNumber(int number) {
        this.number = number;
    }

    @Override // soot.util.Numberable
    public final int getNumber() {
        return this.number;
    }

    public final String toString() {
        return getString();
    }

    public final String getString() {
        if (this.number == 0) {
            throw new RuntimeException("oops");
        }
        return this.s;
    }

    public int hashCode() {
        int result = (31 * 1) + this.number;
        return (31 * result) + (this.s == null ? 0 : this.s.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        NumberedString other = (NumberedString) obj;
        if (this.number != other.number) {
            return false;
        }
        if (this.s == null) {
            return other.s == null;
        }
        return this.s.equals(other.s);
    }
}
