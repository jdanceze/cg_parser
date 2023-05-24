package net.bytebuddy.implementation.attribute;

import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Array;
import java.util.List;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.enumeration.EnumerationDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.description.type.TypeList;
import net.bytebuddy.implementation.attribute.AnnotationValueFilter;
import net.bytebuddy.jar.asm.AnnotationVisitor;
import net.bytebuddy.jar.asm.ClassVisitor;
import net.bytebuddy.jar.asm.FieldVisitor;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.jar.asm.RecordComponentVisitor;
import net.bytebuddy.jar.asm.Type;
import net.bytebuddy.jar.asm.TypePath;
import net.bytebuddy.jar.asm.TypeReference;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/attribute/AnnotationAppender.class */
public interface AnnotationAppender {
    public static final String NO_NAME = null;

    AnnotationAppender append(AnnotationDescription annotationDescription, AnnotationValueFilter annotationValueFilter);

    AnnotationAppender append(AnnotationDescription annotationDescription, AnnotationValueFilter annotationValueFilter, int i, String str);

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/attribute/AnnotationAppender$Target.class */
    public interface Target {
        AnnotationVisitor visit(String str, boolean z);

        AnnotationVisitor visit(String str, boolean z, int i, String str2);

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/attribute/AnnotationAppender$Target$OnType.class */
        public static class OnType implements Target {
            private final ClassVisitor classVisitor;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.classVisitor.equals(((OnType) obj).classVisitor);
            }

            public int hashCode() {
                return (17 * 31) + this.classVisitor.hashCode();
            }

            public OnType(ClassVisitor classVisitor) {
                this.classVisitor = classVisitor;
            }

            @Override // net.bytebuddy.implementation.attribute.AnnotationAppender.Target
            public AnnotationVisitor visit(String annotationTypeDescriptor, boolean visible) {
                return this.classVisitor.visitAnnotation(annotationTypeDescriptor, visible);
            }

            @Override // net.bytebuddy.implementation.attribute.AnnotationAppender.Target
            public AnnotationVisitor visit(String annotationTypeDescriptor, boolean visible, int typeReference, String typePath) {
                return this.classVisitor.visitTypeAnnotation(typeReference, TypePath.fromString(typePath), annotationTypeDescriptor, visible);
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/attribute/AnnotationAppender$Target$OnField.class */
        public static class OnField implements Target {
            private final FieldVisitor fieldVisitor;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.fieldVisitor.equals(((OnField) obj).fieldVisitor);
            }

            public int hashCode() {
                return (17 * 31) + this.fieldVisitor.hashCode();
            }

            public OnField(FieldVisitor fieldVisitor) {
                this.fieldVisitor = fieldVisitor;
            }

            @Override // net.bytebuddy.implementation.attribute.AnnotationAppender.Target
            public AnnotationVisitor visit(String annotationTypeDescriptor, boolean visible) {
                return this.fieldVisitor.visitAnnotation(annotationTypeDescriptor, visible);
            }

            @Override // net.bytebuddy.implementation.attribute.AnnotationAppender.Target
            public AnnotationVisitor visit(String annotationTypeDescriptor, boolean visible, int typeReference, String typePath) {
                return this.fieldVisitor.visitTypeAnnotation(typeReference, TypePath.fromString(typePath), annotationTypeDescriptor, visible);
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/attribute/AnnotationAppender$Target$OnMethod.class */
        public static class OnMethod implements Target {
            private final MethodVisitor methodVisitor;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.methodVisitor.equals(((OnMethod) obj).methodVisitor);
            }

            public int hashCode() {
                return (17 * 31) + this.methodVisitor.hashCode();
            }

            public OnMethod(MethodVisitor methodVisitor) {
                this.methodVisitor = methodVisitor;
            }

            @Override // net.bytebuddy.implementation.attribute.AnnotationAppender.Target
            public AnnotationVisitor visit(String annotationTypeDescriptor, boolean visible) {
                return this.methodVisitor.visitAnnotation(annotationTypeDescriptor, visible);
            }

            @Override // net.bytebuddy.implementation.attribute.AnnotationAppender.Target
            public AnnotationVisitor visit(String annotationTypeDescriptor, boolean visible, int typeReference, String typePath) {
                return this.methodVisitor.visitTypeAnnotation(typeReference, TypePath.fromString(typePath), annotationTypeDescriptor, visible);
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/attribute/AnnotationAppender$Target$OnMethodParameter.class */
        public static class OnMethodParameter implements Target {
            private final MethodVisitor methodVisitor;
            private final int parameterIndex;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.parameterIndex == ((OnMethodParameter) obj).parameterIndex && this.methodVisitor.equals(((OnMethodParameter) obj).methodVisitor);
            }

            public int hashCode() {
                return (((17 * 31) + this.methodVisitor.hashCode()) * 31) + this.parameterIndex;
            }

            public OnMethodParameter(MethodVisitor methodVisitor, int parameterIndex) {
                this.methodVisitor = methodVisitor;
                this.parameterIndex = parameterIndex;
            }

            @Override // net.bytebuddy.implementation.attribute.AnnotationAppender.Target
            public AnnotationVisitor visit(String annotationTypeDescriptor, boolean visible) {
                return this.methodVisitor.visitParameterAnnotation(this.parameterIndex, annotationTypeDescriptor, visible);
            }

            @Override // net.bytebuddy.implementation.attribute.AnnotationAppender.Target
            public AnnotationVisitor visit(String annotationTypeDescriptor, boolean visible, int typeReference, String typePath) {
                return this.methodVisitor.visitTypeAnnotation(typeReference, TypePath.fromString(typePath), annotationTypeDescriptor, visible);
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/attribute/AnnotationAppender$Target$OnRecordComponent.class */
        public static class OnRecordComponent implements Target {
            private final RecordComponentVisitor recordComponentVisitor;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.recordComponentVisitor.equals(((OnRecordComponent) obj).recordComponentVisitor);
            }

            public int hashCode() {
                return (17 * 31) + this.recordComponentVisitor.hashCode();
            }

            public OnRecordComponent(RecordComponentVisitor recordComponentVisitor) {
                this.recordComponentVisitor = recordComponentVisitor;
            }

            @Override // net.bytebuddy.implementation.attribute.AnnotationAppender.Target
            public AnnotationVisitor visit(String annotationTypeDescriptor, boolean visible) {
                return this.recordComponentVisitor.visitAnnotation(annotationTypeDescriptor, visible);
            }

            @Override // net.bytebuddy.implementation.attribute.AnnotationAppender.Target
            public AnnotationVisitor visit(String annotationTypeDescriptor, boolean visible, int typeReference, String typePath) {
                return this.recordComponentVisitor.visitTypeAnnotation(typeReference, TypePath.fromString(typePath), annotationTypeDescriptor, visible);
            }
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/attribute/AnnotationAppender$Default.class */
    public static class Default implements AnnotationAppender {
        private final Target target;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.target.equals(((Default) obj).target);
        }

        public int hashCode() {
            return (17 * 31) + this.target.hashCode();
        }

        public Default(Target target) {
            this.target = target;
        }

        private static void handle(AnnotationVisitor annotationVisitor, AnnotationDescription annotation, AnnotationValueFilter annotationValueFilter) {
            for (MethodDescription.InDefinedShape methodDescription : annotation.getAnnotationType().getDeclaredMethods()) {
                if (annotationValueFilter.isRelevant(annotation, methodDescription)) {
                    apply(annotationVisitor, methodDescription.getReturnType().asErasure(), methodDescription.getName(), annotation.getValue(methodDescription).resolve());
                }
            }
            annotationVisitor.visitEnd();
        }

        public static void apply(AnnotationVisitor annotationVisitor, TypeDescription valueType, String name, Object value) {
            if (valueType.isArray()) {
                AnnotationVisitor arrayVisitor = annotationVisitor.visitArray(name);
                int length = Array.getLength(value);
                TypeDescription componentType = valueType.getComponentType();
                for (int index = 0; index < length; index++) {
                    apply(arrayVisitor, componentType, NO_NAME, Array.get(value, index));
                }
                arrayVisitor.visitEnd();
            } else if (valueType.isAnnotation()) {
                handle(annotationVisitor.visitAnnotation(name, valueType.getDescriptor()), (AnnotationDescription) value, AnnotationValueFilter.Default.APPEND_DEFAULTS);
            } else if (valueType.isEnum()) {
                annotationVisitor.visitEnum(name, valueType.getDescriptor(), ((EnumerationDescription) value).getValue());
            } else if (valueType.represents(Class.class)) {
                annotationVisitor.visit(name, Type.getType(((TypeDescription) value).getDescriptor()));
            } else {
                annotationVisitor.visit(name, value);
            }
        }

        @Override // net.bytebuddy.implementation.attribute.AnnotationAppender
        public AnnotationAppender append(AnnotationDescription annotationDescription, AnnotationValueFilter annotationValueFilter) {
            switch (AnonymousClass1.$SwitchMap$java$lang$annotation$RetentionPolicy[annotationDescription.getRetention().ordinal()]) {
                case 1:
                    doAppend(annotationDescription, true, annotationValueFilter);
                    break;
                case 2:
                    doAppend(annotationDescription, false, annotationValueFilter);
                    break;
                case 3:
                    break;
                default:
                    throw new IllegalStateException("Unexpected retention policy: " + annotationDescription.getRetention());
            }
            return this;
        }

        private void doAppend(AnnotationDescription annotation, boolean visible, AnnotationValueFilter annotationValueFilter) {
            AnnotationVisitor annotationVisitor = this.target.visit(annotation.getAnnotationType().getDescriptor(), visible);
            if (annotationVisitor != null) {
                handle(annotationVisitor, annotation, annotationValueFilter);
            }
        }

        @Override // net.bytebuddy.implementation.attribute.AnnotationAppender
        public AnnotationAppender append(AnnotationDescription annotationDescription, AnnotationValueFilter annotationValueFilter, int typeReference, String typePath) {
            switch (AnonymousClass1.$SwitchMap$java$lang$annotation$RetentionPolicy[annotationDescription.getRetention().ordinal()]) {
                case 1:
                    doAppend(annotationDescription, true, annotationValueFilter, typeReference, typePath);
                    break;
                case 2:
                    doAppend(annotationDescription, false, annotationValueFilter, typeReference, typePath);
                    break;
                case 3:
                    break;
                default:
                    throw new IllegalStateException("Unexpected retention policy: " + annotationDescription.getRetention());
            }
            return this;
        }

        private void doAppend(AnnotationDescription annotation, boolean visible, AnnotationValueFilter annotationValueFilter, int typeReference, String typePath) {
            AnnotationVisitor annotationVisitor = this.target.visit(annotation.getAnnotationType().getDescriptor(), visible, typeReference, typePath);
            if (annotationVisitor != null) {
                handle(annotationVisitor, annotation, annotationValueFilter);
            }
        }
    }

    /* renamed from: net.bytebuddy.implementation.attribute.AnnotationAppender$1  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/attribute/AnnotationAppender$1.class */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$java$lang$annotation$RetentionPolicy = new int[RetentionPolicy.values().length];

        static {
            try {
                $SwitchMap$java$lang$annotation$RetentionPolicy[RetentionPolicy.RUNTIME.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$java$lang$annotation$RetentionPolicy[RetentionPolicy.CLASS.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$java$lang$annotation$RetentionPolicy[RetentionPolicy.SOURCE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/attribute/AnnotationAppender$ForTypeAnnotations.class */
    public static class ForTypeAnnotations implements TypeDescription.Generic.Visitor<AnnotationAppender> {
        public static final boolean VARIABLE_ON_TYPE = true;
        public static final boolean VARIABLE_ON_INVOKEABLE = false;
        private static final String EMPTY_TYPE_PATH = "";
        private static final char COMPONENT_TYPE_PATH = '[';
        private static final char WILDCARD_TYPE_PATH = '*';
        private static final char INNER_CLASS_PATH = '.';
        private static final char INDEXED_TYPE_DELIMITER = ';';
        private static final int SUPER_CLASS_INDEX = -1;
        private final AnnotationAppender annotationAppender;
        private final AnnotationValueFilter annotationValueFilter;
        private final int typeReference;
        private final String typePath;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.typeReference == ((ForTypeAnnotations) obj).typeReference && this.typePath.equals(((ForTypeAnnotations) obj).typePath) && this.annotationAppender.equals(((ForTypeAnnotations) obj).annotationAppender) && this.annotationValueFilter.equals(((ForTypeAnnotations) obj).annotationValueFilter);
        }

        public int hashCode() {
            return (((((((17 * 31) + this.annotationAppender.hashCode()) * 31) + this.annotationValueFilter.hashCode()) * 31) + this.typeReference) * 31) + this.typePath.hashCode();
        }

        protected ForTypeAnnotations(AnnotationAppender annotationAppender, AnnotationValueFilter annotationValueFilter, TypeReference typeReference) {
            this(annotationAppender, annotationValueFilter, typeReference.getValue(), "");
        }

        protected ForTypeAnnotations(AnnotationAppender annotationAppender, AnnotationValueFilter annotationValueFilter, int typeReference, String typePath) {
            this.annotationAppender = annotationAppender;
            this.annotationValueFilter = annotationValueFilter;
            this.typeReference = typeReference;
            this.typePath = typePath;
        }

        public static TypeDescription.Generic.Visitor<AnnotationAppender> ofSuperClass(AnnotationAppender annotationAppender, AnnotationValueFilter annotationValueFilter) {
            return new ForTypeAnnotations(annotationAppender, annotationValueFilter, TypeReference.newSuperTypeReference(-1));
        }

        public static TypeDescription.Generic.Visitor<AnnotationAppender> ofInterfaceType(AnnotationAppender annotationAppender, AnnotationValueFilter annotationValueFilter, int index) {
            return new ForTypeAnnotations(annotationAppender, annotationValueFilter, TypeReference.newSuperTypeReference(index));
        }

        public static TypeDescription.Generic.Visitor<AnnotationAppender> ofFieldType(AnnotationAppender annotationAppender, AnnotationValueFilter annotationValueFilter) {
            return new ForTypeAnnotations(annotationAppender, annotationValueFilter, TypeReference.newTypeReference(19));
        }

        public static TypeDescription.Generic.Visitor<AnnotationAppender> ofMethodReturnType(AnnotationAppender annotationAppender, AnnotationValueFilter annotationValueFilter) {
            return new ForTypeAnnotations(annotationAppender, annotationValueFilter, TypeReference.newTypeReference(20));
        }

        public static TypeDescription.Generic.Visitor<AnnotationAppender> ofMethodParameterType(AnnotationAppender annotationAppender, AnnotationValueFilter annotationValueFilter, int index) {
            return new ForTypeAnnotations(annotationAppender, annotationValueFilter, TypeReference.newFormalParameterReference(index));
        }

        public static TypeDescription.Generic.Visitor<AnnotationAppender> ofExceptionType(AnnotationAppender annotationAppender, AnnotationValueFilter annotationValueFilter, int index) {
            return new ForTypeAnnotations(annotationAppender, annotationValueFilter, TypeReference.newExceptionReference(index));
        }

        public static TypeDescription.Generic.Visitor<AnnotationAppender> ofReceiverType(AnnotationAppender annotationAppender, AnnotationValueFilter annotationValueFilter) {
            return new ForTypeAnnotations(annotationAppender, annotationValueFilter, TypeReference.newTypeReference(21));
        }

        public static AnnotationAppender ofTypeVariable(AnnotationAppender annotationAppender, AnnotationValueFilter annotationValueFilter, boolean variableOnType, List<? extends TypeDescription.Generic> typeVariables) {
            return ofTypeVariable(annotationAppender, annotationValueFilter, variableOnType, 0, typeVariables);
        }

        public static AnnotationAppender ofTypeVariable(AnnotationAppender annotationAppender, AnnotationValueFilter annotationValueFilter, boolean variableOnType, int subListIndex, List<? extends TypeDescription.Generic> typeVariables) {
            int variableBaseReference;
            int variableBoundBaseBase;
            int typeVariableIndex = subListIndex;
            if (variableOnType) {
                variableBaseReference = 0;
                variableBoundBaseBase = 17;
            } else {
                variableBaseReference = 1;
                variableBoundBaseBase = 18;
            }
            for (TypeDescription.Generic typeVariable : typeVariables.subList(subListIndex, typeVariables.size())) {
                int typeReference = TypeReference.newTypeParameterReference(variableBaseReference, typeVariableIndex).getValue();
                for (AnnotationDescription annotationDescription : typeVariable.getDeclaredAnnotations()) {
                    annotationAppender = annotationAppender.append(annotationDescription, annotationValueFilter, typeReference, "");
                }
                int boundIndex = (((TypeDescription.Generic) typeVariable.getUpperBounds().get(0)).getSort().isTypeVariable() || !((TypeDescription.Generic) typeVariable.getUpperBounds().get(0)).isInterface()) ? 0 : 1;
                for (TypeDescription.Generic typeBound : typeVariable.getUpperBounds()) {
                    int i = boundIndex;
                    boundIndex++;
                    annotationAppender = (AnnotationAppender) typeBound.accept(new ForTypeAnnotations(annotationAppender, annotationValueFilter, TypeReference.newTypeParameterBoundReference(variableBoundBaseBase, typeVariableIndex, i)));
                }
                typeVariableIndex++;
            }
            return annotationAppender;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
        public AnnotationAppender onGenericArray(TypeDescription.Generic genericArray) {
            return (AnnotationAppender) genericArray.getComponentType().accept(new ForTypeAnnotations(apply(genericArray, this.typePath), this.annotationValueFilter, this.typeReference, this.typePath + '['));
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
        public AnnotationAppender onWildcard(TypeDescription.Generic wildcard) {
            TypeDescription.Generic only;
            TypeList.Generic lowerBounds = wildcard.getLowerBounds();
            if (lowerBounds.isEmpty()) {
                only = wildcard.getUpperBounds().getOnly();
            } else {
                only = lowerBounds.getOnly();
            }
            return (AnnotationAppender) only.accept(new ForTypeAnnotations(apply(wildcard, this.typePath), this.annotationValueFilter, this.typeReference, this.typePath + '*'));
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
        public AnnotationAppender onParameterizedType(TypeDescription.Generic parameterizedType) {
            StringBuilder typePath = new StringBuilder(this.typePath);
            for (int index = 0; index < parameterizedType.asErasure().getInnerClassCount(); index++) {
                typePath = typePath.append('.');
            }
            AnnotationAppender annotationAppender = apply(parameterizedType, typePath.toString());
            TypeDescription.Generic ownerType = parameterizedType.getOwnerType();
            if (ownerType != null) {
                annotationAppender = (AnnotationAppender) ownerType.accept(new ForTypeAnnotations(annotationAppender, this.annotationValueFilter, this.typeReference, this.typePath));
            }
            int index2 = 0;
            for (TypeDescription.Generic typeArgument : parameterizedType.getTypeArguments()) {
                int i = index2;
                index2++;
                annotationAppender = (AnnotationAppender) typeArgument.accept(new ForTypeAnnotations(annotationAppender, this.annotationValueFilter, this.typeReference, typePath.toString() + i + ';'));
            }
            return annotationAppender;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
        public AnnotationAppender onTypeVariable(TypeDescription.Generic typeVariable) {
            return apply(typeVariable, this.typePath);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
        public AnnotationAppender onNonGenericType(TypeDescription.Generic typeDescription) {
            StringBuilder typePath = new StringBuilder(this.typePath);
            for (int index = 0; index < typeDescription.asErasure().getInnerClassCount(); index++) {
                typePath = typePath.append('.');
            }
            AnnotationAppender annotationAppender = apply(typeDescription, typePath.toString());
            if (typeDescription.isArray()) {
                annotationAppender = (AnnotationAppender) typeDescription.getComponentType().accept(new ForTypeAnnotations(annotationAppender, this.annotationValueFilter, this.typeReference, this.typePath + '['));
            }
            return annotationAppender;
        }

        private AnnotationAppender apply(TypeDescription.Generic typeDescription, String typePath) {
            AnnotationAppender annotationAppender = this.annotationAppender;
            for (AnnotationDescription annotationDescription : typeDescription.getDeclaredAnnotations()) {
                annotationAppender = annotationAppender.append(annotationDescription, this.annotationValueFilter, this.typeReference, typePath);
            }
            return annotationAppender;
        }
    }
}
