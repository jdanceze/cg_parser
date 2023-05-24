package soot.toolkits.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.options.Options;
import soot.util.StationaryArrayList;
@Deprecated
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/StronglyConnectedComponents.class */
public class StronglyConnectedComponents {
    private HashMap<Object, Object> nodeToColor;
    private List<List> componentList;
    private final int[] indexStack;
    private final Object[] nodeStack;
    private int last;
    private static final Logger logger = LoggerFactory.getLogger(StronglyConnectedComponents.class);
    private static final Object Visited = new Object();
    private static final Object Black = new Object();
    private final HashMap<Object, List<Object>> nodeToComponent = new HashMap<>();
    MutableDirectedGraph sccGraph = new HashMutableDirectedGraph();
    private final LinkedList<Object> finishingOrder = new LinkedList<>();

    public StronglyConnectedComponents(DirectedGraph g) {
        this.componentList = new ArrayList();
        this.nodeToColor = new HashMap<>((3 * g.size()) / 2, 0.7f);
        this.indexStack = new int[g.size()];
        this.nodeStack = new Object[g.size()];
        for (Object s : g) {
            if (this.nodeToColor.get(s) == null) {
                visitNode(g, s);
            }
        }
        this.nodeToColor = new HashMap<>(3 * g.size(), 0.7f);
        Iterator<Object> revNodeIt = this.finishingOrder.iterator();
        while (revNodeIt.hasNext()) {
            Object s2 = revNodeIt.next();
            if (this.nodeToColor.get(s2) == null) {
                StationaryArrayList stationaryArrayList = new StationaryArrayList();
                this.nodeToComponent.put(s2, stationaryArrayList);
                this.sccGraph.addNode(stationaryArrayList);
                this.componentList.add(stationaryArrayList);
                visitRevNode(g, s2, stationaryArrayList);
            }
        }
        this.componentList = Collections.unmodifiableList(this.componentList);
        if (Options.v().verbose()) {
            logger.debug("Done computing scc components");
            logger.debug("number of nodes in underlying graph: " + g.size());
            logger.debug("number of components: " + this.sccGraph.size());
        }
    }

    private void visitNode(DirectedGraph graph, Object startNode) {
        this.last = 0;
        this.nodeToColor.put(startNode, Visited);
        this.nodeStack[this.last] = startNode;
        int[] iArr = this.indexStack;
        int i = this.last;
        this.last = i + 1;
        iArr[i] = -1;
        while (this.last > 0) {
            int[] iArr2 = this.indexStack;
            int i2 = this.last - 1;
            int toVisitIndex = iArr2[i2] + 1;
            iArr2[i2] = toVisitIndex;
            Object toVisitNode = this.nodeStack[this.last - 1];
            if (toVisitIndex >= graph.getSuccsOf(toVisitNode).size()) {
                this.finishingOrder.addFirst(toVisitNode);
                this.last--;
            } else {
                Object childNode = graph.getSuccsOf(toVisitNode).get(toVisitIndex);
                if (this.nodeToColor.get(childNode) == null) {
                    this.nodeToColor.put(childNode, Visited);
                    this.nodeStack[this.last] = childNode;
                    int[] iArr3 = this.indexStack;
                    int i3 = this.last;
                    this.last = i3 + 1;
                    iArr3[i3] = -1;
                }
            }
        }
    }

    private void visitRevNode(DirectedGraph graph, Object startNode, List<Object> currentComponent) {
        this.last = 0;
        this.nodeToColor.put(startNode, Visited);
        this.nodeStack[this.last] = startNode;
        int[] iArr = this.indexStack;
        int i = this.last;
        this.last = i + 1;
        iArr[i] = -1;
        while (this.last > 0) {
            int[] iArr2 = this.indexStack;
            int i2 = this.last - 1;
            int toVisitIndex = iArr2[i2] + 1;
            iArr2[i2] = toVisitIndex;
            Object toVisitNode = this.nodeStack[this.last - 1];
            if (toVisitIndex >= graph.getPredsOf(toVisitNode).size()) {
                currentComponent.add(toVisitNode);
                this.nodeToComponent.put(toVisitNode, currentComponent);
                this.nodeToColor.put(toVisitNode, Black);
                this.last--;
            } else {
                Object childNode = graph.getPredsOf(toVisitNode).get(toVisitIndex);
                if (this.nodeToColor.get(childNode) == null) {
                    this.nodeToColor.put(childNode, Visited);
                    this.nodeStack[this.last] = childNode;
                    int[] iArr3 = this.indexStack;
                    int i3 = this.last;
                    this.last = i3 + 1;
                    iArr3[i3] = -1;
                } else if (this.nodeToColor.get(childNode) == Black && this.nodeToComponent.get(childNode) != currentComponent) {
                    this.sccGraph.addEdge(this.nodeToComponent.get(childNode), currentComponent);
                }
            }
        }
    }

    public boolean equivalent(Object a, Object b) {
        return this.nodeToComponent.get(a) == this.nodeToComponent.get(b);
    }

    public List<List> getComponents() {
        return this.componentList;
    }

    public List getComponentOf(Object a) {
        return this.nodeToComponent.get(a);
    }

    public DirectedGraph getSuperGraph() {
        return this.sccGraph;
    }
}
