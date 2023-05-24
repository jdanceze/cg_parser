package soot.toolkits.scalar;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.RandomAccess;
import soot.baf.GotoInst;
import soot.jimple.GotoStmt;
import soot.options.Options;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.interaction.FlowInfo;
import soot.toolkits.graph.interaction.InteractionHandler;
import soot.util.Numberable;
import soot.util.PriorityQueue;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/FlowAnalysis.class */
public abstract class FlowAnalysis<N, A> extends AbstractFlowAnalysis<N, A> {
    protected final Map<N, A> unitToAfterFlow;
    protected Map<N, A> filterUnitToAfterFlow;
    static final /* synthetic */ boolean $assertionsDisabled;

    protected abstract void flowThrough(A a, N n, A a2);

    /* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/FlowAnalysis$Flow.class */
    public enum Flow {
        IN { // from class: soot.toolkits.scalar.FlowAnalysis.Flow.1
            @Override // soot.toolkits.scalar.FlowAnalysis.Flow
            <F> F getFlow(Entry<?, F> e) {
                return e.inFlow;
            }
        },
        OUT { // from class: soot.toolkits.scalar.FlowAnalysis.Flow.2
            @Override // soot.toolkits.scalar.FlowAnalysis.Flow
            <F> F getFlow(Entry<?, F> e) {
                return e.outFlow;
            }
        };

        abstract <F> F getFlow(Entry<?, F> entry);

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static Flow[] valuesCustom() {
            Flow[] valuesCustom = values();
            int length = valuesCustom.length;
            Flow[] flowArr = new Flow[length];
            System.arraycopy(valuesCustom, 0, flowArr, 0, length);
            return flowArr;
        }

        /* synthetic */ Flow(Flow flow) {
            this();
        }
    }

    static {
        $assertionsDisabled = !FlowAnalysis.class.desiredAssertionStatus();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/FlowAnalysis$Entry.class */
    public static class Entry<D, F> implements Numberable {
        final D data;
        int number = Integer.MIN_VALUE;
        boolean isRealStronglyConnected = false;
        Entry<D, F>[] in;
        Entry<D, F>[] out;
        F inFlow;
        F outFlow;

        Entry(D u, Entry<D, F> pred) {
            this.in = new Entry[]{pred};
            this.data = u;
        }

        public String toString() {
            return this.data == null ? "" : this.data.toString();
        }

        @Override // soot.util.Numberable
        public void setNumber(int n) {
            this.number = n;
        }

        @Override // soot.util.Numberable
        public int getNumber() {
            return this.number;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/FlowAnalysis$Orderer.class */
    enum Orderer {
        INSTANCE;
        
        static final /* synthetic */ boolean $assertionsDisabled;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static Orderer[] valuesCustom() {
            Orderer[] valuesCustom = values();
            int length = valuesCustom.length;
            Orderer[] ordererArr = new Orderer[length];
            System.arraycopy(valuesCustom, 0, ordererArr, 0, length);
            return ordererArr;
        }

        static {
            $assertionsDisabled = !FlowAnalysis.class.desiredAssertionStatus();
        }

        /* JADX WARN: Multi-variable type inference failed */
        <D, F> List<Entry<D, F>> newUniverse(DirectedGraph<D> g, GraphView gv, F entryFlow, boolean isForward) {
            List<N> entries;
            int n = g.size();
            ArrayDeque arrayDeque = new ArrayDeque(n);
            List<Entry<D, F>> universe = new ArrayList<>(n);
            HashMap hashMap = new HashMap(((n + 1) * 4) / 3);
            Entry<D, F> superEntry = new Entry<>(null, null);
            List<N> entries2 = gv.getEntries(g);
            if (!entries2.isEmpty()) {
                entries = entries2;
            } else if (isForward) {
                throw new RuntimeException("error: no entry point for method in forward analysis");
            } else {
                List<N> arrayList = new ArrayList<>();
                if (!$assertionsDisabled && g.getHeads().size() != 1) {
                    throw new AssertionError();
                }
                Object obj = g.getHeads().get(0);
                HashSet hashSet = new HashSet();
                ArrayList arrayList2 = new ArrayList();
                arrayList2.add(obj);
                while (!arrayList2.isEmpty()) {
                    Object remove = arrayList2.remove(0);
                    hashSet.add(remove);
                    if ((remove instanceof GotoInst) || (remove instanceof GotoStmt)) {
                        arrayList.add(remove);
                    }
                    for (Object obj2 : g.getSuccsOf(remove)) {
                        if (!hashSet.contains(obj2)) {
                            arrayList2.add(obj2);
                        }
                    }
                }
                boolean isEmpty = arrayList.isEmpty();
                entries = arrayList;
                if (isEmpty) {
                    throw new RuntimeException("error: backward analysis on an empty entry set.");
                }
            }
            visitEntry(hashMap, superEntry, entries);
            superEntry.inFlow = entryFlow;
            superEntry.outFlow = entryFlow;
            Entry<D, F>[] sv = new Entry[g.size()];
            int[] si = new int[g.size()];
            int index = 0;
            int i = 0;
            Entry<D, F> v = superEntry;
            while (true) {
                if (i < v.out.length) {
                    int i2 = i;
                    i++;
                    Entry<D, F> w = v.out[i2];
                    if (w.number == Integer.MIN_VALUE) {
                        w.number = arrayDeque.size();
                        arrayDeque.add(w);
                        visitEntry(hashMap, w, gv.getOut(g, w.data));
                        si[index] = i;
                        sv[index] = v;
                        index++;
                        i = 0;
                        v = w;
                    }
                } else if (index == 0) {
                    break;
                } else {
                    universe.add(v);
                    sccPop(arrayDeque, v);
                    index--;
                    v = sv[index];
                    i = si[index];
                }
            }
            if ($assertionsDisabled || universe.size() <= g.size()) {
                Collections.reverse(universe);
                return universe;
            }
            throw new AssertionError();
        }

        private <D, F> Entry<D, F>[] visitEntry(Map<D, Entry<D, F>> visited, Entry<D, F> v, List<D> out) {
            int n = out.size();
            Entry[] a = new Entry[n];
            if ($assertionsDisabled || (out instanceof RandomAccess)) {
                for (int i = 0; i < n; i++) {
                    a[i] = getEntryOf(visited, out.get(i), v);
                }
                v.out = a;
                return a;
            }
            throw new AssertionError();
        }

        private <D, F> Entry<D, F> getEntryOf(Map<D, Entry<D, F>> visited, D d, Entry<D, F> v) {
            Entry<D, F> newEntry = new Entry<>(d, v);
            Entry<D, F> oldEntry = visited.putIfAbsent(d, newEntry);
            if (oldEntry == null) {
                return newEntry;
            }
            if (oldEntry == v) {
                oldEntry.isRealStronglyConnected = true;
            }
            int l = oldEntry.in.length;
            oldEntry.in = (Entry[]) Arrays.copyOf(oldEntry.in, l + 1);
            oldEntry.in[l] = v;
            return oldEntry;
        }

        private <D, F> void sccPop(Deque<Entry<D, F>> s, Entry<D, F> v) {
            Entry<D, F>[] entryArr;
            Entry<D, F> w;
            int min = v.number;
            for (Entry<D, F> e : v.out) {
                if (!$assertionsDisabled && e.number <= Integer.MIN_VALUE) {
                    throw new AssertionError();
                }
                min = Math.min(min, e.number);
            }
            if (min != v.number) {
                v.number = min;
                return;
            }
            Entry<D, F> w2 = s.removeLast();
            w2.number = Integer.MAX_VALUE;
            if (w2 == v) {
                return;
            }
            w2.isRealStronglyConnected = true;
            do {
                w = s.removeLast();
                if (!$assertionsDisabled && w.number < v.number) {
                    throw new AssertionError();
                }
                w.isRealStronglyConnected = true;
                w.number = Integer.MAX_VALUE;
            } while (w != v);
            if (!$assertionsDisabled && w.in.length < 2) {
                throw new AssertionError();
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/FlowAnalysis$InteractionFlowHandler.class */
    enum InteractionFlowHandler {
        NONE,
        FORWARD { // from class: soot.toolkits.scalar.FlowAnalysis.InteractionFlowHandler.1
            @Override // soot.toolkits.scalar.FlowAnalysis.InteractionFlowHandler
            public <A, N> void handleFlowIn(FlowAnalysis<N, A> a, N s) {
                beforeEvent(stop(s), a, s);
            }

            @Override // soot.toolkits.scalar.FlowAnalysis.InteractionFlowHandler
            public <A, N> void handleFlowOut(FlowAnalysis<N, A> a, N s) {
                afterEvent(InteractionHandler.v(), a, s);
            }
        },
        BACKWARD { // from class: soot.toolkits.scalar.FlowAnalysis.InteractionFlowHandler.2
            @Override // soot.toolkits.scalar.FlowAnalysis.InteractionFlowHandler
            public <A, N> void handleFlowIn(FlowAnalysis<N, A> a, N s) {
                afterEvent(stop(s), a, s);
            }

            @Override // soot.toolkits.scalar.FlowAnalysis.InteractionFlowHandler
            public <A, N> void handleFlowOut(FlowAnalysis<N, A> a, N s) {
                beforeEvent(InteractionHandler.v(), a, s);
            }
        };

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static InteractionFlowHandler[] valuesCustom() {
            InteractionFlowHandler[] valuesCustom = values();
            int length = valuesCustom.length;
            InteractionFlowHandler[] interactionFlowHandlerArr = new InteractionFlowHandler[length];
            System.arraycopy(valuesCustom, 0, interactionFlowHandlerArr, 0, length);
            return interactionFlowHandlerArr;
        }

        /* synthetic */ InteractionFlowHandler(InteractionFlowHandler interactionFlowHandler) {
            this();
        }

        /* JADX WARN: Multi-variable type inference failed */
        <A, N> void beforeEvent(InteractionHandler i, FlowAnalysis<N, A> a, N s) {
            A savedFlow = a.filterUnitToBeforeFlow.get(s);
            if (savedFlow == null) {
                savedFlow = a.newInitialFlow();
            }
            a.copy(a.unitToBeforeFlow.get(s), savedFlow);
            i.handleBeforeAnalysisEvent(new FlowInfo(savedFlow, s, true));
        }

        /* JADX WARN: Multi-variable type inference failed */
        <A, N> void afterEvent(InteractionHandler i, FlowAnalysis<N, A> a, N s) {
            A savedFlow = a.filterUnitToAfterFlow.get(s);
            if (savedFlow == null) {
                savedFlow = a.newInitialFlow();
            }
            a.copy(a.unitToAfterFlow.get(s), savedFlow);
            i.handleAfterAnalysisEvent(new FlowInfo(savedFlow, s, false));
        }

        InteractionHandler stop(Object s) {
            InteractionHandler h = InteractionHandler.v();
            List<?> stopList = h.getStopUnitList();
            if (stopList != null && stopList.contains(s)) {
                h.handleStopAtNodeEvent(s);
            }
            return h;
        }

        public <A, N> void handleFlowIn(FlowAnalysis<N, A> a, N s) {
        }

        public <A, N> void handleFlowOut(FlowAnalysis<N, A> a, N s) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/FlowAnalysis$GraphView.class */
    public enum GraphView {
        BACKWARD { // from class: soot.toolkits.scalar.FlowAnalysis.GraphView.1
            @Override // soot.toolkits.scalar.FlowAnalysis.GraphView
            <N> List<N> getEntries(DirectedGraph<N> g) {
                return g.getTails();
            }

            @Override // soot.toolkits.scalar.FlowAnalysis.GraphView
            <N> List<N> getOut(DirectedGraph<N> g, N s) {
                return g.getPredsOf(s);
            }
        },
        FORWARD { // from class: soot.toolkits.scalar.FlowAnalysis.GraphView.2
            @Override // soot.toolkits.scalar.FlowAnalysis.GraphView
            <N> List<N> getEntries(DirectedGraph<N> g) {
                return g.getHeads();
            }

            @Override // soot.toolkits.scalar.FlowAnalysis.GraphView
            <N> List<N> getOut(DirectedGraph<N> g, N s) {
                return g.getSuccsOf(s);
            }
        };

        abstract <N> List<N> getEntries(DirectedGraph<N> directedGraph);

        abstract <N> List<N> getOut(DirectedGraph<N> directedGraph, N n);

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static GraphView[] valuesCustom() {
            GraphView[] valuesCustom = values();
            int length = valuesCustom.length;
            GraphView[] graphViewArr = new GraphView[length];
            System.arraycopy(valuesCustom, 0, graphViewArr, 0, length);
            return graphViewArr;
        }

        /* synthetic */ GraphView(GraphView graphView) {
            this();
        }
    }

    public FlowAnalysis(DirectedGraph<N> graph) {
        super(graph);
        this.unitToAfterFlow = new IdentityHashMap((graph.size() * 2) + 1);
        this.filterUnitToAfterFlow = Collections.emptyMap();
    }

    public A getFlowAfter(N s) {
        A a = this.unitToAfterFlow.get(s);
        return a == null ? newInitialFlow() : a;
    }

    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public A getFlowBefore(N s) {
        A a = this.unitToBeforeFlow.get(s);
        return a == null ? newInitialFlow() : a;
    }

    /* JADX WARN: Type inference failed for: r1v15, types: [F] */
    /* JADX WARN: Type inference failed for: r1v17, types: [F, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r1v2, types: [F, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r1v7, types: [F, java.lang.Object] */
    private void initFlow(Iterable<Entry<N, A>> universe, Map<N, A> in, Map<N, A> out) {
        if (!$assertionsDisabled && universe == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && in == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && out == null) {
            throw new AssertionError();
        }
        for (Entry<N, A> n : universe) {
            boolean omit = true;
            if (n.in.length > 1) {
                n.inFlow = newInitialFlow();
                omit = !n.isRealStronglyConnected;
            } else if (!$assertionsDisabled && n.in.length != 1) {
                throw new AssertionError("missing superhead");
            } else {
                n.inFlow = getFlow((Entry) n.in[0], (Entry) n);
                if (!$assertionsDisabled && n.inFlow == null) {
                    throw new AssertionError("topological order is broken");
                }
            }
            if (omit && omissible(n.data)) {
                n.outFlow = n.inFlow;
            } else {
                n.outFlow = newInitialFlow();
            }
            in.put(n.data, n.inFlow);
            out.put(n.data, n.outFlow);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean omissible(N n) {
        return false;
    }

    protected Flow getFlow(N from, N mergeNode) {
        return Flow.OUT;
    }

    private A getFlow(Entry<N, A> o, Entry<N, A> e) {
        return o.inFlow == o.outFlow ? o.outFlow : (A) getFlow(o.data, e.data).getFlow(o);
    }

    private void meetFlows(Entry<N, A> e) {
        Entry<N, A>[] entryArr;
        if (!$assertionsDisabled && e.in.length < 1) {
            throw new AssertionError();
        }
        if (e.in.length > 1) {
            boolean copy = true;
            for (Entry<N, A> o : e.in) {
                A flow = getFlow((Entry) o, (Entry) e);
                if (copy) {
                    copy = false;
                    copy(flow, e.inFlow);
                } else {
                    mergeInto(e.data, e.inFlow, flow);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    public final int doAnalysis(GraphView gv, InteractionFlowHandler ifh, Map<N, A> inFlow, Map<N, A> outFlow) {
        if (!$assertionsDisabled && gv == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && ifh == null) {
            throw new AssertionError();
        }
        InteractionFlowHandler ifh2 = Options.v().interactive_mode() ? ifh : InteractionFlowHandler.NONE;
        List<Entry<N, A>> universe = Orderer.INSTANCE.newUniverse(this.graph, gv, entryInitialFlow(), isForward());
        initFlow(universe, inFlow, outFlow);
        Queue<Entry<N, A>> q = PriorityQueue.of(universe, true);
        int numComputations = 0;
        while (true) {
            Entry<N, A> e = q.poll();
            if (e == null) {
                return numComputations;
            }
            meetFlows(e);
            ifh2.handleFlowIn(this, e.data);
            boolean hasChanged = flowThrough(e);
            ifh2.handleFlowOut(this, e.data);
            if (hasChanged) {
                q.addAll(Arrays.asList(e.out));
            }
            numComputations++;
        }
    }

    private boolean flowThrough(Entry<N, A> d) {
        if (d.inFlow == d.outFlow) {
            if ($assertionsDisabled || !d.isRealStronglyConnected || d.in.length == 1) {
                return true;
            }
            throw new AssertionError();
        } else if (d.isRealStronglyConnected) {
            A out = newInitialFlow();
            flowThrough(d.inFlow, d.data, out);
            if (out.equals(d.outFlow)) {
                return false;
            }
            copyFreshToExisting(out, d.outFlow);
            return true;
        } else {
            flowThrough(d.inFlow, d.data, d.outFlow);
            return true;
        }
    }

    protected void copyFreshToExisting(A in, A dest) {
        if ((in instanceof FlowSet) && (dest instanceof FlowSet)) {
            FlowSet<?> fin = (FlowSet) in;
            FlowSet fdest = (FlowSet) dest;
            fin.copyFreshToExisting(fdest);
            return;
        }
        copy(in, dest);
    }
}
