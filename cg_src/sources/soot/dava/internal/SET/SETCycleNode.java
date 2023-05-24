package soot.dava.internal.SET;

import soot.dava.internal.asg.AugmentedStmt;
import soot.util.IterableSet;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/SET/SETCycleNode.class */
public abstract class SETCycleNode extends SETControlFlowNode {
    public SETCycleNode(AugmentedStmt characterizingStmt, IterableSet body) {
        super(characterizingStmt, body);
    }
}
