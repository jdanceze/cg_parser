package soot.jimple.toolkits.annotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.G;
import soot.Scene;
import soot.SceneTransformer;
import soot.Singletons;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.IdentityStmt;
import soot.tagkit.LineNumberTag;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/LineNumberAdder.class */
public class LineNumberAdder extends SceneTransformer {
    public LineNumberAdder(Singletons.Global g) {
    }

    public static LineNumberAdder v() {
        return G.v().soot_jimple_toolkits_annotation_LineNumberAdder();
    }

    @Override // soot.SceneTransformer
    public void internalTransform(String phaseName, Map<String, String> opts) {
        Unit s;
        Unit s2;
        Iterator<SootClass> it = Scene.v().getApplicationClasses().snapshotIterator();
        while (it.hasNext()) {
            SootClass sc = it.next();
            HashMap<Integer, SootMethod> lineToMeth = new HashMap<>();
            Iterator it2 = new ArrayList(sc.getMethods()).iterator();
            while (it2.hasNext()) {
                SootMethod meth = (SootMethod) it2.next();
                if (meth.isConcrete()) {
                    Chain<Unit> units = meth.retrieveActiveBody().getUnits();
                    Unit first = units.getFirst();
                    while (true) {
                        s2 = first;
                        if (!(s2 instanceof IdentityStmt)) {
                            break;
                        }
                        first = units.getSuccOf(s2);
                    }
                    LineNumberTag tag = (LineNumberTag) s2.getTag(LineNumberTag.NAME);
                    if (tag != null) {
                        lineToMeth.put(Integer.valueOf(tag.getLineNumber()), meth);
                    }
                }
            }
            for (SootMethod meth2 : sc.getMethods()) {
                if (meth2.isConcrete()) {
                    Chain<Unit> units2 = meth2.retrieveActiveBody().getUnits();
                    Unit first2 = units2.getFirst();
                    while (true) {
                        s = first2;
                        if (!(s instanceof IdentityStmt)) {
                            break;
                        }
                        first2 = units2.getSuccOf(s);
                    }
                    LineNumberTag tag2 = (LineNumberTag) s.getTag(LineNumberTag.NAME);
                    if (tag2 != null) {
                        int line_num = tag2.getLineNumber() - 1;
                        if (lineToMeth.containsKey(Integer.valueOf(line_num))) {
                            meth2.addTag(new LineNumberTag(line_num + 1));
                        } else {
                            meth2.addTag(new LineNumberTag(line_num));
                        }
                    }
                }
            }
        }
    }
}
