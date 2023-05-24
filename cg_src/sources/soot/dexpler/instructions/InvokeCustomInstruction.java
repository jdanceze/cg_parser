package soot.dexpler.instructions;

import java.util.ArrayList;
import java.util.List;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.ReferenceInstruction;
import org.jf.dexlib2.iface.reference.CallSiteReference;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodHandleReference;
import org.jf.dexlib2.iface.reference.MethodProtoReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.iface.reference.Reference;
import org.jf.dexlib2.iface.value.BooleanEncodedValue;
import org.jf.dexlib2.iface.value.ByteEncodedValue;
import org.jf.dexlib2.iface.value.CharEncodedValue;
import org.jf.dexlib2.iface.value.DoubleEncodedValue;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.iface.value.FloatEncodedValue;
import org.jf.dexlib2.iface.value.IntEncodedValue;
import org.jf.dexlib2.iface.value.LongEncodedValue;
import org.jf.dexlib2.iface.value.MethodHandleEncodedValue;
import org.jf.dexlib2.iface.value.MethodTypeEncodedValue;
import org.jf.dexlib2.iface.value.NullEncodedValue;
import org.jf.dexlib2.iface.value.ShortEncodedValue;
import org.jf.dexlib2.iface.value.StringEncodedValue;
import org.jf.dexlib2.iface.value.TypeEncodedValue;
import soot.Local;
import soot.Scene;
import soot.SootClass;
import soot.SootMethodRef;
import soot.Value;
import soot.dexpler.DexBody;
import soot.dexpler.DexType;
import soot.jimple.ClassConstant;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.LongConstant;
import soot.jimple.MethodHandle;
import soot.jimple.MethodType;
import soot.jimple.NullConstant;
import soot.jimple.StringConstant;
/* loaded from: gencallgraphv3.jar:soot/dexpler/instructions/InvokeCustomInstruction.class */
public class InvokeCustomInstruction extends MethodInvocationInstruction {
    public InvokeCustomInstruction(Instruction instruction, int codeAddress) {
        super(instruction, codeAddress);
    }

    @Override // soot.dexpler.instructions.DexlibAbstractInstruction
    public void jimplify(DexBody body) {
        CallSiteReference callSiteReference = (CallSiteReference) ((ReferenceInstruction) this.instruction).getReference();
        Reference bootstrapRef = callSiteReference.getMethodHandle().getMemberReference();
        if (!(bootstrapRef instanceof MethodReference)) {
            if (bootstrapRef instanceof FieldReference) {
                throw new RuntimeException("Error: Unexpected FieldReference type for boot strap method.");
            }
            throw new RuntimeException("Error: Unhandled MethodHandleReference of type '" + callSiteReference.getMethodHandle().getMethodHandleType() + "'");
        }
        SootMethodRef bootstrapMethodRef = getBootStrapSootMethodRef();
        MethodHandle.Kind bootStrapKind = dexToSootMethodHandleKind(callSiteReference.getMethodHandle().getMethodHandleType());
        List<Value> bootstrapValues = constantEncodedValuesToValues(callSiteReference.getExtraArguments());
        SootMethodRef methodRef = getCustomSootMethodRef();
        List<Local> methodArgs = buildParameters(body, callSiteReference.getMethodProto().getParameterTypes(), true);
        this.invocation = Jimple.v().newDynamicInvokeExpr(bootstrapMethodRef, bootstrapValues, methodRef, bootStrapKind.getValue(), methodArgs);
        body.setDanglingInstruction(this);
    }

    private List<Value> constantEncodedValuesToValues(List<? extends EncodedValue> in) {
        Value v;
        List<Value> out = new ArrayList<>();
        for (EncodedValue ev : in) {
            if (ev instanceof BooleanEncodedValue) {
                out.add(IntConstant.v(((BooleanEncodedValue) ev).getValue() ? 1 : 0));
            } else if (ev instanceof ByteEncodedValue) {
                out.add(IntConstant.v(((ByteEncodedValue) ev).getValue()));
            } else if (ev instanceof CharEncodedValue) {
                out.add(IntConstant.v(((CharEncodedValue) ev).getValue()));
            } else if (ev instanceof DoubleEncodedValue) {
                out.add(DoubleConstant.v(((DoubleEncodedValue) ev).getValue()));
            } else if (ev instanceof FloatEncodedValue) {
                out.add(FloatConstant.v(((FloatEncodedValue) ev).getValue()));
            } else if (ev instanceof IntEncodedValue) {
                out.add(IntConstant.v(((IntEncodedValue) ev).getValue()));
            } else if (ev instanceof LongEncodedValue) {
                out.add(LongConstant.v(((LongEncodedValue) ev).getValue()));
            } else if (ev instanceof ShortEncodedValue) {
                out.add(IntConstant.v(((ShortEncodedValue) ev).getValue()));
            } else if (ev instanceof StringEncodedValue) {
                out.add(StringConstant.v(((StringEncodedValue) ev).getValue()));
            } else if (ev instanceof NullEncodedValue) {
                out.add(NullConstant.v());
            } else if (ev instanceof MethodTypeEncodedValue) {
                MethodProtoReference protRef = ((MethodTypeEncodedValue) ev).getValue();
                out.add(MethodType.v(convertParameterTypes(protRef.getParameterTypes()), DexType.toSoot(protRef.getReturnType())));
            } else if (ev instanceof TypeEncodedValue) {
                out.add(ClassConstant.v(((TypeEncodedValue) ev).getValue()));
            } else if (ev instanceof MethodHandleEncodedValue) {
                MethodHandleReference mh = ((MethodHandleEncodedValue) ev).getValue();
                Reference ref = mh.getMemberReference();
                MethodHandle.Kind kind = dexToSootMethodHandleKind(mh.getMethodHandleType());
                if (ref instanceof MethodReference) {
                    v = MethodHandle.v(getSootMethodRef((MethodReference) ref, kind), kind.getValue());
                } else if (ref instanceof FieldReference) {
                    v = MethodHandle.v(getSootFieldRef((FieldReference) ref, kind), kind.getValue());
                } else {
                    throw new RuntimeException("Error: Unhandled method reference type " + ref.getClass().toString() + ".");
                }
                MethodHandle handle = v;
                out.add(handle);
            } else {
                throw new RuntimeException("Error: Unhandled constant type '" + ev.getClass().toString() + "' when parsing bootstrap arguments in the call site reference.");
            }
        }
        return out;
    }

    private MethodHandle.Kind dexToSootMethodHandleKind(int kind) {
        switch (kind) {
            case 0:
                return MethodHandle.Kind.REF_PUT_FIELD_STATIC;
            case 1:
                return MethodHandle.Kind.REF_GET_FIELD_STATIC;
            case 2:
                return MethodHandle.Kind.REF_PUT_FIELD;
            case 3:
                return MethodHandle.Kind.REF_GET_FIELD;
            case 4:
                return MethodHandle.Kind.REF_INVOKE_STATIC;
            case 5:
                return MethodHandle.Kind.REF_INVOKE_VIRTUAL;
            case 6:
                return MethodHandle.Kind.REF_INVOKE_CONSTRUCTOR;
            case 7:
                return MethodHandle.Kind.REF_INVOKE_SPECIAL;
            case 8:
                return MethodHandle.Kind.REF_INVOKE_INTERFACE;
            default:
                throw new RuntimeException("Error: Unknown kind '" + kind + "' for method handle");
        }
    }

    protected SootMethodRef getCustomSootMethodRef() {
        CallSiteReference callSiteReference = (CallSiteReference) ((ReferenceInstruction) this.instruction).getReference();
        SootClass dummyclass = Scene.v().getSootClass(SootClass.INVOKEDYNAMIC_DUMMY_CLASS_NAME);
        String methodName = callSiteReference.getMethodName();
        MethodProtoReference methodRef = callSiteReference.getMethodProto();
        return getSootMethodRef(dummyclass, methodName, methodRef.getReturnType(), methodRef.getParameterTypes(), MethodHandle.Kind.REF_INVOKE_STATIC);
    }

    protected SootMethodRef getBootStrapSootMethodRef() {
        MethodHandleReference mh = ((CallSiteReference) ((ReferenceInstruction) this.instruction).getReference()).getMethodHandle();
        return getSootMethodRef((MethodReference) mh.getMemberReference(), dexToSootMethodHandleKind(mh.getMethodHandleType()));
    }
}
