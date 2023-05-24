package net.bytebuddy.asm;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.ByteCodeElement;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.field.FieldList;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.description.type.TypeList;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.bytecode.Duplication;
import net.bytebuddy.implementation.bytecode.Removal;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.StackSize;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import net.bytebuddy.implementation.bytecode.constant.DefaultValue;
import net.bytebuddy.implementation.bytecode.member.FieldAccess;
import net.bytebuddy.implementation.bytecode.member.MethodInvocation;
import net.bytebuddy.implementation.bytecode.member.MethodVariableAccess;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.pool.TypePool;
import net.bytebuddy.utility.CompoundList;
import net.bytebuddy.utility.OpenedClassReader;
import net.bytebuddy.utility.visitor.LocalVariableAwareMethodVisitor;
@HashCodeAndEqualsPlugin.Enhance
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution.class */
public class MemberSubstitution implements AsmVisitorWrapper.ForDeclaredMethods.MethodVisitorWrapper {
    private final MethodGraph.Compiler methodGraphCompiler;
    private final boolean strict;
    private final TypePoolResolver typePoolResolver;
    private final Replacement.Factory replacementFactory;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.strict == ((MemberSubstitution) obj).strict && this.methodGraphCompiler.equals(((MemberSubstitution) obj).methodGraphCompiler) && this.typePoolResolver.equals(((MemberSubstitution) obj).typePoolResolver) && this.replacementFactory.equals(((MemberSubstitution) obj).replacementFactory);
    }

    public int hashCode() {
        return (((((((17 * 31) + this.methodGraphCompiler.hashCode()) * 31) + (this.strict ? 1 : 0)) * 31) + this.typePoolResolver.hashCode()) * 31) + this.replacementFactory.hashCode();
    }

    protected MemberSubstitution(boolean strict) {
        this(MethodGraph.Compiler.DEFAULT, TypePoolResolver.OfImplicitPool.INSTANCE, strict, Replacement.NoOp.INSTANCE);
    }

    protected MemberSubstitution(MethodGraph.Compiler methodGraphCompiler, TypePoolResolver typePoolResolver, boolean strict, Replacement.Factory replacementFactory) {
        this.methodGraphCompiler = methodGraphCompiler;
        this.typePoolResolver = typePoolResolver;
        this.strict = strict;
        this.replacementFactory = replacementFactory;
    }

    public static MemberSubstitution strict() {
        return new MemberSubstitution(true);
    }

    public static MemberSubstitution relaxed() {
        return new MemberSubstitution(false);
    }

    public WithoutSpecification element(ElementMatcher<? super ByteCodeElement> matcher) {
        return new WithoutSpecification.ForMatchedByteCodeElement(this.methodGraphCompiler, this.typePoolResolver, this.strict, this.replacementFactory, matcher);
    }

    public WithoutSpecification.ForMatchedField field(ElementMatcher<? super FieldDescription.InDefinedShape> matcher) {
        return new WithoutSpecification.ForMatchedField(this.methodGraphCompiler, this.typePoolResolver, this.strict, this.replacementFactory, matcher);
    }

    public WithoutSpecification.ForMatchedMethod method(ElementMatcher<? super MethodDescription> matcher) {
        return new WithoutSpecification.ForMatchedMethod(this.methodGraphCompiler, this.typePoolResolver, this.strict, this.replacementFactory, matcher);
    }

    public WithoutSpecification constructor(ElementMatcher<? super MethodDescription> matcher) {
        return invokable(ElementMatchers.isConstructor().and(matcher));
    }

    public WithoutSpecification invokable(ElementMatcher<? super MethodDescription> matcher) {
        return new WithoutSpecification.ForMatchedMethod(this.methodGraphCompiler, this.typePoolResolver, this.strict, this.replacementFactory, matcher);
    }

    public MemberSubstitution with(MethodGraph.Compiler methodGraphCompiler) {
        return new MemberSubstitution(methodGraphCompiler, this.typePoolResolver, this.strict, this.replacementFactory);
    }

    public MemberSubstitution with(TypePoolResolver typePoolResolver) {
        return new MemberSubstitution(this.methodGraphCompiler, typePoolResolver, this.strict, this.replacementFactory);
    }

    public AsmVisitorWrapper.ForDeclaredMethods on(ElementMatcher<? super MethodDescription> matcher) {
        return new AsmVisitorWrapper.ForDeclaredMethods().invokable(matcher, this);
    }

    @Override // net.bytebuddy.asm.AsmVisitorWrapper.ForDeclaredMethods.MethodVisitorWrapper
    public MethodVisitor wrap(TypeDescription instrumentedType, MethodDescription instrumentedMethod, MethodVisitor methodVisitor, Implementation.Context implementationContext, TypePool typePool, int writerFlags, int readerFlags) {
        TypePool typePool2 = this.typePoolResolver.resolve(instrumentedType, instrumentedMethod, typePool);
        return new SubstitutingMethodVisitor(methodVisitor, instrumentedType, instrumentedMethod, this.methodGraphCompiler, this.strict, this.replacementFactory.make(instrumentedType, instrumentedMethod, typePool2), implementationContext, typePool2, implementationContext.getClassFileVersion().isAtLeast(ClassFileVersion.JAVA_V11));
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$WithoutSpecification.class */
    public static abstract class WithoutSpecification {
        protected final MethodGraph.Compiler methodGraphCompiler;
        protected final TypePoolResolver typePoolResolver;
        protected final boolean strict;
        protected final Replacement.Factory replacementFactory;

        public abstract MemberSubstitution replaceWith(Substitution.Factory factory);

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.strict == ((WithoutSpecification) obj).strict && this.methodGraphCompiler.equals(((WithoutSpecification) obj).methodGraphCompiler) && this.typePoolResolver.equals(((WithoutSpecification) obj).typePoolResolver) && this.replacementFactory.equals(((WithoutSpecification) obj).replacementFactory);
        }

        public int hashCode() {
            return (((((((17 * 31) + this.methodGraphCompiler.hashCode()) * 31) + this.typePoolResolver.hashCode()) * 31) + (this.strict ? 1 : 0)) * 31) + this.replacementFactory.hashCode();
        }

        protected WithoutSpecification(MethodGraph.Compiler methodGraphCompiler, TypePoolResolver typePoolResolver, boolean strict, Replacement.Factory replacementFactory) {
            this.methodGraphCompiler = methodGraphCompiler;
            this.typePoolResolver = typePoolResolver;
            this.strict = strict;
            this.replacementFactory = replacementFactory;
        }

        public MemberSubstitution stub() {
            return replaceWith(Substitution.Stubbing.INSTANCE);
        }

        public MemberSubstitution replaceWith(Field field) {
            return replaceWith(new FieldDescription.ForLoadedField(field));
        }

        public MemberSubstitution replaceWith(FieldDescription fieldDescription) {
            return replaceWith(new Substitution.ForFieldAccess.OfGivenField(fieldDescription));
        }

        public MemberSubstitution replaceWithField(ElementMatcher<? super FieldDescription> matcher) {
            return replaceWith(new Substitution.ForFieldAccess.OfMatchedField(matcher));
        }

        public MemberSubstitution replaceWith(Method method) {
            return replaceWith(new MethodDescription.ForLoadedMethod(method));
        }

        public MemberSubstitution replaceWith(MethodDescription methodDescription) {
            if (!methodDescription.isMethod()) {
                throw new IllegalArgumentException("Cannot use " + methodDescription + " as a replacement");
            }
            return replaceWith(new Substitution.ForMethodInvocation.OfGivenMethod(methodDescription));
        }

        public MemberSubstitution replaceWithMethod(ElementMatcher<? super MethodDescription> matcher) {
            return replaceWithMethod(matcher, this.methodGraphCompiler);
        }

        public MemberSubstitution replaceWithMethod(ElementMatcher<? super MethodDescription> matcher, MethodGraph.Compiler methodGraphCompiler) {
            return replaceWith(new Substitution.ForMethodInvocation.OfMatchedMethod(matcher, methodGraphCompiler));
        }

        public MemberSubstitution replaceWithInstrumentedMethod() {
            return replaceWith(Substitution.ForMethodInvocation.OfInstrumentedMethod.INSTANCE);
        }

        public MemberSubstitution replaceWithChain(Substitution.Chain.Step.Factory... step) {
            return replaceWithChain(Arrays.asList(step));
        }

        public MemberSubstitution replaceWithChain(List<? extends Substitution.Chain.Step.Factory> steps) {
            return replaceWith(Substitution.Chain.withDefaultAssigner().executing(steps));
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$WithoutSpecification$ForMatchedByteCodeElement.class */
        protected static class ForMatchedByteCodeElement extends WithoutSpecification {
            private final ElementMatcher<? super ByteCodeElement> matcher;

            @Override // net.bytebuddy.asm.MemberSubstitution.WithoutSpecification
            public boolean equals(Object obj) {
                if (super.equals(obj)) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.matcher.equals(((ForMatchedByteCodeElement) obj).matcher);
                }
                return false;
            }

            @Override // net.bytebuddy.asm.MemberSubstitution.WithoutSpecification
            public int hashCode() {
                return (super.hashCode() * 31) + this.matcher.hashCode();
            }

            protected ForMatchedByteCodeElement(MethodGraph.Compiler methodGraphCompiler, TypePoolResolver typePoolResolver, boolean strict, Replacement.Factory replacementFactory, ElementMatcher<? super ByteCodeElement> matcher) {
                super(methodGraphCompiler, typePoolResolver, strict, replacementFactory);
                this.matcher = matcher;
            }

            @Override // net.bytebuddy.asm.MemberSubstitution.WithoutSpecification
            public MemberSubstitution replaceWith(Substitution.Factory substitutionFactory) {
                return new MemberSubstitution(this.methodGraphCompiler, this.typePoolResolver, this.strict, new Replacement.Factory.Compound(this.replacementFactory, Replacement.ForElementMatchers.Factory.of(this.matcher, substitutionFactory)));
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$WithoutSpecification$ForMatchedField.class */
        public static class ForMatchedField extends WithoutSpecification {
            private final ElementMatcher<? super FieldDescription.InDefinedShape> matcher;
            private final boolean matchRead;
            private final boolean matchWrite;

            @Override // net.bytebuddy.asm.MemberSubstitution.WithoutSpecification
            public boolean equals(Object obj) {
                if (super.equals(obj)) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.matchRead == ((ForMatchedField) obj).matchRead && this.matchWrite == ((ForMatchedField) obj).matchWrite && this.matcher.equals(((ForMatchedField) obj).matcher);
                }
                return false;
            }

            @Override // net.bytebuddy.asm.MemberSubstitution.WithoutSpecification
            public int hashCode() {
                return (((((super.hashCode() * 31) + this.matcher.hashCode()) * 31) + (this.matchRead ? 1 : 0)) * 31) + (this.matchWrite ? 1 : 0);
            }

            protected ForMatchedField(MethodGraph.Compiler methodGraphCompiler, TypePoolResolver typePoolResolver, boolean strict, Replacement.Factory replacementFactory, ElementMatcher<? super FieldDescription.InDefinedShape> matcher) {
                this(methodGraphCompiler, typePoolResolver, strict, replacementFactory, matcher, true, true);
            }

            protected ForMatchedField(MethodGraph.Compiler methodGraphCompiler, TypePoolResolver typePoolResolver, boolean strict, Replacement.Factory replacementFactory, ElementMatcher<? super FieldDescription.InDefinedShape> matcher, boolean matchRead, boolean matchWrite) {
                super(methodGraphCompiler, typePoolResolver, strict, replacementFactory);
                this.matcher = matcher;
                this.matchRead = matchRead;
                this.matchWrite = matchWrite;
            }

            public WithoutSpecification onRead() {
                return new ForMatchedField(this.methodGraphCompiler, this.typePoolResolver, this.strict, this.replacementFactory, this.matcher, true, false);
            }

            public WithoutSpecification onWrite() {
                return new ForMatchedField(this.methodGraphCompiler, this.typePoolResolver, this.strict, this.replacementFactory, this.matcher, false, true);
            }

            @Override // net.bytebuddy.asm.MemberSubstitution.WithoutSpecification
            public MemberSubstitution replaceWith(Substitution.Factory substitutionFactory) {
                return new MemberSubstitution(this.methodGraphCompiler, this.typePoolResolver, this.strict, new Replacement.Factory.Compound(this.replacementFactory, Replacement.ForElementMatchers.Factory.ofField(this.matcher, this.matchRead, this.matchWrite, substitutionFactory)));
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$WithoutSpecification$ForMatchedMethod.class */
        public static class ForMatchedMethod extends WithoutSpecification {
            private final ElementMatcher<? super MethodDescription> matcher;
            private final boolean includeVirtualCalls;
            private final boolean includeSuperCalls;

            @Override // net.bytebuddy.asm.MemberSubstitution.WithoutSpecification
            public boolean equals(Object obj) {
                if (super.equals(obj)) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.includeVirtualCalls == ((ForMatchedMethod) obj).includeVirtualCalls && this.includeSuperCalls == ((ForMatchedMethod) obj).includeSuperCalls && this.matcher.equals(((ForMatchedMethod) obj).matcher);
                }
                return false;
            }

            @Override // net.bytebuddy.asm.MemberSubstitution.WithoutSpecification
            public int hashCode() {
                return (((((super.hashCode() * 31) + this.matcher.hashCode()) * 31) + (this.includeVirtualCalls ? 1 : 0)) * 31) + (this.includeSuperCalls ? 1 : 0);
            }

            protected ForMatchedMethod(MethodGraph.Compiler methodGraphCompiler, TypePoolResolver typePoolResolver, boolean strict, Replacement.Factory replacementFactory, ElementMatcher<? super MethodDescription> matcher) {
                this(methodGraphCompiler, typePoolResolver, strict, replacementFactory, matcher, true, true);
            }

            protected ForMatchedMethod(MethodGraph.Compiler methodGraphCompiler, TypePoolResolver typePoolResolver, boolean strict, Replacement.Factory replacementFactory, ElementMatcher<? super MethodDescription> matcher, boolean includeVirtualCalls, boolean includeSuperCalls) {
                super(methodGraphCompiler, typePoolResolver, strict, replacementFactory);
                this.matcher = matcher;
                this.includeVirtualCalls = includeVirtualCalls;
                this.includeSuperCalls = includeSuperCalls;
            }

            public WithoutSpecification onVirtualCall() {
                return new ForMatchedMethod(this.methodGraphCompiler, this.typePoolResolver, this.strict, this.replacementFactory, ElementMatchers.isVirtual().and(this.matcher), true, false);
            }

            public WithoutSpecification onSuperCall() {
                return new ForMatchedMethod(this.methodGraphCompiler, this.typePoolResolver, this.strict, this.replacementFactory, ElementMatchers.isVirtual().and(this.matcher), false, true);
            }

            @Override // net.bytebuddy.asm.MemberSubstitution.WithoutSpecification
            public MemberSubstitution replaceWith(Substitution.Factory substitutionFactory) {
                return new MemberSubstitution(this.methodGraphCompiler, this.typePoolResolver, this.strict, new Replacement.Factory.Compound(this.replacementFactory, Replacement.ForElementMatchers.Factory.ofMethod(this.matcher, this.includeVirtualCalls, this.includeSuperCalls, substitutionFactory)));
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$TypePoolResolver.class */
    public interface TypePoolResolver {
        TypePool resolve(TypeDescription typeDescription, MethodDescription methodDescription, TypePool typePool);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$TypePoolResolver$OfImplicitPool.class */
        public enum OfImplicitPool implements TypePoolResolver {
            INSTANCE;

            @Override // net.bytebuddy.asm.MemberSubstitution.TypePoolResolver
            public TypePool resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, TypePool typePool) {
                return typePool;
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$TypePoolResolver$ForExplicitPool.class */
        public static class ForExplicitPool implements TypePoolResolver {
            private final TypePool typePool;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.typePool.equals(((ForExplicitPool) obj).typePool);
            }

            public int hashCode() {
                return (17 * 31) + this.typePool.hashCode();
            }

            public ForExplicitPool(TypePool typePool) {
                this.typePool = typePool;
            }

            @Override // net.bytebuddy.asm.MemberSubstitution.TypePoolResolver
            public TypePool resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, TypePool typePool) {
                return this.typePool;
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$TypePoolResolver$ForClassFileLocator.class */
        public static class ForClassFileLocator implements TypePoolResolver {
            private final ClassFileLocator classFileLocator;
            private final TypePool.Default.ReaderMode readerMode;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.readerMode.equals(((ForClassFileLocator) obj).readerMode) && this.classFileLocator.equals(((ForClassFileLocator) obj).classFileLocator);
            }

            public int hashCode() {
                return (((17 * 31) + this.classFileLocator.hashCode()) * 31) + this.readerMode.hashCode();
            }

            public ForClassFileLocator(ClassFileLocator classFileLocator) {
                this(classFileLocator, TypePool.Default.ReaderMode.FAST);
            }

            public ForClassFileLocator(ClassFileLocator classFileLocator, TypePool.Default.ReaderMode readerMode) {
                this.classFileLocator = classFileLocator;
                this.readerMode = readerMode;
            }

            public static TypePoolResolver of(ClassLoader classLoader) {
                return new ForClassFileLocator(ClassFileLocator.ForClassLoader.of(classLoader));
            }

            @Override // net.bytebuddy.asm.MemberSubstitution.TypePoolResolver
            public TypePool resolve(TypeDescription instrumentedType, MethodDescription instrumentedMethod, TypePool typePool) {
                return new TypePool.Default(new TypePool.CacheProvider.Simple(), this.classFileLocator, this.readerMode, typePool);
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$Substitution.class */
    public interface Substitution {

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$Substitution$Factory.class */
        public interface Factory {
            Substitution make(TypeDescription typeDescription, MethodDescription methodDescription, TypePool typePool);
        }

        StackManipulation resolve(TypeDescription typeDescription, ByteCodeElement byteCodeElement, TypeList.Generic generic, TypeDescription.Generic generic2, int i);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$Substitution$Stubbing.class */
        public enum Stubbing implements Substitution, Factory {
            INSTANCE;

            @Override // net.bytebuddy.asm.MemberSubstitution.Substitution.Factory
            public Substitution make(TypeDescription instrumentedType, MethodDescription instrumentedMethod, TypePool typePool) {
                return this;
            }

            @Override // net.bytebuddy.asm.MemberSubstitution.Substitution
            public StackManipulation resolve(TypeDescription targetType, ByteCodeElement target, TypeList.Generic parameters, TypeDescription.Generic result, int freeOffset) {
                List<StackManipulation> stackManipulations = new ArrayList<>(parameters.size());
                for (int index = parameters.size() - 1; index >= 0; index--) {
                    stackManipulations.add(Removal.of((TypeDefinition) parameters.get(index)));
                }
                return new StackManipulation.Compound(CompoundList.of(stackManipulations, DefaultValue.of(result.asErasure())));
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$Substitution$ForFieldAccess.class */
        public static class ForFieldAccess implements Substitution {
            private final TypeDescription instrumentedType;
            private final FieldResolver fieldResolver;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.instrumentedType.equals(((ForFieldAccess) obj).instrumentedType) && this.fieldResolver.equals(((ForFieldAccess) obj).fieldResolver);
            }

            public int hashCode() {
                return (((17 * 31) + this.instrumentedType.hashCode()) * 31) + this.fieldResolver.hashCode();
            }

            public ForFieldAccess(TypeDescription instrumentedType, FieldResolver fieldResolver) {
                this.instrumentedType = instrumentedType;
                this.fieldResolver = fieldResolver;
            }

            @Override // net.bytebuddy.asm.MemberSubstitution.Substitution
            public StackManipulation resolve(TypeDescription targetType, ByteCodeElement target, TypeList.Generic parameters, TypeDescription.Generic result, int freeOffset) {
                FieldDescription fieldDescription = this.fieldResolver.resolve(targetType, target, parameters, result);
                if (!fieldDescription.isAccessibleTo(this.instrumentedType)) {
                    throw new IllegalStateException(this.instrumentedType + " cannot access " + fieldDescription);
                }
                if (result.represents(Void.TYPE)) {
                    if (parameters.size() != (fieldDescription.isStatic() ? 1 : 2)) {
                        throw new IllegalStateException("Cannot set " + fieldDescription + " with " + parameters);
                    }
                    if (!fieldDescription.isStatic() && !((TypeDescription.Generic) parameters.get(0)).asErasure().isAssignableTo(fieldDescription.getDeclaringType().asErasure())) {
                        throw new IllegalStateException("Cannot set " + fieldDescription + " on " + parameters.get(0));
                    }
                    if (!((TypeDescription.Generic) parameters.get(fieldDescription.isStatic() ? 0 : 1)).asErasure().isAssignableTo(fieldDescription.getType().asErasure())) {
                        throw new IllegalStateException("Cannot set " + fieldDescription + " to " + parameters.get(fieldDescription.isStatic() ? 0 : 1));
                    }
                    return FieldAccess.forField(fieldDescription).write();
                }
                if (parameters.size() != (fieldDescription.isStatic() ? 0 : 1)) {
                    throw new IllegalStateException("Cannot set " + fieldDescription + " with " + parameters);
                }
                if (!fieldDescription.isStatic() && !((TypeDescription.Generic) parameters.get(0)).asErasure().isAssignableTo(fieldDescription.getDeclaringType().asErasure())) {
                    throw new IllegalStateException("Cannot get " + fieldDescription + " on " + parameters.get(0));
                }
                if (!fieldDescription.getType().asErasure().isAssignableTo(result.asErasure())) {
                    throw new IllegalStateException("Cannot get " + fieldDescription + " as " + result);
                }
                return FieldAccess.forField(fieldDescription).read();
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$Substitution$ForFieldAccess$FieldResolver.class */
            public interface FieldResolver {
                FieldDescription resolve(TypeDescription typeDescription, ByteCodeElement byteCodeElement, TypeList.Generic generic, TypeDescription.Generic generic2);

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$Substitution$ForFieldAccess$FieldResolver$Simple.class */
                public static class Simple implements FieldResolver {
                    private final FieldDescription fieldDescription;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.fieldDescription.equals(((Simple) obj).fieldDescription);
                    }

                    public int hashCode() {
                        return (17 * 31) + this.fieldDescription.hashCode();
                    }

                    public Simple(FieldDescription fieldDescription) {
                        this.fieldDescription = fieldDescription;
                    }

                    @Override // net.bytebuddy.asm.MemberSubstitution.Substitution.ForFieldAccess.FieldResolver
                    public FieldDescription resolve(TypeDescription targetType, ByteCodeElement target, TypeList.Generic parameters, TypeDescription.Generic result) {
                        return this.fieldDescription;
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$Substitution$ForFieldAccess$FieldResolver$ForElementMatcher.class */
                public static class ForElementMatcher implements FieldResolver {
                    private final TypeDescription instrumentedType;
                    private final ElementMatcher<? super FieldDescription> matcher;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.instrumentedType.equals(((ForElementMatcher) obj).instrumentedType) && this.matcher.equals(((ForElementMatcher) obj).matcher);
                    }

                    public int hashCode() {
                        return (((17 * 31) + this.instrumentedType.hashCode()) * 31) + this.matcher.hashCode();
                    }

                    protected ForElementMatcher(TypeDescription instrumentedType, ElementMatcher<? super FieldDescription> matcher) {
                        this.instrumentedType = instrumentedType;
                        this.matcher = matcher;
                    }

                    @Override // net.bytebuddy.asm.MemberSubstitution.Substitution.ForFieldAccess.FieldResolver
                    public FieldDescription resolve(TypeDescription targetType, ByteCodeElement target, TypeList.Generic parameters, TypeDescription.Generic result) {
                        TypeDefinition current;
                        if (parameters.isEmpty()) {
                            throw new IllegalStateException("Cannot substitute parameterless instruction with " + target);
                        }
                        if (((TypeDescription.Generic) parameters.get(0)).isPrimitive() || ((TypeDescription.Generic) parameters.get(0)).isArray()) {
                            throw new IllegalStateException("Cannot access field on primitive or array type for " + target);
                        }
                        TypeDefinition current2 = (TypeDefinition) parameters.get(0);
                        do {
                            FieldList fields = current2.getDeclaredFields().filter(ElementMatchers.not(ElementMatchers.isStatic()).and(ElementMatchers.isVisibleTo(this.instrumentedType)).and(this.matcher));
                            if (fields.size() == 1) {
                                return (FieldDescription) fields.getOnly();
                            }
                            if (fields.size() > 1) {
                                throw new IllegalStateException("Ambiguous field location of " + fields);
                            }
                            current = current2.getSuperClass();
                            current2 = current;
                        } while (current != null);
                        throw new IllegalStateException("Cannot locate field matching " + this.matcher + " on " + targetType);
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$Substitution$ForFieldAccess$OfGivenField.class */
            public static class OfGivenField implements Factory {
                private final FieldDescription fieldDescription;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.fieldDescription.equals(((OfGivenField) obj).fieldDescription);
                }

                public int hashCode() {
                    return (17 * 31) + this.fieldDescription.hashCode();
                }

                public OfGivenField(FieldDescription fieldDescription) {
                    this.fieldDescription = fieldDescription;
                }

                @Override // net.bytebuddy.asm.MemberSubstitution.Substitution.Factory
                public Substitution make(TypeDescription instrumentedType, MethodDescription instrumentedMethod, TypePool typePool) {
                    return new ForFieldAccess(instrumentedType, new FieldResolver.Simple(this.fieldDescription));
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$Substitution$ForFieldAccess$OfMatchedField.class */
            public static class OfMatchedField implements Factory {
                private final ElementMatcher<? super FieldDescription> matcher;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.matcher.equals(((OfMatchedField) obj).matcher);
                }

                public int hashCode() {
                    return (17 * 31) + this.matcher.hashCode();
                }

                public OfMatchedField(ElementMatcher<? super FieldDescription> matcher) {
                    this.matcher = matcher;
                }

                @Override // net.bytebuddy.asm.MemberSubstitution.Substitution.Factory
                public Substitution make(TypeDescription instrumentedType, MethodDescription instrumentedMethod, TypePool typePool) {
                    return new ForFieldAccess(instrumentedType, new FieldResolver.ForElementMatcher(instrumentedType, this.matcher));
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$Substitution$ForMethodInvocation.class */
        public static class ForMethodInvocation implements Substitution {
            private static final int THIS_REFERENCE = 0;
            private final TypeDescription instrumentedType;
            private final MethodResolver methodResolver;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.instrumentedType.equals(((ForMethodInvocation) obj).instrumentedType) && this.methodResolver.equals(((ForMethodInvocation) obj).methodResolver);
            }

            public int hashCode() {
                return (((17 * 31) + this.instrumentedType.hashCode()) * 31) + this.methodResolver.hashCode();
            }

            public ForMethodInvocation(TypeDescription instrumentedType, MethodResolver methodResolver) {
                this.instrumentedType = instrumentedType;
                this.methodResolver = methodResolver;
            }

            @Override // net.bytebuddy.asm.MemberSubstitution.Substitution
            public StackManipulation resolve(TypeDescription targetType, ByteCodeElement target, TypeList.Generic parameters, TypeDescription.Generic result, int freeOffset) {
                TypeList.Generic explicit;
                MethodDescription methodDescription = this.methodResolver.resolve(targetType, target, parameters, result);
                if (!methodDescription.isAccessibleTo(this.instrumentedType)) {
                    throw new IllegalStateException(this.instrumentedType + " cannot access " + methodDescription);
                }
                if (methodDescription.isStatic()) {
                    explicit = methodDescription.getParameters().asTypeList();
                } else {
                    explicit = new TypeList.Generic.Explicit(CompoundList.of(methodDescription.getDeclaringType(), methodDescription.getParameters().asTypeList()));
                }
                TypeList.Generic mapped = explicit;
                if (!methodDescription.getReturnType().asErasure().isAssignableTo(result.asErasure())) {
                    throw new IllegalStateException("Cannot assign return value of " + methodDescription + " to " + result);
                }
                if (mapped.size() != parameters.size()) {
                    throw new IllegalStateException("Cannot invoke " + methodDescription + " on " + parameters.size() + " parameters");
                }
                for (int index = 0; index < mapped.size(); index++) {
                    if (!((TypeDescription.Generic) parameters.get(index)).asErasure().isAssignableTo(((TypeDescription.Generic) mapped.get(index)).asErasure())) {
                        throw new IllegalStateException("Cannot invoke " + methodDescription + " on parameter " + index + " of type " + parameters.get(index));
                    }
                }
                if (methodDescription.isVirtual()) {
                    return MethodInvocation.invoke(methodDescription).virtual(((TypeDescription.Generic) mapped.get(0)).asErasure());
                }
                return MethodInvocation.invoke(methodDescription);
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$Substitution$ForMethodInvocation$MethodResolver.class */
            public interface MethodResolver {
                MethodDescription resolve(TypeDescription typeDescription, ByteCodeElement byteCodeElement, TypeList.Generic generic, TypeDescription.Generic generic2);

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$Substitution$ForMethodInvocation$MethodResolver$Simple.class */
                public static class Simple implements MethodResolver {
                    private final MethodDescription methodDescription;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.methodDescription.equals(((Simple) obj).methodDescription);
                    }

                    public int hashCode() {
                        return (17 * 31) + this.methodDescription.hashCode();
                    }

                    public Simple(MethodDescription methodDescription) {
                        this.methodDescription = methodDescription;
                    }

                    @Override // net.bytebuddy.asm.MemberSubstitution.Substitution.ForMethodInvocation.MethodResolver
                    public MethodDescription resolve(TypeDescription targetType, ByteCodeElement target, TypeList.Generic parameters, TypeDescription.Generic result) {
                        return this.methodDescription;
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$Substitution$ForMethodInvocation$MethodResolver$Matching.class */
                public static class Matching implements MethodResolver {
                    private final TypeDescription instrumentedType;
                    private final MethodGraph.Compiler methodGraphCompiler;
                    private final ElementMatcher<? super MethodDescription> matcher;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.instrumentedType.equals(((Matching) obj).instrumentedType) && this.methodGraphCompiler.equals(((Matching) obj).methodGraphCompiler) && this.matcher.equals(((Matching) obj).matcher);
                    }

                    public int hashCode() {
                        return (((((17 * 31) + this.instrumentedType.hashCode()) * 31) + this.methodGraphCompiler.hashCode()) * 31) + this.matcher.hashCode();
                    }

                    public Matching(TypeDescription instrumentedType, MethodGraph.Compiler methodGraphCompiler, ElementMatcher<? super MethodDescription> matcher) {
                        this.instrumentedType = instrumentedType;
                        this.methodGraphCompiler = methodGraphCompiler;
                        this.matcher = matcher;
                    }

                    @Override // net.bytebuddy.asm.MemberSubstitution.Substitution.ForMethodInvocation.MethodResolver
                    public MethodDescription resolve(TypeDescription targetType, ByteCodeElement target, TypeList.Generic parameters, TypeDescription.Generic result) {
                        if (parameters.isEmpty()) {
                            throw new IllegalStateException("Cannot substitute parameterless instruction with " + target);
                        }
                        if (((TypeDescription.Generic) parameters.get(0)).isPrimitive() || ((TypeDescription.Generic) parameters.get(0)).isArray()) {
                            throw new IllegalStateException("Cannot invoke method on primitive or array type for " + target);
                        }
                        TypeDefinition typeDefinition = (TypeDefinition) parameters.get(0);
                        List<MethodDescription> candidates = CompoundList.of(this.methodGraphCompiler.compile(typeDefinition, this.instrumentedType).listNodes().asMethodList().filter(this.matcher), typeDefinition.getDeclaredMethods().filter(ElementMatchers.isPrivate().and(ElementMatchers.isVisibleTo(this.instrumentedType)).and(this.matcher)));
                        if (candidates.size() == 1) {
                            return candidates.get(0);
                        }
                        throw new IllegalStateException("Not exactly one method that matches " + this.matcher + ": " + candidates);
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$Substitution$ForMethodInvocation$OfInstrumentedMethod.class */
            enum OfInstrumentedMethod implements Factory {
                INSTANCE;

                @Override // net.bytebuddy.asm.MemberSubstitution.Substitution.Factory
                public Substitution make(TypeDescription instrumentedType, MethodDescription instrumentedMethod, TypePool typePool) {
                    return new ForMethodInvocation(instrumentedType, new MethodResolver.Simple(instrumentedMethod));
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$Substitution$ForMethodInvocation$OfGivenMethod.class */
            public static class OfGivenMethod implements Factory {
                private final MethodDescription methodDescription;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.methodDescription.equals(((OfGivenMethod) obj).methodDescription);
                }

                public int hashCode() {
                    return (17 * 31) + this.methodDescription.hashCode();
                }

                public OfGivenMethod(MethodDescription methodDescription) {
                    this.methodDescription = methodDescription;
                }

                @Override // net.bytebuddy.asm.MemberSubstitution.Substitution.Factory
                public Substitution make(TypeDescription instrumentedType, MethodDescription instrumentedMethod, TypePool typePool) {
                    return new ForMethodInvocation(instrumentedType, new MethodResolver.Simple(this.methodDescription));
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$Substitution$ForMethodInvocation$OfMatchedMethod.class */
            public static class OfMatchedMethod implements Factory {
                private final ElementMatcher<? super MethodDescription> matcher;
                private final MethodGraph.Compiler methodGraphCompiler;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.matcher.equals(((OfMatchedMethod) obj).matcher) && this.methodGraphCompiler.equals(((OfMatchedMethod) obj).methodGraphCompiler);
                }

                public int hashCode() {
                    return (((17 * 31) + this.matcher.hashCode()) * 31) + this.methodGraphCompiler.hashCode();
                }

                public OfMatchedMethod(ElementMatcher<? super MethodDescription> matcher, MethodGraph.Compiler methodGraphCompiler) {
                    this.matcher = matcher;
                    this.methodGraphCompiler = methodGraphCompiler;
                }

                @Override // net.bytebuddy.asm.MemberSubstitution.Substitution.Factory
                public Substitution make(TypeDescription instrumentedType, MethodDescription instrumentedMethod, TypePool typePool) {
                    return new ForMethodInvocation(instrumentedType, new MethodResolver.Matching(instrumentedType, this.methodGraphCompiler, this.matcher));
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$Substitution$Chain.class */
        public static class Chain implements Substitution {
            private final Assigner assigner;
            private final Assigner.Typing typing;
            private final List<Step> steps;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.typing.equals(((Chain) obj).typing) && this.assigner.equals(((Chain) obj).assigner) && this.steps.equals(((Chain) obj).steps);
            }

            public int hashCode() {
                return (((((17 * 31) + this.assigner.hashCode()) * 31) + this.typing.hashCode()) * 31) + this.steps.hashCode();
            }

            protected Chain(Assigner assigner, Assigner.Typing typing, List<Step> steps) {
                this.assigner = assigner;
                this.typing = typing;
                this.steps = steps;
            }

            public static Factory withDefaultAssigner() {
                return with(Assigner.DEFAULT, Assigner.Typing.STATIC);
            }

            public static Factory with(Assigner assigner, Assigner.Typing typing) {
                return new Factory(assigner, typing, Collections.emptyList());
            }

            @Override // net.bytebuddy.asm.MemberSubstitution.Substitution
            public StackManipulation resolve(TypeDescription targetType, ByteCodeElement target, TypeList.Generic parameters, TypeDescription.Generic result, int freeOffset) {
                List<StackManipulation> stackManipulations = new ArrayList<>(1 + parameters.size() + (this.steps.size() * 2) + (result.represents(Void.TYPE) ? 0 : 2));
                Map<Integer, Integer> offsets = new HashMap<>();
                for (int index = parameters.size() - 1; index >= 0; index--) {
                    stackManipulations.add(MethodVariableAccess.of((TypeDefinition) parameters.get(index)).storeAt(freeOffset));
                    offsets.put(Integer.valueOf(index), Integer.valueOf(freeOffset));
                    freeOffset += ((TypeDescription.Generic) parameters.get(index)).getStackSize().getSize();
                }
                stackManipulations.add(DefaultValue.of(result));
                TypeDescription.Generic current = result;
                for (Step step : this.steps) {
                    Step.Resolution resulution = step.resolve(targetType, target, parameters, current, offsets, freeOffset);
                    stackManipulations.add(resulution.getStackManipulation());
                    current = resulution.getResultType();
                }
                stackManipulations.add(this.assigner.assign(current, result, this.typing));
                return new StackManipulation.Compound(stackManipulations);
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$Substitution$Chain$Step.class */
            protected interface Step {

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$Substitution$Chain$Step$Factory.class */
                public interface Factory {
                    Step make(Assigner assigner, Assigner.Typing typing, TypeDescription typeDescription, MethodDescription methodDescription);
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$Substitution$Chain$Step$Resolution.class */
                public interface Resolution {
                    StackManipulation getStackManipulation();

                    TypeDescription.Generic getResultType();
                }

                Resolution resolve(TypeDescription typeDescription, ByteCodeElement byteCodeElement, TypeList.Generic generic, TypeDescription.Generic generic2, Map<Integer, Integer> map, int i);

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$Substitution$Chain$Step$Simple.class */
                public static class Simple implements Step, Resolution, Factory {
                    private final StackManipulation stackManipulation;
                    private final TypeDescription.Generic resultType;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.stackManipulation.equals(((Simple) obj).stackManipulation) && this.resultType.equals(((Simple) obj).resultType);
                    }

                    public int hashCode() {
                        return (((17 * 31) + this.stackManipulation.hashCode()) * 31) + this.resultType.hashCode();
                    }

                    public Simple(StackManipulation stackManipulation, TypeDescription.Generic resultType) {
                        this.stackManipulation = stackManipulation;
                        this.resultType = resultType;
                    }

                    @Override // net.bytebuddy.asm.MemberSubstitution.Substitution.Chain.Step.Factory
                    public Step make(Assigner assigner, Assigner.Typing typing, TypeDescription instrumentedType, MethodDescription instrumentedMethod) {
                        return this;
                    }

                    @Override // net.bytebuddy.asm.MemberSubstitution.Substitution.Chain.Step
                    public Resolution resolve(TypeDescription targetType, ByteCodeElement target, TypeList.Generic parameters, TypeDescription.Generic current, Map<Integer, Integer> offsets, int freeOffset) {
                        return targetType.represents(Void.TYPE) ? this : new Simple(new StackManipulation.Compound(Removal.of(targetType), this.stackManipulation), this.resultType);
                    }

                    @Override // net.bytebuddy.asm.MemberSubstitution.Substitution.Chain.Step.Resolution
                    public StackManipulation getStackManipulation() {
                        return this.stackManipulation;
                    }

                    @Override // net.bytebuddy.asm.MemberSubstitution.Substitution.Chain.Step.Resolution
                    public TypeDescription.Generic getResultType() {
                        return this.resultType;
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$Substitution$Chain$Factory.class */
            public static class Factory implements Factory {
                private final Assigner assigner;
                private final Assigner.Typing typing;
                private final List<Step.Factory> steps;

                protected Factory(Assigner assigner, Assigner.Typing typing, List<Step.Factory> steps) {
                    this.assigner = assigner;
                    this.typing = typing;
                    this.steps = steps;
                }

                @Override // net.bytebuddy.asm.MemberSubstitution.Substitution.Factory
                public Substitution make(TypeDescription instrumentedType, MethodDescription instrumentedMethod, TypePool typePool) {
                    if (this.steps.isEmpty()) {
                        return Stubbing.INSTANCE;
                    }
                    List<Step> steps = new ArrayList<>(this.steps.size());
                    for (Step.Factory step : this.steps) {
                        steps.add(step.make(this.assigner, this.typing, instrumentedType, instrumentedMethod));
                    }
                    return new Chain(this.assigner, this.typing, steps);
                }

                public Factory executing(Step.Factory... step) {
                    return executing(Arrays.asList(step));
                }

                public Factory executing(List<? extends Step.Factory> steps) {
                    return new Factory(this.assigner, this.typing, CompoundList.of((List) this.steps, (List) steps));
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$Replacement.class */
    protected interface Replacement {
        Binding bind(TypeDescription typeDescription, MethodDescription methodDescription, FieldDescription.InDefinedShape inDefinedShape, boolean z);

        Binding bind(TypeDescription typeDescription, MethodDescription methodDescription, TypeDescription typeDescription2, MethodDescription methodDescription2, InvocationType invocationType);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$Replacement$Binding.class */
        public interface Binding {
            boolean isBound();

            StackManipulation make(TypeList.Generic generic, TypeDescription.Generic generic2, int i);

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$Replacement$Binding$Unresolved.class */
            public enum Unresolved implements Binding {
                INSTANCE;

                @Override // net.bytebuddy.asm.MemberSubstitution.Replacement.Binding
                public boolean isBound() {
                    return false;
                }

                @Override // net.bytebuddy.asm.MemberSubstitution.Replacement.Binding
                public StackManipulation make(TypeList.Generic parameters, TypeDescription.Generic result, int freeOffset) {
                    throw new IllegalStateException("Cannot resolve unresolved binding");
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$Replacement$Binding$Resolved.class */
            public static class Resolved implements Binding {
                private final TypeDescription instrumentedType;
                private final MethodDescription instrumentedMethod;
                private final TypeDescription targetType;
                private final ByteCodeElement target;
                private final Substitution substitution;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.instrumentedType.equals(((Resolved) obj).instrumentedType) && this.instrumentedMethod.equals(((Resolved) obj).instrumentedMethod) && this.targetType.equals(((Resolved) obj).targetType) && this.target.equals(((Resolved) obj).target) && this.substitution.equals(((Resolved) obj).substitution);
                }

                public int hashCode() {
                    return (((((((((17 * 31) + this.instrumentedType.hashCode()) * 31) + this.instrumentedMethod.hashCode()) * 31) + this.targetType.hashCode()) * 31) + this.target.hashCode()) * 31) + this.substitution.hashCode();
                }

                protected Resolved(TypeDescription instrumentedType, MethodDescription instrumentedMethod, TypeDescription targetType, ByteCodeElement target, Substitution substitution) {
                    this.instrumentedType = instrumentedType;
                    this.instrumentedMethod = instrumentedMethod;
                    this.targetType = targetType;
                    this.target = target;
                    this.substitution = substitution;
                }

                @Override // net.bytebuddy.asm.MemberSubstitution.Replacement.Binding
                public boolean isBound() {
                    return true;
                }

                @Override // net.bytebuddy.asm.MemberSubstitution.Replacement.Binding
                public StackManipulation make(TypeList.Generic parameters, TypeDescription.Generic result, int freeOffset) {
                    return this.substitution.resolve(this.targetType, this.target, parameters, result, freeOffset);
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$Replacement$Factory.class */
        public interface Factory {
            Replacement make(TypeDescription typeDescription, MethodDescription methodDescription, TypePool typePool);

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$Replacement$Factory$Compound.class */
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

                protected Compound(Factory... factory) {
                    this(Arrays.asList(factory));
                }

                protected Compound(List<? extends Factory> factories) {
                    this.factories = new ArrayList();
                    for (Factory factory : factories) {
                        if (factory instanceof Compound) {
                            this.factories.addAll(((Compound) factory).factories);
                        } else if (!(factory instanceof NoOp)) {
                            this.factories.add(factory);
                        }
                    }
                }

                @Override // net.bytebuddy.asm.MemberSubstitution.Replacement.Factory
                public Replacement make(TypeDescription instrumentedType, MethodDescription instrumentedMethod, TypePool typePool) {
                    List<Replacement> replacements = new ArrayList<>();
                    for (Factory factory : this.factories) {
                        replacements.add(factory.make(instrumentedType, instrumentedMethod, typePool));
                    }
                    return new ForFirstBinding(replacements);
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$Replacement$InvocationType.class */
        public enum InvocationType {
            VIRTUAL,
            SUPER,
            OTHER;

            protected static InvocationType of(int opcode, MethodDescription methodDescription) {
                switch (opcode) {
                    case 182:
                    case 185:
                        return VIRTUAL;
                    case 183:
                        return methodDescription.isVirtual() ? SUPER : OTHER;
                    case 184:
                    default:
                        return OTHER;
                }
            }

            protected boolean matches(boolean includeVirtualCalls, boolean includeSuperCalls) {
                switch (this) {
                    case VIRTUAL:
                        return includeVirtualCalls;
                    case SUPER:
                        return includeSuperCalls;
                    default:
                        return true;
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$Replacement$NoOp.class */
        public enum NoOp implements Replacement, Factory {
            INSTANCE;

            @Override // net.bytebuddy.asm.MemberSubstitution.Replacement.Factory
            public Replacement make(TypeDescription instrumentedType, MethodDescription instrumentedMethod, TypePool typePool) {
                return this;
            }

            @Override // net.bytebuddy.asm.MemberSubstitution.Replacement
            public Binding bind(TypeDescription instrumentedType, MethodDescription instrumentedMethod, FieldDescription.InDefinedShape fieldDescription, boolean writeAccess) {
                return Binding.Unresolved.INSTANCE;
            }

            @Override // net.bytebuddy.asm.MemberSubstitution.Replacement
            public Binding bind(TypeDescription instrumentedType, MethodDescription instrumentedMethod, TypeDescription typeDescription, MethodDescription methodDescription, InvocationType invocationType) {
                return Binding.Unresolved.INSTANCE;
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$Replacement$ForElementMatchers.class */
        public static class ForElementMatchers implements Replacement {
            private final ElementMatcher<? super FieldDescription.InDefinedShape> fieldMatcher;
            private final ElementMatcher<? super MethodDescription> methodMatcher;
            private final boolean matchFieldRead;
            private final boolean matchFieldWrite;
            private final boolean includeVirtualCalls;
            private final boolean includeSuperCalls;
            private final Substitution substitution;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.matchFieldRead == ((ForElementMatchers) obj).matchFieldRead && this.matchFieldWrite == ((ForElementMatchers) obj).matchFieldWrite && this.includeVirtualCalls == ((ForElementMatchers) obj).includeVirtualCalls && this.includeSuperCalls == ((ForElementMatchers) obj).includeSuperCalls && this.fieldMatcher.equals(((ForElementMatchers) obj).fieldMatcher) && this.methodMatcher.equals(((ForElementMatchers) obj).methodMatcher) && this.substitution.equals(((ForElementMatchers) obj).substitution);
            }

            public int hashCode() {
                return (((((((((((((17 * 31) + this.fieldMatcher.hashCode()) * 31) + this.methodMatcher.hashCode()) * 31) + (this.matchFieldRead ? 1 : 0)) * 31) + (this.matchFieldWrite ? 1 : 0)) * 31) + (this.includeVirtualCalls ? 1 : 0)) * 31) + (this.includeSuperCalls ? 1 : 0)) * 31) + this.substitution.hashCode();
            }

            protected ForElementMatchers(ElementMatcher<? super FieldDescription.InDefinedShape> fieldMatcher, ElementMatcher<? super MethodDescription> methodMatcher, boolean matchFieldRead, boolean matchFieldWrite, boolean includeVirtualCalls, boolean includeSuperCalls, Substitution substitution) {
                this.fieldMatcher = fieldMatcher;
                this.methodMatcher = methodMatcher;
                this.matchFieldRead = matchFieldRead;
                this.matchFieldWrite = matchFieldWrite;
                this.includeVirtualCalls = includeVirtualCalls;
                this.includeSuperCalls = includeSuperCalls;
                this.substitution = substitution;
            }

            @Override // net.bytebuddy.asm.MemberSubstitution.Replacement
            public Binding bind(TypeDescription instrumentedType, MethodDescription instrumentedMethod, FieldDescription.InDefinedShape fieldDescription, boolean writeAccess) {
                if (!writeAccess ? this.matchFieldRead : this.matchFieldWrite) {
                    if (this.fieldMatcher.matches(fieldDescription)) {
                        return new Binding.Resolved(instrumentedType, instrumentedMethod, fieldDescription.getDeclaringType(), fieldDescription, this.substitution);
                    }
                }
                return Binding.Unresolved.INSTANCE;
            }

            @Override // net.bytebuddy.asm.MemberSubstitution.Replacement
            public Binding bind(TypeDescription instrumentedType, MethodDescription instrumentedMethod, TypeDescription typeDescription, MethodDescription methodDescription, InvocationType invocationType) {
                return (invocationType.matches(this.includeVirtualCalls, this.includeSuperCalls) && this.methodMatcher.matches(methodDescription)) ? new Binding.Resolved(instrumentedType, instrumentedMethod, typeDescription, methodDescription, this.substitution) : Binding.Unresolved.INSTANCE;
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$Replacement$ForElementMatchers$Factory.class */
            protected static class Factory implements Factory {
                private final ElementMatcher<? super FieldDescription.InDefinedShape> fieldMatcher;
                private final ElementMatcher<? super MethodDescription> methodMatcher;
                private final boolean matchFieldRead;
                private final boolean matchFieldWrite;
                private final boolean includeVirtualCalls;
                private final boolean includeSuperCalls;
                private final Substitution.Factory substitutionFactory;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.matchFieldRead == ((Factory) obj).matchFieldRead && this.matchFieldWrite == ((Factory) obj).matchFieldWrite && this.includeVirtualCalls == ((Factory) obj).includeVirtualCalls && this.includeSuperCalls == ((Factory) obj).includeSuperCalls && this.fieldMatcher.equals(((Factory) obj).fieldMatcher) && this.methodMatcher.equals(((Factory) obj).methodMatcher) && this.substitutionFactory.equals(((Factory) obj).substitutionFactory);
                }

                public int hashCode() {
                    return (((((((((((((17 * 31) + this.fieldMatcher.hashCode()) * 31) + this.methodMatcher.hashCode()) * 31) + (this.matchFieldRead ? 1 : 0)) * 31) + (this.matchFieldWrite ? 1 : 0)) * 31) + (this.includeVirtualCalls ? 1 : 0)) * 31) + (this.includeSuperCalls ? 1 : 0)) * 31) + this.substitutionFactory.hashCode();
                }

                protected Factory(ElementMatcher<? super FieldDescription.InDefinedShape> fieldMatcher, ElementMatcher<? super MethodDescription> methodMatcher, boolean matchFieldRead, boolean matchFieldWrite, boolean includeVirtualCalls, boolean includeSuperCalls, Substitution.Factory substitutionFactory) {
                    this.fieldMatcher = fieldMatcher;
                    this.methodMatcher = methodMatcher;
                    this.matchFieldRead = matchFieldRead;
                    this.matchFieldWrite = matchFieldWrite;
                    this.includeVirtualCalls = includeVirtualCalls;
                    this.includeSuperCalls = includeSuperCalls;
                    this.substitutionFactory = substitutionFactory;
                }

                protected static Factory of(ElementMatcher<? super ByteCodeElement> matcher, Substitution.Factory factory) {
                    return new Factory(matcher, matcher, true, true, true, true, factory);
                }

                protected static Factory ofField(ElementMatcher<? super FieldDescription.InDefinedShape> matcher, boolean matchFieldRead, boolean matchFieldWrite, Substitution.Factory factory) {
                    return new Factory(matcher, ElementMatchers.none(), matchFieldRead, matchFieldWrite, false, false, factory);
                }

                protected static Factory ofMethod(ElementMatcher<? super MethodDescription> matcher, boolean includeVirtualCalls, boolean includeSuperCalls, Substitution.Factory factory) {
                    return new Factory(ElementMatchers.none(), matcher, false, false, includeVirtualCalls, includeSuperCalls, factory);
                }

                @Override // net.bytebuddy.asm.MemberSubstitution.Replacement.Factory
                public Replacement make(TypeDescription instrumentedType, MethodDescription instrumentedMethod, TypePool typePool) {
                    return new ForElementMatchers(this.fieldMatcher, this.methodMatcher, this.matchFieldRead, this.matchFieldWrite, this.includeVirtualCalls, this.includeSuperCalls, this.substitutionFactory.make(instrumentedType, instrumentedMethod, typePool));
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$Replacement$ForFirstBinding.class */
        public static class ForFirstBinding implements Replacement {
            private final List<? extends Replacement> replacements;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.replacements.equals(((ForFirstBinding) obj).replacements);
            }

            public int hashCode() {
                return (17 * 31) + this.replacements.hashCode();
            }

            protected ForFirstBinding(List<? extends Replacement> replacements) {
                this.replacements = replacements;
            }

            @Override // net.bytebuddy.asm.MemberSubstitution.Replacement
            public Binding bind(TypeDescription instrumentedType, MethodDescription instrumentedMethod, FieldDescription.InDefinedShape fieldDescription, boolean writeAccess) {
                for (Replacement replacement : this.replacements) {
                    Binding binding = replacement.bind(instrumentedType, instrumentedMethod, fieldDescription, writeAccess);
                    if (binding.isBound()) {
                        return binding;
                    }
                }
                return Binding.Unresolved.INSTANCE;
            }

            @Override // net.bytebuddy.asm.MemberSubstitution.Replacement
            public Binding bind(TypeDescription instrumentedType, MethodDescription instrumentedMethod, TypeDescription typeDescription, MethodDescription methodDescription, InvocationType invocationType) {
                for (Replacement replacement : this.replacements) {
                    Binding binding = replacement.bind(instrumentedType, instrumentedMethod, typeDescription, methodDescription, invocationType);
                    if (binding.isBound()) {
                        return binding;
                    }
                }
                return Binding.Unresolved.INSTANCE;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$SubstitutingMethodVisitor.class */
    public static class SubstitutingMethodVisitor extends LocalVariableAwareMethodVisitor {
        private final TypeDescription instrumentedType;
        private final MethodDescription instrumentedMethod;
        private final MethodGraph.Compiler methodGraphCompiler;
        private final boolean strict;
        private final Replacement replacement;
        private final Implementation.Context implementationContext;
        private final TypePool typePool;
        private final boolean virtualPrivateCalls;
        private int stackSizeBuffer;
        private int localVariableExtension;

        protected SubstitutingMethodVisitor(MethodVisitor methodVisitor, TypeDescription instrumentedType, MethodDescription instrumentedMethod, MethodGraph.Compiler methodGraphCompiler, boolean strict, Replacement replacement, Implementation.Context implementationContext, TypePool typePool, boolean virtualPrivateCalls) {
            super(methodVisitor, instrumentedMethod);
            this.instrumentedType = instrumentedType;
            this.instrumentedMethod = instrumentedMethod;
            this.methodGraphCompiler = methodGraphCompiler;
            this.strict = strict;
            this.replacement = replacement;
            this.implementationContext = implementationContext;
            this.typePool = typePool;
            this.virtualPrivateCalls = virtualPrivateCalls;
            this.stackSizeBuffer = 0;
            this.localVariableExtension = 0;
        }

        @Override // net.bytebuddy.jar.asm.MethodVisitor
        public void visitFieldInsn(int opcode, String owner, String internalName, String descriptor) {
            ElementMatcher.Junction failSafe;
            TypeList.Generic parameters;
            TypeDescription.Generic result;
            TypePool.Resolution resolution = this.typePool.describe(owner.replace('/', '.'));
            if (resolution.isResolved()) {
                FieldList<FieldDescription.InDefinedShape> declaredFields = resolution.resolve().getDeclaredFields();
                if (this.strict) {
                    failSafe = ElementMatchers.named(internalName).and(ElementMatchers.hasDescriptor(descriptor));
                } else {
                    failSafe = ElementMatchers.failSafe(ElementMatchers.named(internalName).and(ElementMatchers.hasDescriptor(descriptor)));
                }
                FieldList candidates = declaredFields.filter(failSafe);
                if (!candidates.isEmpty()) {
                    Replacement.Binding binding = this.replacement.bind(this.instrumentedType, this.instrumentedMethod, (FieldDescription.InDefinedShape) candidates.getOnly(), opcode == 181 || opcode == 179);
                    if (binding.isBound()) {
                        switch (opcode) {
                            case 178:
                                parameters = new TypeList.Generic.Empty();
                                result = ((FieldDescription.InDefinedShape) candidates.getOnly()).getType();
                                break;
                            case 179:
                                parameters = new TypeList.Generic.Explicit(((FieldDescription.InDefinedShape) candidates.getOnly()).getType());
                                result = TypeDescription.Generic.VOID;
                                break;
                            case 180:
                                parameters = new TypeList.Generic.Explicit(((FieldDescription.InDefinedShape) candidates.getOnly()).getDeclaringType());
                                result = ((FieldDescription.InDefinedShape) candidates.getOnly()).getType();
                                break;
                            case 181:
                                parameters = new TypeList.Generic.Explicit(((FieldDescription.InDefinedShape) candidates.getOnly()).getDeclaringType(), ((FieldDescription.InDefinedShape) candidates.getOnly()).getType());
                                result = TypeDescription.Generic.VOID;
                                break;
                            default:
                                throw new IllegalStateException("Unexpected opcode: " + opcode);
                        }
                        this.stackSizeBuffer = Math.max(this.stackSizeBuffer, binding.make(parameters, result, getFreeOffset()).apply(new LocalVariableTracingMethodVisitor(this.mv), this.implementationContext).getMaximalSize() - result.getStackSize().getSize());
                        return;
                    }
                } else if (this.strict) {
                    throw new IllegalStateException("Could not resolve " + owner.replace('/', '.') + "." + internalName + descriptor + " using " + this.typePool);
                }
            } else if (this.strict) {
                throw new IllegalStateException("Could not resolve " + owner.replace('/', '.') + " using " + this.typePool);
            }
            super.visitFieldInsn(opcode, owner, internalName, descriptor);
        }

        @Override // net.bytebuddy.jar.asm.MethodVisitor
        public void visitMethodInsn(int opcode, String owner, String internalName, String descriptor, boolean isInterface) {
            ElementMatcher.Junction failSafe;
            MethodList<?> candidates;
            ElementMatcher.Junction failSafe2;
            ElementMatcher.Junction failSafe3;
            ElementMatcher.Junction failSafe4;
            TypeList.Generic asTypeList;
            TypeDescription.Generic returnType;
            ElementMatcher.Junction failSafe5;
            TypePool.Resolution resolution = this.typePool.describe(owner.replace('/', '.'));
            if (resolution.isResolved()) {
                if (opcode == 183 && internalName.equals("<init>")) {
                    MethodList<MethodDescription.InDefinedShape> declaredMethods = resolution.resolve().getDeclaredMethods();
                    if (this.strict) {
                        failSafe5 = ElementMatchers.isConstructor().and(ElementMatchers.hasDescriptor(descriptor));
                    } else {
                        failSafe5 = ElementMatchers.failSafe(ElementMatchers.isConstructor().and(ElementMatchers.hasDescriptor(descriptor)));
                    }
                    candidates = declaredMethods.filter(failSafe5);
                } else if (opcode == 184 || opcode == 183) {
                    MethodList<MethodDescription.InDefinedShape> declaredMethods2 = resolution.resolve().getDeclaredMethods();
                    if (this.strict) {
                        failSafe = ElementMatchers.named(internalName).and(ElementMatchers.hasDescriptor(descriptor));
                    } else {
                        failSafe = ElementMatchers.failSafe(ElementMatchers.named(internalName).and(ElementMatchers.hasDescriptor(descriptor)));
                    }
                    candidates = declaredMethods2.filter(failSafe);
                } else if (this.virtualPrivateCalls) {
                    MethodList<MethodDescription.InDefinedShape> declaredMethods3 = resolution.resolve().getDeclaredMethods();
                    if (this.strict) {
                        failSafe3 = ElementMatchers.isPrivate().and(ElementMatchers.not(ElementMatchers.isStatic())).and(ElementMatchers.named(internalName).and(ElementMatchers.hasDescriptor(descriptor)));
                    } else {
                        failSafe3 = ElementMatchers.failSafe(ElementMatchers.isPrivate().and(ElementMatchers.not(ElementMatchers.isStatic())).and(ElementMatchers.named(internalName).and(ElementMatchers.hasDescriptor(descriptor))));
                    }
                    candidates = declaredMethods3.filter(failSafe3);
                    if (candidates.isEmpty()) {
                        MethodList<?> asMethodList = this.methodGraphCompiler.compile(resolution.resolve(), this.instrumentedType).listNodes().asMethodList();
                        if (this.strict) {
                            failSafe4 = ElementMatchers.named(internalName).and(ElementMatchers.hasDescriptor(descriptor));
                        } else {
                            failSafe4 = ElementMatchers.failSafe(ElementMatchers.named(internalName).and(ElementMatchers.hasDescriptor(descriptor)));
                        }
                        candidates = (MethodList) asMethodList.filter(failSafe4);
                    }
                } else {
                    MethodList<?> asMethodList2 = this.methodGraphCompiler.compile(resolution.resolve(), this.instrumentedType).listNodes().asMethodList();
                    if (this.strict) {
                        failSafe2 = ElementMatchers.named(internalName).and(ElementMatchers.hasDescriptor(descriptor));
                    } else {
                        failSafe2 = ElementMatchers.failSafe(ElementMatchers.named(internalName).and(ElementMatchers.hasDescriptor(descriptor)));
                    }
                    candidates = (MethodList) asMethodList2.filter(failSafe2);
                }
                if (!candidates.isEmpty()) {
                    Replacement.Binding binding = this.replacement.bind(this.instrumentedType, this.instrumentedMethod, resolution.resolve(), (MethodDescription) candidates.getOnly(), Replacement.InvocationType.of(opcode, (MethodDescription) candidates.getOnly()));
                    if (binding.isBound()) {
                        int i = this.stackSizeBuffer;
                        if (((MethodDescription) candidates.getOnly()).isStatic() || ((MethodDescription) candidates.getOnly()).isConstructor()) {
                            asTypeList = ((MethodDescription) candidates.getOnly()).getParameters().asTypeList();
                        } else {
                            asTypeList = new TypeList.Generic.Explicit(CompoundList.of(resolution.resolve(), ((MethodDescription) candidates.getOnly()).getParameters().asTypeList()));
                        }
                        if (((MethodDescription) candidates.getOnly()).isConstructor()) {
                            returnType = ((MethodDescription) candidates.getOnly()).getDeclaringType().asGenericType();
                        } else {
                            returnType = ((MethodDescription) candidates.getOnly()).getReturnType();
                        }
                        this.stackSizeBuffer = Math.max(i, binding.make(asTypeList, returnType, getFreeOffset()).apply(new LocalVariableTracingMethodVisitor(this.mv), this.implementationContext).getMaximalSize() - (((MethodDescription) candidates.getOnly()).isConstructor() ? StackSize.SINGLE : ((MethodDescription) candidates.getOnly()).getReturnType().getStackSize()).getSize());
                        if (((MethodDescription) candidates.getOnly()).isConstructor()) {
                            this.stackSizeBuffer = Math.max(this.stackSizeBuffer, new StackManipulation.Compound(Duplication.SINGLE.flipOver(TypeDescription.OBJECT), Removal.SINGLE, Removal.SINGLE, Duplication.SINGLE.flipOver(TypeDescription.OBJECT), Removal.SINGLE, Removal.SINGLE).apply(this.mv, this.implementationContext).getMaximalSize() + StackSize.SINGLE.getSize());
                            return;
                        }
                        return;
                    }
                } else if (this.strict) {
                    throw new IllegalStateException("Could not resolve " + owner.replace('/', '.') + "." + internalName + descriptor + " using " + this.typePool);
                }
            } else if (this.strict) {
                throw new IllegalStateException("Could not resolve " + owner.replace('/', '.') + " using " + this.typePool);
            }
            super.visitMethodInsn(opcode, owner, internalName, descriptor, isInterface);
        }

        @Override // net.bytebuddy.jar.asm.MethodVisitor
        public void visitMaxs(int stackSize, int localVariableLength) {
            super.visitMaxs(stackSize + this.stackSizeBuffer, Math.max(this.localVariableExtension, localVariableLength));
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/asm/MemberSubstitution$SubstitutingMethodVisitor$LocalVariableTracingMethodVisitor.class */
        private class LocalVariableTracingMethodVisitor extends MethodVisitor {
            private LocalVariableTracingMethodVisitor(MethodVisitor methodVisitor) {
                super(OpenedClassReader.ASM_API, methodVisitor);
            }

            @Override // net.bytebuddy.jar.asm.MethodVisitor
            @SuppressFBWarnings(value = {"SF_SWITCH_NO_DEFAULT"}, justification = "No action required on default option.")
            public void visitVarInsn(int opcode, int offset) {
                switch (opcode) {
                    case 54:
                    case 56:
                    case 58:
                        SubstitutingMethodVisitor.this.localVariableExtension = Math.max(SubstitutingMethodVisitor.this.localVariableExtension, offset + 1);
                        break;
                    case 55:
                    case 57:
                        SubstitutingMethodVisitor.this.localVariableExtension = Math.max(SubstitutingMethodVisitor.this.localVariableExtension, offset + 2);
                        break;
                }
                super.visitVarInsn(opcode, offset);
            }
        }
    }
}
