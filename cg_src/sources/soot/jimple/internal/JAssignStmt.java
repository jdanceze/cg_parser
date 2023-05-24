package soot.jimple.internal;

import java.util.List;
import soot.Immediate;
import soot.IntType;
import soot.Local;
import soot.Unit;
import soot.UnitBox;
import soot.UnitBoxOwner;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.baf.ArrayWriteInst;
import soot.baf.Baf;
import soot.baf.FieldPutInst;
import soot.baf.StaticPutInst;
import soot.baf.StoreInst;
import soot.jimple.AbstractJimpleValueSwitch;
import soot.jimple.AddExpr;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.ConvertToBaf;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.JimpleToBafContext;
import soot.jimple.StaticFieldRef;
import soot.jimple.StmtSwitch;
import soot.jimple.SubExpr;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/JAssignStmt.class */
public class JAssignStmt extends AbstractDefinitionStmt implements AssignStmt {

    /* loaded from: gencallgraphv3.jar:soot/jimple/internal/JAssignStmt$LinkedVariableBox.class */
    public static class LinkedVariableBox extends VariableBox {
        ValueBox otherBox;

        public LinkedVariableBox(Value v) {
            super(v);
            this.otherBox = null;
        }

        public void setOtherBox(ValueBox otherBox) {
            this.otherBox = otherBox;
        }

        @Override // soot.jimple.internal.VariableBox, soot.ValueBox
        public boolean canContainValue(Value v) {
            if (super.canContainValue(v)) {
                return this.otherBox == null || (v instanceof Immediate) || (this.otherBox.getValue() instanceof Immediate);
            }
            return false;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/internal/JAssignStmt$LinkedRValueBox.class */
    public static class LinkedRValueBox extends RValueBox {
        ValueBox otherBox;

        public LinkedRValueBox(Value v) {
            super(v);
            this.otherBox = null;
        }

        public void setOtherBox(ValueBox otherBox) {
            this.otherBox = otherBox;
        }

        @Override // soot.jimple.internal.RValueBox, soot.ValueBox
        public boolean canContainValue(Value v) {
            if (super.canContainValue(v)) {
                return this.otherBox == null || (v instanceof Immediate) || (this.otherBox.getValue() instanceof Immediate);
            }
            return false;
        }
    }

    public JAssignStmt(Value variable, Value rvalue) {
        this(new LinkedVariableBox(variable), new LinkedRValueBox(rvalue));
        ((LinkedVariableBox) this.leftBox).setOtherBox(this.rightBox);
        ((LinkedRValueBox) this.rightBox).setOtherBox(this.leftBox);
        if (!this.leftBox.canContainValue(variable) || !this.rightBox.canContainValue(rvalue)) {
            throw new RuntimeException("Illegal assignment statement. Make sure that either left side or right hand side has a local or constant.Variable is class " + variable.getClass().getName() + "(" + this.leftBox.canContainValue(variable) + ") and rvalue is class " + rvalue.getClass().getName() + "(" + this.rightBox.canContainValue(rvalue) + ").");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public JAssignStmt(ValueBox variableBox, ValueBox rvalueBox) {
        super(variableBox, rvalueBox);
        if (this.leftBox instanceof LinkedVariableBox) {
            ((LinkedVariableBox) this.leftBox).setOtherBox(this.rightBox);
        }
        if (this.rightBox instanceof LinkedRValueBox) {
            ((LinkedRValueBox) this.rightBox).setOtherBox(this.leftBox);
        }
    }

    @Override // soot.jimple.internal.AbstractStmt, soot.jimple.Stmt
    public boolean containsInvokeExpr() {
        return getRightOp() instanceof InvokeExpr;
    }

    @Override // soot.jimple.internal.AbstractStmt, soot.jimple.Stmt
    public InvokeExpr getInvokeExpr() {
        return (InvokeExpr) getInvokeExprBox().getValue();
    }

    @Override // soot.jimple.internal.AbstractStmt, soot.jimple.Stmt
    public ValueBox getInvokeExprBox() {
        if (!containsInvokeExpr()) {
            throw new RuntimeException("getInvokeExprBox() called with no invokeExpr present!");
        }
        return this.rightBox;
    }

    @Override // soot.jimple.internal.AbstractStmt, soot.jimple.Stmt
    public boolean containsArrayRef() {
        return (getLeftOp() instanceof ArrayRef) || (getRightOp() instanceof ArrayRef);
    }

    @Override // soot.jimple.internal.AbstractStmt, soot.jimple.Stmt
    public ArrayRef getArrayRef() {
        return (ArrayRef) getArrayRefBox().getValue();
    }

    @Override // soot.jimple.internal.AbstractStmt, soot.jimple.Stmt
    public ValueBox getArrayRefBox() {
        if (containsArrayRef()) {
            return this.leftBox.getValue() instanceof ArrayRef ? this.leftBox : this.rightBox;
        }
        throw new RuntimeException("getArrayRefBox() called with no ArrayRef present!");
    }

    @Override // soot.jimple.internal.AbstractStmt, soot.jimple.Stmt
    public boolean containsFieldRef() {
        return (getLeftOp() instanceof FieldRef) || (getRightOp() instanceof FieldRef);
    }

    @Override // soot.jimple.internal.AbstractStmt, soot.jimple.Stmt
    public FieldRef getFieldRef() {
        return (FieldRef) getFieldRefBox().getValue();
    }

    @Override // soot.jimple.internal.AbstractStmt, soot.jimple.Stmt
    public ValueBox getFieldRefBox() {
        if (containsFieldRef()) {
            return this.leftBox.getValue() instanceof FieldRef ? this.leftBox : this.rightBox;
        }
        throw new RuntimeException("getFieldRefBox() called with no FieldRef present!");
    }

    @Override // soot.AbstractUnit, soot.Unit
    public List<UnitBox> getUnitBoxes() {
        Value rValue = this.rightBox.getValue();
        if (rValue instanceof UnitBoxOwner) {
            return ((UnitBoxOwner) rValue).getUnitBoxes();
        }
        return super.getUnitBoxes();
    }

    public String toString() {
        return String.valueOf(this.leftBox.getValue().toString()) + " = " + this.rightBox.getValue().toString();
    }

    public void toString(UnitPrinter up) {
        this.leftBox.toString(up);
        up.literal(" = ");
        this.rightBox.toString(up);
    }

    @Override // soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new JAssignStmt(Jimple.cloneIfNecessary(getLeftOp()), Jimple.cloneIfNecessary(getRightOp()));
    }

    @Override // soot.jimple.AssignStmt
    public void setLeftOp(Value variable) {
        getLeftOpBox().setValue(variable);
    }

    @Override // soot.jimple.AssignStmt
    public void setRightOp(Value rvalue) {
        getRightOpBox().setValue(rvalue);
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((StmtSwitch) sw).caseAssignStmt(this);
    }

    @Override // soot.jimple.internal.AbstractStmt, soot.jimple.ConvertToBaf
    public void convertToBaf(final JimpleToBafContext context, final List<Unit> out) {
        Value lvalue = getLeftOp();
        final Value rvalue = getRightOp();
        if ((lvalue instanceof Local) && ((rvalue instanceof AddExpr) || (rvalue instanceof SubExpr))) {
            Local l = (Local) lvalue;
            BinopExpr expr = (BinopExpr) rvalue;
            Value op1 = expr.getOp1();
            Value op2 = expr.getOp2();
            if (IntType.v().equals(l.getType())) {
                boolean isValidCase = false;
                int x = 0;
                if (op1 == l && (op2 instanceof IntConstant)) {
                    x = ((IntConstant) op2).value;
                    isValidCase = true;
                } else if ((expr instanceof AddExpr) && op2 == l && (op1 instanceof IntConstant)) {
                    x = ((IntConstant) op1).value;
                    isValidCase = true;
                }
                if (isValidCase && x >= -32768 && x <= 32767) {
                    Baf baf = Baf.v();
                    Unit u = baf.newIncInst(context.getBafLocalOfJimpleLocal(l), IntConstant.v(expr instanceof AddExpr ? x : -x));
                    u.addAllTagsOf(this);
                    out.add(u);
                    return;
                }
            }
        }
        context.setCurrentUnit(this);
        lvalue.apply(new AbstractJimpleValueSwitch() { // from class: soot.jimple.internal.JAssignStmt.1
            @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.RefSwitch
            public void caseArrayRef(ArrayRef v) {
                ((ConvertToBaf) v.getBase()).convertToBaf(context, out);
                ((ConvertToBaf) v.getIndex()).convertToBaf(context, out);
                ((ConvertToBaf) rvalue).convertToBaf(context, out);
                ArrayWriteInst newArrayWriteInst = Baf.v().newArrayWriteInst(v.getType());
                newArrayWriteInst.addAllTagsOf(JAssignStmt.this);
                out.add(newArrayWriteInst);
            }

            @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.RefSwitch
            public void caseInstanceFieldRef(InstanceFieldRef v) {
                ((ConvertToBaf) v.getBase()).convertToBaf(context, out);
                ((ConvertToBaf) rvalue).convertToBaf(context, out);
                FieldPutInst newFieldPutInst = Baf.v().newFieldPutInst(v.getFieldRef());
                newFieldPutInst.addAllTagsOf(JAssignStmt.this);
                out.add(newFieldPutInst);
            }

            @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.ImmediateSwitch
            public void caseLocal(Local v) {
                ((ConvertToBaf) rvalue).convertToBaf(context, out);
                StoreInst newStoreInst = Baf.v().newStoreInst(v.getType(), context.getBafLocalOfJimpleLocal(v));
                newStoreInst.addAllTagsOf(JAssignStmt.this);
                out.add(newStoreInst);
            }

            @Override // soot.jimple.AbstractJimpleValueSwitch, soot.jimple.RefSwitch
            public void caseStaticFieldRef(StaticFieldRef v) {
                ((ConvertToBaf) rvalue).convertToBaf(context, out);
                StaticPutInst newStaticPutInst = Baf.v().newStaticPutInst(v.getFieldRef());
                newStaticPutInst.addAllTagsOf(JAssignStmt.this);
                out.add(newStaticPutInst);
            }
        });
    }
}
