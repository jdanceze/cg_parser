package heros;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/MeetLattice.class */
public interface MeetLattice<V> {
    V topElement();

    V bottomElement();

    V meet(V v, V v2);
}
