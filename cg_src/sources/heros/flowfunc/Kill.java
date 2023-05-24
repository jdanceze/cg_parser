package heros.flowfunc;

import heros.FlowFunction;
import java.util.Collections;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/flowfunc/Kill.class */
public class Kill<D> implements FlowFunction<D> {
    private final D killValue;

    public Kill(D killValue) {
        this.killValue = killValue;
    }

    @Override // heros.FlowFunction
    public Set<D> computeTargets(D source) {
        if (source.equals(this.killValue)) {
            return Collections.emptySet();
        }
        return Collections.singleton(source);
    }
}
