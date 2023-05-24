package soot.jimple.infoflow.data;

import gnu.trove.set.hash.TCustomHashSet;
import gnu.trove.strategy.HashingStrategy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.collect.AtomicBitSet;
import soot.jimple.infoflow.solver.cfg.IInfoflowCFG;
import soot.jimple.infoflow.solver.fastSolver.FastSolverLinkedNode;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/data/Abstraction.class */
public class Abstraction implements Cloneable, FastSolverLinkedNode<Abstraction, Unit> {
    protected static boolean flowSensitiveAliasing;
    protected AccessPath accessPath;
    protected Abstraction predecessor;
    protected volatile Set<Abstraction> neighbors;
    protected Stmt currentStmt;
    protected Stmt correspondingCallSite;
    protected SourceContext sourceContext;
    protected Unit activationUnit;
    protected Unit turnUnit;
    protected boolean exceptionThrown;
    protected int hashCode;
    protected int neighborHashCode;
    protected List<IInfoflowCFG.UnitContainer> postdominators;
    protected Unit dominator;
    protected boolean isImplicit;
    protected boolean dependsOnCutAP;
    protected AtomicBitSet pathFlags;
    protected int propagationPathLength;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Abstraction.class.desiredAssertionStatus();
        flowSensitiveAliasing = true;
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/data/Abstraction$NeighborHashingStrategy.class */
    public static class NeighborHashingStrategy implements HashingStrategy<Abstraction> {
        private static final long serialVersionUID = 4836518478381414909L;
        private static final NeighborHashingStrategy INSTANCE = new NeighborHashingStrategy();

        @Override // gnu.trove.strategy.HashingStrategy
        public int computeHashCode(Abstraction abs) {
            if (abs.neighborHashCode != 0) {
                return abs.neighborHashCode;
            }
            int result = (31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * 1) + abs.hashCode())) + (abs.accessPath == null ? 0 : abs.accessPath.hashCode()))) + (abs.predecessor == null ? 0 : abs.predecessor.hashCode()))) + (abs.currentStmt == null ? 0 : abs.currentStmt.hashCode()))) + (abs.sourceContext == null ? 0 : abs.sourceContext.hashCode()))) + (abs.activationUnit == null ? 0 : abs.activationUnit.hashCode()))) + (abs.turnUnit == null ? 0 : abs.turnUnit.hashCode()))) + (abs.postdominators == null ? 0 : abs.postdominators.hashCode()))) + (abs.dominator == null ? 0 : abs.dominator.hashCode());
            abs.neighborHashCode = result;
            return result;
        }

        @Override // gnu.trove.strategy.HashingStrategy
        public boolean equals(Abstraction abs1, Abstraction abs2) {
            if (abs1 == abs2) {
                return true;
            }
            if (abs1 == null || abs2 == null || abs1.getClass() != abs2.getClass()) {
                return false;
            }
            int hashCode1 = abs1.neighborHashCode;
            int hashCode2 = abs2.neighborHashCode;
            if (hashCode1 != 0 && hashCode2 != 0 && hashCode1 != hashCode2) {
                return false;
            }
            if (abs1.accessPath == null) {
                if (abs2.accessPath != null) {
                    return false;
                }
            } else if (!abs1.accessPath.equals(abs2.accessPath)) {
                return false;
            }
            if (abs1.predecessor == null) {
                if (abs2.predecessor != null) {
                    return false;
                }
            } else if (!abs1.predecessor.equals(abs2.predecessor)) {
                return false;
            }
            if (abs1.currentStmt == null) {
                if (abs2.currentStmt != null) {
                    return false;
                }
            } else if (!abs1.currentStmt.equals(abs2.currentStmt)) {
                return false;
            }
            return abs1.localEquals(abs2);
        }
    }

    public Abstraction(ISourceSinkDefinition definition, AccessPath sourceVal, Stmt sourceStmt, Object userData, boolean exceptionThrown, boolean isImplicit) {
        this(sourceVal, new SourceContext(definition, sourceVal, sourceStmt, userData), exceptionThrown, isImplicit);
    }

    Abstraction(AccessPath apToTaint, SourceContext sourceContext, boolean exceptionThrown, boolean isImplicit) {
        this.predecessor = null;
        this.neighbors = null;
        this.currentStmt = null;
        this.correspondingCallSite = null;
        this.sourceContext = null;
        this.activationUnit = null;
        this.turnUnit = null;
        this.exceptionThrown = false;
        this.hashCode = 0;
        this.neighborHashCode = 0;
        this.postdominators = null;
        this.dominator = null;
        this.isImplicit = false;
        this.dependsOnCutAP = false;
        this.pathFlags = null;
        this.propagationPathLength = 0;
        this.sourceContext = sourceContext;
        this.accessPath = apToTaint;
        this.activationUnit = null;
        this.turnUnit = null;
        this.exceptionThrown = exceptionThrown;
        this.neighbors = null;
        this.isImplicit = isImplicit;
        this.currentStmt = sourceContext == null ? null : sourceContext.getStmt();
    }

    protected Abstraction(AccessPath p, Abstraction original) {
        this.predecessor = null;
        this.neighbors = null;
        this.currentStmt = null;
        this.correspondingCallSite = null;
        this.sourceContext = null;
        this.activationUnit = null;
        this.turnUnit = null;
        this.exceptionThrown = false;
        this.hashCode = 0;
        this.neighborHashCode = 0;
        this.postdominators = null;
        this.dominator = null;
        this.isImplicit = false;
        this.dependsOnCutAP = false;
        this.pathFlags = null;
        this.propagationPathLength = 0;
        if (original == null) {
            this.sourceContext = null;
            this.exceptionThrown = false;
            this.activationUnit = null;
            this.turnUnit = null;
            this.isImplicit = false;
        } else {
            this.sourceContext = original.sourceContext;
            this.exceptionThrown = original.exceptionThrown;
            this.activationUnit = original.activationUnit;
            this.turnUnit = original.turnUnit;
            if (!$assertionsDisabled && this.activationUnit != null && !flowSensitiveAliasing) {
                throw new AssertionError();
            }
            this.postdominators = original.postdominators == null ? null : new ArrayList(original.postdominators);
            this.dominator = original.dominator;
            this.dependsOnCutAP = original.dependsOnCutAP;
            this.isImplicit = original.isImplicit;
        }
        this.accessPath = p;
        this.neighbors = null;
        this.currentStmt = null;
    }

    public static void initialize(InfoflowConfiguration config) {
        flowSensitiveAliasing = config.getFlowSensitiveAliasing();
    }

    public Abstraction deriveInactiveAbstraction(Stmt activationUnit) {
        if (!flowSensitiveAliasing) {
            if ($assertionsDisabled || isAbstractionActive()) {
                return this;
            }
            throw new AssertionError();
        } else if (!isAbstractionActive()) {
            return this;
        } else {
            Abstraction a = deriveNewAbstractionMutable(this.accessPath, null);
            if (a == null) {
                return null;
            }
            a.postdominators = null;
            a.dominator = null;
            a.activationUnit = activationUnit;
            a.dependsOnCutAP |= a.getAccessPath().isCutOffApproximation();
            return a;
        }
    }

    public Abstraction deriveNewAbstraction(AccessPath p, Stmt currentStmt) {
        return deriveNewAbstraction(p, currentStmt, this.isImplicit);
    }

    public Abstraction deriveNewAbstraction(AccessPath p, Stmt currentStmt, boolean isImplicit) {
        if (this.accessPath.equals(p) && this.currentStmt == currentStmt && this.isImplicit == isImplicit) {
            return this;
        }
        Abstraction abs = deriveNewAbstractionMutable(p, currentStmt);
        if (abs == null) {
            return null;
        }
        abs.isImplicit = isImplicit;
        return abs;
    }

    protected Abstraction deriveNewAbstractionMutable(AccessPath p, Stmt currentStmt) {
        if (p == null) {
            return null;
        }
        if (this.accessPath.equals(p) && this.currentStmt == currentStmt) {
            Abstraction abs = clone();
            abs.currentStmt = currentStmt;
            return abs;
        }
        Abstraction abs2 = new Abstraction(p, this);
        abs2.predecessor = this;
        abs2.currentStmt = currentStmt;
        abs2.propagationPathLength = this.propagationPathLength + 1;
        if (!abs2.getAccessPath().isEmpty()) {
            abs2.postdominators = null;
        }
        if (!abs2.isAbstractionActive()) {
            abs2.dependsOnCutAP = abs2.dependsOnCutAP || p.isCutOffApproximation();
        }
        abs2.sourceContext = null;
        return abs2;
    }

    public Abstraction deriveNewAbstractionOnThrow(Stmt throwStmt) {
        Abstraction abs = clone();
        abs.currentStmt = throwStmt;
        abs.sourceContext = null;
        abs.exceptionThrown = true;
        return abs;
    }

    public Abstraction deriveNewAbstractionOnCatch(AccessPath ap) {
        if ($assertionsDisabled || this.exceptionThrown) {
            Abstraction abs = deriveNewAbstractionMutable(ap, null);
            if (abs == null) {
                return null;
            }
            abs.exceptionThrown = false;
            return abs;
        }
        throw new AssertionError();
    }

    public boolean isAbstractionActive() {
        return this.activationUnit == null;
    }

    public boolean isImplicit() {
        return this.isImplicit;
    }

    public String toString() {
        return String.valueOf(isAbstractionActive() ? "" : "_") + this.accessPath.toString() + " | " + ((this.turnUnit != null || this.activationUnit == null) ? "" : this.activationUnit.toString()) + (this.turnUnit == null ? "" : this.turnUnit.toString()) + ">>";
    }

    public AccessPath getAccessPath() {
        return this.accessPath;
    }

    public Unit getActivationUnit() {
        return this.activationUnit;
    }

    public Unit getTurnUnit() {
        return this.turnUnit;
    }

    public Abstraction deriveNewAbstractionWithTurnUnit(Unit turnUnit) {
        if (this.turnUnit == turnUnit) {
            return this;
        }
        Abstraction a = clone();
        a.sourceContext = null;
        a.activationUnit = null;
        a.turnUnit = turnUnit;
        return a;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.jimple.infoflow.solver.fastSolver.FastSolverLinkedNode
    public Abstraction getActiveCopy() {
        if (isAbstractionActive()) {
            return this;
        }
        Abstraction a = clone();
        a.sourceContext = null;
        a.activationUnit = null;
        return a;
    }

    public boolean getExceptionThrown() {
        return this.exceptionThrown;
    }

    public Abstraction deriveConditionalAbstractionEnter(IInfoflowCFG.UnitContainer postdom, Stmt conditionalUnit) {
        if ($assertionsDisabled || isAbstractionActive()) {
            if (this.postdominators != null && this.postdominators.contains(postdom)) {
                return this;
            }
            Abstraction abs = deriveNewAbstractionMutable(AccessPath.getEmptyAccessPath(), conditionalUnit);
            if (abs == null) {
                return null;
            }
            if (abs.postdominators == null) {
                abs.postdominators = Collections.singletonList(postdom);
            } else {
                abs.postdominators.add(0, postdom);
            }
            return abs;
        }
        throw new AssertionError();
    }

    public Abstraction deriveConditionalAbstractionCall(Unit conditionalCallSite) {
        if ($assertionsDisabled || isAbstractionActive()) {
            if ($assertionsDisabled || conditionalCallSite != null) {
                Abstraction abs = deriveNewAbstractionMutable(AccessPath.getEmptyAccessPath(), (Stmt) conditionalCallSite);
                if (abs == null) {
                    return null;
                }
                abs.postdominators = null;
                return abs;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public Abstraction dropTopPostdominator() {
        if (this.postdominators == null || this.postdominators.isEmpty()) {
            return this;
        }
        Abstraction abs = clone();
        abs.sourceContext = null;
        abs.postdominators.remove(0);
        return abs;
    }

    public IInfoflowCFG.UnitContainer getTopPostdominator() {
        if (this.postdominators == null || this.postdominators.isEmpty()) {
            return null;
        }
        return this.postdominators.get(0);
    }

    public boolean isTopPostdominator(Unit u) {
        IInfoflowCFG.UnitContainer uc = getTopPostdominator();
        return uc != null && uc.getUnit() == u;
    }

    public boolean isTopPostdominator(SootMethod sm) {
        IInfoflowCFG.UnitContainer uc = getTopPostdominator();
        return uc != null && uc.getMethod() == sm;
    }

    public Abstraction deriveNewAbstractionWithDominator(Unit dominator, Stmt stmt) {
        if (this.dominator != null) {
            return this;
        }
        Abstraction abs = deriveNewAbstractionMutable(this.accessPath, stmt);
        if (abs == null) {
            return null;
        }
        abs.setDominator(dominator);
        return abs;
    }

    public Abstraction deriveNewAbstractionWithDominator(Unit dominator) {
        return deriveNewAbstractionWithDominator(dominator, null);
    }

    public Abstraction deriveConditionalUpdate(Stmt stmt) {
        return deriveNewAbstractionMutable(AccessPath.getEmptyAccessPath(), stmt);
    }

    public Abstraction deriveCondition(AccessPath ap, Stmt stmt) {
        Abstraction abs = deriveNewAbstractionMutable(ap, stmt);
        if (abs == null) {
            return null;
        }
        abs.turnUnit = stmt;
        abs.dominator = null;
        return abs;
    }

    public Abstraction removeDominator(Stmt stmt) {
        Abstraction abs = deriveNewAbstraction(this.accessPath, stmt);
        if (abs == null) {
            return null;
        }
        abs.setDominator(null);
        return abs;
    }

    public void setDominator(Unit dominator) {
        this.dominator = dominator;
    }

    public Unit getDominator() {
        return this.dominator;
    }

    public boolean isDominator(Unit u) {
        return this.dominator != null && this.dominator == u;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.jimple.infoflow.solver.fastSolver.FastSolverLinkedNode
    public Abstraction clone() {
        Abstraction abs = new Abstraction(this.accessPath, this);
        abs.predecessor = this;
        abs.neighbors = null;
        abs.currentStmt = null;
        abs.correspondingCallSite = null;
        abs.propagationPathLength = this.propagationPathLength + 1;
        if ($assertionsDisabled || abs.equals(this)) {
            return abs;
        }
        throw new AssertionError();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Abstraction other = (Abstraction) obj;
        if (this.hashCode != 0 && other.hashCode != 0 && this.hashCode != other.hashCode) {
            return false;
        }
        if (this.accessPath == null) {
            if (other.accessPath != null) {
                return false;
            }
        } else if (!this.accessPath.equals(other.accessPath)) {
            return false;
        }
        return localEquals(other);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean localEquals(Abstraction other) {
        if (this.sourceContext == null) {
            if (other.sourceContext != null) {
                return false;
            }
        } else if (!this.sourceContext.equals(other.sourceContext)) {
            return false;
        }
        if (this.activationUnit == null) {
            if (other.activationUnit != null) {
                return false;
            }
        } else if (!this.activationUnit.equals(other.activationUnit)) {
            return false;
        }
        if (this.turnUnit == null) {
            if (other.turnUnit != null) {
                return false;
            }
        } else if (!this.turnUnit.equals(other.turnUnit)) {
            return false;
        }
        if (this.exceptionThrown != other.exceptionThrown) {
            return false;
        }
        if (this.postdominators == null) {
            if (other.postdominators != null) {
                return false;
            }
        } else if (!this.postdominators.equals(other.postdominators)) {
            return false;
        }
        if (this.dominator == null) {
            if (other.dominator != null) {
                return false;
            }
        } else if (!this.dominator.equals(other.dominator)) {
            return false;
        }
        if (this.dependsOnCutAP != other.dependsOnCutAP || this.isImplicit != other.isImplicit) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        if (this.hashCode != 0) {
            return this.hashCode;
        }
        int result = (31 * 1) + (this.sourceContext == null ? 0 : this.sourceContext.hashCode());
        this.hashCode = (31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * result) + (this.accessPath == null ? 0 : this.accessPath.hashCode()))) + (this.activationUnit == null ? 0 : this.activationUnit.hashCode()))) + (this.turnUnit == null ? 0 : this.turnUnit.hashCode()))) + (this.exceptionThrown ? 1231 : 1237))) + (this.postdominators == null ? 0 : this.postdominators.hashCode()))) + (this.dominator == null ? 0 : this.dominator.hashCode()))) + (this.dependsOnCutAP ? 1231 : 1237))) + (this.isImplicit ? 1231 : 1237);
        return this.hashCode;
    }

    public boolean entails(Abstraction other) {
        if (this.accessPath == null) {
            if (other.accessPath != null) {
                return false;
            }
        } else if (!this.accessPath.entails(other.accessPath)) {
            return false;
        }
        return localEquals(other);
    }

    public SourceContext getSourceContext() {
        return this.sourceContext;
    }

    public boolean dependsOnCutAP() {
        return this.dependsOnCutAP;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.jimple.infoflow.solver.fastSolver.FastSolverLinkedNode
    public Abstraction getPredecessor() {
        return this.predecessor;
    }

    public Set<Abstraction> getNeighbors() {
        return this.neighbors;
    }

    public Stmt getCurrentStmt() {
        return this.currentStmt;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.jimple.infoflow.solver.fastSolver.FastSolverLinkedNode
    public boolean addNeighbor(Abstraction originalAbstraction) {
        if (originalAbstraction == this) {
            return false;
        }
        if (this.predecessor == originalAbstraction.predecessor && this.currentStmt == originalAbstraction.currentStmt && this.predecessor == originalAbstraction.predecessor && this.correspondingCallSite == originalAbstraction.correspondingCallSite) {
            return false;
        }
        synchronized (this) {
            if (this.neighbors == null) {
                this.neighbors = new TCustomHashSet(NeighborHashingStrategy.INSTANCE);
            } else if (InfoflowConfiguration.getMergeNeighbors()) {
                for (Abstraction nb : this.neighbors) {
                    if (nb == originalAbstraction) {
                        return false;
                    }
                    if (originalAbstraction.predecessor == nb.predecessor && originalAbstraction.currentStmt == nb.currentStmt && originalAbstraction.correspondingCallSite == nb.correspondingCallSite) {
                        return false;
                    }
                }
            }
            return this.neighbors.add(originalAbstraction);
        }
    }

    public void setCorrespondingCallSite(Stmt callSite) {
        this.correspondingCallSite = callSite;
    }

    public Stmt getCorrespondingCallSite() {
        return this.correspondingCallSite;
    }

    public static Abstraction getZeroAbstraction(boolean flowSensitiveAliasing2) {
        Abstraction zeroValue = new Abstraction(AccessPath.getZeroAccessPath(), null, false, false);
        flowSensitiveAliasing = flowSensitiveAliasing2;
        return zeroValue;
    }

    @Override // soot.jimple.infoflow.solver.fastSolver.FastSolverLinkedNode
    public void setPredecessor(Abstraction predecessor) {
        this.predecessor = predecessor;
        if (!$assertionsDisabled && this.predecessor == this) {
            throw new AssertionError();
        }
        this.neighborHashCode = 0;
    }

    public void setSourceContext(SourceContext sourceContext) {
        this.sourceContext = sourceContext;
        this.hashCode = 0;
        this.neighborHashCode = 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v18 */
    /* JADX WARN: Type inference failed for: r0v2 */
    /* JADX WARN: Type inference failed for: r0v3, types: [java.lang.Throwable] */
    public boolean registerPathFlag(int id, int maxSize) {
        if (this.pathFlags == null || this.pathFlags.size() < maxSize) {
            ?? r0 = this;
            synchronized (r0) {
                if (this.pathFlags == null) {
                    this.pathFlags = new AtomicBitSet(maxSize);
                } else if (this.pathFlags.size() < maxSize) {
                    AtomicBitSet pf = new AtomicBitSet(maxSize);
                    for (int i = 0; i < this.pathFlags.size(); i++) {
                        if (this.pathFlags.get(i)) {
                            pf.set(i);
                        }
                    }
                    this.pathFlags = pf;
                }
                r0 = r0;
            }
        }
        return this.pathFlags.set(id);
    }

    public Abstraction injectSourceContext(SourceContext sourceContext) {
        if (this.sourceContext != null && this.sourceContext.equals(sourceContext)) {
            return this;
        }
        Abstraction abs = clone();
        abs.predecessor = null;
        abs.neighbors = null;
        abs.sourceContext = sourceContext;
        abs.currentStmt = this.currentStmt;
        return abs;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAccessPath(AccessPath accessPath) {
        this.accessPath = accessPath;
        this.hashCode = 0;
        this.neighborHashCode = 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCurrentStmt(Stmt currentStmt) {
        this.currentStmt = currentStmt;
    }

    @Override // soot.jimple.infoflow.solver.fastSolver.FastSolverLinkedNode
    public int getNeighborCount() {
        if (this.neighbors == null) {
            return 0;
        }
        return this.neighbors.size();
    }

    @Override // soot.jimple.infoflow.solver.fastSolver.FastSolverLinkedNode
    public int getPathLength() {
        return this.propagationPathLength;
    }
}
