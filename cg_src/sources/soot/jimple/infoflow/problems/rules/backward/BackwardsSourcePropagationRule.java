package soot.jimple.infoflow.problems.rules.backward;

import java.util.Collection;
import soot.SootMethod;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.Stmt;
import soot.jimple.TableSwitchStmt;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.aliasing.Aliasing;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AbstractionAtSink;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.problems.TaintPropagationResults;
import soot.jimple.infoflow.problems.rules.AbstractTaintPropagationRule;
import soot.jimple.infoflow.sourcesSinks.manager.IReversibleSourceSinkManager;
import soot.jimple.infoflow.sourcesSinks.manager.SinkInfo;
import soot.jimple.infoflow.util.BaseSelector;
import soot.jimple.infoflow.util.ByReferenceBoolean;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/problems/rules/backward/BackwardsSourcePropagationRule.class */
public class BackwardsSourcePropagationRule extends AbstractTaintPropagationRule {
    private boolean killState;

    public BackwardsSourcePropagationRule(InfoflowManager manager, Abstraction zeroValue, TaintPropagationResults results) {
        super(manager, zeroValue, results);
        this.killState = false;
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateNormalFlow(Abstraction d1, Abstraction source, Stmt stmt, Stmt destStmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll) {
        if (stmt instanceof ReturnStmt) {
            ReturnStmt returnStmt = (ReturnStmt) stmt;
            checkForSource(d1, source, stmt, returnStmt.getOp());
            return null;
        } else if (stmt instanceof IfStmt) {
            IfStmt ifStmt = (IfStmt) stmt;
            checkForSource(d1, source, stmt, ifStmt.getCondition());
            return null;
        } else if (stmt instanceof LookupSwitchStmt) {
            LookupSwitchStmt switchStmt = (LookupSwitchStmt) stmt;
            checkForSource(d1, source, stmt, switchStmt.getKey());
            return null;
        } else if (stmt instanceof TableSwitchStmt) {
            TableSwitchStmt switchStmt2 = (TableSwitchStmt) stmt;
            checkForSource(d1, source, stmt, switchStmt2.getKey());
            return null;
        } else if (stmt instanceof AssignStmt) {
            AssignStmt assignStmt = (AssignStmt) stmt;
            checkForSource(d1, source, stmt, assignStmt.getRightOp());
            return null;
        } else if (stmt instanceof IdentityStmt) {
            IdentityStmt identityStmt = (IdentityStmt) stmt;
            checkForSource(d1, source, stmt, identityStmt.getLeftOp());
            return null;
        } else {
            return null;
        }
    }

    private void checkForSource(Abstraction d1, Abstraction source, Stmt stmt, Value retVal) {
        Value[] selectBaseList;
        SinkInfo sourceInfo;
        AccessPath ap = source.getAccessPath();
        if (!(this.manager.getSourceSinkManager() instanceof IReversibleSourceSinkManager)) {
            return;
        }
        IReversibleSourceSinkManager ssm = (IReversibleSourceSinkManager) this.manager.getSourceSinkManager();
        Aliasing aliasing = getAliasing();
        if (ap != null && ssm != null && aliasing != null && source.isAbstractionActive()) {
            for (Value val : BaseSelector.selectBaseList(retVal, false)) {
                if (aliasing.mayAlias(val, ap.getPlainValue()) && (sourceInfo = ssm.getInverseSourceInfo(stmt, getManager(), source.getAccessPath())) != null && !getResults().addResult(new AbstractionAtSink(sourceInfo.getDefinition(), source, stmt))) {
                    this.killState = true;
                }
            }
        }
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateCallFlow(Abstraction d1, Abstraction source, Stmt stmt, SootMethod dest, ByReferenceBoolean killAll) {
        if (killAll != null) {
            killAll.value |= this.killState;
            return null;
        }
        return null;
    }

    protected boolean isTaintVisibleInCallee(Stmt stmt, Abstraction source) {
        InvokeExpr iexpr = stmt.getInvokeExpr();
        Aliasing aliasing = getAliasing();
        Value apBaseValue = source.getAccessPath().getPlainValue();
        if (apBaseValue != null && aliasing != null) {
            for (int i = 0; i < iexpr.getArgCount(); i++) {
                if (aliasing.mayAlias(iexpr.getArg(i), apBaseValue) && (source.getAccessPath().getTaintSubFields() || source.getAccessPath().isLocal())) {
                    return true;
                }
            }
        }
        if ((iexpr instanceof InstanceInvokeExpr) && ((InstanceInvokeExpr) iexpr).getBase() == source.getAccessPath().getPlainValue()) {
            return true;
        }
        if ((stmt instanceof AssignStmt) && aliasing != null && aliasing.mayAlias(apBaseValue, ((AssignStmt) stmt).getLeftOp())) {
            return true;
        }
        return false;
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateCallToReturnFlow(Abstraction d1, Abstraction source, Stmt stmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll) {
        SinkInfo sourceInfo;
        if (!(this.manager.getSourceSinkManager() instanceof IReversibleSourceSinkManager)) {
            return null;
        }
        IReversibleSourceSinkManager ssm = (IReversibleSourceSinkManager) this.manager.getSourceSinkManager();
        if (source.isAbstractionActive() && !source.getAccessPath().isStaticFieldRef() && !source.getAccessPath().isEmpty() && ((!stmt.containsInvokeExpr() || isTaintVisibleInCallee(stmt, source)) && (sourceInfo = ssm.getInverseSourceInfo(stmt, getManager(), source.getAccessPath())) != null)) {
            boolean result = getResults().addResult(new AbstractionAtSink(sourceInfo.getDefinition(), source, stmt));
            if (!result) {
                this.killState = true;
            }
        }
        if (killAll != null) {
            killAll.value |= this.killState;
            return null;
        }
        return null;
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateReturnFlow(Collection<Abstraction> callerD1s, Abstraction calleeD1, Abstraction source, Stmt stmt, Stmt retSite, Stmt callSite, ByReferenceBoolean killAll) {
        if (killAll != null) {
            killAll.value |= this.killState;
            return null;
        }
        return null;
    }
}
