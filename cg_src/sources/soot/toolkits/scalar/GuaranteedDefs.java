package soot.toolkits.scalar;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Unit;
import soot.Value;
import soot.options.Options;
import soot.toolkits.graph.UnitGraph;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/GuaranteedDefs.class */
public class GuaranteedDefs {
    private static final Logger logger = LoggerFactory.getLogger(GuaranteedDefs.class);
    protected final Map<Unit, List<Value>> unitToGuaranteedDefs;

    public GuaranteedDefs(UnitGraph graph) {
        if (Options.v().verbose()) {
            logger.debug("[" + graph.getBody().getMethod().getName() + "]     Constructing GuaranteedDefs...");
        }
        this.unitToGuaranteedDefs = new HashMap((graph.size() * 2) + 1, 0.7f);
        GuaranteedDefsAnalysis analysis = new GuaranteedDefsAnalysis(graph);
        Iterator<Unit> it = graph.iterator();
        while (it.hasNext()) {
            Unit s = it.next();
            FlowSet<Value> set = analysis.getFlowBefore(s);
            this.unitToGuaranteedDefs.put(s, Collections.unmodifiableList(set.toList()));
        }
    }

    public List<Value> getGuaranteedDefs(Unit s) {
        return this.unitToGuaranteedDefs.get(s);
    }
}
