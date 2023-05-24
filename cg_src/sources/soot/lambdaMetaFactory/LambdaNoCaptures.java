package soot.lambdaMetaFactory;

import java.util.function.Function;
/* loaded from: gencallgraphv3.jar:soot/lambdaMetaFactory/LambdaNoCaptures.class */
class LambdaNoCaptures {
    LambdaNoCaptures() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void staticCallee(Integer i) {
        System.out.println(i);
    }

    public void main() {
        Function<Integer, String> intToString = i -> {
            staticCallee(i);
            return String.valueOf(i);
        };
        String res = intToString.apply(2);
        System.out.println(res);
    }
}
