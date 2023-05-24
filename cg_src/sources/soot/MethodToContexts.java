package soot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:soot/MethodToContexts.class */
public final class MethodToContexts {
    private final Map<SootMethod, List<MethodOrMethodContext>> map = new HashMap();

    public void add(MethodOrMethodContext momc) {
        SootMethod m = momc.method();
        List<MethodOrMethodContext> l = this.map.get(m);
        if (l == null) {
            Map<SootMethod, List<MethodOrMethodContext>> map = this.map;
            List<MethodOrMethodContext> arrayList = new ArrayList<>();
            l = arrayList;
            map.put(m, arrayList);
        }
        l.add(momc);
    }

    public MethodToContexts() {
    }

    public MethodToContexts(Iterator<MethodOrMethodContext> it) {
        add(it);
    }

    public void add(Iterator<MethodOrMethodContext> it) {
        while (it.hasNext()) {
            MethodOrMethodContext momc = it.next();
            add(momc);
        }
    }

    public List<MethodOrMethodContext> get(SootMethod m) {
        List<MethodOrMethodContext> ret = this.map.get(m);
        if (ret == null) {
            ret = new ArrayList<>();
        }
        return ret;
    }
}
