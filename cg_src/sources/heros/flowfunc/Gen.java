package heros.flowfunc;

import heros.FlowFunction;
import heros.TwoElementSet;
import java.util.Collections;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/flowfunc/Gen.class */
public class Gen<D> implements FlowFunction<D> {
    private final D genValue;
    private final D zeroValue;

    public Gen(D genValue, D zeroValue) {
        this.genValue = genValue;
        this.zeroValue = zeroValue;
    }

    @Override // heros.FlowFunction
    public Set<D> computeTargets(D source) {
        if (source.equals(this.zeroValue)) {
            return TwoElementSet.twoElementSet(source, this.genValue);
        }
        return Collections.singleton(source);
    }
}
