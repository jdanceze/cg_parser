package soot.jimple.infoflow.sourcesSinks.definitions;

import java.util.Collection;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/sourcesSinks/definitions/IAccessPathBasedSourceSinkDefinition.class */
public interface IAccessPathBasedSourceSinkDefinition extends ISourceSinkDefinition {
    Set<AccessPathTuple> getAllAccessPaths();

    IAccessPathBasedSourceSinkDefinition filter(Collection<AccessPathTuple> collection);

    @Override // 
    boolean isEmpty();

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition, soot.jimple.infoflow.sourcesSinks.definitions.IAccessPathBasedSourceSinkDefinition
    IAccessPathBasedSourceSinkDefinition getSourceOnlyDefinition();

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition, soot.jimple.infoflow.sourcesSinks.definitions.IAccessPathBasedSourceSinkDefinition
    IAccessPathBasedSourceSinkDefinition getSinkOnlyDefinition();
}
