package soot.jimple.spark.geom.dataRep;

import soot.jimple.spark.pag.Node;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/geom/dataRep/CallsiteContextVar.class */
public class CallsiteContextVar extends ContextVar {
    public CgEdge context;

    public CallsiteContextVar() {
        this.context = null;
    }

    public CallsiteContextVar(CgEdge c, Node v) {
        this.context = null;
        this.context = c;
        this.var = v;
    }

    public CallsiteContextVar(CallsiteContextVar o) {
        this.context = null;
        this.context = o.context;
        this.var = o.var;
    }

    public String toString() {
        return "<" + this.context.toString() + ", " + this.var.toString() + ">";
    }

    public boolean equals(Object o) {
        CallsiteContextVar other = (CallsiteContextVar) o;
        return other.context == this.context && other.var == this.var;
    }

    public int hashCode() {
        int ch = 0;
        if (this.context != null) {
            ch = this.context.hashCode();
        }
        int ans = this.var.hashCode() + ch;
        if (ans < 0) {
            ans = -ans;
        }
        return ans;
    }

    @Override // soot.jimple.spark.geom.dataRep.ContextVar
    public boolean contains(ContextVar cv) {
        CallsiteContextVar ccv = (CallsiteContextVar) cv;
        if (this.context == ccv.context) {
            return true;
        }
        return false;
    }

    @Override // soot.jimple.spark.geom.dataRep.ContextVar
    public boolean merge(ContextVar cv) {
        return false;
    }

    @Override // soot.jimple.spark.geom.dataRep.ContextVar
    public boolean intersect(ContextVar cv) {
        return contains(cv);
    }
}
