package soot.jimple.toolkits.thread.mhp;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import soot.coffi.Instruction;
import soot.jimple.toolkits.thread.mhp.stmt.JPegStmt;
import soot.tagkit.Tag;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/mhp/LoopFinder.class */
public class LoopFinder {
    private final Map<Chain, Set<Set<Object>>> chainToLoop = new HashMap();

    LoopFinder(PegGraph peg) {
        Chain chain = peg.getMainPegChain();
        DfsForBackEdge dfsForBackEdge = new DfsForBackEdge(chain, peg);
        Map<Object, Object> backEdges = dfsForBackEdge.getBackEdges();
        LoopBodyFinder lbf = new LoopBodyFinder(backEdges, peg);
        Set<Set<Object>> loopBody = lbf.getLoopBody();
        testLoops(loopBody);
        this.chainToLoop.put(chain, loopBody);
    }

    private void testLoops(Set<Set<Object>> loopBody) {
        System.out.println("====loops===");
        for (Set<JPegStmt> loop : loopBody) {
            System.out.println("---loop---");
            for (JPegStmt o : loop) {
                Tag tag = o.getTags().get(0);
                System.out.println(tag + Instruction.argsep + o);
            }
        }
        System.out.println("===end===loops===");
    }
}
