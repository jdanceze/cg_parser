package soot.jimple.toolkits.typing;

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
import soot.SootField;
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
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/ConstraintCollector.class */
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
                TypeVariable localType = this.resolver.typeVariable((Local) arg);
                localType.addParent(this.resolver.typeVariable(method.parameterType(i)));
            }
        }
        if (ie instanceof InterfaceInvokeExpr) {
            Value base = ((InterfaceInvokeExpr) ie).getBase();
            if (base instanceof Local) {
                TypeVariable localType2 = this.resolver.typeVariable((Local) base);
                localType2.addParent(this.resolver.typeVariable(method.declaringClass()));
            }
        } else if (ie instanceof SpecialInvokeExpr) {
            Value base2 = ((SpecialInvokeExpr) ie).getBase();
            if (base2 instanceof Local) {
                TypeVariable localType3 = this.resolver.typeVariable((Local) base2);
                localType3.addParent(this.resolver.typeVariable(method.declaringClass()));
            }
        } else if (ie instanceof VirtualInvokeExpr) {
            Value base3 = ((VirtualInvokeExpr) ie).getBase();
            if (base3 instanceof Local) {
                TypeVariable localType4 = this.resolver.typeVariable((Local) base3);
                localType4.addParent(this.resolver.typeVariable(method.declaringClass()));
            }
        } else if (!(ie instanceof StaticInvokeExpr)) {
            if (ie instanceof DynamicInvokeExpr) {
                DynamicInvokeExpr invoke = (DynamicInvokeExpr) ie;
                SootMethodRef bootstrapMethod = invoke.getBootstrapMethodRef();
                for (int i2 = 0; i2 < invoke.getBootstrapArgCount(); i2++) {
                    if (invoke.getArg(i2) instanceof Local) {
                        Local local = (Local) invoke.getBootstrapArg(i2);
                        TypeVariable localType5 = this.resolver.typeVariable(local);
                        localType5.addParent(this.resolver.typeVariable(bootstrapMethod.parameterType(i2)));
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
        handleInvokeExpr(stmt.getInvokeExpr());
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseAssignStmt(AssignStmt stmt) {
        TypeVariable lop;
        TypeVariable rop;
        Value l = stmt.getLeftOp();
        Value r = stmt.getRightOp();
        TypeVariable left = null;
        TypeVariable right = null;
        if (l instanceof ArrayRef) {
            ArrayRef ref = (ArrayRef) l;
            Value index = ref.getIndex();
            Value base = ref.getBase();
            TypeVariable baseType = this.resolver.typeVariable((Local) base);
            baseType.makeElement();
            left = baseType.element();
            if ((index instanceof Local) && this.uses) {
                this.resolver.typeVariable((Local) index).addParent(this.resolver.typeVariable(IntType.v()));
            }
        } else if (l instanceof Local) {
            left = this.resolver.typeVariable((Local) l);
        } else if (l instanceof InstanceFieldRef) {
            InstanceFieldRef ref2 = (InstanceFieldRef) l;
            if (this.uses) {
                TypeVariable baseType2 = this.resolver.typeVariable((Local) ref2.getBase());
                SootField field = ref2.getField();
                baseType2.addParent(this.resolver.typeVariable(field.getDeclaringClass()));
                left = this.resolver.typeVariable(field.getType());
            }
        } else if (l instanceof StaticFieldRef) {
            if (this.uses) {
                left = this.resolver.typeVariable(((StaticFieldRef) l).getField().getType());
            }
        } else {
            throw new RuntimeException("Unhandled assignment left hand side type: " + l.getClass());
        }
        if (r instanceof ArrayRef) {
            ArrayRef ref3 = (ArrayRef) r;
            Value index2 = ref3.getIndex();
            Value base2 = ref3.getBase();
            TypeVariable baseType3 = this.resolver.typeVariable((Local) base2);
            baseType3.makeElement();
            right = baseType3.element();
            if ((index2 instanceof Local) && this.uses) {
                this.resolver.typeVariable((Local) index2).addParent(this.resolver.typeVariable(IntType.v()));
            }
        } else if (r instanceof DoubleConstant) {
            right = this.resolver.typeVariable(DoubleType.v());
        } else if (r instanceof FloatConstant) {
            right = this.resolver.typeVariable(FloatType.v());
        } else if (r instanceof IntConstant) {
            right = this.resolver.typeVariable(IntType.v());
        } else if (r instanceof LongConstant) {
            right = this.resolver.typeVariable(LongType.v());
        } else if (r instanceof NullConstant) {
            right = this.resolver.typeVariable(NullType.v());
        } else if (r instanceof StringConstant) {
            right = this.resolver.typeVariable(RefType.v("java.lang.String"));
        } else if (r instanceof ClassConstant) {
            right = this.resolver.typeVariable(RefType.v("java.lang.Class"));
        } else if (r instanceof BinopExpr) {
            BinopExpr be = (BinopExpr) r;
            Value lv = be.getOp1();
            Value rv = be.getOp2();
            if (lv instanceof Local) {
                lop = this.resolver.typeVariable((Local) lv);
            } else if (lv instanceof DoubleConstant) {
                lop = this.resolver.typeVariable(DoubleType.v());
            } else if (lv instanceof FloatConstant) {
                lop = this.resolver.typeVariable(FloatType.v());
            } else if (lv instanceof IntConstant) {
                lop = this.resolver.typeVariable(IntType.v());
            } else if (lv instanceof LongConstant) {
                lop = this.resolver.typeVariable(LongType.v());
            } else if (lv instanceof NullConstant) {
                lop = this.resolver.typeVariable(NullType.v());
            } else if (lv instanceof StringConstant) {
                lop = this.resolver.typeVariable(RefType.v("java.lang.String"));
            } else if (lv instanceof ClassConstant) {
                lop = this.resolver.typeVariable(RefType.v("java.lang.Class"));
            } else {
                throw new RuntimeException("Unhandled binary expression left operand type: " + lv.getClass());
            }
            if (rv instanceof Local) {
                rop = this.resolver.typeVariable((Local) rv);
            } else if (rv instanceof DoubleConstant) {
                rop = this.resolver.typeVariable(DoubleType.v());
            } else if (rv instanceof FloatConstant) {
                rop = this.resolver.typeVariable(FloatType.v());
            } else if (rv instanceof IntConstant) {
                rop = this.resolver.typeVariable(IntType.v());
            } else if (rv instanceof LongConstant) {
                rop = this.resolver.typeVariable(LongType.v());
            } else if (rv instanceof NullConstant) {
                rop = this.resolver.typeVariable(NullType.v());
            } else if (rv instanceof StringConstant) {
                rop = this.resolver.typeVariable(RefType.v("java.lang.String"));
            } else if (rv instanceof ClassConstant) {
                rop = this.resolver.typeVariable(RefType.v("java.lang.Class"));
            } else {
                throw new RuntimeException("Unhandled binary expression right operand type: " + rv.getClass());
            }
            if ((be instanceof AddExpr) || (be instanceof SubExpr) || (be instanceof MulExpr) || (be instanceof DivExpr) || (be instanceof RemExpr) || (be instanceof AndExpr) || (be instanceof OrExpr) || (be instanceof XorExpr)) {
                if (this.uses) {
                    TypeVariable common = this.resolver.typeVariable();
                    rop.addParent(common);
                    lop.addParent(common);
                }
                if (left != null) {
                    rop.addParent(left);
                    lop.addParent(left);
                }
            } else if ((be instanceof ShlExpr) || (be instanceof ShrExpr) || (be instanceof UshrExpr)) {
                if (this.uses) {
                    rop.addParent(this.resolver.typeVariable(IntType.v()));
                }
                right = lop;
            } else if ((be instanceof CmpExpr) || (be instanceof CmpgExpr) || (be instanceof CmplExpr) || (be instanceof EqExpr) || (be instanceof GeExpr) || (be instanceof GtExpr) || (be instanceof LeExpr) || (be instanceof LtExpr) || (be instanceof NeExpr)) {
                if (this.uses) {
                    TypeVariable common2 = this.resolver.typeVariable();
                    rop.addParent(common2);
                    lop.addParent(common2);
                }
                right = this.resolver.typeVariable(IntType.v());
            } else {
                throw new RuntimeException("Unhandled binary expression type: " + be.getClass());
            }
        } else if (r instanceof CastExpr) {
            CastExpr ce = (CastExpr) r;
            right = this.resolver.typeVariable(ce.getCastType());
        } else if (r instanceof InstanceOfExpr) {
            right = this.resolver.typeVariable(IntType.v());
        } else if (r instanceof InvokeExpr) {
            InvokeExpr ie = (InvokeExpr) r;
            handleInvokeExpr(ie);
            right = this.resolver.typeVariable(ie.getMethodRef().returnType());
        } else if (r instanceof NewArrayExpr) {
            NewArrayExpr nae = (NewArrayExpr) r;
            Type baseType4 = nae.getBaseType();
            if (baseType4 instanceof ArrayType) {
                ArrayType arrTy = (ArrayType) baseType4;
                right = this.resolver.typeVariable(ArrayType.v(arrTy.baseType, arrTy.numDimensions + 1));
            } else {
                right = this.resolver.typeVariable(ArrayType.v(baseType4, 1));
            }
            if (this.uses) {
                Value size = nae.getSize();
                if (size instanceof Local) {
                    TypeVariable var = this.resolver.typeVariable((Local) size);
                    var.addParent(this.resolver.typeVariable(IntType.v()));
                }
            }
        } else if (r instanceof NewExpr) {
            NewExpr na = (NewExpr) r;
            right = this.resolver.typeVariable(na.getBaseType());
        } else if (r instanceof NewMultiArrayExpr) {
            NewMultiArrayExpr nmae = (NewMultiArrayExpr) r;
            right = this.resolver.typeVariable(nmae.getBaseType());
            if (this.uses) {
                for (int i = 0; i < nmae.getSizeCount(); i++) {
                    Value size2 = nmae.getSize(i);
                    if (size2 instanceof Local) {
                        TypeVariable var2 = this.resolver.typeVariable((Local) size2);
                        var2.addParent(this.resolver.typeVariable(IntType.v()));
                    }
                }
            }
        } else if (r instanceof LengthExpr) {
            if (this.uses) {
                Value op = ((LengthExpr) r).getOp();
                if (op instanceof Local) {
                    this.resolver.typeVariable((Local) op).makeElement();
                }
            }
            right = this.resolver.typeVariable(IntType.v());
        } else if (r instanceof NegExpr) {
            Value op2 = ((NegExpr) r).getOp();
            if (op2 instanceof Local) {
                right = this.resolver.typeVariable((Local) op2);
            } else if (op2 instanceof DoubleConstant) {
                right = this.resolver.typeVariable(DoubleType.v());
            } else if (op2 instanceof FloatConstant) {
                right = this.resolver.typeVariable(FloatType.v());
            } else if (op2 instanceof IntConstant) {
                right = this.resolver.typeVariable(IntType.v());
            } else if (op2 instanceof LongConstant) {
                right = this.resolver.typeVariable(LongType.v());
            } else {
                throw new RuntimeException("Unhandled neg expression operand type: " + op2.getClass());
            }
        } else if (r instanceof Local) {
            right = this.resolver.typeVariable((Local) r);
        } else if (r instanceof InstanceFieldRef) {
            InstanceFieldRef ref4 = (InstanceFieldRef) r;
            if (this.uses) {
                TypeVariable baseType5 = this.resolver.typeVariable((Local) ref4.getBase());
                baseType5.addParent(this.resolver.typeVariable(ref4.getField().getDeclaringClass()));
            }
            right = this.resolver.typeVariable(ref4.getField().getType());
        } else if (r instanceof StaticFieldRef) {
            right = this.resolver.typeVariable(((StaticFieldRef) r).getField().getType());
        } else {
            throw new RuntimeException("Unhandled assignment right hand side type: " + r.getClass());
        }
        if (left != null && right != null) {
            right.addParent(left);
        }
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseIdentityStmt(IdentityStmt stmt) {
        Value l = stmt.getLeftOp();
        Value r = stmt.getRightOp();
        if (l instanceof Local) {
            TypeVariable left = this.resolver.typeVariable((Local) l);
            if (r instanceof CaughtExceptionRef) {
                for (Type t : TrapManager.getExceptionTypesOf(stmt, this.stmtBody)) {
                    this.resolver.typeVariable(t).addParent(left);
                }
                if (this.uses) {
                    left.addParent(this.resolver.typeVariable(Scene.v().getBaseExceptionType()));
                    return;
                }
                return;
            }
            TypeVariable right = this.resolver.typeVariable(r.getType());
            right.addParent(left);
        }
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
        if (this.uses) {
            Value op = stmt.getOp();
            if (op instanceof Local) {
                TypeVariable var = this.resolver.typeVariable((Local) op);
                var.addParent(this.resolver.typeVariable(RefType.v(JavaBasicTypes.JAVA_LANG_OBJECT)));
            }
        }
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
        if (this.uses) {
            Value op = stmt.getOp();
            if (op instanceof Local) {
                TypeVariable var = this.resolver.typeVariable((Local) op);
                var.addParent(this.resolver.typeVariable(RefType.v(JavaBasicTypes.JAVA_LANG_OBJECT)));
            }
        }
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseGotoStmt(GotoStmt stmt) {
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseIfStmt(IfStmt stmt) {
        TypeVariable lop;
        TypeVariable rop;
        if (this.uses) {
            ConditionExpr expr = (ConditionExpr) stmt.getCondition();
            Value lv = expr.getOp1();
            Value rv = expr.getOp2();
            if (lv instanceof Local) {
                lop = this.resolver.typeVariable((Local) lv);
            } else if (lv instanceof DoubleConstant) {
                lop = this.resolver.typeVariable(DoubleType.v());
            } else if (lv instanceof FloatConstant) {
                lop = this.resolver.typeVariable(FloatType.v());
            } else if (lv instanceof IntConstant) {
                lop = this.resolver.typeVariable(IntType.v());
            } else if (lv instanceof LongConstant) {
                lop = this.resolver.typeVariable(LongType.v());
            } else if (lv instanceof NullConstant) {
                lop = this.resolver.typeVariable(NullType.v());
            } else if (lv instanceof StringConstant) {
                lop = this.resolver.typeVariable(RefType.v("java.lang.String"));
            } else if (lv instanceof ClassConstant) {
                lop = this.resolver.typeVariable(RefType.v("java.lang.Class"));
            } else {
                throw new RuntimeException("Unhandled binary expression left operand type: " + lv.getClass());
            }
            if (rv instanceof Local) {
                rop = this.resolver.typeVariable((Local) rv);
            } else if (rv instanceof DoubleConstant) {
                rop = this.resolver.typeVariable(DoubleType.v());
            } else if (rv instanceof FloatConstant) {
                rop = this.resolver.typeVariable(FloatType.v());
            } else if (rv instanceof IntConstant) {
                rop = this.resolver.typeVariable(IntType.v());
            } else if (rv instanceof LongConstant) {
                rop = this.resolver.typeVariable(LongType.v());
            } else if (rv instanceof NullConstant) {
                rop = this.resolver.typeVariable(NullType.v());
            } else if (rv instanceof StringConstant) {
                rop = this.resolver.typeVariable(RefType.v("java.lang.String"));
            } else if (rv instanceof ClassConstant) {
                rop = this.resolver.typeVariable(RefType.v("java.lang.Class"));
            } else {
                throw new RuntimeException("Unhandled binary expression right operand type: " + rv.getClass());
            }
            TypeVariable common = this.resolver.typeVariable();
            rop.addParent(common);
            lop.addParent(common);
        }
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
        if (this.uses) {
            Value key = stmt.getKey();
            if (key instanceof Local) {
                TypeVariable var = this.resolver.typeVariable((Local) key);
                var.addParent(this.resolver.typeVariable(IntType.v()));
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
                TypeVariable var = this.resolver.typeVariable((Local) op);
                var.addParent(this.resolver.typeVariable(this.stmtBody.getMethod().getReturnType()));
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
                this.resolver.typeVariable((Local) key).addParent(this.resolver.typeVariable(IntType.v()));
            }
        }
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseThrowStmt(ThrowStmt stmt) {
        if (this.uses) {
            Value op = stmt.getOp();
            if (op instanceof Local) {
                TypeVariable var = this.resolver.typeVariable((Local) op);
                var.addParent(this.resolver.typeVariable(Scene.v().getBaseExceptionType()));
            }
        }
    }

    public void defaultCase(Stmt stmt) {
        throw new RuntimeException("Unhandled statement type: " + stmt.getClass());
    }
}
