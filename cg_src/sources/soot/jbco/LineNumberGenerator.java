package soot.jbco;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.PackManager;
import soot.Transform;
import soot.Unit;
import soot.tagkit.LineNumberTag;
/* loaded from: gencallgraphv3.jar:soot/jbco/LineNumberGenerator.class */
public class LineNumberGenerator {
    BafLineNumberer bln = new BafLineNumberer();

    public static void main(String[] argv) {
        PackManager.v().getPack("jtp").add(new Transform("jtp.lnprinter", new LineNumberGenerator().bln));
        PackManager.v().getPack("bb").add(new Transform("bb.lnprinter", new LineNumberGenerator().bln));
        soot.Main.main(argv);
    }

    /* loaded from: gencallgraphv3.jar:soot/jbco/LineNumberGenerator$BafLineNumberer.class */
    protected static class BafLineNumberer extends BodyTransformer {
        protected BafLineNumberer() {
        }

        @Override // soot.BodyTransformer
        protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
            System.out.println("Printing Line Numbers for: " + b.getMethod().getSignature());
            Iterator<Unit> it = b.getUnits().iterator();
            while (it.hasNext()) {
                Unit u = it.next();
                LineNumberTag tag = (LineNumberTag) u.getTag(LineNumberTag.NAME);
                if (tag != null) {
                    System.out.println(u + " has Line Number: " + tag.getLineNumber());
                } else {
                    System.out.println(u + " has no Line Number");
                }
            }
            System.out.println("\n");
        }
    }
}
