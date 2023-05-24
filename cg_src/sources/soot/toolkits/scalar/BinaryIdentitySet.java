package soot.toolkits.scalar;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/BinaryIdentitySet.class */
public class BinaryIdentitySet<T> {
    protected final T o1;
    protected final T o2;
    protected final int hashCode = computeHashCode();

    public BinaryIdentitySet(T o1, T o2) {
        this.o1 = o1;
        this.o2 = o2;
    }

    public int hashCode() {
        return this.hashCode;
    }

    private int computeHashCode() {
        int result = 1 + System.identityHashCode(this.o1);
        return result + System.identityHashCode(this.o2);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BinaryIdentitySet<?> other = (BinaryIdentitySet) obj;
        if (this.o1 == other.o1 || this.o1 == other.o2) {
            return this.o2 == other.o2 || this.o2 == other.o1;
        }
        return false;
    }

    public T getO1() {
        return this.o1;
    }

    public T getO2() {
        return this.o2;
    }

    public String toString() {
        return "IdentityPair " + this.o1 + "," + this.o2;
    }
}
