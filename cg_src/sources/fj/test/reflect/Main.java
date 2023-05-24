package fj.test.reflect;

import fj.P2;
import fj.data.Array;
import fj.function.Effect1;
import fj.test.CheckResult;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/test/reflect/Main.class */
public final class Main {
    private Main() {
        throw new UnsupportedOperationException();
    }

    public static void main(String... args) {
        if (args.length == 0) {
            System.err.println("<class> [category]*");
            System.exit(441);
            return;
        }
        try {
            Check.check(Class.forName(args[0]), Array.array(args).toList().tail()).foreachDoEffect(new Effect1<P2<String, CheckResult>>() { // from class: fj.test.reflect.Main.1
                @Override // fj.function.Effect1
                public void f(P2<String, CheckResult> r) {
                    CheckResult.summary.print(r._2());
                    System.out.println(" (" + r._1() + ')');
                }
            });
        } catch (ClassNotFoundException e) {
            System.err.println(e);
            System.exit(144);
        }
    }
}
