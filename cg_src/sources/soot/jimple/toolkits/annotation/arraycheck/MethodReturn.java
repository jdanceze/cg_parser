package soot.jimple.toolkits.annotation.arraycheck;

import soot.SootMethod;
import soot.Type;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/arraycheck/MethodReturn.class */
class MethodReturn {
    private SootMethod m;

    public MethodReturn(SootMethod m) {
        this.m = m;
    }

    public SootMethod getMethod() {
        return this.m;
    }

    public Type getType() {
        return this.m.getReturnType();
    }

    public int hashCode() {
        return this.m.hashCode() + this.m.getParameterCount();
    }

    public boolean equals(Object other) {
        if (other instanceof MethodReturn) {
            return this.m.equals(((MethodReturn) other).getMethod());
        }
        return false;
    }

    public String toString() {
        return "[" + this.m.getSignature() + " : R]";
    }
}
