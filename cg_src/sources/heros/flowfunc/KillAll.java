package heros.flowfunc;

import heros.FlowFunction;
import java.util.Collections;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/flowfunc/KillAll.class */
public class KillAll<D> implements FlowFunction<D> {
    private static final KillAll instance = new KillAll();

    private KillAll() {
    }

    @Override // heros.FlowFunction
    public Set<D> computeTargets(D source) {
        return Collections.emptySet();
    }

    public static <D> KillAll<D> v() {
        return instance;
    }
}
