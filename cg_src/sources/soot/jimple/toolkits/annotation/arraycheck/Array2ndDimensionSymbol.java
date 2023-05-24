package soot.jimple.toolkits.annotation.arraycheck;

import soot.G;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/arraycheck/Array2ndDimensionSymbol.class */
public class Array2ndDimensionSymbol {
    private Object var;

    public static Array2ndDimensionSymbol v(Object which) {
        Array2ndDimensionSymbol tdal = G.v().Array2ndDimensionSymbol_pool.get(which);
        if (tdal == null) {
            tdal = new Array2ndDimensionSymbol(which);
            G.v().Array2ndDimensionSymbol_pool.put(which, tdal);
        }
        return tdal;
    }

    private Array2ndDimensionSymbol(Object which) {
        this.var = which;
    }

    public Object getVar() {
        return this.var;
    }

    public int hashCode() {
        return this.var.hashCode() + 1;
    }

    public boolean equals(Object other) {
        if (other instanceof Array2ndDimensionSymbol) {
            Array2ndDimensionSymbol another = (Array2ndDimensionSymbol) other;
            return this.var == another.var;
        }
        return false;
    }

    public String toString() {
        return this.var + "[";
    }
}
