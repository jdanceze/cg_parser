package soot.dava.internal.SET;

import java.util.HashSet;
import java.util.Iterator;
import soot.dava.internal.asg.AugmentedStmt;
import soot.jimple.GotoStmt;
import soot.util.IterableSet;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/SET/SETControlFlowNode.class */
public abstract class SETControlFlowNode extends SETNode {
    private AugmentedStmt characterizingStmt;

    public SETControlFlowNode(AugmentedStmt characterizingStmt, IterableSet<AugmentedStmt> body) {
        super(body);
        this.characterizingStmt = characterizingStmt;
    }

    public AugmentedStmt get_CharacterizingStmt() {
        return this.characterizingStmt;
    }

    @Override // soot.dava.internal.SET.SETNode
    protected boolean resolve(SETNode parent) {
        for (IterableSet subBody : parent.get_SubBodies()) {
            if (subBody.contains(get_EntryStmt())) {
                IterableSet<SETNode> childChain = parent.get_Body2ChildChain().get(subBody);
                HashSet childUnion = new HashSet();
                Iterator<T> it = childChain.iterator();
                while (it.hasNext()) {
                    SETNode child = (SETNode) it.next();
                    IterableSet childBody = child.get_Body();
                    childUnion.addAll(childBody);
                    if (childBody.contains(this.characterizingStmt)) {
                        Iterator<AugmentedStmt> asIt = get_Body().snapshotIterator();
                        while (asIt.hasNext()) {
                            AugmentedStmt as = asIt.next();
                            if (!childBody.contains(as)) {
                                remove_AugmentedStmt(as);
                            } else if ((child instanceof SETControlFlowNode) && !(child instanceof SETUnconditionalWhileNode)) {
                                SETControlFlowNode scfn = (SETControlFlowNode) child;
                                if (scfn.get_CharacterizingStmt() == as || (as.cpreds.size() == 1 && (as.get_Stmt() instanceof GotoStmt) && scfn.get_CharacterizingStmt() == as.cpreds.get(0))) {
                                    remove_AugmentedStmt(as);
                                }
                            }
                        }
                        return true;
                    }
                }
                continue;
            }
        }
        return true;
    }
}
