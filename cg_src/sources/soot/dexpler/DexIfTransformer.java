package soot.dexpler;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.Local;
import soot.SootMethodRef;
import soot.Type;
import soot.Unit;
import soot.UnknownType;
import soot.Value;
import soot.dexpler.tags.ObjectOpTag;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.ConditionExpr;
import soot.jimple.DefinitionStmt;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.EqExpr;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.FieldRef;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.LengthExpr;
import soot.jimple.NeExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.ReturnStmt;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.ThrowStmt;
import soot.jimple.internal.AbstractInstanceInvokeExpr;
import soot.jimple.internal.AbstractInvokeExpr;
/* loaded from: gencallgraphv3.jar:soot/dexpler/DexIfTransformer.class */
public class DexIfTransformer extends AbstractNullTransformer {
    private boolean usedAsObject;
    private boolean doBreak = false;
    Local l = null;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DexIfTransformer.class.desiredAssertionStatus();
    }

    public static DexIfTransformer v() {
        return new DexIfTransformer();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(final Body body, String phaseName, Map<String, String> options) {
        DexDefUseAnalysis localDefs = new DexDefUseAnalysis(body);
        Set<IfStmt> ifSet = getNullIfCandidates(body);
        for (IfStmt ifs : ifSet) {
            ConditionExpr ifCondition = (ConditionExpr) ifs.getCondition();
            Local[] twoIfLocals = {(Local) ifCondition.getOp1(), (Local) ifCondition.getOp2()};
            this.usedAsObject = false;
            for (Local loc : twoIfLocals) {
                Set<Unit> defs = localDefs.collectDefinitionsWithAliases(loc);
                this.doBreak = false;
                for (Unit u : defs) {
                    if (u instanceof DefinitionStmt) {
                        this.l = (Local) ((DefinitionStmt) u).getLeftOp();
                        u.apply(new AbstractStmtSwitch() { // from class: soot.dexpler.DexIfTransformer.1
                            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
                            public void caseAssignStmt(AssignStmt stmt) {
                                Value r = stmt.getRightOp();
                                if (r instanceof FieldRef) {
                                    DexIfTransformer.this.usedAsObject = DexIfTransformer.isObject(((FieldRef) r).getFieldRef().type());
                                    if (DexIfTransformer.this.usedAsObject) {
                                        DexIfTransformer.this.doBreak = true;
                                    }
                                } else if (r instanceof ArrayRef) {
                                    ArrayRef ar = (ArrayRef) r;
                                    if (ar.getType() instanceof UnknownType) {
                                        DexIfTransformer.this.usedAsObject = stmt.hasTag(ObjectOpTag.NAME);
                                    } else {
                                        DexIfTransformer.this.usedAsObject = DexIfTransformer.isObject(ar.getType());
                                    }
                                    if (DexIfTransformer.this.usedAsObject) {
                                        DexIfTransformer.this.doBreak = true;
                                    }
                                } else if ((r instanceof StringConstant) || (r instanceof NewExpr) || (r instanceof NewArrayExpr)) {
                                    DexIfTransformer.this.usedAsObject = true;
                                    if (DexIfTransformer.this.usedAsObject) {
                                        DexIfTransformer.this.doBreak = true;
                                    }
                                } else if (r instanceof CastExpr) {
                                    DexIfTransformer.this.usedAsObject = DexIfTransformer.isObject(((CastExpr) r).getCastType());
                                    if (DexIfTransformer.this.usedAsObject) {
                                        DexIfTransformer.this.doBreak = true;
                                    }
                                } else if (r instanceof InvokeExpr) {
                                    DexIfTransformer.this.usedAsObject = DexIfTransformer.isObject(((InvokeExpr) r).getType());
                                    if (DexIfTransformer.this.usedAsObject) {
                                        DexIfTransformer.this.doBreak = true;
                                    }
                                } else if (r instanceof LengthExpr) {
                                    DexIfTransformer.this.usedAsObject = false;
                                    if (DexIfTransformer.this.usedAsObject) {
                                        DexIfTransformer.this.doBreak = true;
                                    }
                                }
                            }

                            @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
                            public void caseIdentityStmt(IdentityStmt stmt) {
                                if (stmt.getLeftOp() == DexIfTransformer.this.l) {
                                    DexIfTransformer.this.usedAsObject = DexIfTransformer.isObject(stmt.getRightOp().getType());
                                    if (DexIfTransformer.this.usedAsObject) {
                                        DexIfTransformer.this.doBreak = true;
                                    }
                                }
                            }
                        });
                        if (this.doBreak) {
                            break;
                        }
                        for (Unit use : localDefs.getUsesOf(this.l)) {
                            use.apply(new AbstractStmtSwitch() { // from class: soot.dexpler.DexIfTransformer.2
                                private boolean examineInvokeExpr(InvokeExpr e) {
                                    List<Value> args = e.getArgs();
                                    List<Type> argTypes = e.getMethodRef().parameterTypes();
                                    if (DexIfTransformer.$assertionsDisabled || args.size() == argTypes.size()) {
                                        for (int i = 0; i < args.size(); i++) {
                                            if (args.get(i) == DexIfTransformer.this.l && DexIfTransformer.isObject(argTypes.get(i))) {
                                                return true;
                                            }
                                        }
                                        SootMethodRef sm = e.getMethodRef();
                                        if (!sm.isStatic() && (e instanceof AbstractInvokeExpr)) {
                                            AbstractInstanceInvokeExpr aiiexpr = (AbstractInstanceInvokeExpr) e;
                                            Value b = aiiexpr.getBase();
                                            if (b == DexIfTransformer.this.l) {
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
                                    DexIfTransformer.this.usedAsObject = examineInvokeExpr(e);
                                    if (DexIfTransformer.this.usedAsObject) {
                                        DexIfTransformer.this.doBreak = true;
                                    }
                                }

                                @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
                                public void caseAssignStmt(AssignStmt stmt) {
                                    Value left = stmt.getLeftOp();
                                    Value r = stmt.getRightOp();
                                    if ((left instanceof ArrayRef) && ((ArrayRef) left).getIndex() == DexIfTransformer.this.l) {
                                        return;
                                    }
                                    if (stmt.getRightOp() == DexIfTransformer.this.l) {
                                        Value l = stmt.getLeftOp();
                                        if ((l instanceof StaticFieldRef) && DexIfTransformer.isObject(((StaticFieldRef) l).getFieldRef().type())) {
                                            DexIfTransformer.this.usedAsObject = true;
                                            DexIfTransformer.this.doBreak = true;
                                            return;
                                        } else if ((l instanceof InstanceFieldRef) && DexIfTransformer.isObject(((InstanceFieldRef) l).getFieldRef().type())) {
                                            DexIfTransformer.this.usedAsObject = true;
                                            DexIfTransformer.this.doBreak = true;
                                            return;
                                        } else if (l instanceof ArrayRef) {
                                            Type aType = ((ArrayRef) l).getType();
                                            if (aType instanceof UnknownType) {
                                                DexIfTransformer.this.usedAsObject = stmt.hasTag(ObjectOpTag.NAME);
                                            } else {
                                                DexIfTransformer.this.usedAsObject = DexIfTransformer.isObject(aType);
                                            }
                                            if (DexIfTransformer.this.usedAsObject) {
                                                DexIfTransformer.this.doBreak = true;
                                                return;
                                            }
                                            return;
                                        }
                                    }
                                    if (r instanceof FieldRef) {
                                        DexIfTransformer.this.usedAsObject = true;
                                        DexIfTransformer.this.doBreak = true;
                                    } else if (r instanceof ArrayRef) {
                                        ArrayRef ar = (ArrayRef) r;
                                        if (ar.getBase() == DexIfTransformer.this.l) {
                                            DexIfTransformer.this.usedAsObject = true;
                                            DexIfTransformer.this.doBreak = true;
                                            return;
                                        }
                                        DexIfTransformer.this.usedAsObject = false;
                                    } else if ((r instanceof StringConstant) || (r instanceof NewExpr)) {
                                        throw new RuntimeException("NOT POSSIBLE StringConstant or NewExpr at " + stmt);
                                    } else {
                                        if (r instanceof NewArrayExpr) {
                                            DexIfTransformer.this.usedAsObject = false;
                                            if (DexIfTransformer.this.usedAsObject) {
                                                DexIfTransformer.this.doBreak = true;
                                            }
                                        } else if (r instanceof CastExpr) {
                                            DexIfTransformer.this.usedAsObject = DexIfTransformer.isObject(((CastExpr) r).getCastType());
                                            if (DexIfTransformer.this.usedAsObject) {
                                                DexIfTransformer.this.doBreak = true;
                                            }
                                        } else if (r instanceof InvokeExpr) {
                                            DexIfTransformer.this.usedAsObject = examineInvokeExpr((InvokeExpr) stmt.getRightOp());
                                            if (DexIfTransformer.this.usedAsObject) {
                                                DexIfTransformer.this.doBreak = true;
                                            }
                                        } else if (r instanceof LengthExpr) {
                                            DexIfTransformer.this.usedAsObject = true;
                                            if (DexIfTransformer.this.usedAsObject) {
                                                DexIfTransformer.this.doBreak = true;
                                            }
                                        } else if (r instanceof BinopExpr) {
                                            DexIfTransformer.this.usedAsObject = false;
                                            if (DexIfTransformer.this.usedAsObject) {
                                                DexIfTransformer.this.doBreak = true;
                                            }
                                        }
                                    }
                                }

                                @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
                                public void caseIdentityStmt(IdentityStmt stmt) {
                                    if (stmt.getLeftOp() == DexIfTransformer.this.l) {
                                        throw new RuntimeException("IMPOSSIBLE 0");
                                    }
                                }

                                @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
                                public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
                                    DexIfTransformer.this.usedAsObject = stmt.getOp() == DexIfTransformer.this.l;
                                    if (DexIfTransformer.this.usedAsObject) {
                                        DexIfTransformer.this.doBreak = true;
                                    }
                                }

                                @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
                                public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
                                    DexIfTransformer.this.usedAsObject = stmt.getOp() == DexIfTransformer.this.l;
                                    if (DexIfTransformer.this.usedAsObject) {
                                        DexIfTransformer.this.doBreak = true;
                                    }
                                }

                                @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
                                public void caseReturnStmt(ReturnStmt stmt) {
                                    DexIfTransformer.this.usedAsObject = stmt.getOp() == DexIfTransformer.this.l && DexIfTransformer.isObject(body.getMethod().getReturnType());
                                    if (DexIfTransformer.this.usedAsObject) {
                                        DexIfTransformer.this.doBreak = true;
                                    }
                                }

                                @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
                                public void caseThrowStmt(ThrowStmt stmt) {
                                    DexIfTransformer.this.usedAsObject = stmt.getOp() == DexIfTransformer.this.l;
                                    if (DexIfTransformer.this.usedAsObject) {
                                        DexIfTransformer.this.doBreak = true;
                                    }
                                }
                            });
                            if (this.doBreak) {
                                break;
                            }
                        }
                        if (this.doBreak) {
                            break;
                        }
                    } else {
                        throw new RuntimeException("ERROR: def can not be something else than Assign or Identity statement! (def: " + u + " class: " + u.getClass());
                    }
                }
                if (this.doBreak) {
                    break;
                }
            }
            if (this.usedAsObject) {
                Set<Unit> defsOp1 = localDefs.collectDefinitionsWithAliases(twoIfLocals[0]);
                Set<Unit> defsOp2 = localDefs.collectDefinitionsWithAliases(twoIfLocals[1]);
                defsOp1.addAll(defsOp2);
                for (Unit u2 : defsOp1) {
                    Stmt s = (Stmt) u2;
                    if (!s.containsArrayRef() || (!defsOp1.contains(s.getArrayRef().getBase()) && !defsOp2.contains(s.getArrayRef().getBase()))) {
                        replaceWithNull(u2);
                    }
                    Local l = (Local) ((DefinitionStmt) u2).getLeftOp();
                    for (Unit uuse : localDefs.getUsesOf(l)) {
                        Stmt use2 = (Stmt) uuse;
                        if (!use2.containsArrayRef() || (twoIfLocals[0] != use2.getArrayRef().getBase() && twoIfLocals[1] != use2.getArrayRef().getBase())) {
                            replaceWithNull(use2);
                        }
                    }
                }
            }
        }
    }

    private Set<IfStmt> getNullIfCandidates(Body body) {
        Set<IfStmt> candidates = new HashSet<>();
        Iterator<Unit> i = body.getUnits().iterator();
        while (i.hasNext()) {
            Unit u = i.next();
            if (u instanceof IfStmt) {
                ConditionExpr expr = (ConditionExpr) ((IfStmt) u).getCondition();
                boolean isTargetIf = false;
                if (((expr instanceof EqExpr) || (expr instanceof NeExpr)) && (expr.getOp1() instanceof Local) && (expr.getOp2() instanceof Local)) {
                    isTargetIf = true;
                }
                if (isTargetIf) {
                    candidates.add((IfStmt) u);
                }
            }
        }
        return candidates;
    }
}
