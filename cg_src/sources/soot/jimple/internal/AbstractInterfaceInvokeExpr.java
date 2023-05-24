package soot.jimple.internal;

import java.util.List;
import soot.DoubleType;
import soot.LongType;
import soot.SootMethodRef;
import soot.Type;
import soot.Unit;
import soot.UnitPrinter;
import soot.ValueBox;
import soot.VoidType;
import soot.baf.Baf;
import soot.jimple.ConvertToBaf;
import soot.jimple.ExprSwitch;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.JimpleToBafContext;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/AbstractInterfaceInvokeExpr.class */
public abstract class AbstractInterfaceInvokeExpr extends AbstractInstanceInvokeExpr implements InterfaceInvokeExpr, ConvertToBaf {
    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractInterfaceInvokeExpr(ValueBox baseBox, SootMethodRef methodRef, ValueBox[] argBoxes) {
        super(methodRef, baseBox, argBoxes);
        if (methodRef.isStatic()) {
            throw new RuntimeException("wrong static-ness");
        }
    }

    @Override // soot.EquivTo
    public boolean equivTo(Object o) {
        if (o instanceof AbstractInterfaceInvokeExpr) {
            AbstractInterfaceInvokeExpr ie = (AbstractInterfaceInvokeExpr) o;
            if ((this.argBoxes == null ? 0 : this.argBoxes.length) != (ie.argBoxes == null ? 0 : ie.argBoxes.length) || !getMethod().equals(ie.getMethod()) || !this.baseBox.getValue().equivTo(ie.baseBox.getValue())) {
                return false;
            }
            if (this.argBoxes != null) {
                int e = this.argBoxes.length;
                for (int i = 0; i < e; i++) {
                    if (!this.argBoxes[i].getValue().equivTo(ie.argBoxes[i].getValue())) {
                        return false;
                    }
                }
                return true;
            }
            return true;
        }
        return false;
    }

    @Override // soot.EquivTo
    public int equivHashCode() {
        return (this.baseBox.getValue().equivHashCode() * 101) + (getMethod().equivHashCode() * 17);
    }

    public String toString() {
        StringBuilder buf = new StringBuilder("interfaceinvoke ");
        buf.append(this.baseBox.getValue().toString()).append('.').append(this.methodRef.getSignature()).append('(');
        if (this.argBoxes != null) {
            int e = this.argBoxes.length;
            for (int i = 0; i < e; i++) {
                if (i != 0) {
                    buf.append(", ");
                }
                buf.append(this.argBoxes[i].getValue().toString());
            }
        }
        buf.append(')');
        return buf.toString();
    }

    @Override // soot.Value
    public void toString(UnitPrinter up) {
        up.literal("interfaceinvoke ");
        this.baseBox.toString(up);
        up.literal(".");
        up.methodRef(this.methodRef);
        up.literal("(");
        if (this.argBoxes != null) {
            int e = this.argBoxes.length;
            for (int i = 0; i < e; i++) {
                if (i != 0) {
                    up.literal(", ");
                }
                this.argBoxes[i].toString(up);
            }
        }
        up.literal(")");
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((ExprSwitch) sw).caseInterfaceInvokeExpr(this);
    }

    private static int sizeOfType(Type t) {
        if ((t instanceof DoubleType) || (t instanceof LongType)) {
            return 2;
        }
        if (t instanceof VoidType) {
            return 0;
        }
        return 1;
    }

    private static int argCountOf(SootMethodRef m) {
        int argCount = 0;
        for (Type t : m.parameterTypes()) {
            argCount += sizeOfType(t);
        }
        return argCount;
    }

    @Override // soot.jimple.ConvertToBaf
    public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
        ValueBox[] valueBoxArr;
        ((ConvertToBaf) getBase()).convertToBaf(context, out);
        if (this.argBoxes != null) {
            for (ValueBox element : this.argBoxes) {
                ((ConvertToBaf) element.getValue()).convertToBaf(context, out);
            }
        }
        Unit u = Baf.v().newInterfaceInvokeInst(this.methodRef, argCountOf(this.methodRef));
        out.add(u);
        u.addAllTagsOf(context.getCurrentUnit());
    }
}
