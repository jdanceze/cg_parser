package net.bytebuddy.implementation.attribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.annotation.AnnotationList;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.description.type.TypeList;
import net.bytebuddy.implementation.attribute.AnnotationAppender;
import net.bytebuddy.jar.asm.ClassVisitor;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/attribute/TypeAttributeAppender.class */
public interface TypeAttributeAppender {
    void apply(ClassVisitor classVisitor, TypeDescription typeDescription, AnnotationValueFilter annotationValueFilter);

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/attribute/TypeAttributeAppender$NoOp.class */
    public enum NoOp implements TypeAttributeAppender {
        INSTANCE;

        @Override // net.bytebuddy.implementation.attribute.TypeAttributeAppender
        public void apply(ClassVisitor classVisitor, TypeDescription instrumentedType, AnnotationValueFilter annotationValueFilter) {
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/attribute/TypeAttributeAppender$ForInstrumentedType.class */
    public enum ForInstrumentedType implements TypeAttributeAppender {
        INSTANCE;

        @Override // net.bytebuddy.implementation.attribute.TypeAttributeAppender
        public void apply(ClassVisitor classVisitor, TypeDescription instrumentedType, AnnotationValueFilter annotationValueFilter) {
            AnnotationAppender annotationAppender = AnnotationAppender.ForTypeAnnotations.ofTypeVariable(new AnnotationAppender.Default(new AnnotationAppender.Target.OnType(classVisitor)), annotationValueFilter, true, instrumentedType.getTypeVariables());
            TypeDescription.Generic superClass = instrumentedType.getSuperClass();
            if (superClass != null) {
                annotationAppender = (AnnotationAppender) superClass.accept(AnnotationAppender.ForTypeAnnotations.ofSuperClass(annotationAppender, annotationValueFilter));
            }
            int interfaceIndex = 0;
            for (TypeDescription.Generic interfaceType : instrumentedType.getInterfaces()) {
                int i = interfaceIndex;
                interfaceIndex++;
                annotationAppender = (AnnotationAppender) interfaceType.accept(AnnotationAppender.ForTypeAnnotations.ofInterfaceType(annotationAppender, annotationValueFilter, i));
            }
            for (AnnotationDescription annotation : instrumentedType.getDeclaredAnnotations()) {
                annotationAppender = annotationAppender.append(annotation, annotationValueFilter);
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/attribute/TypeAttributeAppender$ForInstrumentedType$Differentiating.class */
        public static class Differentiating implements TypeAttributeAppender {
            private final int annotationIndex;
            private final int typeVariableIndex;
            private final int interfaceTypeIndex;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.annotationIndex == ((Differentiating) obj).annotationIndex && this.typeVariableIndex == ((Differentiating) obj).typeVariableIndex && this.interfaceTypeIndex == ((Differentiating) obj).interfaceTypeIndex;
            }

            public int hashCode() {
                return (((((17 * 31) + this.annotationIndex) * 31) + this.typeVariableIndex) * 31) + this.interfaceTypeIndex;
            }

            public Differentiating(TypeDescription typeDescription) {
                this(typeDescription.getDeclaredAnnotations().size(), typeDescription.getTypeVariables().size(), typeDescription.getInterfaces().size());
            }

            protected Differentiating(int annotationIndex, int typeVariableIndex, int interfaceTypeIndex) {
                this.annotationIndex = annotationIndex;
                this.typeVariableIndex = typeVariableIndex;
                this.interfaceTypeIndex = interfaceTypeIndex;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // net.bytebuddy.implementation.attribute.TypeAttributeAppender
            public void apply(ClassVisitor classVisitor, TypeDescription instrumentedType, AnnotationValueFilter annotationValueFilter) {
                AnnotationAppender annotationAppender = new AnnotationAppender.Default(new AnnotationAppender.Target.OnType(classVisitor));
                AnnotationAppender.ForTypeAnnotations.ofTypeVariable(annotationAppender, annotationValueFilter, true, this.typeVariableIndex, instrumentedType.getTypeVariables());
                TypeList.Generic interfaceTypes = instrumentedType.getInterfaces();
                int interfaceTypeIndex = this.interfaceTypeIndex;
                for (TypeDescription.Generic interfaceType : interfaceTypes.subList(this.interfaceTypeIndex, interfaceTypes.size())) {
                    int i = interfaceTypeIndex;
                    interfaceTypeIndex++;
                    annotationAppender = (AnnotationAppender) interfaceType.accept(AnnotationAppender.ForTypeAnnotations.ofInterfaceType(annotationAppender, annotationValueFilter, i));
                }
                AnnotationList declaredAnnotations = instrumentedType.getDeclaredAnnotations();
                for (AnnotationDescription annotationDescription : declaredAnnotations.subList(this.annotationIndex, declaredAnnotations.size())) {
                    annotationAppender = annotationAppender.append(annotationDescription, annotationValueFilter);
                }
            }
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/attribute/TypeAttributeAppender$Explicit.class */
    public static class Explicit implements TypeAttributeAppender {
        private final List<? extends AnnotationDescription> annotations;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.annotations.equals(((Explicit) obj).annotations);
        }

        public int hashCode() {
            return (17 * 31) + this.annotations.hashCode();
        }

        public Explicit(List<? extends AnnotationDescription> annotations) {
            this.annotations = annotations;
        }

        @Override // net.bytebuddy.implementation.attribute.TypeAttributeAppender
        public void apply(ClassVisitor classVisitor, TypeDescription instrumentedType, AnnotationValueFilter annotationValueFilter) {
            AnnotationAppender appender = new AnnotationAppender.Default(new AnnotationAppender.Target.OnType(classVisitor));
            for (AnnotationDescription annotation : this.annotations) {
                appender = appender.append(annotation, annotationValueFilter);
            }
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/attribute/TypeAttributeAppender$Compound.class */
    public static class Compound implements TypeAttributeAppender {
        private final List<TypeAttributeAppender> typeAttributeAppenders;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.typeAttributeAppenders.equals(((Compound) obj).typeAttributeAppenders);
        }

        public int hashCode() {
            return (17 * 31) + this.typeAttributeAppenders.hashCode();
        }

        public Compound(TypeAttributeAppender... typeAttributeAppender) {
            this(Arrays.asList(typeAttributeAppender));
        }

        public Compound(List<? extends TypeAttributeAppender> typeAttributeAppenders) {
            this.typeAttributeAppenders = new ArrayList();
            for (TypeAttributeAppender typeAttributeAppender : typeAttributeAppenders) {
                if (typeAttributeAppender instanceof Compound) {
                    this.typeAttributeAppenders.addAll(((Compound) typeAttributeAppender).typeAttributeAppenders);
                } else if (!(typeAttributeAppender instanceof NoOp)) {
                    this.typeAttributeAppenders.add(typeAttributeAppender);
                }
            }
        }

        @Override // net.bytebuddy.implementation.attribute.TypeAttributeAppender
        public void apply(ClassVisitor classVisitor, TypeDescription instrumentedType, AnnotationValueFilter annotationValueFilter) {
            for (TypeAttributeAppender typeAttributeAppender : this.typeAttributeAppenders) {
                typeAttributeAppender.apply(classVisitor, instrumentedType, annotationValueFilter);
            }
        }
    }
}
