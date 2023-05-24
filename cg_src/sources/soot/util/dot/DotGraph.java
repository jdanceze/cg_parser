package soot.util.dot;

import android.provider.MediaStore;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* loaded from: gencallgraphv3.jar:soot/util/dot/DotGraph.class */
public class DotGraph extends AbstractDotGraphElement implements Renderable {
    private static final Logger logger = LoggerFactory.getLogger(DotGraph.class);
    public static final String DOT_EXTENSION = ".dot";
    private final HashMap<String, DotGraphNode> nodes;
    private final List<Renderable> drawElements;
    private final boolean isSubGraph;
    private boolean dontQuoteNodeNames;
    private String graphname;

    private DotGraph(String graphname, boolean isSubGraph) {
        this.graphname = graphname;
        this.isSubGraph = isSubGraph;
        this.nodes = new HashMap<>(100);
        this.drawElements = new LinkedList();
        this.dontQuoteNodeNames = false;
    }

    public DotGraph(String graphname) {
        this(graphname, false);
    }

    public void plot(String filename) {
        try {
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(filename));
            try {
                render(out, 0);
                if (out != null) {
                    out.close();
                }
            } catch (Throwable th) {
                if (out != null) {
                    out.close();
                }
                throw th;
            }
        } catch (IOException ioe) {
            logger.debug(ioe.getMessage());
        }
    }

    public DotGraphEdge drawEdge(String from, String to) {
        return drawEdge(from, to, true);
    }

    public DotGraphEdge drawUndirectedEdge(String node1, String node2) {
        return drawEdge(node1, node2, false);
    }

    private DotGraphEdge drawEdge(String from, String to, boolean directed) {
        DotGraphNode src = drawNode(from);
        DotGraphNode dst = drawNode(to);
        DotGraphEdge edge = new DotGraphEdge(src, dst, directed);
        this.drawElements.add(edge);
        return edge;
    }

    public DotGraphNode drawNode(String name) {
        DotGraphNode node = getNode(name);
        if (node == null) {
            throw new RuntimeException("Assertion failed.");
        }
        if (!this.drawElements.contains(node)) {
            this.drawElements.add(node);
        }
        return node;
    }

    public DotGraphNode getNode(String name) {
        if (name == null) {
            return null;
        }
        DotGraphNode node = this.nodes.get(name);
        if (node == null) {
            node = new DotGraphNode(name, this.dontQuoteNodeNames);
            this.nodes.put(name, node);
        }
        return node;
    }

    public boolean containsNode(String name) {
        return name != null && this.nodes.containsKey(name);
    }

    public boolean containsNode(DotGraphNode node) {
        return this.drawElements.contains(node);
    }

    public void quoteNodeNames(boolean value) {
        this.dontQuoteNodeNames = !value;
    }

    public void setNodeShape(String shape) {
        String command = "node [shape=" + shape + "];";
        this.drawElements.add(new DotGraphCommand(command));
    }

    public void setNodeStyle(String style) {
        String command = "node [style=" + style + "];";
        this.drawElements.add(new DotGraphCommand(command));
    }

    public void setGraphSize(double width, double height) {
        String size = "\"" + width + "," + height + "\"";
        setAttribute("size", size);
    }

    public void setPageSize(double width, double height) {
        String size = "\"" + width + ", " + height + "\"";
        setAttribute("page", size);
    }

    public void setOrientation(String orientation) {
        setAttribute(MediaStore.Images.ImageColumns.ORIENTATION, orientation);
    }

    public void setGraphName(String name) {
        this.graphname = name;
    }

    public void setGraphLabel(String label) {
        setLabel(label);
    }

    public void setGraphAttribute(String id, String value) {
        setAttribute(id, value);
    }

    public void setGraphAttribute(DotGraphAttribute attr) {
        setAttribute(attr);
    }

    public DotGraph createSubGraph(String label) {
        DotGraph subgraph = new DotGraph(label, true);
        this.drawElements.add(subgraph);
        return subgraph;
    }

    @Override // soot.util.dot.Renderable
    public void render(OutputStream out, int indent) throws IOException {
        if (!this.isSubGraph) {
            DotGraphUtility.renderLine(out, "digraph \"" + this.graphname + "\" {", indent);
        } else {
            DotGraphUtility.renderLine(out, "subgraph \"" + this.graphname + "\" {", indent);
        }
        for (DotGraphAttribute attr : getAttributes()) {
            DotGraphUtility.renderLine(out, String.valueOf(attr.toString()) + ';', indent + 4);
        }
        for (Renderable element : this.drawElements) {
            element.render(out, indent + 4);
        }
        DotGraphUtility.renderLine(out, "}", indent);
    }
}
