package soot.jimple.toolkits.annotation.purity;

import java.util.HashMap;
import java.util.Map;
import soot.SootMethod;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/purity/PurityMethodNode.class */
public class PurityMethodNode implements PurityNode {
    private static final Map<SootMethod, Integer> nMap = new HashMap();
    private static int n = 0;
    private SootMethod id;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PurityMethodNode(SootMethod id) {
        this.id = id;
        if (!nMap.containsKey(id)) {
            nMap.put(id, Integer.valueOf(n));
            n++;
        }
    }

    public String toString() {
        return "M_" + nMap.get(this.id);
    }

    public int hashCode() {
        return this.id.hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof PurityMethodNode) {
            PurityMethodNode oo = (PurityMethodNode) o;
            return this.id.equals(oo.id);
        }
        return false;
    }

    @Override // soot.jimple.toolkits.annotation.purity.PurityNode
    public boolean isInside() {
        return true;
    }

    @Override // soot.jimple.toolkits.annotation.purity.PurityNode
    public boolean isLoad() {
        return false;
    }

    @Override // soot.jimple.toolkits.annotation.purity.PurityNode
    public boolean isParam() {
        return false;
    }
}
