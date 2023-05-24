package soot.jimple.toolkits.typing;

import java.util.List;
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
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/ConstraintCollectorBV.class */
public class ConstraintCollectorBV extends AbstractStmtSwitch {
    private TypeResolverBV resolver;
    private boolean uses;
    private JimpleBody stmtBody;

    public ConstraintCollectorBV(TypeResolverBV resolver, boolean uses) {
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
        if (ie instanceof InterfaceInvokeExpr) {
            InterfaceInvokeExpr invoke = (InterfaceInvokeExpr) ie;
            SootMethodRef method = invoke.getMethodRef();
            Value base = invoke.getBase();
            if (base instanceof Local) {
                Local local = (Local) base;
                TypeVariableBV localType = this.resolver.typeVariable(local);
                localType.addParent(this.resolver.typeVariable(method.declaringClass()));
            }
            int count = invoke.getArgCount();
            for (int i = 0; i < count; i++) {
                if (invoke.getArg(i) instanceof Local) {
                    Local local2 = (Local) invoke.getArg(i);
                    TypeVariableBV localType2 = this.resolver.typeVariable(local2);
                    localType2.addParent(this.resolver.typeVariable(method.parameterType(i)));
                }
            }
        } else if (ie instanceof SpecialInvokeExpr) {
            SpecialInvokeExpr invoke2 = (SpecialInvokeExpr) ie;
            SootMethodRef method2 = invoke2.getMethodRef();
            Value base2 = invoke2.getBase();
            if (base2 instanceof Local) {
                Local local3 = (Local) base2;
                TypeVariableBV localType3 = this.resolver.typeVariable(local3);
                localType3.addParent(this.resolver.typeVariable(method2.declaringClass()));
            }
            int count2 = invoke2.getArgCount();
            for (int i2 = 0; i2 < count2; i2++) {
                if (invoke2.getArg(i2) instanceof Local) {
                    Local local4 = (Local) invoke2.getArg(i2);
                    TypeVariableBV localType4 = this.resolver.typeVariable(local4);
                    localType4.addParent(this.resolver.typeVariable(method2.parameterType(i2)));
                }
            }
        } else if (ie instanceof VirtualInvokeExpr) {
            VirtualInvokeExpr invoke3 = (VirtualInvokeExpr) ie;
            SootMethodRef method3 = invoke3.getMethodRef();
            Value base3 = invoke3.getBase();
            if (base3 instanceof Local) {
                Local local5 = (Local) base3;
                TypeVariableBV localType5 = this.resolver.typeVariable(local5);
                localType5.addParent(this.resolver.typeVariable(method3.declaringClass()));
            }
            int count3 = invoke3.getArgCount();
            for (int i3 = 0; i3 < count3; i3++) {
                if (invoke3.getArg(i3) instanceof Local) {
                    Local local6 = (Local) invoke3.getArg(i3);
                    TypeVariableBV localType6 = this.resolver.typeVariable(local6);
                    localType6.addParent(this.resolver.typeVariable(method3.parameterType(i3)));
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
                        TypeVariableBV localType7 = this.resolver.typeVariable(local7);
                        localType7.addParent(this.resolver.typeVariable(method4.parameterType(i4)));
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
        TypeVariableBV lop;
        TypeVariableBV rop;
        Value l = stmt.getLeftOp();
        Value r = stmt.getRightOp();
        TypeVariableBV left = null;
        TypeVariableBV right = null;
        if (l instanceof ArrayRef) {
            ArrayRef ref = (ArrayRef) l;
            Value base = ref.getBase();
            Value index = ref.getIndex();
            TypeVariableBV baseType = this.resolver.typeVariable((Local) base);
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
                TypeVariableBV baseType2 = this.resolver.typeVariable((Local) ref2.getBase());
                baseType2.addParent(this.resolver.typeVariable(ref2.getField().getDeclaringClass()));
                left = this.resolver.typeVariable(ref2.getField().getType());
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
            Value base2 = ref3.getBase();
            Value index2 = ref3.getIndex();
            TypeVariableBV baseType3 = this.resolver.typeVariable((Local) base2);
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
                    TypeVariableBV common = this.resolver.typeVariable();
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
                    TypeVariableBV common2 = this.resolver.typeVariable();
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
                right = this.resolver.typeVariable(ArrayType.v(((ArrayType) baseType4).baseType, ((ArrayType) baseType4).numDimensions + 1));
            } else {
                right = this.resolver.typeVariable(ArrayType.v(baseType4, 1));
            }
            if (this.uses) {
                Value size = nae.getSize();
                if (size instanceof Local) {
                    TypeVariableBV var = this.resolver.typeVariable((Local) size);
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
                        TypeVariableBV var2 = this.resolver.typeVariable((Local) size2);
                        var2.addParent(this.resolver.typeVariable(IntType.v()));
                    }
                }
            }
        } else if (r instanceof LengthExpr) {
            LengthExpr le = (LengthExpr) r;
            if (this.uses && (le.getOp() instanceof Local)) {
                this.resolver.typeVariable((Local) le.getOp()).makeElement();
            }
            right = this.resolver.typeVariable(IntType.v());
        } else if (r instanceof NegExpr) {
            NegExpr ne = (NegExpr) r;
            if (ne.getOp() instanceof Local) {
                right = this.resolver.typeVariable((Local) ne.getOp());
            } else if (ne.getOp() instanceof DoubleConstant) {
                right = this.resolver.typeVariable(DoubleType.v());
            } else if (ne.getOp() instanceof FloatConstant) {
                right = this.resolver.typeVariable(FloatType.v());
            } else if (ne.getOp() instanceof IntConstant) {
                right = this.resolver.typeVariable(IntType.v());
            } else if (ne.getOp() instanceof LongConstant) {
                right = this.resolver.typeVariable(LongType.v());
            } else {
                throw new RuntimeException("Unhandled neg expression operand type: " + ne.getOp().getClass());
            }
        } else if (r instanceof Local) {
            right = this.resolver.typeVariable((Local) r);
        } else if (r instanceof InstanceFieldRef) {
            InstanceFieldRef ref4 = (InstanceFieldRef) r;
            if (this.uses) {
                TypeVariableBV baseType5 = this.resolver.typeVariable((Local) ref4.getBase());
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
            TypeVariableBV left = this.resolver.typeVariable((Local) l);
            if (!(r instanceof CaughtExceptionRef)) {
                TypeVariableBV right = this.resolver.typeVariable(r.getType());
                right.addParent(left);
                return;
            }
            List<RefType> exceptionTypes = TrapManager.getExceptionTypesOf(stmt, this.stmtBody);
            for (Type t : exceptionTypes) {
                this.resolver.typeVariable(t).addParent(left);
            }
            if (this.uses) {
                left.addParent(this.resolver.typeVariable(Scene.v().getBaseExceptionType()));
            }
        }
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
        if (this.uses && (stmt.getOp() instanceof Local)) {
            TypeVariableBV op = this.resolver.typeVariable((Local) stmt.getOp());
            op.addParent(this.resolver.typeVariable(RefType.v(JavaBasicTypes.JAVA_LANG_OBJECT)));
        }
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
        if (this.uses && (stmt.getOp() instanceof Local)) {
            TypeVariableBV op = this.resolver.typeVariable((Local) stmt.getOp());
            op.addParent(this.resolver.typeVariable(RefType.v(JavaBasicTypes.JAVA_LANG_OBJECT)));
        }
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseGotoStmt(GotoStmt stmt) {
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseIfStmt(IfStmt stmt) {
        TypeVariableBV lop;
        TypeVariableBV rop;
        if (this.uses) {
            ConditionExpr cond = (ConditionExpr) stmt.getCondition();
            Value lv = cond.getOp1();
            Value rv = cond.getOp2();
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
            TypeVariableBV common = this.resolver.typeVariable();
            rop.addParent(common);
            lop.addParent(common);
        }
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
        if (this.uses) {
            Value key = stmt.getKey();
            if (key instanceof Local) {
                this.resolver.typeVariable((Local) key).addParent(this.resolver.typeVariable(IntType.v()));
            }
        }
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseNopStmt(NopStmt stmt) {
    }

    @Override // soot.jimple.AbstractStmtSwitch, soot.jimple.StmtSwitch
    public void caseReturnStmt(ReturnStmt stmt) {
        if (this.uses && (stmt.getOp() instanceof Local)) {
            this.resolver.typeVariable((Local) stmt.getOp()).addParent(this.resolver.typeVariable(this.stmtBody.getMethod().getReturnType()));
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
        if (this.uses && (stmt.getOp() instanceof Local)) {
            TypeVariableBV op = this.resolver.typeVariable((Local) stmt.getOp());
            op.addParent(this.resolver.typeVariable(Scene.v().getBaseExceptionType()));
        }
    }

    public void defaultCase(Stmt stmt) {
        throw new RuntimeException("Unhandled statement type: " + stmt.getClass());
    }
}
