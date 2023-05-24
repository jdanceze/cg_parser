package soot.jimple.toolkits.thread.mhp.findobject;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import soot.PointsToAnalysis;
import soot.RefType;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.DefinitionStmt;
import soot.jimple.NewExpr;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.PAG;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.thread.mhp.pegcallgraph.PegCallGraph;
import soot.toolkits.graph.CompleteUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.FlowSet;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/mhp/findobject/AllocNodesFinder.class */
public class AllocNodesFinder {
    private final Set<AllocNode> allocNodes = new HashSet();
    private final Set<AllocNode> multiRunAllocNodes = new HashSet();
    private final Set<SootMethod> multiCalledMethods = new HashSet();
    PAG pag;

    public AllocNodesFinder(PegCallGraph pcg, CallGraph cg, PAG pag) {
        this.pag = pag;
        MultiCalledMethods mcm = new MultiCalledMethods(pcg, this.multiCalledMethods);
        find(mcm.getMultiCalledMethods(), pcg, cg);
    }

    private void find(Set<SootMethod> multiCalledMethods, PegCallGraph pcg, CallGraph callGraph) {
        Set clinitMethods = pcg.getClinitMethods();
        Iterator it = pcg.iterator();
        while (it.hasNext()) {
            SootMethod sm = (SootMethod) it.next();
            UnitGraph graph = new CompleteUnitGraph(sm.getActiveBody());
            Iterator iterator = graph.iterator();
            if (multiCalledMethods.contains(sm)) {
                while (iterator.hasNext()) {
                    Unit unit = iterator.next();
                    if (clinitMethods.contains(sm) && (unit instanceof AssignStmt)) {
                        AllocNode allocNode = this.pag.makeAllocNode(PointsToAnalysis.STRING_NODE, RefType.v("java.lang.String"), null);
                        this.allocNodes.add(allocNode);
                        this.multiRunAllocNodes.add(allocNode);
                    } else if (unit instanceof DefinitionStmt) {
                        Value rightOp = ((DefinitionStmt) unit).getRightOp();
                        if (rightOp instanceof NewExpr) {
                            Type type = ((NewExpr) rightOp).getType();
                            AllocNode allocNode2 = this.pag.makeAllocNode(rightOp, type, sm);
                            this.allocNodes.add(allocNode2);
                            this.multiRunAllocNodes.add(allocNode2);
                        }
                    }
                }
            } else {
                MultiRunStatementsFinder finder = new MultiRunStatementsFinder(graph, sm, multiCalledMethods, callGraph);
                FlowSet fs = finder.getMultiRunStatements();
                while (iterator.hasNext()) {
                    Unit unit2 = iterator.next();
                    if (clinitMethods.contains(sm) && (unit2 instanceof AssignStmt)) {
                        this.allocNodes.add(this.pag.makeAllocNode(PointsToAnalysis.STRING_NODE, RefType.v("java.lang.String"), null));
                    } else if (unit2 instanceof DefinitionStmt) {
                        Value rightOp2 = ((DefinitionStmt) unit2).getRightOp();
                        if (rightOp2 instanceof NewExpr) {
                            Type type2 = ((NewExpr) rightOp2).getType();
                            AllocNode allocNode3 = this.pag.makeAllocNode(rightOp2, type2, sm);
                            this.allocNodes.add(allocNode3);
                            if (fs.contains(unit2)) {
                                this.multiRunAllocNodes.add(allocNode3);
                            }
                        }
                    }
                }
            }
        }
    }

    public Set<AllocNode> getAllocNodes() {
        return this.allocNodes;
    }

    public Set<AllocNode> getMultiRunAllocNodes() {
        return this.multiRunAllocNodes;
    }

    public Set<SootMethod> getMultiCalledMethods() {
        return this.multiCalledMethods;
    }
}
