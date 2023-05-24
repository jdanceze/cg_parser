package soot.lambdaMetaFactory;

import java.util.function.Supplier;
/* loaded from: gencallgraphv3.jar:soot/lambdaMetaFactory/Issue1367.class */
public class Issue1367 {
    public Supplier<Object> constructorReference() {
        return Object::new;
    }
}
