package soot.jimple.infoflow.resources.controls;

import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/resources/controls/LayoutControl.class */
public abstract class LayoutControl {
    public abstract ISourceSinkDefinition getSourceDefinition();

    public boolean isSensitive() {
        return false;
    }
}
