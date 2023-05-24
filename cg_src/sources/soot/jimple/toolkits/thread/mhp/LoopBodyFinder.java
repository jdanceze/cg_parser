package soot.jimple.toolkits.thread.mhp;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import soot.toolkits.graph.DirectedGraph;
import soot.util.FastStack;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/mhp/LoopBodyFinder.class */
public class LoopBodyFinder {
    private final FastStack<Object> stack = new FastStack<>();
    private final Set<Set<Object>> loops = new HashSet();

    /* JADX INFO: Access modifiers changed from: package-private */
    public LoopBodyFinder(Map<Object, Object> backEdges, DirectedGraph g) {
        findLoopBody(backEdges, g);
    }

    private void findLoopBody(Map<Object, Object> backEdges, DirectedGraph g) {
        Set maps = backEdges.entrySet();
        for (Map.Entry<Object, Object> entry : maps) {
            Object tail = entry.getKey();
            Object head = entry.getValue();
            Set<Object> loopBody = finder(tail, head, g);
            this.loops.add(loopBody);
        }
    }

    private Set<Object> finder(Object tail, Object head, DirectedGraph g) {
        Set<Object> loop = new HashSet<>();
        loop.add(head);
        insert(tail, loop);
        while (!this.stack.empty()) {
            Object p = this.stack.pop();
            for (Object pred : g.getPredsOf(p)) {
                insert(pred, loop);
            }
        }
        return loop;
    }

    private void insert(Object m, Set<Object> loop) {
        if (!loop.contains(m)) {
            loop.add(m);
            this.stack.push(m);
        }
    }

    public Set<Set<Object>> getLoopBody() {
        return this.loops;
    }
}
