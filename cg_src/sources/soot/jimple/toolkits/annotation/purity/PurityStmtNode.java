package soot.jimple.toolkits.annotation.purity;

import java.util.HashMap;
import java.util.Map;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/purity/PurityStmtNode.class */
public class PurityStmtNode implements PurityNode {
    private static final Map<Stmt, Integer> nMap = new HashMap();
    private static int n = 0;
    private final Stmt id;
    private final boolean inside;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PurityStmtNode(Stmt id, boolean inside) {
        this.id = id;
        this.inside = inside;
        if (!nMap.containsKey(id)) {
            nMap.put(id, Integer.valueOf(n));
            n++;
        }
    }

    public String toString() {
        return this.inside ? "I_" + nMap.get(this.id) : "L_" + nMap.get(this.id);
    }

    public int hashCode() {
        return this.id.hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof PurityStmtNode) {
            PurityStmtNode oo = (PurityStmtNode) o;
            return this.id.equals(oo.id) && this.inside == oo.inside;
        }
        return false;
    }

    @Override // soot.jimple.toolkits.annotation.purity.PurityNode
    public boolean isInside() {
        return this.inside;
    }

    @Override // soot.jimple.toolkits.annotation.purity.PurityNode
    public boolean isLoad() {
        return !this.inside;
    }

    @Override // soot.jimple.toolkits.annotation.purity.PurityNode
    public boolean isParam() {
        return false;
    }
}
