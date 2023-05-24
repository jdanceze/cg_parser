package soot.jimple.spark.ondemand;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.dava.internal.AST.ASTNode;
import soot.jimple.spark.ondemand.genericutil.Predicate;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.FieldRefNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PagToDotDumper;
import soot.jimple.spark.pag.VarNode;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/DotPointerGraph.class */
public class DotPointerGraph {
    private static final Logger logger = LoggerFactory.getLogger(DotPointerGraph.class);
    private final Set<String> edges = new HashSet();
    private final Set<Node> nodes = new HashSet();

    public void addAssign(VarNode from, VarNode to) {
        addEdge(to, from, "", "black");
    }

    private void addEdge(Node from, Node to, String edgeLabel, String color) {
        this.nodes.add(from);
        this.nodes.add(to);
        addEdge(PagToDotDumper.makeNodeName(from), PagToDotDumper.makeNodeName(to), edgeLabel, color);
    }

    private void addEdge(String from, String to, String edgeLabel, String color) {
        StringBuffer tmp = new StringBuffer();
        tmp.append(ASTNode.TAB);
        tmp.append(from);
        tmp.append(" -> ");
        tmp.append(to);
        tmp.append(" [label=\"");
        tmp.append(edgeLabel);
        tmp.append("\", color=");
        tmp.append(color);
        tmp.append("];");
        this.edges.add(tmp.toString());
    }

    public void addNew(AllocNode from, VarNode to) {
        addEdge(to, from, "", "yellow");
    }

    public void addCall(VarNode from, VarNode to, Integer callSite) {
        addEdge(to, from, callSite.toString(), "blue");
    }

    public void addMatch(VarNode from, VarNode to) {
        addEdge(to, from, "", "brown");
    }

    public void addLoad(FieldRefNode from, VarNode to) {
        addEdge(to, from.getBase(), from.getField().toString(), "green");
    }

    public void addStore(VarNode from, FieldRefNode to) {
        addEdge(to.getBase(), from, to.getField().toString(), "red");
    }

    public int numEdges() {
        return this.edges.size();
    }

    public void dump(String filename) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileOutputStream(filename));
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), (Throwable) e);
        }
        pw.println("digraph G {");
        Predicate<Node> falsePred = new Predicate<Node>() { // from class: soot.jimple.spark.ondemand.DotPointerGraph.1
            @Override // soot.jimple.spark.ondemand.genericutil.Predicate
            public boolean test(Node obj_) {
                return false;
            }
        };
        for (Node node : this.nodes) {
            pw.println(PagToDotDumper.makeDotNodeLabel(node, falsePred));
        }
        for (String edge : this.edges) {
            pw.println(edge);
        }
        pw.println("}");
        pw.close();
    }
}
