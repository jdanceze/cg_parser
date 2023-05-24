package soot.toolkits.astmetrics;

import polyglot.ast.Binary;
import polyglot.ast.Expr;
import polyglot.ast.If;
import polyglot.ast.Loop;
import polyglot.ast.Node;
import polyglot.ast.Unary;
import polyglot.visit.NodeVisitor;
/* loaded from: gencallgraphv3.jar:soot/toolkits/astmetrics/ConditionComplexityMetric.class */
public class ConditionComplexityMetric extends ASTMetric {
    int loopComplexity;
    int ifComplexity;

    public ConditionComplexityMetric(Node node) {
        super(node);
    }

    @Override // soot.toolkits.astmetrics.ASTMetric
    public void reset() {
        this.ifComplexity = 0;
        this.loopComplexity = 0;
    }

    @Override // soot.toolkits.astmetrics.ASTMetric
    public void addMetrics(ClassData data) {
        data.addMetric(new MetricData("Loop-Cond-Complexity", new Integer(this.loopComplexity)));
        data.addMetric(new MetricData("If-Cond-Complexity", new Integer(this.ifComplexity)));
        data.addMetric(new MetricData("Total-Cond-Complexity", new Integer(this.loopComplexity + this.ifComplexity)));
    }

    @Override // polyglot.visit.NodeVisitor
    public NodeVisitor enter(Node parent, Node n) {
        if (n instanceof Loop) {
            Expr expr = ((Loop) n).cond();
            this.loopComplexity = (int) (this.loopComplexity + condComplexity(expr));
        } else if (n instanceof If) {
            Expr expr2 = ((If) n).cond();
            this.ifComplexity = (int) (this.ifComplexity + condComplexity(expr2));
        }
        return enter(n);
    }

    private double condComplexity(Expr expr) {
        if (expr instanceof Binary) {
            Binary b = (Binary) expr;
            if (b.operator() == Binary.COND_AND || b.operator() == Binary.COND_OR) {
                return 1.0d + condComplexity(b.left()) + condComplexity(b.right());
            }
            return 0.5d + condComplexity(b.left()) + condComplexity(b.right());
        } else if (expr instanceof Unary) {
            if (((Unary) expr).operator() == Unary.NOT) {
                return 0.5d + condComplexity(((Unary) expr).expr());
            }
            return condComplexity(((Unary) expr).expr());
        } else {
            return 1.0d;
        }
    }
}
