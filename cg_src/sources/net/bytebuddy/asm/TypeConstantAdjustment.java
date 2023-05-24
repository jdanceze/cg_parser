package net.bytebuddy.asm;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.field.FieldList;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.jar.asm.ClassVisitor;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.jar.asm.Type;
import net.bytebuddy.pool.TypePool;
import net.bytebuddy.utility.OpenedClassReader;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/TypeConstantAdjustment.class */
public enum TypeConstantAdjustment implements AsmVisitorWrapper {
    INSTANCE;

    @Override // net.bytebuddy.asm.AsmVisitorWrapper
    public int mergeWriter(int flags) {
        return flags;
    }

    @Override // net.bytebuddy.asm.AsmVisitorWrapper
    public int mergeReader(int flags) {
        return flags;
    }

    @Override // net.bytebuddy.asm.AsmVisitorWrapper
    public ClassVisitor wrap(TypeDescription instrumentedType, ClassVisitor classVisitor, Implementation.Context implementationContext, TypePool typePool, FieldList<FieldDescription.InDefinedShape> fields, MethodList<?> methods, int writerFlags, int readerFlags) {
        return new TypeConstantDissolvingClassVisitor(classVisitor);
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/TypeConstantAdjustment$TypeConstantDissolvingClassVisitor.class */
    protected static class TypeConstantDissolvingClassVisitor extends ClassVisitor {
        private boolean supportsTypeConstants;

        protected TypeConstantDissolvingClassVisitor(ClassVisitor classVisitor) {
            super(OpenedClassReader.ASM_API, classVisitor);
        }

        @Override // net.bytebuddy.jar.asm.ClassVisitor
        public void visit(int version, int modifiers, String name, String signature, String superClassName, String[] interfaceName) {
            this.supportsTypeConstants = ClassFileVersion.ofMinorMajor(version).isAtLeast(ClassFileVersion.JAVA_V5);
            super.visit(version, modifiers, name, signature, superClassName, interfaceName);
        }

        @Override // net.bytebuddy.jar.asm.ClassVisitor
        public MethodVisitor visitMethod(int modifiers, String name, String descriptor, String signature, String[] exception) {
            MethodVisitor methodVisitor = super.visitMethod(modifiers, name, descriptor, signature, exception);
            return (this.supportsTypeConstants || methodVisitor == null) ? methodVisitor : new TypeConstantDissolvingMethodVisitor(methodVisitor);
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/TypeConstantAdjustment$TypeConstantDissolvingClassVisitor$TypeConstantDissolvingMethodVisitor.class */
        protected static class TypeConstantDissolvingMethodVisitor extends MethodVisitor {
            private static final String JAVA_LANG_CLASS = "java/lang/Class";
            private static final String FOR_NAME = "forName";
            private static final String DESCRIPTOR = "(Ljava/lang/String;)Ljava/lang/Class;";

            protected TypeConstantDissolvingMethodVisitor(MethodVisitor methodVisitor) {
                super(OpenedClassReader.ASM_API, methodVisitor);
            }

            @Override // net.bytebuddy.jar.asm.MethodVisitor
            @SuppressFBWarnings(value = {"SF_SWITCH_NO_DEFAULT"}, justification = "Fall through to default case is intentional")
            public void visitLdcInsn(Object value) {
                if (value instanceof Type) {
                    Type type = (Type) value;
                    switch (type.getSort()) {
                        case 9:
                        case 10:
                            super.visitLdcInsn(type.getInternalName().replace('/', '.'));
                            super.visitMethodInsn(184, "java/lang/Class", FOR_NAME, DESCRIPTOR, false);
                            return;
                    }
                }
                super.visitLdcInsn(value);
            }
        }
    }
}
