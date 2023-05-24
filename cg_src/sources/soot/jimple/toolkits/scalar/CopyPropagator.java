package soot.jimple.toolkits.scalar;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Local;
import soot.NullType;
import soot.RefLikeType;
import soot.Scene;
import soot.Singletons;
import soot.Timers;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.IntConstant;
import soot.jimple.LongConstant;
import soot.jimple.NullConstant;
import soot.jimple.Stmt;
import soot.options.CPOptions;
import soot.options.Options;
import soot.tagkit.Host;
import soot.tagkit.LineNumberTag;
import soot.tagkit.SourceLnPosTag;
import soot.tagkit.Tag;
import soot.toolkits.exceptions.ThrowAnalysis;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
import soot.toolkits.graph.PseudoTopologicalOrderer;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.LocalDefs;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/CopyPropagator.class */
public class CopyPropagator extends BodyTransformer {
    private static final Logger logger = LoggerFactory.getLogger(CopyPropagator.class);
    protected ThrowAnalysis throwAnalysis;
    protected boolean forceOmitExceptingUnitEdges;

    public CopyPropagator(Singletons.Global g) {
        this.throwAnalysis = null;
        this.forceOmitExceptingUnitEdges = false;
    }

    public CopyPropagator(ThrowAnalysis ta) {
        this.throwAnalysis = null;
        this.forceOmitExceptingUnitEdges = false;
        this.throwAnalysis = ta;
    }

    public CopyPropagator(ThrowAnalysis ta, boolean forceOmitExceptingUnitEdges) {
        this.throwAnalysis = null;
        this.forceOmitExceptingUnitEdges = false;
        this.throwAnalysis = ta;
        this.forceOmitExceptingUnitEdges = forceOmitExceptingUnitEdges;
    }

    public static CopyPropagator v() {
        return G.v().soot_jimple_toolkits_scalar_CopyPropagator();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> opts) {
        Local m;
        Stmt s;
        if (Options.v().verbose()) {
            logger.debug("[" + b.getMethod().getName() + "] Propagating copies...");
        }
        if (Options.v().time()) {
            Timers.v().propagatorTimer.start();
        }
        Map<Local, Integer> localToDefCount = new HashMap<>((b.getLocalCount() * 2) + 1);
        Iterator<Unit> it = b.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (u instanceof DefinitionStmt) {
                Value leftOp = ((DefinitionStmt) u).getLeftOp();
                if (leftOp instanceof Local) {
                    Local loc = (Local) leftOp;
                    Integer old = localToDefCount.get(loc);
                    localToDefCount.put(loc, Integer.valueOf(old == null ? 1 : old.intValue() + 1));
                }
            }
        }
        if (this.throwAnalysis == null) {
            this.throwAnalysis = Scene.v().getDefaultThrowAnalysis();
        }
        if (!this.forceOmitExceptingUnitEdges) {
            this.forceOmitExceptingUnitEdges = Options.v().omit_excepting_unit_edges();
        }
        int fastCopyPropagationCount = 0;
        int slowCopyPropagationCount = 0;
        UnitGraph graph = ExceptionalUnitGraphFactory.createExceptionalUnitGraph(b, this.throwAnalysis, this.forceOmitExceptingUnitEdges);
        LocalDefs localDefs = G.v().soot_toolkits_scalar_LocalDefsFactory().newLocalDefs(graph);
        CPOptions options = new CPOptions(opts);
        boolean onlyRegularLocals = options.only_regular_locals();
        boolean onlyStackLocals = options.only_stack_locals();
        boolean allLocals = onlyRegularLocals && onlyStackLocals;
        for (Unit u2 : new PseudoTopologicalOrderer().newList(graph, false)) {
            for (ValueBox useBox : u2.getUseBoxes()) {
                Value value = useBox.getValue();
                if (value instanceof Local) {
                    Local l = (Local) value;
                    if (!allLocals && !(l.getType() instanceof NullType)) {
                        if (!onlyRegularLocals || !l.isStackLocal()) {
                            if (onlyStackLocals && !l.isStackLocal()) {
                            }
                        }
                    }
                    List<Unit> defsOfUse = localDefs.getDefsOfAt(l, u2);
                    boolean propagateDef = defsOfUse.size() == 1;
                    if (!propagateDef && defsOfUse.size() > 0) {
                        boolean agrees = true;
                        Constant constVal = null;
                        for (Unit defUnit : defsOfUse) {
                            boolean defAgrees = false;
                            if (defUnit instanceof AssignStmt) {
                                Value rightOp = ((AssignStmt) defUnit).getRightOp();
                                if (rightOp instanceof Constant) {
                                    if (constVal == null) {
                                        constVal = (Constant) rightOp;
                                        defAgrees = true;
                                    } else if (constVal.equals(rightOp)) {
                                        defAgrees = true;
                                    }
                                }
                            }
                            agrees &= defAgrees;
                        }
                        propagateDef = agrees;
                    }
                    if (propagateDef) {
                        DefinitionStmt def = (DefinitionStmt) defsOfUse.get(0);
                        Value rightOp2 = def.getRightOp();
                        if (rightOp2 instanceof Constant) {
                            if (useBox.canContainValue(rightOp2)) {
                                useBox.setValue(rightOp2);
                                copyLineTags(useBox, def);
                            }
                        } else if (rightOp2 instanceof CastExpr) {
                            CastExpr ce = (CastExpr) rightOp2;
                            if (ce.getCastType() instanceof RefLikeType) {
                                Value op = ce.getOp();
                                if (((op instanceof IntConstant) && ((IntConstant) op).value == 0) || ((op instanceof LongConstant) && ((LongConstant) op).value == 0)) {
                                    if (useBox.canContainValue(NullConstant.v())) {
                                        useBox.setValue(NullConstant.v());
                                        copyLineTags(useBox, def);
                                    }
                                }
                            }
                        } else if ((rightOp2 instanceof Local) && l != (m = (Local) rightOp2)) {
                            Integer defCount = localToDefCount.get(m);
                            if (defCount == null || defCount.intValue() == 0) {
                                throw new RuntimeException("Variable " + m + " used without definition!");
                            }
                            if (defCount.intValue() == 1) {
                                useBox.setValue(m);
                                copyLineTags(useBox, def);
                                fastCopyPropagationCount++;
                            } else {
                                List<Unit> path = graph.getExtendedBasicBlockPathBetween(def, u2);
                                if (path != null) {
                                    boolean isRedefined = false;
                                    Iterator<Unit> pathIt = path.iterator();
                                    pathIt.next();
                                    while (true) {
                                        if (!pathIt.hasNext() || u2 == (s = (Stmt) pathIt.next())) {
                                            break;
                                        } else if ((s instanceof DefinitionStmt) && ((DefinitionStmt) s).getLeftOp() == m) {
                                            isRedefined = true;
                                            break;
                                        }
                                    }
                                    if (!isRedefined) {
                                        useBox.setValue(m);
                                        slowCopyPropagationCount++;
                                    }
                                }
                            }
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
        if (Options.v().verbose()) {
            logger.debug("[" + b.getMethod().getName() + "]     Propagated: " + fastCopyPropagationCount + " fast copies  " + slowCopyPropagationCount + " slow copies");
        }
        if (Options.v().time()) {
            Timers.v().propagatorTimer.end();
        }
    }

    public static void copyLineTags(ValueBox useBox, DefinitionStmt def) {
        if (!copyLineTags(useBox, def.getRightOpBox())) {
            copyLineTags(useBox, (Host) def);
        }
    }

    private static boolean copyLineTags(ValueBox useBox, Host host) {
        boolean res = false;
        Tag tag = host.getTag(SourceLnPosTag.NAME);
        if (tag != null) {
            useBox.addTag(tag);
            res = true;
        }
        Tag tag2 = host.getTag(LineNumberTag.NAME);
        if (tag2 != null) {
            useBox.addTag(tag2);
            res = true;
        }
        return res;
    }
}
