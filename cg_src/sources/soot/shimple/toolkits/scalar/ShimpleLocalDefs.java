package soot.shimple.toolkits.scalar;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.shimple.ShimpleBody;
import soot.toolkits.scalar.LocalDefs;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/shimple/toolkits/scalar/ShimpleLocalDefs.class */
public class ShimpleLocalDefs implements LocalDefs {
    protected Map<Value, List<Unit>> localToDefs;

    public ShimpleLocalDefs(ShimpleBody sb) {
        if (!sb.isSSA()) {
            throw new RuntimeException("ShimpleBody is not in proper SSA form as required by ShimpleLocalDefs. You may need to rebuild it or use SimpleLocalDefs instead.");
        }
        Chain<Unit> unitsChain = sb.getUnits();
        this.localToDefs = new HashMap((unitsChain.size() * 2) + 1, 0.7f);
        for (Unit unit : unitsChain) {
            for (ValueBox vb : unit.getDefBoxes()) {
                Value value = vb.getValue();
                if (value instanceof Local) {
                    this.localToDefs.put(value, Collections.singletonList(unit));
                }
            }
        }
    }

    @Override // soot.toolkits.scalar.LocalDefs
    public List<Unit> getDefsOf(Local l) {
        List<Unit> defs = this.localToDefs.get(l);
        if (defs == null) {
            throw new RuntimeException("Local not found in Body.");
        }
        return defs;
    }

    @Override // soot.toolkits.scalar.LocalDefs
    public List<Unit> getDefsOfAt(Local l, Unit s) {
        boolean defined = false;
        Iterator<ValueBox> it = s.getUseBoxes().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            ValueBox vb = it.next();
            if (vb.getValue().equals(l)) {
                defined = true;
                break;
            }
        }
        if (!defined) {
            throw new RuntimeException("Illegal LocalDefs query; local " + l + " is not being used at " + s);
        }
        return getDefsOf(l);
    }
}
