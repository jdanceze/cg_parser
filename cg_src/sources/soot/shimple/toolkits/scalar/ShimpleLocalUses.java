package soot.shimple.toolkits.scalar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.shimple.ShimpleBody;
import soot.toolkits.scalar.LocalUses;
import soot.toolkits.scalar.UnitValueBoxPair;
/* loaded from: gencallgraphv3.jar:soot/shimple/toolkits/scalar/ShimpleLocalUses.class */
public class ShimpleLocalUses implements LocalUses {
    private static final Logger logger = LoggerFactory.getLogger(ShimpleLocalUses.class);
    protected Map<Local, List<UnitValueBoxPair>> localToUses = new HashMap();

    public ShimpleLocalUses(ShimpleBody sb) {
        if (!sb.isSSA()) {
            throw new RuntimeException("ShimpleBody is not in proper SSA form as required by ShimpleLocalUses. You may need to rebuild it or use SimpleLocalUses instead.");
        }
        Map<Local, List<UnitValueBoxPair>> localToUsesRef = this.localToUses;
        for (Local local : sb.getLocals()) {
            localToUsesRef.put(local, new ArrayList());
        }
        Iterator<Unit> it = sb.getUnits().iterator();
        while (it.hasNext()) {
            Unit unit = it.next();
            for (ValueBox box : unit.getUseBoxes()) {
                Value value = box.getValue();
                if (value instanceof Local) {
                    localToUsesRef.get((Local) value).add(new UnitValueBoxPair(unit, box));
                }
            }
        }
    }

    public List<UnitValueBoxPair> getUsesOf(Local local) {
        List<UnitValueBoxPair> uses = this.localToUses.get(local);
        return uses != null ? uses : Collections.emptyList();
    }

    @Override // soot.toolkits.scalar.LocalUses
    public List<UnitValueBoxPair> getUsesOf(Unit unit) {
        List<ValueBox> defBoxes = unit.getDefBoxes();
        switch (defBoxes.size()) {
            case 0:
                return Collections.emptyList();
            case 1:
                Value val = defBoxes.get(0).getValue();
                if (val instanceof Local) {
                    return getUsesOf((Local) val);
                }
                return Collections.emptyList();
            default:
                logger.warn("Unit has multiple definition boxes?");
                List<UnitValueBoxPair> usesList = new ArrayList<>();
                for (ValueBox next : defBoxes) {
                    Value def = next.getValue();
                    if (def instanceof Local) {
                        usesList.addAll(getUsesOf((Local) def));
                    }
                }
                return usesList;
        }
    }
}
