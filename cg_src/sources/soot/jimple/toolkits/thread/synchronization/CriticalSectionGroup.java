package soot.jimple.toolkits.thread.synchronization;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.Value;
import soot.jimple.toolkits.pointer.CodeBlockRWSet;
import soot.jimple.toolkits.pointer.RWSet;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/synchronization/CriticalSectionGroup.class */
class CriticalSectionGroup implements Iterable<CriticalSection> {
    int groupNum;
    List<CriticalSection> criticalSections = new ArrayList();
    RWSet rwSet = new CodeBlockRWSet();
    public boolean isDynamicLock = false;
    public boolean useDynamicLock = false;
    public Value lockObject = null;
    public boolean useLocksets = false;

    public CriticalSectionGroup(int groupNum) {
        this.groupNum = groupNum;
    }

    public int num() {
        return this.groupNum;
    }

    public int size() {
        return this.criticalSections.size();
    }

    public void add(CriticalSection tn) {
        tn.setNumber = this.groupNum;
        tn.group = this;
        if (!this.criticalSections.contains(tn)) {
            this.criticalSections.add(tn);
        }
    }

    public boolean contains(CriticalSection tn) {
        return this.criticalSections.contains(tn);
    }

    @Override // java.lang.Iterable
    public Iterator<CriticalSection> iterator() {
        return this.criticalSections.iterator();
    }

    public void mergeGroups(CriticalSectionGroup other) {
        if (other == this) {
            return;
        }
        for (CriticalSection tn : other.criticalSections) {
            add(tn);
        }
        other.criticalSections.clear();
    }
}
