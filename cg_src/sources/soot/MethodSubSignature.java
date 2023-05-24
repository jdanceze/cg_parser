package soot;

import com.google.common.base.Joiner;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import soot.coffi.Instruction;
import soot.jimple.Stmt;
import soot.util.NumberedString;
/* loaded from: gencallgraphv3.jar:soot/MethodSubSignature.class */
public class MethodSubSignature {
    public final String methodName;
    public final Type returnType;
    public final List<Type> parameterTypes;
    public final NumberedString numberedSubSig;
    private static final Pattern PATTERN_METHOD_SUBSIG = Pattern.compile("(?<returnType>.*?) (?<methodName>.*?)\\((?<parameters>.*?)\\)");

    public MethodSubSignature(SootMethodRef r) {
        this.methodName = r.getName();
        this.returnType = r.getReturnType();
        this.parameterTypes = r.getParameterTypes();
        this.numberedSubSig = r.getSubSignature();
    }

    public MethodSubSignature(NumberedString subsig) {
        this.numberedSubSig = subsig;
        Matcher m = PATTERN_METHOD_SUBSIG.matcher(subsig.toString());
        if (!m.matches()) {
            throw new IllegalArgumentException("Not a valid subsignature: " + subsig);
        }
        Scene sc = Scene.v();
        this.methodName = m.group(2);
        this.returnType = sc.getTypeUnsafe(m.group(1));
        String parameters = m.group(3);
        String[] spl = parameters.split(",");
        this.parameterTypes = new ArrayList(spl.length);
        if (parameters != null && !parameters.isEmpty()) {
            for (String p : spl) {
                this.parameterTypes.add(sc.getTypeUnsafe(p.trim()));
            }
        }
    }

    public MethodSubSignature(String methodName, Type returnType, List<Type> parameterTypes) {
        this.methodName = methodName;
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
        this.numberedSubSig = Scene.v().getSubSigNumberer().findOrAdd(returnType + Instruction.argsep + methodName + "(" + Joiner.on(',').join(parameterTypes) + ")");
    }

    public MethodSubSignature(Stmt callSite) {
        this(callSite.getInvokeExpr().getMethodRef().getSubSignature());
    }

    public String getMethodName() {
        return this.methodName;
    }

    public Type getReturnType() {
        return this.returnType;
    }

    public List<Type> getParameterTypes() {
        return this.parameterTypes;
    }

    public NumberedString getNumberedSubSig() {
        return this.numberedSubSig;
    }

    public SootMethod getInClassUnsafe(SootClass c) {
        return c.getMethodUnsafe(this.numberedSubSig);
    }

    public int hashCode() {
        int result = (31 * 1) + (this.numberedSubSig == null ? 0 : this.numberedSubSig.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MethodSubSignature other = (MethodSubSignature) obj;
        if (this.numberedSubSig == null) {
            if (other.numberedSubSig != null) {
                return false;
            }
            return true;
        } else if (!this.numberedSubSig.equals(other.numberedSubSig)) {
            return false;
        } else {
            return true;
        }
    }

    public String toString() {
        return this.numberedSubSig.toString();
    }
}
