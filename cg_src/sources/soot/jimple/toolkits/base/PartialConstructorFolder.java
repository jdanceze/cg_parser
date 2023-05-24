package soot.jimple.toolkits.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NewExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.options.Options;
import soot.tagkit.SourceLnPosTag;
import soot.toolkits.scalar.LocalUses;
import soot.toolkits.scalar.UnitValueBoxPair;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/base/PartialConstructorFolder.class */
public class PartialConstructorFolder extends BodyTransformer {
    private static final Logger logger = LoggerFactory.getLogger(PartialConstructorFolder.class);
    private List<Type> types;

    public void setTypes(List<Type> t) {
        this.types = t;
    }

    public List<Type> getTypes() {
        return this.types;
    }

    @Override // soot.BodyTransformer
    public void internalTransform(Body b, String phaseName, Map<String, String> options) {
        JimpleBody body = (JimpleBody) b;
        if (Options.v().verbose()) {
            logger.debug("[" + body.getMethod().getName() + "] Folding Jimple constructors...");
        }
        Chain<Unit> units = body.getUnits();
        List<Unit> stmtList = new ArrayList<>(units);
        LocalUses localUses = LocalUses.Factory.newLocalUses(body);
        Iterator<Unit> nextStmtIt = stmtList.iterator();
        nextStmtIt.next();
        for (Unit u : stmtList) {
            if (u instanceof AssignStmt) {
                AssignStmt as = (AssignStmt) u;
                Value lhs = as.getLeftOp();
                if (lhs instanceof Local) {
                    Value rhs = as.getRightOp();
                    if (rhs instanceof NewExpr) {
                        if (nextStmtIt.hasNext()) {
                            Unit next = nextStmtIt.next();
                            if (next instanceof InvokeStmt) {
                                InvokeExpr ie = ((InvokeStmt) next).getInvokeExpr();
                                if (ie instanceof SpecialInvokeExpr) {
                                    SpecialInvokeExpr invokeExpr = (SpecialInvokeExpr) ie;
                                    if (invokeExpr.getBase() == lhs) {
                                        return;
                                    }
                                }
                            }
                        }
                        if (this.types.contains(((NewExpr) rhs).getType())) {
                            boolean madeNewInvokeExpr = false;
                            for (UnitValueBoxPair uvb : localUses.getUsesOf(u)) {
                                Unit use = uvb.unit;
                                if (use instanceof InvokeStmt) {
                                    InvokeExpr ie2 = ((InvokeStmt) use).getInvokeExpr();
                                    if ((ie2 instanceof SpecialInvokeExpr) && lhs == ((SpecialInvokeExpr) ie2).getBase()) {
                                        AssignStmt constructStmt = Jimple.v().newAssignStmt(lhs, rhs);
                                        constructStmt.setRightOp(Jimple.v().newNewExpr(((NewExpr) rhs).getBaseType()));
                                        madeNewInvokeExpr = true;
                                        use.redirectJumpsToThisTo(constructStmt);
                                        units.insertBefore(constructStmt, (AssignStmt) use);
                                        constructStmt.addTag(u.getTag(SourceLnPosTag.NAME));
                                    }
                                }
                            }
                            if (madeNewInvokeExpr) {
                                units.remove(u);
                            }
                        }
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
            }
        }
    }
}
