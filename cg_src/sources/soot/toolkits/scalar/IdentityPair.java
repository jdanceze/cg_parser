package soot.toolkits.scalar;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/IdentityPair.class */
public class IdentityPair<T, U> {
    protected final T o1;
    protected final U o2;
    protected final int hashCode = computeHashCode();

    public IdentityPair(T o1, U o2) {
        this.o1 = o1;
        this.o2 = o2;
    }

    public int hashCode() {
        return this.hashCode;
    }

    private int computeHashCode() {
        int result = (31 * 1) + System.identityHashCode(this.o1);
        return (31 * result) + System.identityHashCode(this.o2);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        IdentityPair<?, ?> other = (IdentityPair) obj;
        return this.o1 == other.o1 && this.o2 == other.o2;
    }

    public T getO1() {
        return this.o1;
    }

    public U getO2() {
        return this.o2;
    }

    public String toString() {
        return "IdentityPair " + this.o1 + "," + this.o2;
    }
}
