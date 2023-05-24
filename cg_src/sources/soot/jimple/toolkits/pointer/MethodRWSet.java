package soot.jimple.toolkits.pointer;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.G;
import soot.PointsToSet;
import soot.SootField;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/MethodRWSet.class */
public class MethodRWSet extends RWSet {
    private static final Logger logger = LoggerFactory.getLogger(MethodRWSet.class);
    public static final int MAX_SIZE = Integer.MAX_VALUE;
    public Set<SootField> globals;
    public Map<Object, PointsToSet> fields;
    protected boolean callsNative = false;
    protected boolean isFull = false;

    public String toString() {
        StringBuilder ret = new StringBuilder();
        boolean empty = true;
        if (this.fields != null) {
            for (Map.Entry<Object, PointsToSet> e : this.fields.entrySet()) {
                ret.append("[Field: ").append(e.getKey()).append(' ').append(e.getValue()).append("]\n");
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
            ret.append("empty");
        }
        return ret.toString();
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public int size() {
        if (this.globals == null) {
            if (this.fields == null) {
                return 0;
            }
            return this.fields.size();
        } else if (this.fields == null) {
            return this.globals.size();
        } else {
            return this.globals.size() + this.fields.size();
        }
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public boolean getCallsNative() {
        return this.callsNative;
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public boolean setCallsNative() {
        boolean ret = !this.callsNative;
        this.callsNative = true;
        return ret;
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public Set<SootField> getGlobals() {
        if (this.isFull) {
            return G.v().MethodRWSet_allGlobals;
        }
        return this.globals == null ? Collections.emptySet() : this.globals;
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public Set<Object> getFields() {
        if (this.isFull) {
            return G.v().MethodRWSet_allFields;
        }
        return this.fields == null ? Collections.emptySet() : this.fields.keySet();
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public PointsToSet getBaseForField(Object f) {
        if (this.isFull) {
            return FullObjectSet.v();
        }
        if (this.fields == null) {
            return null;
        }
        return this.fields.get(f);
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public boolean hasNonEmptyIntersection(RWSet oth) {
        if (this.isFull) {
            return oth != null;
        } else if (!(oth instanceof MethodRWSet)) {
            return oth.hasNonEmptyIntersection(this);
        } else {
            MethodRWSet other = (MethodRWSet) oth;
            if (this.globals != null && other.globals != null && !this.globals.isEmpty() && !other.globals.isEmpty()) {
                for (SootField next : other.globals) {
                    if (this.globals.contains(next)) {
                        return true;
                    }
                }
            }
            if (this.fields != null && other.fields != null && !this.fields.isEmpty() && !other.fields.isEmpty()) {
                for (Object field : other.fields.keySet()) {
                    if (this.fields.containsKey(field) && Union.hasNonEmptyIntersection(getBaseForField(field), other.getBaseForField(field))) {
                        return true;
                    }
                }
                return false;
            }
            return false;
        }
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
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
        } else {
            StmtRWSet oth = (StmtRWSet) other;
            if (oth.base != null) {
                ret = addFieldRef(oth.base, oth.field) | false;
            } else if (oth.field != null) {
                ret = addGlobal((SootField) oth.field) | false;
            }
        }
        if (!getCallsNative() && other.getCallsNative()) {
            setCallsNative();
            return true;
        }
        return ret;
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public boolean addGlobal(SootField global) {
        if (this.globals == null) {
            this.globals = new HashSet();
        }
        boolean ret = this.globals.add(global);
        if (this.globals.size() > Integer.MAX_VALUE) {
            this.globals = null;
            this.isFull = true;
            throw new RuntimeException("attempt to add more than 2147483647 globals into " + this);
        }
        return ret;
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public boolean addFieldRef(PointsToSet otherBase, Object field) {
        Union u;
        boolean ret = false;
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
            if (base == null || !(base instanceof Union)) {
                u = G.v().Union_factory.newUnion();
                if (base != null) {
                    u.addAll(base);
                }
                this.fields.put(field, u);
                if (base == null) {
                    addedField(this.fields.size());
                }
                ret = true;
                if (this.fields.keySet().size() > Integer.MAX_VALUE) {
                    this.fields = null;
                    this.isFull = true;
                    throw new RuntimeException("attempt to add more than 2147483647 fields into " + this);
                }
            } else {
                u = (Union) base;
            }
            return u.addAll(otherBase) | ret;
        }
    }

    static void addedField(int size) {
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public boolean isEquivTo(RWSet other) {
        return other == this;
    }
}
