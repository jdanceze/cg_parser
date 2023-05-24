package soot.lambdaMetaFactory;

import java.util.function.Supplier;
/* loaded from: gencallgraphv3.jar:soot/lambdaMetaFactory/PrivateMethodRef.class */
public class PrivateMethodRef {
    public void main() {
        Supplier<Integer> supplier = this::privateMethod;
        System.out.println(supplier.get());
    }

    private int privateMethod() {
        return 5;
    }
}
