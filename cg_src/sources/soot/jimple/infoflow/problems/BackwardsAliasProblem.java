package soot.jimple.infoflow.problems;

import heros.FlowFunction;
import heros.FlowFunctions;
import heros.flowfunc.Identity;
import heros.flowfunc.KillAll;
import heros.solver.PathEdge;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import soot.ArrayType;
import soot.Local;
import soot.PrimType;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.IdentityStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InstanceOfExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.ReturnStmt;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.UnopExpr;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.aliasing.Aliasing;
import soot.jimple.infoflow.collect.MutableTwoElementSet;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.handlers.TaintPropagationHandler;
import soot.jimple.infoflow.problems.rules.EmptyPropagationRuleManagerFactory;
import soot.jimple.infoflow.solver.functions.SolverCallFlowFunction;
import soot.jimple.infoflow.solver.functions.SolverCallToReturnFlowFunction;
import soot.jimple.infoflow.solver.functions.SolverNormalFlowFunction;
import soot.jimple.infoflow.solver.functions.SolverReturnFlowFunction;
import soot.jimple.infoflow.typing.TypeUtils;
import soot.jimple.infoflow.util.BaseSelector;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/problems/BackwardsAliasProblem.class */
public class BackwardsAliasProblem extends AbstractInfoflowProblem {
    private static final boolean DEBUG_PRINT = false;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !BackwardsAliasProblem.class.desiredAssertionStatus();
    }

    public BackwardsAliasProblem(InfoflowManager manager) {
        super(manager, null, EmptyPropagationRuleManagerFactory.INSTANCE);
    }

    @Override // heros.template.DefaultIFDSTabulationProblem
    protected FlowFunctions<Unit, Abstraction, SootMethod> createFlowFunctionsFactory() {
        return new FlowFunctions<Unit, Abstraction, SootMethod>() { // from class: soot.jimple.infoflow.problems.BackwardsAliasProblem.1
            /* JADX INFO: Access modifiers changed from: private */
            public Abstraction checkAbstraction(Abstraction abs) {
                if (abs == null) {
                    return null;
                }
                if (!abs.getAccessPath().isStaticFieldRef()) {
                    if (abs.getAccessPath().getBaseType() instanceof PrimType) {
                        return null;
                    }
                } else if (abs.getAccessPath().getFirstFieldType() instanceof PrimType) {
                    return null;
                }
                return abs;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public boolean isCircularType(Value val) {
                if (!(val instanceof InstanceFieldRef)) {
                    return false;
                }
                InstanceFieldRef ref = (InstanceFieldRef) val;
                return ref.getBase().getType() == ref.getField().getType();
            }

            @Override // heros.FlowFunctions
            public FlowFunction<Abstraction> getNormalFlowFunction(final Unit srcUnit, Unit destUnit) {
                if (!(srcUnit instanceof DefinitionStmt)) {
                    return Identity.v();
                }
                final DefinitionStmt defStmt = (DefinitionStmt) srcUnit;
                return new SolverNormalFlowFunction() { // from class: soot.jimple.infoflow.problems.BackwardsAliasProblem.1.1
                    @Override // soot.jimple.infoflow.solver.functions.SolverNormalFlowFunction
                    public Set<Abstraction> computeTargets(Abstraction d1, Abstraction source) {
                        if (source == BackwardsAliasProblem.this.getZeroValue()) {
                            return null;
                        }
                        if (BackwardsAliasProblem.$assertionsDisabled || !source.getAccessPath().isEmpty()) {
                            if (BackwardsAliasProblem.this.taintPropagationHandler != null) {
                                BackwardsAliasProblem.this.taintPropagationHandler.notifyFlowIn(srcUnit, source, BackwardsAliasProblem.this.manager, TaintPropagationHandler.FlowFunctionType.NormalFlowFunction);
                            }
                            if (source.getTurnUnit() == srcUnit) {
                                return BackwardsAliasProblem.this.notifyOutFlowHandlers(srcUnit, d1, source, null, TaintPropagationHandler.FlowFunctionType.NormalFlowFunction);
                            }
                            Set<Abstraction> res = computeAliases(defStmt, d1, source);
                            return BackwardsAliasProblem.this.notifyOutFlowHandlers(srcUnit, d1, source, res, TaintPropagationHandler.FlowFunctionType.NormalFlowFunction);
                        }
                        throw new AssertionError();
                    }

                    private Set<Abstraction> computeAliases(DefinitionStmt defStmt2, Abstraction d1, Abstraction source) {
                        AccessPath mappedAp;
                        AccessPath newAp;
                        if (defStmt2 instanceof IdentityStmt) {
                            return Collections.singleton(source);
                        }
                        if (!(defStmt2 instanceof AssignStmt)) {
                            return null;
                        }
                        MutableTwoElementSet<Abstraction> res = new MutableTwoElementSet<>();
                        AssignStmt assignStmt = (AssignStmt) defStmt2;
                        Value leftOp = assignStmt.getLeftOp();
                        Value rightOp = assignStmt.getRightOp();
                        Value leftVal = BaseSelector.selectBase(leftOp, false);
                        Value rightVal = BaseSelector.selectBase(rightOp, false);
                        AccessPath ap = source.getAccessPath();
                        Value sourceBase = ap.getPlainValue();
                        boolean handoverLeftValue = false;
                        boolean leftSideOverwritten = false;
                        if (leftOp instanceof StaticFieldRef) {
                            if (BackwardsAliasProblem.this.manager.getConfig().getStaticFieldTrackingMode() != InfoflowConfiguration.StaticFieldTrackingMode.None && ap.firstFieldMatches(((StaticFieldRef) leftOp).getField())) {
                                handoverLeftValue = true;
                            }
                        } else if (leftOp instanceof InstanceFieldRef) {
                            InstanceFieldRef instRef = (InstanceFieldRef) leftOp;
                            if (instRef.getBase() == sourceBase) {
                                if (ap.firstFieldMatches(instRef.getField())) {
                                    handoverLeftValue = true;
                                } else if (ap.getTaintSubFields() && ap.getFragmentCount() == 0) {
                                    handoverLeftValue = true;
                                } else if (source.dependsOnCutAP() || isCircularType(leftVal)) {
                                    handoverLeftValue = true;
                                }
                            }
                        } else if (leftVal == sourceBase) {
                            handoverLeftValue = leftOp instanceof ArrayRef;
                            leftSideOverwritten = !handoverLeftValue;
                        }
                        if (handoverLeftValue) {
                            handOver(d1, srcUnit, source);
                        }
                        if (leftSideOverwritten) {
                            return null;
                        }
                        res.add(source);
                        if ((rightOp instanceof BinopExpr) || (rightOp instanceof UnopExpr) || (rightOp instanceof NewArrayExpr)) {
                            return res;
                        }
                        boolean localAliases = ((leftOp instanceof Local) || (leftOp instanceof ArrayRef)) && !(leftOp.getType() instanceof PrimType);
                        boolean fieldAliases = (leftOp instanceof FieldRef) && !(((FieldRef) leftOp).getField().getType() instanceof PrimType);
                        if ((localAliases || fieldAliases) && !(rightVal.getType() instanceof PrimType)) {
                            boolean addLeftValue = false;
                            boolean cutFirstFieldLeft = false;
                            Type leftType = null;
                            if (rightVal instanceof StaticFieldRef) {
                                if (BackwardsAliasProblem.this.manager.getConfig().getStaticFieldTrackingMode() != InfoflowConfiguration.StaticFieldTrackingMode.None && ap.firstFieldMatches(((StaticFieldRef) rightVal).getField())) {
                                    addLeftValue = true;
                                }
                            } else if (rightVal instanceof InstanceFieldRef) {
                                InstanceFieldRef instRef2 = (InstanceFieldRef) rightVal;
                                if (instRef2.getBase() == sourceBase && ap.isInstanceFieldRef() && (mappedAp = Aliasing.getReferencedAPBase(ap, new SootField[]{instRef2.getField()}, BackwardsAliasProblem.this.manager)) != null) {
                                    addLeftValue = true;
                                    cutFirstFieldLeft = true;
                                    if (!mappedAp.equals(ap)) {
                                        ap = mappedAp;
                                        source = source.deriveNewAbstraction(mappedAp, null);
                                    }
                                }
                            } else if (rightVal == sourceBase) {
                                addLeftValue = true;
                                leftType = ap.getBaseType();
                                if (rightOp instanceof ArrayRef) {
                                    leftType = ((ArrayType) leftType).getElementType();
                                } else if (leftOp instanceof ArrayRef) {
                                    ArrayRef arrayRef = (ArrayRef) leftOp;
                                    leftType = TypeUtils.buildArrayOrAddDimension(leftType, arrayRef.getType().getArrayType());
                                } else if (!BackwardsAliasProblem.this.manager.getTypeUtils().checkCast(source.getAccessPath().getBaseType(), leftOp.getType())) {
                                    return null;
                                }
                                if (rightVal instanceof CastExpr) {
                                    CastExpr ce = (CastExpr) rightOp;
                                    if (!BackwardsAliasProblem.this.manager.getHierarchy().canStoreType(leftType, ce.getCastType())) {
                                        leftType = ce.getCastType();
                                    }
                                } else if (rightVal instanceof InstanceOfExpr) {
                                    addLeftValue = false;
                                }
                            }
                            if (addLeftValue) {
                                if (0 != 0) {
                                    newAp = BackwardsAliasProblem.this.manager.getAccessPathFactory().createAccessPath(leftVal, true);
                                } else {
                                    newAp = BackwardsAliasProblem.this.manager.getAccessPathFactory().copyWithNewValue(ap, leftOp, leftType, cutFirstFieldLeft);
                                }
                                Abstraction newAbs = checkAbstraction(source.deriveNewAbstraction(newAp, assignStmt));
                                if (newAbs != null && newAbs != source) {
                                    if ((rightVal instanceof StaticFieldRef) && BackwardsAliasProblem.this.manager.getConfig().getStaticFieldTrackingMode() == InfoflowConfiguration.StaticFieldTrackingMode.ContextFlowInsensitive) {
                                        BackwardsAliasProblem.this.manager.getGlobalTaintManager().addToGlobalTaintState(newAbs);
                                    } else {
                                        res.add(newAbs);
                                    }
                                }
                            }
                        }
                        return res;
                    }
                };
            }

            @Override // heros.FlowFunctions
            public FlowFunction<Abstraction> getCallFlowFunction(final Unit callSite, final SootMethod dest) {
                if (!dest.isConcrete()) {
                    BackwardsAliasProblem.this.logger.debug("Call skipped because target has no body: {} -> {}", callSite, dest);
                    return KillAll.v();
                } else if (!(callSite instanceof Stmt)) {
                    return KillAll.v();
                } else {
                    final Stmt callStmt = (Stmt) callSite;
                    final InvokeExpr ie = callStmt.containsInvokeExpr() ? callStmt.getInvokeExpr() : null;
                    final Local[] paramLocals = (Local[]) dest.getActiveBody().getParameterLocals().toArray(new Local[0]);
                    final Local thisLocal = dest.isStatic() ? null : dest.getActiveBody().getThisLocal();
                    final boolean isSource = (BackwardsAliasProblem.this.manager.getSourceSinkManager() == null || BackwardsAliasProblem.this.manager.getSourceSinkManager().getSourceInfo((Stmt) callSite, BackwardsAliasProblem.this.manager) == null) ? false : true;
                    final boolean isSink = (BackwardsAliasProblem.this.manager.getSourceSinkManager() == null || BackwardsAliasProblem.this.manager.getSourceSinkManager().getSinkInfo(callStmt, BackwardsAliasProblem.this.manager, null) == null) ? false : true;
                    final boolean isExecutorExecute = BackwardsAliasProblem.this.interproceduralCFG().isExecutorExecute(ie, dest);
                    final boolean isReflectiveCallSite = BackwardsAliasProblem.this.interproceduralCFG().isReflectiveCallSite(ie);
                    return new SolverCallFlowFunction() { // from class: soot.jimple.infoflow.problems.BackwardsAliasProblem.1.2
                        @Override // soot.jimple.infoflow.solver.functions.SolverCallFlowFunction
                        public Set<Abstraction> computeTargets(Abstraction d1, Abstraction source) {
                            if (source != BackwardsAliasProblem.this.getZeroValue()) {
                                if (BackwardsAliasProblem.this.taintPropagationHandler != null) {
                                    BackwardsAliasProblem.this.taintPropagationHandler.notifyFlowIn(callStmt, source, BackwardsAliasProblem.this.manager, TaintPropagationHandler.FlowFunctionType.CallFlowFunction);
                                }
                                if (source.getTurnUnit() == callSite) {
                                    return BackwardsAliasProblem.this.notifyOutFlowHandlers(callSite, d1, source, null, TaintPropagationHandler.FlowFunctionType.CallFlowFunction);
                                }
                                Set<Abstraction> res = computeTargetsInternal(d1, source);
                                return BackwardsAliasProblem.this.notifyOutFlowHandlers(callStmt, d1, source, res, TaintPropagationHandler.FlowFunctionType.CallFlowFunction);
                            }
                            return null;
                        }

                        /* JADX WARN: Multi-variable type inference failed */
                        private Set<Abstraction> computeTargetsInternal(Abstraction d1, Abstraction source) {
                            Value[] valueArr;
                            Abstraction abs;
                            if (BackwardsAliasProblem.this.manager.getConfig().getInspectSources() || !isSource) {
                                if (!BackwardsAliasProblem.this.manager.getConfig().getInspectSinks() && isSink) {
                                    return null;
                                }
                                if ((BackwardsAliasProblem.this.manager.getConfig().getStaticFieldTrackingMode() != InfoflowConfiguration.StaticFieldTrackingMode.None || !dest.isStaticInitializer()) && !BackwardsAliasProblem.this.isExcluded(dest)) {
                                    if (BackwardsAliasProblem.this.taintWrapper != null && BackwardsAliasProblem.this.taintWrapper.isExclusive(callStmt, source)) {
                                        return null;
                                    }
                                    if (BackwardsAliasProblem.this.manager.getConfig().getStaticFieldTrackingMode() != InfoflowConfiguration.StaticFieldTrackingMode.None && source.getAccessPath().isStaticFieldRef() && !BackwardsAliasProblem.this.interproceduralCFG().isStaticFieldRead(dest, source.getAccessPath().getFirstField())) {
                                        return null;
                                    }
                                    HashSet<Abstraction> res = new HashSet<>();
                                    if (BackwardsAliasProblem.this.manager.getConfig().getStaticFieldTrackingMode() != InfoflowConfiguration.StaticFieldTrackingMode.None && source.getAccessPath().isStaticFieldRef() && (abs = checkAbstraction(source.deriveNewAbstraction(source.getAccessPath(), callStmt))) != null) {
                                        res.add(abs);
                                    }
                                    if (!isExecutorExecute && !source.getAccessPath().isStaticFieldRef() && !dest.isStatic()) {
                                        InstanceInvokeExpr instanceInvokeExpr = (InstanceInvokeExpr) callStmt.getInvokeExpr();
                                        Value callBase = isReflectiveCallSite ? instanceInvokeExpr.getArg(0) : instanceInvokeExpr.getBase();
                                        Value sourceBase = source.getAccessPath().getPlainValue();
                                        if (callBase == sourceBase && BackwardsAliasProblem.this.manager.getTypeUtils().hasCompatibleTypesForCall(source.getAccessPath(), dest.getDeclaringClass()) && (isReflectiveCallSite || instanceInvokeExpr.getArgs().stream().noneMatch(arg -> {
                                            return arg == sourceBase;
                                        }))) {
                                            AccessPath ap = BackwardsAliasProblem.this.manager.getAccessPathFactory().copyWithNewValue(source.getAccessPath(), thisLocal);
                                            Abstraction abs2 = checkAbstraction(source.deriveNewAbstraction(ap, (Stmt) callSite));
                                            if (abs2 != null) {
                                                res.add(abs2);
                                            }
                                        }
                                    }
                                    if (isExecutorExecute && ie != null && ie.getArg(0) == source.getAccessPath().getPlainValue()) {
                                        AccessPath ap2 = BackwardsAliasProblem.this.manager.getAccessPathFactory().copyWithNewValue(source.getAccessPath(), thisLocal);
                                        Abstraction abs3 = checkAbstraction(source.deriveNewAbstraction(ap2, callStmt));
                                        if (abs3 != null) {
                                            res.add(abs3);
                                        }
                                    } else if (ie != null && dest.getParameterCount() > 0) {
                                        for (int i = isReflectiveCallSite ? 1 : 0; i < ie.getArgCount(); i++) {
                                            if (ie.getArg(i) == source.getAccessPath().getPlainValue()) {
                                                if (isReflectiveCallSite) {
                                                    for (Value param : paramLocals) {
                                                        AccessPath ap3 = BackwardsAliasProblem.this.manager.getAccessPathFactory().copyWithNewValue(source.getAccessPath(), param, null, false);
                                                        Abstraction abs4 = checkAbstraction(source.deriveNewAbstraction(ap3, callStmt));
                                                        if (abs4 != null) {
                                                            res.add(abs4);
                                                        }
                                                    }
                                                } else {
                                                    AccessPath ap4 = BackwardsAliasProblem.this.manager.getAccessPathFactory().copyWithNewValue(source.getAccessPath(), paramLocals[i]);
                                                    Abstraction abs5 = checkAbstraction(source.deriveNewAbstraction(ap4, callStmt));
                                                    if (abs5 != null) {
                                                        res.add(abs5);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    Iterator<Abstraction> it = res.iterator();
                                    while (it.hasNext()) {
                                        Abstraction d3 = it.next();
                                        BackwardsAliasProblem.this.manager.getForwardSolver().injectContext(BackwardsAliasProblem.this.solver, dest, d3, callSite, source, d1);
                                    }
                                    return res;
                                }
                                return null;
                            }
                            return null;
                        }
                    };
                }
            }

            @Override // heros.FlowFunctions
            public FlowFunction<Abstraction> getReturnFlowFunction(final Unit callSite, final SootMethod callee, final Unit exitStmt, final Unit returnSite) {
                if (callSite != null && !(callSite instanceof Stmt)) {
                    return KillAll.v();
                }
                final Value[] paramLocals = new Value[callee.getParameterCount()];
                for (int i = 0; i < callee.getParameterCount(); i++) {
                    paramLocals[i] = callee.getActiveBody().getParameterLocal(i);
                }
                final Stmt callStmt = (Stmt) callSite;
                final InvokeExpr ie = (callStmt == null || !callStmt.containsInvokeExpr()) ? null : callStmt.getInvokeExpr();
                final boolean isReflectiveCallSite = BackwardsAliasProblem.this.interproceduralCFG().isReflectiveCallSite(ie);
                final ReturnStmt returnStmt = exitStmt instanceof ReturnStmt ? (ReturnStmt) exitStmt : null;
                final Local thisLocal = callee.isStatic() ? null : callee.getActiveBody().getThisLocal();
                final boolean isExecutorExecute = BackwardsAliasProblem.this.interproceduralCFG().isExecutorExecute(ie, callee);
                return new SolverReturnFlowFunction() { // from class: soot.jimple.infoflow.problems.BackwardsAliasProblem.1.3
                    @Override // soot.jimple.infoflow.solver.functions.SolverReturnFlowFunction
                    public Set<Abstraction> computeTargets(Abstraction source, Abstraction calleeD1, Collection<Abstraction> callerD1s) {
                        if (source != BackwardsAliasProblem.this.getZeroValue() && callSite != null) {
                            if (BackwardsAliasProblem.this.taintPropagationHandler != null) {
                                BackwardsAliasProblem.this.taintPropagationHandler.notifyFlowIn(callStmt, source, BackwardsAliasProblem.this.manager, TaintPropagationHandler.FlowFunctionType.ReturnFlowFunction);
                            }
                            if (source.getTurnUnit() == callSite) {
                                return BackwardsAliasProblem.this.notifyOutFlowHandlers(callSite, calleeD1, source, null, TaintPropagationHandler.FlowFunctionType.ReturnFlowFunction);
                            }
                            Set<Abstraction> res = computeTargetsInternal(source);
                            return BackwardsAliasProblem.this.notifyOutFlowHandlers(exitStmt, calleeD1, source, res, TaintPropagationHandler.FlowFunctionType.ReturnFlowFunction);
                        }
                        return null;
                    }

                    private Set<Abstraction> computeTargetsInternal(Abstraction source) {
                        Value sourceBase;
                        HashSet<Abstraction> res = new HashSet<>();
                        if (BackwardsAliasProblem.this.manager.getConfig().getStaticFieldTrackingMode() != InfoflowConfiguration.StaticFieldTrackingMode.None && source.getAccessPath().isStaticFieldRef()) {
                            res.add(source);
                            return res;
                        }
                        if (returnStmt != null && (callStmt instanceof AssignStmt)) {
                            Value retLocal = returnStmt.getOp();
                            Value leftOp = ((DefinitionStmt) callSite).getLeftOp();
                            if (retLocal == source.getAccessPath().getPlainValue() && !BackwardsAliasProblem.this.isExceptionHandler(returnSite)) {
                                AccessPath ap = BackwardsAliasProblem.this.manager.getAccessPathFactory().copyWithNewValue(source.getAccessPath(), leftOp);
                                Abstraction abs = checkAbstraction(source.deriveNewAbstraction(ap, (Stmt) exitStmt));
                                if (abs != null) {
                                    res.add(abs);
                                }
                            }
                        }
                        if (!isExecutorExecute && !callee.isStatic() && thisLocal == (sourceBase = source.getAccessPath().getPlainValue()) && BackwardsAliasProblem.this.manager.getTypeUtils().hasCompatibleTypesForCall(source.getAccessPath(), callee.getDeclaringClass())) {
                            InstanceInvokeExpr instanceInvokeExpr = (InstanceInvokeExpr) callStmt.getInvokeExpr();
                            Value callBase = isReflectiveCallSite ? instanceInvokeExpr.getArg(0) : instanceInvokeExpr.getBase();
                            if (isReflectiveCallSite || instanceInvokeExpr.getArgs().stream().noneMatch(arg -> {
                                return arg == sourceBase;
                            })) {
                                AccessPath ap2 = BackwardsAliasProblem.this.manager.getAccessPathFactory().copyWithNewValue(source.getAccessPath(), callBase, isReflectiveCallSite ? null : source.getAccessPath().getBaseType(), false);
                                Abstraction abs2 = checkAbstraction(source.deriveNewAbstraction(ap2, (Stmt) exitStmt));
                                if (abs2 != null) {
                                    res.add(abs2);
                                }
                            }
                        }
                        if (isExecutorExecute && ie != null && ie.getArg(0) == source.getAccessPath().getPlainValue()) {
                            AccessPath ap3 = BackwardsAliasProblem.this.manager.getAccessPathFactory().copyWithNewValue(source.getAccessPath(), thisLocal);
                            Abstraction abs3 = checkAbstraction(source.deriveNewAbstraction(ap3, callStmt));
                            if (abs3 != null) {
                                res.add(abs3);
                            }
                        } else if (ie != null) {
                            for (int i2 = 0; i2 < callee.getParameterCount(); i2++) {
                                if (source.getAccessPath().getPlainValue() == paramLocals[i2] && !isPrimitiveOrStringBase(source)) {
                                    Value originalCallArg = ie.getArg(isReflectiveCallSite ? 1 : i2);
                                    if ((callSite instanceof DefinitionStmt) && !BackwardsAliasProblem.this.isExceptionHandler(returnSite)) {
                                        DefinitionStmt defnStmt = (DefinitionStmt) callSite;
                                        Value leftOp2 = defnStmt.getLeftOp();
                                        originalCallArg = defnStmt.getInvokeExpr().getArg(i2);
                                        if (originalCallArg == leftOp2) {
                                        }
                                    }
                                    if (!BackwardsAliasProblem.this.interproceduralCFG().methodWritesValue(callee, paramLocals[i2]) && AccessPath.canContainValue(originalCallArg) && (isReflectiveCallSite || BackwardsAliasProblem.this.manager.getTypeUtils().checkCast(source.getAccessPath(), originalCallArg.getType()))) {
                                        AccessPath ap4 = BackwardsAliasProblem.this.manager.getAccessPathFactory().copyWithNewValue(source.getAccessPath(), originalCallArg, isReflectiveCallSite ? null : source.getAccessPath().getBaseType(), false);
                                        Abstraction abs4 = checkAbstraction(source.deriveNewAbstraction(ap4, (Stmt) exitStmt));
                                        if (abs4 != null) {
                                            res.add(abs4);
                                        }
                                    }
                                }
                            }
                        }
                        if (res.isEmpty()) {
                            return null;
                        }
                        Iterator<Abstraction> it = res.iterator();
                        while (it.hasNext()) {
                            Abstraction abs5 = it.next();
                            if (abs5 != source) {
                                abs5.setCorrespondingCallSite((Stmt) callSite);
                            }
                        }
                        return res;
                    }
                };
            }

            @Override // heros.FlowFunctions
            public FlowFunction<Abstraction> getCallToReturnFlowFunction(final Unit callSite, Unit returnSite) {
                if (!(callSite instanceof Stmt)) {
                    return KillAll.v();
                }
                final Stmt callStmt = (Stmt) callSite;
                InvokeExpr invExpr = callStmt.getInvokeExpr();
                final Value[] callArgs = new Value[invExpr.getArgCount()];
                for (int i = 0; i < invExpr.getArgCount(); i++) {
                    callArgs[i] = invExpr.getArg(i);
                }
                final SootMethod callee = invExpr.getMethod();
                final boolean isSource = (BackwardsAliasProblem.this.manager.getSourceSinkManager() == null || BackwardsAliasProblem.this.manager.getSourceSinkManager().getSourceInfo(callStmt, BackwardsAliasProblem.this.manager) == null) ? false : true;
                return new SolverCallToReturnFlowFunction() { // from class: soot.jimple.infoflow.problems.BackwardsAliasProblem.1.4
                    @Override // soot.jimple.infoflow.solver.functions.SolverCallToReturnFlowFunction
                    public Set<Abstraction> computeTargets(Abstraction d1, Abstraction source) {
                        if (source != BackwardsAliasProblem.this.getZeroValue()) {
                            if (BackwardsAliasProblem.this.taintPropagationHandler != null) {
                                BackwardsAliasProblem.this.taintPropagationHandler.notifyFlowIn(callSite, source, BackwardsAliasProblem.this.manager, TaintPropagationHandler.FlowFunctionType.CallToReturnFlowFunction);
                            }
                            if (source.getTurnUnit() != null && (source.getTurnUnit() == callSite || BackwardsAliasProblem.this.manager.getICFG().getCalleesOfCallAt(callSite).stream().anyMatch(m -> {
                                return BackwardsAliasProblem.this.manager.getICFG().getMethodOf(r4.getTurnUnit()) == source;
                            }))) {
                                return BackwardsAliasProblem.this.notifyOutFlowHandlers(callSite, d1, source, null, TaintPropagationHandler.FlowFunctionType.CallToReturnFlowFunction);
                            }
                            Set<Abstraction> res = computeTargetsInternal(d1, source);
                            return BackwardsAliasProblem.this.notifyOutFlowHandlers(callSite, d1, source, res, TaintPropagationHandler.FlowFunctionType.CallToReturnFlowFunction);
                        }
                        return null;
                    }

                    private Set<Abstraction> computeTargetsInternal(Abstraction d1, Abstraction source) {
                        Value[] valueArr;
                        if (!BackwardsAliasProblem.this.interproceduralCFG().getCalleesOfCallAt(callSite).isEmpty()) {
                            if (BackwardsAliasProblem.this.taintWrapper != null) {
                                if (BackwardsAliasProblem.this.taintWrapper.isExclusive(callStmt, source)) {
                                    handOver(d1, callSite, source);
                                }
                                Set<Abstraction> wrapperAliases = BackwardsAliasProblem.this.taintWrapper.getAliasesForMethod(callStmt, d1, source);
                                if (wrapperAliases != null && !wrapperAliases.isEmpty()) {
                                    Set<Abstraction> passOnSet = new HashSet<>(wrapperAliases.size());
                                    for (Abstraction abs : wrapperAliases) {
                                        passOnSet.add(abs);
                                        if (abs != source) {
                                            abs.setCorrespondingCallSite(callStmt);
                                        }
                                        for (Unit u : BackwardsAliasProblem.this.manager.getICFG().getPredsOf(callSite)) {
                                            handOver(d1, u, abs);
                                        }
                                    }
                                    return passOnSet;
                                }
                            }
                            if (BackwardsAliasProblem.this.isExcluded(callee)) {
                                return Collections.singleton(source);
                            }
                            if (BackwardsAliasProblem.this.manager.getConfig().getStaticFieldTrackingMode() != InfoflowConfiguration.StaticFieldTrackingMode.None && source.getAccessPath().isStaticFieldRef() && BackwardsAliasProblem.this.interproceduralCFG().isStaticFieldUsed(callee, source.getAccessPath().getFirstField())) {
                                return null;
                            }
                            if (!callee.isNative()) {
                                if (callStmt.getInvokeExpr() instanceof InstanceInvokeExpr) {
                                    InstanceInvokeExpr inv = (InstanceInvokeExpr) callStmt.getInvokeExpr();
                                    if (inv.getBase() == source.getAccessPath().getPlainValue()) {
                                        return null;
                                    }
                                }
                                if (Arrays.stream(callArgs).anyMatch(arg -> {
                                    return !isPrimitiveOrStringBase(r4) && source == r4.getAccessPath().getPlainValue();
                                })) {
                                    if (!isSource) {
                                        return null;
                                    }
                                    handOver(d1, callSite, source);
                                    return null;
                                }
                            } else {
                                for (Value arg2 : callArgs) {
                                    if (arg2 == source.getAccessPath().getPlainValue()) {
                                        Abstraction newSource = source.deriveNewAbstractionWithTurnUnit(callSite);
                                        handOver(d1, callSite, newSource);
                                        return null;
                                    }
                                }
                            }
                            return Collections.singleton(source);
                        }
                        return Collections.singleton(source);
                    }
                };
            }

            /* JADX INFO: Access modifiers changed from: private */
            public boolean isPrimitiveOrStringBase(Abstraction abs) {
                Type t = abs.getAccessPath().getBaseType();
                if (t instanceof PrimType) {
                    return true;
                }
                return TypeUtils.isStringType(t) && !abs.getAccessPath().getCanHaveImmutableAliases();
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void handOver(Abstraction d1, Unit unit, Abstraction in) {
                Abstraction in2 = in.getActiveCopy();
                if (BackwardsAliasProblem.this.manager.getConfig().getImplicitFlowMode().trackControlFlowDependencies()) {
                    List<Unit> condUnits = BackwardsAliasProblem.this.manager.getOriginalICFG().getConditionalBranchesInterprocedural(unit);
                    for (Unit condUnit : condUnits) {
                        Abstraction abs = in2.deriveNewAbstractionWithDominator(condUnit);
                        if (abs != null) {
                            BackwardsAliasProblem.this.manager.getForwardSolver().processEdge(new PathEdge<>(d1, unit, abs));
                        }
                    }
                    return;
                }
                BackwardsAliasProblem.this.manager.getForwardSolver().processEdge(new PathEdge<>(d1, unit, in2));
            }
        };
    }
}
