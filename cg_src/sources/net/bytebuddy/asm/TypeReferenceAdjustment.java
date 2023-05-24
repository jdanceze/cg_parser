package net.bytebuddy.asm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.field.FieldList;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.jar.asm.AnnotationVisitor;
import net.bytebuddy.jar.asm.ClassVisitor;
import net.bytebuddy.jar.asm.ConstantDynamic;
import net.bytebuddy.jar.asm.FieldVisitor;
import net.bytebuddy.jar.asm.Handle;
import net.bytebuddy.jar.asm.Label;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.jar.asm.RecordComponentVisitor;
import net.bytebuddy.jar.asm.Type;
import net.bytebuddy.jar.asm.TypePath;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.pool.TypePool;
import net.bytebuddy.utility.OpenedClassReader;
@HashCodeAndEqualsPlugin.Enhance
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/TypeReferenceAdjustment.class */
public class TypeReferenceAdjustment extends AsmVisitorWrapper.AbstractBase {
    private final boolean strict;
    private final ElementMatcher.Junction<? super TypeDescription> filter;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.strict == ((TypeReferenceAdjustment) obj).strict && this.filter.equals(((TypeReferenceAdjustment) obj).filter);
    }

    public int hashCode() {
        return (((17 * 31) + (this.strict ? 1 : 0)) * 31) + this.filter.hashCode();
    }

    protected TypeReferenceAdjustment(boolean strict, ElementMatcher.Junction<? super TypeDescription> filter) {
        this.strict = strict;
        this.filter = filter;
    }

    public static TypeReferenceAdjustment strict() {
        return new TypeReferenceAdjustment(true, ElementMatchers.none());
    }

    public static TypeReferenceAdjustment relaxed() {
        return new TypeReferenceAdjustment(false, ElementMatchers.none());
    }

    public TypeReferenceAdjustment filter(ElementMatcher<? super TypeDescription> filter) {
        return new TypeReferenceAdjustment(this.strict, this.filter.or(filter));
    }

    @Override // net.bytebuddy.asm.AsmVisitorWrapper
    public ClassVisitor wrap(TypeDescription instrumentedType, ClassVisitor classVisitor, Implementation.Context implementationContext, TypePool typePool, FieldList<FieldDescription.InDefinedShape> fields, MethodList<?> methods, int writerFlags, int readerFlags) {
        return new TypeReferenceClassVisitor(classVisitor, this.strict, this.filter, typePool);
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/TypeReferenceAdjustment$TypeReferenceClassVisitor.class */
    protected static class TypeReferenceClassVisitor extends ClassVisitor {
        private static final AnnotationVisitor IGNORE_ANNOTATION = null;
        private static final FieldVisitor IGNORE_FIELD = null;
        private static final MethodVisitor IGNORE_METHOD = null;
        private final boolean strict;
        private final ElementMatcher<? super TypeDescription> filter;
        private final TypePool typePool;
        private final Set<String> observedTypes;
        private final Set<String> visitedInnerTypes;

        protected TypeReferenceClassVisitor(ClassVisitor classVisitor, boolean strict, ElementMatcher<? super TypeDescription> filter, TypePool typePool) {
            super(OpenedClassReader.ASM_API, classVisitor);
            this.typePool = typePool;
            this.strict = strict;
            this.filter = filter;
            this.observedTypes = new HashSet();
            this.visitedInnerTypes = new HashSet();
        }

        @Override // net.bytebuddy.jar.asm.ClassVisitor
        public void visit(int version, int modifiers, String internalName, String genericSignature, String superClassInternalName, String[] interfaceInternalName) {
            if (superClassInternalName != null) {
                this.observedTypes.add(superClassInternalName);
            }
            if (interfaceInternalName != null) {
                this.observedTypes.addAll(Arrays.asList(interfaceInternalName));
            }
            super.visit(version, modifiers, internalName, genericSignature, superClassInternalName, interfaceInternalName);
        }

        @Override // net.bytebuddy.jar.asm.ClassVisitor
        public void visitNestHost(String nestHost) {
            this.observedTypes.add(nestHost);
            super.visitNestHost(nestHost);
        }

        @Override // net.bytebuddy.jar.asm.ClassVisitor
        public void visitOuterClass(String ownerTypeInternalName, String methodName, String methodDescriptor) {
            this.observedTypes.add(ownerTypeInternalName);
            super.visitOuterClass(ownerTypeInternalName, methodName, methodDescriptor);
        }

        @Override // net.bytebuddy.jar.asm.ClassVisitor
        public void visitNestMember(String nestMember) {
            this.observedTypes.add(nestMember);
            super.visitNestMember(nestMember);
        }

        @Override // net.bytebuddy.jar.asm.ClassVisitor
        public void visitInnerClass(String internalName, String outerName, String innerName, int modifiers) {
            this.visitedInnerTypes.add(internalName);
            super.visitInnerClass(internalName, outerName, innerName, modifiers);
        }

        @Override // net.bytebuddy.jar.asm.ClassVisitor
        public RecordComponentVisitor visitRecordComponent(String name, String descriptor, String signature) {
            this.observedTypes.add(Type.getType(descriptor).getInternalName());
            return super.visitRecordComponent(name, descriptor, signature);
        }

        @Override // net.bytebuddy.jar.asm.ClassVisitor
        public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
            this.observedTypes.add(Type.getType(descriptor).getInternalName());
            AnnotationVisitor annotationVisitor = super.visitAnnotation(descriptor, visible);
            if (annotationVisitor != null) {
                return new TypeReferenceAnnotationVisitor(annotationVisitor);
            }
            return IGNORE_ANNOTATION;
        }

        @Override // net.bytebuddy.jar.asm.ClassVisitor
        public AnnotationVisitor visitTypeAnnotation(int typeReference, TypePath typePath, String descriptor, boolean visible) {
            this.observedTypes.add(Type.getType(descriptor).getInternalName());
            AnnotationVisitor annotationVisitor = super.visitTypeAnnotation(typeReference, typePath, descriptor, visible);
            if (annotationVisitor != null) {
                return new TypeReferenceAnnotationVisitor(annotationVisitor);
            }
            return IGNORE_ANNOTATION;
        }

        @Override // net.bytebuddy.jar.asm.ClassVisitor
        public FieldVisitor visitField(int modifiers, String name, String descriptor, String signature, Object defaultValue) {
            FieldVisitor fieldVisitor = super.visitField(modifiers, name, descriptor, signature, defaultValue);
            if (fieldVisitor != null) {
                resolve(Type.getType(descriptor));
                return new TypeReferenceFieldVisitor(fieldVisitor);
            }
            return IGNORE_FIELD;
        }

        @Override // net.bytebuddy.jar.asm.ClassVisitor
        public MethodVisitor visitMethod(int modifiers, String internalName, String descriptor, String signature, String[] exceptionInternalName) {
            MethodVisitor methodVisitor = super.visitMethod(modifiers, internalName, descriptor, signature, exceptionInternalName);
            if (methodVisitor != null) {
                resolve(Type.getType(descriptor));
                if (exceptionInternalName != null) {
                    this.observedTypes.addAll(Arrays.asList(exceptionInternalName));
                }
                return new TypeReferenceMethodVisitor(methodVisitor);
            }
            return IGNORE_METHOD;
        }

        @Override // net.bytebuddy.jar.asm.ClassVisitor
        public void visitEnd() {
            for (String observedType : this.observedTypes) {
                if (this.visitedInnerTypes.add(observedType)) {
                    TypePool.Resolution resolution = this.typePool.describe(observedType.replace('/', '.'));
                    if (resolution.isResolved()) {
                        TypeDescription typeDescription = resolution.resolve();
                        if (this.filter.matches(typeDescription)) {
                            continue;
                        } else {
                            while (typeDescription != null && typeDescription.isNestedClass()) {
                                super.visitInnerClass(typeDescription.getInternalName(), typeDescription.isMemberType() ? typeDescription.getDeclaringType().getInternalName() : null, typeDescription.isAnonymousType() ? null : typeDescription.getSimpleName(), typeDescription.getModifiers());
                                do {
                                    try {
                                        typeDescription = typeDescription.getEnclosingType();
                                        if (typeDescription != null) {
                                        }
                                    } catch (RuntimeException exception) {
                                        if (this.strict) {
                                            throw exception;
                                        }
                                    }
                                } while (!this.visitedInnerTypes.add(typeDescription.getInternalName()));
                            }
                        }
                    } else if (this.strict) {
                        throw new IllegalStateException("Could not locate type for: " + observedType.replace('/', '.'));
                    }
                }
            }
            super.visitEnd();
        }

        protected void resolve(Type type) {
            Type[] argumentTypes;
            if (type.getSort() == 11) {
                resolve(type.getReturnType());
                for (Type argumentType : type.getArgumentTypes()) {
                    resolve(argumentType);
                }
                return;
            }
            while (type.getSort() == 9) {
                type = type.getElementType();
            }
            if (type.getSort() == 10) {
                this.observedTypes.add(type.getInternalName());
            }
        }

        protected void resolve(Handle handle) {
            Type[] argumentTypes;
            this.observedTypes.add(handle.getOwner());
            Type methodType = Type.getType(handle.getDesc());
            resolve(methodType.getReturnType());
            for (Type type : methodType.getArgumentTypes()) {
                resolve(type);
            }
        }

        protected void resolve(ConstantDynamic constant) {
            Type[] argumentTypes;
            Type methodType = Type.getType(constant.getDescriptor());
            resolve(methodType.getReturnType());
            for (Type type : methodType.getArgumentTypes()) {
                resolve(type);
            }
            resolve(constant.getBootstrapMethod());
            for (int index = 0; index < constant.getBootstrapMethodArgumentCount(); index++) {
                resolve(constant.getBootstrapMethodArgument(index));
            }
        }

        protected void resolveInternalName(String internalName) {
            while (internalName.startsWith("[")) {
                internalName = internalName.substring(1);
            }
            this.observedTypes.add(internalName);
        }

        protected void resolve(Object value) {
            if (value instanceof Type) {
                resolve((Type) value);
            } else if (value instanceof Handle) {
                resolve((Handle) value);
            } else if (value instanceof ConstantDynamic) {
                resolve((ConstantDynamic) value);
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/TypeReferenceAdjustment$TypeReferenceClassVisitor$TypeReferenceAnnotationVisitor.class */
        protected class TypeReferenceAnnotationVisitor extends AnnotationVisitor {
            protected TypeReferenceAnnotationVisitor(AnnotationVisitor annotationVisitor) {
                super(OpenedClassReader.ASM_API, annotationVisitor);
            }

            @Override // net.bytebuddy.jar.asm.AnnotationVisitor
            public void visit(String name, Object value) {
                TypeReferenceClassVisitor.this.resolve(value);
                super.visit(name, value);
            }

            @Override // net.bytebuddy.jar.asm.AnnotationVisitor
            public void visitEnum(String name, String descriptor, String value) {
                TypeReferenceClassVisitor.this.observedTypes.add(Type.getType(descriptor).getInternalName());
                super.visitEnum(name, descriptor, value);
            }

            @Override // net.bytebuddy.jar.asm.AnnotationVisitor
            public AnnotationVisitor visitAnnotation(String name, String descriptor) {
                TypeReferenceClassVisitor.this.observedTypes.add(Type.getType(descriptor).getInternalName());
                AnnotationVisitor annotationVisitor = super.visitAnnotation(name, descriptor);
                if (annotationVisitor == null) {
                    return TypeReferenceClassVisitor.IGNORE_ANNOTATION;
                }
                return new TypeReferenceAnnotationVisitor(annotationVisitor);
            }

            @Override // net.bytebuddy.jar.asm.AnnotationVisitor
            public AnnotationVisitor visitArray(String name) {
                AnnotationVisitor annotationVisitor = super.visitArray(name);
                if (annotationVisitor == null) {
                    return TypeReferenceClassVisitor.IGNORE_ANNOTATION;
                }
                return new TypeReferenceAnnotationVisitor(annotationVisitor);
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/TypeReferenceAdjustment$TypeReferenceClassVisitor$TypeReferenceFieldVisitor.class */
        protected class TypeReferenceFieldVisitor extends FieldVisitor {
            protected TypeReferenceFieldVisitor(FieldVisitor fieldVisitor) {
                super(OpenedClassReader.ASM_API, fieldVisitor);
            }

            @Override // net.bytebuddy.jar.asm.FieldVisitor
            public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                TypeReferenceClassVisitor.this.observedTypes.add(Type.getType(descriptor).getInternalName());
                AnnotationVisitor annotationVisitor = super.visitAnnotation(descriptor, visible);
                if (annotationVisitor == null) {
                    return TypeReferenceClassVisitor.IGNORE_ANNOTATION;
                }
                return new TypeReferenceAnnotationVisitor(annotationVisitor);
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/TypeReferenceAdjustment$TypeReferenceClassVisitor$TypeReferenceMethodVisitor.class */
        protected class TypeReferenceMethodVisitor extends MethodVisitor {
            protected TypeReferenceMethodVisitor(MethodVisitor methodVisitor) {
                super(OpenedClassReader.ASM_API, methodVisitor);
            }

            @Override // net.bytebuddy.jar.asm.MethodVisitor
            public AnnotationVisitor visitAnnotationDefault() {
                AnnotationVisitor annotationVisitor = super.visitAnnotationDefault();
                if (annotationVisitor == null) {
                    return TypeReferenceClassVisitor.IGNORE_ANNOTATION;
                }
                return new TypeReferenceAnnotationVisitor(annotationVisitor);
            }

            @Override // net.bytebuddy.jar.asm.MethodVisitor
            public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                TypeReferenceClassVisitor.this.observedTypes.add(Type.getType(descriptor).getInternalName());
                AnnotationVisitor annotationVisitor = super.visitAnnotation(descriptor, visible);
                if (annotationVisitor == null) {
                    return TypeReferenceClassVisitor.IGNORE_ANNOTATION;
                }
                return new TypeReferenceAnnotationVisitor(annotationVisitor);
            }

            @Override // net.bytebuddy.jar.asm.MethodVisitor
            public AnnotationVisitor visitTypeAnnotation(int typeReference, TypePath typePath, String descriptor, boolean visible) {
                TypeReferenceClassVisitor.this.observedTypes.add(Type.getType(descriptor).getInternalName());
                AnnotationVisitor annotationVisitor = super.visitTypeAnnotation(typeReference, typePath, descriptor, visible);
                if (annotationVisitor == null) {
                    return TypeReferenceClassVisitor.IGNORE_ANNOTATION;
                }
                return new TypeReferenceAnnotationVisitor(annotationVisitor);
            }

            @Override // net.bytebuddy.jar.asm.MethodVisitor
            public AnnotationVisitor visitParameterAnnotation(int index, String descriptor, boolean visible) {
                TypeReferenceClassVisitor.this.observedTypes.add(Type.getType(descriptor).getInternalName());
                AnnotationVisitor annotationVisitor = super.visitParameterAnnotation(index, descriptor, visible);
                if (annotationVisitor == null) {
                    return TypeReferenceClassVisitor.IGNORE_ANNOTATION;
                }
                return new TypeReferenceAnnotationVisitor(annotationVisitor);
            }

            @Override // net.bytebuddy.jar.asm.MethodVisitor
            public AnnotationVisitor visitInsnAnnotation(int typeReference, TypePath typePath, String descriptor, boolean visible) {
                TypeReferenceClassVisitor.this.observedTypes.add(Type.getType(descriptor).getInternalName());
                AnnotationVisitor annotationVisitor = super.visitInsnAnnotation(typeReference, typePath, descriptor, visible);
                if (annotationVisitor == null) {
                    return TypeReferenceClassVisitor.IGNORE_ANNOTATION;
                }
                return new TypeReferenceAnnotationVisitor(annotationVisitor);
            }

            @Override // net.bytebuddy.jar.asm.MethodVisitor
            public AnnotationVisitor visitTryCatchAnnotation(int typeReference, TypePath typePath, String descriptor, boolean visible) {
                TypeReferenceClassVisitor.this.observedTypes.add(Type.getType(descriptor).getInternalName());
                AnnotationVisitor annotationVisitor = super.visitTryCatchAnnotation(typeReference, typePath, descriptor, visible);
                if (annotationVisitor == null) {
                    return TypeReferenceClassVisitor.IGNORE_ANNOTATION;
                }
                return new TypeReferenceAnnotationVisitor(annotationVisitor);
            }

            @Override // net.bytebuddy.jar.asm.MethodVisitor
            public AnnotationVisitor visitLocalVariableAnnotation(int typeReference, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
                TypeReferenceClassVisitor.this.observedTypes.add(Type.getType(descriptor).getInternalName());
                AnnotationVisitor annotationVisitor = super.visitLocalVariableAnnotation(typeReference, typePath, start, end, index, descriptor, visible);
                if (annotationVisitor == null) {
                    return TypeReferenceClassVisitor.IGNORE_ANNOTATION;
                }
                return new TypeReferenceAnnotationVisitor(annotationVisitor);
            }

            @Override // net.bytebuddy.jar.asm.MethodVisitor
            public void visitTypeInsn(int opcode, String internalName) {
                TypeReferenceClassVisitor.this.resolveInternalName(internalName);
                super.visitTypeInsn(opcode, internalName);
            }

            @Override // net.bytebuddy.jar.asm.MethodVisitor
            public void visitFieldInsn(int opcode, String ownerInternalName, String name, String descriptor) {
                TypeReferenceClassVisitor.this.resolveInternalName(ownerInternalName);
                TypeReferenceClassVisitor.this.resolve(Type.getType(descriptor));
                super.visitFieldInsn(opcode, ownerInternalName, name, descriptor);
            }

            @Override // net.bytebuddy.jar.asm.MethodVisitor
            public void visitMethodInsn(int opcode, String ownerInternalName, String name, String descriptor, boolean isInterface) {
                TypeReferenceClassVisitor.this.resolveInternalName(ownerInternalName);
                TypeReferenceClassVisitor.this.resolve(Type.getType(descriptor));
                super.visitMethodInsn(opcode, ownerInternalName, name, descriptor, isInterface);
            }

            @Override // net.bytebuddy.jar.asm.MethodVisitor
            public void visitInvokeDynamicInsn(String name, String descriptor, Handle handle, Object[] argument) {
                TypeReferenceClassVisitor.this.resolve(Type.getType(descriptor));
                TypeReferenceClassVisitor.this.resolve(handle);
                for (Object anArgument : argument) {
                    TypeReferenceClassVisitor.this.resolve(anArgument);
                }
                super.visitInvokeDynamicInsn(name, descriptor, handle, argument);
            }

            @Override // net.bytebuddy.jar.asm.MethodVisitor
            public void visitLdcInsn(Object value) {
                TypeReferenceClassVisitor.this.resolve(value);
                super.visitLdcInsn(value);
            }

            @Override // net.bytebuddy.jar.asm.MethodVisitor
            public void visitMultiANewArrayInsn(String descriptor, int dimension) {
                TypeReferenceClassVisitor.this.resolve(Type.getType(descriptor));
                super.visitMultiANewArrayInsn(descriptor, dimension);
            }

            @Override // net.bytebuddy.jar.asm.MethodVisitor
            public void visitTryCatchBlock(Label start, Label end, Label handler, String typeInternalName) {
                if (typeInternalName != null) {
                    TypeReferenceClassVisitor.this.observedTypes.add(typeInternalName);
                }
                super.visitTryCatchBlock(start, end, handler, typeInternalName);
            }
        }
    }
}
