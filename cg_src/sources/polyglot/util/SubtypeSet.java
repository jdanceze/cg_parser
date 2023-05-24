package polyglot.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/SubtypeSet.class */
public class SubtypeSet implements Set {
    Vector v;
    TypeSystem ts;
    Type topType;

    public SubtypeSet(TypeSystem ts) {
        this(ts.Object());
    }

    public SubtypeSet(Type top) {
        this.v = new Vector();
        this.ts = top.typeSystem();
        this.topType = top;
    }

    public SubtypeSet(SubtypeSet s) {
        this.v = new Vector(s.v);
        this.ts = s.ts;
        this.topType = s.topType;
    }

    public SubtypeSet(TypeSystem ts, Collection c) {
        this(ts);
        addAll(c);
    }

    public SubtypeSet(Type top, Collection c) {
        this(top);
        addAll(c);
    }

    @Override // java.util.Set, java.util.Collection
    public boolean add(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof Type) {
            Type type = (Type) o;
            if (this.ts.isSubtype(type, this.topType)) {
                boolean haveToAdd = true;
                Iterator i = this.v.iterator();
                while (true) {
                    if (!i.hasNext()) {
                        break;
                    }
                    Type t = (Type) i.next();
                    if (this.ts.descendsFrom(t, type)) {
                        i.remove();
                    }
                    if (this.ts.isSubtype(type, t)) {
                        haveToAdd = false;
                        break;
                    }
                }
                if (haveToAdd) {
                    this.v.add(type);
                }
                return haveToAdd;
            }
        }
        throw new InternalCompilerError(new StringBuffer().append("Can only add ").append(this.topType).append("s to the set. Got a ").append(o).toString());
    }

    @Override // java.util.Set, java.util.Collection
    public boolean addAll(Collection c) {
        if (c == null) {
            return false;
        }
        boolean changed = false;
        for (Object obj : c) {
            changed |= add(obj);
        }
        return changed;
    }

    @Override // java.util.Set, java.util.Collection
    public void clear() {
        this.v.clear();
    }

    @Override // java.util.Set, java.util.Collection
    public boolean contains(Object o) {
        if (o instanceof Type) {
            Type type = (Type) o;
            Iterator i = this.v.iterator();
            while (i.hasNext()) {
                Type t = (Type) i.next();
                if (this.ts.isSubtype(type, t)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:5:0x0011  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean containsSubtype(polyglot.types.Type r5) {
        /*
            r4 = this;
            r0 = r4
            java.util.Vector r0 = r0.v
            java.util.Iterator r0 = r0.iterator()
            r6 = r0
        L8:
            r0 = r6
            boolean r0 = r0.hasNext()
            if (r0 == 0) goto L3c
            r0 = r6
            java.lang.Object r0 = r0.next()
            polyglot.types.Type r0 = (polyglot.types.Type) r0
            r7 = r0
            r0 = r4
            polyglot.types.TypeSystem r0 = r0.ts
            r1 = r5
            r2 = r7
            boolean r0 = r0.isSubtype(r1, r2)
            if (r0 != 0) goto L37
            r0 = r4
            polyglot.types.TypeSystem r0 = r0.ts
            r1 = r7
            r2 = r5
            boolean r0 = r0.isSubtype(r1, r2)
            if (r0 == 0) goto L39
        L37:
            r0 = 1
            return r0
        L39:
            goto L8
        L3c:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: polyglot.util.SubtypeSet.containsSubtype(polyglot.types.Type):boolean");
    }

    @Override // java.util.Set, java.util.Collection
    public boolean containsAll(Collection c) {
        for (Object obj : c) {
            if (!contains(obj)) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean isEmpty() {
        return this.v.isEmpty();
    }

    @Override // java.util.Set, java.util.Collection, java.lang.Iterable
    public Iterator iterator() {
        return this.v.iterator();
    }

    @Override // java.util.Set, java.util.Collection
    public boolean remove(Object o) {
        Type type = (Type) o;
        boolean removed = false;
        Iterator i = this.v.iterator();
        while (i.hasNext()) {
            Type t = (Type) i.next();
            if (this.ts.isSubtype(t, type)) {
                removed = true;
                i.remove();
            }
        }
        return removed;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean removeAll(Collection c) {
        boolean changed = false;
        for (Object o : c) {
            changed |= remove(o);
        }
        return changed;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean retainAll(Collection c) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override // java.util.Set, java.util.Collection
    public int size() {
        return this.v.size();
    }

    @Override // java.util.Set, java.util.Collection
    public Object[] toArray() {
        return this.v.toArray();
    }

    @Override // java.util.Set, java.util.Collection
    public Object[] toArray(Object[] a) {
        return this.v.toArray(a);
    }

    public String toString() {
        return this.v.toString();
    }
}
