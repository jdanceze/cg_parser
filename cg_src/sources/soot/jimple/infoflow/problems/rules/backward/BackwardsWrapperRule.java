package soot.jimple.infoflow.problems.rules.backward;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;
import soot.Local;
import soot.PrimType;
import soot.RefType;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.FieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.aliasing.Aliasing;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.problems.TaintPropagationResults;
import soot.jimple.infoflow.problems.rules.AbstractTaintPropagationRule;
import soot.jimple.infoflow.sourcesSinks.manager.IReversibleSourceSinkManager;
import soot.jimple.infoflow.sourcesSinks.manager.SinkInfo;
import soot.jimple.infoflow.taintWrappers.EasyTaintWrapper;
import soot.jimple.infoflow.taintWrappers.IReversibleTaintWrapper;
import soot.jimple.infoflow.taintWrappers.ITaintPropagationWrapper;
import soot.jimple.infoflow.typing.TypeUtils;
import soot.jimple.infoflow.util.ByReferenceBoolean;
import soot.jimple.infoflow.util.preanalyses.SingleLiveVariableAnalysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/problems/rules/backward/BackwardsWrapperRule.class */
public class BackwardsWrapperRule extends AbstractTaintPropagationRule {
    private static final Set<String> excludeList = parseExcludeList();

    public BackwardsWrapperRule(InfoflowManager manager, Abstraction zeroValue, TaintPropagationResults results) {
        super(manager, zeroValue, results);
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateNormalFlow(Abstraction d1, Abstraction source, Stmt stmt, Stmt destStmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll) {
        return null;
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateCallFlow(Abstraction d1, Abstraction source, Stmt stmt, SootMethod dest, ByReferenceBoolean killAll) {
        ITaintPropagationWrapper wrapper = this.manager.getTaintWrapper();
        if (wrapper != null && wrapper.isExclusive(stmt, source)) {
            killAll.value = true;
            return null;
        }
        return null;
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateCallToReturnFlow(Abstraction d1, Abstraction source, Stmt stmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll) {
        Type t;
        if (source == this.zeroValue || this.manager.getTaintWrapper() == null || !(this.manager.getTaintWrapper() instanceof IReversibleTaintWrapper)) {
            return null;
        }
        IReversibleTaintWrapper wrapper = (IReversibleTaintWrapper) this.manager.getTaintWrapper();
        Aliasing aliasing = getAliasing();
        if (aliasing == null) {
            return null;
        }
        AccessPath sourceAp = source.getAccessPath();
        boolean isTainted = false;
        boolean retValTainted = false;
        Value leftOp = null;
        if (!sourceAp.isStaticFieldRef() && !sourceAp.isEmpty()) {
            InvokeExpr invokeExpr = stmt.getInvokeExpr();
            if (stmt instanceof AssignStmt) {
                leftOp = ((AssignStmt) stmt).getLeftOp();
                isTainted = aliasing.mayAlias(leftOp, sourceAp.getPlainValue());
                killSource.value = isTainted;
                retValTainted = isTainted;
            }
            if (!isTainted && (invokeExpr instanceof InstanceInvokeExpr)) {
                isTainted = aliasing.mayAlias(((InstanceInvokeExpr) invokeExpr).getBase(), sourceAp.getPlainValue());
            }
            if (!isTainted) {
                isTainted = (!(wrapper instanceof EasyTaintWrapper) || invokeExpr.getArgCount() < 3) ? invokeExpr.getArgs().stream().anyMatch(arg -> {
                    return ((arg.getType() instanceof PrimType) || TypeUtils.isStringType(arg.getType()) || !aliasing.mayAlias(arg, sourceAp.getPlainValue())) ? false : true;
                }) : aliasing.mayAlias(invokeExpr.getArg(2), sourceAp.getPlainValue());
            }
        }
        if (!isTainted) {
            return null;
        }
        if (!getManager().getConfig().getInspectSources() && this.manager.getSourceSinkManager() != null && (this.manager.getSourceSinkManager() instanceof IReversibleSourceSinkManager)) {
            SinkInfo sourceInfo = ((IReversibleSourceSinkManager) this.manager.getSourceSinkManager()).getInverseSourceInfo(stmt, this.manager, null);
            if (sourceInfo != null) {
                return null;
            }
        }
        Set<Abstraction> res = wrapper.getInverseTaintsForMethod(stmt, d1, source);
        if (res != null) {
            Set<Abstraction> hashSet = new HashSet<>();
            SootMethod sm = this.manager.getICFG().getMethodOf(stmt);
            boolean intraTurnUnit = source.getTurnUnit() != null && this.manager.getICFG().getMethodOf(source.getTurnUnit()) == sm;
            Iterator<Abstraction> it = res.iterator();
            while (it.hasNext()) {
                Abstraction abs = it.next();
                AccessPath absAp = abs.getAccessPath();
                boolean addAbstraction = true;
                if (leftOp != null && aliasing.mayAlias(leftOp, absAp.getPlainValue())) {
                    Value rightOp = ((AssignStmt) stmt).getRightOp();
                    boolean localNotReused = rightOp.getUseBoxes().stream().noneMatch(box -> {
                        return aliasing.mayAlias(box.getValue(), absAp.getPlainValue());
                    });
                    if (localNotReused) {
                        addAbstraction = false;
                    }
                }
                boolean performAliasSearch = true;
                if (retValTainted && leftOp != null) {
                    if (leftOp instanceof FieldRef) {
                        t = ((FieldRef) leftOp).getField().getType();
                    } else {
                        t = leftOp.getType();
                    }
                    boolean setTurnUnit = (t instanceof PrimType) || (TypeUtils.isStringType(t) && !absAp.getCanHaveImmutableAliases());
                    if (setTurnUnit) {
                        abs = abs.deriveNewAbstractionWithTurnUnit(stmt);
                        performAliasSearch = false;
                    }
                }
                if (performAliasSearch) {
                    if (!absAp.equals(sourceAp) && !absAp.isEmpty() && (!retValTainted || !intraTurnUnit || !canOmitAliasing(abs, stmt, sm))) {
                        boolean isBasicString = (!TypeUtils.isStringType(absAp.getBaseType()) || absAp.getCanHaveImmutableAliases() || getAliasing().isStringConstructorCall(stmt)) ? false : true;
                        boolean taintsObjectValue = (absAp.getBaseType() instanceof RefType) && !isBasicString && (absAp.getFragmentCount() > 0 || absAp.getTaintSubFields());
                        boolean taintsStaticField = (getManager().getConfig().getStaticFieldTrackingMode() == InfoflowConfiguration.StaticFieldTrackingMode.None || !abs.getAccessPath().isStaticFieldRef() || (absAp.getFirstFieldType() instanceof PrimType) || TypeUtils.isStringType(absAp.getFirstFieldType())) ? false : true;
                        if (taintsObjectValue || taintsStaticField || aliasing.canHaveAliasesRightSide(stmt, abs.getAccessPath().getPlainValue(), abs)) {
                            for (Unit pred : this.manager.getICFG().getPredsOf(stmt)) {
                                aliasing.computeAliases(d1, (Stmt) pred, absAp.getPlainValue(), hashSet, getManager().getICFG().getMethodOf(pred), abs);
                            }
                        } else {
                            abs = abs.deriveNewAbstractionWithTurnUnit(stmt);
                        }
                    }
                    if (!killSource.value && absAp.equals(sourceAp)) {
                        killSource.value = source != abs;
                    }
                }
                if (addAbstraction) {
                    hashSet.add(abs);
                }
            }
            res = hashSet;
        }
        if (res != null) {
            for (Abstraction abs2 : res) {
                if (abs2 != source) {
                    abs2.setCorrespondingCallSite(stmt);
                }
            }
        }
        return res;
    }

    private static Set<String> parseExcludeList() {
        try {
            return (Set) Files.lines(Paths.get("BackwardsWrapperExcludeList.txt", new String[0])).filter(p -> {
                return !p.startsWith("#");
            }).collect(Collectors.toSet());
        } catch (IOException e) {
            return Collections.emptySet();
        }
    }

    private boolean canOmitAliasing(Abstraction abs, Stmt callStmt, SootMethod sm) {
        if (!(callStmt instanceof AssignStmt)) {
            return false;
        }
        AssignStmt assignStmt = (AssignStmt) callStmt;
        if (!(assignStmt.getRightOp() instanceof InstanceInvokeExpr)) {
            return false;
        }
        InstanceInvokeExpr ie = (InstanceInvokeExpr) assignStmt.getRightOp();
        if (ie.getBase() == assignStmt.getLeftOp() || !getAliasing().mayAlias(ie.getBase(), abs.getAccessPath().getPlainValue()) || !excludeList.contains(ie.getMethod().getSignature())) {
            return false;
        }
        SingleLiveVariableAnalysis slva = new SingleLiveVariableAnalysis(this.manager.getICFG().getOrCreateUnitGraph(sm), (Local) ie.getBase(), abs.getTurnUnit());
        return slva.canOmitAlias(callStmt);
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateReturnFlow(Collection<Abstraction> callerD1s, Abstraction calleeD1, Abstraction source, Stmt stmt, Stmt retSite, Stmt callSite, ByReferenceBoolean killAll) {
        return null;
    }
}
