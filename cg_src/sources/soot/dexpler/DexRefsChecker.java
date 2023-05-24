package soot.dexpler;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.Local;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.Type;
import soot.Unit;
import soot.coffi.Instruction;
import soot.jimple.FieldRef;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/dexpler/DexRefsChecker.class */
public class DexRefsChecker extends DexTransformer {
    Local l = null;

    public static DexRefsChecker v() {
        return new DexRefsChecker();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body body, String phaseName, Map options) {
        SootClass sootClass;
        for (Unit u : getRefCandidates(body)) {
            Stmt s = (Stmt) u;
            boolean hasField = false;
            if (s.containsFieldRef()) {
                FieldRef fr = s.getFieldRef();
                SootField sf = fr.getField();
                if (sf != null) {
                    hasField = true;
                }
                if (!hasField) {
                    System.out.println("Warning: add missing field '" + fr + "' to class!");
                    String frStr = fr.toString();
                    if (frStr.contains(".<")) {
                        sootClass = Scene.v().getSootClass(frStr.split(".<")[1].split(Instruction.argsep)[0].split(":")[0]);
                    } else {
                        sootClass = Scene.v().getSootClass(frStr.split(":")[0].replaceAll("^<", ""));
                    }
                    SootClass sc = sootClass;
                    String fname = fr.toString().split(">")[0].split(Instruction.argsep)[2];
                    Type ftype = fr.getType();
                    sc.addField(Scene.v().makeSootField(fname, ftype, 1));
                }
            } else {
                throw new RuntimeException("Unit '" + u + "' does not contain array ref nor field ref.");
            }
        }
    }

    private Set<Unit> getRefCandidates(Body body) {
        Set<Unit> candidates = new HashSet<>();
        Iterator<Unit> i = body.getUnits().iterator();
        while (i.hasNext()) {
            Unit u = i.next();
            Stmt s = (Stmt) u;
            if (s.containsFieldRef()) {
                candidates.add(u);
            }
        }
        return candidates;
    }
}
