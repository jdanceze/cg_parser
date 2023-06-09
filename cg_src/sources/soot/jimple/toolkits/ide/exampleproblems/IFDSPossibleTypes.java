package soot.jimple.toolkits.ide.exampleproblems;

import heros.DefaultSeeds;
import heros.FlowFunction;
import heros.FlowFunctions;
import heros.InterproceduralCFG;
import heros.flowfunc.Identity;
import heros.flowfunc.KillAll;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.JavaMethods;
import soot.Local;
import soot.PointsToAnalysis;
import soot.PointsToSet;
import soot.PrimType;
import soot.Scene;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.UnknownType;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.NewExpr;
import soot.jimple.Ref;
import soot.jimple.ReturnStmt;
import soot.jimple.Stmt;
import soot.jimple.toolkits.ide.DefaultJimpleIFDSTabulationProblem;
import soot.toolkits.scalar.Pair;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/ide/exampleproblems/IFDSPossibleTypes.class */
public class IFDSPossibleTypes extends DefaultJimpleIFDSTabulationProblem<Pair<Value, Type>, InterproceduralCFG<Unit, SootMethod>> {
    public IFDSPossibleTypes(InterproceduralCFG<Unit, SootMethod> icfg) {
        super(icfg);
    }

    @Override // heros.template.DefaultIFDSTabulationProblem
    public FlowFunctions<Unit, Pair<Value, Type>, SootMethod> createFlowFunctionsFactory() {
        return new FlowFunctions<Unit, Pair<Value, Type>, SootMethod>() { // from class: soot.jimple.toolkits.ide.exampleproblems.IFDSPossibleTypes.1
            @Override // heros.FlowFunctions
            public FlowFunction<Pair<Value, Type>> getNormalFlowFunction(Unit src, Unit dest) {
                if (src instanceof DefinitionStmt) {
                    DefinitionStmt defnStmt = (DefinitionStmt) src;
                    if (defnStmt.containsInvokeExpr()) {
                        return Identity.v();
                    }
                    final Value right = defnStmt.getRightOp();
                    final Value left = defnStmt.getLeftOp();
                    if (right.getType() instanceof PrimType) {
                        return Identity.v();
                    }
                    if ((right instanceof Constant) || (right instanceof NewExpr)) {
                        return new FlowFunction<Pair<Value, Type>>() { // from class: soot.jimple.toolkits.ide.exampleproblems.IFDSPossibleTypes.1.1
                            @Override // heros.FlowFunction
                            public Set<Pair<Value, Type>> computeTargets(Pair<Value, Type> source) {
                                if (source == IFDSPossibleTypes.this.zeroValue()) {
                                    Set<Pair<Value, Type>> res = new LinkedHashSet<>();
                                    res.add(new Pair<>(left, right.getType()));
                                    res.add(IFDSPossibleTypes.this.zeroValue());
                                    return res;
                                } else if ((source.getO1() instanceof Local) && source.getO1().equivTo(left)) {
                                    return Collections.emptySet();
                                } else {
                                    return Collections.singleton(source);
                                }
                            }
                        };
                    }
                    if ((right instanceof Ref) || (right instanceof Local)) {
                        return new FlowFunction<Pair<Value, Type>>() { // from class: soot.jimple.toolkits.ide.exampleproblems.IFDSPossibleTypes.1.2
                            @Override // heros.FlowFunction
                            public Set<Pair<Value, Type>> computeTargets(Pair<Value, Type> source) {
                                Value value = source.getO1();
                                if ((source.getO1() instanceof Local) && source.getO1().equivTo(left)) {
                                    return Collections.emptySet();
                                }
                                if (maybeSameLocation(value, right)) {
                                    return new LinkedHashSet<Pair<Value, Type>>(left, source) { // from class: soot.jimple.toolkits.ide.exampleproblems.IFDSPossibleTypes.1.2.1
                                        {
                                            add(new Pair(r8, (Type) source.getO2()));
                                            add(source);
                                        }
                                    };
                                }
                                return Collections.singleton(source);
                            }

                            private boolean maybeSameLocation(Value v1, Value v2) {
                                if ((!(v1 instanceof InstanceFieldRef) || !(v2 instanceof InstanceFieldRef)) && (!(v1 instanceof ArrayRef) || !(v2 instanceof ArrayRef))) {
                                    return v1.equivTo(v2);
                                }
                                if ((v1 instanceof InstanceFieldRef) && (v2 instanceof InstanceFieldRef)) {
                                    InstanceFieldRef ifr1 = (InstanceFieldRef) v1;
                                    InstanceFieldRef ifr2 = (InstanceFieldRef) v2;
                                    if (!ifr1.getField().getName().equals(ifr2.getField().getName())) {
                                        return false;
                                    }
                                    Local base1 = (Local) ifr1.getBase();
                                    Local base2 = (Local) ifr2.getBase();
                                    PointsToAnalysis pta = Scene.v().getPointsToAnalysis();
                                    PointsToSet pts1 = pta.reachingObjects(base1);
                                    PointsToSet pts2 = pta.reachingObjects(base2);
                                    return pts1.hasNonEmptyIntersection(pts2);
                                }
                                ArrayRef ar1 = (ArrayRef) v1;
                                ArrayRef ar2 = (ArrayRef) v2;
                                Local base12 = (Local) ar1.getBase();
                                Local base22 = (Local) ar2.getBase();
                                PointsToAnalysis pta2 = Scene.v().getPointsToAnalysis();
                                PointsToSet pts12 = pta2.reachingObjects(base12);
                                PointsToSet pts22 = pta2.reachingObjects(base22);
                                return pts12.hasNonEmptyIntersection(pts22);
                            }
                        };
                    }
                }
                return Identity.v();
            }

            @Override // heros.FlowFunctions
            public FlowFunction<Pair<Value, Type>> getCallFlowFunction(Unit src, final SootMethod dest) {
                Stmt stmt = (Stmt) src;
                InvokeExpr ie = stmt.getInvokeExpr();
                final List<Value> callArgs = ie.getArgs();
                final List<Local> paramLocals = new ArrayList<>();
                for (int i = 0; i < dest.getParameterCount(); i++) {
                    paramLocals.add(dest.getActiveBody().getParameterLocal(i));
                }
                return new FlowFunction<Pair<Value, Type>>() { // from class: soot.jimple.toolkits.ide.exampleproblems.IFDSPossibleTypes.1.3
                    @Override // heros.FlowFunction
                    public Set<Pair<Value, Type>> computeTargets(Pair<Value, Type> source) {
                        if (!dest.getName().equals("<clinit>") && !dest.getSubSignature().equals(JavaMethods.SIG_RUN)) {
                            Value value = source.getO1();
                            int argIndex = callArgs.indexOf(value);
                            if (argIndex > -1) {
                                return Collections.singleton(new Pair((Value) paramLocals.get(argIndex), source.getO2()));
                            }
                        }
                        return Collections.emptySet();
                    }
                };
            }

            @Override // heros.FlowFunctions
            public FlowFunction<Pair<Value, Type>> getReturnFlowFunction(Unit callSite, SootMethod callee, Unit exitStmt, Unit retSite) {
                if (exitStmt instanceof ReturnStmt) {
                    ReturnStmt returnStmt = (ReturnStmt) exitStmt;
                    Value op = returnStmt.getOp();
                    if ((op instanceof Local) && (callSite instanceof DefinitionStmt)) {
                        DefinitionStmt defnStmt = (DefinitionStmt) callSite;
                        Value leftOp = defnStmt.getLeftOp();
                        if (leftOp instanceof Local) {
                            final Local tgtLocal = (Local) leftOp;
                            final Local retLocal = (Local) op;
                            return new FlowFunction<Pair<Value, Type>>() { // from class: soot.jimple.toolkits.ide.exampleproblems.IFDSPossibleTypes.1.4
                                @Override // heros.FlowFunction
                                public Set<Pair<Value, Type>> computeTargets(Pair<Value, Type> source) {
                                    if (source.getO1() == retLocal) {
                                        return Collections.singleton(new Pair(tgtLocal, source.getO2()));
                                    }
                                    return Collections.emptySet();
                                }
                            };
                        }
                    }
                }
                return KillAll.v();
            }

            @Override // heros.FlowFunctions
            public FlowFunction<Pair<Value, Type>> getCallToReturnFlowFunction(Unit call, Unit returnSite) {
                return Identity.v();
            }
        };
    }

    @Override // heros.IFDSTabulationProblem
    public Map<Unit, Set<Pair<Value, Type>>> initialSeeds() {
        return DefaultSeeds.make(Collections.singleton(Scene.v().getMainMethod().getActiveBody().getUnits().getFirst()), zeroValue());
    }

    @Override // heros.template.DefaultIFDSTabulationProblem
    public Pair<Value, Type> createZeroValue() {
        return new Pair<>(Jimple.v().newLocal("<dummy>", UnknownType.v()), UnknownType.v());
    }
}
