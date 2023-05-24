package soot.jimple.infoflow.problems;

import heros.FlowFunction;
import heros.FlowFunctions;
import heros.flowfunc.Identity;
import heros.flowfunc.KillAll;
import heros.solver.PathEdge;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import soot.ArrayType;
import soot.Local;
import soot.PrimType;
import soot.RefType;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.IdentityStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InstanceOfExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.LengthExpr;
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
import soot.jimple.infoflow.taintWrappers.ITaintPropagationWrapper;
import soot.jimple.infoflow.typing.TypeUtils;
import soot.jimple.infoflow.util.BaseSelector;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/problems/AliasProblem.class */
public class AliasProblem extends AbstractInfoflowProblem {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !AliasProblem.class.desiredAssertionStatus();
    }

    @Override // soot.jimple.infoflow.problems.AbstractInfoflowProblem
    public void setTaintWrapper(ITaintPropagationWrapper wrapper) {
        this.taintWrapper = wrapper;
    }

    public AliasProblem(InfoflowManager manager) {
        super(manager, null, EmptyPropagationRuleManagerFactory.INSTANCE);
    }

    @Override // heros.template.DefaultIFDSTabulationProblem
    public FlowFunctions<Unit, Abstraction, SootMethod> createFlowFunctionsFactory() {
        return new FlowFunctions<Unit, Abstraction, SootMethod>() { // from class: soot.jimple.infoflow.problems.AliasProblem.1
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
            public Set<Abstraction> computeAliases(DefinitionStmt defStmt, Value leftValue, Abstraction d1, Abstraction source) {
                if (AliasProblem.$assertionsDisabled || !source.getAccessPath().isEmpty()) {
                    if (source == AliasProblem.this.getZeroValue()) {
                        return null;
                    }
                    Set<Abstraction> res = new MutableTwoElementSet<>();
                    boolean leftSideMatches = Aliasing.baseMatches(leftValue, source);
                    if (!leftSideMatches) {
                        res.add(source);
                    } else {
                        for (Unit u : AliasProblem.this.interproceduralCFG().getPredsOf(defStmt)) {
                            AliasProblem.this.manager.getForwardSolver().processEdge(new PathEdge<>(d1, u, source));
                        }
                    }
                    if (defStmt instanceof IdentityStmt) {
                        res.add(source);
                        return res;
                    } else if (!(defStmt instanceof AssignStmt)) {
                        return res;
                    } else {
                        Value rightValue = BaseSelector.selectBase(defStmt.getRightOp(), false);
                        if (leftSideMatches && !(rightValue instanceof Local) && !(rightValue instanceof FieldRef) && !(rightValue instanceof ArrayRef)) {
                            res.add(source);
                            return res;
                        } else if (rightValue instanceof Constant) {
                            return res;
                        } else {
                            if (defStmt.getRightOp() instanceof NewArrayExpr) {
                                return res;
                            }
                            if (defStmt.getRightOp() instanceof BinopExpr) {
                                return res;
                            }
                            if (defStmt.getRightOp() instanceof UnopExpr) {
                                return res;
                            }
                            boolean aliasOverwritten = Aliasing.baseMatchesStrict(rightValue, source) && (rightValue.getType() instanceof RefType) && !source.dependsOnCutAP();
                            if (!aliasOverwritten && !(rightValue.getType() instanceof PrimType)) {
                                Abstraction newLeftAbs = null;
                                if (rightValue instanceof InstanceFieldRef) {
                                    InstanceFieldRef ref = (InstanceFieldRef) rightValue;
                                    if (source.getAccessPath().isInstanceFieldRef() && ref.getBase() == source.getAccessPath().getPlainValue() && source.getAccessPath().firstFieldMatches(ref.getField())) {
                                        AccessPath ap = AliasProblem.this.manager.getAccessPathFactory().copyWithNewValue(source.getAccessPath(), leftValue, source.getAccessPath().getFirstFieldType(), true);
                                        newLeftAbs = checkAbstraction(source.deriveNewAbstraction(ap, defStmt));
                                    }
                                } else if (AliasProblem.this.manager.getConfig().getStaticFieldTrackingMode() != InfoflowConfiguration.StaticFieldTrackingMode.None && (rightValue instanceof StaticFieldRef)) {
                                    StaticFieldRef ref2 = (StaticFieldRef) rightValue;
                                    if (source.getAccessPath().isStaticFieldRef() && source.getAccessPath().firstFieldMatches(ref2.getField())) {
                                        AccessPath ap2 = AliasProblem.this.manager.getAccessPathFactory().copyWithNewValue(source.getAccessPath(), leftValue, source.getAccessPath().getBaseType(), true);
                                        newLeftAbs = checkAbstraction(source.deriveNewAbstraction(ap2, defStmt));
                                    }
                                } else if (rightValue == source.getAccessPath().getPlainValue()) {
                                    Type newType = source.getAccessPath().getBaseType();
                                    if (leftValue instanceof ArrayRef) {
                                        ArrayRef arrayRef = (ArrayRef) leftValue;
                                        newType = TypeUtils.buildArrayOrAddDimension(newType, arrayRef.getType().getArrayType());
                                    } else if (defStmt.getRightOp() instanceof ArrayRef) {
                                        newType = ((ArrayType) newType).getElementType();
                                    } else if (!AliasProblem.this.manager.getTypeUtils().checkCast(source.getAccessPath(), leftValue.getType())) {
                                        return null;
                                    }
                                    if (defStmt.getRightOp() instanceof CastExpr) {
                                        CastExpr ce = (CastExpr) defStmt.getRightOp();
                                        if (!AliasProblem.this.manager.getHierarchy().canStoreType(newType, ce.getCastType())) {
                                            newType = ce.getCastType();
                                        }
                                    } else if (defStmt.getRightOp() instanceof LengthExpr) {
                                        return res;
                                    } else {
                                        if (defStmt.getRightOp() instanceof InstanceOfExpr) {
                                            return res;
                                        }
                                    }
                                    AccessPath ap3 = AliasProblem.this.manager.getAccessPathFactory().copyWithNewValue(source.getAccessPath(), leftValue, newType, false);
                                    newLeftAbs = checkAbstraction(source.deriveNewAbstraction(ap3, defStmt));
                                }
                                if (newLeftAbs != null) {
                                    if (newLeftAbs.getAccessPath().getLastFieldType() instanceof PrimType) {
                                        return res;
                                    }
                                    if (!newLeftAbs.getAccessPath().equals(source.getAccessPath())) {
                                        res.add(newLeftAbs);
                                        for (Unit u2 : AliasProblem.this.interproceduralCFG().getPredsOf(defStmt)) {
                                            AliasProblem.this.manager.getForwardSolver().processEdge(new PathEdge<>(d1, u2, newLeftAbs));
                                        }
                                    }
                                }
                            }
                            if (((rightValue instanceof Local) || (rightValue instanceof FieldRef)) && !(leftValue.getType() instanceof PrimType)) {
                                boolean addRightValue = false;
                                boolean cutFirstField = false;
                                Type targetType = null;
                                if (leftValue instanceof InstanceFieldRef) {
                                    if (source.getAccessPath().isInstanceFieldRef()) {
                                        InstanceFieldRef leftRef = (InstanceFieldRef) leftValue;
                                        if (leftRef.getBase() == source.getAccessPath().getPlainValue() && source.getAccessPath().firstFieldMatches(leftRef.getField())) {
                                            targetType = source.getAccessPath().getFirstFieldType();
                                            addRightValue = true;
                                            cutFirstField = true;
                                        }
                                    }
                                } else if ((leftValue instanceof Local) && source.getAccessPath().isInstanceFieldRef()) {
                                    Local base = source.getAccessPath().getPlainValue();
                                    if (leftValue == base) {
                                        targetType = source.getAccessPath().getBaseType();
                                        addRightValue = true;
                                    }
                                } else if (leftValue instanceof ArrayRef) {
                                    ArrayRef ar = (ArrayRef) leftValue;
                                    Local leftBase = (Local) ar.getBase();
                                    if (leftBase == source.getAccessPath().getPlainValue()) {
                                        addRightValue = true;
                                        targetType = source.getAccessPath().getBaseType();
                                    }
                                } else if (leftValue == source.getAccessPath().getPlainValue()) {
                                    if (!AliasProblem.this.manager.getTypeUtils().checkCast(source.getAccessPath(), defStmt.getRightOp().getType())) {
                                        return null;
                                    }
                                    addRightValue = true;
                                    targetType = source.getAccessPath().getBaseType();
                                }
                                if (addRightValue) {
                                    if (targetType != null) {
                                        if (defStmt.getRightOp() instanceof ArrayRef) {
                                            ArrayRef arrayRef2 = (ArrayRef) defStmt.getRightOp();
                                            targetType = TypeUtils.buildArrayOrAddDimension(targetType, arrayRef2.getType().getArrayType());
                                        } else if (leftValue instanceof ArrayRef) {
                                            if (!AliasProblem.$assertionsDisabled && !(source.getAccessPath().getBaseType() instanceof ArrayType)) {
                                                throw new AssertionError();
                                            }
                                            targetType = ((ArrayType) targetType).getElementType();
                                            if (TypeUtils.isObjectLikeType(targetType)) {
                                                targetType = rightValue.getType();
                                            }
                                            if (!AliasProblem.this.manager.getTypeUtils().checkCast(rightValue.getType(), targetType)) {
                                                addRightValue = false;
                                            }
                                        }
                                    }
                                    if (defStmt.getRightOp() instanceof LengthExpr) {
                                        targetType = null;
                                    }
                                    if (((targetType instanceof PrimType) || ((targetType instanceof ArrayType) && (((ArrayType) targetType).getElementType() instanceof PrimType))) && !source.getAccessPath().isStaticFieldRef() && !source.getAccessPath().isLocal()) {
                                        return null;
                                    }
                                    if (((rightValue.getType() instanceof PrimType) || ((rightValue.getType() instanceof ArrayType) && (((ArrayType) rightValue.getType()).getElementType() instanceof PrimType))) && !source.getAccessPath().isStaticFieldRef() && !source.getAccessPath().isLocal()) {
                                        return null;
                                    }
                                    if (addRightValue && !AliasProblem.this.manager.getTypeUtils().checkCast(rightValue.getType(), targetType)) {
                                        addRightValue = false;
                                    }
                                    if (addRightValue && (rightValue instanceof StaticFieldRef) && AliasProblem.this.manager.getConfig().getStaticFieldTrackingMode() == InfoflowConfiguration.StaticFieldTrackingMode.None) {
                                        addRightValue = false;
                                    }
                                    if (addRightValue) {
                                        AccessPath ap4 = AliasProblem.this.manager.getAccessPathFactory().copyWithNewValue(source.getAccessPath(), rightValue, targetType, cutFirstField);
                                        Abstraction newAbs = checkAbstraction(source.deriveNewAbstraction(ap4, defStmt));
                                        if (newAbs != null && !newAbs.getAccessPath().equals(source.getAccessPath())) {
                                            if ((rightValue instanceof StaticFieldRef) && AliasProblem.this.manager.getConfig().getStaticFieldTrackingMode() == InfoflowConfiguration.StaticFieldTrackingMode.ContextFlowInsensitive) {
                                                AliasProblem.this.manager.getGlobalTaintManager().addToGlobalTaintState(newAbs);
                                            } else {
                                                res.add(newAbs);
                                                for (Unit u3 : AliasProblem.this.interproceduralCFG().getPredsOf(defStmt)) {
                                                    AliasProblem.this.manager.getForwardSolver().processEdge(new PathEdge<>(d1, u3, newAbs));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            return res;
                        }
                    }
                }
                throw new AssertionError();
            }

            @Override // heros.FlowFunctions
            public FlowFunction<Abstraction> getNormalFlowFunction(final Unit src, Unit dest) {
                if (src instanceof DefinitionStmt) {
                    final DefinitionStmt defStmt = (DefinitionStmt) src;
                    final Value leftValue = BaseSelector.selectBase(defStmt.getLeftOp(), true);
                    final DefinitionStmt destDefStmt = dest instanceof DefinitionStmt ? (DefinitionStmt) dest : null;
                    final Value destLeftValue = destDefStmt == null ? null : BaseSelector.selectBase(destDefStmt.getLeftOp(), true);
                    return new SolverNormalFlowFunction() { // from class: soot.jimple.infoflow.problems.AliasProblem.1.1
                        @Override // soot.jimple.infoflow.solver.functions.SolverNormalFlowFunction
                        public Set<Abstraction> computeTargets(Abstraction d1, Abstraction source) {
                            if (source == AliasProblem.this.getZeroValue()) {
                                return null;
                            }
                            if (AliasProblem.$assertionsDisabled || source.isAbstractionActive() || AliasProblem.this.manager.getConfig().getFlowSensitiveAliasing()) {
                                if (AliasProblem.this.taintPropagationHandler != null) {
                                    AliasProblem.this.taintPropagationHandler.notifyFlowIn(src, source, AliasProblem.this.manager, TaintPropagationHandler.FlowFunctionType.NormalFlowFunction);
                                }
                                Set<Abstraction> res = computeAliases(defStmt, leftValue, d1, source);
                                if (destDefStmt != null && res != null && !res.isEmpty() && AliasProblem.this.interproceduralCFG().isExitStmt(destDefStmt)) {
                                    for (Abstraction abs : res) {
                                        computeAliases(destDefStmt, destLeftValue, d1, abs);
                                    }
                                }
                                return AliasProblem.this.notifyOutFlowHandlers(src, d1, source, res, TaintPropagationHandler.FlowFunctionType.NormalFlowFunction);
                            }
                            throw new AssertionError();
                        }
                    };
                }
                return Identity.v();
            }

            @Override // heros.FlowFunctions
            public FlowFunction<Abstraction> getCallFlowFunction(final Unit src, final SootMethod dest) {
                boolean z;
                boolean z2;
                if (!dest.isConcrete()) {
                    return KillAll.v();
                }
                final Stmt stmt = (Stmt) src;
                final InvokeExpr ie = (stmt == null || !stmt.containsInvokeExpr()) ? null : stmt.getInvokeExpr();
                final boolean isReflectiveCallSite = AliasProblem.this.interproceduralCFG().isReflectiveCallSite(ie);
                final Value[] paramLocals = new Value[dest.getParameterCount()];
                for (int i = 0; i < dest.getParameterCount(); i++) {
                    paramLocals[i] = dest.getActiveBody().getParameterLocal(i);
                }
                if (AliasProblem.this.manager.getSourceSinkManager() != null) {
                    z = AliasProblem.this.manager.getSourceSinkManager().getSourceInfo((Stmt) src, AliasProblem.this.manager) != null;
                } else {
                    z = false;
                }
                final boolean isSource = z;
                if (AliasProblem.this.manager.getSourceSinkManager() != null) {
                    z2 = AliasProblem.this.manager.getSourceSinkManager().getSinkInfo(stmt, AliasProblem.this.manager, null) != null;
                } else {
                    z2 = false;
                }
                final boolean isSink = z2;
                final Local thisLocal = dest.isStatic() ? null : dest.getActiveBody().getThisLocal();
                final boolean isExecutorExecute = AliasProblem.this.interproceduralCFG().isExecutorExecute(ie, dest);
                return new SolverCallFlowFunction() { // from class: soot.jimple.infoflow.problems.AliasProblem.1.2
                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // soot.jimple.infoflow.solver.functions.SolverCallFlowFunction
                    public Set<Abstraction> computeTargets(Abstraction d1, Abstraction source) {
                        Abstraction abs;
                        if (source == AliasProblem.this.getZeroValue()) {
                            return null;
                        }
                        if (AliasProblem.$assertionsDisabled || source.isAbstractionActive() || AliasProblem.this.manager.getConfig().getFlowSensitiveAliasing()) {
                            if (AliasProblem.this.taintPropagationHandler != null) {
                                AliasProblem.this.taintPropagationHandler.notifyFlowIn(stmt, source, AliasProblem.this.manager, TaintPropagationHandler.FlowFunctionType.CallFlowFunction);
                            }
                            if (AliasProblem.this.manager.getConfig().getInspectSources() || !isSource) {
                                if ((AliasProblem.this.manager.getConfig().getInspectSinks() || !isSink) && !AliasProblem.this.isCallSiteActivatingTaint(stmt, source.getActivationUnit())) {
                                    if (AliasProblem.this.manager.getConfig().getStaticFieldTrackingMode() != InfoflowConfiguration.StaticFieldTrackingMode.None || !dest.isStaticInitializer()) {
                                        if ((AliasProblem.this.taintWrapper == null || !AliasProblem.this.taintWrapper.isExclusive(stmt, source)) && !AliasProblem.this.isExcluded(dest)) {
                                            if (AliasProblem.this.manager.getConfig().getStaticFieldTrackingMode() != InfoflowConfiguration.StaticFieldTrackingMode.None && source.getAccessPath().isStaticFieldRef() && !AliasProblem.this.interproceduralCFG().isStaticFieldRead(dest, source.getAccessPath().getFirstField())) {
                                                return null;
                                            }
                                            Set<Abstraction> res = new HashSet<>();
                                            if (src instanceof DefinitionStmt) {
                                                DefinitionStmt defnStmt = (DefinitionStmt) src;
                                                Value leftOp = defnStmt.getLeftOp();
                                                if (leftOp == source.getAccessPath().getPlainValue()) {
                                                    Iterator<Unit> it = dest.getActiveBody().getUnits().iterator();
                                                    while (it.hasNext()) {
                                                        Unit u = it.next();
                                                        if (u instanceof ReturnStmt) {
                                                            ReturnStmt rStmt = (ReturnStmt) u;
                                                            if ((rStmt.getOp() instanceof Local) || (rStmt.getOp() instanceof FieldRef)) {
                                                                if (AliasProblem.this.manager.getTypeUtils().checkCast(source.getAccessPath(), rStmt.getOp().getType())) {
                                                                    AccessPath ap = AliasProblem.this.manager.getAccessPathFactory().copyWithNewValue(source.getAccessPath(), rStmt.getOp(), null, false);
                                                                    Abstraction abs2 = checkAbstraction(source.deriveNewAbstraction(ap, (Stmt) src));
                                                                    if (abs2 != null) {
                                                                        res.add(abs2);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            if (AliasProblem.this.manager.getConfig().getStaticFieldTrackingMode() != InfoflowConfiguration.StaticFieldTrackingMode.None && source.getAccessPath().isStaticFieldRef() && (abs = checkAbstraction(source.deriveNewAbstraction(source.getAccessPath(), stmt))) != null) {
                                                res.add(abs);
                                            }
                                            Value sourceBase = source.getAccessPath().getPlainValue();
                                            if (!isExecutorExecute && !source.getAccessPath().isStaticFieldRef() && !dest.isStatic()) {
                                                InstanceInvokeExpr iIExpr = (InstanceInvokeExpr) stmt.getInvokeExpr();
                                                Value callBase = isReflectiveCallSite ? iIExpr.getArg(0) : iIExpr.getBase();
                                                if (callBase == sourceBase && AliasProblem.this.manager.getTypeUtils().hasCompatibleTypesForCall(source.getAccessPath(), dest.getDeclaringClass())) {
                                                    boolean param = false;
                                                    if (!isReflectiveCallSite) {
                                                        int i2 = 0;
                                                        while (true) {
                                                            if (i2 < dest.getParameterCount()) {
                                                                if (stmt.getInvokeExpr().getArg(i2) != sourceBase) {
                                                                    i2++;
                                                                } else {
                                                                    param = true;
                                                                    break;
                                                                }
                                                            } else {
                                                                break;
                                                            }
                                                        }
                                                    }
                                                    if (!param) {
                                                        AccessPath ap2 = AliasProblem.this.manager.getAccessPathFactory().copyWithNewValue(source.getAccessPath(), thisLocal);
                                                        Abstraction abs3 = checkAbstraction(source.deriveNewAbstraction(ap2, (Stmt) src));
                                                        if (abs3 != null) {
                                                            res.add(abs3);
                                                        }
                                                    }
                                                }
                                            }
                                            if (isExecutorExecute) {
                                                if (ie.getArg(0) == source.getAccessPath().getPlainValue()) {
                                                    AccessPath ap3 = AliasProblem.this.manager.getAccessPathFactory().copyWithNewValue(source.getAccessPath(), thisLocal);
                                                    Abstraction abs4 = checkAbstraction(source.deriveNewAbstraction(ap3, stmt));
                                                    if (abs4 != null) {
                                                        res.add(abs4);
                                                    }
                                                }
                                            } else if (ie != null && dest.getParameterCount() > 0) {
                                                if (!AliasProblem.$assertionsDisabled && dest.getParameterCount() != ie.getArgCount()) {
                                                    throw new AssertionError();
                                                }
                                                int i3 = isReflectiveCallSite ? 1 : 0;
                                                while (i3 < ie.getArgCount()) {
                                                    if (ie.getArg(i3) == source.getAccessPath().getPlainValue()) {
                                                        if (isReflectiveCallSite) {
                                                            while (i3 < paramLocals.length) {
                                                                AccessPath ap4 = AliasProblem.this.manager.getAccessPathFactory().copyWithNewValue(source.getAccessPath(), paramLocals[0], null, false);
                                                                Abstraction abs5 = checkAbstraction(source.deriveNewAbstraction(ap4, stmt));
                                                                if (abs5 != null) {
                                                                    res.add(abs5);
                                                                }
                                                                i3++;
                                                            }
                                                        } else {
                                                            AccessPath ap5 = AliasProblem.this.manager.getAccessPathFactory().copyWithNewValue(source.getAccessPath(), paramLocals[i3]);
                                                            Abstraction abs6 = checkAbstraction(source.deriveNewAbstraction(ap5, stmt));
                                                            if (abs6 != null) {
                                                                res.add(abs6);
                                                            }
                                                        }
                                                    }
                                                    i3++;
                                                }
                                            }
                                            if (res != null && !res.isEmpty()) {
                                                for (Abstraction d3 : res) {
                                                    AliasProblem.this.manager.getForwardSolver().injectContext(AliasProblem.this.solver, dest, d3, src, source, d1);
                                                }
                                            }
                                            return AliasProblem.this.notifyOutFlowHandlers(src, d1, source, res, TaintPropagationHandler.FlowFunctionType.CallFlowFunction);
                                        }
                                        return null;
                                    }
                                    return null;
                                }
                                return null;
                            }
                            return null;
                        }
                        throw new AssertionError();
                    }
                };
            }

            @Override // heros.FlowFunctions
            public FlowFunction<Abstraction> getReturnFlowFunction(final Unit callSite, final SootMethod callee, final Unit exitStmt, Unit retSite) {
                final Value[] paramLocals = new Value[callee.getParameterCount()];
                for (int i = 0; i < callee.getParameterCount(); i++) {
                    paramLocals[i] = callee.getActiveBody().getParameterLocal(i);
                }
                final Stmt stmt = (Stmt) callSite;
                final InvokeExpr ie = (stmt == null || !stmt.containsInvokeExpr()) ? null : stmt.getInvokeExpr();
                final boolean isReflectiveCallSite = AliasProblem.this.interproceduralCFG().isReflectiveCallSite(ie);
                final Local thisLocal = callee.isStatic() ? null : callee.getActiveBody().getThisLocal();
                final boolean isExecutorExecute = AliasProblem.this.interproceduralCFG().isExecutorExecute(ie, callee);
                return new SolverReturnFlowFunction() { // from class: soot.jimple.infoflow.problems.AliasProblem.1.3
                    @Override // soot.jimple.infoflow.solver.functions.SolverReturnFlowFunction
                    public Set<Abstraction> computeTargets(Abstraction source, Abstraction d1, Collection<Abstraction> callerD1s) {
                        if (source == AliasProblem.this.getZeroValue()) {
                            return null;
                        }
                        if (AliasProblem.$assertionsDisabled || source.isAbstractionActive() || AliasProblem.this.manager.getConfig().getFlowSensitiveAliasing()) {
                            if (callSite != null) {
                                if (AliasProblem.this.taintPropagationHandler != null) {
                                    AliasProblem.this.taintPropagationHandler.notifyFlowIn(stmt, source, AliasProblem.this.manager, TaintPropagationHandler.FlowFunctionType.ReturnFlowFunction);
                                }
                                if (AliasProblem.this.manager.getConfig().getStaticFieldTrackingMode() != InfoflowConfiguration.StaticFieldTrackingMode.None && source.getAccessPath().isStaticFieldRef()) {
                                    AliasProblem.this.registerActivationCallSite(callSite, callee, source);
                                    return AliasProblem.this.notifyOutFlowHandlers(exitStmt, d1, source, Collections.singleton(source), TaintPropagationHandler.FlowFunctionType.ReturnFlowFunction);
                                }
                                Value sourceBase = source.getAccessPath().getPlainValue();
                                Set<Abstraction> res = new HashSet<>();
                                if (isExecutorExecute) {
                                    if (source.getAccessPath().getPlainValue() == thisLocal) {
                                        AccessPath ap = AliasProblem.this.manager.getAccessPathFactory().copyWithNewValue(source.getAccessPath(), ie.getArg(0));
                                        Abstraction abs = checkAbstraction(source.deriveNewAbstraction(ap, (Stmt) exitStmt));
                                        if (abs != null) {
                                            res.add(abs);
                                            AliasProblem.this.registerActivationCallSite(callSite, callee, abs);
                                        }
                                    }
                                } else {
                                    boolean parameterAliases = false;
                                    for (int i2 = 0; i2 < paramLocals.length; i2++) {
                                        if (paramLocals[i2] == sourceBase) {
                                            parameterAliases = true;
                                            if (callSite instanceof Stmt) {
                                                Value originalCallArg = ie.getArg(isReflectiveCallSite ? 1 : i2);
                                                if (AccessPath.canContainValue(originalCallArg) && ((isReflectiveCallSite || AliasProblem.this.manager.getTypeUtils().checkCast(source.getAccessPath(), originalCallArg.getType())) && !(source.getAccessPath().getBaseType() instanceof PrimType) && ((!TypeUtils.isStringType(source.getAccessPath().getBaseType()) || source.getAccessPath().getCanHaveImmutableAliases()) && !AliasProblem.this.interproceduralCFG().methodWritesValue(callee, paramLocals[i2])))) {
                                                    AccessPath ap2 = AliasProblem.this.manager.getAccessPathFactory().copyWithNewValue(source.getAccessPath(), originalCallArg, isReflectiveCallSite ? null : source.getAccessPath().getBaseType(), false);
                                                    Abstraction abs2 = checkAbstraction(source.deriveNewAbstraction(ap2, (Stmt) exitStmt));
                                                    if (abs2 != null) {
                                                        res.add(abs2);
                                                        AliasProblem.this.registerActivationCallSite(callSite, callee, abs2);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (!callee.isStatic() && thisLocal == sourceBase && AliasProblem.this.manager.getTypeUtils().hasCompatibleTypesForCall(source.getAccessPath(), callee.getDeclaringClass()) && !parameterAliases && (callSite instanceof Stmt)) {
                                        Stmt stmt2 = (Stmt) callSite;
                                        if (stmt2.getInvokeExpr() instanceof InstanceInvokeExpr) {
                                            InstanceInvokeExpr iIExpr = (InstanceInvokeExpr) stmt2.getInvokeExpr();
                                            Value callerBaseLocal = AliasProblem.this.interproceduralCFG().isReflectiveCallSite(iIExpr) ? iIExpr.getArg(0) : iIExpr.getBase();
                                            AccessPath ap3 = AliasProblem.this.manager.getAccessPathFactory().copyWithNewValue(source.getAccessPath(), callerBaseLocal, isReflectiveCallSite ? null : source.getAccessPath().getBaseType(), false);
                                            Abstraction abs3 = checkAbstraction(source.deriveNewAbstraction(ap3, (Stmt) exitStmt));
                                            if (abs3 != null) {
                                                res.add(abs3);
                                                AliasProblem.this.registerActivationCallSite(callSite, callee, abs3);
                                            }
                                        }
                                    }
                                }
                                for (Abstraction abs4 : res) {
                                    if (abs4 != source) {
                                        abs4.setCorrespondingCallSite((Stmt) callSite);
                                    }
                                }
                                return AliasProblem.this.notifyOutFlowHandlers(exitStmt, d1, source, res, TaintPropagationHandler.FlowFunctionType.ReturnFlowFunction);
                            }
                            return null;
                        }
                        throw new AssertionError();
                    }
                };
            }

            @Override // heros.FlowFunctions
            public FlowFunction<Abstraction> getCallToReturnFlowFunction(final Unit call, Unit returnSite) {
                final Stmt iStmt = (Stmt) call;
                InvokeExpr invExpr = iStmt.getInvokeExpr();
                final Value[] callArgs = new Value[iStmt.getInvokeExpr().getArgCount()];
                for (int i = 0; i < iStmt.getInvokeExpr().getArgCount(); i++) {
                    callArgs[i] = iStmt.getInvokeExpr().getArg(i);
                }
                final SootMethod callee = invExpr.getMethod();
                final DefinitionStmt defStmt = iStmt instanceof DefinitionStmt ? (DefinitionStmt) iStmt : null;
                return new SolverCallToReturnFlowFunction() { // from class: soot.jimple.infoflow.problems.AliasProblem.1.4
                    @Override // soot.jimple.infoflow.solver.functions.SolverCallToReturnFlowFunction
                    public Set<Abstraction> computeTargets(Abstraction d1, Abstraction source) {
                        Set<Abstraction> wrapperAliases;
                        if (source == AliasProblem.this.getZeroValue()) {
                            return null;
                        }
                        if (AliasProblem.$assertionsDisabled || source.isAbstractionActive() || AliasProblem.this.manager.getConfig().getFlowSensitiveAliasing()) {
                            if (AliasProblem.this.taintPropagationHandler != null) {
                                AliasProblem.this.taintPropagationHandler.notifyFlowIn(call, source, AliasProblem.this.manager, TaintPropagationHandler.FlowFunctionType.CallToReturnFlowFunction);
                            }
                            if (AliasProblem.this.taintWrapper != null && (wrapperAliases = AliasProblem.this.taintWrapper.getAliasesForMethod(iStmt, d1, source)) != null && !wrapperAliases.isEmpty()) {
                                Set<Abstraction> passOnSet = new HashSet<>(wrapperAliases.size());
                                for (Abstraction abs : wrapperAliases) {
                                    if (defStmt == null || defStmt.getLeftOp() != abs.getAccessPath().getPlainValue()) {
                                        passOnSet.add(abs);
                                    }
                                    for (Unit u : AliasProblem.this.interproceduralCFG().getPredsOf(call)) {
                                        AliasProblem.this.manager.getForwardSolver().processEdge(new PathEdge<>(d1, u, abs));
                                    }
                                }
                                return AliasProblem.this.notifyOutFlowHandlers(call, d1, source, passOnSet, TaintPropagationHandler.FlowFunctionType.CallToReturnFlowFunction);
                            }
                            boolean mustPropagate = AliasProblem.this.isExcluded(callee) | AliasProblem.this.interproceduralCFG().getCalleesOfCallAt(call).isEmpty();
                            if (!mustPropagate && AliasProblem.this.manager.getConfig().getStaticFieldTrackingMode() != InfoflowConfiguration.StaticFieldTrackingMode.None && source.getAccessPath().isStaticFieldRef() && AliasProblem.this.interproceduralCFG().isStaticFieldUsed(callee, source.getAccessPath().getFirstField())) {
                                return null;
                            }
                            if ((iStmt instanceof DefinitionStmt) && ((DefinitionStmt) iStmt).getLeftOp() == source.getAccessPath().getPlainValue()) {
                                return null;
                            }
                            if (!mustPropagate && (iStmt.getInvokeExpr() instanceof InstanceInvokeExpr)) {
                                InstanceInvokeExpr iinv = (InstanceInvokeExpr) iStmt.getInvokeExpr();
                                if (iinv.getBase() == source.getAccessPath().getPlainValue() && !AliasProblem.this.interproceduralCFG().getCalleesOfCallAt(call).isEmpty()) {
                                    return null;
                                }
                            }
                            if (!mustPropagate) {
                                for (int i2 = 0; i2 < callArgs.length; i2++) {
                                    if (callArgs[i2] == source.getAccessPath().getPlainValue()) {
                                        return null;
                                    }
                                }
                            }
                            return AliasProblem.this.notifyOutFlowHandlers(call, d1, source, Collections.singleton(source), TaintPropagationHandler.FlowFunctionType.CallToReturnFlowFunction);
                        }
                        throw new AssertionError();
                    }
                };
            }
        };
    }
}
