package soot.jimple.infoflow.util;

import com.google.common.html.HtmlEscapers;
import java.util.ArrayDeque;
import java.util.IdentityHashMap;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.jimple.infoflow.data.Abstraction;
import soot.util.IdentityHashSet;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/util/DebugAbstractionTree.class */
public final class DebugAbstractionTree {
    private static final Logger logger = LoggerFactory.getLogger(DebugAbstractionTree.class);

    private DebugAbstractionTree() {
    }

    public static String createDotGraph(Abstraction absStart, boolean printNeighbors) {
        IdentityHashMap<Abstraction, Integer> absToId = new IdentityHashMap<>();
        ArrayDeque<Abstraction> workQueue = new ArrayDeque<>();
        StringBuilder sbNodes = new StringBuilder();
        StringBuilder sbEdges = new StringBuilder();
        int idCounter = 0;
        workQueue.add(absStart);
        while (!workQueue.isEmpty()) {
            Abstraction p = workQueue.poll();
            if (absToId.get(p) == null) {
                idCounter++;
                absToId.put(p, Integer.valueOf(idCounter));
                String absName = String.valueOf(idCounter);
                logger.info(String.valueOf(idCounter) + ": " + p);
                StringBuilder neighbors = new StringBuilder();
                for (int i = 0; i < p.getNeighborCount(); i++) {
                    neighbors.append(String.format("|<n%d> n%d", Integer.valueOf(i), Integer.valueOf(i)));
                }
                if (p.getNeighborCount() > 0 && printNeighbors) {
                    workQueue.addAll(p.getNeighbors());
                }
                if (p.getPredecessor() != null) {
                    workQueue.add(p.getPredecessor());
                } else if (p.getSourceContext() != null) {
                    absName = String.valueOf(absName) + " [source]";
                }
                sbNodes.append(String.format("    abs%d[label=\"{%s|{<a> A%s}}\"];\n", Integer.valueOf(idCounter), absName, neighbors.toString()));
            }
        }
        workQueue.add(absStart);
        Set<Abstraction> seen = new IdentityHashSet<>();
        while (!workQueue.isEmpty()) {
            Abstraction p2 = workQueue.poll();
            if (seen.add(p2)) {
                Integer id = absToId.get(p2);
                Abstraction pred = p2.getPredecessor();
                if (pred != null) {
                    int dest = absToId.get(pred).intValue();
                    sbEdges.append(String.format("    abs%s:a -> abs%d;\n", id, Integer.valueOf(dest)));
                    workQueue.add(pred);
                }
                if (p2.getNeighborCount() > 0 && printNeighbors) {
                    int i2 = 0;
                    for (Abstraction n : p2.getNeighbors()) {
                        int dest2 = absToId.get(n).intValue();
                        sbEdges.append(String.format("    abs%s:n%s -> abs%d;\n", id, Integer.valueOf(i2), Integer.valueOf(dest2)));
                        i2++;
                    }
                    workQueue.addAll(p2.getNeighbors());
                }
            }
        }
        return "digraph Debug {\n    node [shape=record];\n" + sbNodes.toString() + sbEdges.toString() + "}";
    }

    private static String escape(String string) {
        return HtmlEscapers.htmlEscaper().escape(string);
    }
}
