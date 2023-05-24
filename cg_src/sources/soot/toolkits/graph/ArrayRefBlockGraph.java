package soot.toolkits.graph;

import java.util.Iterator;
import java.util.Set;
import soot.Body;
import soot.Unit;
import soot.baf.Inst;
import soot.jimple.Stmt;
import soot.util.PhaseDumper;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/ArrayRefBlockGraph.class */
public class ArrayRefBlockGraph extends BlockGraph {
    public ArrayRefBlockGraph(Body body) {
        this(new BriefUnitGraph(body));
    }

    public ArrayRefBlockGraph(BriefUnitGraph unitGraph) {
        super(unitGraph);
        PhaseDumper.v().dumpGraph(this, this.mBody);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.graph.BlockGraph
    public Set<Unit> computeLeaders(UnitGraph unitGraph) {
        Body body = unitGraph.getBody();
        if (body != this.mBody) {
            throw new RuntimeException("ArrayRefBlockGraph.computeLeaders() called with a UnitGraph that doesn't match its mBody.");
        }
        Set<Unit> leaders = super.computeLeaders(unitGraph);
        Iterator<Unit> it = body.getUnits().iterator();
        while (it.hasNext()) {
            Unit unit = it.next();
            if (((unit instanceof Stmt) && ((Stmt) unit).containsArrayRef()) || ((unit instanceof Inst) && ((Inst) unit).containsArrayRef())) {
                leaders.add(unit);
            }
        }
        return leaders;
    }
}
