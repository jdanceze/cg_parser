package fj;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/Unit.class */
public final class Unit {
    private static final Unit u = new Unit();

    private Unit() {
    }

    public static Unit unit() {
        return u;
    }
}
