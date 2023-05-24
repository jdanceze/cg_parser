package soot.jimple.toolkits.thread.synchronization;

import soot.jimple.toolkits.pointer.RWSet;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/synchronization/CriticalSectionDataDependency.class */
class CriticalSectionDataDependency {
    public CriticalSection other;
    public int size;
    public RWSet rw;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CriticalSectionDataDependency(CriticalSection other, int size, RWSet rw) {
        this.other = other;
        this.size = size;
        this.rw = rw;
    }
}
