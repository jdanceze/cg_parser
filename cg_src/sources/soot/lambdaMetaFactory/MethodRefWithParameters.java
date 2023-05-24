package soot.lambdaMetaFactory;

import java.util.function.BiFunction;
/* loaded from: gencallgraphv3.jar:soot/lambdaMetaFactory/MethodRefWithParameters.class */
public class MethodRefWithParameters {
    public static int staticWithCaptures(int a, Integer b) {
        return a + b.intValue();
    }

    public void main() {
        BiFunction<Integer, Integer, Integer> fun = (v0, v1) -> {
            return staticWithCaptures(v0, v1);
        };
        System.out.println(fun.apply(1, 2));
    }
}
