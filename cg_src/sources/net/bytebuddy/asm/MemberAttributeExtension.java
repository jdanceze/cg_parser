package net.bytebuddy.asm;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.annotation.AnnotationList;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.attribute.AnnotationValueFilter;
import net.bytebuddy.implementation.attribute.FieldAttributeAppender;
import net.bytebuddy.implementation.attribute.MethodAttributeAppender;
import net.bytebuddy.jar.asm.FieldVisitor;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.pool.TypePool;
import net.bytebuddy.utility.OpenedClassReader;
@HashCodeAndEqualsPlugin.Enhance
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberAttributeExtension.class */
public abstract class MemberAttributeExtension<T> {
    protected final AnnotationValueFilter.Factory annotationValueFilterFactory;
    protected final T attributeAppenderFactory;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.annotationValueFilterFactory.equals(((MemberAttributeExtension) obj).annotationValueFilterFactory) && this.attributeAppenderFactory.equals(((MemberAttributeExtension) obj).attributeAppenderFactory);
    }

    public int hashCode() {
        return (((17 * 31) + this.annotationValueFilterFactory.hashCode()) * 31) + this.attributeAppenderFactory.hashCode();
    }

    protected MemberAttributeExtension(AnnotationValueFilter.Factory annotationValueFilterFactory, T attributeAppenderFactory) {
        this.annotationValueFilterFactory = annotationValueFilterFactory;
        this.attributeAppenderFactory = attributeAppenderFactory;
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberAttributeExtension$ForField.class */
    public static class ForField extends MemberAttributeExtension<FieldAttributeAppender.Factory> implements AsmVisitorWrapper.ForDeclaredFields.FieldVisitorWrapper {
        public ForField() {
            this(AnnotationValueFilter.Default.APPEND_DEFAULTS);
        }

        public ForField(AnnotationValueFilter.Factory annotationValueFilterFactory) {
            this(annotationValueFilterFactory, FieldAttributeAppender.NoOp.INSTANCE);
        }

        protected ForField(AnnotationValueFilter.Factory annotationValueFilterFactory, FieldAttributeAppender.Factory attributeAppenderFactory) {
            super(annotationValueFilterFactory, attributeAppenderFactory);
        }

        public ForField annotate(Annotation... annotation) {
            return annotate(Arrays.asList(annotation));
        }

        public ForField annotate(List<? extends Annotation> annotations) {
            return annotate((Collection<? extends AnnotationDescription>) new AnnotationList.ForLoadedAnnotations(annotations));
        }

        public ForField annotate(AnnotationDescription... annotation) {
            return annotate((Collection<? extends AnnotationDescription>) Arrays.asList(annotation));
        }

        public ForField annotate(Collection<? extends AnnotationDescription> annotations) {
            return attribute(new FieldAttributeAppender.Explicit(new ArrayList(annotations)));
        }

        public ForField attribute(FieldAttributeAppender.Factory attributeAppenderFactory) {
            return new ForField(this.annotationValueFilterFactory, new FieldAttributeAppender.Factory.Compound((FieldAttributeAppender.Factory) this.attributeAppenderFactory, attributeAppenderFactory));
        }

        @Override // net.bytebuddy.asm.AsmVisitorWrapper.ForDeclaredFields.FieldVisitorWrapper
        public FieldVisitor wrap(TypeDescription instrumentedType, FieldDescription.InDefinedShape fieldDescription, FieldVisitor fieldVisitor) {
            return new FieldAttributeVisitor(fieldVisitor, fieldDescription, ((FieldAttributeAppender.Factory) this.attributeAppenderFactory).make(instrumentedType), this.annotationValueFilterFactory.on(fieldDescription));
        }

        public AsmVisitorWrapper on(ElementMatcher<? super FieldDescription.InDefinedShape> matcher) {
            return new AsmVisitorWrapper.ForDeclaredFields().field(matcher, this);
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberAttributeExtension$ForField$FieldAttributeVisitor.class */
        private static class FieldAttributeVisitor extends FieldVisitor {
            private final FieldDescription fieldDescription;
            private final FieldAttributeAppender fieldAttributeAppender;
            private final AnnotationValueFilter annotationValueFilter;

            private FieldAttributeVisitor(FieldVisitor fieldVisitor, FieldDescription fieldDescription, FieldAttributeAppender fieldAttributeAppender, AnnotationValueFilter annotationValueFilter) {
                super(OpenedClassReader.ASM_API, fieldVisitor);
                this.fieldDescription = fieldDescription;
                this.fieldAttributeAppender = fieldAttributeAppender;
                this.annotationValueFilter = annotationValueFilter;
            }

            @Override // net.bytebuddy.jar.asm.FieldVisitor
            public void visitEnd() {
                this.fieldAttributeAppender.apply(this.fv, this.fieldDescription, this.annotationValueFilter);
                super.visitEnd();
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberAttributeExtension$ForMethod.class */
    public static class ForMethod extends MemberAttributeExtension<MethodAttributeAppender.Factory> implements AsmVisitorWrapper.ForDeclaredMethods.MethodVisitorWrapper {
        public ForMethod() {
            this(AnnotationValueFilter.Default.APPEND_DEFAULTS);
        }

        public ForMethod(AnnotationValueFilter.Factory annotationValueFilterFactory) {
            this(annotationValueFilterFactory, MethodAttributeAppender.NoOp.INSTANCE);
        }

        protected ForMethod(AnnotationValueFilter.Factory annotationValueFilterFactory, MethodAttributeAppender.Factory attributeAppenderFactory) {
            super(annotationValueFilterFactory, attributeAppenderFactory);
        }

        public ForMethod annotateMethod(Annotation... annotation) {
            return annotateMethod(Arrays.asList(annotation));
        }

        public ForMethod annotateMethod(List<? extends Annotation> annotations) {
            return annotateMethod((Collection<? extends AnnotationDescription>) new AnnotationList.ForLoadedAnnotations(annotations));
        }

        public ForMethod annotateMethod(AnnotationDescription... annotation) {
            return annotateMethod((Collection<? extends AnnotationDescription>) Arrays.asList(annotation));
        }

        public ForMethod annotateMethod(Collection<? extends AnnotationDescription> annotations) {
            return attribute(new MethodAttributeAppender.Explicit(new ArrayList(annotations)));
        }

        public ForMethod annotateParameter(int index, Annotation... annotation) {
            return annotateParameter(index, Arrays.asList(annotation));
        }

        public ForMethod annotateParameter(int index, List<? extends Annotation> annotations) {
            return annotateParameter(index, (Collection<? extends AnnotationDescription>) new AnnotationList.ForLoadedAnnotations(annotations));
        }

        public ForMethod annotateParameter(int index, AnnotationDescription... annotation) {
            return annotateParameter(index, (Collection<? extends AnnotationDescription>) Arrays.asList(annotation));
        }

        public ForMethod annotateParameter(int index, Collection<? extends AnnotationDescription> annotations) {
            if (index < 0) {
                throw new IllegalArgumentException("Parameter index cannot be negative: " + index);
            }
            return attribute(new MethodAttributeAppender.Explicit(index, new ArrayList(annotations)));
        }

        public ForMethod attribute(MethodAttributeAppender.Factory attributeAppenderFactory) {
            return new ForMethod(this.annotationValueFilterFactory, new MethodAttributeAppender.Factory.Compound((MethodAttributeAppender.Factory) this.attributeAppenderFactory, attributeAppenderFactory));
        }

        @Override // net.bytebuddy.asm.AsmVisitorWrapper.ForDeclaredMethods.MethodVisitorWrapper
        public MethodVisitor wrap(TypeDescription instrumentedType, MethodDescription instrumentedMethod, MethodVisitor methodVisitor, Implementation.Context implementationContext, TypePool typePool, int writerFlags, int readerFlags) {
            return new AttributeAppendingMethodVisitor(methodVisitor, instrumentedMethod, ((MethodAttributeAppender.Factory) this.attributeAppenderFactory).make(instrumentedType), this.annotationValueFilterFactory.on(instrumentedMethod));
        }

        public AsmVisitorWrapper on(ElementMatcher<? super MethodDescription> matcher) {
            return new AsmVisitorWrapper.ForDeclaredMethods().invokable(matcher, this);
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberAttributeExtension$ForMethod$AttributeAppendingMethodVisitor.class */
        private static class AttributeAppendingMethodVisitor extends MethodVisitor {
            private final MethodDescription methodDescription;
            private final MethodAttributeAppender methodAttributeAppender;
            private final AnnotationValueFilter annotationValueFilter;
            private boolean applicable;

            private AttributeAppendingMethodVisitor(MethodVisitor methodVisitor, MethodDescription methodDescription, MethodAttributeAppender methodAttributeAppender, AnnotationValueFilter annotationValueFilter) {
                super(OpenedClassReader.ASM_API, methodVisitor);
                this.methodDescription = methodDescription;
                this.methodAttributeAppender = methodAttributeAppender;
                this.annotationValueFilter = annotationValueFilter;
                this.applicable = true;
            }

            @Override // net.bytebuddy.jar.asm.MethodVisitor
            public void visitCode() {
                if (this.applicable) {
                    this.methodAttributeAppender.apply(this.mv, this.methodDescription, this.annotationValueFilter);
                    this.applicable = false;
                }
                super.visitCode();
            }

            @Override // net.bytebuddy.jar.asm.MethodVisitor
            public void visitEnd() {
                if (this.applicable) {
                    this.methodAttributeAppender.apply(this.mv, this.methodDescription, this.annotationValueFilter);
                    this.applicable = false;
                }
                super.visitEnd();
            }
        }
    }
}
