package soot.jimple.toolkits.thread.synchronization;

import java.util.HashMap;
import java.util.Map;
import soot.EquivalentValue;
import soot.jimple.Ref;
/* compiled from: LockableReferenceAnalysis.java */
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/synchronization/LocksetFlowInfo.class */
class LocksetFlowInfo {
    public Map<EquivalentValue, Integer> groups = new HashMap();
    public Map<Ref, Integer> refToBaseGroup = new HashMap();
    public Map<Ref, Integer> refToIndexGroup = new HashMap();

    public Object clone() {
        LocksetFlowInfo ret = new LocksetFlowInfo();
        ret.groups.putAll(this.groups);
        ret.refToBaseGroup.putAll(this.refToBaseGroup);
        ret.refToIndexGroup.putAll(this.refToIndexGroup);
        return ret;
    }

    public int hashCode() {
        return this.groups.hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof LocksetFlowInfo) {
            LocksetFlowInfo other = (LocksetFlowInfo) o;
            return this.groups.equals(other.groups);
        }
        return false;
    }
}
