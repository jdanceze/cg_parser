package soot.jimple.toolkits.scalar;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Singletons;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.jimple.ConditionExpr;
import soot.jimple.EqExpr;
import soot.jimple.GeExpr;
import soot.jimple.GotoStmt;
import soot.jimple.GtExpr;
import soot.jimple.IfStmt;
import soot.jimple.Jimple;
import soot.jimple.LeExpr;
import soot.jimple.LtExpr;
import soot.jimple.NeExpr;
import soot.jimple.Stmt;
import soot.jimple.StmtBody;
import soot.jimple.SwitchStmt;
import soot.options.Options;
import soot.shimple.Shimple;
import soot.shimple.ShimpleBody;
import soot.shimple.internal.SUnitBox;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/UnconditionalBranchFolder.class */
public class UnconditionalBranchFolder extends BodyTransformer {
    private static final Logger logger = LoggerFactory.getLogger(UnconditionalBranchFolder.class);

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/UnconditionalBranchFolder$BranchType.class */
    public enum BranchType {
        TOTAL_COUNT,
        GOTO_GOTO,
        IF_TO_GOTO,
        GOTO_IF,
        IF_TO_IF,
        IF_SAME_TARGET,
        GOTO_SUCCESSOR;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static BranchType[] valuesCustom() {
            BranchType[] valuesCustom = values();
            int length = valuesCustom.length;
            BranchType[] branchTypeArr = new BranchType[length];
            System.arraycopy(valuesCustom, 0, branchTypeArr, 0, length);
            return branchTypeArr;
        }
    }

    public UnconditionalBranchFolder(Singletons.Global g) {
    }

    public static UnconditionalBranchFolder v() {
        return G.v().soot_jimple_toolkits_scalar_UnconditionalBranchFolder();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        Result res;
        boolean verbose = Options.v().verbose();
        if (verbose) {
            logger.debug("[" + b.getMethod().getName() + "] Folding unconditional branches...");
        }
        Transformer transformer = new Transformer((StmtBody) b);
        int iter = 0;
        do {
            res = transformer.transform();
            if (verbose) {
                iter++;
                logger.debug("[" + b.getMethod().getName() + "]     " + res.getNumFixed(BranchType.TOTAL_COUNT) + " of " + res.getNumFound(BranchType.TOTAL_COUNT) + " branches folded in iteration " + iter + ".");
            }
        } while (res.modified);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/UnconditionalBranchFolder$HandleRes.class */
    public static class HandleRes {
        final BranchType type;
        final boolean fixed;

        public HandleRes(BranchType type, boolean fixed) {
            this.type = type;
            this.fixed = fixed;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/UnconditionalBranchFolder$Result.class */
    public static class Result {
        private final int[] numFound;
        private final int[] numFixed;
        private boolean modified;

        private Result() {
            this.numFound = new int[BranchType.valuesCustom().length];
            this.numFixed = new int[BranchType.valuesCustom().length];
        }

        /* synthetic */ Result(Result result) {
            this();
        }

        public void updateCounters(HandleRes r) {
            updateCounters(r.type, r.fixed);
        }

        public void updateCounters(BranchType type, boolean fixed) {
            int indexTotal = BranchType.TOTAL_COUNT.ordinal();
            int indexUpdate = type.ordinal();
            boolean updatingTotal = indexUpdate == indexTotal;
            if (updatingTotal && fixed) {
                throw new IllegalArgumentException("Cannot mark TOTAL as fixed!");
            }
            int[] iArr = this.numFound;
            iArr[indexTotal] = iArr[indexTotal] + 1;
            if (!updatingTotal) {
                int[] iArr2 = this.numFound;
                iArr2[indexUpdate] = iArr2[indexUpdate] + 1;
            }
            if (fixed) {
                this.modified = true;
                int[] iArr3 = this.numFixed;
                iArr3[indexTotal] = iArr3[indexTotal] + 1;
                int[] iArr4 = this.numFixed;
                iArr4[indexUpdate] = iArr4[indexUpdate] + 1;
            }
        }

        public int getNumFound(BranchType type) {
            int indexCurrType = type.ordinal();
            return this.numFound[indexCurrType];
        }

        public int getNumFixed(BranchType type) {
            int indexCurrType = type.ordinal();
            return this.numFixed[indexCurrType];
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/UnconditionalBranchFolder$Transformer.class */
    private static class Transformer {
        private final Map<Stmt, Stmt> stmtMap = new HashMap();
        private final boolean isShimple;
        private final Chain<Unit> units;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !UnconditionalBranchFolder.class.desiredAssertionStatus();
        }

        public Transformer(StmtBody body) {
            this.isShimple = body instanceof ShimpleBody;
            this.units = body.getUnits();
        }

        public Result transform() {
            this.stmtMap.clear();
            Result res = new Result(null);
            Iterator<Unit> stmtIt = this.units.iterator();
            while (stmtIt.hasNext()) {
                Unit stmt = stmtIt.next();
                if (stmt instanceof GotoStmt) {
                    GotoStmt stmtAsGotoStmt = (GotoStmt) stmt;
                    Stmt gotoTarget = (Stmt) stmtAsGotoStmt.getTarget();
                    if (stmtIt.hasNext() && this.units.getSuccOf(stmt) == gotoTarget) {
                        if (!this.isShimple || removalIsSafeInShimple(stmt)) {
                            stmtIt.remove();
                            res.updateCounters(BranchType.GOTO_SUCCESSOR, true);
                        } else {
                            res.updateCounters(BranchType.GOTO_SUCCESSOR, false);
                        }
                    } else {
                        res.updateCounters(handle(stmtAsGotoStmt, gotoTarget));
                    }
                } else if (stmt instanceof IfStmt) {
                    IfStmt stmtAsIfStmt = (IfStmt) stmt;
                    Stmt ifTarget = stmtAsIfStmt.getTarget();
                    if (stmtIt.hasNext()) {
                        Unit successor = this.units.getSuccOf(stmt);
                        if (successor == ifTarget) {
                            if (!this.isShimple || removalIsSafeInShimple(stmt)) {
                                stmtIt.remove();
                                res.updateCounters(BranchType.IF_SAME_TARGET, true);
                            } else {
                                res.updateCounters(BranchType.IF_SAME_TARGET, false);
                            }
                        } else if (successor instanceof GotoStmt) {
                            GotoStmt succAsGoto = (GotoStmt) successor;
                            Stmt gotoTarget2 = (Stmt) succAsGoto.getTarget();
                            if (gotoTarget2 == ifTarget) {
                                if (!this.isShimple || removalIsSafeInShimple(stmt)) {
                                    stmtIt.remove();
                                    res.updateCounters(BranchType.IF_SAME_TARGET, true);
                                } else {
                                    res.updateCounters(BranchType.IF_SAME_TARGET, false);
                                }
                            } else {
                                Unit afterSuccessor = this.units.getSuccOf(successor);
                                if (afterSuccessor == ifTarget) {
                                    Value oldCondition = stmtAsIfStmt.getCondition();
                                    if (!$assertionsDisabled && !(oldCondition instanceof ConditionExpr)) {
                                        throw new AssertionError();
                                    }
                                    stmtAsIfStmt.setCondition(UnconditionalBranchFolder.reverseCondition((ConditionExpr) oldCondition));
                                    succAsGoto.setTarget(ifTarget);
                                    stmtAsIfStmt.setTarget(gotoTarget2);
                                    ifTarget = gotoTarget2;
                                }
                            }
                        }
                    }
                    res.updateCounters(handle(stmtAsIfStmt, ifTarget));
                } else if (stmt instanceof SwitchStmt) {
                    SwitchStmt stmtAsSwitchStmt = (SwitchStmt) stmt;
                    for (UnitBox ub : stmtAsSwitchStmt.getUnitBoxes()) {
                        Stmt caseTarget = (Stmt) ub.getUnit();
                        if (caseTarget instanceof GotoStmt) {
                            Stmt newTarget = getFinalTarget(caseTarget);
                            if (newTarget == null || newTarget == caseTarget) {
                                res.updateCounters(BranchType.GOTO_GOTO, false);
                            } else {
                                ub.setUnit(newTarget);
                                res.updateCounters(BranchType.GOTO_GOTO, true);
                            }
                        }
                    }
                }
            }
            return res;
        }

        private HandleRes handle(GotoStmt gotoStmt, Stmt target) {
            if ($assertionsDisabled || gotoStmt.getTarget() == target) {
                if (target instanceof GotoStmt) {
                    if (!this.isShimple || removalIsSafeInShimple(target)) {
                        Stmt newTarget = getFinalTarget(target);
                        if (newTarget == null) {
                            newTarget = gotoStmt;
                        }
                        if (newTarget != target) {
                            if (this.isShimple) {
                                if (!$assertionsDisabled && !hasNoPointersOrSingleJumpPred(target, gotoStmt)) {
                                    throw new AssertionError();
                                }
                                Shimple.redirectPointers(target, gotoStmt);
                            }
                            gotoStmt.setTarget(newTarget);
                            return new HandleRes(BranchType.GOTO_GOTO, true);
                        }
                    }
                    return new HandleRes(BranchType.GOTO_GOTO, false);
                } else if (target instanceof IfStmt) {
                    return new HandleRes(BranchType.GOTO_IF, false);
                } else {
                    return new HandleRes(BranchType.TOTAL_COUNT, false);
                }
            }
            throw new AssertionError();
        }

        private HandleRes handle(IfStmt ifStmt, Stmt target) {
            IfStmt targetAsIfStmt;
            Stmt newTarget;
            if ($assertionsDisabled || ifStmt.getTarget() == target) {
                if (target instanceof GotoStmt) {
                    if (!this.isShimple || removalIsSafeInShimple(target)) {
                        Stmt newTarget2 = getFinalTarget(target);
                        if (newTarget2 == null) {
                            newTarget2 = ifStmt;
                        }
                        if (newTarget2 != target) {
                            if (this.isShimple) {
                                if (!$assertionsDisabled && !hasNoPointersOrSingleJumpPred(target, ifStmt)) {
                                    throw new AssertionError();
                                }
                                Shimple.redirectPointers(target, ifStmt);
                            }
                            ifStmt.setTarget(newTarget2);
                            return new HandleRes(BranchType.IF_TO_GOTO, true);
                        }
                    }
                    return new HandleRes(BranchType.IF_TO_GOTO, false);
                } else if (target instanceof IfStmt) {
                    if (ifStmt != target && ((!this.isShimple || removalIsSafeInShimple(target)) && (newTarget = (targetAsIfStmt = (IfStmt) target).getTarget()) != target && ifStmt.getCondition().equivTo(targetAsIfStmt.getCondition()))) {
                        if (this.isShimple) {
                            if (!$assertionsDisabled && !hasNoPointersOrSingleJumpPred(target, ifStmt)) {
                                throw new AssertionError();
                            }
                            Shimple.redirectPointers(target, ifStmt);
                        }
                        ifStmt.setTarget(newTarget);
                        return new HandleRes(BranchType.IF_TO_IF, true);
                    }
                    return new HandleRes(BranchType.IF_TO_IF, false);
                } else {
                    return new HandleRes(BranchType.TOTAL_COUNT, false);
                }
            }
            throw new AssertionError();
        }

        private Stmt getFinalTarget(Stmt stmt) {
            Stmt finalTarget;
            if (!(stmt instanceof GotoStmt)) {
                return stmt;
            }
            this.stmtMap.put(stmt, stmt);
            Stmt target = (Stmt) ((GotoStmt) stmt).getTarget();
            if (this.stmtMap.containsKey(target)) {
                finalTarget = this.stmtMap.get(target);
                if (finalTarget == target) {
                    finalTarget = null;
                }
            } else {
                finalTarget = getFinalTarget(target);
            }
            this.stmtMap.put(stmt, finalTarget);
            return finalTarget;
        }

        private boolean removalIsSafeInShimple(Unit stmt) {
            Unit chainPred = this.units.getPredOf(stmt);
            if (chainPred == null) {
                return true;
            }
            List<UnitBox> boxesPointingToThis = stmt.getBoxesPointingToThis();
            if (boxesPointingToThis.isEmpty()) {
                return true;
            }
            boolean hasBackref = false;
            HashSet<UnitBox> predBoxes = new HashSet<>();
            for (UnitBox ub : boxesPointingToThis) {
                if (ub.isBranchTarget()) {
                    if (!$assertionsDisabled && stmt != ub.getUnit()) {
                        throw new AssertionError();
                    }
                    predBoxes.add(ub);
                } else if (!$assertionsDisabled && !(ub instanceof SUnitBox)) {
                    throw new AssertionError();
                } else {
                    hasBackref = true;
                }
            }
            if (!hasBackref) {
                return true;
            }
            int predecessorCount = predBoxes.size();
            if (predecessorCount > 1) {
                return false;
            }
            if (chainPred.fallsThrough() && Collections.disjoint(chainPred.getUnitBoxes(), predBoxes)) {
                predecessorCount++;
            }
            return predecessorCount == 1;
        }

        private boolean hasNoPointersOrSingleJumpPred(Unit toRemove, Unit jumpPred) {
            boolean hasBackref = false;
            HashSet<UnitBox> predBoxes = new HashSet<>();
            for (UnitBox ub : toRemove.getBoxesPointingToThis()) {
                if (ub.isBranchTarget()) {
                    if (!$assertionsDisabled && toRemove != ub.getUnit()) {
                        throw new AssertionError();
                    }
                    predBoxes.add(ub);
                } else if (!$assertionsDisabled && !(ub instanceof SUnitBox)) {
                    throw new AssertionError();
                } else {
                    hasBackref = true;
                }
            }
            if (!hasBackref) {
                return true;
            }
            if (predBoxes.size() != 1 || !jumpPred.getUnitBoxes().containsAll(predBoxes)) {
                return false;
            }
            Unit chainPred = this.units.getPredOf(toRemove);
            return chainPred == null || !chainPred.fallsThrough();
        }
    }

    public static ConditionExpr reverseCondition(ConditionExpr cond) {
        ConditionExpr newExpr;
        if (cond instanceof EqExpr) {
            newExpr = Jimple.v().newNeExpr(cond.getOp1(), cond.getOp2());
        } else if (cond instanceof NeExpr) {
            newExpr = Jimple.v().newEqExpr(cond.getOp1(), cond.getOp2());
        } else if (cond instanceof GtExpr) {
            newExpr = Jimple.v().newLeExpr(cond.getOp1(), cond.getOp2());
        } else if (cond instanceof GeExpr) {
            newExpr = Jimple.v().newLtExpr(cond.getOp1(), cond.getOp2());
        } else if (cond instanceof LtExpr) {
            newExpr = Jimple.v().newGeExpr(cond.getOp1(), cond.getOp2());
        } else if (cond instanceof LeExpr) {
            newExpr = Jimple.v().newGtExpr(cond.getOp1(), cond.getOp2());
        } else {
            throw new RuntimeException("Unknown ConditionExpr");
        }
        newExpr.getOp1Box().addAllTagsOf(cond.getOp1Box());
        newExpr.getOp2Box().addAllTagsOf(cond.getOp2Box());
        return newExpr;
    }
}
