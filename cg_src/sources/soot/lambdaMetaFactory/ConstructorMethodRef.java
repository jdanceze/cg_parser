package soot.lambdaMetaFactory;

import java.util.function.Supplier;
/* loaded from: gencallgraphv3.jar:soot/lambdaMetaFactory/ConstructorMethodRef.class */
public class ConstructorMethodRef {
    public void main() {
        Supplier<ConstructorMethodRef> supplier = ConstructorMethodRef::new;
        System.out.println(supplier.get());
    }
}
