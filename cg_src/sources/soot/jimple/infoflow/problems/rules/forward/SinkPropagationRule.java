package soot.jimple.infoflow.problems.rules.forward;

import java.util.Collection;
import soot.SootMethod;
import soot.Value;
import soot.jimple.AssignStmt;
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
import soot.jimple.infoflow.sourcesSinks.manager.ISourceSinkManager;
import soot.jimple.infoflow.sourcesSinks.manager.SinkInfo;
import soot.jimple.infoflow.util.BaseSelector;
import soot.jimple.infoflow.util.ByReferenceBoolean;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/problems/rules/forward/SinkPropagationRule.class */
public class SinkPropagationRule extends AbstractTaintPropagationRule {
    private boolean killState;

    public SinkPropagationRule(InfoflowManager manager, Abstraction zeroValue, TaintPropagationResults results) {
        super(manager, zeroValue, results);
        this.killState = false;
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateNormalFlow(Abstraction d1, Abstraction source, Stmt stmt, Stmt destStmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll) {
        if (stmt instanceof ReturnStmt) {
            ReturnStmt returnStmt = (ReturnStmt) stmt;
            checkForSink(d1, source, stmt, returnStmt.getOp());
            return null;
        } else if (stmt instanceof IfStmt) {
            IfStmt ifStmt = (IfStmt) stmt;
            checkForSink(d1, source, stmt, ifStmt.getCondition());
            return null;
        } else if (stmt instanceof LookupSwitchStmt) {
            LookupSwitchStmt switchStmt = (LookupSwitchStmt) stmt;
            checkForSink(d1, source, stmt, switchStmt.getKey());
            return null;
        } else if (stmt instanceof TableSwitchStmt) {
            TableSwitchStmt switchStmt2 = (TableSwitchStmt) stmt;
            checkForSink(d1, source, stmt, switchStmt2.getKey());
            return null;
        } else if (stmt instanceof AssignStmt) {
            AssignStmt assignStmt = (AssignStmt) stmt;
            checkForSink(d1, source, stmt, assignStmt.getRightOp());
            return null;
        } else {
            return null;
        }
    }

    private void checkForSink(Abstraction d1, Abstraction source, Stmt stmt, Value retVal) {
        Value[] selectBaseList;
        SinkInfo sinkInfo;
        AccessPath ap = source.getAccessPath();
        Aliasing aliasing = getAliasing();
        ISourceSinkManager sourceSinkManager = getManager().getSourceSinkManager();
        if (ap != null && sourceSinkManager != null && aliasing != null && source.isAbstractionActive()) {
            for (Value val : BaseSelector.selectBaseList(retVal, false)) {
                if (aliasing.mayAlias(val, ap.getPlainValue()) && (sinkInfo = sourceSinkManager.getSinkInfo(stmt, getManager(), source.getAccessPath())) != null && getResults().addResult(new AbstractionAtSink(sinkInfo.getDefinition(), source, stmt))) {
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
        if (getAliasing() == null) {
            return false;
        }
        InvokeExpr iexpr = stmt.getInvokeExpr();
        Value apBaseValue = source.getAccessPath().getPlainValue();
        if (apBaseValue != null) {
            for (int i = 0; i < iexpr.getArgCount(); i++) {
                if (getAliasing().mayAlias(iexpr.getArg(i), apBaseValue) && (source.getAccessPath().getTaintSubFields() || source.getAccessPath().isLocal())) {
                    return true;
                }
            }
        }
        if (0 == 0 && (iexpr instanceof InstanceInvokeExpr) && ((InstanceInvokeExpr) iexpr).getBase() == source.getAccessPath().getPlainValue()) {
            return true;
        }
        return false;
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateCallToReturnFlow(Abstraction d1, Abstraction source, Stmt stmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll) {
        ISourceSinkManager ssm;
        SinkInfo sinkInfo;
        if (source.isAbstractionActive() && !source.getAccessPath().isStaticFieldRef() && ((!stmt.containsInvokeExpr() || isTaintVisibleInCallee(stmt, source)) && (ssm = getManager().getSourceSinkManager()) != null && (sinkInfo = ssm.getSinkInfo(stmt, getManager(), source.getAccessPath())) != null && !getResults().addResult(new AbstractionAtSink(sinkInfo.getDefinition(), source, stmt)))) {
            this.killState = true;
        }
        if (killAll != null) {
            killAll.value |= this.killState;
            return null;
        }
        return null;
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateReturnFlow(Collection<Abstraction> callerD1s, Abstraction calleeD1, Abstraction source, Stmt stmt, Stmt retSite, Stmt callSite, ByReferenceBoolean killAll) {
        SinkInfo sinkInfo;
        if (stmt instanceof ReturnStmt) {
            ReturnStmt returnStmt = (ReturnStmt) stmt;
            ISourceSinkManager ssm = getManager().getSourceSinkManager();
            Aliasing aliasing = getAliasing();
            boolean matches = source.getAccessPath().isLocal() || source.getAccessPath().getTaintSubFields();
            if (matches && source.isAbstractionActive() && ssm != null && aliasing != null && aliasing.mayAlias(source.getAccessPath().getPlainValue(), returnStmt.getOp()) && (sinkInfo = ssm.getSinkInfo(returnStmt, getManager(), source.getAccessPath())) != null && !getResults().addResult(new AbstractionAtSink(sinkInfo.getDefinition(), source, returnStmt))) {
                this.killState = true;
            }
        }
        if (killAll != null) {
            killAll.value |= this.killState;
            return null;
        }
        return null;
    }
}
