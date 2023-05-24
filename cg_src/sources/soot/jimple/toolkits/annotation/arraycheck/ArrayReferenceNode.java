package soot.jimple.toolkits.annotation.arraycheck;

import soot.Local;
import soot.SootMethod;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/arraycheck/ArrayReferenceNode.class */
class ArrayReferenceNode {
    private final SootMethod m;
    private final Local l;

    public ArrayReferenceNode(SootMethod method, Local local) {
        this.m = method;
        this.l = local;
    }

    public SootMethod getMethod() {
        return this.m;
    }

    public Local getLocal() {
        return this.l;
    }

    public int hashCode() {
        return this.m.hashCode() + this.l.hashCode() + 1;
    }

    public boolean equals(Object other) {
        if (other instanceof ArrayReferenceNode) {
            ArrayReferenceNode another = (ArrayReferenceNode) other;
            return this.m.equals(another.getMethod()) && this.l.equals(another.getLocal());
        }
        return false;
    }

    public String toString() {
        return "[" + this.m.getSignature() + " : " + this.l.toString() + "[ ]";
    }
}
