package soot.jimple.infoflow.sourcesSinks.definitions;

import java.util.Collection;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/sourcesSinks/definitions/ISourceSinkDefinitionProvider.class */
public interface ISourceSinkDefinitionProvider {
    Collection<? extends ISourceSinkDefinition> getSources();

    Collection<? extends ISourceSinkDefinition> getSinks();

    Collection<? extends ISourceSinkDefinition> getAllMethods();
}
