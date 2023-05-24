package soot.toolkits.scalar;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Local;
import soot.Timers;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.options.Options;
import soot.toolkits.graph.DirectedBodyGraph;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/SimpleLiveLocals.class */
public class SimpleLiveLocals implements LiveLocals {
    private static final Logger logger = LoggerFactory.getLogger(SimpleLiveLocals.class);
    private final FlowAnalysis<Unit, FlowSet<Local>> analysis;

    public SimpleLiveLocals(DirectedBodyGraph<Unit> graph) {
        if (Options.v().verbose()) {
            logger.debug("[" + graph.getBody().getMethod().getName() + "]     Constructing SimpleLiveLocals...");
        }
        if (Options.v().time()) {
            Timers.v().liveTimer.start();
        }
        this.analysis = new Analysis(graph);
        if (Options.v().time()) {
            Timers.v().liveAnalysisTimer.start();
        }
        this.analysis.doAnalysis();
        if (Options.v().time()) {
            Timers.v().liveAnalysisTimer.end();
            Timers.v().liveTimer.end();
        }
    }

    @Override // soot.toolkits.scalar.LiveLocals
    public List<Local> getLiveLocalsAfter(Unit s) {
        return this.analysis.getFlowAfter(s).toList();
    }

    @Override // soot.toolkits.scalar.LiveLocals
    public List<Local> getLiveLocalsBefore(Unit s) {
        return this.analysis.getFlowBefore(s).toList();
    }

    /* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/SimpleLiveLocals$Analysis.class */
    private static class Analysis extends BackwardFlowAnalysis<Unit, FlowSet<Local>> {
        Analysis(DirectedBodyGraph<Unit> g) {
            super(g);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // soot.toolkits.scalar.AbstractFlowAnalysis
        public FlowSet<Local> newInitialFlow() {
            return new ArraySparseSet();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // soot.toolkits.scalar.FlowAnalysis
        public void flowThrough(FlowSet<Local> in, Unit unit, FlowSet<Local> out) {
            in.copy(out);
            for (ValueBox box : unit.getDefBoxes()) {
                Value v = box.getValue();
                if (v instanceof Local) {
                    out.remove((Local) v);
                }
            }
            for (ValueBox box2 : unit.getUseBoxes()) {
                Value v2 = box2.getValue();
                if (v2 instanceof Local) {
                    out.add((Local) v2);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // soot.toolkits.scalar.AbstractFlowAnalysis
        public void mergeInto(Unit succNode, FlowSet<Local> inout, FlowSet<Local> in) {
            inout.union(in);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // soot.toolkits.scalar.AbstractFlowAnalysis
        public void merge(FlowSet<Local> in1, FlowSet<Local> in2, FlowSet<Local> out) {
            in1.union(in2, out);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // soot.toolkits.scalar.AbstractFlowAnalysis
        public void copy(FlowSet<Local> source, FlowSet<Local> dest) {
            source.copy(dest);
        }
    }
}
