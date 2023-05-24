package soot.jimple.toolkits.pointer;

import java.util.BitSet;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.FastHierarchy;
import soot.Local;
import soot.RefType;
import soot.Scene;
import soot.Type;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/LocalTypeSet.class */
class LocalTypeSet extends BitSet {
    private static final Logger logger = LoggerFactory.getLogger(LocalTypeSet.class);
    protected final List<Local> locals;
    protected final List<Type> types;

    public LocalTypeSet(List<Local> locals, List<Type> types) {
        super(locals.size() * types.size());
        this.locals = locals;
        this.types = types;
        Scene.v().getOrMakeFastHierarchy();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int indexOf(Local l, RefType t) {
        int index_l = this.locals.indexOf(l);
        int index_t = this.types.indexOf(t);
        if (index_l == -1 || index_t == -1) {
            throw new RuntimeException("Invalid local or type in LocalTypeSet");
        }
        return (index_l * this.types.size()) + index_t;
    }

    public void killLocal(Local l) {
        int typesSize = this.types.size();
        int base = typesSize * this.locals.indexOf(l);
        for (int i = 0; i < typesSize; i++) {
            clear(i + base);
        }
    }

    public void localCopy(Local to, Local from) {
        int typesSize = this.types.size();
        int baseTo = typesSize * this.locals.indexOf(to);
        int baseFrom = typesSize * this.locals.indexOf(from);
        for (int i = 0; i < typesSize; i++) {
            if (get(i + baseFrom)) {
                set(i + baseTo);
            } else {
                clear(i + baseTo);
            }
        }
    }

    public void clearAllBits() {
        int e = this.types.size() * this.locals.size();
        for (int i = 0; i < e; i++) {
            clear(i);
        }
    }

    public void setAllBits() {
        int e = this.types.size() * this.locals.size();
        for (int i = 0; i < e; i++) {
            set(i);
        }
    }

    public void localMustBeSubtypeOf(Local l, RefType t) {
        FastHierarchy fh = Scene.v().getFastHierarchy();
        for (Type type : this.types) {
            RefType supertype = (RefType) type;
            if (fh.canStoreType(t, supertype)) {
                set(indexOf(l, supertype));
            }
        }
    }

    @Override // java.util.BitSet
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Local l : this.locals) {
            for (Type t : this.types) {
                RefType rt = (RefType) t;
                int index = indexOf(l, rt);
                if (get(index)) {
                    sb.append("((").append(l).append(',').append(rt).append(") -> elim cast check) ");
                }
            }
        }
        return sb.toString();
    }
}
