package polyglot.util;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/IdentityKey.class */
public class IdentityKey {
    Object obj;

    public IdentityKey(Object obj) {
        this.obj = obj;
    }

    public Object object() {
        return this.obj;
    }

    public int hashCode() {
        return System.identityHashCode(this.obj);
    }

    public boolean equals(Object other) {
        return (other instanceof IdentityKey) && ((IdentityKey) other).obj == this.obj;
    }

    public String toString() {
        return new StringBuffer().append("Id(").append(this.obj).append(")").toString();
    }
}
