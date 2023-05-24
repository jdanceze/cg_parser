package soot.toolkits.graph.pdg;

import java.util.List;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.MutableEdgeLabelledDirectedGraph;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/pdg/ProgramDependenceGraph.class */
public interface ProgramDependenceGraph extends MutableEdgeLabelledDirectedGraph<PDGNode, String> {
    List<Region> getWeakRegions();

    List<Region> getStrongRegions();

    List<PDGRegion> getPDGRegions();

    IRegion GetStartRegion();

    BlockGraph getBlockGraph();

    PDGNode GetStartNode();

    boolean dependentOn(PDGNode pDGNode, PDGNode pDGNode2);

    List<PDGNode> getDependents(PDGNode pDGNode);

    PDGNode getPDGNode(Object obj);

    String toString();
}
