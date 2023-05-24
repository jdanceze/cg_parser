package soot.toolkits.astmetrics;

import java.util.ArrayList;
import java.util.Iterator;
import polyglot.ast.Node;
import soot.options.Options;
/* loaded from: gencallgraphv3.jar:soot/toolkits/astmetrics/ComputeASTMetrics.class */
public class ComputeASTMetrics {
    ArrayList<ASTMetric> metrics = new ArrayList<>();

    public ComputeASTMetrics(Node astNode) {
        this.metrics.add(new AbruptEdgesMetric(astNode));
        this.metrics.add(new NumLocalsMetric(astNode));
        this.metrics.add(new ConstructNumbersMetric(astNode));
        this.metrics.add(new StmtSumWeightedByDepth(astNode));
        this.metrics.add(new ConditionComplexityMetric(astNode));
        this.metrics.add(new ExpressionComplexityMetric(astNode));
        this.metrics.add(new IdentifiersMetric(astNode));
    }

    public void apply() {
        if (!Options.v().ast_metrics()) {
            return;
        }
        Iterator<ASTMetric> metricIt = this.metrics.iterator();
        while (metricIt.hasNext()) {
            metricIt.next().execute();
        }
    }
}
