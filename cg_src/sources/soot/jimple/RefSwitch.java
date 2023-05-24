package soot.jimple;

import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/RefSwitch.class */
public interface RefSwitch extends Switch {
    void caseArrayRef(ArrayRef arrayRef);

    void caseStaticFieldRef(StaticFieldRef staticFieldRef);

    void caseInstanceFieldRef(InstanceFieldRef instanceFieldRef);

    void caseParameterRef(ParameterRef parameterRef);

    void caseCaughtExceptionRef(CaughtExceptionRef caughtExceptionRef);

    void caseThisRef(ThisRef thisRef);

    void defaultCase(Object obj);
}
