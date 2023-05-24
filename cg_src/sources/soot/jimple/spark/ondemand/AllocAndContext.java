package soot.jimple.spark.ondemand;

import soot.jimple.spark.ondemand.genericutil.ImmutableStack;
import soot.jimple.spark.pag.AllocNode;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/AllocAndContext.class */
public class AllocAndContext {
    public final AllocNode alloc;
    public final ImmutableStack<Integer> context;

    public AllocAndContext(AllocNode alloc, ImmutableStack<Integer> context) {
        this.alloc = alloc;
        this.context = context;
    }

    public String toString() {
        return this.alloc + ", context " + this.context;
    }

    public int hashCode() {
        int result = (31 * 1) + this.alloc.hashCode();
        return (31 * result) + this.context.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AllocAndContext other = (AllocAndContext) obj;
        if (!this.alloc.equals(other.alloc) || !this.context.equals(other.context)) {
            return false;
        }
        return true;
    }
}
