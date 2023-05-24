package soot.toolkits.graph;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;
import soot.Body;
import soot.jimple.Stmt;
import soot.jimple.toolkits.annotation.logic.Loop;
import soot.jimple.toolkits.annotation.logic.LoopFinder;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/LoopNestTree.class */
public class LoopNestTree extends TreeSet<Loop> {

    /* loaded from: gencallgraphv3.jar:soot/toolkits/graph/LoopNestTree$LoopNestTreeComparator.class */
    private static class LoopNestTreeComparator implements Comparator<Loop> {
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !LoopNestTree.class.desiredAssertionStatus();
        }

        private LoopNestTreeComparator() {
        }

        /* synthetic */ LoopNestTreeComparator(LoopNestTreeComparator loopNestTreeComparator) {
            this();
        }

        @Override // java.util.Comparator
        public int compare(Loop loop1, Loop loop2) {
            Collection<?> stmts1 = loop1.getLoopStatements();
            Collection<Stmt> stmts2 = loop2.getLoopStatements();
            if (stmts1.equals(stmts2)) {
                if ($assertionsDisabled || loop1.getHead().equals(loop2.getHead())) {
                    return 0;
                }
                throw new AssertionError();
            } else if (!stmts1.containsAll(stmts2) && stmts2.containsAll(stmts1)) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    public LoopNestTree(Body b) {
        this(computeLoops(b));
    }

    public LoopNestTree(Collection<Loop> loops) {
        super(new LoopNestTreeComparator(null));
        addAll(loops);
    }

    private static Collection<Loop> computeLoops(Body b) {
        return new LoopFinder().getLoops(b);
    }

    public boolean hasNestedLoops() {
        LoopNestTreeComparator comp = new LoopNestTreeComparator(null);
        Iterator<Loop> it = iterator();
        while (it.hasNext()) {
            Loop loop1 = it.next();
            Iterator<Loop> it2 = iterator();
            while (it2.hasNext()) {
                Loop loop2 = it2.next();
                if (comp.compare(loop1, loop2) != 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
