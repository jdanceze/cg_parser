package soot.jimple.toolkits.pointer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import soot.PointsToSet;
import soot.Type;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/MemoryEfficientRasUnion.class */
public class MemoryEfficientRasUnion extends Union {
    HashSet<PointsToSet> subsets;

    @Override // soot.PointsToSet
    public boolean isEmpty() {
        if (this.subsets != null) {
            Iterator<PointsToSet> it = this.subsets.iterator();
            while (it.hasNext()) {
                PointsToSet subset = it.next();
                if (!subset.isEmpty()) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    @Override // soot.PointsToSet
    public boolean hasNonEmptyIntersection(PointsToSet other) {
        if (this.subsets == null) {
            return true;
        }
        Iterator<PointsToSet> it = this.subsets.iterator();
        while (it.hasNext()) {
            PointsToSet subset = it.next();
            if (other instanceof Union) {
                if (other.hasNonEmptyIntersection(subset)) {
                    return true;
                }
            } else if (subset.hasNonEmptyIntersection(other)) {
                return true;
            }
        }
        return false;
    }

    @Override // soot.jimple.toolkits.pointer.Union
    public boolean addAll(PointsToSet s) {
        if (this.subsets == null) {
            this.subsets = new HashSet<>();
        }
        if (s instanceof MemoryEfficientRasUnion) {
            MemoryEfficientRasUnion meru = (MemoryEfficientRasUnion) s;
            if (meru.subsets == null || this.subsets.containsAll(meru.subsets)) {
                return false;
            }
            return this.subsets.addAll(meru.subsets);
        }
        return this.subsets.add(s);
    }

    public Object clone() {
        MemoryEfficientRasUnion ret = new MemoryEfficientRasUnion();
        ret.addAll(this);
        return ret;
    }

    @Override // soot.PointsToSet
    public Set<Type> possibleTypes() {
        if (this.subsets == null) {
            return Collections.emptySet();
        }
        HashSet<Type> ret = new HashSet<>();
        Iterator<PointsToSet> it = this.subsets.iterator();
        while (it.hasNext()) {
            PointsToSet subset = it.next();
            ret.addAll(subset.possibleTypes());
        }
        return ret;
    }

    public int hashCode() {
        int result = (31 * 1) + (this.subsets == null ? 0 : this.subsets.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MemoryEfficientRasUnion other = (MemoryEfficientRasUnion) obj;
        if (this.subsets == null) {
            if (other.subsets != null) {
                return false;
            }
            return true;
        } else if (!this.subsets.equals(other.subsets)) {
            return false;
        } else {
            return true;
        }
    }

    public String toString() {
        return this.subsets == null ? "[]" : this.subsets.toString();
    }
}
