package soot.baf.internal;

import java.util.Iterator;
import java.util.List;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.UnitPrinter;
import soot.Value;
import soot.VoidType;
import soot.baf.DynamicInvokeInst;
import soot.baf.InstSwitch;
import soot.jimple.Jimple;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BDynamicInvokeInst.class */
public class BDynamicInvokeInst extends AbstractInvokeInst implements DynamicInvokeInst {
    protected final SootMethodRef bsmRef;
    private final List<Value> bsmArgs;
    protected int tag;

    public BDynamicInvokeInst(SootMethodRef bsmMethodRef, List<Value> bsmArgs, SootMethodRef methodRef, int tag) {
        this.methodRef = methodRef;
        this.bsmRef = bsmMethodRef;
        this.bsmArgs = bsmArgs;
        this.tag = tag;
    }

    @Override // soot.baf.internal.AbstractInst, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new BDynamicInvokeInst(this.bsmRef, this.bsmArgs, this.methodRef, this.tag);
    }

    @Override // soot.baf.internal.AbstractInvokeInst, soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInCount() {
        return this.methodRef.getParameterTypes().size();
    }

    @Override // soot.baf.internal.AbstractInvokeInst, soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutCount() {
        return this.methodRef.getReturnType() instanceof VoidType ? 0 : 1;
    }

    @Override // soot.baf.DynamicInvokeInst
    public SootMethodRef getBootstrapMethodRef() {
        return this.bsmRef;
    }

    @Override // soot.baf.DynamicInvokeInst
    public List<Value> getBootstrapArgs() {
        return this.bsmArgs;
    }

    @Override // soot.baf.internal.AbstractInvokeInst, soot.baf.internal.AbstractInst
    public String getName() {
        return Jimple.DYNAMICINVOKE;
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((InstSwitch) sw).caseDynamicInvokeInst(this);
    }

    @Override // soot.baf.internal.AbstractInvokeInst, soot.baf.internal.AbstractInst
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("dynamicinvoke \"");
        buffer.append(this.methodRef.getName());
        buffer.append("\" <");
        buffer.append(SootMethod.getSubSignature("", this.methodRef.getParameterTypes(), this.methodRef.getReturnType()));
        buffer.append('>');
        buffer.append(this.bsmRef.getSignature());
        buffer.append('(');
        Iterator<Value> it = this.bsmArgs.iterator();
        while (it.hasNext()) {
            Value v = it.next();
            buffer.append(v.toString());
            if (it.hasNext()) {
                buffer.append(", ");
            }
        }
        buffer.append(')');
        return buffer.toString();
    }

    @Override // soot.baf.internal.AbstractInst, soot.Unit
    public void toString(UnitPrinter up) {
        up.literal("dynamicinvoke \"");
        up.literal(this.methodRef.getName());
        up.literal("\" <");
        up.literal(SootMethod.getSubSignature("", this.methodRef.getParameterTypes(), this.methodRef.getReturnType()));
        up.literal("> ");
        up.methodRef(this.bsmRef);
        up.literal("(");
        Iterator<Value> it = this.bsmArgs.iterator();
        while (it.hasNext()) {
            Value v = it.next();
            v.toString(up);
            if (it.hasNext()) {
                up.literal(", ");
            }
        }
        up.literal(")");
    }

    @Override // soot.baf.DynamicInvokeInst
    public int getHandleTag() {
        return this.tag;
    }
}
