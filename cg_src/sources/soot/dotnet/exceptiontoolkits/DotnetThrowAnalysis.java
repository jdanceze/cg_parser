package soot.dotnet.exceptiontoolkits;

import soot.FastHierarchy;
import soot.G;
import soot.IntegerType;
import soot.Local;
import soot.LongType;
import soot.NullType;
import soot.RefLikeType;
import soot.RefType;
import soot.Scene;
import soot.Singletons;
import soot.SootMethod;
import soot.Type;
import soot.UnknownType;
import soot.Value;
import soot.baf.ArrayLengthInst;
import soot.baf.ArrayReadInst;
import soot.baf.ArrayWriteInst;
import soot.baf.DivInst;
import soot.baf.DynamicInvokeInst;
import soot.baf.EnterMonitorInst;
import soot.baf.ExitMonitorInst;
import soot.baf.FieldGetInst;
import soot.baf.FieldPutInst;
import soot.baf.IdentityInst;
import soot.baf.InstanceCastInst;
import soot.baf.InstanceOfInst;
import soot.baf.InterfaceInvokeInst;
import soot.baf.LoadInst;
import soot.baf.LookupSwitchInst;
import soot.baf.NewArrayInst;
import soot.baf.NewInst;
import soot.baf.NewMultiArrayInst;
import soot.baf.PrimitiveCastInst;
import soot.baf.RemInst;
import soot.baf.ReturnInst;
import soot.baf.ReturnVoidInst;
import soot.baf.SpecialInvokeInst;
import soot.baf.StaticGetInst;
import soot.baf.StaticInvokeInst;
import soot.baf.StaticPutInst;
import soot.baf.StoreInst;
import soot.baf.TableSwitchInst;
import soot.baf.ThrowInst;
import soot.baf.VirtualInvokeInst;
import soot.dotnet.types.DotnetBasicTypes;
import soot.grimp.NewInvokeExpr;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.ClassConstant;
import soot.jimple.DivExpr;
import soot.jimple.DynamicInvokeExpr;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InstanceOfExpr;
import soot.jimple.IntConstant;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.LengthExpr;
import soot.jimple.LongConstant;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.MethodHandle;
import soot.jimple.MethodType;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.NopStmt;
import soot.jimple.ParameterRef;
import soot.jimple.RemExpr;
import soot.jimple.RetStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThrowStmt;
import soot.jimple.VirtualInvokeExpr;
import soot.toolkits.exceptions.ThrowableSet;
import soot.toolkits.exceptions.UnitThrowAnalysis;
/* loaded from: gencallgraphv3.jar:soot/dotnet/exceptiontoolkits/DotnetThrowAnalysis.class */
public class DotnetThrowAnalysis extends UnitThrowAnalysis {
    public static DotnetThrowAnalysis interproceduralAnalysis = null;
    private static final IntConstant INT_CONSTANT_ZERO = IntConstant.v(0);
    private static final LongConstant LONG_CONSTANT_ZERO = LongConstant.v(0);

    public DotnetThrowAnalysis(Singletons.Global g) {
    }

    public static DotnetThrowAnalysis v() {
        return G.v().soot_dotnet_exceptiontoolkits_DotnetThrowAnalysis();
    }

    protected DotnetThrowAnalysis(boolean isInterproc) {
        super(isInterproc);
    }

    public DotnetThrowAnalysis(Singletons.Global g, boolean isInterproc) {
        super(isInterproc);
    }

    @Override // soot.toolkits.exceptions.UnitThrowAnalysis
    protected ThrowableSet defaultResult() {
        return this.mgr.EMPTY;
    }

    @Override // soot.toolkits.exceptions.UnitThrowAnalysis
    protected UnitThrowAnalysis.UnitSwitch unitSwitch(SootMethod sm) {
        return new UnitThrowAnalysis.UnitSwitch(this, sm) { // from class: soot.dotnet.exceptiontoolkits.DotnetThrowAnalysis.1
            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.baf.InstSwitch
            public void caseReturnVoidInst(ReturnVoidInst i) {
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.baf.InstSwitch
            public void caseReturnInst(ReturnInst i) {
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.baf.InstSwitch
            public void caseIdentityInst(IdentityInst i) {
                Value rightOp = i.getRightOp();
                if (rightOp instanceof CaughtExceptionRef) {
                    this.result = this.result.add(Scene.v().getRefType(i.getLeftOp().getType().toString()));
                }
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.baf.InstSwitch
            public void caseStoreInst(StoreInst i) {
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.baf.InstSwitch
            public void caseLoadInst(LoadInst i) {
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.baf.InstSwitch
            public void caseArrayWriteInst(ArrayWriteInst i) {
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_NULLREFERENCEEXCEPTION));
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_INDEXOUTOFRANGEEXCEPTION));
                if (i.getOpType() instanceof RefType) {
                    this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_ARRAYTYPEMISMATCHEXCEPTION));
                }
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.baf.InstSwitch
            public void caseArrayReadInst(ArrayReadInst i) {
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_NULLREFERENCEEXCEPTION));
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_INDEXOUTOFRANGEEXCEPTION));
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.baf.InstSwitch
            public void caseStaticGetInst(StaticGetInst i) {
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_FIELDACCESSEXCEPTION));
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_MISSINGFIELDEXCEPTION));
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.baf.InstSwitch
            public void caseStaticPutInst(StaticPutInst i) {
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_FIELDACCESSEXCEPTION));
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_MISSINGFIELDEXCEPTION));
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.baf.InstSwitch
            public void caseFieldGetInst(FieldGetInst i) {
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_NULLREFERENCEEXCEPTION));
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_FIELDACCESSEXCEPTION));
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_MISSINGFIELDEXCEPTION));
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.baf.InstSwitch
            public void caseFieldPutInst(FieldPutInst i) {
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_NULLREFERENCEEXCEPTION));
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_FIELDACCESSEXCEPTION));
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_MISSINGFIELDEXCEPTION));
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.baf.InstSwitch
            public void caseInstanceCastInst(InstanceCastInst i) {
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_INVALIDCASTEXCEPTION));
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_TYPELOADEXCEPTION));
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.baf.InstSwitch
            public void caseInstanceOfInst(InstanceOfInst i) {
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_TYPELOADEXCEPTION));
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.baf.InstSwitch
            public void casePrimitiveCastInst(PrimitiveCastInst i) {
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_TYPELOADEXCEPTION));
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_INVALIDCASTEXCEPTION));
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.baf.InstSwitch
            public void caseDynamicInvokeInst(DynamicInvokeInst i) {
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.baf.InstSwitch
            public void caseStaticInvokeInst(StaticInvokeInst i) {
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_METHODACCESSEXCEPTION));
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_MISSINGMETHODEXCEPTION));
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_NULLREFERENCEEXCEPTION));
                this.result = this.result.add(DotnetThrowAnalysis.this.mightThrow(i.getMethodRef()));
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.baf.InstSwitch
            public void caseVirtualInvokeInst(VirtualInvokeInst i) {
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_METHODACCESSEXCEPTION));
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_MISSINGMETHODEXCEPTION));
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_NULLREFERENCEEXCEPTION));
                this.result = this.result.add(DotnetThrowAnalysis.this.mightThrow(i.getMethodRef()));
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.baf.InstSwitch
            public void caseInterfaceInvokeInst(InterfaceInvokeInst i) {
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_METHODACCESSEXCEPTION));
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_MISSINGMETHODEXCEPTION));
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_NULLREFERENCEEXCEPTION));
                this.result = this.result.add(DotnetThrowAnalysis.this.mightThrow(i.getMethodRef()));
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.baf.InstSwitch
            public void caseSpecialInvokeInst(SpecialInvokeInst i) {
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_METHODACCESSEXCEPTION));
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_MISSINGMETHODEXCEPTION));
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_NULLREFERENCEEXCEPTION));
                this.result = this.result.add(DotnetThrowAnalysis.this.mightThrow(i.getMethodRef()));
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.baf.InstSwitch
            public void caseThrowInst(ThrowInst i) {
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_NULLREFERENCEEXCEPTION));
                this.result = this.result.add(DotnetThrowAnalysis.this.mightThrowExplicitly(i));
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.baf.InstSwitch
            public void caseArrayLengthInst(ArrayLengthInst i) {
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_NULLREFERENCEEXCEPTION));
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.baf.InstSwitch
            public void caseDivInst(DivInst i) {
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_ARITHMETICEXCEPTION));
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_DIVIDEBYZEROEXCEPTION));
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.baf.InstSwitch
            public void caseRemInst(RemInst i) {
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_ARITHMETICEXCEPTION));
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_DIVIDEBYZEROEXCEPTION));
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.baf.InstSwitch
            public void caseNewInst(NewInst i) {
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_INVALIDOPERATIONEXCEPTION));
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_METHODACCESSEXCEPTION));
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_OUTOFMEMORYEXCEPTION));
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_MISSINGMETHODEXCEPTION));
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.baf.InstSwitch
            public void caseNewArrayInst(NewArrayInst i) {
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_OUTOFMEMORYEXCEPTION));
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_OVERFLOWEXCEPTION));
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.baf.InstSwitch
            public void caseNewMultiArrayInst(NewMultiArrayInst i) {
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_OUTOFMEMORYEXCEPTION));
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_OVERFLOWEXCEPTION));
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.baf.InstSwitch
            public void caseLookupSwitchInst(LookupSwitchInst i) {
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.baf.InstSwitch
            public void caseTableSwitchInst(TableSwitchInst i) {
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.baf.InstSwitch
            public void caseEnterMonitorInst(EnterMonitorInst i) {
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.baf.InstSwitch
            public void caseExitMonitorInst(ExitMonitorInst i) {
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.jimple.StmtSwitch
            public void caseAssignStmt(AssignStmt s) {
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_NULLREFERENCEEXCEPTION));
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_TYPELOADEXCEPTION));
                this.result = this.result.add(DotnetThrowAnalysis.this.mightThrow(s.getLeftOp()));
                this.result = this.result.add(DotnetThrowAnalysis.this.mightThrow(s.getRightOp()));
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.jimple.StmtSwitch
            public void caseEnterMonitorStmt(EnterMonitorStmt s) {
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.jimple.StmtSwitch
            public void caseExitMonitorStmt(ExitMonitorStmt s) {
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.jimple.StmtSwitch
            public void caseGotoStmt(GotoStmt s) {
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.jimple.StmtSwitch
            public void caseIdentityStmt(IdentityStmt s) {
                Value rightOp = s.getRightOp();
                if (rightOp instanceof CaughtExceptionRef) {
                    this.result = this.result.add(Scene.v().getRefType(s.getLeftOp().getType().toString()));
                }
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.jimple.StmtSwitch
            public void caseIfStmt(IfStmt s) {
                this.result = this.result.add(DotnetThrowAnalysis.this.mightThrow(s.getCondition()));
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.jimple.StmtSwitch
            public void caseInvokeStmt(InvokeStmt s) {
                this.result = this.result.add(DotnetThrowAnalysis.this.mightThrow(s.getInvokeExpr()));
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.jimple.StmtSwitch
            public void caseLookupSwitchStmt(LookupSwitchStmt s) {
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.jimple.StmtSwitch
            public void caseNopStmt(NopStmt s) {
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.jimple.StmtSwitch
            public void caseRetStmt(RetStmt s) {
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.jimple.StmtSwitch
            public void caseReturnStmt(ReturnStmt s) {
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.jimple.StmtSwitch
            public void caseReturnVoidStmt(ReturnVoidStmt s) {
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.jimple.StmtSwitch
            public void caseTableSwitchStmt(TableSwitchStmt s) {
                this.result = this.result.add(DotnetThrowAnalysis.this.mightThrow(s.getKey()));
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.jimple.StmtSwitch
            public void caseThrowStmt(ThrowStmt s) {
                this.result = this.result.add(Scene.v().getRefType(s.getOp().getType().toString()));
                this.result = this.result.add(DotnetThrowAnalysis.this.mightThrowExplicitly(s, this.sm));
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.jimple.StmtSwitch
            public void defaultCase(Object obj) {
            }
        };
    }

    @Override // soot.toolkits.exceptions.UnitThrowAnalysis
    protected UnitThrowAnalysis.ValueSwitch valueSwitch() {
        return new UnitThrowAnalysis.ValueSwitch(this) { // from class: soot.dotnet.exceptiontoolkits.DotnetThrowAnalysis.2
            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.ValueSwitch, soot.jimple.ConstantSwitch
            public void caseClassConstant(ClassConstant c) {
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_TYPELOADEXCEPTION));
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.ValueSwitch, soot.jimple.ConstantSwitch
            public void caseMethodHandle(MethodHandle handle) {
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.ValueSwitch, soot.jimple.ConstantSwitch
            public void caseMethodType(MethodType type) {
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.ValueSwitch, soot.jimple.ExprSwitch
            public void caseDivExpr(DivExpr expr) {
                caseBinopDivExpr(expr);
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.ValueSwitch, soot.jimple.ExprSwitch
            public void caseRemExpr(RemExpr expr) {
                caseBinopDivExpr(expr);
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.ValueSwitch, soot.jimple.ExprSwitch
            public void caseInterfaceInvokeExpr(InterfaceInvokeExpr expr) {
                caseInstanceInvokeExpr(expr, false);
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.ValueSwitch, soot.jimple.ExprSwitch
            public void caseSpecialInvokeExpr(SpecialInvokeExpr expr) {
                caseInstanceInvokeExpr(expr, false);
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.ValueSwitch, soot.jimple.ExprSwitch
            public void caseStaticInvokeExpr(StaticInvokeExpr expr) {
                caseInstanceInvokeExpr(expr, true);
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.ValueSwitch, soot.jimple.ExprSwitch
            public void caseVirtualInvokeExpr(VirtualInvokeExpr expr) {
                caseInstanceInvokeExpr(expr, false);
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.ValueSwitch, soot.jimple.ExprSwitch
            public void caseDynamicInvokeExpr(DynamicInvokeExpr expr) {
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.ValueSwitch, soot.jimple.ExprSwitch
            public void caseCastExpr(CastExpr expr) {
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_OUTOFMEMORYEXCEPTION));
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_NULLREFERENCEEXCEPTION));
                Type fromType = expr.getOp().getType();
                Type toType = expr.getCastType();
                if (!(fromType instanceof RefLikeType)) {
                    this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_INVALIDCASTEXCEPTION));
                }
                if (toType instanceof RefLikeType) {
                    FastHierarchy h = Scene.v().getOrMakeFastHierarchy();
                    if (fromType == null || (fromType instanceof UnknownType) || (!(fromType instanceof NullType) && !h.canStoreType(fromType, toType))) {
                        this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_TYPELOADEXCEPTION));
                    }
                }
                this.result = this.result.add(DotnetThrowAnalysis.this.mightThrow(expr.getOp()));
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.ValueSwitch, soot.jimple.ExprSwitch
            public void caseInstanceOfExpr(InstanceOfExpr expr) {
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_TYPELOADEXCEPTION));
                this.result = this.result.add(DotnetThrowAnalysis.this.mightThrow(expr.getOp()));
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.ValueSwitch, soot.jimple.ExprSwitch
            public void caseNewArrayExpr(NewArrayExpr expr) {
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_OUTOFMEMORYEXCEPTION));
                Value count = expr.getSize();
                if (!(count instanceof IntConstant) || ((IntConstant) count).lessThan(DotnetThrowAnalysis.INT_CONSTANT_ZERO).equals(DotnetThrowAnalysis.INT_CONSTANT_ZERO)) {
                    this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_OVERFLOWEXCEPTION));
                }
                this.result = this.result.add(DotnetThrowAnalysis.this.mightThrow(count));
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.ValueSwitch, soot.jimple.ExprSwitch
            public void caseNewMultiArrayExpr(NewMultiArrayExpr expr) {
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_OUTOFMEMORYEXCEPTION));
                for (int i = 0; i < expr.getSizeCount(); i++) {
                    Value count = expr.getSize(i);
                    if (!(count instanceof IntConstant) || ((IntConstant) count).lessThan(DotnetThrowAnalysis.INT_CONSTANT_ZERO).equals(DotnetThrowAnalysis.INT_CONSTANT_ZERO)) {
                        this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_OVERFLOWEXCEPTION));
                    }
                    this.result = this.result.add(DotnetThrowAnalysis.this.mightThrow(count));
                }
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.ValueSwitch, soot.jimple.ExprSwitch
            public void caseNewExpr(NewExpr expr) {
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.ValueSwitch, soot.jimple.ExprSwitch
            public void caseLengthExpr(LengthExpr expr) {
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_NULLREFERENCEEXCEPTION));
                this.result = this.result.add(DotnetThrowAnalysis.this.mightThrow(expr.getOp()));
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.ValueSwitch, soot.jimple.RefSwitch
            public void caseArrayRef(ArrayRef ref) {
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_NULLREFERENCEEXCEPTION));
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_INDEXOUTOFRANGEEXCEPTION));
                this.result = this.result.add(DotnetThrowAnalysis.this.mightThrow(ref.getBase()));
                this.result = this.result.add(DotnetThrowAnalysis.this.mightThrow(ref.getIndex()));
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.ValueSwitch, soot.jimple.RefSwitch
            public void caseStaticFieldRef(StaticFieldRef ref) {
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_FIELDACCESSEXCEPTION));
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_MISSINGFIELDEXCEPTION));
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.ValueSwitch, soot.jimple.RefSwitch
            public void caseInstanceFieldRef(InstanceFieldRef ref) {
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_FIELDACCESSEXCEPTION));
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_INVALIDOPERATIONEXCEPTION));
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_MISSINGFIELDEXCEPTION));
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_NULLREFERENCEEXCEPTION));
                this.result = this.result.add(DotnetThrowAnalysis.this.mightThrow(ref.getBase()));
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.ValueSwitch, soot.jimple.RefSwitch
            public void caseParameterRef(ParameterRef v) {
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.ValueSwitch, soot.jimple.RefSwitch
            public void caseCaughtExceptionRef(CaughtExceptionRef v) {
                this.result = this.result.add(Scene.v().getRefType(v.getType().toString()));
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.ValueSwitch, soot.jimple.ImmediateSwitch
            public void caseLocal(Local l) {
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_VERIFICATIONEXCEPTION));
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.ValueSwitch, soot.grimp.GrimpValueSwitch
            public void caseNewInvokeExpr(NewInvokeExpr e) {
                caseStaticInvokeExpr(e);
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.ValueSwitch, soot.jimple.ExprSwitch, soot.jimple.ConstantSwitch
            public void defaultCase(Object obj) {
            }

            private void caseBinopDivExpr(BinopExpr expr) {
                Value divisor = expr.getOp2();
                Type divisorType = divisor.getType();
                if ((divisorType instanceof UnknownType) || (((divisorType instanceof IntegerType) && (!(divisor instanceof IntConstant) || divisor.equals(DotnetThrowAnalysis.INT_CONSTANT_ZERO))) || ((divisorType instanceof LongType) && (!(divisor instanceof LongConstant) || divisor.equals(DotnetThrowAnalysis.LONG_CONSTANT_ZERO))))) {
                    this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_DIVIDEBYZEROEXCEPTION));
                }
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_ARITHMETICEXCEPTION));
                this.result = this.result.add(DotnetThrowAnalysis.this.mightThrow(expr.getOp1()));
                this.result = this.result.add(DotnetThrowAnalysis.this.mightThrow(expr.getOp2()));
            }

            private void caseInstanceInvokeExpr(InvokeExpr expr, boolean staticInvoke) {
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_METHODACCESSEXCEPTION));
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_MISSINGMETHODEXCEPTION));
                this.result = this.result.add(Scene.v().getRefType(DotnetBasicTypes.SYSTEM_NULLREFERENCEEXCEPTION));
                for (int i = 0; i < expr.getArgCount(); i++) {
                    this.result = this.result.add(DotnetThrowAnalysis.this.mightThrow(expr.getArg(i)));
                }
                if (!staticInvoke) {
                    this.result = this.result.add(DotnetThrowAnalysis.this.mightThrow(((InstanceInvokeExpr) expr).getBase()));
                }
                this.result = this.result.add(DotnetThrowAnalysis.this.mightThrow(expr.getMethodRef()));
            }
        };
    }
}
