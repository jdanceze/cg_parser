package soot.grimp.toolkits.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Local;
import soot.Singletons;
import soot.Unit;
import soot.UnitPatchingChain;
import soot.Value;
import soot.grimp.Grimp;
import soot.grimp.GrimpBody;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeStmt;
import soot.jimple.NewExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.Stmt;
import soot.options.Options;
import soot.toolkits.scalar.LocalUses;
import soot.toolkits.scalar.UnitValueBoxPair;
/* loaded from: gencallgraphv3.jar:soot/grimp/toolkits/base/ConstructorFolder.class */
public class ConstructorFolder extends BodyTransformer {
    private static final Logger logger = LoggerFactory.getLogger(ConstructorFolder.class);

    public ConstructorFolder(Singletons.Global g) {
    }

    public static ConstructorFolder v() {
        return G.v().soot_grimp_toolkits_base_ConstructorFolder();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map options) {
        GrimpBody body = (GrimpBody) b;
        if (Options.v().verbose()) {
            logger.debug("[" + body.getMethod().getName() + "] Folding constructors...");
        }
        UnitPatchingChain units = body.getUnits();
        List<Unit> stmtList = new ArrayList<>();
        stmtList.addAll(units);
        Iterator<Unit> it = stmtList.iterator();
        LocalUses localUses = LocalUses.Factory.newLocalUses(b);
        while (it.hasNext()) {
            Stmt s = (Stmt) it.next();
            if (s instanceof AssignStmt) {
                Value lhs = ((AssignStmt) s).getLeftOp();
                if (lhs instanceof Local) {
                    Value rhs = ((AssignStmt) s).getRightOp();
                    if (rhs instanceof NewExpr) {
                        List<UnitValueBoxPair> lu = localUses.getUsesOf(s);
                        boolean MadeNewInvokeExpr = false;
                        for (UnitValueBoxPair unitValueBoxPair : lu) {
                            Unit use = unitValueBoxPair.unit;
                            if (use instanceof InvokeStmt) {
                                InvokeStmt is = (InvokeStmt) use;
                                if ((is.getInvokeExpr() instanceof SpecialInvokeExpr) && lhs == ((SpecialInvokeExpr) is.getInvokeExpr()).getBase()) {
                                    SpecialInvokeExpr oldInvoke = (SpecialInvokeExpr) is.getInvokeExpr();
                                    LinkedList invokeArgs = new LinkedList();
                                    for (int i = 0; i < oldInvoke.getArgCount(); i++) {
                                        invokeArgs.add(oldInvoke.getArg(i));
                                    }
                                    AssignStmt constructStmt = Grimp.v().newAssignStmt((AssignStmt) s);
                                    constructStmt.setRightOp(Grimp.v().newNewInvokeExpr(((NewExpr) rhs).getBaseType(), oldInvoke.getMethodRef(), invokeArgs));
                                    MadeNewInvokeExpr = true;
                                    use.redirectJumpsToThisTo(constructStmt);
                                    units.insertBefore(constructStmt, (AssignStmt) use);
                                    units.remove(use);
                                }
                            }
                        }
                        if (MadeNewInvokeExpr) {
                            units.remove(s);
                        }
                    }
                }
            }
        }
    }
}
