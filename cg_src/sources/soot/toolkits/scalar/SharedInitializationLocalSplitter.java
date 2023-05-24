package soot.toolkits.scalar;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Local;
import soot.Scene;
import soot.Singletons;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.dexpler.DexNullArrayRefTransformer;
import soot.dexpler.DexNullThrowTransformer;
import soot.jimple.AssignStmt;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.Jimple;
import soot.jimple.toolkits.scalar.ConstantPropagatorAndFolder;
import soot.jimple.toolkits.scalar.CopyPropagator;
import soot.jimple.toolkits.scalar.DeadAssignmentEliminator;
import soot.options.Options;
import soot.toolkits.exceptions.ThrowAnalysis;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
import soot.toolkits.graph.UnitGraph;
import soot.util.Chain;
import soot.util.HashMultiMap;
import soot.util.MultiMap;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/SharedInitializationLocalSplitter.class */
public class SharedInitializationLocalSplitter extends BodyTransformer {
    private static final Logger logger = LoggerFactory.getLogger(SharedInitializationLocalSplitter.class);
    protected ThrowAnalysis throwAnalysis;
    protected boolean omitExceptingUnitEdges;

    public SharedInitializationLocalSplitter(Singletons.Global g) {
    }

    public SharedInitializationLocalSplitter(ThrowAnalysis ta) {
        this(ta, false);
    }

    public SharedInitializationLocalSplitter(ThrowAnalysis ta, boolean omitExceptingUnitEdges) {
        this.throwAnalysis = ta;
        this.omitExceptingUnitEdges = omitExceptingUnitEdges;
    }

    public static SharedInitializationLocalSplitter v() {
        return G.v().soot_toolkits_scalar_SharedInitializationLocalSplitter();
    }

    /* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/SharedInitializationLocalSplitter$Cluster.class */
    private static final class Cluster {
        protected final List<Unit> constantInitializers;
        protected final Unit use;

        public Cluster(Unit use, List<Unit> constantInitializers) {
            this.use = use;
            this.constantInitializers = constantInitializers;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Constant intializers:\n");
            for (Unit r : this.constantInitializers) {
                sb.append("\n - ").append(toStringUnit(r));
            }
            return sb.toString();
        }

        private String toStringUnit(Unit u) {
            return u + " (" + System.identityHashCode(u) + ")";
        }
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
        if (Options.v().verbose()) {
            logger.debug("[" + body.getMethod().getName() + "] Splitting for shared initialization of locals...");
        }
        if (this.throwAnalysis == null) {
            this.throwAnalysis = Scene.v().getDefaultThrowAnalysis();
        }
        if (!this.omitExceptingUnitEdges) {
            this.omitExceptingUnitEdges = Options.v().omit_excepting_unit_edges();
        }
        CopyPropagator.v().transform(body);
        ConstantPropagatorAndFolder.v().transform(body);
        DexNullThrowTransformer.v().transform(body);
        DexNullArrayRefTransformer.v().transform(body);
        FlowSensitiveConstantPropagator.v().transform(body);
        CopyPropagator.v().transform(body);
        DexNullThrowTransformer.v().transform(body);
        DexNullArrayRefTransformer.v().transform(body);
        DeadAssignmentEliminator.v().transform(body);
        CopyPropagator.v().transform(body);
        ExceptionalUnitGraph graph = ExceptionalUnitGraphFactory.createExceptionalUnitGraph(body, this.throwAnalysis, this.omitExceptingUnitEdges);
        LocalDefs defs = G.v().soot_toolkits_scalar_LocalDefsFactory().newLocalDefs((UnitGraph) graph, true);
        MultiMap<Local, Cluster> clustersPerLocal = new HashMultiMap<>();
        Chain<Unit> units = body.getUnits();
        for (Unit s : units) {
            for (ValueBox useBox : s.getUseBoxes()) {
                Value v = useBox.getValue();
                if (v instanceof Local) {
                    Local luse = (Local) v;
                    List<Unit> allAffectingDefs = defs.getDefsOfAt(luse, s);
                    if (!allAffectingDefs.isEmpty() && allAffectingDefs.stream().allMatch(u -> {
                        return (u instanceof AssignStmt) && (((AssignStmt) u).getRightOp() instanceof Constant);
                    })) {
                        clustersPerLocal.put(luse, new Cluster(s, allAffectingDefs));
                    }
                }
            }
        }
        Chain<Local> locals = body.getLocals();
        int w = 0;
        for (Local lcl : clustersPerLocal.keySet()) {
            Set<Cluster> clusters = clustersPerLocal.get(lcl);
            if (clusters.size() > 1) {
                for (Cluster cluster : clusters) {
                    Local newLocal = (Local) lcl.clone();
                    w++;
                    newLocal.setName(String.valueOf(newLocal.getName()) + '_' + w);
                    locals.add(newLocal);
                    for (Unit u2 : cluster.constantInitializers) {
                        AssignStmt assign = (AssignStmt) u2;
                        AssignStmt newAssign = Jimple.v().newAssignStmt(newLocal, assign.getRightOp());
                        units.insertAfter(newAssign, assign);
                        CopyPropagator.copyLineTags(newAssign.getUseBoxes().get(0), (DefinitionStmt) assign);
                    }
                    replaceLocalsInUnitUses(cluster.use, lcl, newLocal);
                }
            }
        }
    }

    private void replaceLocalsInUnitUses(Unit change, Value oldLocal, Local newLocal) {
        for (ValueBox u : change.getUseBoxes()) {
            if (u.getValue() == oldLocal) {
                u.setValue(newLocal);
            }
        }
    }
}
