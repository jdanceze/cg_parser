package soot.jimple.infoflow.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import soot.SootMethod;
import soot.Type;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/data/SootMethodAndClass.class */
public class SootMethodAndClass extends AbstractMethodAndClass {
    private int hashCode;

    public SootMethodAndClass(String methodName, String className, String returnType, List<String> parameters) {
        super(methodName, className, returnType, parameters);
        this.hashCode = 0;
    }

    public SootMethodAndClass(String methodName, String className, String returnType, String parameters) {
        super(methodName, className, returnType, parameterFromString(parameters));
        this.hashCode = 0;
    }

    private static List<String> parameterFromString(String parameters) {
        if (parameters != null && !parameters.isEmpty()) {
            return Arrays.asList(parameters.split(","));
        }
        return new ArrayList();
    }

    public SootMethodAndClass(SootMethod sm) {
        super(sm.getName(), sm.getDeclaringClass().getName(), sm.getReturnType().toString(), parameterFromMethod(sm));
        this.hashCode = 0;
    }

    private static List<String> parameterFromMethod(SootMethod sm) {
        ArrayList<String> parameters = new ArrayList<>();
        for (Type p : sm.getParameterTypes()) {
            parameters.add(p.toString());
        }
        return parameters;
    }

    public SootMethodAndClass(SootMethodAndClass methodAndClass) {
        super(methodAndClass.methodName, methodAndClass.className, methodAndClass.returnType, new ArrayList(methodAndClass.parameters));
        this.hashCode = 0;
    }

    public boolean equals(Object another) {
        if (super.equals(another)) {
            return true;
        }
        if (!(another instanceof SootMethodAndClass)) {
            return false;
        }
        SootMethodAndClass otherMethod = (SootMethodAndClass) another;
        if (!this.methodName.equals(otherMethod.methodName) || !this.parameters.equals(otherMethod.parameters) || !this.className.equals(otherMethod.className)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = this.methodName.hashCode() + (this.className.hashCode() * 5);
        }
        return this.hashCode + (this.parameters.hashCode() * 7);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        sb.append(this.className);
        sb.append(": ");
        sb.append(this.returnType);
        sb.append(Instruction.argsep);
        sb.append(this.methodName);
        sb.append("(");
        boolean isFirst = true;
        for (String param : this.parameters) {
            if (!isFirst) {
                sb.append(",");
            }
            sb.append(param);
            isFirst = false;
        }
        sb.append(")>");
        return sb.toString();
    }
}
