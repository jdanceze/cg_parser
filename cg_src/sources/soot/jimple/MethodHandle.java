package soot.jimple;

import java.util.Objects;
import soot.PolymorphicMethodRef;
import soot.RefType;
import soot.SootFieldRef;
import soot.SootMethodRef;
import soot.Type;
import soot.dotnet.types.DotnetBasicTypes;
import soot.options.Options;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/MethodHandle.class */
public class MethodHandle extends Constant {
    private static final long serialVersionUID = -7948291265532721191L;
    protected final SootFieldRef fieldRef;
    protected final SootMethodRef methodRef;
    protected final int kind;

    /* loaded from: gencallgraphv3.jar:soot/jimple/MethodHandle$Kind.class */
    public enum Kind {
        REF_GET_FIELD(1, "REF_GET_FIELD"),
        REF_GET_FIELD_STATIC(2, "REF_GET_FIELD_STATIC"),
        REF_PUT_FIELD(3, "REF_PUT_FIELD"),
        REF_PUT_FIELD_STATIC(4, "REF_PUT_FIELD_STATIC"),
        REF_INVOKE_VIRTUAL(5, "REF_INVOKE_VIRTUAL"),
        REF_INVOKE_STATIC(6, "REF_INVOKE_STATIC"),
        REF_INVOKE_SPECIAL(7, "REF_INVOKE_SPECIAL"),
        REF_INVOKE_CONSTRUCTOR(8, "REF_INVOKE_CONSTRUCTOR"),
        REF_INVOKE_INTERFACE(9, "REF_INVOKE_INTERFACE");
        
        private final int val;
        private final String valStr;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static Kind[] valuesCustom() {
            Kind[] valuesCustom = values();
            int length = valuesCustom.length;
            Kind[] kindArr = new Kind[length];
            System.arraycopy(valuesCustom, 0, kindArr, 0, length);
            return kindArr;
        }

        Kind(int val, String valStr) {
            this.val = val;
            this.valStr = valStr;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.valStr;
        }

        public int getValue() {
            return this.val;
        }

        public static Kind getKind(int kind) {
            Kind[] valuesCustom;
            for (Kind k : valuesCustom()) {
                if (k.getValue() == kind) {
                    return k;
                }
            }
            throw new RuntimeException("Error: No method handle kind for value '" + kind + "'.");
        }

        public static Kind getKind(String kind) {
            Kind[] valuesCustom;
            for (Kind k : valuesCustom()) {
                if (k.toString().equals(kind)) {
                    return k;
                }
            }
            throw new RuntimeException("Error: No method handle kind for value '" + kind + "'.");
        }
    }

    private MethodHandle(SootMethodRef ref, int kind) {
        this.methodRef = ref;
        this.kind = kind;
        this.fieldRef = null;
    }

    private MethodHandle(SootFieldRef ref, int kind) {
        this.fieldRef = ref;
        this.kind = kind;
        this.methodRef = null;
    }

    public static MethodHandle v(SootMethodRef ref, int kind) {
        return new MethodHandle(ref, kind);
    }

    public static MethodHandle v(SootFieldRef ref, int kind) {
        return new MethodHandle(ref, kind);
    }

    public String toString() {
        return "methodhandle: \"" + getKindString() + "\" " + (this.methodRef == null ? Objects.toString(this.fieldRef) : Objects.toString(this.methodRef));
    }

    @Override // soot.Value
    public Type getType() {
        if (Options.v().src_prec() == 7) {
            return isMethodRef() ? RefType.v(DotnetBasicTypes.SYSTEM_RUNTIMEMETHODHANDLE) : RefType.v(DotnetBasicTypes.SYSTEM_RUNTIMEFIELDHANDLE);
        }
        return RefType.v(PolymorphicMethodRef.METHODHANDLE_SIGNATURE);
    }

    public SootMethodRef getMethodRef() {
        return this.methodRef;
    }

    public SootFieldRef getFieldRef() {
        return this.fieldRef;
    }

    public int getKind() {
        return this.kind;
    }

    public String getKindString() {
        return Kind.getKind(this.kind).toString();
    }

    public boolean isFieldRef() {
        return isFieldRef(this.kind);
    }

    public static boolean isFieldRef(int kind) {
        return kind == Kind.REF_GET_FIELD.getValue() || kind == Kind.REF_GET_FIELD_STATIC.getValue() || kind == Kind.REF_PUT_FIELD.getValue() || kind == Kind.REF_PUT_FIELD_STATIC.getValue();
    }

    public boolean isMethodRef() {
        return isMethodRef(this.kind);
    }

    public static boolean isMethodRef(int kind) {
        return kind == Kind.REF_INVOKE_VIRTUAL.getValue() || kind == Kind.REF_INVOKE_STATIC.getValue() || kind == Kind.REF_INVOKE_SPECIAL.getValue() || kind == Kind.REF_INVOKE_CONSTRUCTOR.getValue() || kind == Kind.REF_INVOKE_INTERFACE.getValue();
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((ConstantSwitch) sw).caseMethodHandle(this);
    }

    public int hashCode() {
        int result = (17 * 31) + Objects.hashCode(this.methodRef);
        return (17 * ((17 * result) + Objects.hashCode(this.fieldRef))) + this.kind;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MethodHandle other = (MethodHandle) obj;
        return Objects.equals(this.methodRef, other.methodRef) && Objects.equals(this.fieldRef, other.fieldRef);
    }
}
