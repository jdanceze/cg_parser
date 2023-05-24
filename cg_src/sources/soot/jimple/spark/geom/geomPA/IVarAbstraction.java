package soot.jimple.spark.geom.geomPA;

import java.io.PrintStream;
import java.util.Set;
import soot.SootMethod;
import soot.Type;
import soot.jimple.spark.geom.dataMgr.PtSensVisitor;
import soot.jimple.spark.geom.dataRep.PlainConstraint;
import soot.jimple.spark.geom.dataRep.RectangleNode;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.LocalVarNode;
import soot.jimple.spark.pag.Node;
import soot.util.Numberable;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/geom/geomPA/IVarAbstraction.class */
public abstract class IVarAbstraction implements Numberable {
    protected static IFigureManager stubManager = null;
    protected static IFigureManager deadManager = null;
    protected static RectangleNode pres = null;
    public Node me;
    public int id = -1;
    public int Qpos = 0;
    public boolean willUpdate = false;
    public int top_value = 1;
    public int lrf_value = 0;
    protected IVarAbstraction parent = this;

    public abstract boolean add_points_to_3(AllocNode allocNode, long j, long j2, long j3);

    public abstract boolean add_points_to_4(AllocNode allocNode, long j, long j2, long j3, long j4);

    public abstract boolean add_simple_constraint_3(IVarAbstraction iVarAbstraction, long j, long j2, long j3);

    public abstract boolean add_simple_constraint_4(IVarAbstraction iVarAbstraction, long j, long j2, long j3, long j4);

    public abstract void put_complex_constraint(PlainConstraint plainConstraint);

    public abstract void reconstruct();

    public abstract void do_before_propagation();

    public abstract void do_after_propagation();

    public abstract void propagate(GeomPointsTo geomPointsTo, IWorklist iWorklist);

    public abstract void drop_duplicates();

    public abstract void remove_points_to(AllocNode allocNode);

    public abstract void deleteAll();

    public abstract void keepPointsToOnly();

    public abstract void injectPts();

    public abstract int num_of_diff_objs();

    public abstract int num_of_diff_edges();

    public abstract int count_pts_intervals(AllocNode allocNode);

    public abstract int count_new_pts_intervals();

    public abstract int count_flow_intervals(IVarAbstraction iVarAbstraction);

    public abstract boolean heap_sensitive_intersection(IVarAbstraction iVarAbstraction);

    public abstract boolean pointer_interval_points_to(long j, long j2, AllocNode allocNode);

    public abstract boolean isDeadObject(AllocNode allocNode);

    public abstract Set<AllocNode> get_all_points_to_objects();

    public abstract void get_all_context_sensitive_objects(long j, long j2, PtSensVisitor ptSensVisitor);

    public abstract void print_context_sensitive_points_to(PrintStream printStream);

    public boolean lessThan(IVarAbstraction other) {
        return this.lrf_value != other.lrf_value ? this.lrf_value < other.lrf_value : this.top_value < other.top_value;
    }

    public IVarAbstraction getRepresentative() {
        if (this.parent == this) {
            return this;
        }
        IVarAbstraction representative = this.parent.getRepresentative();
        this.parent = representative;
        return representative;
    }

    public IVarAbstraction merge(IVarAbstraction other) {
        getRepresentative();
        this.parent = other.getRepresentative();
        return this.parent;
    }

    @Override // soot.util.Numberable
    public void setNumber(int number) {
        this.id = number;
    }

    @Override // soot.util.Numberable
    public int getNumber() {
        return this.id;
    }

    public String toString() {
        if (this.me != null) {
            return this.me.toString();
        }
        return super.toString();
    }

    public boolean reachable() {
        return this.id != -1;
    }

    public boolean hasPTResult() {
        return num_of_diff_objs() != -1;
    }

    public Node getWrappedNode() {
        return this.me;
    }

    public Type getType() {
        return this.me.getType();
    }

    public boolean isLocalPointer() {
        return this.me instanceof LocalVarNode;
    }

    public SootMethod enclosingMethod() {
        if (this.me instanceof LocalVarNode) {
            return ((LocalVarNode) this.me).getMethod();
        }
        return null;
    }
}
