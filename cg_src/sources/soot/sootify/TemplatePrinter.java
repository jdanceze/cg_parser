package soot.sootify;

import java.io.PrintWriter;
import java.util.Iterator;
import soot.Body;
import soot.G;
import soot.Local;
import soot.Singletons;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
/* loaded from: gencallgraphv3.jar:soot/sootify/TemplatePrinter.class */
public class TemplatePrinter {
    private PrintWriter out;
    private int indentationLevel = 0;

    public TemplatePrinter(Singletons.Global g) {
    }

    public static TemplatePrinter v() {
        return G.v().soot_sootify_TemplatePrinter();
    }

    public void printTo(SootClass c, PrintWriter out) {
        this.out = out;
        printTo(c);
    }

    private void printTo(SootClass c) {
        println("import java.util.*;");
        println("import soot.*;");
        println("import soot.jimple.*;");
        println("import soot.util.*;");
        println("");
        print("public class ");
        print(String.valueOf(c.getName().replace('.', '_')) + "_Maker");
        println(" {");
        println("private static Local localByName(Body b, String name) {");
        println("\tfor(Local l: b.getLocals()) {");
        println("\t\tif(l.getName().equals(name))");
        println("\t\t\treturn l;");
        println("\t}");
        println("\tthrow new IllegalArgumentException(\"No such local: \"+name);");
        println("}");
        indent();
        println("public void create() {");
        indent();
        println("SootClass c = new SootClass(\"" + c.getName() + "\");");
        println("c.setApplicationClass();");
        println("Scene.v().addClass(c);");
        for (int i = 0; i < c.getMethodCount(); i++) {
            println("createMethod" + i + "(c);");
        }
        closeMethod();
        int i2 = 0;
        for (SootMethod m : c.getMethods()) {
            newMethod("createMethod" + i2);
            println("SootMethod m = new SootMethod(\"" + m.getName() + "\",null,null);");
            println("Body b = Jimple.v().newBody(m);");
            println("m.setActiveBody(b);");
            if (m.hasActiveBody()) {
                Body b = m.getActiveBody();
                println("Chain<Local> locals = b.getLocals();");
                for (Local l : b.getLocals()) {
                    println("locals.add(Jimple.v().newLocal(\"" + l.getName() + "\", RefType.v(\"" + l.getType() + "\")));");
                }
                println("Chain<Unit> units = b.getUnits();");
                StmtTemplatePrinter sw = new StmtTemplatePrinter(this, b.getUnits());
                Iterator<Unit> it = b.getUnits().iterator();
                while (it.hasNext()) {
                    Unit u = it.next();
                    u.apply(sw);
                }
                closeMethod();
                i2++;
            }
        }
        println("}");
    }

    private void closeMethod() {
        unindent();
        println("}");
        unindent();
        println("");
    }

    private void newMethod(String name) {
        indent();
        println("public void " + name + "(SootClass c) {");
        indent();
    }

    public void printlnNoIndent(String s) {
        printNoIndent(s);
        print("\n");
    }

    public void println(String s) {
        print(s);
        print("\n");
    }

    public void printNoIndent(String s) {
        this.out.print(s);
    }

    public void print(String s) {
        for (int i = 0; i < this.indentationLevel; i++) {
            this.out.print("  ");
        }
        this.out.print(s);
    }

    public void indent() {
        this.indentationLevel++;
    }

    public void unindent() {
        this.indentationLevel--;
    }

    public void openBlock() {
        println("{");
        indent();
    }

    public void closeBlock() {
        unindent();
        println("}");
    }
}
