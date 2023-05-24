package soot.jimple.infoflow.sourcesSinks.definitions;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/sourcesSinks/definitions/ISourceSinkDefinition.class */
public interface ISourceSinkDefinition {
    void setCategory(ISourceSinkCategory iSourceSinkCategory);

    ISourceSinkCategory getCategory();

    ISourceSinkDefinition getSourceOnlyDefinition();

    ISourceSinkDefinition getSinkOnlyDefinition();

    void merge(ISourceSinkDefinition iSourceSinkDefinition);

    boolean isEmpty();
}
