package soot.toolkits.astmetrics;

import polyglot.ast.Block;
import polyglot.ast.Do;
import polyglot.ast.For;
import polyglot.ast.If;
import polyglot.ast.Labeled;
import polyglot.ast.Node;
import polyglot.ast.Stmt;
import polyglot.ast.While;
import polyglot.visit.NodeVisitor;
/* loaded from: gencallgraphv3.jar:soot/toolkits/astmetrics/ConstructNumbersMetric.class */
public class ConstructNumbersMetric extends ASTMetric {
    private int numIf;
    private int numIfElse;
    private int numLabeledBlocks;
    private int doLoop;
    private int forLoop;
    private int whileLoop;
    private int whileTrue;

    public ConstructNumbersMetric(Node node) {
        super(node);
    }

    @Override // soot.toolkits.astmetrics.ASTMetric
    public void reset() {
        this.numIfElse = 0;
        this.numIf = 0;
        this.numLabeledBlocks = 0;
        this.whileTrue = 0;
        this.whileLoop = 0;
        this.forLoop = 0;
        this.doLoop = 0;
    }

    @Override // soot.toolkits.astmetrics.ASTMetric
    public void addMetrics(ClassData data) {
        data.addMetric(new MetricData("If", new Integer(this.numIf)));
        data.addMetric(new MetricData("IfElse", new Integer(this.numIfElse)));
        data.addMetric(new MetricData("Total-Conditionals", new Integer(this.numIf + this.numIfElse)));
        data.addMetric(new MetricData("LabelBlock", new Integer(this.numLabeledBlocks)));
        data.addMetric(new MetricData("Do", new Integer(this.doLoop)));
        data.addMetric(new MetricData("For", new Integer(this.forLoop)));
        data.addMetric(new MetricData("While", new Integer(this.whileLoop)));
        data.addMetric(new MetricData("UnConditional", new Integer(this.whileTrue)));
        data.addMetric(new MetricData("Total Loops", new Integer(this.whileTrue + this.whileLoop + this.forLoop + this.doLoop)));
    }

    @Override // polyglot.visit.NodeVisitor
    public NodeVisitor enter(Node parent, Node n) {
        if (n instanceof If) {
            If ifNode = (If) n;
            Stmt temp = ifNode.alternative();
            if (temp == null) {
                this.numIf++;
            } else {
                this.numIfElse++;
            }
        }
        if (n instanceof Labeled) {
            Stmt s = ((Labeled) n).statement();
            if (s instanceof Block) {
                this.numLabeledBlocks++;
            }
        }
        if (n instanceof Do) {
            this.doLoop++;
        }
        if (n instanceof For) {
            this.forLoop++;
        }
        if (n instanceof While) {
            if (((While) n).condIsConstantTrue()) {
                this.whileTrue++;
            } else {
                this.whileLoop++;
            }
        }
        return enter(n);
    }
}
