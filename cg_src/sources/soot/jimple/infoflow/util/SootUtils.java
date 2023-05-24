package soot.jimple.infoflow.util;

import java.util.ArrayList;
import java.util.List;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.InvokeExpr;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/util/SootUtils.class */
public class SootUtils {
    public static List<Value> getUseAndDefValues(Unit u) {
        List<Value> valueList = new ArrayList<>();
        for (ValueBox vb : u.getUseAndDefBoxes()) {
            Value val = vb.getValue();
            if (val instanceof InvokeExpr) {
                InvokeExpr iexpr = (InvokeExpr) val;
                for (ValueBox invBox : iexpr.getUseBoxes()) {
                    valueList.add(invBox.getValue());
                }
            } else {
                valueList.add(val);
            }
        }
        return valueList;
    }
}
