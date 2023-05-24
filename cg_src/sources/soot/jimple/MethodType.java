package soot.jimple;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import soot.RefType;
import soot.SootMethod;
import soot.Type;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/MethodType.class */
public class MethodType extends Constant {
    private static final long serialVersionUID = 3523899677165980823L;
    protected Type returnType;
    protected List<Type> parameterTypes;

    private MethodType(List<Type> parameterTypes, Type returnType) {
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
    }

    public static MethodType v(List<Type> paramaterTypes, Type returnType) {
        return new MethodType(paramaterTypes, returnType);
    }

    @Override // soot.Value
    public Type getType() {
        return RefType.v("java.lang.invoke.MethodType");
    }

    public String toString() {
        return "methodtype: " + SootMethod.getSubSignature("__METHODTYPE__", this.parameterTypes, this.returnType);
    }

    public List<Type> getParameterTypes() {
        return this.parameterTypes == null ? Collections.emptyList() : this.parameterTypes;
    }

    public Type getReturnType() {
        return this.returnType;
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((ConstantSwitch) sw).caseMethodType(this);
    }

    public int hashCode() {
        int result = (31 * 17) + Objects.hashCode(this.parameterTypes);
        return (31 * result) + Objects.hashCode(this.returnType);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MethodType other = (MethodType) obj;
        return Objects.equals(this.returnType, other.returnType) && Objects.equals(this.parameterTypes, other.parameterTypes);
    }
}
