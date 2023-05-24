package soot.jimple.infoflow.sourcesSinks.definitions;

import java.util.Collections;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/sourcesSinks/definitions/NullSourceSinkDefinitionProvider.class */
public class NullSourceSinkDefinitionProvider implements ISourceSinkDefinitionProvider {
    @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinitionProvider
    public Set<ISourceSinkDefinition> getSources() {
        return Collections.emptySet();
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinitionProvider
    public Set<ISourceSinkDefinition> getSinks() {
        return Collections.emptySet();
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinitionProvider
    public Set<ISourceSinkDefinition> getAllMethods() {
        return Collections.emptySet();
    }
}
