package soot.jimple.toolkits.annotation.methods;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import soot.G;
import soot.Scene;
import soot.SceneTransformer;
import soot.Singletons;
import soot.SootClass;
import soot.SootMethod;
import soot.tagkit.ColorTag;
import soot.tagkit.StringTag;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/methods/UnreachableMethodsTagger.class */
public class UnreachableMethodsTagger extends SceneTransformer {
    public UnreachableMethodsTagger(Singletons.Global g) {
    }

    public static UnreachableMethodsTagger v() {
        return G.v().soot_jimple_toolkits_annotation_methods_UnreachableMethodsTagger();
    }

    @Override // soot.SceneTransformer
    protected void internalTransform(String phaseName, Map options) {
        ArrayList<SootMethod> methodList = new ArrayList<>();
        for (SootClass appClass : Scene.v().getApplicationClasses()) {
            for (SootMethod method : appClass.getMethods()) {
                if (!Scene.v().getReachableMethods().contains(method)) {
                    methodList.add(method);
                }
            }
        }
        Iterator<SootMethod> unusedIt = methodList.iterator();
        while (unusedIt.hasNext()) {
            SootMethod unusedMethod = unusedIt.next();
            unusedMethod.addTag(new StringTag("Method " + unusedMethod.getName() + " is not reachable!", "Unreachable Methods"));
            unusedMethod.addTag(new ColorTag(255, 0, 0, true, "Unreachable Methods"));
        }
    }
}
