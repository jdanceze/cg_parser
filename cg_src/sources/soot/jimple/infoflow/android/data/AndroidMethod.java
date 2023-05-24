package soot.jimple.infoflow.android.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import soot.SootMethod;
import soot.coffi.Instruction;
import soot.jimple.infoflow.data.SootMethodAndClass;
import soot.jimple.infoflow.sourcesSinks.definitions.SourceSinkType;
import soot.jimple.infoflow.util.SootMethodRepresentationParser;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/data/AndroidMethod.class */
public class AndroidMethod extends SootMethodAndClass {
    private Set<String> permissions;
    private SourceSinkType sourceSinkType;

    public AndroidMethod(String methodName, String returnType, String className) {
        super(methodName, className, returnType, new ArrayList());
        this.sourceSinkType = SourceSinkType.Undefined;
        this.permissions = null;
    }

    public AndroidMethod(String methodName, List<String> parameters, String returnType, String className) {
        super(methodName, className, returnType, parameters);
        this.sourceSinkType = SourceSinkType.Undefined;
        this.permissions = null;
    }

    public AndroidMethod(String methodName, List<String> parameters, String returnType, String className, Set<String> permissions) {
        super(methodName, className, returnType, parameters);
        this.sourceSinkType = SourceSinkType.Undefined;
        this.permissions = permissions;
    }

    public AndroidMethod(SootMethod sm) {
        super(sm);
        this.sourceSinkType = SourceSinkType.Undefined;
        this.permissions = null;
    }

    public AndroidMethod(SootMethodAndClass methodAndClass) {
        super(methodAndClass);
        this.sourceSinkType = SourceSinkType.Undefined;
        this.permissions = null;
    }

    public Set<String> getPermissions() {
        return this.permissions == null ? Collections.emptySet() : this.permissions;
    }

    public void setSourceSinkType(SourceSinkType sourceSinkType) {
        this.sourceSinkType = sourceSinkType;
    }

    public void addPermission(String permission) {
        if (this.permissions == null) {
            this.permissions = new HashSet();
        }
        this.permissions.add(permission);
    }

    @Override // soot.jimple.infoflow.data.SootMethodAndClass
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getSignature());
        if (this.permissions != null) {
            for (String perm : this.permissions) {
                sb.append(Instruction.argsep);
                sb.append(perm);
            }
        }
        if (this.sourceSinkType != SourceSinkType.Undefined) {
            sb.append(" ->");
        }
        if (this.sourceSinkType == SourceSinkType.Source) {
            sb.append(" _SOURCE_");
        } else if (this.sourceSinkType == SourceSinkType.Sink) {
            sb.append(" _SINK_ ");
        } else if (this.sourceSinkType == SourceSinkType.Neither) {
            sb.append(" _NONE_");
        } else if (this.sourceSinkType == SourceSinkType.Both) {
            sb.append(" _BOTH_");
        }
        return sb.toString();
    }

    public String getSignatureAndPermissions() {
        String s = getSignature();
        if (this.permissions != null) {
            for (String perm : this.permissions) {
                s = String.valueOf(s) + Instruction.argsep + perm;
            }
        }
        return s;
    }

    public boolean isAnnotated() {
        return this.sourceSinkType != SourceSinkType.Undefined;
    }

    public SourceSinkType getSourceSinkType() {
        return this.sourceSinkType;
    }

    public static AndroidMethod createFromSignature(String signature) {
        if (!signature.startsWith("<")) {
            signature = "<" + signature;
        }
        if (!signature.endsWith(">")) {
            signature = String.valueOf(signature) + ">";
        }
        SootMethodAndClass smac = SootMethodRepresentationParser.v().parseSootMethodString(signature);
        if (smac == null) {
            return null;
        }
        return new AndroidMethod(smac.getMethodName(), smac.getParameters(), smac.getReturnType(), smac.getClassName());
    }

    @Override // soot.jimple.infoflow.data.SootMethodAndClass
    public int hashCode() {
        int result = super.hashCode();
        return (31 * ((31 * result) + (this.permissions == null ? 0 : this.permissions.hashCode()))) + (this.sourceSinkType == null ? 0 : this.sourceSinkType.hashCode());
    }

    @Override // soot.jimple.infoflow.data.SootMethodAndClass
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        AndroidMethod other = (AndroidMethod) obj;
        if (this.permissions == null) {
            if (other.permissions != null) {
                return false;
            }
        } else if (!this.permissions.equals(other.permissions)) {
            return false;
        }
        if (this.sourceSinkType != other.sourceSinkType) {
            return false;
        }
        return true;
    }
}
