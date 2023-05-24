package soot.jimple.toolkits.callgraph;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.RefType;
import soot.Scene;
import soot.SootMethod;
import soot.UnitPatchingChain;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.StringConstant;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/UnreachableMethodTransformer.class */
public class UnreachableMethodTransformer extends BodyTransformer {
    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        SootMethod method = b.getMethod();
        Scene scene = Scene.v();
        if (scene.getReachableMethods().contains(method)) {
            return;
        }
        Jimple jimp = Jimple.v();
        Vector vector = new Vector();
        Local tmpRef = jimp.newLocal("tmpRef", RefType.v("java.io.PrintStream"));
        b.getLocals().add(tmpRef);
        vector.add(jimp.newAssignStmt(tmpRef, jimp.newStaticFieldRef(scene.getField("<java.lang.System: java.io.PrintStream out>").makeRef())));
        vector.add(jimp.newInvokeStmt(jimp.newStaticInvokeExpr(scene.getMethod("<java.lang.Thread: void dumpStack()>").makeRef())));
        SootMethod toCall = scene.getMethod("<java.io.PrintStream: void println(java.lang.String)>");
        vector.add(jimp.newInvokeStmt(jimp.newVirtualInvokeExpr(tmpRef, toCall.makeRef(), StringConstant.v("Executing supposedly unreachable method:"))));
        vector.add(jimp.newInvokeStmt(jimp.newVirtualInvokeExpr(tmpRef, toCall.makeRef(), StringConstant.v("\t" + method.getDeclaringClass().getName() + "." + method.getName()))));
        vector.add(jimp.newInvokeStmt(jimp.newStaticInvokeExpr(scene.getMethod("<java.lang.System: void exit(int)>").makeRef(), IntConstant.v(1))));
        UnitPatchingChain units = b.getUnits();
        units.insertBefore((List<Vector>) vector, (Vector) units.getFirst());
    }
}
