package soot.dava.toolkits.base.finders;

import java.util.Iterator;
import java.util.LinkedList;
import soot.G;
import soot.Singletons;
import soot.dava.Dava;
import soot.dava.DavaBody;
import soot.dava.RetriggerAnalysisException;
import soot.dava.internal.SET.SETIfElseNode;
import soot.dava.internal.SET.SETNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.asg.AugmentedStmtGraph;
import soot.jimple.IfStmt;
import soot.jimple.Stmt;
import soot.util.IterableSet;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/finders/IfFinder.class */
public class IfFinder implements FactFinder {
    public IfFinder(Singletons.Global g) {
    }

    public static IfFinder v() {
        return G.v().soot_dava_toolkits_base_finders_IfFinder();
    }

    @Override // soot.dava.toolkits.base.finders.FactFinder
    public void find(DavaBody body, AugmentedStmtGraph asg, SETNode SET) throws RetriggerAnalysisException {
        Dava.v().log("IfFinder::find()");
        Iterator asgit = asg.iterator();
        while (asgit.hasNext()) {
            AugmentedStmt as = asgit.next();
            Stmt s = as.get_Stmt();
            if (s instanceof IfStmt) {
                IfStmt ifs = (IfStmt) s;
                if (!body.get_ConsumedConditions().contains(as)) {
                    body.consume_Condition(as);
                    AugmentedStmt succIf = asg.get_AugStmt(ifs.getTarget());
                    AugmentedStmt succElse = as.bsuccs.get(0);
                    if (succIf == succElse) {
                        succElse = as.bsuccs.get(1);
                    }
                    asg.calculate_Reachability(succIf, succElse, as);
                    asg.calculate_Reachability(succElse, succIf, as);
                    IterableSet fullBody = new IterableSet();
                    IterableSet ifBody = find_Body(succIf, succElse);
                    IterableSet elseBody = find_Body(succElse, succIf);
                    fullBody.add(as);
                    fullBody.addAll(ifBody);
                    fullBody.addAll(elseBody);
                    Iterator enlit = body.get_ExceptionFacts().iterator();
                    while (enlit.hasNext()) {
                        ExceptionNode en = enlit.next();
                        IterableSet tryBody = en.get_TryBody();
                        if (tryBody.contains(as)) {
                            Iterator fbit = fullBody.snapshotIterator();
                            while (fbit.hasNext()) {
                                AugmentedStmt fbas = (AugmentedStmt) fbit.next();
                                if (!tryBody.contains(fbas)) {
                                    fullBody.remove(fbas);
                                    if (ifBody.contains(fbas)) {
                                        ifBody.remove(fbas);
                                    }
                                    if (elseBody.contains(fbas)) {
                                        elseBody.remove(fbas);
                                    }
                                }
                            }
                        }
                    }
                    SET.nest(new SETIfElseNode(as, fullBody, ifBody, elseBody));
                }
            }
        }
    }

    private IterableSet find_Body(AugmentedStmt targetBranch, AugmentedStmt otherBranch) {
        IterableSet body = new IterableSet();
        if (targetBranch.get_Reachers().contains(otherBranch)) {
            return body;
        }
        LinkedList<AugmentedStmt> worklist = new LinkedList<>();
        worklist.addLast(targetBranch);
        while (!worklist.isEmpty()) {
            AugmentedStmt as = worklist.removeFirst();
            if (!body.contains(as)) {
                body.add(as);
                for (AugmentedStmt sas : as.csuccs) {
                    if (!sas.get_Reachers().contains(otherBranch) && sas.get_Dominators().contains(targetBranch)) {
                        worklist.addLast(sas);
                    }
                }
            }
        }
        return body;
    }
}
