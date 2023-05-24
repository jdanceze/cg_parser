package soot.jimple.infoflow.collect;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/collect/IdentityWrapper.class */
class IdentityWrapper<E> {
    private final E contents;

    public IdentityWrapper(E abs) {
        this.contents = abs;
    }

    public E getContents() {
        return this.contents;
    }

    public String toString() {
        return this.contents.toString();
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        return getClass() == other.getClass() && this.contents == ((IdentityWrapper) other).contents;
    }

    public int hashCode() {
        return System.identityHashCode(this.contents);
    }
}
