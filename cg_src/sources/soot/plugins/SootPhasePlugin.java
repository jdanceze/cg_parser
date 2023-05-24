package soot.plugins;

import soot.Transformer;
import soot.plugins.model.PhasePluginDescription;
/* loaded from: gencallgraphv3.jar:soot/plugins/SootPhasePlugin.class */
public interface SootPhasePlugin {
    public static final String ENABLED_BY_DEFAULT = "enabled:true";

    String[] getDeclaredOptions();

    String[] getDefaultOptions();

    Transformer getTransformer();

    void setDescription(PhasePluginDescription phasePluginDescription);
}
