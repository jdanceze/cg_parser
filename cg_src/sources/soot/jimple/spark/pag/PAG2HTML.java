package soot.jimple.spark.pag;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import soot.SootMethod;
import soot.jimple.Jimple;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.util.HashMultiMap;
import soot.util.MultiMap;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/pag/PAG2HTML.class */
public class PAG2HTML {
    protected PAG pag;
    protected String output_dir;
    protected MultiMap mergedNodes = new HashMultiMap();
    protected MultiMap methodToNodes = new HashMultiMap();

    public PAG2HTML(PAG pag, String output_dir) {
        this.pag = pag;
        this.output_dir = output_dir;
    }

    public void dump() {
        SootMethod m;
        Iterator vIt = this.pag.getVarNodeNumberer().iterator();
        while (vIt.hasNext()) {
            VarNode v = vIt.next();
            this.mergedNodes.put(v.getReplacement(), v);
            if ((v instanceof LocalVarNode) && (m = ((LocalVarNode) v).getMethod()) != null) {
                this.methodToNodes.put(m, v);
            }
        }
        try {
            JarOutputStream jarOut = new JarOutputStream(new FileOutputStream(new File(this.output_dir, "pag.jar")));
            for (VarNode v2 : this.mergedNodes.keySet()) {
                dumpVarNode(v2, jarOut);
            }
            for (SootMethod m2 : this.methodToNodes.keySet()) {
                dumpMethod(m2, jarOut);
            }
            addSymLinks(this.pag.getVarNodeNumberer().iterator(), jarOut);
            jarOut.close();
        } catch (IOException e) {
            throw new RuntimeException("Couldn't dump html" + e);
        }
    }

    protected void dumpVarNode(VarNode v, JarOutputStream jarOut) throws IOException {
        jarOut.putNextEntry(new ZipEntry("nodes/n" + v.getNumber() + ".html"));
        final PrintWriter out = new PrintWriter(jarOut);
        out.println("<html>");
        out.println("Green node for:");
        out.println(varNodeReps(v));
        out.println("Declared type: " + v.getType());
        out.println("<hr>Reaching blue nodes:");
        out.println("<ul>");
        v.getP2Set().forall(new P2SetVisitor() { // from class: soot.jimple.spark.pag.PAG2HTML.1
            @Override // soot.jimple.spark.sets.P2SetVisitor
            public final void visit(Node n) {
                out.println("<li>" + PAG2HTML.htmlify(n.toString()));
            }
        });
        out.println("</ul>");
        out.println("<hr>Outgoing edges:");
        Node[] succs = this.pag.simpleLookup(v);
        for (Node element : succs) {
            VarNode succ = (VarNode) element;
            out.println(varNodeReps(succ));
        }
        out.println("<hr>Incoming edges: ");
        Node[] succs2 = this.pag.simpleInvLookup(v);
        for (Node element2 : succs2) {
            VarNode succ2 = (VarNode) element2;
            out.println(varNodeReps(succ2));
        }
        out.println("</html>");
        out.flush();
    }

    protected String varNodeReps(VarNode v) {
        StringBuffer ret = new StringBuffer();
        ret.append("<ul>\n");
        for (VarNode vv : this.mergedNodes.get(v)) {
            ret.append(varNode("", vv));
        }
        ret.append("</ul>\n");
        return ret.toString();
    }

    protected String varNode(String dirPrefix, VarNode vv) {
        StringBuffer ret = new StringBuffer();
        ret.append("<li><a href=\"" + dirPrefix + "n" + vv.getNumber() + ".html\">");
        if (vv.getVariable() != null) {
            ret.append(htmlify(vv.getVariable().toString()));
        }
        ret.append("GlobalVarNode");
        ret.append("</a><br>");
        ret.append("<li>Context: ");
        ret.append((vv.context() == null ? Jimple.NULL : htmlify(vv.context().toString())));
        ret.append("</a><br>");
        if (vv instanceof LocalVarNode) {
            LocalVarNode lvn = (LocalVarNode) vv;
            SootMethod m = lvn.getMethod();
            if (m != null) {
                ret.append("<a href=\"../" + toFileName(m.toString()) + ".html\">");
                ret.append(String.valueOf(htmlify(m.toString())) + "</a><br>");
            }
        }
        ret.append(String.valueOf(htmlify(vv.getType().toString())) + "\n");
        return ret.toString();
    }

    protected static String htmlify(String s) {
        StringBuffer b = new StringBuffer(s);
        for (int i = 0; i < b.length(); i++) {
            if (b.charAt(i) == '<') {
                b.replace(i, i + 1, "&lt;");
            }
            if (b.charAt(i) == '>') {
                b.replace(i, i + 1, "&gt;");
            }
        }
        return b.toString();
    }

    protected void dumpMethod(SootMethod m, JarOutputStream jarOut) throws IOException {
        jarOut.putNextEntry(new ZipEntry(toFileName(m.toString()) + ".html"));
        PrintWriter out = new PrintWriter(jarOut);
        out.println("<html>");
        out.println("This is method " + htmlify(m.toString()) + "<hr>");
        for (VarNode varNode : this.methodToNodes.get(m)) {
            out.println(varNode("nodes/", varNode));
        }
        out.println("</html>");
        out.flush();
    }

    protected void addSymLinks(Iterator nodes, JarOutputStream jarOut) throws IOException {
        jarOut.putNextEntry(new ZipEntry("symlinks.sh"));
        PrintWriter out = new PrintWriter(jarOut);
        out.println("#!/bin/sh");
        while (nodes.hasNext()) {
            VarNode v = (VarNode) nodes.next();
            VarNode rep = (VarNode) v.getReplacement();
            if (v != rep) {
                out.println("ln -s n" + rep.getNumber() + ".html n" + v.getNumber() + ".html");
            }
        }
        out.flush();
    }

    protected String toFileName(String s) {
        StringBuffer ret = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '<') {
                ret.append('{');
            } else if (c == '>') {
                ret.append('}');
            } else {
                ret.append(c);
            }
        }
        return ret.toString();
    }
}
