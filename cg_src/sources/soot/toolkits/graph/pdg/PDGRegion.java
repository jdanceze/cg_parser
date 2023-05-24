package soot.toolkits.graph.pdg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.options.Options;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.graph.pdg.PDGNode;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/pdg/PDGRegion.class */
public class PDGRegion implements IRegion, Iterable<PDGNode> {
    private static final Logger logger = LoggerFactory.getLogger(PDGRegion.class);
    private SootClass m_class;
    private SootMethod m_method;
    private List<PDGNode> m_nodes;
    private List<Unit> m_units;
    private LinkedHashMap<Unit, PDGNode> m_unit2pdgnode;
    private int m_id;
    private UnitGraph m_unitGraph;
    private PDGNode m_corrspondingPDGNode;
    private IRegion m_parent;
    private List<IRegion> m_children;

    public PDGRegion(int id, SootMethod m, SootClass c, UnitGraph ug, PDGNode node) {
        this(id, new ArrayList(), m, c, ug, node);
    }

    public PDGRegion(int id, List<PDGNode> nodes, SootMethod m, SootClass c, UnitGraph ug, PDGNode node) {
        this.m_class = null;
        this.m_method = null;
        this.m_nodes = null;
        this.m_units = null;
        this.m_unit2pdgnode = null;
        this.m_id = -1;
        this.m_unitGraph = null;
        this.m_corrspondingPDGNode = null;
        this.m_parent = null;
        this.m_children = new ArrayList();
        this.m_nodes = nodes;
        this.m_id = id;
        this.m_method = m;
        this.m_class = c;
        this.m_unitGraph = ug;
        this.m_units = null;
        this.m_corrspondingPDGNode = node;
        if (Options.v().verbose()) {
            logger.debug("New pdg region create: " + id);
        }
    }

    public PDGRegion(PDGNode node) {
        this(((IRegion) node.getNode()).getID(), new ArrayList(), ((IRegion) node.getNode()).getSootMethod(), ((IRegion) node.getNode()).getSootClass(), ((IRegion) node.getNode()).getUnitGraph(), node);
    }

    public PDGNode getCorrespondingPDGNode() {
        return this.m_corrspondingPDGNode;
    }

    public Object clone() {
        PDGRegion r = new PDGRegion(this.m_id, this.m_method, this.m_class, this.m_unitGraph, this.m_corrspondingPDGNode);
        r.m_nodes = (List) ((ArrayList) this.m_nodes).clone();
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

    public List<PDGNode> getNodes() {
        return this.m_nodes;
    }

    @Override // soot.toolkits.graph.pdg.IRegion
    public UnitGraph getUnitGraph() {
        return this.m_unitGraph;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:soot/toolkits/graph/pdg/PDGRegion$ChildPDGFlowIterator.class */
    public class ChildPDGFlowIterator implements Iterator<PDGNode> {
        List<PDGNode> m_list;
        PDGNode m_current = null;
        boolean beginning = true;

        public ChildPDGFlowIterator(List<PDGNode> list) {
            this.m_list = null;
            this.m_list = list;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (!this.beginning || this.m_list.size() <= 0) {
                return (this.m_current == null || this.m_current.getNext() == null) ? false : true;
            }
            return true;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        /* JADX WARN: Removed duplicated region for block: B:13:0x005c  */
        @Override // java.util.Iterator
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public soot.toolkits.graph.pdg.PDGNode next() {
            /*
                r4 = this;
                r0 = r4
                boolean r0 = r0.beginning
                if (r0 == 0) goto La8
                r0 = r4
                r1 = 0
                r0.beginning = r1
                r0 = r4
                r1 = r4
                java.util.List<soot.toolkits.graph.pdg.PDGNode> r1 = r1.m_list
                r2 = 0
                java.lang.Object r1 = r1.get(r2)
                soot.toolkits.graph.pdg.PDGNode r1 = (soot.toolkits.graph.pdg.PDGNode) r1
                r0.m_current = r1
                goto L2b
            L20:
                r0 = r4
                r1 = r4
                soot.toolkits.graph.pdg.PDGNode r1 = r1.m_current
                soot.toolkits.graph.pdg.PDGNode r1 = r1.getPrev()
                r0.m_current = r1
            L2b:
                r0 = r4
                soot.toolkits.graph.pdg.PDGNode r0 = r0.m_current
                soot.toolkits.graph.pdg.PDGNode r0 = r0.getPrev()
                if (r0 != 0) goto L20
                r0 = r4
                soot.toolkits.graph.pdg.PDGNode r0 = r0.m_current
                soot.toolkits.graph.pdg.PDGNode$Type r0 = r0.getType()
                soot.toolkits.graph.pdg.PDGNode$Type r1 = soot.toolkits.graph.pdg.PDGNode.Type.CFGNODE
                if (r0 == r1) goto La3
                r0 = r4
                soot.toolkits.graph.pdg.PDGNode r0 = r0.m_current
                soot.toolkits.graph.pdg.PDGNode$Attribute r0 = r0.getAttrib()
                soot.toolkits.graph.pdg.PDGNode$Attribute r1 = soot.toolkits.graph.pdg.PDGNode.Attribute.LOOPHEADER
                if (r0 == r1) goto La3
                r0 = r4
                java.util.List<soot.toolkits.graph.pdg.PDGNode> r0 = r0.m_list
                java.util.Iterator r0 = r0.iterator()
                r5 = r0
                goto L9a
            L5c:
                r0 = r5
                java.lang.Object r0 = r0.next()
                soot.toolkits.graph.pdg.PDGNode r0 = (soot.toolkits.graph.pdg.PDGNode) r0
                r6 = r0
                r0 = r6
                soot.toolkits.graph.pdg.PDGNode$Type r0 = r0.getType()
                soot.toolkits.graph.pdg.PDGNode$Type r1 = soot.toolkits.graph.pdg.PDGNode.Type.CFGNODE
                if (r0 == r1) goto L7a
                r0 = r6
                soot.toolkits.graph.pdg.PDGNode$Attribute r0 = r0.getAttrib()
                soot.toolkits.graph.pdg.PDGNode$Attribute r1 = soot.toolkits.graph.pdg.PDGNode.Attribute.LOOPHEADER
                if (r0 != r1) goto L9a
            L7a:
                r0 = r4
                r1 = r6
                r0.m_current = r1
                goto L8d
            L82:
                r0 = r4
                r1 = r4
                soot.toolkits.graph.pdg.PDGNode r1 = r1.m_current
                soot.toolkits.graph.pdg.PDGNode r1 = r1.getPrev()
                r0.m_current = r1
            L8d:
                r0 = r4
                soot.toolkits.graph.pdg.PDGNode r0 = r0.m_current
                soot.toolkits.graph.pdg.PDGNode r0 = r0.getPrev()
                if (r0 != 0) goto L82
                goto La3
            L9a:
                r0 = r5
                boolean r0 = r0.hasNext()
                if (r0 != 0) goto L5c
            La3:
                r0 = r4
                soot.toolkits.graph.pdg.PDGNode r0 = r0.m_current
                return r0
            La8:
                r0 = r4
                boolean r0 = r0.hasNext()
                if (r0 != 0) goto Lb9
                java.lang.RuntimeException r0 = new java.lang.RuntimeException
                r1 = r0
                java.lang.String r2 = "No more nodes!"
                r1.<init>(r2)
                throw r0
            Lb9:
                r0 = r4
                r1 = r4
                soot.toolkits.graph.pdg.PDGNode r1 = r1.m_current
                soot.toolkits.graph.pdg.PDGNode r1 = r1.getNext()
                r0.m_current = r1
                r0 = r4
                soot.toolkits.graph.pdg.PDGNode r0 = r0.m_current
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: soot.toolkits.graph.pdg.PDGRegion.ChildPDGFlowIterator.next():soot.toolkits.graph.pdg.PDGNode");
        }

        @Override // java.util.Iterator
        public void remove() {
        }
    }

    @Override // java.lang.Iterable
    public Iterator<PDGNode> iterator() {
        return new ChildPDGFlowIterator(this.m_nodes);
    }

    @Override // soot.toolkits.graph.pdg.IRegion
    public List<Unit> getUnits() {
        if (this.m_units == null) {
            this.m_units = new LinkedList();
            this.m_unit2pdgnode = new LinkedHashMap<>();
            Iterator<PDGNode> itr = iterator();
            while (itr.hasNext()) {
                PDGNode node = itr.next();
                if (node.getType() == PDGNode.Type.REGION) {
                    if (node instanceof LoopedPDGNode) {
                        LoopedPDGNode n = (LoopedPDGNode) node;
                        PDGNode header = n.getHeader();
                        Block headerBlock = (Block) header.getNode();
                        Iterator<Unit> itr1 = headerBlock.iterator();
                        while (itr1.hasNext()) {
                            Unit u = itr1.next();
                            ((LinkedList) this.m_units).addLast(u);
                            this.m_unit2pdgnode.put(u, header);
                        }
                    }
                } else if (node.getType() == PDGNode.Type.CFGNODE) {
                    Block b = (Block) node.getNode();
                    Iterator<Unit> itr12 = b.iterator();
                    while (itr12.hasNext()) {
                        Unit u2 = itr12.next();
                        ((LinkedList) this.m_units).addLast(u2);
                        this.m_unit2pdgnode.put(u2, node);
                    }
                } else {
                    throw new RuntimeException("Exception in PDGRegion.getUnits: PDGNode's type is undefined!");
                }
            }
        }
        return this.m_units;
    }

    public PDGNode unit2PDGNode(Unit u) {
        if (this.m_unit2pdgnode.containsKey(u)) {
            return this.m_unit2pdgnode.get(u);
        }
        return null;
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

    @Override // soot.toolkits.graph.pdg.IRegion
    public List<Block> getBlocks() {
        return new ArrayList();
    }

    public void addPDGNode(PDGNode node) {
        this.m_nodes.add(node);
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
        String str = String.valueOf(new String()) + "Begin-----------PDGRegion:  " + this.m_id + "-------------\n";
        if (this.m_parent != null) {
            str = String.valueOf(str) + "Parent is: " + this.m_parent.getID() + "----\n";
        }
        String str2 = String.valueOf(str) + "Children Regions are: ";
        Iterator<IRegion> ritr = this.m_children.iterator();
        while (ritr.hasNext()) {
            str2 = String.valueOf(str2) + ritr.next().getID() + ", ";
        }
        String str3 = String.valueOf(str2) + "\nUnits are: \n";
        List<Unit> regionUnits = getUnits();
        for (Unit u : regionUnits) {
            str3 = String.valueOf(str3) + u + "\n";
        }
        return String.valueOf(str3) + "End of PDG Region " + this.m_id + " -----------------------------\n";
    }
}
