package heros.flowfunc;

import heros.FlowFunction;
import heros.TwoElementSet;
import java.util.Collections;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/flowfunc/Transfer.class */
public class Transfer<D> implements FlowFunction<D> {
    private final D toValue;
    private final D fromValue;

    public Transfer(D toValue, D fromValue) {
        this.toValue = toValue;
        this.fromValue = fromValue;
    }

    @Override // heros.FlowFunction
    public Set<D> computeTargets(D source) {
        if (source.equals(this.fromValue)) {
            return TwoElementSet.twoElementSet(source, this.toValue);
        }
        if (source.equals(this.toValue)) {
            return Collections.emptySet();
        }
        return Collections.singleton(source);
    }
}
