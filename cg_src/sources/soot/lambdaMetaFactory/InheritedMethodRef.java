package soot.lambdaMetaFactory;

import java.util.function.Supplier;
/* loaded from: gencallgraphv3.jar:soot/lambdaMetaFactory/InheritedMethodRef.class */
public class InheritedMethodRef extends Super {
    public void main() {
        Supplier<Integer> supplier = () -> {
            return super.superMethod();
        };
        System.out.println(supplier.get());
    }
}
