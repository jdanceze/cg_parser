package soot.jimple.spark.pag;

import java.util.Map;
import soot.G;
import soot.Scene;
import soot.SootMethod;
import soot.Type;
import soot.toolkits.scalar.Pair;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/pag/Parm.class */
public class Parm implements SparkField {
    private final int index;
    private final SootMethod method;
    private int number = 0;

    private Parm(SootMethod m, int i) {
        this.index = i;
        this.method = m;
        Scene.v().getFieldNumberer().add(this);
    }

    public static Parm v(SootMethod m, int index) {
        Pair<SootMethod, Integer> p = new Pair<>(m, new Integer(index));
        Parm ret = G.v().Parm_pairToElement.get(p);
        if (ret == null) {
            Map<Pair<SootMethod, Integer>, Parm> map = G.v().Parm_pairToElement;
            Parm parm = new Parm(m, index);
            ret = parm;
            map.put(p, parm);
        }
        return ret;
    }

    public static final void delete() {
        G.v().Parm_pairToElement = null;
    }

    public String toString() {
        return "Parm " + this.index + " to " + this.method;
    }

    @Override // soot.util.Numberable
    public final int getNumber() {
        return this.number;
    }

    @Override // soot.util.Numberable
    public final void setNumber(int number) {
        this.number = number;
    }

    public int getIndex() {
        return this.index;
    }

    @Override // soot.jimple.spark.pag.SparkField
    public Type getType() {
        if (this.index == -2) {
            return this.method.getReturnType();
        }
        return this.method.getParameterType(this.index);
    }
}
