package soot.jimple.spark;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.G;
import soot.Local;
import soot.PointsToAnalysis;
import soot.Scene;
import soot.SceneTransformer;
import soot.Singletons;
import soot.SootClass;
import soot.SootMethod;
import soot.SourceLocator;
import soot.Unit;
import soot.Value;
import soot.coffi.Instruction;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.ReachingTypeDumper;
import soot.jimple.Stmt;
import soot.jimple.spark.builder.ContextInsensitiveBuilder;
import soot.jimple.spark.geom.geomPA.GeomPointsTo;
import soot.jimple.spark.ondemand.DemandCSPointsTo;
import soot.jimple.spark.pag.AllocDotField;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.PAG2HTML;
import soot.jimple.spark.pag.PAGDumper;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.jimple.spark.solver.EBBCollapser;
import soot.jimple.spark.solver.PropAlias;
import soot.jimple.spark.solver.PropCycle;
import soot.jimple.spark.solver.PropIter;
import soot.jimple.spark.solver.PropMerge;
import soot.jimple.spark.solver.PropWorklist;
import soot.jimple.spark.solver.Propagator;
import soot.jimple.spark.solver.SCCCollapser;
import soot.jimple.toolkits.callgraph.CallGraphBuilder;
import soot.options.SparkOptions;
import soot.tagkit.Host;
import soot.tagkit.StringTag;
import soot.tagkit.Tag;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/SparkTransformer.class */
public class SparkTransformer extends SceneTransformer {
    private static final Logger logger = LoggerFactory.getLogger(SparkTransformer.class);

    public SparkTransformer(Singletons.Global g) {
    }

    public static SparkTransformer v() {
        return G.v().soot_jimple_spark_SparkTransformer();
    }

    @Override // soot.SceneTransformer
    protected void internalTransform(String phaseName, Map<String, String> options) {
        SparkOptions opts = new SparkOptions(options);
        String output_dir = SourceLocator.v().getOutputDir();
        ContextInsensitiveBuilder b = new ContextInsensitiveBuilder();
        if (opts.pre_jimplify()) {
            b.preJimplify();
        }
        if (opts.force_gc()) {
            doGC();
        }
        Date startBuild = new Date();
        PAG pag = b.setup(opts);
        b.build();
        Date endBuild = new Date();
        reportTime("Pointer Assignment Graph", startBuild, endBuild);
        if (opts.force_gc()) {
            doGC();
        }
        Date startTM = new Date();
        pag.getTypeManager().makeTypeMask();
        Date endTM = new Date();
        reportTime("Type masks", startTM, endTM);
        if (opts.force_gc()) {
            doGC();
        }
        if (opts.verbose()) {
            logger.debug("VarNodes: " + pag.getVarNodeNumberer().size());
            logger.debug("FieldRefNodes: " + pag.getFieldRefNodeNumberer().size());
            logger.debug("AllocNodes: " + pag.getAllocNodeNumberer().size());
        }
        Date startSimplify = new Date();
        if ((opts.simplify_sccs() && !opts.on_fly_cg()) || opts.vta()) {
            new SCCCollapser(pag, opts.ignore_types_for_sccs()).collapse();
        }
        if (opts.simplify_offline() && !opts.on_fly_cg()) {
            new EBBCollapser(pag).collapse();
        }
        pag.cleanUpMerges();
        Date endSimplify = new Date();
        reportTime("Pointer Graph simplified", startSimplify, endSimplify);
        if (opts.force_gc()) {
            doGC();
        }
        PAGDumper dumper = null;
        if (opts.dump_pag() || opts.dump_solution()) {
            dumper = new PAGDumper(pag, output_dir);
        }
        if (opts.dump_pag()) {
            dumper.dump();
        }
        Date startProp = new Date();
        propagatePAG(opts, pag);
        Date endProp = new Date();
        reportTime("Propagation", startProp, endProp);
        reportTime("Solution found", startSimplify, endProp);
        if (opts.force_gc()) {
            doGC();
        }
        if (!opts.on_fly_cg() || opts.vta()) {
            CallGraphBuilder cgb = new CallGraphBuilder(pag);
            cgb.build();
        }
        if (opts.verbose()) {
            logger.debug("[Spark] Number of reachable methods: " + Scene.v().getReachableMethods().size());
        }
        if (opts.set_mass()) {
            findSetMass(pag);
        }
        if (opts.dump_answer()) {
            new ReachingTypeDumper(pag, output_dir).dump();
        }
        if (opts.dump_solution()) {
            dumper.dumpPointsToSets();
        }
        if (opts.dump_html()) {
            new PAG2HTML(pag, output_dir).dump();
        }
        Scene.v().setPointsToAnalysis(pag);
        if (opts.add_tags()) {
            addTags(pag);
        }
        if (opts.geom_pta()) {
            if (opts.simplify_offline() || opts.simplify_sccs()) {
                logger.debug("Please turn off the simplify-offline and simplify-sccs to run the geometric points-to analysis");
                logger.debug("Now, we keep the SPARK result for querying.");
            } else {
                GeomPointsTo geomPTA = (GeomPointsTo) pag;
                geomPTA.parametrize(endProp.getTime() - startSimplify.getTime());
                geomPTA.solve();
            }
        }
        if (opts.cs_demand()) {
            Date startOnDemand = new Date();
            PointsToAnalysis onDemandAnalysis = DemandCSPointsTo.makeWithBudget(opts.traversal(), opts.passes(), opts.lazy_pts());
            Date endOndemand = new Date();
            reportTime("Initialized on-demand refinement-based context-sensitive analysis", startOnDemand, endOndemand);
            Scene.v().setPointsToAnalysis(onDemandAnalysis);
        }
    }

    protected void propagatePAG(SparkOptions opts, PAG pag) {
        Propagator propagator = null;
        switch (opts.propagator()) {
            case 1:
                propagator = new PropIter(pag);
                break;
            case 2:
                propagator = new PropWorklist(pag);
                break;
            case 3:
                propagator = new PropCycle(pag);
                break;
            case 4:
                propagator = new PropMerge(pag);
                break;
            case 5:
                propagator = new PropAlias(pag);
                break;
            case 6:
                break;
            default:
                throw new RuntimeException();
        }
        if (propagator != null) {
            propagator.propagate();
        }
    }

    protected void addTags(PAG pag) {
        final Tag unknown = new StringTag("Untagged Spark node");
        final Map<Node, Tag> nodeToTag = pag.getNodeTags();
        for (SootClass c : Scene.v().getClasses()) {
            for (SootMethod m : c.getMethods()) {
                if (m.isConcrete() && m.hasActiveBody()) {
                    Iterator<Unit> it = m.getActiveBody().getUnits().iterator();
                    while (it.hasNext()) {
                        Unit u = it.next();
                        final Stmt s = (Stmt) u;
                        if (s instanceof DefinitionStmt) {
                            Value lhs = ((DefinitionStmt) s).getLeftOp();
                            VarNode v = null;
                            if (lhs instanceof Local) {
                                v = pag.findLocalVarNode(lhs);
                            } else if (lhs instanceof FieldRef) {
                                v = pag.findGlobalVarNode(((FieldRef) lhs).getField());
                            }
                            if (v != null) {
                                PointsToSetInternal p2set = v.getP2Set();
                                p2set.forall(new P2SetVisitor() { // from class: soot.jimple.spark.SparkTransformer.1
                                    @Override // soot.jimple.spark.sets.P2SetVisitor
                                    public final void visit(Node n) {
                                        SparkTransformer.this.addTag(s, n, nodeToTag, unknown);
                                    }
                                });
                                Node[] simpleSources = pag.simpleInvLookup(v);
                                for (Node element : simpleSources) {
                                    addTag(s, element, nodeToTag, unknown);
                                }
                                Node[] simpleSources2 = pag.allocInvLookup(v);
                                for (Node element2 : simpleSources2) {
                                    addTag(s, element2, nodeToTag, unknown);
                                }
                                Node[] simpleSources3 = pag.loadInvLookup(v);
                                for (Node element3 : simpleSources3) {
                                    addTag(s, element3, nodeToTag, unknown);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected static void reportTime(String desc, Date start, Date end) {
        long time = end.getTime() - start.getTime();
        logger.debug("[Spark] " + desc + " in " + (time / 1000) + "." + ((time / 100) % 10) + " seconds.");
    }

    protected static void doGC() {
        System.gc();
        System.gc();
        System.gc();
        System.gc();
        System.gc();
    }

    protected void addTag(Host h, Node n, Map<Node, Tag> nodeToTag, Tag unknown) {
        if (nodeToTag.containsKey(n)) {
            h.addTag(nodeToTag.get(n));
        } else {
            h.addTag(unknown);
        }
    }

    protected void findSetMass(PAG pag) {
        int mass = 0;
        int varMass = 0;
        int adfs = 0;
        int scalars = 0;
        Iterator<VarNode> it = pag.getVarNodeNumberer().iterator();
        while (it.hasNext()) {
            VarNode v = it.next();
            scalars++;
            PointsToSetInternal set = v.getP2Set();
            if (set != null) {
                mass += set.size();
            }
            if (set != null) {
                varMass += set.size();
            }
        }
        for (AllocNode an : pag.allocSources()) {
            for (AllocDotField adf : an.getFields()) {
                PointsToSetInternal set2 = adf.getP2Set();
                if (set2 != null) {
                    mass += set2.size();
                }
                if (set2 != null && set2.size() > 0) {
                    adfs++;
                }
            }
        }
        logger.debug("Set mass: " + mass);
        logger.debug("Variable mass: " + varMass);
        logger.debug("Scalars: " + scalars);
        logger.debug("adfs: " + adfs);
        int[] deRefCounts = new int[30001];
        for (VarNode v2 : pag.getDereferences()) {
            PointsToSetInternal set3 = v2.getP2Set();
            int size = 0;
            if (set3 != null) {
                size = set3.size();
            }
            int i = size;
            deRefCounts[i] = deRefCounts[i] + 1;
        }
        int total = 0;
        for (int element : deRefCounts) {
            total += element;
        }
        logger.debug("Dereference counts BEFORE trimming (total = " + total + "):");
        for (int i2 = 0; i2 < deRefCounts.length; i2++) {
            if (deRefCounts[i2] > 0) {
                logger.debug(i2 + Instruction.argsep + deRefCounts[i2] + Instruction.argsep + ((deRefCounts[i2] * 100.0d) / total) + "%");
            }
        }
    }
}
