package soot.dava.internal.SET;

import soot.dava.internal.asg.AugmentedStmt;
import soot.util.IterableSet;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/SET/SETDagNode.class */
public abstract class SETDagNode extends SETControlFlowNode {
    public SETDagNode(AugmentedStmt characterizingStmt, IterableSet body) {
        super(characterizingStmt, body);
    }

    @Override // soot.dava.internal.SET.SETNode
    public AugmentedStmt get_EntryStmt() {
        return get_CharacterizingStmt();
    }
}
