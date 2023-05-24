package soot.jimple.toolkits.typing.integer;

import soot.ArrayType;
import soot.IntegerType;
import soot.Local;
import soot.NullType;
import soot.SootMethodRef;
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
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/integer/ConstraintCollector.class */
public class ConstraintCollector extends AbstractStmtSwitch {
    private final TypeResolver resolver;
    private final boolean uses;
    private JimpleBody stmtBody;

    public ConstraintCollector(TypeResolver resolver, boolean uses) {
        this.resolver = resolver;
        this.uses = uses;
    }

    public void collect(Stmt stmt, JimpleBody stmtBody) {
        this.stmtBody = stmtBody;
        stmt.apply(this);
    }

    private void handleInvokeExpr(InvokeExpr ie) {
        if (!this.uses) {
            return;
        }
        SootMethodRef method = ie.getMethodRef();
        for (int i = 0; i < ie.getArgCount(); i++) {
            Value arg = ie.getArg(i);
            if (arg instanceof Local) {
                Local local = (Local) arg;
                if (local.getType() instanceof IntegerType) {
                    TypeVariable localType = this.resolver.typeVariable(local);
                    localType.addParent(this.resolver.typeVariable(method.parameterType(i)));
                }
            }
        }
        if (ie instanceof DynamicInvokeExpr) {
            DynamicInvokeExpr die = (DynamicInvokeExpr) ie;
            SootMethodRef bootstrapMethod = die.getBootstrapMethodRef();
            for (int i2 = 0; i2 < die.getBootstrapArgCount(); i2++) {
                Value arg2 = die.getBootstrapArg(i2);
                if (arg2 instanceof Local) {
                    Local local2 = (Local) arg2;
                    if (local2.getType() instanceof IntegerType) {
                        TypeVariable localType2 = this.resolver.typeVariable(local2);
                        localType2.addParent(this.resolver.typeVariable(bootstrapMethod.parameterType(i2)));
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
        handleInvokeExpr(stmt.getInvokeExpr());
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseAssignStmt(AssignStmt stmt) {
        Value l = stmt.getLeftOp();
        Value r = stmt.getRightOp();
        TypeVariable left = null;
        TypeVariable right = null;
        if (l instanceof ArrayRef) {
            ArrayRef ref = (ArrayRef) l;
            Type baset = ((Local) ref.getBase()).getType();
            if (baset instanceof ArrayType) {
                ArrayType base = (ArrayType) baset;
                Value index = ref.getIndex();
                if (this.uses) {
                    if (base.numDimensions == 1 && (base.baseType instanceof IntegerType)) {
                        left = this.resolver.typeVariable(base.baseType);
                    }
                    if (index instanceof Local) {
                        this.resolver.typeVariable((Local) index).addParent(this.resolver.INT);
                    }
                }
            }
        } else if (l instanceof Local) {
            Local loc = (Local) l;
            if (loc.getType() instanceof IntegerType) {
                left = this.resolver.typeVariable(loc);
            }
        } else if (l instanceof InstanceFieldRef) {
            if (this.uses) {
                InstanceFieldRef ref2 = (InstanceFieldRef) l;
                Type fieldType = ref2.getFieldRef().type();
                if (fieldType instanceof IntegerType) {
                    left = this.resolver.typeVariable(fieldType);
                }
            }
        } else if (l instanceof StaticFieldRef) {
            if (this.uses) {
                StaticFieldRef ref3 = (StaticFieldRef) l;
                Type fieldType2 = ref3.getFieldRef().type();
                if (fieldType2 instanceof IntegerType) {
                    left = this.resolver.typeVariable(fieldType2);
                }
            }
        } else {
            throw new RuntimeException("Unhandled assignment left hand side type: " + l.getClass());
        }
        if (r instanceof ArrayRef) {
            ArrayRef ref4 = (ArrayRef) r;
            Type baset2 = ((Local) ref4.getBase()).getType();
            if (!(baset2 instanceof NullType)) {
                Value index2 = ref4.getIndex();
                if (baset2 instanceof ArrayType) {
                    ArrayType base2 = (ArrayType) baset2;
                    if (base2.numDimensions == 1 && (base2.baseType instanceof IntegerType)) {
                        right = this.resolver.typeVariable(base2.baseType);
                    }
                } else if (baset2 instanceof IntegerType) {
                    right = this.resolver.typeVariable(baset2);
                }
                if (this.uses && (index2 instanceof Local)) {
                    this.resolver.typeVariable((Local) index2).addParent(this.resolver.INT);
                }
            }
        } else if (!(r instanceof DoubleConstant) && !(r instanceof FloatConstant)) {
            if (r instanceof IntConstant) {
                int value = ((IntConstant) r).value;
                right = value < -32768 ? this.resolver.INT : value < -128 ? this.resolver.SHORT : value < 0 ? this.resolver.BYTE : value < 2 ? this.resolver.R0_1 : value < 128 ? this.resolver.R0_127 : value < 32768 ? this.resolver.R0_32767 : value < 65536 ? this.resolver.CHAR : this.resolver.INT;
            } else if (!(r instanceof LongConstant) && !(r instanceof NullConstant) && !(r instanceof StringConstant) && !(r instanceof ClassConstant)) {
                if (r instanceof BinopExpr) {
                    BinopExpr be = (BinopExpr) r;
                    Value lv = be.getOp1();
                    Value rv = be.getOp2();
                    TypeVariable lop = null;
                    TypeVariable rop = null;
                    if (lv instanceof Local) {
                        Local loc2 = (Local) lv;
                        if (loc2.getType() instanceof IntegerType) {
                            lop = this.resolver.typeVariable(loc2);
                        }
                    } else if (!(lv instanceof DoubleConstant) && !(lv instanceof FloatConstant)) {
                        if (lv instanceof IntConstant) {
                            int value2 = ((IntConstant) lv).value;
                            lop = value2 < -32768 ? this.resolver.INT : value2 < -128 ? this.resolver.SHORT : value2 < 0 ? this.resolver.BYTE : value2 < 2 ? this.resolver.R0_1 : value2 < 128 ? this.resolver.R0_127 : value2 < 32768 ? this.resolver.R0_32767 : value2 < 65536 ? this.resolver.CHAR : this.resolver.INT;
                        } else if (!(lv instanceof LongConstant) && !(lv instanceof NullConstant) && !(lv instanceof StringConstant) && !(lv instanceof ClassConstant)) {
                            throw new RuntimeException("Unhandled binary expression left operand type: " + lv.getClass());
                        }
                    }
                    if (rv instanceof Local) {
                        Local loc3 = (Local) rv;
                        if (loc3.getType() instanceof IntegerType) {
                            rop = this.resolver.typeVariable(loc3);
                        }
                    } else if (!(rv instanceof DoubleConstant) && !(rv instanceof FloatConstant)) {
                        if (rv instanceof IntConstant) {
                            int value3 = ((IntConstant) rv).value;
                            rop = value3 < -32768 ? this.resolver.INT : value3 < -128 ? this.resolver.SHORT : value3 < 0 ? this.resolver.BYTE : value3 < 2 ? this.resolver.R0_1 : value3 < 128 ? this.resolver.R0_127 : value3 < 32768 ? this.resolver.R0_32767 : value3 < 65536 ? this.resolver.CHAR : this.resolver.INT;
                        } else if (!(rv instanceof LongConstant) && !(rv instanceof NullConstant) && !(rv instanceof StringConstant) && !(rv instanceof ClassConstant)) {
                            throw new RuntimeException("Unhandled binary expression right operand type: " + rv.getClass());
                        }
                    }
                    if ((be instanceof AddExpr) || (be instanceof SubExpr) || (be instanceof DivExpr) || (be instanceof RemExpr) || (be instanceof MulExpr)) {
                        if (lop != null && rop != null) {
                            if (this.uses) {
                                if (lop.type() == null) {
                                    lop.addParent(this.resolver.INT);
                                }
                                if (rop.type() == null) {
                                    rop.addParent(this.resolver.INT);
                                }
                            }
                            right = this.resolver.INT;
                        }
                    } else if ((be instanceof AndExpr) || (be instanceof OrExpr) || (be instanceof XorExpr)) {
                        if (lop != null && rop != null) {
                            right = this.resolver.typeVariable();
                            rop.addParent(right);
                            lop.addParent(right);
                        }
                    } else if (be instanceof ShlExpr) {
                        if (this.uses) {
                            if (lop != null && lop.type() == null) {
                                lop.addParent(this.resolver.INT);
                            }
                            if (rop.type() == null) {
                                rop.addParent(this.resolver.INT);
                            }
                        }
                        right = lop == null ? null : this.resolver.INT;
                    } else if ((be instanceof ShrExpr) || (be instanceof UshrExpr)) {
                        if (this.uses) {
                            if (lop != null && lop.type() == null) {
                                lop.addParent(this.resolver.INT);
                            }
                            if (rop.type() == null) {
                                rop.addParent(this.resolver.INT);
                            }
                        }
                        right = lop;
                    } else if ((be instanceof CmpExpr) || (be instanceof CmpgExpr) || (be instanceof CmplExpr)) {
                        right = this.resolver.BYTE;
                    } else if ((be instanceof EqExpr) || (be instanceof GeExpr) || (be instanceof GtExpr) || (be instanceof LeExpr) || (be instanceof LtExpr) || (be instanceof NeExpr)) {
                        if (this.uses) {
                            TypeVariable common = this.resolver.typeVariable();
                            if (rop != null) {
                                rop.addParent(common);
                            }
                            if (lop != null) {
                                lop.addParent(common);
                            }
                        }
                        right = this.resolver.BOOLEAN;
                    } else {
                        throw new RuntimeException("Unhandled binary expression type: " + be.getClass());
                    }
                } else if (r instanceof CastExpr) {
                    Type ty = ((CastExpr) r).getCastType();
                    if (ty instanceof IntegerType) {
                        right = this.resolver.typeVariable(ty);
                    }
                } else if (r instanceof InstanceOfExpr) {
                    right = this.resolver.BOOLEAN;
                } else if (r instanceof InvokeExpr) {
                    InvokeExpr ie = (InvokeExpr) r;
                    handleInvokeExpr(ie);
                    Type returnType = ie.getMethodRef().getReturnType();
                    if (returnType instanceof IntegerType) {
                        right = this.resolver.typeVariable(returnType);
                    }
                } else if (r instanceof NewArrayExpr) {
                    NewArrayExpr nae = (NewArrayExpr) r;
                    if (this.uses) {
                        Value size = nae.getSize();
                        if (size instanceof Local) {
                            TypeVariable var = this.resolver.typeVariable((Local) size);
                            var.addParent(this.resolver.INT);
                        }
                    }
                } else if (!(r instanceof NewExpr)) {
                    if (r instanceof NewMultiArrayExpr) {
                        NewMultiArrayExpr nmae = (NewMultiArrayExpr) r;
                        if (this.uses) {
                            for (int i = 0; i < nmae.getSizeCount(); i++) {
                                Value size2 = nmae.getSize(i);
                                if (size2 instanceof Local) {
                                    TypeVariable var2 = this.resolver.typeVariable((Local) size2);
                                    var2.addParent(this.resolver.INT);
                                }
                            }
                        }
                    } else if (r instanceof LengthExpr) {
                        right = this.resolver.INT;
                    } else if (r instanceof NegExpr) {
                        NegExpr ne = (NegExpr) r;
                        if (ne.getOp() instanceof Local) {
                            Local local = (Local) ne.getOp();
                            if (local.getType() instanceof IntegerType) {
                                if (this.uses) {
                                    this.resolver.typeVariable(local).addParent(this.resolver.INT);
                                }
                                right = this.resolver.typeVariable();
                                right.addChild(this.resolver.BYTE);
                                right.addChild(this.resolver.typeVariable(local));
                            }
                        } else if (!(ne.getOp() instanceof DoubleConstant) && !(ne.getOp() instanceof FloatConstant)) {
                            if (ne.getOp() instanceof IntConstant) {
                                int value4 = ((IntConstant) ne.getOp()).value;
                                right = value4 < -32768 ? this.resolver.INT : value4 < -128 ? this.resolver.SHORT : value4 < 0 ? this.resolver.BYTE : value4 < 2 ? this.resolver.BYTE : value4 < 128 ? this.resolver.BYTE : value4 < 32768 ? this.resolver.SHORT : value4 < 65536 ? this.resolver.INT : this.resolver.INT;
                            } else if (!(ne.getOp() instanceof LongConstant)) {
                                throw new RuntimeException("Unhandled neg expression operand type: " + ne.getOp().getClass());
                            }
                        }
                    } else if (r instanceof Local) {
                        Local local2 = (Local) r;
                        if (local2.getType() instanceof IntegerType) {
                            right = this.resolver.typeVariable(local2);
                        }
                    } else if (r instanceof InstanceFieldRef) {
                        Type type = ((InstanceFieldRef) r).getFieldRef().type();
                        if (type instanceof IntegerType) {
                            right = this.resolver.typeVariable(type);
                        }
                    } else if (r instanceof StaticFieldRef) {
                        Type type2 = ((StaticFieldRef) r).getFieldRef().type();
                        if (type2 instanceof IntegerType) {
                            right = this.resolver.typeVariable(type2);
                        }
                    } else {
                        throw new RuntimeException("Unhandled assignment right hand side type: " + r.getClass());
                    }
                }
            }
        }
        if (left == null || right == null) {
            return;
        }
        if (left.type() == null || right.type() == null) {
            right.addParent(left);
        }
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseIdentityStmt(IdentityStmt stmt) {
        Value l = stmt.getLeftOp();
        if (l instanceof Local) {
            Local loc = (Local) l;
            if (loc.getType() instanceof IntegerType) {
                TypeVariable left = this.resolver.typeVariable(loc);
                TypeVariable right = this.resolver.typeVariable(stmt.getRightOp().getType());
                right.addParent(left);
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
        if (this.uses) {
            ConditionExpr expr = (ConditionExpr) stmt.getCondition();
            Value lv = expr.getOp1();
            Value rv = expr.getOp2();
            TypeVariable lop = null;
            TypeVariable rop = null;
            if (lv instanceof Local) {
                Local loc = (Local) lv;
                if (loc.getType() instanceof IntegerType) {
                    lop = this.resolver.typeVariable(loc);
                }
            } else if (!(lv instanceof DoubleConstant) && !(lv instanceof FloatConstant)) {
                if (lv instanceof IntConstant) {
                    int value = ((IntConstant) lv).value;
                    lop = value < -32768 ? this.resolver.INT : value < -128 ? this.resolver.SHORT : value < 0 ? this.resolver.BYTE : value < 2 ? this.resolver.R0_1 : value < 128 ? this.resolver.R0_127 : value < 32768 ? this.resolver.R0_32767 : value < 65536 ? this.resolver.CHAR : this.resolver.INT;
                } else if (!(lv instanceof LongConstant) && !(lv instanceof NullConstant) && !(lv instanceof StringConstant) && !(lv instanceof ClassConstant)) {
                    throw new RuntimeException("Unhandled binary expression left operand type: " + lv.getClass());
                }
            }
            if (rv instanceof Local) {
                Local loc2 = (Local) rv;
                if (loc2.getType() instanceof IntegerType) {
                    rop = this.resolver.typeVariable(loc2);
                }
            } else if (!(rv instanceof DoubleConstant) && !(rv instanceof FloatConstant)) {
                if (rv instanceof IntConstant) {
                    int value2 = ((IntConstant) rv).value;
                    rop = value2 < -32768 ? this.resolver.INT : value2 < -128 ? this.resolver.SHORT : value2 < 0 ? this.resolver.BYTE : value2 < 2 ? this.resolver.R0_1 : value2 < 128 ? this.resolver.R0_127 : value2 < 32768 ? this.resolver.R0_32767 : value2 < 65536 ? this.resolver.CHAR : this.resolver.INT;
                } else if (!(rv instanceof LongConstant) && !(rv instanceof NullConstant) && !(rv instanceof StringConstant) && !(rv instanceof ClassConstant)) {
                    throw new RuntimeException("Unhandled binary expression right operand type: " + rv.getClass());
                }
            }
            if (rop != null && lop != null) {
                TypeVariable common = this.resolver.typeVariable();
                rop.addParent(common);
                lop.addParent(common);
            }
        }
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
        if (this.uses) {
            Value key = stmt.getKey();
            if (key instanceof Local) {
                this.resolver.typeVariable((Local) key).addParent(this.resolver.INT);
            }
        }
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseNopStmt(NopStmt stmt) {
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseReturnStmt(ReturnStmt stmt) {
        if (this.uses) {
            Value op = stmt.getOp();
            if (op instanceof Local) {
                Local opLocal = (Local) op;
                if (opLocal.getType() instanceof IntegerType) {
                    this.resolver.typeVariable(opLocal).addParent(this.resolver.typeVariable(this.stmtBody.getMethod().getReturnType()));
                }
            }
        }
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseTableSwitchStmt(TableSwitchStmt stmt) {
        if (this.uses) {
            Value key = stmt.getKey();
            if (key instanceof Local) {
                this.resolver.typeVariable((Local) key).addParent(this.resolver.INT);
            }
        }
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseThrowStmt(ThrowStmt stmt) {
    }

    public void defaultCase(Stmt stmt) {
        throw new RuntimeException("Unhandled statement type: " + stmt.getClass());
    }
}
