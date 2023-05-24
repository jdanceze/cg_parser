package soot.jimple.toolkits.typing.fast;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import soot.JavaBasicTypes;
import soot.Local;
import soot.RefType;
import soot.Scene;
import soot.Type;
import soot.util.Chain;
import soot.util.HashMultiMap;
import soot.util.MultiMap;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/fast/DefaultTypingStrategy.class */
public class DefaultTypingStrategy implements ITypingStrategy {
    public static final ITypingStrategy INSTANCE = new DefaultTypingStrategy();

    @Override // soot.jimple.toolkits.typing.fast.ITypingStrategy
    public Typing createTyping(Chain<Local> locals) {
        return new Typing(locals);
    }

    @Override // soot.jimple.toolkits.typing.fast.ITypingStrategy
    public Typing createTyping(Typing tg) {
        return new Typing(tg);
    }

    public static MultiMap<Local, Type> getFlatTyping(List<Typing> tgs) {
        MultiMap<Local, Type> map = new HashMultiMap<>();
        for (Typing tg : tgs) {
            map.putMap(tg.map);
        }
        return map;
    }

    public static Set<Local> getObjectLikeTypings(List<Typing> tgs) {
        Set<Type> objectLikeTypeSet = new HashSet<>();
        objectLikeTypeSet.add(Scene.v().getObjectType());
        objectLikeTypeSet.add(RefType.v(JavaBasicTypes.JAVA_IO_SERIALIZABLE));
        objectLikeTypeSet.add(RefType.v("java.lang.Cloneable"));
        Set<Local> objectLikeVars = new HashSet<>();
        MultiMap<Local, Type> ft = getFlatTyping(tgs);
        for (Local l : ft.keySet()) {
            if (objectLikeTypeSet.equals(ft.get(l))) {
                objectLikeVars.add(l);
            }
        }
        return objectLikeVars;
    }

    @Override // soot.jimple.toolkits.typing.fast.ITypingStrategy
    public void minimize(List<Typing> tgs, IHierarchy h) {
        Set<Local> objectVars = getObjectLikeTypings(tgs);
        ListIterator<Typing> i = tgs.listIterator();
        while (i.hasNext()) {
            Typing tgi = i.next();
            Iterator<Typing> it = tgs.iterator();
            while (true) {
                if (it.hasNext()) {
                    Typing tgj = it.next();
                    if (tgi != tgj && compare(tgi, tgj, h, objectVars) == 1) {
                        i.remove();
                        break;
                    }
                }
            }
        }
    }

    public int compare(Typing a, Typing b, IHierarchy h, Collection<Local> localsToIgnore) {
        int cmp;
        int r = 0;
        for (Local v : a.map.keySet()) {
            if (!localsToIgnore.contains(v)) {
                Type ta = a.get(v);
                Type tb = b.get(v);
                if (TypeResolver.typesEqual(ta, tb)) {
                    cmp = 0;
                } else if (h.ancestor(ta, tb)) {
                    cmp = 1;
                } else if (h.ancestor(tb, ta)) {
                    cmp = -1;
                } else {
                    return -2;
                }
                if (cmp == 1 && r == -1) {
                    return 2;
                }
                if (cmp == -1 && r == 1) {
                    return 2;
                }
                if (r == 0) {
                    r = cmp;
                }
            }
        }
        return r;
    }

    @Override // soot.jimple.toolkits.typing.fast.ITypingStrategy
    public void finalizeTypes(Typing tp) {
        for (Local l : tp.getAllLocals()) {
            Type t = tp.get(l);
            if (!t.isAllowedInFinalCode()) {
                tp.set(l, t.getDefaultFinalType());
            }
        }
    }
}
