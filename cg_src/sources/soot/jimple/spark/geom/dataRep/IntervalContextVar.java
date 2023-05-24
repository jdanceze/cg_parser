package soot.jimple.spark.geom.dataRep;

import soot.jimple.spark.pag.Node;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/geom/dataRep/IntervalContextVar.class */
public class IntervalContextVar extends ContextVar implements Comparable<IntervalContextVar> {
    public long L;
    public long R;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !IntervalContextVar.class.desiredAssertionStatus();
    }

    public IntervalContextVar() {
        this.L = 0L;
        this.R = 0L;
    }

    public IntervalContextVar(long l, long r, Node v) {
        this.L = 0L;
        this.R = 0L;
        if (!$assertionsDisabled && l >= r) {
            throw new AssertionError();
        }
        this.L = l;
        this.R = r;
        this.var = v;
    }

    public IntervalContextVar(IntervalContextVar o) {
        this.L = 0L;
        this.R = 0L;
        this.L = o.L;
        this.R = o.R;
        this.var = o.var;
    }

    public String toString() {
        return "<" + this.var.toString() + ", " + this.L + ", " + this.R + ">";
    }

    public boolean equals(Object o) {
        IntervalContextVar other = (IntervalContextVar) o;
        return other.L == this.L && other.R == this.R && other.var == this.var;
    }

    public int hashCode() {
        int ch = (int) ((this.L + this.R) % 2147483647L);
        int ans = this.var.hashCode() + ch;
        if (ans < 0) {
            ans = this.var.hashCode();
        }
        return ans;
    }

    @Override // java.lang.Comparable
    public int compareTo(IntervalContextVar o) {
        return this.L == o.L ? this.R < o.R ? -1 : 1 : this.L < o.L ? -1 : 1;
    }

    @Override // soot.jimple.spark.geom.dataRep.ContextVar
    public boolean contains(ContextVar cv) {
        IntervalContextVar icv = (IntervalContextVar) cv;
        if (this.L <= icv.L && this.R >= icv.R) {
            return true;
        }
        return false;
    }

    @Override // soot.jimple.spark.geom.dataRep.ContextVar
    public boolean merge(ContextVar cv) {
        IntervalContextVar icv = (IntervalContextVar) cv;
        if (icv.L < this.L) {
            if (this.L <= icv.R) {
                this.L = icv.L;
                if (this.R < icv.R) {
                    this.R = icv.R;
                    return true;
                }
                return true;
            }
            return false;
        } else if (icv.L <= this.R) {
            if (this.R < icv.R) {
                this.R = icv.R;
                return true;
            }
            return true;
        } else {
            return false;
        }
    }

    @Override // soot.jimple.spark.geom.dataRep.ContextVar
    public boolean intersect(ContextVar cv) {
        IntervalContextVar icv = (IntervalContextVar) cv;
        if (this.L > icv.L || icv.L >= this.R) {
            if (icv.L <= this.L && this.L < icv.R) {
                return true;
            }
            return false;
        }
        return true;
    }
}
