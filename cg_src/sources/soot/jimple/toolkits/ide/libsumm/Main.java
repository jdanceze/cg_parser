package soot.jimple.toolkits.ide.libsumm;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.PackManager;
import soot.Transform;
import soot.Unit;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/ide/libsumm/Main.class */
public class Main {
    static int yes = 0;
    static int no = 0;

    public static void main(String[] args) {
        PackManager.v().getPack("jtp").add(new Transform("jtp.fixedie", new BodyTransformer() { // from class: soot.jimple.toolkits.ide.libsumm.Main.1
            @Override // soot.BodyTransformer
            protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
                Iterator<Unit> it = b.getUnits().iterator();
                while (it.hasNext()) {
                    Unit u = it.next();
                    Stmt s = (Stmt) u;
                    if (s.containsInvokeExpr()) {
                        InvokeExpr ie = s.getInvokeExpr();
                        if (FixedMethods.isFixed(ie)) {
                            System.err.println("+++ " + ie);
                            Main.yes++;
                        } else {
                            System.err.println(" -  " + ie);
                            Main.no++;
                        }
                    }
                }
            }
        }));
        soot.Main.main(args);
        System.err.println("+++ " + yes);
        System.err.println(" -  " + no);
    }
}
