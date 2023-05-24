package soot.jimple.spark.geom.geomPA;

import java.util.Deque;
import java.util.LinkedList;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/geom/geomPA/FIFO_Worklist.class */
public class FIFO_Worklist implements IWorklist {
    Deque<IVarAbstraction> Q = null;

    @Override // soot.jimple.spark.geom.geomPA.IWorklist
    public void initialize(int size) {
        this.Q = new LinkedList();
    }

    @Override // soot.jimple.spark.geom.geomPA.IWorklist
    public boolean has_job() {
        return this.Q.size() != 0;
    }

    @Override // soot.jimple.spark.geom.geomPA.IWorklist
    public IVarAbstraction next() {
        IVarAbstraction t = this.Q.getFirst();
        this.Q.removeFirst();
        t.Qpos = 0;
        return t;
    }

    @Override // soot.jimple.spark.geom.geomPA.IWorklist
    public void push(IVarAbstraction pv) {
        if (pv.Qpos == 0) {
            this.Q.addLast(pv);
            pv.Qpos = 1;
        }
    }

    @Override // soot.jimple.spark.geom.geomPA.IWorklist
    public int size() {
        return this.Q.size();
    }

    @Override // soot.jimple.spark.geom.geomPA.IWorklist
    public void clear() {
        this.Q = null;
    }
}
