package soot.jimple.internal;

import java.util.List;
import java.util.ListIterator;
import soot.SootMethodRef;
import soot.Unit;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.baf.Baf;
import soot.coffi.Instruction;
import soot.jimple.ConvertToBaf;
import soot.jimple.ExprSwitch;
import soot.jimple.Jimple;
import soot.jimple.JimpleToBafContext;
import soot.jimple.StaticInvokeExpr;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/AbstractStaticInvokeExpr.class */
public abstract class AbstractStaticInvokeExpr extends AbstractInvokeExpr implements StaticInvokeExpr, ConvertToBaf {
    AbstractStaticInvokeExpr(SootMethodRef methodRef, List<Value> args) {
        this(methodRef, new ValueBox[args.size()]);
        Jimple jimp = Jimple.v();
        ListIterator<Value> it = args.listIterator();
        while (it.hasNext()) {
            Value v = it.next();
            this.argBoxes[it.previousIndex()] = jimp.newImmediateBox(v);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractStaticInvokeExpr(SootMethodRef methodRef, ValueBox[] argBoxes) {
        super(methodRef, argBoxes);
        if (!methodRef.isStatic()) {
            throw new RuntimeException("wrong static-ness");
        }
    }

    @Override // soot.EquivTo
    public boolean equivTo(Object o) {
        if (o instanceof AbstractStaticInvokeExpr) {
            AbstractStaticInvokeExpr ie = (AbstractStaticInvokeExpr) o;
            if ((this.argBoxes == null ? 0 : this.argBoxes.length) != (ie.argBoxes == null ? 0 : ie.argBoxes.length) || !getMethod().equals(ie.getMethod())) {
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

    public String toString() {
        StringBuilder buf = new StringBuilder("staticinvoke ");
        buf.append(this.methodRef.getSignature()).append('(');
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
        up.literal(Jimple.STATICINVOKE);
        up.literal(Instruction.argsep);
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
        ((ExprSwitch) sw).caseStaticInvokeExpr(this);
    }

    @Override // soot.jimple.ConvertToBaf
    public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
        ValueBox[] valueBoxArr;
        if (this.argBoxes != null) {
            for (ValueBox element : this.argBoxes) {
                ((ConvertToBaf) element.getValue()).convertToBaf(context, out);
            }
        }
        Unit u = Baf.v().newStaticInvokeInst(this.methodRef);
        out.add(u);
        u.addAllTagsOf(context.getCurrentUnit());
    }
}
