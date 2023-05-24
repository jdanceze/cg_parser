package soot.shimple.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.PatchingChain;
import soot.TrapManager;
import soot.Unit;
import soot.UnitBox;
import soot.UnitPatchingChain;
import soot.options.Options;
import soot.shimple.PhiExpr;
import soot.shimple.Shimple;
import soot.shimple.ShimpleBody;
import soot.util.Chain;
import soot.util.HashMultiMap;
import soot.util.MultiMap;
/* loaded from: gencallgraphv3.jar:soot/shimple/internal/SPatchingChain.class */
public class SPatchingChain extends UnitPatchingChain {
    private static final Logger logger = LoggerFactory.getLogger(SPatchingChain.class);
    protected final Body body;
    protected final boolean debug;
    protected Map<UnitBox, Unit> boxToPhiNode;
    protected Set<Unit> phiNodeSet;
    protected Map<SUnitBox, Boolean> boxToNeedsPatching;

    public SPatchingChain(Body aBody, Chain<Unit> aChain) {
        super(aChain);
        this.boxToPhiNode = new HashMap();
        this.phiNodeSet = new HashSet();
        this.boxToNeedsPatching = new HashMap();
        this.body = aBody;
        boolean debug = Options.v().debug();
        this.debug = aBody instanceof ShimpleBody ? debug | ((ShimpleBody) aBody).getOptions().debug() : debug;
    }

    @Override // soot.PatchingChain
    public boolean add(Unit o) {
        processPhiNode(o);
        return super.add((SPatchingChain) o);
    }

    @Override // soot.PatchingChain
    public void swapWith(Unit out, Unit in) {
        processPhiNode(in);
        Shimple.redirectPointers(out, in);
        super.insertBefore(in, out);
        remove(out);
    }

    @Override // soot.PatchingChain
    public void insertAfter(Unit toInsert, Unit point) {
        processPhiNode(toInsert);
        super.insertAfter(toInsert, point);
        if (!point.fallsThrough()) {
            return;
        }
        if (!point.branches() && (this.body == null || !TrapManager.getTrappedUnitsOf(this.body).contains(point))) {
            Shimple.redirectPointers(point, toInsert);
            return;
        }
        Iterator it = new ArrayList(point.getBoxesPointingToThis()).iterator();
        while (it.hasNext()) {
            UnitBox ub = (UnitBox) it.next();
            if (ub.getUnit() != point) {
                throw new RuntimeException("Assertion failed.");
            }
            if (!ub.isBranchTarget()) {
                SUnitBox box = getSBox(ub);
                Boolean needsPatching = this.boxToNeedsPatching.get(box);
                if (needsPatching == null || box.isUnitChanged()) {
                    if (!this.boxToPhiNode.containsKey(box)) {
                        reprocessPhiNodes();
                        if (!this.boxToPhiNode.containsKey(box) && this.debug) {
                            throw new RuntimeException("SPatchingChain has pointers from a Phi node that has never been seen.");
                        }
                    }
                    computeNeedsPatching();
                    needsPatching = this.boxToNeedsPatching.get(box);
                    if (needsPatching == null) {
                        if (this.debug) {
                            logger.warn("Orphaned UnitBox to " + point + "?  SPatchingChain will not move the pointer.");
                        }
                    }
                }
                if (needsPatching.booleanValue()) {
                    box.setUnit(toInsert);
                    box.setUnitChanged(false);
                }
            }
        }
    }

    @Override // soot.PatchingChain
    public void insertAfter(List<Unit> toInsert, Unit point) {
        for (Unit unit : toInsert) {
            processPhiNode(unit);
        }
        super.insertAfter((List<List<Unit>>) toInsert, (List<Unit>) point);
    }

    @Override // soot.PatchingChain
    public void insertAfter(Chain<Unit> toInsert, Unit point) {
        for (Unit unit : toInsert) {
            processPhiNode(unit);
        }
        super.insertAfter((Chain<Chain<Unit>>) toInsert, (Chain<Unit>) point);
    }

    @Override // soot.PatchingChain
    public void insertAfter(Collection<? extends Unit> toInsert, Unit point) {
        for (Unit unit : toInsert) {
            processPhiNode(unit);
        }
        super.insertAfter((Collection<? extends Collection<? extends Unit>>) toInsert, (Collection<? extends Unit>) point);
    }

    @Override // soot.PatchingChain
    public void insertBefore(Unit toInsert, Unit point) {
        processPhiNode(toInsert);
        super.insertBefore(toInsert, point);
    }

    @Override // soot.PatchingChain
    public void insertBefore(List<Unit> toInsert, Unit point) {
        for (Unit unit : toInsert) {
            processPhiNode(unit);
        }
        super.insertBefore((List<List<Unit>>) toInsert, (List<Unit>) point);
    }

    @Override // soot.PatchingChain
    public void insertBefore(Chain<Unit> toInsert, Unit point) {
        for (Unit unit : toInsert) {
            processPhiNode(unit);
        }
        super.insertBefore((Chain<Chain<Unit>>) toInsert, (Chain<Unit>) point);
    }

    @Override // soot.PatchingChain
    public void insertBefore(Collection<? extends Unit> toInsert, Unit point) {
        for (Unit unit : toInsert) {
            processPhiNode(unit);
        }
        super.insertBefore((Collection<? extends Collection<? extends Unit>>) toInsert, (Collection<? extends Unit>) point);
    }

    @Override // soot.PatchingChain
    public void addFirst(Unit u) {
        processPhiNode(u);
        super.addFirst((SPatchingChain) u);
    }

    @Override // soot.PatchingChain
    public void addLast(Unit u) {
        processPhiNode(u);
        super.addLast((SPatchingChain) u);
    }

    public boolean remove(Unit u) {
        if (contains(u)) {
            Shimple.redirectToPreds(this.body, u);
            boolean ret = super.remove((Object) u);
            patchAfterRemoval(u);
            return ret;
        }
        return false;
    }

    @Override // soot.PatchingChain, java.util.AbstractCollection, java.util.Collection, soot.util.Chain
    public boolean remove(Object obj) {
        if (obj instanceof Unit) {
            return remove((Unit) obj);
        }
        return false;
    }

    protected static void patchAfterRemoval(Unit removing) {
        if (removing != null) {
            for (UnitBox ub : removing.getUnitBoxes()) {
                Unit u = ub.getUnit();
                if (u != null) {
                    u.removeBoxPointingToThis(ub);
                }
            }
        }
    }

    protected void processPhiNode(Unit phiNode) {
        PhiExpr phi = Shimple.getPhiExpr(phiNode);
        if (phi == null || this.phiNodeSet.contains(phiNode)) {
            return;
        }
        for (UnitBox box : phi.getUnitBoxes()) {
            this.boxToPhiNode.put(box, phiNode);
            this.phiNodeSet.add(phiNode);
        }
    }

    protected void reprocessPhiNodes() {
        Set<Unit> phiNodes = new HashSet<>(this.boxToPhiNode.values());
        this.boxToPhiNode = new HashMap();
        this.phiNodeSet = new HashSet();
        this.boxToNeedsPatching = new HashMap();
        for (Unit next : phiNodes) {
            processPhiNode(next);
        }
    }

    protected void computeNeedsPatching() {
        if (this.boxToPhiNode.isEmpty()) {
            return;
        }
        MultiMap<Unit, UnitBox> trackedPhiToBoxes = new HashMultiMap<>();
        Set<Unit> trackedBranchTargets = new HashSet<>();
        Iterator<Unit> it = iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            List<UnitBox> boxesToTrack = u.getBoxesPointingToThis();
            if (boxesToTrack != null) {
                for (UnitBox boxToTrack : boxesToTrack) {
                    if (!boxToTrack.isBranchTarget()) {
                        trackedPhiToBoxes.put(this.boxToPhiNode.get(boxToTrack), boxToTrack);
                    }
                }
            }
            if (u.fallsThrough() && u.branches()) {
                for (UnitBox ub : u.getUnitBoxes()) {
                    trackedBranchTargets.add(ub.getUnit());
                }
            }
            if (!u.fallsThrough() || trackedBranchTargets.contains(u)) {
                for (UnitBox next : trackedPhiToBoxes.values()) {
                    SUnitBox box = getSBox(next);
                    this.boxToNeedsPatching.put(box, Boolean.FALSE);
                    box.setUnitChanged(false);
                }
                trackedPhiToBoxes.clear();
            } else {
                Set<UnitBox> boxes = trackedPhiToBoxes.get(u);
                if (boxes != null) {
                    for (UnitBox ub2 : boxes) {
                        SUnitBox box2 = getSBox(ub2);
                        this.boxToNeedsPatching.put(box2, Boolean.TRUE);
                        box2.setUnitChanged(false);
                    }
                    trackedPhiToBoxes.remove(u);
                }
            }
        }
        for (UnitBox next2 : trackedPhiToBoxes.values()) {
            SUnitBox box3 = getSBox(next2);
            this.boxToNeedsPatching.put(box3, Boolean.FALSE);
            box3.setUnitChanged(false);
        }
    }

    protected SUnitBox getSBox(UnitBox box) {
        if (box instanceof SUnitBox) {
            return (SUnitBox) box;
        }
        throw new RuntimeException("Shimple box not an SUnitBox?");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:soot/shimple/internal/SPatchingChain$SPatchingIterator.class */
    public class SPatchingIterator extends PatchingChain<Unit>.PatchingIterator {
        SPatchingIterator(Chain<Unit> innerChain) {
            super(innerChain);
        }

        SPatchingIterator(Chain<Unit> innerChain, Unit u) {
            super(innerChain, u);
        }

        SPatchingIterator(Chain<Unit> innerChain, Unit head, Unit tail) {
            super(innerChain, head, tail);
        }

        @Override // soot.PatchingChain.PatchingIterator, java.util.Iterator
        public void remove() {
            if (!this.state) {
                throw new IllegalStateException("remove called before first next() call");
            }
            this.state = false;
            Unit victim = this.lastObject;
            Shimple.redirectToPreds(SPatchingChain.this.body, victim);
            SPatchingChain.patchBeforeRemoval(this.innerChain, victim);
            this.innerIterator.remove();
            SPatchingChain.patchAfterRemoval(victim);
        }
    }

    @Override // soot.PatchingChain, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, soot.util.Chain
    public Iterator<Unit> iterator() {
        return new SPatchingIterator(this.innerChain);
    }

    @Override // soot.PatchingChain
    public Iterator<Unit> iterator(Unit u) {
        return new SPatchingIterator(this.innerChain, u);
    }

    @Override // soot.PatchingChain
    public Iterator<Unit> iterator(Unit head, Unit tail) {
        return new SPatchingIterator(this.innerChain, head, tail);
    }
}
