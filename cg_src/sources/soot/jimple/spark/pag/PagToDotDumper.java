package soot.jimple.spark.pag;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.SootField;
import soot.SootMethod;
import soot.coffi.Instruction;
import soot.jimple.infoflow.rifl.RIFLConstants;
import soot.jimple.spark.ondemand.genericutil.Predicate;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.util.dot.DotGraph;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/pag/PagToDotDumper.class */
public class PagToDotDumper {
    public static final int TRACE_MAX_LVL = 99;
    private PAG pag;
    private HashMap<Node, Node[]> vmatches = new HashMap<>();
    private HashMap<Node, Node[]> invVmatches = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(PagToDotDumper.class);
    private static final Predicate<Node> emptyP2SetPred = new Predicate<Node>() { // from class: soot.jimple.spark.pag.PagToDotDumper.1
        @Override // soot.jimple.spark.ondemand.genericutil.Predicate
        public boolean test(Node n) {
            return !(n instanceof AllocNode) && n.getP2Set().isEmpty();
        }
    };

    public PagToDotDumper(PAG pag) {
        this.pag = pag;
    }

    private void buildVmatchEdges() {
        Iterator iter = this.pag.loadSourcesIterator();
        while (iter.hasNext()) {
            FieldRefNode frn1 = iter.next();
            Iterator iter2 = this.pag.storeInvSourcesIterator();
            while (iter2.hasNext()) {
                FieldRefNode frn2 = iter2.next();
                VarNode base1 = frn1.getBase();
                VarNode base2 = frn2.getBase();
                if (frn1.getField().equals(frn2.getField()) && base1.getP2Set().hasNonEmptyIntersection(base2.getP2Set())) {
                    Node[] src = this.pag.loadLookup(frn1);
                    Node[] dst = this.pag.storeInvLookup(frn2);
                    for (Node node : src) {
                        this.vmatches.put(node, dst);
                    }
                    for (Node node2 : dst) {
                        this.invVmatches.put(node2, src);
                    }
                }
            }
        }
    }

    private void debug(FieldRefNode frn1, FieldRefNode frn2, VarNode base1, VarNode base2) {
        if ((base1 instanceof LocalVarNode) && (base2 instanceof LocalVarNode)) {
            LocalVarNode lvn1 = (LocalVarNode) base1;
            LocalVarNode lvn2 = (LocalVarNode) base2;
            if (lvn1.getMethod().getDeclaringClass().getName().equals("java.util.Hashtable$ValueCollection") && lvn1.getMethod().getName().equals("contains") && lvn2.getMethod().getDeclaringClass().getName().equals("java.util.Hashtable$ValueCollection") && lvn2.getMethod().getName().equals("<init>")) {
                System.err.println("Method: " + lvn1.getMethod().getName());
                System.err.println(makeLabel(frn1));
                System.err.println("Base: " + base1.getVariable());
                System.err.println("Field: " + frn1.getField());
                System.err.println(makeLabel(frn2));
                System.err.println("Base: " + base2.getVariable());
                System.err.println("Field: " + frn2.getField());
                if (frn1.getField().equals(frn2.getField())) {
                    System.err.println("field match");
                    if (base1.getP2Set().hasNonEmptyIntersection(base2.getP2Set())) {
                        System.err.println("non empty");
                        return;
                    }
                    System.err.println("b1: " + base1.getP2Set());
                    System.err.println("b2: " + base2.getP2Set());
                }
            }
        }
    }

    private static String translateEdge(Node src, Node dest, String label) {
        return String.valueOf(makeNodeName(src)) + " -> " + makeNodeName(dest) + " [label=\"" + label + "\"];";
    }

    public static String makeDotNodeLabel(Node n, Predicate<Node> p) {
        String label;
        String color = "";
        if (p.test(n)) {
            color = ", color=red";
        }
        if (n instanceof LocalVarNode) {
            label = makeLabel((LocalVarNode) n);
        } else if (n instanceof AllocNode) {
            label = makeLabel((AllocNode) n);
        } else if (n instanceof FieldRefNode) {
            label = makeLabel((FieldRefNode) n);
        } else {
            label = n.toString();
        }
        return String.valueOf(makeNodeName(n)) + "[label=\"" + label + "\"" + color + "];";
    }

    private static String translateLabel(Node n) {
        return makeDotNodeLabel(n, emptyP2SetPred);
    }

    private boolean isDefinedIn(LocalVarNode lvNode, String cName, String mName) {
        return lvNode.getMethod() != null && lvNode.getMethod().getDeclaringClass().getName().equals(cName) && lvNode.getMethod().getName().equals(mName);
    }

    private void printOneNode(VarNode node) {
        PrintStream ps = System.err;
        ps.println(makeLabel(node));
        Node[] succs = this.pag.simpleInvLookup(node);
        ps.println(RIFLConstants.ASSIGN_TAG);
        ps.println("======");
        for (Node node2 : succs) {
            ps.println(node2);
        }
        Node[] succs2 = this.pag.allocInvLookup(node);
        ps.println("new");
        ps.println("======");
        for (Node node3 : succs2) {
            ps.println(node3);
        }
        Node[] succs3 = this.pag.loadInvLookup(node);
        ps.println("load");
        ps.println("======");
        for (Node node4 : succs3) {
            ps.println(node4);
        }
        Node[] succs4 = this.pag.storeLookup(node);
        ps.println("store");
        ps.println("======");
        for (Node node5 : succs4) {
            ps.println(node5);
        }
    }

    public void dumpP2SetsForLocals(String fName, String mName) throws FileNotFoundException {
        FileOutputStream fos = new FileOutputStream(new File(fName));
        PrintStream ps = new PrintStream(fos);
        ps.println("digraph G {");
        dumpLocalP2Set(mName, ps);
        ps.print("}");
    }

    private void dumpLocalP2Set(String mName, PrintStream ps) {
        Iterator iter = this.pag.getVarNodeNumberer().iterator();
        while (iter.hasNext()) {
            VarNode vNode = iter.next();
            if (vNode instanceof LocalVarNode) {
                LocalVarNode lvNode = (LocalVarNode) vNode;
                if (lvNode.getMethod() != null && lvNode.getMethod().getName().equals(mName)) {
                    ps.println("\t" + makeNodeName(lvNode) + " [label=\"" + makeLabel(lvNode) + "\"];");
                    lvNode.getP2Set().forall(new P2SetToDotPrinter(lvNode, ps));
                }
            }
        }
    }

    public void dumpPAGForMethod(String fName, String cName, String mName) throws FileNotFoundException {
        FileOutputStream fos = new FileOutputStream(new File(fName));
        PrintStream ps = new PrintStream(fos);
        ps.println("digraph G {");
        ps.println("\trankdir=LR;");
        dumpLocalPAG(cName, mName, ps);
        ps.print("}");
        try {
            fos.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), (Throwable) e);
        }
        ps.close();
    }

    private void dumpLocalPAG(String cName, String mName, PrintStream ps) {
        Iterator iter = this.pag.getVarNodeNumberer().iterator();
        while (iter.hasNext()) {
            VarNode node = iter.next();
            if (node instanceof LocalVarNode) {
                LocalVarNode lvNode = (LocalVarNode) node;
                if (isDefinedIn(lvNode, cName, mName)) {
                    dumpForwardReachableNodesFrom(lvNode, ps);
                }
            }
        }
    }

    private void dumpForwardReachableNodesFrom(LocalVarNode lvNode, PrintStream ps) {
        ps.println("\t" + translateLabel(lvNode));
        Node[] succs = this.pag.simpleInvLookup(lvNode);
        for (int i = 0; i < succs.length; i++) {
            ps.println("\t" + translateLabel(succs[i]));
            ps.println("\t" + translateEdge(lvNode, succs[i], RIFLConstants.ASSIGN_TAG));
        }
        Node[] succs2 = this.pag.allocInvLookup(lvNode);
        for (int i2 = 0; i2 < succs2.length; i2++) {
            ps.println("\t" + translateLabel(succs2[i2]));
            ps.println("\t" + translateEdge(lvNode, succs2[i2], "new"));
        }
        for (Node node : this.pag.loadInvLookup(lvNode)) {
            FieldRefNode frNode = (FieldRefNode) node;
            ps.println("\t" + translateLabel(frNode));
            ps.println("\t" + translateLabel(frNode.getBase()));
            ps.println("\t" + translateEdge(lvNode, frNode, "load"));
            ps.println("\t" + translateEdge(frNode, frNode.getBase(), "getBase"));
        }
        for (Node node2 : this.pag.storeLookup(lvNode)) {
            FieldRefNode frNode2 = (FieldRefNode) node2;
            ps.println("\t" + translateLabel(frNode2));
            ps.println("\t" + translateLabel(frNode2.getBase()));
            ps.println("\t" + translateEdge(frNode2, lvNode, "store"));
            ps.println("\t" + translateEdge(frNode2, frNode2.getBase(), "getBase"));
        }
    }

    public void traceNode(int id) {
        buildVmatchEdges();
        String fName = "trace." + id + DotGraph.DOT_EXTENSION;
        try {
            FileOutputStream fos = new FileOutputStream(new File(fName));
            PrintStream ps = new PrintStream(fos);
            ps.println("digraph G {");
            Iterator iter = this.pag.getVarNodeNumberer().iterator();
            while (iter.hasNext()) {
                VarNode n = iter.next();
                if (n.getNumber() == id) {
                    LocalVarNode lvn = (LocalVarNode) n;
                    printOneNode(lvn);
                    trace(lvn, ps, new HashSet<>(), 99);
                }
            }
            ps.print("}");
            ps.close();
            fos.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), (Throwable) e);
        }
    }

    public void traceNode(String cName, String mName, String varName) {
        String mName2 = mName;
        if (mName.indexOf(60) == 0) {
            mName2 = mName.substring(1, mName.length() - 1);
        }
        traceLocalVarNode("trace." + cName + "." + mName2 + "." + varName + DotGraph.DOT_EXTENSION, cName, mName, varName);
    }

    public void traceLocalVarNode(String fName, String cName, String mName, String varName) {
        buildVmatchEdges();
        try {
            FileOutputStream fos = new FileOutputStream(new File(fName));
            PrintStream ps = new PrintStream(fos);
            ps.println("digraph G {");
            Iterator iter = this.pag.getVarNodeNumberer().iterator();
            while (iter.hasNext()) {
                VarNode n = iter.next();
                if (n instanceof LocalVarNode) {
                    LocalVarNode lvn = (LocalVarNode) n;
                    if (lvn.getMethod() != null && isDefinedIn(lvn, cName, mName) && lvn.getVariable().toString().equals(varName)) {
                        trace(lvn, ps, new HashSet<>(), 10);
                    }
                }
            }
            ps.print("}");
            ps.close();
            fos.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), (Throwable) e);
        }
    }

    private void trace(VarNode node, PrintStream ps, HashSet<Node> visitedNodes, int level) {
        if (level < 1) {
            return;
        }
        ps.println("\t" + translateLabel(node));
        Node[] succs = this.pag.simpleInvLookup(node);
        for (int i = 0; i < succs.length; i++) {
            if (!visitedNodes.contains(succs[i])) {
                ps.println("\t" + translateLabel(succs[i]));
                ps.println("\t" + translateEdge(node, succs[i], RIFLConstants.ASSIGN_TAG));
                visitedNodes.add(succs[i]);
                trace((VarNode) succs[i], ps, visitedNodes, level - 1);
            }
        }
        Node[] succs2 = this.pag.allocInvLookup(node);
        for (int i2 = 0; i2 < succs2.length; i2++) {
            if (!visitedNodes.contains(succs2[i2])) {
                ps.println("\t" + translateLabel(succs2[i2]));
                ps.println("\t" + translateEdge(node, succs2[i2], "new"));
            }
        }
        Node[] succs3 = this.vmatches.get(node);
        if (succs3 != null) {
            for (int i3 = 0; i3 < succs3.length; i3++) {
                if (!visitedNodes.contains(succs3[i3])) {
                    ps.println("\t" + translateLabel(succs3[i3]));
                    ps.println("\t" + translateEdge(node, succs3[i3], "vmatch"));
                    trace((VarNode) succs3[i3], ps, visitedNodes, level - 1);
                }
            }
        }
    }

    public static String makeNodeName(Node n) {
        return "node_" + n.getNumber();
    }

    public static String makeLabel(AllocNode n) {
        return n.getNewExpr().toString();
    }

    public static String makeLabel(LocalVarNode n) {
        SootMethod sm = n.getMethod();
        return "LV " + n.getVariable().toString() + Instruction.argsep + n.getNumber() + "\\n" + sm.getDeclaringClass() + "\\n" + sm.getName();
    }

    public static String makeLabel(FieldRefNode node) {
        if (node.getField() instanceof SootField) {
            SootField sf = (SootField) node.getField();
            return "FNR " + makeLabel(node.getBase()) + "." + sf.getName();
        }
        return "FNR " + makeLabel(node.getBase()) + "." + node.getField();
    }

    public static String makeLabel(VarNode base) {
        if (base instanceof LocalVarNode) {
            return makeLabel((LocalVarNode) base);
        }
        return base.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:soot/jimple/spark/pag/PagToDotDumper$P2SetToDotPrinter.class */
    public class P2SetToDotPrinter extends P2SetVisitor {
        private final Node curNode;
        private final PrintStream ps;

        P2SetToDotPrinter(Node curNode, PrintStream ps) {
            this.curNode = curNode;
            this.ps = ps;
        }

        @Override // soot.jimple.spark.sets.P2SetVisitor
        public void visit(Node n) {
            this.ps.println("\t" + PagToDotDumper.makeNodeName(n) + " [label=\"" + PagToDotDumper.makeLabel((AllocNode) n) + "\"];");
            this.ps.print("\t" + PagToDotDumper.makeNodeName(this.curNode) + " -> ");
            this.ps.println(String.valueOf(PagToDotDumper.makeNodeName(n)) + ";");
        }
    }
}
