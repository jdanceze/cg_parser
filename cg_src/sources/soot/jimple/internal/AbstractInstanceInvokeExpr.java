package soot.jimple.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import soot.SootMethodRef;
import soot.Value;
import soot.ValueBox;
import soot.jimple.InstanceInvokeExpr;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/AbstractInstanceInvokeExpr.class */
public abstract class AbstractInstanceInvokeExpr extends AbstractInvokeExpr implements InstanceInvokeExpr {
    protected final ValueBox baseBox;

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractInstanceInvokeExpr(SootMethodRef methodRef, ValueBox baseBox, ValueBox[] argBoxes) {
        super(methodRef, argBoxes);
        this.baseBox = baseBox;
    }

    @Override // soot.jimple.InstanceInvokeExpr
    public Value getBase() {
        return this.baseBox.getValue();
    }

    @Override // soot.jimple.InstanceInvokeExpr
    public ValueBox getBaseBox() {
        return this.baseBox;
    }

    @Override // soot.jimple.InstanceInvokeExpr
    public void setBase(Value base) {
        this.baseBox.setValue(base);
    }

    @Override // soot.jimple.internal.AbstractInvokeExpr, soot.Value
    public List<ValueBox> getUseBoxes() {
        ValueBox[] valueBoxArr;
        List<ValueBox> list = new ArrayList<>(this.baseBox.getValue().getUseBoxes());
        list.add(this.baseBox);
        if (this.argBoxes != null) {
            Collections.addAll(list, this.argBoxes);
            for (ValueBox element : this.argBoxes) {
                list.addAll(element.getValue().getUseBoxes());
            }
        }
        return list;
    }
}
