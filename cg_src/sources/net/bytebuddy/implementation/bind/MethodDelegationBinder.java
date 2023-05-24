package net.bytebuddy.implementation.bind;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.bind.annotation.BindingPriority;
import net.bytebuddy.implementation.bytecode.Removal;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import net.bytebuddy.implementation.bytecode.member.MethodInvocation;
import net.bytebuddy.implementation.bytecode.member.MethodReturn;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.utility.CompoundList;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/MethodDelegationBinder.class */
public interface MethodDelegationBinder {

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/MethodDelegationBinder$TerminationHandler.class */
    public interface TerminationHandler {

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/MethodDelegationBinder$TerminationHandler$Default.class */
        public enum Default implements TerminationHandler {
            RETURNING { // from class: net.bytebuddy.implementation.bind.MethodDelegationBinder.TerminationHandler.Default.1
                @Override // net.bytebuddy.implementation.bind.MethodDelegationBinder.TerminationHandler
                public StackManipulation resolve(Assigner assigner, Assigner.Typing typing, MethodDescription source, MethodDescription target) {
                    TypeDescription.Generic returnType;
                    StackManipulation[] stackManipulationArr = new StackManipulation[2];
                    if (target.isConstructor()) {
                        returnType = target.getDeclaringType().asGenericType();
                    } else {
                        returnType = target.getReturnType();
                    }
                    stackManipulationArr[0] = assigner.assign(returnType, source.getReturnType(), typing);
                    stackManipulationArr[1] = MethodReturn.of(source.getReturnType());
                    return new StackManipulation.Compound(stackManipulationArr);
                }
            },
            DROPPING { // from class: net.bytebuddy.implementation.bind.MethodDelegationBinder.TerminationHandler.Default.2
                @Override // net.bytebuddy.implementation.bind.MethodDelegationBinder.TerminationHandler
                public StackManipulation resolve(Assigner assigner, Assigner.Typing typing, MethodDescription source, MethodDescription target) {
                    TypeDefinition returnType;
                    if (target.isConstructor()) {
                        returnType = target.getDeclaringType();
                    } else {
                        returnType = target.getReturnType();
                    }
                    return Removal.of(returnType);
                }
            }
        }

        StackManipulation resolve(Assigner assigner, Assigner.Typing typing, MethodDescription methodDescription, MethodDescription methodDescription2);
    }

    Record compile(MethodDescription methodDescription);

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/MethodDelegationBinder$Record.class */
    public interface Record {
        MethodBinding bind(Implementation.Target target, MethodDescription methodDescription, TerminationHandler terminationHandler, MethodInvoker methodInvoker, Assigner assigner);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/MethodDelegationBinder$Record$Illegal.class */
        public enum Illegal implements Record {
            INSTANCE;

            @Override // net.bytebuddy.implementation.bind.MethodDelegationBinder.Record
            public MethodBinding bind(Implementation.Target implementationTarget, MethodDescription source, TerminationHandler terminationHandler, MethodInvoker methodInvoker, Assigner assigner) {
                return MethodBinding.Illegal.INSTANCE;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/MethodDelegationBinder$MethodInvoker.class */
    public interface MethodInvoker {
        StackManipulation invoke(MethodDescription methodDescription);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/MethodDelegationBinder$MethodInvoker$Simple.class */
        public enum Simple implements MethodInvoker {
            INSTANCE;

            @Override // net.bytebuddy.implementation.bind.MethodDelegationBinder.MethodInvoker
            public StackManipulation invoke(MethodDescription methodDescription) {
                return MethodInvocation.invoke(methodDescription);
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/MethodDelegationBinder$MethodInvoker$Virtual.class */
        public static class Virtual implements MethodInvoker {
            private final TypeDescription typeDescription;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.typeDescription.equals(((Virtual) obj).typeDescription);
            }

            public int hashCode() {
                return (17 * 31) + this.typeDescription.hashCode();
            }

            public Virtual(TypeDescription typeDescription) {
                this.typeDescription = typeDescription;
            }

            @Override // net.bytebuddy.implementation.bind.MethodDelegationBinder.MethodInvoker
            public StackManipulation invoke(MethodDescription methodDescription) {
                return MethodInvocation.invoke(methodDescription).virtual(this.typeDescription);
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/MethodDelegationBinder$ParameterBinding.class */
    public interface ParameterBinding<T> extends StackManipulation {
        T getIdentificationToken();

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/MethodDelegationBinder$ParameterBinding$Illegal.class */
        public enum Illegal implements ParameterBinding<Void> {
            INSTANCE;

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // net.bytebuddy.implementation.bind.MethodDelegationBinder.ParameterBinding
            public Void getIdentificationToken() {
                throw new IllegalStateException("An illegal binding does not define an identification token");
            }

            @Override // net.bytebuddy.implementation.bytecode.StackManipulation
            public boolean isValid() {
                return false;
            }

            @Override // net.bytebuddy.implementation.bytecode.StackManipulation
            public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
                throw new IllegalStateException("An illegal parameter binding must not be applied");
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/MethodDelegationBinder$ParameterBinding$Anonymous.class */
        public static class Anonymous implements ParameterBinding<Object> {
            @HashCodeAndEqualsPlugin.ValueHandling(HashCodeAndEqualsPlugin.ValueHandling.Sort.IGNORE)
            private final Object anonymousToken = new Object();
            private final StackManipulation delegate;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.delegate.equals(((Anonymous) obj).delegate);
            }

            public int hashCode() {
                return (17 * 31) + this.delegate.hashCode();
            }

            public Anonymous(StackManipulation delegate) {
                this.delegate = delegate;
            }

            @Override // net.bytebuddy.implementation.bind.MethodDelegationBinder.ParameterBinding
            public Object getIdentificationToken() {
                return this.anonymousToken;
            }

            @Override // net.bytebuddy.implementation.bytecode.StackManipulation
            public boolean isValid() {
                return this.delegate.isValid();
            }

            @Override // net.bytebuddy.implementation.bytecode.StackManipulation
            public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
                return this.delegate.apply(methodVisitor, implementationContext);
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/MethodDelegationBinder$ParameterBinding$Unique.class */
        public static class Unique<T> implements ParameterBinding<T> {
            private final T identificationToken;
            private final StackManipulation delegate;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.identificationToken.equals(((Unique) obj).identificationToken) && this.delegate.equals(((Unique) obj).delegate);
            }

            public int hashCode() {
                return (((17 * 31) + this.identificationToken.hashCode()) * 31) + this.delegate.hashCode();
            }

            public Unique(StackManipulation delegate, T identificationToken) {
                this.delegate = delegate;
                this.identificationToken = identificationToken;
            }

            public static <S> Unique<S> of(StackManipulation delegate, S identificationToken) {
                return new Unique<>(delegate, identificationToken);
            }

            @Override // net.bytebuddy.implementation.bind.MethodDelegationBinder.ParameterBinding
            public T getIdentificationToken() {
                return this.identificationToken;
            }

            @Override // net.bytebuddy.implementation.bytecode.StackManipulation
            public boolean isValid() {
                return this.delegate.isValid();
            }

            @Override // net.bytebuddy.implementation.bytecode.StackManipulation
            public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
                return this.delegate.apply(methodVisitor, implementationContext);
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/MethodDelegationBinder$MethodBinding.class */
    public interface MethodBinding extends StackManipulation {
        Integer getTargetParameterIndex(Object obj);

        MethodDescription getTarget();

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/MethodDelegationBinder$MethodBinding$Illegal.class */
        public enum Illegal implements MethodBinding {
            INSTANCE;

            @Override // net.bytebuddy.implementation.bind.MethodDelegationBinder.MethodBinding
            public Integer getTargetParameterIndex(Object parameterBindingToken) {
                throw new IllegalStateException("Method is not bound");
            }

            @Override // net.bytebuddy.implementation.bind.MethodDelegationBinder.MethodBinding
            public MethodDescription getTarget() {
                throw new IllegalStateException("Method is not bound");
            }

            @Override // net.bytebuddy.implementation.bytecode.StackManipulation
            public boolean isValid() {
                return false;
            }

            @Override // net.bytebuddy.implementation.bytecode.StackManipulation
            public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
                throw new IllegalStateException("Cannot delegate to an unbound method");
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/MethodDelegationBinder$MethodBinding$Builder.class */
        public static class Builder {
            private final MethodInvoker methodInvoker;
            private final MethodDescription candidate;
            private final List<StackManipulation> parameterStackManipulations;
            private final LinkedHashMap<Object, Integer> registeredTargetIndices = new LinkedHashMap<>();
            private int nextParameterIndex = 0;

            public Builder(MethodInvoker methodInvoker, MethodDescription candidate) {
                this.methodInvoker = methodInvoker;
                this.candidate = candidate;
                this.parameterStackManipulations = new ArrayList(candidate.getParameters().size());
            }

            public boolean append(ParameterBinding<?> parameterBinding) {
                this.parameterStackManipulations.add(parameterBinding);
                LinkedHashMap<Object, Integer> linkedHashMap = this.registeredTargetIndices;
                Object identificationToken = parameterBinding.getIdentificationToken();
                int i = this.nextParameterIndex;
                this.nextParameterIndex = i + 1;
                return linkedHashMap.put(identificationToken, Integer.valueOf(i)) == null;
            }

            public MethodBinding build(StackManipulation terminatingManipulation) {
                if (this.candidate.getParameters().size() != this.nextParameterIndex) {
                    throw new IllegalStateException("The number of parameters bound does not equal the target's number of parameters");
                }
                return new Build(this.candidate, this.registeredTargetIndices, this.methodInvoker.invoke(this.candidate), this.parameterStackManipulations, terminatingManipulation);
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/MethodDelegationBinder$MethodBinding$Builder$Build.class */
            protected static class Build implements MethodBinding {
                private final MethodDescription target;
                private final Map<?, Integer> registeredTargetIndices;
                private final StackManipulation methodInvocation;
                private final List<StackManipulation> parameterStackManipulations;
                private final StackManipulation terminatingStackManipulation;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.target.equals(((Build) obj).target) && this.registeredTargetIndices.equals(((Build) obj).registeredTargetIndices) && this.methodInvocation.equals(((Build) obj).methodInvocation) && this.parameterStackManipulations.equals(((Build) obj).parameterStackManipulations) && this.terminatingStackManipulation.equals(((Build) obj).terminatingStackManipulation);
                }

                public int hashCode() {
                    return (((((((((17 * 31) + this.target.hashCode()) * 31) + this.registeredTargetIndices.hashCode()) * 31) + this.methodInvocation.hashCode()) * 31) + this.parameterStackManipulations.hashCode()) * 31) + this.terminatingStackManipulation.hashCode();
                }

                protected Build(MethodDescription target, Map<?, Integer> registeredTargetIndices, StackManipulation methodInvocation, List<StackManipulation> parameterStackManipulations, StackManipulation terminatingStackManipulation) {
                    this.target = target;
                    this.registeredTargetIndices = new HashMap(registeredTargetIndices);
                    this.methodInvocation = methodInvocation;
                    this.parameterStackManipulations = new ArrayList(parameterStackManipulations);
                    this.terminatingStackManipulation = terminatingStackManipulation;
                }

                @Override // net.bytebuddy.implementation.bytecode.StackManipulation
                public boolean isValid() {
                    boolean result = this.methodInvocation.isValid() && this.terminatingStackManipulation.isValid();
                    Iterator<StackManipulation> assignment = this.parameterStackManipulations.iterator();
                    while (result && assignment.hasNext()) {
                        result = assignment.next().isValid();
                    }
                    return result;
                }

                @Override // net.bytebuddy.implementation.bind.MethodDelegationBinder.MethodBinding
                public Integer getTargetParameterIndex(Object parameterBindingToken) {
                    return this.registeredTargetIndices.get(parameterBindingToken);
                }

                @Override // net.bytebuddy.implementation.bind.MethodDelegationBinder.MethodBinding
                public MethodDescription getTarget() {
                    return this.target;
                }

                @Override // net.bytebuddy.implementation.bytecode.StackManipulation
                public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
                    return new StackManipulation.Compound(CompoundList.of((List) this.parameterStackManipulations, Arrays.asList(this.methodInvocation, this.terminatingStackManipulation))).apply(methodVisitor, implementationContext);
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/MethodDelegationBinder$BindingResolver.class */
    public interface BindingResolver {
        MethodBinding resolve(AmbiguityResolver ambiguityResolver, MethodDescription methodDescription, List<MethodBinding> list);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/MethodDelegationBinder$BindingResolver$Default.class */
        public enum Default implements BindingResolver {
            INSTANCE;
            
            private static final int ONLY = 0;
            private static final int LEFT = 0;
            private static final int RIGHT = 1;

            @Override // net.bytebuddy.implementation.bind.MethodDelegationBinder.BindingResolver
            public MethodBinding resolve(AmbiguityResolver ambiguityResolver, MethodDescription source, List<MethodBinding> targets) {
                return doResolve(ambiguityResolver, source, new ArrayList(targets));
            }

            private MethodBinding doResolve(AmbiguityResolver ambiguityResolver, MethodDescription source, List<MethodBinding> targets) {
                switch (targets.size()) {
                    case 1:
                        return targets.get(0);
                    case 2:
                        MethodBinding left = targets.get(0);
                        MethodBinding right = targets.get(1);
                        switch (ambiguityResolver.resolve(source, left, right)) {
                            case LEFT:
                                return left;
                            case RIGHT:
                                return right;
                            case AMBIGUOUS:
                            case UNKNOWN:
                                throw new IllegalArgumentException("Cannot resolve ambiguous delegation of " + source + " to " + left.getTarget() + " or " + right.getTarget());
                            default:
                                throw new AssertionError();
                        }
                    default:
                        MethodBinding left2 = targets.get(0);
                        MethodBinding right2 = targets.get(1);
                        switch (ambiguityResolver.resolve(source, left2, right2)) {
                            case LEFT:
                                targets.remove(1);
                                return doResolve(ambiguityResolver, source, targets);
                            case RIGHT:
                                targets.remove(0);
                                return doResolve(ambiguityResolver, source, targets);
                            case AMBIGUOUS:
                            case UNKNOWN:
                                targets.remove(1);
                                targets.remove(0);
                                MethodBinding subResult = doResolve(ambiguityResolver, source, targets);
                                switch (ambiguityResolver.resolve(source, left2, subResult).merge(ambiguityResolver.resolve(source, right2, subResult))) {
                                    case LEFT:
                                    case AMBIGUOUS:
                                    case UNKNOWN:
                                        throw new IllegalArgumentException("Cannot resolve ambiguous delegation of " + source + " to " + left2.getTarget() + " or " + right2.getTarget());
                                    case RIGHT:
                                        return subResult;
                                    default:
                                        throw new AssertionError();
                                }
                            default:
                                throw new IllegalStateException("Unexpected amount of targets: " + targets.size());
                        }
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/MethodDelegationBinder$BindingResolver$Unique.class */
        public enum Unique implements BindingResolver {
            INSTANCE;
            
            private static final int ONLY = 0;

            @Override // net.bytebuddy.implementation.bind.MethodDelegationBinder.BindingResolver
            public MethodBinding resolve(AmbiguityResolver ambiguityResolver, MethodDescription source, List<MethodBinding> targets) {
                if (targets.size() == 1) {
                    return targets.get(0);
                }
                throw new IllegalStateException(source + " allowed for more than one binding: " + targets);
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/MethodDelegationBinder$BindingResolver$StreamWriting.class */
        public static class StreamWriting implements BindingResolver {
            private final BindingResolver delegate;
            private final PrintStream printStream;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.delegate.equals(((StreamWriting) obj).delegate) && this.printStream.equals(((StreamWriting) obj).printStream);
            }

            public int hashCode() {
                return (((17 * 31) + this.delegate.hashCode()) * 31) + this.printStream.hashCode();
            }

            public StreamWriting(BindingResolver delegate, PrintStream printStream) {
                this.delegate = delegate;
                this.printStream = printStream;
            }

            public static BindingResolver toSystemOut() {
                return toSystemOut(Default.INSTANCE);
            }

            public static BindingResolver toSystemOut(BindingResolver bindingResolver) {
                return new StreamWriting(bindingResolver, System.out);
            }

            public static BindingResolver toSystemError() {
                return toSystemError(Default.INSTANCE);
            }

            public static BindingResolver toSystemError(BindingResolver bindingResolver) {
                return new StreamWriting(bindingResolver, System.err);
            }

            @Override // net.bytebuddy.implementation.bind.MethodDelegationBinder.BindingResolver
            public MethodBinding resolve(AmbiguityResolver ambiguityResolver, MethodDescription source, List<MethodBinding> targets) {
                MethodBinding methodBinding = this.delegate.resolve(ambiguityResolver, source, targets);
                this.printStream.println("Binding " + source + " as delegation to " + methodBinding.getTarget());
                return methodBinding;
            }
        }
    }

    @SuppressFBWarnings(value = {"IC_SUPERCLASS_USES_SUBCLASS_DURING_INITIALIZATION"}, justification = "Safe initialization is implied")
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/MethodDelegationBinder$AmbiguityResolver.class */
    public interface AmbiguityResolver {
        public static final AmbiguityResolver DEFAULT = new Compound(BindingPriority.Resolver.INSTANCE, DeclaringTypeResolver.INSTANCE, ArgumentTypeResolver.INSTANCE, MethodNameEqualityResolver.INSTANCE, ParameterLengthResolver.INSTANCE);

        Resolution resolve(MethodDescription methodDescription, MethodBinding methodBinding, MethodBinding methodBinding2);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/MethodDelegationBinder$AmbiguityResolver$Resolution.class */
        public enum Resolution {
            UNKNOWN(true),
            LEFT(false),
            RIGHT(false),
            AMBIGUOUS(true);
            
            private final boolean unresolved;

            Resolution(boolean unresolved) {
                this.unresolved = unresolved;
            }

            public boolean isUnresolved() {
                return this.unresolved;
            }

            public Resolution merge(Resolution other) {
                switch (this) {
                    case LEFT:
                    case RIGHT:
                        return (other == UNKNOWN || other == this) ? this : AMBIGUOUS;
                    case AMBIGUOUS:
                        return AMBIGUOUS;
                    case UNKNOWN:
                        return other;
                    default:
                        throw new AssertionError();
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/MethodDelegationBinder$AmbiguityResolver$NoOp.class */
        public enum NoOp implements AmbiguityResolver {
            INSTANCE;

            @Override // net.bytebuddy.implementation.bind.MethodDelegationBinder.AmbiguityResolver
            public Resolution resolve(MethodDescription source, MethodBinding left, MethodBinding right) {
                return Resolution.UNKNOWN;
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/MethodDelegationBinder$AmbiguityResolver$Directional.class */
        public enum Directional implements AmbiguityResolver {
            LEFT(true),
            RIGHT(false);
            
            private final boolean left;

            Directional(boolean left) {
                this.left = left;
            }

            @Override // net.bytebuddy.implementation.bind.MethodDelegationBinder.AmbiguityResolver
            public Resolution resolve(MethodDescription source, MethodBinding left, MethodBinding right) {
                return this.left ? Resolution.LEFT : Resolution.RIGHT;
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/MethodDelegationBinder$AmbiguityResolver$Compound.class */
        public static class Compound implements AmbiguityResolver {
            private final List<AmbiguityResolver> ambiguityResolvers;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.ambiguityResolvers.equals(((Compound) obj).ambiguityResolvers);
            }

            public int hashCode() {
                return (17 * 31) + this.ambiguityResolvers.hashCode();
            }

            public Compound(AmbiguityResolver... ambiguityResolver) {
                this(Arrays.asList(ambiguityResolver));
            }

            public Compound(List<? extends AmbiguityResolver> ambiguityResolvers) {
                this.ambiguityResolvers = new ArrayList();
                for (AmbiguityResolver ambiguityResolver : ambiguityResolvers) {
                    if (ambiguityResolver instanceof Compound) {
                        this.ambiguityResolvers.addAll(((Compound) ambiguityResolver).ambiguityResolvers);
                    } else if (!(ambiguityResolver instanceof NoOp)) {
                        this.ambiguityResolvers.add(ambiguityResolver);
                    }
                }
            }

            @Override // net.bytebuddy.implementation.bind.MethodDelegationBinder.AmbiguityResolver
            public Resolution resolve(MethodDescription source, MethodBinding left, MethodBinding right) {
                Resolution resolution = Resolution.UNKNOWN;
                Iterator<? extends AmbiguityResolver> iterator = this.ambiguityResolvers.iterator();
                while (resolution.isUnresolved() && iterator.hasNext()) {
                    resolution = iterator.next().resolve(source, left, right);
                }
                return resolution;
            }
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bind/MethodDelegationBinder$Processor.class */
    public static class Processor implements Record {
        private final List<? extends Record> records;
        private final AmbiguityResolver ambiguityResolver;
        private final BindingResolver bindingResolver;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.records.equals(((Processor) obj).records) && this.ambiguityResolver.equals(((Processor) obj).ambiguityResolver) && this.bindingResolver.equals(((Processor) obj).bindingResolver);
        }

        public int hashCode() {
            return (((((17 * 31) + this.records.hashCode()) * 31) + this.ambiguityResolver.hashCode()) * 31) + this.bindingResolver.hashCode();
        }

        public Processor(List<? extends Record> records, AmbiguityResolver ambiguityResolver, BindingResolver bindingResolver) {
            this.records = records;
            this.ambiguityResolver = ambiguityResolver;
            this.bindingResolver = bindingResolver;
        }

        @Override // net.bytebuddy.implementation.bind.MethodDelegationBinder.Record
        public MethodBinding bind(Implementation.Target implementationTarget, MethodDescription source, TerminationHandler terminationHandler, MethodInvoker methodInvoker, Assigner assigner) {
            List<MethodBinding> targets = new ArrayList<>();
            for (Record record : this.records) {
                MethodBinding methodBinding = record.bind(implementationTarget, source, terminationHandler, methodInvoker, assigner);
                if (methodBinding.isValid()) {
                    targets.add(methodBinding);
                }
            }
            if (targets.isEmpty()) {
                throw new IllegalArgumentException("None of " + this.records + " allows for delegation from " + source);
            }
            return this.bindingResolver.resolve(this.ambiguityResolver, source, targets);
        }
    }
}
