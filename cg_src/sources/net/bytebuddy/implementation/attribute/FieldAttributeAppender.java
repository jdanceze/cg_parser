package net.bytebuddy.implementation.attribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.attribute.AnnotationAppender;
import net.bytebuddy.jar.asm.FieldVisitor;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/attribute/FieldAttributeAppender.class */
public interface FieldAttributeAppender {
    void apply(FieldVisitor fieldVisitor, FieldDescription fieldDescription, AnnotationValueFilter annotationValueFilter);

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/attribute/FieldAttributeAppender$NoOp.class */
    public enum NoOp implements FieldAttributeAppender, Factory {
        INSTANCE;

        @Override // net.bytebuddy.implementation.attribute.FieldAttributeAppender.Factory
        public FieldAttributeAppender make(TypeDescription typeDescription) {
            return this;
        }

        @Override // net.bytebuddy.implementation.attribute.FieldAttributeAppender
        public void apply(FieldVisitor fieldVisitor, FieldDescription fieldDescription, AnnotationValueFilter annotationValueFilter) {
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/attribute/FieldAttributeAppender$Factory.class */
    public interface Factory {
        FieldAttributeAppender make(TypeDescription typeDescription);

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/attribute/FieldAttributeAppender$Factory$Compound.class */
        public static class Compound implements Factory {
            private final List<Factory> factories;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.factories.equals(((Compound) obj).factories);
            }

            public int hashCode() {
                return (17 * 31) + this.factories.hashCode();
            }

            public Compound(Factory... factory) {
                this(Arrays.asList(factory));
            }

            public Compound(List<? extends Factory> factories) {
                this.factories = new ArrayList();
                for (Factory factory : factories) {
                    if (factory instanceof Compound) {
                        this.factories.addAll(((Compound) factory).factories);
                    } else if (!(factory instanceof NoOp)) {
                        this.factories.add(factory);
                    }
                }
            }

            @Override // net.bytebuddy.implementation.attribute.FieldAttributeAppender.Factory
            public FieldAttributeAppender make(TypeDescription typeDescription) {
                List<FieldAttributeAppender> fieldAttributeAppenders = new ArrayList<>(this.factories.size());
                for (Factory factory : this.factories) {
                    fieldAttributeAppenders.add(factory.make(typeDescription));
                }
                return new Compound(fieldAttributeAppenders);
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/attribute/FieldAttributeAppender$ForInstrumentedField.class */
    public enum ForInstrumentedField implements FieldAttributeAppender, Factory {
        INSTANCE;

        @Override // net.bytebuddy.implementation.attribute.FieldAttributeAppender
        public void apply(FieldVisitor fieldVisitor, FieldDescription fieldDescription, AnnotationValueFilter annotationValueFilter) {
            AnnotationAppender annotationAppender = new AnnotationAppender.Default(new AnnotationAppender.Target.OnField(fieldVisitor));
            AnnotationAppender annotationAppender2 = (AnnotationAppender) fieldDescription.getType().accept(AnnotationAppender.ForTypeAnnotations.ofFieldType(annotationAppender, annotationValueFilter));
            for (AnnotationDescription annotation : fieldDescription.getDeclaredAnnotations()) {
                annotationAppender2 = annotationAppender2.append(annotation, annotationValueFilter);
            }
        }

        @Override // net.bytebuddy.implementation.attribute.FieldAttributeAppender.Factory
        public FieldAttributeAppender make(TypeDescription typeDescription) {
            return this;
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/attribute/FieldAttributeAppender$Explicit.class */
    public static class Explicit implements FieldAttributeAppender, Factory {
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

        @Override // net.bytebuddy.implementation.attribute.FieldAttributeAppender
        public void apply(FieldVisitor fieldVisitor, FieldDescription fieldDescription, AnnotationValueFilter annotationValueFilter) {
            AnnotationAppender appender = new AnnotationAppender.Default(new AnnotationAppender.Target.OnField(fieldVisitor));
            for (AnnotationDescription annotation : this.annotations) {
                appender = appender.append(annotation, annotationValueFilter);
            }
        }

        @Override // net.bytebuddy.implementation.attribute.FieldAttributeAppender.Factory
        public FieldAttributeAppender make(TypeDescription typeDescription) {
            return this;
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/attribute/FieldAttributeAppender$Compound.class */
    public static class Compound implements FieldAttributeAppender {
        private final List<FieldAttributeAppender> fieldAttributeAppenders;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.fieldAttributeAppenders.equals(((Compound) obj).fieldAttributeAppenders);
        }

        public int hashCode() {
            return (17 * 31) + this.fieldAttributeAppenders.hashCode();
        }

        public Compound(FieldAttributeAppender... fieldAttributeAppender) {
            this(Arrays.asList(fieldAttributeAppender));
        }

        public Compound(List<? extends FieldAttributeAppender> fieldAttributeAppenders) {
            this.fieldAttributeAppenders = new ArrayList();
            for (FieldAttributeAppender fieldAttributeAppender : fieldAttributeAppenders) {
                if (fieldAttributeAppender instanceof Compound) {
                    this.fieldAttributeAppenders.addAll(((Compound) fieldAttributeAppender).fieldAttributeAppenders);
                } else if (!(fieldAttributeAppender instanceof NoOp)) {
                    this.fieldAttributeAppenders.add(fieldAttributeAppender);
                }
            }
        }

        @Override // net.bytebuddy.implementation.attribute.FieldAttributeAppender
        public void apply(FieldVisitor fieldVisitor, FieldDescription fieldDescription, AnnotationValueFilter annotationValueFilter) {
            for (FieldAttributeAppender fieldAttributeAppender : this.fieldAttributeAppenders) {
                fieldAttributeAppender.apply(fieldVisitor, fieldDescription, annotationValueFilter);
            }
        }
    }
}
