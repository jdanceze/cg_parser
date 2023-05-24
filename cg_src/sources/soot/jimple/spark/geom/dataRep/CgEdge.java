package soot.jimple.spark.geom.dataRep;

import soot.jimple.spark.pag.VarNode;
import soot.jimple.toolkits.callgraph.Edge;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/geom/dataRep/CgEdge.class */
public class CgEdge {
    public Edge sootEdge;
    public int s;
    public int t;
    public long map_offset;
    public boolean scc_edge = false;
    public boolean is_obsoleted = false;
    public VarNode base_var = null;
    public CgEdge next;

    public CgEdge(int ss, int tt, Edge se, CgEdge ne) {
        this.next = null;
        this.s = ss;
        this.t = tt;
        this.sootEdge = se;
        this.next = ne;
    }

    public CgEdge duplicate() {
        CgEdge new_edge = new CgEdge(this.s, this.t, this.sootEdge, null);
        new_edge.map_offset = this.map_offset;
        new_edge.scc_edge = this.scc_edge;
        new_edge.base_var = this.base_var;
        return new_edge;
    }

    public String toString() {
        if (this.sootEdge != null) {
            return this.sootEdge.toString();
        }
        return "(" + this.s + "->" + this.t + ", " + this.map_offset + ")";
    }
}
