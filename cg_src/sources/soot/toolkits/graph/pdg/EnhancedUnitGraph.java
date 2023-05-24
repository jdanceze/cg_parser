package soot.toolkits.graph.pdg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.Trap;
import soot.Unit;
import soot.toolkits.graph.UnitGraph;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/pdg/EnhancedUnitGraph.class */
public class EnhancedUnitGraph extends UnitGraph {
    protected final Hashtable<Unit, Unit> try2nop;
    protected final Hashtable<Unit, Unit> handler2header;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !EnhancedUnitGraph.class.desiredAssertionStatus();
    }

    public EnhancedUnitGraph(Body body) {
        super(body);
        this.try2nop = new Hashtable<>();
        this.handler2header = new Hashtable<>();
        int size = this.unitChain.size() + body.getTraps().size() + 2;
        this.unitToSuccs = new HashMap((size * 2) + 1, 0.7f);
        this.unitToPreds = new HashMap((size * 2) + 1, 0.7f);
        Iterator<Unit> it = body.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            this.unitToSuccs.put(u, new ArrayList());
            this.unitToPreds.put(u, new ArrayList());
        }
        buildUnexceptionalEdges(this.unitToSuccs, this.unitToPreds);
        addAuxiliaryExceptionalEdges();
        buildHeadsAndTails();
        handleExplicitThrowEdges();
        buildHeadsAndTails();
        handleMultipleReturns();
        buildHeadsAndTails();
        removeBogusHeads();
        buildHeadsAndTails();
    }

    protected void handleMultipleReturns() {
        if (getTails().size() > 1) {
            Unit stop = new ExitStmt();
            List<Unit> predsOfstop = new ArrayList<>();
            for (Unit tail : getTails()) {
                predsOfstop.add(tail);
                List<Unit> tailSuccs = this.unitToSuccs.get(tail);
                if (tailSuccs == null) {
                    tailSuccs = new ArrayList<>();
                    this.unitToSuccs.put(tail, tailSuccs);
                }
                tailSuccs.add(stop);
            }
            this.unitToPreds.put(stop, predsOfstop);
            this.unitToSuccs.put(stop, new ArrayList());
            Chain<Unit> units = this.body.getUnits().getNonPatchingChain();
            if (!units.contains(stop)) {
                units.addLast(stop);
            }
        }
    }

    protected void removeBogusHeads() {
        Chain<Unit> units = this.body.getUnits();
        Unit trueHead = units.getFirst();
        while (getHeads().size() > 1) {
            for (Unit head : getHeads()) {
                if (trueHead != head) {
                    this.unitToPreds.remove(head);
                    for (Unit succ : this.unitToSuccs.get(head)) {
                        List<Unit> tobeRemoved = new ArrayList<>();
                        List<Unit> predOfSuccs = this.unitToPreds.get(succ);
                        if (predOfSuccs != null) {
                            for (Unit pred : predOfSuccs) {
                                if (pred == head) {
                                    tobeRemoved.add(pred);
                                }
                            }
                            predOfSuccs.removeAll(tobeRemoved);
                        }
                    }
                    this.unitToSuccs.remove(head);
                    if (units.contains(head)) {
                        units.remove(head);
                    }
                }
            }
            buildHeadsAndTails();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:119:0x030e A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:125:0x0383 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void handleExplicitThrowEdges() {
        /*
            Method dump skipped, instructions count: 910
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.toolkits.graph.pdg.EnhancedUnitGraph.handleExplicitThrowEdges():void");
    }

    protected void addAuxiliaryExceptionalEdges() {
        Unit pred;
        for (Trap trap : this.body.getTraps()) {
            Unit handler = trap.getHandlerUnit();
            Unit unit = handler;
            while (true) {
                pred = unit;
                List<Unit> preds = this.unitToPreds.get(pred);
                if (preds.isEmpty()) {
                    break;
                }
                unit = preds.get(0);
            }
            this.handler2header.put(handler, pred);
            Unit trapBegin = trap.getBeginUnit();
            if (!this.try2nop.containsKey(trapBegin)) {
                this.try2nop.put(trapBegin, new EHNopStmt());
            }
        }
        Hashtable<Unit, Boolean> nop2added = new Hashtable<>();
        for (Trap trap2 : this.body.getTraps()) {
            Unit b = trap2.getBeginUnit();
            Unit handler2 = this.handler2header.get(trap2.getHandlerUnit());
            if (this.unitToPreds.containsKey(handler2)) {
                Iterator<Unit> it = this.unitToPreds.get(handler2).iterator();
                while (true) {
                    if (it.hasNext()) {
                        Unit u = it.next();
                        if (this.try2nop.containsValue(u)) {
                            break;
                        }
                    } else {
                        Unit ehnop = this.try2nop.get(b);
                        if (!nop2added.containsKey(ehnop)) {
                            List<Unit> predsOfB = getPredsOf(b);
                            List<Unit> predsOfehnop = new ArrayList<>(predsOfB);
                            for (Unit a : predsOfB) {
                                List<Unit> succsOfA = this.unitToSuccs.get(a);
                                if (succsOfA == null) {
                                    succsOfA = new ArrayList<>();
                                    this.unitToSuccs.put(a, succsOfA);
                                } else {
                                    succsOfA.remove(b);
                                }
                                succsOfA.add(ehnop);
                            }
                            predsOfB.clear();
                            predsOfB.add(ehnop);
                            this.unitToPreds.put(ehnop, predsOfehnop);
                        }
                        List<Unit> succsOfehnop = this.unitToSuccs.get(ehnop);
                        if (succsOfehnop == null) {
                            succsOfehnop = new ArrayList<>();
                            this.unitToSuccs.put(ehnop, succsOfehnop);
                        }
                        if (!succsOfehnop.contains(b)) {
                            succsOfehnop.add(b);
                        }
                        succsOfehnop.add(handler2);
                        List<Unit> predsOfhandler = this.unitToPreds.get(handler2);
                        if (predsOfhandler == null) {
                            predsOfhandler = new ArrayList<>();
                            this.unitToPreds.put(handler2, predsOfhandler);
                        }
                        predsOfhandler.add(ehnop);
                        Chain<Unit> units = this.body.getUnits().getNonPatchingChain();
                        if (!units.contains(ehnop)) {
                            units.insertBefore(ehnop, b);
                        }
                        nop2added.put(ehnop, Boolean.TRUE);
                    }
                }
            }
        }
    }
}
