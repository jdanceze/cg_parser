package soot.jimple.infoflow.sourcesSinks.definitions;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/sourcesSinks/definitions/ImmutableAccessPathTuple.class */
public class ImmutableAccessPathTuple extends AccessPathTuple {
    public ImmutableAccessPathTuple(AccessPathTuple parent) {
        super(parent);
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.AccessPathTuple
    public void setDescription(String description) {
        throw new RuntimeException("Immutable object cannot be modified");
    }
}
