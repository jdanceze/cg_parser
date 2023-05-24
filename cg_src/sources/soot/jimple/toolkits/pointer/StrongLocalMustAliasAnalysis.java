package soot.jimple.toolkits.pointer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import soot.Local;
import soot.RefLikeType;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.Stmt;
import soot.toolkits.graph.StronglyConnectedComponentsFast;
import soot.toolkits.graph.UnitGraph;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/StrongLocalMustAliasAnalysis.class */
public class StrongLocalMustAliasAnalysis extends LocalMustAliasAnalysis {
    protected final Set<Integer> invalidInstanceKeys;

    public StrongLocalMustAliasAnalysis(UnitGraph g) {
        super(g);
        this.invalidInstanceKeys = new HashSet();
        StronglyConnectedComponentsFast<Unit> sccAnalysis = new StronglyConnectedComponentsFast<>(g);
        for (List<Unit> scc : sccAnalysis.getTrueComponents()) {
            for (Unit unit : scc) {
                for (ValueBox vb : unit.getDefBoxes()) {
                    Value defValue = vb.getValue();
                    if (defValue instanceof Local) {
                        Local defLocal = (Local) defValue;
                        if (defLocal.getType() instanceof RefLikeType) {
                            this.invalidInstanceKeys.add(getFlowBefore(unit).get(defLocal));
                            this.invalidInstanceKeys.add(getFlowAfter(unit).get(defLocal));
                        }
                    }
                }
            }
        }
    }

    @Override // soot.jimple.toolkits.pointer.LocalMustAliasAnalysis
    public boolean mustAlias(Local l1, Stmt s1, Local l2, Stmt s2) {
        Integer l1n = getFlowBefore(s1).get(l1);
        Integer l2n = getFlowBefore(s2).get(l2);
        return (l1n == null || l2n == null || this.invalidInstanceKeys.contains(l1n) || this.invalidInstanceKeys.contains(l2n) || !l1n.equals(l2n)) ? false : true;
    }

    @Override // soot.jimple.toolkits.pointer.LocalMustAliasAnalysis
    public String instanceKeyString(Local l, Stmt s) {
        return this.invalidInstanceKeys.contains(getFlowBefore(s).get(l)) ? "UNKNOWN" : super.instanceKeyString(l, s);
    }
}
