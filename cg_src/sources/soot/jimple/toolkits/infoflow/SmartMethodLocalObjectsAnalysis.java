package soot.jimple.toolkits.infoflow;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.EquivalentValue;
import soot.SootMethod;
import soot.Value;
import soot.coffi.Instruction;
import soot.jimple.Constant;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.Ref;
import soot.toolkits.graph.UnitGraph;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/infoflow/SmartMethodLocalObjectsAnalysis.class */
public class SmartMethodLocalObjectsAnalysis {
    private static final Logger logger = LoggerFactory.getLogger(SmartMethodLocalObjectsAnalysis.class);
    public static int counter = 0;
    static boolean printMessages;
    SootMethod method;
    InfoFlowAnalysis dfa;
    SmartMethodInfoFlowAnalysis smdfa;

    public SmartMethodLocalObjectsAnalysis(SootMethod method, InfoFlowAnalysis dfa) {
        this.method = method;
        this.dfa = dfa;
        this.smdfa = dfa.getMethodInfoFlowAnalysis(method);
        printMessages = dfa.printDebug();
        counter++;
    }

    public SmartMethodLocalObjectsAnalysis(UnitGraph g, InfoFlowAnalysis dfa) {
        this(g.getBody().getMethod(), dfa);
    }

    public Value getThisLocal() {
        return this.smdfa.getThisLocal();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean isObjectLocal(Value local, CallLocalityContext context) {
        EquivalentValue localEqVal;
        if (local instanceof InstanceFieldRef) {
            localEqVal = InfoFlowAnalysis.getNodeForFieldRef(this.method, ((FieldRef) local).getField());
        } else {
            localEqVal = new CachedEquivalentValue(local);
        }
        List<EquivalentValue> sources = this.smdfa.sourcesOf(localEqVal);
        for (EquivalentValue source : sources) {
            if (source.getValue() instanceof Ref) {
                if (!context.isFieldLocal(source)) {
                    if (printMessages) {
                        logger.debug("      Requested value " + local + " is SHARED in " + this.method + Instruction.argsep);
                        return false;
                    }
                    return false;
                }
            } else if (source.getValue() instanceof Constant) {
                if (printMessages) {
                    logger.debug("      Requested value " + local + " is SHARED in " + this.method + Instruction.argsep);
                    return false;
                }
                return false;
            }
        }
        if (printMessages) {
            logger.debug("      Requested value " + local + " is LOCAL in " + this.method + Instruction.argsep);
            return true;
        }
        return true;
    }

    public static boolean isObjectLocal(InfoFlowAnalysis dfa, SootMethod method, CallLocalityContext context, Value local) {
        EquivalentValue localEqVal;
        SmartMethodInfoFlowAnalysis smdfa = dfa.getMethodInfoFlowAnalysis(method);
        if (local instanceof InstanceFieldRef) {
            localEqVal = InfoFlowAnalysis.getNodeForFieldRef(method, ((FieldRef) local).getField());
        } else {
            localEqVal = new CachedEquivalentValue(local);
        }
        List<EquivalentValue> sources = smdfa.sourcesOf(localEqVal);
        for (EquivalentValue source : sources) {
            if ((source.getValue() instanceof Ref) && !context.isFieldLocal(source)) {
                if (printMessages) {
                    logger.debug("      Requested value " + local + " is LOCAL in " + method + Instruction.argsep);
                    return false;
                }
                return false;
            }
        }
        if (printMessages) {
            logger.debug("      Requested value " + local + " is SHARED in " + method + Instruction.argsep);
            return true;
        }
        return true;
    }
}
