package soot.toolkits.graph.pdg;

import soot.Unit;
/* compiled from: EnhancedUnitGraph.java */
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/pdg/GuardedBlock.class */
class GuardedBlock {
    Unit start;
    Unit end;

    public GuardedBlock(Unit s, Unit e) {
        this.start = s;
        this.end = e;
    }

    public int hashCode() {
        int result = (37 * 17) + this.start.hashCode();
        return (37 * result) + this.end.hashCode();
    }

    public boolean equals(Object rhs) {
        if (rhs == this) {
            return true;
        }
        if (!(rhs instanceof GuardedBlock)) {
            return false;
        }
        GuardedBlock rhsGB = (GuardedBlock) rhs;
        return this.start == rhsGB.start && this.end == rhsGB.end;
    }
}
