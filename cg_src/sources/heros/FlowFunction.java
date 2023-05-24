package heros;

import java.util.Set;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/FlowFunction.class */
public interface FlowFunction<D> {
    Set<D> computeTargets(D d);
}
