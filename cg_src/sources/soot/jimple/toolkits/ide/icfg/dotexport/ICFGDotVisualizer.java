package soot.jimple.toolkits.ide.icfg.dotexport;

import heros.InterproceduralCFG;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.SootMethod;
import soot.Unit;
import soot.util.dot.DotGraph;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/ide/icfg/dotexport/ICFGDotVisualizer.class */
public class ICFGDotVisualizer {
    private static final Logger logger = LoggerFactory.getLogger(ICFGDotVisualizer.class);
    private DotGraph dotIcfg = new DotGraph("");
    private ArrayList<Unit> visited = new ArrayList<>();
    String fileName;
    Unit startPoint;
    InterproceduralCFG<Unit, SootMethod> icfg;

    public ICFGDotVisualizer(String fileName, Unit startPoint, InterproceduralCFG<Unit, SootMethod> icfg) {
        this.fileName = fileName;
        this.startPoint = startPoint;
        this.icfg = icfg;
        if (this.fileName == null || this.fileName == "") {
            System.out.println("Please provide a vaid filename");
        }
        if (this.startPoint == null) {
            System.out.println("startPoint is null!");
        }
        if (this.icfg == null) {
            System.out.println("ICFG is null!");
        }
    }

    public void exportToDot() {
        if (this.startPoint != null && this.icfg != null && this.fileName != null) {
            graphTraverse(this.startPoint, this.icfg);
            this.dotIcfg.plot(this.fileName);
            logger.debug(this.fileName + DotGraph.DOT_EXTENSION);
            return;
        }
        System.out.println("Parameters not properly initialized!");
    }

    private void graphTraverse(Unit startPoint, InterproceduralCFG<Unit, SootMethod> icfg) {
        List<Unit> currentSuccessors = icfg.getSuccsOf(startPoint);
        if (currentSuccessors.size() == 0) {
            System.out.println("Traversal complete");
            return;
        }
        for (Unit succ : currentSuccessors) {
            System.out.println("Succesor: " + succ.toString());
            if (!this.visited.contains(succ)) {
                this.dotIcfg.drawEdge(startPoint.toString(), succ.toString());
                this.visited.add(succ);
                graphTraverse(succ, icfg);
            } else {
                this.dotIcfg.drawEdge(startPoint.toString(), succ.toString());
            }
        }
    }
}
