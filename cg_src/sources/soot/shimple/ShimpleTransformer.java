package soot.shimple;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.G;
import soot.Scene;
import soot.SceneTransformer;
import soot.Singletons;
import soot.SootClass;
import soot.SootMethod;
import soot.options.Options;
/* loaded from: gencallgraphv3.jar:soot/shimple/ShimpleTransformer.class */
public class ShimpleTransformer extends SceneTransformer {
    private static final Logger logger = LoggerFactory.getLogger(ShimpleTransformer.class);

    public ShimpleTransformer(Singletons.Global g) {
    }

    public static ShimpleTransformer v() {
        return G.v().soot_shimple_ShimpleTransformer();
    }

    @Override // soot.SceneTransformer
    protected void internalTransform(String phaseName, Map options) {
        ShimpleBody sBody;
        if (Options.v().verbose()) {
            logger.debug("Transforming all classes in the Scene to Shimple...");
        }
        for (SootClass sClass : Scene.v().getClasses()) {
            if (!sClass.isPhantom()) {
                for (SootMethod method : sClass.getMethods()) {
                    if (method.isConcrete()) {
                        if (method.hasActiveBody()) {
                            Body body = method.getActiveBody();
                            if (body instanceof ShimpleBody) {
                                sBody = (ShimpleBody) body;
                                if (!sBody.isSSA()) {
                                    sBody.rebuild();
                                }
                            } else {
                                sBody = Shimple.v().newBody(body);
                            }
                            method.setActiveBody(sBody);
                        } else {
                            method.setSource(new ShimpleMethodSource(method.getSource()));
                        }
                    }
                }
            }
        }
    }
}
