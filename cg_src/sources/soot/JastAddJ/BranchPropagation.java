package soot.JastAddJ;

import java.util.ArrayList;
import java.util.Collection;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/BranchPropagation.class */
public interface BranchPropagation {
    void collectBranches(Collection collection);

    Stmt branchTarget(Stmt stmt);

    void collectFinally(Stmt stmt, ArrayList arrayList);

    Collection targetContinues();

    Collection targetBreaks();

    Collection targetBranches();

    Collection escapedBranches();

    Collection branches();

    boolean targetOf(ContinueStmt continueStmt);

    boolean targetOf(BreakStmt breakStmt);
}
