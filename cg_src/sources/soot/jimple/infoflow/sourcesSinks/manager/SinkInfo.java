package soot.jimple.infoflow.sourcesSinks.manager;

import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/sourcesSinks/manager/SinkInfo.class */
public class SinkInfo extends AbstractSourceSinkInfo {
    @Override // soot.jimple.infoflow.sourcesSinks.manager.AbstractSourceSinkInfo
    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    @Override // soot.jimple.infoflow.sourcesSinks.manager.AbstractSourceSinkInfo
    public /* bridge */ /* synthetic */ Object getUserData() {
        return super.getUserData();
    }

    @Override // soot.jimple.infoflow.sourcesSinks.manager.AbstractSourceSinkInfo
    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override // soot.jimple.infoflow.sourcesSinks.manager.AbstractSourceSinkInfo
    public /* bridge */ /* synthetic */ ISourceSinkDefinition getDefinition() {
        return super.getDefinition();
    }

    public SinkInfo(ISourceSinkDefinition definition, Object userData) {
        super(definition, userData);
    }

    public SinkInfo(ISourceSinkDefinition definition) {
        super(definition);
    }
}
