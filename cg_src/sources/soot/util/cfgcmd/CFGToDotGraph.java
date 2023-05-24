package soot.util.cfgcmd;

import android.provider.CalendarContract;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import polyglot.main.Report;
import soot.Body;
import soot.BriefUnitPrinter;
import soot.LabeledUnitPrinter;
import soot.Unit;
import soot.toolkits.exceptions.ThrowableSet;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.DominatorNode;
import soot.toolkits.graph.ExceptionalGraph;
import soot.util.dot.DotGraph;
import soot.util.dot.DotGraphAttribute;
import soot.util.dot.DotGraphConstants;
import soot.util.dot.DotGraphEdge;
import soot.util.dot.DotGraphNode;
/* loaded from: gencallgraphv3.jar:soot/util/cfgcmd/CFGToDotGraph.class */
public class CFGToDotGraph {
    private boolean onePage;
    private boolean isBrief;
    private boolean showExceptions;
    private DotGraphAttribute unexceptionalControlFlowAttr;
    private DotGraphAttribute exceptionalControlFlowAttr;
    private DotGraphAttribute exceptionEdgeAttr;
    private DotGraphAttribute headAttr;
    private DotGraphAttribute tailAttr;

    public CFGToDotGraph() {
        setOnePage(true);
        setBriefLabels(false);
        setShowExceptions(true);
        setUnexceptionalControlFlowAttr(CalendarContract.ColorsColumns.COLOR, "black");
        setExceptionalControlFlowAttr(CalendarContract.ColorsColumns.COLOR, "red");
        setExceptionEdgeAttr(CalendarContract.ColorsColumns.COLOR, "lightgray");
        setHeadAttr("fillcolor", "gray");
        setTailAttr("fillcolor", "lightgray");
    }

    public void setOnePage(boolean onePage) {
        this.onePage = onePage;
    }

    public void setBriefLabels(boolean useBrief) {
        this.isBrief = useBrief;
    }

    public void setShowExceptions(boolean showExceptions) {
        this.showExceptions = showExceptions;
    }

    public void setUnexceptionalControlFlowAttr(String id, String value) {
        this.unexceptionalControlFlowAttr = new DotGraphAttribute(id, value);
    }

    public void setExceptionalControlFlowAttr(String id, String value) {
        this.exceptionalControlFlowAttr = new DotGraphAttribute(id, value);
    }

    public void setExceptionEdgeAttr(String id, String value) {
        this.exceptionEdgeAttr = new DotGraphAttribute(id, value);
    }

    public void setHeadAttr(String id, String value) {
        this.headAttr = new DotGraphAttribute(id, value);
    }

    public void setTailAttr(String id, String value) {
        this.tailAttr = new DotGraphAttribute(id, value);
    }

    private static <T> Iterator<T> sortedIterator(Collection<T> coll, Comparator<? super T> comp) {
        if (coll.size() <= 1) {
            return coll.iterator();
        }
        ArrayList<T> list = new ArrayList<>((Collection<? extends T>) coll);
        Collections.sort(list, comp);
        return list.iterator();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/util/cfgcmd/CFGToDotGraph$NodeComparator.class */
    public static class NodeComparator<T> implements Comparator<T> {
        private final DotNamer<T> namer;

        NodeComparator(DotNamer<T> namer) {
            this.namer = namer;
        }

        @Override // java.util.Comparator
        public int compare(T o1, T o2) {
            return this.namer.getNumber(o1) - this.namer.getNumber(o2);
        }

        public boolean equal(T o1, T o2) {
            return this.namer.getNumber(o1) == this.namer.getNumber(o2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/util/cfgcmd/CFGToDotGraph$ExceptionDestComparator.class */
    public static class ExceptionDestComparator<T> implements Comparator<ExceptionalGraph.ExceptionDest<T>> {
        private final DotNamer<T> namer;

        @Override // java.util.Comparator
        public /* bridge */ /* synthetic */ int compare(Object obj, Object obj2) {
            return compare((ExceptionalGraph.ExceptionDest) ((ExceptionalGraph.ExceptionDest) obj), (ExceptionalGraph.ExceptionDest) ((ExceptionalGraph.ExceptionDest) obj2));
        }

        ExceptionDestComparator(DotNamer<T> namer) {
            this.namer = namer;
        }

        private int getValue(ExceptionalGraph.ExceptionDest<T> o) {
            T handler = o.getHandlerNode();
            if (handler == null) {
                return Integer.MAX_VALUE;
            }
            return this.namer.getNumber(handler);
        }

        public int compare(ExceptionalGraph.ExceptionDest<T> o1, ExceptionalGraph.ExceptionDest<T> o2) {
            return getValue(o1) - getValue(o2);
        }

        public boolean equal(ExceptionalGraph.ExceptionDest<T> o1, ExceptionalGraph.ExceptionDest<T> o2) {
            return getValue(o1) == getValue(o2);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <N> DotGraph drawCFG(DirectedGraph<N> graph, Body body) {
        DotGraph canvas = initDotGraph(body);
        DotNamer dotNamer = new DotNamer((int) (graph.size() / 0.7f), 0.7f);
        NodeComparator<N> comparator = new NodeComparator<>(dotNamer);
        for (N node : graph) {
            dotNamer.getName(node);
        }
        for (N node2 : graph) {
            canvas.drawNode(dotNamer.getName(node2));
            Iterator<N> succsIt = sortedIterator(graph.getSuccsOf(node2), comparator);
            while (succsIt.hasNext()) {
                N succ = succsIt.next();
                canvas.drawEdge(dotNamer.getName(node2), dotNamer.getName(succ));
            }
        }
        setStyle(graph.getHeads(), canvas, dotNamer, DotGraphConstants.NODE_STYLE_FILLED, this.headAttr);
        setStyle(graph.getTails(), canvas, dotNamer, DotGraphConstants.NODE_STYLE_FILLED, this.tailAttr);
        if (!this.isBrief) {
            formatNodeText(body, canvas, dotNamer);
        }
        return canvas;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <N> DotGraph drawCFG(ExceptionalGraph<N> graph) {
        Collection<? extends ExceptionalGraph.ExceptionDest<N>> exDests;
        Body body = graph.getBody();
        DotGraph canvas = initDotGraph(body);
        DotNamer<Object> namer = new DotNamer<>((int) (graph.size() / 0.7f), 0.7f);
        NodeComparator<N> nodeComparator = new NodeComparator<>(namer);
        for (N n : graph) {
            namer.getName(n);
        }
        for (N node : graph) {
            canvas.drawNode(namer.getName(node));
            Iterator<N> succsIt = sortedIterator(graph.getUnexceptionalSuccsOf(node), nodeComparator);
            while (succsIt.hasNext()) {
                N succ = succsIt.next();
                canvas.drawEdge(namer.getName(node), namer.getName(succ)).setAttribute(this.unexceptionalControlFlowAttr);
            }
            Iterator<N> succsIt2 = sortedIterator(graph.getExceptionalSuccsOf(node), nodeComparator);
            while (succsIt2.hasNext()) {
                N succ2 = succsIt2.next();
                canvas.drawEdge(namer.getName(node), namer.getName(succ2)).setAttribute(this.exceptionalControlFlowAttr);
            }
            if (this.showExceptions && (exDests = graph.getExceptionDests(node)) != null) {
                ExceptionDestComparator<N> comp = new ExceptionDestComparator<>(namer);
                Iterator<? extends ExceptionalGraph.ExceptionDest<N>> destsIt = sortedIterator(exDests, comp);
                while (destsIt.hasNext()) {
                    ExceptionalGraph.ExceptionDest<N> dest = destsIt.next();
                    Object handlerStart = dest.getHandlerNode();
                    if (handlerStart == null) {
                        handlerStart = new Object() { // from class: soot.util.cfgcmd.CFGToDotGraph.1
                            public String toString() {
                                return "Esc";
                            }
                        };
                        DotGraphNode escapeNode = canvas.drawNode(namer.getName(handlerStart));
                        escapeNode.setStyle(DotGraphConstants.NODE_STYLE_INVISIBLE);
                    }
                    DotGraphEdge edge = canvas.drawEdge(namer.getName(node), namer.getName(handlerStart));
                    edge.setAttribute(this.exceptionEdgeAttr);
                    edge.setLabel(formatThrowableSet(dest.getThrowables()));
                }
            }
        }
        setStyle(graph.getHeads(), canvas, namer, DotGraphConstants.NODE_STYLE_FILLED, this.headAttr);
        setStyle(graph.getTails(), canvas, namer, DotGraphConstants.NODE_STYLE_FILLED, this.tailAttr);
        if (!this.isBrief) {
            formatNodeText(graph.getBody(), canvas, namer);
        }
        return canvas;
    }

    private DotGraph initDotGraph(Body body) {
        String graphname = Report.cfg;
        if (body != null) {
            graphname = body.getMethod().getSubSignature();
        }
        DotGraph canvas = new DotGraph(graphname);
        canvas.setGraphLabel(graphname);
        if (!this.onePage) {
            canvas.setPageSize(8.5d, 11.0d);
        }
        if (this.isBrief) {
            canvas.setNodeShape(DotGraphConstants.NODE_SHAPE_CIRCLE);
        } else {
            canvas.setNodeShape(DotGraphConstants.NODE_SHAPE_BOX);
        }
        return canvas;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/util/cfgcmd/CFGToDotGraph$DotNamer.class */
    public static class DotNamer<N> extends HashMap<N, Integer> {
        private int nodecount;

        DotNamer(int initialCapacity, float loadFactor) {
            super(initialCapacity, loadFactor);
            this.nodecount = 0;
        }

        String getName(N node) {
            Integer index = get(node);
            if (index == null) {
                int i = this.nodecount;
                this.nodecount = i + 1;
                index = Integer.valueOf(i);
                put(node, index);
            }
            return index.toString();
        }

        int getNumber(N node) {
            Integer index = get(node);
            if (index == null) {
                int i = this.nodecount;
                this.nodecount = i + 1;
                index = Integer.valueOf(i);
                put(node, index);
            }
            return index.intValue();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <N> void formatNodeText(Body body, DotGraph canvas, DotNamer<N> namer) {
        String obj;
        LabeledUnitPrinter printer = null;
        if (body != null) {
            printer = new BriefUnitPrinter(body);
            printer.noIndent();
        }
        Iterator<N> it = namer.keySet().iterator();
        while (it.hasNext()) {
            N node = it.next();
            DotGraphNode dotnode = canvas.getNode(namer.getName(node));
            if (node instanceof DominatorNode) {
                DominatorNode<N> n = (DominatorNode) node;
                node = n.getGode();
            }
            if (printer == null) {
                obj = node.toString();
            } else if (node instanceof Unit) {
                Unit nodeUnit = (Unit) node;
                nodeUnit.toString(printer);
                String targetLabel = printer.labels().get(nodeUnit);
                if (targetLabel == null) {
                    obj = printer.toString();
                } else {
                    obj = String.valueOf(targetLabel) + ": " + printer.toString();
                }
            } else if (node instanceof Block) {
                Block block = (Block) node;
                StringBuilder buffer = new StringBuilder();
                buffer.append(block.toShortString()).append("\\n");
                Iterator<Unit> it2 = block.iterator();
                while (it2.hasNext()) {
                    Unit unit = it2.next();
                    String targetLabel2 = printer.labels().get(unit);
                    if (targetLabel2 != null) {
                        buffer.append(targetLabel2).append(":\\n");
                    }
                    unit.toString(printer);
                    buffer.append(printer.toString()).append("\\l");
                }
                obj = buffer.toString();
            } else {
                obj = node.toString();
            }
            String nodeLabel = obj;
            dotnode.setLabel(nodeLabel);
        }
    }

    private <N> void setStyle(Collection<? extends N> objects, DotGraph canvas, DotNamer<N> namer, String style, DotGraphAttribute attrib) {
        for (N object : objects) {
            DotGraphNode objectNode = canvas.getNode(namer.getName(object));
            objectNode.setStyle(style);
            objectNode.setAttribute(attrib);
        }
    }

    private String formatThrowableSet(ThrowableSet set) {
        String input = set.toAbbreviatedString();
        int inputLength = input.length();
        StringBuilder result = new StringBuilder(inputLength + 5);
        for (int i = 0; i < inputLength; i++) {
            char c = input.charAt(i);
            if (c == '+' || c == '-') {
                result.append("\\l");
            }
            result.append(c);
        }
        return result.toString();
    }
}
