package soot.jimple.toolkits.pointer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import soot.PointsToSet;
import soot.Scene;
import soot.SootField;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.sets.HashPointsToSet;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/CodeBlockRWSet.class */
public class CodeBlockRWSet extends MethodRWSet {
    @Override // soot.jimple.toolkits.pointer.MethodRWSet
    public String toString() {
        StringBuilder ret = new StringBuilder();
        boolean empty = true;
        if (this.fields != null) {
            for (Map.Entry<Object, PointsToSet> e : this.fields.entrySet()) {
                ret.append("[Field: ").append(e.getKey()).append(' ');
                PointsToSet baseObj = e.getValue();
                if (baseObj instanceof PointsToSetInternal) {
                    int baseSize = ((PointsToSetInternal) baseObj).size();
                    ret.append(baseSize).append(baseSize == 1 ? " Node]\n" : " Nodes]\n");
                } else {
                    ret.append(baseObj).append("]\n");
                }
                empty = false;
            }
        }
        if (this.globals != null) {
            for (SootField global : this.globals) {
                ret.append("[Global: ").append(global).append("]\n");
                empty = false;
            }
        }
        if (empty) {
            ret.append("[emptyset]\n");
        }
        return ret.toString();
    }

    @Override // soot.jimple.toolkits.pointer.MethodRWSet, soot.jimple.toolkits.pointer.RWSet
    public boolean union(RWSet other) {
        if (other == null || this.isFull) {
            return false;
        }
        boolean ret = false;
        if (other instanceof MethodRWSet) {
            MethodRWSet o = (MethodRWSet) other;
            if (o.getCallsNative()) {
                ret = (!getCallsNative()) | false;
                setCallsNative();
            }
            if (o.isFull) {
                boolean z = (!this.isFull) | ret;
                this.isFull = true;
                throw new RuntimeException("attempt to add full set " + o + " into " + this);
            }
            if (o.globals != null) {
                if (this.globals == null) {
                    this.globals = new HashSet();
                }
                ret = this.globals.addAll(o.globals) | ret;
                if (this.globals.size() > Integer.MAX_VALUE) {
                    this.globals = null;
                    this.isFull = true;
                    throw new RuntimeException("attempt to add full set " + o + " into " + this);
                }
            }
            if (o.fields != null) {
                for (Object field : o.fields.keySet()) {
                    ret = addFieldRef(o.getBaseForField(field), field) | ret;
                }
            }
        } else if (other instanceof StmtRWSet) {
            StmtRWSet oth = (StmtRWSet) other;
            if (oth.base != null) {
                ret = addFieldRef(oth.base, oth.field) | false;
            } else if (oth.field != null) {
                ret = addGlobal((SootField) oth.field) | false;
            }
        } else if (other instanceof SiteRWSet) {
            Iterator<RWSet> it = ((SiteRWSet) other).sets.iterator();
            while (it.hasNext()) {
                RWSet set = it.next();
                union(set);
            }
        }
        if (!getCallsNative() && other.getCallsNative()) {
            setCallsNative();
            return true;
        }
        return ret;
    }

    public boolean containsField(Object field) {
        return this.fields != null && this.fields.containsKey(field);
    }

    public CodeBlockRWSet intersection(MethodRWSet other) {
        CodeBlockRWSet ret = new CodeBlockRWSet();
        if (this.isFull) {
            return ret;
        }
        if (this.globals != null && other.globals != null && !this.globals.isEmpty() && !other.globals.isEmpty()) {
            for (SootField sg : other.globals) {
                if (this.globals.contains(sg)) {
                    ret.addGlobal(sg);
                }
            }
        }
        if (this.fields != null && other.fields != null && !this.fields.isEmpty() && !other.fields.isEmpty()) {
            for (Object field : other.fields.keySet()) {
                if (this.fields.containsKey(field)) {
                    PointsToSet pts1 = getBaseForField(field);
                    PointsToSet pts2 = other.getBaseForField(field);
                    if (pts1 instanceof FullObjectSet) {
                        ret.addFieldRef(pts2, field);
                    } else if (pts2 instanceof FullObjectSet) {
                        ret.addFieldRef(pts1, field);
                    } else if (pts1.hasNonEmptyIntersection(pts2) && (pts1 instanceof PointsToSetInternal) && (pts2 instanceof PointsToSetInternal)) {
                        PointsToSetInternal pti1 = (PointsToSetInternal) pts1;
                        final PointsToSetInternal pti2 = (PointsToSetInternal) pts2;
                        final PointsToSetInternal newpti = new HashPointsToSet(pti1.getType(), (PAG) Scene.v().getPointsToAnalysis());
                        pti1.forall(new P2SetVisitor() { // from class: soot.jimple.toolkits.pointer.CodeBlockRWSet.1
                            @Override // soot.jimple.spark.sets.P2SetVisitor
                            public void visit(Node n) {
                                if (pti2.contains(n)) {
                                    newpti.add(n);
                                }
                            }
                        });
                        ret.addFieldRef(newpti, field);
                    }
                }
            }
        }
        return ret;
    }

    @Override // soot.jimple.toolkits.pointer.MethodRWSet, soot.jimple.toolkits.pointer.RWSet
    public boolean addFieldRef(PointsToSet otherBase, Object field) {
        if (this.fields == null) {
            this.fields = new HashMap();
        }
        PointsToSet base = getBaseForField(field);
        if (base instanceof FullObjectSet) {
            return false;
        }
        if (otherBase instanceof FullObjectSet) {
            this.fields.put(field, otherBase);
            return true;
        } else if (otherBase.equals(base)) {
            return false;
        } else {
            if (base == null) {
                base = new HashPointsToSet(((PointsToSetInternal) otherBase).getType(), (PAG) Scene.v().getPointsToAnalysis());
                this.fields.put(field, base);
            }
            return ((PointsToSetInternal) base).addAll((PointsToSetInternal) otherBase, null) | false;
        }
    }
}
