package soot.jimple.toolkits.scalar;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.EquivalentValue;
import soot.G;
import soot.Local;
import soot.PhaseOptions;
import soot.Scene;
import soot.SideEffectTester;
import soot.Singletons;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.Jimple;
import soot.jimple.NaiveSideEffectTester;
import soot.jimple.toolkits.pointer.PASideEffectTester;
import soot.options.Options;
import soot.tagkit.StringTag;
import soot.toolkits.scalar.UnitValueBoxPair;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/CommonSubexpressionEliminator.class */
public class CommonSubexpressionEliminator extends BodyTransformer {
    private static final Logger logger = LoggerFactory.getLogger(CommonSubexpressionEliminator.class);

    public CommonSubexpressionEliminator(Singletons.Global g) {
    }

    public static CommonSubexpressionEliminator v() {
        return G.v().soot_jimple_toolkits_scalar_CommonSubexpressionEliminator();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        SideEffectTester sideEffect;
        String newName;
        Chain<Local> locals = b.getLocals();
        Set<String> localNames = new HashSet<>(locals.size());
        for (Local loc : locals) {
            localNames.add(loc.getName());
        }
        if (Scene.v().hasCallGraph() && !PhaseOptions.getBoolean(options, "naive-side-effect")) {
            sideEffect = new PASideEffectTester();
        } else {
            sideEffect = new NaiveSideEffectTester();
        }
        sideEffect.newMethod(b.getMethod());
        if (Options.v().verbose()) {
            logger.debug("[" + b.getMethod().getName() + "]     Eliminating common subexpressions " + (sideEffect instanceof NaiveSideEffectTester ? "(naively)" : "") + "...");
        }
        AvailableExpressions ae = new FastAvailableExpressions(b, sideEffect);
        int counter = 0;
        Chain<Unit> units = b.getUnits();
        Iterator<Unit> unitsIt = units.snapshotIterator();
        while (unitsIt.hasNext()) {
            Unit u = unitsIt.next();
            if (u instanceof AssignStmt) {
                Value v = ((AssignStmt) u).getRightOp();
                EquivalentValue ev = new EquivalentValue(v);
                if (ae.getAvailableEquivsBefore(u).contains(ev)) {
                    for (UnitValueBoxPair up : ae.getAvailablePairsBefore(u)) {
                        if (up.getValueBox().getValue().equivTo(v)) {
                            do {
                                newName = "$cseTmp" + counter;
                                counter++;
                            } while (localNames.contains(newName));
                            Local l = Jimple.v().newLocal(newName, Type.toMachineType(v.getType()));
                            locals.add(l);
                            AssignStmt origCalc = (AssignStmt) up.getUnit();
                            Unit copier = Jimple.v().newAssignStmt(origCalc.getLeftOp(), l);
                            origCalc.setLeftOp(l);
                            units.insertAfter(copier, origCalc);
                            ((AssignStmt) u).setRightOp(l);
                            copier.addTag(new StringTag("Common sub-expression"));
                            u.addTag(new StringTag("Common sub-expression"));
                        }
                    }
                }
            }
        }
        if (Options.v().verbose()) {
            logger.debug("[" + b.getMethod().getName() + "]     Eliminating common subexpressions done!");
        }
    }
}
