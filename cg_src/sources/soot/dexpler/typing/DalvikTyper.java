package soot.dexpler.typing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.JavaBasicTypes;
import soot.Local;
import soot.LongType;
import soot.RefType;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.coffi.Instruction;
import soot.dexpler.IDalvikTyper;
import soot.dexpler.tags.DoubleOpTag;
import soot.dexpler.tags.FloatOpTag;
import soot.dexpler.tags.IntOpTag;
import soot.dexpler.tags.LongOpTag;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.BreakpointStmt;
import soot.jimple.CastExpr;
import soot.jimple.DivExpr;
import soot.jimple.DynamicInvokeExpr;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.NewArrayExpr;
import soot.jimple.NopStmt;
import soot.jimple.RemExpr;
import soot.jimple.RetStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.ShlExpr;
import soot.jimple.ShrExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.StmtSwitch;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThrowStmt;
import soot.jimple.UshrExpr;
import soot.tagkit.Tag;
/* loaded from: gencallgraphv3.jar:soot/dexpler/typing/DalvikTyper.class */
public class DalvikTyper implements IDalvikTyper {
    private static DalvikTyper dt = null;
    private Set<Constraint> constraints = new HashSet();
    private Map<ValueBox, Type> typed = new HashMap();
    private Map<Local, Type> localTyped = new HashMap();
    private Set<Local> localTemp = new HashSet();
    private List<LocalObj> localObjList = new ArrayList();
    private Map<Local, List<LocalObj>> local2Obj = new HashMap();

    private DalvikTyper() {
    }

    public static DalvikTyper v() {
        if (dt == null) {
            dt = new DalvikTyper();
        }
        return dt;
    }

    public void clear() {
        this.constraints.clear();
        this.typed.clear();
        this.localTyped.clear();
        this.localTemp.clear();
        this.localObjList.clear();
        this.local2Obj.clear();
    }

    @Override // soot.dexpler.IDalvikTyper
    public void setType(ValueBox vb, Type t, boolean isUse) {
        if (vb.getValue() instanceof Local) {
            LocalObj lb = new LocalObj(vb, t, isUse);
            this.localObjList.add(lb);
            Local k = (Local) vb.getValue();
            if (!this.local2Obj.containsKey(k)) {
                this.local2Obj.put(k, new ArrayList());
            }
            this.local2Obj.get(k).add(lb);
        }
    }

    @Override // soot.dexpler.IDalvikTyper
    public void addConstraint(ValueBox l, ValueBox r) {
    }

    /* JADX WARN: Code restructure failed: missing block: B:225:0x09f3, code lost:
        throw new java.lang.RuntimeException("error: do not handling this kind of constraint: " + r0);
     */
    @Override // soot.dexpler.IDalvikTyper
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void assignType(final soot.Body r7) {
        /*
            Method dump skipped, instructions count: 3099
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.dexpler.typing.DalvikTyper.assignType(soot.Body):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeUntypedConstantsInInvoke(InvokeExpr invokeExpr) {
        for (int i = 0; i < invokeExpr.getArgCount(); i++) {
            Value v = invokeExpr.getArg(i);
            if (v instanceof UntypedConstant) {
                Type t = invokeExpr.getMethodRef().parameterType(i);
                UntypedConstant uc = (UntypedConstant) v;
                invokeExpr.setArg(i, uc.defineType(t));
            }
        }
    }

    protected void checkExpr(Value v, Type t) {
        for (ValueBox vb : v.getUseBoxes()) {
            Value value = vb.getValue();
            if (value instanceof Local) {
                if (((v instanceof ShrExpr) || (v instanceof ShlExpr) || (v instanceof UshrExpr)) && ((BinopExpr) v).getOp2() == value) {
                    v().setType(vb, IntType.v(), true);
                } else {
                    v().setType(vb, t, true);
                }
            } else if (value instanceof UntypedConstant) {
                UntypedConstant uc = (UntypedConstant) value;
                if (((v instanceof ShrExpr) || (v instanceof ShlExpr) || (v instanceof UshrExpr)) && ((BinopExpr) v).getOp2() == value) {
                    UntypedIntOrFloatConstant ui = (UntypedIntOrFloatConstant) uc;
                    vb.setValue(ui.toIntConstant());
                } else {
                    vb.setValue(uc.defineType(t));
                }
            }
        }
    }

    protected void setInvokeType(InvokeExpr invokeExpr) {
        for (int i = 0; i < invokeExpr.getArgCount(); i++) {
            Value v = invokeExpr.getArg(i);
            if (v instanceof Local) {
                Type t = invokeExpr.getMethodRef().parameterType(i);
                v().setType(invokeExpr.getArgBox(i), t, true);
            }
        }
        if (!(invokeExpr instanceof StaticInvokeExpr)) {
            if (invokeExpr instanceof InstanceInvokeExpr) {
                InstanceInvokeExpr iie = (InstanceInvokeExpr) invokeExpr;
                v().setType(iie.getBaseBox(), RefType.v(JavaBasicTypes.JAVA_LANG_OBJECT), true);
            } else if (invokeExpr instanceof DynamicInvokeExpr) {
                DynamicInvokeExpr dynamicInvokeExpr = (DynamicInvokeExpr) invokeExpr;
            } else {
                throw new RuntimeException("error: unhandled invoke expression: " + invokeExpr + Instruction.argsep + invokeExpr.getClass());
            }
        }
    }

    private void setLocalTyped(Local l, Type t) {
        this.localTyped.put(l, t);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:soot/dexpler/typing/DalvikTyper$LocalObj.class */
    public class LocalObj {
        ValueBox vb;
        Type t;
        boolean isUse;

        public LocalObj(ValueBox vb, Type t, boolean isUse) {
            this.vb = vb;
            this.t = t;
            this.isUse = isUse;
        }

        public Local getLocal() {
            return (Local) this.vb.getValue();
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/dexpler/typing/DalvikTyper$Constraint.class */
    class Constraint {
        ValueBox l;
        ValueBox r;

        public Constraint(ValueBox l, ValueBox r) {
            this.l = l;
            this.r = r;
        }

        public String toString() {
            return this.l + " < " + this.r;
        }
    }

    public void typeUntypedConstrantInDiv(final Body b) {
        Iterator<Unit> it = b.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            StmtSwitch sw = new StmtSwitch() { // from class: soot.dexpler.typing.DalvikTyper.3
                @Override // soot.jimple.StmtSwitch
                public void caseBreakpointStmt(BreakpointStmt stmt) {
                }

                @Override // soot.jimple.StmtSwitch
                public void caseInvokeStmt(InvokeStmt stmt) {
                    DalvikTyper.this.changeUntypedConstantsInInvoke(stmt.getInvokeExpr());
                }

                @Override // soot.jimple.StmtSwitch
                public void caseAssignStmt(AssignStmt stmt) {
                    if (stmt.getRightOp() instanceof NewArrayExpr) {
                        NewArrayExpr nae = (NewArrayExpr) stmt.getRightOp();
                        if (nae.getSize() instanceof UntypedConstant) {
                            nae.setSize(((UntypedIntOrFloatConstant) nae.getSize()).defineType(IntType.v()));
                        }
                    } else if (stmt.getRightOp() instanceof InvokeExpr) {
                        DalvikTyper.this.changeUntypedConstantsInInvoke((InvokeExpr) stmt.getRightOp());
                    } else if (stmt.getRightOp() instanceof CastExpr) {
                        CastExpr ce = (CastExpr) stmt.getRightOp();
                        if (ce.getOp() instanceof UntypedConstant) {
                            UntypedConstant uc = (UntypedConstant) ce.getOp();
                            for (Tag t : stmt.getTags()) {
                                if (t instanceof IntOpTag) {
                                    ce.setOp(uc.defineType(IntType.v()));
                                    return;
                                } else if (t instanceof FloatOpTag) {
                                    ce.setOp(uc.defineType(FloatType.v()));
                                    return;
                                } else if (t instanceof DoubleOpTag) {
                                    ce.setOp(uc.defineType(DoubleType.v()));
                                    return;
                                } else if (t instanceof LongOpTag) {
                                    ce.setOp(uc.defineType(LongType.v()));
                                    return;
                                }
                            }
                            ce.setOp(uc.defineType(RefType.v(JavaBasicTypes.JAVA_LANG_OBJECT)));
                        }
                    }
                    if (stmt.containsArrayRef()) {
                        ArrayRef ar = stmt.getArrayRef();
                        if (ar.getIndex() instanceof UntypedConstant) {
                            ar.setIndex(((UntypedIntOrFloatConstant) ar.getIndex()).toIntConstant());
                        }
                    }
                    Value r = stmt.getRightOp();
                    if ((r instanceof DivExpr) || (r instanceof RemExpr)) {
                        for (Tag t2 : stmt.getTags()) {
                            if (t2 instanceof IntOpTag) {
                                DalvikTyper.this.checkExpr(r, IntType.v());
                                return;
                            } else if (t2 instanceof FloatOpTag) {
                                DalvikTyper.this.checkExpr(r, FloatType.v());
                                return;
                            } else if (t2 instanceof DoubleOpTag) {
                                DalvikTyper.this.checkExpr(r, DoubleType.v());
                                return;
                            } else if (t2 instanceof LongOpTag) {
                                DalvikTyper.this.checkExpr(r, LongType.v());
                                return;
                            }
                        }
                    }
                }

                @Override // soot.jimple.StmtSwitch
                public void caseIdentityStmt(IdentityStmt stmt) {
                }

                @Override // soot.jimple.StmtSwitch
                public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
                }

                @Override // soot.jimple.StmtSwitch
                public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
                }

                @Override // soot.jimple.StmtSwitch
                public void caseGotoStmt(GotoStmt stmt) {
                }

                @Override // soot.jimple.StmtSwitch
                public void caseIfStmt(IfStmt stmt) {
                }

                @Override // soot.jimple.StmtSwitch
                public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
                }

                @Override // soot.jimple.StmtSwitch
                public void caseNopStmt(NopStmt stmt) {
                }

                @Override // soot.jimple.StmtSwitch
                public void caseRetStmt(RetStmt stmt) {
                }

                @Override // soot.jimple.StmtSwitch
                public void caseReturnStmt(ReturnStmt stmt) {
                    if (stmt.getOp() instanceof UntypedConstant) {
                        UntypedConstant uc = (UntypedConstant) stmt.getOp();
                        Type type = b.getMethod().getReturnType();
                        stmt.setOp(uc.defineType(type));
                    }
                }

                @Override // soot.jimple.StmtSwitch
                public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
                }

                @Override // soot.jimple.StmtSwitch
                public void caseTableSwitchStmt(TableSwitchStmt stmt) {
                }

                @Override // soot.jimple.StmtSwitch
                public void caseThrowStmt(ThrowStmt stmt) {
                    if (stmt.getOp() instanceof UntypedConstant) {
                        UntypedConstant uc = (UntypedConstant) stmt.getOp();
                        stmt.setOp(uc.defineType(RefType.v(JavaBasicTypes.JAVA_LANG_OBJECT)));
                    }
                }

                @Override // soot.jimple.StmtSwitch
                public void defaultCase(Object obj) {
                }
            };
            u.apply(sw);
        }
    }
}
