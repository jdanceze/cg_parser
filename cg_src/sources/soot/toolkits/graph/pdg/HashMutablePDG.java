package soot.toolkits.graph.pdg;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import soot.Body;
import soot.SootClass;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.DominatorNode;
import soot.toolkits.graph.DominatorTree;
import soot.toolkits.graph.HashMutableEdgeLabelledDirectedGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.graph.pdg.PDGNode;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/pdg/HashMutablePDG.class */
public class HashMutablePDG extends HashMutableEdgeLabelledDirectedGraph<PDGNode, String> implements ProgramDependenceGraph {
    protected Body m_body;
    protected SootClass m_class;
    protected UnitGraph m_cfg;
    protected BlockGraph m_blockCFG;
    protected List<Region> m_weakRegions;
    protected List<Region> m_strongRegions;
    protected List<PDGRegion> m_pdgRegions;
    private RegionAnalysis m_regionAnalysis;
    private int m_strongRegionStartID;
    private static Hashtable<PDGNode, PDGRegion> node2Region;
    static final /* synthetic */ boolean $assertionsDisabled;
    protected Hashtable<Object, PDGNode> m_obj2pdgNode = new Hashtable<>();
    protected PDGNode m_startNode = null;

    static {
        $assertionsDisabled = !HashMutablePDG.class.desiredAssertionStatus();
        node2Region = new Hashtable<>();
    }

    public HashMutablePDG(UnitGraph cfg) {
        this.m_body = null;
        this.m_class = null;
        this.m_cfg = null;
        this.m_blockCFG = null;
        this.m_weakRegions = null;
        this.m_strongRegions = null;
        this.m_pdgRegions = null;
        this.m_regionAnalysis = null;
        this.m_body = cfg.getBody();
        this.m_class = this.m_body.getMethod().getDeclaringClass();
        this.m_cfg = cfg;
        this.m_regionAnalysis = new RegionAnalysis(this.m_cfg, this.m_body.getMethod(), this.m_class);
        this.m_strongRegions = this.m_regionAnalysis.getRegions();
        this.m_weakRegions = cloneRegions(this.m_strongRegions);
        this.m_blockCFG = this.m_regionAnalysis.getBlockCFG();
        constructPDG();
        this.m_pdgRegions = computePDGRegions(this.m_startNode);
        PDGRegion pDGRegion = this.m_pdgRegions.get(0);
        while (true) {
            IRegion r = pDGRegion;
            if (r.getParent() != null) {
                pDGRegion = r.getParent();
            } else {
                this.m_startNode.setNode(r);
                return;
            }
        }
    }

    @Override // soot.toolkits.graph.pdg.ProgramDependenceGraph
    public BlockGraph getBlockGraph() {
        return this.m_blockCFG;
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void constructPDG() {
        PDGNode pdgNodeOfA;
        PDGNode pdgnodeOfBRegion;
        PDGNode pdgnodeOfdepBRegion;
        PDGNode pdgnodeOfdepBRegion2;
        List<Block> blocks2BRemoved;
        Hashtable<Block, Region> block2region = this.m_regionAnalysis.getBlock2RegionMap();
        DominatorTree<Block> pdom = this.m_regionAnalysis.getPostDominatorTree();
        DominatorTree<Block> dom = this.m_regionAnalysis.getDominatorTree();
        List<Region> regions2process = new LinkedList<>();
        Region topLevelRegion = this.m_regionAnalysis.getTopLevelRegion();
        this.m_strongRegionStartID = this.m_weakRegions.size();
        PDGNode pdgnode = new PDGNode(topLevelRegion, PDGNode.Type.REGION);
        addNode(pdgnode);
        this.m_obj2pdgNode.put(topLevelRegion, pdgnode);
        this.m_startNode = pdgnode;
        topLevelRegion.setParent(null);
        Set<Region> processedRegions = new HashSet<>();
        regions2process.add(topLevelRegion);
        while (!regions2process.isEmpty()) {
            Region r = regions2process.remove(0);
            processedRegions.add(r);
            PDGNode pdgnode2 = this.m_obj2pdgNode.get(r);
            List<Block> blocks = r.getBlocks();
            Hashtable<Region, List<Block>> toBeRemoved = new Hashtable<>();
            PDGNode prevPDGNodeInRegion = null;
            for (Block a : blocks) {
                if (!this.m_obj2pdgNode.containsKey(a)) {
                    pdgNodeOfA = new PDGNode(a, PDGNode.Type.CFGNODE);
                    addNode(pdgNodeOfA);
                    this.m_obj2pdgNode.put(a, pdgNodeOfA);
                } else {
                    pdgNodeOfA = this.m_obj2pdgNode.get(a);
                }
                addEdge(pdgnode2, pdgNodeOfA, "dependency");
                pdgnode2.addDependent(pdgNodeOfA);
                PDGNode curNodeInRegion = pdgNodeOfA;
                for (Block b : this.m_blockCFG.getSuccsOf(a)) {
                    if (!b.equals(a)) {
                        DominatorNode<Block> aDode = pdom.getDode(a);
                        DominatorNode<Block> bDode = pdom.getDode(b);
                        if (pdom.isDominatorOf(bDode, aDode)) {
                            continue;
                        } else {
                            List<Block> dependents = new ArrayList<>();
                            DominatorNode<Block> aParentDode = aDode.getParent();
                            DominatorNode<Block> dominatorNode = bDode;
                            while (true) {
                                DominatorNode<Block> dode = dominatorNode;
                                if (dode == aParentDode) {
                                    break;
                                }
                                dependents.add(dode.getGode());
                                if (dode.getParent() == null) {
                                    break;
                                }
                                dominatorNode = dode.getParent();
                            }
                            PDGNode.Attribute attrib = pdgNodeOfA.getAttrib();
                            pdgNodeOfA = pdgNodeOfA;
                            if (attrib != PDGNode.Attribute.CONDHEADER) {
                                PDGNode oldA = pdgNodeOfA;
                                PDGNode pdgNodeOfA2 = new ConditionalPDGNode(pdgNodeOfA);
                                replaceInGraph(pdgNodeOfA2, oldA);
                                pdgnode2.removeDependent(oldA);
                                this.m_obj2pdgNode.put(a, pdgNodeOfA2);
                                pdgnode2.addDependent(pdgNodeOfA2);
                                pdgNodeOfA2.setAttrib(PDGNode.Attribute.CONDHEADER);
                                curNodeInRegion = pdgNodeOfA2;
                                pdgNodeOfA = pdgNodeOfA2;
                            }
                            List<Block> copyOfDependents = new ArrayList<>(dependents);
                            Region regionOfB = block2region.get(b);
                            if (this.m_obj2pdgNode.containsKey(regionOfB)) {
                                pdgnodeOfBRegion = this.m_obj2pdgNode.get(regionOfB);
                            } else {
                                pdgnodeOfBRegion = new PDGNode(regionOfB, PDGNode.Type.REGION);
                                addNode(pdgnodeOfBRegion);
                                this.m_obj2pdgNode.put(regionOfB, pdgnodeOfBRegion);
                            }
                            regionOfB.setParent(r);
                            r.addChildRegion(regionOfB);
                            addEdge(pdgNodeOfA, pdgnodeOfBRegion, "dependency");
                            pdgNodeOfA.addDependent(pdgnodeOfBRegion);
                            if (!processedRegions.contains(regionOfB)) {
                                regions2process.add(regionOfB);
                            }
                            copyOfDependents.remove(b);
                            copyOfDependents.removeAll(regionOfB.getBlocks());
                            while (!copyOfDependents.isEmpty()) {
                                Block depB = copyOfDependents.remove(0);
                                Region rdepB = block2region.get(depB);
                                PDGNode depBPDGNode = this.m_obj2pdgNode.get(depB);
                                if (depBPDGNode == null) {
                                    if (this.m_obj2pdgNode.containsKey(rdepB)) {
                                        pdgnodeOfdepBRegion = this.m_obj2pdgNode.get(rdepB);
                                    } else {
                                        pdgnodeOfdepBRegion = new PDGNode(rdepB, PDGNode.Type.REGION);
                                        addNode(pdgnodeOfdepBRegion);
                                        this.m_obj2pdgNode.put(rdepB, pdgnodeOfdepBRegion);
                                    }
                                    rdepB.setParent(regionOfB);
                                    regionOfB.addChildRegion(rdepB);
                                    addEdge(pdgnodeOfBRegion, pdgnodeOfdepBRegion, "dependency");
                                    pdgnodeOfBRegion.addDependent(pdgnodeOfdepBRegion);
                                    if (!processedRegions.contains(rdepB)) {
                                        regions2process.add(rdepB);
                                    }
                                    copyOfDependents.removeAll(rdepB.getBlocks());
                                } else if (dependents.containsAll(rdepB.getBlocks())) {
                                    if (this.m_obj2pdgNode.containsKey(rdepB)) {
                                        pdgnodeOfdepBRegion2 = this.m_obj2pdgNode.get(rdepB);
                                    } else {
                                        pdgnodeOfdepBRegion2 = new PDGNode(rdepB, PDGNode.Type.REGION);
                                        addNode(pdgnodeOfdepBRegion2);
                                        this.m_obj2pdgNode.put(rdepB, pdgnodeOfdepBRegion2);
                                    }
                                    addEdge(pdgnodeOfBRegion, pdgnodeOfdepBRegion2, "dependency");
                                    pdgnodeOfBRegion.addDependent(pdgnodeOfdepBRegion2);
                                    if (!processedRegions.contains(rdepB)) {
                                        regions2process.add(rdepB);
                                    }
                                    copyOfDependents.removeAll(rdepB.getBlocks());
                                } else {
                                    PDGNode predPDGofdepB = getPredsOf(depBPDGNode).get(0);
                                    if (!$assertionsDisabled && !this.m_obj2pdgNode.containsKey(rdepB)) {
                                        throw new AssertionError();
                                    }
                                    PDGNode pdgnodeOfdepBRegion3 = this.m_obj2pdgNode.get(rdepB);
                                    if (predPDGofdepB == pdgnodeOfdepBRegion3) {
                                        int i = this.m_strongRegionStartID;
                                        this.m_strongRegionStartID = i + 1;
                                        Region newRegion = new Region(i, topLevelRegion.getSootMethod(), topLevelRegion.getSootClass(), this.m_cfg);
                                        newRegion.add(depB);
                                        this.m_strongRegions.add(newRegion);
                                        if (toBeRemoved.contains(predPDGofdepB)) {
                                            blocks2BRemoved = toBeRemoved.get(predPDGofdepB);
                                        } else {
                                            blocks2BRemoved = new ArrayList<>();
                                            toBeRemoved.put(rdepB, blocks2BRemoved);
                                        }
                                        blocks2BRemoved.add(depB);
                                        LoopedPDGNode loopedPDGNode = new LoopedPDGNode(newRegion, PDGNode.Type.REGION, depBPDGNode);
                                        addNode(loopedPDGNode);
                                        this.m_obj2pdgNode.put(newRegion, loopedPDGNode);
                                        loopedPDGNode.setAttrib(PDGNode.Attribute.LOOPHEADER);
                                        depBPDGNode.setAttrib(PDGNode.Attribute.LOOPHEADER);
                                        removeEdge(pdgnodeOfdepBRegion3, depBPDGNode, "dependency");
                                        pdgnodeOfdepBRegion3.removeDependent(depBPDGNode);
                                        addEdge(pdgnodeOfdepBRegion3, loopedPDGNode, "dependency");
                                        addEdge(loopedPDGNode, depBPDGNode, "dependency");
                                        pdgnodeOfdepBRegion3.addDependent(loopedPDGNode);
                                        loopedPDGNode.addDependent(depBPDGNode);
                                        if (depB == a) {
                                            PDGNode loopBodyPDGNode = getSuccsOf(depBPDGNode).get(0);
                                            addEdge(depBPDGNode, loopedPDGNode, "dependency-back");
                                            loopedPDGNode.setBody(loopBodyPDGNode);
                                            depBPDGNode.addBackDependent(loopedPDGNode);
                                            curNodeInRegion = loopedPDGNode;
                                        } else {
                                            pdgnodeOfBRegion.addBackDependent(loopedPDGNode);
                                            addEdge(pdgnodeOfBRegion, loopedPDGNode, "dependency-back");
                                            PDGNode loopBodyPDGNode2 = null;
                                            Iterator<PDGNode> it = getSuccsOf(depBPDGNode).iterator();
                                            while (true) {
                                                if (!it.hasNext()) {
                                                    break;
                                                }
                                                PDGNode succRPDGNode = it.next();
                                                if (succRPDGNode.getType() == PDGNode.Type.REGION) {
                                                    Region succR = (Region) succRPDGNode.getNode();
                                                    Block h = succR.getBlocks().get(0);
                                                    if (dom.isDominatorOf(dom.getDode(h), dom.getDode(a))) {
                                                        loopBodyPDGNode2 = succRPDGNode;
                                                        break;
                                                    }
                                                }
                                            }
                                            if (!$assertionsDisabled && loopBodyPDGNode2 == null) {
                                                throw new AssertionError();
                                            }
                                            loopedPDGNode.setBody(loopBodyPDGNode2);
                                            PDGNode prev = depBPDGNode.getPrev();
                                            if (prev != null) {
                                                removeEdge(prev, depBPDGNode, "controlflow");
                                                addEdge(prev, loopedPDGNode, "controlflow");
                                                prev.setNext(loopedPDGNode);
                                                loopedPDGNode.setPrev(prev);
                                                depBPDGNode.setPrev(null);
                                            }
                                            PDGNode next = depBPDGNode.getNext();
                                            if (next != null) {
                                                removeEdge(depBPDGNode, next, "controlflow");
                                                addEdge(loopedPDGNode, next, "controlflow");
                                                loopedPDGNode.setNext(next);
                                                next.setPrev(loopedPDGNode);
                                                depBPDGNode.setNext(null);
                                            }
                                        }
                                    } else {
                                        addEdge(pdgnodeOfBRegion, predPDGofdepB, "dependency-back");
                                        pdgnodeOfBRegion.addBackDependent(predPDGofdepB);
                                    }
                                }
                            }
                            continue;
                        }
                    }
                }
                if (prevPDGNodeInRegion != null) {
                    addEdge(prevPDGNodeInRegion, curNodeInRegion, "controlflow");
                    prevPDGNodeInRegion.setNext(curNodeInRegion);
                    curNodeInRegion.setPrev(prevPDGNodeInRegion);
                }
                prevPDGNodeInRegion = curNodeInRegion;
            }
            Enumeration<Region> itr = toBeRemoved.keys();
            while (itr.hasMoreElements()) {
                Region region = itr.nextElement();
                for (Block next2 : toBeRemoved.get(region)) {
                    region.remove(next2);
                }
            }
        }
    }

    private List<Region> cloneRegions(List<Region> weak) {
        List<Region> strong = new ArrayList<>();
        for (Region r : weak) {
            strong.add((Region) r.clone());
        }
        return strong;
    }

    public UnitGraph getCFG() {
        return this.m_cfg;
    }

    @Override // soot.toolkits.graph.pdg.ProgramDependenceGraph
    public List<Region> getWeakRegions() {
        return this.m_weakRegions;
    }

    @Override // soot.toolkits.graph.pdg.ProgramDependenceGraph
    public List<Region> getStrongRegions() {
        return this.m_strongRegions;
    }

    @Override // soot.toolkits.graph.pdg.ProgramDependenceGraph
    public IRegion GetStartRegion() {
        return (IRegion) GetStartNode().getNode();
    }

    @Override // soot.toolkits.graph.pdg.ProgramDependenceGraph
    public PDGNode GetStartNode() {
        return this.m_startNode;
    }

    public static List<IRegion> getPreorderRegionList(IRegion r) {
        List<IRegion> list = new ArrayList<>();
        Queue<IRegion> toProcess = new LinkedList<>();
        toProcess.add(r);
        while (!toProcess.isEmpty()) {
            IRegion reg = toProcess.poll();
            list.add(reg);
            for (IRegion next : reg.getChildRegions()) {
                toProcess.add((Region) next);
            }
        }
        return list;
    }

    public static List<IRegion> getPostorderRegionList(IRegion r) {
        List<IRegion> list = new ArrayList<>();
        postorder(r, list);
        return list;
    }

    private static List<IRegion> postorder(IRegion r, List<IRegion> list) {
        for (IRegion next : r.getChildRegions()) {
            postorder(next, list);
        }
        list.add(r);
        return list;
    }

    @Override // soot.toolkits.graph.pdg.ProgramDependenceGraph
    public List<PDGRegion> getPDGRegions() {
        return this.m_pdgRegions;
    }

    public static List<PDGRegion> getPostorderPDGRegionList(PDGNode root) {
        return computePDGRegions(root);
    }

    private static List<PDGRegion> computePDGRegions(PDGNode root) {
        List<PDGRegion> regions = new ArrayList<>();
        node2Region.clear();
        pdgpostorder(root, regions);
        return regions;
    }

    private static PDGRegion pdgpostorder(PDGNode node, List<PDGRegion> list) {
        PDGRegion region;
        if (node.getVisited()) {
            return null;
        }
        node.setVisited(true);
        if (!node2Region.containsKey(node)) {
            region = new PDGRegion(node);
            node2Region.put(node, region);
        } else {
            region = node2Region.get(node);
        }
        for (PDGNode curNode : node.getDependents()) {
            if (!curNode.getVisited()) {
                region.addPDGNode(curNode);
                if (curNode instanceof LoopedPDGNode) {
                    PDGNode body = ((LoopedPDGNode) curNode).getBody();
                    PDGRegion kid = pdgpostorder(body, list);
                    if (kid != null) {
                        kid.setParent(region);
                        region.addChildRegion(kid);
                        body.setNode(kid);
                    }
                } else if (curNode instanceof ConditionalPDGNode) {
                    for (PDGNode child : curNode.getDependents()) {
                        PDGRegion kid2 = pdgpostorder(child, list);
                        if (kid2 != null) {
                            kid2.setParent(region);
                            region.addChildRegion(kid2);
                            child.setNode(kid2);
                        }
                    }
                }
            }
        }
        list.add(region);
        return region;
    }

    @Override // soot.toolkits.graph.pdg.ProgramDependenceGraph
    public boolean dependentOn(PDGNode node1, PDGNode node2) {
        return node2.getDependents().contains(node1);
    }

    @Override // soot.toolkits.graph.pdg.ProgramDependenceGraph
    public List<PDGNode> getDependents(PDGNode node) {
        List<PDGNode> toReturn = new ArrayList<>();
        for (PDGNode succ : getSuccsOf(node)) {
            if (dependentOn(succ, node)) {
                toReturn.add(succ);
            }
        }
        return toReturn;
    }

    @Override // soot.toolkits.graph.pdg.ProgramDependenceGraph
    public PDGNode getPDGNode(Object cfgNode) {
        if (cfgNode != null && (cfgNode instanceof Block) && this.m_obj2pdgNode.containsKey(cfgNode)) {
            return this.m_obj2pdgNode.get(cfgNode);
        }
        return null;
    }

    private void replaceInGraph(PDGNode newnode, PDGNode oldnode) {
        addNode(newnode);
        HashMutableEdgeLabelledDirectedGraph<PDGNode, String> graph = m3032clone();
        for (PDGNode succ : graph.getSuccsOf(oldnode)) {
            for (Object label : graph.getLabelsForEdges(oldnode, succ)) {
                addEdge(newnode, succ, label.toString());
            }
        }
        for (PDGNode pred : graph.getPredsOf(oldnode)) {
            for (Object label2 : graph.getLabelsForEdges(pred, oldnode)) {
                addEdge(pred, newnode, label2.toString());
            }
        }
        removeNode(oldnode);
    }

    @Override // soot.toolkits.graph.HashMutableEdgeLabelledDirectedGraph, soot.toolkits.graph.MutableEdgeLabelledDirectedGraph
    public void removeAllEdges(PDGNode from, PDGNode to) {
        if (containsAnyEdge(from, to)) {
            Iterator it = new ArrayList(getLabelsForEdges(from, to)).iterator();
            while (it.hasNext()) {
                String label = (String) it.next();
                removeEdge(from, to, label);
            }
        }
    }

    @Override // soot.toolkits.graph.pdg.ProgramDependenceGraph
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nProgram Dependence Graph for Method ").append(this.m_body.getMethod().getName()).append("\n*********CFG******** \n").append(RegionAnalysis.CFGtoString(this.m_blockCFG, true)).append("\n*********PDG******** \n");
        List<PDGNode> processed = new ArrayList<>();
        Queue<PDGNode> nodes = new LinkedList<>();
        nodes.offer(this.m_startNode);
        while (nodes.peek() != null) {
            PDGNode node = nodes.remove();
            processed.add(node);
            sb.append("\n Begin PDGNode: ").append(node);
            List<PDGNode> succs = getSuccsOf(node);
            sb.append("\n has ").append(succs.size()).append(" successors:\n");
            int i = 0;
            for (PDGNode succ : succs) {
                List<String> labels = getLabelsForEdges(node, succ);
                int i2 = i;
                i++;
                sb.append(i2);
                sb.append(": Edge's label: ").append(labels).append(" \n");
                sb.append("   Target: ").append(succ.toShortString());
                sb.append('\n');
                if ("dependency".equals(labels.get(0)) && !processed.contains(succ)) {
                    nodes.offer(succ);
                }
            }
            sb.append("\n End PDGNode.");
        }
        return sb.toString();
    }
}
