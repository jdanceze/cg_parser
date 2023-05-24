package soot.jimple.toolkits.thread.synchronization;

import soot.SootClass;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/synchronization/DeadlockAvoidanceEdge.class */
public class DeadlockAvoidanceEdge extends NewStaticLock {
    public DeadlockAvoidanceEdge(SootClass sc) {
        super(sc);
    }

    @Override // soot.jimple.toolkits.thread.synchronization.NewStaticLock, soot.Value
    public Object clone() {
        return new DeadlockAvoidanceEdge(this.sc);
    }

    @Override // soot.jimple.toolkits.thread.synchronization.NewStaticLock
    public boolean equals(Object c) {
        return (c instanceof DeadlockAvoidanceEdge) && ((DeadlockAvoidanceEdge) c).idnum == this.idnum;
    }

    @Override // soot.jimple.toolkits.thread.synchronization.NewStaticLock
    public String toString() {
        return "dae<" + this.sc.toString() + ">";
    }
}
