package soot.javaToJimple;

import soot.Body;
import soot.Local;
import soot.LocalGenerator;
import soot.MethodSource;
import soot.Scene;
import soot.SootClass;
import soot.SootFieldRef;
import soot.SootMethod;
import soot.Type;
import soot.UnitPatchingChain;
import soot.jimple.AssignStmt;
import soot.jimple.FieldRef;
import soot.jimple.Jimple;
import soot.jimple.ParameterRef;
/* loaded from: gencallgraphv3.jar:soot/javaToJimple/PrivateFieldAccMethodSource.class */
public class PrivateFieldAccMethodSource implements MethodSource {
    private final Type fieldType;
    private final String fieldName;
    private final boolean isStatic;
    private final SootClass classToInvoke;

    public PrivateFieldAccMethodSource(Type fieldType, String fieldName, boolean isStatic, SootClass classToInvoke) {
        this.fieldType = fieldType;
        this.fieldName = fieldName;
        this.isStatic = isStatic;
        this.classToInvoke = classToInvoke;
    }

    @Override // soot.MethodSource
    public Body getBody(SootMethod sootMethod, String phaseName) {
        FieldRef fieldRef;
        Body body = Jimple.v().newBody(sootMethod);
        LocalGenerator lg = Scene.v().createLocalGenerator(body);
        Local fieldBase = null;
        for (Type sootType : sootMethod.getParameterTypes()) {
            Local paramLocal = lg.generateLocal(sootType);
            ParameterRef paramRef = Jimple.v().newParameterRef(sootType, 0);
            body.getUnits().add((UnitPatchingChain) Jimple.v().newIdentityStmt(paramLocal, paramRef));
            fieldBase = paramLocal;
        }
        Local fieldLocal = lg.generateLocal(this.fieldType);
        SootFieldRef field = Scene.v().makeFieldRef(this.classToInvoke, this.fieldName, this.fieldType, this.isStatic);
        if (this.isStatic) {
            fieldRef = Jimple.v().newStaticFieldRef(field);
        } else {
            fieldRef = Jimple.v().newInstanceFieldRef(fieldBase, field);
        }
        AssignStmt assign = Jimple.v().newAssignStmt(fieldLocal, fieldRef);
        body.getUnits().add((UnitPatchingChain) assign);
        body.getUnits().add((UnitPatchingChain) Jimple.v().newReturnStmt(fieldLocal));
        return body;
    }
}
