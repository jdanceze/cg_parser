package net.bytebuddy.dynamic.scaffold.inline;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.TypeResolutionStrategy;
import net.bytebuddy.dynamic.VisibilityBridgeStrategy;
import net.bytebuddy.dynamic.scaffold.ClassWriterStrategy;
import net.bytebuddy.dynamic.scaffold.FieldRegistry;
import net.bytebuddy.dynamic.scaffold.InstrumentedType;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import net.bytebuddy.dynamic.scaffold.MethodRegistry;
import net.bytebuddy.dynamic.scaffold.RecordComponentRegistry;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.dynamic.scaffold.TypeWriter;
import net.bytebuddy.dynamic.scaffold.inline.MethodRebaseResolver;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.attribute.AnnotationRetention;
import net.bytebuddy.implementation.attribute.AnnotationValueFilter;
import net.bytebuddy.implementation.attribute.TypeAttributeAppender;
import net.bytebuddy.implementation.auxiliary.AuxiliaryType;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.matcher.LatentMatcher;
import net.bytebuddy.pool.TypePool;
@HashCodeAndEqualsPlugin.Enhance
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/inline/RebaseDynamicTypeBuilder.class */
public class RebaseDynamicTypeBuilder<T> extends AbstractInliningDynamicTypeBuilder<T> {
    private final MethodNameTransformer methodNameTransformer;

    @Override // net.bytebuddy.dynamic.scaffold.inline.AbstractInliningDynamicTypeBuilder, net.bytebuddy.dynamic.DynamicType.Builder.AbstractBase.Adapter
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.methodNameTransformer.equals(((RebaseDynamicTypeBuilder) obj).methodNameTransformer);
        }
        return false;
    }

    @Override // net.bytebuddy.dynamic.scaffold.inline.AbstractInliningDynamicTypeBuilder, net.bytebuddy.dynamic.DynamicType.Builder.AbstractBase.Adapter
    public int hashCode() {
        return (super.hashCode() * 31) + this.methodNameTransformer.hashCode();
    }

    public RebaseDynamicTypeBuilder(InstrumentedType.WithFlexibleName instrumentedType, ClassFileVersion classFileVersion, AuxiliaryType.NamingStrategy auxiliaryTypeNamingStrategy, AnnotationValueFilter.Factory annotationValueFilterFactory, AnnotationRetention annotationRetention, Implementation.Context.Factory implementationContextFactory, MethodGraph.Compiler methodGraphCompiler, TypeValidation typeValidation, VisibilityBridgeStrategy visibilityBridgeStrategy, ClassWriterStrategy classWriterStrategy, LatentMatcher<? super MethodDescription> ignoredMethods, TypeDescription originalType, ClassFileLocator classFileLocator, MethodNameTransformer methodNameTransformer) {
        this(instrumentedType, new FieldRegistry.Default(), new MethodRegistry.Default(), new RecordComponentRegistry.Default(), annotationRetention.isEnabled() ? new TypeAttributeAppender.ForInstrumentedType.Differentiating(originalType) : TypeAttributeAppender.ForInstrumentedType.INSTANCE, AsmVisitorWrapper.NoOp.INSTANCE, classFileVersion, auxiliaryTypeNamingStrategy, annotationValueFilterFactory, annotationRetention, implementationContextFactory, methodGraphCompiler, typeValidation, visibilityBridgeStrategy, classWriterStrategy, ignoredMethods, Collections.emptyList(), originalType, classFileLocator, methodNameTransformer);
    }

    protected RebaseDynamicTypeBuilder(InstrumentedType.WithFlexibleName instrumentedType, FieldRegistry fieldRegistry, MethodRegistry methodRegistry, RecordComponentRegistry recordComponentRegistry, TypeAttributeAppender typeAttributeAppender, AsmVisitorWrapper asmVisitorWrapper, ClassFileVersion classFileVersion, AuxiliaryType.NamingStrategy auxiliaryTypeNamingStrategy, AnnotationValueFilter.Factory annotationValueFilterFactory, AnnotationRetention annotationRetention, Implementation.Context.Factory implementationContextFactory, MethodGraph.Compiler methodGraphCompiler, TypeValidation typeValidation, VisibilityBridgeStrategy visibilityBridgeStrategy, ClassWriterStrategy classWriterStrategy, LatentMatcher<? super MethodDescription> ignoredMethods, List<? extends DynamicType> auxiliaryTypes, TypeDescription originalType, ClassFileLocator classFileLocator, MethodNameTransformer methodNameTransformer) {
        super(instrumentedType, fieldRegistry, methodRegistry, recordComponentRegistry, typeAttributeAppender, asmVisitorWrapper, classFileVersion, auxiliaryTypeNamingStrategy, annotationValueFilterFactory, annotationRetention, implementationContextFactory, methodGraphCompiler, typeValidation, visibilityBridgeStrategy, classWriterStrategy, ignoredMethods, auxiliaryTypes, originalType, classFileLocator);
        this.methodNameTransformer = methodNameTransformer;
    }

    @Override // net.bytebuddy.dynamic.DynamicType.Builder.AbstractBase.Adapter
    protected DynamicType.Builder<T> materialize(InstrumentedType.WithFlexibleName instrumentedType, FieldRegistry fieldRegistry, MethodRegistry methodRegistry, RecordComponentRegistry recordComponentRegistry, TypeAttributeAppender typeAttributeAppender, AsmVisitorWrapper asmVisitorWrapper, ClassFileVersion classFileVersion, AuxiliaryType.NamingStrategy auxiliaryTypeNamingStrategy, AnnotationValueFilter.Factory annotationValueFilterFactory, AnnotationRetention annotationRetention, Implementation.Context.Factory implementationContextFactory, MethodGraph.Compiler methodGraphCompiler, TypeValidation typeValidation, VisibilityBridgeStrategy visibilityBridgeStrategy, ClassWriterStrategy classWriterStrategy, LatentMatcher<? super MethodDescription> ignoredMethods, List<? extends DynamicType> auxiliaryTypes) {
        return new RebaseDynamicTypeBuilder(instrumentedType, fieldRegistry, methodRegistry, recordComponentRegistry, typeAttributeAppender, asmVisitorWrapper, classFileVersion, auxiliaryTypeNamingStrategy, annotationValueFilterFactory, annotationRetention, implementationContextFactory, methodGraphCompiler, typeValidation, visibilityBridgeStrategy, classWriterStrategy, ignoredMethods, auxiliaryTypes, this.originalType, this.classFileLocator, this.methodNameTransformer);
    }

    @Override // net.bytebuddy.dynamic.DynamicType.Builder
    public DynamicType.Unloaded<T> make(TypeResolutionStrategy typeResolutionStrategy, TypePool typePool) {
        MethodRegistry.Prepared methodRegistry = this.methodRegistry.prepare(this.instrumentedType, this.methodGraphCompiler, this.typeValidation, this.visibilityBridgeStrategy, InliningImplementationMatcher.of(this.ignoredMethods, this.originalType));
        MethodRebaseResolver methodRebaseResolver = MethodRebaseResolver.Default.make(methodRegistry.getInstrumentedType(), new HashSet(this.originalType.getDeclaredMethods().asTokenList(ElementMatchers.is(this.originalType)).filter(RebaseableMatcher.of(methodRegistry.getInstrumentedType(), methodRegistry.getInstrumentedMethods()))), this.classFileVersion, this.auxiliaryTypeNamingStrategy, this.methodNameTransformer);
        return TypeWriter.Default.forRebasing(methodRegistry, this.auxiliaryTypes, this.fieldRegistry.compile(methodRegistry.getInstrumentedType()), this.recordComponentRegistry.compile(methodRegistry.getInstrumentedType()), this.typeAttributeAppender, this.asmVisitorWrapper, this.classFileVersion, this.annotationValueFilterFactory, this.annotationRetention, this.auxiliaryTypeNamingStrategy, this.implementationContextFactory, this.typeValidation, this.classWriterStrategy, typePool, this.originalType, this.classFileLocator, methodRebaseResolver).make(typeResolutionStrategy.resolve());
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/inline/RebaseDynamicTypeBuilder$RebaseableMatcher.class */
    protected static class RebaseableMatcher implements ElementMatcher<MethodDescription.Token> {
        private final Set<MethodDescription.Token> tokens;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.tokens.equals(((RebaseableMatcher) obj).tokens);
        }

        public int hashCode() {
            return (17 * 31) + this.tokens.hashCode();
        }

        protected RebaseableMatcher(Set<MethodDescription.Token> tokens) {
            this.tokens = tokens;
        }

        protected static ElementMatcher<MethodDescription.Token> of(TypeDescription instrumentedType, MethodList<?> instrumentedMethods) {
            return new RebaseableMatcher(new HashSet(instrumentedMethods.asTokenList(ElementMatchers.is(instrumentedType))));
        }

        @Override // net.bytebuddy.matcher.ElementMatcher
        public boolean matches(MethodDescription.Token target) {
            return this.tokens.contains(target);
        }
    }
}
