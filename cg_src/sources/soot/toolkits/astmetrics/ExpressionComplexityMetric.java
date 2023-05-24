package soot.toolkits.astmetrics;

import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.visit.NodeVisitor;
/* loaded from: gencallgraphv3.jar:soot/toolkits/astmetrics/ExpressionComplexityMetric.class */
public class ExpressionComplexityMetric extends ASTMetric {
    int currentExprDepth;
    int exprDepthSum;
    int exprCount;
    int inExpr;

    public ExpressionComplexityMetric(Node node) {
        super(node);
    }

    @Override // soot.toolkits.astmetrics.ASTMetric
    public void reset() {
        this.currentExprDepth = 0;
        this.exprDepthSum = 0;
        this.exprCount = 0;
        this.inExpr = 0;
    }

    @Override // soot.toolkits.astmetrics.ASTMetric
    public void addMetrics(ClassData data) {
        double a = this.exprDepthSum;
        double b = this.exprCount;
        data.addMetric(new MetricData("Expr-Complexity", new Double(a)));
        data.addMetric(new MetricData("Expr-Count", new Double(b)));
    }

    @Override // polyglot.visit.NodeVisitor
    public NodeVisitor enter(Node parent, Node n) {
        if (n instanceof Expr) {
            this.inExpr++;
            this.currentExprDepth++;
        }
        return enter(n);
    }

    @Override // polyglot.visit.NodeVisitor
    public Node leave(Node old, Node n, NodeVisitor v) {
        if (n instanceof Expr) {
            if (this.currentExprDepth == 1) {
                this.exprCount++;
                this.exprDepthSum += this.inExpr;
                this.inExpr = 0;
            }
            this.currentExprDepth--;
        }
        return super.leave(old, n, v);
    }
}
