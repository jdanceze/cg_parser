package soot.jimple.spark.geom.dataMgr;

import java.util.ArrayList;
import java.util.List;
import soot.jimple.spark.geom.dataRep.IntervalContextVar;
import soot.jimple.spark.pag.Node;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/geom/dataMgr/Obj_full_extractor.class */
public class Obj_full_extractor extends PtSensVisitor<IntervalContextVar> {
    private List<IntervalContextVar> backupList = new ArrayList();
    private IntervalContextVar tmp_icv = new IntervalContextVar();

    @Override // soot.jimple.spark.geom.dataMgr.PtSensVisitor
    public boolean visit(Node var, long L, long R, int sm_int) {
        List<IntervalContextVar> resList;
        if (this.readyToUse) {
            return false;
        }
        List<IntervalContextVar> resList2 = (List) this.tableView.get(var);
        if (resList2 == null) {
            resList = new ArrayList<>();
        } else {
            this.backupList.clear();
            this.tmp_icv.L = L;
            this.tmp_icv.R = R;
            for (IntervalContextVar old_cv : resList2) {
                if (old_cv.contains(this.tmp_icv)) {
                    return false;
                }
                if (!this.tmp_icv.merge(old_cv)) {
                    this.backupList.add(old_cv);
                }
            }
            List<IntervalContextVar> tmpList = this.backupList;
            this.backupList = resList2;
            resList = tmpList;
            L = this.tmp_icv.L;
            R = this.tmp_icv.R;
        }
        IntervalContextVar icv = new IntervalContextVar(L, R, var);
        resList.add(icv);
        this.tableView.put(var, resList);
        return true;
    }
}
