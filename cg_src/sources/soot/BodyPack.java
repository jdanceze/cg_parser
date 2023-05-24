package soot;

import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.options.Options;
import soot.toolkits.graph.interaction.InteractionHandler;
/* loaded from: gencallgraphv3.jar:soot/BodyPack.class */
public class BodyPack extends Pack {
    private static final Logger logger = LoggerFactory.getLogger(BodyPack.class);

    public BodyPack(String name) {
        super(name);
    }

    @Override // soot.Pack
    protected void internalApply(Body b) {
        boolean interactive_mode = Options.v().interactive_mode();
        Iterator<Transform> it = iterator();
        while (it.hasNext()) {
            Transform t = it.next();
            if (interactive_mode) {
                InteractionHandler.v().handleNewAnalysis(t, b);
            }
            t.apply(b);
            if (interactive_mode) {
                InteractionHandler.v().handleTransformDone(t, b);
            }
        }
    }
}
