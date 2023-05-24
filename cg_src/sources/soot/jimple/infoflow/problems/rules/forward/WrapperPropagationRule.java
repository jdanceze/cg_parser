package soot.jimple.infoflow.problems.rules.forward;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import soot.RefType;
import soot.SootMethod;
import soot.jimple.DefinitionStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.aliasing.Aliasing;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.problems.TaintPropagationResults;
import soot.jimple.infoflow.problems.rules.AbstractTaintPropagationRule;
import soot.jimple.infoflow.sourcesSinks.manager.SourceInfo;
import soot.jimple.infoflow.typing.TypeUtils;
import soot.jimple.infoflow.util.ByReferenceBoolean;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/problems/rules/forward/WrapperPropagationRule.class */
public class WrapperPropagationRule extends AbstractTaintPropagationRule {
    public WrapperPropagationRule(InfoflowManager manager, Abstraction zeroValue, TaintPropagationResults results) {
        super(manager, zeroValue, results);
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateNormalFlow(Abstraction d1, Abstraction source, Stmt stmt, Stmt destStmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll) {
        return null;
    }

    private Set<Abstraction> computeWrapperTaints(Abstraction d1, Stmt iStmt, Abstraction source) {
        SourceInfo sourceInfo;
        if (source == getZeroValue() || getManager().getTaintWrapper() == null) {
            return null;
        }
        Aliasing aliasing = getAliasing();
        if (aliasing != null && !source.getAccessPath().isStaticFieldRef() && !source.getAccessPath().isEmpty()) {
            boolean found = false;
            if (iStmt.getInvokeExpr() instanceof InstanceInvokeExpr) {
                InstanceInvokeExpr iiExpr = (InstanceInvokeExpr) iStmt.getInvokeExpr();
                found = aliasing.mayAlias(iiExpr.getBase(), source.getAccessPath().getPlainValue());
            }
            if (!found) {
                int paramIdx = 0;
                while (true) {
                    if (paramIdx < iStmt.getInvokeExpr().getArgCount()) {
                        if (!aliasing.mayAlias(source.getAccessPath().getPlainValue(), iStmt.getInvokeExpr().getArg(paramIdx))) {
                            paramIdx++;
                        } else {
                            found = true;
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
            if (!found) {
                return null;
            }
        }
        if (!getManager().getConfig().getInspectSources()) {
            if (getManager().getSourceSinkManager() != null) {
                sourceInfo = getManager().getSourceSinkManager().getSourceInfo(iStmt, getManager());
            } else {
                sourceInfo = null;
            }
            SourceInfo sourceInfo2 = sourceInfo;
            if (sourceInfo2 != null) {
                return null;
            }
        }
        Set<Abstraction> res = getManager().getTaintWrapper().getTaintsForMethod(iStmt, d1, source);
        if (res != null) {
            Set<Abstraction> hashSet = new HashSet<>(res);
            for (Abstraction abs : res) {
                if (!abs.equals(source)) {
                    checkAndPropagateAlias(d1, iStmt, hashSet, abs);
                }
            }
            res = hashSet;
        }
        return res;
    }

    protected void checkAndPropagateAlias(Abstraction d1, Stmt iStmt, Set<Abstraction> resWithAliases, Abstraction abs) {
        boolean z;
        AccessPath val = abs.getAccessPath();
        boolean isBasicString = (!TypeUtils.isStringType(val.getBaseType()) || val.getCanHaveImmutableAliases() || getAliasing().isStringConstructorCall(iStmt)) ? false : true;
        boolean taintsObjectValue = (val.getBaseType() instanceof RefType) && (abs.getAccessPath().getBaseType() instanceof RefType) && !isBasicString;
        boolean taintsStaticField = getManager().getConfig().getStaticFieldTrackingMode() != InfoflowConfiguration.StaticFieldTrackingMode.None && abs.getAccessPath().isStaticFieldRef();
        if (iStmt instanceof DefinitionStmt) {
            z = Aliasing.baseMatches(((DefinitionStmt) iStmt).getLeftOp(), abs);
        } else {
            z = false;
        }
        boolean taintedValueOverwritten = z;
        if (!taintedValueOverwritten) {
            if (taintsStaticField || ((taintsObjectValue && abs.getAccessPath().getTaintSubFields()) || this.manager.getAliasing().canHaveAliases(iStmt, val.getCompleteValue(), abs))) {
                getAliasing().computeAliases(d1, iStmt, val.getPlainValue(), resWithAliases, getManager().getICFG().getMethodOf(iStmt), abs);
            }
        }
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateCallToReturnFlow(Abstraction d1, Abstraction source, Stmt stmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll) {
        Collection<Abstraction> wrapperTaints = computeWrapperTaints(d1, stmt, source);
        if (wrapperTaints != null) {
            Iterator<Abstraction> it = wrapperTaints.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Abstraction wrapperAbs = it.next();
                if (wrapperAbs.getAccessPath().equals(source.getAccessPath())) {
                    if (wrapperAbs != source) {
                        killSource.value = true;
                    }
                }
            }
        }
        return wrapperTaints;
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateReturnFlow(Collection<Abstraction> callerD1s, Abstraction calleeD1, Abstraction source, Stmt stmt, Stmt retSite, Stmt callSite, ByReferenceBoolean killAll) {
        return null;
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateCallFlow(Abstraction d1, Abstraction source, Stmt stmt, SootMethod dest, ByReferenceBoolean killAll) {
        if (getManager().getTaintWrapper() != null && getManager().getTaintWrapper().isExclusive(stmt, source)) {
            killAll.value = true;
            return null;
        }
        return null;
    }
}
