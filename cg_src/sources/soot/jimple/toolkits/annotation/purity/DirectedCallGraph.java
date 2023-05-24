package soot.jimple.toolkits.annotation.purity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.toolkits.graph.DirectedGraph;
import soot.util.HashMultiMap;
import soot.util.MultiMap;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/purity/DirectedCallGraph.class */
public class DirectedCallGraph implements DirectedGraph<SootMethod> {
    private static final Logger logger = LoggerFactory.getLogger(DirectedCallGraph.class);
    protected Set<SootMethod> nodes;
    protected Map<SootMethod, List<SootMethod>> succ;
    protected Map<SootMethod, List<SootMethod>> pred;
    protected List<SootMethod> heads;
    protected List<SootMethod> tails;
    protected int size;

    public DirectedCallGraph(CallGraph cg, SootMethodFilter filter, Iterator<SootMethod> heads, boolean verbose) {
        List<SootMethod> filteredHeads = new LinkedList<>();
        while (heads.hasNext()) {
            SootMethod m = heads.next();
            if (m.isConcrete() && filter.want(m)) {
                filteredHeads.add(m);
            }
        }
        this.nodes = new HashSet(filteredHeads);
        MultiMap<SootMethod, SootMethod> s = new HashMultiMap<>();
        MultiMap<SootMethod, SootMethod> p = new HashMultiMap<>();
        if (verbose) {
            logger.debug("[AM] dumping method dependencies");
        }
        int nb = 0;
        Set<SootMethod> hashSet = new HashSet<>(filteredHeads);
        while (true) {
            Set<SootMethod> remain = hashSet;
            if (remain.isEmpty()) {
                break;
            }
            HashSet hashSet2 = new HashSet();
            for (SootMethod m2 : remain) {
                if (verbose) {
                    logger.debug(" |- " + m2.toString() + " calls");
                }
                Iterator<Edge> itt = cg.edgesOutOf(m2);
                while (itt.hasNext()) {
                    Edge edge = itt.next();
                    SootMethod mm = edge.tgt();
                    boolean keep = mm.isConcrete() && filter.want(mm);
                    if (verbose) {
                        logger.debug(" |  |- " + mm.toString() + (keep ? "" : " (filtered out)"));
                    }
                    if (keep) {
                        if (this.nodes.add(mm)) {
                            hashSet2.add(mm);
                        }
                        s.put(m2, mm);
                        p.put(mm, m2);
                    }
                }
                nb++;
            }
            hashSet = hashSet2;
        }
        logger.debug("[AM] number of methods to be analysed: " + nb);
        this.succ = new HashMap();
        this.pred = new HashMap();
        this.tails = new LinkedList();
        this.heads = new LinkedList();
        for (SootMethod x : this.nodes) {
            Set<SootMethod> ss = s.get(x);
            Set<SootMethod> pp = p.get(x);
            this.succ.put(x, new LinkedList(ss));
            this.pred.put(x, new LinkedList(pp));
            if (ss.isEmpty()) {
                this.tails.add(x);
            }
            if (pp.isEmpty()) {
                this.heads.add(x);
            }
        }
        this.size = this.nodes.size();
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List<SootMethod> getHeads() {
        return this.heads;
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List<SootMethod> getTails() {
        return this.tails;
    }

    @Override // soot.toolkits.graph.DirectedGraph, java.lang.Iterable
    public Iterator<SootMethod> iterator() {
        return this.nodes.iterator();
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public int size() {
        return this.size;
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List<SootMethod> getSuccsOf(SootMethod s) {
        return this.succ.get(s);
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List<SootMethod> getPredsOf(SootMethod s) {
        return this.pred.get(s);
    }
}
