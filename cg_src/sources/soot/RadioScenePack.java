package soot;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.plugins.SootPhasePlugin;
/* loaded from: gencallgraphv3.jar:soot/RadioScenePack.class */
public class RadioScenePack extends ScenePack {
    private static final Logger logger = LoggerFactory.getLogger(RadioScenePack.class);

    public RadioScenePack(String name) {
        super(name);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.ScenePack, soot.Pack
    public void internalApply() {
        LinkedList<Transform> enabled = new LinkedList<>();
        Iterator<Transform> it = iterator();
        while (it.hasNext()) {
            Transform t = it.next();
            Map<String, String> opts = PhaseOptions.v().getPhaseOptions(t);
            if (PhaseOptions.getBoolean(opts, "enabled")) {
                enabled.add(t);
            }
        }
        if (enabled.isEmpty()) {
            logger.debug("Exactly one phase in the pack " + getPhaseName() + " must be enabled. Currently, none of them are.");
            throw new CompilationDeathException(0);
        } else if (enabled.size() > 1) {
            logger.debug("Only one phase in the pack " + getPhaseName() + " may be enabled. The following are enabled currently: ");
            Iterator<Transform> it2 = enabled.iterator();
            while (it2.hasNext()) {
                Transform t2 = it2.next();
                logger.debug("  " + t2.getPhaseName());
            }
            throw new CompilationDeathException(0);
        } else {
            Iterator<Transform> it3 = enabled.iterator();
            while (it3.hasNext()) {
                Transform t3 = it3.next();
                t3.apply();
            }
        }
    }

    @Override // soot.Pack
    public void add(Transform t) {
        super.add(t);
        checkEnabled(t);
    }

    @Override // soot.Pack
    public void insertAfter(Transform t, String phaseName) {
        super.insertAfter(t, phaseName);
        checkEnabled(t);
    }

    @Override // soot.Pack
    public void insertBefore(Transform t, String phaseName) {
        super.insertBefore(t, phaseName);
        checkEnabled(t);
    }

    private void checkEnabled(Transform t) {
        Map<String, String> options = PhaseOptions.v().getPhaseOptions(t);
        if (PhaseOptions.getBoolean(options, "enabled")) {
            PhaseOptions.v().setPhaseOption(t, SootPhasePlugin.ENABLED_BY_DEFAULT);
        }
    }
}
