package soot.jimple.infoflow.handlers;

import java.util.Set;
import soot.Unit;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.data.Abstraction;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/handlers/TaintPropagationHandler.class */
public interface TaintPropagationHandler {

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/handlers/TaintPropagationHandler$FlowFunctionType.class */
    public enum FlowFunctionType {
        NormalFlowFunction,
        CallFlowFunction,
        CallToReturnFlowFunction,
        ReturnFlowFunction;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static FlowFunctionType[] valuesCustom() {
            FlowFunctionType[] valuesCustom = values();
            int length = valuesCustom.length;
            FlowFunctionType[] flowFunctionTypeArr = new FlowFunctionType[length];
            System.arraycopy(valuesCustom, 0, flowFunctionTypeArr, 0, length);
            return flowFunctionTypeArr;
        }
    }

    void notifyFlowIn(Unit unit, Abstraction abstraction, InfoflowManager infoflowManager, FlowFunctionType flowFunctionType);

    Set<Abstraction> notifyFlowOut(Unit unit, Abstraction abstraction, Abstraction abstraction2, Set<Abstraction> set, InfoflowManager infoflowManager, FlowFunctionType flowFunctionType);
}
