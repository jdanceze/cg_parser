package soot.toolkits.graph.pdg;

import soot.jimple.internal.JNopStmt;
/* compiled from: EnhancedUnitGraph.java */
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/pdg/EntryStmt.class */
class EntryStmt extends JNopStmt {
    @Override // soot.jimple.internal.JNopStmt, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new EntryStmt();
    }

    @Override // soot.jimple.internal.JNopStmt, soot.Unit
    public boolean fallsThrough() {
        return true;
    }

    @Override // soot.jimple.internal.JNopStmt, soot.Unit
    public boolean branches() {
        return false;
    }
}
