package heros;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/DefaultSeeds.class */
public class DefaultSeeds {
    public static <N, D> Map<N, Set<D>> make(Iterable<N> units, D zeroNode) {
        Map<N, Set<D>> res = new HashMap<>();
        for (N n : units) {
            res.put(n, Collections.singleton(zeroNode));
        }
        return res;
    }
}
