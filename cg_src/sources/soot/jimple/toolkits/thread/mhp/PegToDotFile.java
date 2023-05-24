package soot.jimple.toolkits.thread.mhp;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.tools.ant.taskdefs.optional.j2ee.HotDeploymentTool;
import soot.coffi.Instruction;
import soot.jimple.toolkits.thread.mhp.stmt.JPegStmt;
import soot.tagkit.Tag;
import soot.util.Chain;
import soot.util.dot.DotGraph;
import soot.util.dot.DotGraphConstants;
import soot.util.dot.DotGraphEdge;
import soot.util.dot.DotGraphNode;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/mhp/PegToDotFile.class */
public class PegToDotFile {
    public static final int UNITGRAPH = 0;
    public static final int BLOCKGRAPH = 1;
    public static final int ARRAYBLOCK = 2;
    public static int graphtype = 0;
    public static boolean isBrief = false;
    private static final Map<Object, String> listNodeName = new HashMap();
    private static final Map<Object, String> startNodeToName = new HashMap();
    public static boolean onepage = true;
    private static int nodecount = 0;

    public PegToDotFile(PegGraph graph, boolean onepage2, String name) {
        onepage = onepage2;
        toDotFile(name, graph, "PEG graph");
    }

    public static void toDotFile(String methodname, PegGraph graph, String graphname) {
        String nodeName;
        String makeNodeName;
        String nodeName2;
        String makeNodeName2;
        int sequence = 0;
        nodecount = 0;
        Hashtable nodeindex = new Hashtable(graph.size());
        DotGraph canvas = new DotGraph(methodname);
        if (!onepage) {
            canvas.setPageSize(8.5d, 11.0d);
        }
        canvas.setNodeShape(DotGraphConstants.NODE_SHAPE_BOX);
        canvas.setGraphLabel(graphname);
        Iterator nodesIt = graph.iterator();
        while (nodesIt.hasNext()) {
            Object node = nodesIt.next();
            if (node instanceof List) {
                int i = sequence;
                sequence++;
                String listName = HotDeploymentTool.ACTION_LIST + new Integer(i).toString();
                makeNodeName(getNodeOrder(nodeindex, listName));
                listNodeName.put(node, listName);
            }
        }
        Iterator nodesIt2 = graph.mainIterator();
        while (nodesIt2.hasNext()) {
            Object node2 = nodesIt2.next();
            if (node2 instanceof List) {
                nodeName2 = makeNodeName(getNodeOrder(nodeindex, listNodeName.get(node2)));
            } else {
                Tag tag = ((JPegStmt) node2).getTags().get(0);
                nodeName2 = makeNodeName(getNodeOrder(nodeindex, tag + Instruction.argsep + node2));
                if (((JPegStmt) node2).getName().equals("start")) {
                    startNodeToName.put(node2, nodeName2);
                }
            }
            for (Object s : graph.getSuccsOf(node2)) {
                if (s instanceof List) {
                    makeNodeName2 = makeNodeName(getNodeOrder(nodeindex, listNodeName.get(s)));
                } else {
                    JPegStmt succ = (JPegStmt) s;
                    Tag succTag = succ.getTags().get(0);
                    makeNodeName2 = makeNodeName(getNodeOrder(nodeindex, succTag + Instruction.argsep + succ));
                }
                String succName = makeNodeName2;
                canvas.drawEdge(nodeName2, succName);
            }
        }
        System.out.println("Drew main chain");
        System.out.println("while printing, startToThread has size " + graph.getStartToThread().size());
        Set maps = graph.getStartToThread().entrySet();
        System.out.println("maps has size " + maps.size());
        for (Map.Entry<JPegStmt, List> entry : maps) {
            Object startNode = entry.getKey();
            System.out.println("startNode is: " + startNode);
            String startNodeName = startNodeToName.get(startNode);
            System.out.println("startNodeName is: " + startNodeName);
            List<Chain> runMethodChainList = entry.getValue();
            for (Chain chain : runMethodChainList) {
                boolean firstNode = false;
                for (Object node3 : chain) {
                    if (node3 instanceof List) {
                        nodeName = makeNodeName(getNodeOrder(nodeindex, listNodeName.get(node3)));
                        System.out.println("Didn't draw list node");
                    } else {
                        if (((JPegStmt) node3).getName().equals("begin")) {
                            firstNode = true;
                        }
                        Tag tag2 = ((JPegStmt) node3).getTags().get(0);
                        nodeName = makeNodeName(getNodeOrder(nodeindex, tag2 + Instruction.argsep + node3));
                        if (((JPegStmt) node3).getName().equals("start")) {
                            startNodeToName.put(node3, nodeName);
                        }
                        if (firstNode) {
                            if (startNodeName == null) {
                                System.out.println("00000000startNodeName is null ");
                            }
                            if (nodeName == null) {
                                System.out.println("00000000nodeName is null ");
                            }
                            DotGraphEdge startThreadEdge = canvas.drawEdge(startNodeName, nodeName);
                            startThreadEdge.setStyle("dotted");
                            firstNode = false;
                        }
                    }
                    for (Object succ2 : graph.getSuccsOf(node3)) {
                        if (succ2 instanceof List) {
                            makeNodeName = makeNodeName(getNodeOrder(nodeindex, listNodeName.get(succ2)));
                        } else {
                            JPegStmt succStmt = (JPegStmt) succ2;
                            Tag succTag2 = succStmt.getTags().get(0);
                            makeNodeName = makeNodeName(getNodeOrder(nodeindex, succTag2 + Instruction.argsep + succStmt));
                        }
                        String threadNodeName = makeNodeName;
                        canvas.drawEdge(nodeName, threadNodeName);
                    }
                }
            }
        }
        if (!isBrief) {
            for (Object node4 : nodeindex.keySet()) {
                String nodename = makeNodeName(getNodeOrder(nodeindex, node4));
                DotGraphNode dotnode = canvas.getNode(nodename);
                dotnode.setLabel(node4.toString());
            }
        }
        canvas.plot("peg.dot");
        listNodeName.clear();
        startNodeToName.clear();
    }

    private static int getNodeOrder(Hashtable<Object, Integer> nodeindex, Object node) {
        if (node == null) {
            System.out.println("----node is null-----");
            return 0;
        }
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
