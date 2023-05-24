package soot.jimple.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Type;
import soot.Value;
import soot.ValueBox;
import soot.jimple.InvokeExpr;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/AbstractInvokeExpr.class */
public abstract class AbstractInvokeExpr implements InvokeExpr {
    protected SootMethodRef methodRef;
    protected final ValueBox[] argBoxes;

    @Override // soot.Value
    public abstract Object clone();

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractInvokeExpr(SootMethodRef methodRef, ValueBox[] argBoxes) {
        this.methodRef = methodRef;
        this.argBoxes = argBoxes.length == 0 ? null : argBoxes;
    }

    @Override // soot.jimple.InvokeExpr
    public void setMethodRef(SootMethodRef methodRef) {
        this.methodRef = methodRef;
    }

    @Override // soot.jimple.InvokeExpr
    public SootMethodRef getMethodRef() {
        return this.methodRef;
    }

    @Override // soot.jimple.InvokeExpr
    public SootMethod getMethod() {
        return this.methodRef.resolve();
    }

    @Override // soot.jimple.InvokeExpr
    public Value getArg(int index) {
        ValueBox vb;
        if (this.argBoxes == null || (vb = this.argBoxes[index]) == null) {
            return null;
        }
        return vb.getValue();
    }

    @Override // soot.jimple.InvokeExpr
    public List<Value> getArgs() {
        List<Value> r;
        ValueBox[] boxes = this.argBoxes;
        if (boxes == null) {
            r = new ArrayList<>(0);
        } else {
            r = new ArrayList<>(boxes.length);
            int length = boxes.length;
            for (int i = 0; i < length; i++) {
                ValueBox element = boxes[i];
                r.add(element == null ? null : element.getValue());
            }
        }
        return r;
    }

    @Override // soot.jimple.InvokeExpr
    public int getArgCount() {
        if (this.argBoxes == null) {
            return 0;
        }
        return this.argBoxes.length;
    }

    @Override // soot.jimple.InvokeExpr
    public void setArg(int index, Value arg) {
        this.argBoxes[index].setValue(arg);
    }

    @Override // soot.jimple.InvokeExpr
    public ValueBox getArgBox(int index) {
        return this.argBoxes[index];
    }

    @Override // soot.Value
    public Type getType() {
        return this.methodRef.returnType();
    }

    @Override // soot.Value
    public List<ValueBox> getUseBoxes() {
        ValueBox[] boxes = this.argBoxes;
        if (boxes == null) {
            return Collections.emptyList();
        }
        List<ValueBox> list = new ArrayList<>();
        Collections.addAll(list, boxes);
        for (ValueBox element : boxes) {
            list.addAll(element.getValue().getUseBoxes());
        }
        return list;
    }
}
