package soot.jimple.toolkits.pointer.util;

import soot.SootMethod;
import soot.jimple.toolkits.pointer.representations.AbstractObject;
import soot.jimple.toolkits.pointer.representations.ReferenceVariable;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/util/NativeHelper.class */
public abstract class NativeHelper {
    protected abstract void assignImpl(ReferenceVariable referenceVariable, ReferenceVariable referenceVariable2);

    protected abstract void assignObjectToImpl(ReferenceVariable referenceVariable, AbstractObject abstractObject);

    protected abstract void throwExceptionImpl(AbstractObject abstractObject);

    protected abstract ReferenceVariable arrayElementOfImpl(ReferenceVariable referenceVariable);

    protected abstract ReferenceVariable cloneObjectImpl(ReferenceVariable referenceVariable);

    protected abstract ReferenceVariable newInstanceOfImpl(ReferenceVariable referenceVariable);

    protected abstract ReferenceVariable staticFieldImpl(String str, String str2);

    protected abstract ReferenceVariable tempFieldImpl(String str);

    protected abstract ReferenceVariable tempVariableImpl();

    protected abstract ReferenceVariable tempLocalVariableImpl(SootMethod sootMethod);

    public void assign(ReferenceVariable lhs, ReferenceVariable rhs) {
        assignImpl(lhs, rhs);
    }

    public void assignObjectTo(ReferenceVariable lhs, AbstractObject obj) {
        assignObjectToImpl(lhs, obj);
    }

    public void throwException(AbstractObject obj) {
        throwExceptionImpl(obj);
    }

    public ReferenceVariable arrayElementOf(ReferenceVariable base) {
        return arrayElementOfImpl(base);
    }

    public ReferenceVariable cloneObject(ReferenceVariable source) {
        return cloneObjectImpl(source);
    }

    public ReferenceVariable newInstanceOf(ReferenceVariable cls) {
        return newInstanceOfImpl(cls);
    }

    public ReferenceVariable staticField(String className, String fieldName) {
        return staticFieldImpl(className, fieldName);
    }

    public ReferenceVariable tempField(String fieldsig) {
        return tempFieldImpl(fieldsig);
    }

    public ReferenceVariable tempVariable() {
        return tempVariableImpl();
    }

    public ReferenceVariable tempLocalVariable(SootMethod method) {
        return tempLocalVariableImpl(method);
    }
}
