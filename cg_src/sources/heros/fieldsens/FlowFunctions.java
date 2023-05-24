package heros.fieldsens;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/fieldsens/FlowFunctions.class */
public interface FlowFunctions<Stmt, FieldRef, F, Method> {
    FlowFunction<FieldRef, F, Stmt, Method> getNormalFlowFunction(Stmt stmt);

    FlowFunction<FieldRef, F, Stmt, Method> getCallFlowFunction(Stmt stmt, Method method);

    FlowFunction<FieldRef, F, Stmt, Method> getReturnFlowFunction(Stmt stmt, Method method, Stmt stmt2, Stmt stmt3);

    FlowFunction<FieldRef, F, Stmt, Method> getCallToReturnFlowFunction(Stmt stmt, Stmt stmt2);
}
