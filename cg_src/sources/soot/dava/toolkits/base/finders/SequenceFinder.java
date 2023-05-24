package soot.dava.toolkits.base.finders;

import java.util.HashSet;
import java.util.Iterator;
import soot.G;
import soot.Singletons;
import soot.dava.Dava;
import soot.dava.DavaBody;
import soot.dava.RetriggerAnalysisException;
import soot.dava.internal.SET.SETNode;
import soot.dava.internal.SET.SETStatementSequenceNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.asg.AugmentedStmtGraph;
import soot.util.IterableSet;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/finders/SequenceFinder.class */
public class SequenceFinder implements FactFinder {
    public SequenceFinder(Singletons.Global g) {
    }

    public static SequenceFinder v() {
        return G.v().soot_dava_toolkits_base_finders_SequenceFinder();
    }

    @Override // soot.dava.toolkits.base.finders.FactFinder
    public void find(DavaBody body, AugmentedStmtGraph asg, SETNode SET) throws RetriggerAnalysisException {
        Dava.v().log("SequenceFinder::find()");
        SET.find_StatementSequences(this, body);
    }

    public void find_StatementSequences(SETNode SETParent, IterableSet body, HashSet<AugmentedStmt> childUnion, DavaBody davaBody) {
        Iterator bit = body.iterator();
        while (bit.hasNext()) {
            AugmentedStmt as = (AugmentedStmt) bit.next();
            if (!childUnion.contains(as)) {
                IterableSet sequenceBody = new IterableSet();
                while (as.bpreds.size() == 1) {
                    AugmentedStmt pas = as.bpreds.get(0);
                    if (!body.contains(pas) || childUnion.contains(pas)) {
                        break;
                    }
                    as = pas;
                }
                while (body.contains(as) && !childUnion.contains(as)) {
                    childUnion.add(as);
                    sequenceBody.addLast(as);
                    if (!as.bsuccs.isEmpty()) {
                        as = as.bsuccs.get(0);
                    }
                    if (as.bpreds.size() != 1) {
                        break;
                    }
                }
                SETParent.add_Child(new SETStatementSequenceNode(sequenceBody, davaBody), SETParent.get_Body2ChildChain().get(body));
            }
        }
    }
}
