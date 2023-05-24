package soot.jimple.toolkits.pointer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.Local;
import soot.RefType;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.ConditionExpr;
import soot.jimple.EqExpr;
import soot.jimple.IfStmt;
import soot.jimple.InstanceOfExpr;
import soot.jimple.IntConstant;
import soot.jimple.NeExpr;
import soot.jimple.NewExpr;
import soot.jimple.NullConstant;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ForwardBranchedFlowAnalysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/CastCheckEliminator.class */
public class CastCheckEliminator extends ForwardBranchedFlowAnalysis<LocalTypeSet> {
    protected LocalTypeSet emptySet;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.BranchedFlowAnalysis
    public /* bridge */ /* synthetic */ void flowThrough(Object obj, Unit unit, List list, List list2) {
        flowThrough((LocalTypeSet) obj, unit, (List<LocalTypeSet>) list, (List<LocalTypeSet>) list2);
    }

    static {
        $assertionsDisabled = !CastCheckEliminator.class.desiredAssertionStatus();
    }

    public CastCheckEliminator(BriefUnitGraph cfg) {
        super(cfg);
        makeInitialSet();
        doAnalysis();
        tagCasts();
    }

    protected void tagCasts() {
        Iterator<Unit> it = ((UnitGraph) this.graph).getBody().getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (u instanceof AssignStmt) {
                Value rhs = ((AssignStmt) u).getRightOp();
                if (rhs instanceof CastExpr) {
                    CastExpr cast = (CastExpr) rhs;
                    Type t = cast.getCastType();
                    if (t instanceof RefType) {
                        Value op = cast.getOp();
                        if (op instanceof Local) {
                            LocalTypeSet set = getFlowBefore(u);
                            u.addTag(new CastCheckTag(set.get(set.indexOf((Local) op, (RefType) t))));
                        } else if (!$assertionsDisabled && !(op instanceof NullConstant)) {
                            throw new AssertionError();
                        } else {
                            u.addTag(new CastCheckTag(true));
                        }
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
            }
        }
    }

    protected void makeInitialSet() {
        Body body = ((UnitGraph) this.graph).getBody();
        List<Local> refLocals = new ArrayList<>();
        for (Local l : body.getLocals()) {
            if (l.getType() instanceof RefType) {
                refLocals.add(l);
            }
        }
        List<Type> types = new ArrayList<>();
        Iterator<Unit> it = body.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (u instanceof AssignStmt) {
                Value rhs = ((AssignStmt) u).getRightOp();
                if (rhs instanceof CastExpr) {
                    Type t = ((CastExpr) rhs).getCastType();
                    if ((t instanceof RefType) && !types.contains(t)) {
                        types.add(t);
                    }
                }
            }
        }
        this.emptySet = new LocalTypeSet(refLocals, types);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public LocalTypeSet newInitialFlow() {
        LocalTypeSet ret = (LocalTypeSet) this.emptySet.clone();
        ret.setAllBits();
        return ret;
    }

    protected void flowThrough(LocalTypeSet in, Unit unit, List<LocalTypeSet> outFallVals, List<LocalTypeSet> outBranchVals) {
        LocalTypeSet out = (LocalTypeSet) in.clone();
        LocalTypeSet outBranch = out;
        for (ValueBox b : unit.getDefBoxes()) {
            Value v = b.getValue();
            if ((v instanceof Local) && (v.getType() instanceof RefType)) {
                out.killLocal((Local) v);
            }
        }
        if (unit instanceof AssignStmt) {
            AssignStmt astmt = (AssignStmt) unit;
            Value rhs = astmt.getRightOp();
            Value lhs = astmt.getLeftOp();
            if ((lhs instanceof Local) && (rhs.getType() instanceof RefType)) {
                Local l = (Local) lhs;
                if (rhs instanceof NewExpr) {
                    out.localMustBeSubtypeOf(l, (RefType) rhs.getType());
                } else if (rhs instanceof CastExpr) {
                    CastExpr cast = (CastExpr) rhs;
                    Type castType = cast.getCastType();
                    if ((castType instanceof RefType) && (cast.getOp() instanceof Local)) {
                        RefType refType = (RefType) castType;
                        Local opLocal = (Local) cast.getOp();
                        out.localCopy(l, opLocal);
                        out.localMustBeSubtypeOf(l, refType);
                        out.localMustBeSubtypeOf(opLocal, refType);
                    }
                } else if (rhs instanceof Local) {
                    out.localCopy(l, (Local) rhs);
                }
            }
        } else if (unit instanceof IfStmt) {
            IfStmt ifstmt = (IfStmt) unit;
            List predsOf = this.graph.getPredsOf(unit);
            if (predsOf.size() == 1) {
                Unit predecessor = (Unit) predsOf.get(0);
                if (predecessor instanceof AssignStmt) {
                    AssignStmt pred = (AssignStmt) predecessor;
                    Value predRHS = pred.getRightOp();
                    if (predRHS instanceof InstanceOfExpr) {
                        InstanceOfExpr iofexpr = (InstanceOfExpr) predRHS;
                        Type iofCheckType = iofexpr.getCheckType();
                        if (iofCheckType instanceof RefType) {
                            Value iofOp = iofexpr.getOp();
                            if (iofOp instanceof Local) {
                                ConditionExpr c = (ConditionExpr) ifstmt.getCondition();
                                if (c.getOp1().equals(pred.getLeftOp())) {
                                    Value conditionOp2 = c.getOp2();
                                    if ((conditionOp2 instanceof IntConstant) && ((IntConstant) conditionOp2).value == 0) {
                                        if (c instanceof NeExpr) {
                                            outBranch = (LocalTypeSet) out.clone();
                                            outBranch.localMustBeSubtypeOf((Local) iofOp, (RefType) iofCheckType);
                                        } else if (c instanceof EqExpr) {
                                            outBranch = (LocalTypeSet) out.clone();
                                            out.localMustBeSubtypeOf((Local) iofOp, (RefType) iofCheckType);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        for (LocalTypeSet ts : outFallVals) {
            copy(out, ts);
        }
        for (LocalTypeSet ts2 : outBranchVals) {
            copy(outBranch, ts2);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void copy(LocalTypeSet s, LocalTypeSet d) {
        d.and(s);
        d.or(s);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void merge(LocalTypeSet in1, LocalTypeSet in2, LocalTypeSet o) {
        o.setAllBits();
        o.and(in1);
        o.and(in2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public LocalTypeSet entryInitialFlow() {
        return (LocalTypeSet) this.emptySet.clone();
    }
}
