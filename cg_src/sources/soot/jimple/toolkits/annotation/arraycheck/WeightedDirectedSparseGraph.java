package soot.jimple.toolkits.annotation.arraycheck;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/arraycheck/WeightedDirectedSparseGraph.class */
class WeightedDirectedSparseGraph {
    private boolean isUnknown;
    private Hashtable<Object, Hashtable<Object, IntContainer>> sources;
    private HashSet vertexes;
    private final HashSet<Object> reachableNodes;
    private final HashSet<WeightedDirectedEdge> reachableEdges;
    private final Hashtable<Object, IntContainer> distance;
    private final Hashtable<Object, Object> pei;

    public WeightedDirectedSparseGraph(HashSet vertexset) {
        this(vertexset, false);
    }

    public WeightedDirectedSparseGraph(HashSet vertexset, boolean isTop) {
        this.sources = new Hashtable<>();
        this.vertexes = new HashSet();
        this.reachableNodes = new HashSet<>();
        this.reachableEdges = new HashSet<>();
        this.distance = new Hashtable<>();
        this.pei = new Hashtable<>();
        this.vertexes = vertexset;
        this.isUnknown = !isTop;
    }

    public void setTop() {
        this.isUnknown = false;
        this.sources.clear();
    }

    public HashSet getVertexes() {
        return this.vertexes;
    }

    public void setVertexes(HashSet newset) {
        this.vertexes = newset;
        this.sources.clear();
    }

    public void addEdge(Object from, Object to, int w) {
        if (this.isUnknown) {
            throw new RuntimeException("Unknown graph can not have edges");
        }
        Hashtable<Object, IntContainer> targets = this.sources.get(from);
        if (targets == null) {
            targets = new Hashtable<>();
            this.sources.put(from, targets);
        }
        IntContainer weight = targets.get(to);
        if (weight == null) {
            targets.put(to, new IntContainer(w));
        } else if (weight.value > w) {
            weight.value = w;
        }
    }

    public void addMutualEdges(Object from, Object to, int weight) {
        addEdge(from, to, weight);
        addEdge(to, from, -weight);
    }

    public void removeEdge(Object from, Object to) {
        Hashtable targets = this.sources.get(from);
        if (targets == null) {
            return;
        }
        targets.remove(to);
        if (targets.size() == 0) {
            this.sources.remove(from);
        }
    }

    public boolean hasEdge(Object from, Object to) {
        Hashtable targets = this.sources.get(from);
        if (targets == null) {
            return false;
        }
        return targets.containsKey(to);
    }

    public int edgeWeight(Object from, Object to) {
        Hashtable targets = this.sources.get(from);
        if (targets == null) {
            throw new RuntimeException("No such edge (" + from + " ," + to + ") exists.");
        }
        IntContainer weight = targets.get(to);
        if (weight == null) {
            throw new RuntimeException("No such edge (" + from + ", " + to + ") exists.");
        }
        return weight.value;
    }

    public void unionSelf(WeightedDirectedSparseGraph other) {
        if (other == null || other.isUnknown) {
            return;
        }
        if (this.isUnknown) {
            addAll(other);
        }
        List<Object> sourceList = new ArrayList<>(this.sources.keySet());
        for (Object srcKey : sourceList) {
            Hashtable src1 = this.sources.get(srcKey);
            Hashtable src2 = other.sources.get(srcKey);
            if (src2 == null) {
                this.sources.remove(srcKey);
            } else {
                List targetList = new ArrayList(src1.keySet());
                for (Object target : targetList) {
                    IntContainer w1 = src1.get(target);
                    IntContainer w2 = src2.get(target);
                    if (w2 == null) {
                        src1.remove(target);
                    } else if (w2.value > w1.value) {
                        w1.value = w2.value;
                    }
                }
                if (src1.size() == 0) {
                    this.sources.remove(srcKey);
                }
            }
        }
    }

    public void widenEdges(WeightedDirectedSparseGraph othergraph) {
        if (othergraph.isUnknown) {
            return;
        }
        Hashtable<Object, Hashtable<Object, IntContainer>> othersources = othergraph.sources;
        List<Object> sourceList = new ArrayList<>(this.sources.keySet());
        for (Object src : sourceList) {
            Hashtable thistargets = this.sources.get(src);
            Hashtable othertargets = othersources.get(src);
            if (othertargets == null) {
                this.sources.remove(src);
            } else {
                List targetList = new ArrayList(thistargets.keySet());
                for (Object target : targetList) {
                    IntContainer thisweight = thistargets.get(target);
                    IntContainer otherweight = othertargets.get(target);
                    if (otherweight == null) {
                        thistargets.remove(target);
                    } else if (thisweight.value > otherweight.value) {
                        thistargets.remove(target);
                    }
                }
                if (thistargets.size() == 0) {
                    this.sources.remove(src);
                }
            }
        }
    }

    public void killNode(Object tokill) {
        if (!this.vertexes.contains(tokill)) {
            return;
        }
        makeShortestPathGraph();
        List<Object> sourceList = new ArrayList<>(this.sources.keySet());
        for (Object src : sourceList) {
            Hashtable targets = this.sources.get(src);
            targets.remove(tokill);
            if (targets.size() == 0) {
                this.sources.remove(src);
            }
        }
        this.sources.remove(tokill);
        makeShortestPathGraph();
    }

    public void updateWeight(Object which, int c) {
        for (Object from : this.sources.keySet()) {
            Hashtable targets = this.sources.get(from);
            IntContainer weight = targets.get(which);
            if (weight != null) {
                weight.value += c;
            }
        }
        Hashtable toset = this.sources.get(which);
        if (toset == null) {
            return;
        }
        for (Object to : toset.keySet()) {
            toset.get(to).value -= c;
        }
    }

    public void clear() {
        this.sources.clear();
    }

    public void replaceAllEdges(WeightedDirectedSparseGraph other) {
        this.isUnknown = other.isUnknown;
        this.vertexes = other.vertexes;
        this.sources = other.sources;
    }

    public void addBoundedAll(WeightedDirectedSparseGraph another) {
        this.isUnknown = another.isUnknown;
        Hashtable<Object, Hashtable<Object, IntContainer>> othersources = another.sources;
        Iterator thisnodeIt = this.vertexes.iterator();
        while (thisnodeIt.hasNext()) {
            Object src = thisnodeIt.next();
            Hashtable othertargets = othersources.get(src);
            if (othertargets != null) {
                Hashtable<Object, IntContainer> thistargets = new Hashtable<>();
                for (Object key : othertargets.keySet()) {
                    if (this.vertexes.contains(key)) {
                        IntContainer weight = othertargets.get(key);
                        thistargets.put(key, weight.dup());
                    }
                }
                if (thistargets.size() > 0) {
                    this.sources.put(src, thistargets);
                }
            }
        }
    }

    public void addAll(WeightedDirectedSparseGraph othergraph) {
        this.isUnknown = othergraph.isUnknown;
        clear();
        Hashtable<Object, Hashtable<Object, IntContainer>> othersources = othergraph.sources;
        for (Object src : othersources.keySet()) {
            Hashtable othertargets = othersources.get(src);
            Hashtable<Object, IntContainer> thistargets = new Hashtable<>(othersources.size());
            this.sources.put(src, thistargets);
            for (Object target : othertargets.keySet()) {
                IntContainer otherweight = othertargets.get(target);
                thistargets.put(target, otherweight.dup());
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x0093  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean equals(java.lang.Object r4) {
        /*
            r3 = this;
            r0 = r4
            if (r0 == 0) goto Lb
            r0 = r4
            boolean r0 = r0 instanceof soot.jimple.toolkits.annotation.arraycheck.WeightedDirectedSparseGraph
            if (r0 != 0) goto Ld
        Lb:
            r0 = 0
            return r0
        Ld:
            r0 = r4
            soot.jimple.toolkits.annotation.arraycheck.WeightedDirectedSparseGraph r0 = (soot.jimple.toolkits.annotation.arraycheck.WeightedDirectedSparseGraph) r0
            r5 = r0
            r0 = r3
            boolean r0 = r0.isUnknown
            r1 = r5
            boolean r1 = r1.isUnknown
            if (r0 == r1) goto L1f
            r0 = 0
            return r0
        L1f:
            r0 = r3
            boolean r0 = r0.isUnknown
            if (r0 == 0) goto L28
            r0 = 1
            return r0
        L28:
            r0 = r5
            java.util.Hashtable<java.lang.Object, java.util.Hashtable<java.lang.Object, soot.jimple.toolkits.annotation.arraycheck.IntContainer>> r0 = r0.sources
            r6 = r0
            r0 = r3
            java.util.Hashtable<java.lang.Object, java.util.Hashtable<java.lang.Object, soot.jimple.toolkits.annotation.arraycheck.IntContainer>> r0 = r0.sources
            int r0 = r0.size()
            r1 = r6
            int r1 = r1.size()
            if (r0 == r1) goto L3d
            r0 = 0
            return r0
        L3d:
            r0 = r3
            java.util.Hashtable<java.lang.Object, java.util.Hashtable<java.lang.Object, soot.jimple.toolkits.annotation.arraycheck.IntContainer>> r0 = r0.sources
            java.util.Set r0 = r0.keySet()
            java.util.Iterator r0 = r0.iterator()
            r7 = r0
            goto Ld4
        L4e:
            r0 = r7
            java.lang.Object r0 = r0.next()
            r8 = r0
            r0 = r3
            java.util.Hashtable<java.lang.Object, java.util.Hashtable<java.lang.Object, soot.jimple.toolkits.annotation.arraycheck.IntContainer>> r0 = r0.sources
            r1 = r8
            java.lang.Object r0 = r0.get(r1)
            java.util.Hashtable r0 = (java.util.Hashtable) r0
            r9 = r0
            r0 = r6
            r1 = r8
            java.lang.Object r0 = r0.get(r1)
            java.util.Hashtable r0 = (java.util.Hashtable) r0
            r10 = r0
            r0 = r10
            if (r0 == 0) goto L82
            r0 = r9
            int r0 = r0.size()
            r1 = r10
            int r1 = r1.size()
            if (r0 == r1) goto L84
        L82:
            r0 = 0
            return r0
        L84:
            r0 = r9
            java.util.Set r0 = r0.keySet()
            java.util.Iterator r0 = r0.iterator()
            r11 = r0
            goto Lca
        L93:
            r0 = r11
            java.lang.Object r0 = r0.next()
            r12 = r0
            r0 = r9
            r1 = r12
            java.lang.Object r0 = r0.get(r1)
            soot.jimple.toolkits.annotation.arraycheck.IntContainer r0 = (soot.jimple.toolkits.annotation.arraycheck.IntContainer) r0
            r13 = r0
            r0 = r10
            r1 = r12
            java.lang.Object r0 = r0.get(r1)
            soot.jimple.toolkits.annotation.arraycheck.IntContainer r0 = (soot.jimple.toolkits.annotation.arraycheck.IntContainer) r0
            r14 = r0
            r0 = r14
            if (r0 != 0) goto Lbb
            r0 = 0
            return r0
        Lbb:
            r0 = r13
            int r0 = r0.value
            r1 = r14
            int r1 = r1.value
            if (r0 == r1) goto Lca
            r0 = 0
            return r0
        Lca:
            r0 = r11
            boolean r0 = r0.hasNext()
            if (r0 != 0) goto L93
        Ld4:
            r0 = r7
            boolean r0 = r0.hasNext()
            if (r0 != 0) goto L4e
            r0 = 1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.jimple.toolkits.annotation.arraycheck.WeightedDirectedSparseGraph.equals(java.lang.Object):boolean");
    }

    public String toString() {
        String graphstring = String.valueOf("WeightedDirectedSparseGraph:\n") + this.vertexes + "\n";
        for (Object src : this.sources.keySet()) {
            String graphstring2 = String.valueOf(graphstring) + src + " : ";
            Hashtable targets = this.sources.get(src);
            for (Object target : targets.keySet()) {
                IntContainer weight = targets.get(target);
                graphstring2 = String.valueOf(graphstring2) + target + "(" + weight.value + ")  ";
            }
            graphstring = String.valueOf(graphstring2) + "\n";
        }
        return graphstring;
    }

    public WeightedDirectedSparseGraph dup() {
        WeightedDirectedSparseGraph newone = new WeightedDirectedSparseGraph(this.vertexes);
        newone.addAll(this);
        return newone;
    }

    public boolean makeShortestPathGraph() {
        boolean nonegcycle = true;
        List<Object> srcList = new ArrayList<>(this.sources.keySet());
        for (Object src : srcList) {
            if (!SSSPFinder(src)) {
                nonegcycle = false;
            }
        }
        return nonegcycle;
    }

    private boolean SSSPFinder(Object src) {
        IntContainer dto;
        Hashtable<Object, IntContainer> outedges = this.sources.get(src);
        if (outedges == null || outedges.size() == 0) {
            return true;
        }
        InitializeSingleSource(src);
        getReachableNodesAndEdges(src);
        int vSize = this.reachableNodes.size();
        for (int i = 0; i < vSize; i++) {
            Iterator<WeightedDirectedEdge> edgeIt = this.reachableEdges.iterator();
            while (edgeIt.hasNext()) {
                WeightedDirectedEdge edge = edgeIt.next();
                Relax(edge.from, edge.to, edge.weight);
            }
        }
        this.distance.remove(src);
        Iterator<WeightedDirectedEdge> edgeIt2 = this.reachableEdges.iterator();
        while (edgeIt2.hasNext()) {
            WeightedDirectedEdge edge2 = edgeIt2.next();
            IntContainer dfrom = this.distance.get(edge2.from);
            if (dfrom != null && (dto = this.distance.get(edge2.to)) != null && dto.value > dfrom.value + edge2.weight) {
                return false;
            }
        }
        outedges.clear();
        for (Object to : this.distance.keySet()) {
            IntContainer dist = this.distance.get(to);
            outedges.put(to, dist.dup());
        }
        return true;
    }

    private void InitializeSingleSource(Object src) {
        this.reachableNodes.clear();
        this.reachableEdges.clear();
        this.pei.clear();
        this.distance.clear();
        this.distance.put(src, new IntContainer(0));
    }

    private void getReachableNodesAndEdges(Object src) {
        LinkedList<Object> worklist = new LinkedList<>();
        this.reachableNodes.add(src);
        worklist.add(src);
        while (!worklist.isEmpty()) {
            Object from = worklist.removeFirst();
            Hashtable targets = this.sources.get(from);
            if (targets != null) {
                for (Object target : targets.keySet()) {
                    if (!this.reachableNodes.contains(target)) {
                        worklist.add(target);
                        this.reachableNodes.add(target);
                    }
                    IntContainer weight = targets.get(target);
                    this.reachableEdges.add(new WeightedDirectedEdge(from, target, weight.value));
                }
            }
        }
    }

    private void Relax(Object from, Object to, int weight) {
        IntContainer dfrom = this.distance.get(from);
        IntContainer dto = this.distance.get(to);
        if (dfrom != null) {
            int vfrom = dfrom.value;
            int vnew = vfrom + weight;
            if (dto == null) {
                this.distance.put(to, new IntContainer(vnew));
                this.pei.put(to, from);
                return;
            }
            int vto = dto.value;
            if (vto > vnew) {
                this.distance.put(to, new IntContainer(vnew));
                this.pei.put(to, from);
            }
        }
    }
}
