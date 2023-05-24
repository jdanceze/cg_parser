package soot.jimple.spark.geom.dataMgr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import soot.PointsToSet;
import soot.Scene;
import soot.jimple.spark.geom.dataRep.ContextVar;
import soot.jimple.spark.geom.geomPA.GeomPointsTo;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.spark.sets.PointsToSetInternal;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/geom/dataMgr/PtSensVisitor.class */
public abstract class PtSensVisitor<VarType extends ContextVar> {
    protected boolean readyToUse = false;
    protected GeomPointsTo ptsProvider = (GeomPointsTo) Scene.v().getPointsToAnalysis();
    public List<VarType> outList = new ArrayList();
    protected Map<Node, List<VarType>> tableView = new HashMap();

    public abstract boolean visit(Node node, long j, long j2, int i);

    public void prepare() {
        this.tableView.clear();
        this.readyToUse = false;
    }

    public void finish() {
        if (!this.readyToUse) {
            this.readyToUse = true;
            this.outList.clear();
            if (this.tableView.size() == 0) {
                return;
            }
            for (Map.Entry<Node, List<VarType>> entry : this.tableView.entrySet()) {
                List<VarType> resList = entry.getValue();
                this.outList.addAll(resList);
            }
        }
    }

    public boolean getUsageState() {
        return this.readyToUse;
    }

    public int numOfDiffObjects() {
        return this.readyToUse ? this.outList.size() : this.tableView.size();
    }

    public boolean hasNonEmptyIntersection(PtSensVisitor<VarType> other) {
        for (Map.Entry<Node, List<VarType>> entry : this.tableView.entrySet()) {
            Node var = entry.getKey();
            List<VarType> list1 = entry.getValue();
            List<VarType> list2 = other.getCSList(var);
            if (list1.size() != 0 && list2.size() != 0) {
                for (VarType cv1 : list1) {
                    for (VarType cv2 : list2) {
                        if (cv1.intersect(cv2)) {
                            return true;
                        }
                    }
                }
                continue;
            }
        }
        return false;
    }

    public List<VarType> getCSList(Node var) {
        return this.tableView.get(var);
    }

    public PointsToSet toSparkCompatiableResult(VarNode vn) {
        if (!this.readyToUse) {
            finish();
        }
        PointsToSetInternal ptset = vn.makeP2Set();
        for (VarType cv : this.outList) {
            ptset.add(cv.var);
        }
        return ptset;
    }

    public void debugPrint() {
        if (!this.readyToUse) {
            finish();
        }
        for (VarType cv : this.outList) {
            System.out.printf("\t%s\n", cv.toString());
        }
    }
}
