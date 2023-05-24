package soot.jimple.toolkits.infoflow;

import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.EquivalentValue;
import soot.SootField;
import soot.SootMethod;
import soot.Value;
import soot.coffi.Instruction;
import soot.toolkits.graph.UnitGraph;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/infoflow/SimpleMethodLocalObjectsAnalysis.class */
public class SimpleMethodLocalObjectsAnalysis extends SimpleMethodInfoFlowAnalysis {
    private static final Logger logger = LoggerFactory.getLogger(SimpleMethodLocalObjectsAnalysis.class);
    public static int mlocounter = 0;

    public SimpleMethodLocalObjectsAnalysis(UnitGraph g, ClassLocalObjectsAnalysis cloa, InfoFlowAnalysis dfa) {
        super(g, dfa, true, true);
        mlocounter++;
        this.printMessages = false;
        SootMethod method = g.getBody().getMethod();
        AbstractDataSource sharedDataSource = new AbstractDataSource(new String("SHARED"));
        for (int i = 0; i < method.getParameterCount(); i++) {
            EquivalentValue paramEqVal = InfoFlowAnalysis.getNodeForParameterRef(method, i);
            if (!cloa.parameterIsLocal(method, paramEqVal)) {
                addToEntryInitialFlow(sharedDataSource, paramEqVal.getValue());
                addToNewInitialFlow(sharedDataSource, paramEqVal.getValue());
            }
        }
        for (SootField sf : cloa.getSharedFields()) {
            EquivalentValue fieldRefEqVal = InfoFlowAnalysis.getNodeForFieldRef(method, sf);
            addToEntryInitialFlow(sharedDataSource, fieldRefEqVal.getValue());
            addToNewInitialFlow(sharedDataSource, fieldRefEqVal.getValue());
        }
        if (this.printMessages) {
            logger.debug("----- STARTING SHARED/LOCAL ANALYSIS FOR " + g.getBody().getMethod() + " -----");
        }
        doFlowInsensitiveAnalysis();
        if (this.printMessages) {
            logger.debug("----- ENDING   SHARED/LOCAL ANALYSIS FOR " + g.getBody().getMethod() + " -----");
        }
    }

    public SimpleMethodLocalObjectsAnalysis(UnitGraph g, CallLocalityContext context, InfoFlowAnalysis dfa) {
        super(g, dfa, true, true);
        mlocounter++;
        this.printMessages = false;
        g.getBody().getMethod();
        AbstractDataSource sharedDataSource = new AbstractDataSource(new String("SHARED"));
        List<Object> sharedRefs = context.getSharedRefs();
        Iterator<Object> sharedRefEqValIt = sharedRefs.iterator();
        while (sharedRefEqValIt.hasNext()) {
            EquivalentValue refEqVal = (EquivalentValue) sharedRefEqValIt.next();
            addToEntryInitialFlow(sharedDataSource, refEqVal.getValue());
            addToNewInitialFlow(sharedDataSource, refEqVal.getValue());
        }
        if (this.printMessages) {
            logger.debug("----- STARTING SHARED/LOCAL ANALYSIS FOR " + g.getBody().getMethod() + " -----");
            logger.debug("      " + context.toString().replaceAll("\n", "\n      "));
            logger.debug("found " + sharedRefs.size() + " shared refs in context.");
        }
        doFlowInsensitiveAnalysis();
        if (this.printMessages) {
            logger.debug("----- ENDING   SHARED/LOCAL ANALYSIS FOR " + g.getBody().getMethod() + " -----");
        }
    }

    @Override // soot.jimple.toolkits.infoflow.SimpleMethodInfoFlowAnalysis
    public boolean isInterestingSource(Value source) {
        return source instanceof AbstractDataSource;
    }

    @Override // soot.jimple.toolkits.infoflow.SimpleMethodInfoFlowAnalysis
    public boolean isInterestingSink(Value sink) {
        return true;
    }

    public boolean isObjectLocal(Value local) {
        EquivalentValue source = new CachedEquivalentValue(new AbstractDataSource(new String("SHARED")));
        if (this.infoFlowGraph.containsNode(source)) {
            List sinks = this.infoFlowGraph.getSuccsOf(source);
            if (this.printMessages) {
                logger.debug("      Requested value " + local + " is " + (!sinks.contains(new CachedEquivalentValue(local)) ? "Local" : "Shared") + " in " + this.sm + Instruction.argsep);
            }
            return !sinks.contains(new CachedEquivalentValue(local));
        } else if (this.printMessages) {
            logger.debug("      Requested value " + local + " is Local (LIKE ALL VALUES) in " + this.sm + Instruction.argsep);
            return true;
        } else {
            return true;
        }
    }
}
