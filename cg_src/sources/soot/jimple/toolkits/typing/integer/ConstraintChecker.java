package soot.jimple.toolkits.typing.integer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.BooleanType;
import soot.ByteType;
import soot.IntType;
import soot.IntegerType;
import soot.Local;
import soot.LocalGenerator;
import soot.NullType;
import soot.Scene;
import soot.ShortType;
import soot.SootMethodRef;
import soot.Type;
import soot.Unit;
import soot.UnitPatchingChain;
import soot.Value;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.AddExpr;
import soot.jimple.AndExpr;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.BreakpointStmt;
import soot.jimple.CastExpr;
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
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.SubExpr;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThrowStmt;
import soot.jimple.UshrExpr;
import soot.jimple.XorExpr;
import soot.jimple.toolkits.typing.Util;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/integer/ConstraintChecker.class */
public class ConstraintChecker extends AbstractStmtSwitch {
    private static final Logger logger = LoggerFactory.getLogger(ConstraintChecker.class);
    private final TypeResolver resolver;
    private final boolean fix;
    private JimpleBody stmtBody;
    private LocalGenerator localGenerator;

    public ConstraintChecker(TypeResolver resolver, boolean fix) {
        this.resolver = resolver;
        this.fix = fix;
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
    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/integer/ConstraintChecker$RuntimeTypeException.class */
    public static class RuntimeTypeException extends RuntimeException {
        RuntimeTypeException(String message) {
            super(message);
        }
    }

    static void error(String message) {
        throw new RuntimeTypeException(message);
    }

    private void handleInvokeExpr(InvokeExpr ie, Stmt invokestmt) {
        ClassHierarchy classHierarchy = ClassHierarchy.v();
        SootMethodRef method = ie.getMethodRef();
        int e = ie.getArgCount();
        for (int i = 0; i < e; i++) {
            Value currArg = ie.getArg(i);
            if (currArg instanceof Local) {
                Local local = (Local) currArg;
                Type localType = local.getType();
                if (localType instanceof IntegerType) {
                    Type currParamType = method.getParameterType(i);
                    if (!classHierarchy.typeNode(localType).hasAncestor_1(classHierarchy.typeNode(currParamType))) {
                        if (this.fix) {
                            ie.setArg(i, insertCast(local, currParamType, invokestmt));
                        } else {
                            error("Type Error");
                        }
                    }
                }
            }
        }
        if (ie instanceof DynamicInvokeExpr) {
            DynamicInvokeExpr die = (DynamicInvokeExpr) ie;
            SootMethodRef bootstrapMethod = die.getBootstrapMethodRef();
            int e2 = die.getBootstrapArgCount();
            for (int i2 = 0; i2 < e2; i2++) {
                Value currBootstrapArg = die.getBootstrapArg(i2);
                if (currBootstrapArg instanceof Local) {
                    Local local2 = (Local) currBootstrapArg;
                    Type localType2 = local2.getType();
                    if (localType2 instanceof IntegerType) {
                        Type currParamType2 = bootstrapMethod.getParameterType(i2);
                        if (!classHierarchy.typeNode(localType2).hasAncestor_1(classHierarchy.typeNode(currParamType2))) {
                            if (this.fix) {
                                die.setArg(i2, insertCast(local2, currParamType2, invokestmt));
                            } else {
                                error("Type Error");
                            }
                        }
                    }
                }
            }
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
        ClassHierarchy classHierarchy = ClassHierarchy.v();
        Value l = stmt.getLeftOp();
        Value r = stmt.getRightOp();
        TypeNode left = null;
        TypeNode right = null;
        if (l instanceof ArrayRef) {
            ArrayRef ref = (ArrayRef) l;
            Type baset = ((Local) ref.getBase()).getType();
            if (baset instanceof ArrayType) {
                ArrayType base = (ArrayType) baset;
                Value index = ref.getIndex();
                if (base.numDimensions == 1 && (base.baseType instanceof IntegerType)) {
                    left = classHierarchy.typeNode(base.baseType);
                }
                if ((index instanceof Local) && !classHierarchy.typeNode(((Local) index).getType()).hasAncestor_1(classHierarchy.INT)) {
                    if (this.fix) {
                        ref.setIndex(insertCast((Local) index, IntType.v(), stmt));
                    } else {
                        error("Type Error(5)");
                    }
                }
            }
        } else if (l instanceof Local) {
            Type ty = ((Local) l).getType();
            if (ty instanceof IntegerType) {
                left = classHierarchy.typeNode(ty);
            }
        } else if (l instanceof InstanceFieldRef) {
            Type ty2 = ((InstanceFieldRef) l).getFieldRef().type();
            if (ty2 instanceof IntegerType) {
                left = classHierarchy.typeNode(ty2);
            }
        } else if (l instanceof StaticFieldRef) {
            Type ty3 = ((StaticFieldRef) l).getFieldRef().type();
            if (ty3 instanceof IntegerType) {
                left = classHierarchy.typeNode(ty3);
            }
        } else {
            throw new RuntimeException("Unhandled assignment left hand side type: " + l.getClass());
        }
        if (r instanceof ArrayRef) {
            ArrayRef ref2 = (ArrayRef) r;
            Type baset2 = ((Local) ref2.getBase()).getType();
            if (!(baset2 instanceof NullType)) {
                ArrayType base2 = (ArrayType) baset2;
                Value index2 = ref2.getIndex();
                if (base2.numDimensions == 1 && (base2.baseType instanceof IntegerType)) {
                    right = classHierarchy.typeNode(base2.baseType);
                }
                if ((index2 instanceof Local) && !classHierarchy.typeNode(((Local) index2).getType()).hasAncestor_1(classHierarchy.INT)) {
                    if (this.fix) {
                        ref2.setIndex(insertCast((Local) index2, IntType.v(), stmt));
                    } else {
                        error("Type Error(6)");
                    }
                }
            }
        } else if (!(r instanceof DoubleConstant) && !(r instanceof FloatConstant)) {
            if (r instanceof IntConstant) {
                int value = ((IntConstant) r).value;
                right = value < -32768 ? classHierarchy.INT : value < -128 ? classHierarchy.SHORT : value < 0 ? classHierarchy.BYTE : value < 2 ? classHierarchy.R0_1 : value < 128 ? classHierarchy.R0_127 : value < 32768 ? classHierarchy.R0_32767 : value < 65536 ? classHierarchy.CHAR : classHierarchy.INT;
            } else if (!(r instanceof LongConstant) && !(r instanceof NullConstant) && !(r instanceof StringConstant) && !(r instanceof ClassConstant)) {
                if (r instanceof BinopExpr) {
                    BinopExpr be = (BinopExpr) r;
                    Value lv = be.getOp1();
                    Value rv = be.getOp2();
                    TypeNode lop = null;
                    TypeNode rop = null;
                    if (lv instanceof Local) {
                        if (((Local) lv).getType() instanceof IntegerType) {
                            lop = classHierarchy.typeNode(((Local) lv).getType());
                        }
                    } else if (!(lv instanceof DoubleConstant) && !(lv instanceof FloatConstant)) {
                        if (lv instanceof IntConstant) {
                            int value2 = ((IntConstant) lv).value;
                            lop = value2 < -32768 ? classHierarchy.INT : value2 < -128 ? classHierarchy.SHORT : value2 < 0 ? classHierarchy.BYTE : value2 < 2 ? classHierarchy.R0_1 : value2 < 128 ? classHierarchy.R0_127 : value2 < 32768 ? classHierarchy.R0_32767 : value2 < 65536 ? classHierarchy.CHAR : classHierarchy.INT;
                        } else if (!(lv instanceof LongConstant) && !(lv instanceof NullConstant) && !(lv instanceof StringConstant) && !(lv instanceof ClassConstant)) {
                            throw new RuntimeException("Unhandled binary expression left operand type: " + lv.getClass());
                        }
                    }
                    if (rv instanceof Local) {
                        if (((Local) rv).getType() instanceof IntegerType) {
                            rop = classHierarchy.typeNode(((Local) rv).getType());
                        }
                    } else if (!(rv instanceof DoubleConstant) && !(rv instanceof FloatConstant)) {
                        if (rv instanceof IntConstant) {
                            int value3 = ((IntConstant) rv).value;
                            rop = value3 < -32768 ? classHierarchy.INT : value3 < -128 ? classHierarchy.SHORT : value3 < 0 ? classHierarchy.BYTE : value3 < 2 ? classHierarchy.R0_1 : value3 < 128 ? classHierarchy.R0_127 : value3 < 32768 ? classHierarchy.R0_32767 : value3 < 65536 ? classHierarchy.CHAR : classHierarchy.INT;
                        } else if (!(rv instanceof LongConstant) && !(rv instanceof NullConstant) && !(rv instanceof StringConstant) && !(rv instanceof ClassConstant)) {
                            throw new RuntimeException("Unhandled binary expression right operand type: " + rv.getClass());
                        }
                    }
                    if ((be instanceof AddExpr) || (be instanceof SubExpr) || (be instanceof MulExpr) || (be instanceof DivExpr) || (be instanceof RemExpr)) {
                        if (lop != null && rop != null) {
                            if (!lop.hasAncestor_1(classHierarchy.INT)) {
                                if (this.fix) {
                                    be.setOp1(insertCast(be.getOp1(), getTypeForCast(lop), IntType.v(), stmt));
                                } else {
                                    error("Type Error(7)");
                                }
                            }
                            if (!rop.hasAncestor_1(classHierarchy.INT)) {
                                if (this.fix) {
                                    be.setOp2(insertCast(be.getOp2(), getTypeForCast(rop), IntType.v(), stmt));
                                } else {
                                    error("Type Error(8)");
                                }
                            }
                        }
                        right = classHierarchy.INT;
                    } else if ((be instanceof AndExpr) || (be instanceof OrExpr) || (be instanceof XorExpr)) {
                        if (lop != null && rop != null) {
                            TypeNode lca = lop.lca_1(rop);
                            if (lca == classHierarchy.TOP) {
                                if (this.fix) {
                                    if (!lop.hasAncestor_1(classHierarchy.INT)) {
                                        be.setOp1(insertCast(be.getOp1(), getTypeForCast(lop), getTypeForCast(rop), stmt));
                                        lca = rop;
                                    }
                                    if (!rop.hasAncestor_1(classHierarchy.INT)) {
                                        be.setOp2(insertCast(be.getOp2(), getTypeForCast(rop), getTypeForCast(lop), stmt));
                                        lca = lop;
                                    }
                                } else {
                                    error("Type Error(11)");
                                }
                            }
                            right = lca;
                        }
                    } else if (be instanceof ShlExpr) {
                        if (lop != null && !lop.hasAncestor_1(classHierarchy.INT)) {
                            if (this.fix) {
                                be.setOp1(insertCast(be.getOp1(), getTypeForCast(lop), IntType.v(), stmt));
                            } else {
                                error("Type Error(9)");
                            }
                        }
                        if (!rop.hasAncestor_1(classHierarchy.INT)) {
                            if (this.fix) {
                                be.setOp2(insertCast(be.getOp2(), getTypeForCast(rop), IntType.v(), stmt));
                            } else {
                                error("Type Error(10)");
                            }
                        }
                        right = lop == null ? null : classHierarchy.INT;
                    } else if ((be instanceof ShrExpr) || (be instanceof UshrExpr)) {
                        if (lop != null && !lop.hasAncestor_1(classHierarchy.INT)) {
                            if (this.fix) {
                                be.setOp1(insertCast(be.getOp1(), getTypeForCast(lop), ByteType.v(), stmt));
                                lop = classHierarchy.BYTE;
                            } else {
                                error("Type Error(9)");
                            }
                        }
                        if (!rop.hasAncestor_1(classHierarchy.INT)) {
                            if (this.fix) {
                                be.setOp2(insertCast(be.getOp2(), getTypeForCast(rop), IntType.v(), stmt));
                            } else {
                                error("Type Error(10)");
                            }
                        }
                        right = lop;
                    } else if ((be instanceof CmpExpr) || (be instanceof CmpgExpr) || (be instanceof CmplExpr)) {
                        right = classHierarchy.BYTE;
                    } else if ((be instanceof EqExpr) || (be instanceof GeExpr) || (be instanceof GtExpr) || (be instanceof LeExpr) || (be instanceof LtExpr) || (be instanceof NeExpr)) {
                        if (rop != null && lop.lca_1(rop) == classHierarchy.TOP) {
                            if (this.fix) {
                                if (!lop.hasAncestor_1(classHierarchy.INT)) {
                                    be.setOp1(insertCast(be.getOp1(), getTypeForCast(lop), getTypeForCast(rop), stmt));
                                }
                                if (!rop.hasAncestor_1(classHierarchy.INT)) {
                                    be.setOp2(insertCast(be.getOp2(), getTypeForCast(rop), getTypeForCast(lop), stmt));
                                }
                            } else {
                                error("Type Error(11)");
                            }
                        }
                        right = classHierarchy.BOOLEAN;
                    } else {
                        throw new RuntimeException("Unhandled binary expression type: " + be.getClass());
                    }
                } else if (r instanceof CastExpr) {
                    Type ty4 = ((CastExpr) r).getCastType();
                    if (ty4 instanceof IntegerType) {
                        right = classHierarchy.typeNode(ty4);
                    }
                } else if (r instanceof InstanceOfExpr) {
                    right = classHierarchy.BOOLEAN;
                } else if (r instanceof InvokeExpr) {
                    InvokeExpr ie = (InvokeExpr) r;
                    handleInvokeExpr(ie, stmt);
                    Type retTy = ie.getMethodRef().getReturnType();
                    if (retTy instanceof IntegerType) {
                        right = classHierarchy.typeNode(retTy);
                    }
                } else if (r instanceof NewArrayExpr) {
                    NewArrayExpr nae = (NewArrayExpr) r;
                    Value size = nae.getSize();
                    if ((size instanceof Local) && !classHierarchy.typeNode(((Local) size).getType()).hasAncestor_1(classHierarchy.INT)) {
                        if (this.fix) {
                            nae.setSize(insertCast((Local) size, IntType.v(), stmt));
                        } else {
                            error("Type Error(12)");
                        }
                    }
                } else if (!(r instanceof NewExpr)) {
                    if (r instanceof NewMultiArrayExpr) {
                        NewMultiArrayExpr nmae = (NewMultiArrayExpr) r;
                        for (int i = 0; i < nmae.getSizeCount(); i++) {
                            Value size2 = nmae.getSize(i);
                            if ((size2 instanceof Local) && !classHierarchy.typeNode(((Local) size2).getType()).hasAncestor_1(classHierarchy.INT)) {
                                if (this.fix) {
                                    nmae.setSize(i, insertCast((Local) size2, IntType.v(), stmt));
                                } else {
                                    error("Type Error(13)");
                                }
                            }
                        }
                    } else if (r instanceof LengthExpr) {
                        right = classHierarchy.INT;
                    } else if (r instanceof NegExpr) {
                        NegExpr ne = (NegExpr) r;
                        Value op = ne.getOp();
                        if (op instanceof Local) {
                            Local local = (Local) op;
                            if (local.getType() instanceof IntegerType) {
                                TypeNode ltype = classHierarchy.typeNode(local.getType());
                                if (!ltype.hasAncestor_1(classHierarchy.INT)) {
                                    if (this.fix) {
                                        ne.setOp(insertCast(local, IntType.v(), stmt));
                                        ltype = classHierarchy.BYTE;
                                    } else {
                                        error("Type Error(14)");
                                    }
                                }
                                right = ltype == classHierarchy.CHAR ? classHierarchy.INT : ltype;
                            }
                        } else if (!(op instanceof DoubleConstant) && !(op instanceof FloatConstant)) {
                            if (op instanceof IntConstant) {
                                right = classHierarchy.INT;
                            } else if (!(op instanceof LongConstant)) {
                                throw new RuntimeException("Unhandled neg expression operand type: " + op.getClass());
                            }
                        }
                    } else if (r instanceof Local) {
                        Type ty5 = ((Local) r).getType();
                        if (ty5 instanceof IntegerType) {
                            right = classHierarchy.typeNode(ty5);
                        }
                    } else if (r instanceof InstanceFieldRef) {
                        Type ty6 = ((InstanceFieldRef) r).getFieldRef().type();
                        if (ty6 instanceof IntegerType) {
                            right = classHierarchy.typeNode(ty6);
                        }
                    } else if (r instanceof StaticFieldRef) {
                        Type ty7 = ((StaticFieldRef) r).getFieldRef().type();
                        if (ty7 instanceof IntegerType) {
                            right = classHierarchy.typeNode(ty7);
                        }
                    } else {
                        throw new RuntimeException("Unhandled assignment right hand side type: " + r.getClass());
                    }
                }
            }
        }
        if (left != null && right != null && !right.hasAncestor_1(left)) {
            if (this.fix) {
                stmt.setRightOp(insertCast(stmt.getRightOp(), getTypeForCast(right), getTypeForCast(left), stmt));
            } else {
                error("Type Error(15)");
            }
        }
    }

    static Type getTypeForCast(TypeNode node) {
        if (node.type() == null) {
            if (node == ClassHierarchy.v().R0_1) {
                return BooleanType.v();
            }
            if (node == ClassHierarchy.v().R0_127) {
                return ByteType.v();
            }
            if (node == ClassHierarchy.v().R0_32767) {
                return ShortType.v();
            }
        }
        return node.type();
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseIdentityStmt(IdentityStmt stmt) {
        Value l = stmt.getLeftOp();
        Value r = stmt.getRightOp();
        if (l instanceof Local) {
            Type locType = ((Local) l).getType();
            if (locType instanceof IntegerType) {
                TypeNode left = ClassHierarchy.v().typeNode(locType);
                TypeNode right = ClassHierarchy.v().typeNode(r.getType());
                if (!right.hasAncestor_1(left)) {
                    if (this.fix) {
                        stmt.setLeftOp(insertCastAfter((Local) l, getTypeForCast(left), getTypeForCast(right), stmt));
                    } else {
                        error("Type Error(16)");
                    }
                }
            }
        }
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseGotoStmt(GotoStmt stmt) {
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseIfStmt(IfStmt stmt) {
        ConditionExpr cond = (ConditionExpr) stmt.getCondition();
        Value lv = cond.getOp1();
        Value rv = cond.getOp2();
        TypeNode lop = null;
        TypeNode rop = null;
        if (lv instanceof Local) {
            Type ty = ((Local) lv).getType();
            if (ty instanceof IntegerType) {
                lop = ClassHierarchy.v().typeNode(ty);
            }
        } else if (!(lv instanceof DoubleConstant) && !(lv instanceof FloatConstant)) {
            if (lv instanceof IntConstant) {
                int value = ((IntConstant) lv).value;
                lop = value < -32768 ? ClassHierarchy.v().INT : value < -128 ? ClassHierarchy.v().SHORT : value < 0 ? ClassHierarchy.v().BYTE : value < 2 ? ClassHierarchy.v().R0_1 : value < 128 ? ClassHierarchy.v().R0_127 : value < 32768 ? ClassHierarchy.v().R0_32767 : value < 65536 ? ClassHierarchy.v().CHAR : ClassHierarchy.v().INT;
            } else if (!(lv instanceof LongConstant) && !(lv instanceof NullConstant) && !(lv instanceof StringConstant) && !(lv instanceof ClassConstant)) {
                throw new RuntimeException("Unhandled binary expression left operand type: " + lv.getClass());
            }
        }
        if (rv instanceof Local) {
            Type ty2 = ((Local) rv).getType();
            if (ty2 instanceof IntegerType) {
                rop = ClassHierarchy.v().typeNode(ty2);
            }
        } else if (!(rv instanceof DoubleConstant) && !(rv instanceof FloatConstant)) {
            if (rv instanceof IntConstant) {
                int value2 = ((IntConstant) rv).value;
                rop = value2 < -32768 ? ClassHierarchy.v().INT : value2 < -128 ? ClassHierarchy.v().SHORT : value2 < 0 ? ClassHierarchy.v().BYTE : value2 < 2 ? ClassHierarchy.v().R0_1 : value2 < 128 ? ClassHierarchy.v().R0_127 : value2 < 32768 ? ClassHierarchy.v().R0_32767 : value2 < 65536 ? ClassHierarchy.v().CHAR : ClassHierarchy.v().INT;
            } else if (!(rv instanceof LongConstant) && !(rv instanceof NullConstant) && !(rv instanceof StringConstant) && !(rv instanceof ClassConstant)) {
                throw new RuntimeException("Unhandled binary expression right operand type: " + rv.getClass());
            }
        }
        if (lop != null && rop != null && lop.lca_1(rop) == ClassHierarchy.v().TOP) {
            if (this.fix) {
                if (!lop.hasAncestor_1(ClassHierarchy.v().INT)) {
                    cond.setOp1(insertCast(cond.getOp1(), getTypeForCast(lop), getTypeForCast(rop), stmt));
                }
                if (!rop.hasAncestor_1(ClassHierarchy.v().INT)) {
                    cond.setOp2(insertCast(cond.getOp2(), getTypeForCast(rop), getTypeForCast(lop), stmt));
                    return;
                }
                return;
            }
            error("Type Error(17)");
        }
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
        Value key = stmt.getKey();
        if ((key instanceof Local) && !ClassHierarchy.v().typeNode(((Local) key).getType()).hasAncestor_1(ClassHierarchy.v().INT)) {
            if (this.fix) {
                stmt.setKey(insertCast((Local) key, IntType.v(), stmt));
            } else {
                error("Type Error(18)");
            }
        }
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseNopStmt(NopStmt stmt) {
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseReturnStmt(ReturnStmt stmt) {
        Value op = stmt.getOp();
        if (op instanceof Local) {
            Type opType = ((Local) op).getType();
            if (opType instanceof IntegerType) {
                Type returnType = this.stmtBody.getMethod().getReturnType();
                if (!ClassHierarchy.v().typeNode(opType).hasAncestor_1(ClassHierarchy.v().typeNode(returnType))) {
                    if (this.fix) {
                        stmt.setOp(insertCast((Local) op, returnType, stmt));
                    } else {
                        error("Type Error(19)");
                    }
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
        if (key instanceof Local) {
            Local keyLocal = (Local) key;
            if (!ClassHierarchy.v().typeNode(keyLocal.getType()).hasAncestor_1(ClassHierarchy.v().INT)) {
                if (this.fix) {
                    stmt.setKey(insertCast(keyLocal, IntType.v(), stmt));
                } else {
                    error("Type Error(20)");
                }
            }
            this.resolver.typeVariable(keyLocal).addParent(this.resolver.INT);
        }
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseThrowStmt(ThrowStmt stmt) {
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

    private Local insertCastAfter(Local leftlocal, Type lefttype, Type righttype, Stmt stmt) {
        Jimple jimp = Jimple.v();
        Local newlocal = this.localGenerator.generateLocal(righttype);
        this.stmtBody.getUnits().insertAfter(jimp.newAssignStmt(leftlocal, jimp.newCastExpr(newlocal, lefttype)), (AssignStmt) Util.findLastIdentityUnit(this.stmtBody, stmt));
        return newlocal;
    }

    private Local insertCast(Value oldvalue, Type oldtype, Type type, Stmt stmt) {
        Jimple jimp = Jimple.v();
        Local newlocal1 = this.localGenerator.generateLocal(oldtype);
        Local newlocal2 = this.localGenerator.generateLocal(type);
        Unit u = Util.findFirstNonIdentityUnit(this.stmtBody, stmt);
        UnitPatchingChain units = this.stmtBody.getUnits();
        units.insertBefore(jimp.newAssignStmt(newlocal1, oldvalue), (AssignStmt) u);
        units.insertBefore(jimp.newAssignStmt(newlocal2, jimp.newCastExpr(newlocal1, type)), (AssignStmt) u);
        return newlocal2;
    }
}
