package soot.jimple.toolkits.annotation.nullcheck;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.PhaseOptions;
import soot.Scene;
import soot.Singletons;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.LengthExpr;
import soot.jimple.MonitorStmt;
import soot.jimple.Stmt;
import soot.jimple.ThrowStmt;
import soot.jimple.toolkits.annotation.tags.NullCheckTag;
import soot.options.Options;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/nullcheck/NullPointerChecker.class */
public class NullPointerChecker extends BodyTransformer {
    private static final Logger logger = LoggerFactory.getLogger(NullPointerChecker.class);

    public NullPointerChecker(Singletons.Global g) {
    }

    public static NullPointerChecker v() {
        return G.v().soot_jimple_toolkits_annotation_nullcheck_NullPointerChecker();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
        boolean isProfiling = PhaseOptions.getBoolean(options, "profiling");
        boolean enableOther = !PhaseOptions.getBoolean(options, "onlyarrayref");
        Date start = new Date();
        if (Options.v().verbose()) {
            logger.debug("[npc] Null pointer check for " + body.getMethod().getName() + " started on " + start);
        }
        BranchedRefVarsAnalysis analysis = new BranchedRefVarsAnalysis(ExceptionalUnitGraphFactory.createExceptionalUnitGraph(body));
        SootMethod increase = isProfiling ? Scene.v().loadClassAndSupport("MultiCounter").getMethod("void increase(int)") : null;
        Chain<Unit> units = body.getUnits();
        Iterator<Unit> stmtIt = units.snapshotIterator();
        while (stmtIt.hasNext()) {
            Stmt s = (Stmt) stmtIt.next();
            Value obj = null;
            if (s.containsArrayRef()) {
                obj = s.getArrayRef().getBase();
            } else if (enableOther) {
                if (!(s instanceof ThrowStmt)) {
                    if (s instanceof MonitorStmt) {
                        obj = ((MonitorStmt) s).getOp();
                    } else {
                        Iterator<ValueBox> it = s.getDefBoxes().iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            ValueBox vBox = it.next();
                            Value v = vBox.getValue();
                            if (v instanceof InstanceFieldRef) {
                                obj = ((InstanceFieldRef) v).getBase();
                                break;
                            } else if (v instanceof InstanceInvokeExpr) {
                                obj = ((InstanceInvokeExpr) v).getBase();
                                break;
                            } else if (v instanceof LengthExpr) {
                                obj = ((LengthExpr) v).getOp();
                                break;
                            }
                        }
                        Iterator<ValueBox> it2 = s.getUseBoxes().iterator();
                        while (true) {
                            if (!it2.hasNext()) {
                                break;
                            }
                            ValueBox vBox2 = it2.next();
                            Value v2 = vBox2.getValue();
                            if (v2 instanceof InstanceFieldRef) {
                                obj = ((InstanceFieldRef) v2).getBase();
                                break;
                            } else if (v2 instanceof InstanceInvokeExpr) {
                                obj = ((InstanceInvokeExpr) v2).getBase();
                                break;
                            } else if (v2 instanceof LengthExpr) {
                                obj = ((LengthExpr) v2).getOp();
                                break;
                            }
                        }
                    }
                } else {
                    obj = ((ThrowStmt) s).getOp();
                }
            }
            if (obj != null) {
                boolean needCheck = analysis.anyRefInfo(obj, analysis.getFlowBefore(s)) != 2;
                if (isProfiling) {
                    int count = needCheck ? 5 : 6;
                    Jimple jimp = Jimple.v();
                    units.insertBefore(jimp.newInvokeStmt(jimp.newStaticInvokeExpr(increase.makeRef(), IntConstant.v(count))), s);
                }
                s.addTag(new NullCheckTag(needCheck));
            }
        }
        if (Options.v().verbose()) {
            Date finish = new Date();
            long runtime = finish.getTime() - start.getTime();
            long mins = runtime / 60000;
            long secs = (runtime % 60000) / 1000;
            logger.debug("[npc] Null pointer checker finished. It took " + mins + " mins and " + secs + " secs.");
        }
    }
}
