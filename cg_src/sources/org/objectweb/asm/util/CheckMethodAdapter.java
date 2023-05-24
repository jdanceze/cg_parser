package org.objectweb.asm.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.bytebuddy.description.modifier.ModifierContributor;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.ConstantDynamic;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.TypeReference;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.BasicVerifier;
/* loaded from: gencallgraphv3.jar:asm-util-9.4.jar:org/objectweb/asm/util/CheckMethodAdapter.class */
public class CheckMethodAdapter extends MethodVisitor {
    private static final Method[] OPCODE_METHODS = {Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INT_INSN, Method.VISIT_INT_INSN, null, null, null, Method.VISIT_VAR_INSN, Method.VISIT_VAR_INSN, Method.VISIT_VAR_INSN, Method.VISIT_VAR_INSN, Method.VISIT_VAR_INSN, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_VAR_INSN, Method.VISIT_VAR_INSN, Method.VISIT_VAR_INSN, Method.VISIT_VAR_INSN, Method.VISIT_VAR_INSN, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, null, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_JUMP_INSN, Method.VISIT_JUMP_INSN, Method.VISIT_JUMP_INSN, Method.VISIT_JUMP_INSN, Method.VISIT_JUMP_INSN, Method.VISIT_JUMP_INSN, Method.VISIT_JUMP_INSN, Method.VISIT_JUMP_INSN, Method.VISIT_JUMP_INSN, Method.VISIT_JUMP_INSN, Method.VISIT_JUMP_INSN, Method.VISIT_JUMP_INSN, Method.VISIT_JUMP_INSN, Method.VISIT_JUMP_INSN, Method.VISIT_JUMP_INSN, Method.VISIT_JUMP_INSN, Method.VISIT_VAR_INSN, null, null, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_FIELD_INSN, Method.VISIT_FIELD_INSN, Method.VISIT_FIELD_INSN, Method.VISIT_FIELD_INSN, Method.VISIT_METHOD_INSN, Method.VISIT_METHOD_INSN, Method.VISIT_METHOD_INSN, Method.VISIT_METHOD_INSN, null, Method.VISIT_TYPE_INSN, Method.VISIT_INT_INSN, Method.VISIT_TYPE_INSN, Method.VISIT_INSN, Method.VISIT_INSN, Method.VISIT_TYPE_INSN, Method.VISIT_TYPE_INSN, Method.VISIT_INSN, Method.VISIT_INSN, null, null, Method.VISIT_JUMP_INSN, Method.VISIT_JUMP_INSN};
    private static final String INVALID = "Invalid ";
    private static final String INVALID_DESCRIPTOR = "Invalid descriptor: ";
    private static final String INVALID_TYPE_REFERENCE = "Invalid type reference sort 0x";
    private static final String INVALID_LOCAL_VARIABLE_INDEX = "Invalid local variable index";
    private static final String MUST_NOT_BE_NULL_OR_EMPTY = " (must not be null or empty)";
    private static final String START_LABEL = "start label";
    private static final String END_LABEL = "end label";
    public int version;
    private int access;
    private int visibleAnnotableParameterCount;
    private int invisibleAnnotableParameterCount;
    private boolean visitCodeCalled;
    private boolean visitMaxCalled;
    private boolean visitEndCalled;
    private int insnCount;
    private final Map<Label, Integer> labelInsnIndices;
    private Set<Label> referencedLabels;
    private int lastFrameInsnIndex;
    private int numExpandedFrames;
    private int numCompressedFrames;
    private List<Label> handlers;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:asm-util-9.4.jar:org/objectweb/asm/util/CheckMethodAdapter$Method.class */
    public enum Method {
        VISIT_INSN,
        VISIT_INT_INSN,
        VISIT_VAR_INSN,
        VISIT_TYPE_INSN,
        VISIT_FIELD_INSN,
        VISIT_METHOD_INSN,
        VISIT_JUMP_INSN
    }

    public CheckMethodAdapter(MethodVisitor methodvisitor) {
        this(methodvisitor, new HashMap());
    }

    public CheckMethodAdapter(MethodVisitor methodVisitor, Map<Label, Integer> labelInsnIndices) {
        this(Opcodes.ASM9, methodVisitor, labelInsnIndices);
        if (getClass() != CheckMethodAdapter.class) {
            throw new IllegalStateException();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CheckMethodAdapter(int api, MethodVisitor methodVisitor, Map<Label, Integer> labelInsnIndices) {
        super(api, methodVisitor);
        this.lastFrameInsnIndex = -1;
        this.labelInsnIndices = labelInsnIndices;
        this.referencedLabels = new HashSet();
        this.handlers = new ArrayList();
    }

    public CheckMethodAdapter(int access, String name, String descriptor, MethodVisitor methodVisitor, Map<Label, Integer> labelInsnIndices) {
        this(Opcodes.ASM9, access, name, descriptor, methodVisitor, labelInsnIndices);
        if (getClass() != CheckMethodAdapter.class) {
            throw new IllegalStateException();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CheckMethodAdapter(int api, int access, String name, String descriptor, final MethodVisitor methodVisitor, Map<Label, Integer> labelInsnIndices) {
        this(api, new MethodNode(api, access, name, descriptor, null, null) { // from class: org.objectweb.asm.util.CheckMethodAdapter.1
            @Override // org.objectweb.asm.tree.MethodNode, org.objectweb.asm.MethodVisitor
            public void visitEnd() {
                Analyzer<BasicValue> analyzer;
                int originalMaxLocals = this.maxLocals;
                int originalMaxStack = this.maxStack;
                boolean checkMaxStackAndLocals = false;
                boolean checkFrames = false;
                if (methodVisitor instanceof MethodWriterWrapper) {
                    MethodWriterWrapper methodWriter = (MethodWriterWrapper) methodVisitor;
                    checkMaxStackAndLocals = !methodWriter.computesMaxs();
                    checkFrames = methodWriter.requiresFrames() && !methodWriter.computesFrames();
                }
                if (checkFrames) {
                    analyzer = new CheckFrameAnalyzer<>(new BasicVerifier());
                } else {
                    analyzer = new Analyzer<>(new BasicVerifier());
                }
                Analyzer<BasicValue> analyzer2 = analyzer;
                try {
                    if (checkMaxStackAndLocals) {
                        analyzer2.analyze("dummy", this);
                    } else {
                        analyzer2.analyzeAndComputeMaxs("dummy", this);
                    }
                } catch (IndexOutOfBoundsException | AnalyzerException e) {
                    throwError(analyzer2, e);
                }
                if (methodVisitor != null) {
                    this.maxLocals = originalMaxLocals;
                    this.maxStack = originalMaxStack;
                    accept(methodVisitor);
                }
            }

            private void throwError(Analyzer<BasicValue> analyzer, Exception e) {
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter((Writer) stringWriter, true);
                CheckClassAdapter.printAnalyzerResult(this, analyzer, printWriter);
                printWriter.close();
                throw new IllegalArgumentException(e.getMessage() + ' ' + stringWriter.toString(), e);
            }
        }, labelInsnIndices);
        this.access = access;
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitParameter(String name, int access) {
        if (name != null) {
            checkUnqualifiedName(this.version, name, "name");
        }
        CheckClassAdapter.checkAccess(access, ModifierContributor.ForParameter.MASK);
        super.visitParameter(name, access);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        checkVisitEndNotCalled();
        checkDescriptor(this.version, descriptor, false);
        return new CheckAnnotationAdapter(super.visitAnnotation(descriptor, visible));
    }

    @Override // org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        checkVisitEndNotCalled();
        int sort = new TypeReference(typeRef).getSort();
        if (sort != 1 && sort != 18 && sort != 20 && sort != 21 && sort != 22 && sort != 23) {
            throw new IllegalArgumentException(INVALID_TYPE_REFERENCE + Integer.toHexString(sort));
        }
        CheckClassAdapter.checkTypeRef(typeRef);
        checkDescriptor(this.version, descriptor, false);
        return new CheckAnnotationAdapter(super.visitTypeAnnotation(typeRef, typePath, descriptor, visible));
    }

    @Override // org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitAnnotationDefault() {
        checkVisitEndNotCalled();
        return new CheckAnnotationAdapter(super.visitAnnotationDefault(), false);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitAnnotableParameterCount(int parameterCount, boolean visible) {
        checkVisitEndNotCalled();
        if (visible) {
            this.visibleAnnotableParameterCount = parameterCount;
        } else {
            this.invisibleAnnotableParameterCount = parameterCount;
        }
        super.visitAnnotableParameterCount(parameterCount, visible);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible) {
        checkVisitEndNotCalled();
        if ((visible && this.visibleAnnotableParameterCount > 0 && parameter >= this.visibleAnnotableParameterCount) || (!visible && this.invisibleAnnotableParameterCount > 0 && parameter >= this.invisibleAnnotableParameterCount)) {
            throw new IllegalArgumentException("Invalid parameter index");
        }
        checkDescriptor(this.version, descriptor, false);
        return new CheckAnnotationAdapter(super.visitParameterAnnotation(parameter, descriptor, visible));
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitAttribute(Attribute attribute) {
        checkVisitEndNotCalled();
        if (attribute == null) {
            throw new IllegalArgumentException("Invalid attribute (must not be null)");
        }
        super.visitAttribute(attribute);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitCode() {
        if ((this.access & 1024) != 0) {
            throw new UnsupportedOperationException("Abstract methods cannot have code");
        }
        this.visitCodeCalled = true;
        super.visitCode();
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
        int maxNumLocal;
        int maxNumStack;
        if (this.insnCount == this.lastFrameInsnIndex) {
            throw new IllegalStateException("At most one frame can be visited at a given code location.");
        }
        this.lastFrameInsnIndex = this.insnCount;
        switch (type) {
            case -1:
            case 0:
                maxNumLocal = Integer.MAX_VALUE;
                maxNumStack = Integer.MAX_VALUE;
                break;
            case 1:
            case 2:
                maxNumLocal = 3;
                maxNumStack = 0;
                break;
            case 3:
                maxNumLocal = 0;
                maxNumStack = 0;
                break;
            case 4:
                maxNumLocal = 0;
                maxNumStack = 1;
                break;
            default:
                throw new IllegalArgumentException("Invalid frame type " + type);
        }
        if (numLocal > maxNumLocal) {
            throw new IllegalArgumentException("Invalid numLocal=" + numLocal + " for frame type " + type);
        }
        if (numStack > maxNumStack) {
            throw new IllegalArgumentException("Invalid numStack=" + numStack + " for frame type " + type);
        }
        if (type != 2) {
            if (numLocal > 0 && (local == null || local.length < numLocal)) {
                throw new IllegalArgumentException("Array local[] is shorter than numLocal");
            }
            for (int i = 0; i < numLocal; i++) {
                checkFrameValue(local[i]);
            }
        }
        if (numStack > 0 && (stack == null || stack.length < numStack)) {
            throw new IllegalArgumentException("Array stack[] is shorter than numStack");
        }
        for (int i2 = 0; i2 < numStack; i2++) {
            checkFrameValue(stack[i2]);
        }
        if (type == -1) {
            this.numExpandedFrames++;
        } else {
            this.numCompressedFrames++;
        }
        if (this.numExpandedFrames > 0 && this.numCompressedFrames > 0) {
            throw new IllegalArgumentException("Expanded and compressed frames must not be mixed.");
        }
        super.visitFrame(type, numLocal, local, numStack, stack);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitInsn(int opcode) {
        checkVisitCodeCalled();
        checkVisitMaxsNotCalled();
        checkOpcodeMethod(opcode, Method.VISIT_INSN);
        super.visitInsn(opcode);
        this.insnCount++;
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitIntInsn(int opcode, int operand) {
        checkVisitCodeCalled();
        checkVisitMaxsNotCalled();
        checkOpcodeMethod(opcode, Method.VISIT_INT_INSN);
        switch (opcode) {
            case 16:
                checkSignedByte(operand, "Invalid operand");
                break;
            case 17:
                checkSignedShort(operand, "Invalid operand");
                break;
            case 188:
                if (operand < 4 || operand > 11) {
                    throw new IllegalArgumentException("Invalid operand (must be an array type code T_...): " + operand);
                }
                break;
            default:
                throw new AssertionError();
        }
        super.visitIntInsn(opcode, operand);
        this.insnCount++;
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitVarInsn(int opcode, int varIndex) {
        checkVisitCodeCalled();
        checkVisitMaxsNotCalled();
        checkOpcodeMethod(opcode, Method.VISIT_VAR_INSN);
        checkUnsignedShort(varIndex, INVALID_LOCAL_VARIABLE_INDEX);
        super.visitVarInsn(opcode, varIndex);
        this.insnCount++;
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitTypeInsn(int opcode, String type) {
        checkVisitCodeCalled();
        checkVisitMaxsNotCalled();
        checkOpcodeMethod(opcode, Method.VISIT_TYPE_INSN);
        checkInternalName(this.version, type, "type");
        if (opcode == 187 && type.charAt(0) == '[') {
            throw new IllegalArgumentException("NEW cannot be used to create arrays: " + type);
        }
        super.visitTypeInsn(opcode, type);
        this.insnCount++;
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        checkVisitCodeCalled();
        checkVisitMaxsNotCalled();
        checkOpcodeMethod(opcode, Method.VISIT_FIELD_INSN);
        checkInternalName(this.version, owner, "owner");
        checkUnqualifiedName(this.version, name, "name");
        checkDescriptor(this.version, descriptor, false);
        super.visitFieldInsn(opcode, owner, name, descriptor);
        this.insnCount++;
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitMethodInsn(int opcodeAndSource, String owner, String name, String descriptor, boolean isInterface) {
        if (this.api < 327680 && (opcodeAndSource & 256) == 0) {
            super.visitMethodInsn(opcodeAndSource, owner, name, descriptor, isInterface);
            return;
        }
        int opcode = opcodeAndSource & (-257);
        checkVisitCodeCalled();
        checkVisitMaxsNotCalled();
        checkOpcodeMethod(opcode, Method.VISIT_METHOD_INSN);
        if (opcode != 183 || !"<init>".equals(name)) {
            checkMethodIdentifier(this.version, name, "name");
        }
        checkInternalName(this.version, owner, "owner");
        checkMethodDescriptor(this.version, descriptor);
        if (opcode == 182 && isInterface) {
            throw new IllegalArgumentException("INVOKEVIRTUAL can't be used with interfaces");
        }
        if (opcode == 185 && !isInterface) {
            throw new IllegalArgumentException("INVOKEINTERFACE can't be used with classes");
        }
        if (opcode == 183 && isInterface && (this.version & 65535) < 52) {
            throw new IllegalArgumentException("INVOKESPECIAL can't be used with interfaces prior to Java 8");
        }
        super.visitMethodInsn(opcodeAndSource, owner, name, descriptor, isInterface);
        this.insnCount++;
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
        checkVisitCodeCalled();
        checkVisitMaxsNotCalled();
        checkMethodIdentifier(this.version, name, "name");
        checkMethodDescriptor(this.version, descriptor);
        if (bootstrapMethodHandle.getTag() != 6 && bootstrapMethodHandle.getTag() != 8) {
            throw new IllegalArgumentException("invalid handle tag " + bootstrapMethodHandle.getTag());
        }
        for (Object bootstrapMethodArgument : bootstrapMethodArguments) {
            checkLdcConstant(bootstrapMethodArgument);
        }
        super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
        this.insnCount++;
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitJumpInsn(int opcode, Label label) {
        checkVisitCodeCalled();
        checkVisitMaxsNotCalled();
        checkOpcodeMethod(opcode, Method.VISIT_JUMP_INSN);
        checkLabel(label, false, "label");
        super.visitJumpInsn(opcode, label);
        this.insnCount++;
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitLabel(Label label) {
        checkVisitCodeCalled();
        checkVisitMaxsNotCalled();
        checkLabel(label, false, "label");
        if (this.labelInsnIndices.get(label) != null) {
            throw new IllegalStateException("Already visited label");
        }
        this.labelInsnIndices.put(label, Integer.valueOf(this.insnCount));
        super.visitLabel(label);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitLdcInsn(Object value) {
        checkVisitCodeCalled();
        checkVisitMaxsNotCalled();
        checkLdcConstant(value);
        super.visitLdcInsn(value);
        this.insnCount++;
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitIincInsn(int varIndex, int increment) {
        checkVisitCodeCalled();
        checkVisitMaxsNotCalled();
        checkUnsignedShort(varIndex, INVALID_LOCAL_VARIABLE_INDEX);
        checkSignedShort(increment, "Invalid increment");
        super.visitIincInsn(varIndex, increment);
        this.insnCount++;
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        checkVisitCodeCalled();
        checkVisitMaxsNotCalled();
        if (max < min) {
            throw new IllegalArgumentException("Max = " + max + " must be greater than or equal to min = " + min);
        }
        checkLabel(dflt, false, "default label");
        if (labels == null || labels.length != (max - min) + 1) {
            throw new IllegalArgumentException("There must be max - min + 1 labels");
        }
        for (int i = 0; i < labels.length; i++) {
            checkLabel(labels[i], false, "label at index " + i);
        }
        super.visitTableSwitchInsn(min, max, dflt, labels);
        this.insnCount++;
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        checkVisitMaxsNotCalled();
        checkVisitCodeCalled();
        checkLabel(dflt, false, "default label");
        if (keys == null || labels == null || keys.length != labels.length) {
            throw new IllegalArgumentException("There must be the same number of keys and labels");
        }
        for (int i = 0; i < labels.length; i++) {
            checkLabel(labels[i], false, "label at index " + i);
        }
        super.visitLookupSwitchInsn(dflt, keys, labels);
        this.insnCount++;
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
        checkVisitCodeCalled();
        checkVisitMaxsNotCalled();
        checkDescriptor(this.version, descriptor, false);
        if (descriptor.charAt(0) != '[') {
            throw new IllegalArgumentException("Invalid descriptor (must be an array type descriptor): " + descriptor);
        }
        if (numDimensions < 1) {
            throw new IllegalArgumentException("Invalid dimensions (must be greater than 0): " + numDimensions);
        }
        if (numDimensions > descriptor.lastIndexOf(91) + 1) {
            throw new IllegalArgumentException("Invalid dimensions (must not be greater than numDimensions(descriptor)): " + numDimensions);
        }
        super.visitMultiANewArrayInsn(descriptor, numDimensions);
        this.insnCount++;
    }

    @Override // org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        checkVisitCodeCalled();
        checkVisitMaxsNotCalled();
        int sort = new TypeReference(typeRef).getSort();
        if (sort != 67 && sort != 68 && sort != 69 && sort != 70 && sort != 71 && sort != 72 && sort != 73 && sort != 74 && sort != 75) {
            throw new IllegalArgumentException(INVALID_TYPE_REFERENCE + Integer.toHexString(sort));
        }
        CheckClassAdapter.checkTypeRef(typeRef);
        checkDescriptor(this.version, descriptor, false);
        return new CheckAnnotationAdapter(super.visitInsnAnnotation(typeRef, typePath, descriptor, visible));
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        checkVisitCodeCalled();
        checkVisitMaxsNotCalled();
        checkLabel(start, false, START_LABEL);
        checkLabel(end, false, END_LABEL);
        checkLabel(handler, false, "handler label");
        if (this.labelInsnIndices.get(start) != null || this.labelInsnIndices.get(end) != null || this.labelInsnIndices.get(handler) != null) {
            throw new IllegalStateException("Try catch blocks must be visited before their labels");
        }
        if (type != null) {
            checkInternalName(this.version, type, "type");
        }
        super.visitTryCatchBlock(start, end, handler, type);
        this.handlers.add(start);
        this.handlers.add(end);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        checkVisitCodeCalled();
        checkVisitMaxsNotCalled();
        int sort = new TypeReference(typeRef).getSort();
        if (sort != 66) {
            throw new IllegalArgumentException(INVALID_TYPE_REFERENCE + Integer.toHexString(sort));
        }
        CheckClassAdapter.checkTypeRef(typeRef);
        checkDescriptor(this.version, descriptor, false);
        return new CheckAnnotationAdapter(super.visitTryCatchAnnotation(typeRef, typePath, descriptor, visible));
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        checkVisitCodeCalled();
        checkVisitMaxsNotCalled();
        checkUnqualifiedName(this.version, name, "name");
        checkDescriptor(this.version, descriptor, false);
        if (signature != null) {
            CheckClassAdapter.checkFieldSignature(signature);
        }
        checkLabel(start, true, START_LABEL);
        checkLabel(end, true, END_LABEL);
        checkUnsignedShort(index, INVALID_LOCAL_VARIABLE_INDEX);
        int startInsnIndex = this.labelInsnIndices.get(start).intValue();
        int endInsnIndex = this.labelInsnIndices.get(end).intValue();
        if (endInsnIndex < startInsnIndex) {
            throw new IllegalArgumentException("Invalid start and end labels (end must be greater than start)");
        }
        super.visitLocalVariable(name, descriptor, signature, start, end, index);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
        checkVisitCodeCalled();
        checkVisitMaxsNotCalled();
        int sort = new TypeReference(typeRef).getSort();
        if (sort != 64 && sort != 65) {
            throw new IllegalArgumentException(INVALID_TYPE_REFERENCE + Integer.toHexString(sort));
        }
        CheckClassAdapter.checkTypeRef(typeRef);
        checkDescriptor(this.version, descriptor, false);
        if (start == null || end == null || index == null || end.length != start.length || index.length != start.length) {
            throw new IllegalArgumentException("Invalid start, end and index arrays (must be non null and of identical length");
        }
        for (int i = 0; i < start.length; i++) {
            checkLabel(start[i], true, START_LABEL);
            checkLabel(end[i], true, END_LABEL);
            checkUnsignedShort(index[i], INVALID_LOCAL_VARIABLE_INDEX);
            int startInsnIndex = this.labelInsnIndices.get(start[i]).intValue();
            int endInsnIndex = this.labelInsnIndices.get(end[i]).intValue();
            if (endInsnIndex < startInsnIndex) {
                throw new IllegalArgumentException("Invalid start and end labels (end must be greater than start)");
            }
        }
        return super.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, descriptor, visible);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitLineNumber(int line, Label start) {
        checkVisitCodeCalled();
        checkVisitMaxsNotCalled();
        checkUnsignedShort(line, "Invalid line number");
        checkLabel(start, true, START_LABEL);
        super.visitLineNumber(line, start);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitMaxs(int maxStack, int maxLocals) {
        checkVisitCodeCalled();
        checkVisitMaxsNotCalled();
        this.visitMaxCalled = true;
        for (Label l : this.referencedLabels) {
            if (this.labelInsnIndices.get(l) == null) {
                throw new IllegalStateException("Undefined label used");
            }
        }
        for (int i = 0; i < this.handlers.size(); i += 2) {
            Integer startInsnIndex = this.labelInsnIndices.get(this.handlers.get(i));
            Integer endInsnIndex = this.labelInsnIndices.get(this.handlers.get(i + 1));
            if (endInsnIndex.intValue() <= startInsnIndex.intValue()) {
                throw new IllegalStateException("Empty try catch block handler range");
            }
        }
        checkUnsignedShort(maxStack, "Invalid max stack");
        checkUnsignedShort(maxLocals, "Invalid max locals");
        super.visitMaxs(maxStack, maxLocals);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitEnd() {
        checkVisitEndNotCalled();
        this.visitEndCalled = true;
        super.visitEnd();
    }

    private void checkVisitCodeCalled() {
        if (!this.visitCodeCalled) {
            throw new IllegalStateException("Cannot visit instructions before visitCode has been called.");
        }
    }

    private void checkVisitMaxsNotCalled() {
        if (this.visitMaxCalled) {
            throw new IllegalStateException("Cannot visit instructions after visitMaxs has been called.");
        }
    }

    private void checkVisitEndNotCalled() {
        if (this.visitEndCalled) {
            throw new IllegalStateException("Cannot visit elements after visitEnd has been called.");
        }
    }

    private void checkFrameValue(Object value) {
        if (value == Opcodes.TOP || value == Opcodes.INTEGER || value == Opcodes.FLOAT || value == Opcodes.LONG || value == Opcodes.DOUBLE || value == Opcodes.NULL || value == Opcodes.UNINITIALIZED_THIS) {
            return;
        }
        if (value instanceof String) {
            checkInternalName(this.version, (String) value, "Invalid stack frame value");
        } else if (value instanceof Label) {
            checkLabel((Label) value, false, "label");
        } else {
            throw new IllegalArgumentException("Invalid stack frame value: " + value);
        }
    }

    private static void checkOpcodeMethod(int opcode, Method method) {
        if (opcode < 0 || opcode > 199 || OPCODE_METHODS[opcode] != method) {
            throw new IllegalArgumentException("Invalid opcode: " + opcode);
        }
    }

    private static void checkSignedByte(int value, String message) {
        if (value < -128 || value > 127) {
            throw new IllegalArgumentException(message + " (must be a signed byte): " + value);
        }
    }

    private static void checkSignedShort(int value, String message) {
        if (value < -32768 || value > 32767) {
            throw new IllegalArgumentException(message + " (must be a signed short): " + value);
        }
    }

    private static void checkUnsignedShort(int value, String message) {
        if (value < 0 || value > 65535) {
            throw new IllegalArgumentException(message + " (must be an unsigned short): " + value);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkConstant(Object value) {
        if (!(value instanceof Integer) && !(value instanceof Float) && !(value instanceof Long) && !(value instanceof Double) && !(value instanceof String)) {
            throw new IllegalArgumentException("Invalid constant: " + value);
        }
    }

    private void checkLdcConstant(Object value) {
        if (value instanceof Type) {
            int sort = ((Type) value).getSort();
            if (sort != 10 && sort != 9 && sort != 11) {
                throw new IllegalArgumentException("Illegal LDC constant value");
            }
            if (sort != 11 && (this.version & 65535) < 49) {
                throw new IllegalArgumentException("ldc of a constant class requires at least version 1.5");
            }
            if (sort == 11 && (this.version & 65535) < 51) {
                throw new IllegalArgumentException("ldc of a method type requires at least version 1.7");
            }
        } else if (value instanceof Handle) {
            if ((this.version & 65535) < 51) {
                throw new IllegalArgumentException("ldc of a Handle requires at least version 1.7");
            } else {
                Handle handle = (Handle) value;
                int tag = handle.getTag();
                if (tag < 1 || tag > 9) {
                    throw new IllegalArgumentException("invalid handle tag " + tag);
                }
                checkInternalName(this.version, handle.getOwner(), "handle owner");
                if (tag <= 4) {
                    checkDescriptor(this.version, handle.getDesc(), false);
                } else {
                    checkMethodDescriptor(this.version, handle.getDesc());
                }
                String handleName = handle.getName();
                if (!"<init>".equals(handleName) || tag != 8) {
                    checkMethodIdentifier(this.version, handleName, "handle name");
                }
            }
        } else {
            if (value instanceof ConstantDynamic) {
                if ((this.version & 65535) < 55) {
                    throw new IllegalArgumentException("ldc of a ConstantDynamic requires at least version 11");
                }
                ConstantDynamic constantDynamic = (ConstantDynamic) value;
                checkMethodIdentifier(this.version, constantDynamic.getName(), "constant dynamic name");
                checkDescriptor(this.version, constantDynamic.getDescriptor(), false);
                checkLdcConstant(constantDynamic.getBootstrapMethod());
                int bootstrapMethodArgumentCount = constantDynamic.getBootstrapMethodArgumentCount();
                for (int i = 0; i < bootstrapMethodArgumentCount; i++) {
                    checkLdcConstant(constantDynamic.getBootstrapMethodArgument(i));
                }
                return;
            }
            checkConstant(value);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkUnqualifiedName(int version, String name, String message) {
        checkIdentifier(version, name, 0, -1, message);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkIdentifier(int version, String name, int startPos, int endPos, String message) {
        if (name == null || (endPos != -1 ? endPos <= startPos : name.length() <= startPos)) {
            throw new IllegalArgumentException(INVALID + message + MUST_NOT_BE_NULL_OR_EMPTY);
        }
        int max = endPos == -1 ? name.length() : endPos;
        if ((version & 65535) >= 49) {
            int i = startPos;
            while (true) {
                int i2 = i;
                if (i2 < max) {
                    if (".;[/".indexOf(name.codePointAt(i2)) == -1) {
                        i = name.offsetByCodePoints(i2, 1);
                    } else {
                        throw new IllegalArgumentException(INVALID + message + " (must not contain . ; [ or /): " + name);
                    }
                } else {
                    return;
                }
            }
        } else {
            int i3 = startPos;
            while (true) {
                int i4 = i3;
                if (i4 < max) {
                    if (i4 == startPos) {
                        if (!Character.isJavaIdentifierStart(name.codePointAt(i4))) {
                            break;
                        }
                        i3 = name.offsetByCodePoints(i4, 1);
                    } else if (!Character.isJavaIdentifierPart(name.codePointAt(i4))) {
                        break;
                    } else {
                        i3 = name.offsetByCodePoints(i4, 1);
                    }
                } else {
                    return;
                }
            }
            throw new IllegalArgumentException(INVALID + message + " (must be a valid Java identifier): " + name);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x00c9, code lost:
        throw new java.lang.IllegalArgumentException(org.objectweb.asm.util.CheckMethodAdapter.INVALID + r7 + " (must be a '<init>', '<clinit>' or a valid Java identifier): " + r6);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void checkMethodIdentifier(int r5, java.lang.String r6, java.lang.String r7) {
        /*
            Method dump skipped, instructions count: 213
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.objectweb.asm.util.CheckMethodAdapter.checkMethodIdentifier(int, java.lang.String, java.lang.String):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkInternalName(int version, String name, String message) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException(INVALID + message + MUST_NOT_BE_NULL_OR_EMPTY);
        }
        if (name.charAt(0) == '[') {
            checkDescriptor(version, name, false);
        } else {
            checkInternalClassName(version, name, message);
        }
    }

    private static void checkInternalClassName(int version, String name, String message) {
        int startIndex = 0;
        while (true) {
            try {
                int slashIndex = name.indexOf(47, startIndex + 1);
                if (slashIndex != -1) {
                    checkIdentifier(version, name, startIndex, slashIndex, null);
                    startIndex = slashIndex + 1;
                } else {
                    checkIdentifier(version, name, startIndex, name.length(), null);
                    return;
                }
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(INVALID + message + " (must be an internal class name): " + name, e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkDescriptor(int version, String descriptor, boolean canBeVoid) {
        int endPos = checkDescriptor(version, descriptor, 0, canBeVoid);
        if (endPos != descriptor.length()) {
            throw new IllegalArgumentException(INVALID_DESCRIPTOR + descriptor);
        }
    }

    private static int checkDescriptor(int version, String descriptor, int startPos, boolean canBeVoid) {
        if (descriptor == null || startPos >= descriptor.length()) {
            throw new IllegalArgumentException("Invalid type descriptor (must not be null or empty)");
        }
        switch (descriptor.charAt(startPos)) {
            case 'B':
            case 'C':
            case 'D':
            case 'F':
            case 'I':
            case 'J':
            case 'S':
            case 'Z':
                return startPos + 1;
            case 'E':
            case 'G':
            case 'H':
            case 'K':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'T':
            case 'U':
            case 'W':
            case 'X':
            case 'Y':
            default:
                throw new IllegalArgumentException(INVALID_DESCRIPTOR + descriptor);
            case 'L':
                int endPos = descriptor.indexOf(59, startPos);
                if (startPos == -1 || endPos - startPos < 2) {
                    throw new IllegalArgumentException(INVALID_DESCRIPTOR + descriptor);
                }
                try {
                    checkInternalClassName(version, descriptor.substring(startPos + 1, endPos), null);
                    return endPos + 1;
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException(INVALID_DESCRIPTOR + descriptor, e);
                }
            case 'V':
                if (canBeVoid) {
                    return startPos + 1;
                }
                throw new IllegalArgumentException(INVALID_DESCRIPTOR + descriptor);
            case '[':
                int pos = startPos + 1;
                while (pos < descriptor.length() && descriptor.charAt(pos) == '[') {
                    pos++;
                }
                if (pos < descriptor.length()) {
                    return checkDescriptor(version, descriptor, pos, false);
                }
                throw new IllegalArgumentException(INVALID_DESCRIPTOR + descriptor);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkMethodDescriptor(int version, String descriptor) {
        if (descriptor == null || descriptor.length() == 0) {
            throw new IllegalArgumentException("Invalid method descriptor (must not be null or empty)");
        }
        if (descriptor.charAt(0) != '(' || descriptor.length() < 3) {
            throw new IllegalArgumentException(INVALID_DESCRIPTOR + descriptor);
        }
        int pos = 1;
        if (descriptor.charAt(1) != ')') {
            while (descriptor.charAt(pos) != 'V') {
                pos = checkDescriptor(version, descriptor, pos, false);
                if (pos < descriptor.length()) {
                    if (descriptor.charAt(pos) == ')') {
                    }
                }
            }
            throw new IllegalArgumentException(INVALID_DESCRIPTOR + descriptor);
        }
        if (checkDescriptor(version, descriptor, pos + 1, true) != descriptor.length()) {
            throw new IllegalArgumentException(INVALID_DESCRIPTOR + descriptor);
        }
    }

    private void checkLabel(Label label, boolean checkVisited, String message) {
        if (label == null) {
            throw new IllegalArgumentException(INVALID + message + " (must not be null)");
        }
        if (checkVisited && this.labelInsnIndices.get(label) == null) {
            throw new IllegalArgumentException(INVALID + message + " (must be visited first)");
        }
        this.referencedLabels.add(label);
    }

    /* loaded from: gencallgraphv3.jar:asm-util-9.4.jar:org/objectweb/asm/util/CheckMethodAdapter$MethodWriterWrapper.class */
    static class MethodWriterWrapper extends MethodVisitor {
        private final int version;
        private final ClassWriter owner;

        /* JADX INFO: Access modifiers changed from: package-private */
        public MethodWriterWrapper(int api, int version, ClassWriter owner, MethodVisitor methodWriter) {
            super(api, methodWriter);
            this.version = version;
            this.owner = owner;
        }

        boolean computesMaxs() {
            return this.owner.hasFlags(1) || this.owner.hasFlags(2);
        }

        boolean computesFrames() {
            return this.owner.hasFlags(2);
        }

        boolean requiresFrames() {
            return (this.version & 65535) >= 51;
        }
    }
}
