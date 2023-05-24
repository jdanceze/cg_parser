package soot.dexpler;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.ArrayType;
import soot.Body;
import soot.Local;
import soot.SootMethodRef;
import soot.Type;
import soot.Unit;
import soot.UnknownType;
import soot.Value;
import soot.ValueBox;
import soot.dexpler.tags.ObjectOpTag;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.ClassConstant;
import soot.jimple.ConditionExpr;
import soot.jimple.DefinitionStmt;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.FieldRef;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.LengthExpr;
import soot.jimple.LongConstant;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NullConstant;
import soot.jimple.ReturnStmt;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.ThrowStmt;
import soot.jimple.internal.AbstractInstanceInvokeExpr;
import soot.jimple.internal.AbstractInvokeExpr;
/* loaded from: gencallgraphv3.jar:soot/dexpler/DexNullTransformer.class */
public class DexNullTransformer extends AbstractNullTransformer {
    private boolean usedAsObject;
    private boolean doBreak = false;
    private Local l = null;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DexNullTransformer.class.desiredAssertionStatus();
    }

    public static DexNullTransformer v() {
        return new DexNullTransformer();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(final Body body, String phaseName, Map<String, String> options) {
        DexDefUseAnalysis localDefs = new DexDefUseAnalysis(body);
        AbstractStmtSwitch checkDef = new AbstractStmtSwitch() { // from class: soot.dexpler.DexNullTransformer.1
            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
            public void caseAssignStmt(AssignStmt stmt) {
                Value r = stmt.getRightOp();
                if (r instanceof FieldRef) {
                    DexNullTransformer.this.usedAsObject = DexNullTransformer.isObject(((FieldRef) r).getFieldRef().type());
                    DexNullTransformer.this.doBreak = true;
                } else if (r instanceof ArrayRef) {
                    ArrayRef ar = (ArrayRef) r;
                    if (ar.getType() instanceof UnknownType) {
                        DexNullTransformer.this.usedAsObject = stmt.hasTag(ObjectOpTag.NAME);
                    } else {
                        DexNullTransformer.this.usedAsObject = DexNullTransformer.isObject(ar.getType());
                    }
                    DexNullTransformer.this.doBreak = true;
                } else if ((r instanceof StringConstant) || (r instanceof NewExpr) || (r instanceof NewArrayExpr) || (r instanceof ClassConstant)) {
                    DexNullTransformer.this.usedAsObject = true;
                    DexNullTransformer.this.doBreak = true;
                } else if (r instanceof CastExpr) {
                    DexNullTransformer.this.usedAsObject = DexNullTransformer.isObject(((CastExpr) r).getCastType());
                    DexNullTransformer.this.doBreak = true;
                } else if (r instanceof InvokeExpr) {
                    DexNullTransformer.this.usedAsObject = DexNullTransformer.isObject(((InvokeExpr) r).getType());
                    DexNullTransformer.this.doBreak = true;
                } else if (r instanceof LengthExpr) {
                    DexNullTransformer.this.usedAsObject = false;
                    DexNullTransformer.this.doBreak = true;
                }
            }

            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
            public void caseIdentityStmt(IdentityStmt stmt) {
                if (stmt.getLeftOp() == DexNullTransformer.this.l) {
                    DexNullTransformer.this.usedAsObject = DexNullTransformer.isObject(stmt.getRightOp().getType());
                    DexNullTransformer.this.doBreak = true;
                }
            }
        };
        AbstractStmtSwitch checkUse = new AbstractStmtSwitch() { // from class: soot.dexpler.DexNullTransformer.2
            private boolean examineInvokeExpr(InvokeExpr e) {
                List<Value> args = e.getArgs();
                List<Type> argTypes = e.getMethodRef().parameterTypes();
                if (DexNullTransformer.$assertionsDisabled || args.size() == argTypes.size()) {
                    for (int i = 0; i < args.size(); i++) {
                        if (args.get(i) == DexNullTransformer.this.l && DexNullTransformer.isObject(argTypes.get(i))) {
                            return true;
                        }
                    }
                    SootMethodRef sm = e.getMethodRef();
                    if (!sm.isStatic() && (e instanceof AbstractInvokeExpr)) {
                        AbstractInstanceInvokeExpr aiiexpr = (AbstractInstanceInvokeExpr) e;
                        Value b = aiiexpr.getBase();
                        if (b == DexNullTransformer.this.l) {
                            return true;
                        }
                        return false;
                    }
                    return false;
                }
                throw new AssertionError();
            }

            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
            public void caseInvokeStmt(InvokeStmt stmt) {
                InvokeExpr e = stmt.getInvokeExpr();
                DexNullTransformer.this.usedAsObject = examineInvokeExpr(e);
                DexNullTransformer.this.doBreak = true;
            }

            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
            public void caseAssignStmt(AssignStmt stmt) {
                Value left = stmt.getLeftOp();
                Value r = stmt.getRightOp();
                if (left instanceof ArrayRef) {
                    ArrayRef ar = (ArrayRef) left;
                    if (ar.getIndex() == DexNullTransformer.this.l) {
                        DexNullTransformer.this.doBreak = true;
                        return;
                    } else if (ar.getBase() == DexNullTransformer.this.l) {
                        DexNullTransformer.this.usedAsObject = true;
                        DexNullTransformer.this.doBreak = true;
                        return;
                    }
                }
                if (left instanceof InstanceFieldRef) {
                    InstanceFieldRef ifr = (InstanceFieldRef) left;
                    if (ifr.getBase() == DexNullTransformer.this.l) {
                        DexNullTransformer.this.usedAsObject = true;
                        DexNullTransformer.this.doBreak = true;
                        return;
                    }
                }
                if (stmt.getRightOp() == DexNullTransformer.this.l) {
                    Value l = stmt.getLeftOp();
                    if ((l instanceof StaticFieldRef) && DexNullTransformer.isObject(((StaticFieldRef) l).getFieldRef().type())) {
                        DexNullTransformer.this.usedAsObject = true;
                        DexNullTransformer.this.doBreak = true;
                        return;
                    } else if ((l instanceof InstanceFieldRef) && DexNullTransformer.isObject(((InstanceFieldRef) l).getFieldRef().type())) {
                        DexNullTransformer.this.usedAsObject = true;
                        DexNullTransformer.this.doBreak = true;
                        return;
                    } else if (l instanceof ArrayRef) {
                        Type aType = ((ArrayRef) l).getType();
                        if (aType instanceof UnknownType) {
                            DexNullTransformer.this.usedAsObject = stmt.hasTag(ObjectOpTag.NAME);
                        } else {
                            DexNullTransformer.this.usedAsObject = DexNullTransformer.isObject(aType);
                        }
                        DexNullTransformer.this.doBreak = true;
                        return;
                    }
                }
                if (r instanceof FieldRef) {
                    DexNullTransformer.this.usedAsObject = true;
                    DexNullTransformer.this.doBreak = true;
                } else if (r instanceof ArrayRef) {
                    if (((ArrayRef) r).getBase() == DexNullTransformer.this.l) {
                        DexNullTransformer.this.usedAsObject = true;
                    } else {
                        DexNullTransformer.this.usedAsObject = false;
                    }
                    DexNullTransformer.this.doBreak = true;
                } else if ((r instanceof StringConstant) || (r instanceof NewExpr)) {
                    throw new RuntimeException("NOT POSSIBLE StringConstant or NewExpr at " + stmt);
                } else {
                    if (r instanceof NewArrayExpr) {
                        DexNullTransformer.this.usedAsObject = false;
                        DexNullTransformer.this.doBreak = true;
                    } else if (r instanceof CastExpr) {
                        DexNullTransformer.this.usedAsObject = DexNullTransformer.isObject(((CastExpr) r).getCastType());
                        DexNullTransformer.this.doBreak = true;
                    } else if (r instanceof InvokeExpr) {
                        DexNullTransformer.this.usedAsObject = examineInvokeExpr((InvokeExpr) stmt.getRightOp());
                        DexNullTransformer.this.doBreak = true;
                    } else if (r instanceof LengthExpr) {
                        DexNullTransformer.this.usedAsObject = true;
                        DexNullTransformer.this.doBreak = true;
                    } else if (r instanceof BinopExpr) {
                        DexNullTransformer.this.usedAsObject = false;
                        DexNullTransformer.this.doBreak = true;
                    }
                }
            }

            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
            public void caseIdentityStmt(IdentityStmt stmt) {
                if (stmt.getLeftOp() == DexNullTransformer.this.l) {
                    throw new RuntimeException("IMPOSSIBLE 0");
                }
            }

            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
            public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
                DexNullTransformer.this.usedAsObject = stmt.getOp() == DexNullTransformer.this.l;
                DexNullTransformer.this.doBreak = true;
            }

            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
            public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
                DexNullTransformer.this.usedAsObject = stmt.getOp() == DexNullTransformer.this.l;
                DexNullTransformer.this.doBreak = true;
            }

            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
            public void caseReturnStmt(ReturnStmt stmt) {
                DexNullTransformer.this.usedAsObject = stmt.getOp() == DexNullTransformer.this.l && DexNullTransformer.isObject(body.getMethod().getReturnType());
                DexNullTransformer.this.doBreak = true;
            }

            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
            public void caseThrowStmt(ThrowStmt stmt) {
                DexNullTransformer.this.usedAsObject = stmt.getOp() == DexNullTransformer.this.l;
                DexNullTransformer.this.doBreak = true;
            }
        };
        for (Local loc : getNullCandidates(body)) {
            this.usedAsObject = false;
            Set<Unit> defs = localDefs.collectDefinitionsWithAliases(loc);
            this.doBreak = false;
            for (Unit u : defs) {
                if (u instanceof DefinitionStmt) {
                    this.l = (Local) ((DefinitionStmt) u).getLeftOp();
                } else if (u instanceof IfStmt) {
                    throw new RuntimeException("ERROR: def can not be something else than Assign or Identity statement! (def: " + u + " class: " + u.getClass());
                }
                u.apply(checkDef);
                if (this.doBreak) {
                    break;
                }
                for (Unit use : localDefs.getUsesOf(this.l)) {
                    use.apply(checkUse);
                    if (this.doBreak) {
                        break;
                    }
                }
                if (this.doBreak) {
                    break;
                }
            }
            if (this.usedAsObject) {
                for (Unit u2 : defs) {
                    replaceWithNull(u2);
                    Set<Value> defLocals = new HashSet<>();
                    for (ValueBox vb : u2.getDefBoxes()) {
                        defLocals.add(vb.getValue());
                    }
                    Local l = (Local) ((DefinitionStmt) u2).getLeftOp();
                    for (Unit uuse : localDefs.getUsesOf(l)) {
                        Stmt use2 = (Stmt) uuse;
                        if (!use2.containsArrayRef() || !defLocals.contains(use2.getArrayRef().getBase())) {
                            replaceWithNull(use2);
                        }
                    }
                }
            }
        }
        AbstractStmtSwitch inlinedZeroValues = new AbstractStmtSwitch() { // from class: soot.dexpler.DexNullTransformer.3
            final NullConstant nullConstant = NullConstant.v();
            Set<Value> objects = null;

            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
            public void caseAssignStmt(AssignStmt stmt) {
                if (DexNullTransformer.isObject(stmt.getLeftOp().getType()) && isConstZero(stmt.getRightOp())) {
                    stmt.setRightOp(this.nullConstant);
                    return;
                }
                if (stmt.getRightOp() instanceof CastExpr) {
                    CastExpr ce = (CastExpr) stmt.getRightOp();
                    if (DexNullTransformer.isObject(ce.getCastType()) && isConstZero(ce.getOp())) {
                        stmt.setRightOp(this.nullConstant);
                    }
                }
                if ((stmt.getLeftOp() instanceof ArrayRef) && isConstZero(stmt.getRightOp())) {
                    ArrayRef ar = (ArrayRef) stmt.getLeftOp();
                    if (this.objects == null) {
                        this.objects = DexNullTransformer.getObjectArray(body);
                    }
                    if (this.objects.contains(ar.getBase()) || stmt.hasTag(ObjectOpTag.NAME)) {
                        stmt.setRightOp(this.nullConstant);
                    }
                }
            }

            private boolean isConstZero(Value rightOp) {
                if (!(rightOp instanceof IntConstant) || ((IntConstant) rightOp).value != 0) {
                    if ((rightOp instanceof LongConstant) && ((LongConstant) rightOp).value == 0) {
                        return true;
                    }
                    return false;
                }
                return true;
            }

            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
            public void caseReturnStmt(ReturnStmt stmt) {
                if ((stmt.getOp() instanceof IntConstant) && DexNullTransformer.isObject(body.getMethod().getReturnType())) {
                    IntConstant iconst = (IntConstant) stmt.getOp();
                    if (!DexNullTransformer.$assertionsDisabled && iconst.value != 0) {
                        throw new AssertionError();
                    }
                    stmt.setOp(this.nullConstant);
                }
            }

            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
            public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
                if ((stmt.getOp() instanceof IntConstant) && ((IntConstant) stmt.getOp()).value == 0) {
                    stmt.setOp(this.nullConstant);
                }
            }

            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
            public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
                if ((stmt.getOp() instanceof IntConstant) && ((IntConstant) stmt.getOp()).value == 0) {
                    stmt.setOp(this.nullConstant);
                }
            }
        };
        NullConstant nullConstant = NullConstant.v();
        Iterator<Unit> it = body.getUnits().iterator();
        while (it.hasNext()) {
            Unit u3 = it.next();
            u3.apply(inlinedZeroValues);
            if (u3 instanceof Stmt) {
                Stmt stmt = (Stmt) u3;
                if (stmt.containsInvokeExpr()) {
                    InvokeExpr invExpr = stmt.getInvokeExpr();
                    for (int i = 0; i < invExpr.getArgCount(); i++) {
                        if (isObject(invExpr.getMethodRef().parameterType(i)) && (invExpr.getArg(i) instanceof IntConstant)) {
                            IntConstant iconst = (IntConstant) invExpr.getArg(i);
                            if (!$assertionsDisabled && iconst.value != 0) {
                                throw new AssertionError();
                            }
                            invExpr.setArg(i, nullConstant);
                        }
                    }
                    continue;
                } else {
                    continue;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Set<Value> getObjectArray(Body body) {
        Set<Value> objArrays = new HashSet<>();
        Iterator<Unit> it = body.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (u instanceof AssignStmt) {
                AssignStmt assign = (AssignStmt) u;
                if (assign.getRightOp() instanceof NewArrayExpr) {
                    NewArrayExpr nea = (NewArrayExpr) assign.getRightOp();
                    if (isObject(nea.getBaseType())) {
                        objArrays.add(assign.getLeftOp());
                    }
                } else if (assign.getRightOp() instanceof FieldRef) {
                    FieldRef fr = (FieldRef) assign.getRightOp();
                    if ((fr.getType() instanceof ArrayType) && isObject(((ArrayType) fr.getType()).getArrayElementType())) {
                        objArrays.add(assign.getLeftOp());
                    }
                }
            }
        }
        return objArrays;
    }

    private Set<Local> getNullCandidates(Body body) {
        Set<Local> candidates = null;
        Iterator<Unit> it = body.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (u instanceof AssignStmt) {
                AssignStmt a = (AssignStmt) u;
                if (a.getLeftOp() instanceof Local) {
                    Local l = (Local) a.getLeftOp();
                    Value r = a.getRightOp();
                    if (((r instanceof IntConstant) && ((IntConstant) r).value == 0) || ((r instanceof LongConstant) && ((LongConstant) r).value == 0)) {
                        if (candidates == null) {
                            candidates = new HashSet<>();
                        }
                        candidates.add(l);
                    }
                }
            } else if (u instanceof IfStmt) {
                ConditionExpr expr = (ConditionExpr) ((IfStmt) u).getCondition();
                if (isZeroComparison(expr) && (expr.getOp1() instanceof Local)) {
                    if (candidates == null) {
                        candidates = new HashSet<>();
                    }
                    candidates.add((Local) expr.getOp1());
                }
            }
        }
        return candidates == null ? Collections.emptySet() : candidates;
    }
}
