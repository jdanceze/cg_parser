package soot.jimple.toolkits.scalar;

import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Singletons;
import soot.Unit;
import soot.Value;
import soot.jimple.IfStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.StmtBody;
import soot.options.Options;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/ConditionalBranchFolder.class */
public class ConditionalBranchFolder extends BodyTransformer {
    private static final Logger logger;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ConditionalBranchFolder.class.desiredAssertionStatus();
        logger = LoggerFactory.getLogger(ConditionalBranchFolder.class);
    }

    public ConditionalBranchFolder(Singletons.Global g) {
    }

    public static ConditionalBranchFolder v() {
        return G.v().soot_jimple_toolkits_scalar_ConditionalBranchFolder();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
        StmtBody stmtBody = (StmtBody) body;
        if (Options.v().verbose()) {
            logger.debug("[" + stmtBody.getMethod().getName() + "] Folding conditional branches...");
        }
        int numTrue = 0;
        int numFalse = 0;
        Chain<Unit> units = stmtBody.getUnits();
        Iterator<Unit> it = units.snapshotIterator();
        while (it.hasNext()) {
            Unit stmt = it.next();
            if (stmt instanceof IfStmt) {
                IfStmt ifs = (IfStmt) stmt;
                Value cond = Evaluator.getConstantValueOf(ifs.getCondition());
                if (cond == null) {
                    continue;
                } else if (!$assertionsDisabled && !(cond instanceof IntConstant)) {
                    throw new AssertionError();
                } else {
                    if (((IntConstant) cond).value == 1) {
                        units.swapWith(stmt, Jimple.v().newGotoStmt(ifs.getTarget()));
                        numTrue++;
                    } else if (!$assertionsDisabled && ((IntConstant) cond).value != 0) {
                        throw new AssertionError();
                    } else {
                        units.remove(stmt);
                        numFalse++;
                    }
                }
            }
        }
        if (Options.v().verbose()) {
            logger.debug("[" + stmtBody.getMethod().getName() + "]     Folded " + numTrue + " true, " + numFalse + " conditional branches");
        }
    }
}
