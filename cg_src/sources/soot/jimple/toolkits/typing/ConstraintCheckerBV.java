package soot.jimple.toolkits.typing;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.JavaBasicTypes;
import soot.Local;
import soot.LongType;
import soot.NullType;
import soot.RefType;
import soot.Scene;
import soot.SootMethodRef;
import soot.TrapManager;
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
import soot.jimple.CaughtExceptionRef;
import soot.jimple.ClassConstant;
import soot.jimple.CmpExpr;
import soot.jimple.CmpgExpr;
import soot.jimple.CmplExpr;
import soot.jimple.ConditionExpr;
import soot.jimple.DivExpr;
import soot.jimple.DoubleConstant;
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
import soot.jimple.InstanceOfExpr;
import soot.jimple.IntConstant;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.LeExpr;
import soot.jimple.LengthExpr;
import soot.jimple.LongConstant;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.LtExpr;
import soot.jimple.MulExpr;
import soot.jimple.NeExpr;
import soot.jimple.NegExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.NopStmt;
import soot.jimple.NullConstant;
import soot.jimple.OrExpr;
import soot.jimple.RemExpr;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.ShlExpr;
import soot.jimple.ShrExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.SubExpr;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThrowStmt;
import soot.jimple.UshrExpr;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.XorExpr;
/* JADX INFO: Access modifiers changed from: package-private */
@Deprecated
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/ConstraintCheckerBV.class */
public class ConstraintCheckerBV extends AbstractStmtSwitch {
    private static final Logger logger = LoggerFactory.getLogger(ConstraintCheckerBV.class);
    private final ClassHierarchy hierarchy;
    private final boolean fix;
    private JimpleBody stmtBody;

    public ConstraintCheckerBV(TypeResolverBV resolver, boolean fix) {
        this.fix = fix;
        this.hierarchy = resolver.hierarchy();
    }

    public void check(Stmt stmt, JimpleBody stmtBody) throws TypeException {
        try {
            this.stmtBody = stmtBody;
            stmt.apply(this);
        } catch (RuntimeTypeException e) {
            StringWriter st = new StringWriter();
            PrintWriter pw = new PrintWriter(st);
            logger.error(e.getMessage(), (Throwable) e);
            pw.close();
            throw new TypeException(st.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/ConstraintCheckerBV$RuntimeTypeException.class */
    public static class RuntimeTypeException extends RuntimeException {
        RuntimeTypeException(String message) {
            super(message);
        }
    }

    static void error(String message) {
        throw new RuntimeTypeException(message);
    }

    private void handleInvokeExpr(InvokeExpr ie, Stmt invokestmt) {
        if (ie instanceof InterfaceInvokeExpr) {
            InterfaceInvokeExpr invoke = (InterfaceInvokeExpr) ie;
            SootMethodRef method = invoke.getMethodRef();
            Value base = invoke.getBase();
            if (base instanceof Local) {
                Local local = (Local) base;
                if (!this.hierarchy.typeNode(local.getType()).hasAncestorOrSelf(this.hierarchy.typeNode(method.declaringClass().getType()))) {
                    if (this.fix) {
                        invoke.setBase(insertCast(local, method.declaringClass().getType(), invokestmt));
                    } else {
                        error("Type Error(7): local " + local + " is of incompatible type " + local.getType());
                    }
                }
            }
            int count = invoke.getArgCount();
            for (int i = 0; i < count; i++) {
                if (invoke.getArg(i) instanceof Local) {
                    Local local2 = (Local) invoke.getArg(i);
                    if (!this.hierarchy.typeNode(local2.getType()).hasAncestorOrSelf(this.hierarchy.typeNode(method.parameterType(i)))) {
                        if (this.fix) {
                            invoke.setArg(i, insertCast(local2, method.parameterType(i), invokestmt));
                        } else {
                            error("Type Error(8)");
                        }
                    }
                }
            }
        } else if (ie instanceof SpecialInvokeExpr) {
            SpecialInvokeExpr invoke2 = (SpecialInvokeExpr) ie;
            SootMethodRef method2 = invoke2.getMethodRef();
            Value base2 = invoke2.getBase();
            if (base2 instanceof Local) {
                Local local3 = (Local) base2;
                if (!this.hierarchy.typeNode(local3.getType()).hasAncestorOrSelf(this.hierarchy.typeNode(method2.declaringClass().getType()))) {
                    if (this.fix) {
                        invoke2.setBase(insertCast(local3, method2.declaringClass().getType(), invokestmt));
                    } else {
                        error("Type Error(9)");
                    }
                }
            }
            int count2 = invoke2.getArgCount();
            for (int i2 = 0; i2 < count2; i2++) {
                if (invoke2.getArg(i2) instanceof Local) {
                    Local local4 = (Local) invoke2.getArg(i2);
                    if (!this.hierarchy.typeNode(local4.getType()).hasAncestorOrSelf(this.hierarchy.typeNode(method2.parameterType(i2)))) {
                        if (this.fix) {
                            invoke2.setArg(i2, insertCast(local4, method2.parameterType(i2), invokestmt));
                        } else {
                            error("Type Error(10)");
                        }
                    }
                }
            }
        } else if (ie instanceof VirtualInvokeExpr) {
            VirtualInvokeExpr invoke3 = (VirtualInvokeExpr) ie;
            SootMethodRef method3 = invoke3.getMethodRef();
            Value base3 = invoke3.getBase();
            if (base3 instanceof Local) {
                Local local5 = (Local) base3;
                if (!this.hierarchy.typeNode(local5.getType()).hasAncestorOrSelf(this.hierarchy.typeNode(method3.declaringClass().getType()))) {
                    if (this.fix) {
                        invoke3.setBase(insertCast(local5, method3.declaringClass().getType(), invokestmt));
                    } else {
                        error("Type Error(13)");
                    }
                }
            }
            int count3 = invoke3.getArgCount();
            for (int i3 = 0; i3 < count3; i3++) {
                if (invoke3.getArg(i3) instanceof Local) {
                    Local local6 = (Local) invoke3.getArg(i3);
                    if (!this.hierarchy.typeNode(local6.getType()).hasAncestorOrSelf(this.hierarchy.typeNode(method3.parameterType(i3)))) {
                        if (this.fix) {
                            invoke3.setArg(i3, insertCast(local6, method3.parameterType(i3), invokestmt));
                        } else {
                            error("Type Error(14)");
                        }
                    }
                }
            }
        } else {
            if (ie instanceof StaticInvokeExpr) {
                StaticInvokeExpr invoke4 = (StaticInvokeExpr) ie;
                SootMethodRef method4 = invoke4.getMethodRef();
                int count4 = invoke4.getArgCount();
                for (int i4 = 0; i4 < count4; i4++) {
                    if (invoke4.getArg(i4) instanceof Local) {
                        Local local7 = (Local) invoke4.getArg(i4);
                        if (!this.hierarchy.typeNode(local7.getType()).hasAncestorOrSelf(this.hierarchy.typeNode(method4.parameterType(i4)))) {
                            if (this.fix) {
                                invoke4.setArg(i4, insertCast(local7, method4.parameterType(i4), invokestmt));
                            } else {
                                error("Type Error(15)");
                            }
                        }
                    }
                }
                return;
            }
            throw new RuntimeException("Unhandled invoke expression type: " + ie.getClass());
        }
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
        TypeNode left;
        TypeNode right;
        TypeNode right2;
        TypeNode lop;
        TypeNode rop;
        Value l = stmt.getLeftOp();
        Value r = stmt.getRightOp();
        if (l instanceof ArrayRef) {
            ArrayRef ref = (ArrayRef) l;
            TypeNode base = this.hierarchy.typeNode(((Local) ref.getBase()).getType());
            if (!base.isArray()) {
                error("Type Error(16)");
            }
            left = base.element();
            Value index = ref.getIndex();
            if ((index instanceof Local) && !this.hierarchy.typeNode(((Local) index).getType()).hasAncestorOrSelf(this.hierarchy.typeNode(IntType.v()))) {
                error("Type Error(17)");
            }
        } else if (l instanceof Local) {
            try {
                left = this.hierarchy.typeNode(((Local) l).getType());
            } catch (InternalTypingException e) {
                logger.debug("untyped local: " + l);
                throw e;
            }
        } else if (l instanceof InstanceFieldRef) {
            InstanceFieldRef ref2 = (InstanceFieldRef) l;
            if (!this.hierarchy.typeNode(((Local) ref2.getBase()).getType()).hasAncestorOrSelf(this.hierarchy.typeNode(ref2.getField().getDeclaringClass().getType()))) {
                if (this.fix) {
                    ref2.setBase(insertCast((Local) ref2.getBase(), ref2.getField().getDeclaringClass().getType(), stmt));
                } else {
                    error("Type Error(18)");
                }
            }
            left = this.hierarchy.typeNode(ref2.getField().getType());
        } else if (l instanceof StaticFieldRef) {
            left = this.hierarchy.typeNode(((StaticFieldRef) l).getField().getType());
        } else {
            throw new RuntimeException("Unhandled assignment left hand side type: " + l.getClass());
        }
        if (r instanceof ArrayRef) {
            ArrayRef ref3 = (ArrayRef) r;
            TypeNode base2 = this.hierarchy.typeNode(((Local) ref3.getBase()).getType());
            if (!base2.isArray()) {
                error("Type Error(19): " + base2 + " is not an array type");
            }
            if (base2 == this.hierarchy.NULL) {
                return;
            }
            if (!left.hasDescendantOrSelf(base2.element())) {
                if (this.fix) {
                    Type lefttype = left.type();
                    if (lefttype instanceof ArrayType) {
                        ArrayType atype = (ArrayType) lefttype;
                        ref3.setBase(insertCast((Local) ref3.getBase(), ArrayType.v(atype.baseType, atype.numDimensions + 1), stmt));
                    } else {
                        ref3.setBase(insertCast((Local) ref3.getBase(), ArrayType.v(lefttype, 1), stmt));
                    }
                } else {
                    error("Type Error(20)");
                }
            }
            Value index2 = ref3.getIndex();
            if ((index2 instanceof Local) && !this.hierarchy.typeNode(((Local) index2).getType()).hasAncestorOrSelf(this.hierarchy.typeNode(IntType.v()))) {
                error("Type Error(21)");
            }
        } else if (r instanceof DoubleConstant) {
            if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(DoubleType.v()))) {
                error("Type Error(22)");
            }
        } else if (r instanceof FloatConstant) {
            if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(FloatType.v()))) {
                error("Type Error(45)");
            }
        } else if (r instanceof IntConstant) {
            if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(IntType.v()))) {
                error("Type Error(23)");
            }
        } else if (r instanceof LongConstant) {
            if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(LongType.v()))) {
                error("Type Error(24)");
            }
        } else if (r instanceof NullConstant) {
            if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(NullType.v()))) {
                error("Type Error(25)");
            }
        } else if (r instanceof StringConstant) {
            if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(RefType.v("java.lang.String")))) {
                error("Type Error(26)");
            }
        } else if (r instanceof ClassConstant) {
            if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(RefType.v("java.lang.Class")))) {
                error("Type Error(27)");
            }
        } else if (r instanceof BinopExpr) {
            BinopExpr be = (BinopExpr) r;
            Value lv = be.getOp1();
            Value rv = be.getOp2();
            if (lv instanceof Local) {
                lop = this.hierarchy.typeNode(((Local) lv).getType());
            } else if (lv instanceof DoubleConstant) {
                lop = this.hierarchy.typeNode(DoubleType.v());
            } else if (lv instanceof FloatConstant) {
                lop = this.hierarchy.typeNode(FloatType.v());
            } else if (lv instanceof IntConstant) {
                lop = this.hierarchy.typeNode(IntType.v());
            } else if (lv instanceof LongConstant) {
                lop = this.hierarchy.typeNode(LongType.v());
            } else if (lv instanceof NullConstant) {
                lop = this.hierarchy.typeNode(NullType.v());
            } else if (lv instanceof StringConstant) {
                lop = this.hierarchy.typeNode(RefType.v("java.lang.String"));
            } else if (lv instanceof ClassConstant) {
                lop = this.hierarchy.typeNode(RefType.v("java.lang.Class"));
            } else {
                throw new RuntimeException("Unhandled binary expression left operand type: " + lv.getClass());
            }
            if (rv instanceof Local) {
                rop = this.hierarchy.typeNode(((Local) rv).getType());
            } else if (rv instanceof DoubleConstant) {
                rop = this.hierarchy.typeNode(DoubleType.v());
            } else if (rv instanceof FloatConstant) {
                rop = this.hierarchy.typeNode(FloatType.v());
            } else if (rv instanceof IntConstant) {
                rop = this.hierarchy.typeNode(IntType.v());
            } else if (rv instanceof LongConstant) {
                rop = this.hierarchy.typeNode(LongType.v());
            } else if (rv instanceof NullConstant) {
                rop = this.hierarchy.typeNode(NullType.v());
            } else if (rv instanceof StringConstant) {
                rop = this.hierarchy.typeNode(RefType.v("java.lang.String"));
            } else if (rv instanceof ClassConstant) {
                rop = this.hierarchy.typeNode(RefType.v("java.lang.Class"));
            } else {
                throw new RuntimeException("Unhandled binary expression right operand type: " + rv.getClass());
            }
            if ((be instanceof AddExpr) || (be instanceof SubExpr) || (be instanceof MulExpr) || (be instanceof DivExpr) || (be instanceof RemExpr) || (be instanceof AndExpr) || (be instanceof OrExpr) || (be instanceof XorExpr)) {
                if (!left.hasDescendantOrSelf(lop) || !left.hasDescendantOrSelf(rop)) {
                    error("Type Error(27)");
                }
            } else if ((be instanceof ShlExpr) || (be instanceof ShrExpr) || (be instanceof UshrExpr)) {
                if (!left.hasDescendantOrSelf(lop) || !this.hierarchy.typeNode(IntType.v()).hasAncestorOrSelf(rop)) {
                    error("Type Error(28)");
                }
            } else if ((be instanceof CmpExpr) || (be instanceof CmpgExpr) || (be instanceof CmplExpr) || (be instanceof EqExpr) || (be instanceof GeExpr) || (be instanceof GtExpr) || (be instanceof LeExpr) || (be instanceof LtExpr) || (be instanceof NeExpr)) {
                try {
                    lop.lca(rop);
                } catch (TypeException e2) {
                    error(e2.getMessage());
                }
                if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(IntType.v()))) {
                    error("Type Error(29)");
                }
            } else {
                throw new RuntimeException("Unhandled binary expression type: " + be.getClass());
            }
        } else if (r instanceof CastExpr) {
            CastExpr ce = (CastExpr) r;
            TypeNode cast = this.hierarchy.typeNode(ce.getCastType());
            if (ce.getOp() instanceof Local) {
                TypeNode op = this.hierarchy.typeNode(((Local) ce.getOp()).getType());
                try {
                    if (cast.isClassOrInterface() || op.isClassOrInterface()) {
                        cast.lca(op);
                    }
                } catch (TypeException e3) {
                    logger.debug(r + "[" + op + "<->" + cast + "]");
                    error(e3.getMessage());
                }
            }
            if (!left.hasDescendantOrSelf(cast)) {
                error("Type Error(30)");
            }
        } else if (r instanceof InstanceOfExpr) {
            InstanceOfExpr ioe = (InstanceOfExpr) r;
            TypeNode type = this.hierarchy.typeNode(ioe.getCheckType());
            TypeNode op2 = this.hierarchy.typeNode(ioe.getOp().getType());
            try {
                op2.lca(type);
            } catch (TypeException e4) {
                logger.debug(r + "[" + op2 + "<->" + type + "]");
                error(e4.getMessage());
            }
            if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(IntType.v()))) {
                error("Type Error(31)");
            }
        } else if (r instanceof InvokeExpr) {
            InvokeExpr ie = (InvokeExpr) r;
            handleInvokeExpr(ie, stmt);
            if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(ie.getMethodRef().returnType()))) {
                error("Type Error(32)");
            }
        } else if (r instanceof NewArrayExpr) {
            NewArrayExpr nae = (NewArrayExpr) r;
            Type baseType = nae.getBaseType();
            if (baseType instanceof ArrayType) {
                right2 = this.hierarchy.typeNode(ArrayType.v(((ArrayType) baseType).baseType, ((ArrayType) baseType).numDimensions + 1));
            } else {
                right2 = this.hierarchy.typeNode(ArrayType.v(baseType, 1));
            }
            if (!left.hasDescendantOrSelf(right2)) {
                error("Type Error(33)");
            }
            Value size = nae.getSize();
            if (size instanceof Local) {
                TypeNode var = this.hierarchy.typeNode(((Local) size).getType());
                if (!var.hasAncestorOrSelf(this.hierarchy.typeNode(IntType.v()))) {
                    error("Type Error(34)");
                }
            }
        } else if (r instanceof NewExpr) {
            if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(((NewExpr) r).getBaseType()))) {
                error("Type Error(35)");
            }
        } else if (r instanceof NewMultiArrayExpr) {
            NewMultiArrayExpr nmae = (NewMultiArrayExpr) r;
            if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(nmae.getBaseType()))) {
                error("Type Error(36)");
            }
            for (int i = 0; i < nmae.getSizeCount(); i++) {
                Value size2 = nmae.getSize(i);
                if (size2 instanceof Local) {
                    TypeNode var2 = this.hierarchy.typeNode(((Local) size2).getType());
                    if (!var2.hasAncestorOrSelf(this.hierarchy.typeNode(IntType.v()))) {
                        error("Type Error(37)");
                    }
                }
            }
        } else if (r instanceof LengthExpr) {
            LengthExpr le = (LengthExpr) r;
            if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(IntType.v()))) {
                error("Type Error(38)");
            }
            if ((le.getOp() instanceof Local) && !this.hierarchy.typeNode(((Local) le.getOp()).getType()).isArray()) {
                error("Type Error(39)");
            }
        } else if (r instanceof NegExpr) {
            NegExpr ne = (NegExpr) r;
            if (ne.getOp() instanceof Local) {
                right = this.hierarchy.typeNode(((Local) ne.getOp()).getType());
            } else if (ne.getOp() instanceof DoubleConstant) {
                right = this.hierarchy.typeNode(DoubleType.v());
            } else if (ne.getOp() instanceof FloatConstant) {
                right = this.hierarchy.typeNode(FloatType.v());
            } else if (ne.getOp() instanceof IntConstant) {
                right = this.hierarchy.typeNode(IntType.v());
            } else if (ne.getOp() instanceof LongConstant) {
                right = this.hierarchy.typeNode(LongType.v());
            } else {
                throw new RuntimeException("Unhandled neg expression operand type: " + ne.getOp().getClass());
            }
            if (!left.hasDescendantOrSelf(right)) {
                error("Type Error(40)");
            }
        } else if (r instanceof Local) {
            if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(((Local) r).getType()))) {
                if (this.fix) {
                    stmt.setRightOp(insertCast((Local) r, left.type(), stmt));
                } else {
                    error("Type Error(41)");
                }
            }
        } else if (r instanceof InstanceFieldRef) {
            InstanceFieldRef ref4 = (InstanceFieldRef) r;
            if (!this.hierarchy.typeNode(((Local) ref4.getBase()).getType()).hasAncestorOrSelf(this.hierarchy.typeNode(ref4.getField().getDeclaringClass().getType()))) {
                if (this.fix) {
                    ref4.setBase(insertCast((Local) ref4.getBase(), ref4.getField().getDeclaringClass().getType(), stmt));
                } else {
                    error("Type Error(42)");
                }
            }
            if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(ref4.getField().getType()))) {
                error("Type Error(43)");
            }
        } else {
            if (r instanceof StaticFieldRef) {
                if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(((StaticFieldRef) r).getField().getType()))) {
                    error("Type Error(44)");
                    return;
                }
                return;
            }
            throw new RuntimeException("Unhandled assignment right hand side type: " + r.getClass());
        }
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseIdentityStmt(IdentityStmt stmt) {
        TypeNode left = this.hierarchy.typeNode(((Local) stmt.getLeftOp()).getType());
        Value r = stmt.getRightOp();
        if (!(r instanceof CaughtExceptionRef)) {
            TypeNode right = this.hierarchy.typeNode(r.getType());
            if (!left.hasDescendantOrSelf(right)) {
                error("Type Error(46) [" + left + " <- " + right + "]");
                return;
            }
            return;
        }
        List<RefType> exceptionTypes = TrapManager.getExceptionTypesOf(stmt, this.stmtBody);
        for (Type t : exceptionTypes) {
            if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(t))) {
                error("Type Error(47)");
            }
        }
        if (!left.hasAncestorOrSelf(this.hierarchy.typeNode(Scene.v().getBaseExceptionType()))) {
            error("Type Error(48)");
        }
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
        if (stmt.getOp() instanceof Local) {
            TypeNode op = this.hierarchy.typeNode(((Local) stmt.getOp()).getType());
            if (!op.hasAncestorOrSelf(this.hierarchy.typeNode(RefType.v(JavaBasicTypes.JAVA_LANG_OBJECT)))) {
                error("Type Error(49)");
            }
        }
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
        if (stmt.getOp() instanceof Local) {
            TypeNode op = this.hierarchy.typeNode(((Local) stmt.getOp()).getType());
            if (!op.hasAncestorOrSelf(this.hierarchy.typeNode(RefType.v(JavaBasicTypes.JAVA_LANG_OBJECT)))) {
                error("Type Error(49)");
            }
        }
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseGotoStmt(GotoStmt stmt) {
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseIfStmt(IfStmt stmt) {
        TypeNode lop;
        TypeNode rop;
        ConditionExpr cond = (ConditionExpr) stmt.getCondition();
        Value lv = cond.getOp1();
        Value rv = cond.getOp2();
        if (lv instanceof Local) {
            lop = this.hierarchy.typeNode(((Local) lv).getType());
        } else if (lv instanceof DoubleConstant) {
            lop = this.hierarchy.typeNode(DoubleType.v());
        } else if (lv instanceof FloatConstant) {
            lop = this.hierarchy.typeNode(FloatType.v());
        } else if (lv instanceof IntConstant) {
            lop = this.hierarchy.typeNode(IntType.v());
        } else if (lv instanceof LongConstant) {
            lop = this.hierarchy.typeNode(LongType.v());
        } else if (lv instanceof NullConstant) {
            lop = this.hierarchy.typeNode(NullType.v());
        } else if (lv instanceof StringConstant) {
            lop = this.hierarchy.typeNode(RefType.v("java.lang.String"));
        } else if (lv instanceof ClassConstant) {
            lop = this.hierarchy.typeNode(RefType.v("java.lang.Class"));
        } else {
            throw new RuntimeException("Unhandled binary expression left operand type: " + lv.getClass());
        }
        if (rv instanceof Local) {
            rop = this.hierarchy.typeNode(((Local) rv).getType());
        } else if (rv instanceof DoubleConstant) {
            rop = this.hierarchy.typeNode(DoubleType.v());
        } else if (rv instanceof FloatConstant) {
            rop = this.hierarchy.typeNode(FloatType.v());
        } else if (rv instanceof IntConstant) {
            rop = this.hierarchy.typeNode(IntType.v());
        } else if (rv instanceof LongConstant) {
            rop = this.hierarchy.typeNode(LongType.v());
        } else if (rv instanceof NullConstant) {
            rop = this.hierarchy.typeNode(NullType.v());
        } else if (rv instanceof StringConstant) {
            rop = this.hierarchy.typeNode(RefType.v("java.lang.String"));
        } else if (rv instanceof ClassConstant) {
            rop = this.hierarchy.typeNode(RefType.v("java.lang.Class"));
        } else {
            throw new RuntimeException("Unhandled binary expression right operand type: " + rv.getClass());
        }
        try {
            lop.lca(rop);
        } catch (TypeException e) {
            error(e.getMessage());
        }
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
        Value key = stmt.getKey();
        if ((key instanceof Local) && !this.hierarchy.typeNode(((Local) key).getType()).hasAncestorOrSelf(this.hierarchy.typeNode(IntType.v()))) {
            error("Type Error(50)");
        }
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseNopStmt(NopStmt stmt) {
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseReturnStmt(ReturnStmt stmt) {
        if ((stmt.getOp() instanceof Local) && !this.hierarchy.typeNode(((Local) stmt.getOp()).getType()).hasAncestorOrSelf(this.hierarchy.typeNode(this.stmtBody.getMethod().getReturnType()))) {
            if (this.fix) {
                stmt.setOp(insertCast((Local) stmt.getOp(), this.stmtBody.getMethod().getReturnType(), stmt));
            } else {
                error("Type Error(51)");
            }
        }
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseTableSwitchStmt(TableSwitchStmt stmt) {
        Value key = stmt.getKey();
        if ((key instanceof Local) && !this.hierarchy.typeNode(((Local) key).getType()).hasAncestorOrSelf(this.hierarchy.typeNode(IntType.v()))) {
            error("Type Error(52)");
        }
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseThrowStmt(ThrowStmt stmt) {
        if (stmt.getOp() instanceof Local) {
            TypeNode op = this.hierarchy.typeNode(((Local) stmt.getOp()).getType());
            if (!op.hasAncestorOrSelf(this.hierarchy.typeNode(Scene.v().getBaseExceptionType()))) {
                if (this.fix) {
                    stmt.setOp(insertCast((Local) stmt.getOp(), Scene.v().getBaseExceptionType(), stmt));
                } else {
                    error("Type Error(53)");
                }
            }
        }
    }

    public void defaultCase(Stmt stmt) {
        throw new RuntimeException("Unhandled statement type: " + stmt.getClass());
    }

    private Local insertCast(Local oldlocal, Type type, Stmt stmt) {
        Local newlocal = Jimple.v().newLocal("tmp", type);
        this.stmtBody.getLocals().add(newlocal);
        Unit u = Util.findFirstNonIdentityUnit(this.stmtBody, stmt);
        this.stmtBody.getUnits().insertBefore(Jimple.v().newAssignStmt(newlocal, Jimple.v().newCastExpr(oldlocal, type)), (AssignStmt) u);
        return newlocal;
    }
}
