package soot.jimple.infoflow.solver.cfg;

import java.util.Collection;
import java.util.List;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.InvokeExpr;
import soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/cfg/IInfoflowCFG.class */
public interface IInfoflowCFG extends BiDiInterproceduralCFG<Unit, SootMethod> {
    UnitContainer getPostdominatorOf(Unit unit);

    UnitContainer getDominatorOf(Unit unit);

    List<Unit> getConditionalBranchIntraprocedural(Unit unit);

    List<Unit> getConditionalBranchesInterprocedural(Unit unit);

    boolean isStaticFieldRead(SootMethod sootMethod, SootField sootField);

    boolean isStaticFieldUsed(SootMethod sootMethod, SootField sootField);

    boolean hasSideEffects(SootMethod sootMethod);

    void notifyMethodChanged(SootMethod sootMethod);

    boolean methodReadsValue(SootMethod sootMethod, Value value);

    boolean methodWritesValue(SootMethod sootMethod, Value value);

    boolean isExceptionalEdgeBetween(Unit unit, Unit unit2);

    Collection<SootMethod> getOrdinaryCalleesOfCallAt(Unit unit);

    boolean isExecutorExecute(InvokeExpr invokeExpr, SootMethod sootMethod);

    boolean isReflectiveCallSite(Unit unit);

    boolean isReflectiveCallSite(InvokeExpr invokeExpr);

    void purge();

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/cfg/IInfoflowCFG$UnitContainer.class */
    public static class UnitContainer {
        private final Unit unit;
        private final SootMethod method;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !IInfoflowCFG.class.desiredAssertionStatus();
        }

        public UnitContainer(Unit u) {
            this.unit = u;
            this.method = null;
        }

        public UnitContainer(SootMethod sm) {
            this.unit = null;
            this.method = sm;
        }

        public int hashCode() {
            return (31 * (this.unit == null ? 0 : this.unit.hashCode())) + (31 * (this.method == null ? 0 : this.method.hashCode()));
        }

        public boolean equals(Object other) {
            if (other == null || !(other instanceof UnitContainer)) {
                return false;
            }
            UnitContainer container = (UnitContainer) other;
            if (this.unit == null) {
                if (container.unit != null) {
                    return false;
                }
            } else if (!this.unit.equals(container.unit)) {
                return false;
            }
            if (this.method == null) {
                if (container.method != null) {
                    return false;
                }
            } else if (!this.method.equals(container.method)) {
                return false;
            }
            if ($assertionsDisabled || hashCode() == container.hashCode()) {
                return true;
            }
            throw new AssertionError();
        }

        public Unit getUnit() {
            return this.unit;
        }

        public SootMethod getMethod() {
            return this.method;
        }

        public String toString() {
            if (this.method != null) {
                return "(Method) " + this.method.toString();
            }
            return "(Unit) " + this.unit.toString();
        }
    }
}
