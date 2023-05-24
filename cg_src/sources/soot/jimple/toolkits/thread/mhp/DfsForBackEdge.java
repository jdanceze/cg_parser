package soot.jimple.toolkits.thread.mhp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import soot.coffi.Instruction;
import soot.jimple.toolkits.thread.mhp.stmt.JPegStmt;
import soot.tagkit.Tag;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.FlowSet;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/mhp/DfsForBackEdge.class */
public class DfsForBackEdge {
    private final Map<Object, Object> backEdges = new HashMap();
    private final Set<Object> gray = new HashSet();
    private final Set<Object> black = new HashSet();
    private final DominatorsFinder domFinder;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DfsForBackEdge(Chain chain, DirectedGraph peg) {
        this.domFinder = new DominatorsFinder(chain, peg);
        Iterator it = chain.iterator();
        dfs(it, peg);
        testBackEdge();
    }

    private void dfs(Iterator it, DirectedGraph g) {
        while (it.hasNext()) {
            Object s = it.next();
            if (!this.gray.contains(s)) {
                visitNode(g, s);
            }
        }
    }

    private void visitNode(DirectedGraph g, Object s) {
        this.gray.add(s);
        if (g.getSuccsOf(s).size() > 0) {
            for (Object succ : g.getSuccsOf(s)) {
                if (!this.gray.contains(succ)) {
                    visitNode(g, succ);
                } else if (this.gray.contains(succ) && !this.black.contains(succ)) {
                    FlowSet dominators = this.domFinder.getDominatorsOf(s);
                    if (dominators.contains(succ)) {
                        System.out.println("s is " + s);
                        System.out.println("succ is " + succ);
                        this.backEdges.put(s, succ);
                    }
                }
            }
        }
        this.black.add(s);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Map<Object, Object> getBackEdges() {
        return this.backEdges;
    }

    private void testBackEdge() {
        System.out.println("===test backEdges==");
        Set maps = this.backEdges.entrySet();
        for (Map.Entry<Object, Object> entry : maps) {
            JPegStmt key = (JPegStmt) entry.getKey();
            Tag tag = key.getTags().get(0);
            System.out.println("---key=  " + tag + Instruction.argsep + key);
            JPegStmt value = (JPegStmt) entry.getValue();
            Tag tag1 = value.getTags().get(0);
            System.out.println("---value=  " + tag1 + Instruction.argsep + value);
        }
        System.out.println("===test backEdges==end==");
    }
}
