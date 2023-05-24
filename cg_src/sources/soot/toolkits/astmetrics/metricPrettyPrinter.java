package soot.toolkits.astmetrics;

import polyglot.ast.Node;
import polyglot.util.CodeWriter;
import polyglot.visit.PrettyPrinter;
/* loaded from: gencallgraphv3.jar:soot/toolkits/astmetrics/metricPrettyPrinter.class */
public class metricPrettyPrinter extends PrettyPrinter {
    ASTMetric astMetric;

    public metricPrettyPrinter(ASTMetric astMetric) {
        this.astMetric = astMetric;
    }

    @Override // polyglot.visit.PrettyPrinter
    public void print(Node parent, Node child, CodeWriter w) {
        this.astMetric.printAstMetric(child, w);
        super.print(parent, child, w);
    }
}
