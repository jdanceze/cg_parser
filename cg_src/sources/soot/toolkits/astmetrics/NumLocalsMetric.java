package soot.toolkits.astmetrics;

import polyglot.ast.LocalDecl;
import polyglot.ast.Node;
import polyglot.visit.NodeVisitor;
/* loaded from: gencallgraphv3.jar:soot/toolkits/astmetrics/NumLocalsMetric.class */
public class NumLocalsMetric extends ASTMetric {
    int numLocals;

    public NumLocalsMetric(Node node) {
        super(node);
    }

    @Override // soot.toolkits.astmetrics.ASTMetric
    public void reset() {
        this.numLocals = 0;
    }

    @Override // soot.toolkits.astmetrics.ASTMetric
    public void addMetrics(ClassData data) {
        data.addMetric(new MetricData("Number-Locals", new Integer(this.numLocals)));
    }

    @Override // polyglot.visit.NodeVisitor
    public NodeVisitor enter(Node parent, Node n) {
        if (n instanceof LocalDecl) {
            this.numLocals++;
        }
        return enter(n);
    }
}
