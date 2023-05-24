package soot.toolkits.astmetrics;

import java.util.Iterator;
import polyglot.ast.ClassDecl;
import polyglot.ast.Node;
import polyglot.util.CodeWriter;
import polyglot.visit.NodeVisitor;
import soot.G;
/* loaded from: gencallgraphv3.jar:soot/toolkits/astmetrics/ASTMetric.class */
public abstract class ASTMetric extends NodeVisitor implements MetricInterface {
    Node astNode;
    String className = null;

    public abstract void reset();

    public abstract void addMetrics(ClassData classData);

    public ASTMetric(Node astNode) {
        this.astNode = astNode;
        reset();
    }

    @Override // polyglot.visit.NodeVisitor
    public final NodeVisitor enter(Node n) {
        if (n instanceof ClassDecl) {
            this.className = ((ClassDecl) n).name();
            System.out.println("Starting processing: " + this.className);
        }
        return this;
    }

    @Override // polyglot.visit.NodeVisitor
    public final Node leave(Node parent, Node old, Node n, NodeVisitor v) {
        if (n instanceof ClassDecl) {
            if (this.className == null) {
                throw new RuntimeException("className is null");
            }
            System.out.println("Done with class " + this.className);
            ClassData data = getClassData();
            addMetrics(data);
            reset();
        }
        return leave(old, n, v);
    }

    @Override // soot.toolkits.astmetrics.MetricInterface
    public final void execute() {
        this.astNode.visit(this);
        System.out.println("\n\n\n PRETTY P{RINTING");
        if (this instanceof StmtSumWeightedByDepth) {
            metricPrettyPrinter p = new metricPrettyPrinter(this);
            p.printAst(this.astNode, new CodeWriter(System.out, 80));
        }
    }

    public void printAstMetric(Node n, CodeWriter w) {
    }

    public final ClassData getClassData() {
        if (this.className == null) {
            throw new RuntimeException("className is null");
        }
        Iterator<ClassData> it = G.v().ASTMetricsData.iterator();
        while (it.hasNext()) {
            ClassData tempData = it.next();
            if (tempData.classNameEquals(this.className)) {
                return tempData;
            }
        }
        ClassData data = new ClassData(this.className);
        G.v().ASTMetricsData.add(data);
        return data;
    }
}
