package soot;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import soot.jimple.GotoStmt;
import soot.jimple.Jimple;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/UnitPatchingChain.class */
public class UnitPatchingChain extends PatchingChain<Unit> {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !UnitPatchingChain.class.desiredAssertionStatus();
    }

    public UnitPatchingChain(Chain<Unit> aChain) {
        super(aChain);
    }

    public void insertOnEdge(Collection<? extends Unit> toInsert, Unit point_src, Unit point_tgt) {
        if (toInsert == null) {
            throw new RuntimeException("Tried to insert a null Collection into the Chain!");
        }
        if (point_src == null && point_tgt == null) {
            throw new RuntimeException("insertOnEdge failed! Both source unit and target points are null.");
        }
        if (toInsert.isEmpty()) {
            return;
        }
        if (point_src == null) {
            if (!$assertionsDisabled && point_tgt == null) {
                throw new AssertionError();
            }
            point_tgt.redirectJumpsToThisTo(toInsert.iterator().next());
            this.innerChain.insertBefore((Collection<? extends Collection<? extends Unit>>) toInsert, (Collection<? extends Unit>) point_tgt);
        } else if (point_tgt == null) {
            if (!$assertionsDisabled && point_src == null) {
                throw new AssertionError();
            }
            this.innerChain.insertAfter((Collection<? extends Collection<? extends Unit>>) toInsert, (Collection<? extends Unit>) point_src);
        } else if (getSuccOf((UnitPatchingChain) point_src) == point_tgt) {
            Unit firstInserted = toInsert.iterator().next();
            for (UnitBox box : point_src.getUnitBoxes()) {
                if (box.getUnit() == point_tgt) {
                    box.setUnit(firstInserted);
                }
            }
            this.innerChain.insertAfter((Collection<? extends Collection<? extends Unit>>) toInsert, (Collection<? extends Unit>) point_src);
        } else {
            Unit firstInserted2 = toInsert.iterator().next();
            boolean validEdgeFound = false;
            Unit originalPred = getPredOf((UnitPatchingChain) point_tgt);
            for (UnitBox box2 : point_src.getUnitBoxes()) {
                if (box2.getUnit() == point_tgt) {
                    if (point_src instanceof GotoStmt) {
                        box2.setUnit(firstInserted2);
                        this.innerChain.insertAfter((Collection<? extends Collection<? extends Unit>>) toInsert, (Collection<? extends Unit>) point_src);
                        GotoStmt newGotoStmt = Jimple.v().newGotoStmt(point_tgt);
                        if (toInsert instanceof List) {
                            List<? extends Unit> l = (List) toInsert;
                            this.innerChain.insertAfter(newGotoStmt, (GotoStmt) l.get(l.size() - 1));
                            return;
                        }
                        this.innerChain.insertAfter(newGotoStmt, (GotoStmt) toInsert.toArray()[toInsert.size() - 1]);
                        return;
                    }
                    box2.setUnit(firstInserted2);
                    validEdgeFound = true;
                }
            }
            if (validEdgeFound) {
                this.innerChain.insertBefore((Collection<? extends Collection<? extends Unit>>) toInsert, (Collection<? extends Unit>) point_tgt);
                if (originalPred == point_src || (originalPred instanceof GotoStmt)) {
                    return;
                }
                Unit goto_unit = Jimple.v().newGotoStmt(point_tgt);
                this.innerChain.insertBefore((List<List>) Collections.singletonList(goto_unit), (List) firstInserted2);
                return;
            }
            Unit succ = getSuccOf((UnitPatchingChain) point_src);
            if ((succ instanceof GotoStmt) && succ.getUnitBoxes().get(0).getUnit() == point_tgt) {
                succ.redirectJumpsToThisTo(firstInserted2);
                this.innerChain.insertBefore((Collection<? extends Collection<? extends Unit>>) toInsert, (Collection<? extends Unit>) succ);
                return;
            }
            throw new RuntimeException("insertOnEdge failed! No such edge found. The edge on which you want to insert an instrumentation is invalid.");
        }
    }

    public void insertOnEdge(List<Unit> toInsert, Unit point_src, Unit point_tgt) {
        insertOnEdge((Collection<? extends Unit>) toInsert, point_src, point_tgt);
    }

    public void insertOnEdge(Chain<Unit> toInsert, Unit point_src, Unit point_tgt) {
        insertOnEdge((Collection<? extends Unit>) toInsert, point_src, point_tgt);
    }

    public void insertOnEdge(Unit toInsert, Unit point_src, Unit point_tgt) {
        insertOnEdge(Collections.singleton(toInsert), point_src, point_tgt);
    }
}
