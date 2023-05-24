package soot.jimple.infoflow.problems.rules.forward;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import soot.Local;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.IfStmt;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.Stmt;
import soot.jimple.TableSwitchStmt;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.collect.ConcurrentHashSet;
import soot.jimple.infoflow.collect.MyConcurrentHashMap;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AbstractionAtSink;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.problems.TaintPropagationResults;
import soot.jimple.infoflow.problems.rules.AbstractTaintPropagationRule;
import soot.jimple.infoflow.solver.cfg.IInfoflowCFG;
import soot.jimple.infoflow.sourcesSinks.manager.SinkInfo;
import soot.jimple.infoflow.util.ByReferenceBoolean;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/problems/rules/forward/ImplicitPropagtionRule.class */
public class ImplicitPropagtionRule extends AbstractTaintPropagationRule {
    private final MyConcurrentHashMap<Unit, Set<Abstraction>> implicitTargets;

    public ImplicitPropagtionRule(InfoflowManager manager, Abstraction zeroValue, TaintPropagationResults results) {
        super(manager, zeroValue, results);
        this.implicitTargets = new MyConcurrentHashMap<>();
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateNormalFlow(Abstraction d1, Abstraction source, Stmt stmt, Stmt destStmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll) {
        Value condition;
        if (source == getZeroValue() || leavesConditionalBranch(stmt, source, killAll) || !source.isAbstractionActive() || source.getAccessPath().isEmpty()) {
            return null;
        }
        Set<Value> values = new HashSet<>();
        if (getManager().getICFG().isExceptionalEdgeBetween(stmt, destStmt)) {
            for (ValueBox box : stmt.getUseBoxes()) {
                values.add(box.getValue());
            }
        } else {
            if (stmt instanceof IfStmt) {
                condition = ((IfStmt) stmt).getCondition();
            } else if (stmt instanceof LookupSwitchStmt) {
                condition = ((LookupSwitchStmt) stmt).getKey();
            } else if (stmt instanceof TableSwitchStmt) {
                condition = ((TableSwitchStmt) stmt).getKey();
            } else {
                return null;
            }
            if (condition instanceof Local) {
                values.add(condition);
            } else {
                for (ValueBox box2 : condition.getUseBoxes()) {
                    values.add(box2.getValue());
                }
            }
        }
        Set<Abstraction> res = null;
        for (Value val : values) {
            if (getAliasing().mayAlias(val, source.getAccessPath().getPlainValue())) {
                IInfoflowCFG.UnitContainer postdom = getManager().getICFG().getPostdominatorOf(stmt);
                if (postdom.getMethod() != null || source.getTopPostdominator() == null || getManager().getICFG().getMethodOf(postdom.getUnit()) != source.getTopPostdominator().getMethod()) {
                    Abstraction newAbs = source.deriveConditionalAbstractionEnter(postdom, stmt);
                    if (0 == 0) {
                        res = new HashSet<>();
                    }
                    res.add(newAbs);
                    return res;
                }
            }
        }
        return res;
    }

    private boolean leavesConditionalBranch(Stmt stmt, Abstraction source, ByReferenceBoolean killAll) {
        if (source.isTopPostdominator(stmt)) {
            Abstraction source2 = source.dropTopPostdominator();
            if (source2.getAccessPath().isEmpty() && source2.getTopPostdominator() == null) {
                if (killAll != null) {
                    killAll.value = true;
                    return true;
                }
                return true;
            }
            return false;
        }
        return false;
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateCallFlow(Abstraction d1, Abstraction source, Stmt stmt, SootMethod dest, ByReferenceBoolean killAll) {
        if (source == getZeroValue() || leavesConditionalBranch(stmt, source, killAll)) {
            return null;
        }
        if (this.implicitTargets.containsKey(stmt) && (d1 == null || this.implicitTargets.get(stmt).contains(d1))) {
            if (killAll != null) {
                killAll.value = true;
                return null;
            }
            return null;
        } else if (source.getAccessPath().isEmpty()) {
            if (d1 != null) {
                Set<Abstraction> callSites = this.implicitTargets.putIfAbsentElseGet((MyConcurrentHashMap<Unit, Set<Abstraction>>) stmt, (Stmt) new ConcurrentHashSet());
                callSites.add(d1);
            }
            Abstraction abs = source.deriveConditionalAbstractionCall(stmt);
            return Collections.singleton(abs);
        } else if (source.getTopPostdominator() != null && killAll != null) {
            killAll.value = true;
            return null;
        } else {
            return null;
        }
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateCallToReturnFlow(Abstraction d1, Abstraction source, Stmt stmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll) {
        SinkInfo sinkInfo;
        if (source == getZeroValue() || leavesConditionalBranch(stmt, source, killAll)) {
            return null;
        }
        if (source.isAbstractionActive()) {
            if (source.getAccessPath().isEmpty() || source.getTopPostdominator() != null) {
                SinkInfo sinkInfo2 = getManager().getSourceSinkManager().getSinkInfo(stmt, getManager(), null);
                if (sinkInfo2 != null) {
                    getResults().addResult(new AbstractionAtSink(sinkInfo2.getDefinition(), source, stmt));
                }
            } else {
                SootMethod curMethod = getManager().getICFG().getMethodOf(stmt);
                if (!curMethod.isStatic() && source.getAccessPath().getFirstField() == null && getAliasing().mayAlias(curMethod.getActiveBody().getThisLocal(), source.getAccessPath().getPlainValue()) && (sinkInfo = getManager().getSourceSinkManager().getSinkInfo(stmt, getManager(), null)) != null) {
                    getResults().addResult(new AbstractionAtSink(sinkInfo.getDefinition(), source, stmt));
                }
            }
        }
        if (stmt instanceof DefinitionStmt) {
            boolean implicitTaint = (source.getTopPostdominator() == null || source.getTopPostdominator().getUnit() == null) ? false : true;
            if (implicitTaint | source.getAccessPath().isEmpty()) {
                Value leftVal = ((DefinitionStmt) stmt).getLeftOp();
                if ((d1 == null || d1.getAccessPath().isEmpty()) && !(leftVal instanceof FieldRef)) {
                    return null;
                }
                Abstraction abs = source.deriveNewAbstraction(getManager().getAccessPathFactory().createAccessPath(leftVal, true), stmt);
                return Collections.singleton(abs);
            }
            return null;
        }
        return null;
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateReturnFlow(Collection<Abstraction> callerD1s, Abstraction calleeD1, Abstraction source, Stmt returnStmt, Stmt retSite, Stmt callSite, ByReferenceBoolean killAll) {
        boolean callerD1sConditional = false;
        Iterator<Abstraction> it = callerD1s.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Abstraction d1 = it.next();
            if (d1.getAccessPath().isEmpty()) {
                callerD1sConditional = true;
                break;
            }
        }
        if (source.getAccessPath().isEmpty()) {
            if ((returnStmt instanceof ReturnStmt) && (((ReturnStmt) returnStmt).getOp() instanceof Constant) && (callSite instanceof DefinitionStmt)) {
                DefinitionStmt def = (DefinitionStmt) callSite;
                AccessPath ap = getManager().getAccessPathFactory().copyWithNewValue(source.getAccessPath(), def.getLeftOp());
                Abstraction abs = source.deriveNewAbstraction(ap, returnStmt);
                Set<Abstraction> res = new HashSet<>();
                res.add(abs);
                if (this.manager.getAliasing().canHaveAliases(def, def.getLeftOp(), abs) && !callerD1sConditional) {
                    for (Abstraction d12 : callerD1s) {
                        getAliasing().computeAliases(d12, returnStmt, def.getLeftOp(), res, getManager().getICFG().getMethodOf(callSite), abs);
                    }
                }
                return res;
            }
            killAll.value = true;
            return null;
        } else if ((returnStmt instanceof ReturnStmt) && (callSite instanceof DefinitionStmt)) {
            DefinitionStmt defnStmt = (DefinitionStmt) callSite;
            Value leftOp = defnStmt.getLeftOp();
            boolean insideConditional = source.getTopPostdominator() != null || source.getAccessPath().isEmpty();
            if (insideConditional && (leftOp instanceof FieldRef)) {
                AccessPath ap2 = getManager().getAccessPathFactory().copyWithNewValue(source.getAccessPath(), leftOp);
                Abstraction abs2 = source.deriveNewAbstraction(ap2, returnStmt);
                if (abs2.isImplicit() && abs2.getAccessPath().isFieldRef() && !callerD1sConditional) {
                    Set<Abstraction> res2 = new HashSet<>();
                    res2.add(abs2);
                    for (Abstraction d13 : callerD1s) {
                        getAliasing().computeAliases(d13, callSite, leftOp, res2, getManager().getICFG().getMethodOf(callSite), abs2);
                    }
                }
                return Collections.singleton(abs2);
            }
            return null;
        } else {
            return null;
        }
    }
}
