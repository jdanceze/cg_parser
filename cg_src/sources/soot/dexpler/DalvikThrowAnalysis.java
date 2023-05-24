package soot.dexpler;

import soot.FastHierarchy;
import soot.G;
import soot.NullType;
import soot.PrimType;
import soot.RefLikeType;
import soot.Scene;
import soot.Singletons;
import soot.SootMethod;
import soot.Type;
import soot.UnknownType;
import soot.baf.EnterMonitorInst;
import soot.baf.ReturnInst;
import soot.baf.ReturnVoidInst;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.ClassConstant;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.StringConstant;
import soot.toolkits.exceptions.ThrowableSet;
import soot.toolkits.exceptions.UnitThrowAnalysis;
/* loaded from: gencallgraphv3.jar:soot/dexpler/DalvikThrowAnalysis.class */
public class DalvikThrowAnalysis extends UnitThrowAnalysis {
    public static DalvikThrowAnalysis interproceduralAnalysis = null;

    public DalvikThrowAnalysis(Singletons.Global g) {
    }

    public static DalvikThrowAnalysis v() {
        return G.v().soot_dexpler_DalvikThrowAnalysis();
    }

    protected DalvikThrowAnalysis(boolean isInterproc) {
        super(isInterproc);
    }

    public DalvikThrowAnalysis(Singletons.Global g, boolean isInterproc) {
        super(isInterproc);
    }

    public static DalvikThrowAnalysis interproc() {
        return G.v().interproceduralDalvikThrowAnalysis();
    }

    @Override // soot.toolkits.exceptions.UnitThrowAnalysis
    protected ThrowableSet defaultResult() {
        return this.mgr.EMPTY;
    }

    @Override // soot.toolkits.exceptions.UnitThrowAnalysis
    protected UnitThrowAnalysis.UnitSwitch unitSwitch(SootMethod sm) {
        return new UnitThrowAnalysis.UnitSwitch(this, sm) { // from class: soot.dexpler.DalvikThrowAnalysis.1
            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.baf.InstSwitch
            public void caseReturnInst(ReturnInst i) {
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.baf.InstSwitch
            public void caseReturnVoidInst(ReturnVoidInst i) {
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.baf.InstSwitch
            public void caseEnterMonitorInst(EnterMonitorInst i) {
                this.result = this.result.add(DalvikThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);
                this.result = this.result.add(DalvikThrowAnalysis.this.mgr.ILLEGAL_MONITOR_STATE_EXCEPTION);
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.jimple.StmtSwitch
            public void caseEnterMonitorStmt(EnterMonitorStmt s) {
                this.result = this.result.add(DalvikThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);
                this.result = this.result.add(DalvikThrowAnalysis.this.mgr.ILLEGAL_MONITOR_STATE_EXCEPTION);
                this.result = this.result.add(DalvikThrowAnalysis.this.mightThrow(s.getOp()));
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.UnitSwitch, soot.jimple.StmtSwitch
            public void caseAssignStmt(AssignStmt s) {
                this.result = this.result.add(DalvikThrowAnalysis.this.mightThrow(s.getLeftOp()));
                this.result = this.result.add(DalvikThrowAnalysis.this.mightThrow(s.getRightOp()));
            }
        };
    }

    @Override // soot.toolkits.exceptions.UnitThrowAnalysis
    protected UnitThrowAnalysis.ValueSwitch valueSwitch() {
        return new UnitThrowAnalysis.ValueSwitch(this) { // from class: soot.dexpler.DalvikThrowAnalysis.2
            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.ValueSwitch, soot.jimple.ConstantSwitch
            public void caseStringConstant(StringConstant c) {
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.ValueSwitch, soot.jimple.ConstantSwitch
            public void caseClassConstant(ClassConstant c) {
            }

            @Override // soot.toolkits.exceptions.UnitThrowAnalysis.ValueSwitch, soot.jimple.ExprSwitch
            public void caseCastExpr(CastExpr expr) {
                if (expr.getCastType() instanceof PrimType) {
                    return;
                }
                Type fromType = expr.getOp().getType();
                Type toType = expr.getCastType();
                this.result = this.result.add(DalvikThrowAnalysis.this.mgr.RESOLVE_CLASS_ERRORS);
                if (toType instanceof RefLikeType) {
                    FastHierarchy h = Scene.v().getOrMakeFastHierarchy();
                    if (fromType == null || (fromType instanceof UnknownType) || (!(fromType instanceof NullType) && !h.canStoreType(fromType, toType))) {
                        this.result = this.result.add(DalvikThrowAnalysis.this.mgr.CLASS_CAST_EXCEPTION);
                    }
                }
                this.result = this.result.add(DalvikThrowAnalysis.this.mightThrow(expr.getOp()));
            }
        };
    }
}
