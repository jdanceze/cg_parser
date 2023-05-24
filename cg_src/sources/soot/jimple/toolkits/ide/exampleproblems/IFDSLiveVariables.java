package soot.jimple.toolkits.ide.exampleproblems;

import heros.DefaultSeeds;
import heros.FlowFunction;
import heros.FlowFunctions;
import heros.InterproceduralCFG;
import heros.flowfunc.Identity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Local;
import soot.NullType;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.InvokeExpr;
import soot.jimple.ReturnStmt;
import soot.jimple.Stmt;
import soot.jimple.internal.JimpleLocal;
import soot.jimple.toolkits.ide.DefaultJimpleIFDSTabulationProblem;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/ide/exampleproblems/IFDSLiveVariables.class */
public class IFDSLiveVariables extends DefaultJimpleIFDSTabulationProblem<Value, InterproceduralCFG<Unit, SootMethod>> {
    public IFDSLiveVariables(InterproceduralCFG<Unit, SootMethod> icfg) {
        super(icfg);
    }

    @Override // heros.template.DefaultIFDSTabulationProblem
    public FlowFunctions<Unit, Value, SootMethod> createFlowFunctionsFactory() {
        return new FlowFunctions<Unit, Value, SootMethod>() { // from class: soot.jimple.toolkits.ide.exampleproblems.IFDSLiveVariables.1
            @Override // heros.FlowFunctions
            public FlowFunction<Value> getNormalFlowFunction(Unit curr, Unit succ) {
                if (curr.getUseAndDefBoxes().isEmpty()) {
                    return Identity.v();
                }
                final Stmt s = (Stmt) curr;
                return new FlowFunction<Value>() { // from class: soot.jimple.toolkits.ide.exampleproblems.IFDSLiveVariables.1.1
                    @Override // heros.FlowFunction
                    public Set<Value> computeTargets(Value source) {
                        List<ValueBox> defs = s.getDefBoxes();
                        if (!defs.isEmpty() && defs.get(0).getValue().equivTo(source)) {
                            return Collections.emptySet();
                        }
                        if (source.equals(IFDSLiveVariables.this.zeroValue())) {
                            Set<Value> liveVars = new HashSet<>();
                            for (ValueBox useBox : s.getUseBoxes()) {
                                Value value = useBox.getValue();
                                liveVars.add(value);
                            }
                            return liveVars;
                        }
                        return Collections.singleton(source);
                    }
                };
            }

            @Override // heros.FlowFunctions
            public FlowFunction<Value> getCallFlowFunction(Unit callStmt, final SootMethod destinationMethod) {
                final Stmt s = (Stmt) callStmt;
                return new FlowFunction<Value>() { // from class: soot.jimple.toolkits.ide.exampleproblems.IFDSLiveVariables.1.2
                    /* JADX WARN: Type inference failed for: r0v17, types: [heros.InterproceduralCFG] */
                    @Override // heros.FlowFunction
                    public Set<Value> computeTargets(Value source) {
                        if (!s.getDefBoxes().isEmpty()) {
                            Value callerSideReturnValue = s.getDefBoxes().get(0).getValue();
                            if (callerSideReturnValue.equivTo(source)) {
                                Set<Value> calleeSideReturnValues = new HashSet<>();
                                for (Unit calleeUnit : IFDSLiveVariables.this.interproceduralCFG().getStartPointsOf(destinationMethod)) {
                                    if (calleeUnit instanceof ReturnStmt) {
                                        ReturnStmt returnStmt = (ReturnStmt) calleeUnit;
                                        calleeSideReturnValues.add(returnStmt.getOp());
                                    }
                                }
                                return calleeSideReturnValues;
                            }
                        }
                        return Collections.emptySet();
                    }
                };
            }

            @Override // heros.FlowFunctions
            public FlowFunction<Value> getReturnFlowFunction(Unit callSite, SootMethod calleeMethod, Unit exitStmt, Unit returnSite) {
                Stmt s = (Stmt) callSite;
                InvokeExpr ie = s.getInvokeExpr();
                final List<Value> callArgs = ie.getArgs();
                final List<Local> paramLocals = new ArrayList<>();
                for (int i = 0; i < calleeMethod.getParameterCount(); i++) {
                    paramLocals.add(calleeMethod.getActiveBody().getParameterLocal(i));
                }
                return new FlowFunction<Value>() { // from class: soot.jimple.toolkits.ide.exampleproblems.IFDSLiveVariables.1.3
                    @Override // heros.FlowFunction
                    public Set<Value> computeTargets(Value source) {
                        Set<Value> liveParamsAtCallee = new HashSet<>();
                        for (int i2 = 0; i2 < paramLocals.size(); i2++) {
                            if (((Local) paramLocals.get(i2)).equivTo(source)) {
                                liveParamsAtCallee.add((Value) callArgs.get(i2));
                            }
                        }
                        return liveParamsAtCallee;
                    }
                };
            }

            @Override // heros.FlowFunctions
            public FlowFunction<Value> getCallToReturnFlowFunction(Unit callSite, Unit returnSite) {
                if (callSite.getUseAndDefBoxes().isEmpty()) {
                    return Identity.v();
                }
                final Stmt s = (Stmt) callSite;
                return new FlowFunction<Value>() { // from class: soot.jimple.toolkits.ide.exampleproblems.IFDSLiveVariables.1.4
                    @Override // heros.FlowFunction
                    public Set<Value> computeTargets(Value source) {
                        List<ValueBox> defs = s.getDefBoxes();
                        if (!defs.isEmpty() && defs.get(0).getValue().equivTo(source)) {
                            return Collections.emptySet();
                        }
                        if (source.equals(IFDSLiveVariables.this.zeroValue())) {
                            Set<Value> liveVars = new HashSet<>();
                            List<Value> args = s.getInvokeExpr().getArgs();
                            for (ValueBox useBox : s.getUseBoxes()) {
                                Value value = useBox.getValue();
                                if (!args.contains(value)) {
                                    liveVars.add(value);
                                }
                            }
                            return liveVars;
                        }
                        return Collections.singleton(source);
                    }
                };
            }
        };
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [heros.InterproceduralCFG] */
    @Override // heros.IFDSTabulationProblem
    public Map<Unit, Set<Value>> initialSeeds() {
        return DefaultSeeds.make(interproceduralCFG().getStartPointsOf(Scene.v().getMainMethod()), zeroValue());
    }

    @Override // heros.template.DefaultIFDSTabulationProblem
    public Value createZeroValue() {
        return new JimpleLocal("<<zero>>", NullType.v());
    }
}
