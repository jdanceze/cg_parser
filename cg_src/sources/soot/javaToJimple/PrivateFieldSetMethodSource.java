package soot.javaToJimple;

import soot.Body;
import soot.Local;
import soot.LocalGenerator;
import soot.MethodSource;
import soot.Scene;
import soot.SootFieldRef;
import soot.SootMethod;
import soot.Type;
import soot.UnitPatchingChain;
import soot.jimple.AssignStmt;
import soot.jimple.FieldRef;
import soot.jimple.Jimple;
import soot.jimple.ParameterRef;
/* loaded from: gencallgraphv3.jar:soot/javaToJimple/PrivateFieldSetMethodSource.class */
public class PrivateFieldSetMethodSource implements MethodSource {
    private final Type fieldType;
    private final String fieldName;
    private final boolean isStatic;

    public PrivateFieldSetMethodSource(Type fieldType, String fieldName, boolean isStatic) {
        this.fieldType = fieldType;
        this.fieldName = fieldName;
        this.isStatic = isStatic;
    }

    @Override // soot.MethodSource
    public Body getBody(SootMethod sootMethod, String phaseName) {
        FieldRef fieldRef;
        Body body = Jimple.v().newBody(sootMethod);
        LocalGenerator lg = Scene.v().createLocalGenerator(body);
        Local fieldBase = null;
        Local assignLocal = null;
        int paramCounter = 0;
        for (Type sootType : sootMethod.getParameterTypes()) {
            Local paramLocal = lg.generateLocal(sootType);
            ParameterRef paramRef = Jimple.v().newParameterRef(sootType, paramCounter);
            body.getUnits().add((UnitPatchingChain) Jimple.v().newIdentityStmt(paramLocal, paramRef));
            if (paramCounter == 0) {
                fieldBase = paramLocal;
            }
            assignLocal = paramLocal;
            paramCounter++;
        }
        SootFieldRef field = Scene.v().makeFieldRef(sootMethod.getDeclaringClass(), this.fieldName, this.fieldType, this.isStatic);
        if (this.isStatic) {
            fieldRef = Jimple.v().newStaticFieldRef(field);
        } else {
            fieldRef = Jimple.v().newInstanceFieldRef(fieldBase, field);
        }
        AssignStmt assign = Jimple.v().newAssignStmt(fieldRef, assignLocal);
        body.getUnits().add((UnitPatchingChain) assign);
        body.getUnits().add((UnitPatchingChain) Jimple.v().newReturnStmt(assignLocal));
        return body;
    }
}
