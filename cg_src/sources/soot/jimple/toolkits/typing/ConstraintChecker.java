package soot.jimple.toolkits.typing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.JavaBasicTypes;
import soot.Local;
import soot.LocalGenerator;
import soot.LongType;
import soot.NullType;
import soot.RefType;
import soot.Scene;
import soot.SootMethodRef;
import soot.TrapManager;
import soot.Type;
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
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/ConstraintChecker.class */
public class ConstraintChecker extends AbstractStmtSwitch {
    private static final Logger logger = LoggerFactory.getLogger(ConstraintChecker.class);
    private final ClassHierarchy hierarchy;
    private final boolean fix;
    private JimpleBody stmtBody;
    private LocalGenerator localGenerator;

    public ConstraintChecker(TypeResolver resolver, boolean fix) {
        this.fix = fix;
        this.hierarchy = resolver.hierarchy();
    }

    public void check(Stmt stmt, JimpleBody stmtBody) throws TypeException {
        try {
            this.stmtBody = stmtBody;
            this.localGenerator = Scene.v().createLocalGenerator(stmtBody);
            stmt.apply(this);
        } catch (RuntimeTypeException e) {
            logger.error(e.getMessage(), (Throwable) e);
            throw new TypeException(e.getMessage(), e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/ConstraintChecker$RuntimeTypeException.class */
    public static class RuntimeTypeException extends RuntimeException {
        RuntimeTypeException(String message) {
            super(message);
        }
    }

    static void error(String message) {
        throw new RuntimeTypeException(message);
    }

    private void handleInvokeExpr(InvokeExpr ie, Stmt invokestmt) {
        SootMethodRef method = ie.getMethodRef();
        for (int i = 0; i < ie.getArgCount(); i++) {
            Value arg = ie.getArg(i);
            if (arg instanceof Local) {
                Local local = (Local) arg;
                Type parameterType = method.getParameterType(i);
                if (!this.hierarchy.typeNode(local.getType()).hasAncestorOrSelf(this.hierarchy.typeNode(parameterType))) {
                    if (this.fix) {
                        ie.setArg(i, insertCast(local, parameterType, invokestmt));
                    } else {
                        error("Type Error");
                    }
                }
            }
        }
        if (ie instanceof InterfaceInvokeExpr) {
            InterfaceInvokeExpr invoke = (InterfaceInvokeExpr) ie;
            Value base = invoke.getBase();
            if (base instanceof Local) {
                Local local2 = (Local) base;
                RefType classType = method.getDeclaringClass().getType();
                if (!this.hierarchy.typeNode(local2.getType()).hasAncestorOrSelf(this.hierarchy.typeNode(classType))) {
                    if (this.fix) {
                        invoke.setBase(insertCast(local2, classType, invokestmt));
                    } else {
                        error("Type Error(7): local " + local2 + " is of incompatible type " + local2.getType());
                    }
                }
            }
        } else if (ie instanceof SpecialInvokeExpr) {
            SpecialInvokeExpr invoke2 = (SpecialInvokeExpr) ie;
            Value base2 = invoke2.getBase();
            if (base2 instanceof Local) {
                Local local3 = (Local) base2;
                RefType classType2 = method.getDeclaringClass().getType();
                if (!this.hierarchy.typeNode(local3.getType()).hasAncestorOrSelf(this.hierarchy.typeNode(classType2))) {
                    if (this.fix) {
                        invoke2.setBase(insertCast(local3, classType2, invokestmt));
                    } else {
                        error("Type Error(9)");
                    }
                }
            }
        } else if (ie instanceof VirtualInvokeExpr) {
            VirtualInvokeExpr invoke3 = (VirtualInvokeExpr) ie;
            Value base3 = invoke3.getBase();
            if (base3 instanceof Local) {
                Local local4 = (Local) base3;
                RefType classType3 = method.getDeclaringClass().getType();
                if (!this.hierarchy.typeNode(local4.getType()).hasAncestorOrSelf(this.hierarchy.typeNode(classType3))) {
                    if (this.fix) {
                        invoke3.setBase(insertCast(local4, classType3, invokestmt));
                    } else {
                        error("Type Error(13)");
                    }
                }
            }
        } else if (!(ie instanceof StaticInvokeExpr)) {
            if (ie instanceof DynamicInvokeExpr) {
                DynamicInvokeExpr die = (DynamicInvokeExpr) ie;
                SootMethodRef bootstrapMethod = die.getMethodRef();
                for (int i2 = 0; i2 < die.getBootstrapArgCount(); i2++) {
                    if (die.getBootstrapArg(i2) instanceof Local) {
                        Local local5 = (Local) die.getBootstrapArg(i2);
                        Type parameterType2 = bootstrapMethod.getParameterType(i2);
                        if (!this.hierarchy.typeNode(local5.getType()).hasAncestorOrSelf(this.hierarchy.typeNode(parameterType2))) {
                            if (this.fix) {
                                ie.setArg(i2, insertCast(local5, parameterType2, invokestmt));
                            } else {
                                error("Type Error");
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
            Local base2 = (Local) ref2.getBase();
            RefType classTy = ref2.getField().getDeclaringClass().getType();
            if (!this.hierarchy.typeNode(base2.getType()).hasAncestorOrSelf(this.hierarchy.typeNode(classTy))) {
                if (this.fix) {
                    ref2.setBase(insertCast(base2, classTy, stmt));
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
            Local base3 = (Local) ref3.getBase();
            TypeNode baseTy = this.hierarchy.typeNode(base3.getType());
            if (!baseTy.isArray()) {
                error("Type Error(19): " + baseTy + " is not an array type");
            }
            if (baseTy == this.hierarchy.NULL) {
                return;
            }
            if (!left.hasDescendantOrSelf(baseTy.element())) {
                if (this.fix) {
                    Type lefttype = left.type();
                    if (lefttype instanceof ArrayType) {
                        ArrayType atype = (ArrayType) lefttype;
                        ref3.setBase(insertCast(base3, ArrayType.v(atype.baseType, atype.numDimensions + 1), stmt));
                    } else {
                        ref3.setBase(insertCast(base3, ArrayType.v(lefttype, 1), stmt));
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
            TypeNode castTy = this.hierarchy.typeNode(ce.getCastType());
            Value op = ce.getOp();
            if (op instanceof Local) {
                TypeNode opTy = this.hierarchy.typeNode(((Local) op).getType());
                try {
                    if (castTy.isClassOrInterface() || opTy.isClassOrInterface()) {
                        castTy.lca(opTy);
                    }
                } catch (TypeException e3) {
                    logger.debug(r + "[" + opTy + "<->" + castTy + "]");
                    error(e3.getMessage());
                }
            }
            if (!left.hasDescendantOrSelf(castTy)) {
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
            if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(ie.getMethodRef().getReturnType()))) {
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
            NewExpr ne = (NewExpr) r;
            if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(ne.getBaseType()))) {
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
            Value op3 = le.getOp();
            if ((op3 instanceof Local) && !this.hierarchy.typeNode(((Local) op3).getType()).isArray()) {
                error("Type Error(39)");
            }
        } else if (r instanceof NegExpr) {
            NegExpr ne2 = (NegExpr) r;
            Value op4 = ne2.getOp();
            if (op4 instanceof Local) {
                right = this.hierarchy.typeNode(((Local) op4).getType());
            } else if (op4 instanceof DoubleConstant) {
                right = this.hierarchy.typeNode(DoubleType.v());
            } else if (op4 instanceof FloatConstant) {
                right = this.hierarchy.typeNode(FloatType.v());
            } else if (op4 instanceof IntConstant) {
                right = this.hierarchy.typeNode(IntType.v());
            } else if (op4 instanceof LongConstant) {
                right = this.hierarchy.typeNode(LongType.v());
            } else {
                throw new RuntimeException("Unhandled neg expression operand type: " + op4.getClass());
            }
            if (!left.hasDescendantOrSelf(right)) {
                error("Type Error(40)");
            }
        } else if (r instanceof Local) {
            Local loc = (Local) r;
            if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(loc.getType()))) {
                if (this.fix) {
                    stmt.setRightOp(insertCast(loc, left.type(), stmt));
                } else {
                    error("Type Error(41)");
                }
            }
        } else if (r instanceof InstanceFieldRef) {
            InstanceFieldRef ref4 = (InstanceFieldRef) r;
            Local base4 = (Local) ref4.getBase();
            RefType classTy2 = ref4.getField().getDeclaringClass().getType();
            if (!this.hierarchy.typeNode(base4.getType()).hasAncestorOrSelf(this.hierarchy.typeNode(classTy2))) {
                if (this.fix) {
                    ref4.setBase(insertCast(base4, classTy2, stmt));
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
        if (r instanceof CaughtExceptionRef) {
            for (Type t : TrapManager.getExceptionTypesOf(stmt, this.stmtBody)) {
                if (!left.hasDescendantOrSelf(this.hierarchy.typeNode(t))) {
                    error("Type Error(47)");
                }
            }
            if (!left.hasAncestorOrSelf(this.hierarchy.typeNode(Scene.v().getBaseExceptionType()))) {
                error("Type Error(48)");
                return;
            }
            return;
        }
        TypeNode right = this.hierarchy.typeNode(r.getType());
        if (!left.hasDescendantOrSelf(right)) {
            error("Type Error(46) [" + left + " <- " + right + "]");
        }
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
        Value op = stmt.getOp();
        if (op instanceof Local) {
            TypeNode opTy = this.hierarchy.typeNode(((Local) op).getType());
            if (!opTy.hasAncestorOrSelf(this.hierarchy.typeNode(RefType.v(JavaBasicTypes.JAVA_LANG_OBJECT)))) {
                error("Type Error(49)");
            }
        }
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
        Value op = stmt.getOp();
        if (op instanceof Local) {
            TypeNode opTy = this.hierarchy.typeNode(((Local) op).getType());
            if (!opTy.hasAncestorOrSelf(this.hierarchy.typeNode(RefType.v(JavaBasicTypes.JAVA_LANG_OBJECT)))) {
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
        ConditionExpr expr = (ConditionExpr) stmt.getCondition();
        Value lv = expr.getOp1();
        Value rv = expr.getOp2();
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
        Value op = stmt.getOp();
        if (op instanceof Local) {
            Local opLocal = (Local) op;
            Type returnType = this.stmtBody.getMethod().getReturnType();
            if (!this.hierarchy.typeNode(opLocal.getType()).hasAncestorOrSelf(this.hierarchy.typeNode(returnType))) {
                if (this.fix) {
                    stmt.setOp(insertCast(opLocal, returnType, stmt));
                } else {
                    error("Type Error(51)");
                }
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
        Value op = stmt.getOp();
        if (op instanceof Local) {
            Local opLocal = (Local) op;
            TypeNode opTy = this.hierarchy.typeNode(opLocal.getType());
            if (!opTy.hasAncestorOrSelf(this.hierarchy.typeNode(Scene.v().getBaseExceptionType()))) {
                if (this.fix) {
                    stmt.setOp(insertCast(opLocal, Scene.v().getBaseExceptionType(), stmt));
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
        Jimple jimp = Jimple.v();
        Local newlocal = this.localGenerator.generateLocal(type);
        this.stmtBody.getUnits().insertBefore(jimp.newAssignStmt(newlocal, jimp.newCastExpr(oldlocal, type)), (AssignStmt) Util.findFirstNonIdentityUnit(this.stmtBody, stmt));
        return newlocal;
    }
}
