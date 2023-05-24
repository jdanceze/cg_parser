package net.bytebuddy.implementation.attribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.type.RecordComponentDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.attribute.AnnotationAppender;
import net.bytebuddy.jar.asm.RecordComponentVisitor;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/attribute/RecordComponentAttributeAppender.class */
public interface RecordComponentAttributeAppender {
    void apply(RecordComponentVisitor recordComponentVisitor, RecordComponentDescription recordComponentDescription, AnnotationValueFilter annotationValueFilter);

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/attribute/RecordComponentAttributeAppender$NoOp.class */
    public enum NoOp implements RecordComponentAttributeAppender, Factory {
        INSTANCE;

        @Override // net.bytebuddy.implementation.attribute.RecordComponentAttributeAppender.Factory
        public RecordComponentAttributeAppender make(TypeDescription typeDescription) {
            return this;
        }

        @Override // net.bytebuddy.implementation.attribute.RecordComponentAttributeAppender
        public void apply(RecordComponentVisitor recordComponentVisitor, RecordComponentDescription recordComponentDescription, AnnotationValueFilter annotationValueFilter) {
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/attribute/RecordComponentAttributeAppender$Factory.class */
    public interface Factory {
        RecordComponentAttributeAppender make(TypeDescription typeDescription);

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/attribute/RecordComponentAttributeAppender$Factory$Compound.class */
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

            @Override // net.bytebuddy.implementation.attribute.RecordComponentAttributeAppender.Factory
            public RecordComponentAttributeAppender make(TypeDescription typeDescription) {
                List<RecordComponentAttributeAppender> recordComponentAttributeAppenders = new ArrayList<>(this.factories.size());
                for (Factory factory : this.factories) {
                    recordComponentAttributeAppenders.add(factory.make(typeDescription));
                }
                return new Compound(recordComponentAttributeAppenders);
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/attribute/RecordComponentAttributeAppender$ForInstrumentedRecordComponent.class */
    public enum ForInstrumentedRecordComponent implements RecordComponentAttributeAppender, Factory {
        INSTANCE;

        @Override // net.bytebuddy.implementation.attribute.RecordComponentAttributeAppender
        public void apply(RecordComponentVisitor recordComponentVisitor, RecordComponentDescription recordComponentDescription, AnnotationValueFilter annotationValueFilter) {
            AnnotationAppender annotationAppender = new AnnotationAppender.Default(new AnnotationAppender.Target.OnRecordComponent(recordComponentVisitor));
            AnnotationAppender annotationAppender2 = (AnnotationAppender) recordComponentDescription.getType().accept(AnnotationAppender.ForTypeAnnotations.ofFieldType(annotationAppender, annotationValueFilter));
            for (AnnotationDescription annotation : recordComponentDescription.getDeclaredAnnotations()) {
                annotationAppender2 = annotationAppender2.append(annotation, annotationValueFilter);
            }
        }

        @Override // net.bytebuddy.implementation.attribute.RecordComponentAttributeAppender.Factory
        public RecordComponentAttributeAppender make(TypeDescription typeDescription) {
            return this;
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/attribute/RecordComponentAttributeAppender$Explicit.class */
    public static class Explicit implements RecordComponentAttributeAppender, Factory {
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

        @Override // net.bytebuddy.implementation.attribute.RecordComponentAttributeAppender
        public void apply(RecordComponentVisitor recordComponentVisitor, RecordComponentDescription recordComponentDescription, AnnotationValueFilter annotationValueFilter) {
            AnnotationAppender appender = new AnnotationAppender.Default(new AnnotationAppender.Target.OnRecordComponent(recordComponentVisitor));
            for (AnnotationDescription annotation : this.annotations) {
                appender = appender.append(annotation, annotationValueFilter);
            }
        }

        @Override // net.bytebuddy.implementation.attribute.RecordComponentAttributeAppender.Factory
        public RecordComponentAttributeAppender make(TypeDescription typeDescription) {
            return this;
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/attribute/RecordComponentAttributeAppender$Compound.class */
    public static class Compound implements RecordComponentAttributeAppender {
        private final List<RecordComponentAttributeAppender> recordComponentAttributeAppenders;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.recordComponentAttributeAppenders.equals(((Compound) obj).recordComponentAttributeAppenders);
        }

        public int hashCode() {
            return (17 * 31) + this.recordComponentAttributeAppenders.hashCode();
        }

        public Compound(RecordComponentAttributeAppender... recordComponentAttributeAppender) {
            this(Arrays.asList(recordComponentAttributeAppender));
        }

        public Compound(List<? extends RecordComponentAttributeAppender> recordComponentAttributeAppenders) {
            this.recordComponentAttributeAppenders = new ArrayList();
            for (RecordComponentAttributeAppender recordComponentAttributeAppender : recordComponentAttributeAppenders) {
                if (recordComponentAttributeAppender instanceof Compound) {
                    this.recordComponentAttributeAppenders.addAll(((Compound) recordComponentAttributeAppender).recordComponentAttributeAppenders);
                } else if (!(recordComponentAttributeAppender instanceof NoOp)) {
                    this.recordComponentAttributeAppenders.add(recordComponentAttributeAppender);
                }
            }
        }

        @Override // net.bytebuddy.implementation.attribute.RecordComponentAttributeAppender
        public void apply(RecordComponentVisitor recordComponentVisitor, RecordComponentDescription recordComponentDescription, AnnotationValueFilter annotationValueFilter) {
            for (RecordComponentAttributeAppender recordComponentAttributeAppender : this.recordComponentAttributeAppenders) {
                recordComponentAttributeAppender.apply(recordComponentVisitor, recordComponentDescription, annotationValueFilter);
            }
        }
    }
}
