package soot.jimple.toolkits.annotation;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Singletons;
import soot.jimple.Stmt;
import soot.tagkit.LinkTag;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
import soot.toolkits.graph.MHGDominatorsFinder;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/DominatorsTagger.class */
public class DominatorsTagger extends BodyTransformer {
    public DominatorsTagger(Singletons.Global g) {
    }

    public static DominatorsTagger v() {
        return G.v().soot_jimple_toolkits_annotation_DominatorsTagger();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map opts) {
        MHGDominatorsFinder analysis = new MHGDominatorsFinder(ExceptionalUnitGraphFactory.createExceptionalUnitGraph(b));
        Iterator it = b.getUnits().iterator();
        while (it.hasNext()) {
            Stmt s = (Stmt) it.next();
            List<Stmt> dominators = analysis.getDominators(s);
            for (Stmt ds : dominators) {
                String info = ds + " dominates " + s;
                s.addTag(new LinkTag(info, ds, b.getMethod().getDeclaringClass().getName(), "Dominators"));
            }
        }
    }
}
