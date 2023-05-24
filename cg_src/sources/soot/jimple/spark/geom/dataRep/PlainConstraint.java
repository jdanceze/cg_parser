package soot.jimple.spark.geom.dataRep;

import java.util.Set;
import soot.jimple.spark.geom.geomPA.IVarAbstraction;
import soot.jimple.spark.pag.SparkField;
import soot.jimple.toolkits.callgraph.Edge;
import soot.toolkits.scalar.Pair;
import soot.util.Numberable;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/geom/dataRep/PlainConstraint.class */
public class PlainConstraint implements Numberable {
    public int type;
    public int code;
    public Pair<IVarAbstraction, IVarAbstraction> expr = new Pair<>();
    public IVarAbstraction otherSide = null;
    public SparkField f = null;
    public Set<Edge> interCallEdges = null;
    public boolean isActive = true;
    private int id = -1;

    @Override // soot.util.Numberable
    public void setNumber(int number) {
        this.id = number;
    }

    @Override // soot.util.Numberable
    public int getNumber() {
        return this.id;
    }

    public IVarAbstraction getLHS() {
        return this.expr.getO1();
    }

    public void setLHS(IVarAbstraction newLHS) {
        this.expr.setO1(newLHS);
    }

    public IVarAbstraction getRHS() {
        return this.expr.getO2();
    }

    public void setRHS(IVarAbstraction newRHS) {
        this.expr.setO2(newRHS);
    }
}
