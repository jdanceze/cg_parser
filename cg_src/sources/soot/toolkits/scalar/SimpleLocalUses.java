package soot.toolkits.scalar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.Local;
import soot.Timers;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.options.Options;
import soot.toolkits.graph.UnitGraph;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/SimpleLocalUses.class */
public class SimpleLocalUses implements LocalUses {
    private static final Logger logger = LoggerFactory.getLogger(SimpleLocalUses.class);
    final Body body;
    private final Map<Unit, List<UnitValueBoxPair>> unitToUses;

    public SimpleLocalUses(UnitGraph graph, LocalDefs localDefs) {
        this(graph.getBody(), localDefs);
    }

    public SimpleLocalUses(Body body, LocalDefs localDefs) {
        this.body = body;
        this.unitToUses = new HashMap((body.getUnits().size() * 2) + 1, 0.7f);
        Options options = Options.v();
        if (options.verbose()) {
            logger.debug("[" + body.getMethod().getName() + "]     Constructing SimpleLocalUses...");
        }
        if (options.time()) {
            Timers.v().usesTimer.start();
        }
        Iterator<Unit> it = body.getUnits().iterator();
        while (it.hasNext()) {
            Unit unit = it.next();
            for (ValueBox useBox : unit.getUseBoxes()) {
                Value v = useBox.getValue();
                if (v instanceof Local) {
                    Local l = (Local) v;
                    List<Unit> defs = localDefs.getDefsOfAt(l, unit);
                    if (defs != null) {
                        UnitValueBoxPair newPair = new UnitValueBoxPair(unit, useBox);
                        for (Unit def : defs) {
                            List<UnitValueBoxPair> lst = this.unitToUses.get(def);
                            if (lst == null) {
                                Map<Unit, List<UnitValueBoxPair>> map = this.unitToUses;
                                List<UnitValueBoxPair> arrayList = new ArrayList<>();
                                lst = arrayList;
                                map.put(def, arrayList);
                            }
                            lst.add(newPair);
                        }
                    }
                }
            }
        }
        if (options.time()) {
            Timers.v().usesTimer.end();
        }
        if (options.verbose()) {
            logger.debug("[" + body.getMethod().getName() + "]     finished SimpleLocalUses...");
        }
    }

    @Override // soot.toolkits.scalar.LocalUses
    public List<UnitValueBoxPair> getUsesOf(Unit s) {
        List<UnitValueBoxPair> l = this.unitToUses.get(s);
        return l == null ? Collections.emptyList() : Collections.unmodifiableList(l);
    }

    public Set<Local> getUsedVariables() {
        Set<Local> res = new HashSet<>();
        for (List<UnitValueBoxPair> vals : this.unitToUses.values()) {
            for (UnitValueBoxPair val : vals) {
                res.add((Local) val.valueBox.getValue());
            }
        }
        return res;
    }

    public Set<Local> getUnusedVariables() {
        Set<Local> res = new HashSet<>(this.body.getLocals());
        res.retainAll(getUsedVariables());
        return res;
    }
}
