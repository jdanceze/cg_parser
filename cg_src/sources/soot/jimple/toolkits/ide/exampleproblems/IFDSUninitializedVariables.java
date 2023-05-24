package soot.jimple.toolkits.ide.exampleproblems;

import heros.DefaultSeeds;
import heros.FlowFunction;
import heros.FlowFunctions;
import heros.InterproceduralCFG;
import heros.flowfunc.Identity;
import heros.flowfunc.Kill;
import heros.flowfunc.KillAll;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.JavaMethods;
import soot.Local;
import soot.NullType;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.DefinitionStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.ReturnStmt;
import soot.jimple.Stmt;
import soot.jimple.ThrowStmt;
import soot.jimple.internal.JimpleLocal;
import soot.jimple.toolkits.ide.DefaultJimpleIFDSTabulationProblem;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/ide/exampleproblems/IFDSUninitializedVariables.class */
public class IFDSUninitializedVariables extends DefaultJimpleIFDSTabulationProblem<Local, InterproceduralCFG<Unit, SootMethod>> {
    public IFDSUninitializedVariables(InterproceduralCFG<Unit, SootMethod> icfg) {
        super(icfg);
    }

    @Override // heros.template.DefaultIFDSTabulationProblem
    public FlowFunctions<Unit, Local, SootMethod> createFlowFunctionsFactory() {
        return new FlowFunctions<Unit, Local, SootMethod>() { // from class: soot.jimple.toolkits.ide.exampleproblems.IFDSUninitializedVariables.1
            /* JADX WARN: Type inference failed for: r0v2, types: [heros.InterproceduralCFG] */
            /* JADX WARN: Type inference failed for: r0v22, types: [heros.InterproceduralCFG] */
            @Override // heros.FlowFunctions
            public FlowFunction<Local> getNormalFlowFunction(Unit curr, Unit succ) {
                final SootMethod m = (SootMethod) IFDSUninitializedVariables.this.interproceduralCFG().getMethodOf(curr);
                if (Scene.v().getEntryPoints().contains(m) && IFDSUninitializedVariables.this.interproceduralCFG().isStartPoint(curr)) {
                    return new FlowFunction<Local>() { // from class: soot.jimple.toolkits.ide.exampleproblems.IFDSUninitializedVariables.1.1
                        @Override // heros.FlowFunction
                        public Set<Local> computeTargets(Local source) {
                            if (source == IFDSUninitializedVariables.this.zeroValue()) {
                                Set<Local> res = new LinkedHashSet<>();
                                res.addAll(m.getActiveBody().getLocals());
                                for (int i = 0; i < m.getParameterCount(); i++) {
                                    res.remove(m.getActiveBody().getParameterLocal(i));
                                }
                                return res;
                            }
                            return Collections.emptySet();
                        }
                    };
                }
                if (curr instanceof DefinitionStmt) {
                    final DefinitionStmt definition = (DefinitionStmt) curr;
                    final Value leftOp = definition.getLeftOp();
                    if (leftOp instanceof Local) {
                        final Local leftOpLocal = (Local) leftOp;
                        return new FlowFunction<Local>() { // from class: soot.jimple.toolkits.ide.exampleproblems.IFDSUninitializedVariables.1.2
                            @Override // heros.FlowFunction
                            public Set<Local> computeTargets(Local source) {
                                List<ValueBox> useBoxes = definition.getUseBoxes();
                                for (ValueBox valueBox : useBoxes) {
                                    if (valueBox.getValue().equivTo(source)) {
                                        LinkedHashSet<Local> res = new LinkedHashSet<>();
                                        res.add(source);
                                        res.add(leftOpLocal);
                                        return res;
                                    }
                                }
                                if (leftOp.equivTo(source)) {
                                    return Collections.emptySet();
                                }
                                return Collections.singleton(source);
                            }
                        };
                    }
                }
                return Identity.v();
            }

            @Override // heros.FlowFunctions
            public FlowFunction<Local> getCallFlowFunction(Unit callStmt, final SootMethod destinationMethod) {
                Stmt stmt = (Stmt) callStmt;
                InvokeExpr invokeExpr = stmt.getInvokeExpr();
                final List<Value> args = invokeExpr.getArgs();
                final List<Local> localArguments = new ArrayList<>();
                for (Value value : args) {
                    if (value instanceof Local) {
                        localArguments.add((Local) value);
                    }
                }
                return new FlowFunction<Local>() { // from class: soot.jimple.toolkits.ide.exampleproblems.IFDSUninitializedVariables.1.3
                    @Override // heros.FlowFunction
                    public Set<Local> computeTargets(Local source) {
                        if (destinationMethod.getName().equals("<clinit>") || destinationMethod.getSubSignature().equals(JavaMethods.SIG_RUN)) {
                            return Collections.emptySet();
                        }
                        for (Local localArgument : localArguments) {
                            if (source.equivTo(localArgument)) {
                                return Collections.singleton(destinationMethod.getActiveBody().getParameterLocal(args.indexOf(localArgument)));
                            }
                        }
                        if (source == IFDSUninitializedVariables.this.zeroValue()) {
                            Collection<Local> locals = destinationMethod.getActiveBody().getLocals();
                            LinkedHashSet<Local> uninitializedLocals = new LinkedHashSet<>(locals);
                            for (int i = 0; i < destinationMethod.getParameterCount(); i++) {
                                uninitializedLocals.remove(destinationMethod.getActiveBody().getParameterLocal(i));
                            }
                            return uninitializedLocals;
                        }
                        return Collections.emptySet();
                    }
                };
            }

            @Override // heros.FlowFunctions
            public FlowFunction<Local> getReturnFlowFunction(Unit callSite, SootMethod calleeMethod, Unit exitStmt, Unit returnSite) {
                if (callSite instanceof DefinitionStmt) {
                    DefinitionStmt definition = (DefinitionStmt) callSite;
                    if (definition.getLeftOp() instanceof Local) {
                        final Local leftOpLocal = (Local) definition.getLeftOp();
                        if (exitStmt instanceof ReturnStmt) {
                            final ReturnStmt returnStmt = (ReturnStmt) exitStmt;
                            return new FlowFunction<Local>() { // from class: soot.jimple.toolkits.ide.exampleproblems.IFDSUninitializedVariables.1.4
                                @Override // heros.FlowFunction
                                public Set<Local> computeTargets(Local source) {
                                    if (returnStmt.getOp().equivTo(source)) {
                                        return Collections.singleton(leftOpLocal);
                                    }
                                    return Collections.emptySet();
                                }
                            };
                        } else if (exitStmt instanceof ThrowStmt) {
                            return new FlowFunction<Local>() { // from class: soot.jimple.toolkits.ide.exampleproblems.IFDSUninitializedVariables.1.5
                                @Override // heros.FlowFunction
                                public Set<Local> computeTargets(Local source) {
                                    if (source == IFDSUninitializedVariables.this.zeroValue()) {
                                        return Collections.singleton(leftOpLocal);
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
            public FlowFunction<Local> getCallToReturnFlowFunction(Unit callSite, Unit returnSite) {
                if (callSite instanceof DefinitionStmt) {
                    DefinitionStmt definition = (DefinitionStmt) callSite;
                    if (definition.getLeftOp() instanceof Local) {
                        Local leftOpLocal = (Local) definition.getLeftOp();
                        return new Kill(leftOpLocal);
                    }
                }
                return Identity.v();
            }
        };
    }

    @Override // heros.IFDSTabulationProblem
    public Map<Unit, Set<Local>> initialSeeds() {
        return DefaultSeeds.make(Collections.singleton(Scene.v().getMainMethod().getActiveBody().getUnits().getFirst()), zeroValue());
    }

    @Override // heros.template.DefaultIFDSTabulationProblem
    public Local createZeroValue() {
        return new JimpleLocal("<<zero>>", NullType.v());
    }
}
