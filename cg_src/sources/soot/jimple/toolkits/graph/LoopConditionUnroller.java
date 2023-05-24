package soot.jimple.toolkits.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.PhaseOptions;
import soot.Trap;
import soot.Unit;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;
import soot.jimple.Jimple;
import soot.options.Options;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.BriefBlockGraph;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/graph/LoopConditionUnroller.class */
public class LoopConditionUnroller extends BodyTransformer {
    private static final Logger logger;
    private Set<Block> visitingSuccs;
    private Set<Block> visitedBlocks;
    private int maxSize;
    private Chain<Unit> unitChain;
    private Chain<Trap> trapChain;
    private Map<Unit, List<Trap>> unitsToTraps;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !LoopConditionUnroller.class.desiredAssertionStatus();
        logger = LoggerFactory.getLogger(LoopConditionUnroller.class);
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
        if (Options.v().verbose()) {
            logger.debug("[" + body.getMethod().getName() + "]     Unrolling Loop Conditions...");
        }
        this.visitingSuccs = new HashSet();
        this.visitedBlocks = new HashSet();
        this.maxSize = PhaseOptions.getInt(options, "maxSize");
        this.unitChain = body.getUnits();
        this.trapChain = body.getTraps();
        this.unitsToTraps = mapBeginEndUnitToTrap(this.trapChain);
        BlockGraph bg = new BriefBlockGraph(body);
        for (Block b : bg.getHeads()) {
            unrollConditions(b);
        }
        if (Options.v().verbose()) {
            logger.debug("[" + body.getMethod().getName() + "]     Unrolling Loop Conditions done.");
        }
    }

    private Unit insertGotoAfter(Unit node, Unit target) {
        Unit newGoto = Jimple.v().newGotoStmt(target);
        this.unitChain.insertAfter(newGoto, node);
        return newGoto;
    }

    private Unit insertCloneAfter(Unit node, Unit toClone) {
        Unit clone = (Unit) toClone.clone();
        this.unitChain.insertAfter(clone, node);
        return clone;
    }

    private int getSize(Block block) {
        int size = 1;
        Chain<Unit> chain = this.unitChain;
        Unit e = block.getTail();
        for (Unit unit = block.getHead(); unit != e; unit = chain.getSuccOf(unit)) {
            size++;
        }
        return size;
    }

    private static Map<Unit, List<Trap>> mapBeginEndUnitToTrap(Chain<Trap> trapChain) {
        Map<Unit, List<Trap>> unitsToTraps = new HashMap<>();
        for (Trap trap : trapChain) {
            Unit beginUnit = trap.getBeginUnit();
            List<Trap> unitTraps = unitsToTraps.get(beginUnit);
            if (unitTraps == null) {
                unitTraps = new ArrayList<>();
                unitsToTraps.put(beginUnit, unitTraps);
            }
            unitTraps.add(trap);
            Unit endUnit = trap.getEndUnit();
            if (endUnit != beginUnit) {
                List<Trap> unitTraps2 = unitsToTraps.get(endUnit);
                if (unitTraps2 == null) {
                    unitTraps2 = new ArrayList<>();
                    unitsToTraps.put(endUnit, unitTraps2);
                }
                unitTraps2.add(trap);
            }
        }
        return unitsToTraps;
    }

    private Unit copyBlock(Block block) {
        Set<Trap> openedTraps = new HashSet<>();
        Map<Trap, Trap> copiedTraps = new HashMap<>();
        Chain<Unit> chain = this.unitChain;
        Unit tail = block.getTail();
        Unit newGoto = insertGotoAfter(tail, chain.getSuccOf(tail));
        Unit last = newGoto;
        Unit copiedHead = null;
        Unit head = block.getHead();
        while (true) {
            Unit curr = head;
            if (curr != newGoto) {
                last = insertCloneAfter(last, curr);
                if (copiedHead == null) {
                    copiedHead = last;
                    if (!$assertionsDisabled && copiedHead == null) {
                        throw new AssertionError();
                    }
                }
                List<Trap> currentTraps = this.unitsToTraps.get(curr);
                if (currentTraps != null) {
                    for (Trap trap : currentTraps) {
                        if (trap.getBeginUnit() == curr) {
                            Trap copiedTrap = (Trap) trap.clone();
                            copiedTrap.setBeginUnit(last);
                            copiedTraps.put(trap, copiedTrap);
                            openedTraps.add(copiedTrap);
                            this.trapChain.insertAfter(copiedTrap, trap);
                        }
                        if (trap.getEndUnit() == curr) {
                            Trap copiedTrap2 = copiedTraps.get(trap);
                            if (copiedTrap2 == null) {
                                copiedTrap2 = (Trap) trap.clone();
                                copiedTrap2.setBeginUnit(copiedHead);
                                this.trapChain.insertAfter(copiedTrap2, trap);
                            } else {
                                openedTraps.remove(copiedTrap2);
                            }
                            copiedTrap2.setEndUnit(last);
                        }
                    }
                }
                head = chain.getSuccOf(curr);
            } else {
                for (Trap t : openedTraps) {
                    t.setEndUnit(last);
                }
                return copiedHead;
            }
        }
    }

    private void unrollConditions(Block block) {
        if (this.visitedBlocks.contains(block)) {
            return;
        }
        this.visitedBlocks.add(block);
        this.visitingSuccs.add(block);
        for (Block succ : block.getSuccs()) {
            if (this.visitedBlocks.contains(succ)) {
                if (succ != block && this.visitingSuccs.contains(succ) && succ.getPreds().size() >= 2 && succ.getSuccs().size() == 2 && getSize(succ) <= this.maxSize) {
                    Unit copiedHead = copyBlock(succ);
                    Unit loopTail = block.getTail();
                    if (loopTail instanceof GotoStmt) {
                        ((GotoStmt) loopTail).setTarget(copiedHead);
                    } else if (loopTail instanceof IfStmt) {
                        IfStmt tailIf = (IfStmt) loopTail;
                        if (tailIf.getTarget() == succ.getHead()) {
                            tailIf.setTarget(copiedHead);
                        } else {
                            insertGotoAfter(loopTail, copiedHead);
                        }
                    } else {
                        insertGotoAfter(loopTail, copiedHead);
                    }
                }
            } else {
                unrollConditions(succ);
            }
        }
        this.visitingSuccs.remove(block);
    }
}
