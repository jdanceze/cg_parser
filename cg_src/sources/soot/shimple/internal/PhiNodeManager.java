package soot.shimple.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import soot.IdentityUnit;
import soot.Local;
import soot.Trap;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.Jimple;
import soot.shimple.PhiExpr;
import soot.shimple.Shimple;
import soot.shimple.ShimpleBody;
import soot.shimple.ShimpleFactory;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.DominanceFrontier;
import soot.toolkits.graph.DominatorNode;
import soot.toolkits.graph.DominatorTree;
import soot.toolkits.scalar.ValueUnitPair;
import soot.util.Chain;
import soot.util.HashMultiMap;
import soot.util.MultiMap;
/* loaded from: gencallgraphv3.jar:soot/shimple/internal/PhiNodeManager.class */
public class PhiNodeManager {
    protected final ShimpleBody body;
    protected final ShimpleFactory sf;
    protected BlockGraph cfg;
    protected MultiMap<Local, Block> varToBlocks;
    protected Map<Unit, Block> unitToBlock;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !PhiNodeManager.class.desiredAssertionStatus();
    }

    public PhiNodeManager(ShimpleBody body, ShimpleFactory sf) {
        this.body = body;
        this.sf = sf;
    }

    public void update() {
        BlockGraph oldCfg = this.cfg;
        this.cfg = this.sf.getBlockGraph();
        if (oldCfg != this.cfg) {
            this.unitToBlock = null;
            this.varToBlocks = null;
        }
    }

    public boolean insertTrivialPhiNodes() {
        update();
        this.varToBlocks = new HashMultiMap();
        Map<Local, List<Block>> localsToDefPoints = new LinkedHashMap<>();
        Iterator<Block> it = this.cfg.iterator();
        while (it.hasNext()) {
            Block block = it.next();
            Iterator<Unit> it2 = block.iterator();
            while (it2.hasNext()) {
                Unit unit = it2.next();
                for (ValueBox vb : unit.getDefBoxes()) {
                    Value def = vb.getValue();
                    if (def instanceof Local) {
                        Local local = (Local) def;
                        List<Block> def_points = localsToDefPoints.get(local);
                        if (def_points == null) {
                            def_points = new ArrayList<>();
                            localsToDefPoints.put(local, def_points);
                        }
                        def_points.add(block);
                    }
                }
                if (Shimple.isPhiNode(unit)) {
                    this.varToBlocks.put(Shimple.getLhsLocal(unit), block);
                }
            }
        }
        boolean change = false;
        DominatorTree<Block> dt = this.sf.getDominatorTree();
        DominanceFrontier<Block> df = this.sf.getDominanceFrontier();
        int iterCount = 0;
        int[] workFlags = new int[this.cfg.size()];
        Stack<Block> workList = new Stack<>();
        Map<Integer, Integer> has_already = new HashMap<>();
        Iterator<Block> it3 = this.cfg.iterator();
        while (it3.hasNext()) {
            has_already.put(Integer.valueOf(it3.next().getIndexInMethod()), 0);
        }
        for (Map.Entry<Local, List<Block>> e : localsToDefPoints.entrySet()) {
            iterCount++;
            if (!$assertionsDisabled && !workList.isEmpty()) {
                throw new AssertionError();
            }
            List<Block> def_points2 = e.getValue();
            if (def_points2.size() != 1) {
                for (Block block2 : def_points2) {
                    workFlags[block2.getIndexInMethod()] = iterCount;
                    workList.push(block2);
                }
                Local local2 = e.getKey();
                while (!workList.empty()) {
                    for (DominatorNode<Block> dn : df.getDominanceFrontierOf(dt.getDode(workList.pop()))) {
                        Block frontierBlock = dn.getGode();
                        if (frontierBlock.iterator().hasNext()) {
                            int fBIndex = frontierBlock.getIndexInMethod();
                            if (has_already.get(Integer.valueOf(fBIndex)).intValue() < iterCount) {
                                has_already.put(Integer.valueOf(fBIndex), Integer.valueOf(iterCount));
                                prependTrivialPhiNode(local2, frontierBlock);
                                change = true;
                                if (workFlags[fBIndex] < iterCount) {
                                    workFlags[fBIndex] = iterCount;
                                    workList.push(frontierBlock);
                                }
                            }
                        }
                    }
                }
            }
        }
        return change;
    }

    public void prependTrivialPhiNode(Local local, Block frontierBlock) {
        PhiExpr pe = Shimple.v().newPhiExpr(local, frontierBlock.getPreds());
        pe.setBlockId(frontierBlock.getIndexInMethod());
        Unit trivialPhi = Jimple.v().newAssignStmt(local, pe);
        Unit head = frontierBlock.getHead();
        if (head instanceof IdentityUnit) {
            frontierBlock.insertAfter(trivialPhi, head);
        } else {
            frontierBlock.insertBefore(trivialPhi, head);
        }
        this.varToBlocks.put(local, frontierBlock);
    }

    public void trimExceptionalPhiNodes() {
        Set<Unit> handlerUnits = new HashSet<>();
        for (Trap trap : this.body.getTraps()) {
            handlerUnits.add(trap.getHandlerUnit());
        }
        Iterator<Block> it = this.cfg.iterator();
        while (it.hasNext()) {
            Block block = it.next();
            if (handlerUnits.contains(block.getHead())) {
                Iterator<Unit> it2 = block.iterator();
                while (it2.hasNext()) {
                    Unit unit = it2.next();
                    PhiExpr phi = Shimple.getPhiExpr(unit);
                    if (phi != null) {
                        trimPhiNode(phi);
                    }
                }
            }
        }
    }

    public void trimPhiNode(PhiExpr phiExpr) {
        MultiMap<Value, ValueUnitPair> valueToPairs = new HashMultiMap<>();
        for (ValueUnitPair argPair : phiExpr.getArgs()) {
            valueToPairs.put(argPair.getValue(), argPair);
        }
        for (Value value : valueToPairs.keySet()) {
            Set<ValueUnitPair> pairsSet = valueToPairs.get(value);
            List<ValueUnitPair> champs = new LinkedList<>(pairsSet);
            List<ValueUnitPair> challengers = new LinkedList<>(pairsSet);
            ValueUnitPair champ = champs.remove(0);
            Unit champU = champ.getUnit();
            boolean retry = true;
            while (retry) {
                retry = false;
                Iterator<ValueUnitPair> itr = challengers.iterator();
                while (itr.hasNext()) {
                    ValueUnitPair challenger = itr.next();
                    if (!challenger.equals(champ)) {
                        Unit challengerU = challenger.getUnit();
                        if (dominates(champU, challengerU)) {
                            phiExpr.removeArg(challenger);
                            itr.remove();
                        } else if (dominates(challengerU, champU)) {
                            phiExpr.removeArg(champ);
                            champ = challenger;
                            champU = champ.getUnit();
                        } else {
                            retry = true;
                        }
                    }
                }
                if (retry) {
                    if (champs.isEmpty()) {
                        break;
                    }
                    champ = champs.remove(0);
                    champU = champ.getUnit();
                }
            }
        }
    }

    public boolean dominates(Unit champ, Unit challenger) {
        if (champ == null || challenger == null) {
            throw new RuntimeException("Assertion failed.");
        }
        if (champ.equals(challenger)) {
            return true;
        }
        Map<Unit, Block> unitToBlock = this.unitToBlock;
        if (unitToBlock == null) {
            unitToBlock = getUnitToBlockMap(this.cfg);
            this.unitToBlock = unitToBlock;
        }
        Block champBlock = unitToBlock.get(champ);
        Block challengerBlock = unitToBlock.get(challenger);
        if (champBlock.equals(challengerBlock)) {
            Iterator<Unit> it = champBlock.iterator();
            while (it.hasNext()) {
                Unit unit = it.next();
                if (unit.equals(champ)) {
                    return true;
                }
                if (unit.equals(challenger)) {
                    return false;
                }
            }
            throw new RuntimeException("Assertion failed.");
        }
        DominatorTree<Block> dt = this.sf.getDominatorTree();
        DominatorNode<Block> champNode = dt.getDode(champBlock);
        DominatorNode<Block> challengerNode = dt.getDode(challengerBlock);
        return dt.isDominatorOf(champNode, challengerNode);
    }

    public boolean doEliminatePhiNodes() {
        boolean addedNewLocals = false;
        List<Unit> phiNodes = new ArrayList<>();
        List<AssignStmt> equivStmts = new ArrayList<>();
        List<ValueUnitPair> predBoxes = new ArrayList<>();
        Jimple jimp = Jimple.v();
        Chain<Unit> units = this.body.getUnits();
        for (Unit unit : units) {
            PhiExpr phi = Shimple.getPhiExpr(unit);
            if (phi != null) {
                Local lhsLocal = Shimple.getLhsLocal(unit);
                for (ValueUnitPair vup : phi.getArgs()) {
                    predBoxes.add(vup);
                    equivStmts.add(jimp.newAssignStmt(lhsLocal, vup.getValue()));
                }
                phiNodes.add(unit);
            }
        }
        if (equivStmts.size() != predBoxes.size()) {
            throw new RuntimeException("Assertion failed.");
        }
        ListIterator<ValueUnitPair> it = predBoxes.listIterator();
        while (it.hasNext()) {
            Unit pred = it.next().getUnit();
            if (pred == null) {
                throw new RuntimeException("Assertion failed.");
            }
            AssignStmt stmt = equivStmts.get(it.previousIndex());
            if (pred.branches()) {
                boolean needPriming = false;
                Local lhsLocal2 = (Local) stmt.getLeftOp();
                Local savedLocal = jimp.newLocal(String.valueOf(lhsLocal2.getName()) + "_", lhsLocal2.getType());
                for (ValueBox useBox : pred.getUseBoxes()) {
                    if (lhsLocal2.equals(useBox.getValue())) {
                        needPriming = true;
                        addedNewLocals = true;
                        useBox.setValue(savedLocal);
                    }
                }
                if (needPriming) {
                    this.body.getLocals().add(savedLocal);
                    units.insertBefore(jimp.newAssignStmt(savedLocal, lhsLocal2), pred);
                }
                units.insertBefore(stmt, (AssignStmt) pred);
            } else {
                units.insertAfter(stmt, (AssignStmt) pred);
            }
        }
        for (Unit removeMe : phiNodes) {
            units.remove(removeMe);
            removeMe.clearUnitBoxes();
        }
        return addedNewLocals;
    }

    public static Map<Unit, Block> getUnitToBlockMap(BlockGraph blocks) {
        Map<Unit, Block> unitToBlock = new HashMap<>();
        Iterator<Block> it = blocks.iterator();
        while (it.hasNext()) {
            Block block = it.next();
            Iterator<Unit> it2 = block.iterator();
            while (it2.hasNext()) {
                Unit unit = it2.next();
                unitToBlock.put(unit, block);
            }
        }
        return unitToBlock;
    }
}
