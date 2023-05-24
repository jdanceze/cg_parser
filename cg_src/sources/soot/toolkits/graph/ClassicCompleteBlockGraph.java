package soot.toolkits.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.Unit;
import soot.util.PhaseDumper;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/ClassicCompleteBlockGraph.class */
public class ClassicCompleteBlockGraph extends BlockGraph {
    public ClassicCompleteBlockGraph(Body body) {
        super(new ClassicCompleteUnitGraph(body));
    }

    public ClassicCompleteBlockGraph(ClassicCompleteUnitGraph unitGraph) {
        super(unitGraph);
        Unit entryPoint = getBody().getUnits().getFirst();
        List<Block> newHeads = new ArrayList<>(1);
        Iterator<Block> it = getBlocks().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Block b = it.next();
            if (b.getHead() == entryPoint) {
                newHeads.add(b);
                break;
            }
        }
        this.mHeads = Collections.unmodifiableList(newHeads);
        this.mTails = Collections.emptyList();
        PhaseDumper.v().dumpGraph(this, this.mBody);
    }
}
