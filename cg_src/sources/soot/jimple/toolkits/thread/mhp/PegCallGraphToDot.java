package soot.jimple.toolkits.thread.mhp;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.apache.tools.ant.taskdefs.optional.j2ee.HotDeploymentTool;
import soot.toolkits.graph.DirectedGraph;
import soot.util.dot.DotGraph;
import soot.util.dot.DotGraphConstants;
import soot.util.dot.DotGraphNode;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/mhp/PegCallGraphToDot.class */
public class PegCallGraphToDot {
    public static boolean isBrief = false;
    private static final Map<Object, String> listNodeName = new HashMap();
    public static boolean onepage = true;
    private static int nodecount = 0;

    public PegCallGraphToDot(DirectedGraph graph, boolean onepage2, String name) {
        onepage = onepage2;
        toDotFile(name, graph, "PegCallGraph");
    }

    public static void toDotFile(String methodname, DirectedGraph graph, String graphname) {
        String nodeName;
        String makeNodeName;
        int sequence = 0;
        nodecount = 0;
        Hashtable nodeindex = new Hashtable(graph.size());
        DotGraph canvas = new DotGraph(methodname);
        if (!onepage) {
            canvas.setPageSize(8.5d, 11.0d);
        }
        canvas.setNodeShape(DotGraphConstants.NODE_SHAPE_BOX);
        canvas.setGraphLabel(graphname);
        for (Object node : graph) {
            if (node instanceof List) {
                int i = sequence;
                sequence++;
                String listName = HotDeploymentTool.ACTION_LIST + new Integer(i).toString();
                makeNodeName(getNodeOrder(nodeindex, listName));
                listNodeName.put(node, listName);
            }
        }
        for (Object node2 : graph) {
            if (node2 instanceof List) {
                nodeName = makeNodeName(getNodeOrder(nodeindex, listNodeName.get(node2)));
            } else {
                nodeName = makeNodeName(getNodeOrder(nodeindex, node2));
            }
            for (Object s : graph.getSuccsOf(node2)) {
                if (s instanceof List) {
                    makeNodeName = makeNodeName(getNodeOrder(nodeindex, listNodeName.get(s)));
                } else {
                    makeNodeName = makeNodeName(getNodeOrder(nodeindex, s));
                }
                String succName = makeNodeName;
                canvas.drawEdge(nodeName, succName);
            }
        }
        if (!isBrief) {
            for (Object node3 : nodeindex.keySet()) {
                if (node3 != null) {
                    String nodename = makeNodeName(getNodeOrder(nodeindex, node3));
                    DotGraphNode dotnode = canvas.getNode(nodename);
                    if (dotnode != null) {
                        dotnode.setLabel(node3.toString());
                    }
                }
            }
        }
        canvas.plot("pecg.dot");
        listNodeName.clear();
    }

    private static int getNodeOrder(Hashtable<Object, Integer> nodeindex, Object node) {
        Integer index = nodeindex.get(node);
        if (index == null) {
            int i = nodecount;
            nodecount = i + 1;
            index = new Integer(i);
            nodeindex.put(node, index);
        }
        return index.intValue();
    }

    private static String makeNodeName(int index) {
        return "N" + index;
    }
}
