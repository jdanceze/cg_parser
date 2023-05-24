package soot.jimple.toolkits.thread.mhp.findobject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import soot.Scene;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.thread.mhp.pegcallgraph.PegCallGraph;
import soot.toolkits.graph.CompleteUnitGraph;
import soot.toolkits.graph.UnitGraph;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/mhp/findobject/MultiCalledMethods.class */
public class MultiCalledMethods {
    Set<SootMethod> multiCalledMethods;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MultiCalledMethods(PegCallGraph pcg, Set<SootMethod> mcm) {
        this.multiCalledMethods = new HashSet();
        this.multiCalledMethods = mcm;
        byMCalledS0(pcg);
        finder1(pcg);
        finder2(pcg);
        propagate(pcg);
    }

    private void byMCalledS0(PegCallGraph pcg) {
        Iterator it = pcg.iterator();
        while (it.hasNext()) {
            SootMethod sm = (SootMethod) it.next();
            UnitGraph graph = new CompleteUnitGraph(sm.getActiveBody());
            CallGraph callGraph = Scene.v().getCallGraph();
            MultiRunStatementsFinder finder = new MultiRunStatementsFinder(graph, sm, this.multiCalledMethods, callGraph);
            finder.getMultiRunStatements();
        }
    }

    private void propagate(PegCallGraph pcg) {
        Set<SootMethod> visited = new HashSet<>();
        List<SootMethod> reachable = new ArrayList<>();
        reachable.addAll(this.multiCalledMethods);
        while (reachable.size() >= 1) {
            SootMethod popped = reachable.remove(0);
            if (!visited.contains(popped)) {
                if (!this.multiCalledMethods.contains(popped)) {
                    this.multiCalledMethods.add(popped);
                }
                visited.add(popped);
                for (Object succ : pcg.getSuccsOf(popped)) {
                    reachable.add((SootMethod) succ);
                }
            }
        }
    }

    private void finder1(PegCallGraph pcg) {
        Set clinitMethods = pcg.getClinitMethods();
        Iterator it = pcg.iterator();
        while (it.hasNext()) {
            Object head = it.next();
            Set<Object> gray = new HashSet<>();
            LinkedList<Object> queue = new LinkedList<>();
            queue.add(head);
            while (queue.size() > 0) {
                Object root = queue.getFirst();
                for (Object succ : pcg.getSuccsOf(root)) {
                    if (!gray.contains(succ)) {
                        gray.add(succ);
                        queue.addLast(succ);
                    } else if (!clinitMethods.contains(succ)) {
                        this.multiCalledMethods.add((SootMethod) succ);
                    }
                }
                queue.remove(root);
            }
        }
    }

    private void finder2(PegCallGraph pcg) {
        pcg.trim();
        Set<SootMethod> first = new HashSet<>();
        Set<SootMethod> second = new HashSet<>();
        Iterator it = pcg.iterator();
        while (it.hasNext()) {
            SootMethod s = (SootMethod) it.next();
            if (!second.contains(s)) {
                visitNode(s, pcg, first, second);
            }
        }
    }

    private void visitNode(SootMethod node, PegCallGraph pcg, Set<SootMethod> first, Set<SootMethod> second) {
        if (first.contains(node)) {
            second.add(node);
            if (!this.multiCalledMethods.contains(node)) {
                this.multiCalledMethods.add(node);
            }
        } else {
            first.add(node);
        }
        for (SootMethod succ : pcg.getTrimSuccsOf(node)) {
            if (!second.contains(succ)) {
                visitNode(succ, pcg, first, second);
            }
        }
    }

    public Set<SootMethod> getMultiCalledMethods() {
        return this.multiCalledMethods;
    }
}
