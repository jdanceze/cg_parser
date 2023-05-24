package heros.solver;
@Deprecated
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/solver/LinkedNode.class */
public interface LinkedNode<D> {
    void addNeighbor(D d);

    void setCallingContext(D d);
}
