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
import soot.EquivalentValue;
import soot.JavaMethods;
import soot.Local;
import soot.NullType;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.DefinitionStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
import soot.jimple.internal.JimpleLocal;
import soot.jimple.toolkits.ide.DefaultJimpleIFDSTabulationProblem;
import soot.toolkits.scalar.Pair;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/ide/exampleproblems/IFDSReachingDefinitions.class */
public class IFDSReachingDefinitions extends DefaultJimpleIFDSTabulationProblem<Pair<Value, Set<DefinitionStmt>>, InterproceduralCFG<Unit, SootMethod>> {
    public IFDSReachingDefinitions(InterproceduralCFG<Unit, SootMethod> icfg) {
        super(icfg);
    }

    @Override // heros.template.DefaultIFDSTabulationProblem
    public FlowFunctions<Unit, Pair<Value, Set<DefinitionStmt>>, SootMethod> createFlowFunctionsFactory() {
        return new FlowFunctions<Unit, Pair<Value, Set<DefinitionStmt>>, SootMethod>() { // from class: soot.jimple.toolkits.ide.exampleproblems.IFDSReachingDefinitions.1
            @Override // heros.FlowFunctions
            public FlowFunction<Pair<Value, Set<DefinitionStmt>>> getNormalFlowFunction(Unit curr, Unit succ) {
                if (curr instanceof DefinitionStmt) {
                    final DefinitionStmt assignment = (DefinitionStmt) curr;
                    return new FlowFunction<Pair<Value, Set<DefinitionStmt>>>() { // from class: soot.jimple.toolkits.ide.exampleproblems.IFDSReachingDefinitions.1.1
                        @Override // heros.FlowFunction
                        public Set<Pair<Value, Set<DefinitionStmt>>> computeTargets(Pair<Value, Set<DefinitionStmt>> source) {
                            if (source != IFDSReachingDefinitions.this.zeroValue()) {
                                if (source.getO1().equivTo(assignment.getLeftOp())) {
                                    return Collections.emptySet();
                                }
                                return Collections.singleton(source);
                            }
                            LinkedHashSet<Pair<Value, Set<DefinitionStmt>>> res = new LinkedHashSet<>();
                            res.add(new Pair<>(assignment.getLeftOp(), Collections.singleton(assignment)));
                            return res;
                        }
                    };
                }
                return Identity.v();
            }

            @Override // heros.FlowFunctions
            public FlowFunction<Pair<Value, Set<DefinitionStmt>>> getCallFlowFunction(Unit callStmt, final SootMethod destinationMethod) {
                Stmt stmt = (Stmt) callStmt;
                InvokeExpr invokeExpr = stmt.getInvokeExpr();
                final List<Value> args = invokeExpr.getArgs();
                final List<Local> localArguments = new ArrayList<>(args.size());
                for (Value value : args) {
                    if (value instanceof Local) {
                        localArguments.add((Local) value);
                    } else {
                        localArguments.add(null);
                    }
                }
                return new FlowFunction<Pair<Value, Set<DefinitionStmt>>>() { // from class: soot.jimple.toolkits.ide.exampleproblems.IFDSReachingDefinitions.1.2
                    @Override // heros.FlowFunction
                    public Set<Pair<Value, Set<DefinitionStmt>>> computeTargets(Pair<Value, Set<DefinitionStmt>> source) {
                        if (!destinationMethod.getName().equals("<clinit>") && !destinationMethod.getSubSignature().equals(JavaMethods.SIG_RUN) && localArguments.contains(source.getO1())) {
                            int paramIndex = args.indexOf(source.getO1());
                            Pair<Value, Set<DefinitionStmt>> pair = new Pair<>(new EquivalentValue(Jimple.v().newParameterRef(destinationMethod.getParameterType(paramIndex), paramIndex)), source.getO2());
                            return Collections.singleton(pair);
                        }
                        return Collections.emptySet();
                    }
                };
            }

            @Override // heros.FlowFunctions
            public FlowFunction<Pair<Value, Set<DefinitionStmt>>> getReturnFlowFunction(final Unit callSite, SootMethod calleeMethod, final Unit exitStmt, Unit returnSite) {
                if (!(callSite instanceof DefinitionStmt) || (exitStmt instanceof ReturnVoidStmt)) {
                    return KillAll.v();
                }
                return new FlowFunction<Pair<Value, Set<DefinitionStmt>>>() { // from class: soot.jimple.toolkits.ide.exampleproblems.IFDSReachingDefinitions.1.3
                    @Override // heros.FlowFunction
                    public Set<Pair<Value, Set<DefinitionStmt>>> computeTargets(Pair<Value, Set<DefinitionStmt>> source) {
                        if (exitStmt instanceof ReturnStmt) {
                            ReturnStmt returnStmt = (ReturnStmt) exitStmt;
                            if (returnStmt.getOp().equivTo(source.getO1())) {
                                DefinitionStmt definitionStmt = (DefinitionStmt) callSite;
                                Pair<Value, Set<DefinitionStmt>> pair = new Pair<>(definitionStmt.getLeftOp(), source.getO2());
                                return Collections.singleton(pair);
                            }
                        }
                        return Collections.emptySet();
                    }
                };
            }

            @Override // heros.FlowFunctions
            public FlowFunction<Pair<Value, Set<DefinitionStmt>>> getCallToReturnFlowFunction(Unit callSite, Unit returnSite) {
                if (!(callSite instanceof DefinitionStmt)) {
                    return Identity.v();
                }
                final DefinitionStmt definitionStmt = (DefinitionStmt) callSite;
                return new FlowFunction<Pair<Value, Set<DefinitionStmt>>>() { // from class: soot.jimple.toolkits.ide.exampleproblems.IFDSReachingDefinitions.1.4
                    @Override // heros.FlowFunction
                    public Set<Pair<Value, Set<DefinitionStmt>>> computeTargets(Pair<Value, Set<DefinitionStmt>> source) {
                        if (source.getO1().equivTo(definitionStmt.getLeftOp())) {
                            return Collections.emptySet();
                        }
                        return Collections.singleton(source);
                    }
                };
            }
        };
    }

    @Override // heros.IFDSTabulationProblem
    public Map<Unit, Set<Pair<Value, Set<DefinitionStmt>>>> initialSeeds() {
        return DefaultSeeds.make(Collections.singleton(Scene.v().getMainMethod().getActiveBody().getUnits().getFirst()), zeroValue());
    }

    @Override // heros.template.DefaultIFDSTabulationProblem
    public Pair<Value, Set<DefinitionStmt>> createZeroValue() {
        return new Pair<>(new JimpleLocal("<<zero>>", NullType.v()), Collections.emptySet());
    }
}
