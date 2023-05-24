package soot.jimple.toolkits.thread.mhp;

import soot.JavaMethods;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.EdgePredicate;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/mhp/RunMethodsPred.class */
public class RunMethodsPred implements EdgePredicate {
    @Override // soot.jimple.toolkits.callgraph.EdgePredicate
    public boolean want(Edge e) {
        String tgtSubSignature = e.tgt().getSubSignature();
        if (tgtSubSignature.equals(JavaMethods.SIG_RUN)) {
            return true;
        }
        return false;
    }
}
