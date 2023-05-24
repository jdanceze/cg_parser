package soot.lambdaMetaFactory;

import java.util.function.Supplier;
/* loaded from: gencallgraphv3.jar:soot/lambdaMetaFactory/PublicMethodRef.class */
public class PublicMethodRef {
    public void main() {
        Supplier<Integer> supplier = this::publicMethod;
        System.out.println(supplier.get());
    }

    public int publicMethod() {
        return 5;
    }
}
