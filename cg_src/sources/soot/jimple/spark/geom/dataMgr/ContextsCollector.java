package soot.jimple.spark.geom.dataMgr;

import java.util.ArrayList;
import java.util.List;
import soot.jimple.spark.geom.dataRep.SimpleInterval;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/geom/dataMgr/ContextsCollector.class */
public class ContextsCollector {
    protected int nBudget = -1;
    public List<SimpleInterval> bars = new ArrayList();
    protected List<SimpleInterval> backupList = new ArrayList();
    protected SimpleInterval tmp_si = new SimpleInterval();

    public int getBudget() {
        return this.nBudget;
    }

    public int setBudget(int n) {
        int original = this.nBudget;
        this.nBudget = n;
        return original;
    }

    public boolean insert(long L, long R) {
        this.backupList.clear();
        this.tmp_si.L = L;
        this.tmp_si.R = R;
        long minL = L;
        long maxR = R;
        for (SimpleInterval old_si : this.bars) {
            if (old_si.contains(this.tmp_si)) {
                return false;
            }
            if (!this.tmp_si.merge(old_si)) {
                if (old_si.L < minL) {
                    minL = old_si.L;
                }
                if (old_si.R > maxR) {
                    maxR = old_si.R;
                }
                this.backupList.add(old_si);
            }
        }
        List<SimpleInterval> tmpList = this.backupList;
        this.backupList = this.bars;
        this.bars = tmpList;
        SimpleInterval new_si = new SimpleInterval(this.tmp_si);
        this.bars.add(new_si);
        if (this.nBudget != -1 && this.bars.size() > this.nBudget) {
            this.bars.clear();
            new_si.L = minL;
            new_si.R = maxR;
            this.bars.add(new_si);
            return true;
        }
        return true;
    }

    public void clear() {
        this.bars.clear();
    }
}
