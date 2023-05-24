package heros.flowfunc;

import com.google.common.collect.Sets;
import heros.FlowFunction;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/flowfunc/Compose.class */
public class Compose<D> implements FlowFunction<D> {
    private final FlowFunction<D>[] funcs;

    private Compose(FlowFunction<D>... funcs) {
        this.funcs = funcs;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // heros.FlowFunction
    public Set<D> computeTargets(D source) {
        FlowFunction<D>[] flowFunctionArr;
        Set<D> curr = Sets.newHashSet();
        curr.add(source);
        for (FlowFunction<D> func : this.funcs) {
            Set<D> next = Sets.newHashSet();
            for (D d : curr) {
                next.addAll(func.computeTargets(d));
            }
            curr = next;
        }
        return curr;
    }

    public static <D> FlowFunction<D> compose(FlowFunction<D>... funcs) {
        List<FlowFunction<D>> list = new ArrayList<>();
        for (FlowFunction<D> f : funcs) {
            if (f != Identity.v()) {
                list.add(f);
            }
        }
        return list.size() == 1 ? list.get(0) : list.isEmpty() ? Identity.v() : new Compose((FlowFunction[]) list.toArray(new FlowFunction[list.size()]));
    }
}
