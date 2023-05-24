package fj.test;

import fj.P1;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Bool.class */
public final class Bool {
    private final boolean b;
    private static final Bool t = new Bool(true);
    private static final Bool f = new Bool(false);

    private Bool(boolean b) {
        this.b = b;
    }

    public boolean is() {
        return this.b;
    }

    public boolean isNot() {
        return !this.b;
    }

    public Property implies(P1<Property> p) {
        return Property.implies(this.b, p);
    }

    public Property implies(final Property p) {
        return Property.implies(this.b, new P1<Property>() { // from class: fj.test.Bool.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // fj.P1
            public Property _1() {
                return p;
            }
        });
    }

    public Property implies(Bool c) {
        return implies(Property.prop(c.b));
    }

    public Property implies(final boolean c) {
        return Property.implies(this.b, new P1<Property>() { // from class: fj.test.Bool.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // fj.P1
            public Property _1() {
                return Property.prop(c);
            }
        });
    }

    public static Bool bool(boolean b) {
        return b ? t : f;
    }
}
