package soot.jimple.toolkits.annotation.purity;

import android.provider.CalendarContract;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Local;
import soot.RefLikeType;
import soot.SootMethod;
import soot.Type;
import soot.Value;
import soot.dava.internal.AST.ASTNode;
import soot.jimple.Jimple;
import soot.jimple.Stmt;
import soot.util.HashMultiMap;
import soot.util.MultiMap;
import soot.util.dot.DotGraph;
import soot.util.dot.DotGraphConstants;
import soot.util.dot.DotGraphEdge;
import soot.util.dot.DotGraphNode;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/purity/PurityGraph.class */
public class PurityGraph {
    public static final boolean doCheck = false;
    static final int PARAM_RW = 0;
    static final int PARAM_RO = 1;
    static final int PARAM_SAFE = 2;
    protected Set<PurityNode> nodes;
    protected Set<PurityNode> paramNodes;
    protected MultiMap<PurityNode, PurityEdge> edges;
    protected MultiMap<Local, PurityNode> locals;
    protected Set<PurityNode> ret;
    protected Set<PurityNode> globEscape;
    protected MultiMap<PurityNode, PurityEdge> backEdges;
    protected MultiMap<PurityNode, Local> backLocals;
    protected MultiMap<PurityNode, String> mutated;
    private static final Logger logger = LoggerFactory.getLogger(PurityGraph.class);
    private static final Map<PurityNode, PurityNode> nodeCache = new HashMap();
    private static final Map<PurityEdge, PurityEdge> edgeCache = new HashMap();
    private static int maxInsideNodes = 0;
    private static int maxLoadNodes = 0;
    private static int maxInsideEdges = 0;
    private static int maxOutsideEdges = 0;
    private static int maxMutated = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PurityGraph() {
        this.nodes = new HashSet();
        this.paramNodes = new HashSet();
        this.edges = new HashMultiMap();
        this.locals = new HashMultiMap();
        this.ret = new HashSet();
        this.globEscape = new HashSet();
        this.backEdges = new HashMultiMap();
        this.backLocals = new HashMultiMap();
        this.mutated = new HashMultiMap();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PurityGraph(PurityGraph x) {
        this.nodes = new HashSet(x.nodes);
        this.paramNodes = new HashSet(x.paramNodes);
        this.edges = new HashMultiMap(x.edges);
        this.locals = new HashMultiMap(x.locals);
        this.ret = new HashSet(x.ret);
        this.globEscape = new HashSet(x.globEscape);
        this.backEdges = new HashMultiMap(x.backEdges);
        this.backLocals = new HashMultiMap(x.backLocals);
        this.mutated = new HashMultiMap(x.mutated);
    }

    public int hashCode() {
        return this.nodes.hashCode() + this.edges.hashCode() + this.locals.hashCode() + this.ret.hashCode() + this.globEscape.hashCode() + this.mutated.hashCode();
    }

    public boolean equals(Object o) {
        if (!(o instanceof PurityGraph)) {
            return false;
        }
        PurityGraph g = (PurityGraph) o;
        return this.nodes.equals(g.nodes) && this.edges.equals(g.edges) && this.locals.equals(g.locals) && this.ret.equals(g.ret) && this.globEscape.equals(g.globEscape) && this.mutated.equals(g.mutated);
    }

    private static PurityNode cacheNode(PurityNode p) {
        if (!nodeCache.containsKey(p)) {
            nodeCache.put(p, p);
        }
        return nodeCache.get(p);
    }

    private static PurityEdge cacheEdge(PurityEdge e) {
        if (!edgeCache.containsKey(e)) {
            edgeCache.put(e, e);
        }
        return edgeCache.get(e);
    }

    public static PurityGraph conservativeGraph(SootMethod m, boolean withEffect) {
        PurityGraph g = new PurityGraph();
        PurityNode glob = PurityGlobalNode.node;
        g.nodes.add(glob);
        int i = 0;
        for (Type next : m.getParameterTypes()) {
            if (next instanceof RefLikeType) {
                PurityNode n = cacheNode(new PurityParamNode(i));
                g.globEscape.add(n);
                g.nodes.add(n);
                g.paramNodes.add(n);
            }
            i++;
        }
        if (m.getReturnType() instanceof RefLikeType) {
            g.ret.add(glob);
        }
        if (withEffect) {
            g.mutated.put(glob, "outside-world");
        }
        return g;
    }

    public static PurityGraph freshGraph(SootMethod m) {
        PurityGraph g = new PurityGraph();
        if (m.getReturnType() instanceof RefLikeType) {
            PurityNode n = cacheNode(new PurityMethodNode(m));
            g.ret.add(n);
            g.nodes.add(n);
        }
        return g;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void union(PurityGraph arg) {
        this.nodes.addAll(arg.nodes);
        this.paramNodes.addAll(arg.paramNodes);
        this.edges.putAll(arg.edges);
        this.locals.putAll(arg.locals);
        this.ret.addAll(arg.ret);
        this.globEscape.addAll(arg.globEscape);
        this.backEdges.putAll(arg.backEdges);
        this.backLocals.putAll(arg.backLocals);
        this.mutated.putAll(arg.mutated);
    }

    protected void sanityCheck() {
        boolean err = false;
        for (PurityNode src : this.edges.keySet()) {
            for (PurityEdge e : this.edges.get(src)) {
                if (!src.equals(e.getSource())) {
                    logger.debug("invalid edge source " + e + ", should be " + src);
                    err = true;
                }
                if (!this.nodes.contains(e.getSource())) {
                    logger.debug("nodes does not contain edge source " + e);
                    err = true;
                }
                if (!this.nodes.contains(e.getTarget())) {
                    logger.debug("nodes does not contain edge target " + e);
                    err = true;
                }
                if (!this.backEdges.get(e.getTarget()).contains(e)) {
                    logger.debug("backEdges does not contain edge " + e);
                    err = true;
                }
                if (!e.isInside() && !e.getTarget().isLoad()) {
                    logger.debug("target of outside edge is not a load node " + e);
                    err = true;
                }
            }
        }
        for (PurityNode dst : this.backEdges.keySet()) {
            for (PurityEdge e2 : this.backEdges.get(dst)) {
                if (!dst.equals(e2.getTarget())) {
                    logger.debug("invalid backEdge dest " + e2 + ", should be " + dst);
                    err = true;
                }
                if (!this.edges.get(e2.getSource()).contains(e2)) {
                    logger.debug("backEdge not in edges " + e2);
                    err = true;
                }
            }
        }
        for (PurityNode n : this.nodes) {
            if (n.isParam() && !this.paramNodes.contains(n)) {
                logger.debug("paramNode not in paramNodes " + n);
                err = true;
            }
        }
        for (PurityNode n2 : this.paramNodes) {
            if (!n2.isParam()) {
                logger.debug("paramNode contains a non-param node " + n2);
                err = true;
            }
            if (!this.nodes.contains(n2)) {
                logger.debug("paramNode not in nodes " + n2);
                err = true;
            }
        }
        for (PurityNode n3 : this.globEscape) {
            if (!this.nodes.contains(n3)) {
                logger.debug("globEscape not in nodes " + n3);
                err = true;
            }
        }
        for (Local l : this.locals.keySet()) {
            for (PurityNode n4 : this.locals.get(l)) {
                if (!this.nodes.contains(n4)) {
                    logger.debug("target of local node in nodes " + l + " / " + n4);
                    err = true;
                }
                if (!this.backLocals.get(n4).contains(l)) {
                    logger.debug("backLocals does contain local " + l + " / " + n4);
                    err = true;
                }
            }
        }
        for (PurityNode n5 : this.backLocals.keySet()) {
            for (Local l2 : this.backLocals.get(n5)) {
                if (!this.nodes.contains(n5)) {
                    logger.debug("backLocal node not in in nodes " + l2 + " / " + n5);
                    err = true;
                }
                if (!this.locals.get(l2).contains(n5)) {
                    logger.debug("locals does contain backLocal " + l2 + " / " + n5);
                    err = true;
                }
            }
        }
        for (PurityNode n6 : this.ret) {
            if (!this.nodes.contains(n6)) {
                logger.debug("target of ret not in nodes " + n6);
                err = true;
            }
        }
        for (PurityNode n7 : this.mutated.keySet()) {
            if (!this.nodes.contains(n7)) {
                logger.debug("mutated node not in nodes " + n7);
                err = true;
            }
        }
        if (err) {
            dump();
            DotGraph dot = new DotGraph("sanityCheckFailure");
            fillDotGraph("chk", dot);
            dot.plot("sanityCheckFailure.dot");
            throw new Error("PurityGraph sanity check failed!!!");
        }
    }

    protected void internalPassEdges(Set<PurityEdge> toColor, Set<PurityNode> dest, boolean consider_inside) {
        for (PurityEdge edge : toColor) {
            if (consider_inside || !edge.isInside()) {
                PurityNode node = edge.getTarget();
                if (!dest.contains(node)) {
                    dest.add(node);
                    internalPassEdges(this.edges.get(node), dest, consider_inside);
                }
            }
        }
    }

    protected void internalPassNode(PurityNode node, Set<PurityNode> dest, boolean consider_inside) {
        if (!dest.contains(node)) {
            dest.add(node);
            internalPassEdges(this.edges.get(node), dest, consider_inside);
        }
    }

    protected void internalPassNodes(Set<PurityNode> toColor, Set<PurityNode> dest, boolean consider_inside) {
        for (PurityNode n : toColor) {
            internalPassNode(n, dest, consider_inside);
        }
    }

    protected Set<PurityNode> getEscaping() {
        Set<PurityNode> escaping = new HashSet<>();
        internalPassNodes(this.ret, escaping, true);
        internalPassNodes(this.globEscape, escaping, true);
        internalPassNode(PurityGlobalNode.node, escaping, true);
        internalPassNodes(this.paramNodes, escaping, true);
        return escaping;
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x004e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean isPure() {
        /*
            r5 = this;
            r0 = r5
            soot.util.MultiMap<soot.jimple.toolkits.annotation.purity.PurityNode, java.lang.String> r0 = r0.mutated
            soot.jimple.toolkits.annotation.purity.PurityGlobalNode r1 = soot.jimple.toolkits.annotation.purity.PurityGlobalNode.node
            java.util.Set r0 = r0.get(r1)
            boolean r0 = r0.isEmpty()
            if (r0 != 0) goto L16
            r0 = 0
            return r0
        L16:
            java.util.HashSet r0 = new java.util.HashSet
            r1 = r0
            r1.<init>()
            r6 = r0
            java.util.HashSet r0 = new java.util.HashSet
            r1 = r0
            r1.<init>()
            r7 = r0
            r0 = r5
            r1 = r5
            java.util.Set<soot.jimple.toolkits.annotation.purity.PurityNode> r1 = r1.paramNodes
            r2 = r6
            r3 = 0
            r0.internalPassNodes(r1, r2, r3)
            r0 = r5
            r1 = r5
            java.util.Set<soot.jimple.toolkits.annotation.purity.PurityNode> r1 = r1.globEscape
            r2 = r7
            r3 = 1
            r0.internalPassNodes(r1, r2, r3)
            r0 = r5
            soot.jimple.toolkits.annotation.purity.PurityGlobalNode r1 = soot.jimple.toolkits.annotation.purity.PurityGlobalNode.node
            r2 = r7
            r3 = 1
            r0.internalPassNode(r1, r2, r3)
            r0 = r6
            java.util.Iterator r0 = r0.iterator()
            r9 = r0
            goto L77
        L4e:
            r0 = r9
            java.lang.Object r0 = r0.next()
            soot.jimple.toolkits.annotation.purity.PurityNode r0 = (soot.jimple.toolkits.annotation.purity.PurityNode) r0
            r8 = r0
            r0 = r7
            r1 = r8
            boolean r0 = r0.contains(r1)
            if (r0 != 0) goto L75
            r0 = r5
            soot.util.MultiMap<soot.jimple.toolkits.annotation.purity.PurityNode, java.lang.String> r0 = r0.mutated
            r1 = r8
            java.util.Set r0 = r0.get(r1)
            boolean r0 = r0.isEmpty()
            if (r0 != 0) goto L77
        L75:
            r0 = 0
            return r0
        L77:
            r0 = r9
            boolean r0 = r0.hasNext()
            if (r0 != 0) goto L4e
            r0 = 1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.jimple.toolkits.annotation.purity.PurityGraph.isPure():boolean");
    }

    public boolean isPureConstructor() {
        if (!this.mutated.get(PurityGlobalNode.node).isEmpty()) {
            return false;
        }
        Set<PurityNode> A = new HashSet<>();
        Set<PurityNode> B = new HashSet<>();
        internalPassNodes(this.paramNodes, A, false);
        internalPassNodes(this.globEscape, B, true);
        internalPassNode(PurityGlobalNode.node, B, true);
        PurityNode th = PurityThisNode.node;
        for (PurityNode n : A) {
            if (B.contains(n)) {
                return false;
            }
            if (!n.equals(th) && !this.mutated.get(n).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    protected int internalParamStatus(PurityNode p) {
        if (!this.paramNodes.contains(p)) {
            return 0;
        }
        Set<PurityNode> S1 = new HashSet<>();
        internalPassNode(p, S1, false);
        for (PurityNode n : S1) {
            if (n.isLoad() || n.equals(p)) {
                if (!this.mutated.get(n).isEmpty() || this.globEscape.contains(n)) {
                    return 0;
                }
            }
        }
        Set<PurityNode> S2 = new HashSet<>();
        internalPassNodes(this.ret, S2, true);
        internalPassNodes(this.paramNodes, S2, true);
        for (PurityNode n2 : S2) {
            for (PurityEdge e : this.edges.get(n2)) {
                if (e.isInside() && S1.contains(e.getTarget())) {
                    return 1;
                }
            }
        }
        return 2;
    }

    public int paramStatus(int param) {
        return internalParamStatus(cacheNode(new PurityParamNode(param)));
    }

    public int thisStatus() {
        return internalParamStatus(PurityThisNode.node);
    }

    public Object clone() {
        return new PurityGraph(this);
    }

    protected final boolean localsRemove(Local local) {
        for (PurityNode node : this.locals.get(local)) {
            this.backLocals.remove(node, local);
        }
        return this.locals.remove(local);
    }

    protected final boolean localsPut(Local local, PurityNode node) {
        this.backLocals.put(node, local);
        return this.locals.put(local, node);
    }

    protected final boolean localsPutAll(Local local, Set<PurityNode> nodes) {
        for (PurityNode node : nodes) {
            this.backLocals.put(node, local);
        }
        return this.locals.putAll(local, nodes);
    }

    protected final void removeNode(PurityNode n) {
        for (PurityEdge e : this.edges.get(n)) {
            this.backEdges.remove(e.getTarget(), e);
        }
        for (PurityEdge e2 : this.backEdges.get(n)) {
            this.edges.remove(e2.getSource(), e2);
        }
        for (Local l : this.backLocals.get(n)) {
            this.locals.remove(l, n);
        }
        this.ret.remove(n);
        this.edges.remove(n);
        this.backEdges.remove(n);
        this.backLocals.remove(n);
        this.nodes.remove(n);
        this.paramNodes.remove(n);
        this.globEscape.remove(n);
        this.mutated.remove(n);
    }

    protected final void mergeNodes(PurityNode src, PurityNode dst) {
        Iterator it = new ArrayList(this.edges.get(src)).iterator();
        while (it.hasNext()) {
            PurityEdge e = (PurityEdge) it.next();
            PurityNode n = e.getTarget();
            if (n.equals(src)) {
                n = dst;
            }
            PurityEdge ee = cacheEdge(new PurityEdge(dst, e.getField(), n, e.isInside()));
            this.edges.remove(src, e);
            this.edges.put(dst, ee);
            this.backEdges.remove(n, e);
            this.backEdges.put(n, ee);
        }
        Iterator it2 = new ArrayList(this.backEdges.get(src)).iterator();
        while (it2.hasNext()) {
            PurityEdge e2 = (PurityEdge) it2.next();
            PurityNode n2 = e2.getSource();
            if (n2.equals(src)) {
                n2 = dst;
            }
            PurityEdge ee2 = cacheEdge(new PurityEdge(n2, e2.getField(), dst, e2.isInside()));
            this.edges.remove(n2, e2);
            this.edges.put(n2, ee2);
            this.backEdges.remove(src, e2);
            this.backEdges.put(dst, ee2);
        }
        Iterator it3 = new ArrayList(this.backLocals.get(src)).iterator();
        while (it3.hasNext()) {
            Local l = (Local) it3.next();
            this.locals.remove(l, src);
            this.backLocals.remove(src, l);
            this.locals.put(l, dst);
            this.backLocals.put(dst, l);
        }
        Set<String> m = this.mutated.get(src);
        this.mutated.remove(src);
        this.mutated.putAll(dst, m);
        if (this.ret.contains(src)) {
            this.ret.remove(src);
            this.ret.add(dst);
        }
        if (this.globEscape.contains(src)) {
            this.globEscape.remove(src);
            this.globEscape.add(dst);
        }
        this.nodes.remove(src);
        this.nodes.add(dst);
        this.paramNodes.remove(src);
        if (dst.isParam()) {
            this.paramNodes.add(dst);
        }
    }

    void simplifyLoad() {
        Iterator it = new ArrayList(this.nodes).iterator();
        while (it.hasNext()) {
            PurityNode p = (PurityNode) it.next();
            Map<String, PurityNode> fmap = new HashMap<>();
            Iterator it2 = new ArrayList(this.edges.get(p)).iterator();
            while (it2.hasNext()) {
                PurityEdge e = (PurityEdge) it2.next();
                PurityNode tgt = e.getTarget();
                if (!e.isInside() && !tgt.equals(p)) {
                    String f = e.getField();
                    if (fmap.containsKey(f) && this.nodes.contains(fmap.get(f))) {
                        mergeNodes(tgt, fmap.get(f));
                    } else {
                        fmap.put(f, tgt);
                    }
                }
            }
        }
    }

    void simplifyInside() {
        Set<PurityNode> r = new HashSet<>();
        internalPassNodes(this.paramNodes, r, true);
        internalPassNodes(this.ret, r, true);
        internalPassNodes(this.globEscape, r, true);
        internalPassNode(PurityGlobalNode.node, r, true);
        for (PurityNode n : this.nodes) {
            if (n.isLoad()) {
                internalPassNode(n, r, true);
            }
        }
        Iterator it = new ArrayList(this.nodes).iterator();
        while (it.hasNext()) {
            PurityNode n2 = (PurityNode) it.next();
            if (n2.isInside() && !r.contains(n2)) {
                removeNode(n2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeLocals() {
        this.locals = new HashMultiMap();
        this.backLocals = new HashMultiMap();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void assignParamToLocal(int right, Local left) {
        PurityNode node = cacheNode(new PurityParamNode(right));
        localsRemove(left);
        localsPut(left, node);
        this.nodes.add(node);
        this.paramNodes.add(node);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void assignThisToLocal(Local left) {
        PurityNode node = PurityThisNode.node;
        localsRemove(left);
        localsPut(left, node);
        this.nodes.add(node);
        this.paramNodes.add(node);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void assignLocalToLocal(Local right, Local left) {
        localsRemove(left);
        localsPutAll(left, this.locals.get(right));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void returnLocal(Local right) {
        this.ret.clear();
        this.ret.addAll(this.locals.get(right));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void assignFieldToLocal(Stmt stmt, Local right, String field, Local left) {
        Set<PurityNode> esc = new HashSet<>();
        Set<PurityNode> escaping = getEscaping();
        localsRemove(left);
        for (PurityNode nodeRight : this.locals.get(right)) {
            for (PurityEdge edge : this.edges.get(nodeRight)) {
                if (edge.isInside() && edge.getField().equals(field)) {
                    localsPut(left, edge.getTarget());
                }
            }
            if (escaping.contains(nodeRight)) {
                esc.add(nodeRight);
            }
        }
        if (!esc.isEmpty()) {
            PurityNode loadNode = cacheNode(new PurityStmtNode(stmt, false));
            this.nodes.add(loadNode);
            for (PurityNode node : esc) {
                PurityEdge edge2 = cacheEdge(new PurityEdge(node, field, loadNode, false));
                if (this.edges.put(node, edge2)) {
                    this.backEdges.put(loadNode, edge2);
                }
            }
            localsPut(left, loadNode);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void assignLocalToField(Local right, Local left, String field) {
        for (PurityNode nodeLeft : this.locals.get(left)) {
            for (PurityNode nodeRight : this.locals.get(right)) {
                PurityEdge edge = cacheEdge(new PurityEdge(nodeLeft, field, nodeRight, true));
                if (this.edges.put(nodeLeft, edge)) {
                    this.backEdges.put(nodeRight, edge);
                }
            }
            if (!nodeLeft.isInside()) {
                this.mutated.put(nodeLeft, field);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void assignNewToLocal(Stmt stmt, Local left) {
        PurityNode node = cacheNode(new PurityStmtNode(stmt, true));
        localsRemove(left);
        localsPut(left, node);
        this.nodes.add(node);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void localEscapes(Local l) {
        this.globEscape.addAll(this.locals.get(l));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void localIsUnknown(Local l) {
        PurityNode node = PurityGlobalNode.node;
        localsRemove(l);
        localsPut(l, node);
        this.nodes.add(node);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void assignLocalToStaticField(Local right, String field) {
        PurityNode node = PurityGlobalNode.node;
        localEscapes(right);
        this.mutated.put(node, field);
        this.nodes.add(node);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void mutateField(Local left, String field) {
        for (PurityNode n : this.locals.get(left)) {
            if (!n.isInside()) {
                this.mutated.put(n, field);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void mutateStaticField(String field) {
        PurityNode node = PurityGlobalNode.node;
        this.mutated.put(node, field);
        this.nodes.add(node);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void methodCall(PurityGraph g, Local right, List<Value> args, Local left) {
        MultiMap<PurityNode, PurityNode> mu = new HashMultiMap<>();
        int nb = 0;
        for (Value arg : args) {
            if (arg instanceof Local) {
                Local loc = (Local) arg;
                if (loc.getType() instanceof RefLikeType) {
                    mu.putAll(cacheNode(new PurityParamNode(nb)), this.locals.get(loc));
                }
            }
            nb++;
        }
        if (right != null) {
            mu.putAll(PurityThisNode.node, this.locals.get(right));
        }
        boolean hasChanged = true;
        while (hasChanged) {
            hasChanged = false;
            Iterator it = new ArrayList(mu.keySet()).iterator();
            while (it.hasNext()) {
                PurityNode n1 = (PurityNode) it.next();
                Iterator it2 = new ArrayList(mu.get(n1)).iterator();
                while (it2.hasNext()) {
                    PurityNode n3 = (PurityNode) it2.next();
                    for (PurityEdge e12 : g.edges.get(n1)) {
                        if (!e12.isInside()) {
                            for (PurityEdge e34 : this.edges.get(n3)) {
                                if (e34.isInside() && e12.getField().equals(e34.getField()) && mu.put(e12.getTarget(), e34.getTarget())) {
                                    hasChanged = true;
                                }
                            }
                        }
                    }
                }
            }
            for (PurityNode n12 : g.edges.keySet()) {
                for (PurityNode n32 : g.edges.keySet()) {
                    Set<PurityNode> mu1 = mu.get(n12);
                    Set<PurityNode> mu3 = mu.get(n32);
                    boolean cond = n12.equals(n32) || mu1.contains(n32) || mu3.contains(n12);
                    if (!cond) {
                        for (PurityNode next : mu1) {
                            cond |= mu3.contains(next);
                            if (cond) {
                                break;
                            }
                        }
                    }
                    if (cond && (!n12.equals(n32) || n12.isLoad())) {
                        for (PurityEdge e122 : g.edges.get(n12)) {
                            if (!e122.isInside()) {
                                for (PurityEdge e342 : g.edges.get(n32)) {
                                    if (e342.isInside() && e122.getField().equals(e342.getField())) {
                                        PurityNode n2 = e122.getTarget();
                                        PurityNode n4 = e342.getTarget();
                                        if (!n4.isParam() && mu.put(n2, n4)) {
                                            hasChanged = true;
                                        }
                                        if (mu.putAll(n2, mu.get(n4))) {
                                            hasChanged = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        for (PurityNode n : g.nodes) {
            if (!n.isParam()) {
                mu.put(n, n);
                this.nodes.add(n);
            }
        }
        for (PurityNode n13 : g.edges.keySet()) {
            for (PurityEdge e123 : g.edges.get(n13)) {
                String f = e123.getField();
                PurityNode n22 = e123.getTarget();
                for (PurityNode mu12 : mu.get(n13)) {
                    if (e123.isInside()) {
                        for (PurityNode mu2 : mu.get(n22)) {
                            PurityEdge edge = cacheEdge(new PurityEdge(mu12, f, mu2, true));
                            this.edges.put(mu12, edge);
                            this.backEdges.put(mu2, edge);
                        }
                    } else {
                        PurityEdge edge2 = cacheEdge(new PurityEdge(mu12, f, n22, false));
                        this.edges.put(mu12, edge2);
                        this.backEdges.put(n22, edge2);
                    }
                }
            }
        }
        if (left != null) {
            localsRemove(left);
            for (PurityNode next2 : g.ret) {
                localsPutAll(left, mu.get(next2));
            }
        }
        for (PurityNode next3 : g.globEscape) {
            this.globEscape.addAll(mu.get(next3));
        }
        Set<PurityNode> escaping = getEscaping();
        Iterator it3 = new ArrayList(this.nodes).iterator();
        while (it3.hasNext()) {
            PurityNode n5 = (PurityNode) it3.next();
            if (!escaping.contains(n5)) {
                if (n5.isLoad()) {
                    removeNode(n5);
                } else {
                    Iterator it4 = new ArrayList(this.edges.get(n5)).iterator();
                    while (it4.hasNext()) {
                        PurityEdge e = (PurityEdge) it4.next();
                        if (!e.isInside()) {
                            this.edges.remove(n5, e);
                            this.backEdges.remove(e.getTarget(), e);
                        }
                    }
                }
            }
        }
        for (PurityNode n6 : g.mutated.keySet()) {
            for (PurityNode nn : mu.get(n6)) {
                if (this.nodes.contains(nn) && !nn.isInside()) {
                    for (String next4 : g.mutated.get(n6)) {
                        this.mutated.put(nn, next4);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void fillDotGraph(String prefix, DotGraph out) {
        Map<PurityNode, String> nodeId = new HashMap<>();
        int id = 0;
        for (PurityNode n : this.nodes) {
            String label = "N" + prefix + "_" + id;
            DotGraphNode node = out.drawNode(label);
            node.setLabel(n.toString());
            if (!n.isInside()) {
                node.setStyle(DotGraphConstants.NODE_STYLE_DASHED);
                node.setAttribute(CalendarContract.ColorsColumns.COLOR, "gray50");
            }
            if (this.globEscape.contains(n)) {
                node.setAttribute("fontcolor", "red");
            }
            nodeId.put(n, label);
            id++;
        }
        for (PurityNode src : this.edges.keySet()) {
            for (PurityEdge e : this.edges.get(src)) {
                DotGraphEdge edge = out.drawEdge(nodeId.get(e.getSource()), nodeId.get(e.getTarget()));
                edge.setLabel(e.getField());
                if (!e.isInside()) {
                    edge.setStyle(DotGraphConstants.NODE_STYLE_DASHED);
                    edge.setAttribute(CalendarContract.ColorsColumns.COLOR, "gray50");
                    edge.setAttribute("fontcolor", "gray40");
                }
            }
        }
        for (Local local : this.locals.keySet()) {
            if (!this.locals.get(local).isEmpty()) {
                String label2 = "L" + prefix + "_" + id;
                DotGraphNode node2 = out.drawNode(label2);
                node2.setLabel(local.toString());
                node2.setShape(DotGraphConstants.NODE_SHAPE_PLAINTEXT);
                for (PurityNode dst : this.locals.get(local)) {
                    out.drawEdge(label2, nodeId.get(dst));
                }
                id++;
            }
        }
        if (!this.ret.isEmpty()) {
            DotGraphNode node3 = out.drawNode("ret_" + prefix);
            node3.setLabel(Jimple.RET);
            node3.setShape(DotGraphConstants.NODE_SHAPE_PLAINTEXT);
            for (PurityNode dst2 : this.ret) {
                out.drawEdge("ret_" + prefix, nodeId.get(dst2));
            }
        }
        for (PurityNode n2 : this.mutated.keySet()) {
            for (String next : this.mutated.get(n2)) {
                String label3 = "M" + prefix + "_" + id;
                DotGraphNode node4 = out.drawNode(label3);
                node4.setLabel("");
                node4.setShape(DotGraphConstants.NODE_SHAPE_PLAINTEXT);
                out.drawEdge(nodeId.get(n2), label3).setLabel(next);
                id++;
            }
        }
    }

    private static void dumpSet(String name, Set<PurityNode> s) {
        logger.debug(name);
        for (PurityNode next : s) {
            logger.debug("  " + next);
        }
    }

    private static <A, B> void dumpMultiMap(String name, MultiMap<A, B> s) {
        logger.debug(name);
        for (A key : s.keySet()) {
            logger.debug("  " + key);
            for (B value : s.get(key)) {
                logger.debug(ASTNode.TAB + value);
            }
        }
    }

    void dump() {
        dumpSet("nodes Set:", this.nodes);
        dumpSet("paramNodes Set:", this.paramNodes);
        dumpMultiMap("edges MultiMap:", this.edges);
        dumpMultiMap("locals MultiMap:", this.locals);
        dumpSet("ret Set:", this.ret);
        dumpSet("globEscape Set:", this.globEscape);
        dumpMultiMap("backEdges MultiMap:", this.backEdges);
        dumpMultiMap("backLocals MultiMap:", this.backLocals);
        dumpMultiMap("mutated MultiMap:", this.mutated);
        logger.debug("");
    }

    static void dumpStat() {
        logger.debug("Stat: " + maxInsideNodes + " inNodes, " + maxLoadNodes + " loadNodes, " + maxInsideEdges + " inEdges, " + maxOutsideEdges + " outEdges, " + maxMutated + " mutated.");
    }

    void updateStat() {
        int insideNodes = 0;
        int loadNodes = 0;
        for (PurityNode n : this.nodes) {
            if (n.isInside()) {
                insideNodes++;
            } else if (n.isLoad()) {
                loadNodes++;
            }
        }
        int insideEdges = 0;
        int outsideEdges = 0;
        for (PurityNode next : this.edges.keySet()) {
            for (PurityEdge e : this.edges.get(next)) {
                if (e.isInside()) {
                    insideEdges++;
                } else {
                    outsideEdges++;
                }
            }
        }
        int mutatedFields = 0;
        for (PurityNode next2 : this.mutated.keySet()) {
            mutatedFields += this.mutated.get(next2).size();
        }
        boolean changed = false;
        if (insideNodes > maxInsideNodes) {
            maxInsideNodes = insideNodes;
            changed = true;
        }
        if (loadNodes > maxLoadNodes) {
            maxLoadNodes = loadNodes;
            changed = true;
        }
        if (insideEdges > maxInsideEdges) {
            maxInsideEdges = insideEdges;
            changed = true;
        }
        if (outsideEdges > maxOutsideEdges) {
            maxOutsideEdges = outsideEdges;
            changed = true;
        }
        if (mutatedFields > maxMutated) {
            maxMutated = mutatedFields;
            changed = true;
        }
        if (changed) {
            dumpStat();
        }
    }
}
