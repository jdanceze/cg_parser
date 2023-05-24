package soot.jimple.infoflow.problems;

import heros.FlowFunction;
import heros.FlowFunctions;
import heros.flowfunc.KillAll;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import soot.BooleanType;
import soot.Local;
import soot.NullType;
import soot.PrimType;
import soot.RefType;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InstanceOfExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.LengthExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.ReturnStmt;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.aliasing.Aliasing;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.handlers.TaintPropagationHandler;
import soot.jimple.infoflow.problems.rules.IPropagationRuleManagerFactory;
import soot.jimple.infoflow.solver.functions.SolverCallFlowFunction;
import soot.jimple.infoflow.solver.functions.SolverCallToReturnFlowFunction;
import soot.jimple.infoflow.solver.functions.SolverNormalFlowFunction;
import soot.jimple.infoflow.solver.functions.SolverReturnFlowFunction;
import soot.jimple.infoflow.typing.TypeUtils;
import soot.jimple.infoflow.util.BaseSelector;
import soot.jimple.infoflow.util.ByReferenceBoolean;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/problems/InfoflowProblem.class */
public class InfoflowProblem extends AbstractInfoflowProblem {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !InfoflowProblem.class.desiredAssertionStatus();
    }

    public InfoflowProblem(InfoflowManager manager, Abstraction zeroValue, IPropagationRuleManagerFactory ruleManagerFactory) {
        super(manager, zeroValue, ruleManagerFactory);
    }

    @Override // heros.template.DefaultIFDSTabulationProblem
    public FlowFunctions<Unit, Abstraction, SootMethod> createFlowFunctionsFactory() {
        return new FlowFunctions<Unit, Abstraction, SootMethod>() { // from class: soot.jimple.infoflow.problems.InfoflowProblem.1

            /* renamed from: soot.jimple.infoflow.problems.InfoflowProblem$1$NotifyingNormalFlowFunction */
            /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/problems/InfoflowProblem$1$NotifyingNormalFlowFunction.class */
            abstract class NotifyingNormalFlowFunction extends SolverNormalFlowFunction {
                protected final Stmt stmt;

                public abstract Set<Abstraction> computeTargetsInternal(Abstraction abstraction, Abstraction abstraction2);

                public NotifyingNormalFlowFunction(Stmt stmt) {
                    this.stmt = stmt;
                }

                @Override // soot.jimple.infoflow.solver.functions.SolverNormalFlowFunction
                public Set<Abstraction> computeTargets(Abstraction d1, Abstraction source) {
                    if (InfoflowProblem.this.taintPropagationHandler != null) {
                        InfoflowProblem.this.taintPropagationHandler.notifyFlowIn(this.stmt, source, InfoflowProblem.this.manager, TaintPropagationHandler.FlowFunctionType.NormalFlowFunction);
                    }
                    Set<Abstraction> res = computeTargetsInternal(d1, source);
                    return InfoflowProblem.this.notifyOutFlowHandlers(this.stmt, d1, source, res, TaintPropagationHandler.FlowFunctionType.NormalFlowFunction);
                }
            }

            private void addTaintViaStmt(Abstraction d1, AssignStmt assignStmt, Abstraction source, Set<Abstraction> taintSet, boolean cutFirstField, SootMethod method, Type targetType) {
                Value leftValue = assignStmt.getLeftOp();
                Value rightValue = assignStmt.getRightOp();
                if ((leftValue instanceof StaticFieldRef) && InfoflowProblem.this.manager.getConfig().getStaticFieldTrackingMode() == InfoflowConfiguration.StaticFieldTrackingMode.None) {
                    return;
                }
                Abstraction newAbs = null;
                if (!source.getAccessPath().isEmpty()) {
                    if ((leftValue instanceof ArrayRef) && targetType != null) {
                        ArrayRef arrayRef = (ArrayRef) leftValue;
                        targetType = TypeUtils.buildArrayOrAddDimension(targetType, arrayRef.getType().getArrayType());
                    }
                    if (rightValue instanceof CastExpr) {
                        CastExpr cast = (CastExpr) assignStmt.getRightOp();
                        targetType = cast.getType();
                    } else if (rightValue instanceof InstanceOfExpr) {
                        newAbs = source.deriveNewAbstraction(InfoflowProblem.this.manager.getAccessPathFactory().createAccessPath(leftValue, BooleanType.v(), true, AccessPath.ArrayTaintType.ContentsAndLength), assignStmt);
                    }
                } else if (!InfoflowProblem.$assertionsDisabled && targetType != null) {
                    throw new AssertionError();
                }
                AccessPath.ArrayTaintType arrayTaintType = source.getAccessPath().getArrayTaintType();
                if ((leftValue instanceof ArrayRef) && InfoflowProblem.this.manager.getConfig().getEnableArraySizeTainting()) {
                    arrayTaintType = AccessPath.ArrayTaintType.Contents;
                }
                if (newAbs == null) {
                    if (source.getAccessPath().isEmpty()) {
                        newAbs = source.deriveNewAbstraction(InfoflowProblem.this.manager.getAccessPathFactory().createAccessPath(leftValue, true), assignStmt, true);
                    } else {
                        AccessPath ap = InfoflowProblem.this.manager.getAccessPathFactory().copyWithNewValue(source.getAccessPath(), leftValue, targetType, cutFirstField, true, arrayTaintType);
                        newAbs = source.deriveNewAbstraction(ap, assignStmt);
                    }
                }
                if (newAbs != null) {
                    if ((leftValue instanceof StaticFieldRef) && InfoflowProblem.this.manager.getConfig().getStaticFieldTrackingMode() == InfoflowConfiguration.StaticFieldTrackingMode.ContextFlowInsensitive) {
                        InfoflowProblem.this.manager.getGlobalTaintManager().addToGlobalTaintState(newAbs);
                        return;
                    }
                    taintSet.add(newAbs);
                    Aliasing aliasing = InfoflowProblem.this.manager.getAliasing();
                    if (aliasing != null && aliasing.canHaveAliases(assignStmt, leftValue, newAbs)) {
                        aliasing.computeAliases(d1, assignStmt, leftValue, taintSet, method, newAbs);
                    }
                }
            }

            private boolean hasValidCallees(Unit call) {
                Collection<SootMethod> callees = InfoflowProblem.this.interproceduralCFG().getCalleesOfCallAt(call);
                for (SootMethod callee : callees) {
                    if (callee.isConcrete()) {
                        return true;
                    }
                }
                return false;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public Set<Abstraction> createNewTaintOnAssignment(AssignStmt assignStmt, Value[] rightVals, Abstraction d1, Abstraction newSource) {
                Value leftValue = assignStmt.getLeftOp();
                Value rightValue = assignStmt.getRightOp();
                boolean addLeftValue = false;
                if (rightValue instanceof LengthExpr) {
                    return Collections.singleton(newSource);
                }
                boolean implicitTaint = (newSource.getTopPostdominator() == null || newSource.getTopPostdominator().getUnit() == null) ? false : true;
                if (implicitTaint | newSource.getAccessPath().isEmpty()) {
                    if ((d1 == null || d1.getAccessPath().isEmpty()) && !(leftValue instanceof FieldRef)) {
                        return Collections.singleton(newSource);
                    }
                    if (newSource.getAccessPath().isEmpty()) {
                        addLeftValue = true;
                    }
                }
                boolean aliasOverwritten = (addLeftValue || newSource.isAbstractionActive() || !Aliasing.baseMatchesStrict(rightValue, newSource) || !(rightValue.getType() instanceof RefType) || newSource.dependsOnCutAP()) ? false : true;
                Aliasing aliasing = InfoflowProblem.this.manager.getAliasing();
                if (aliasing == null) {
                    return null;
                }
                boolean cutFirstField = false;
                AccessPath mappedAP = newSource.getAccessPath();
                Type targetType = null;
                if (!addLeftValue && !aliasOverwritten) {
                    for (Value rightVal : rightVals) {
                        if (rightVal instanceof FieldRef) {
                            FieldRef rightRef = (FieldRef) rightVal;
                            if ((rightRef instanceof InstanceFieldRef) && (((InstanceFieldRef) rightRef).getBase().getType() instanceof NullType)) {
                                return null;
                            }
                            mappedAP = aliasing.mayAlias(newSource.getAccessPath(), rightRef);
                            if (rightVal instanceof StaticFieldRef) {
                                if (InfoflowProblem.this.manager.getConfig().getStaticFieldTrackingMode() != InfoflowConfiguration.StaticFieldTrackingMode.None && mappedAP != null) {
                                    addLeftValue = true;
                                    cutFirstField = true;
                                }
                            } else if (rightVal instanceof InstanceFieldRef) {
                                Local rightBase = (Local) ((InstanceFieldRef) rightRef).getBase();
                                Local sourceBase = newSource.getAccessPath().getPlainValue();
                                SootField rightField = rightRef.getField();
                                if (mappedAP != null) {
                                    addLeftValue = true;
                                    cutFirstField = mappedAP.getFragmentCount() > 0 && mappedAP.getFirstField() == rightField;
                                } else if (aliasing.mayAlias(rightBase, sourceBase) && newSource.getAccessPath().getFragmentCount() == 0 && newSource.getAccessPath().getTaintSubFields()) {
                                    addLeftValue = true;
                                    targetType = rightField.getType();
                                    if (mappedAP == null) {
                                        mappedAP = InfoflowProblem.this.manager.getAccessPathFactory().createAccessPath(rightBase, true);
                                    }
                                }
                            }
                        } else if ((rightVal instanceof Local) && newSource.getAccessPath().isInstanceFieldRef()) {
                            Local base = newSource.getAccessPath().getPlainValue();
                            if (aliasing.mayAlias(rightVal, base)) {
                                addLeftValue = true;
                                targetType = newSource.getAccessPath().getBaseType();
                            }
                        } else if (aliasing.mayAlias(rightVal, newSource.getAccessPath().getPlainValue()) && !(assignStmt.getRightOp() instanceof NewArrayExpr) && (InfoflowProblem.this.manager.getConfig().getEnableArraySizeTainting() || !(rightValue instanceof NewArrayExpr))) {
                            addLeftValue = true;
                            targetType = newSource.getAccessPath().getBaseType();
                        }
                        if (addLeftValue) {
                            break;
                        }
                    }
                }
                if (!addLeftValue) {
                    return null;
                }
                if (!newSource.isAbstractionActive() && ((assignStmt.getLeftOp().getType() instanceof PrimType) || (TypeUtils.isStringType(assignStmt.getLeftOp().getType()) && !newSource.getAccessPath().getCanHaveImmutableAliases()))) {
                    return Collections.singleton(newSource);
                }
                Set<Abstraction> res = new HashSet<>();
                Abstraction targetAB = mappedAP.equals(newSource.getAccessPath()) ? newSource : newSource.deriveNewAbstraction(mappedAP, null);
                addTaintViaStmt(d1, assignStmt, targetAB, res, cutFirstField, InfoflowProblem.this.interproceduralCFG().getMethodOf(assignStmt), targetType);
                res.add(newSource);
                return res;
            }

            @Override // heros.FlowFunctions
            public FlowFunction<Abstraction> getNormalFlowFunction(final Unit src, final Unit dest) {
                if (!(src instanceof Stmt)) {
                    return KillAll.v();
                }
                return new NotifyingNormalFlowFunction(this, (Stmt) src) { // from class: soot.jimple.infoflow.problems.InfoflowProblem.1.1
                    @Override // soot.jimple.infoflow.problems.InfoflowProblem.AnonymousClass1.NotifyingNormalFlowFunction
                    public Set<Abstraction> computeTargetsInternal(Abstraction d1, Abstraction source) {
                        Abstraction newSource;
                        if (!source.isAbstractionActive() && src == source.getActivationUnit()) {
                            newSource = source.getActiveCopy();
                        } else {
                            newSource = source;
                        }
                        ByReferenceBoolean killSource = new ByReferenceBoolean();
                        ByReferenceBoolean killAll = new ByReferenceBoolean();
                        Set<Abstraction> res = InfoflowProblem.this.propagationRules.applyNormalFlowFunction(d1, newSource, this.stmt, (Stmt) dest, killSource, killAll);
                        if (killAll.value) {
                            return Collections.emptySet();
                        }
                        if (src instanceof AssignStmt) {
                            AssignStmt assignStmt = (AssignStmt) src;
                            Value right = assignStmt.getRightOp();
                            Value[] rightVals = BaseSelector.selectBaseList(right, true);
                            Set<Abstraction> resAssign = createNewTaintOnAssignment(assignStmt, rightVals, d1, newSource);
                            if (resAssign != null && !resAssign.isEmpty()) {
                                if (res != null) {
                                    res.addAll(resAssign);
                                    return res;
                                }
                                res = resAssign;
                            }
                        }
                        return (res == null || res.isEmpty()) ? Collections.emptySet() : res;
                    }
                };
            }

            @Override // heros.FlowFunctions
            public FlowFunction<Abstraction> getCallFlowFunction(final Unit src, final SootMethod dest) {
                if (!dest.isConcrete()) {
                    InfoflowProblem.this.logger.debug("Call skipped because target has no body: {} -> {}", src, dest);
                    return KillAll.v();
                }
                final Stmt stmt = (Stmt) src;
                final InvokeExpr ie = (stmt == null || !stmt.containsInvokeExpr()) ? null : stmt.getInvokeExpr();
                final Local[] paramLocals = (Local[]) dest.getActiveBody().getParameterLocals().toArray(new Local[0]);
                final Local thisLocal = dest.isStatic() ? null : dest.getActiveBody().getThisLocal();
                final Aliasing aliasing = InfoflowProblem.this.manager.getAliasing();
                if (aliasing == null) {
                    return KillAll.v();
                }
                return new SolverCallFlowFunction() { // from class: soot.jimple.infoflow.problems.InfoflowProblem.1.2
                    @Override // soot.jimple.infoflow.solver.functions.SolverCallFlowFunction
                    public Set<Abstraction> computeTargets(Abstraction d1, Abstraction source) {
                        Set<Abstraction> res = computeTargetsInternal(d1, source);
                        if (res != null && !res.isEmpty() && d1 != null) {
                            for (Abstraction abs : res) {
                                aliasing.getAliasingStrategy().injectCallingContext(abs, InfoflowProblem.this.solver, dest, src, source, d1);
                            }
                        }
                        return InfoflowProblem.this.notifyOutFlowHandlers(stmt, d1, source, res, TaintPropagationHandler.FlowFunctionType.CallFlowFunction);
                    }

                    private Set<Abstraction> computeTargetsInternal(Abstraction d1, Abstraction source) {
                        if ((!InfoflowProblem.this.manager.getConfig().getStopAfterFirstFlow() || InfoflowProblem.this.results.isEmpty()) && source != InfoflowProblem.this.getZeroValue() && !InfoflowProblem.this.isExcluded(dest)) {
                            if (InfoflowProblem.this.taintPropagationHandler != null) {
                                InfoflowProblem.this.taintPropagationHandler.notifyFlowIn(stmt, source, InfoflowProblem.this.manager, TaintPropagationHandler.FlowFunctionType.CallFlowFunction);
                            }
                            if (!source.isAbstractionActive() && source.getActivationUnit() == src) {
                                source = source.getActiveCopy();
                            }
                            ByReferenceBoolean killAll = new ByReferenceBoolean();
                            Set<Abstraction> res = InfoflowProblem.this.propagationRules.applyCallFlowFunction(d1, source, stmt, dest, killAll);
                            if (killAll.value) {
                                return null;
                            }
                            Set<AccessPath> resMapping = mapAccessPathToCallee(dest, ie, paramLocals, thisLocal, source.getAccessPath());
                            if (resMapping == null) {
                                return res;
                            }
                            Set<Abstraction> resAbs = new HashSet<>(resMapping.size());
                            if (res != null && !res.isEmpty()) {
                                resAbs.addAll(res);
                            }
                            for (AccessPath ap : resMapping) {
                                if (ap != null && (aliasing.getAliasingStrategy().isLazyAnalysis() || source.isImplicit() || InfoflowProblem.this.interproceduralCFG().methodReadsValue(dest, ap.getPlainValue()))) {
                                    Abstraction newAbs = source.deriveNewAbstraction(ap, stmt);
                                    if (newAbs != null) {
                                        resAbs.add(newAbs);
                                    }
                                }
                            }
                            return resAbs;
                        }
                        return null;
                    }
                };
            }

            @Override // heros.FlowFunctions
            public FlowFunction<Abstraction> getReturnFlowFunction(final Unit callSite, final SootMethod callee, final Unit exitStmt, final Unit retSite) {
                if (callSite != null && !(callSite instanceof Stmt)) {
                    return KillAll.v();
                }
                final Stmt iCallStmt = (Stmt) callSite;
                final boolean isReflectiveCallSite = callSite != null && InfoflowProblem.this.interproceduralCFG().isReflectiveCallSite(callSite);
                final ReturnStmt returnStmt = exitStmt instanceof ReturnStmt ? (ReturnStmt) exitStmt : null;
                final Local[] paramLocals = (Local[]) callee.getActiveBody().getParameterLocals().toArray(new Local[0]);
                final Aliasing aliasing = InfoflowProblem.this.manager.getAliasing();
                if (aliasing == null) {
                    return KillAll.v();
                }
                final Local thisLocal = callee.isStatic() ? null : callee.getActiveBody().getThisLocal();
                return new SolverReturnFlowFunction() { // from class: soot.jimple.infoflow.problems.InfoflowProblem.1.3
                    @Override // soot.jimple.infoflow.solver.functions.SolverReturnFlowFunction
                    public Set<Abstraction> computeTargets(Abstraction source, Abstraction d1, Collection<Abstraction> callerD1s) {
                        Set<Abstraction> res = computeTargetsInternal(source, d1, callerD1s);
                        return InfoflowProblem.this.notifyOutFlowHandlers(exitStmt, d1, source, res, TaintPropagationHandler.FlowFunctionType.ReturnFlowFunction);
                    }

                    private Set<Abstraction> computeTargetsInternal(Abstraction source, Abstraction calleeD1, Collection<Abstraction> callerD1s) {
                        int i;
                        Value base;
                        if ((!InfoflowProblem.this.manager.getConfig().getStopAfterFirstFlow() || InfoflowProblem.this.results.isEmpty()) && source != InfoflowProblem.this.getZeroValue()) {
                            if (InfoflowProblem.this.taintPropagationHandler != null) {
                                InfoflowProblem.this.taintPropagationHandler.notifyFlowIn(exitStmt, source, InfoflowProblem.this.manager, TaintPropagationHandler.FlowFunctionType.ReturnFlowFunction);
                            }
                            boolean callerD1sConditional = false;
                            Iterator<Abstraction> it = callerD1s.iterator();
                            while (true) {
                                if (!it.hasNext()) {
                                    break;
                                }
                                Abstraction d1 = it.next();
                                if (d1.getAccessPath().isEmpty()) {
                                    callerD1sConditional = true;
                                    break;
                                }
                            }
                            Abstraction newSource = source;
                            if (!source.isAbstractionActive() && callSite != null && (callSite == source.getActivationUnit() || InfoflowProblem.this.isCallSiteActivatingTaint(callSite, source.getActivationUnit()))) {
                                newSource = source.getActiveCopy();
                            }
                            if (!newSource.isAbstractionActive() && newSource.getActivationUnit() != null && InfoflowProblem.this.interproceduralCFG().getMethodOf(newSource.getActivationUnit()) == callee) {
                                return null;
                            }
                            ByReferenceBoolean killAll = new ByReferenceBoolean();
                            Set<Abstraction> res = InfoflowProblem.this.propagationRules.applyReturnFlowFunction(callerD1s, calleeD1, newSource, (Stmt) exitStmt, (Stmt) retSite, (Stmt) callSite, killAll);
                            if (killAll.value) {
                                return null;
                            }
                            if (res == null) {
                                res = new HashSet<>();
                            }
                            if (callSite == null) {
                                return null;
                            }
                            if (aliasing.getAliasingStrategy().isLazyAnalysis() && Aliasing.canHaveAliases(newSource.getAccessPath())) {
                                res.add(newSource);
                            }
                            if (!newSource.getAccessPath().isStaticFieldRef() && !callee.isStaticInitializer()) {
                                if (returnStmt != null && (callSite instanceof DefinitionStmt)) {
                                    Value retLocal = returnStmt.getOp();
                                    Value leftOp = ((DefinitionStmt) callSite).getLeftOp();
                                    if (aliasing.mayAlias(retLocal, newSource.getAccessPath().getPlainValue()) && !InfoflowProblem.this.isExceptionHandler(retSite)) {
                                        AccessPath ap = InfoflowProblem.this.manager.getAccessPathFactory().copyWithNewValue(newSource.getAccessPath(), leftOp);
                                        Abstraction abs = newSource.deriveNewAbstraction(ap, (Stmt) exitStmt);
                                        if (abs != null) {
                                            res.add(abs);
                                            if (aliasing.getAliasingStrategy().requiresAnalysisOnReturn()) {
                                                for (Abstraction d12 : callerD1s) {
                                                    aliasing.computeAliases(d12, iCallStmt, leftOp, res, InfoflowProblem.this.interproceduralCFG().getMethodOf(callSite), abs);
                                                }
                                            }
                                        }
                                    }
                                }
                                Value sourceBase = newSource.getAccessPath().getPlainValue();
                                boolean parameterAliases = false;
                                for (i = 0; i < callee.getParameterCount(); i = i + 1) {
                                    if ((callSite instanceof DefinitionStmt) && !InfoflowProblem.this.isExceptionHandler(retSite)) {
                                        DefinitionStmt defnStmt = (DefinitionStmt) callSite;
                                        Value leftOp2 = defnStmt.getLeftOp();
                                        i = defnStmt.getInvokeExpr().getArg(i) == leftOp2 ? i + 1 : 0;
                                    }
                                    if (aliasing.mayAlias(paramLocals[i], sourceBase)) {
                                        parameterAliases = true;
                                        Value originalCallArg = iCallStmt.getInvokeExpr().getArg(isReflectiveCallSite ? 1 : i);
                                        if (AccessPath.canContainValue(originalCallArg) && ((isReflectiveCallSite || InfoflowProblem.this.manager.getTypeUtils().checkCast(source.getAccessPath(), originalCallArg.getType())) && source.getAccessPath().getTaintSubFields() && !(source.getAccessPath().getBaseType() instanceof PrimType) && ((!TypeUtils.isStringType(source.getAccessPath().getBaseType()) || source.getAccessPath().getCanHaveImmutableAliases()) && !InfoflowProblem.this.interproceduralCFG().methodWritesValue(callee, paramLocals[i])))) {
                                            AccessPath ap2 = InfoflowProblem.this.manager.getAccessPathFactory().copyWithNewValue(newSource.getAccessPath(), originalCallArg, isReflectiveCallSite ? null : newSource.getAccessPath().getBaseType(), false);
                                            Abstraction abs2 = newSource.deriveNewAbstraction(ap2, (Stmt) exitStmt);
                                            if (abs2 != null) {
                                                res.add(abs2);
                                            }
                                        }
                                    }
                                }
                                boolean thisAliases = false;
                                if ((callSite instanceof DefinitionStmt) && !InfoflowProblem.this.isExceptionHandler(retSite)) {
                                    Value leftOp3 = ((DefinitionStmt) callSite).getLeftOp();
                                    if (thisLocal == leftOp3) {
                                        thisAliases = true;
                                    }
                                }
                                if (!parameterAliases && !thisAliases && source.getAccessPath().getTaintSubFields() && (iCallStmt.getInvokeExpr() instanceof InstanceInvokeExpr) && aliasing.mayAlias(thisLocal, sourceBase) && InfoflowProblem.this.manager.getTypeUtils().checkCast(source.getAccessPath(), thisLocal.getType())) {
                                    InstanceInvokeExpr iIExpr = (InstanceInvokeExpr) iCallStmt.getInvokeExpr();
                                    if (InfoflowProblem.this.interproceduralCFG().isReflectiveCallSite(iIExpr)) {
                                        base = iIExpr.getArg(0);
                                    } else {
                                        base = iIExpr.getBase();
                                    }
                                    Value callerBaseLocal = base;
                                    AccessPath ap3 = InfoflowProblem.this.manager.getAccessPathFactory().copyWithNewValue(newSource.getAccessPath(), callerBaseLocal, isReflectiveCallSite ? null : newSource.getAccessPath().getBaseType(), false);
                                    Abstraction abs3 = newSource.deriveNewAbstraction(ap3, (Stmt) exitStmt);
                                    if (abs3 != null) {
                                        res.add(abs3);
                                    }
                                }
                            }
                            for (Abstraction abs4 : res) {
                                if ((abs4.isImplicit() && !callerD1sConditional) || aliasing.getAliasingStrategy().requiresAnalysisOnReturn()) {
                                    for (Abstraction d13 : callerD1s) {
                                        aliasing.computeAliases(d13, iCallStmt, null, res, InfoflowProblem.this.interproceduralCFG().getMethodOf(callSite), abs4);
                                    }
                                }
                                if (abs4 != newSource) {
                                    abs4.setCorrespondingCallSite(iCallStmt);
                                }
                            }
                            return res;
                        }
                        return null;
                    }
                };
            }

            @Override // heros.FlowFunctions
            public FlowFunction<Abstraction> getCallToReturnFlowFunction(final Unit call, Unit returnSite) {
                boolean z;
                boolean z2;
                if (!(call instanceof Stmt)) {
                    return KillAll.v();
                }
                final Stmt iCallStmt = (Stmt) call;
                final InvokeExpr invExpr = iCallStmt.getInvokeExpr();
                final Aliasing aliasing = InfoflowProblem.this.manager.getAliasing();
                if (aliasing == null) {
                    return KillAll.v();
                }
                final Value[] callArgs = new Value[invExpr.getArgCount()];
                for (int i = 0; i < invExpr.getArgCount(); i++) {
                    callArgs[i] = invExpr.getArg(i);
                }
                if (InfoflowProblem.this.manager.getSourceSinkManager() != null) {
                    z = InfoflowProblem.this.manager.getSourceSinkManager().getSinkInfo(iCallStmt, InfoflowProblem.this.manager, null) != null;
                } else {
                    z = false;
                }
                final boolean isSink = z;
                if (InfoflowProblem.this.manager.getSourceSinkManager() != null) {
                    z2 = InfoflowProblem.this.manager.getSourceSinkManager().getSourceInfo(iCallStmt, InfoflowProblem.this.manager) != null;
                } else {
                    z2 = false;
                }
                final boolean isSource = z2;
                final SootMethod callee = invExpr.getMethod();
                final boolean hasValidCallees = hasValidCallees(call);
                return new SolverCallToReturnFlowFunction() { // from class: soot.jimple.infoflow.problems.InfoflowProblem.1.4
                    @Override // soot.jimple.infoflow.solver.functions.SolverCallToReturnFlowFunction
                    public Set<Abstraction> computeTargets(Abstraction d1, Abstraction source) {
                        Set<Abstraction> res = computeTargetsInternal(d1, source);
                        return InfoflowProblem.this.notifyOutFlowHandlers(call, d1, source, res, TaintPropagationHandler.FlowFunctionType.CallToReturnFlowFunction);
                    }

                    private Set<Abstraction> computeTargetsInternal(Abstraction d1, Abstraction source) {
                        Abstraction newSource;
                        Set<AccessPath> calleeAPs;
                        if (!InfoflowProblem.this.manager.getConfig().getStopAfterFirstFlow() || InfoflowProblem.this.results.isEmpty()) {
                            if (InfoflowProblem.this.taintPropagationHandler != null) {
                                InfoflowProblem.this.taintPropagationHandler.notifyFlowIn(call, source, InfoflowProblem.this.manager, TaintPropagationHandler.FlowFunctionType.CallToReturnFlowFunction);
                            }
                            if (!source.isAbstractionActive() && (call == source.getActivationUnit() || InfoflowProblem.this.isCallSiteActivatingTaint(call, source.getActivationUnit()))) {
                                newSource = source.getActiveCopy();
                            } else {
                                newSource = source;
                            }
                            ByReferenceBoolean killSource = new ByReferenceBoolean();
                            ByReferenceBoolean killAll = new ByReferenceBoolean();
                            Set<Abstraction> res = InfoflowProblem.this.propagationRules.applyCallToReturnFlowFunction(d1, newSource, iCallStmt, killSource, killAll, true);
                            if (killAll.value) {
                                return null;
                            }
                            boolean passOn = !killSource.value;
                            if (source == InfoflowProblem.this.getZeroValue()) {
                                return (res == null || res.isEmpty()) ? Collections.emptySet() : res;
                            }
                            if (res == null) {
                                res = new HashSet<>();
                            }
                            if (newSource.getTopPostdominator() != null && newSource.getTopPostdominator().getUnit() == null) {
                                return Collections.singleton(newSource);
                            }
                            if (newSource.getAccessPath().isStaticFieldRef()) {
                                passOn = false;
                            }
                            boolean isPrimitiveOrString = false;
                            if (source.getAccessPath().getBaseType() instanceof PrimType) {
                                isPrimitiveOrString = true;
                            }
                            if (TypeUtils.isStringType(source.getAccessPath().getBaseType()) && !source.getAccessPath().getCanHaveImmutableAliases()) {
                                isPrimitiveOrString = true;
                            }
                            if (passOn && (invExpr instanceof InstanceInvokeExpr) && ((InfoflowProblem.this.manager.getConfig().getInspectSources() || !isSource) && ((InfoflowProblem.this.manager.getConfig().getInspectSinks() || !isSink) && !isPrimitiveOrString && newSource.getAccessPath().isInstanceFieldRef() && (hasValidCallees || (InfoflowProblem.this.taintWrapper != null && InfoflowProblem.this.taintWrapper.isExclusive(iCallStmt, newSource)))))) {
                                Collection<SootMethod> callees = InfoflowProblem.this.interproceduralCFG().getCalleesOfCallAt(call);
                                boolean allCalleesRead = !callees.isEmpty();
                                Iterator<SootMethod> it = callees.iterator();
                                loop0: while (true) {
                                    if (!it.hasNext()) {
                                        break;
                                    }
                                    SootMethod callee2 = it.next();
                                    if (callee2.isConcrete() && callee2.hasActiveBody() && (calleeAPs = mapAccessPathToCallee(callee2, invExpr, null, null, source.getAccessPath())) != null) {
                                        for (AccessPath ap : calleeAPs) {
                                            if (ap != null && !InfoflowProblem.this.interproceduralCFG().methodReadsValue(callee2, ap.getPlainValue())) {
                                                allCalleesRead = false;
                                                break loop0;
                                            }
                                        }
                                    }
                                    if (InfoflowProblem.this.isExcluded(callee2)) {
                                        allCalleesRead = false;
                                        break;
                                    }
                                }
                                if (allCalleesRead) {
                                    if (aliasing.mayAlias(((InstanceInvokeExpr) invExpr).getBase(), newSource.getAccessPath().getPlainValue())) {
                                        passOn = false;
                                    }
                                    if (passOn) {
                                        int i2 = 0;
                                        while (true) {
                                            if (i2 < callArgs.length) {
                                                if (!aliasing.mayAlias(callArgs[i2], newSource.getAccessPath().getPlainValue())) {
                                                    i2++;
                                                } else {
                                                    passOn = false;
                                                    break;
                                                }
                                            } else {
                                                break;
                                            }
                                        }
                                    }
                                    if (newSource.getAccessPath().isStaticFieldRef()) {
                                        passOn = false;
                                    }
                                }
                            }
                            if (source.getAccessPath().isStaticFieldRef() && !InfoflowProblem.this.interproceduralCFG().isStaticFieldUsed(callee, source.getAccessPath().getFirstField())) {
                                passOn = true;
                            }
                            if ((passOn | (source.getTopPostdominator() != null || source.getAccessPath().isEmpty())) && newSource != InfoflowProblem.this.getZeroValue()) {
                                res.add(newSource);
                            }
                            if (callee.isNative() && InfoflowProblem.this.ncHandler != null) {
                                Value[] valueArr = callArgs;
                                int length = valueArr.length;
                                int i3 = 0;
                                while (true) {
                                    if (i3 >= length) {
                                        break;
                                    }
                                    Value callVal = valueArr[i3];
                                    if (callVal != newSource.getAccessPath().getPlainValue()) {
                                        i3++;
                                    } else {
                                        Set<Abstraction> nativeAbs = InfoflowProblem.this.ncHandler.getTaintedValues(iCallStmt, newSource, callArgs);
                                        if (nativeAbs != null) {
                                            res.addAll(nativeAbs);
                                            for (Abstraction abs : nativeAbs) {
                                                if (abs.getAccessPath().isStaticFieldRef() || aliasing.canHaveAliases(iCallStmt, abs.getAccessPath().getCompleteValue(), abs)) {
                                                    aliasing.computeAliases(d1, iCallStmt, abs.getAccessPath().getPlainValue(), res, InfoflowProblem.this.interproceduralCFG().getMethodOf(call), abs);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            for (Abstraction abs2 : res) {
                                if (abs2 != newSource) {
                                    abs2.setCorrespondingCallSite(iCallStmt);
                                }
                            }
                            return res;
                        }
                        return null;
                    }
                };
            }

            /* JADX INFO: Access modifiers changed from: private */
            public Set<AccessPath> mapAccessPathToCallee(SootMethod callee, InvokeExpr ie, Value[] paramLocals, Local thisLocal, AccessPath ap) {
                boolean isReflectiveCallSite;
                if (ap.isEmpty()) {
                    return null;
                }
                boolean isExecutorExecute = InfoflowProblem.this.interproceduralCFG().isExecutorExecute(ie, callee);
                Set<AccessPath> res = null;
                Aliasing aliasing = InfoflowProblem.this.manager.getAliasing();
                if (aliasing == null) {
                    return null;
                }
                if (aliasing.getAliasingStrategy().isLazyAnalysis() && Aliasing.canHaveAliases(ap)) {
                    res = new HashSet<>();
                    res.add(ap);
                }
                Value baseLocal = null;
                if (!isExecutorExecute && !ap.isStaticFieldRef() && !callee.isStatic()) {
                    if (InfoflowProblem.this.interproceduralCFG().isReflectiveCallSite(ie)) {
                        baseLocal = ie.getArg(0);
                    } else if (!InfoflowProblem.$assertionsDisabled && !(ie instanceof InstanceInvokeExpr)) {
                        throw new AssertionError();
                    } else {
                        InstanceInvokeExpr vie = (InstanceInvokeExpr) ie;
                        baseLocal = vie.getBase();
                    }
                }
                if (baseLocal != null && aliasing.mayAlias(baseLocal, ap.getPlainValue()) && InfoflowProblem.this.manager.getTypeUtils().hasCompatibleTypesForCall(ap, callee.getDeclaringClass())) {
                    if (res == null) {
                        res = new HashSet<>();
                    }
                    if (thisLocal == null) {
                        thisLocal = callee.getActiveBody().getThisLocal();
                    }
                    res.add(InfoflowProblem.this.manager.getAccessPathFactory().copyWithNewValue(ap, thisLocal));
                }
                int calleeParamCount = callee.getParameterCount();
                if (isExecutorExecute) {
                    if (aliasing.mayAlias(ie.getArg(0), ap.getPlainValue())) {
                        if (res == null) {
                            res = new HashSet<>();
                        }
                        res.add(InfoflowProblem.this.manager.getAccessPathFactory().copyWithNewValue(ap, callee.getActiveBody().getThisLocal()));
                    }
                } else if (calleeParamCount > 0 && ((isReflectiveCallSite = InfoflowProblem.this.interproceduralCFG().isReflectiveCallSite(ie)) || ie.getArgCount() == calleeParamCount)) {
                    for (int i = isReflectiveCallSite ? 1 : 0; i < ie.getArgCount(); i++) {
                        if (aliasing.mayAlias(ie.getArg(i), ap.getPlainValue())) {
                            if (res == null) {
                                res = new HashSet<>();
                            }
                            if (paramLocals == null) {
                                paramLocals = (Value[]) callee.getActiveBody().getParameterLocals().toArray(new Local[calleeParamCount]);
                            }
                            if (isReflectiveCallSite) {
                                for (Value value : paramLocals) {
                                    AccessPath newAP = InfoflowProblem.this.manager.getAccessPathFactory().copyWithNewValue(ap, value, null, false);
                                    if (newAP != null) {
                                        res.add(newAP);
                                    }
                                }
                            } else {
                                AccessPath newAP2 = InfoflowProblem.this.manager.getAccessPathFactory().copyWithNewValue(ap, paramLocals[i]);
                                if (newAP2 != null) {
                                    res.add(newAP2);
                                }
                            }
                        }
                    }
                }
                return res;
            }
        };
    }
}
