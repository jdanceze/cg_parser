package soot.jimple.toolkits.scalar;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Local;
import soot.RefType;
import soot.Singletons;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.CastExpr;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.NullConstant;
import soot.jimple.NumericConstant;
import soot.jimple.StringConstant;
import soot.options.Options;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
import soot.toolkits.graph.PseudoTopologicalOrderer;
import soot.toolkits.scalar.LocalDefs;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/ConstantPropagatorAndFolder.class */
public class ConstantPropagatorAndFolder extends BodyTransformer {
    private static final Logger logger = LoggerFactory.getLogger(ConstantPropagatorAndFolder.class);

    public ConstantPropagatorAndFolder(Singletons.Global g) {
    }

    public static ConstantPropagatorAndFolder v() {
        return G.v().soot_jimple_toolkits_scalar_ConstantPropagatorAndFolder();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        int numFolded = 0;
        int numPropagated = 0;
        if (Options.v().verbose()) {
            logger.debug("[" + b.getMethod().getName() + "] Propagating and folding constants...");
        }
        ExceptionalUnitGraph createExceptionalUnitGraph = ExceptionalUnitGraphFactory.createExceptionalUnitGraph(b);
        LocalDefs localDefs = G.v().soot_toolkits_scalar_LocalDefsFactory().newLocalDefs(createExceptionalUnitGraph);
        for (Unit u : new PseudoTopologicalOrderer().newList(createExceptionalUnitGraph, false)) {
            for (ValueBox useBox : u.getUseBoxes()) {
                Value value = useBox.getValue();
                if (value instanceof Local) {
                    Local local = (Local) value;
                    List<Unit> defsOfUse = localDefs.getDefsOfAt(local, u);
                    if (defsOfUse.size() == 1) {
                        DefinitionStmt defStmt = (DefinitionStmt) defsOfUse.get(0);
                        Value rhs = defStmt.getRightOp();
                        if ((rhs instanceof NumericConstant) || (rhs instanceof StringConstant) || (rhs instanceof NullConstant)) {
                            if (useBox.canContainValue(rhs)) {
                                useBox.setValue(rhs);
                                numPropagated++;
                            }
                        } else if (rhs instanceof CastExpr) {
                            CastExpr ce = (CastExpr) rhs;
                            if ((ce.getCastType() instanceof RefType) && (ce.getOp() instanceof NullConstant)) {
                                defStmt.getRightOpBox().setValue(NullConstant.v());
                                numPropagated++;
                            }
                        }
                    }
                }
            }
            for (ValueBox useBox2 : u.getUseBoxes()) {
                Value value2 = useBox2.getValue();
                if (!(value2 instanceof Constant) && Evaluator.isValueConstantValued(value2)) {
                    Value constValue = Evaluator.getConstantValueOf(value2);
                    if (useBox2.canContainValue(constValue)) {
                        useBox2.setValue(constValue);
                        numFolded++;
                    }
                }
            }
        }
        if (Options.v().verbose()) {
            logger.debug("[" + b.getMethod().getName() + "]     Propagated: " + numPropagated + ", Folded:  " + numFolded);
        }
    }
}
