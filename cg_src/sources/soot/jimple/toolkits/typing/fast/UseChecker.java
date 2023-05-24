package soot.jimple.toolkits.typing.fast;

import heros.solver.Pair;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.BooleanType;
import soot.G;
import soot.IntType;
import soot.IntegerType;
import soot.JavaBasicTypes;
import soot.Local;
import soot.NullType;
import soot.PrimType;
import soot.RefType;
import soot.Scene;
import soot.SootMethodRef;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.AddExpr;
import soot.jimple.AndExpr;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.BreakpointStmt;
import soot.jimple.CastExpr;
import soot.jimple.CmpExpr;
import soot.jimple.CmpgExpr;
import soot.jimple.CmplExpr;
import soot.jimple.Constant;
import soot.jimple.DivExpr;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.EqExpr;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.FieldRef;
import soot.jimple.GeExpr;
import soot.jimple.GotoStmt;
import soot.jimple.GtExpr;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InstanceOfExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.JimpleBody;
import soot.jimple.LeExpr;
import soot.jimple.LengthExpr;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.LtExpr;
import soot.jimple.MulExpr;
import soot.jimple.NeExpr;
import soot.jimple.NegExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.NopStmt;
import soot.jimple.NullConstant;
import soot.jimple.OrExpr;
import soot.jimple.RemExpr;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.ShlExpr;
import soot.jimple.ShrExpr;
import soot.jimple.Stmt;
import soot.jimple.SubExpr;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThrowStmt;
import soot.jimple.UshrExpr;
import soot.jimple.XorExpr;
import soot.toolkits.scalar.LocalDefs;
import soot.toolkits.scalar.LocalUses;
import soot.toolkits.scalar.UnitValueBoxPair;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/fast/UseChecker.class */
public class UseChecker extends AbstractStmtSwitch {
    private final JimpleBody jb;
    private Typing tg;
    private IUseVisitor uv;
    private LocalDefs defs = null;
    private LocalUses uses = null;
    private static final Logger logger = LoggerFactory.getLogger(UseChecker.class);

    public UseChecker(JimpleBody jb) {
        this.jb = jb;
    }

    public void check(Typing tg, IUseVisitor uv) {
        if (tg == null) {
            throw new RuntimeException("null typing passed to useChecker");
        }
        this.tg = tg;
        this.uv = uv;
        Iterator<Unit> i = this.jb.getUnits().snapshotIterator();
        while (i.hasNext() && !uv.finish()) {
            i.next().apply(this);
        }
    }

    private void handleInvokeExpr(InvokeExpr ie, Stmt stmt) {
        SootMethodRef m = ie.getMethodRef();
        if (ie instanceof InstanceInvokeExpr) {
            InstanceInvokeExpr iie = (InstanceInvokeExpr) ie;
            iie.setBase(this.uv.visit(iie.getBase(), m.getDeclaringClass().getType(), stmt));
        }
        int e = ie.getArgCount();
        for (int i = 0; i < e; i++) {
            ie.setArg(i, this.uv.visit(ie.getArg(i), m.getParameterType(i), stmt));
        }
    }

    private void handleBinopExpr(BinopExpr be, Stmt stmt, Type tlhs) {
        Value opl = be.getOp1();
        Value opr = be.getOp2();
        Type tl = AugEvalFunction.eval_(this.tg, opl, stmt, this.jb);
        Type tr = AugEvalFunction.eval_(this.tg, opr, stmt, this.jb);
        if ((be instanceof AddExpr) || (be instanceof SubExpr) || (be instanceof MulExpr) || (be instanceof DivExpr) || (be instanceof RemExpr) || (be instanceof GeExpr) || (be instanceof GtExpr) || (be instanceof LeExpr) || (be instanceof LtExpr) || (be instanceof ShlExpr) || (be instanceof ShrExpr) || (be instanceof UshrExpr)) {
            if (tlhs instanceof IntegerType) {
                be.setOp1(this.uv.visit(opl, IntType.v(), stmt, true));
                be.setOp2(this.uv.visit(opr, IntType.v(), stmt, true));
            }
        } else if (!(be instanceof CmpExpr) && !(be instanceof CmpgExpr) && !(be instanceof CmplExpr)) {
            if ((be instanceof AndExpr) || (be instanceof OrExpr) || (be instanceof XorExpr)) {
                be.setOp1(this.uv.visit(opl, tlhs, stmt, true));
                be.setOp2(this.uv.visit(opr, tlhs, stmt, true));
            } else if ((be instanceof EqExpr) || (be instanceof NeExpr)) {
                if ((!(tl instanceof BooleanType) || !(tr instanceof BooleanType)) && !(tl instanceof Integer1Type) && !(tr instanceof Integer1Type) && (tl instanceof IntegerType)) {
                    be.setOp1(this.uv.visit(opl, IntType.v(), stmt, true));
                    be.setOp2(this.uv.visit(opr, IntType.v(), stmt, true));
                }
            }
        }
    }

    private void handleArrayRef(ArrayRef ar, Stmt stmt) {
        ar.setIndex(this.uv.visit(ar.getIndex(), IntType.v(), stmt));
    }

    private void handleInstanceFieldRef(InstanceFieldRef ifr, Stmt stmt) {
        ifr.setBase(this.uv.visit(ifr.getBase(), ifr.getFieldRef().declaringClass().getType(), stmt));
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseBreakpointStmt(BreakpointStmt stmt) {
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseInvokeStmt(InvokeStmt stmt) {
        handleInvokeExpr(stmt.getInvokeExpr(), stmt);
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseAssignStmt(AssignStmt stmt) {
        Value other;
        Value other2;
        ArrayType at;
        Value lhs = stmt.getLeftOp();
        Value rhs = stmt.getRightOp();
        Type tlhs = null;
        if (lhs instanceof Local) {
            tlhs = this.tg.get((Local) lhs);
        } else if (lhs instanceof ArrayRef) {
            ArrayRef aref = (ArrayRef) lhs;
            Local base = (Local) aref.getBase();
            ArrayType at2 = null;
            Type tgType = this.tg.get(base);
            if (tgType instanceof ArrayType) {
                at2 = (ArrayType) tgType;
            } else {
                if (rhs instanceof Local) {
                    Type rhsType = this.tg.get((Local) rhs);
                    if ((tgType == Scene.v().getObjectType() && (rhsType instanceof PrimType)) || (tgType instanceof WeakObjectType)) {
                        if (this.defs == null) {
                            this.defs = G.v().soot_toolkits_scalar_LocalDefsFactory().newLocalDefs(this.jb);
                            this.uses = LocalUses.Factory.newLocalUses(this.jb, this.defs);
                        }
                        boolean hasDefs = false;
                        Iterator<Unit> it = this.defs.getDefsOfAt(base, stmt).iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            Unit defU = it.next();
                            if (defU instanceof AssignStmt) {
                                AssignStmt defUas = (AssignStmt) defU;
                                if (defUas.getRightOp() instanceof NewArrayExpr) {
                                    at2 = (ArrayType) defUas.getRightOp().getType();
                                    hasDefs = true;
                                    break;
                                }
                            }
                        }
                        if (!hasDefs) {
                            at2 = ArrayType.v(rhsType, 1);
                        }
                    }
                }
                if (at2 == null) {
                    at2 = tgType.makeArrayType();
                }
            }
            tlhs = at2.getElementType();
            handleArrayRef(aref, stmt);
            aref.setBase((Local) this.uv.visit(aref.getBase(), at2, stmt));
            stmt.setRightOp(this.uv.visit(rhs, tlhs, stmt));
            stmt.setLeftOp(this.uv.visit(lhs, tlhs, stmt));
        } else if (lhs instanceof FieldRef) {
            tlhs = ((FieldRef) lhs).getFieldRef().type();
            if (lhs instanceof InstanceFieldRef) {
                handleInstanceFieldRef((InstanceFieldRef) lhs, stmt);
            }
        }
        stmt.getLeftOp();
        Value rhs2 = stmt.getRightOp();
        if (rhs2 instanceof Local) {
            stmt.setRightOp(this.uv.visit(rhs2, tlhs, stmt));
        } else if (rhs2 instanceof ArrayRef) {
            ArrayRef aref2 = (ArrayRef) rhs2;
            Type bt = this.tg.get((Local) aref2.getBase());
            if (bt instanceof ArrayType) {
                at = (ArrayType) bt;
            } else {
                Type et = null;
                if ((bt instanceof RefType) || (bt instanceof NullType)) {
                    String btName = bt instanceof NullType ? null : ((RefType) bt).getSootClass().getName();
                    if (btName == null || Scene.v().getObjectType().toString().equals(btName) || JavaBasicTypes.JAVA_IO_SERIALIZABLE.equals(btName) || "java.lang.Cloneable".equals(btName)) {
                        if (this.defs == null) {
                            this.defs = G.v().soot_toolkits_scalar_LocalDefsFactory().newLocalDefs(this.jb);
                            this.uses = LocalUses.Factory.newLocalUses(this.jb, this.defs);
                        }
                        ArrayDeque<Pair<Unit, Local>> worklist = new ArrayDeque<>();
                        Set<Pair<Unit, Local>> seen = new HashSet<>();
                        worklist.add(new Pair<>(stmt, (Local) ((ArrayRef) rhs2).getBase()));
                        while (!worklist.isEmpty()) {
                            Pair<Unit, Local> r = worklist.removeFirst();
                            if (seen.add(r)) {
                                List<Unit> d = this.defs.getDefsOfAt(r.getO2(), r.getO1());
                                if (d.isEmpty()) {
                                    this.defs = G.v().soot_toolkits_scalar_LocalDefsFactory().newLocalDefs(this.jb);
                                    this.uses = LocalUses.Factory.newLocalUses(this.jb, this.defs);
                                    d = this.defs.getDefsOfAt(r.getO2(), r.getO1());
                                }
                                for (Unit u : d) {
                                    if (u instanceof AssignStmt) {
                                        AssignStmt assign = (AssignStmt) u;
                                        Value rop = assign.getRightOp();
                                        if (rop instanceof NewArrayExpr) {
                                            et = merge(stmt, et, ((NewArrayExpr) assign.getRightOp()).getBaseType());
                                        } else if (rop instanceof Local) {
                                            worklist.add(new Pair<>(u, (Local) rop));
                                        } else if (rop instanceof CastExpr) {
                                            worklist.add(new Pair<>(u, (Local) ((CastExpr) rop).getOp()));
                                        }
                                    }
                                }
                            }
                        }
                        if (et == null) {
                            Iterator<UnitValueBoxPair> it2 = this.uses.getUsesOf(stmt).iterator();
                            loop2: while (true) {
                                if (!it2.hasNext()) {
                                    break;
                                }
                                UnitValueBoxPair usePair = it2.next();
                                Stmt useStmt = (Stmt) usePair.getUnit();
                                if (useStmt.containsInvokeExpr()) {
                                    InvokeExpr invokeExpr = useStmt.getInvokeExpr();
                                    int e = invokeExpr.getArgCount();
                                    for (int i = 0; i < e; i++) {
                                        if (invokeExpr.getArg(i) == usePair.getValueBox().getValue()) {
                                            et = merge(stmt, et, invokeExpr.getMethod().getParameterType(i));
                                            break loop2;
                                        }
                                    }
                                    continue;
                                } else if (useStmt instanceof IfStmt) {
                                    Value condition = ((IfStmt) useStmt).getCondition();
                                    if (condition instanceof EqExpr) {
                                        EqExpr expr = (EqExpr) condition;
                                        if (expr.getOp1() == usePair.getValueBox().getValue()) {
                                            other = expr.getOp2();
                                        } else {
                                            other = expr.getOp1();
                                        }
                                        Type newEt = getTargetType(other);
                                        if (newEt != null) {
                                            et = merge(stmt, et, newEt);
                                        }
                                    }
                                } else if (useStmt instanceof AssignStmt) {
                                    AssignStmt useAssignStmt = (AssignStmt) useStmt;
                                    Value rop2 = useAssignStmt.getRightOp();
                                    if (rop2 instanceof BinopExpr) {
                                        BinopExpr binOp = (BinopExpr) rop2;
                                        if (binOp.getOp1() == usePair.getValueBox().getValue()) {
                                            other2 = binOp.getOp2();
                                        } else {
                                            other2 = binOp.getOp1();
                                        }
                                        Type newEt2 = getTargetType(other2);
                                        if (newEt2 != null) {
                                            et = merge(stmt, et, newEt2);
                                        }
                                    } else if (rop2 instanceof CastExpr) {
                                        et = merge(stmt, et, ((CastExpr) rop2).getCastType());
                                    }
                                } else if (useStmt instanceof ReturnStmt) {
                                    et = merge(stmt, et, this.jb.getMethod().getReturnType());
                                }
                            }
                        }
                    }
                }
                if (et == null) {
                    et = bt;
                    logger.warn("Could not find any indication on the array type of " + stmt + " in " + this.jb.getMethod().getSignature(), ", assuming its base type is " + bt);
                }
                at = et.makeArrayType();
            }
            Type trhs = at.getElementType();
            handleArrayRef(aref2, stmt);
            aref2.setBase((Local) this.uv.visit(aref2.getBase(), at, stmt));
            stmt.setRightOp(this.uv.visit(rhs2, trhs, stmt));
        } else if (rhs2 instanceof InstanceFieldRef) {
            handleInstanceFieldRef((InstanceFieldRef) rhs2, stmt);
            stmt.setRightOp(this.uv.visit(rhs2, tlhs, stmt));
        } else if (rhs2 instanceof BinopExpr) {
            handleBinopExpr((BinopExpr) rhs2, stmt, tlhs);
        } else if (rhs2 instanceof InvokeExpr) {
            handleInvokeExpr((InvokeExpr) rhs2, stmt);
            stmt.setRightOp(this.uv.visit(rhs2, tlhs, stmt));
        } else if (rhs2 instanceof CastExpr) {
            stmt.setRightOp(this.uv.visit(rhs2, tlhs, stmt));
        } else if (rhs2 instanceof InstanceOfExpr) {
            InstanceOfExpr ioe = (InstanceOfExpr) rhs2;
            ioe.setOp(this.uv.visit(ioe.getOp(), Scene.v().getObjectType(), stmt));
            stmt.setRightOp(this.uv.visit(rhs2, tlhs, stmt));
        } else if (rhs2 instanceof NewArrayExpr) {
            NewArrayExpr nae = (NewArrayExpr) rhs2;
            nae.setSize(this.uv.visit(nae.getSize(), IntType.v(), stmt));
            stmt.setRightOp(this.uv.visit(rhs2, tlhs, stmt));
        } else if (rhs2 instanceof NewMultiArrayExpr) {
            NewMultiArrayExpr nmae = (NewMultiArrayExpr) rhs2;
            int e2 = nmae.getSizeCount();
            for (int i2 = 0; i2 < e2; i2++) {
                nmae.setSize(i2, this.uv.visit(nmae.getSize(i2), IntType.v(), stmt));
            }
            stmt.setRightOp(this.uv.visit(rhs2, tlhs, stmt));
        } else if (rhs2 instanceof LengthExpr) {
            stmt.setRightOp(this.uv.visit(rhs2, tlhs, stmt));
        } else if (rhs2 instanceof NegExpr) {
            ((NegExpr) rhs2).setOp(this.uv.visit(((NegExpr) rhs2).getOp(), tlhs, stmt));
        } else if ((rhs2 instanceof Constant) && !(rhs2 instanceof NullConstant)) {
            stmt.setRightOp(this.uv.visit(rhs2, tlhs, stmt));
        }
    }

    protected Type merge(Stmt stmt, Type previousType, Type newType) {
        Type choose;
        if (previousType == null) {
            return newType;
        }
        if (newType == previousType) {
            return previousType;
        }
        if (TypeUtils.getValueBitSize(previousType) > TypeUtils.getValueBitSize(newType)) {
            choose = previousType;
        } else {
            choose = newType;
        }
        logger.warn("Conflicting array types at " + stmt + " in " + this.jb.getMethod().getSignature(), ", its base type may be " + previousType + " or " + newType + ". Choosing " + choose + ".");
        return newType;
    }

    private Type getTargetType(Value other) {
        if (other instanceof Constant) {
            if (other.getType() != NullType.v()) {
                return other.getType();
            }
            return null;
        } else if (other instanceof Local) {
            Type tgTp = this.tg.get((Local) other);
            if (tgTp instanceof PrimType) {
                return tgTp;
            }
            return null;
        } else {
            return null;
        }
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseIdentityStmt(IdentityStmt stmt) {
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
        stmt.setOp(this.uv.visit(stmt.getOp(), RefType.v(JavaBasicTypes.JAVA_LANG_OBJECT), stmt));
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
        stmt.setOp(this.uv.visit(stmt.getOp(), RefType.v(JavaBasicTypes.JAVA_LANG_OBJECT), stmt));
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseGotoStmt(GotoStmt stmt) {
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseIfStmt(IfStmt stmt) {
        handleBinopExpr((BinopExpr) stmt.getCondition(), stmt, BooleanType.v());
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
        stmt.setKey(this.uv.visit(stmt.getKey(), IntType.v(), stmt));
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseNopStmt(NopStmt stmt) {
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseReturnStmt(ReturnStmt stmt) {
        stmt.setOp(this.uv.visit(stmt.getOp(), this.jb.getMethod().getReturnType(), stmt));
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseTableSwitchStmt(TableSwitchStmt stmt) {
        stmt.setKey(this.uv.visit(stmt.getKey(), IntType.v(), stmt));
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseThrowStmt(ThrowStmt stmt) {
        stmt.setOp(this.uv.visit(stmt.getOp(), Scene.v().getBaseExceptionType(), stmt));
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void defaultCase(Object stmt) {
        throw new RuntimeException("Unhandled type: " + stmt.getClass());
    }
}
