package org.objectweb.asm.commons;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.TypePath;
/* loaded from: gencallgraphv3.jar:asm-commons-9.4.jar:org/objectweb/asm/commons/MethodRemapper.class */
public class MethodRemapper extends MethodVisitor {
    protected final Remapper remapper;

    public MethodRemapper(MethodVisitor methodVisitor, Remapper remapper) {
        this(Opcodes.ASM9, methodVisitor, remapper);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public MethodRemapper(int api, MethodVisitor methodVisitor, Remapper remapper) {
        super(api, methodVisitor);
        this.remapper = remapper;
    }

    @Override // org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitAnnotationDefault() {
        AnnotationVisitor annotationVisitor = super.visitAnnotationDefault();
        if (annotationVisitor == null) {
            return annotationVisitor;
        }
        return createAnnotationRemapper(null, annotationVisitor);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        AnnotationVisitor annotationVisitor = super.visitAnnotation(this.remapper.mapDesc(descriptor), visible);
        if (annotationVisitor == null) {
            return annotationVisitor;
        }
        return createAnnotationRemapper(descriptor, annotationVisitor);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        AnnotationVisitor annotationVisitor = super.visitTypeAnnotation(typeRef, typePath, this.remapper.mapDesc(descriptor), visible);
        if (annotationVisitor == null) {
            return annotationVisitor;
        }
        return createAnnotationRemapper(descriptor, annotationVisitor);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible) {
        AnnotationVisitor annotationVisitor = super.visitParameterAnnotation(parameter, this.remapper.mapDesc(descriptor), visible);
        if (annotationVisitor == null) {
            return annotationVisitor;
        }
        return createAnnotationRemapper(descriptor, annotationVisitor);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
        super.visitFrame(type, numLocal, remapFrameTypes(numLocal, local), numStack, remapFrameTypes(numStack, stack));
    }

    private Object[] remapFrameTypes(int numTypes, Object[] frameTypes) {
        if (frameTypes == null) {
            return frameTypes;
        }
        Object[] remappedFrameTypes = null;
        for (int i = 0; i < numTypes; i++) {
            if (frameTypes[i] instanceof String) {
                if (remappedFrameTypes == null) {
                    remappedFrameTypes = new Object[numTypes];
                    System.arraycopy(frameTypes, 0, remappedFrameTypes, 0, numTypes);
                }
                remappedFrameTypes[i] = this.remapper.mapType((String) frameTypes[i]);
            }
        }
        return remappedFrameTypes == null ? frameTypes : remappedFrameTypes;
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        super.visitFieldInsn(opcode, this.remapper.mapType(owner), this.remapper.mapFieldName(owner, name, descriptor), this.remapper.mapDesc(descriptor));
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitMethodInsn(int opcodeAndSource, String owner, String name, String descriptor, boolean isInterface) {
        if (this.api < 327680 && (opcodeAndSource & 256) == 0) {
            super.visitMethodInsn(opcodeAndSource, owner, name, descriptor, isInterface);
        } else {
            super.visitMethodInsn(opcodeAndSource, this.remapper.mapType(owner), this.remapper.mapMethodName(owner, name, descriptor), this.remapper.mapMethodDesc(descriptor), isInterface);
        }
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
        Object[] remappedBootstrapMethodArguments = new Object[bootstrapMethodArguments.length];
        for (int i = 0; i < bootstrapMethodArguments.length; i++) {
            remappedBootstrapMethodArguments[i] = this.remapper.mapValue(bootstrapMethodArguments[i]);
        }
        super.visitInvokeDynamicInsn(this.remapper.mapInvokeDynamicMethodName(name, descriptor), this.remapper.mapMethodDesc(descriptor), (Handle) this.remapper.mapValue(bootstrapMethodHandle), remappedBootstrapMethodArguments);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitTypeInsn(int opcode, String type) {
        super.visitTypeInsn(opcode, this.remapper.mapType(type));
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitLdcInsn(Object value) {
        super.visitLdcInsn(this.remapper.mapValue(value));
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
        super.visitMultiANewArrayInsn(this.remapper.mapDesc(descriptor), numDimensions);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        AnnotationVisitor annotationVisitor = super.visitInsnAnnotation(typeRef, typePath, this.remapper.mapDesc(descriptor), visible);
        if (annotationVisitor == null) {
            return annotationVisitor;
        }
        return createAnnotationRemapper(descriptor, annotationVisitor);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        super.visitTryCatchBlock(start, end, handler, type == null ? null : this.remapper.mapType(type));
    }

    @Override // org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        AnnotationVisitor annotationVisitor = super.visitTryCatchAnnotation(typeRef, typePath, this.remapper.mapDesc(descriptor), visible);
        if (annotationVisitor == null) {
            return annotationVisitor;
        }
        return createAnnotationRemapper(descriptor, annotationVisitor);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        super.visitLocalVariable(name, this.remapper.mapDesc(descriptor), this.remapper.mapSignature(signature, true), start, end, index);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
        AnnotationVisitor annotationVisitor = super.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, this.remapper.mapDesc(descriptor), visible);
        if (annotationVisitor == null) {
            return annotationVisitor;
        }
        return createAnnotationRemapper(descriptor, annotationVisitor);
    }

    @Deprecated
    protected AnnotationVisitor createAnnotationRemapper(AnnotationVisitor annotationVisitor) {
        return new AnnotationRemapper(this.api, null, annotationVisitor, this.remapper);
    }

    protected AnnotationVisitor createAnnotationRemapper(String descriptor, AnnotationVisitor annotationVisitor) {
        return new AnnotationRemapper(this.api, descriptor, annotationVisitor, this.remapper).orDeprecatedValue(createAnnotationRemapper(annotationVisitor));
    }
}
