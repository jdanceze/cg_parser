package net.bytebuddy.dynamic.scaffold.inline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.modifier.ModifierContributor;
import net.bytebuddy.description.type.RecordComponentDescription;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.description.type.TypeVariableToken;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.Transformer;
import net.bytebuddy.dynamic.TypeResolutionStrategy;
import net.bytebuddy.dynamic.scaffold.ClassWriterStrategy;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.dynamic.scaffold.TypeWriter;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.LoadedTypeInitializer;
import net.bytebuddy.implementation.attribute.AnnotationRetention;
import net.bytebuddy.implementation.attribute.AnnotationValueFilter;
import net.bytebuddy.implementation.attribute.TypeAttributeAppender;
import net.bytebuddy.implementation.auxiliary.AuxiliaryType;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.matcher.LatentMatcher;
import net.bytebuddy.pool.TypePool;
import net.bytebuddy.utility.CompoundList;
@HashCodeAndEqualsPlugin.Enhance
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/inline/DecoratingDynamicTypeBuilder.class */
public class DecoratingDynamicTypeBuilder<T> extends DynamicType.Builder.AbstractBase<T> {
    private final TypeDescription instrumentedType;
    private final TypeAttributeAppender typeAttributeAppender;
    private final AsmVisitorWrapper asmVisitorWrapper;
    private final ClassFileVersion classFileVersion;
    private final AuxiliaryType.NamingStrategy auxiliaryTypeNamingStrategy;
    private final AnnotationValueFilter.Factory annotationValueFilterFactory;
    private final AnnotationRetention annotationRetention;
    private final Implementation.Context.Factory implementationContextFactory;
    private final MethodGraph.Compiler methodGraphCompiler;
    private final TypeValidation typeValidation;
    private final ClassWriterStrategy classWriterStrategy;
    private final LatentMatcher<? super MethodDescription> ignoredMethods;
    private final List<DynamicType> auxiliaryTypes;
    private final ClassFileLocator classFileLocator;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.annotationRetention.equals(((DecoratingDynamicTypeBuilder) obj).annotationRetention) && this.typeValidation.equals(((DecoratingDynamicTypeBuilder) obj).typeValidation) && this.instrumentedType.equals(((DecoratingDynamicTypeBuilder) obj).instrumentedType) && this.typeAttributeAppender.equals(((DecoratingDynamicTypeBuilder) obj).typeAttributeAppender) && this.asmVisitorWrapper.equals(((DecoratingDynamicTypeBuilder) obj).asmVisitorWrapper) && this.classFileVersion.equals(((DecoratingDynamicTypeBuilder) obj).classFileVersion) && this.auxiliaryTypeNamingStrategy.equals(((DecoratingDynamicTypeBuilder) obj).auxiliaryTypeNamingStrategy) && this.annotationValueFilterFactory.equals(((DecoratingDynamicTypeBuilder) obj).annotationValueFilterFactory) && this.implementationContextFactory.equals(((DecoratingDynamicTypeBuilder) obj).implementationContextFactory) && this.methodGraphCompiler.equals(((DecoratingDynamicTypeBuilder) obj).methodGraphCompiler) && this.classWriterStrategy.equals(((DecoratingDynamicTypeBuilder) obj).classWriterStrategy) && this.ignoredMethods.equals(((DecoratingDynamicTypeBuilder) obj).ignoredMethods) && this.auxiliaryTypes.equals(((DecoratingDynamicTypeBuilder) obj).auxiliaryTypes) && this.classFileLocator.equals(((DecoratingDynamicTypeBuilder) obj).classFileLocator);
    }

    public int hashCode() {
        return (((((((((((((((((((((((((((17 * 31) + this.instrumentedType.hashCode()) * 31) + this.typeAttributeAppender.hashCode()) * 31) + this.asmVisitorWrapper.hashCode()) * 31) + this.classFileVersion.hashCode()) * 31) + this.auxiliaryTypeNamingStrategy.hashCode()) * 31) + this.annotationValueFilterFactory.hashCode()) * 31) + this.annotationRetention.hashCode()) * 31) + this.implementationContextFactory.hashCode()) * 31) + this.methodGraphCompiler.hashCode()) * 31) + this.typeValidation.hashCode()) * 31) + this.classWriterStrategy.hashCode()) * 31) + this.ignoredMethods.hashCode()) * 31) + this.auxiliaryTypes.hashCode()) * 31) + this.classFileLocator.hashCode();
    }

    public DecoratingDynamicTypeBuilder(TypeDescription instrumentedType, ClassFileVersion classFileVersion, AuxiliaryType.NamingStrategy auxiliaryTypeNamingStrategy, AnnotationValueFilter.Factory annotationValueFilterFactory, AnnotationRetention annotationRetention, Implementation.Context.Factory implementationContextFactory, MethodGraph.Compiler methodGraphCompiler, TypeValidation typeValidation, ClassWriterStrategy classWriterStrategy, LatentMatcher<? super MethodDescription> ignoredMethods, ClassFileLocator classFileLocator) {
        this(instrumentedType, annotationRetention.isEnabled() ? new TypeAttributeAppender.ForInstrumentedType.Differentiating(instrumentedType) : TypeAttributeAppender.ForInstrumentedType.INSTANCE, AsmVisitorWrapper.NoOp.INSTANCE, classFileVersion, auxiliaryTypeNamingStrategy, annotationValueFilterFactory, annotationRetention, implementationContextFactory, methodGraphCompiler, typeValidation, classWriterStrategy, ignoredMethods, Collections.emptyList(), classFileLocator);
    }

    protected DecoratingDynamicTypeBuilder(TypeDescription instrumentedType, TypeAttributeAppender typeAttributeAppender, AsmVisitorWrapper asmVisitorWrapper, ClassFileVersion classFileVersion, AuxiliaryType.NamingStrategy auxiliaryTypeNamingStrategy, AnnotationValueFilter.Factory annotationValueFilterFactory, AnnotationRetention annotationRetention, Implementation.Context.Factory implementationContextFactory, MethodGraph.Compiler methodGraphCompiler, TypeValidation typeValidation, ClassWriterStrategy classWriterStrategy, LatentMatcher<? super MethodDescription> ignoredMethods, List<DynamicType> auxiliaryTypes, ClassFileLocator classFileLocator) {
        this.instrumentedType = instrumentedType;
        this.typeAttributeAppender = typeAttributeAppender;
        this.asmVisitorWrapper = asmVisitorWrapper;
        this.classFileVersion = classFileVersion;
        this.auxiliaryTypeNamingStrategy = auxiliaryTypeNamingStrategy;
        this.annotationValueFilterFactory = annotationValueFilterFactory;
        this.annotationRetention = annotationRetention;
        this.implementationContextFactory = implementationContextFactory;
        this.methodGraphCompiler = methodGraphCompiler;
        this.typeValidation = typeValidation;
        this.classWriterStrategy = classWriterStrategy;
        this.ignoredMethods = ignoredMethods;
        this.auxiliaryTypes = auxiliaryTypes;
        this.classFileLocator = classFileLocator;
    }

    @Override // net.bytebuddy.dynamic.DynamicType.Builder
    public DynamicType.Builder<T> visit(AsmVisitorWrapper asmVisitorWrapper) {
        return new DecoratingDynamicTypeBuilder(this.instrumentedType, this.typeAttributeAppender, new AsmVisitorWrapper.Compound(this.asmVisitorWrapper, asmVisitorWrapper), this.classFileVersion, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.typeValidation, this.classWriterStrategy, this.ignoredMethods, this.auxiliaryTypes, this.classFileLocator);
    }

    @Override // net.bytebuddy.dynamic.DynamicType.Builder
    public DynamicType.Builder<T> suffix(String suffix) {
        throw new UnsupportedOperationException("Cannot change name of decorated type: " + this.instrumentedType);
    }

    @Override // net.bytebuddy.dynamic.DynamicType.Builder
    public DynamicType.Builder<T> name(String name) {
        throw new UnsupportedOperationException("Cannot change name of decorated type: " + this.instrumentedType);
    }

    @Override // net.bytebuddy.dynamic.DynamicType.Builder
    public DynamicType.Builder<T> modifiers(int modifiers) {
        throw new UnsupportedOperationException("Cannot change modifiers of decorated type: " + this.instrumentedType);
    }

    @Override // net.bytebuddy.dynamic.DynamicType.Builder
    public DynamicType.Builder<T> merge(Collection<? extends ModifierContributor.ForType> modifierContributors) {
        throw new UnsupportedOperationException("Cannot change modifiers of decorated type: " + this.instrumentedType);
    }

    @Override // net.bytebuddy.dynamic.DynamicType.Builder
    public DynamicType.Builder<T> topLevelType() {
        throw new UnsupportedOperationException("Cannot change type declaration of decorated type: " + this.instrumentedType);
    }

    @Override // net.bytebuddy.dynamic.DynamicType.Builder
    public DynamicType.Builder.InnerTypeDefinition.ForType<T> innerTypeOf(TypeDescription type) {
        throw new UnsupportedOperationException("Cannot change type declaration of decorated type: " + this.instrumentedType);
    }

    @Override // net.bytebuddy.dynamic.DynamicType.Builder
    public DynamicType.Builder.InnerTypeDefinition<T> innerTypeOf(MethodDescription.InDefinedShape methodDescription) {
        throw new UnsupportedOperationException("Cannot change type declaration of decorated type: " + this.instrumentedType);
    }

    @Override // net.bytebuddy.dynamic.DynamicType.Builder
    public DynamicType.Builder<T> declaredTypes(Collection<? extends TypeDescription> types) {
        throw new UnsupportedOperationException("Cannot change type declaration of decorated type: " + this.instrumentedType);
    }

    @Override // net.bytebuddy.dynamic.DynamicType.Builder
    public DynamicType.Builder<T> nestHost(TypeDescription type) {
        throw new UnsupportedOperationException("Cannot change type declaration of decorated type: " + this.instrumentedType);
    }

    @Override // net.bytebuddy.dynamic.DynamicType.Builder
    public DynamicType.Builder<T> nestMembers(Collection<? extends TypeDescription> types) {
        throw new UnsupportedOperationException("Cannot change type declaration of decorated type: " + this.instrumentedType);
    }

    @Override // net.bytebuddy.dynamic.DynamicType.Builder
    public DynamicType.Builder<T> permittedSubclass(Collection<? extends TypeDescription> types) {
        throw new UnsupportedOperationException("Cannot change permitted subclasses of decorated type: " + this.instrumentedType);
    }

    @Override // net.bytebuddy.dynamic.DynamicType.Builder
    public DynamicType.Builder<T> unsealed() {
        throw new UnsupportedOperationException("Cannot unseal decorated type: " + this.instrumentedType);
    }

    @Override // net.bytebuddy.dynamic.DynamicType.Builder
    public DynamicType.Builder<T> attribute(TypeAttributeAppender typeAttributeAppender) {
        return new DecoratingDynamicTypeBuilder(this.instrumentedType, new TypeAttributeAppender.Compound(this.typeAttributeAppender, typeAttributeAppender), this.asmVisitorWrapper, this.classFileVersion, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.typeValidation, this.classWriterStrategy, this.ignoredMethods, this.auxiliaryTypes, this.classFileLocator);
    }

    @Override // net.bytebuddy.dynamic.DynamicType.Builder
    public DynamicType.Builder<T> annotateType(Collection<? extends AnnotationDescription> annotations) {
        return attribute(new TypeAttributeAppender.Explicit(new ArrayList(annotations)));
    }

    @Override // net.bytebuddy.dynamic.DynamicType.Builder
    public DynamicType.Builder.MethodDefinition.ImplementationDefinition.Optional<T> implement(Collection<? extends TypeDefinition> interfaceTypes) {
        throw new UnsupportedOperationException("Cannot implement interface for decorated type: " + this.instrumentedType);
    }

    @Override // net.bytebuddy.dynamic.DynamicType.Builder
    public DynamicType.Builder<T> initializer(ByteCodeAppender byteCodeAppender) {
        throw new UnsupportedOperationException("Cannot add initializer of decorated type: " + this.instrumentedType);
    }

    @Override // net.bytebuddy.dynamic.DynamicType.Builder
    public DynamicType.Builder<T> initializer(LoadedTypeInitializer loadedTypeInitializer) {
        throw new UnsupportedOperationException("Cannot add initializer of decorated type: " + this.instrumentedType);
    }

    @Override // net.bytebuddy.dynamic.DynamicType.Builder
    public DynamicType.Builder.TypeVariableDefinition<T> typeVariable(String symbol, Collection<? extends TypeDefinition> bounds) {
        throw new UnsupportedOperationException("Cannot add type variable to decorated type: " + this.instrumentedType);
    }

    @Override // net.bytebuddy.dynamic.DynamicType.Builder
    public DynamicType.Builder<T> transform(ElementMatcher<? super TypeDescription.Generic> matcher, Transformer<TypeVariableToken> transformer) {
        throw new UnsupportedOperationException("Cannot transform decorated type: " + this.instrumentedType);
    }

    @Override // net.bytebuddy.dynamic.DynamicType.Builder
    public DynamicType.Builder.FieldDefinition.Optional.Valuable<T> defineField(String name, TypeDefinition type, int modifiers) {
        throw new UnsupportedOperationException("Cannot define field for decorated type: " + this.instrumentedType);
    }

    @Override // net.bytebuddy.dynamic.DynamicType.Builder
    public DynamicType.Builder.FieldDefinition.Valuable<T> field(LatentMatcher<? super FieldDescription> matcher) {
        throw new UnsupportedOperationException("Cannot change field for decorated type: " + this.instrumentedType);
    }

    @Override // net.bytebuddy.dynamic.DynamicType.Builder
    public DynamicType.Builder<T> ignoreAlso(LatentMatcher<? super MethodDescription> ignoredMethods) {
        return new DecoratingDynamicTypeBuilder(this.instrumentedType, this.typeAttributeAppender, this.asmVisitorWrapper, this.classFileVersion, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.typeValidation, this.classWriterStrategy, new LatentMatcher.Disjunction(this.ignoredMethods, ignoredMethods), this.auxiliaryTypes, this.classFileLocator);
    }

    @Override // net.bytebuddy.dynamic.DynamicType.Builder
    public DynamicType.Builder.MethodDefinition.ParameterDefinition.Initial<T> defineMethod(String name, TypeDefinition returnType, int modifiers) {
        throw new UnsupportedOperationException("Cannot define method for decorated type: " + this.instrumentedType);
    }

    @Override // net.bytebuddy.dynamic.DynamicType.Builder
    public DynamicType.Builder.MethodDefinition.ParameterDefinition.Initial<T> defineConstructor(int modifiers) {
        throw new UnsupportedOperationException("Cannot define constructor for decorated type: " + this.instrumentedType);
    }

    @Override // net.bytebuddy.dynamic.DynamicType.Builder
    public DynamicType.Builder.MethodDefinition.ImplementationDefinition<T> invokable(LatentMatcher<? super MethodDescription> matcher) {
        throw new UnsupportedOperationException("Cannot intercept method for decorated type: " + this.instrumentedType);
    }

    @Override // net.bytebuddy.dynamic.DynamicType.Builder
    public DynamicType.Builder.RecordComponentDefinition.Optional<T> defineRecordComponent(String name, TypeDefinition type) {
        throw new UnsupportedOperationException("Cannot define record component for decorated type: " + this.instrumentedType);
    }

    @Override // net.bytebuddy.dynamic.DynamicType.Builder
    public DynamicType.Builder.RecordComponentDefinition<T> recordComponent(LatentMatcher<? super RecordComponentDescription> matcher) {
        throw new UnsupportedOperationException("Cannot change record component for decorated type: " + this.instrumentedType);
    }

    @Override // net.bytebuddy.dynamic.DynamicType.Builder
    public DynamicType.Builder<T> require(Collection<DynamicType> auxiliaryTypes) {
        return new DecoratingDynamicTypeBuilder(this.instrumentedType, this.typeAttributeAppender, this.asmVisitorWrapper, this.classFileVersion, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler, this.typeValidation, this.classWriterStrategy, this.ignoredMethods, CompoundList.of((List) this.auxiliaryTypes, (List) new ArrayList(auxiliaryTypes)), this.classFileLocator);
    }

    @Override // net.bytebuddy.dynamic.DynamicType.Builder
    public DynamicType.Unloaded<T> make(TypeResolutionStrategy typeResolutionStrategy) {
        return make(typeResolutionStrategy, TypePool.Empty.INSTANCE);
    }

    @Override // net.bytebuddy.dynamic.DynamicType.Builder
    public DynamicType.Unloaded<T> make(TypeResolutionStrategy typeResolutionStrategy, TypePool typePool) {
        return TypeWriter.Default.forDecoration(this.instrumentedType, this.classFileVersion, this.auxiliaryTypes, CompoundList.of((List) this.methodGraphCompiler.compile(this.instrumentedType).listNodes().asMethodList().filter(ElementMatchers.not(this.ignoredMethods.resolve(this.instrumentedType))), (List) this.instrumentedType.getDeclaredMethods().filter(ElementMatchers.not(ElementMatchers.isVirtual()))), this.typeAttributeAppender, this.asmVisitorWrapper, this.annotationValueFilterFactory, this.annotationRetention, this.auxiliaryTypeNamingStrategy, this.implementationContextFactory, this.typeValidation, this.classWriterStrategy, typePool, this.classFileLocator).make(typeResolutionStrategy.resolve());
    }

    @Override // net.bytebuddy.dynamic.DynamicType.Builder
    public TypeDescription toTypeDescription() {
        return this.instrumentedType;
    }
}
