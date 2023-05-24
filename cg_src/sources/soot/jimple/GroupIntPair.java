package soot.jimple;
/* loaded from: gencallgraphv3.jar:soot/jimple/GroupIntPair.class */
public class GroupIntPair {
    public final Object group;
    public final int x;

    public GroupIntPair(Object group, int x) {
        this.group = group;
        this.x = x;
    }

    public boolean equals(Object other) {
        if (other instanceof GroupIntPair) {
            GroupIntPair cast = (GroupIntPair) other;
            return cast.group.equals(this.group) && cast.x == this.x;
        }
        return false;
    }

    public int hashCode() {
        return this.group.hashCode() + (1013 * this.x);
    }

    public String toString() {
        return this.group + ": " + this.x;
    }
}
