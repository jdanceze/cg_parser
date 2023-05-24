package soot.toolkits.graph.pdg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.UnitGraph;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/pdg/Region.class */
public class Region implements IRegion {
    private SootClass m_class;
    private SootMethod m_method;
    private List<Block> m_blocks;
    private List<Unit> m_units;
    private int m_id;
    private UnitGraph m_unitGraph;
    private IRegion m_parent;
    private List<IRegion> m_children;

    public Region(int id, SootMethod m, SootClass c, UnitGraph ug) {
        this(id, new ArrayList(), m, c, ug);
    }

    public Region(int id, List<Block> blocks, SootMethod m, SootClass c, UnitGraph ug) {
        this.m_class = null;
        this.m_method = null;
        this.m_blocks = null;
        this.m_units = null;
        this.m_id = -1;
        this.m_unitGraph = null;
        this.m_parent = null;
        this.m_children = new ArrayList();
        this.m_blocks = blocks;
        this.m_id = id;
        this.m_method = m;
        this.m_class = c;
        this.m_unitGraph = ug;
        this.m_units = null;
    }

    public Object clone() {
        Region r = new Region(this.m_id, this.m_method, this.m_class, this.m_unitGraph);
        r.m_blocks = (List) ((ArrayList) this.m_blocks).clone();
        return r;
    }

    @Override // soot.toolkits.graph.pdg.IRegion
    public SootMethod getSootMethod() {
        return this.m_method;
    }

    @Override // soot.toolkits.graph.pdg.IRegion
    public SootClass getSootClass() {
        return this.m_class;
    }

    @Override // soot.toolkits.graph.pdg.IRegion
    public List<Block> getBlocks() {
        return this.m_blocks;
    }

    @Override // soot.toolkits.graph.pdg.IRegion
    public UnitGraph getUnitGraph() {
        return this.m_unitGraph;
    }

    @Override // soot.toolkits.graph.pdg.IRegion
    public List<Unit> getUnits() {
        if (this.m_units == null) {
            this.m_units = new LinkedList();
            for (Block b : this.m_blocks) {
                Iterator<Unit> itr1 = b.iterator();
                while (itr1.hasNext()) {
                    Unit u = itr1.next();
                    ((LinkedList) this.m_units).addLast(u);
                }
            }
        }
        return this.m_units;
    }

    @Override // soot.toolkits.graph.pdg.IRegion
    public List<Unit> getUnits(Unit from, Unit to) {
        return this.m_units.subList(this.m_units.indexOf(from), this.m_units.indexOf(to));
    }

    @Override // soot.toolkits.graph.pdg.IRegion
    public Unit getLast() {
        if (this.m_units != null && this.m_units.size() > 0) {
            return (Unit) ((LinkedList) this.m_units).getLast();
        }
        return null;
    }

    @Override // soot.toolkits.graph.pdg.IRegion
    public Unit getFirst() {
        if (this.m_units != null && this.m_units.size() > 0) {
            return (Unit) ((LinkedList) this.m_units).getFirst();
        }
        return null;
    }

    public void add(Block b) {
        this.m_blocks.add(0, b);
    }

    public void add2Back(Block b) {
        this.m_blocks.add(this.m_blocks.size(), b);
    }

    public void remove(Block b) {
        this.m_blocks.remove(b);
        this.m_units = null;
    }

    @Override // soot.toolkits.graph.pdg.IRegion
    public int getID() {
        return this.m_id;
    }

    @Override // soot.toolkits.graph.pdg.IRegion
    public boolean occursBefore(Unit u1, Unit u2) {
        int i = this.m_units.lastIndexOf(u1);
        int j = this.m_units.lastIndexOf(u2);
        if (i == -1 || j == -1) {
            throw new RuntimeException("These units don't exist in the region!");
        }
        return i < j;
    }

    @Override // soot.toolkits.graph.pdg.IRegion
    public void setParent(IRegion pr) {
        this.m_parent = pr;
    }

    @Override // soot.toolkits.graph.pdg.IRegion
    public IRegion getParent() {
        return this.m_parent;
    }

    @Override // soot.toolkits.graph.pdg.IRegion
    public void addChildRegion(IRegion chr) {
        if (!this.m_children.contains(chr)) {
            this.m_children.add(chr);
        }
    }

    @Override // soot.toolkits.graph.pdg.IRegion
    public List<IRegion> getChildRegions() {
        return this.m_children;
    }

    public String toString() {
        String str = new String();
        String str2 = String.valueOf(str) + "Begin-----------Region:  " + this.m_id + "-------------\n";
        List<Unit> regionUnits = getUnits();
        for (Unit u : regionUnits) {
            str2 = String.valueOf(str2) + u + "\n";
        }
        return String.valueOf(str2) + "End Region " + this.m_id + " -----------------------------\n";
    }
}
