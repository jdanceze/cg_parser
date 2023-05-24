package soot.jimple.spark.ondemand.genericutil;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/genericutil/UnorderedPair.class */
public class UnorderedPair<U, V> {
    public U o1;
    public V o2;

    public UnorderedPair(U o1, V o2) {
        this.o1 = o1;
        this.o2 = o2;
    }

    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == UnorderedPair.class) {
            UnorderedPair u = (UnorderedPair) obj;
            if (u.o1.equals(this.o1) && u.o2.equals(this.o2)) {
                return true;
            }
            return u.o1.equals(this.o2) && u.o2.equals(this.o1);
        }
        return false;
    }

    public int hashCode() {
        return this.o1.hashCode() + this.o2.hashCode();
    }

    public String toString() {
        return "{" + this.o1.toString() + ", " + this.o2.toString() + "}";
    }
}
