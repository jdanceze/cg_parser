package soot.jimple.spark.geom.geomPA;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/geom/geomPA/PQ_Worklist.class */
public class PQ_Worklist implements IWorklist {
    private IVarAbstraction[] heap = null;
    int cur_tail = 0;

    @Override // soot.jimple.spark.geom.geomPA.IWorklist
    public void initialize(int size) {
        this.heap = new IVarAbstraction[size];
        this.cur_tail = 1;
    }

    @Override // soot.jimple.spark.geom.geomPA.IWorklist
    public boolean has_job() {
        return this.cur_tail > 1;
    }

    @Override // soot.jimple.spark.geom.geomPA.IWorklist
    public IVarAbstraction next() {
        int k;
        IVarAbstraction ret = this.heap[1];
        this.cur_tail--;
        if (this.cur_tail > 1) {
            IVarAbstraction e = this.heap[this.cur_tail];
            int i = 1;
            while (true) {
                k = i;
                if (k * 2 >= this.cur_tail) {
                    break;
                }
                int kk = k * 2;
                if (kk + 1 < this.cur_tail && this.heap[kk + 1].lessThan(this.heap[kk])) {
                    kk++;
                }
                if (e.lessThan(this.heap[kk])) {
                    break;
                }
                this.heap[k] = this.heap[kk];
                this.heap[k].Qpos = k;
                i = kk;
            }
            e.Qpos = k;
            this.heap[k] = e;
        }
        ret.Qpos = 0;
        return ret;
    }

    @Override // soot.jimple.spark.geom.geomPA.IWorklist
    public void push(IVarAbstraction e) {
        int k;
        int k2;
        e.lrf_value++;
        if (e.Qpos == 0) {
            int i = this.cur_tail;
            while (true) {
                k2 = i;
                if (k2 <= 1) {
                    break;
                }
                int kk = k2 / 2;
                if (this.heap[kk].lessThan(e)) {
                    break;
                }
                this.heap[k2] = this.heap[kk];
                this.heap[k2].Qpos = k2;
                i = k2 / 2;
            }
            e.Qpos = k2;
            this.heap[k2] = e;
            this.cur_tail++;
            return;
        }
        int i2 = e.Qpos;
        while (true) {
            k = i2;
            if (k * 2 >= this.cur_tail) {
                break;
            }
            int kk2 = k * 2;
            if (kk2 + 1 < this.cur_tail && this.heap[kk2 + 1].lessThan(this.heap[kk2])) {
                kk2++;
            }
            if (e.lessThan(this.heap[kk2])) {
                break;
            }
            this.heap[k] = this.heap[kk2];
            this.heap[kk2].Qpos = k;
            i2 = kk2;
        }
        e.Qpos = k;
        this.heap[k] = e;
    }

    @Override // soot.jimple.spark.geom.geomPA.IWorklist
    public int size() {
        return this.cur_tail - 1;
    }

    @Override // soot.jimple.spark.geom.geomPA.IWorklist
    public void clear() {
        this.cur_tail = 1;
    }
}
