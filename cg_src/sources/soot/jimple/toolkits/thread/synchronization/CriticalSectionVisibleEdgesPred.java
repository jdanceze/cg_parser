package soot.jimple.toolkits.thread.synchronization;

import java.util.Collection;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.EdgePredicate;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/synchronization/CriticalSectionVisibleEdgesPred.class */
public class CriticalSectionVisibleEdgesPred implements EdgePredicate {
    Collection<CriticalSection> tns;
    CriticalSection exemptTn;

    public CriticalSectionVisibleEdgesPred(Collection<CriticalSection> tns) {
        this.tns = tns;
    }

    public void setExemptTransaction(CriticalSection exemptTn) {
        this.exemptTn = exemptTn;
    }

    @Override // soot.jimple.toolkits.callgraph.EdgePredicate
    public boolean want(Edge e) {
        String tgtMethod = e.tgt().toString();
        String tgtClass = e.tgt().getDeclaringClass().toString();
        e.src().toString();
        String srcClass = e.src().getDeclaringClass().toString();
        if (tgtClass.startsWith("sun.") || tgtClass.startsWith("com.sun.") || tgtMethod.endsWith("void <clinit>()>")) {
            return false;
        }
        if (((tgtClass.startsWith("java.") || tgtClass.startsWith("javax.")) && e.tgt().toString().endsWith("boolean equals(java.lang.Object)>")) || tgtClass.startsWith("java.util") || srcClass.startsWith("java.util") || tgtClass.startsWith("java.lang") || srcClass.startsWith("java.lang") || tgtClass.startsWith("java") || e.tgt().isSynchronized()) {
            return false;
        }
        if (this.tns != null) {
            for (CriticalSection tn : this.tns) {
                if (tn != this.exemptTn && tn.units.contains(e.srcStmt())) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }
}
