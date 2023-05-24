package heros.flowfunc;

import com.google.common.collect.Sets;
import heros.FlowFunction;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/flowfunc/Union.class */
public class Union<D> implements FlowFunction<D> {
    private final FlowFunction<D>[] funcs;

    private Union(FlowFunction<D>... funcs) {
        this.funcs = funcs;
    }

    @Override // heros.FlowFunction
    public Set<D> computeTargets(D source) {
        FlowFunction<D>[] flowFunctionArr;
        Set<D> res = Sets.newHashSet();
        for (FlowFunction<D> func : this.funcs) {
            res.addAll(func.computeTargets(source));
        }
        return res;
    }

    public static <D> FlowFunction<D> union(FlowFunction<D>... funcs) {
        List<FlowFunction<D>> list = new ArrayList<>();
        for (FlowFunction<D> f : funcs) {
            if (f != Identity.v()) {
                list.add(f);
            }
        }
        return list.size() == 1 ? list.get(0) : list.isEmpty() ? Identity.v() : new Union((FlowFunction[]) list.toArray(new FlowFunction[list.size()]));
    }
}
