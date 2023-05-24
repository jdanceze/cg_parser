package soot.toolkits.exceptions;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import heros.solver.IDESolver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.FastHierarchy;
import soot.G;
import soot.IntegerType;
import soot.Local;
import soot.LongType;
import soot.NullType;
import soot.PatchingChain;
import soot.RefLikeType;
import soot.RefType;
import soot.Scene;
import soot.Singletons;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.UnknownType;
import soot.Value;
import soot.ValueBox;
import soot.baf.AddInst;
import soot.baf.AndInst;
import soot.baf.ArrayLengthInst;
import soot.baf.ArrayReadInst;
import soot.baf.ArrayWriteInst;
import soot.baf.CmpInst;
import soot.baf.CmpgInst;
import soot.baf.CmplInst;
import soot.baf.DivInst;
import soot.baf.Dup1Inst;
import soot.baf.Dup1_x1Inst;
import soot.baf.Dup1_x2Inst;
import soot.baf.Dup2Inst;
import soot.baf.Dup2_x1Inst;
import soot.baf.Dup2_x2Inst;
import soot.baf.DynamicInvokeInst;
import soot.baf.EnterMonitorInst;
import soot.baf.ExitMonitorInst;
import soot.baf.FieldGetInst;
import soot.baf.FieldPutInst;
import soot.baf.GotoInst;
import soot.baf.IdentityInst;
import soot.baf.IfCmpEqInst;
import soot.baf.IfCmpGeInst;
import soot.baf.IfCmpGtInst;
import soot.baf.IfCmpLeInst;
import soot.baf.IfCmpLtInst;
import soot.baf.IfCmpNeInst;
import soot.baf.IfEqInst;
import soot.baf.IfGeInst;
import soot.baf.IfGtInst;
import soot.baf.IfLeInst;
import soot.baf.IfLtInst;
import soot.baf.IfNeInst;
import soot.baf.IfNonNullInst;
import soot.baf.IfNullInst;
import soot.baf.IncInst;
import soot.baf.InstSwitch;
import soot.baf.InstanceCastInst;
import soot.baf.InstanceOfInst;
import soot.baf.InterfaceInvokeInst;
import soot.baf.JSRInst;
import soot.baf.LoadInst;
import soot.baf.LookupSwitchInst;
import soot.baf.MulInst;
import soot.baf.NegInst;
import soot.baf.NewArrayInst;
import soot.baf.NewInst;
import soot.baf.NewMultiArrayInst;
import soot.baf.NopInst;
import soot.baf.OrInst;
import soot.baf.PopInst;
import soot.baf.PrimitiveCastInst;
import soot.baf.PushInst;
import soot.baf.RemInst;
import soot.baf.ReturnInst;
import soot.baf.ReturnVoidInst;
import soot.baf.ShlInst;
import soot.baf.ShrInst;
import soot.baf.SpecialInvokeInst;
import soot.baf.StaticGetInst;
import soot.baf.StaticInvokeInst;
import soot.baf.StaticPutInst;
import soot.baf.StoreInst;
import soot.baf.SubInst;
import soot.baf.SwapInst;
import soot.baf.TableSwitchInst;
import soot.baf.ThrowInst;
import soot.baf.UshrInst;
import soot.baf.VirtualInvokeInst;
import soot.baf.XorInst;
import soot.grimp.GrimpValueSwitch;
import soot.grimp.NewInvokeExpr;
import soot.jimple.AddExpr;
import soot.jimple.AndExpr;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.BreakpointStmt;
import soot.jimple.CastExpr;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.ClassConstant;
import soot.jimple.CmpExpr;
import soot.jimple.CmpgExpr;
import soot.jimple.CmplExpr;
import soot.jimple.DivExpr;
import soot.jimple.DoubleConstant;
import soot.jimple.DynamicInvokeExpr;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.EqExpr;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.FloatConstant;
import soot.jimple.GeExpr;
import soot.jimple.GotoStmt;
import soot.jimple.GtExpr;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InstanceOfExpr;
import soot.jimple.IntConstant;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.LeExpr;
import soot.jimple.LengthExpr;
import soot.jimple.LongConstant;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.LtExpr;
import soot.jimple.MethodHandle;
import soot.jimple.MethodType;
import soot.jimple.MulExpr;
import soot.jimple.NeExpr;
import soot.jimple.NegExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.NopStmt;
import soot.jimple.NullConstant;
import soot.jimple.OrExpr;
import soot.jimple.ParameterRef;
import soot.jimple.RemExpr;
import soot.jimple.RetStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.ShlExpr;
import soot.jimple.ShrExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.StmtSwitch;
import soot.jimple.StringConstant;
import soot.jimple.SubExpr;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThisRef;
import soot.jimple.ThrowStmt;
import soot.jimple.UshrExpr;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.XorExpr;
import soot.options.Options;
import soot.shimple.PhiExpr;
import soot.shimple.ShimpleValueSwitch;
import soot.toolkits.exceptions.ThrowableSet;
/* loaded from: gencallgraphv3.jar:soot/toolkits/exceptions/UnitThrowAnalysis.class */
public class UnitThrowAnalysis extends AbstractThrowAnalysis {
    protected final ThrowableSet.Manager mgr;
    private final ThrowableSet implicitThrowExceptions;
    protected final boolean isInterproc;
    protected final LoadingCache<SootMethod, ThrowableSet> methodToThrowSet;
    public static UnitThrowAnalysis interproceduralAnalysis = null;
    private static final IntConstant INT_CONSTANT_ZERO = IntConstant.v(0);
    private static final LongConstant LONG_CONSTANT_ZERO = LongConstant.v(0);

    public UnitThrowAnalysis(Singletons.Global g) {
        this(false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public UnitThrowAnalysis() {
        this(false);
    }

    public static UnitThrowAnalysis v() {
        return G.v().soot_toolkits_exceptions_UnitThrowAnalysis();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public UnitThrowAnalysis(boolean isInterproc) {
        this.mgr = ThrowableSet.Manager.v();
        this.implicitThrowExceptions = ThrowableSet.Manager.v().VM_ERRORS.add(ThrowableSet.Manager.v().NULL_POINTER_EXCEPTION).add(ThrowableSet.Manager.v().ILLEGAL_MONITOR_STATE_EXCEPTION);
        this.methodToThrowSet = IDESolver.DEFAULT_CACHE_BUILDER.build(new CacheLoader<SootMethod, ThrowableSet>() { // from class: soot.toolkits.exceptions.UnitThrowAnalysis.1
            @Override // com.google.common.cache.CacheLoader
            public ThrowableSet load(SootMethod sm) throws Exception {
                return UnitThrowAnalysis.this.mightThrow(sm, new HashSet());
            }
        });
        this.isInterproc = isInterproc;
    }

    public static UnitThrowAnalysis interproc() {
        if (interproceduralAnalysis == null) {
            interproceduralAnalysis = new UnitThrowAnalysis(true);
        }
        return interproceduralAnalysis;
    }

    protected ThrowableSet defaultResult() {
        return this.mgr.VM_ERRORS;
    }

    protected UnitSwitch unitSwitch(SootMethod sm) {
        return new UnitSwitch(sm);
    }

    protected ValueSwitch valueSwitch() {
        return new ValueSwitch();
    }

    @Override // soot.toolkits.exceptions.AbstractThrowAnalysis, soot.toolkits.exceptions.ThrowAnalysis
    public ThrowableSet mightThrow(Unit u) {
        return mightThrow(u, (SootMethod) null);
    }

    public ThrowableSet mightThrow(Unit u, SootMethod sm) {
        UnitSwitch sw = unitSwitch(sm);
        u.apply(sw);
        return sw.getResult();
    }

    @Override // soot.toolkits.exceptions.AbstractThrowAnalysis, soot.toolkits.exceptions.ThrowAnalysis
    public ThrowableSet mightThrowImplicitly(ThrowInst t) {
        return this.implicitThrowExceptions;
    }

    @Override // soot.toolkits.exceptions.AbstractThrowAnalysis, soot.toolkits.exceptions.ThrowAnalysis
    public ThrowableSet mightThrowImplicitly(ThrowStmt t) {
        return this.implicitThrowExceptions;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ThrowableSet mightThrow(Value v) {
        ValueSwitch sw = valueSwitch();
        v.apply(sw);
        return sw.getResult();
    }

    public ThrowableSet mightThrow(SootMethodRef m) {
        SootMethod sm = m.tryResolve();
        if (sm != null) {
            return mightThrow(sm);
        }
        return this.mgr.ALL_THROWABLES;
    }

    public ThrowableSet mightThrow(SootMethod sm) {
        if (!this.isInterproc) {
            return ThrowableSet.Manager.v().ALL_THROWABLES;
        }
        return this.methodToThrowSet.getUnchecked(sm);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ThrowableSet mightThrow(SootMethod sm, Set<SootMethod> doneSet) {
        ThrowableSet curStmtSet;
        Collection<Trap> trapsForUnit;
        if (!doneSet.add(sm)) {
            return ThrowableSet.Manager.v().EMPTY;
        }
        if (!sm.hasActiveBody()) {
            if (Options.v().src_prec() == 7) {
                return ThrowableSet.Manager.v().ALL_THROWABLES;
            }
            return ThrowableSet.Manager.v().EMPTY;
        }
        PatchingChain<Unit> units = sm.getActiveBody().getUnits();
        Map<Unit, Collection<Trap>> unitToTraps = sm.getActiveBody().getTraps().isEmpty() ? null : new HashMap<>();
        for (Trap t : sm.getActiveBody().getTraps()) {
            Iterator<Unit> unitIt = units.iterator(t.getBeginUnit(), units.getPredOf((PatchingChain<Unit>) t.getEndUnit()));
            while (unitIt.hasNext()) {
                Unit unit = unitIt.next();
                Collection<Trap> unitsForTrap = unitToTraps.get(unit);
                if (unitsForTrap == null) {
                    unitsForTrap = new ArrayList<>();
                    unitToTraps.put(unit, unitsForTrap);
                }
                unitsForTrap.add(t);
            }
        }
        ThrowableSet methodSet = ThrowableSet.Manager.v().EMPTY;
        if (sm.hasActiveBody()) {
            Body methodBody = sm.getActiveBody();
            Iterator<Unit> it = methodBody.getUnits().iterator();
            while (it.hasNext()) {
                Unit u = it.next();
                if (u instanceof Stmt) {
                    Stmt stmt = (Stmt) u;
                    if (stmt.containsInvokeExpr()) {
                        InvokeExpr inv = stmt.getInvokeExpr();
                        curStmtSet = mightThrow(inv.getMethod(), doneSet);
                    } else {
                        curStmtSet = mightThrow(u, sm);
                    }
                    if (unitToTraps != null && (trapsForUnit = unitToTraps.get(stmt)) != null) {
                        for (Trap t2 : trapsForUnit) {
                            ThrowableSet.Pair p = curStmtSet.whichCatchableAs(t2.getException().getType());
                            curStmtSet = curStmtSet.remove(p.getCaught());
                        }
                    }
                    methodSet = methodSet.add(curStmtSet);
                }
            }
        }
        return methodSet;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:soot/toolkits/exceptions/UnitThrowAnalysis$UnitSwitch.class */
    public class UnitSwitch implements InstSwitch, StmtSwitch {
        protected ThrowableSet result;
        protected SootMethod sm;

        public UnitSwitch(SootMethod sm) {
            this.result = UnitThrowAnalysis.this.defaultResult();
            this.sm = sm;
        }

        ThrowableSet getResult() {
            return this.result;
        }

        @Override // soot.baf.InstSwitch
        public void caseReturnVoidInst(ReturnVoidInst i) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.ILLEGAL_MONITOR_STATE_EXCEPTION);
        }

        @Override // soot.baf.InstSwitch
        public void caseReturnInst(ReturnInst i) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.ILLEGAL_MONITOR_STATE_EXCEPTION);
        }

        @Override // soot.baf.InstSwitch
        public void caseNopInst(NopInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseGotoInst(GotoInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseJSRInst(JSRInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void casePushInst(PushInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void casePopInst(PopInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseIdentityInst(IdentityInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseStoreInst(StoreInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseLoadInst(LoadInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseArrayWriteInst(ArrayWriteInst i) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.ARRAY_INDEX_OUT_OF_BOUNDS_EXCEPTION);
            if (i.getOpType() instanceof RefType) {
                this.result = this.result.add(UnitThrowAnalysis.this.mgr.ARRAY_STORE_EXCEPTION);
            }
        }

        @Override // soot.baf.InstSwitch
        public void caseArrayReadInst(ArrayReadInst i) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.ARRAY_INDEX_OUT_OF_BOUNDS_EXCEPTION);
        }

        @Override // soot.baf.InstSwitch
        public void caseIfNullInst(IfNullInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseIfNonNullInst(IfNonNullInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseIfEqInst(IfEqInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseIfNeInst(IfNeInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseIfGtInst(IfGtInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseIfGeInst(IfGeInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseIfLtInst(IfLtInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseIfLeInst(IfLeInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseIfCmpEqInst(IfCmpEqInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseIfCmpNeInst(IfCmpNeInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseIfCmpGtInst(IfCmpGtInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseIfCmpGeInst(IfCmpGeInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseIfCmpLtInst(IfCmpLtInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseIfCmpLeInst(IfCmpLeInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseStaticGetInst(StaticGetInst i) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.INITIALIZATION_ERRORS);
        }

        @Override // soot.baf.InstSwitch
        public void caseStaticPutInst(StaticPutInst i) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.INITIALIZATION_ERRORS);
        }

        @Override // soot.baf.InstSwitch
        public void caseFieldGetInst(FieldGetInst i) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.RESOLVE_FIELD_ERRORS);
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);
        }

        @Override // soot.baf.InstSwitch
        public void caseFieldPutInst(FieldPutInst i) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.RESOLVE_FIELD_ERRORS);
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);
        }

        @Override // soot.baf.InstSwitch
        public void caseInstanceCastInst(InstanceCastInst i) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.RESOLVE_CLASS_ERRORS);
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.CLASS_CAST_EXCEPTION);
        }

        @Override // soot.baf.InstSwitch
        public void caseInstanceOfInst(InstanceOfInst i) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.RESOLVE_CLASS_ERRORS);
        }

        @Override // soot.baf.InstSwitch
        public void casePrimitiveCastInst(PrimitiveCastInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseDynamicInvokeInst(DynamicInvokeInst i) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.RESOLVE_METHOD_ERRORS);
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.INITIALIZATION_ERRORS);
            this.result = this.result.add(ThrowableSet.Manager.v().ALL_THROWABLES);
        }

        @Override // soot.baf.InstSwitch
        public void caseStaticInvokeInst(StaticInvokeInst i) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.INITIALIZATION_ERRORS);
            this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(i.getMethodRef()));
        }

        @Override // soot.baf.InstSwitch
        public void caseVirtualInvokeInst(VirtualInvokeInst i) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.RESOLVE_METHOD_ERRORS);
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);
            this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(i.getMethodRef()));
        }

        @Override // soot.baf.InstSwitch
        public void caseInterfaceInvokeInst(InterfaceInvokeInst i) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.RESOLVE_METHOD_ERRORS);
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);
            this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(i.getMethodRef()));
        }

        @Override // soot.baf.InstSwitch
        public void caseSpecialInvokeInst(SpecialInvokeInst i) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.RESOLVE_METHOD_ERRORS);
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);
            this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(i.getMethodRef()));
        }

        @Override // soot.baf.InstSwitch
        public void caseThrowInst(ThrowInst i) {
            this.result = UnitThrowAnalysis.this.mightThrowImplicitly(i);
            this.result = this.result.add(UnitThrowAnalysis.this.mightThrowExplicitly(i));
        }

        @Override // soot.baf.InstSwitch
        public void caseAddInst(AddInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseAndInst(AndInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseOrInst(OrInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseXorInst(XorInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseArrayLengthInst(ArrayLengthInst i) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);
        }

        @Override // soot.baf.InstSwitch
        public void caseCmpInst(CmpInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseCmpgInst(CmpgInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseCmplInst(CmplInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseDivInst(DivInst i) {
            if ((i.getOpType() instanceof IntegerType) || i.getOpType() == LongType.v()) {
                this.result = this.result.add(UnitThrowAnalysis.this.mgr.ARITHMETIC_EXCEPTION);
            }
        }

        @Override // soot.baf.InstSwitch
        public void caseIncInst(IncInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseMulInst(MulInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseRemInst(RemInst i) {
            if ((i.getOpType() instanceof IntegerType) || i.getOpType() == LongType.v()) {
                this.result = this.result.add(UnitThrowAnalysis.this.mgr.ARITHMETIC_EXCEPTION);
            }
        }

        @Override // soot.baf.InstSwitch
        public void caseSubInst(SubInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseShlInst(ShlInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseShrInst(ShrInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseUshrInst(UshrInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseNewInst(NewInst i) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.INITIALIZATION_ERRORS);
        }

        @Override // soot.baf.InstSwitch
        public void caseNegInst(NegInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseSwapInst(SwapInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseDup1Inst(Dup1Inst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseDup2Inst(Dup2Inst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseDup1_x1Inst(Dup1_x1Inst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseDup1_x2Inst(Dup1_x2Inst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseDup2_x1Inst(Dup2_x1Inst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseDup2_x2Inst(Dup2_x2Inst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseNewArrayInst(NewArrayInst i) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.RESOLVE_CLASS_ERRORS);
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.NEGATIVE_ARRAY_SIZE_EXCEPTION);
        }

        @Override // soot.baf.InstSwitch
        public void caseNewMultiArrayInst(NewMultiArrayInst i) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.RESOLVE_CLASS_ERRORS);
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.NEGATIVE_ARRAY_SIZE_EXCEPTION);
        }

        @Override // soot.baf.InstSwitch
        public void caseLookupSwitchInst(LookupSwitchInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseTableSwitchInst(TableSwitchInst i) {
        }

        @Override // soot.baf.InstSwitch
        public void caseEnterMonitorInst(EnterMonitorInst i) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);
        }

        @Override // soot.baf.InstSwitch
        public void caseExitMonitorInst(ExitMonitorInst i) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.ILLEGAL_MONITOR_STATE_EXCEPTION);
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);
        }

        public void caseAssignStmt(AssignStmt s) {
            Value lhs = s.getLeftOp();
            if ((lhs instanceof ArrayRef) && ((lhs.getType() instanceof UnknownType) || (lhs.getType() instanceof RefType))) {
                this.result = this.result.add(UnitThrowAnalysis.this.mgr.ARRAY_STORE_EXCEPTION);
            }
            this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(s.getLeftOp()));
            this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(s.getRightOp()));
        }

        @Override // soot.jimple.StmtSwitch
        public void caseBreakpointStmt(BreakpointStmt s) {
        }

        public void caseEnterMonitorStmt(EnterMonitorStmt s) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);
            this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(s.getOp()));
        }

        public void caseExitMonitorStmt(ExitMonitorStmt s) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.ILLEGAL_MONITOR_STATE_EXCEPTION);
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);
            this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(s.getOp()));
        }

        public void caseGotoStmt(GotoStmt s) {
        }

        public void caseIdentityStmt(IdentityStmt s) {
        }

        public void caseIfStmt(IfStmt s) {
            this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(s.getCondition()));
        }

        public void caseInvokeStmt(InvokeStmt s) {
            this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(s.getInvokeExpr()));
        }

        public void caseLookupSwitchStmt(LookupSwitchStmt s) {
            this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(s.getKey()));
        }

        public void caseNopStmt(NopStmt s) {
        }

        public void caseRetStmt(RetStmt s) {
        }

        public void caseReturnStmt(ReturnStmt s) {
        }

        public void caseReturnVoidStmt(ReturnVoidStmt s) {
        }

        public void caseTableSwitchStmt(TableSwitchStmt s) {
            this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(s.getKey()));
        }

        public void caseThrowStmt(ThrowStmt s) {
            this.result = UnitThrowAnalysis.this.mightThrowImplicitly(s);
            this.result = this.result.add(UnitThrowAnalysis.this.mightThrowExplicitly(s, this.sm));
        }

        public void defaultCase(Object obj) {
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:soot/toolkits/exceptions/UnitThrowAnalysis$ValueSwitch.class */
    public class ValueSwitch implements GrimpValueSwitch, ShimpleValueSwitch {
        protected ThrowableSet result;

        /* JADX INFO: Access modifiers changed from: protected */
        public ValueSwitch() {
            this.result = UnitThrowAnalysis.this.defaultResult();
        }

        ThrowableSet getResult() {
            return this.result;
        }

        @Override // soot.jimple.ConstantSwitch
        public void caseDoubleConstant(DoubleConstant c) {
        }

        @Override // soot.jimple.ConstantSwitch
        public void caseFloatConstant(FloatConstant c) {
        }

        @Override // soot.jimple.ConstantSwitch
        public void caseIntConstant(IntConstant c) {
        }

        @Override // soot.jimple.ConstantSwitch
        public void caseLongConstant(LongConstant c) {
        }

        @Override // soot.jimple.ConstantSwitch
        public void caseNullConstant(NullConstant c) {
        }

        public void caseStringConstant(StringConstant c) {
        }

        public void caseClassConstant(ClassConstant c) {
        }

        public void caseMethodHandle(MethodHandle handle) {
        }

        public void caseMethodType(MethodType type) {
        }

        @Override // soot.jimple.ExprSwitch
        public void caseAddExpr(AddExpr expr) {
            caseBinopExpr(expr);
        }

        @Override // soot.jimple.ExprSwitch
        public void caseAndExpr(AndExpr expr) {
            caseBinopExpr(expr);
        }

        @Override // soot.jimple.ExprSwitch
        public void caseCmpExpr(CmpExpr expr) {
            caseBinopExpr(expr);
        }

        @Override // soot.jimple.ExprSwitch
        public void caseCmpgExpr(CmpgExpr expr) {
            caseBinopExpr(expr);
        }

        @Override // soot.jimple.ExprSwitch
        public void caseCmplExpr(CmplExpr expr) {
            caseBinopExpr(expr);
        }

        @Override // soot.jimple.ExprSwitch
        public void caseDivExpr(DivExpr expr) {
            caseBinopDivExpr(expr);
        }

        @Override // soot.jimple.ExprSwitch
        public void caseEqExpr(EqExpr expr) {
            caseBinopExpr(expr);
        }

        @Override // soot.jimple.ExprSwitch
        public void caseNeExpr(NeExpr expr) {
            caseBinopExpr(expr);
        }

        @Override // soot.jimple.ExprSwitch
        public void caseGeExpr(GeExpr expr) {
            caseBinopExpr(expr);
        }

        @Override // soot.jimple.ExprSwitch
        public void caseGtExpr(GtExpr expr) {
            caseBinopExpr(expr);
        }

        @Override // soot.jimple.ExprSwitch
        public void caseLeExpr(LeExpr expr) {
            caseBinopExpr(expr);
        }

        @Override // soot.jimple.ExprSwitch
        public void caseLtExpr(LtExpr expr) {
            caseBinopExpr(expr);
        }

        @Override // soot.jimple.ExprSwitch
        public void caseMulExpr(MulExpr expr) {
            caseBinopExpr(expr);
        }

        @Override // soot.jimple.ExprSwitch
        public void caseOrExpr(OrExpr expr) {
            caseBinopExpr(expr);
        }

        @Override // soot.jimple.ExprSwitch
        public void caseRemExpr(RemExpr expr) {
            caseBinopDivExpr(expr);
        }

        @Override // soot.jimple.ExprSwitch
        public void caseShlExpr(ShlExpr expr) {
            caseBinopExpr(expr);
        }

        @Override // soot.jimple.ExprSwitch
        public void caseShrExpr(ShrExpr expr) {
            caseBinopExpr(expr);
        }

        @Override // soot.jimple.ExprSwitch
        public void caseUshrExpr(UshrExpr expr) {
            caseBinopExpr(expr);
        }

        @Override // soot.jimple.ExprSwitch
        public void caseSubExpr(SubExpr expr) {
            caseBinopExpr(expr);
        }

        @Override // soot.jimple.ExprSwitch
        public void caseXorExpr(XorExpr expr) {
            caseBinopExpr(expr);
        }

        @Override // soot.jimple.ExprSwitch
        public void caseInterfaceInvokeExpr(InterfaceInvokeExpr expr) {
            caseInstanceInvokeExpr(expr);
        }

        @Override // soot.jimple.ExprSwitch
        public void caseSpecialInvokeExpr(SpecialInvokeExpr expr) {
            caseInstanceInvokeExpr(expr);
        }

        @Override // soot.jimple.ExprSwitch
        public void caseStaticInvokeExpr(StaticInvokeExpr expr) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.INITIALIZATION_ERRORS);
            for (int i = 0; i < expr.getArgCount(); i++) {
                this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(expr.getArg(i)));
            }
            this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(expr.getMethodRef()));
        }

        @Override // soot.jimple.ExprSwitch
        public void caseVirtualInvokeExpr(VirtualInvokeExpr expr) {
            caseInstanceInvokeExpr(expr);
        }

        @Override // soot.jimple.ExprSwitch
        public void caseDynamicInvokeExpr(DynamicInvokeExpr expr) {
        }

        @Override // soot.jimple.ExprSwitch
        public void caseCastExpr(CastExpr expr) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.RESOLVE_CLASS_ERRORS);
            Type fromType = expr.getOp().getType();
            Type toType = expr.getCastType();
            if (toType instanceof RefLikeType) {
                FastHierarchy h = Scene.v().getOrMakeFastHierarchy();
                if (fromType == null || (fromType instanceof UnknownType) || (!(fromType instanceof NullType) && !h.canStoreType(fromType, toType))) {
                    this.result = this.result.add(UnitThrowAnalysis.this.mgr.CLASS_CAST_EXCEPTION);
                }
            }
            this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(expr.getOp()));
        }

        @Override // soot.jimple.ExprSwitch
        public void caseInstanceOfExpr(InstanceOfExpr expr) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.RESOLVE_CLASS_ERRORS);
            this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(expr.getOp()));
        }

        @Override // soot.jimple.ExprSwitch
        public void caseNewArrayExpr(NewArrayExpr expr) {
            if (expr.getBaseType() instanceof RefLikeType) {
                this.result = this.result.add(UnitThrowAnalysis.this.mgr.RESOLVE_CLASS_ERRORS);
            }
            Value count = expr.getSize();
            if (!(count instanceof IntConstant) || ((IntConstant) count).isLessThan(UnitThrowAnalysis.INT_CONSTANT_ZERO)) {
                this.result = this.result.add(UnitThrowAnalysis.this.mgr.NEGATIVE_ARRAY_SIZE_EXCEPTION);
            }
            this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(count));
        }

        @Override // soot.jimple.ExprSwitch
        public void caseNewMultiArrayExpr(NewMultiArrayExpr expr) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.RESOLVE_CLASS_ERRORS);
            for (int i = 0; i < expr.getSizeCount(); i++) {
                Value count = expr.getSize(i);
                if (!(count instanceof IntConstant) || ((IntConstant) count).isLessThan(UnitThrowAnalysis.INT_CONSTANT_ZERO)) {
                    this.result = this.result.add(UnitThrowAnalysis.this.mgr.NEGATIVE_ARRAY_SIZE_EXCEPTION);
                }
                this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(count));
            }
        }

        @Override // soot.jimple.ExprSwitch
        public void caseNewExpr(NewExpr expr) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.INITIALIZATION_ERRORS);
            for (ValueBox box : expr.getUseBoxes()) {
                this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(box.getValue()));
            }
        }

        @Override // soot.jimple.ExprSwitch
        public void caseLengthExpr(LengthExpr expr) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);
            this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(expr.getOp()));
        }

        @Override // soot.jimple.ExprSwitch
        public void caseNegExpr(NegExpr expr) {
            this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(expr.getOp()));
        }

        public void caseArrayRef(ArrayRef ref) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.ARRAY_INDEX_OUT_OF_BOUNDS_EXCEPTION);
            this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(ref.getBase()));
            this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(ref.getIndex()));
        }

        public void caseStaticFieldRef(StaticFieldRef ref) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.INITIALIZATION_ERRORS);
        }

        public void caseInstanceFieldRef(InstanceFieldRef ref) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.RESOLVE_FIELD_ERRORS);
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);
            this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(ref.getBase()));
        }

        public void caseParameterRef(ParameterRef v) {
        }

        public void caseCaughtExceptionRef(CaughtExceptionRef v) {
        }

        @Override // soot.jimple.RefSwitch
        public void caseThisRef(ThisRef v) {
        }

        public void caseLocal(Local l) {
        }

        @Override // soot.grimp.GrimpValueSwitch
        public void caseNewInvokeExpr(NewInvokeExpr e) {
            caseStaticInvokeExpr(e);
        }

        @Override // soot.shimple.ShimpleExprSwitch
        public void casePhiExpr(PhiExpr e) {
            for (ValueBox box : e.getUseBoxes()) {
                this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(box.getValue()));
            }
        }

        @Override // soot.jimple.ExprSwitch, soot.jimple.ConstantSwitch
        public void defaultCase(Object obj) {
        }

        private void caseBinopExpr(BinopExpr expr) {
            this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(expr.getOp1()));
            this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(expr.getOp2()));
        }

        private void caseBinopDivExpr(BinopExpr expr) {
            Value divisor = expr.getOp2();
            Type divisorType = divisor.getType();
            if (divisorType instanceof UnknownType) {
                this.result = this.result.add(UnitThrowAnalysis.this.mgr.ARITHMETIC_EXCEPTION);
            } else if ((divisorType instanceof IntegerType) && (!(divisor instanceof IntConstant) || ((IntConstant) divisor).equals(UnitThrowAnalysis.INT_CONSTANT_ZERO))) {
                this.result = this.result.add(UnitThrowAnalysis.this.mgr.ARITHMETIC_EXCEPTION);
            } else if (divisorType == LongType.v() && (!(divisor instanceof LongConstant) || ((LongConstant) divisor).equals(UnitThrowAnalysis.LONG_CONSTANT_ZERO))) {
                this.result = this.result.add(UnitThrowAnalysis.this.mgr.ARITHMETIC_EXCEPTION);
            }
            caseBinopExpr(expr);
        }

        private void caseInstanceInvokeExpr(InstanceInvokeExpr expr) {
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.RESOLVE_METHOD_ERRORS);
            this.result = this.result.add(UnitThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);
            for (int i = 0; i < expr.getArgCount(); i++) {
                this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(expr.getArg(i)));
            }
            this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(expr.getBase()));
            this.result = this.result.add(UnitThrowAnalysis.this.mightThrow(expr.getMethodRef()));
        }
    }
}
