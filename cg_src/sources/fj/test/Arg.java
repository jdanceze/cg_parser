package fj.test;

import fj.F;
import fj.Show;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/Arg.class */
public final class Arg<T> {
    private final T value;
    private final int shrinks;
    public static final Show<Arg<?>> argShow = Show.showS((F) new F<Arg<?>, String>() { // from class: fj.test.Arg.1
        @Override // fj.F
        public String f(Arg<?> arg) {
            String str;
            StringBuilder append = new StringBuilder().append(Show.anyShow().showS((Show) ((Arg) arg).value));
            if (((Arg) arg).shrinks > 0) {
                str = " (" + ((Arg) arg).shrinks + " shrink" + (((Arg) arg).shrinks == 1 ? "" : 's') + ')';
            } else {
                str = "";
            }
            return append.append(str).toString();
        }
    });

    private Arg(T value, int shrinks) {
        this.value = value;
        this.shrinks = shrinks;
    }

    public static <T> Arg<T> arg(T value, int shrinks) {
        return new Arg<>(value, shrinks);
    }

    public Object value() {
        return this.value;
    }

    public int shrinks() {
        return this.shrinks;
    }
}
