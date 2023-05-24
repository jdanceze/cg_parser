package heros.flowfunc;

import heros.FlowFunction;
import java.util.Collections;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/flowfunc/Identity.class */
public class Identity<D> implements FlowFunction<D> {
    private static final Identity instance = new Identity();

    private Identity() {
    }

    @Override // heros.FlowFunction
    public Set<D> computeTargets(D source) {
        return Collections.singleton(source);
    }

    public static <D> Identity<D> v() {
        return instance;
    }
}
