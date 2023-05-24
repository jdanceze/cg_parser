package soot.jimple.infoflow.util;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/util/ByReferenceBoolean.class */
public class ByReferenceBoolean {
    public boolean value;

    public ByReferenceBoolean() {
        this.value = false;
    }

    public ByReferenceBoolean(boolean initialValue) {
        this.value = initialValue;
    }

    public ByReferenceBoolean and(boolean b) {
        this.value &= b;
        return this;
    }

    public ByReferenceBoolean or(boolean b) {
        this.value |= b;
        return this;
    }

    public ByReferenceBoolean xor(boolean b) {
        this.value ^= b;
        return this;
    }

    public String toString() {
        return String.valueOf(this.value);
    }

    public int hashCode() {
        int result = (31 * 1) + (this.value ? 1231 : 1237);
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ByReferenceBoolean other = (ByReferenceBoolean) obj;
        if (this.value != other.value) {
            return false;
        }
        return true;
    }
}
