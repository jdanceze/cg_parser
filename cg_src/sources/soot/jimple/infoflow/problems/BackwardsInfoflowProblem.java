package soot.jimple.infoflow.problems;

import heros.FlowFunction;
import heros.FlowFunctions;
import heros.flowfunc.KillAll;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import soot.ArrayType;
import soot.Local;
import soot.NullType;
import soot.PrimType;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AnyNewExpr;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.Constant;
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
import soot.jimple.infoflow.solver.cfg.IInfoflowCFG;
import soot.jimple.infoflow.solver.functions.SolverCallFlowFunction;
import soot.jimple.infoflow.solver.functions.SolverCallToReturnFlowFunction;
import soot.jimple.infoflow.solver.functions.SolverNormalFlowFunction;
import soot.jimple.infoflow.solver.functions.SolverReturnFlowFunction;
import soot.jimple.infoflow.typing.TypeUtils;
import soot.jimple.infoflow.util.BaseSelector;
import soot.jimple.infoflow.util.ByReferenceBoolean;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/problems/BackwardsInfoflowProblem.class */
public class BackwardsInfoflowProblem extends AbstractInfoflowProblem {
    public BackwardsInfoflowProblem(InfoflowManager manager, Abstraction zeroValue, IPropagationRuleManagerFactory ruleManagerFactory) {
        super(manager, zeroValue, ruleManagerFactory);
    }

    @Override // heros.template.DefaultIFDSTabulationProblem
    protected FlowFunctions<Unit, Abstraction, SootMethod> createFlowFunctionsFactory() {
        return new FlowFunctions<Unit, Abstraction, SootMethod>() { // from class: soot.jimple.infoflow.problems.BackwardsInfoflowProblem.1
            @Override // heros.FlowFunctions
            public FlowFunction<Abstraction> getNormalFlowFunction(final Unit srcUnit, final Unit destUnit) {
                if (!(srcUnit instanceof Stmt)) {
                    return KillAll.v();
                }
                final Aliasing aliasing = BackwardsInfoflowProblem.this.manager.getAliasing();
                if (aliasing == null) {
                    return KillAll.v();
                }
                return new SolverNormalFlowFunction() { // from class: soot.jimple.infoflow.problems.BackwardsInfoflowProblem.1.1
                    @Override // soot.jimple.infoflow.solver.functions.SolverNormalFlowFunction
                    public Set<Abstraction> computeTargets(Abstraction d1, Abstraction source) {
                        if (BackwardsInfoflowProblem.this.taintPropagationHandler != null) {
                            BackwardsInfoflowProblem.this.taintPropagationHandler.notifyFlowIn(srcUnit, source, BackwardsInfoflowProblem.this.manager, TaintPropagationHandler.FlowFunctionType.NormalFlowFunction);
                        }
                        Set<Abstraction> res = computeTargetsInternal(d1, source);
                        return BackwardsInfoflowProblem.this.notifyOutFlowHandlers(srcUnit, d1, source, res, TaintPropagationHandler.FlowFunctionType.NormalFlowFunction);
                    }

                    private Set<Abstraction> computeTargetsInternal(Abstraction d1, Abstraction source) {
                        AccessPath newAp;
                        Set<Abstraction> res = null;
                        ByReferenceBoolean killSource = new ByReferenceBoolean();
                        ByReferenceBoolean killAll = new ByReferenceBoolean();
                        if (BackwardsInfoflowProblem.this.propagationRules != null) {
                            res = BackwardsInfoflowProblem.this.propagationRules.applyNormalFlowFunction(d1, source, (Stmt) srcUnit, (Stmt) destUnit, killSource, killAll);
                        }
                        if (killAll.value) {
                            return null;
                        }
                        if (res == null) {
                            res = new HashSet<>();
                        }
                        if (source.getAccessPath().isEmpty()) {
                            if (killSource.value) {
                                res.remove(source);
                            }
                            return res;
                        } else if (!(srcUnit instanceof AssignStmt)) {
                            return res;
                        } else {
                            AssignStmt assignStmt = (AssignStmt) srcUnit;
                            Value leftVal = assignStmt.getLeftOp();
                            Value rightOp = assignStmt.getRightOp();
                            Value[] rightVals = BaseSelector.selectBaseList(rightOp, true);
                            AccessPath ap = source.getAccessPath();
                            Local sourceBase = ap.getPlainValue();
                            boolean keepSource = false;
                            for (Value rightVal : rightVals) {
                                boolean addLeftValue = false;
                                boolean cutFirstFieldLeft = false;
                                boolean createNewVal = false;
                                Type leftType = null;
                                if (rightVal instanceof StaticFieldRef) {
                                    StaticFieldRef staticRef = (StaticFieldRef) rightVal;
                                    AccessPath mappedAp = aliasing.mayAlias(ap, staticRef);
                                    if (BackwardsInfoflowProblem.this.manager.getConfig().getStaticFieldTrackingMode() != InfoflowConfiguration.StaticFieldTrackingMode.None && mappedAp != null) {
                                        addLeftValue = true;
                                        cutFirstFieldLeft = true;
                                        if (!mappedAp.equals(ap)) {
                                            ap = mappedAp;
                                            source = source.deriveNewAbstraction(ap, null);
                                        }
                                    }
                                } else if (rightVal instanceof InstanceFieldRef) {
                                    InstanceFieldRef instRef = (InstanceFieldRef) rightVal;
                                    if (instRef.getBase().getType() instanceof NullType) {
                                        return null;
                                    }
                                    AccessPath mappedAp2 = aliasing.mayAlias(ap, instRef);
                                    if (mappedAp2 != null) {
                                        addLeftValue = true;
                                        cutFirstFieldLeft = mappedAp2.getFragmentCount() > 0 && mappedAp2.getFirstField() == instRef.getField();
                                        if (!mappedAp2.equals(ap)) {
                                            ap = mappedAp2;
                                        }
                                    } else if (aliasing.mayAlias(instRef.getBase(), sourceBase) && ap.getTaintSubFields() && ap.getFragmentCount() == 0) {
                                        addLeftValue = true;
                                        createNewVal = true;
                                    }
                                } else if (rightVal instanceof ArrayRef) {
                                    if (BackwardsInfoflowProblem.this.getManager().getConfig().getEnableArrayTracking() && ap.getArrayTaintType() != AccessPath.ArrayTaintType.Length) {
                                        ArrayRef arrayRef = (ArrayRef) rightVal;
                                        if (BackwardsInfoflowProblem.this.getManager().getConfig().getImplicitFlowMode().trackArrayAccesses()) {
                                            if (arrayRef.getIndex() == sourceBase) {
                                                addLeftValue = true;
                                                leftType = ((ArrayType) arrayRef.getBase().getType()).getElementType();
                                            }
                                        } else if (aliasing.mayAlias(arrayRef.getBase(), sourceBase)) {
                                            addLeftValue = true;
                                            leftType = ((ArrayType) arrayRef.getBase().getType()).getElementType();
                                        }
                                    }
                                }
                                if (rightVal == sourceBase) {
                                    addLeftValue = true;
                                    leftType = ap.getBaseType();
                                    if (leftVal instanceof ArrayRef) {
                                        leftType = TypeUtils.buildArrayOrAddDimension(leftType, ((ArrayRef) leftVal).getType().getArrayType());
                                    } else if (rightOp instanceof InstanceOfExpr) {
                                        createNewVal = true;
                                    } else if (rightOp instanceof LengthExpr) {
                                        if (ap.getArrayTaintType() == AccessPath.ArrayTaintType.Contents) {
                                            addLeftValue = false;
                                        }
                                        createNewVal = true;
                                    } else if (!(rightOp instanceof NewArrayExpr)) {
                                        if (!BackwardsInfoflowProblem.this.manager.getTypeUtils().checkCast(source.getAccessPath(), leftVal.getType())) {
                                            return null;
                                        }
                                    } else {
                                        createNewVal = true;
                                    }
                                    if (rightVal instanceof CastExpr) {
                                        CastExpr ce = (CastExpr) rightOp;
                                        if (!BackwardsInfoflowProblem.this.manager.getHierarchy().canStoreType(leftType, ce.getCastType())) {
                                            leftType = ce.getCastType();
                                        }
                                    }
                                }
                                if (addLeftValue) {
                                    if (createNewVal) {
                                        newAp = BackwardsInfoflowProblem.this.manager.getAccessPathFactory().createAccessPath(leftVal, true);
                                    } else {
                                        newAp = BackwardsInfoflowProblem.this.manager.getAccessPathFactory().copyWithNewValue(ap, leftVal, leftType, cutFirstFieldLeft);
                                    }
                                    Abstraction newAbs = source.deriveNewAbstraction(newAp, assignStmt);
                                    if (newAbs != null && aliasing.canHaveAliasesRightSide(assignStmt, leftVal, newAbs)) {
                                        for (Unit pred : BackwardsInfoflowProblem.this.manager.getICFG().getPredsOf(srcUnit)) {
                                            aliasing.computeAliases(d1, (Stmt) pred, leftVal, Collections.singleton(newAbs), BackwardsInfoflowProblem.this.interproceduralCFG().getMethodOf(pred), newAbs);
                                        }
                                    }
                                }
                                boolean addRightValue = false;
                                boolean cutFirstField = false;
                                Type rightType = null;
                                if (leftVal instanceof StaticFieldRef) {
                                    StaticFieldRef staticRef2 = (StaticFieldRef) leftVal;
                                    AccessPath mappedAp3 = aliasing.mayAlias(ap, staticRef2);
                                    if (BackwardsInfoflowProblem.this.getManager().getConfig().getStaticFieldTrackingMode() != InfoflowConfiguration.StaticFieldTrackingMode.None && mappedAp3 != null) {
                                        addRightValue = true;
                                        cutFirstField = true;
                                        rightType = mappedAp3.getFirstFieldType();
                                        if (!mappedAp3.equals(ap)) {
                                            ap = mappedAp3;
                                            source = source.deriveNewAbstraction(ap, null);
                                        }
                                    }
                                } else if (leftVal instanceof InstanceFieldRef) {
                                    InstanceFieldRef instRef2 = (InstanceFieldRef) leftVal;
                                    if (instRef2.getBase().getType() instanceof NullType) {
                                        return null;
                                    }
                                    AccessPath mappedAp4 = aliasing.mayAlias(ap, instRef2);
                                    if (mappedAp4 != null) {
                                        addRightValue = true;
                                        cutFirstField = mappedAp4.getFragmentCount() > 0 && mappedAp4.getFirstField() == instRef2.getField();
                                        rightType = mappedAp4.getFirstFieldType();
                                        if (!mappedAp4.equals(ap)) {
                                            ap = mappedAp4;
                                            source = source.deriveNewAbstraction(ap, null);
                                        }
                                    } else if (aliasing.mayAlias(instRef2.getBase(), sourceBase) && ap.getTaintSubFields() && ap.getFragmentCount() == 0) {
                                        addRightValue = true;
                                        rightType = instRef2.getField().getType();
                                        keepSource = true;
                                    }
                                } else if (leftVal instanceof ArrayRef) {
                                    if (BackwardsInfoflowProblem.this.getManager().getConfig().getEnableArrayTracking() && ap.getArrayTaintType() != AccessPath.ArrayTaintType.Length) {
                                        ArrayRef arrayRef2 = (ArrayRef) leftVal;
                                        if (BackwardsInfoflowProblem.this.getManager().getConfig().getImplicitFlowMode().trackArrayAccesses()) {
                                            if (arrayRef2.getIndex() == sourceBase) {
                                                addRightValue = true;
                                                rightType = ((ArrayType) arrayRef2.getBase().getType()).getElementType();
                                            }
                                        } else if (aliasing.mayAlias(arrayRef2.getBase(), sourceBase)) {
                                            addRightValue = true;
                                            rightType = ((ArrayType) arrayRef2.getBase().getType()).getElementType();
                                            keepSource = true;
                                        }
                                    }
                                } else if (aliasing.mayAlias(leftVal, sourceBase)) {
                                    if (rightOp instanceof InstanceOfExpr) {
                                        rightType = rightVal.getType();
                                    } else if (rightOp instanceof CastExpr) {
                                        if (!BackwardsInfoflowProblem.this.manager.getTypeUtils().checkCast(((CastExpr) rightOp).getCastType(), rightVal.getType())) {
                                            return null;
                                        }
                                    }
                                    addRightValue = ((rightOp instanceof LengthExpr) || (rightOp instanceof ArrayRef)) ? false : true;
                                }
                                if (addRightValue) {
                                    if (!keepSource) {
                                        res.remove(source);
                                    }
                                    boolean isImplicit = source.getDominator() != null;
                                    if (isImplicit) {
                                        res.add(source.deriveConditionalUpdate(assignStmt));
                                    }
                                    if (!(rightVal instanceof Constant) && !(rightOp instanceof AnyNewExpr)) {
                                        AccessPath newAp2 = BackwardsInfoflowProblem.this.manager.getAccessPathFactory().copyWithNewValue(ap, rightVal, rightType, cutFirstField);
                                        Abstraction newAbs2 = source.deriveNewAbstraction(newAp2, assignStmt);
                                        if (newAbs2 != null) {
                                            if ((rightVal instanceof StaticFieldRef) && BackwardsInfoflowProblem.this.manager.getConfig().getStaticFieldTrackingMode() == InfoflowConfiguration.StaticFieldTrackingMode.ContextFlowInsensitive) {
                                                BackwardsInfoflowProblem.this.manager.getGlobalTaintManager().addToGlobalTaintState(newAbs2);
                                            } else {
                                                enterConditional(newAbs2, assignStmt, destUnit);
                                                if (isPrimitiveOrStringBase(source)) {
                                                    newAbs2 = newAbs2.deriveNewAbstractionWithTurnUnit(srcUnit);
                                                } else if ((leftVal instanceof FieldRef) && isPrimitiveOrStringType(((FieldRef) leftVal).getField().getType()) && !ap.getCanHaveImmutableAliases()) {
                                                    newAbs2 = newAbs2.deriveNewAbstractionWithTurnUnit(srcUnit);
                                                } else if (aliasing.canHaveAliasesRightSide(assignStmt, rightVal, newAbs2)) {
                                                    for (Unit pred2 : BackwardsInfoflowProblem.this.manager.getICFG().getPredsOf(assignStmt)) {
                                                        aliasing.computeAliases(d1, (Stmt) pred2, rightVal, res, BackwardsInfoflowProblem.this.interproceduralCFG().getMethodOf(pred2), newAbs2);
                                                    }
                                                }
                                                res.add(newAbs2);
                                            }
                                        }
                                    }
                                }
                            }
                            return res;
                        }
                    }
                };
            }

            @Override // heros.FlowFunctions
            public FlowFunction<Abstraction> getCallFlowFunction(final Unit callStmt, final SootMethod dest) {
                if (!dest.isConcrete()) {
                    BackwardsInfoflowProblem.this.logger.debug("Call skipped because target has no body: {} -> {}", callStmt, dest);
                    return KillAll.v();
                }
                final Aliasing aliasing = BackwardsInfoflowProblem.this.manager.getAliasing();
                if (aliasing == null) {
                    return KillAll.v();
                }
                if (!(callStmt instanceof Stmt)) {
                    return KillAll.v();
                }
                final Stmt stmt = (Stmt) callStmt;
                final InvokeExpr ie = stmt.containsInvokeExpr() ? stmt.getInvokeExpr() : null;
                final Local[] paramLocals = (Local[]) dest.getActiveBody().getParameterLocals().toArray(new Local[0]);
                final Local thisLocal = dest.isStatic() ? null : dest.getActiveBody().getThisLocal();
                final boolean isSource = (BackwardsInfoflowProblem.this.manager.getSourceSinkManager() == null || BackwardsInfoflowProblem.this.manager.getSourceSinkManager().getSourceInfo((Stmt) callStmt, BackwardsInfoflowProblem.this.manager) == null) ? false : true;
                final boolean isSink = (BackwardsInfoflowProblem.this.manager.getSourceSinkManager() == null || BackwardsInfoflowProblem.this.manager.getSourceSinkManager().getSinkInfo(stmt, BackwardsInfoflowProblem.this.manager, null) == null) ? false : true;
                final boolean isExecutorExecute = BackwardsInfoflowProblem.this.interproceduralCFG().isExecutorExecute(ie, dest);
                final boolean isReflectiveCallSite = BackwardsInfoflowProblem.this.interproceduralCFG().isReflectiveCallSite(ie);
                return new SolverCallFlowFunction() { // from class: soot.jimple.infoflow.problems.BackwardsInfoflowProblem.1.2
                    @Override // soot.jimple.infoflow.solver.functions.SolverCallFlowFunction
                    public Set<Abstraction> computeTargets(Abstraction d1, Abstraction source) {
                        if (source != BackwardsInfoflowProblem.this.getZeroValue()) {
                            if (BackwardsInfoflowProblem.this.taintPropagationHandler != null) {
                                BackwardsInfoflowProblem.this.taintPropagationHandler.notifyFlowIn(stmt, source, BackwardsInfoflowProblem.this.manager, TaintPropagationHandler.FlowFunctionType.CallFlowFunction);
                            }
                            Set<Abstraction> res = computeTargetsInternal(d1, source);
                            if (res != null) {
                                for (Abstraction abs : res) {
                                    aliasing.getAliasingStrategy().injectCallingContext(abs, BackwardsInfoflowProblem.this.solver, dest, callStmt, source, d1);
                                }
                            }
                            return BackwardsInfoflowProblem.this.notifyOutFlowHandlers(stmt, d1, source, res, TaintPropagationHandler.FlowFunctionType.CallFlowFunction);
                        }
                        return null;
                    }

                    private Set<Abstraction> computeTargetsInternal(Abstraction d1, Abstraction source) {
                        Value[] valueArr;
                        Abstraction abs;
                        if (!BackwardsInfoflowProblem.this.manager.getConfig().getStopAfterFirstFlow() || BackwardsInfoflowProblem.this.results.isEmpty()) {
                            if (BackwardsInfoflowProblem.this.manager.getConfig().getInspectSources() || !isSource) {
                                if (!BackwardsInfoflowProblem.this.manager.getConfig().getInspectSinks() && isSink) {
                                    return null;
                                }
                                if ((BackwardsInfoflowProblem.this.manager.getConfig().getStaticFieldTrackingMode() == InfoflowConfiguration.StaticFieldTrackingMode.None && dest.isStaticInitializer()) || BackwardsInfoflowProblem.this.isExcluded(dest)) {
                                    return null;
                                }
                                if (BackwardsInfoflowProblem.this.manager.getConfig().getStaticFieldTrackingMode() != InfoflowConfiguration.StaticFieldTrackingMode.None && source.getAccessPath().isStaticFieldRef() && !BackwardsInfoflowProblem.this.interproceduralCFG().isStaticFieldUsed(dest, source.getAccessPath().getFirstField()) && !BackwardsInfoflowProblem.this.interproceduralCFG().isStaticFieldRead(dest, source.getAccessPath().getFirstField())) {
                                    return null;
                                }
                                Set<Abstraction> res = null;
                                ByReferenceBoolean killAll = new ByReferenceBoolean();
                                if (BackwardsInfoflowProblem.this.propagationRules != null) {
                                    res = BackwardsInfoflowProblem.this.propagationRules.applyCallFlowFunction(d1, source, stmt, dest, killAll);
                                }
                                if (killAll.value) {
                                    return null;
                                }
                                if (res == null) {
                                    res = new HashSet<>();
                                }
                                if (callStmt instanceof AssignStmt) {
                                    AssignStmt assignStmt = (AssignStmt) callStmt;
                                    Value left = assignStmt.getLeftOp();
                                    boolean isImplicit = source.getDominator() != null;
                                    if (aliasing.mayAlias(left, source.getAccessPath().getPlainValue()) && !isImplicit) {
                                        Iterator<Unit> it = dest.getActiveBody().getUnits().iterator();
                                        while (it.hasNext()) {
                                            Unit unit = it.next();
                                            if (unit instanceof ReturnStmt) {
                                                ReturnStmt returnStmt = (ReturnStmt) unit;
                                                Value retVal = returnStmt.getOp();
                                                if ((retVal instanceof Local) && BackwardsInfoflowProblem.this.manager.getTypeUtils().checkCast(source.getAccessPath().getBaseType(), retVal.getType())) {
                                                    AccessPath ap = BackwardsInfoflowProblem.this.manager.getAccessPathFactory().copyWithNewValue(source.getAccessPath(), retVal, returnStmt.getOp().getType(), false);
                                                    Abstraction abs2 = source.deriveNewAbstraction(ap, stmt);
                                                    if (abs2 != null) {
                                                        if (isPrimitiveOrStringBase(source)) {
                                                            abs2 = abs2.deriveNewAbstractionWithTurnUnit(stmt);
                                                        }
                                                        if (abs2.getDominator() == null && BackwardsInfoflowProblem.this.manager.getConfig().getImplicitFlowMode().trackControlFlowDependencies()) {
                                                            List<Unit> condUnits = BackwardsInfoflowProblem.this.manager.getICFG().getConditionalBranchIntraprocedural(returnStmt);
                                                            if (condUnits.size() >= 1) {
                                                                abs2.setDominator(condUnits.get(0));
                                                                for (int i = 1; i < condUnits.size(); i++) {
                                                                    res.add(abs2.deriveNewAbstractionWithDominator(condUnits.get(i)));
                                                                }
                                                            }
                                                        }
                                                        res.add(abs2);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                if (BackwardsInfoflowProblem.this.manager.getConfig().getStaticFieldTrackingMode() != InfoflowConfiguration.StaticFieldTrackingMode.None && source.getAccessPath().isStaticFieldRef() && (abs = source.deriveNewAbstraction(source.getAccessPath(), stmt)) != null) {
                                    res.add(abs);
                                }
                                if (!isExecutorExecute && !source.getAccessPath().isStaticFieldRef() && !dest.isStatic()) {
                                    InstanceInvokeExpr instanceInvokeExpr = (InstanceInvokeExpr) stmt.getInvokeExpr();
                                    Value callBase = isReflectiveCallSite ? instanceInvokeExpr.getArg(0) : instanceInvokeExpr.getBase();
                                    Value sourceBase = source.getAccessPath().getPlainValue();
                                    if (aliasing.mayAlias(callBase, sourceBase) && BackwardsInfoflowProblem.this.manager.getTypeUtils().hasCompatibleTypesForCall(source.getAccessPath(), dest.getDeclaringClass()) && (isReflectiveCallSite || instanceInvokeExpr.getArgs().stream().noneMatch(arg -> {
                                        return arg == sourceBase;
                                    }))) {
                                        AccessPath ap2 = BackwardsInfoflowProblem.this.manager.getAccessPathFactory().copyWithNewValue(source.getAccessPath(), thisLocal);
                                        Abstraction abs3 = source.deriveNewAbstraction(ap2, (Stmt) callStmt);
                                        if (abs3 != null) {
                                            res.add(abs3);
                                        }
                                    }
                                }
                                if (isExecutorExecute) {
                                    if (ie != null && aliasing.mayAlias(ie.getArg(0), source.getAccessPath().getPlainValue())) {
                                        AccessPath ap3 = BackwardsInfoflowProblem.this.manager.getAccessPathFactory().copyWithNewValue(source.getAccessPath(), thisLocal);
                                        Abstraction abs4 = source.deriveNewAbstraction(ap3, stmt);
                                        if (abs4 != null) {
                                            res.add(abs4);
                                        }
                                    }
                                } else if (ie != null && dest.getParameterCount() > 0 && (isReflectiveCallSite || ie.getArgCount() == dest.getParameterCount())) {
                                    for (int i2 = isReflectiveCallSite ? 1 : 0; i2 < ie.getArgCount(); i2++) {
                                        if (aliasing.mayAlias(ie.getArg(i2), source.getAccessPath().getPlainValue()) && !isPrimitiveOrStringBase(source) && source.getAccessPath().getTaintSubFields() && !BackwardsInfoflowProblem.this.interproceduralCFG().methodWritesValue(dest, paramLocals[i2])) {
                                            if (isReflectiveCallSite) {
                                                for (Value param : paramLocals) {
                                                    AccessPath ap4 = BackwardsInfoflowProblem.this.manager.getAccessPathFactory().copyWithNewValue(source.getAccessPath(), param, null, false);
                                                    Abstraction abs5 = source.deriveNewAbstraction(ap4, stmt);
                                                    if (abs5 != null) {
                                                        res.add(abs5);
                                                    }
                                                }
                                            } else {
                                                AccessPath ap5 = BackwardsInfoflowProblem.this.manager.getAccessPathFactory().copyWithNewValue(source.getAccessPath(), paramLocals[i2]);
                                                Abstraction abs6 = source.deriveNewAbstraction(ap5, stmt);
                                                if (abs6 != null) {
                                                    res.add(abs6);
                                                }
                                            }
                                        }
                                    }
                                }
                                return res;
                            }
                            return null;
                        }
                        return null;
                    }
                };
            }

            @Override // heros.FlowFunctions
            public FlowFunction<Abstraction> getReturnFlowFunction(final Unit callSite, final SootMethod callee, final Unit exitSite, final Unit returnSite) {
                if (callSite != null && !(callSite instanceof Stmt)) {
                    return KillAll.v();
                }
                final Aliasing aliasing = BackwardsInfoflowProblem.this.manager.getAliasing();
                if (aliasing == null) {
                    return KillAll.v();
                }
                final Value[] paramLocals = new Value[callee.getParameterCount()];
                for (int i = 0; i < callee.getParameterCount(); i++) {
                    paramLocals[i] = callee.getActiveBody().getParameterLocal(i);
                }
                final Stmt stmt = (Stmt) callSite;
                final InvokeExpr ie = (stmt == null || !stmt.containsInvokeExpr()) ? null : stmt.getInvokeExpr();
                final boolean isReflectiveCallSite = BackwardsInfoflowProblem.this.interproceduralCFG().isReflectiveCallSite(ie);
                final Stmt callStmt = (Stmt) callSite;
                final Stmt exitStmt = (Stmt) exitSite;
                final Local thisLocal = callee.isStatic() ? null : callee.getActiveBody().getThisLocal();
                final boolean isExecutorExecute = BackwardsInfoflowProblem.this.interproceduralCFG().isExecutorExecute(ie, callee);
                return new SolverReturnFlowFunction() { // from class: soot.jimple.infoflow.problems.BackwardsInfoflowProblem.1.3
                    @Override // soot.jimple.infoflow.solver.functions.SolverReturnFlowFunction
                    public Set<Abstraction> computeTargets(Abstraction source, Abstraction calleeD1, Collection<Abstraction> callerD1s) {
                        if (source != BackwardsInfoflowProblem.this.getZeroValue() && callSite != null) {
                            if (BackwardsInfoflowProblem.this.taintPropagationHandler != null) {
                                BackwardsInfoflowProblem.this.taintPropagationHandler.notifyFlowIn(stmt, source, BackwardsInfoflowProblem.this.manager, TaintPropagationHandler.FlowFunctionType.ReturnFlowFunction);
                            }
                            Set<Abstraction> res = computeTargetsInternal(source, calleeD1, callerD1s);
                            return BackwardsInfoflowProblem.this.notifyOutFlowHandlers(exitSite, calleeD1, source, res, TaintPropagationHandler.FlowFunctionType.ReturnFlowFunction);
                        }
                        return null;
                    }

                    /* JADX WARN: Code restructure failed: missing block: B:42:0x0149, code lost:
                        if (r0.noneMatch((v2) -> { // java.util.function.Predicate.test(java.lang.Object):boolean
                            return lambda$0(r1, r2, v2);
                        }) != false) goto L40;
                     */
                    /*
                        Code decompiled incorrectly, please refer to instructions dump.
                        To view partially-correct add '--show-bad-code' argument
                    */
                    private java.util.Set<soot.jimple.infoflow.data.Abstraction> computeTargetsInternal(soot.jimple.infoflow.data.Abstraction r10, soot.jimple.infoflow.data.Abstraction r11, java.util.Collection<soot.jimple.infoflow.data.Abstraction> r12) {
                        /*
                            Method dump skipped, instructions count: 1223
                            To view this dump add '--comments-level debug' option
                        */
                        throw new UnsupportedOperationException("Method not decompiled: soot.jimple.infoflow.problems.BackwardsInfoflowProblem.AnonymousClass1.AnonymousClass3.computeTargetsInternal(soot.jimple.infoflow.data.Abstraction, soot.jimple.infoflow.data.Abstraction, java.util.Collection):java.util.Set");
                    }
                };
            }

            @Override // heros.FlowFunctions
            public FlowFunction<Abstraction> getCallToReturnFlowFunction(final Unit callSite, final Unit returnSite) {
                if (!(callSite instanceof Stmt)) {
                    return KillAll.v();
                }
                final Aliasing aliasing = BackwardsInfoflowProblem.this.manager.getAliasing();
                if (aliasing == null) {
                    return KillAll.v();
                }
                final Stmt callStmt = (Stmt) callSite;
                final InvokeExpr invExpr = callStmt.getInvokeExpr();
                final Value[] callArgs = new Value[invExpr.getArgCount()];
                for (int i = 0; i < invExpr.getArgCount(); i++) {
                    callArgs[i] = invExpr.getArg(i);
                }
                final SootMethod callee = invExpr.getMethod();
                final boolean isSink = (BackwardsInfoflowProblem.this.manager.getSourceSinkManager() == null || BackwardsInfoflowProblem.this.manager.getSourceSinkManager().getSinkInfo(callStmt, BackwardsInfoflowProblem.this.manager, null) == null) ? false : true;
                return new SolverCallToReturnFlowFunction() { // from class: soot.jimple.infoflow.problems.BackwardsInfoflowProblem.1.4
                    @Override // soot.jimple.infoflow.solver.functions.SolverCallToReturnFlowFunction
                    public Set<Abstraction> computeTargets(Abstraction d1, Abstraction source) {
                        if (BackwardsInfoflowProblem.this.taintPropagationHandler != null) {
                            BackwardsInfoflowProblem.this.taintPropagationHandler.notifyFlowIn(callSite, source, BackwardsInfoflowProblem.this.manager, TaintPropagationHandler.FlowFunctionType.CallToReturnFlowFunction);
                        }
                        Set<Abstraction> res = computeTargetsInternal(d1, source);
                        return BackwardsInfoflowProblem.this.notifyOutFlowHandlers(callSite, d1, source, res, TaintPropagationHandler.FlowFunctionType.CallToReturnFlowFunction);
                    }

                    private Set<Abstraction> computeTargetsInternal(Abstraction d1, Abstraction source) {
                        if (BackwardsInfoflowProblem.this.manager.getConfig().getStopAfterFirstFlow() && !BackwardsInfoflowProblem.this.results.isEmpty()) {
                            return null;
                        }
                        Set<Abstraction> res = null;
                        ByReferenceBoolean killSource = new ByReferenceBoolean();
                        ByReferenceBoolean killAll = new ByReferenceBoolean();
                        if (BackwardsInfoflowProblem.this.propagationRules != null) {
                            res = BackwardsInfoflowProblem.this.propagationRules.applyCallToReturnFlowFunction(d1, source, callStmt, killSource, killAll, true);
                        }
                        if (killAll.value) {
                            return null;
                        }
                        if (res == null) {
                            res = new HashSet<>();
                        }
                        if (!(callStmt instanceof AssignStmt) || !aliasing.mayAlias(((AssignStmt) callStmt).getLeftOp(), source.getAccessPath().getPlainValue())) {
                            if (BackwardsInfoflowProblem.this.interproceduralCFG().getCalleesOfCallAt(callSite).isEmpty()) {
                                if (source != BackwardsInfoflowProblem.this.zeroValue) {
                                    res.add(source);
                                }
                                return res;
                            }
                            if (isSink && !BackwardsInfoflowProblem.this.manager.getConfig().getInspectSinks() && source != BackwardsInfoflowProblem.this.zeroValue) {
                                res.add(source);
                            }
                            if (BackwardsInfoflowProblem.this.isExcluded(callee) && source != BackwardsInfoflowProblem.this.zeroValue) {
                                res.add(source);
                            }
                            if (BackwardsInfoflowProblem.this.manager.getConfig().getStaticFieldTrackingMode() != InfoflowConfiguration.StaticFieldTrackingMode.None && source.getAccessPath().isStaticFieldRef() && BackwardsInfoflowProblem.this.interproceduralCFG().isStaticFieldUsed(callee, source.getAccessPath().getFirstField())) {
                                return res;
                            }
                            if (callee.isNative() && BackwardsInfoflowProblem.this.ncHandler != null) {
                                Value[] valueArr = callArgs;
                                int length = valueArr.length;
                                int i2 = 0;
                                while (true) {
                                    if (i2 >= length) {
                                        break;
                                    }
                                    Value arg = valueArr[i2];
                                    if (aliasing.mayAlias(arg, source.getAccessPath().getPlainValue())) {
                                        Set<Abstraction> nativeAbs = BackwardsInfoflowProblem.this.ncHandler.getTaintedValues(callStmt, source, callArgs);
                                        if (nativeAbs != null) {
                                            res.addAll(nativeAbs);
                                            for (Abstraction abs : nativeAbs) {
                                                enterConditional(abs, callStmt, returnSite);
                                                if (abs.getAccessPath().isStaticFieldRef() || aliasing.canHaveAliasesRightSide(callStmt, abs.getAccessPath().getPlainValue(), abs)) {
                                                    for (Unit pred : BackwardsInfoflowProblem.this.manager.getICFG().getPredsOf(callStmt)) {
                                                        aliasing.computeAliases(d1, (Stmt) pred, abs.getAccessPath().getPlainValue(), res, BackwardsInfoflowProblem.this.interproceduralCFG().getMethodOf(pred), abs);
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        i2++;
                                    }
                                }
                            }
                            if ((invExpr instanceof InstanceInvokeExpr) && aliasing.mayAlias(((InstanceInvokeExpr) invExpr).getBase(), source.getAccessPath().getPlainValue()) && ((source.getAccessPath().getTaintSubFields() || source.getAccessPath().getFragmentCount() > 0) && !callee.isNative())) {
                                return res;
                            }
                            Stream stream = Arrays.stream(callArgs);
                            Aliasing aliasing2 = aliasing;
                            if (stream.anyMatch(arg2 -> {
                                return !isPrimitiveOrStringBase(r5) && source.mayAlias(aliasing2, r5.getAccessPath().getPlainValue());
                            })) {
                                return res;
                            }
                            if (!killSource.value && source != BackwardsInfoflowProblem.this.zeroValue) {
                                res.add(source);
                            }
                            if (BackwardsInfoflowProblem.this.manager.getConfig().getImplicitFlowMode().trackControlFlowDependencies() && source.getDominator() == null && res.contains(source)) {
                                IInfoflowCFG.UnitContainer dom = BackwardsInfoflowProblem.this.manager.getICFG().getDominatorOf(callSite);
                                if (dom.getUnit() != null && dom.getUnit() != returnSite) {
                                    res.remove(source);
                                    res.add(source.deriveNewAbstractionWithDominator(dom.getUnit()));
                                }
                            }
                            return res;
                        }
                        return res;
                    }
                };
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void enterConditional(Abstraction abs, Unit stmt, Unit destStmt) {
                if (!BackwardsInfoflowProblem.this.manager.getConfig().getImplicitFlowMode().trackControlFlowDependencies() || abs.getDominator() != null) {
                    return;
                }
                IInfoflowCFG.UnitContainer dom = BackwardsInfoflowProblem.this.manager.getICFG().getDominatorOf(stmt);
                if (dom.getUnit() != null && dom.getUnit() != destStmt) {
                    abs.setDominator(dom.getUnit());
                }
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
            public boolean isPrimitiveOrStringType(Type t) {
                return (t instanceof PrimType) || TypeUtils.isStringType(t);
            }
        };
    }
}
