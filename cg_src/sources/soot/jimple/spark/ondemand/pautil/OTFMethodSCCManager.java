package soot.jimple.spark.ondemand.pautil;

import java.util.Set;
import soot.Scene;
import soot.SootMethod;
import soot.jimple.spark.ondemand.genericutil.DisjointSets;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/pautil/OTFMethodSCCManager.class */
public final class OTFMethodSCCManager {
    private DisjointSets disj;

    public OTFMethodSCCManager() {
        int size = Scene.v().getMethodNumberer().size();
        this.disj = new DisjointSets(size + 1);
    }

    public boolean inSameSCC(SootMethod m1, SootMethod m2) {
        return this.disj.find(m1.getNumber()) == this.disj.find(m2.getNumber());
    }

    public void makeSameSCC(Set<SootMethod> methods) {
        int prevMethodRep;
        int methodRep;
        SootMethod prevMethod = null;
        for (SootMethod method : methods) {
            if (prevMethod != null && (prevMethodRep = this.disj.find(prevMethod.getNumber())) != (methodRep = this.disj.find(method.getNumber()))) {
                this.disj.union(prevMethodRep, methodRep);
            }
            prevMethod = method;
        }
    }
}
