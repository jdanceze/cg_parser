package soot.grimp.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.grimp.Grimp;
import soot.jimple.DynamicInvokeExpr;
import soot.jimple.ExprSwitch;
import soot.jimple.internal.AbstractInvokeExpr;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/grimp/internal/GDynamicInvokeExpr.class */
public class GDynamicInvokeExpr extends AbstractInvokeExpr implements DynamicInvokeExpr {
    protected final SootMethodRef bsmRef;
    protected final ValueBox[] bsmArgBoxes;
    protected final int tag;

    public GDynamicInvokeExpr(SootMethodRef bootStrapMethodRef, List<? extends Value> bootstrapArgs, SootMethodRef methodRef, int tag, List<? extends Value> methodArgs) {
        super(methodRef, new ValueBox[methodArgs.size()]);
        this.bsmRef = bootStrapMethodRef;
        this.bsmArgBoxes = new ValueBox[bootstrapArgs.size()];
        this.tag = tag;
        Grimp grmp = Grimp.v();
        ListIterator<? extends Value> it = bootstrapArgs.listIterator();
        while (it.hasNext()) {
            Value v = it.next();
            this.bsmArgBoxes[it.previousIndex()] = grmp.newExprBox(v);
        }
        ListIterator<? extends Value> it2 = methodArgs.listIterator();
        while (it2.hasNext()) {
            Value v2 = it2.next();
            this.argBoxes[it2.previousIndex()] = grmp.newExprBox(v2);
        }
    }

    @Override // soot.jimple.internal.AbstractInvokeExpr, soot.Value
    public Object clone() {
        ValueBox[] valueBoxArr;
        List<Value> clonedBsmArgs = new ArrayList<>(this.bsmArgBoxes.length);
        for (ValueBox box : this.bsmArgBoxes) {
            clonedBsmArgs.add(box.getValue());
        }
        int count = getArgCount();
        List<Value> clonedArgs = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            clonedArgs.add(Grimp.cloneIfNecessary(getArg(i)));
        }
        return new GDynamicInvokeExpr(this.bsmRef, clonedBsmArgs, this.methodRef, this.tag, clonedArgs);
    }

    @Override // soot.jimple.DynamicInvokeExpr
    public int getBootstrapArgCount() {
        return this.bsmArgBoxes.length;
    }

    @Override // soot.jimple.DynamicInvokeExpr
    public Value getBootstrapArg(int i) {
        return this.bsmArgBoxes[i].getValue();
    }

    @Override // soot.jimple.DynamicInvokeExpr
    public List<Value> getBootstrapArgs() {
        ValueBox[] valueBoxArr;
        List<Value> l = new ArrayList<>();
        for (ValueBox element : this.bsmArgBoxes) {
            l.add(element.getValue());
        }
        return l;
    }

    @Override // soot.jimple.DynamicInvokeExpr
    public SootMethodRef getBootstrapMethodRef() {
        return this.bsmRef;
    }

    public SootMethod getBootstrapMethod() {
        return this.bsmRef.resolve();
    }

    @Override // soot.jimple.DynamicInvokeExpr
    public int getHandleTag() {
        return this.tag;
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((ExprSwitch) sw).caseDynamicInvokeExpr(this);
    }

    @Override // soot.EquivTo
    public boolean equivTo(Object o) {
        ValueBox[] valueBoxArr;
        ValueBox[] valueBoxArr2;
        if (o instanceof GDynamicInvokeExpr) {
            GDynamicInvokeExpr ie = (GDynamicInvokeExpr) o;
            if ((this.argBoxes == null ? 0 : this.argBoxes.length) != (ie.argBoxes == null ? 0 : ie.argBoxes.length) || this.bsmArgBoxes.length != ie.bsmArgBoxes.length || !getMethod().equals(ie.getMethod()) || !this.methodRef.equals(ie.methodRef) || !this.bsmRef.equals(ie.bsmRef)) {
                return false;
            }
            int i = 0;
            for (ValueBox element : this.bsmArgBoxes) {
                if (!element.getValue().equivTo(ie.getBootstrapArg(i))) {
                    return false;
                }
                i++;
            }
            if (this.argBoxes != null) {
                int i2 = 0;
                for (ValueBox element2 : this.argBoxes) {
                    if (!element2.getValue().equivTo(ie.getArg(i2))) {
                        return false;
                    }
                    i2++;
                }
                return true;
            }
            return true;
        }
        return false;
    }

    @Override // soot.EquivTo
    public int equivHashCode() {
        return getBootstrapMethod().equivHashCode() * getMethod().equivHashCode() * 17;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder("dynamicinvoke \"");
        buf.append(this.methodRef.name());
        buf.append("\" <");
        buf.append(SootMethod.getSubSignature("", this.methodRef.parameterTypes(), this.methodRef.returnType()));
        buf.append(">(");
        if (this.argBoxes != null) {
            int e = this.argBoxes.length;
            for (int i = 0; i < e; i++) {
                if (i != 0) {
                    buf.append(", ");
                }
                buf.append(this.argBoxes[i].getValue().toString());
            }
        }
        buf.append(") ");
        buf.append(this.bsmRef.getSignature());
        buf.append('(');
        int e2 = this.bsmArgBoxes.length;
        for (int i2 = 0; i2 < e2; i2++) {
            if (i2 != 0) {
                buf.append(", ");
            }
            buf.append(this.bsmArgBoxes[i2].getValue().toString());
        }
        buf.append(')');
        return buf.toString();
    }

    @Override // soot.Value
    public void toString(UnitPrinter up) {
        up.literal("dynamicinvoke \"" + this.methodRef.name() + "\" <" + SootMethod.getSubSignature("", this.methodRef.parameterTypes(), this.methodRef.returnType()) + ">(");
        if (this.argBoxes != null) {
            int e = this.argBoxes.length;
            for (int i = 0; i < e; i++) {
                if (i != 0) {
                    up.literal(", ");
                }
                this.argBoxes[i].toString(up);
            }
        }
        up.literal(") ");
        up.methodRef(this.bsmRef);
        up.literal("(");
        int e2 = this.bsmArgBoxes.length;
        for (int i2 = 0; i2 < e2; i2++) {
            if (i2 != 0) {
                up.literal(", ");
            }
            this.bsmArgBoxes[i2].toString(up);
        }
        up.literal(")");
    }
}
