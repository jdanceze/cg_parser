package soot.jimple.toolkits.thread.mhp;

import heros.solver.Pair;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import soot.toolkits.graph.DirectedGraph;
import soot.util.FastStack;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/mhp/SCC.class */
public class SCC<T> {
    private Set<T> gray;
    private final LinkedList<T> finishedOrder = new LinkedList<>();
    private final List<List<T>> sccList = new ArrayList();

    public SCC(Iterator<T> it, DirectedGraph<T> g) {
        this.gray = new HashSet();
        while (it.hasNext()) {
            T s = it.next();
            if (!this.gray.contains(s)) {
                visitNode(g, s);
            }
        }
        this.gray = new HashSet();
        Iterator<T> revNodeIt = this.finishedOrder.iterator();
        while (revNodeIt.hasNext()) {
            T s2 = revNodeIt.next();
            if (!this.gray.contains(s2)) {
                List<T> scc = new ArrayList<>();
                visitRevNode(g, s2, scc);
                this.sccList.add(scc);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void visitNode(DirectedGraph<T> g, T s) {
        this.gray.add(s);
        FastStack<Pair<T, Iterator<T>>> stack = new FastStack<>();
        stack.push(new Pair<>(s, g.getSuccsOf(s).iterator()));
        while (!stack.isEmpty()) {
            Pair<T, Iterator<T>> p = stack.peek();
            Iterator<T> it = p.getO2();
            while (true) {
                if (it.hasNext()) {
                    T succ = it.next();
                    if (!this.gray.contains(succ)) {
                        this.gray.add(succ);
                        stack.push(new Pair<>(succ, g.getSuccsOf(succ).iterator()));
                        break;
                    }
                } else {
                    stack.pop();
                    this.finishedOrder.addFirst(p.getO1());
                    break;
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void visitRevNode(DirectedGraph<T> g, T s, List<T> scc) {
        scc.add(s);
        this.gray.add(s);
        FastStack<Iterator<T>> stack = new FastStack<>();
        stack.push(g.getPredsOf(s).iterator());
        while (!stack.isEmpty()) {
            Iterator<T> predsIt = stack.peek();
            while (true) {
                if (predsIt.hasNext()) {
                    T pred = predsIt.next();
                    if (!this.gray.contains(pred)) {
                        scc.add(pred);
                        this.gray.add(pred);
                        stack.push(g.getPredsOf(pred).iterator());
                        break;
                    }
                } else {
                    stack.pop();
                    break;
                }
            }
        }
    }

    public List<List<T>> getSccList() {
        return this.sccList;
    }

    public LinkedList<T> getFinishedOrder() {
        return this.finishedOrder;
    }
}
