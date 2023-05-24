package soot.asm;

import com.google.common.base.Optional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.commons.JSRInlinerAdapter;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.TryCatchBlockNode;
import soot.ArrayType;
import soot.MethodSource;
import soot.RefType;
import soot.SootMethod;
import soot.Type;
import soot.tagkit.AnnotationDefaultTag;
import soot.tagkit.AnnotationTag;
import soot.tagkit.ParamNamesTag;
import soot.tagkit.VisibilityAnnotationTag;
import soot.tagkit.VisibilityLocalVariableAnnotationTag;
import soot.tagkit.VisibilityParameterAnnotationTag;
/* loaded from: gencallgraphv3.jar:soot/asm/MethodBuilder.class */
public class MethodBuilder extends JSRInlinerAdapter {
    private TagBuilder tb;
    private VisibilityAnnotationTag[] visibleParamAnnotations;
    private VisibilityAnnotationTag[] invisibleParamAnnotations;
    private List<VisibilityAnnotationTag> visibleLocalVarAnnotations;
    private List<VisibilityAnnotationTag> invisibleLocalVarAnnotations;
    private final SootMethod method;
    private final SootClassBuilder scb;
    private final String[] parameterNames;
    private final Map<Integer, Integer> slotToParameter;

    public MethodBuilder(SootMethod method, SootClassBuilder scb, String desc, String[] ex) {
        super(393216, null, method.getModifiers(), method.getName(), desc, null, ex);
        this.method = method;
        this.scb = scb;
        this.parameterNames = new String[method.getParameterCount()];
        this.slotToParameter = createSlotToParameterMap();
    }

    private Map<Integer, Integer> createSlotToParameterMap() {
        int paramCount = this.method.getParameterCount();
        Map<Integer, Integer> slotMap = new HashMap<>(paramCount);
        int curSlot = this.method.isStatic() ? 0 : 1;
        for (int i = 0; i < paramCount; i++) {
            slotMap.put(Integer.valueOf(curSlot), Integer.valueOf(i));
            curSlot++;
            if (AsmUtil.isDWord(this.method.getParameterType(i))) {
                curSlot++;
            }
        }
        return slotMap;
    }

    private TagBuilder getTagBuilder() {
        TagBuilder t = this.tb;
        if (t == null) {
            TagBuilder tagBuilder = new TagBuilder(this.method, this.scb);
            this.tb = tagBuilder;
            t = tagBuilder;
        }
        return t;
    }

    @Override // org.objectweb.asm.tree.MethodNode, org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return getTagBuilder().visitAnnotation(desc, visible);
    }

    @Override // org.objectweb.asm.tree.MethodNode, org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitAnnotationDefault() {
        return new AnnotationElemBuilder(1) { // from class: soot.asm.MethodBuilder.1
            @Override // soot.asm.AnnotationElemBuilder, org.objectweb.asm.AnnotationVisitor
            public void visitEnd() {
                MethodBuilder.this.method.addTag(new AnnotationDefaultTag(this.elems.get(0)));
            }
        };
    }

    @Override // org.objectweb.asm.tree.MethodNode, org.objectweb.asm.MethodVisitor
    public void visitAttribute(Attribute attr) {
        getTagBuilder().visitAttribute(attr);
    }

    @Override // org.objectweb.asm.tree.MethodNode, org.objectweb.asm.MethodVisitor
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        Integer paramIdx;
        super.visitLocalVariable(name, descriptor, signature, start, end, index);
        if (name != null && !name.isEmpty() && index > 0 && (paramIdx = this.slotToParameter.get(Integer.valueOf(index))) != null) {
            this.parameterNames[paramIdx.intValue()] = name;
        }
    }

    @Override // org.objectweb.asm.tree.MethodNode, org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
        final VisibilityAnnotationTag vat = new VisibilityAnnotationTag(visible ? 0 : 1);
        if (visible) {
            if (this.visibleLocalVarAnnotations == null) {
                this.visibleLocalVarAnnotations = new ArrayList(2);
            }
            this.visibleLocalVarAnnotations.add(vat);
        } else {
            if (this.invisibleLocalVarAnnotations == null) {
                this.invisibleLocalVarAnnotations = new ArrayList(2);
            }
            this.invisibleLocalVarAnnotations.add(vat);
        }
        return new AnnotationElemBuilder() { // from class: soot.asm.MethodBuilder.2
            @Override // soot.asm.AnnotationElemBuilder, org.objectweb.asm.AnnotationVisitor
            public void visitEnd() {
                AnnotationTag annotTag = new AnnotationTag(MethodBuilder.this.desc, this.elems);
                vat.addAnnotation(annotTag);
            }
        };
    }

    @Override // org.objectweb.asm.tree.MethodNode, org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitParameterAnnotation(int parameter, final String desc, boolean visible) {
        VisibilityAnnotationTag vat;
        if (visible) {
            VisibilityAnnotationTag[] vats = this.visibleParamAnnotations;
            if (vats == null) {
                vats = new VisibilityAnnotationTag[this.method.getParameterCount()];
                this.visibleParamAnnotations = vats;
            }
            vat = vats[parameter];
            if (vat == null) {
                vat = new VisibilityAnnotationTag(0);
                vats[parameter] = vat;
            }
        } else {
            VisibilityAnnotationTag[] vats2 = this.invisibleParamAnnotations;
            if (vats2 == null) {
                vats2 = new VisibilityAnnotationTag[this.method.getParameterCount()];
                this.invisibleParamAnnotations = vats2;
            }
            vat = vats2[parameter];
            if (vat == null) {
                vat = new VisibilityAnnotationTag(1);
                vats2[parameter] = vat;
            }
        }
        final VisibilityAnnotationTag _vat = vat;
        return new AnnotationElemBuilder() { // from class: soot.asm.MethodBuilder.3
            @Override // soot.asm.AnnotationElemBuilder, org.objectweb.asm.AnnotationVisitor
            public void visitEnd() {
                AnnotationTag annotTag = new AnnotationTag(desc, this.elems);
                _vat.addAnnotation(annotTag);
            }
        };
    }

    @Override // org.objectweb.asm.tree.MethodNode, org.objectweb.asm.MethodVisitor
    public void visitTypeInsn(int op, String t) {
        super.visitTypeInsn(op, t);
        Type rt = AsmUtil.toJimpleRefType(t, Optional.fromNullable(this.scb.getKlass().moduleName));
        if (rt instanceof ArrayType) {
            this.scb.addDep(((ArrayType) rt).baseType);
        } else {
            this.scb.addDep(rt);
        }
    }

    @Override // org.objectweb.asm.tree.MethodNode, org.objectweb.asm.MethodVisitor
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        super.visitFieldInsn(opcode, owner, name, desc);
        for (Type t : AsmUtil.toJimpleDesc(desc, Optional.fromNullable(this.scb.getKlass().moduleName))) {
            if (t instanceof RefType) {
                this.scb.addDep(t);
            }
        }
        this.scb.addDep(AsmUtil.toQualifiedName(owner));
    }

    @Override // org.objectweb.asm.tree.MethodNode, org.objectweb.asm.MethodVisitor
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean isInterf) {
        super.visitMethodInsn(opcode, owner, name, desc, isInterf);
        for (Type t : AsmUtil.toJimpleDesc(desc, Optional.fromNullable(this.scb.getKlass().moduleName))) {
            addDeps(t);
        }
        this.scb.addDep(AsmUtil.toJimpleRefType(owner, Optional.fromNullable(this.scb.getKlass().moduleName)));
    }

    @Override // org.objectweb.asm.tree.MethodNode, org.objectweb.asm.MethodVisitor
    public void visitLdcInsn(Object cst) {
        super.visitLdcInsn(cst);
        if (cst instanceof Handle) {
            Handle methodHandle = (Handle) cst;
            this.scb.addDep(AsmUtil.toBaseType(methodHandle.getOwner(), Optional.fromNullable(this.scb.getKlass().moduleName)));
        }
    }

    private void addDeps(Type t) {
        if (t instanceof RefType) {
            this.scb.addDep(t);
        } else if (t instanceof ArrayType) {
            ArrayType at = (ArrayType) t;
            addDeps(at.getElementType());
        }
    }

    @Override // org.objectweb.asm.tree.MethodNode, org.objectweb.asm.MethodVisitor
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        super.visitTryCatchBlock(start, end, handler, type);
        if (type != null) {
            this.scb.addDep(AsmUtil.toQualifiedName(type));
        }
    }

    @Override // org.objectweb.asm.commons.JSRInlinerAdapter, org.objectweb.asm.tree.MethodNode, org.objectweb.asm.MethodVisitor
    public void visitEnd() {
        VisibilityAnnotationTag[] visibilityAnnotationTagArr;
        VisibilityAnnotationTag[] visibilityAnnotationTagArr2;
        super.visitEnd();
        if (this.visibleParamAnnotations != null) {
            VisibilityParameterAnnotationTag tag = new VisibilityParameterAnnotationTag(this.visibleParamAnnotations.length, 0);
            for (VisibilityAnnotationTag vat : this.visibleParamAnnotations) {
                tag.addVisibilityAnnotation(vat);
            }
            this.method.addTag(tag);
        }
        if (this.invisibleParamAnnotations != null) {
            VisibilityParameterAnnotationTag tag2 = new VisibilityParameterAnnotationTag(this.invisibleParamAnnotations.length, 1);
            for (VisibilityAnnotationTag vat2 : this.invisibleParamAnnotations) {
                tag2.addVisibilityAnnotation(vat2);
            }
            this.method.addTag(tag2);
        }
        if (this.visibleLocalVarAnnotations != null) {
            VisibilityLocalVariableAnnotationTag tag3 = new VisibilityLocalVariableAnnotationTag(this.visibleLocalVarAnnotations.size(), 0);
            for (VisibilityAnnotationTag vat3 : this.visibleLocalVarAnnotations) {
                tag3.addVisibilityAnnotation(vat3);
            }
            this.method.addTag(tag3);
        }
        if (this.invisibleLocalVarAnnotations != null) {
            VisibilityLocalVariableAnnotationTag tag4 = new VisibilityLocalVariableAnnotationTag(this.invisibleLocalVarAnnotations.size(), 1);
            for (VisibilityAnnotationTag vat4 : this.invisibleLocalVarAnnotations) {
                tag4.addVisibilityAnnotation(vat4);
            }
            this.method.addTag(tag4);
        }
        if (!isFullyEmpty(this.parameterNames)) {
            this.method.addTag(new ParamNamesTag(this.parameterNames));
        }
        if (this.method.isConcrete()) {
            this.method.setSource(createAsmMethodSource(this.maxLocals, this.instructions, this.localVariables, this.tryCatchBlocks, this.scb.getKlass().moduleName));
        }
    }

    protected MethodSource createAsmMethodSource(int maxLocals, InsnList instructions, List<LocalVariableNode> localVariables, List<TryCatchBlockNode> tryCatchBlocks, String moduleName) {
        return new AsmMethodSource(maxLocals, instructions, localVariables, tryCatchBlocks, moduleName);
    }

    private boolean isFullyEmpty(String[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null && !array[i].isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        return this.method.toString();
    }

    @Override // org.objectweb.asm.tree.MethodNode, org.objectweb.asm.MethodVisitor
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
        super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
        String bsmClsName = AsmUtil.toQualifiedName(bootstrapMethodHandle.getOwner());
        this.scb.addDep(RefType.v(bsmClsName));
        for (Object arg : bootstrapMethodArguments) {
            if (arg instanceof Handle) {
                Handle argHandle = (Handle) arg;
                String handleClsName = AsmUtil.toQualifiedName(argHandle.getOwner());
                this.scb.addDep(RefType.v(handleClsName));
            }
        }
    }
}
