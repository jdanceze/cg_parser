package soot.dava.toolkits.base.AST.transformations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.Value;
import soot.ValueBox;
import soot.dava.internal.javaRep.DNewInvokeExpr;
import soot.dava.internal.javaRep.DVirtualInvokeExpr;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.grimp.internal.GAddExpr;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/transformations/NewStringBufferSimplification.class */
public class NewStringBufferSimplification extends DepthFirstAdapter {
    public static boolean DEBUG = false;

    public NewStringBufferSimplification() {
    }

    public NewStringBufferSimplification(boolean verbose) {
        super(verbose);
    }

    @Override // soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter
    public void inExprOrRefValueBox(ValueBox argBox) {
        Value newVal;
        if (DEBUG) {
            System.out.println("ValBox is: " + argBox.toString());
        }
        Value tempArgValue = argBox.getValue();
        if (DEBUG) {
            System.out.println("arg value is: " + tempArgValue);
        }
        if (!(tempArgValue instanceof DVirtualInvokeExpr)) {
            if (DEBUG) {
                System.out.println("Not a DVirtualInvokeExpr" + tempArgValue.getClass());
                return;
            }
            return;
        }
        if (DEBUG) {
            System.out.println("arg value is a virtual invokeExpr");
        }
        DVirtualInvokeExpr vInvokeExpr = (DVirtualInvokeExpr) tempArgValue;
        try {
            if (!vInvokeExpr.getMethod().toString().equals("<java.lang.StringBuffer: java.lang.String toString()>")) {
                return;
            }
            if (DEBUG) {
                System.out.println("Ends in toString()");
            }
            Value base = vInvokeExpr.getBase();
            List args = new ArrayList();
            while (base instanceof DVirtualInvokeExpr) {
                DVirtualInvokeExpr tempV = (DVirtualInvokeExpr) base;
                if (DEBUG) {
                    System.out.println("base method is " + tempV.getMethod());
                }
                if (!tempV.getMethod().toString().startsWith("<java.lang.StringBuffer: java.lang.StringBuffer append")) {
                    if (DEBUG) {
                        System.out.println("Found a virtual invoke which is not a append" + tempV.getMethod());
                        return;
                    }
                    return;
                }
                args.add(0, tempV.getArg(0));
                base = ((DVirtualInvokeExpr) base).getBase();
            }
            if (!(base instanceof DNewInvokeExpr)) {
                return;
            }
            if (DEBUG) {
                System.out.println("New expr is " + ((DNewInvokeExpr) base).getMethod());
            }
            if (!((DNewInvokeExpr) base).getMethod().toString().equals("<java.lang.StringBuffer: void <init>()>")) {
                return;
            }
            if (DEBUG) {
                System.out.println("Found a new StringBuffer.append list in it");
            }
            Iterator it = args.iterator();
            Value value = null;
            while (true) {
                newVal = value;
                if (!it.hasNext()) {
                    break;
                }
                Value temp = (Value) it.next();
                if (newVal == null) {
                    value = temp;
                } else {
                    value = new GAddExpr(newVal, temp);
                }
            }
            if (DEBUG) {
                System.out.println("New expression for System.out.println is" + newVal);
            }
            argBox.setValue(newVal);
        } catch (Exception e) {
        }
    }
}
