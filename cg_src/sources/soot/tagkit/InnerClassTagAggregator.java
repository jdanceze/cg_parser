package soot.tagkit;

import java.util.ArrayList;
import java.util.Map;
import soot.G;
import soot.Scene;
import soot.SceneTransformer;
import soot.Singletons;
import soot.SootClass;
/* loaded from: gencallgraphv3.jar:soot/tagkit/InnerClassTagAggregator.class */
public class InnerClassTagAggregator extends SceneTransformer {
    public InnerClassTagAggregator(Singletons.Global g) {
    }

    public static InnerClassTagAggregator v() {
        return G.v().soot_tagkit_InnerClassTagAggregator();
    }

    public String aggregatedName() {
        return "InnerClasses";
    }

    @Override // soot.SceneTransformer
    public void internalTransform(String phaseName, Map<String, String> options) {
        for (SootClass nextSc : Scene.v().getApplicationClasses()) {
            ArrayList<InnerClassTag> list = new ArrayList<>();
            for (Tag t : nextSc.getTags()) {
                if (t instanceof InnerClassTag) {
                    list.add((InnerClassTag) t);
                }
            }
            if (!list.isEmpty()) {
                nextSc.addTag(new InnerClassAttribute(list));
            }
        }
    }
}
