package soot.jimple.spark.geom.geomPA;

import java.io.PrintStream;
import java.util.Set;
import soot.jimple.spark.geom.dataMgr.PtSensVisitor;
import soot.jimple.spark.geom.dataRep.PlainConstraint;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.Node;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/geom/geomPA/DummyNode.class */
public class DummyNode extends IVarAbstraction {
    public DummyNode(Node thisVarNode) {
        this.me = thisVarNode;
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public void deleteAll() {
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public boolean add_points_to_3(AllocNode obj, long I1, long I2, long L) {
        return false;
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public boolean add_points_to_4(AllocNode obj, long I1, long I2, long L1, long L2) {
        return false;
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public boolean add_simple_constraint_3(IVarAbstraction qv, long I1, long I2, long L) {
        return false;
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public boolean add_simple_constraint_4(IVarAbstraction qv, long I1, long I2, long L1, long L2) {
        return false;
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public void put_complex_constraint(PlainConstraint cons) {
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public void reconstruct() {
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public void do_before_propagation() {
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public void do_after_propagation() {
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public void propagate(GeomPointsTo ptAnalyzer, IWorklist worklist) {
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public void drop_duplicates() {
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public void remove_points_to(AllocNode obj) {
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public int num_of_diff_objs() {
        return -1;
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public int num_of_diff_edges() {
        return -1;
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public int count_pts_intervals(AllocNode obj) {
        return 0;
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public int count_new_pts_intervals() {
        return 0;
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public int count_flow_intervals(IVarAbstraction qv) {
        return 0;
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public boolean heap_sensitive_intersection(IVarAbstraction qv) {
        return false;
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public boolean pointer_interval_points_to(long l, long r, AllocNode obj) {
        return false;
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public Set<AllocNode> get_all_points_to_objects() {
        return null;
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public void print_context_sensitive_points_to(PrintStream outPrintStream) {
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public void keepPointsToOnly() {
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public void injectPts() {
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public boolean isDeadObject(AllocNode obj) {
        return false;
    }

    @Override // soot.jimple.spark.geom.geomPA.IVarAbstraction
    public void get_all_context_sensitive_objects(long l, long r, PtSensVisitor visitor) {
    }
}
