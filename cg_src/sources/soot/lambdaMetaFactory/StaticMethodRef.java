package soot.lambdaMetaFactory;

import java.util.function.Supplier;
/* loaded from: gencallgraphv3.jar:soot/lambdaMetaFactory/StaticMethodRef.class */
public class StaticMethodRef {
    static int staticMethod() {
        return 5;
    }

    public void main() {
        Supplier<Integer> supplier = StaticMethodRef::staticMethod;
        System.out.println(supplier.get());
    }
}
