package soot.jimple.toolkits.annotation.arraycheck;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.Local;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.AddExpr;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.ConditionExpr;
import soot.jimple.EqExpr;
import soot.jimple.FieldRef;
import soot.jimple.GeExpr;
import soot.jimple.GtExpr;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.LeExpr;
import soot.jimple.LengthExpr;
import soot.jimple.LtExpr;
import soot.jimple.MulExpr;
import soot.jimple.NeExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.Stmt;
import soot.jimple.SubExpr;
import soot.options.Options;
import soot.toolkits.graph.ArrayRefBlockGraph;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
import soot.toolkits.graph.SlowPseudoTopologicalOrderer;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/arraycheck/ArrayBoundsCheckerAnalysis.class */
class ArrayBoundsCheckerAnalysis {
    private static final Logger logger = LoggerFactory.getLogger(ArrayBoundsCheckerAnalysis.class);
    protected Map<Block, WeightedDirectedSparseGraph> blockToBeforeFlow;
    protected Map<Unit, WeightedDirectedSparseGraph> unitToBeforeFlow;
    private final Map<FlowGraphEdge, WeightedDirectedSparseGraph> edgeMap;
    private final Set<FlowGraphEdge> edgeSet;
    private HashMap<Block, Integer> stableRoundOfUnits;
    private ArrayRefBlockGraph graph;
    private boolean fieldin;
    private HashMap<Object, HashSet<Value>> localToFieldRef;
    private HashMap<Object, HashSet<Value>> fieldToFieldRef;
    private boolean arrayin;
    private boolean csin;
    private HashMap<Value, HashSet<Value>> localToExpr;
    private boolean classfieldin;
    private ClassFieldAnalysis cfield;
    private boolean rectarray;
    private HashSet<Local> rectarrayset;
    private HashSet<Local> multiarraylocals;
    private final ArrayIndexLivenessAnalysis ailanalysis;
    private final IntContainer zero = new IntContainer(0);
    private final int strictness = 2;

    public ArrayBoundsCheckerAnalysis(Body body, boolean takeClassField, boolean takeFieldRef, boolean takeArrayRef, boolean takeCSE, boolean takeRectArray) {
        this.fieldin = false;
        this.arrayin = false;
        this.csin = false;
        this.classfieldin = false;
        this.rectarray = false;
        this.classfieldin = takeClassField;
        this.fieldin = takeFieldRef;
        this.arrayin = takeArrayRef;
        this.csin = takeCSE;
        this.rectarray = takeRectArray;
        SootMethod thismethod = body.getMethod();
        if (Options.v().debug()) {
            logger.debug("ArrayBoundsCheckerAnalysis started on  " + thismethod.getName());
        }
        this.ailanalysis = new ArrayIndexLivenessAnalysis(ExceptionalUnitGraphFactory.createExceptionalUnitGraph(body), this.fieldin, this.arrayin, this.csin, this.rectarray);
        if (this.fieldin) {
            this.localToFieldRef = this.ailanalysis.getLocalToFieldRef();
            this.fieldToFieldRef = this.ailanalysis.getFieldToFieldRef();
        }
        if (this.arrayin && this.rectarray) {
            this.multiarraylocals = this.ailanalysis.getMultiArrayLocals();
            this.rectarrayset = new HashSet<>();
            RectangularArrayFinder pgbuilder = RectangularArrayFinder.v();
            Iterator<Local> localIt = this.multiarraylocals.iterator();
            while (localIt.hasNext()) {
                Local local = localIt.next();
                MethodLocal mlocal = new MethodLocal(thismethod, local);
                if (pgbuilder.isRectangular(mlocal)) {
                    this.rectarrayset.add(local);
                }
            }
        }
        if (this.csin) {
            this.localToExpr = this.ailanalysis.getLocalToExpr();
        }
        if (this.classfieldin) {
            this.cfield = ClassFieldAnalysis.v();
        }
        this.graph = new ArrayRefBlockGraph(body);
        this.blockToBeforeFlow = new HashMap((this.graph.size() * 2) + 1, 0.7f);
        this.edgeMap = new HashMap((this.graph.size() * 2) + 1, 0.7f);
        this.edgeSet = buildEdgeSet(this.graph);
        doAnalysis();
        convertToUnitEntry();
        if (Options.v().debug()) {
            logger.debug("ArrayBoundsCheckerAnalysis finished.");
        }
    }

    private void convertToUnitEntry() {
        this.unitToBeforeFlow = new HashMap();
        for (Block block : this.blockToBeforeFlow.keySet()) {
            Unit first = block.getHead();
            this.unitToBeforeFlow.put(first, this.blockToBeforeFlow.get(block));
        }
    }

    public Set<FlowGraphEdge> buildEdgeSet(DirectedGraph<Block> dg) {
        HashSet<FlowGraphEdge> edges = new HashSet<>();
        for (Block s : dg) {
            List<Block> preds = this.graph.getPredsOf(s);
            List<Block> succs = this.graph.getSuccsOf(s);
            if (preds.size() == 0) {
                edges.add(new FlowGraphEdge(s, s));
            }
            if (succs.size() == 0) {
                edges.add(new FlowGraphEdge(s, s));
            } else {
                for (Block block : succs) {
                    edges.add(new FlowGraphEdge(s, block));
                }
            }
        }
        return edges;
    }

    public Object getFlowBefore(Object s) {
        return this.unitToBeforeFlow.get(s);
    }

    private void mergebunch(Object[] ins, Object s, Object prevOut, Object out) {
        WeightedDirectedSparseGraph prevgraph = (WeightedDirectedSparseGraph) prevOut;
        WeightedDirectedSparseGraph outgraph = (WeightedDirectedSparseGraph) out;
        WeightedDirectedSparseGraph[] ingraphs = new WeightedDirectedSparseGraph[ins.length];
        for (int i = 0; i < ins.length; i++) {
            ingraphs[i] = (WeightedDirectedSparseGraph) ins[i];
        }
        outgraph.addBoundedAll(ingraphs[0]);
        for (int i2 = 1; i2 < ingraphs.length; i2++) {
            outgraph.unionSelf(ingraphs[i2]);
            outgraph.makeShortestPathGraph();
        }
        outgraph.widenEdges(prevgraph);
    }

    private void doAnalysis() {
        Date start = new Date();
        if (Options.v().debug()) {
            logger.debug("Building PseudoTopological order list on " + start);
        }
        LinkedList allUnits = (LinkedList) SlowPseudoTopologicalOrderer.v().newList(this.graph, false);
        BoundedPriorityList changedUnits = new BoundedPriorityList(allUnits);
        Date finish = new Date();
        if (Options.v().debug()) {
            long runtime = finish.getTime() - start.getTime();
            long mins = runtime / 60000;
            long secs = (runtime % 60000) / 1000;
            logger.debug("Building PseudoTopological order finished. It took " + mins + " mins and " + secs + " secs.");
        }
        Date start2 = new Date();
        HashSet changedUnitsSet = new HashSet(allUnits);
        FlowGraphEdge tmpEdge = new FlowGraphEdge();
        HashSet<Block> unvisitedNodes = new HashSet<>((this.graph.size() * 2) + 1, 0.7f);
        Iterator blockIt = this.graph.iterator();
        while (blockIt.hasNext()) {
            HashSet<IntContainer> livelocals = (HashSet) this.ailanalysis.getFlowBefore(blockIt.next().getHead());
            livelocals.add(this.zero);
        }
        this.stableRoundOfUnits = new HashMap<>();
        Iterator it = this.graph.iterator();
        while (it.hasNext()) {
            Block block = it.next();
            unvisitedNodes.add(block);
            this.stableRoundOfUnits.put(block, new Integer(0));
            HashSet livelocals2 = (HashSet) this.ailanalysis.getFlowBefore(block.getHead());
            this.blockToBeforeFlow.put(block, new WeightedDirectedSparseGraph(livelocals2, false));
        }
        for (FlowGraphEdge edge : this.edgeSet) {
            Block target = (Block) edge.to;
            HashSet livelocals3 = (HashSet) this.ailanalysis.getFlowBefore(target.getHead());
            this.edgeMap.put(edge, new WeightedDirectedSparseGraph(livelocals3, false));
        }
        List headlist = this.graph.getHeads();
        for (Object head : headlist) {
            WeightedDirectedSparseGraph initgraph = this.edgeMap.get(new FlowGraphEdge(head, head));
            initgraph.setTop();
        }
        WeightedDirectedSparseGraph beforeFlow = new WeightedDirectedSparseGraph(null, false);
        while (!changedUnits.isEmpty()) {
            Block s = (Block) changedUnits.removeFirst();
            changedUnitsSet.remove(s);
            WeightedDirectedSparseGraph previousBeforeFlow = this.blockToBeforeFlow.get(s);
            beforeFlow.setVertexes(previousBeforeFlow.getVertexes());
            List preds = this.graph.getPredsOf(s);
            if (preds.size() == 0) {
                tmpEdge.changeEndUnits(s, s);
                copy(this.edgeMap.get(tmpEdge), beforeFlow);
            } else if (preds.size() == 1) {
                tmpEdge.changeEndUnits(preds.get(0), s);
                copy(this.edgeMap.get(tmpEdge), beforeFlow);
            } else {
                Object[] predFlows = new Object[preds.size()];
                boolean allUnvisited = true;
                int index = 0;
                int lastVisited = 0;
                for (Object pred : preds) {
                    tmpEdge.changeEndUnits(pred, s);
                    if (!unvisitedNodes.contains(pred)) {
                        allUnvisited = false;
                        lastVisited = index;
                    }
                    int i = index;
                    index++;
                    predFlows[i] = this.edgeMap.get(tmpEdge);
                }
                if (allUnvisited) {
                    logger.debug("Warning : see all unvisited node");
                } else {
                    Object tmp = predFlows[0];
                    predFlows[0] = predFlows[lastVisited];
                    predFlows[lastVisited] = tmp;
                }
                mergebunch(predFlows, s, previousBeforeFlow, beforeFlow);
            }
            copy(beforeFlow, previousBeforeFlow);
            List<Object> changedSuccs = flowThrough(beforeFlow, s);
            for (int i2 = 0; i2 < changedSuccs.size(); i2++) {
                Object succ = changedSuccs.get(i2);
                if (!changedUnitsSet.contains(succ)) {
                    changedUnits.add(succ);
                    changedUnitsSet.add(succ);
                }
            }
            unvisitedNodes.remove(s);
        }
        Date finish2 = new Date();
        if (Options.v().debug()) {
            long runtime2 = finish2.getTime() - start2.getTime();
            long mins2 = runtime2 / 60000;
            long secs2 = (runtime2 / 60000) / 1000;
            logger.debug("Doing analysis finished. It took " + mins2 + " mins and " + secs2 + "secs.");
        }
    }

    private List<Object> flowThrough(Object inValue, Object unit) {
        ArrayList<Object> changedSuccs = new ArrayList<>();
        WeightedDirectedSparseGraph ingraph = (WeightedDirectedSparseGraph) inValue;
        Block block = (Block) unit;
        List succs = block.getSuccs();
        Unit s = block.getHead();
        Unit succOf = block.getSuccOf(s);
        while (true) {
            Unit nexts = succOf;
            if (nexts == null) {
                break;
            }
            assertArrayRef(ingraph, s);
            assertNormalExpr(ingraph, s);
            s = nexts;
            succOf = block.getSuccOf(nexts);
        }
        if (s instanceof IfStmt) {
            if (!assertBranchStmt(ingraph, s, block, succs, changedSuccs)) {
                updateOutEdges(ingraph, block, succs, changedSuccs);
            }
        } else {
            assertArrayRef(ingraph, s);
            assertNormalExpr(ingraph, s);
            updateOutEdges(ingraph, block, succs, changedSuccs);
        }
        return changedSuccs;
    }

    private void assertArrayRef(Object in, Unit unit) {
        if (!(unit instanceof AssignStmt)) {
            return;
        }
        Stmt s = (Stmt) unit;
        WeightedDirectedSparseGraph ingraph = (WeightedDirectedSparseGraph) in;
        if (!s.containsArrayRef()) {
            return;
        }
        ArrayRef op = s.getArrayRef();
        Value base = op.getBase();
        Value index = op.getIndex();
        HashSet livelocals = (HashSet) this.ailanalysis.getFlowAfter(s);
        if (!livelocals.contains(base) && !livelocals.contains(index)) {
            return;
        }
        if (index instanceof IntConstant) {
            int weight = ((IntConstant) index).value;
            ingraph.addEdge(base, this.zero, (-1) - weight);
            return;
        }
        ingraph.addEdge(base, index, -1);
        ingraph.addEdge(index, this.zero, 0);
    }

    private void assertNormalExpr(Object in, Unit s) {
        HashSet exprs;
        WeightedDirectedSparseGraph ingraph = (WeightedDirectedSparseGraph) in;
        if (this.fieldin) {
            Stmt stmt = (Stmt) s;
            if (stmt.containsInvokeExpr()) {
                new HashSet();
                Value expr = stmt.getInvokeExpr();
                ((InvokeExpr) expr).getArgs();
                HashSet vertexes = ingraph.getVertexes();
                Iterator nodeIt = vertexes.iterator();
                while (nodeIt.hasNext()) {
                    Object node = nodeIt.next();
                    if (node instanceof FieldRef) {
                        ingraph.killNode(node);
                    }
                }
            }
        }
        if (this.arrayin && ((Stmt) s).containsInvokeExpr()) {
            HashSet vertexes2 = ingraph.getVertexes();
            Iterator nodeIt2 = vertexes2.iterator();
            while (nodeIt2.hasNext()) {
                Object node2 = nodeIt2.next();
                if (node2 instanceof ArrayRef) {
                    ingraph.killNode(node2);
                }
            }
        }
        if (!(s instanceof AssignStmt)) {
            return;
        }
        Value leftOp = ((AssignStmt) s).getLeftOp();
        Value rightOp = ((AssignStmt) s).getRightOp();
        HashSet livelocals = (HashSet) this.ailanalysis.getFlowAfter(s);
        if (this.fieldin) {
            if (leftOp instanceof Local) {
                HashSet fieldrefs = this.localToFieldRef.get(leftOp);
                if (fieldrefs != null) {
                    Iterator refsIt = fieldrefs.iterator();
                    while (refsIt.hasNext()) {
                        Object ref = refsIt.next();
                        if (livelocals.contains(ref)) {
                            ingraph.killNode(ref);
                        }
                    }
                }
            } else if (leftOp instanceof InstanceFieldRef) {
                SootField field = ((InstanceFieldRef) leftOp).getField();
                HashSet fieldrefs2 = this.fieldToFieldRef.get(field);
                if (fieldrefs2 != null) {
                    Iterator refsIt2 = fieldrefs2.iterator();
                    while (refsIt2.hasNext()) {
                        Object ref2 = refsIt2.next();
                        if (livelocals.contains(ref2)) {
                            ingraph.killNode(ref2);
                        }
                    }
                }
            }
        }
        if (this.arrayin) {
            if (leftOp instanceof Local) {
                HashSet vertexes3 = ingraph.getVertexes();
                Iterator nodeIt3 = vertexes3.iterator();
                while (nodeIt3.hasNext()) {
                    Object node3 = nodeIt3.next();
                    if (node3 instanceof ArrayRef) {
                        Value base = ((ArrayRef) node3).getBase();
                        Value index = ((ArrayRef) node3).getIndex();
                        if (base.equals(leftOp) || index.equals(leftOp)) {
                            ingraph.killNode(node3);
                        }
                    }
                    if (this.rectarray && (node3 instanceof Array2ndDimensionSymbol)) {
                        if (((Array2ndDimensionSymbol) node3).getVar().equals(leftOp)) {
                            ingraph.killNode(node3);
                        }
                    }
                }
            } else if (leftOp instanceof ArrayRef) {
                HashSet vertexes4 = ingraph.getVertexes();
                Iterator nodeIt4 = vertexes4.iterator();
                while (nodeIt4.hasNext()) {
                    Object node4 = nodeIt4.next();
                    if (node4 instanceof ArrayRef) {
                        ingraph.killNode(node4);
                    }
                }
            }
        }
        if ((!livelocals.contains(leftOp) && !livelocals.contains(rightOp)) || rightOp.equals(leftOp)) {
            return;
        }
        if (this.csin && (exprs = this.localToExpr.get(leftOp)) != null) {
            Iterator exprIt = exprs.iterator();
            while (exprIt.hasNext()) {
                ingraph.killNode(exprIt.next());
            }
        }
        if (rightOp instanceof AddExpr) {
            Value op1 = ((AddExpr) rightOp).getOp1();
            Value op2 = ((AddExpr) rightOp).getOp2();
            if (op1 == leftOp && (op2 instanceof IntConstant)) {
                int inc_w = ((IntConstant) op2).value;
                ingraph.updateWeight(leftOp, inc_w);
                return;
            } else if (op2 == leftOp && (op1 instanceof IntConstant)) {
                int inc_w2 = ((IntConstant) op1).value;
                ingraph.updateWeight(leftOp, inc_w2);
                return;
            }
        }
        if (rightOp instanceof SubExpr) {
            Value op12 = ((SubExpr) rightOp).getOp1();
            Value op22 = ((SubExpr) rightOp).getOp2();
            if (op12 == leftOp && (op22 instanceof IntConstant)) {
                int inc_w3 = -((IntConstant) op22).value;
                ingraph.updateWeight(leftOp, inc_w3);
                return;
            }
        }
        ingraph.killNode(leftOp);
        if (rightOp instanceof IntConstant) {
            int inc_w4 = ((IntConstant) rightOp).value;
            ingraph.addMutualEdges(this.zero, leftOp, inc_w4);
        } else if (rightOp instanceof Local) {
            ingraph.addMutualEdges(rightOp, leftOp, 0);
        } else if (rightOp instanceof FieldRef) {
            if (this.fieldin) {
                ingraph.addMutualEdges(rightOp, leftOp, 0);
            }
            if (this.classfieldin) {
                SootField field2 = ((FieldRef) rightOp).getField();
                IntValueContainer flength = (IntValueContainer) this.cfield.getFieldInfo(field2);
                if (flength != null && flength.isInteger()) {
                    ingraph.addMutualEdges(this.zero, leftOp, flength.getValue());
                }
            }
        } else if (this.arrayin && (rightOp instanceof ArrayRef)) {
            ingraph.addMutualEdges(rightOp, leftOp, 0);
            if (this.rectarray) {
                Value base2 = ((ArrayRef) rightOp).getBase();
                if (this.rectarrayset.contains(base2)) {
                    ingraph.addMutualEdges(leftOp, Array2ndDimensionSymbol.v(base2), 0);
                }
            }
        } else {
            if (this.csin && (rightOp instanceof BinopExpr)) {
                Value op13 = ((BinopExpr) rightOp).getOp1();
                Value op23 = ((BinopExpr) rightOp).getOp2();
                if (rightOp instanceof AddExpr) {
                    if ((op13 instanceof Local) && (op23 instanceof Local)) {
                        ingraph.addMutualEdges(rightOp, leftOp, 0);
                        return;
                    }
                } else if (rightOp instanceof MulExpr) {
                    if ((op13 instanceof Local) || (op23 instanceof Local)) {
                        ingraph.addMutualEdges(rightOp, leftOp, 0);
                        return;
                    }
                } else if ((rightOp instanceof SubExpr) && (op23 instanceof Local)) {
                    ingraph.addMutualEdges(rightOp, leftOp, 0);
                    return;
                }
            }
            if (rightOp instanceof AddExpr) {
                Value op14 = ((AddExpr) rightOp).getOp1();
                Value op24 = ((AddExpr) rightOp).getOp2();
                if ((op14 instanceof Local) && (op24 instanceof IntConstant)) {
                    int inc_w5 = ((IntConstant) op24).value;
                    ingraph.addMutualEdges(op14, leftOp, inc_w5);
                    return;
                } else if ((op24 instanceof Local) && (op14 instanceof IntConstant)) {
                    int inc_w6 = ((IntConstant) op14).value;
                    ingraph.addMutualEdges(op24, leftOp, inc_w6);
                    return;
                }
            }
            if (rightOp instanceof SubExpr) {
                Value op15 = ((SubExpr) rightOp).getOp1();
                Value op25 = ((SubExpr) rightOp).getOp2();
                if ((op15 instanceof Local) && (op25 instanceof IntConstant)) {
                    int inc_w7 = -((IntConstant) op25).value;
                    ingraph.addMutualEdges(op15, leftOp, inc_w7);
                    return;
                }
            }
            if (rightOp instanceof NewArrayExpr) {
                Value size = ((NewArrayExpr) rightOp).getSize();
                if (size instanceof Local) {
                    ingraph.addMutualEdges(size, leftOp, 0);
                    return;
                } else if (size instanceof IntConstant) {
                    int inc_w8 = ((IntConstant) size).value;
                    ingraph.addMutualEdges(this.zero, leftOp, inc_w8);
                    return;
                }
            }
            if (rightOp instanceof NewMultiArrayExpr) {
                Value size2 = ((NewMultiArrayExpr) rightOp).getSize(0);
                if (size2 instanceof Local) {
                    ingraph.addMutualEdges(size2, leftOp, 0);
                } else if (size2 instanceof IntConstant) {
                    int inc_w9 = ((IntConstant) size2).value;
                    ingraph.addMutualEdges(this.zero, leftOp, inc_w9);
                }
                if (this.arrayin && this.rectarray && ((NewMultiArrayExpr) rightOp).getSizeCount() > 1) {
                    Value size3 = ((NewMultiArrayExpr) rightOp).getSize(1);
                    if (size3 instanceof Local) {
                        ingraph.addMutualEdges(size3, Array2ndDimensionSymbol.v(leftOp), 0);
                    } else if (size3 instanceof IntConstant) {
                        int inc_w10 = ((IntConstant) size3).value;
                        ingraph.addMutualEdges(this.zero, Array2ndDimensionSymbol.v(leftOp), inc_w10);
                    }
                }
            } else if (rightOp instanceof LengthExpr) {
                ingraph.addMutualEdges(((LengthExpr) rightOp).getOp(), leftOp, 0);
            }
        }
    }

    private boolean assertBranchStmt(Object in, Unit s, Block current, List succs, List<Object> changedSuccs) {
        IfStmt ifstmt = (IfStmt) s;
        Value cmpcond = ifstmt.getCondition();
        if (!(cmpcond instanceof ConditionExpr) || succs.size() != 2) {
            return false;
        }
        Stmt targetUnit = ifstmt.getTarget();
        Block targetBlock = (Block) succs.get(0);
        Block nextBlock = (Block) succs.get(1);
        if (!targetUnit.equals(targetBlock.getHead())) {
            targetBlock = nextBlock;
            nextBlock = targetBlock;
        }
        Object op1 = ((ConditionExpr) cmpcond).getOp1();
        Object op2 = ((ConditionExpr) cmpcond).getOp2();
        HashSet livelocals = (HashSet) this.ailanalysis.getFlowAfter(s);
        if (!livelocals.contains(op1) && !livelocals.contains(op2)) {
            return false;
        }
        WeightedDirectedSparseGraph ingraph = (WeightedDirectedSparseGraph) in;
        WeightedDirectedSparseGraph targetgraph = ingraph.dup();
        if ((cmpcond instanceof EqExpr) || (cmpcond instanceof NeExpr)) {
            Object node1 = op1;
            Object node2 = op2;
            int weight = 0;
            if (node1 instanceof IntConstant) {
                weight = ((IntConstant) node1).value;
                node1 = this.zero;
            }
            if (node2 instanceof IntConstant) {
                weight = ((IntConstant) node2).value;
                node2 = this.zero;
            }
            if (node1 == node2) {
                return false;
            }
            if (cmpcond instanceof EqExpr) {
                targetgraph.addMutualEdges(node1, node2, weight);
            } else {
                ingraph.addMutualEdges(node1, node2, weight);
            }
        } else if ((cmpcond instanceof GtExpr) || (cmpcond instanceof GeExpr)) {
            Object node12 = op1;
            Object node22 = op2;
            int weight2 = 0;
            if (node12 instanceof IntConstant) {
                weight2 = 0 + ((IntConstant) node12).value;
                node12 = this.zero;
            }
            if (node22 instanceof IntConstant) {
                weight2 -= ((IntConstant) node22).value;
                node22 = this.zero;
            }
            if (node12 == node22) {
                return false;
            }
            if (cmpcond instanceof GtExpr) {
                targetgraph.addEdge(node12, node22, weight2 - 1);
                ingraph.addEdge(node22, node12, -weight2);
            } else {
                targetgraph.addEdge(node12, node22, weight2);
                ingraph.addEdge(node22, node12, (-weight2) - 1);
            }
        } else if ((cmpcond instanceof LtExpr) || (cmpcond instanceof LeExpr)) {
            Object node13 = op1;
            Object node23 = op2;
            int weight3 = 0;
            if (node13 instanceof IntConstant) {
                weight3 = 0 - ((IntConstant) node13).value;
                node13 = this.zero;
            }
            if (node23 instanceof IntConstant) {
                weight3 += ((IntConstant) node23).value;
                node23 = this.zero;
            }
            if (node13 == node23) {
                return false;
            }
            if (cmpcond instanceof LtExpr) {
                targetgraph.addEdge(node23, node13, weight3 - 1);
                ingraph.addEdge(node13, node23, -weight3);
            } else {
                targetgraph.addEdge(node23, node13, weight3);
                ingraph.addEdge(node13, node23, (-weight3) - 1);
            }
        } else {
            return false;
        }
        FlowGraphEdge targetEdge = new FlowGraphEdge(current, targetBlock);
        WeightedDirectedSparseGraph prevtarget = this.edgeMap.get(targetEdge);
        boolean changed = false;
        targetgraph.makeShortestPathGraph();
        WeightedDirectedSparseGraph tmpgraph = new WeightedDirectedSparseGraph(prevtarget.getVertexes(), true);
        copy(targetgraph, tmpgraph);
        if (!tmpgraph.equals(prevtarget)) {
            prevtarget.replaceAllEdges(tmpgraph);
            changed = true;
        }
        if (changed) {
            changedSuccs.add(targetBlock);
        }
        FlowGraphEdge nextEdge = new FlowGraphEdge(current, nextBlock);
        WeightedDirectedSparseGraph prevnext = this.edgeMap.get(nextEdge);
        boolean changed2 = false;
        ingraph.makeShortestPathGraph();
        WeightedDirectedSparseGraph tmpgraph2 = new WeightedDirectedSparseGraph(prevnext.getVertexes(), true);
        copy(ingraph, tmpgraph2);
        if (!tmpgraph2.equals(prevnext)) {
            prevnext.replaceAllEdges(tmpgraph2);
            changed2 = true;
        }
        if (changed2) {
            changedSuccs.add(nextBlock);
            return true;
        }
        return true;
    }

    private void updateOutEdges(Object in, Block current, List succs, List<Object> changedSuccs) {
        WeightedDirectedSparseGraph ingraph = (WeightedDirectedSparseGraph) in;
        ingraph.makeShortestPathGraph();
        for (int i = 0; i < succs.size(); i++) {
            Object next = succs.get(i);
            FlowGraphEdge nextEdge = new FlowGraphEdge(current, next);
            WeightedDirectedSparseGraph prevs = this.edgeMap.get(nextEdge);
            boolean changed = false;
            WeightedDirectedSparseGraph tmpgraph = new WeightedDirectedSparseGraph(prevs.getVertexes(), true);
            copy(ingraph, tmpgraph);
            if (!tmpgraph.equals(prevs)) {
                prevs.replaceAllEdges(tmpgraph);
                changed = true;
            }
            if (changed) {
                changedSuccs.add(next);
            }
        }
    }

    protected void copy(Object from, Object to) {
        WeightedDirectedSparseGraph fromgraph = (WeightedDirectedSparseGraph) from;
        WeightedDirectedSparseGraph tograph = (WeightedDirectedSparseGraph) to;
        tograph.clear();
        tograph.addBoundedAll(fromgraph);
    }

    protected void widenGraphs(Object current, Object before) {
        WeightedDirectedSparseGraph curgraphs = (WeightedDirectedSparseGraph) current;
        WeightedDirectedSparseGraph pregraphs = (WeightedDirectedSparseGraph) before;
        curgraphs.widenEdges(pregraphs);
        curgraphs.makeShortestPathGraph();
    }
}
