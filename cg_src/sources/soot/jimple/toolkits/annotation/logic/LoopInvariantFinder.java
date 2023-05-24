package soot.jimple.toolkits.annotation.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Local;
import soot.Singletons;
import soot.Value;
import soot.ValueBox;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.Expr;
import soot.jimple.GotoStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.NaiveSideEffectTester;
import soot.jimple.NewExpr;
import soot.jimple.Stmt;
import soot.tagkit.ColorTag;
import soot.tagkit.LoopInvariantTag;
import soot.toolkits.scalar.SmartLocalDefs;
import soot.toolkits.scalar.SmartLocalDefsPool;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/logic/LoopInvariantFinder.class */
public class LoopInvariantFinder extends BodyTransformer {
    private static final Logger logger = LoggerFactory.getLogger(LoopInvariantFinder.class);
    private ArrayList constants;

    public LoopInvariantFinder(Singletons.Global g) {
    }

    public static LoopInvariantFinder v() {
        return G.v().soot_jimple_toolkits_annotation_logic_LoopInvariantFinder();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map options) {
        SmartLocalDefs sld = SmartLocalDefsPool.v().getSmartLocalDefsFor(b);
        sld.getGraph();
        NaiveSideEffectTester nset = new NaiveSideEffectTester();
        Collection<Loop> loops = new LoopFinder().getLoops(b);
        this.constants = new ArrayList();
        if (loops.isEmpty()) {
            return;
        }
        for (Loop loop : loops) {
            loop.getHead();
            Collection<Stmt> loopStmts = loop.getLoopStatements();
            for (Stmt tStmt : loopStmts) {
                handleLoopBodyStmt(tStmt, nset, loopStmts);
            }
        }
    }

    private void handleLoopBodyStmt(Stmt s, NaiveSideEffectTester nset, Collection<Stmt> loopStmts) {
        if (s instanceof DefinitionStmt) {
            DefinitionStmt ds = (DefinitionStmt) s;
            if ((ds.getLeftOp() instanceof Local) && (ds.getRightOp() instanceof Constant)) {
                if (!this.constants.contains(ds.getLeftOp())) {
                    this.constants.add(ds.getLeftOp());
                } else {
                    this.constants.remove(ds.getLeftOp());
                }
            }
        }
        if ((s instanceof GotoStmt) || (s instanceof InvokeStmt)) {
            return;
        }
        logger.debug("s : " + s + " use boxes: " + s.getUseBoxes() + " def boxes: " + s.getDefBoxes());
        Iterator useBoxesIt = s.getUseBoxes().iterator();
        boolean result = true;
        loop0: while (true) {
            if (!useBoxesIt.hasNext()) {
                break;
            }
            ValueBox vb = useBoxesIt.next();
            Value v = vb.getValue();
            if (v instanceof NewExpr) {
                result = false;
                logger.debug("break uses: due to new expr");
                break;
            } else if (v instanceof InvokeExpr) {
                result = false;
                logger.debug("break uses: due to invoke expr");
                break;
            } else if (!(v instanceof Expr)) {
                logger.debug("test: " + v + " of kind: " + v.getClass());
                for (Stmt next : loopStmts) {
                    if (nset.unitCanWriteTo(next, v) && !isConstant(next)) {
                        logger.debug("result = false unit can be written to by: " + next);
                        result = false;
                        break loop0;
                    }
                }
                continue;
            }
        }
        Iterator defBoxesIt = s.getDefBoxes().iterator();
        loop2: while (true) {
            if (!defBoxesIt.hasNext()) {
                break;
            }
            ValueBox vb2 = defBoxesIt.next();
            Value v2 = vb2.getValue();
            if (v2 instanceof NewExpr) {
                result = false;
                logger.debug("break defs due to new");
                break;
            } else if (v2 instanceof InvokeExpr) {
                result = false;
                logger.debug("break defs due to invoke");
                break;
            } else if (!(v2 instanceof Expr)) {
                logger.debug("test: " + v2 + " of kind: " + v2.getClass());
                for (Stmt next2 : loopStmts) {
                    if (!next2.equals(s) && nset.unitCanWriteTo(next2, v2) && !isConstant(next2)) {
                        logger.debug("result false: unit can be written to by: " + next2);
                        result = false;
                        break loop2;
                    }
                }
                continue;
            }
        }
        logger.debug("stmt: " + s + " result: " + result);
        if (result) {
            s.addTag(new LoopInvariantTag("is loop invariant"));
            s.addTag(new ColorTag(0, "Loop Invariant Analysis"));
        }
    }

    private boolean isConstant(Stmt s) {
        if (s instanceof DefinitionStmt) {
            DefinitionStmt ds = (DefinitionStmt) s;
            if (this.constants.contains(ds.getLeftOp())) {
                return true;
            }
            return false;
        }
        return false;
    }
}
