package soot.dexpler.instructions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.ReferenceInstruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction35c;
import org.jf.dexlib2.iface.instruction.formats.Instruction3rc;
import org.jf.dexlib2.iface.instruction.formats.Instruction45cc;
import org.jf.dexlib2.iface.instruction.formats.Instruction4rcc;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import soot.JavaBasicTypes;
import soot.Local;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootFieldRef;
import soot.SootMethodRef;
import soot.SootResolver;
import soot.Type;
import soot.dexpler.DexBody;
import soot.dexpler.DexType;
import soot.dexpler.Util;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.MethodHandle;
/* loaded from: gencallgraphv3.jar:soot/dexpler/instructions/MethodInvocationInstruction.class */
public abstract class MethodInvocationInstruction extends DexlibAbstractInstruction implements DanglingInstruction {
    protected InvokeExpr invocation;
    protected AssignStmt assign;

    public MethodInvocationInstruction(Instruction instruction, int codeAddress) {
        super(instruction, codeAddress);
        this.assign = null;
    }

    @Override // soot.dexpler.instructions.DanglingInstruction
    public void finalize(DexBody body, DexlibAbstractInstruction successor) {
        if (successor instanceof MoveResultInstruction) {
            this.assign = Jimple.v().newAssignStmt(body.getStoreResultLocal(), this.invocation);
            setUnit(this.assign);
            addTags(this.assign);
            body.add(this.assign);
            this.unit = this.assign;
            return;
        }
        InvokeStmt invoke = Jimple.v().newInvokeStmt(this.invocation);
        setUnit(invoke);
        addTags(invoke);
        body.add(invoke);
        this.unit = invoke;
    }

    @Override // soot.dexpler.instructions.DexlibAbstractInstruction
    public Set<Type> introducedTypes() {
        Set<Type> types = new HashSet<>();
        MethodReference method = (MethodReference) ((ReferenceInstruction) this.instruction).getReference();
        types.add(DexType.toSoot(method.getDefiningClass()));
        types.add(DexType.toSoot(method.getReturnType()));
        List<? extends CharSequence> paramTypes = method.getParameterTypes();
        if (paramTypes != null) {
            for (CharSequence type : paramTypes) {
                types.add(DexType.toSoot(type.toString()));
            }
        }
        return types;
    }

    @Override // soot.dexpler.instructions.DexlibAbstractInstruction
    boolean isUsedAsFloatingPoint(DexBody body, int register) {
        return isUsedAsFloatingPoint(body, register, false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isUsedAsFloatingPoint(DexBody body, int register, boolean isStatic) {
        MethodReference item = (MethodReference) ((ReferenceInstruction) this.instruction).getReference();
        List<? extends CharSequence> paramTypes = item.getParameterTypes();
        List<Integer> regs = getUsedRegistersNums();
        if (paramTypes == null) {
            return false;
        }
        int i = 0;
        int j = 0;
        while (i < regs.size()) {
            if (!isStatic && i == 0) {
                j--;
            } else if (regs.get(i).intValue() == register && Util.isFloatLike(DexType.toSoot(paramTypes.get(j).toString()))) {
                return true;
            } else {
                if (DexType.isWide(paramTypes.get(j).toString())) {
                    i++;
                }
            }
            i++;
            j++;
        }
        return false;
    }

    protected boolean isUsedAsObject(DexBody body, int register, boolean isStatic) {
        MethodReference item = (MethodReference) ((ReferenceInstruction) this.instruction).getReference();
        List<? extends CharSequence> paramTypes = item.getParameterTypes();
        List<Integer> regs = getUsedRegistersNums();
        if (paramTypes == null) {
            return false;
        }
        if (!isStatic && regs.get(0).intValue() == register) {
            return true;
        }
        int i = 0;
        int j = 0;
        while (i < regs.size()) {
            if (!isStatic && i == 0) {
                j--;
            } else if (regs.get(i).intValue() == register && (DexType.toSoot(paramTypes.get(j).toString()) instanceof RefType)) {
                return true;
            } else {
                if (DexType.isWide(paramTypes.get(j).toString())) {
                    i++;
                }
            }
            i++;
            j++;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public SootMethodRef getVirtualSootMethodRef() {
        return getNormalSootMethodRef(MethodHandle.Kind.REF_INVOKE_VIRTUAL);
    }

    protected SootMethodRef getStaticSootMethodRef() {
        return getNormalSootMethodRef(MethodHandle.Kind.REF_INVOKE_STATIC);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public SootMethodRef getInterfaceSootMethodRef() {
        return getNormalSootMethodRef(MethodHandle.Kind.REF_INVOKE_INTERFACE);
    }

    protected SootMethodRef getNormalSootMethodRef(MethodHandle.Kind kind) {
        return getSootMethodRef((MethodReference) ((ReferenceInstruction) this.instruction).getReference(), kind);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public SootMethodRef getSootMethodRef(MethodReference mItem, MethodHandle.Kind kind) {
        return getSootMethodRef(convertClassName(mItem.getDefiningClass(), kind), mItem.getName(), mItem.getReturnType(), mItem.getParameterTypes(), kind);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public SootMethodRef getSootMethodRef(SootClass sc, String name, String returnType, List<? extends CharSequence> paramTypes, MethodHandle.Kind kind) {
        return Scene.v().makeMethodRef(sc, name, convertParameterTypes(paramTypes), DexType.toSoot(returnType), kind == MethodHandle.Kind.REF_INVOKE_STATIC);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public SootFieldRef getSootFieldRef(FieldReference mItem, MethodHandle.Kind kind) {
        return getSootFieldRef(convertClassName(mItem.getDefiningClass(), kind), mItem.getName(), mItem.getType(), kind);
    }

    protected SootFieldRef getSootFieldRef(SootClass sc, String name, String type, MethodHandle.Kind kind) {
        return Scene.v().makeFieldRef(sc, name, DexType.toSoot(type), kind == MethodHandle.Kind.REF_GET_FIELD_STATIC || kind == MethodHandle.Kind.REF_PUT_FIELD_STATIC);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public List<Type> convertParameterTypes(List<? extends CharSequence> paramTypes) {
        List<Type> parameterTypes = new ArrayList<>();
        if (paramTypes != null) {
            for (CharSequence type : paramTypes) {
                parameterTypes.add(DexType.toSoot(type.toString()));
            }
        }
        return parameterTypes;
    }

    protected SootClass convertClassName(String name, MethodHandle.Kind kind) {
        String name2;
        if (name.startsWith("[")) {
            name2 = JavaBasicTypes.JAVA_LANG_OBJECT;
        } else {
            name2 = Util.dottedClassName(name);
        }
        SootClass sc = SootResolver.v().makeClassRef(name2);
        if (kind == MethodHandle.Kind.REF_INVOKE_INTERFACE && sc.isPhantom()) {
            sc.setModifiers(sc.getModifiers() | 512);
        }
        return sc;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public List<Local> buildParameters(DexBody body, List<? extends CharSequence> paramTypes, boolean isStatic) {
        List<Local> parameters = new ArrayList<>();
        List<Integer> regs = getUsedRegistersNums();
        int i = 0;
        int j = 0;
        while (i < regs.size()) {
            parameters.add(body.getRegisterLocal(regs.get(i).intValue()));
            if (!isStatic && i == 0) {
                j--;
            } else if (paramTypes != null && DexType.isWide(paramTypes.get(j).toString())) {
                i++;
            }
            i++;
            j++;
        }
        return parameters;
    }

    protected List<Integer> getUsedRegistersNums() {
        if (this.instruction instanceof Instruction35c) {
            return getUsedRegistersNums((Instruction35c) this.instruction);
        }
        if (this.instruction instanceof Instruction3rc) {
            return getUsedRegistersNums((Instruction3rc) this.instruction);
        }
        if (this.instruction instanceof Instruction45cc) {
            return getUsedRegistersNums((Instruction45cc) this.instruction);
        }
        if (this.instruction instanceof Instruction4rcc) {
            return getUsedRegistersNums((Instruction4rcc) this.instruction);
        }
        throw new RuntimeException("Instruction is neither a InvokeInstruction nor a InvokeRangeInstruction");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void jimplifyVirtual(DexBody body) {
        SootMethodRef ref = getVirtualSootMethodRef();
        if (ref.getDeclaringClass().isInterface()) {
            jimplifyInterface(body);
            return;
        }
        MethodReference item = (MethodReference) ((ReferenceInstruction) this.instruction).getReference();
        List<Local> parameters = buildParameters(body, item.getParameterTypes(), false);
        this.invocation = Jimple.v().newVirtualInvokeExpr(parameters.get(0), ref, parameters.subList(1, parameters.size()));
        body.setDanglingInstruction(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void jimplifyInterface(DexBody body) {
        SootMethodRef ref = getInterfaceSootMethodRef();
        if (!ref.getDeclaringClass().isInterface()) {
            jimplifyVirtual(body);
            return;
        }
        MethodReference item = (MethodReference) ((ReferenceInstruction) this.instruction).getReference();
        List<Local> parameters = buildParameters(body, item.getParameterTypes(), false);
        this.invocation = Jimple.v().newInterfaceInvokeExpr(parameters.get(0), ref, parameters.subList(1, parameters.size()));
        body.setDanglingInstruction(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void jimplifySpecial(DexBody body) {
        MethodReference item = (MethodReference) ((ReferenceInstruction) this.instruction).getReference();
        List<Local> parameters = buildParameters(body, item.getParameterTypes(), false);
        this.invocation = Jimple.v().newSpecialInvokeExpr(parameters.get(0), getVirtualSootMethodRef(), parameters.subList(1, parameters.size()));
        body.setDanglingInstruction(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void jimplifyStatic(DexBody body) {
        MethodReference item = (MethodReference) ((ReferenceInstruction) this.instruction).getReference();
        this.invocation = Jimple.v().newStaticInvokeExpr(getStaticSootMethodRef(), buildParameters(body, item.getParameterTypes(), true));
        body.setDanglingInstruction(this);
    }
}
