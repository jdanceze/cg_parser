package soot.toolkits.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.RefType;
import soot.Scene;
import soot.Timers;
import soot.Trap;
import soot.Unit;
import soot.baf.ReturnInst;
import soot.baf.ReturnVoidInst;
import soot.baf.ThrowInst;
import soot.jimple.Jimple;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.ThrowStmt;
import soot.options.Options;
import soot.toolkits.exceptions.ThrowAnalysis;
import soot.toolkits.exceptions.ThrowableSet;
import soot.toolkits.graph.ExceptionalGraph;
import soot.util.ArraySet;
import soot.util.Chain;
import soot.util.PhaseDumper;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/ExceptionalUnitGraph.class */
public class ExceptionalUnitGraph extends UnitGraph implements ExceptionalGraph<Unit> {
    protected Map<Unit, List<Unit>> unitToUnexceptionalSuccs;
    protected Map<Unit, List<Unit>> unitToUnexceptionalPreds;
    protected Map<Unit, List<Unit>> unitToExceptionalSuccs;
    protected Map<Unit, List<Unit>> unitToExceptionalPreds;
    protected Map<Unit, Collection<ExceptionDest>> unitToExceptionDests;
    protected ThrowAnalysis throwAnalysis;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ExceptionalUnitGraph.class.desiredAssertionStatus();
    }

    public ExceptionalUnitGraph(Body body, ThrowAnalysis throwAnalysis, boolean omitExceptingUnitEdges) {
        super(body);
        initialize(throwAnalysis, omitExceptingUnitEdges);
    }

    public ExceptionalUnitGraph(Body body, ThrowAnalysis throwAnalysis) {
        this(body, throwAnalysis, Options.v().omit_excepting_unit_edges());
    }

    public ExceptionalUnitGraph(Body body) {
        this(body, Scene.v().getDefaultThrowAnalysis(), Options.v().omit_excepting_unit_edges());
    }

    protected ExceptionalUnitGraph(Body body, boolean ignoredBogusParameter) {
        super(body);
    }

    protected void initialize(ThrowAnalysis throwAnalysis, boolean omitExceptingUnitEdges) {
        Set<Unit> trapUnitsThatAreHeads;
        int size = this.unitChain.size();
        if (Options.v().time()) {
            Timers.v().graphTimer.start();
        }
        this.unitToUnexceptionalSuccs = new LinkedHashMap((size * 2) + 1, 0.7f);
        this.unitToUnexceptionalPreds = new LinkedHashMap((size * 2) + 1, 0.7f);
        buildUnexceptionalEdges(this.unitToUnexceptionalSuccs, this.unitToUnexceptionalPreds);
        this.throwAnalysis = throwAnalysis;
        if (this.body.getTraps().isEmpty()) {
            this.unitToExceptionDests = Collections.emptyMap();
            this.unitToExceptionalSuccs = Collections.emptyMap();
            this.unitToExceptionalPreds = Collections.emptyMap();
            trapUnitsThatAreHeads = Collections.emptySet();
            this.unitToSuccs = this.unitToUnexceptionalSuccs;
            this.unitToPreds = this.unitToUnexceptionalPreds;
        } else {
            this.unitToExceptionDests = buildExceptionDests(throwAnalysis);
            this.unitToExceptionalSuccs = new LinkedHashMap((this.unitToExceptionDests.size() * 2) + 1, 0.7f);
            this.unitToExceptionalPreds = new LinkedHashMap((this.body.getTraps().size() * 2) + 1, 0.7f);
            trapUnitsThatAreHeads = buildExceptionalEdges(throwAnalysis, this.unitToExceptionDests, this.unitToExceptionalSuccs, this.unitToExceptionalPreds, omitExceptingUnitEdges);
            this.unitToSuccs = combineMapValues(this.unitToUnexceptionalSuccs, this.unitToExceptionalSuccs);
            this.unitToPreds = combineMapValues(this.unitToUnexceptionalPreds, this.unitToExceptionalPreds);
        }
        buildHeadsAndTails(trapUnitsThatAreHeads);
        if (Options.v().time()) {
            Timers.v().graphTimer.end();
        }
        PhaseDumper.v().dumpGraph(this);
    }

    protected Map<Unit, Collection<ExceptionDest>> buildExceptionDests(ThrowAnalysis throwAnalysis) {
        Chain<Unit> units = this.body.getUnits();
        Map<Unit, ThrowableSet> unitToUncaughtThrowables = new LinkedHashMap<>(units.size());
        Map<Unit, Collection<ExceptionDest>> result = null;
        for (Trap trap : this.body.getTraps()) {
            RefType catcher = trap.getException().getType();
            Iterator<Unit> unitIt = units.iterator(trap.getBeginUnit(), units.getPredOf(trap.getEndUnit()));
            while (unitIt.hasNext()) {
                Unit unit = unitIt.next();
                ThrowableSet thrownSet = unitToUncaughtThrowables.get(unit);
                if (thrownSet == null) {
                    thrownSet = throwAnalysis.mightThrow(unit);
                }
                ThrowableSet.Pair catchableAs = thrownSet.whichCatchableAs(catcher);
                if (!catchableAs.getCaught().equals(ThrowableSet.Manager.v().EMPTY)) {
                    result = addDestToMap(result, unit, trap, catchableAs.getCaught());
                    unitToUncaughtThrowables.put(unit, catchableAs.getUncaught());
                } else if (!$assertionsDisabled && !thrownSet.equals(catchableAs.getUncaught())) {
                    throw new AssertionError("ExceptionalUnitGraph.buildExceptionDests(): catchableAs.caught == EMPTY, but catchableAs.uncaught != thrownSet" + System.getProperty("line.separator") + this.body.getMethod().getSubSignature() + " Unit: " + unit.toString() + System.getProperty("line.separator") + " catchableAs.getUncaught() == " + catchableAs.getUncaught().toString() + System.getProperty("line.separator") + " thrownSet == " + thrownSet.toString());
                }
            }
        }
        for (Map.Entry<Unit, ThrowableSet> entry : unitToUncaughtThrowables.entrySet()) {
            Unit unit2 = entry.getKey();
            ThrowableSet escaping = entry.getValue();
            if (escaping != ThrowableSet.Manager.v().EMPTY) {
                result = addDestToMap(result, unit2, null, escaping);
            }
        }
        return result == null ? Collections.emptyMap() : result;
    }

    private Map<Unit, Collection<ExceptionDest>> addDestToMap(Map<Unit, Collection<ExceptionDest>> map, Unit u, Trap t, ThrowableSet caught) {
        Collection<ExceptionDest> dests = map == null ? null : map.get(u);
        if (dests == null) {
            if (t == null) {
                return map;
            }
            if (map == null) {
                map = new LinkedHashMap((this.unitChain.size() * 2) + 1);
            }
            dests = new ArrayList<>(3);
            map.put(u, dests);
        }
        dests.add(new ExceptionDest(t, caught));
        return map;
    }

    protected Set<Unit> buildExceptionalEdges(ThrowAnalysis throwAnalysis, Map<Unit, Collection<ExceptionDest>> unitToExceptionDests, Map<Unit, List<Unit>> unitToSuccs, Map<Unit, List<Unit>> unitToPreds, boolean omitExceptingUnitEdges) {
        Set<Unit> trapsThatAreHeads = new ArraySet<>();
        Unit entryPoint = this.unitChain.getFirst();
        for (Map.Entry<Unit, Collection<ExceptionDest>> entry : unitToExceptionDests.entrySet()) {
            Unit thrower = entry.getKey();
            List<Unit> throwersPreds = getUnexceptionalPredsOf(thrower);
            Collection<ExceptionDest> dests = entry.getValue();
            boolean alwaysAddSelfEdges = !omitExceptingUnitEdges || mightHaveSideEffects(thrower);
            ThrowableSet predThrowables = null;
            ThrowableSet selfThrowables = null;
            if (thrower instanceof ThrowInst) {
                ThrowInst throwInst = (ThrowInst) thrower;
                predThrowables = throwAnalysis.mightThrowImplicitly(throwInst);
                selfThrowables = throwAnalysis.mightThrowExplicitly(throwInst);
            } else if (thrower instanceof ThrowStmt) {
                ThrowStmt throwStmt = (ThrowStmt) thrower;
                predThrowables = throwAnalysis.mightThrowImplicitly(throwStmt);
                selfThrowables = throwAnalysis.mightThrowExplicitly(throwStmt);
            }
            for (ExceptionDest dest : dests) {
                if (dest.getTrap() != null) {
                    Unit catcher = dest.getTrap().getHandlerUnit();
                    RefType trapsType = dest.getTrap().getException().getType();
                    if (predThrowables == null || predThrowables.catchableAs(trapsType)) {
                        if (thrower == entryPoint) {
                            trapsThatAreHeads.add(catcher);
                        }
                        for (Unit pred : throwersPreds) {
                            addEdge(unitToSuccs, unitToPreds, pred, catcher);
                        }
                    }
                    if (alwaysAddSelfEdges || (selfThrowables != null && selfThrowables.catchableAs(trapsType))) {
                        addEdge(unitToSuccs, unitToPreds, thrower, catcher);
                    }
                }
            }
        }
        LinkedList<C1CFGEdge> workList = new LinkedList<>();
        for (Trap trap : this.body.getTraps()) {
            Unit handlerStart = trap.getHandlerUnit();
            if (mightThrowToIntraproceduralCatcher(handlerStart)) {
                List<Unit> handlerPreds = getUnexceptionalPredsOf(handlerStart);
                for (Unit pred2 : handlerPreds) {
                    workList.addLast(new C1CFGEdge(pred2, handlerStart));
                }
                List<Unit> handlerPreds2 = getExceptionalPredsOf(handlerStart);
                for (Unit pred3 : handlerPreds2) {
                    workList.addLast(new C1CFGEdge(pred3, handlerStart));
                }
                if (trapsThatAreHeads.contains(handlerStart)) {
                    workList.addLast(new C1CFGEdge(null, handlerStart));
                }
            }
        }
        while (workList.size() > 0) {
            C1CFGEdge edgeToThrower = workList.removeFirst();
            Unit pred4 = edgeToThrower.head;
            Collection<ExceptionDest> throwerDests = getExceptionDests(edgeToThrower.tail);
            for (ExceptionDest dest2 : throwerDests) {
                if (dest2.getTrap() != null) {
                    Unit handlerStart2 = dest2.getTrap().getHandlerUnit();
                    boolean edgeAdded = false;
                    if (pred4 == null) {
                        if (!trapsThatAreHeads.contains(handlerStart2)) {
                            trapsThatAreHeads.add(handlerStart2);
                            edgeAdded = true;
                        }
                    } else if (!getExceptionalSuccsOf(pred4).contains(handlerStart2)) {
                        addEdge(unitToSuccs, unitToPreds, pred4, handlerStart2);
                        edgeAdded = true;
                    }
                    if (edgeAdded && mightThrowToIntraproceduralCatcher(handlerStart2)) {
                        workList.addLast(new C1CFGEdge(pred4, handlerStart2));
                    }
                }
            }
        }
        return trapsThatAreHeads;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: soot.toolkits.graph.ExceptionalUnitGraph$1CFGEdge  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:soot/toolkits/graph/ExceptionalUnitGraph$1CFGEdge.class */
    public class C1CFGEdge {
        Unit head;
        Unit tail;

        C1CFGEdge(Unit head, Unit tail) {
            if (tail == null) {
                throw new RuntimeException("invalid CFGEdge(" + (head == null ? Jimple.NULL : head.toString()) + ',' + Jimple.NULL + ')');
            }
            this.head = head;
            this.tail = tail;
        }

        public boolean equals(Object rhs) {
            if (rhs == this) {
                return true;
            }
            if (!(rhs instanceof C1CFGEdge)) {
                return false;
            }
            C1CFGEdge rhsEdge = (C1CFGEdge) rhs;
            return this.head == rhsEdge.head && this.tail == rhsEdge.tail;
        }

        public int hashCode() {
            int result = (37 * 17) + this.head.hashCode();
            return (37 * result) + this.tail.hashCode();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0044  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static boolean mightHaveSideEffects(soot.Unit r2) {
        /*
            r0 = r2
            boolean r0 = r0 instanceof soot.baf.Inst
            if (r0 == 0) goto L2e
            r0 = r2
            soot.baf.Inst r0 = (soot.baf.Inst) r0
            r3 = r0
            r0 = r3
            boolean r0 = r0.containsInvokeExpr()
            if (r0 != 0) goto L2c
            r0 = r3
            boolean r0 = r0 instanceof soot.baf.StaticPutInst
            if (r0 != 0) goto L2c
            r0 = r3
            boolean r0 = r0 instanceof soot.baf.StaticGetInst
            if (r0 != 0) goto L2c
            r0 = r3
            boolean r0 = r0 instanceof soot.baf.NewInst
            if (r0 != 0) goto L2c
            r0 = 0
            return r0
        L2c:
            r0 = 1
            return r0
        L2e:
            r0 = r2
            boolean r0 = r0 instanceof soot.jimple.Stmt
            if (r0 == 0) goto L75
            r0 = r2
            java.util.List r0 = r0.getUseBoxes()
            java.util.Iterator r0 = r0.iterator()
            r4 = r0
            goto L6c
        L44:
            r0 = r4
            java.lang.Object r0 = r0.next()
            soot.ValueBox r0 = (soot.ValueBox) r0
            r3 = r0
            r0 = r3
            soot.Value r0 = r0.getValue()
            r5 = r0
            r0 = r5
            boolean r0 = r0 instanceof soot.jimple.StaticFieldRef
            if (r0 != 0) goto L6a
            r0 = r5
            boolean r0 = r0 instanceof soot.jimple.InvokeExpr
            if (r0 != 0) goto L6a
            r0 = r5
            boolean r0 = r0 instanceof soot.jimple.NewExpr
            if (r0 == 0) goto L6c
        L6a:
            r0 = 1
            return r0
        L6c:
            r0 = r4
            boolean r0 = r0.hasNext()
            if (r0 != 0) goto L44
        L75:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.toolkits.graph.ExceptionalUnitGraph.mightHaveSideEffects(soot.Unit):boolean");
    }

    private boolean mightThrowToIntraproceduralCatcher(Unit u) {
        for (ExceptionDest dest : getExceptionDests(u)) {
            if (dest.getTrap() != null) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.graph.UnitGraph
    public void buildHeadsAndTails() throws IllegalStateException {
        throw new IllegalStateException("ExceptionalUnitGraph uses buildHeadsAndTails(List) instead of buildHeadsAndTails()");
    }

    private void buildHeadsAndTails(Set<Unit> additionalHeads) {
        this.heads = new ArrayList(additionalHeads.size() + 1);
        this.heads.addAll(additionalHeads);
        if (this.unitChain.isEmpty()) {
            throw new IllegalStateException("No body for method " + this.body.getMethod().getSignature());
        }
        Unit entryPoint = this.unitChain.getFirst();
        if (!this.heads.contains(entryPoint)) {
            this.heads.add(entryPoint);
        }
        this.tails = new ArrayList();
        for (Unit u : this.unitChain) {
            if ((u instanceof ReturnStmt) || (u instanceof ReturnVoidStmt) || (u instanceof ReturnInst) || (u instanceof ReturnVoidInst)) {
                this.tails.add(u);
            } else if ((u instanceof ThrowStmt) || (u instanceof ThrowInst)) {
                int escapeMethodCount = 0;
                for (ExceptionDest dest : getExceptionDests(u)) {
                    if (dest.getTrap() == null) {
                        escapeMethodCount++;
                    }
                }
                if (escapeMethodCount > 0) {
                    this.tails.add(u);
                }
            }
        }
    }

    @Override // soot.toolkits.graph.ExceptionalGraph
    public Collection<ExceptionDest> getExceptionDests(final Unit u) {
        Collection<ExceptionDest> result = this.unitToExceptionDests.get(u);
        if (result == null) {
            ExceptionDest e = new ExceptionDest(null, null) { // from class: soot.toolkits.graph.ExceptionalUnitGraph.1
                private ThrowableSet throwables;

                @Override // soot.toolkits.graph.ExceptionalUnitGraph.ExceptionDest, soot.toolkits.graph.ExceptionalGraph.ExceptionDest
                public ThrowableSet getThrowables() {
                    if (this.throwables == null) {
                        this.throwables = ExceptionalUnitGraph.this.throwAnalysis.mightThrow(u);
                    }
                    return this.throwables;
                }
            };
            return Collections.singletonList(e);
        }
        return result;
    }

    /* loaded from: gencallgraphv3.jar:soot/toolkits/graph/ExceptionalUnitGraph$ExceptionDest.class */
    public static class ExceptionDest implements ExceptionalGraph.ExceptionDest<Unit> {
        private Trap trap;
        private ThrowableSet throwables;

        protected ExceptionDest(Trap trap, ThrowableSet throwables) {
            this.trap = trap;
            this.throwables = throwables;
        }

        public int hashCode() {
            int result = (31 * 1) + (this.throwables == null ? 0 : this.throwables.hashCode());
            return (31 * result) + (this.trap == null ? 0 : this.trap.hashCode());
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ExceptionDest other = (ExceptionDest) obj;
            if (this.throwables == null) {
                if (other.throwables != null) {
                    return false;
                }
            } else if (!this.throwables.equals(other.throwables)) {
                return false;
            }
            if (this.trap == null) {
                if (other.trap != null) {
                    return false;
                }
                return true;
            } else if (!this.trap.equals(other.trap)) {
                return false;
            } else {
                return true;
            }
        }

        @Override // soot.toolkits.graph.ExceptionalGraph.ExceptionDest
        public Trap getTrap() {
            return this.trap;
        }

        @Override // soot.toolkits.graph.ExceptionalGraph.ExceptionDest
        public ThrowableSet getThrowables() {
            return this.throwables;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // soot.toolkits.graph.ExceptionalGraph.ExceptionDest
        public Unit getHandlerNode() {
            if (this.trap == null) {
                return null;
            }
            return this.trap.getHandlerUnit();
        }

        public String toString() {
            StringBuffer buf = new StringBuffer();
            buf.append(getThrowables());
            buf.append(" -> ");
            if (this.trap == null) {
                buf.append("(escapes)");
            } else {
                buf.append(this.trap.toString());
            }
            return buf.toString();
        }
    }

    @Override // soot.toolkits.graph.ExceptionalGraph
    public List<Unit> getUnexceptionalPredsOf(Unit u) {
        List<Unit> preds = this.unitToUnexceptionalPreds.get(u);
        return preds == null ? Collections.emptyList() : preds;
    }

    @Override // soot.toolkits.graph.ExceptionalGraph
    public List<Unit> getUnexceptionalSuccsOf(Unit u) {
        List<Unit> succs = this.unitToUnexceptionalSuccs.get(u);
        return succs == null ? Collections.emptyList() : succs;
    }

    @Override // soot.toolkits.graph.ExceptionalGraph
    public List<Unit> getExceptionalPredsOf(Unit u) {
        List<Unit> preds = this.unitToExceptionalPreds.get(u);
        return preds == null ? Collections.emptyList() : preds;
    }

    @Override // soot.toolkits.graph.ExceptionalGraph
    public List<Unit> getExceptionalSuccsOf(Unit u) {
        List<Unit> succs = this.unitToExceptionalSuccs.get(u);
        return succs == null ? Collections.emptyList() : succs;
    }

    public ThrowAnalysis getThrowAnalysis() {
        return this.throwAnalysis;
    }

    @Override // soot.toolkits.graph.UnitGraph
    public String toString() {
        StringBuilder buf = new StringBuilder();
        for (Unit u : this.unitChain) {
            buf.append("  preds: ").append(getPredsOf(u)).append('\n');
            buf.append("  unexceptional preds: ").append(getUnexceptionalPredsOf(u)).append('\n');
            buf.append("  exceptional preds: ").append(getExceptionalPredsOf(u)).append('\n');
            buf.append(u.toString()).append('\n');
            buf.append("  exception destinations: ").append(getExceptionDests(u)).append('\n');
            buf.append("  unexceptional succs: ").append(getUnexceptionalSuccsOf(u)).append('\n');
            buf.append("  exceptional succs: ").append(getExceptionalSuccsOf(u)).append('\n');
            buf.append("  succs ").append(getSuccsOf(u)).append("\n\n");
        }
        return buf.toString();
    }
}
