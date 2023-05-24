package soot;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.function.Function;
import soot.Singletons;
import soot.coffi.Instruction;
import soot.dava.internal.AST.ASTNode;
import soot.jimple.Jimple;
import soot.options.Options;
import soot.tagkit.Host;
import soot.tagkit.JimpleLineNumberTag;
import soot.tagkit.Tag;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.util.Chain;
import soot.util.DeterministicHashMap;
/* loaded from: gencallgraphv3.jar:soot/Printer.class */
public class Printer {
    public static final int USE_ABBREVIATIONS = 1;
    public static final int ADD_JIMPLE_LN = 16;
    private int options = 0;
    private int jimpleLnNum = 0;
    private Function<Body, LabeledUnitPrinter> customUnitPrinter;
    private Function<SootClass, String> customClassSignaturePrinter;
    private Function<SootMethod, String> customMethodSignaturePrinter;

    public Printer(Singletons.Global g) {
    }

    public static Printer v() {
        return G.v().soot_Printer();
    }

    public boolean useAbbreviations() {
        return (this.options & 1) != 0;
    }

    public boolean addJimpleLn() {
        return (this.options & 16) != 0;
    }

    public void setOption(int opt) {
        this.options |= opt;
    }

    public void clearOption(int opt) {
        this.options &= opt ^ (-1);
    }

    public int getJimpleLnNum() {
        return this.jimpleLnNum;
    }

    public void setJimpleLnNum(int newVal) {
        this.jimpleLnNum = newVal;
    }

    public void incJimpleLnNum() {
        this.jimpleLnNum++;
    }

    public void printTo(SootClass cl, PrintWriter out) {
        setJimpleLnNum(1);
        StringBuilder sb = new StringBuilder();
        StringTokenizer st = new StringTokenizer(Modifier.toString(cl.getModifiers()));
        while (st.hasMoreTokens()) {
            String tok = st.nextToken();
            if (!cl.isInterface() || !Jimple.ABSTRACT.equals(tok)) {
                sb.append(tok).append(' ');
            }
        }
        sb.append(cl.isInterface() ? Instruction.argsep : "class ").append(printSignature(cl));
        out.print(sb.toString());
        if (cl.hasSuperclass()) {
            out.print(" extends " + printSignature(cl.getSuperclass()));
        }
        Iterator<SootClass> interfaceIt = cl.getInterfaces().iterator();
        if (interfaceIt.hasNext()) {
            out.print(" implements " + printSignature(interfaceIt.next()));
            while (interfaceIt.hasNext()) {
                out.print(", " + printSignature(interfaceIt.next()));
            }
        }
        out.println();
        incJimpleLnNum();
        out.println('{');
        incJimpleLnNum();
        boolean printTagsInOutput = Options.v().print_tags_in_output();
        if (printTagsInOutput) {
            for (Tag t : cl.getTags()) {
                out.println("/*" + t.toString() + "*/");
            }
        }
        for (SootField f : cl.getFields()) {
            if (!f.isPhantom()) {
                if (printTagsInOutput) {
                    for (Tag t2 : f.getTags()) {
                        out.println("/*" + t2.toString() + "*/");
                    }
                }
                out.println(ASTNode.TAB + f.getDeclaration() + ";");
                if (addJimpleLn()) {
                    setJimpleLnNum(addJimpleLnTags(getJimpleLnNum(), f));
                }
            }
        }
        Iterator<SootMethod> methodIt = cl.methodIterator();
        if (methodIt.hasNext()) {
            if (cl.getMethodCount() != 0) {
                out.println();
                incJimpleLnNum();
            }
            do {
                SootMethod method = methodIt.next();
                if (!method.isPhantom()) {
                    if (!Modifier.isAbstract(method.getModifiers()) && !Modifier.isNative(method.getModifiers())) {
                        Body body = method.retrieveActiveBody();
                        if (body == null) {
                            throw new RuntimeException("method " + method.getName() + " has no active body!");
                        }
                        if (printTagsInOutput) {
                            for (Tag t3 : method.getTags()) {
                                out.println("/*" + t3.toString() + "*/");
                            }
                        }
                        printTo(body, out);
                    } else {
                        if (printTagsInOutput) {
                            for (Tag t4 : method.getTags()) {
                                out.println("/*" + t4.toString() + "*/");
                            }
                        }
                        out.println(ASTNode.TAB + method.getDeclaration() + ";");
                        incJimpleLnNum();
                    }
                    if (methodIt.hasNext()) {
                        out.println();
                        incJimpleLnNum();
                    }
                }
            } while (methodIt.hasNext());
            out.println("}");
            incJimpleLnNum();
        }
        out.println("}");
        incJimpleLnNum();
    }

    public void printTo(Body b, PrintWriter out) {
        out.println(ASTNode.TAB + printSignature(b.getMethod()));
        if (addJimpleLn()) {
            setJimpleLnNum(addJimpleLnTags(getJimpleLnNum(), b.getMethod()));
        }
        out.println("    {");
        incJimpleLnNum();
        LabeledUnitPrinter up = getUnitPrinter(b);
        if (addJimpleLn()) {
            up.setPositionTagger(new AttributesUnitPrinter(getJimpleLnNum()));
        }
        printLocalsInBody(b, up);
        printStatementsInBody(b, out, up, new BriefUnitGraph(b));
        out.println("    }");
        incJimpleLnNum();
    }

    public void setCustomUnitPrinter(Function<Body, LabeledUnitPrinter> customUnitPrinter) {
        this.customUnitPrinter = customUnitPrinter;
    }

    public void setCustomClassSignaturePrinter(Function<SootClass, String> customPrinter) {
        this.customClassSignaturePrinter = customPrinter;
    }

    public void setCustomMethodSignaturePrinter(Function<SootMethod, String> customPrinter) {
        this.customMethodSignaturePrinter = customPrinter;
    }

    private LabeledUnitPrinter getUnitPrinter(Body b) {
        if (this.customUnitPrinter != null) {
            return this.customUnitPrinter.apply(b);
        }
        if (useAbbreviations()) {
            return new BriefUnitPrinter(b);
        }
        return new NormalUnitPrinter(b);
    }

    private String printSignature(SootClass sootClass) {
        if (this.customClassSignaturePrinter != null) {
            return this.customClassSignaturePrinter.apply(sootClass);
        }
        return Scene.v().quotedNameOf(sootClass.getName());
    }

    private String printSignature(SootMethod sootMethod) {
        if (this.customMethodSignaturePrinter != null) {
            return this.customMethodSignaturePrinter.apply(sootMethod);
        }
        return sootMethod.getDeclaration();
    }

    private void printStatementsInBody(Body body, PrintWriter out, LabeledUnitPrinter up, UnitGraph unitGraph) {
        Chain<Unit> units = body.getUnits();
        Unit firstUnit = units.getFirst();
        for (Unit currentStmt : units) {
            if (currentStmt != firstUnit) {
                List<Unit> succs = unitGraph.getSuccsOf(currentStmt);
                if (succs.size() != 1 || succs.get(0) != currentStmt || unitGraph.getPredsOf(currentStmt).size() != 1 || up.labels().containsKey(currentStmt)) {
                    up.newline();
                }
            }
            if (up.labels().containsKey(currentStmt)) {
                up.unitRef(currentStmt, true);
                up.literal(":");
                up.newline();
            }
            if (up.references().containsKey(currentStmt)) {
                up.unitRef(currentStmt, false);
            }
            up.startUnit(currentStmt);
            currentStmt.toString(up);
            up.endUnit(currentStmt);
            up.literal(";");
            up.newline();
            if (Options.v().print_tags_in_output()) {
                for (Tag t : currentStmt.getTags()) {
                    up.noIndent();
                    up.literal("/*");
                    up.literal(t.toString());
                    up.literal("*/");
                    up.newline();
                }
            }
        }
        out.print(up.toString());
        if (addJimpleLn()) {
            setJimpleLnNum(up.getPositionTagger().getEndLn());
        }
        Iterator<Trap> trapIt = body.getTraps().iterator();
        if (trapIt.hasNext()) {
            out.println();
            incJimpleLnNum();
            do {
                Trap trap = trapIt.next();
                Map<Unit, String> lbls = up.labels();
                out.println("        catch " + printSignature(trap.getException()) + " from " + lbls.get(trap.getBeginUnit()) + " to " + lbls.get(trap.getEndUnit()) + " with " + lbls.get(trap.getHandlerUnit()) + ";");
                incJimpleLnNum();
            } while (trapIt.hasNext());
        }
    }

    private int addJimpleLnTags(int lnNum, Host h) {
        h.addTag(new JimpleLineNumberTag(lnNum));
        return lnNum + 1;
    }

    private void printLocalsInBody(Body body, UnitPrinter up) {
        Map<Type, List<Local>> typeToLocals = new DeterministicHashMap<>((body.getLocalCount() * 2) + 1, 0.7f);
        for (Local local : body.getLocals()) {
            Type t = local.getType();
            List<Local> localList = typeToLocals.get(t);
            if (localList == null) {
                List<Local> arrayList = new ArrayList<>();
                localList = arrayList;
                typeToLocals.put(t, arrayList);
            }
            localList.add(local);
        }
        for (Map.Entry<Type, List<Local>> e : typeToLocals.entrySet()) {
            up.type(e.getKey());
            up.literal(Instruction.argsep);
            Iterator<Local> it = e.getValue().iterator();
            while (it.hasNext()) {
                Local l = it.next();
                up.local(l);
                if (it.hasNext()) {
                    up.literal(", ");
                }
            }
            up.literal(";");
            up.newline();
        }
        if (!typeToLocals.isEmpty()) {
            up.newline();
        }
    }
}
