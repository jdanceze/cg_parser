package soot.dexpler;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.Unit;
import soot.Value;
import soot.jimple.BinopExpr;
import soot.jimple.IfStmt;
import soot.jimple.IntConstant;
import soot.jimple.NullConstant;
/* loaded from: gencallgraphv3.jar:soot/dexpler/DexNullIfTransformer.class */
public class DexNullIfTransformer extends BodyTransformer {
    private boolean hasModifiedBody;

    public static DexNullIfTransformer v() {
        return new DexNullIfTransformer();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        NullConstant nullC = NullConstant.v();
        Iterator<Unit> unitIt = b.getUnits().snapshotIterator();
        while (unitIt.hasNext()) {
            Unit u = unitIt.next();
            if (u instanceof IfStmt) {
                IfStmt ifStmt = (IfStmt) u;
                Value o = ifStmt.getCondition();
                if (o instanceof BinopExpr) {
                    BinopExpr bop = (BinopExpr) o;
                    Value l = bop.getOp1();
                    Value r = bop.getOp2();
                    if (isNull(l) && isNull(r) && ((l instanceof NullConstant) || (r instanceof NullConstant))) {
                        bop.setOp1(nullC);
                        bop.setOp2(nullC);
                        this.hasModifiedBody = true;
                    }
                }
            }
        }
    }

    private boolean isNull(Value l) {
        if (l instanceof NullConstant) {
            return true;
        }
        if ((l instanceof IntConstant) && ((IntConstant) l).value == 0) {
            return true;
        }
        return false;
    }

    public boolean hasModifiedBody() {
        return this.hasModifiedBody;
    }
}
