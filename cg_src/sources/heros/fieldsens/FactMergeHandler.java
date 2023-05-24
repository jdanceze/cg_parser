package heros.fieldsens;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/fieldsens/FactMergeHandler.class */
public interface FactMergeHandler<Fact> {
    void merge(Fact fact, Fact fact2);

    void restoreCallingContext(Fact fact, Fact fact2);
}
