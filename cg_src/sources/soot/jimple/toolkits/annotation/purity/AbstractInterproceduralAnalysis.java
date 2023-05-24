package soot.jimple.toolkits.annotation.purity;

import java.io.File;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.SootMethod;
import soot.SourceLocator;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.PseudoTopologicalOrderer;
import soot.util.dot.DotGraph;
import soot.util.dot.DotGraphConstants;
import soot.util.dot.DotGraphEdge;
import soot.util.dot.DotGraphNode;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/purity/AbstractInterproceduralAnalysis.class */
public abstract class AbstractInterproceduralAnalysis<S> {
    private static final Logger logger = LoggerFactory.getLogger(AbstractInterproceduralAnalysis.class);
    public static final boolean doCheck = false;
    protected final CallGraph cg;
    protected final DirectedGraph<SootMethod> dg;
    protected final Map<SootMethod, S> data = new HashMap();
    protected final Map<SootMethod, S> unanalysed = new HashMap();
    protected final Map<SootMethod, Integer> order = new HashMap();

    protected abstract S newInitialSummary();

    protected abstract S summaryOfUnanalysedMethod(SootMethod sootMethod);

    protected abstract void analyseMethod(SootMethod sootMethod, S s);

    protected abstract void applySummary(S s, Stmt stmt, S s2, S s3);

    protected abstract void merge(S s, S s2, S s3);

    protected abstract void copy(S s, S s2);

    public AbstractInterproceduralAnalysis(CallGraph cg, SootMethodFilter filter, Iterator<SootMethod> heads, boolean verbose) {
        this.cg = cg;
        this.dg = new DirectedCallGraph(cg, filter, heads, verbose);
        int i = 0;
        for (SootMethod m : new PseudoTopologicalOrderer().newList(this.dg, true)) {
            this.order.put(m, Integer.valueOf(i));
            i++;
        }
    }

    protected void fillDotGraph(String prefix, S o, DotGraph out) {
        throw new Error("abstract function AbstractInterproceduralAnalysis.fillDotGraph called but not implemented.");
    }

    public void analyseCall(S src, Stmt callStmt, S dst) {
        S s;
        S accum = newInitialSummary();
        copy(accum, dst);
        Iterator<Edge> it = this.cg.edgesOutOf(callStmt);
        while (it.hasNext()) {
            Edge edge = it.next();
            SootMethod m = edge.tgt();
            if (this.data.containsKey(m)) {
                s = this.data.get(m);
            } else {
                if (!this.unanalysed.containsKey(m)) {
                    this.unanalysed.put(m, summaryOfUnanalysedMethod(m));
                }
                s = this.unanalysed.get(m);
            }
            S elem = s;
            applySummary(src, callStmt, elem, accum);
            merge(dst, accum, dst);
        }
    }

    public void drawAsOneDot(String name) {
        DotGraph dot = new DotGraph(name);
        dot.setGraphLabel(name);
        dot.setGraphAttribute("compound", "true");
        int id = 0;
        Map<SootMethod, Integer> idmap = new HashMap<>();
        for (SootMethod m : this.dg) {
            DotGraph sub = dot.createSubGraph("cluster" + id);
            DotGraphNode label = sub.drawNode("head" + id);
            idmap.put(m, Integer.valueOf(id));
            sub.setGraphLabel("");
            label.setLabel("(" + this.order.get(m) + ") " + m.toString());
            label.setAttribute("fontsize", "18");
            label.setShape(DotGraphConstants.NODE_SHAPE_BOX);
            if (this.data.containsKey(m)) {
                fillDotGraph("X" + id, this.data.get(m), sub);
            }
            id++;
        }
        for (SootMethod m2 : this.dg) {
            for (SootMethod mm : this.dg.getSuccsOf(m2)) {
                DotGraphEdge edge = dot.drawEdge("head" + idmap.get(m2), "head" + idmap.get(mm));
                edge.setAttribute("ltail", "cluster" + idmap.get(m2));
                edge.setAttribute("lhead", "cluster" + idmap.get(mm));
            }
        }
        File f = new File(SourceLocator.v().getOutputDir(), String.valueOf(name) + DotGraph.DOT_EXTENSION);
        dot.plot(f.getPath());
    }

    public void drawAsManyDot(String prefix, boolean drawUnanalysed) {
        for (SootMethod m : this.data.keySet()) {
            DotGraph dot = new DotGraph(m.toString());
            dot.setGraphLabel(m.toString());
            fillDotGraph("X", this.data.get(m), dot);
            File f = new File(SourceLocator.v().getOutputDir(), String.valueOf(prefix) + m.toString() + DotGraph.DOT_EXTENSION);
            dot.plot(f.getPath());
        }
        if (drawUnanalysed) {
            for (SootMethod m2 : this.unanalysed.keySet()) {
                DotGraph dot2 = new DotGraph(m2.toString());
                dot2.setGraphLabel(String.valueOf(m2.toString()) + " (unanalysed)");
                fillDotGraph("X", this.unanalysed.get(m2), dot2);
                File f2 = new File(SourceLocator.v().getOutputDir(), String.valueOf(prefix) + m2.toString() + "_u" + DotGraph.DOT_EXTENSION);
                dot2.plot(f2.getPath());
            }
        }
    }

    public S getSummaryFor(SootMethod m) {
        if (this.data.containsKey(m)) {
            return this.data.get(m);
        }
        if (this.unanalysed.containsKey(m)) {
            return this.unanalysed.get(m);
        }
        return newInitialSummary();
    }

    public Iterator<SootMethod> getAnalysedMethods() {
        return this.data.keySet().iterator();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void doAnalysis(boolean verbose) {
        SortedSet<SootMethod> queue = new TreeSet<>(new Comparator<SootMethod>() { // from class: soot.jimple.toolkits.annotation.purity.AbstractInterproceduralAnalysis.1IntComparator
            @Override // java.util.Comparator
            public int compare(SootMethod o1, SootMethod o2) {
                return AbstractInterproceduralAnalysis.this.order.get(o1).intValue() - AbstractInterproceduralAnalysis.this.order.get(o2).intValue();
            }
        });
        for (SootMethod o : this.order.keySet()) {
            this.data.put(o, newInitialSummary());
            queue.add(o);
        }
        Map<SootMethod, Integer> nb = new HashMap<>();
        while (!queue.isEmpty()) {
            SootMethod m = queue.first();
            queue.remove(m);
            S newSummary = newInitialSummary();
            S oldSummary = this.data.get(m);
            if (nb.containsKey(m)) {
                nb.put(m, Integer.valueOf(nb.get(m).intValue() + 1));
            } else {
                nb.put(m, 1);
            }
            if (verbose) {
                logger.debug(" |- processing " + m.toString() + " (" + nb.get(m) + "-st time)");
            }
            analyseMethod(m, newSummary);
            if (!oldSummary.equals(newSummary)) {
                this.data.put(m, newSummary);
                queue.addAll(this.dg.getPredsOf(m));
            }
        }
    }
}
