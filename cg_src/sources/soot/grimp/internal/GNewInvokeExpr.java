package soot.grimp.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import soot.RefType;
import soot.SootMethodRef;
import soot.Type;
import soot.UnitPrinter;
import soot.Value;
import soot.grimp.Grimp;
import soot.grimp.GrimpValueSwitch;
import soot.grimp.NewInvokeExpr;
import soot.grimp.Precedence;
import soot.jimple.internal.AbstractInvokeExpr;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/grimp/internal/GNewInvokeExpr.class */
public class GNewInvokeExpr extends AbstractInvokeExpr implements NewInvokeExpr, Precedence {
    protected RefType type;

    public GNewInvokeExpr(RefType type, SootMethodRef methodRef, List<? extends Value> args) {
        super(methodRef, new ExprBox[args.size()]);
        if (methodRef != null && methodRef.isStatic()) {
            throw new RuntimeException("wrong static-ness");
        }
        this.type = type;
        Grimp grmp = Grimp.v();
        ListIterator<? extends Value> it = args.listIterator();
        while (it.hasNext()) {
            Value v = it.next();
            this.argBoxes[it.previousIndex()] = grmp.newExprBox(v);
        }
    }

    @Override // soot.grimp.NewInvokeExpr
    public RefType getBaseType() {
        return this.type;
    }

    @Override // soot.grimp.NewInvokeExpr
    public void setBaseType(RefType type) {
        this.type = type;
    }

    @Override // soot.jimple.internal.AbstractInvokeExpr, soot.Value
    public Type getType() {
        return this.type;
    }

    @Override // soot.grimp.Precedence
    public int getPrecedence() {
        return 850;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder("new ");
        buf.append(this.type.toString()).append('(');
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
        up.literal("new ");
        up.type(this.type);
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
        ((GrimpValueSwitch) sw).caseNewInvokeExpr(this);
    }

    @Override // soot.jimple.internal.AbstractInvokeExpr, soot.Value
    public Object clone() {
        int count = getArgCount();
        List<Value> clonedArgs = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            clonedArgs.add(Grimp.cloneIfNecessary(getArg(i)));
        }
        return new GNewInvokeExpr(getBaseType(), this.methodRef, clonedArgs);
    }

    @Override // soot.EquivTo
    public boolean equivTo(Object o) {
        if (o instanceof GNewInvokeExpr) {
            GNewInvokeExpr ie = (GNewInvokeExpr) o;
            if ((this.argBoxes == null ? 0 : this.argBoxes.length) != (ie.argBoxes == null ? 0 : ie.argBoxes.length) || !getMethod().equals(ie.getMethod()) || !this.type.equals(ie.type)) {
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
        return getMethod().equivHashCode();
    }
}
