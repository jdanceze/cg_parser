package net.bytebuddy.agent.builder;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.File;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.agent.builder.ResettableClassFileTransformer;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.build.EntryPoint;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.build.Plugin;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.modifier.FieldManifestation;
import net.bytebuddy.description.modifier.MethodManifestation;
import net.bytebuddy.description.modifier.Ownership;
import net.bytebuddy.description.modifier.TypeManifestation;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.NexusAccessor;
import net.bytebuddy.dynamic.TypeResolutionStrategy;
import net.bytebuddy.dynamic.VisibilityBridgeStrategy;
import net.bytebuddy.dynamic.loading.ClassInjector;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.dynamic.scaffold.InstrumentedType;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.dynamic.scaffold.inline.MethodNameTransformer;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.ExceptionMethod;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.LoadedTypeInitializer;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.implementation.auxiliary.AuxiliaryType;
import net.bytebuddy.implementation.auxiliary.TypeProxy;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;
import net.bytebuddy.implementation.bytecode.Duplication;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.TypeCreation;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import net.bytebuddy.implementation.bytecode.assign.TypeCasting;
import net.bytebuddy.implementation.bytecode.collection.ArrayFactory;
import net.bytebuddy.implementation.bytecode.constant.ClassConstant;
import net.bytebuddy.implementation.bytecode.constant.IntegerConstant;
import net.bytebuddy.implementation.bytecode.constant.TextConstant;
import net.bytebuddy.implementation.bytecode.member.FieldAccess;
import net.bytebuddy.implementation.bytecode.member.MethodInvocation;
import net.bytebuddy.implementation.bytecode.member.MethodReturn;
import net.bytebuddy.implementation.bytecode.member.MethodVariableAccess;
import net.bytebuddy.jar.asm.Label;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.jar.asm.Opcodes;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.matcher.LatentMatcher;
import net.bytebuddy.pool.TypePool;
import net.bytebuddy.utility.CompoundList;
import net.bytebuddy.utility.JavaConstant;
import net.bytebuddy.utility.JavaModule;
import net.bytebuddy.utility.JavaType;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder.class */
public interface AgentBuilder {

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$ClassFileBufferStrategy.class */
    public interface ClassFileBufferStrategy {

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$ClassFileBufferStrategy$Default.class */
        public enum Default implements ClassFileBufferStrategy {
            RETAINING { // from class: net.bytebuddy.agent.builder.AgentBuilder.ClassFileBufferStrategy.Default.1
                @Override // net.bytebuddy.agent.builder.AgentBuilder.ClassFileBufferStrategy
                public ClassFileLocator resolve(String name, byte[] binaryRepresentation, ClassLoader classLoader, JavaModule module, ProtectionDomain protectionDomain) {
                    return ClassFileLocator.Simple.of(name, binaryRepresentation);
                }
            },
            DISCARDING { // from class: net.bytebuddy.agent.builder.AgentBuilder.ClassFileBufferStrategy.Default.2
                @Override // net.bytebuddy.agent.builder.AgentBuilder.ClassFileBufferStrategy
                public ClassFileLocator resolve(String name, byte[] binaryRepresentation, ClassLoader classLoader, JavaModule module, ProtectionDomain protectionDomain) {
                    return ClassFileLocator.NoOp.INSTANCE;
                }
            }
        }

        ClassFileLocator resolve(String str, byte[] bArr, ClassLoader classLoader, JavaModule javaModule, ProtectionDomain protectionDomain);
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Identified.class */
    public interface Identified {

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Identified$Extendable.class */
        public interface Extendable extends AgentBuilder, Identified {
            AgentBuilder asTerminalTransformation();
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Identified$Narrowable.class */
        public interface Narrowable extends Matchable<Narrowable>, Identified {
        }

        Extendable transform(Transformer transformer);
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Ignored.class */
    public interface Ignored extends Matchable<Ignored>, AgentBuilder {
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionListenable.class */
    public interface RedefinitionListenable extends AgentBuilder {

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionListenable$WithImplicitDiscoveryStrategy.class */
        public interface WithImplicitDiscoveryStrategy extends RedefinitionListenable {
            RedefinitionListenable redefineOnly(Class<?>... clsArr);

            RedefinitionListenable with(RedefinitionStrategy.DiscoveryStrategy discoveryStrategy);
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionListenable$WithoutBatchStrategy.class */
        public interface WithoutBatchStrategy extends WithImplicitDiscoveryStrategy {
            WithImplicitDiscoveryStrategy with(RedefinitionStrategy.BatchAllocator batchAllocator);
        }

        RedefinitionListenable with(RedefinitionStrategy.Listener listener);

        AgentBuilder withResubmission(RedefinitionStrategy.ResubmissionScheduler resubmissionScheduler);

        AgentBuilder withResubmission(RedefinitionStrategy.ResubmissionScheduler resubmissionScheduler, ElementMatcher<? super Throwable> elementMatcher);

        AgentBuilder withResubmission(RedefinitionStrategy.ResubmissionScheduler resubmissionScheduler, ElementMatcher<? super Throwable> elementMatcher, ElementMatcher<? super ClassLoader> elementMatcher2);
    }

    AgentBuilder with(ByteBuddy byteBuddy);

    AgentBuilder with(Listener listener);

    AgentBuilder with(CircularityLock circularityLock);

    AgentBuilder with(PoolStrategy poolStrategy);

    AgentBuilder with(LocationStrategy locationStrategy);

    AgentBuilder with(TypeStrategy typeStrategy);

    AgentBuilder with(InitializationStrategy initializationStrategy);

    RedefinitionListenable.WithoutBatchStrategy with(RedefinitionStrategy redefinitionStrategy);

    AgentBuilder with(LambdaInstrumentationStrategy lambdaInstrumentationStrategy);

    AgentBuilder with(DescriptionStrategy descriptionStrategy);

    AgentBuilder with(FallbackStrategy fallbackStrategy);

    AgentBuilder with(ClassFileBufferStrategy classFileBufferStrategy);

    AgentBuilder with(InstallationListener installationListener);

    AgentBuilder with(InjectionStrategy injectionStrategy);

    AgentBuilder with(TransformerDecorator transformerDecorator);

    AgentBuilder enableNativeMethodPrefix(String str);

    AgentBuilder disableNativeMethodPrefix();

    AgentBuilder disableClassFormatChanges();

    AgentBuilder assureReadEdgeTo(Instrumentation instrumentation, Class<?>... clsArr);

    AgentBuilder assureReadEdgeTo(Instrumentation instrumentation, JavaModule... javaModuleArr);

    AgentBuilder assureReadEdgeTo(Instrumentation instrumentation, Collection<? extends JavaModule> collection);

    AgentBuilder assureReadEdgeFromAndTo(Instrumentation instrumentation, Class<?>... clsArr);

    AgentBuilder assureReadEdgeFromAndTo(Instrumentation instrumentation, JavaModule... javaModuleArr);

    AgentBuilder assureReadEdgeFromAndTo(Instrumentation instrumentation, Collection<? extends JavaModule> collection);

    Identified.Narrowable type(ElementMatcher<? super TypeDescription> elementMatcher);

    Identified.Narrowable type(ElementMatcher<? super TypeDescription> elementMatcher, ElementMatcher<? super ClassLoader> elementMatcher2);

    Identified.Narrowable type(ElementMatcher<? super TypeDescription> elementMatcher, ElementMatcher<? super ClassLoader> elementMatcher2, ElementMatcher<? super JavaModule> elementMatcher3);

    Identified.Narrowable type(RawMatcher rawMatcher);

    Ignored ignore(ElementMatcher<? super TypeDescription> elementMatcher);

    Ignored ignore(ElementMatcher<? super TypeDescription> elementMatcher, ElementMatcher<? super ClassLoader> elementMatcher2);

    Ignored ignore(ElementMatcher<? super TypeDescription> elementMatcher, ElementMatcher<? super ClassLoader> elementMatcher2, ElementMatcher<? super JavaModule> elementMatcher3);

    Ignored ignore(RawMatcher rawMatcher);

    ClassFileTransformer makeRaw();

    ResettableClassFileTransformer installOn(Instrumentation instrumentation);

    ResettableClassFileTransformer installOnByteBuddyAgent();

    ResettableClassFileTransformer patchOn(Instrumentation instrumentation, ResettableClassFileTransformer resettableClassFileTransformer);

    ResettableClassFileTransformer patchOnByteBuddyAgent(ResettableClassFileTransformer resettableClassFileTransformer);

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Matchable.class */
    public interface Matchable<T extends Matchable<T>> {
        T and(ElementMatcher<? super TypeDescription> elementMatcher);

        T and(ElementMatcher<? super TypeDescription> elementMatcher, ElementMatcher<? super ClassLoader> elementMatcher2);

        T and(ElementMatcher<? super TypeDescription> elementMatcher, ElementMatcher<? super ClassLoader> elementMatcher2, ElementMatcher<? super JavaModule> elementMatcher3);

        T and(RawMatcher rawMatcher);

        T or(ElementMatcher<? super TypeDescription> elementMatcher);

        T or(ElementMatcher<? super TypeDescription> elementMatcher, ElementMatcher<? super ClassLoader> elementMatcher2);

        T or(ElementMatcher<? super TypeDescription> elementMatcher, ElementMatcher<? super ClassLoader> elementMatcher2, ElementMatcher<? super JavaModule> elementMatcher3);

        T or(RawMatcher rawMatcher);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Matchable$AbstractBase.class */
        public static abstract class AbstractBase<S extends Matchable<S>> implements Matchable<S> {
            @Override // net.bytebuddy.agent.builder.AgentBuilder.Matchable
            public S and(ElementMatcher<? super TypeDescription> typeMatcher) {
                return and(typeMatcher, ElementMatchers.any());
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Matchable
            public S and(ElementMatcher<? super TypeDescription> typeMatcher, ElementMatcher<? super ClassLoader> classLoaderMatcher) {
                return and(typeMatcher, classLoaderMatcher, ElementMatchers.any());
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Matchable
            public S and(ElementMatcher<? super TypeDescription> typeMatcher, ElementMatcher<? super ClassLoader> classLoaderMatcher, ElementMatcher<? super JavaModule> moduleMatcher) {
                return (S) and(new RawMatcher.ForElementMatchers(typeMatcher, classLoaderMatcher, moduleMatcher));
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Matchable
            public S or(ElementMatcher<? super TypeDescription> typeMatcher) {
                return or(typeMatcher, ElementMatchers.any());
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Matchable
            public S or(ElementMatcher<? super TypeDescription> typeMatcher, ElementMatcher<? super ClassLoader> classLoaderMatcher) {
                return or(typeMatcher, classLoaderMatcher, ElementMatchers.any());
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Matchable
            public S or(ElementMatcher<? super TypeDescription> typeMatcher, ElementMatcher<? super ClassLoader> classLoaderMatcher, ElementMatcher<? super JavaModule> moduleMatcher) {
                return (S) or(new RawMatcher.ForElementMatchers(typeMatcher, classLoaderMatcher, moduleMatcher));
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RawMatcher.class */
    public interface RawMatcher {
        boolean matches(TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, Class<?> cls, ProtectionDomain protectionDomain);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RawMatcher$Trivial.class */
        public enum Trivial implements RawMatcher {
            MATCHING(true),
            NON_MATCHING(false);
            
            private final boolean matches;

            Trivial(boolean matches) {
                this.matches = matches;
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.RawMatcher
            public boolean matches(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, Class<?> classBeingRedefined, ProtectionDomain protectionDomain) {
                return this.matches;
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RawMatcher$ForLoadState.class */
        public enum ForLoadState implements RawMatcher {
            LOADED(false),
            UNLOADED(true);
            
            private final boolean unloaded;

            ForLoadState(boolean unloaded) {
                this.unloaded = unloaded;
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.RawMatcher
            public boolean matches(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, Class<?> classBeingRedefined, ProtectionDomain protectionDomain) {
                return (classBeingRedefined == null) == this.unloaded;
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RawMatcher$ForResolvableTypes.class */
        public enum ForResolvableTypes implements RawMatcher {
            INSTANCE;

            @Override // net.bytebuddy.agent.builder.AgentBuilder.RawMatcher
            public boolean matches(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, Class<?> classBeingRedefined, ProtectionDomain protectionDomain) {
                if (classBeingRedefined != null) {
                    try {
                        return Class.forName(classBeingRedefined.getName(), true, classLoader) == classBeingRedefined;
                    } catch (Throwable th) {
                        return false;
                    }
                }
                return true;
            }

            public RawMatcher inverted() {
                return new Inversion(this);
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RawMatcher$Conjunction.class */
        public static class Conjunction implements RawMatcher {
            private final RawMatcher left;
            private final RawMatcher right;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.left.equals(((Conjunction) obj).left) && this.right.equals(((Conjunction) obj).right);
            }

            public int hashCode() {
                return (((17 * 31) + this.left.hashCode()) * 31) + this.right.hashCode();
            }

            protected Conjunction(RawMatcher left, RawMatcher right) {
                this.left = left;
                this.right = right;
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.RawMatcher
            public boolean matches(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, Class<?> classBeingRedefined, ProtectionDomain protectionDomain) {
                return this.left.matches(typeDescription, classLoader, module, classBeingRedefined, protectionDomain) && this.right.matches(typeDescription, classLoader, module, classBeingRedefined, protectionDomain);
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RawMatcher$Disjunction.class */
        public static class Disjunction implements RawMatcher {
            private final RawMatcher left;
            private final RawMatcher right;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.left.equals(((Disjunction) obj).left) && this.right.equals(((Disjunction) obj).right);
            }

            public int hashCode() {
                return (((17 * 31) + this.left.hashCode()) * 31) + this.right.hashCode();
            }

            protected Disjunction(RawMatcher left, RawMatcher right) {
                this.left = left;
                this.right = right;
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.RawMatcher
            public boolean matches(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, Class<?> classBeingRedefined, ProtectionDomain protectionDomain) {
                return this.left.matches(typeDescription, classLoader, module, classBeingRedefined, protectionDomain) || this.right.matches(typeDescription, classLoader, module, classBeingRedefined, protectionDomain);
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RawMatcher$Inversion.class */
        public static class Inversion implements RawMatcher {
            private final RawMatcher matcher;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.matcher.equals(((Inversion) obj).matcher);
            }

            public int hashCode() {
                return (17 * 31) + this.matcher.hashCode();
            }

            public Inversion(RawMatcher matcher) {
                this.matcher = matcher;
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.RawMatcher
            public boolean matches(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, Class<?> classBeingRedefined, ProtectionDomain protectionDomain) {
                return !this.matcher.matches(typeDescription, classLoader, module, classBeingRedefined, protectionDomain);
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RawMatcher$ForElementMatchers.class */
        public static class ForElementMatchers implements RawMatcher {
            private final ElementMatcher<? super TypeDescription> typeMatcher;
            private final ElementMatcher<? super ClassLoader> classLoaderMatcher;
            private final ElementMatcher<? super JavaModule> moduleMatcher;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.typeMatcher.equals(((ForElementMatchers) obj).typeMatcher) && this.classLoaderMatcher.equals(((ForElementMatchers) obj).classLoaderMatcher) && this.moduleMatcher.equals(((ForElementMatchers) obj).moduleMatcher);
            }

            public int hashCode() {
                return (((((17 * 31) + this.typeMatcher.hashCode()) * 31) + this.classLoaderMatcher.hashCode()) * 31) + this.moduleMatcher.hashCode();
            }

            public ForElementMatchers(ElementMatcher<? super TypeDescription> typeMatcher) {
                this(typeMatcher, ElementMatchers.any());
            }

            public ForElementMatchers(ElementMatcher<? super TypeDescription> typeMatcher, ElementMatcher<? super ClassLoader> classLoaderMatcher) {
                this(typeMatcher, classLoaderMatcher, ElementMatchers.any());
            }

            public ForElementMatchers(ElementMatcher<? super TypeDescription> typeMatcher, ElementMatcher<? super ClassLoader> classLoaderMatcher, ElementMatcher<? super JavaModule> moduleMatcher) {
                this.typeMatcher = typeMatcher;
                this.classLoaderMatcher = classLoaderMatcher;
                this.moduleMatcher = moduleMatcher;
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.RawMatcher
            public boolean matches(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, Class<?> classBeingRedefined, ProtectionDomain protectionDomain) {
                return this.moduleMatcher.matches(module) && this.classLoaderMatcher.matches(classLoader) && this.typeMatcher.matches(typeDescription);
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Listener.class */
    public interface Listener {
        public static final boolean LOADED = true;

        void onDiscovery(String str, ClassLoader classLoader, JavaModule javaModule, boolean z);

        void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, boolean z, DynamicType dynamicType);

        void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, boolean z);

        void onError(String str, ClassLoader classLoader, JavaModule javaModule, boolean z, Throwable th);

        void onComplete(String str, ClassLoader classLoader, JavaModule javaModule, boolean z);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Listener$NoOp.class */
        public enum NoOp implements Listener {
            INSTANCE;

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Listener
            public void onDiscovery(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Listener
            public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded, DynamicType dynamicType) {
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Listener
            public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded) {
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Listener
            public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded, Throwable throwable) {
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Listener
            public void onComplete(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Listener$Adapter.class */
        public static abstract class Adapter implements Listener {
            @Override // net.bytebuddy.agent.builder.AgentBuilder.Listener
            public void onDiscovery(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Listener
            public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded, DynamicType dynamicType) {
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Listener
            public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded) {
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Listener
            public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded, Throwable throwable) {
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Listener
            public void onComplete(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Listener$StreamWriting.class */
        public static class StreamWriting implements Listener {
            protected static final String PREFIX = "[Byte Buddy]";
            private final PrintStream printStream;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.printStream.equals(((StreamWriting) obj).printStream);
            }

            public int hashCode() {
                return (17 * 31) + this.printStream.hashCode();
            }

            public StreamWriting(PrintStream printStream) {
                this.printStream = printStream;
            }

            public static StreamWriting toSystemOut() {
                return new StreamWriting(System.out);
            }

            public static StreamWriting toSystemError() {
                return new StreamWriting(System.err);
            }

            public Listener withTransformationsOnly() {
                return new WithTransformationsOnly(this);
            }

            public Listener withErrorsOnly() {
                return new WithErrorsOnly(this);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Listener
            public void onDiscovery(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
                this.printStream.printf("[Byte Buddy] DISCOVERY %s [%s, %s, loaded=%b]%n", typeName, classLoader, module, Boolean.valueOf(loaded));
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Listener
            public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded, DynamicType dynamicType) {
                this.printStream.printf("[Byte Buddy] TRANSFORM %s [%s, %s, loaded=%b]%n", typeDescription.getName(), classLoader, module, Boolean.valueOf(loaded));
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Listener
            public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded) {
                this.printStream.printf("[Byte Buddy] IGNORE %s [%s, %s, loaded=%b]%n", typeDescription.getName(), classLoader, module, Boolean.valueOf(loaded));
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Listener
            public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded, Throwable throwable) {
                synchronized (this.printStream) {
                    this.printStream.printf("[Byte Buddy] ERROR %s [%s, %s, loaded=%b]%n", typeName, classLoader, module, Boolean.valueOf(loaded));
                    throwable.printStackTrace(this.printStream);
                }
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Listener
            public void onComplete(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
                this.printStream.printf("[Byte Buddy] COMPLETE %s [%s, %s, loaded=%b]%n", typeName, classLoader, module, Boolean.valueOf(loaded));
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Listener$Filtering.class */
        public static class Filtering implements Listener {
            private final ElementMatcher<? super String> matcher;
            private final Listener delegate;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.matcher.equals(((Filtering) obj).matcher) && this.delegate.equals(((Filtering) obj).delegate);
            }

            public int hashCode() {
                return (((17 * 31) + this.matcher.hashCode()) * 31) + this.delegate.hashCode();
            }

            public Filtering(ElementMatcher<? super String> matcher, Listener delegate) {
                this.matcher = matcher;
                this.delegate = delegate;
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Listener
            public void onDiscovery(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
                if (this.matcher.matches(typeName)) {
                    this.delegate.onDiscovery(typeName, classLoader, module, loaded);
                }
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Listener
            public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded, DynamicType dynamicType) {
                if (this.matcher.matches(typeDescription.getName())) {
                    this.delegate.onTransformation(typeDescription, classLoader, module, loaded, dynamicType);
                }
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Listener
            public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded) {
                if (this.matcher.matches(typeDescription.getName())) {
                    this.delegate.onIgnored(typeDescription, classLoader, module, loaded);
                }
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Listener
            public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded, Throwable throwable) {
                if (this.matcher.matches(typeName)) {
                    this.delegate.onError(typeName, classLoader, module, loaded, throwable);
                }
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Listener
            public void onComplete(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
                if (this.matcher.matches(typeName)) {
                    this.delegate.onComplete(typeName, classLoader, module, loaded);
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Listener$WithTransformationsOnly.class */
        public static class WithTransformationsOnly extends Adapter {
            private final Listener delegate;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.delegate.equals(((WithTransformationsOnly) obj).delegate);
            }

            public int hashCode() {
                return (17 * 31) + this.delegate.hashCode();
            }

            public WithTransformationsOnly(Listener delegate) {
                this.delegate = delegate;
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Listener.Adapter, net.bytebuddy.agent.builder.AgentBuilder.Listener
            public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded, DynamicType dynamicType) {
                this.delegate.onTransformation(typeDescription, classLoader, module, loaded, dynamicType);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Listener.Adapter, net.bytebuddy.agent.builder.AgentBuilder.Listener
            public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded, Throwable throwable) {
                this.delegate.onError(typeName, classLoader, module, loaded, throwable);
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Listener$WithErrorsOnly.class */
        public static class WithErrorsOnly extends Adapter {
            private final Listener delegate;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.delegate.equals(((WithErrorsOnly) obj).delegate);
            }

            public int hashCode() {
                return (17 * 31) + this.delegate.hashCode();
            }

            public WithErrorsOnly(Listener delegate) {
                this.delegate = delegate;
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Listener.Adapter, net.bytebuddy.agent.builder.AgentBuilder.Listener
            public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded, Throwable throwable) {
                this.delegate.onError(typeName, classLoader, module, loaded, throwable);
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Listener$ModuleReadEdgeCompleting.class */
        public static class ModuleReadEdgeCompleting extends Adapter {
            private final Instrumentation instrumentation;
            private final boolean addTargetEdge;
            private final Set<? extends JavaModule> modules;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.addTargetEdge == ((ModuleReadEdgeCompleting) obj).addTargetEdge && this.instrumentation.equals(((ModuleReadEdgeCompleting) obj).instrumentation) && this.modules.equals(((ModuleReadEdgeCompleting) obj).modules);
            }

            public int hashCode() {
                return (((((17 * 31) + this.instrumentation.hashCode()) * 31) + (this.addTargetEdge ? 1 : 0)) * 31) + this.modules.hashCode();
            }

            public ModuleReadEdgeCompleting(Instrumentation instrumentation, boolean addTargetEdge, Set<? extends JavaModule> modules) {
                this.instrumentation = instrumentation;
                this.addTargetEdge = addTargetEdge;
                this.modules = modules;
            }

            public static Listener of(Instrumentation instrumentation, boolean addTargetEdge, Class<?>... type) {
                Set<JavaModule> modules = new HashSet<>();
                for (Class<?> aType : type) {
                    modules.add(JavaModule.ofType(aType));
                }
                return modules.isEmpty() ? NoOp.INSTANCE : new ModuleReadEdgeCompleting(instrumentation, addTargetEdge, modules);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Listener.Adapter, net.bytebuddy.agent.builder.AgentBuilder.Listener
            public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded, DynamicType dynamicType) {
                Map<String, Set<JavaModule>> emptyMap;
                if (module != JavaModule.UNSUPPORTED && module.isNamed()) {
                    for (JavaModule target : this.modules) {
                        if (!module.canRead(target) || (this.addTargetEdge && !module.isOpened(typeDescription.getPackage(), target))) {
                            Instrumentation instrumentation = this.instrumentation;
                            Set<JavaModule> singleton = Collections.singleton(target);
                            Map<String, Set<JavaModule>> emptyMap2 = Collections.emptyMap();
                            if (!this.addTargetEdge || typeDescription.getPackage() == null) {
                                emptyMap = Collections.emptyMap();
                            } else {
                                emptyMap = Collections.singletonMap(typeDescription.getPackage().getName(), Collections.singleton(target));
                            }
                            module.modify(instrumentation, singleton, emptyMap2, emptyMap, Collections.emptySet(), Collections.emptyMap());
                        }
                        if (this.addTargetEdge && !target.canRead(module)) {
                            target.modify(this.instrumentation, Collections.singleton(module), Collections.emptyMap(), Collections.emptyMap(), Collections.emptySet(), Collections.emptyMap());
                        }
                    }
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Listener$Compound.class */
        public static class Compound implements Listener {
            private final List<Listener> listeners;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.listeners.equals(((Compound) obj).listeners);
            }

            public int hashCode() {
                return (17 * 31) + this.listeners.hashCode();
            }

            public Compound(Listener... listener) {
                this(Arrays.asList(listener));
            }

            public Compound(List<? extends Listener> listeners) {
                this.listeners = new ArrayList();
                for (Listener listener : listeners) {
                    if (listener instanceof Compound) {
                        this.listeners.addAll(((Compound) listener).listeners);
                    } else if (!(listener instanceof NoOp)) {
                        this.listeners.add(listener);
                    }
                }
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Listener
            public void onDiscovery(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
                for (Listener listener : this.listeners) {
                    listener.onDiscovery(typeName, classLoader, module, loaded);
                }
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Listener
            public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded, DynamicType dynamicType) {
                for (Listener listener : this.listeners) {
                    listener.onTransformation(typeDescription, classLoader, module, loaded, dynamicType);
                }
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Listener
            public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded) {
                for (Listener listener : this.listeners) {
                    listener.onIgnored(typeDescription, classLoader, module, loaded);
                }
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Listener
            public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded, Throwable throwable) {
                for (Listener listener : this.listeners) {
                    listener.onError(typeName, classLoader, module, loaded, throwable);
                }
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Listener
            public void onComplete(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
                for (Listener listener : this.listeners) {
                    listener.onComplete(typeName, classLoader, module, loaded);
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$CircularityLock.class */
    public interface CircularityLock {
        boolean acquire();

        void release();

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$CircularityLock$Inactive.class */
        public enum Inactive implements CircularityLock {
            INSTANCE;

            @Override // net.bytebuddy.agent.builder.AgentBuilder.CircularityLock
            public boolean acquire() {
                return true;
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.CircularityLock
            public void release() {
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$CircularityLock$Default.class */
        public static class Default extends ThreadLocal<Boolean> implements CircularityLock {
            private static final Boolean NOT_ACQUIRED = null;

            @Override // net.bytebuddy.agent.builder.AgentBuilder.CircularityLock
            public boolean acquire() {
                if (get() == NOT_ACQUIRED) {
                    set(true);
                    return true;
                }
                return false;
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.CircularityLock
            public void release() {
                set(NOT_ACQUIRED);
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$CircularityLock$Global.class */
        public static class Global implements CircularityLock {
            private final Lock lock;
            private final long time;
            private final TimeUnit timeUnit;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.time == ((Global) obj).time && this.timeUnit.equals(((Global) obj).timeUnit) && this.lock.equals(((Global) obj).lock);
            }

            public int hashCode() {
                int hashCode = ((17 * 31) + this.lock.hashCode()) * 31;
                return ((hashCode + ((int) (hashCode ^ (this.time >>> 32)))) * 31) + this.timeUnit.hashCode();
            }

            public Global() {
                this(0L, TimeUnit.MILLISECONDS);
            }

            public Global(long time, TimeUnit timeUnit) {
                this.lock = new ReentrantLock();
                this.time = time;
                this.timeUnit = timeUnit;
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.CircularityLock
            public boolean acquire() {
                try {
                    if (this.time == 0) {
                        return this.lock.tryLock();
                    }
                    return this.lock.tryLock(this.time, this.timeUnit);
                } catch (InterruptedException e) {
                    return false;
                }
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.CircularityLock
            public void release() {
                this.lock.unlock();
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$TypeStrategy.class */
    public interface TypeStrategy {

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$TypeStrategy$Default.class */
        public enum Default implements TypeStrategy {
            REBASE { // from class: net.bytebuddy.agent.builder.AgentBuilder.TypeStrategy.Default.1
                @Override // net.bytebuddy.agent.builder.AgentBuilder.TypeStrategy
                public DynamicType.Builder<?> builder(TypeDescription typeDescription, ByteBuddy byteBuddy, ClassFileLocator classFileLocator, MethodNameTransformer methodNameTransformer, ClassLoader classLoader, JavaModule module, ProtectionDomain protectionDomain) {
                    return byteBuddy.rebase(typeDescription, classFileLocator, methodNameTransformer);
                }
            },
            REDEFINE { // from class: net.bytebuddy.agent.builder.AgentBuilder.TypeStrategy.Default.2
                @Override // net.bytebuddy.agent.builder.AgentBuilder.TypeStrategy
                public DynamicType.Builder<?> builder(TypeDescription typeDescription, ByteBuddy byteBuddy, ClassFileLocator classFileLocator, MethodNameTransformer methodNameTransformer, ClassLoader classLoader, JavaModule module, ProtectionDomain protectionDomain) {
                    return byteBuddy.redefine(typeDescription, classFileLocator);
                }
            },
            REDEFINE_FROZEN { // from class: net.bytebuddy.agent.builder.AgentBuilder.TypeStrategy.Default.3
                @Override // net.bytebuddy.agent.builder.AgentBuilder.TypeStrategy
                public DynamicType.Builder<?> builder(TypeDescription typeDescription, ByteBuddy byteBuddy, ClassFileLocator classFileLocator, MethodNameTransformer methodNameTransformer, ClassLoader classLoader, JavaModule module, ProtectionDomain protectionDomain) {
                    return byteBuddy.with(InstrumentedType.Factory.Default.FROZEN).with(VisibilityBridgeStrategy.Default.NEVER).redefine(typeDescription, classFileLocator).ignoreAlso(LatentMatcher.ForSelfDeclaredMethod.NOT_DECLARED);
                }
            },
            DECORATE { // from class: net.bytebuddy.agent.builder.AgentBuilder.TypeStrategy.Default.4
                @Override // net.bytebuddy.agent.builder.AgentBuilder.TypeStrategy
                public DynamicType.Builder<?> builder(TypeDescription typeDescription, ByteBuddy byteBuddy, ClassFileLocator classFileLocator, MethodNameTransformer methodNameTransformer, ClassLoader classLoader, JavaModule module, ProtectionDomain protectionDomain) {
                    return byteBuddy.decorate(typeDescription, classFileLocator);
                }
            }
        }

        DynamicType.Builder<?> builder(TypeDescription typeDescription, ByteBuddy byteBuddy, ClassFileLocator classFileLocator, MethodNameTransformer methodNameTransformer, ClassLoader classLoader, JavaModule javaModule, ProtectionDomain protectionDomain);

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$TypeStrategy$ForBuildEntryPoint.class */
        public static class ForBuildEntryPoint implements TypeStrategy {
            private final EntryPoint entryPoint;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.entryPoint.equals(((ForBuildEntryPoint) obj).entryPoint);
            }

            public int hashCode() {
                return (17 * 31) + this.entryPoint.hashCode();
            }

            public ForBuildEntryPoint(EntryPoint entryPoint) {
                this.entryPoint = entryPoint;
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.TypeStrategy
            public DynamicType.Builder<?> builder(TypeDescription typeDescription, ByteBuddy byteBuddy, ClassFileLocator classFileLocator, MethodNameTransformer methodNameTransformer, ClassLoader classLoader, JavaModule module, ProtectionDomain protectionDomain) {
                return this.entryPoint.transform(typeDescription, byteBuddy, classFileLocator, methodNameTransformer);
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Transformer.class */
    public interface Transformer {
        DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule);

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Transformer$ForBuildPlugin.class */
        public static class ForBuildPlugin implements Transformer {
            private final Plugin plugin;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.plugin.equals(((ForBuildPlugin) obj).plugin);
            }

            public int hashCode() {
                return (17 * 31) + this.plugin.hashCode();
            }

            public ForBuildPlugin(Plugin plugin) {
                this.plugin = plugin;
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Transformer
            public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module) {
                return this.plugin.apply(builder, typeDescription, ClassFileLocator.ForClassLoader.of(classLoader));
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Transformer$ForAdvice.class */
        public static class ForAdvice implements Transformer {
            private final Advice.WithCustomMapping advice;
            private final Advice.ExceptionHandler exceptionHandler;
            private final Assigner assigner;
            private final ClassFileLocator classFileLocator;
            private final PoolStrategy poolStrategy;
            private final LocationStrategy locationStrategy;
            private final List<Entry> entries;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.advice.equals(((ForAdvice) obj).advice) && this.exceptionHandler.equals(((ForAdvice) obj).exceptionHandler) && this.assigner.equals(((ForAdvice) obj).assigner) && this.classFileLocator.equals(((ForAdvice) obj).classFileLocator) && this.poolStrategy.equals(((ForAdvice) obj).poolStrategy) && this.locationStrategy.equals(((ForAdvice) obj).locationStrategy) && this.entries.equals(((ForAdvice) obj).entries);
            }

            public int hashCode() {
                return (((((((((((((17 * 31) + this.advice.hashCode()) * 31) + this.exceptionHandler.hashCode()) * 31) + this.assigner.hashCode()) * 31) + this.classFileLocator.hashCode()) * 31) + this.poolStrategy.hashCode()) * 31) + this.locationStrategy.hashCode()) * 31) + this.entries.hashCode();
            }

            public ForAdvice() {
                this(Advice.withCustomMapping());
            }

            public ForAdvice(Advice.WithCustomMapping advice) {
                this(advice, Advice.ExceptionHandler.Default.SUPPRESSING, Assigner.DEFAULT, ClassFileLocator.NoOp.INSTANCE, PoolStrategy.Default.FAST, LocationStrategy.ForClassLoader.STRONG, Collections.emptyList());
            }

            protected ForAdvice(Advice.WithCustomMapping advice, Advice.ExceptionHandler exceptionHandler, Assigner assigner, ClassFileLocator classFileLocator, PoolStrategy poolStrategy, LocationStrategy locationStrategy, List<Entry> entries) {
                this.advice = advice;
                this.exceptionHandler = exceptionHandler;
                this.assigner = assigner;
                this.classFileLocator = classFileLocator;
                this.poolStrategy = poolStrategy;
                this.locationStrategy = locationStrategy;
                this.entries = entries;
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Transformer
            public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module) {
                ClassFileLocator classFileLocator = new ClassFileLocator.Compound(this.classFileLocator, this.locationStrategy.classFileLocator(classLoader, module));
                TypePool typePool = this.poolStrategy.typePool(classFileLocator, classLoader);
                AsmVisitorWrapper.ForDeclaredMethods asmVisitorWrapper = new AsmVisitorWrapper.ForDeclaredMethods();
                for (Entry entry : this.entries) {
                    asmVisitorWrapper = asmVisitorWrapper.invokable(entry.getMatcher().resolve(typeDescription), entry.resolve(this.advice, typePool, classFileLocator).withAssigner(this.assigner).withExceptionHandler(this.exceptionHandler));
                }
                return builder.visit(asmVisitorWrapper);
            }

            public ForAdvice with(PoolStrategy poolStrategy) {
                return new ForAdvice(this.advice, this.exceptionHandler, this.assigner, this.classFileLocator, poolStrategy, this.locationStrategy, this.entries);
            }

            public ForAdvice with(LocationStrategy locationStrategy) {
                return new ForAdvice(this.advice, this.exceptionHandler, this.assigner, this.classFileLocator, this.poolStrategy, locationStrategy, this.entries);
            }

            public ForAdvice withExceptionHandler(Advice.ExceptionHandler exceptionHandler) {
                return new ForAdvice(this.advice, exceptionHandler, this.assigner, this.classFileLocator, this.poolStrategy, this.locationStrategy, this.entries);
            }

            public ForAdvice with(Assigner assigner) {
                return new ForAdvice(this.advice, this.exceptionHandler, assigner, this.classFileLocator, this.poolStrategy, this.locationStrategy, this.entries);
            }

            public ForAdvice include(ClassLoader... classLoader) {
                Set<ClassFileLocator> classFileLocators = new LinkedHashSet<>();
                for (ClassLoader aClassLoader : classLoader) {
                    classFileLocators.add(ClassFileLocator.ForClassLoader.of(aClassLoader));
                }
                return include(new ArrayList(classFileLocators));
            }

            public ForAdvice include(ClassFileLocator... classFileLocator) {
                return include(Arrays.asList(classFileLocator));
            }

            public ForAdvice include(List<? extends ClassFileLocator> classFileLocators) {
                return new ForAdvice(this.advice, this.exceptionHandler, this.assigner, new ClassFileLocator.Compound(CompoundList.of(this.classFileLocator, classFileLocators)), this.poolStrategy, this.locationStrategy, this.entries);
            }

            public ForAdvice advice(ElementMatcher<? super MethodDescription> matcher, String name) {
                return advice(new LatentMatcher.Resolved(matcher), name);
            }

            public ForAdvice advice(LatentMatcher<? super MethodDescription> matcher, String name) {
                return new ForAdvice(this.advice, this.exceptionHandler, this.assigner, this.classFileLocator, this.poolStrategy, this.locationStrategy, CompoundList.of(this.entries, new Entry.ForUnifiedAdvice(matcher, name)));
            }

            public ForAdvice advice(ElementMatcher<? super MethodDescription> matcher, String enter, String exit) {
                return advice(new LatentMatcher.Resolved(matcher), enter, exit);
            }

            public ForAdvice advice(LatentMatcher<? super MethodDescription> matcher, String enter, String exit) {
                return new ForAdvice(this.advice, this.exceptionHandler, this.assigner, this.classFileLocator, this.poolStrategy, this.locationStrategy, CompoundList.of(this.entries, new Entry.ForSplitAdvice(matcher, enter, exit)));
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Transformer$ForAdvice$Entry.class */
            public static abstract class Entry {
                private final LatentMatcher<? super MethodDescription> matcher;

                protected abstract Advice resolve(Advice.WithCustomMapping withCustomMapping, TypePool typePool, ClassFileLocator classFileLocator);

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.matcher.equals(((Entry) obj).matcher);
                }

                public int hashCode() {
                    return (17 * 31) + this.matcher.hashCode();
                }

                protected Entry(LatentMatcher<? super MethodDescription> matcher) {
                    this.matcher = matcher;
                }

                protected LatentMatcher<? super MethodDescription> getMatcher() {
                    return this.matcher;
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Transformer$ForAdvice$Entry$ForUnifiedAdvice.class */
                public static class ForUnifiedAdvice extends Entry {
                    protected final String name;

                    @Override // net.bytebuddy.agent.builder.AgentBuilder.Transformer.ForAdvice.Entry
                    public boolean equals(Object obj) {
                        if (super.equals(obj)) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.name.equals(((ForUnifiedAdvice) obj).name);
                        }
                        return false;
                    }

                    @Override // net.bytebuddy.agent.builder.AgentBuilder.Transformer.ForAdvice.Entry
                    public int hashCode() {
                        return (super.hashCode() * 31) + this.name.hashCode();
                    }

                    protected ForUnifiedAdvice(LatentMatcher<? super MethodDescription> matcher, String name) {
                        super(matcher);
                        this.name = name;
                    }

                    @Override // net.bytebuddy.agent.builder.AgentBuilder.Transformer.ForAdvice.Entry
                    protected Advice resolve(Advice.WithCustomMapping advice, TypePool typePool, ClassFileLocator classFileLocator) {
                        return advice.to(typePool.describe(this.name).resolve(), classFileLocator);
                    }
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Transformer$ForAdvice$Entry$ForSplitAdvice.class */
                public static class ForSplitAdvice extends Entry {
                    private final String enter;
                    private final String exit;

                    @Override // net.bytebuddy.agent.builder.AgentBuilder.Transformer.ForAdvice.Entry
                    public boolean equals(Object obj) {
                        if (super.equals(obj)) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.enter.equals(((ForSplitAdvice) obj).enter) && this.exit.equals(((ForSplitAdvice) obj).exit);
                        }
                        return false;
                    }

                    @Override // net.bytebuddy.agent.builder.AgentBuilder.Transformer.ForAdvice.Entry
                    public int hashCode() {
                        return (((super.hashCode() * 31) + this.enter.hashCode()) * 31) + this.exit.hashCode();
                    }

                    protected ForSplitAdvice(LatentMatcher<? super MethodDescription> matcher, String enter, String exit) {
                        super(matcher);
                        this.enter = enter;
                        this.exit = exit;
                    }

                    @Override // net.bytebuddy.agent.builder.AgentBuilder.Transformer.ForAdvice.Entry
                    protected Advice resolve(Advice.WithCustomMapping advice, TypePool typePool, ClassFileLocator classFileLocator) {
                        return advice.to(typePool.describe(this.enter).resolve(), typePool.describe(this.exit).resolve(), classFileLocator);
                    }
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$PoolStrategy.class */
    public interface PoolStrategy {
        TypePool typePool(ClassFileLocator classFileLocator, ClassLoader classLoader);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$PoolStrategy$Default.class */
        public enum Default implements PoolStrategy {
            EXTENDED(TypePool.Default.ReaderMode.EXTENDED),
            FAST(TypePool.Default.ReaderMode.FAST);
            
            private final TypePool.Default.ReaderMode readerMode;

            Default(TypePool.Default.ReaderMode readerMode) {
                this.readerMode = readerMode;
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.PoolStrategy
            public TypePool typePool(ClassFileLocator classFileLocator, ClassLoader classLoader) {
                return new TypePool.Default.WithLazyResolution(TypePool.CacheProvider.Simple.withObjectType(), classFileLocator, this.readerMode);
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$PoolStrategy$Eager.class */
        public enum Eager implements PoolStrategy {
            EXTENDED(TypePool.Default.ReaderMode.EXTENDED),
            FAST(TypePool.Default.ReaderMode.FAST);
            
            private final TypePool.Default.ReaderMode readerMode;

            Eager(TypePool.Default.ReaderMode readerMode) {
                this.readerMode = readerMode;
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.PoolStrategy
            public TypePool typePool(ClassFileLocator classFileLocator, ClassLoader classLoader) {
                return new TypePool.Default(TypePool.CacheProvider.Simple.withObjectType(), classFileLocator, this.readerMode);
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$PoolStrategy$ClassLoading.class */
        public enum ClassLoading implements PoolStrategy {
            EXTENDED(TypePool.Default.ReaderMode.EXTENDED),
            FAST(TypePool.Default.ReaderMode.FAST);
            
            private final TypePool.Default.ReaderMode readerMode;

            ClassLoading(TypePool.Default.ReaderMode readerMode) {
                this.readerMode = readerMode;
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.PoolStrategy
            public TypePool typePool(ClassFileLocator classFileLocator, ClassLoader classLoader) {
                return TypePool.ClassLoading.of(classLoader, new TypePool.Default.WithLazyResolution(TypePool.CacheProvider.Simple.withObjectType(), classFileLocator, this.readerMode));
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$PoolStrategy$WithTypePoolCache.class */
        public static abstract class WithTypePoolCache implements PoolStrategy {
            protected final TypePool.Default.ReaderMode readerMode;

            protected abstract TypePool.CacheProvider locate(ClassLoader classLoader);

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.readerMode.equals(((WithTypePoolCache) obj).readerMode);
            }

            public int hashCode() {
                return (17 * 31) + this.readerMode.hashCode();
            }

            protected WithTypePoolCache(TypePool.Default.ReaderMode readerMode) {
                this.readerMode = readerMode;
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.PoolStrategy
            public TypePool typePool(ClassFileLocator classFileLocator, ClassLoader classLoader) {
                return new TypePool.Default.WithLazyResolution(locate(classLoader), classFileLocator, this.readerMode);
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$PoolStrategy$WithTypePoolCache$Simple.class */
            public static class Simple extends WithTypePoolCache {
                private final ConcurrentMap<? super ClassLoader, TypePool.CacheProvider> cacheProviders;

                @Override // net.bytebuddy.agent.builder.AgentBuilder.PoolStrategy.WithTypePoolCache
                public boolean equals(Object obj) {
                    if (super.equals(obj)) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.cacheProviders.equals(((Simple) obj).cacheProviders);
                    }
                    return false;
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.PoolStrategy.WithTypePoolCache
                public int hashCode() {
                    return (super.hashCode() * 31) + this.cacheProviders.hashCode();
                }

                public Simple(ConcurrentMap<? super ClassLoader, TypePool.CacheProvider> cacheProviders) {
                    this(TypePool.Default.ReaderMode.FAST, cacheProviders);
                }

                public Simple(TypePool.Default.ReaderMode readerMode, ConcurrentMap<? super ClassLoader, TypePool.CacheProvider> cacheProviders) {
                    super(readerMode);
                    this.cacheProviders = cacheProviders;
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.PoolStrategy.WithTypePoolCache
                protected TypePool.CacheProvider locate(ClassLoader classLoader) {
                    ClassLoader classLoader2 = classLoader == null ? getBootstrapMarkerLoader() : classLoader;
                    TypePool.CacheProvider cacheProvider = this.cacheProviders.get(classLoader2);
                    while (cacheProvider == null) {
                        cacheProvider = TypePool.CacheProvider.Simple.withObjectType();
                        TypePool.CacheProvider previous = this.cacheProviders.putIfAbsent(classLoader2, cacheProvider);
                        if (previous != null) {
                            cacheProvider = previous;
                        }
                    }
                    return cacheProvider;
                }

                protected ClassLoader getBootstrapMarkerLoader() {
                    return ClassLoader.getSystemClassLoader();
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$InitializationStrategy.class */
    public interface InitializationStrategy {

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$InitializationStrategy$Dispatcher.class */
        public interface Dispatcher {
            DynamicType.Builder<?> apply(DynamicType.Builder<?> builder);

            void register(DynamicType dynamicType, ClassLoader classLoader, ProtectionDomain protectionDomain, InjectionStrategy injectionStrategy);
        }

        Dispatcher dispatcher();

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$InitializationStrategy$NoOp.class */
        public enum NoOp implements InitializationStrategy, Dispatcher {
            INSTANCE;

            @Override // net.bytebuddy.agent.builder.AgentBuilder.InitializationStrategy
            public Dispatcher dispatcher() {
                return this;
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.InitializationStrategy.Dispatcher
            public DynamicType.Builder<?> apply(DynamicType.Builder<?> builder) {
                return builder;
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.InitializationStrategy.Dispatcher
            public void register(DynamicType dynamicType, ClassLoader classLoader, ProtectionDomain protectionDomain, InjectionStrategy injectionStrategy) {
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$InitializationStrategy$Minimal.class */
        public enum Minimal implements InitializationStrategy, Dispatcher {
            INSTANCE;

            @Override // net.bytebuddy.agent.builder.AgentBuilder.InitializationStrategy
            public Dispatcher dispatcher() {
                return this;
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.InitializationStrategy.Dispatcher
            public DynamicType.Builder<?> apply(DynamicType.Builder<?> builder) {
                return builder;
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.InitializationStrategy.Dispatcher
            public void register(DynamicType dynamicType, ClassLoader classLoader, ProtectionDomain protectionDomain, InjectionStrategy injectionStrategy) {
                Map<TypeDescription, byte[]> auxiliaryTypes = dynamicType.getAuxiliaryTypes();
                Map<TypeDescription, byte[]> independentTypes = new LinkedHashMap<>(auxiliaryTypes);
                for (TypeDescription auxiliaryType : auxiliaryTypes.keySet()) {
                    if (!auxiliaryType.getDeclaredAnnotations().isAnnotationPresent(AuxiliaryType.SignatureRelevant.class)) {
                        independentTypes.remove(auxiliaryType);
                    }
                }
                if (!independentTypes.isEmpty()) {
                    ClassInjector classInjector = injectionStrategy.resolve(classLoader, protectionDomain);
                    Map<TypeDescription, LoadedTypeInitializer> loadedTypeInitializers = dynamicType.getLoadedTypeInitializers();
                    for (Map.Entry<TypeDescription, Class<?>> entry : classInjector.inject(independentTypes).entrySet()) {
                        loadedTypeInitializers.get(entry.getKey()).onLoad(entry.getValue());
                    }
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$InitializationStrategy$SelfInjection.class */
        public static abstract class SelfInjection implements InitializationStrategy {
            protected final NexusAccessor nexusAccessor;

            protected abstract Dispatcher dispatcher(int i);

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.nexusAccessor.equals(((SelfInjection) obj).nexusAccessor);
            }

            public int hashCode() {
                return (17 * 31) + this.nexusAccessor.hashCode();
            }

            protected SelfInjection(NexusAccessor nexusAccessor) {
                this.nexusAccessor = nexusAccessor;
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.InitializationStrategy
            @SuppressFBWarnings(value = {"DMI_RANDOM_USED_ONLY_ONCE"}, justification = "Avoiding synchronization without security concerns")
            public Dispatcher dispatcher() {
                return dispatcher(new Random().nextInt());
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$InitializationStrategy$SelfInjection$Dispatcher.class */
            protected static abstract class Dispatcher implements Dispatcher {
                protected final NexusAccessor nexusAccessor;
                protected final int identification;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.identification == ((Dispatcher) obj).identification && this.nexusAccessor.equals(((Dispatcher) obj).nexusAccessor);
                }

                public int hashCode() {
                    return (((17 * 31) + this.nexusAccessor.hashCode()) * 31) + this.identification;
                }

                protected Dispatcher(NexusAccessor nexusAccessor, int identification) {
                    this.nexusAccessor = nexusAccessor;
                    this.identification = identification;
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.InitializationStrategy.Dispatcher
                public DynamicType.Builder<?> apply(DynamicType.Builder<?> builder) {
                    return builder.initializer(new NexusAccessor.InitializationAppender(this.identification));
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$InitializationStrategy$SelfInjection$Dispatcher$InjectingInitializer.class */
                protected static class InjectingInitializer implements LoadedTypeInitializer {
                    private final TypeDescription instrumentedType;
                    private final Map<TypeDescription, byte[]> rawAuxiliaryTypes;
                    private final Map<TypeDescription, LoadedTypeInitializer> loadedTypeInitializers;
                    private final ClassInjector classInjector;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.instrumentedType.equals(((InjectingInitializer) obj).instrumentedType) && this.rawAuxiliaryTypes.equals(((InjectingInitializer) obj).rawAuxiliaryTypes) && this.loadedTypeInitializers.equals(((InjectingInitializer) obj).loadedTypeInitializers) && this.classInjector.equals(((InjectingInitializer) obj).classInjector);
                    }

                    public int hashCode() {
                        return (((((((17 * 31) + this.instrumentedType.hashCode()) * 31) + this.rawAuxiliaryTypes.hashCode()) * 31) + this.loadedTypeInitializers.hashCode()) * 31) + this.classInjector.hashCode();
                    }

                    protected InjectingInitializer(TypeDescription instrumentedType, Map<TypeDescription, byte[]> rawAuxiliaryTypes, Map<TypeDescription, LoadedTypeInitializer> loadedTypeInitializers, ClassInjector classInjector) {
                        this.instrumentedType = instrumentedType;
                        this.rawAuxiliaryTypes = rawAuxiliaryTypes;
                        this.loadedTypeInitializers = loadedTypeInitializers;
                        this.classInjector = classInjector;
                    }

                    @Override // net.bytebuddy.implementation.LoadedTypeInitializer
                    public void onLoad(Class<?> type) {
                        for (Map.Entry<TypeDescription, Class<?>> auxiliary : this.classInjector.inject(this.rawAuxiliaryTypes).entrySet()) {
                            this.loadedTypeInitializers.get(auxiliary.getKey()).onLoad(auxiliary.getValue());
                        }
                        this.loadedTypeInitializers.get(this.instrumentedType).onLoad(type);
                    }

                    @Override // net.bytebuddy.implementation.LoadedTypeInitializer
                    public boolean isAlive() {
                        return true;
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$InitializationStrategy$SelfInjection$Split.class */
            public static class Split extends SelfInjection {
                public Split() {
                    this(new NexusAccessor());
                }

                public Split(NexusAccessor nexusAccessor) {
                    super(nexusAccessor);
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.InitializationStrategy.SelfInjection
                protected Dispatcher dispatcher(int identification) {
                    return new Dispatcher(this.nexusAccessor, identification);
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$InitializationStrategy$SelfInjection$Split$Dispatcher.class */
                protected static class Dispatcher extends Dispatcher {
                    protected Dispatcher(NexusAccessor nexusAccessor, int identification) {
                        super(nexusAccessor, identification);
                    }

                    @Override // net.bytebuddy.agent.builder.AgentBuilder.InitializationStrategy.Dispatcher
                    public void register(DynamicType dynamicType, ClassLoader classLoader, ProtectionDomain protectionDomain, InjectionStrategy injectionStrategy) {
                        LoadedTypeInitializer loadedTypeInitializer;
                        Map<TypeDescription, byte[]> auxiliaryTypes = dynamicType.getAuxiliaryTypes();
                        if (!auxiliaryTypes.isEmpty()) {
                            TypeDescription instrumentedType = dynamicType.getTypeDescription();
                            ClassInjector classInjector = injectionStrategy.resolve(classLoader, protectionDomain);
                            Map<TypeDescription, byte[]> independentTypes = new LinkedHashMap<>(auxiliaryTypes);
                            Map<TypeDescription, byte[]> dependentTypes = new LinkedHashMap<>(auxiliaryTypes);
                            for (TypeDescription auxiliaryType : auxiliaryTypes.keySet()) {
                                (auxiliaryType.getDeclaredAnnotations().isAnnotationPresent(AuxiliaryType.SignatureRelevant.class) ? dependentTypes : independentTypes).remove(auxiliaryType);
                            }
                            Map<TypeDescription, LoadedTypeInitializer> loadedTypeInitializers = dynamicType.getLoadedTypeInitializers();
                            if (!independentTypes.isEmpty()) {
                                for (Map.Entry<TypeDescription, Class<?>> entry : classInjector.inject(independentTypes).entrySet()) {
                                    loadedTypeInitializers.get(entry.getKey()).onLoad(entry.getValue());
                                }
                            }
                            Map<TypeDescription, LoadedTypeInitializer> lazyInitializers = new HashMap<>(loadedTypeInitializers);
                            loadedTypeInitializers.keySet().removeAll(independentTypes.keySet());
                            loadedTypeInitializer = lazyInitializers.size() > 1 ? new Dispatcher.InjectingInitializer(instrumentedType, dependentTypes, lazyInitializers, classInjector) : lazyInitializers.get(instrumentedType);
                        } else {
                            loadedTypeInitializer = dynamicType.getLoadedTypeInitializers().get(dynamicType.getTypeDescription());
                        }
                        this.nexusAccessor.register(dynamicType.getTypeDescription().getName(), classLoader, this.identification, loadedTypeInitializer);
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$InitializationStrategy$SelfInjection$Lazy.class */
            public static class Lazy extends SelfInjection {
                public Lazy() {
                    this(new NexusAccessor());
                }

                public Lazy(NexusAccessor nexusAccessor) {
                    super(nexusAccessor);
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.InitializationStrategy.SelfInjection
                protected Dispatcher dispatcher(int identification) {
                    return new Dispatcher(this.nexusAccessor, identification);
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$InitializationStrategy$SelfInjection$Lazy$Dispatcher.class */
                protected static class Dispatcher extends Dispatcher {
                    protected Dispatcher(NexusAccessor nexusAccessor, int identification) {
                        super(nexusAccessor, identification);
                    }

                    @Override // net.bytebuddy.agent.builder.AgentBuilder.InitializationStrategy.Dispatcher
                    public void register(DynamicType dynamicType, ClassLoader classLoader, ProtectionDomain protectionDomain, InjectionStrategy injectionStrategy) {
                        LoadedTypeInitializer injectingInitializer;
                        Map<TypeDescription, byte[]> auxiliaryTypes = dynamicType.getAuxiliaryTypes();
                        if (auxiliaryTypes.isEmpty()) {
                            injectingInitializer = dynamicType.getLoadedTypeInitializers().get(dynamicType.getTypeDescription());
                        } else {
                            injectingInitializer = new Dispatcher.InjectingInitializer(dynamicType.getTypeDescription(), auxiliaryTypes, dynamicType.getLoadedTypeInitializers(), injectionStrategy.resolve(classLoader, protectionDomain));
                        }
                        LoadedTypeInitializer loadedTypeInitializer = injectingInitializer;
                        this.nexusAccessor.register(dynamicType.getTypeDescription().getName(), classLoader, this.identification, loadedTypeInitializer);
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$InitializationStrategy$SelfInjection$Eager.class */
            public static class Eager extends SelfInjection {
                public Eager() {
                    this(new NexusAccessor());
                }

                public Eager(NexusAccessor nexusAccessor) {
                    super(nexusAccessor);
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.InitializationStrategy.SelfInjection
                protected Dispatcher dispatcher(int identification) {
                    return new Dispatcher(this.nexusAccessor, identification);
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$InitializationStrategy$SelfInjection$Eager$Dispatcher.class */
                protected static class Dispatcher extends Dispatcher {
                    protected Dispatcher(NexusAccessor nexusAccessor, int identification) {
                        super(nexusAccessor, identification);
                    }

                    @Override // net.bytebuddy.agent.builder.AgentBuilder.InitializationStrategy.Dispatcher
                    public void register(DynamicType dynamicType, ClassLoader classLoader, ProtectionDomain protectionDomain, InjectionStrategy injectionStrategy) {
                        Map<TypeDescription, byte[]> auxiliaryTypes = dynamicType.getAuxiliaryTypes();
                        Map<TypeDescription, LoadedTypeInitializer> loadedTypeInitializers = dynamicType.getLoadedTypeInitializers();
                        if (!auxiliaryTypes.isEmpty()) {
                            for (Map.Entry<TypeDescription, Class<?>> entry : injectionStrategy.resolve(classLoader, protectionDomain).inject(auxiliaryTypes).entrySet()) {
                                loadedTypeInitializers.get(entry.getKey()).onLoad(entry.getValue());
                            }
                        }
                        LoadedTypeInitializer loadedTypeInitializer = loadedTypeInitializers.get(dynamicType.getTypeDescription());
                        this.nexusAccessor.register(dynamicType.getTypeDescription().getName(), classLoader, this.identification, loadedTypeInitializer);
                    }
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$InjectionStrategy.class */
    public interface InjectionStrategy {
        ClassInjector resolve(ClassLoader classLoader, ProtectionDomain protectionDomain);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$InjectionStrategy$Disabled.class */
        public enum Disabled implements InjectionStrategy {
            INSTANCE;

            @Override // net.bytebuddy.agent.builder.AgentBuilder.InjectionStrategy
            public ClassInjector resolve(ClassLoader classLoader, ProtectionDomain protectionDomain) {
                throw new IllegalStateException("Class injection is disabled");
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$InjectionStrategy$UsingReflection.class */
        public enum UsingReflection implements InjectionStrategy {
            INSTANCE;

            @Override // net.bytebuddy.agent.builder.AgentBuilder.InjectionStrategy
            public ClassInjector resolve(ClassLoader classLoader, ProtectionDomain protectionDomain) {
                if (classLoader == null) {
                    throw new IllegalStateException("Cannot inject auxiliary class into bootstrap loader using reflection");
                }
                if (ClassInjector.UsingReflection.isAvailable()) {
                    return new ClassInjector.UsingReflection(classLoader, protectionDomain);
                }
                throw new IllegalStateException("Reflection-based injection is not available on the current VM");
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$InjectionStrategy$UsingUnsafe.class */
        public enum UsingUnsafe implements InjectionStrategy {
            INSTANCE;

            @Override // net.bytebuddy.agent.builder.AgentBuilder.InjectionStrategy
            public ClassInjector resolve(ClassLoader classLoader, ProtectionDomain protectionDomain) {
                if (ClassInjector.UsingUnsafe.isAvailable()) {
                    return new ClassInjector.UsingUnsafe(classLoader, protectionDomain);
                }
                throw new IllegalStateException("Unsafe-based injection is not available on the current VM");
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$InjectionStrategy$UsingUnsafe$OfFactory.class */
            public static class OfFactory implements InjectionStrategy {
                private final ClassInjector.UsingUnsafe.Factory factory;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.factory.equals(((OfFactory) obj).factory);
                }

                public int hashCode() {
                    return (17 * 31) + this.factory.hashCode();
                }

                public OfFactory(ClassInjector.UsingUnsafe.Factory factory) {
                    this.factory = factory;
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.InjectionStrategy
                public ClassInjector resolve(ClassLoader classLoader, ProtectionDomain protectionDomain) {
                    return this.factory.make(classLoader, protectionDomain);
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$InjectionStrategy$UsingInstrumentation.class */
        public static class UsingInstrumentation implements InjectionStrategy {
            private final Instrumentation instrumentation;
            private final File folder;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.instrumentation.equals(((UsingInstrumentation) obj).instrumentation) && this.folder.equals(((UsingInstrumentation) obj).folder);
            }

            public int hashCode() {
                return (((17 * 31) + this.instrumentation.hashCode()) * 31) + this.folder.hashCode();
            }

            public UsingInstrumentation(Instrumentation instrumentation, File folder) {
                this.instrumentation = instrumentation;
                this.folder = folder;
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.InjectionStrategy
            public ClassInjector resolve(ClassLoader classLoader, ProtectionDomain protectionDomain) {
                if (classLoader == null) {
                    return ClassInjector.UsingInstrumentation.of(this.folder, ClassInjector.UsingInstrumentation.Target.BOOTSTRAP, this.instrumentation);
                }
                return UsingReflection.INSTANCE.resolve(classLoader, protectionDomain);
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$DescriptionStrategy.class */
    public interface DescriptionStrategy {
        boolean isLoadedFirst();

        TypeDescription apply(String str, Class<?> cls, TypePool typePool, CircularityLock circularityLock, ClassLoader classLoader, JavaModule javaModule);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$DescriptionStrategy$Default.class */
        public enum Default implements DescriptionStrategy {
            HYBRID(true) { // from class: net.bytebuddy.agent.builder.AgentBuilder.DescriptionStrategy.Default.1
                @Override // net.bytebuddy.agent.builder.AgentBuilder.DescriptionStrategy
                public TypeDescription apply(String typeName, Class<?> type, TypePool typePool, CircularityLock circularityLock, ClassLoader classLoader, JavaModule module) {
                    if (type == null) {
                        return typePool.describe(typeName).resolve();
                    }
                    return TypeDescription.ForLoadedType.of(type);
                }
            },
            POOL_ONLY(false) { // from class: net.bytebuddy.agent.builder.AgentBuilder.DescriptionStrategy.Default.2
                @Override // net.bytebuddy.agent.builder.AgentBuilder.DescriptionStrategy
                public TypeDescription apply(String typeName, Class<?> type, TypePool typePool, CircularityLock circularityLock, ClassLoader classLoader, JavaModule module) {
                    return typePool.describe(typeName).resolve();
                }
            },
            POOL_FIRST(false) { // from class: net.bytebuddy.agent.builder.AgentBuilder.DescriptionStrategy.Default.3
                @Override // net.bytebuddy.agent.builder.AgentBuilder.DescriptionStrategy
                public TypeDescription apply(String typeName, Class<?> type, TypePool typePool, CircularityLock circularityLock, ClassLoader classLoader, JavaModule module) {
                    TypePool.Resolution resolution = typePool.describe(typeName);
                    if (resolution.isResolved() || type == null) {
                        return resolution.resolve();
                    }
                    return TypeDescription.ForLoadedType.of(type);
                }
            };
            
            private final boolean loadedFirst;

            Default(boolean loadedFirst) {
                this.loadedFirst = loadedFirst;
            }

            public DescriptionStrategy withSuperTypeLoading() {
                return new SuperTypeLoading(this);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.DescriptionStrategy
            public boolean isLoadedFirst() {
                return this.loadedFirst;
            }

            public DescriptionStrategy withSuperTypeLoading(ExecutorService executorService) {
                return new SuperTypeLoading.Asynchronous(this, executorService);
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$DescriptionStrategy$SuperTypeLoading.class */
        public static class SuperTypeLoading implements DescriptionStrategy {
            private final DescriptionStrategy delegate;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.delegate.equals(((SuperTypeLoading) obj).delegate);
            }

            public int hashCode() {
                return (17 * 31) + this.delegate.hashCode();
            }

            public SuperTypeLoading(DescriptionStrategy delegate) {
                this.delegate = delegate;
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.DescriptionStrategy
            public boolean isLoadedFirst() {
                return this.delegate.isLoadedFirst();
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.DescriptionStrategy
            public TypeDescription apply(String typeName, Class<?> type, TypePool typePool, CircularityLock circularityLock, ClassLoader classLoader, JavaModule module) {
                TypeDescription typeDescription = this.delegate.apply(typeName, type, typePool, circularityLock, classLoader, module);
                return typeDescription instanceof TypeDescription.ForLoadedType ? typeDescription : new TypeDescription.SuperTypeLoading(typeDescription, classLoader, new UnlockingClassLoadingDelegate(circularityLock));
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$DescriptionStrategy$SuperTypeLoading$UnlockingClassLoadingDelegate.class */
            protected static class UnlockingClassLoadingDelegate implements TypeDescription.SuperTypeLoading.ClassLoadingDelegate {
                private final CircularityLock circularityLock;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.circularityLock.equals(((UnlockingClassLoadingDelegate) obj).circularityLock);
                }

                public int hashCode() {
                    return (17 * 31) + this.circularityLock.hashCode();
                }

                protected UnlockingClassLoadingDelegate(CircularityLock circularityLock) {
                    this.circularityLock = circularityLock;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.SuperTypeLoading.ClassLoadingDelegate
                public Class<?> load(String name, ClassLoader classLoader) throws ClassNotFoundException {
                    this.circularityLock.release();
                    try {
                        Class<?> cls = Class.forName(name, false, classLoader);
                        this.circularityLock.acquire();
                        return cls;
                    } catch (Throwable th) {
                        this.circularityLock.acquire();
                        throw th;
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$DescriptionStrategy$SuperTypeLoading$Asynchronous.class */
            public static class Asynchronous implements DescriptionStrategy {
                private final DescriptionStrategy delegate;
                private final ExecutorService executorService;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.delegate.equals(((Asynchronous) obj).delegate) && this.executorService.equals(((Asynchronous) obj).executorService);
                }

                public int hashCode() {
                    return (((17 * 31) + this.delegate.hashCode()) * 31) + this.executorService.hashCode();
                }

                public Asynchronous(DescriptionStrategy delegate, ExecutorService executorService) {
                    this.delegate = delegate;
                    this.executorService = executorService;
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.DescriptionStrategy
                public boolean isLoadedFirst() {
                    return this.delegate.isLoadedFirst();
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.DescriptionStrategy
                public TypeDescription apply(String typeName, Class<?> type, TypePool typePool, CircularityLock circularityLock, ClassLoader classLoader, JavaModule module) {
                    TypeDescription typeDescription = this.delegate.apply(typeName, type, typePool, circularityLock, classLoader, module);
                    return typeDescription instanceof TypeDescription.ForLoadedType ? typeDescription : new TypeDescription.SuperTypeLoading(typeDescription, classLoader, new ThreadSwitchingClassLoadingDelegate(this.executorService));
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$DescriptionStrategy$SuperTypeLoading$Asynchronous$ThreadSwitchingClassLoadingDelegate.class */
                protected static class ThreadSwitchingClassLoadingDelegate implements TypeDescription.SuperTypeLoading.ClassLoadingDelegate {
                    private final ExecutorService executorService;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.executorService.equals(((ThreadSwitchingClassLoadingDelegate) obj).executorService);
                    }

                    public int hashCode() {
                        return (17 * 31) + this.executorService.hashCode();
                    }

                    protected ThreadSwitchingClassLoadingDelegate(ExecutorService executorService) {
                        this.executorService = executorService;
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.SuperTypeLoading.ClassLoadingDelegate
                    public Class<?> load(String name, ClassLoader classLoader) {
                        boolean holdsLock = classLoader != null && Thread.holdsLock(classLoader);
                        AtomicBoolean signal = new AtomicBoolean(holdsLock);
                        Future<Class<?>> future = this.executorService.submit(holdsLock ? new NotifyingClassLoadingAction(name, classLoader, signal) : new SimpleClassLoadingAction(name, classLoader));
                        while (holdsLock) {
                            try {
                                if (!signal.get()) {
                                    break;
                                }
                                classLoader.wait();
                            } catch (ExecutionException exception) {
                                throw new IllegalStateException("Could not load " + name + " asynchronously", exception.getCause());
                            } catch (Exception exception2) {
                                throw new IllegalStateException("Could not load " + name + " asynchronously", exception2);
                            }
                        }
                        return future.get();
                    }

                    @HashCodeAndEqualsPlugin.Enhance
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$DescriptionStrategy$SuperTypeLoading$Asynchronous$ThreadSwitchingClassLoadingDelegate$SimpleClassLoadingAction.class */
                    protected static class SimpleClassLoadingAction implements Callable<Class<?>> {
                        private final String name;
                        private final ClassLoader classLoader;

                        public boolean equals(Object obj) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.name.equals(((SimpleClassLoadingAction) obj).name) && this.classLoader.equals(((SimpleClassLoadingAction) obj).classLoader);
                        }

                        public int hashCode() {
                            return (((17 * 31) + this.name.hashCode()) * 31) + this.classLoader.hashCode();
                        }

                        protected SimpleClassLoadingAction(String name, ClassLoader classLoader) {
                            this.name = name;
                            this.classLoader = classLoader;
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.util.concurrent.Callable
                        public Class<?> call() throws ClassNotFoundException {
                            return Class.forName(this.name, false, this.classLoader);
                        }
                    }

                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$DescriptionStrategy$SuperTypeLoading$Asynchronous$ThreadSwitchingClassLoadingDelegate$NotifyingClassLoadingAction.class */
                    protected static class NotifyingClassLoadingAction implements Callable<Class<?>> {
                        private final String name;
                        private final ClassLoader classLoader;
                        private final AtomicBoolean signal;

                        protected NotifyingClassLoadingAction(String name, ClassLoader classLoader, AtomicBoolean signal) {
                            this.name = name;
                            this.classLoader = classLoader;
                            this.signal = signal;
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.util.concurrent.Callable
                        public Class<?> call() throws ClassNotFoundException {
                            Class<?> cls;
                            synchronized (this.classLoader) {
                                cls = Class.forName(this.name, false, this.classLoader);
                                this.signal.set(false);
                                this.classLoader.notifyAll();
                            }
                            return cls;
                        }
                    }
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$LocationStrategy.class */
    public interface LocationStrategy {
        ClassFileLocator classFileLocator(ClassLoader classLoader, JavaModule javaModule);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$LocationStrategy$NoOp.class */
        public enum NoOp implements LocationStrategy {
            INSTANCE;

            @Override // net.bytebuddy.agent.builder.AgentBuilder.LocationStrategy
            public ClassFileLocator classFileLocator(ClassLoader classLoader, JavaModule module) {
                return ClassFileLocator.NoOp.INSTANCE;
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$LocationStrategy$ForClassLoader.class */
        public enum ForClassLoader implements LocationStrategy {
            STRONG { // from class: net.bytebuddy.agent.builder.AgentBuilder.LocationStrategy.ForClassLoader.1
                @Override // net.bytebuddy.agent.builder.AgentBuilder.LocationStrategy
                public ClassFileLocator classFileLocator(ClassLoader classLoader, JavaModule module) {
                    return ClassFileLocator.ForClassLoader.of(classLoader);
                }
            },
            WEAK { // from class: net.bytebuddy.agent.builder.AgentBuilder.LocationStrategy.ForClassLoader.2
                @Override // net.bytebuddy.agent.builder.AgentBuilder.LocationStrategy
                public ClassFileLocator classFileLocator(ClassLoader classLoader, JavaModule module) {
                    return ClassFileLocator.ForClassLoader.WeaklyReferenced.of(classLoader);
                }
            };

            public LocationStrategy withFallbackTo(ClassFileLocator... classFileLocator) {
                return withFallbackTo((Collection<? extends ClassFileLocator>) Arrays.asList(classFileLocator));
            }

            public LocationStrategy withFallbackTo(Collection<? extends ClassFileLocator> classFileLocators) {
                List<LocationStrategy> locationStrategies = new ArrayList<>(classFileLocators.size());
                for (ClassFileLocator classFileLocator : classFileLocators) {
                    locationStrategies.add(new Simple(classFileLocator));
                }
                return withFallbackTo((List<? extends LocationStrategy>) locationStrategies);
            }

            public LocationStrategy withFallbackTo(LocationStrategy... locationStrategy) {
                return withFallbackTo(Arrays.asList(locationStrategy));
            }

            public LocationStrategy withFallbackTo(List<? extends LocationStrategy> locationStrategies) {
                List<LocationStrategy> allLocationStrategies = new ArrayList<>(locationStrategies.size() + 1);
                allLocationStrategies.add(this);
                allLocationStrategies.addAll(locationStrategies);
                return new Compound(allLocationStrategies);
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$LocationStrategy$Simple.class */
        public static class Simple implements LocationStrategy {
            private final ClassFileLocator classFileLocator;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.classFileLocator.equals(((Simple) obj).classFileLocator);
            }

            public int hashCode() {
                return (17 * 31) + this.classFileLocator.hashCode();
            }

            public Simple(ClassFileLocator classFileLocator) {
                this.classFileLocator = classFileLocator;
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.LocationStrategy
            public ClassFileLocator classFileLocator(ClassLoader classLoader, JavaModule module) {
                return this.classFileLocator;
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$LocationStrategy$Compound.class */
        public static class Compound implements LocationStrategy {
            private final List<LocationStrategy> locationStrategies;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.locationStrategies.equals(((Compound) obj).locationStrategies);
            }

            public int hashCode() {
                return (17 * 31) + this.locationStrategies.hashCode();
            }

            public Compound(LocationStrategy... locationStrategy) {
                this(Arrays.asList(locationStrategy));
            }

            public Compound(List<? extends LocationStrategy> locationStrategies) {
                this.locationStrategies = new ArrayList();
                for (LocationStrategy locationStrategy : locationStrategies) {
                    if (locationStrategy instanceof Compound) {
                        this.locationStrategies.addAll(((Compound) locationStrategy).locationStrategies);
                    } else if (!(locationStrategy instanceof NoOp)) {
                        this.locationStrategies.add(locationStrategy);
                    }
                }
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.LocationStrategy
            public ClassFileLocator classFileLocator(ClassLoader classLoader, JavaModule module) {
                List<ClassFileLocator> classFileLocators = new ArrayList<>(this.locationStrategies.size());
                for (LocationStrategy locationStrategy : this.locationStrategies) {
                    classFileLocators.add(locationStrategy.classFileLocator(classLoader, module));
                }
                return new ClassFileLocator.Compound(classFileLocators);
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$FallbackStrategy.class */
    public interface FallbackStrategy {
        boolean isFallback(Class<?> cls, Throwable th);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$FallbackStrategy$Simple.class */
        public enum Simple implements FallbackStrategy {
            ENABLED(true),
            DISABLED(false);
            
            private final boolean enabled;

            Simple(boolean enabled) {
                this.enabled = enabled;
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.FallbackStrategy
            public boolean isFallback(Class<?> type, Throwable throwable) {
                return this.enabled;
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$FallbackStrategy$ByThrowableType.class */
        public static class ByThrowableType implements FallbackStrategy {
            private final Set<? extends Class<? extends Throwable>> types;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.types.equals(((ByThrowableType) obj).types);
            }

            public int hashCode() {
                return (17 * 31) + this.types.hashCode();
            }

            public ByThrowableType(Class<? extends Throwable>... type) {
                this(new HashSet(Arrays.asList(type)));
            }

            public ByThrowableType(Set<? extends Class<? extends Throwable>> types) {
                this.types = types;
            }

            public static FallbackStrategy ofOptionalTypes() {
                return new ByThrowableType(LinkageError.class, TypeNotPresentException.class);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.FallbackStrategy
            public boolean isFallback(Class<?> type, Throwable throwable) {
                for (Class<? extends Throwable> aType : this.types) {
                    if (aType.isInstance(throwable)) {
                        return true;
                    }
                }
                return false;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$InstallationListener.class */
    public interface InstallationListener {
        public static final Throwable SUPPRESS_ERROR = null;

        void onBeforeInstall(Instrumentation instrumentation, ResettableClassFileTransformer resettableClassFileTransformer);

        void onInstall(Instrumentation instrumentation, ResettableClassFileTransformer resettableClassFileTransformer);

        Throwable onError(Instrumentation instrumentation, ResettableClassFileTransformer resettableClassFileTransformer, Throwable th);

        void onReset(Instrumentation instrumentation, ResettableClassFileTransformer resettableClassFileTransformer);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$InstallationListener$NoOp.class */
        public enum NoOp implements InstallationListener {
            INSTANCE;

            @Override // net.bytebuddy.agent.builder.AgentBuilder.InstallationListener
            public void onBeforeInstall(Instrumentation instrumentation, ResettableClassFileTransformer classFileTransformer) {
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.InstallationListener
            public void onInstall(Instrumentation instrumentation, ResettableClassFileTransformer classFileTransformer) {
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.InstallationListener
            public Throwable onError(Instrumentation instrumentation, ResettableClassFileTransformer classFileTransformer, Throwable throwable) {
                return throwable;
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.InstallationListener
            public void onReset(Instrumentation instrumentation, ResettableClassFileTransformer classFileTransformer) {
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$InstallationListener$ErrorSuppressing.class */
        public enum ErrorSuppressing implements InstallationListener {
            INSTANCE;

            @Override // net.bytebuddy.agent.builder.AgentBuilder.InstallationListener
            public void onBeforeInstall(Instrumentation instrumentation, ResettableClassFileTransformer classFileTransformer) {
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.InstallationListener
            public void onInstall(Instrumentation instrumentation, ResettableClassFileTransformer classFileTransformer) {
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.InstallationListener
            public Throwable onError(Instrumentation instrumentation, ResettableClassFileTransformer classFileTransformer, Throwable throwable) {
                return SUPPRESS_ERROR;
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.InstallationListener
            public void onReset(Instrumentation instrumentation, ResettableClassFileTransformer classFileTransformer) {
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$InstallationListener$Adapter.class */
        public static abstract class Adapter implements InstallationListener {
            @Override // net.bytebuddy.agent.builder.AgentBuilder.InstallationListener
            public void onBeforeInstall(Instrumentation instrumentation, ResettableClassFileTransformer classFileTransformer) {
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.InstallationListener
            public void onInstall(Instrumentation instrumentation, ResettableClassFileTransformer classFileTransformer) {
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.InstallationListener
            public Throwable onError(Instrumentation instrumentation, ResettableClassFileTransformer classFileTransformer, Throwable throwable) {
                return throwable;
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.InstallationListener
            public void onReset(Instrumentation instrumentation, ResettableClassFileTransformer classFileTransformer) {
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$InstallationListener$StreamWriting.class */
        public static class StreamWriting implements InstallationListener {
            protected static final String PREFIX = "[Byte Buddy]";
            private final PrintStream printStream;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.printStream.equals(((StreamWriting) obj).printStream);
            }

            public int hashCode() {
                return (17 * 31) + this.printStream.hashCode();
            }

            public StreamWriting(PrintStream printStream) {
                this.printStream = printStream;
            }

            public static InstallationListener toSystemOut() {
                return new StreamWriting(System.out);
            }

            public static InstallationListener toSystemError() {
                return new StreamWriting(System.err);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.InstallationListener
            public void onBeforeInstall(Instrumentation instrumentation, ResettableClassFileTransformer classFileTransformer) {
                this.printStream.printf("[Byte Buddy] BEFORE_INSTALL %s on %s%n", classFileTransformer, instrumentation);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.InstallationListener
            public void onInstall(Instrumentation instrumentation, ResettableClassFileTransformer classFileTransformer) {
                this.printStream.printf("[Byte Buddy] INSTALL %s on %s%n", classFileTransformer, instrumentation);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.InstallationListener
            public Throwable onError(Instrumentation instrumentation, ResettableClassFileTransformer classFileTransformer, Throwable throwable) {
                synchronized (this.printStream) {
                    this.printStream.printf("[Byte Buddy] ERROR %s on %s%n", classFileTransformer, instrumentation);
                    throwable.printStackTrace(this.printStream);
                }
                return throwable;
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.InstallationListener
            public void onReset(Instrumentation instrumentation, ResettableClassFileTransformer classFileTransformer) {
                this.printStream.printf("[Byte Buddy] RESET %s on %s%n", classFileTransformer, instrumentation);
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$InstallationListener$Compound.class */
        public static class Compound implements InstallationListener {
            private final List<InstallationListener> installationListeners;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.installationListeners.equals(((Compound) obj).installationListeners);
            }

            public int hashCode() {
                return (17 * 31) + this.installationListeners.hashCode();
            }

            public Compound(InstallationListener... installationListener) {
                this(Arrays.asList(installationListener));
            }

            public Compound(List<? extends InstallationListener> installationListeners) {
                this.installationListeners = new ArrayList();
                for (InstallationListener installationListener : installationListeners) {
                    if (installationListener instanceof Compound) {
                        this.installationListeners.addAll(((Compound) installationListener).installationListeners);
                    } else if (!(installationListener instanceof NoOp)) {
                        this.installationListeners.add(installationListener);
                    }
                }
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.InstallationListener
            public void onBeforeInstall(Instrumentation instrumentation, ResettableClassFileTransformer classFileTransformer) {
                for (InstallationListener installationListener : this.installationListeners) {
                    installationListener.onBeforeInstall(instrumentation, classFileTransformer);
                }
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.InstallationListener
            public void onInstall(Instrumentation instrumentation, ResettableClassFileTransformer classFileTransformer) {
                for (InstallationListener installationListener : this.installationListeners) {
                    installationListener.onInstall(instrumentation, classFileTransformer);
                }
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.InstallationListener
            public Throwable onError(Instrumentation instrumentation, ResettableClassFileTransformer classFileTransformer, Throwable throwable) {
                for (InstallationListener installationListener : this.installationListeners) {
                    if (throwable == SUPPRESS_ERROR) {
                        return SUPPRESS_ERROR;
                    }
                    throwable = installationListener.onError(instrumentation, classFileTransformer, throwable);
                }
                return throwable;
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.InstallationListener
            public void onReset(Instrumentation instrumentation, ResettableClassFileTransformer classFileTransformer) {
                for (InstallationListener installationListener : this.installationListeners) {
                    installationListener.onReset(instrumentation, classFileTransformer);
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$TransformerDecorator.class */
    public interface TransformerDecorator {
        ResettableClassFileTransformer decorate(ResettableClassFileTransformer resettableClassFileTransformer);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$TransformerDecorator$NoOp.class */
        public enum NoOp implements TransformerDecorator {
            INSTANCE;

            @Override // net.bytebuddy.agent.builder.AgentBuilder.TransformerDecorator
            public ResettableClassFileTransformer decorate(ResettableClassFileTransformer classFileTransformer) {
                return classFileTransformer;
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$TransformerDecorator$Compound.class */
        public static class Compound implements TransformerDecorator {
            private final List<TransformerDecorator> transformerDecorators;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.transformerDecorators.equals(((Compound) obj).transformerDecorators);
            }

            public int hashCode() {
                return (17 * 31) + this.transformerDecorators.hashCode();
            }

            public Compound(TransformerDecorator... transformerDecorator) {
                this(Arrays.asList(transformerDecorator));
            }

            public Compound(List<? extends TransformerDecorator> transformerDecorators) {
                this.transformerDecorators = new ArrayList();
                for (TransformerDecorator transformerDecorator : transformerDecorators) {
                    if (transformerDecorator instanceof Compound) {
                        this.transformerDecorators.addAll(((Compound) transformerDecorator).transformerDecorators);
                    } else if (!(transformerDecorator instanceof NoOp)) {
                        this.transformerDecorators.add(transformerDecorator);
                    }
                }
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.TransformerDecorator
            public ResettableClassFileTransformer decorate(ResettableClassFileTransformer classFileTransformer) {
                for (TransformerDecorator transformerDecorator : this.transformerDecorators) {
                    classFileTransformer = transformerDecorator.decorate(classFileTransformer);
                }
                return classFileTransformer;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy.class */
    public enum RedefinitionStrategy {
        DISABLED(false, false) { // from class: net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.1
            @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy
            public void apply(Instrumentation instrumentation, Listener listener, CircularityLock circularityLock, PoolStrategy poolStrategy, LocationStrategy locationStrategy, DiscoveryStrategy discoveryStrategy, BatchAllocator redefinitionBatchAllocator, Listener redefinitionListener, LambdaInstrumentationStrategy lambdaInstrumentationStrategy, DescriptionStrategy descriptionStrategy, FallbackStrategy fallbackStrategy, RawMatcher matcher) {
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy
            protected void check(Instrumentation instrumentation) {
                throw new IllegalStateException("Cannot apply redefinition on disabled strategy");
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy
            protected Collector make() {
                throw new IllegalStateException("A disabled redefinition strategy cannot create a collector");
            }
        },
        REDEFINITION(true, false) { // from class: net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.2
            @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy
            protected void check(Instrumentation instrumentation) {
                if (!instrumentation.isRedefineClassesSupported()) {
                    throw new IllegalStateException("Cannot apply redefinition on " + instrumentation);
                }
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy
            protected Collector make() {
                return new Collector.ForRedefinition();
            }
        },
        RETRANSFORMATION(true, true) { // from class: net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.3
            @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy
            protected void check(Instrumentation instrumentation) {
                if (!DISPATCHER.isRetransformClassesSupported(instrumentation)) {
                    throw new IllegalStateException("Cannot apply retransformation on " + instrumentation);
                }
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy
            protected Collector make() {
                return new Collector.ForRetransformation();
            }
        };
        
        protected static final Dispatcher DISPATCHER = (Dispatcher) AccessController.doPrivileged(Dispatcher.CreationAction.INSTANCE);
        private final boolean enabled;
        private final boolean retransforming;

        protected abstract void check(Instrumentation instrumentation);

        protected abstract Collector make();

        RedefinitionStrategy(boolean enabled, boolean retransforming) {
            this.enabled = enabled;
            this.retransforming = retransforming;
        }

        protected boolean isRetransforming() {
            return this.retransforming;
        }

        protected boolean isEnabled() {
            return this.enabled;
        }

        protected void apply(Instrumentation instrumentation, Listener listener, CircularityLock circularityLock, PoolStrategy poolStrategy, LocationStrategy locationStrategy, DiscoveryStrategy redefinitionDiscoveryStrategy, BatchAllocator redefinitionBatchAllocator, Listener redefinitionListener, LambdaInstrumentationStrategy lambdaInstrumentationStrategy, DescriptionStrategy descriptionStrategy, FallbackStrategy fallbackStrategy, RawMatcher matcher) {
            check(instrumentation);
            int batch = 0;
            for (Iterable<Class<?>> types : redefinitionDiscoveryStrategy.resolve(instrumentation)) {
                Collector collector = make();
                for (Class<?> type : types) {
                    if (type != null && !type.isArray() && lambdaInstrumentationStrategy.isInstrumented(type)) {
                        JavaModule module = JavaModule.ofType(type);
                        try {
                            TypePool typePool = poolStrategy.typePool(locationStrategy.classFileLocator(type.getClassLoader(), module), type.getClassLoader());
                            collector.consider(matcher, listener, descriptionStrategy.apply(TypeDescription.ForLoadedType.getName(type), type, typePool, circularityLock, type.getClassLoader(), module), type, type, module, !DISPATCHER.isModifiableClass(instrumentation, type));
                        } catch (Throwable throwable) {
                            try {
                                listener.onDiscovery(TypeDescription.ForLoadedType.getName(type), type.getClassLoader(), module, true);
                                listener.onError(TypeDescription.ForLoadedType.getName(type), type.getClassLoader(), module, true, throwable);
                                listener.onComplete(TypeDescription.ForLoadedType.getName(type), type.getClassLoader(), module, true);
                            } catch (Throwable th) {
                            }
                        }
                    }
                }
                batch = collector.apply(instrumentation, circularityLock, locationStrategy, listener, redefinitionBatchAllocator, redefinitionListener, batch);
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$BatchAllocator.class */
        public interface BatchAllocator {
            public static final int FIRST_BATCH = 0;

            Iterable<? extends List<Class<?>>> batch(List<Class<?>> list);

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$BatchAllocator$ForTotal.class */
            public enum ForTotal implements BatchAllocator {
                INSTANCE;

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.BatchAllocator
                public Iterable<? extends List<Class<?>>> batch(List<Class<?>> types) {
                    if (types.isEmpty()) {
                        return Collections.emptySet();
                    }
                    return Collections.singleton(types);
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$BatchAllocator$ForFixedSize.class */
            public static class ForFixedSize implements BatchAllocator {
                private final int size;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.size == ((ForFixedSize) obj).size;
                }

                public int hashCode() {
                    return (17 * 31) + this.size;
                }

                protected ForFixedSize(int size) {
                    this.size = size;
                }

                public static BatchAllocator ofSize(int size) {
                    if (size > 0) {
                        return new ForFixedSize(size);
                    }
                    if (size == 0) {
                        return ForTotal.INSTANCE;
                    }
                    throw new IllegalArgumentException("Cannot define a batch with a negative size: " + size);
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.BatchAllocator
                public Iterable<? extends List<Class<?>>> batch(List<Class<?>> types) {
                    List<List<Class<?>>> batches = new ArrayList<>();
                    int i = 0;
                    while (true) {
                        int index = i;
                        if (index < types.size()) {
                            batches.add(new ArrayList<>(types.subList(index, Math.min(types.size(), index + this.size))));
                            i = index + this.size;
                        } else {
                            return batches;
                        }
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$BatchAllocator$ForMatchedGrouping.class */
            public static class ForMatchedGrouping implements BatchAllocator {
                private final Collection<? extends ElementMatcher<? super TypeDescription>> matchers;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.matchers.equals(((ForMatchedGrouping) obj).matchers);
                }

                public int hashCode() {
                    return (17 * 31) + this.matchers.hashCode();
                }

                public ForMatchedGrouping(ElementMatcher<? super TypeDescription>... matcher) {
                    this(new LinkedHashSet(Arrays.asList(matcher)));
                }

                public ForMatchedGrouping(Collection<? extends ElementMatcher<? super TypeDescription>> matchers) {
                    this.matchers = matchers;
                }

                public BatchAllocator withMinimum(int threshold) {
                    return Slicing.withMinimum(threshold, this);
                }

                public BatchAllocator withMaximum(int threshold) {
                    return Slicing.withMaximum(threshold, this);
                }

                public BatchAllocator withinRange(int minimum, int maximum) {
                    return Slicing.withinRange(minimum, maximum, this);
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.BatchAllocator
                public Iterable<? extends List<Class<?>>> batch(List<Class<?>> types) {
                    Map<ElementMatcher<? super TypeDescription>, List<Class<?>>> matched = new LinkedHashMap<>();
                    List<Class<?>> arrayList = new ArrayList<>();
                    for (ElementMatcher<? super TypeDescription> matcher : this.matchers) {
                        matched.put(matcher, new ArrayList<>());
                    }
                    for (Class<?> type : types) {
                        Iterator<? extends ElementMatcher<? super TypeDescription>> it = this.matchers.iterator();
                        while (true) {
                            if (it.hasNext()) {
                                ElementMatcher<? super TypeDescription> matcher2 = it.next();
                                if (matcher2.matches(TypeDescription.ForLoadedType.of(type))) {
                                    matched.get(matcher2).add(type);
                                    break;
                                }
                            } else {
                                arrayList.add(type);
                                break;
                            }
                        }
                    }
                    List<List<Class<?>>> batches = new ArrayList<>(this.matchers.size() + 1);
                    for (List<Class<?>> batch : matched.values()) {
                        if (!batch.isEmpty()) {
                            batches.add(batch);
                        }
                    }
                    if (!arrayList.isEmpty()) {
                        batches.add(arrayList);
                    }
                    return batches;
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$BatchAllocator$Slicing.class */
            public static class Slicing implements BatchAllocator {
                private final int minimum;
                private final int maximum;
                private final BatchAllocator batchAllocator;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.minimum == ((Slicing) obj).minimum && this.maximum == ((Slicing) obj).maximum && this.batchAllocator.equals(((Slicing) obj).batchAllocator);
                }

                public int hashCode() {
                    return (((((17 * 31) + this.minimum) * 31) + this.maximum) * 31) + this.batchAllocator.hashCode();
                }

                protected Slicing(int minimum, int maximum, BatchAllocator batchAllocator) {
                    this.minimum = minimum;
                    this.maximum = maximum;
                    this.batchAllocator = batchAllocator;
                }

                public static BatchAllocator withMinimum(int minimum, BatchAllocator batchAllocator) {
                    return withinRange(minimum, Integer.MAX_VALUE, batchAllocator);
                }

                public static BatchAllocator withMaximum(int maximum, BatchAllocator batchAllocator) {
                    return withinRange(1, maximum, batchAllocator);
                }

                public static BatchAllocator withinRange(int minimum, int maximum, BatchAllocator batchAllocator) {
                    if (minimum <= 0) {
                        throw new IllegalArgumentException("Minimum must be a positive number: " + minimum);
                    }
                    if (minimum > maximum) {
                        throw new IllegalArgumentException("Minimum must not be bigger than maximum: " + minimum + " >" + maximum);
                    }
                    return new Slicing(minimum, maximum, batchAllocator);
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.BatchAllocator
                public Iterable<? extends List<Class<?>>> batch(List<Class<?>> types) {
                    return new SlicingIterable(this.minimum, this.maximum, this.batchAllocator.batch(types));
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$BatchAllocator$Slicing$SlicingIterable.class */
                protected static class SlicingIterable implements Iterable<List<Class<?>>> {
                    private final int minimum;
                    private final int maximum;
                    private final Iterable<? extends List<Class<?>>> iterable;

                    protected SlicingIterable(int minimum, int maximum, Iterable<? extends List<Class<?>>> iterable) {
                        this.minimum = minimum;
                        this.maximum = maximum;
                        this.iterable = iterable;
                    }

                    @Override // java.lang.Iterable
                    public Iterator<List<Class<?>>> iterator() {
                        return new SlicingIterator(this.minimum, this.maximum, this.iterable.iterator());
                    }

                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$BatchAllocator$Slicing$SlicingIterable$SlicingIterator.class */
                    protected static class SlicingIterator implements Iterator<List<Class<?>>> {
                        private final int minimum;
                        private final int maximum;
                        private final Iterator<? extends List<Class<?>>> iterator;
                        private List<Class<?>> buffer = new ArrayList();

                        protected SlicingIterator(int minimum, int maximum, Iterator<? extends List<Class<?>>> iterator) {
                            this.minimum = minimum;
                            this.maximum = maximum;
                            this.iterator = iterator;
                        }

                        @Override // java.util.Iterator
                        public boolean hasNext() {
                            return !this.buffer.isEmpty() || this.iterator.hasNext();
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.util.Iterator
                        public List<Class<?>> next() {
                            if (this.buffer.isEmpty()) {
                                this.buffer = this.iterator.next();
                            }
                            while (this.buffer.size() < this.minimum && this.iterator.hasNext()) {
                                this.buffer.addAll(this.iterator.next());
                            }
                            if (this.buffer.size() > this.maximum) {
                                try {
                                    return this.buffer.subList(0, this.maximum);
                                } finally {
                                    this.buffer = new ArrayList(this.buffer.subList(this.maximum, this.buffer.size()));
                                }
                            }
                            try {
                                return this.buffer;
                            } finally {
                                this.buffer = new ArrayList();
                            }
                        }

                        @Override // java.util.Iterator
                        public void remove() {
                            throw new UnsupportedOperationException("remove");
                        }
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$BatchAllocator$Partitioning.class */
            public static class Partitioning implements BatchAllocator {
                private final int parts;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.parts == ((Partitioning) obj).parts;
                }

                public int hashCode() {
                    return (17 * 31) + this.parts;
                }

                protected Partitioning(int parts) {
                    this.parts = parts;
                }

                public static BatchAllocator of(int parts) {
                    if (parts < 1) {
                        throw new IllegalArgumentException("A batch size must be positive: " + parts);
                    }
                    return new Partitioning(parts);
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.BatchAllocator
                public Iterable<? extends List<Class<?>>> batch(List<Class<?>> types) {
                    if (types.isEmpty()) {
                        return Collections.emptyList();
                    }
                    List<List<Class<?>>> batches = new ArrayList<>();
                    int size = types.size() / this.parts;
                    int reminder = types.size() % this.parts;
                    int i = reminder;
                    while (true) {
                        int index = i;
                        if (index >= types.size()) {
                            break;
                        }
                        batches.add(new ArrayList<>(types.subList(index, index + size)));
                        i = index + size;
                    }
                    if (batches.isEmpty()) {
                        return Collections.singletonList(types);
                    }
                    batches.get(0).addAll(0, types.subList(0, reminder));
                    return batches;
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$Listener.class */
        public interface Listener {
            void onBatch(int i, List<Class<?>> list, List<Class<?>> list2);

            Iterable<? extends List<Class<?>>> onError(int i, List<Class<?>> list, Throwable th, List<Class<?>> list2);

            void onComplete(int i, List<Class<?>> list, Map<List<Class<?>>, Throwable> map);

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$Listener$NoOp.class */
            public enum NoOp implements Listener {
                INSTANCE;

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Listener
                public void onBatch(int index, List<Class<?>> batch, List<Class<?>> types) {
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Listener
                public Iterable<? extends List<Class<?>>> onError(int index, List<Class<?>> batch, Throwable throwable, List<Class<?>> types) {
                    return Collections.emptyList();
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Listener
                public void onComplete(int amount, List<Class<?>> types, Map<List<Class<?>>, Throwable> failures) {
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$Listener$Yielding.class */
            public enum Yielding implements Listener {
                INSTANCE;

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Listener
                public void onBatch(int index, List<Class<?>> batch, List<Class<?>> types) {
                    if (index > 0) {
                        Thread.yield();
                    }
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Listener
                public Iterable<? extends List<Class<?>>> onError(int index, List<Class<?>> batch, Throwable throwable, List<Class<?>> types) {
                    return Collections.emptyList();
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Listener
                public void onComplete(int amount, List<Class<?>> types, Map<List<Class<?>>, Throwable> failures) {
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$Listener$ErrorEscalating.class */
            public enum ErrorEscalating implements Listener {
                FAIL_FAST { // from class: net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Listener.ErrorEscalating.1
                    @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Listener
                    public Iterable<? extends List<Class<?>>> onError(int index, List<Class<?>> batch, Throwable throwable, List<Class<?>> types) {
                        throw new IllegalStateException("Could not transform any of " + batch, throwable);
                    }

                    @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Listener
                    public void onComplete(int amount, List<Class<?>> types, Map<List<Class<?>>, Throwable> failures) {
                    }
                },
                FAIL_LAST { // from class: net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Listener.ErrorEscalating.2
                    @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Listener
                    public Iterable<? extends List<Class<?>>> onError(int index, List<Class<?>> batch, Throwable throwable, List<Class<?>> types) {
                        return Collections.emptyList();
                    }

                    @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Listener
                    public void onComplete(int amount, List<Class<?>> types, Map<List<Class<?>>, Throwable> failures) {
                        if (!failures.isEmpty()) {
                            throw new IllegalStateException("Could not transform any of " + failures);
                        }
                    }
                };

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Listener
                public void onBatch(int index, List<Class<?>> batch, List<Class<?>> types) {
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$Listener$Adapter.class */
            public static abstract class Adapter implements Listener {
                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass();
                }

                public int hashCode() {
                    return 17;
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Listener
                public void onBatch(int index, List<Class<?>> batch, List<Class<?>> types) {
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Listener
                public Iterable<? extends List<Class<?>>> onError(int index, List<Class<?>> batch, Throwable throwable, List<Class<?>> types) {
                    return Collections.emptyList();
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Listener
                public void onComplete(int amount, List<Class<?>> types, Map<List<Class<?>>, Throwable> failures) {
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$Listener$BatchReallocator.class */
            public static class BatchReallocator extends Adapter {
                private final BatchAllocator batchAllocator;

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Listener.Adapter
                public boolean equals(Object obj) {
                    if (super.equals(obj)) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.batchAllocator.equals(((BatchReallocator) obj).batchAllocator);
                    }
                    return false;
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Listener.Adapter
                public int hashCode() {
                    return (super.hashCode() * 31) + this.batchAllocator.hashCode();
                }

                public BatchReallocator(BatchAllocator batchAllocator) {
                    this.batchAllocator = batchAllocator;
                }

                public static Listener splitting() {
                    return new BatchReallocator(new BatchAllocator.Partitioning(2));
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Listener.Adapter, net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Listener
                public Iterable<? extends List<Class<?>>> onError(int index, List<Class<?>> batch, Throwable throwable, List<Class<?>> types) {
                    if (batch.size() < 2) {
                        return Collections.emptyList();
                    }
                    return this.batchAllocator.batch(batch);
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$Listener$Pausing.class */
            public static class Pausing extends Adapter {
                private final long value;

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Listener.Adapter
                public boolean equals(Object obj) {
                    if (super.equals(obj)) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.value == ((Pausing) obj).value;
                    }
                    return false;
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Listener.Adapter
                public int hashCode() {
                    int hashCode = super.hashCode() * 31;
                    return hashCode + ((int) (hashCode ^ (this.value >>> 32)));
                }

                protected Pausing(long value) {
                    this.value = value;
                }

                public static Listener of(long value, TimeUnit timeUnit) {
                    if (value > 0) {
                        return new Pausing(timeUnit.toMillis(value));
                    }
                    if (value == 0) {
                        return NoOp.INSTANCE;
                    }
                    throw new IllegalArgumentException("Cannot sleep for a non-positive amount of time: " + value);
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Listener.Adapter, net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Listener
                public void onBatch(int index, List<Class<?>> batch, List<Class<?>> types) {
                    if (index > 0) {
                        try {
                            Thread.sleep(this.value);
                        } catch (InterruptedException exception) {
                            Thread.currentThread().interrupt();
                            throw new IllegalStateException(exception);
                        }
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$Listener$StreamWriting.class */
            public static class StreamWriting implements Listener {
                private final PrintStream printStream;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.printStream.equals(((StreamWriting) obj).printStream);
                }

                public int hashCode() {
                    return (17 * 31) + this.printStream.hashCode();
                }

                public StreamWriting(PrintStream printStream) {
                    this.printStream = printStream;
                }

                public static Listener toSystemOut() {
                    return new StreamWriting(System.out);
                }

                public static Listener toSystemError() {
                    return new StreamWriting(System.err);
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Listener
                public void onBatch(int index, List<Class<?>> batch, List<Class<?>> types) {
                    this.printStream.printf("[Byte Buddy] REDEFINE BATCH #%d [%d of %d type(s)]%n", Integer.valueOf(index), Integer.valueOf(batch.size()), Integer.valueOf(types.size()));
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Listener
                public Iterable<? extends List<Class<?>>> onError(int index, List<Class<?>> batch, Throwable throwable, List<Class<?>> types) {
                    synchronized (this.printStream) {
                        this.printStream.printf("[Byte Buddy] REDEFINE ERROR #%d [%d of %d type(s)]%n", Integer.valueOf(index), Integer.valueOf(batch.size()), Integer.valueOf(types.size()));
                        throwable.printStackTrace(this.printStream);
                    }
                    return Collections.emptyList();
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Listener
                public void onComplete(int amount, List<Class<?>> types, Map<List<Class<?>>, Throwable> failures) {
                    this.printStream.printf("[Byte Buddy] REDEFINE COMPLETE %d batch(es) containing %d types [%d failed batch(es)]%n", Integer.valueOf(amount), Integer.valueOf(types.size()), Integer.valueOf(failures.size()));
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$Listener$Compound.class */
            public static class Compound implements Listener {
                private final List<Listener> listeners;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.listeners.equals(((Compound) obj).listeners);
                }

                public int hashCode() {
                    return (17 * 31) + this.listeners.hashCode();
                }

                public Compound(Listener... listener) {
                    this(Arrays.asList(listener));
                }

                public Compound(List<? extends Listener> listeners) {
                    this.listeners = new ArrayList();
                    for (Listener listener : listeners) {
                        if (listener instanceof Compound) {
                            this.listeners.addAll(((Compound) listener).listeners);
                        } else if (!(listener instanceof NoOp)) {
                            this.listeners.add(listener);
                        }
                    }
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Listener
                public void onBatch(int index, List<Class<?>> batch, List<Class<?>> types) {
                    for (Listener listener : this.listeners) {
                        listener.onBatch(index, batch, types);
                    }
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Listener
                public Iterable<? extends List<Class<?>>> onError(int index, List<Class<?>> batch, Throwable throwable, List<Class<?>> types) {
                    List<Iterable<? extends List<Class<?>>>> reattempts = new ArrayList<>();
                    for (Listener listener : this.listeners) {
                        reattempts.add(listener.onError(index, batch, throwable, types));
                    }
                    return new CompoundIterable(reattempts);
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Listener
                public void onComplete(int amount, List<Class<?>> types, Map<List<Class<?>>, Throwable> failures) {
                    for (Listener listener : this.listeners) {
                        listener.onComplete(amount, types, failures);
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$Listener$Compound$CompoundIterable.class */
                protected static class CompoundIterable implements Iterable<List<Class<?>>> {
                    private final List<Iterable<? extends List<Class<?>>>> iterables;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.iterables.equals(((CompoundIterable) obj).iterables);
                    }

                    public int hashCode() {
                        return (17 * 31) + this.iterables.hashCode();
                    }

                    protected CompoundIterable(List<Iterable<? extends List<Class<?>>>> iterables) {
                        this.iterables = iterables;
                    }

                    @Override // java.lang.Iterable
                    public Iterator<List<Class<?>>> iterator() {
                        return new CompoundIterator(new ArrayList(this.iterables));
                    }

                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$Listener$Compound$CompoundIterable$CompoundIterator.class */
                    protected static class CompoundIterator implements Iterator<List<Class<?>>> {
                        private Iterator<? extends List<Class<?>>> current;
                        private final List<Iterable<? extends List<Class<?>>>> backlog;

                        protected CompoundIterator(List<Iterable<? extends List<Class<?>>>> iterables) {
                            this.backlog = iterables;
                            forward();
                        }

                        @Override // java.util.Iterator
                        public boolean hasNext() {
                            return this.current != null && this.current.hasNext();
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.util.Iterator
                        public List<Class<?>> next() {
                            try {
                                if (this.current != null) {
                                    List<Class<?>> next = this.current.next();
                                    forward();
                                    return next;
                                }
                                throw new NoSuchElementException();
                            } catch (Throwable th) {
                                forward();
                                throw th;
                            }
                        }

                        private void forward() {
                            while (true) {
                                if ((this.current == null || !this.current.hasNext()) && !this.backlog.isEmpty()) {
                                    this.current = this.backlog.remove(0).iterator();
                                } else {
                                    return;
                                }
                            }
                        }

                        @Override // java.util.Iterator
                        public void remove() {
                            throw new UnsupportedOperationException("remove");
                        }
                    }
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$DiscoveryStrategy.class */
        public interface DiscoveryStrategy {
            Iterable<Iterable<Class<?>>> resolve(Instrumentation instrumentation);

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$DiscoveryStrategy$SinglePass.class */
            public enum SinglePass implements DiscoveryStrategy {
                INSTANCE;

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.DiscoveryStrategy
                public Iterable<Iterable<Class<?>>> resolve(Instrumentation instrumentation) {
                    return Collections.singleton(Arrays.asList(instrumentation.getAllLoadedClasses()));
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$DiscoveryStrategy$Reiterating.class */
            public enum Reiterating implements DiscoveryStrategy {
                INSTANCE;

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.DiscoveryStrategy
                public Iterable<Iterable<Class<?>>> resolve(Instrumentation instrumentation) {
                    return new ReiteratingIterable(instrumentation);
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$DiscoveryStrategy$Reiterating$ReiteratingIterable.class */
                protected static class ReiteratingIterable implements Iterable<Iterable<Class<?>>> {
                    private final Instrumentation instrumentation;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.instrumentation.equals(((ReiteratingIterable) obj).instrumentation);
                    }

                    public int hashCode() {
                        return (17 * 31) + this.instrumentation.hashCode();
                    }

                    protected ReiteratingIterable(Instrumentation instrumentation) {
                        this.instrumentation = instrumentation;
                    }

                    @Override // java.lang.Iterable
                    public Iterator<Iterable<Class<?>>> iterator() {
                        return new ReiteratingIterator(this.instrumentation);
                    }
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$DiscoveryStrategy$Reiterating$ReiteratingIterator.class */
                protected static class ReiteratingIterator implements Iterator<Iterable<Class<?>>> {
                    private final Instrumentation instrumentation;
                    private final Set<Class<?>> processed = new HashSet();
                    private List<Class<?>> types;

                    protected ReiteratingIterator(Instrumentation instrumentation) {
                        this.instrumentation = instrumentation;
                    }

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        Class<?>[] allLoadedClasses;
                        if (this.types == null) {
                            this.types = new ArrayList();
                            for (Class<?> type : this.instrumentation.getAllLoadedClasses()) {
                                if (type != null && this.processed.add(type)) {
                                    this.types.add(type);
                                }
                            }
                        }
                        return !this.types.isEmpty();
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.Iterator
                    public Iterable<Class<?>> next() {
                        if (hasNext()) {
                            try {
                                return this.types;
                            } finally {
                                this.types = null;
                            }
                        }
                        throw new NoSuchElementException();
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        throw new UnsupportedOperationException("remove");
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$DiscoveryStrategy$Explicit.class */
            public static class Explicit implements DiscoveryStrategy {
                private final Set<Class<?>> types;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.types.equals(((Explicit) obj).types);
                }

                public int hashCode() {
                    return (17 * 31) + this.types.hashCode();
                }

                public Explicit(Class<?>... type) {
                    this(new LinkedHashSet(Arrays.asList(type)));
                }

                public Explicit(Set<Class<?>> types) {
                    this.types = types;
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.DiscoveryStrategy
                public Iterable<Iterable<Class<?>>> resolve(Instrumentation instrumentation) {
                    return Collections.singleton(this.types);
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$ResubmissionScheduler.class */
        public interface ResubmissionScheduler {
            boolean isAlive();

            Cancelable schedule(Runnable runnable);

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$ResubmissionScheduler$Cancelable.class */
            public interface Cancelable {
                void cancel();

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$ResubmissionScheduler$Cancelable$NoOp.class */
                public enum NoOp implements Cancelable {
                    INSTANCE;

                    @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.ResubmissionScheduler.Cancelable
                    public void cancel() {
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$ResubmissionScheduler$Cancelable$ForFuture.class */
                public static class ForFuture implements Cancelable {
                    private final Future<?> future;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.future.equals(((ForFuture) obj).future);
                    }

                    public int hashCode() {
                        return (17 * 31) + this.future.hashCode();
                    }

                    public ForFuture(Future<?> future) {
                        this.future = future;
                    }

                    @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.ResubmissionScheduler.Cancelable
                    public void cancel() {
                        this.future.cancel(true);
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$ResubmissionScheduler$NoOp.class */
            public enum NoOp implements ResubmissionScheduler {
                INSTANCE;

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.ResubmissionScheduler
                public boolean isAlive() {
                    return false;
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.ResubmissionScheduler
                public Cancelable schedule(Runnable job) {
                    return Cancelable.NoOp.INSTANCE;
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$ResubmissionScheduler$AtFixedRate.class */
            public static class AtFixedRate implements ResubmissionScheduler {
                private final ScheduledExecutorService scheduledExecutorService;
                private final long time;
                private final TimeUnit timeUnit;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.time == ((AtFixedRate) obj).time && this.timeUnit.equals(((AtFixedRate) obj).timeUnit) && this.scheduledExecutorService.equals(((AtFixedRate) obj).scheduledExecutorService);
                }

                public int hashCode() {
                    int hashCode = ((17 * 31) + this.scheduledExecutorService.hashCode()) * 31;
                    return ((hashCode + ((int) (hashCode ^ (this.time >>> 32)))) * 31) + this.timeUnit.hashCode();
                }

                public AtFixedRate(ScheduledExecutorService scheduledExecutorService, long time, TimeUnit timeUnit) {
                    this.scheduledExecutorService = scheduledExecutorService;
                    this.time = time;
                    this.timeUnit = timeUnit;
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.ResubmissionScheduler
                public boolean isAlive() {
                    return !this.scheduledExecutorService.isShutdown();
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.ResubmissionScheduler
                public Cancelable schedule(Runnable job) {
                    return new Cancelable.ForFuture(this.scheduledExecutorService.scheduleAtFixedRate(job, this.time, this.time, this.timeUnit));
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$ResubmissionScheduler$WithFixedDelay.class */
            public static class WithFixedDelay implements ResubmissionScheduler {
                private final ScheduledExecutorService scheduledExecutorService;
                private final long time;
                private final TimeUnit timeUnit;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.time == ((WithFixedDelay) obj).time && this.timeUnit.equals(((WithFixedDelay) obj).timeUnit) && this.scheduledExecutorService.equals(((WithFixedDelay) obj).scheduledExecutorService);
                }

                public int hashCode() {
                    int hashCode = ((17 * 31) + this.scheduledExecutorService.hashCode()) * 31;
                    return ((hashCode + ((int) (hashCode ^ (this.time >>> 32)))) * 31) + this.timeUnit.hashCode();
                }

                public WithFixedDelay(ScheduledExecutorService scheduledExecutorService, long time, TimeUnit timeUnit) {
                    this.scheduledExecutorService = scheduledExecutorService;
                    this.time = time;
                    this.timeUnit = timeUnit;
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.ResubmissionScheduler
                public boolean isAlive() {
                    return !this.scheduledExecutorService.isShutdown();
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.ResubmissionScheduler
                public Cancelable schedule(Runnable job) {
                    return new Cancelable.ForFuture(this.scheduledExecutorService.scheduleWithFixedDelay(job, this.time, this.time, this.timeUnit));
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$ResubmissionStrategy.class */
        public interface ResubmissionStrategy {
            Installation apply(Instrumentation instrumentation, LocationStrategy locationStrategy, Listener listener, InstallationListener installationListener, CircularityLock circularityLock, RawMatcher rawMatcher, RedefinitionStrategy redefinitionStrategy, BatchAllocator batchAllocator, Listener listener2);

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$ResubmissionStrategy$Disabled.class */
            public enum Disabled implements ResubmissionStrategy {
                INSTANCE;

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.ResubmissionStrategy
                public Installation apply(Instrumentation instrumentation, LocationStrategy locationStrategy, Listener listener, InstallationListener installationListener, CircularityLock circularityLock, RawMatcher matcher, RedefinitionStrategy redefinitionStrategy, BatchAllocator redefinitionBatchAllocator, Listener redefinitionBatchListener) {
                    return new Installation(listener, installationListener);
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$ResubmissionStrategy$Enabled.class */
            public static class Enabled implements ResubmissionStrategy {
                private final ResubmissionScheduler resubmissionScheduler;
                private final ElementMatcher<? super Throwable> matcher;
                private final ElementMatcher<? super ClassLoader> classLoaderFilter;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.resubmissionScheduler.equals(((Enabled) obj).resubmissionScheduler) && this.matcher.equals(((Enabled) obj).matcher) && this.classLoaderFilter.equals(((Enabled) obj).classLoaderFilter);
                }

                public int hashCode() {
                    return (((((17 * 31) + this.resubmissionScheduler.hashCode()) * 31) + this.matcher.hashCode()) * 31) + this.classLoaderFilter.hashCode();
                }

                protected Enabled(ResubmissionScheduler resubmissionScheduler, ElementMatcher<? super Throwable> matcher, ElementMatcher<? super ClassLoader> classLoaderFilter) {
                    this.resubmissionScheduler = resubmissionScheduler;
                    this.matcher = matcher;
                    this.classLoaderFilter = classLoaderFilter;
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.ResubmissionStrategy
                public Installation apply(Instrumentation instrumentation, LocationStrategy locationStrategy, Listener listener, InstallationListener installationListener, CircularityLock circularityLock, RawMatcher matcher, RedefinitionStrategy redefinitionStrategy, BatchAllocator redefinitionBatchAllocator, Listener redefinitionBatchListener) {
                    if (redefinitionStrategy.isEnabled() && this.resubmissionScheduler.isAlive()) {
                        ConcurrentMap<StorageKey, Set<String>> types = new ConcurrentHashMap<>();
                        return new Installation(new Listener.Compound(new ResubmissionListener(this.matcher, types), listener), new InstallationListener.Compound(new ResubmissionInstallationListener(this.resubmissionScheduler, instrumentation, locationStrategy, listener, circularityLock, matcher, this.classLoaderFilter, redefinitionStrategy, redefinitionBatchAllocator, redefinitionBatchListener, types), installationListener));
                    }
                    return new Installation(listener, installationListener);
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$ResubmissionStrategy$Enabled$ResubmissionListener.class */
                protected static class ResubmissionListener extends Listener.Adapter {
                    private final ElementMatcher<? super Throwable> matcher;
                    private final ConcurrentMap<StorageKey, Set<String>> types;

                    protected ResubmissionListener(ElementMatcher<? super Throwable> matcher, ConcurrentMap<StorageKey, Set<String>> types) {
                        this.matcher = matcher;
                        this.types = types;
                    }

                    @Override // net.bytebuddy.agent.builder.AgentBuilder.Listener.Adapter, net.bytebuddy.agent.builder.AgentBuilder.Listener
                    @SuppressFBWarnings(value = {"GC_UNRELATED_TYPES"}, justification = "Use of unrelated key is intended for avoiding unnecessary weak reference")
                    public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded, Throwable throwable) {
                        if (!loaded && this.matcher.matches(throwable)) {
                            Set<String> types = this.types.get(new LookupKey(classLoader));
                            if (types == null) {
                                types = new ConcurrentHashSet<>();
                                Set<String> previous = this.types.putIfAbsent(new StorageKey(classLoader), types);
                                if (previous != null) {
                                    types = previous;
                                }
                            }
                            types.add(typeName);
                        }
                    }

                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$ResubmissionStrategy$Enabled$ResubmissionListener$ConcurrentHashSet.class */
                    protected static class ConcurrentHashSet<T> extends AbstractSet<T> {
                        private final ConcurrentMap<T, Boolean> delegate = new ConcurrentHashMap();

                        protected ConcurrentHashSet() {
                        }

                        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                        public boolean add(T value) {
                            return this.delegate.put(value, Boolean.TRUE) == null;
                        }

                        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                        public boolean remove(Object value) {
                            return this.delegate.remove(value) != null;
                        }

                        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
                        public Iterator<T> iterator() {
                            return this.delegate.keySet().iterator();
                        }

                        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                        public int size() {
                            return this.delegate.size();
                        }
                    }
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$ResubmissionStrategy$Enabled$ResubmissionInstallationListener.class */
                protected static class ResubmissionInstallationListener extends InstallationListener.Adapter implements Runnable {
                    private final ResubmissionScheduler resubmissionScheduler;
                    private final Instrumentation instrumentation;
                    private final LocationStrategy locationStrategy;
                    private final Listener listener;
                    private final CircularityLock circularityLock;
                    private final RawMatcher matcher;
                    private final ElementMatcher<? super ClassLoader> classLoaderFilter;
                    private final RedefinitionStrategy redefinitionStrategy;
                    private final BatchAllocator redefinitionBatchAllocator;
                    private final Listener redefinitionBatchListener;
                    private final ConcurrentMap<StorageKey, Set<String>> types;
                    private volatile ResubmissionScheduler.Cancelable cancelable;

                    protected ResubmissionInstallationListener(ResubmissionScheduler resubmissionScheduler, Instrumentation instrumentation, LocationStrategy locationStrategy, Listener listener, CircularityLock circularityLock, RawMatcher matcher, ElementMatcher<? super ClassLoader> classLoaderFilter, RedefinitionStrategy redefinitionStrategy, BatchAllocator redefinitionBatchAllocator, Listener redefinitionBatchListener, ConcurrentMap<StorageKey, Set<String>> types) {
                        this.resubmissionScheduler = resubmissionScheduler;
                        this.instrumentation = instrumentation;
                        this.locationStrategy = locationStrategy;
                        this.listener = listener;
                        this.circularityLock = circularityLock;
                        this.matcher = matcher;
                        this.redefinitionStrategy = redefinitionStrategy;
                        this.redefinitionBatchAllocator = redefinitionBatchAllocator;
                        this.redefinitionBatchListener = redefinitionBatchListener;
                        this.classLoaderFilter = classLoaderFilter;
                        this.types = types;
                    }

                    @Override // net.bytebuddy.agent.builder.AgentBuilder.InstallationListener.Adapter, net.bytebuddy.agent.builder.AgentBuilder.InstallationListener
                    public void onInstall(Instrumentation instrumentation, ResettableClassFileTransformer classFileTransformer) {
                        this.cancelable = this.resubmissionScheduler.schedule(this);
                    }

                    @Override // net.bytebuddy.agent.builder.AgentBuilder.InstallationListener.Adapter, net.bytebuddy.agent.builder.AgentBuilder.InstallationListener
                    public void onReset(Instrumentation instrumentation, ResettableClassFileTransformer classFileTransformer) {
                        ResubmissionScheduler.Cancelable cancelable = this.cancelable;
                        if (cancelable != null) {
                            cancelable.cancel();
                        }
                    }

                    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
                        jadx.core.utils.exceptions.JadxRuntimeException: Unreachable block: B:62:0x01ef
                        	at jadx.core.dex.visitors.blocks.BlockProcessor.checkForUnreachableBlocks(BlockProcessor.java:81)
                        	at jadx.core.dex.visitors.blocks.BlockProcessor.processBlocksTree(BlockProcessor.java:47)
                        	at jadx.core.dex.visitors.blocks.BlockProcessor.visit(BlockProcessor.java:39)
                        */
                    @Override // java.lang.Runnable
                    public void run() {
                        /*
                            Method dump skipped, instructions count: 612
                            To view this dump add '--comments-level debug' option
                        */
                        throw new UnsupportedOperationException("Method not decompiled: net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.ResubmissionStrategy.Enabled.ResubmissionInstallationListener.run():void");
                    }
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$ResubmissionStrategy$Enabled$LookupKey.class */
                protected static class LookupKey {
                    private final ClassLoader classLoader;
                    private final int hashCode;

                    protected LookupKey(ClassLoader classLoader) {
                        this.classLoader = classLoader;
                        this.hashCode = System.identityHashCode(classLoader);
                    }

                    public int hashCode() {
                        return this.hashCode;
                    }

                    @SuppressFBWarnings(value = {"EQ_CHECK_FOR_OPERAND_NOT_COMPATIBLE_WITH_THIS"}, justification = "Cross-comparison is intended")
                    public boolean equals(Object other) {
                        if (this == other) {
                            return true;
                        }
                        if (other instanceof LookupKey) {
                            return this.classLoader == ((LookupKey) other).classLoader;
                        } else if (other instanceof StorageKey) {
                            StorageKey storageKey = (StorageKey) other;
                            return this.hashCode == storageKey.hashCode && this.classLoader == storageKey.get();
                        } else {
                            return false;
                        }
                    }
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$ResubmissionStrategy$Enabled$StorageKey.class */
                protected static class StorageKey extends WeakReference<ClassLoader> {
                    private final int hashCode;

                    protected StorageKey(ClassLoader classLoader) {
                        super(classLoader);
                        this.hashCode = System.identityHashCode(classLoader);
                    }

                    protected boolean isBootstrapLoader() {
                        return this.hashCode == 0;
                    }

                    public int hashCode() {
                        return this.hashCode;
                    }

                    @SuppressFBWarnings(value = {"EQ_CHECK_FOR_OPERAND_NOT_COMPATIBLE_WITH_THIS"}, justification = "Cross-comparison is intended")
                    public boolean equals(Object other) {
                        if (this == other) {
                            return true;
                        }
                        if (other instanceof LookupKey) {
                            LookupKey lookupKey = (LookupKey) other;
                            return this.hashCode == lookupKey.hashCode && get() == lookupKey.classLoader;
                        } else if (other instanceof StorageKey) {
                            StorageKey storageKey = (StorageKey) other;
                            return this.hashCode == storageKey.hashCode && get() == storageKey.get();
                        } else {
                            return false;
                        }
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$ResubmissionStrategy$Installation.class */
            public static class Installation {
                private final Listener listener;
                private final InstallationListener installationListener;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.listener.equals(((Installation) obj).listener) && this.installationListener.equals(((Installation) obj).installationListener);
                }

                public int hashCode() {
                    return (((17 * 31) + this.listener.hashCode()) * 31) + this.installationListener.hashCode();
                }

                protected Installation(Listener listener, InstallationListener installationListener) {
                    this.listener = listener;
                    this.installationListener = installationListener;
                }

                protected Listener getListener() {
                    return this.listener;
                }

                protected InstallationListener getInstallationListener() {
                    return this.installationListener;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$Dispatcher.class */
        public interface Dispatcher {
            boolean isModifiableClass(Instrumentation instrumentation, Class<?> cls);

            boolean isRetransformClassesSupported(Instrumentation instrumentation);

            void retransformClasses(Instrumentation instrumentation, Class<?>[] clsArr) throws UnmodifiableClassException;

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$Dispatcher$CreationAction.class */
            public enum CreationAction implements PrivilegedAction<Dispatcher> {
                INSTANCE;

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public Dispatcher run() {
                    try {
                        return new ForJava6CapableVm(Instrumentation.class.getMethod("isModifiableClass", Class.class), Instrumentation.class.getMethod("isRetransformClassesSupported", new Class[0]), Instrumentation.class.getMethod("retransformClasses", Class[].class));
                    } catch (NoSuchMethodException e) {
                        return ForLegacyVm.INSTANCE;
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$Dispatcher$ForLegacyVm.class */
            public enum ForLegacyVm implements Dispatcher {
                INSTANCE;

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Dispatcher
                public boolean isModifiableClass(Instrumentation instrumentation, Class<?> type) {
                    return (type.isArray() || type.isPrimitive()) ? false : true;
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Dispatcher
                public boolean isRetransformClassesSupported(Instrumentation instrumentation) {
                    return false;
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Dispatcher
                public void retransformClasses(Instrumentation instrumentation, Class<?>[] type) {
                    throw new UnsupportedOperationException("The current VM does not support retransformation");
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$Dispatcher$ForJava6CapableVm.class */
            public static class ForJava6CapableVm implements Dispatcher {
                private final Method isModifiableClass;
                private final Method isRetransformClassesSupported;
                private final Method retransformClasses;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.isModifiableClass.equals(((ForJava6CapableVm) obj).isModifiableClass) && this.isRetransformClassesSupported.equals(((ForJava6CapableVm) obj).isRetransformClassesSupported) && this.retransformClasses.equals(((ForJava6CapableVm) obj).retransformClasses);
                }

                public int hashCode() {
                    return (((((17 * 31) + this.isModifiableClass.hashCode()) * 31) + this.isRetransformClassesSupported.hashCode()) * 31) + this.retransformClasses.hashCode();
                }

                protected ForJava6CapableVm(Method isModifiableClass, Method isRetransformClassesSupported, Method retransformClasses) {
                    this.isModifiableClass = isModifiableClass;
                    this.isRetransformClassesSupported = isRetransformClassesSupported;
                    this.retransformClasses = retransformClasses;
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Dispatcher
                public boolean isModifiableClass(Instrumentation instrumentation, Class<?> type) {
                    try {
                        return ((Boolean) this.isModifiableClass.invoke(instrumentation, type)).booleanValue();
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.instrument.Instrumentation#isModifiableClass", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.instrument.Instrumentation#isModifiableClass", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Dispatcher
                public boolean isRetransformClassesSupported(Instrumentation instrumentation) {
                    try {
                        return ((Boolean) this.isRetransformClassesSupported.invoke(instrumentation, new Object[0])).booleanValue();
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.instrument.Instrumentation#isRetransformClassesSupported", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.instrument.Instrumentation#isRetransformClassesSupported", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Dispatcher
                public void retransformClasses(Instrumentation instrumentation, Class<?>[] type) throws UnmodifiableClassException {
                    try {
                        this.retransformClasses.invoke(instrumentation, type);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.instrument.Instrumentation#retransformClasses", exception);
                    } catch (InvocationTargetException exception2) {
                        UnmodifiableClassException cause = exception2.getCause();
                        if (cause instanceof UnmodifiableClassException) {
                            throw cause;
                        }
                        throw new IllegalStateException("Error invoking java.lang.instrument.Instrumentation#retransformClasses", cause);
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$Collector.class */
        public static abstract class Collector {
            private static final Class<?> NO_LOADED_TYPE = null;
            protected final List<Class<?>> types = new ArrayList();

            protected abstract void doApply(Instrumentation instrumentation, CircularityLock circularityLock, List<Class<?>> list, LocationStrategy locationStrategy, Listener listener) throws UnmodifiableClassException, ClassNotFoundException;

            protected Collector() {
            }

            protected void consider(RawMatcher matcher, Listener listener, TypeDescription typeDescription, Class<?> type, JavaModule module) {
                consider(matcher, listener, typeDescription, type, NO_LOADED_TYPE, module, false);
            }

            protected void consider(RawMatcher matcher, Listener listener, TypeDescription typeDescription, Class<?> type, Class<?> classBeingRedefined, JavaModule module, boolean unmodifiable) {
                if (unmodifiable || !matcher.matches(typeDescription, type.getClassLoader(), module, classBeingRedefined, type.getProtectionDomain())) {
                    try {
                        listener.onDiscovery(TypeDescription.ForLoadedType.getName(type), type.getClassLoader(), module, classBeingRedefined != null);
                        listener.onIgnored(typeDescription, type.getClassLoader(), module, classBeingRedefined != null);
                        listener.onComplete(TypeDescription.ForLoadedType.getName(type), type.getClassLoader(), module, classBeingRedefined != null);
                        return;
                    } catch (Throwable th) {
                        return;
                    }
                }
                this.types.add(type);
            }

            protected void include(List<Class<?>> types) {
                this.types.addAll(types);
            }

            protected int apply(Instrumentation instrumentation, CircularityLock circularityLock, LocationStrategy locationStrategy, Listener listener, BatchAllocator redefinitionBatchAllocator, Listener redefinitionListener, int batch) {
                Map<List<Class<?>>, Throwable> failures = new HashMap<>();
                PrependableIterator prependableIterator = new PrependableIterator(redefinitionBatchAllocator.batch(this.types));
                while (prependableIterator.hasNext()) {
                    List<Class<?>> types = prependableIterator.next();
                    redefinitionListener.onBatch(batch, types, this.types);
                    try {
                        doApply(instrumentation, circularityLock, types, locationStrategy, listener);
                    } catch (Throwable throwable) {
                        prependableIterator.prepend(redefinitionListener.onError(batch, types, throwable, this.types));
                        failures.put(types, throwable);
                    }
                    batch++;
                }
                redefinitionListener.onComplete(batch, this.types, failures);
                return batch;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$Collector$PrependableIterator.class */
            public static class PrependableIterator implements Iterator<List<Class<?>>> {
                private Iterator<? extends List<Class<?>>> current;
                private final LinkedList<Iterator<? extends List<Class<?>>>> backlog = new LinkedList<>();

                protected PrependableIterator(Iterable<? extends List<Class<?>>> origin) {
                    this.current = origin.iterator();
                }

                public void prepend(Iterable<? extends List<Class<?>>> iterable) {
                    Iterator<? extends List<Class<?>>> iterator = iterable.iterator();
                    if (iterator.hasNext()) {
                        if (this.current.hasNext()) {
                            this.backlog.addLast(this.current);
                        }
                        this.current = iterator;
                    }
                }

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return this.current.hasNext();
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.Iterator
                public List<Class<?>> next() {
                    boolean hasNext;
                    boolean isEmpty;
                    try {
                        while (true) {
                            if (!hasNext) {
                                if (isEmpty) {
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                        return this.current.next();
                    } finally {
                        while (!this.current.hasNext() && !this.backlog.isEmpty()) {
                            this.current = this.backlog.removeLast();
                        }
                    }
                }

                @Override // java.util.Iterator
                public void remove() {
                    throw new UnsupportedOperationException("remove");
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$Collector$ForRedefinition.class */
            protected static class ForRedefinition extends Collector {
                protected ForRedefinition() {
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Collector
                protected void doApply(Instrumentation instrumentation, CircularityLock circularityLock, List<Class<?>> types, LocationStrategy locationStrategy, Listener listener) throws UnmodifiableClassException, ClassNotFoundException {
                    List<ClassDefinition> classDefinitions = new ArrayList<>(types.size());
                    for (Class<?> type : types) {
                        try {
                            classDefinitions.add(new ClassDefinition(type, locationStrategy.classFileLocator(type.getClassLoader(), JavaModule.ofType(type)).locate(TypeDescription.ForLoadedType.getName(type)).resolve()));
                        } catch (Throwable th) {
                        }
                    }
                    if (classDefinitions.isEmpty()) {
                        return;
                    }
                    circularityLock.release();
                    try {
                        instrumentation.redefineClasses((ClassDefinition[]) classDefinitions.toArray(new ClassDefinition[0]));
                        circularityLock.acquire();
                    } catch (Throwable th2) {
                        circularityLock.acquire();
                        throw th2;
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$RedefinitionStrategy$Collector$ForRetransformation.class */
            protected static class ForRetransformation extends Collector {
                protected ForRetransformation() {
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.Collector
                protected void doApply(Instrumentation instrumentation, CircularityLock circularityLock, List<Class<?>> types, LocationStrategy locationStrategy, Listener listener) throws UnmodifiableClassException {
                    if (!types.isEmpty()) {
                        circularityLock.release();
                        try {
                            RedefinitionStrategy.DISPATCHER.retransformClasses(instrumentation, (Class[]) types.toArray(new Class[0]));
                            circularityLock.acquire();
                        } catch (Throwable th) {
                            circularityLock.acquire();
                            throw th;
                        }
                    }
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$LambdaInstrumentationStrategy.class */
    public enum LambdaInstrumentationStrategy {
        ENABLED { // from class: net.bytebuddy.agent.builder.AgentBuilder.LambdaInstrumentationStrategy.1
            @Override // net.bytebuddy.agent.builder.AgentBuilder.LambdaInstrumentationStrategy
            protected void apply(ByteBuddy byteBuddy, Instrumentation instrumentation, ClassFileTransformer classFileTransformer) {
                if (LambdaFactory.register(classFileTransformer, new LambdaInstanceFactory(byteBuddy))) {
                    try {
                        Class<?> lambdaMetaFactory = Class.forName("java.lang.invoke.LambdaMetafactory");
                        byteBuddy.with(Implementation.Context.Disabled.Factory.INSTANCE).redefine(lambdaMetaFactory).visit(new AsmVisitorWrapper.ForDeclaredMethods().method(ElementMatchers.named("metafactory"), MetaFactoryRedirection.INSTANCE).method(ElementMatchers.named("altMetafactory"), AlternativeMetaFactoryRedirection.INSTANCE)).make().load(lambdaMetaFactory.getClassLoader(), ClassReloadingStrategy.of(instrumentation));
                    } catch (ClassNotFoundException e) {
                    }
                }
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.LambdaInstrumentationStrategy
            protected boolean isInstrumented(Class<?> type) {
                return true;
            }
        },
        DISABLED { // from class: net.bytebuddy.agent.builder.AgentBuilder.LambdaInstrumentationStrategy.2
            @Override // net.bytebuddy.agent.builder.AgentBuilder.LambdaInstrumentationStrategy
            protected void apply(ByteBuddy byteBuddy, Instrumentation instrumentation, ClassFileTransformer classFileTransformer) {
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.LambdaInstrumentationStrategy
            protected boolean isInstrumented(Class<?> type) {
                return type == null || !type.getName().contains("/");
            }
        };
        
        private static final String UNSAFE_CLASS;
        protected static final MethodVisitor IGNORE_ORIGINAL;

        protected abstract void apply(ByteBuddy byteBuddy, Instrumentation instrumentation, ClassFileTransformer classFileTransformer);

        protected abstract boolean isInstrumented(Class<?> cls);

        static {
            UNSAFE_CLASS = ClassFileVersion.ofThisVm(ClassFileVersion.JAVA_V6).isAtLeast(ClassFileVersion.JAVA_V9) ? "jdk/internal/misc/Unsafe" : "sun/misc/Unsafe";
            IGNORE_ORIGINAL = null;
        }

        public static void release(ClassFileTransformer classFileTransformer, Instrumentation instrumentation) {
            if (LambdaFactory.release(classFileTransformer)) {
                try {
                    ClassReloadingStrategy.of(instrumentation).reset(Class.forName("java.lang.invoke.LambdaMetafactory"));
                } catch (Exception exception) {
                    throw new IllegalStateException("Could not release lambda transformer", exception);
                }
            }
        }

        public static LambdaInstrumentationStrategy of(boolean enabled) {
            return enabled ? ENABLED : DISABLED;
        }

        public boolean isEnabled() {
            return this == ENABLED;
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$LambdaInstrumentationStrategy$LambdaInstanceFactory.class */
        protected static class LambdaInstanceFactory {
            private static final String LAMBDA_FACTORY = "get$Lambda";
            private static final String FIELD_PREFIX = "arg$";
            private static final String LAMBDA_TYPE_INFIX = "$$Lambda$ByteBuddy$";
            private static final Class<?> NOT_PREVIOUSLY_DEFINED = null;
            private static final AtomicInteger LAMBDA_NAME_COUNTER = new AtomicInteger();
            private final ByteBuddy byteBuddy;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.byteBuddy.equals(((LambdaInstanceFactory) obj).byteBuddy);
            }

            public int hashCode() {
                return (17 * 31) + this.byteBuddy.hashCode();
            }

            protected LambdaInstanceFactory(ByteBuddy byteBuddy) {
                this.byteBuddy = byteBuddy;
            }

            public byte[] make(Object targetTypeLookup, String lambdaMethodName, Object factoryMethodType, Object lambdaMethodType, Object targetMethodHandle, Object specializedLambdaMethodType, boolean serializable, List<Class<?>> markerInterfaces, List<?> additionalBridges, Collection<? extends ClassFileTransformer> classFileTransformers) {
                JavaConstant.MethodType factoryMethod = JavaConstant.MethodType.ofLoaded(factoryMethodType);
                JavaConstant.MethodType lambdaMethod = JavaConstant.MethodType.ofLoaded(lambdaMethodType);
                JavaConstant.MethodHandle targetMethod = JavaConstant.MethodHandle.ofLoaded(targetMethodHandle, targetTypeLookup);
                JavaConstant.MethodType specializedLambdaMethod = JavaConstant.MethodType.ofLoaded(specializedLambdaMethodType);
                Class<?> targetType = JavaConstant.MethodHandle.lookupType(targetTypeLookup);
                String lambdaClassName = targetType.getName() + LAMBDA_TYPE_INFIX + LAMBDA_NAME_COUNTER.incrementAndGet();
                DynamicType.Builder<?> builder = this.byteBuddy.subclass(factoryMethod.getReturnType(), ConstructorStrategy.Default.NO_CONSTRUCTORS).modifiers(TypeManifestation.FINAL, Visibility.PUBLIC).implement((List<? extends Type>) markerInterfaces).name(lambdaClassName).defineConstructor(Visibility.PUBLIC).withParameters((Collection<? extends TypeDefinition>) factoryMethod.getParameterTypes()).intercept(ConstructorImplementation.INSTANCE).method(ElementMatchers.named(lambdaMethodName).and(ElementMatchers.takesArguments(lambdaMethod.getParameterTypes())).and(ElementMatchers.returns(lambdaMethod.getReturnType()))).intercept(new LambdaMethodImplementation(targetMethod, specializedLambdaMethod));
                int index = 0;
                for (TypeDescription capturedType : factoryMethod.getParameterTypes()) {
                    index++;
                    builder = builder.defineField(FIELD_PREFIX + index, capturedType, Visibility.PRIVATE, FieldManifestation.FINAL);
                }
                if (!factoryMethod.getParameterTypes().isEmpty()) {
                    builder = builder.defineMethod(LAMBDA_FACTORY, factoryMethod.getReturnType(), Visibility.PRIVATE, Ownership.STATIC).withParameters((Collection<? extends TypeDefinition>) factoryMethod.getParameterTypes()).intercept(FactoryImplementation.INSTANCE);
                }
                if (serializable) {
                    if (!markerInterfaces.contains(Serializable.class)) {
                        builder = builder.implement(Serializable.class);
                    }
                    builder = builder.defineMethod("writeReplace", Object.class, Visibility.PRIVATE).intercept(new SerializationImplementation(TypeDescription.ForLoadedType.of(targetType), factoryMethod.getReturnType(), lambdaMethodName, lambdaMethod, targetMethod, JavaConstant.MethodType.ofLoaded(specializedLambdaMethodType)));
                } else if (factoryMethod.getReturnType().isAssignableTo(Serializable.class)) {
                    builder = builder.defineMethod("readObject", Void.TYPE, Visibility.PRIVATE).withParameters(ObjectInputStream.class).throwing(NotSerializableException.class).intercept(ExceptionMethod.throwing(NotSerializableException.class, "Non-serializable lambda")).defineMethod("writeObject", Void.TYPE, Visibility.PRIVATE).withParameters(ObjectOutputStream.class).throwing(NotSerializableException.class).intercept(ExceptionMethod.throwing(NotSerializableException.class, "Non-serializable lambda"));
                }
                for (Object additionalBridgeType : additionalBridges) {
                    JavaConstant.MethodType additionalBridge = JavaConstant.MethodType.ofLoaded(additionalBridgeType);
                    builder = builder.defineMethod(lambdaMethodName, additionalBridge.getReturnType(), MethodManifestation.BRIDGE, Visibility.PUBLIC).withParameters((Collection<? extends TypeDefinition>) additionalBridge.getParameterTypes()).intercept(new BridgeMethodImplementation(lambdaMethodName, lambdaMethod));
                }
                byte[] classFile = builder.make().getBytes();
                for (ClassFileTransformer classFileTransformer : classFileTransformers) {
                    try {
                        byte[] transformedClassFile = classFileTransformer.transform(targetType.getClassLoader(), lambdaClassName.replace('.', '/'), NOT_PREVIOUSLY_DEFINED, targetType.getProtectionDomain(), classFile);
                        classFile = transformedClassFile == null ? classFile : transformedClassFile;
                    } catch (Throwable th) {
                    }
                }
                return classFile;
            }

            @SuppressFBWarnings(value = {"SE_BAD_FIELD"}, justification = "An enumeration does not serialize fields")
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$LambdaInstrumentationStrategy$LambdaInstanceFactory$ConstructorImplementation.class */
            protected enum ConstructorImplementation implements Implementation {
                INSTANCE;
                
                private final MethodDescription.InDefinedShape objectConstructor = (MethodDescription.InDefinedShape) TypeDescription.OBJECT.getDeclaredMethods().filter(ElementMatchers.isConstructor()).getOnly();

                ConstructorImplementation() {
                }

                @Override // net.bytebuddy.implementation.Implementation
                public ByteCodeAppender appender(Implementation.Target implementationTarget) {
                    return new Appender(implementationTarget.getInstrumentedType().getDeclaredFields());
                }

                @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return instrumentedType;
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$LambdaInstrumentationStrategy$LambdaInstanceFactory$ConstructorImplementation$Appender.class */
                protected static class Appender implements ByteCodeAppender {
                    private final List<FieldDescription.InDefinedShape> declaredFields;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.declaredFields.equals(((Appender) obj).declaredFields);
                    }

                    public int hashCode() {
                        return (17 * 31) + this.declaredFields.hashCode();
                    }

                    protected Appender(List<FieldDescription.InDefinedShape> declaredFields) {
                        this.declaredFields = declaredFields;
                    }

                    @Override // net.bytebuddy.implementation.bytecode.ByteCodeAppender
                    public ByteCodeAppender.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodDescription instrumentedMethod) {
                        List<StackManipulation> fieldAssignments = new ArrayList<>(this.declaredFields.size() * 3);
                        Iterator it = instrumentedMethod.getParameters().iterator();
                        while (it.hasNext()) {
                            ParameterDescription parameterDescription = (ParameterDescription) it.next();
                            fieldAssignments.add(MethodVariableAccess.loadThis());
                            fieldAssignments.add(MethodVariableAccess.load(parameterDescription));
                            fieldAssignments.add(FieldAccess.forField(this.declaredFields.get(parameterDescription.getIndex())).write());
                        }
                        return new ByteCodeAppender.Size(new StackManipulation.Compound(MethodVariableAccess.loadThis(), MethodInvocation.invoke(ConstructorImplementation.INSTANCE.objectConstructor), new StackManipulation.Compound(fieldAssignments), MethodReturn.VOID).apply(methodVisitor, implementationContext).getMaximalSize(), instrumentedMethod.getStackSize());
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$LambdaInstrumentationStrategy$LambdaInstanceFactory$FactoryImplementation.class */
            protected enum FactoryImplementation implements Implementation {
                INSTANCE;

                @Override // net.bytebuddy.implementation.Implementation
                public ByteCodeAppender appender(Implementation.Target implementationTarget) {
                    return new Appender(implementationTarget.getInstrumentedType());
                }

                @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return instrumentedType;
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$LambdaInstrumentationStrategy$LambdaInstanceFactory$FactoryImplementation$Appender.class */
                protected static class Appender implements ByteCodeAppender {
                    private final TypeDescription instrumentedType;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.instrumentedType.equals(((Appender) obj).instrumentedType);
                    }

                    public int hashCode() {
                        return (17 * 31) + this.instrumentedType.hashCode();
                    }

                    protected Appender(TypeDescription instrumentedType) {
                        this.instrumentedType = instrumentedType;
                    }

                    @Override // net.bytebuddy.implementation.bytecode.ByteCodeAppender
                    public ByteCodeAppender.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodDescription instrumentedMethod) {
                        return new ByteCodeAppender.Size(new StackManipulation.Compound(TypeCreation.of(this.instrumentedType), Duplication.SINGLE, MethodVariableAccess.allArgumentsOf(instrumentedMethod), MethodInvocation.invoke((MethodDescription.InDefinedShape) this.instrumentedType.getDeclaredMethods().filter(ElementMatchers.isConstructor()).getOnly()), MethodReturn.REFERENCE).apply(methodVisitor, implementationContext).getMaximalSize(), instrumentedMethod.getStackSize());
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$LambdaInstrumentationStrategy$LambdaInstanceFactory$LambdaMethodImplementation.class */
            protected static class LambdaMethodImplementation implements Implementation {
                private final JavaConstant.MethodHandle targetMethod;
                private final JavaConstant.MethodType specializedLambdaMethod;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.targetMethod.equals(((LambdaMethodImplementation) obj).targetMethod) && this.specializedLambdaMethod.equals(((LambdaMethodImplementation) obj).specializedLambdaMethod);
                }

                public int hashCode() {
                    return (((17 * 31) + this.targetMethod.hashCode()) * 31) + this.specializedLambdaMethod.hashCode();
                }

                protected LambdaMethodImplementation(JavaConstant.MethodHandle targetMethod, JavaConstant.MethodType specializedLambdaMethod) {
                    this.targetMethod = targetMethod;
                    this.specializedLambdaMethod = specializedLambdaMethod;
                }

                @Override // net.bytebuddy.implementation.Implementation
                public ByteCodeAppender appender(Implementation.Target implementationTarget) {
                    return new Appender((MethodDescription) this.targetMethod.getOwnerType().getDeclaredMethods().filter(ElementMatchers.hasMethodName(this.targetMethod.getName()).and(ElementMatchers.returns(this.targetMethod.getReturnType())).and(ElementMatchers.takesArguments(this.targetMethod.getParameterTypes()))).getOnly(), this.specializedLambdaMethod, implementationTarget.getInstrumentedType().getDeclaredFields());
                }

                @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return instrumentedType;
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$LambdaInstrumentationStrategy$LambdaInstanceFactory$LambdaMethodImplementation$Appender.class */
                protected static class Appender implements ByteCodeAppender {
                    private final MethodDescription targetMethod;
                    private final JavaConstant.MethodType specializedLambdaMethod;
                    private final List<FieldDescription.InDefinedShape> declaredFields;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.targetMethod.equals(((Appender) obj).targetMethod) && this.specializedLambdaMethod.equals(((Appender) obj).specializedLambdaMethod) && this.declaredFields.equals(((Appender) obj).declaredFields);
                    }

                    public int hashCode() {
                        return (((((17 * 31) + this.targetMethod.hashCode()) * 31) + this.specializedLambdaMethod.hashCode()) * 31) + this.declaredFields.hashCode();
                    }

                    protected Appender(MethodDescription targetMethod, JavaConstant.MethodType specializedLambdaMethod, List<FieldDescription.InDefinedShape> declaredFields) {
                        this.targetMethod = targetMethod;
                        this.specializedLambdaMethod = specializedLambdaMethod;
                        this.declaredFields = declaredFields;
                    }

                    @Override // net.bytebuddy.implementation.bytecode.ByteCodeAppender
                    public ByteCodeAppender.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodDescription instrumentedMethod) {
                        TypeDescription.Generic returnType;
                        StackManipulation preparation = this.targetMethod.isConstructor() ? new StackManipulation.Compound(TypeCreation.of(this.targetMethod.getDeclaringType().asErasure()), Duplication.SINGLE) : StackManipulation.Trivial.INSTANCE;
                        List<StackManipulation> fieldAccess = new ArrayList<>((this.declaredFields.size() * 2) + 1);
                        for (FieldDescription.InDefinedShape fieldDescription : this.declaredFields) {
                            fieldAccess.add(MethodVariableAccess.loadThis());
                            fieldAccess.add(FieldAccess.forField(fieldDescription).read());
                        }
                        List<StackManipulation> parameterAccess = new ArrayList<>(instrumentedMethod.getParameters().size() * 2);
                        Iterator it = instrumentedMethod.getParameters().iterator();
                        while (it.hasNext()) {
                            ParameterDescription parameterDescription = (ParameterDescription) it.next();
                            parameterAccess.add(MethodVariableAccess.load(parameterDescription));
                            parameterAccess.add(Assigner.DEFAULT.assign(parameterDescription.getType(), ((TypeDescription) this.specializedLambdaMethod.getParameterTypes().get(parameterDescription.getIndex())).asGenericType(), Assigner.Typing.DYNAMIC));
                        }
                        StackManipulation[] stackManipulationArr = new StackManipulation[6];
                        stackManipulationArr[0] = preparation;
                        stackManipulationArr[1] = new StackManipulation.Compound(fieldAccess);
                        stackManipulationArr[2] = new StackManipulation.Compound(parameterAccess);
                        stackManipulationArr[3] = MethodInvocation.invoke(this.targetMethod);
                        Assigner assigner = Assigner.DEFAULT;
                        if (this.targetMethod.isConstructor()) {
                            returnType = this.targetMethod.getDeclaringType().asGenericType();
                        } else {
                            returnType = this.targetMethod.getReturnType();
                        }
                        stackManipulationArr[4] = assigner.assign(returnType, this.specializedLambdaMethod.getReturnType().asGenericType(), Assigner.Typing.DYNAMIC);
                        stackManipulationArr[5] = MethodReturn.of(this.specializedLambdaMethod.getReturnType());
                        return new ByteCodeAppender.Size(new StackManipulation.Compound(stackManipulationArr).apply(methodVisitor, implementationContext).getMaximalSize(), instrumentedMethod.getStackSize());
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$LambdaInstrumentationStrategy$LambdaInstanceFactory$SerializationImplementation.class */
            protected static class SerializationImplementation implements Implementation {
                private final TypeDescription targetType;
                private final TypeDescription lambdaType;
                private final String lambdaMethodName;
                private final JavaConstant.MethodType lambdaMethod;
                private final JavaConstant.MethodHandle targetMethod;
                private final JavaConstant.MethodType specializedMethod;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.lambdaMethodName.equals(((SerializationImplementation) obj).lambdaMethodName) && this.targetType.equals(((SerializationImplementation) obj).targetType) && this.lambdaType.equals(((SerializationImplementation) obj).lambdaType) && this.lambdaMethod.equals(((SerializationImplementation) obj).lambdaMethod) && this.targetMethod.equals(((SerializationImplementation) obj).targetMethod) && this.specializedMethod.equals(((SerializationImplementation) obj).specializedMethod);
                }

                public int hashCode() {
                    return (((((((((((17 * 31) + this.targetType.hashCode()) * 31) + this.lambdaType.hashCode()) * 31) + this.lambdaMethodName.hashCode()) * 31) + this.lambdaMethod.hashCode()) * 31) + this.targetMethod.hashCode()) * 31) + this.specializedMethod.hashCode();
                }

                protected SerializationImplementation(TypeDescription targetType, TypeDescription lambdaType, String lambdaMethodName, JavaConstant.MethodType lambdaMethod, JavaConstant.MethodHandle targetMethod, JavaConstant.MethodType specializedMethod) {
                    this.targetType = targetType;
                    this.lambdaType = lambdaType;
                    this.lambdaMethodName = lambdaMethodName;
                    this.lambdaMethod = lambdaMethod;
                    this.targetMethod = targetMethod;
                    this.specializedMethod = specializedMethod;
                }

                @Override // net.bytebuddy.implementation.Implementation
                public ByteCodeAppender appender(Implementation.Target implementationTarget) {
                    try {
                        TypeDescription serializedLambda = TypeDescription.ForLoadedType.of(Class.forName("java.lang.invoke.SerializedLambda"));
                        List<StackManipulation> lambdaArguments = new ArrayList<>(implementationTarget.getInstrumentedType().getDeclaredFields().size());
                        for (FieldDescription.InDefinedShape fieldDescription : implementationTarget.getInstrumentedType().getDeclaredFields()) {
                            lambdaArguments.add(new StackManipulation.Compound(MethodVariableAccess.loadThis(), FieldAccess.forField(fieldDescription).read(), Assigner.DEFAULT.assign(fieldDescription.getType(), TypeDescription.Generic.OBJECT, Assigner.Typing.STATIC)));
                        }
                        return new ByteCodeAppender.Simple(new StackManipulation.Compound(TypeCreation.of(serializedLambda), Duplication.SINGLE, ClassConstant.of(this.targetType), new TextConstant(this.lambdaType.getInternalName()), new TextConstant(this.lambdaMethodName), new TextConstant(this.lambdaMethod.getDescriptor()), IntegerConstant.forValue(this.targetMethod.getHandleType().getIdentifier()), new TextConstant(this.targetMethod.getOwnerType().getInternalName()), new TextConstant(this.targetMethod.getName()), new TextConstant(this.targetMethod.getDescriptor()), new TextConstant(this.specializedMethod.getDescriptor()), ArrayFactory.forType(TypeDescription.Generic.OBJECT).withValues(lambdaArguments), MethodInvocation.invoke((MethodDescription.InDefinedShape) serializedLambda.getDeclaredMethods().filter(ElementMatchers.isConstructor()).getOnly()), MethodReturn.REFERENCE));
                    } catch (ClassNotFoundException exception) {
                        throw new IllegalStateException("Cannot find class for lambda serialization", exception);
                    }
                }

                @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return instrumentedType;
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$LambdaInstrumentationStrategy$LambdaInstanceFactory$BridgeMethodImplementation.class */
            protected static class BridgeMethodImplementation implements Implementation {
                private final String lambdaMethodName;
                private final JavaConstant.MethodType lambdaMethod;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.lambdaMethodName.equals(((BridgeMethodImplementation) obj).lambdaMethodName) && this.lambdaMethod.equals(((BridgeMethodImplementation) obj).lambdaMethod);
                }

                public int hashCode() {
                    return (((17 * 31) + this.lambdaMethodName.hashCode()) * 31) + this.lambdaMethod.hashCode();
                }

                protected BridgeMethodImplementation(String lambdaMethodName, JavaConstant.MethodType lambdaMethod) {
                    this.lambdaMethodName = lambdaMethodName;
                    this.lambdaMethod = lambdaMethod;
                }

                @Override // net.bytebuddy.implementation.Implementation
                public ByteCodeAppender appender(Implementation.Target implementationTarget) {
                    return new Appender(implementationTarget.invokeSuper(new MethodDescription.SignatureToken(this.lambdaMethodName, this.lambdaMethod.getReturnType(), this.lambdaMethod.getParameterTypes())));
                }

                @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
                public InstrumentedType prepare(InstrumentedType instrumentedType) {
                    return instrumentedType;
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$LambdaInstrumentationStrategy$LambdaInstanceFactory$BridgeMethodImplementation$Appender.class */
                protected static class Appender implements ByteCodeAppender {
                    private final Implementation.SpecialMethodInvocation bridgeTargetInvocation;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.bridgeTargetInvocation.equals(((Appender) obj).bridgeTargetInvocation);
                    }

                    public int hashCode() {
                        return (17 * 31) + this.bridgeTargetInvocation.hashCode();
                    }

                    protected Appender(Implementation.SpecialMethodInvocation bridgeTargetInvocation) {
                        this.bridgeTargetInvocation = bridgeTargetInvocation;
                    }

                    @Override // net.bytebuddy.implementation.bytecode.ByteCodeAppender
                    public ByteCodeAppender.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodDescription instrumentedMethod) {
                        ByteCodeAppender[] byteCodeAppenderArr = new ByteCodeAppender[1];
                        StackManipulation[] stackManipulationArr = new StackManipulation[4];
                        stackManipulationArr[0] = MethodVariableAccess.allArgumentsOf(instrumentedMethod).asBridgeOf(this.bridgeTargetInvocation.getMethodDescription()).prependThisReference();
                        stackManipulationArr[1] = this.bridgeTargetInvocation;
                        stackManipulationArr[2] = this.bridgeTargetInvocation.getMethodDescription().getReturnType().asErasure().isAssignableTo(instrumentedMethod.getReturnType().asErasure()) ? StackManipulation.Trivial.INSTANCE : TypeCasting.to(instrumentedMethod.getReceiverType());
                        stackManipulationArr[3] = MethodReturn.of(instrumentedMethod.getReturnType());
                        byteCodeAppenderArr[0] = new ByteCodeAppender.Simple(stackManipulationArr);
                        return new ByteCodeAppender.Compound(byteCodeAppenderArr).apply(methodVisitor, implementationContext, instrumentedMethod);
                    }
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$LambdaInstrumentationStrategy$MetaFactoryRedirection.class */
        protected enum MetaFactoryRedirection implements AsmVisitorWrapper.ForDeclaredMethods.MethodVisitorWrapper {
            INSTANCE;

            @Override // net.bytebuddy.asm.AsmVisitorWrapper.ForDeclaredMethods.MethodVisitorWrapper
            public MethodVisitor wrap(TypeDescription instrumentedType, MethodDescription instrumentedMethod, MethodVisitor methodVisitor, Implementation.Context implementationContext, TypePool typePool, int writerFlags, int readerFlags) {
                methodVisitor.visitCode();
                methodVisitor.visitMethodInsn(184, LambdaInstrumentationStrategy.UNSAFE_CLASS, "getUnsafe", "()L" + LambdaInstrumentationStrategy.UNSAFE_CLASS + ";", false);
                methodVisitor.visitVarInsn(58, 6);
                methodVisitor.visitVarInsn(25, 6);
                methodVisitor.visitVarInsn(25, 0);
                methodVisitor.visitMethodInsn(182, "java/lang/invoke/MethodHandles$Lookup", "lookupClass", "()Ljava/lang/Class;", false);
                methodVisitor.visitMethodInsn(184, "java/lang/ClassLoader", "getSystemClassLoader", "()Ljava/lang/ClassLoader;", false);
                methodVisitor.visitLdcInsn("net.bytebuddy.agent.builder.LambdaFactory");
                methodVisitor.visitMethodInsn(182, "java/lang/ClassLoader", "loadClass", "(Ljava/lang/String;)Ljava/lang/Class;", false);
                methodVisitor.visitLdcInsn(TypeProxy.REFLECTION_METHOD);
                methodVisitor.visitIntInsn(16, 9);
                methodVisitor.visitTypeInsn(189, TypeProxy.SilentConstruction.Appender.JAVA_LANG_CLASS_INTERNAL_NAME);
                methodVisitor.visitInsn(89);
                methodVisitor.visitInsn(3);
                methodVisitor.visitLdcInsn(net.bytebuddy.jar.asm.Type.getType(TypeProxy.SilentConstruction.Appender.JAVA_LANG_OBJECT_DESCRIPTOR));
                methodVisitor.visitInsn(83);
                methodVisitor.visitInsn(89);
                methodVisitor.visitInsn(4);
                methodVisitor.visitLdcInsn(net.bytebuddy.jar.asm.Type.getType("Ljava/lang/String;"));
                methodVisitor.visitInsn(83);
                methodVisitor.visitInsn(89);
                methodVisitor.visitInsn(5);
                methodVisitor.visitLdcInsn(net.bytebuddy.jar.asm.Type.getType(TypeProxy.SilentConstruction.Appender.JAVA_LANG_OBJECT_DESCRIPTOR));
                methodVisitor.visitInsn(83);
                methodVisitor.visitInsn(89);
                methodVisitor.visitInsn(6);
                methodVisitor.visitLdcInsn(net.bytebuddy.jar.asm.Type.getType(TypeProxy.SilentConstruction.Appender.JAVA_LANG_OBJECT_DESCRIPTOR));
                methodVisitor.visitInsn(83);
                methodVisitor.visitInsn(89);
                methodVisitor.visitInsn(7);
                methodVisitor.visitLdcInsn(net.bytebuddy.jar.asm.Type.getType(TypeProxy.SilentConstruction.Appender.JAVA_LANG_OBJECT_DESCRIPTOR));
                methodVisitor.visitInsn(83);
                methodVisitor.visitInsn(89);
                methodVisitor.visitInsn(8);
                methodVisitor.visitLdcInsn(net.bytebuddy.jar.asm.Type.getType(TypeProxy.SilentConstruction.Appender.JAVA_LANG_OBJECT_DESCRIPTOR));
                methodVisitor.visitInsn(83);
                methodVisitor.visitInsn(89);
                methodVisitor.visitIntInsn(16, 6);
                methodVisitor.visitFieldInsn(178, "java/lang/Boolean", "TYPE", "Ljava/lang/Class;");
                methodVisitor.visitInsn(83);
                methodVisitor.visitInsn(89);
                methodVisitor.visitIntInsn(16, 7);
                methodVisitor.visitLdcInsn(net.bytebuddy.jar.asm.Type.getType("Ljava/util/List;"));
                methodVisitor.visitInsn(83);
                methodVisitor.visitInsn(89);
                methodVisitor.visitIntInsn(16, 8);
                methodVisitor.visitLdcInsn(net.bytebuddy.jar.asm.Type.getType("Ljava/util/List;"));
                methodVisitor.visitInsn(83);
                methodVisitor.visitMethodInsn(182, TypeProxy.SilentConstruction.Appender.JAVA_LANG_CLASS_INTERNAL_NAME, "getDeclaredMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;", false);
                methodVisitor.visitInsn(1);
                methodVisitor.visitIntInsn(16, 9);
                methodVisitor.visitTypeInsn(189, TypeProxy.SilentConstruction.Appender.JAVA_LANG_OBJECT_INTERNAL_NAME);
                methodVisitor.visitInsn(89);
                methodVisitor.visitInsn(3);
                methodVisitor.visitVarInsn(25, 0);
                methodVisitor.visitInsn(83);
                methodVisitor.visitInsn(89);
                methodVisitor.visitInsn(4);
                methodVisitor.visitVarInsn(25, 1);
                methodVisitor.visitInsn(83);
                methodVisitor.visitInsn(89);
                methodVisitor.visitInsn(5);
                methodVisitor.visitVarInsn(25, 2);
                methodVisitor.visitInsn(83);
                methodVisitor.visitInsn(89);
                methodVisitor.visitInsn(6);
                methodVisitor.visitVarInsn(25, 3);
                methodVisitor.visitInsn(83);
                methodVisitor.visitInsn(89);
                methodVisitor.visitInsn(7);
                methodVisitor.visitVarInsn(25, 4);
                methodVisitor.visitInsn(83);
                methodVisitor.visitInsn(89);
                methodVisitor.visitInsn(8);
                methodVisitor.visitVarInsn(25, 5);
                methodVisitor.visitInsn(83);
                methodVisitor.visitInsn(89);
                methodVisitor.visitIntInsn(16, 6);
                methodVisitor.visitInsn(3);
                methodVisitor.visitMethodInsn(184, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false);
                methodVisitor.visitInsn(83);
                methodVisitor.visitInsn(89);
                methodVisitor.visitIntInsn(16, 7);
                methodVisitor.visitMethodInsn(184, "java/util/Collections", "emptyList", "()Ljava/util/List;", false);
                methodVisitor.visitInsn(83);
                methodVisitor.visitInsn(89);
                methodVisitor.visitIntInsn(16, 8);
                methodVisitor.visitMethodInsn(184, "java/util/Collections", "emptyList", "()Ljava/util/List;", false);
                methodVisitor.visitInsn(83);
                methodVisitor.visitMethodInsn(182, "java/lang/reflect/Method", "invoke", "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", false);
                methodVisitor.visitTypeInsn(192, "[B");
                methodVisitor.visitInsn(1);
                methodVisitor.visitMethodInsn(182, LambdaInstrumentationStrategy.UNSAFE_CLASS, "defineAnonymousClass", "(Ljava/lang/Class;[B[Ljava/lang/Object;)Ljava/lang/Class;", false);
                methodVisitor.visitVarInsn(58, 7);
                methodVisitor.visitVarInsn(25, 6);
                methodVisitor.visitVarInsn(25, 7);
                methodVisitor.visitMethodInsn(182, LambdaInstrumentationStrategy.UNSAFE_CLASS, "ensureClassInitialized", "(Ljava/lang/Class;)V", false);
                methodVisitor.visitVarInsn(25, 2);
                methodVisitor.visitMethodInsn(182, "java/lang/invoke/MethodType", "parameterCount", "()I", false);
                Label conditionalDefault = new Label();
                methodVisitor.visitJumpInsn(154, conditionalDefault);
                methodVisitor.visitTypeInsn(187, "java/lang/invoke/ConstantCallSite");
                methodVisitor.visitInsn(89);
                methodVisitor.visitVarInsn(25, 2);
                methodVisitor.visitMethodInsn(182, "java/lang/invoke/MethodType", "returnType", "()Ljava/lang/Class;", false);
                methodVisitor.visitVarInsn(25, 7);
                methodVisitor.visitMethodInsn(182, TypeProxy.SilentConstruction.Appender.JAVA_LANG_CLASS_INTERNAL_NAME, "getDeclaredConstructors", "()[Ljava/lang/reflect/Constructor;", false);
                methodVisitor.visitInsn(3);
                methodVisitor.visitInsn(50);
                methodVisitor.visitInsn(3);
                methodVisitor.visitTypeInsn(189, TypeProxy.SilentConstruction.Appender.JAVA_LANG_OBJECT_INTERNAL_NAME);
                methodVisitor.visitMethodInsn(182, TypeProxy.SilentConstruction.Appender.JAVA_LANG_CONSTRUCTOR_INTERNAL_NAME, TypeProxy.SilentConstruction.Appender.NEW_INSTANCE_METHOD_NAME, TypeProxy.SilentConstruction.Appender.NEW_INSTANCE_METHOD_DESCRIPTOR, false);
                methodVisitor.visitMethodInsn(184, "java/lang/invoke/MethodHandles", "constant", "(Ljava/lang/Class;Ljava/lang/Object;)Ljava/lang/invoke/MethodHandle;", false);
                methodVisitor.visitMethodInsn(183, "java/lang/invoke/ConstantCallSite", "<init>", "(Ljava/lang/invoke/MethodHandle;)V", false);
                Label conditionalAlternative = new Label();
                methodVisitor.visitJumpInsn(167, conditionalAlternative);
                methodVisitor.visitLabel(conditionalDefault);
                methodVisitor.visitFrame(1, 2, new Object[]{LambdaInstrumentationStrategy.UNSAFE_CLASS, TypeProxy.SilentConstruction.Appender.JAVA_LANG_CLASS_INTERNAL_NAME}, 0, null);
                methodVisitor.visitTypeInsn(187, "java/lang/invoke/ConstantCallSite");
                methodVisitor.visitInsn(89);
                methodVisitor.visitFieldInsn(178, "java/lang/invoke/MethodHandles$Lookup", "IMPL_LOOKUP", "Ljava/lang/invoke/MethodHandles$Lookup;");
                methodVisitor.visitVarInsn(25, 7);
                methodVisitor.visitLdcInsn("get$Lambda");
                methodVisitor.visitVarInsn(25, 2);
                methodVisitor.visitMethodInsn(182, "java/lang/invoke/MethodHandles$Lookup", "findStatic", "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/MethodHandle;", false);
                methodVisitor.visitMethodInsn(183, "java/lang/invoke/ConstantCallSite", "<init>", "(Ljava/lang/invoke/MethodHandle;)V", false);
                methodVisitor.visitLabel(conditionalAlternative);
                methodVisitor.visitFrame(4, 0, null, 1, new Object[]{"java/lang/invoke/CallSite"});
                methodVisitor.visitInsn(176);
                methodVisitor.visitMaxs(8, 8);
                methodVisitor.visitEnd();
                return LambdaInstrumentationStrategy.IGNORE_ORIGINAL;
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$LambdaInstrumentationStrategy$AlternativeMetaFactoryRedirection.class */
        protected enum AlternativeMetaFactoryRedirection implements AsmVisitorWrapper.ForDeclaredMethods.MethodVisitorWrapper {
            INSTANCE;

            @Override // net.bytebuddy.asm.AsmVisitorWrapper.ForDeclaredMethods.MethodVisitorWrapper
            public MethodVisitor wrap(TypeDescription instrumentedType, MethodDescription instrumentedMethod, MethodVisitor methodVisitor, Implementation.Context implementationContext, TypePool typePool, int writerFlags, int readerFlags) {
                methodVisitor.visitCode();
                methodVisitor.visitVarInsn(25, 3);
                methodVisitor.visitInsn(6);
                methodVisitor.visitInsn(50);
                methodVisitor.visitTypeInsn(192, "java/lang/Integer");
                methodVisitor.visitMethodInsn(182, "java/lang/Integer", "intValue", "()I", false);
                methodVisitor.visitVarInsn(54, 4);
                methodVisitor.visitInsn(7);
                methodVisitor.visitVarInsn(54, 5);
                methodVisitor.visitVarInsn(21, 4);
                methodVisitor.visitInsn(5);
                methodVisitor.visitInsn(126);
                Label markerInterfaceLoop = new Label();
                methodVisitor.visitJumpInsn(153, markerInterfaceLoop);
                methodVisitor.visitVarInsn(25, 3);
                methodVisitor.visitVarInsn(21, 5);
                methodVisitor.visitIincInsn(5, 1);
                methodVisitor.visitInsn(50);
                methodVisitor.visitTypeInsn(192, "java/lang/Integer");
                methodVisitor.visitMethodInsn(182, "java/lang/Integer", "intValue", "()I", false);
                methodVisitor.visitVarInsn(54, 7);
                methodVisitor.visitVarInsn(21, 7);
                methodVisitor.visitTypeInsn(189, TypeProxy.SilentConstruction.Appender.JAVA_LANG_CLASS_INTERNAL_NAME);
                methodVisitor.visitVarInsn(58, 6);
                methodVisitor.visitVarInsn(25, 3);
                methodVisitor.visitVarInsn(21, 5);
                methodVisitor.visitVarInsn(25, 6);
                methodVisitor.visitInsn(3);
                methodVisitor.visitVarInsn(21, 7);
                methodVisitor.visitMethodInsn(184, "java/lang/System", "arraycopy", "(Ljava/lang/Object;ILjava/lang/Object;II)V", false);
                methodVisitor.visitVarInsn(21, 5);
                methodVisitor.visitVarInsn(21, 7);
                methodVisitor.visitInsn(96);
                methodVisitor.visitVarInsn(54, 5);
                Label markerInterfaceExit = new Label();
                methodVisitor.visitJumpInsn(167, markerInterfaceExit);
                methodVisitor.visitLabel(markerInterfaceLoop);
                methodVisitor.visitFrame(1, 2, new Object[]{Opcodes.INTEGER, Opcodes.INTEGER}, 0, null);
                methodVisitor.visitInsn(3);
                methodVisitor.visitTypeInsn(189, TypeProxy.SilentConstruction.Appender.JAVA_LANG_CLASS_INTERNAL_NAME);
                methodVisitor.visitVarInsn(58, 6);
                methodVisitor.visitLabel(markerInterfaceExit);
                methodVisitor.visitFrame(1, 1, new Object[]{"[Ljava/lang/Class;"}, 0, null);
                methodVisitor.visitVarInsn(21, 4);
                methodVisitor.visitInsn(7);
                methodVisitor.visitInsn(126);
                Label additionalBridgesLoop = new Label();
                methodVisitor.visitJumpInsn(153, additionalBridgesLoop);
                methodVisitor.visitVarInsn(25, 3);
                methodVisitor.visitVarInsn(21, 5);
                methodVisitor.visitIincInsn(5, 1);
                methodVisitor.visitInsn(50);
                methodVisitor.visitTypeInsn(192, "java/lang/Integer");
                methodVisitor.visitMethodInsn(182, "java/lang/Integer", "intValue", "()I", false);
                methodVisitor.visitVarInsn(54, 8);
                methodVisitor.visitVarInsn(21, 8);
                methodVisitor.visitTypeInsn(189, "java/lang/invoke/MethodType");
                methodVisitor.visitVarInsn(58, 7);
                methodVisitor.visitVarInsn(25, 3);
                methodVisitor.visitVarInsn(21, 5);
                methodVisitor.visitVarInsn(25, 7);
                methodVisitor.visitInsn(3);
                methodVisitor.visitVarInsn(21, 8);
                methodVisitor.visitMethodInsn(184, "java/lang/System", "arraycopy", "(Ljava/lang/Object;ILjava/lang/Object;II)V", false);
                Label additionalBridgesExit = new Label();
                methodVisitor.visitJumpInsn(167, additionalBridgesExit);
                methodVisitor.visitLabel(additionalBridgesLoop);
                methodVisitor.visitFrame(3, 0, null, 0, null);
                methodVisitor.visitInsn(3);
                methodVisitor.visitTypeInsn(189, "java/lang/invoke/MethodType");
                methodVisitor.visitVarInsn(58, 7);
                methodVisitor.visitLabel(additionalBridgesExit);
                methodVisitor.visitFrame(1, 1, new Object[]{"[Ljava/lang/invoke/MethodType;"}, 0, null);
                methodVisitor.visitMethodInsn(184, LambdaInstrumentationStrategy.UNSAFE_CLASS, "getUnsafe", "()L" + LambdaInstrumentationStrategy.UNSAFE_CLASS + ";", false);
                methodVisitor.visitVarInsn(58, 8);
                methodVisitor.visitVarInsn(25, 8);
                methodVisitor.visitVarInsn(25, 0);
                methodVisitor.visitMethodInsn(182, "java/lang/invoke/MethodHandles$Lookup", "lookupClass", "()Ljava/lang/Class;", false);
                methodVisitor.visitMethodInsn(184, "java/lang/ClassLoader", "getSystemClassLoader", "()Ljava/lang/ClassLoader;", false);
                methodVisitor.visitLdcInsn("net.bytebuddy.agent.builder.LambdaFactory");
                methodVisitor.visitMethodInsn(182, "java/lang/ClassLoader", "loadClass", "(Ljava/lang/String;)Ljava/lang/Class;", false);
                methodVisitor.visitLdcInsn(TypeProxy.REFLECTION_METHOD);
                methodVisitor.visitIntInsn(16, 9);
                methodVisitor.visitTypeInsn(189, TypeProxy.SilentConstruction.Appender.JAVA_LANG_CLASS_INTERNAL_NAME);
                methodVisitor.visitInsn(89);
                methodVisitor.visitInsn(3);
                methodVisitor.visitLdcInsn(net.bytebuddy.jar.asm.Type.getType(TypeProxy.SilentConstruction.Appender.JAVA_LANG_OBJECT_DESCRIPTOR));
                methodVisitor.visitInsn(83);
                methodVisitor.visitInsn(89);
                methodVisitor.visitInsn(4);
                methodVisitor.visitLdcInsn(net.bytebuddy.jar.asm.Type.getType("Ljava/lang/String;"));
                methodVisitor.visitInsn(83);
                methodVisitor.visitInsn(89);
                methodVisitor.visitInsn(5);
                methodVisitor.visitLdcInsn(net.bytebuddy.jar.asm.Type.getType(TypeProxy.SilentConstruction.Appender.JAVA_LANG_OBJECT_DESCRIPTOR));
                methodVisitor.visitInsn(83);
                methodVisitor.visitInsn(89);
                methodVisitor.visitInsn(6);
                methodVisitor.visitLdcInsn(net.bytebuddy.jar.asm.Type.getType(TypeProxy.SilentConstruction.Appender.JAVA_LANG_OBJECT_DESCRIPTOR));
                methodVisitor.visitInsn(83);
                methodVisitor.visitInsn(89);
                methodVisitor.visitInsn(7);
                methodVisitor.visitLdcInsn(net.bytebuddy.jar.asm.Type.getType(TypeProxy.SilentConstruction.Appender.JAVA_LANG_OBJECT_DESCRIPTOR));
                methodVisitor.visitInsn(83);
                methodVisitor.visitInsn(89);
                methodVisitor.visitInsn(8);
                methodVisitor.visitLdcInsn(net.bytebuddy.jar.asm.Type.getType(TypeProxy.SilentConstruction.Appender.JAVA_LANG_OBJECT_DESCRIPTOR));
                methodVisitor.visitInsn(83);
                methodVisitor.visitInsn(89);
                methodVisitor.visitIntInsn(16, 6);
                methodVisitor.visitFieldInsn(178, "java/lang/Boolean", "TYPE", "Ljava/lang/Class;");
                methodVisitor.visitInsn(83);
                methodVisitor.visitInsn(89);
                methodVisitor.visitIntInsn(16, 7);
                methodVisitor.visitLdcInsn(net.bytebuddy.jar.asm.Type.getType("Ljava/util/List;"));
                methodVisitor.visitInsn(83);
                methodVisitor.visitInsn(89);
                methodVisitor.visitIntInsn(16, 8);
                methodVisitor.visitLdcInsn(net.bytebuddy.jar.asm.Type.getType("Ljava/util/List;"));
                methodVisitor.visitInsn(83);
                methodVisitor.visitMethodInsn(182, TypeProxy.SilentConstruction.Appender.JAVA_LANG_CLASS_INTERNAL_NAME, "getDeclaredMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;", false);
                methodVisitor.visitInsn(1);
                methodVisitor.visitIntInsn(16, 9);
                methodVisitor.visitTypeInsn(189, TypeProxy.SilentConstruction.Appender.JAVA_LANG_OBJECT_INTERNAL_NAME);
                methodVisitor.visitInsn(89);
                methodVisitor.visitInsn(3);
                methodVisitor.visitVarInsn(25, 0);
                methodVisitor.visitInsn(83);
                methodVisitor.visitInsn(89);
                methodVisitor.visitInsn(4);
                methodVisitor.visitVarInsn(25, 1);
                methodVisitor.visitInsn(83);
                methodVisitor.visitInsn(89);
                methodVisitor.visitInsn(5);
                methodVisitor.visitVarInsn(25, 2);
                methodVisitor.visitInsn(83);
                methodVisitor.visitInsn(89);
                methodVisitor.visitInsn(6);
                methodVisitor.visitVarInsn(25, 3);
                methodVisitor.visitInsn(3);
                methodVisitor.visitInsn(50);
                methodVisitor.visitInsn(83);
                methodVisitor.visitInsn(89);
                methodVisitor.visitInsn(7);
                methodVisitor.visitVarInsn(25, 3);
                methodVisitor.visitInsn(4);
                methodVisitor.visitInsn(50);
                methodVisitor.visitInsn(83);
                methodVisitor.visitInsn(89);
                methodVisitor.visitInsn(8);
                methodVisitor.visitVarInsn(25, 3);
                methodVisitor.visitInsn(5);
                methodVisitor.visitInsn(50);
                methodVisitor.visitInsn(83);
                methodVisitor.visitInsn(89);
                methodVisitor.visitIntInsn(16, 6);
                methodVisitor.visitVarInsn(21, 4);
                methodVisitor.visitInsn(4);
                methodVisitor.visitInsn(126);
                Label callSiteConditional = new Label();
                methodVisitor.visitJumpInsn(153, callSiteConditional);
                methodVisitor.visitInsn(4);
                Label callSiteAlternative = new Label();
                methodVisitor.visitJumpInsn(167, callSiteAlternative);
                methodVisitor.visitLabel(callSiteConditional);
                methodVisitor.visitFrame(0, 9, new Object[]{"java/lang/invoke/MethodHandles$Lookup", "java/lang/String", "java/lang/invoke/MethodType", "[Ljava/lang/Object;", Opcodes.INTEGER, Opcodes.INTEGER, "[Ljava/lang/Class;", "[Ljava/lang/invoke/MethodType;", LambdaInstrumentationStrategy.UNSAFE_CLASS}, 7, new Object[]{LambdaInstrumentationStrategy.UNSAFE_CLASS, TypeProxy.SilentConstruction.Appender.JAVA_LANG_CLASS_INTERNAL_NAME, "java/lang/reflect/Method", Opcodes.NULL, "[Ljava/lang/Object;", "[Ljava/lang/Object;", Opcodes.INTEGER});
                methodVisitor.visitInsn(3);
                methodVisitor.visitLabel(callSiteAlternative);
                methodVisitor.visitFrame(0, 9, new Object[]{"java/lang/invoke/MethodHandles$Lookup", "java/lang/String", "java/lang/invoke/MethodType", "[Ljava/lang/Object;", Opcodes.INTEGER, Opcodes.INTEGER, "[Ljava/lang/Class;", "[Ljava/lang/invoke/MethodType;", LambdaInstrumentationStrategy.UNSAFE_CLASS}, 8, new Object[]{LambdaInstrumentationStrategy.UNSAFE_CLASS, TypeProxy.SilentConstruction.Appender.JAVA_LANG_CLASS_INTERNAL_NAME, "java/lang/reflect/Method", Opcodes.NULL, "[Ljava/lang/Object;", "[Ljava/lang/Object;", Opcodes.INTEGER, Opcodes.INTEGER});
                methodVisitor.visitMethodInsn(184, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false);
                methodVisitor.visitInsn(83);
                methodVisitor.visitInsn(89);
                methodVisitor.visitIntInsn(16, 7);
                methodVisitor.visitVarInsn(25, 6);
                methodVisitor.visitMethodInsn(184, "java/util/Arrays", "asList", "([Ljava/lang/Object;)Ljava/util/List;", false);
                methodVisitor.visitInsn(83);
                methodVisitor.visitInsn(89);
                methodVisitor.visitIntInsn(16, 8);
                methodVisitor.visitVarInsn(25, 7);
                methodVisitor.visitMethodInsn(184, "java/util/Arrays", "asList", "([Ljava/lang/Object;)Ljava/util/List;", false);
                methodVisitor.visitInsn(83);
                methodVisitor.visitMethodInsn(182, "java/lang/reflect/Method", "invoke", "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", false);
                methodVisitor.visitTypeInsn(192, "[B");
                methodVisitor.visitInsn(1);
                methodVisitor.visitMethodInsn(182, LambdaInstrumentationStrategy.UNSAFE_CLASS, "defineAnonymousClass", "(Ljava/lang/Class;[B[Ljava/lang/Object;)Ljava/lang/Class;", false);
                methodVisitor.visitVarInsn(58, 9);
                methodVisitor.visitVarInsn(25, 8);
                methodVisitor.visitVarInsn(25, 9);
                methodVisitor.visitMethodInsn(182, LambdaInstrumentationStrategy.UNSAFE_CLASS, "ensureClassInitialized", "(Ljava/lang/Class;)V", false);
                methodVisitor.visitVarInsn(25, 2);
                methodVisitor.visitMethodInsn(182, "java/lang/invoke/MethodType", "parameterCount", "()I", false);
                Label callSiteJump = new Label();
                methodVisitor.visitJumpInsn(154, callSiteJump);
                methodVisitor.visitTypeInsn(187, "java/lang/invoke/ConstantCallSite");
                methodVisitor.visitInsn(89);
                methodVisitor.visitVarInsn(25, 2);
                methodVisitor.visitMethodInsn(182, "java/lang/invoke/MethodType", "returnType", "()Ljava/lang/Class;", false);
                methodVisitor.visitVarInsn(25, 9);
                methodVisitor.visitMethodInsn(182, TypeProxy.SilentConstruction.Appender.JAVA_LANG_CLASS_INTERNAL_NAME, "getDeclaredConstructors", "()[Ljava/lang/reflect/Constructor;", false);
                methodVisitor.visitInsn(3);
                methodVisitor.visitInsn(50);
                methodVisitor.visitInsn(3);
                methodVisitor.visitTypeInsn(189, TypeProxy.SilentConstruction.Appender.JAVA_LANG_OBJECT_INTERNAL_NAME);
                methodVisitor.visitMethodInsn(182, TypeProxy.SilentConstruction.Appender.JAVA_LANG_CONSTRUCTOR_INTERNAL_NAME, TypeProxy.SilentConstruction.Appender.NEW_INSTANCE_METHOD_NAME, TypeProxy.SilentConstruction.Appender.NEW_INSTANCE_METHOD_DESCRIPTOR, false);
                methodVisitor.visitMethodInsn(184, "java/lang/invoke/MethodHandles", "constant", "(Ljava/lang/Class;Ljava/lang/Object;)Ljava/lang/invoke/MethodHandle;", false);
                methodVisitor.visitMethodInsn(183, "java/lang/invoke/ConstantCallSite", "<init>", "(Ljava/lang/invoke/MethodHandle;)V", false);
                Label callSiteExit = new Label();
                methodVisitor.visitJumpInsn(167, callSiteExit);
                methodVisitor.visitLabel(callSiteJump);
                methodVisitor.visitFrame(1, 1, new Object[]{TypeProxy.SilentConstruction.Appender.JAVA_LANG_CLASS_INTERNAL_NAME}, 0, null);
                methodVisitor.visitTypeInsn(187, "java/lang/invoke/ConstantCallSite");
                methodVisitor.visitInsn(89);
                methodVisitor.visitFieldInsn(178, "java/lang/invoke/MethodHandles$Lookup", "IMPL_LOOKUP", "Ljava/lang/invoke/MethodHandles$Lookup;");
                methodVisitor.visitVarInsn(25, 9);
                methodVisitor.visitLdcInsn("get$Lambda");
                methodVisitor.visitVarInsn(25, 2);
                methodVisitor.visitMethodInsn(182, "java/lang/invoke/MethodHandles$Lookup", "findStatic", "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/MethodHandle;", false);
                methodVisitor.visitMethodInsn(183, "java/lang/invoke/ConstantCallSite", "<init>", "(Ljava/lang/invoke/MethodHandle;)V", false);
                methodVisitor.visitLabel(callSiteExit);
                methodVisitor.visitFrame(4, 0, null, 1, new Object[]{"java/lang/invoke/CallSite"});
                methodVisitor.visitInsn(176);
                methodVisitor.visitMaxs(9, 10);
                methodVisitor.visitEnd();
                return LambdaInstrumentationStrategy.IGNORE_ORIGINAL;
            }
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Default.class */
    public static class Default implements AgentBuilder {
        private static final String INSTALLER_TYPE = "net.bytebuddy.agent.Installer";
        private static final String INSTRUMENTATION_GETTER = "getInstrumentation";
        private static final Object STATIC_MEMBER = null;
        private static final byte[] NO_TRANSFORMATION = null;
        private static final Class<?> NO_LOADED_TYPE = null;
        private static final Dispatcher DISPATCHER = (Dispatcher) AccessController.doPrivileged(Dispatcher.CreationAction.INSTANCE);
        private static final CircularityLock DEFAULT_LOCK = new CircularityLock.Default();
        protected final ByteBuddy byteBuddy;
        protected final Listener listener;
        protected final CircularityLock circularityLock;
        protected final PoolStrategy poolStrategy;
        protected final TypeStrategy typeStrategy;
        protected final LocationStrategy locationStrategy;
        protected final NativeMethodStrategy nativeMethodStrategy;
        protected final TransformerDecorator transformerDecorator;
        protected final InitializationStrategy initializationStrategy;
        protected final RedefinitionStrategy redefinitionStrategy;
        protected final RedefinitionStrategy.DiscoveryStrategy redefinitionDiscoveryStrategy;
        protected final RedefinitionStrategy.BatchAllocator redefinitionBatchAllocator;
        protected final RedefinitionStrategy.Listener redefinitionListener;
        protected final RedefinitionStrategy.ResubmissionStrategy redefinitionResubmissionStrategy;
        protected final InjectionStrategy injectionStrategy;
        protected final LambdaInstrumentationStrategy lambdaInstrumentationStrategy;
        protected final DescriptionStrategy descriptionStrategy;
        protected final FallbackStrategy fallbackStrategy;
        protected final ClassFileBufferStrategy classFileBufferStrategy;
        protected final InstallationListener installationListener;
        protected final RawMatcher ignoreMatcher;
        protected final List<Transformation> transformations;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.redefinitionStrategy.equals(((Default) obj).redefinitionStrategy) && this.lambdaInstrumentationStrategy.equals(((Default) obj).lambdaInstrumentationStrategy) && this.byteBuddy.equals(((Default) obj).byteBuddy) && this.listener.equals(((Default) obj).listener) && this.circularityLock.equals(((Default) obj).circularityLock) && this.poolStrategy.equals(((Default) obj).poolStrategy) && this.typeStrategy.equals(((Default) obj).typeStrategy) && this.locationStrategy.equals(((Default) obj).locationStrategy) && this.nativeMethodStrategy.equals(((Default) obj).nativeMethodStrategy) && this.transformerDecorator.equals(((Default) obj).transformerDecorator) && this.initializationStrategy.equals(((Default) obj).initializationStrategy) && this.redefinitionDiscoveryStrategy.equals(((Default) obj).redefinitionDiscoveryStrategy) && this.redefinitionBatchAllocator.equals(((Default) obj).redefinitionBatchAllocator) && this.redefinitionListener.equals(((Default) obj).redefinitionListener) && this.redefinitionResubmissionStrategy.equals(((Default) obj).redefinitionResubmissionStrategy) && this.injectionStrategy.equals(((Default) obj).injectionStrategy) && this.descriptionStrategy.equals(((Default) obj).descriptionStrategy) && this.fallbackStrategy.equals(((Default) obj).fallbackStrategy) && this.classFileBufferStrategy.equals(((Default) obj).classFileBufferStrategy) && this.installationListener.equals(((Default) obj).installationListener) && this.ignoreMatcher.equals(((Default) obj).ignoreMatcher) && this.transformations.equals(((Default) obj).transformations);
        }

        public int hashCode() {
            return (((((((((((((((((((((((((((((((((((((((((((17 * 31) + this.byteBuddy.hashCode()) * 31) + this.listener.hashCode()) * 31) + this.circularityLock.hashCode()) * 31) + this.poolStrategy.hashCode()) * 31) + this.typeStrategy.hashCode()) * 31) + this.locationStrategy.hashCode()) * 31) + this.nativeMethodStrategy.hashCode()) * 31) + this.transformerDecorator.hashCode()) * 31) + this.initializationStrategy.hashCode()) * 31) + this.redefinitionStrategy.hashCode()) * 31) + this.redefinitionDiscoveryStrategy.hashCode()) * 31) + this.redefinitionBatchAllocator.hashCode()) * 31) + this.redefinitionListener.hashCode()) * 31) + this.redefinitionResubmissionStrategy.hashCode()) * 31) + this.injectionStrategy.hashCode()) * 31) + this.lambdaInstrumentationStrategy.hashCode()) * 31) + this.descriptionStrategy.hashCode()) * 31) + this.fallbackStrategy.hashCode()) * 31) + this.classFileBufferStrategy.hashCode()) * 31) + this.installationListener.hashCode()) * 31) + this.ignoreMatcher.hashCode()) * 31) + this.transformations.hashCode();
        }

        static /* synthetic */ Class access$1400() {
            return NO_LOADED_TYPE;
        }

        public Default() {
            this(new ByteBuddy());
        }

        public Default(ByteBuddy byteBuddy) {
            this(byteBuddy, Listener.NoOp.INSTANCE, DEFAULT_LOCK, PoolStrategy.Default.FAST, TypeStrategy.Default.REBASE, LocationStrategy.ForClassLoader.STRONG, NativeMethodStrategy.Disabled.INSTANCE, TransformerDecorator.NoOp.INSTANCE, new InitializationStrategy.SelfInjection.Split(), RedefinitionStrategy.DISABLED, RedefinitionStrategy.DiscoveryStrategy.SinglePass.INSTANCE, RedefinitionStrategy.BatchAllocator.ForTotal.INSTANCE, RedefinitionStrategy.Listener.NoOp.INSTANCE, RedefinitionStrategy.ResubmissionStrategy.Disabled.INSTANCE, InjectionStrategy.UsingReflection.INSTANCE, LambdaInstrumentationStrategy.DISABLED, DescriptionStrategy.Default.HYBRID, FallbackStrategy.ByThrowableType.ofOptionalTypes(), ClassFileBufferStrategy.Default.RETAINING, InstallationListener.NoOp.INSTANCE, new RawMatcher.Disjunction(new RawMatcher.ForElementMatchers(ElementMatchers.any(), ElementMatchers.isBootstrapClassLoader().or(ElementMatchers.isExtensionClassLoader())), new RawMatcher.ForElementMatchers(ElementMatchers.nameStartsWith("net.bytebuddy.").or(ElementMatchers.nameStartsWith("sun.reflect.")).or(ElementMatchers.isSynthetic()))), Collections.emptyList());
        }

        protected Default(ByteBuddy byteBuddy, Listener listener, CircularityLock circularityLock, PoolStrategy poolStrategy, TypeStrategy typeStrategy, LocationStrategy locationStrategy, NativeMethodStrategy nativeMethodStrategy, TransformerDecorator transformerDecorator, InitializationStrategy initializationStrategy, RedefinitionStrategy redefinitionStrategy, RedefinitionStrategy.DiscoveryStrategy redefinitionDiscoveryStrategy, RedefinitionStrategy.BatchAllocator redefinitionBatchAllocator, RedefinitionStrategy.Listener redefinitionListener, RedefinitionStrategy.ResubmissionStrategy redefinitionResubmissionStrategy, InjectionStrategy injectionStrategy, LambdaInstrumentationStrategy lambdaInstrumentationStrategy, DescriptionStrategy descriptionStrategy, FallbackStrategy fallbackStrategy, ClassFileBufferStrategy classFileBufferStrategy, InstallationListener installationListener, RawMatcher ignoreMatcher, List<Transformation> transformations) {
            this.byteBuddy = byteBuddy;
            this.listener = listener;
            this.circularityLock = circularityLock;
            this.poolStrategy = poolStrategy;
            this.typeStrategy = typeStrategy;
            this.locationStrategy = locationStrategy;
            this.nativeMethodStrategy = nativeMethodStrategy;
            this.transformerDecorator = transformerDecorator;
            this.initializationStrategy = initializationStrategy;
            this.redefinitionStrategy = redefinitionStrategy;
            this.redefinitionDiscoveryStrategy = redefinitionDiscoveryStrategy;
            this.redefinitionBatchAllocator = redefinitionBatchAllocator;
            this.redefinitionListener = redefinitionListener;
            this.redefinitionResubmissionStrategy = redefinitionResubmissionStrategy;
            this.injectionStrategy = injectionStrategy;
            this.lambdaInstrumentationStrategy = lambdaInstrumentationStrategy;
            this.descriptionStrategy = descriptionStrategy;
            this.fallbackStrategy = fallbackStrategy;
            this.classFileBufferStrategy = classFileBufferStrategy;
            this.installationListener = installationListener;
            this.ignoreMatcher = ignoreMatcher;
            this.transformations = transformations;
        }

        public static AgentBuilder of(Plugin... plugin) {
            return of(Arrays.asList(plugin));
        }

        public static AgentBuilder of(List<? extends Plugin> plugins) {
            return of(EntryPoint.Default.REBASE, plugins);
        }

        public static AgentBuilder of(EntryPoint entryPoint, Plugin... plugin) {
            return of(entryPoint, Arrays.asList(plugin));
        }

        public static AgentBuilder of(EntryPoint entryPoint, List<? extends Plugin> plugins) {
            return of(entryPoint, ClassFileVersion.ofThisVm(), plugins);
        }

        public static AgentBuilder of(ClassFileVersion classFileVersion, Plugin... plugin) {
            return of(classFileVersion, Arrays.asList(plugin));
        }

        public static AgentBuilder of(ClassFileVersion classFileVersion, List<? extends Plugin> plugins) {
            return of(EntryPoint.Default.REBASE, classFileVersion, plugins);
        }

        public static AgentBuilder of(EntryPoint entryPoint, ClassFileVersion classFileVersion, Plugin... plugin) {
            return of(entryPoint, classFileVersion, Arrays.asList(plugin));
        }

        public static AgentBuilder of(EntryPoint entryPoint, ClassFileVersion classFileVersion, List<? extends Plugin> plugins) {
            AgentBuilder agentBuilder = new Default(entryPoint.byteBuddy(classFileVersion)).with(new TypeStrategy.ForBuildEntryPoint(entryPoint));
            for (Plugin plugin : plugins) {
                agentBuilder = agentBuilder.type(plugin).transform(new Transformer.ForBuildPlugin(plugin));
            }
            return agentBuilder;
        }

        @Override // net.bytebuddy.agent.builder.AgentBuilder
        public AgentBuilder with(ByteBuddy byteBuddy) {
            return new Default(byteBuddy, this.listener, this.circularityLock, this.poolStrategy, this.typeStrategy, this.locationStrategy, this.nativeMethodStrategy, this.transformerDecorator, this.initializationStrategy, this.redefinitionStrategy, this.redefinitionDiscoveryStrategy, this.redefinitionBatchAllocator, this.redefinitionListener, this.redefinitionResubmissionStrategy, this.injectionStrategy, this.lambdaInstrumentationStrategy, this.descriptionStrategy, this.fallbackStrategy, this.classFileBufferStrategy, this.installationListener, this.ignoreMatcher, this.transformations);
        }

        @Override // net.bytebuddy.agent.builder.AgentBuilder
        public AgentBuilder with(Listener listener) {
            return new Default(this.byteBuddy, new Listener.Compound(this.listener, listener), this.circularityLock, this.poolStrategy, this.typeStrategy, this.locationStrategy, this.nativeMethodStrategy, this.transformerDecorator, this.initializationStrategy, this.redefinitionStrategy, this.redefinitionDiscoveryStrategy, this.redefinitionBatchAllocator, this.redefinitionListener, this.redefinitionResubmissionStrategy, this.injectionStrategy, this.lambdaInstrumentationStrategy, this.descriptionStrategy, this.fallbackStrategy, this.classFileBufferStrategy, this.installationListener, this.ignoreMatcher, this.transformations);
        }

        @Override // net.bytebuddy.agent.builder.AgentBuilder
        public AgentBuilder with(CircularityLock circularityLock) {
            return new Default(this.byteBuddy, this.listener, circularityLock, this.poolStrategy, this.typeStrategy, this.locationStrategy, this.nativeMethodStrategy, this.transformerDecorator, this.initializationStrategy, this.redefinitionStrategy, this.redefinitionDiscoveryStrategy, this.redefinitionBatchAllocator, this.redefinitionListener, this.redefinitionResubmissionStrategy, this.injectionStrategy, this.lambdaInstrumentationStrategy, this.descriptionStrategy, this.fallbackStrategy, this.classFileBufferStrategy, this.installationListener, this.ignoreMatcher, this.transformations);
        }

        @Override // net.bytebuddy.agent.builder.AgentBuilder
        public AgentBuilder with(TypeStrategy typeStrategy) {
            return new Default(this.byteBuddy, this.listener, this.circularityLock, this.poolStrategy, typeStrategy, this.locationStrategy, this.nativeMethodStrategy, this.transformerDecorator, this.initializationStrategy, this.redefinitionStrategy, this.redefinitionDiscoveryStrategy, this.redefinitionBatchAllocator, this.redefinitionListener, this.redefinitionResubmissionStrategy, this.injectionStrategy, this.lambdaInstrumentationStrategy, this.descriptionStrategy, this.fallbackStrategy, this.classFileBufferStrategy, this.installationListener, this.ignoreMatcher, this.transformations);
        }

        @Override // net.bytebuddy.agent.builder.AgentBuilder
        public AgentBuilder with(PoolStrategy poolStrategy) {
            return new Default(this.byteBuddy, this.listener, this.circularityLock, poolStrategy, this.typeStrategy, this.locationStrategy, this.nativeMethodStrategy, this.transformerDecorator, this.initializationStrategy, this.redefinitionStrategy, this.redefinitionDiscoveryStrategy, this.redefinitionBatchAllocator, this.redefinitionListener, this.redefinitionResubmissionStrategy, this.injectionStrategy, this.lambdaInstrumentationStrategy, this.descriptionStrategy, this.fallbackStrategy, this.classFileBufferStrategy, this.installationListener, this.ignoreMatcher, this.transformations);
        }

        @Override // net.bytebuddy.agent.builder.AgentBuilder
        public AgentBuilder with(LocationStrategy locationStrategy) {
            return new Default(this.byteBuddy, this.listener, this.circularityLock, this.poolStrategy, this.typeStrategy, locationStrategy, this.nativeMethodStrategy, this.transformerDecorator, this.initializationStrategy, this.redefinitionStrategy, this.redefinitionDiscoveryStrategy, this.redefinitionBatchAllocator, this.redefinitionListener, this.redefinitionResubmissionStrategy, this.injectionStrategy, this.lambdaInstrumentationStrategy, this.descriptionStrategy, this.fallbackStrategy, this.classFileBufferStrategy, this.installationListener, this.ignoreMatcher, this.transformations);
        }

        @Override // net.bytebuddy.agent.builder.AgentBuilder
        public AgentBuilder enableNativeMethodPrefix(String prefix) {
            return new Default(this.byteBuddy, this.listener, this.circularityLock, this.poolStrategy, this.typeStrategy, this.locationStrategy, NativeMethodStrategy.ForPrefix.of(prefix), this.transformerDecorator, this.initializationStrategy, this.redefinitionStrategy, this.redefinitionDiscoveryStrategy, this.redefinitionBatchAllocator, this.redefinitionListener, this.redefinitionResubmissionStrategy, this.injectionStrategy, this.lambdaInstrumentationStrategy, this.descriptionStrategy, this.fallbackStrategy, this.classFileBufferStrategy, this.installationListener, this.ignoreMatcher, this.transformations);
        }

        @Override // net.bytebuddy.agent.builder.AgentBuilder
        public AgentBuilder disableNativeMethodPrefix() {
            return new Default(this.byteBuddy, this.listener, this.circularityLock, this.poolStrategy, this.typeStrategy, this.locationStrategy, NativeMethodStrategy.Disabled.INSTANCE, this.transformerDecorator, this.initializationStrategy, this.redefinitionStrategy, this.redefinitionDiscoveryStrategy, this.redefinitionBatchAllocator, this.redefinitionListener, this.redefinitionResubmissionStrategy, this.injectionStrategy, this.lambdaInstrumentationStrategy, this.descriptionStrategy, this.fallbackStrategy, this.classFileBufferStrategy, this.installationListener, this.ignoreMatcher, this.transformations);
        }

        @Override // net.bytebuddy.agent.builder.AgentBuilder
        public AgentBuilder with(TransformerDecorator transformerDecorator) {
            return new Default(this.byteBuddy, this.listener, this.circularityLock, this.poolStrategy, this.typeStrategy, this.locationStrategy, this.nativeMethodStrategy, new TransformerDecorator.Compound(this.transformerDecorator, transformerDecorator), this.initializationStrategy, this.redefinitionStrategy, this.redefinitionDiscoveryStrategy, this.redefinitionBatchAllocator, this.redefinitionListener, this.redefinitionResubmissionStrategy, this.injectionStrategy, this.lambdaInstrumentationStrategy, this.descriptionStrategy, this.fallbackStrategy, this.classFileBufferStrategy, this.installationListener, this.ignoreMatcher, this.transformations);
        }

        @Override // net.bytebuddy.agent.builder.AgentBuilder
        public RedefinitionListenable.WithoutBatchStrategy with(RedefinitionStrategy redefinitionStrategy) {
            return new Redefining(this.byteBuddy, this.listener, this.circularityLock, this.poolStrategy, this.typeStrategy, this.locationStrategy, this.nativeMethodStrategy, this.transformerDecorator, this.initializationStrategy, redefinitionStrategy, RedefinitionStrategy.DiscoveryStrategy.SinglePass.INSTANCE, RedefinitionStrategy.BatchAllocator.ForTotal.INSTANCE, RedefinitionStrategy.Listener.NoOp.INSTANCE, RedefinitionStrategy.ResubmissionStrategy.Disabled.INSTANCE, this.injectionStrategy, this.lambdaInstrumentationStrategy, this.descriptionStrategy, this.fallbackStrategy, this.classFileBufferStrategy, this.installationListener, this.ignoreMatcher, this.transformations);
        }

        @Override // net.bytebuddy.agent.builder.AgentBuilder
        public AgentBuilder with(InitializationStrategy initializationStrategy) {
            return new Default(this.byteBuddy, this.listener, this.circularityLock, this.poolStrategy, this.typeStrategy, this.locationStrategy, this.nativeMethodStrategy, this.transformerDecorator, initializationStrategy, this.redefinitionStrategy, this.redefinitionDiscoveryStrategy, this.redefinitionBatchAllocator, this.redefinitionListener, this.redefinitionResubmissionStrategy, this.injectionStrategy, this.lambdaInstrumentationStrategy, this.descriptionStrategy, this.fallbackStrategy, this.classFileBufferStrategy, this.installationListener, this.ignoreMatcher, this.transformations);
        }

        @Override // net.bytebuddy.agent.builder.AgentBuilder
        public AgentBuilder with(LambdaInstrumentationStrategy lambdaInstrumentationStrategy) {
            return new Default(this.byteBuddy, this.listener, this.circularityLock, this.poolStrategy, this.typeStrategy, this.locationStrategy, this.nativeMethodStrategy, this.transformerDecorator, this.initializationStrategy, this.redefinitionStrategy, this.redefinitionDiscoveryStrategy, this.redefinitionBatchAllocator, this.redefinitionListener, this.redefinitionResubmissionStrategy, this.injectionStrategy, lambdaInstrumentationStrategy, this.descriptionStrategy, this.fallbackStrategy, this.classFileBufferStrategy, this.installationListener, this.ignoreMatcher, this.transformations);
        }

        @Override // net.bytebuddy.agent.builder.AgentBuilder
        public AgentBuilder with(DescriptionStrategy descriptionStrategy) {
            return new Default(this.byteBuddy, this.listener, this.circularityLock, this.poolStrategy, this.typeStrategy, this.locationStrategy, this.nativeMethodStrategy, this.transformerDecorator, this.initializationStrategy, this.redefinitionStrategy, this.redefinitionDiscoveryStrategy, this.redefinitionBatchAllocator, this.redefinitionListener, this.redefinitionResubmissionStrategy, this.injectionStrategy, this.lambdaInstrumentationStrategy, descriptionStrategy, this.fallbackStrategy, this.classFileBufferStrategy, this.installationListener, this.ignoreMatcher, this.transformations);
        }

        @Override // net.bytebuddy.agent.builder.AgentBuilder
        public AgentBuilder with(FallbackStrategy fallbackStrategy) {
            return new Default(this.byteBuddy, this.listener, this.circularityLock, this.poolStrategy, this.typeStrategy, this.locationStrategy, this.nativeMethodStrategy, this.transformerDecorator, this.initializationStrategy, this.redefinitionStrategy, this.redefinitionDiscoveryStrategy, this.redefinitionBatchAllocator, this.redefinitionListener, this.redefinitionResubmissionStrategy, this.injectionStrategy, this.lambdaInstrumentationStrategy, this.descriptionStrategy, fallbackStrategy, this.classFileBufferStrategy, this.installationListener, this.ignoreMatcher, this.transformations);
        }

        @Override // net.bytebuddy.agent.builder.AgentBuilder
        public AgentBuilder with(ClassFileBufferStrategy classFileBufferStrategy) {
            return new Default(this.byteBuddy, this.listener, this.circularityLock, this.poolStrategy, this.typeStrategy, this.locationStrategy, this.nativeMethodStrategy, this.transformerDecorator, this.initializationStrategy, this.redefinitionStrategy, this.redefinitionDiscoveryStrategy, this.redefinitionBatchAllocator, this.redefinitionListener, this.redefinitionResubmissionStrategy, this.injectionStrategy, this.lambdaInstrumentationStrategy, this.descriptionStrategy, this.fallbackStrategy, classFileBufferStrategy, this.installationListener, this.ignoreMatcher, this.transformations);
        }

        @Override // net.bytebuddy.agent.builder.AgentBuilder
        public AgentBuilder with(InstallationListener installationListener) {
            return new Default(this.byteBuddy, this.listener, this.circularityLock, this.poolStrategy, this.typeStrategy, this.locationStrategy, this.nativeMethodStrategy, this.transformerDecorator, this.initializationStrategy, this.redefinitionStrategy, this.redefinitionDiscoveryStrategy, this.redefinitionBatchAllocator, this.redefinitionListener, this.redefinitionResubmissionStrategy, this.injectionStrategy, this.lambdaInstrumentationStrategy, this.descriptionStrategy, this.fallbackStrategy, this.classFileBufferStrategy, new InstallationListener.Compound(this.installationListener, installationListener), this.ignoreMatcher, this.transformations);
        }

        @Override // net.bytebuddy.agent.builder.AgentBuilder
        public AgentBuilder with(InjectionStrategy injectionStrategy) {
            return new Default(this.byteBuddy, this.listener, this.circularityLock, this.poolStrategy, this.typeStrategy, this.locationStrategy, this.nativeMethodStrategy, this.transformerDecorator, this.initializationStrategy, this.redefinitionStrategy, this.redefinitionDiscoveryStrategy, this.redefinitionBatchAllocator, this.redefinitionListener, this.redefinitionResubmissionStrategy, injectionStrategy, this.lambdaInstrumentationStrategy, this.descriptionStrategy, this.fallbackStrategy, this.classFileBufferStrategy, this.installationListener, this.ignoreMatcher, this.transformations);
        }

        @Override // net.bytebuddy.agent.builder.AgentBuilder
        public AgentBuilder disableClassFormatChanges() {
            return new Default(this.byteBuddy.with(Implementation.Context.Disabled.Factory.INSTANCE), this.listener, this.circularityLock, this.poolStrategy, this.typeStrategy == TypeStrategy.Default.DECORATE ? TypeStrategy.Default.DECORATE : TypeStrategy.Default.REDEFINE_FROZEN, this.locationStrategy, NativeMethodStrategy.Disabled.INSTANCE, this.transformerDecorator, InitializationStrategy.NoOp.INSTANCE, this.redefinitionStrategy, this.redefinitionDiscoveryStrategy, this.redefinitionBatchAllocator, this.redefinitionListener, this.redefinitionResubmissionStrategy, this.injectionStrategy, this.lambdaInstrumentationStrategy, this.descriptionStrategy, this.fallbackStrategy, this.classFileBufferStrategy, this.installationListener, this.ignoreMatcher, this.transformations);
        }

        @Override // net.bytebuddy.agent.builder.AgentBuilder
        public AgentBuilder assureReadEdgeTo(Instrumentation instrumentation, Class<?>... type) {
            return JavaModule.isSupported() ? with(Listener.ModuleReadEdgeCompleting.of(instrumentation, false, type)) : this;
        }

        @Override // net.bytebuddy.agent.builder.AgentBuilder
        public AgentBuilder assureReadEdgeTo(Instrumentation instrumentation, JavaModule... module) {
            return assureReadEdgeTo(instrumentation, Arrays.asList(module));
        }

        @Override // net.bytebuddy.agent.builder.AgentBuilder
        public AgentBuilder assureReadEdgeTo(Instrumentation instrumentation, Collection<? extends JavaModule> modules) {
            return with(new Listener.ModuleReadEdgeCompleting(instrumentation, false, new HashSet(modules)));
        }

        @Override // net.bytebuddy.agent.builder.AgentBuilder
        public AgentBuilder assureReadEdgeFromAndTo(Instrumentation instrumentation, Class<?>... type) {
            return JavaModule.isSupported() ? with(Listener.ModuleReadEdgeCompleting.of(instrumentation, true, type)) : this;
        }

        @Override // net.bytebuddy.agent.builder.AgentBuilder
        public AgentBuilder assureReadEdgeFromAndTo(Instrumentation instrumentation, JavaModule... module) {
            return assureReadEdgeFromAndTo(instrumentation, Arrays.asList(module));
        }

        @Override // net.bytebuddy.agent.builder.AgentBuilder
        public AgentBuilder assureReadEdgeFromAndTo(Instrumentation instrumentation, Collection<? extends JavaModule> modules) {
            return with(new Listener.ModuleReadEdgeCompleting(instrumentation, true, new HashSet(modules)));
        }

        @Override // net.bytebuddy.agent.builder.AgentBuilder
        public Identified.Narrowable type(RawMatcher matcher) {
            return new Transforming(matcher, Collections.emptyList(), false);
        }

        @Override // net.bytebuddy.agent.builder.AgentBuilder
        public Identified.Narrowable type(ElementMatcher<? super TypeDescription> typeMatcher) {
            return type(typeMatcher, ElementMatchers.any());
        }

        @Override // net.bytebuddy.agent.builder.AgentBuilder
        public Identified.Narrowable type(ElementMatcher<? super TypeDescription> typeMatcher, ElementMatcher<? super ClassLoader> classLoaderMatcher) {
            return type(typeMatcher, classLoaderMatcher, ElementMatchers.any());
        }

        @Override // net.bytebuddy.agent.builder.AgentBuilder
        public Identified.Narrowable type(ElementMatcher<? super TypeDescription> typeMatcher, ElementMatcher<? super ClassLoader> classLoaderMatcher, ElementMatcher<? super JavaModule> moduleMatcher) {
            return type(new RawMatcher.ForElementMatchers(typeMatcher, classLoaderMatcher, ElementMatchers.not(ElementMatchers.supportsModules()).or(moduleMatcher)));
        }

        @Override // net.bytebuddy.agent.builder.AgentBuilder
        public Ignored ignore(ElementMatcher<? super TypeDescription> typeMatcher) {
            return ignore(typeMatcher, ElementMatchers.any());
        }

        @Override // net.bytebuddy.agent.builder.AgentBuilder
        public Ignored ignore(ElementMatcher<? super TypeDescription> typeMatcher, ElementMatcher<? super ClassLoader> classLoaderMatcher) {
            return ignore(typeMatcher, classLoaderMatcher, ElementMatchers.any());
        }

        @Override // net.bytebuddy.agent.builder.AgentBuilder
        public Ignored ignore(ElementMatcher<? super TypeDescription> typeMatcher, ElementMatcher<? super ClassLoader> classLoaderMatcher, ElementMatcher<? super JavaModule> moduleMatcher) {
            return ignore(new RawMatcher.ForElementMatchers(typeMatcher, classLoaderMatcher, ElementMatchers.not(ElementMatchers.supportsModules()).or(moduleMatcher)));
        }

        @Override // net.bytebuddy.agent.builder.AgentBuilder
        public Ignored ignore(RawMatcher rawMatcher) {
            return new Ignoring(rawMatcher);
        }

        @Override // net.bytebuddy.agent.builder.AgentBuilder
        public ResettableClassFileTransformer makeRaw() {
            return makeRaw(this.listener, InstallationListener.NoOp.INSTANCE);
        }

        private ResettableClassFileTransformer makeRaw(Listener listener, InstallationListener installationListener) {
            return ExecutingTransformer.FACTORY.make(this.byteBuddy, listener, this.poolStrategy, this.typeStrategy, this.locationStrategy, this.nativeMethodStrategy, this.initializationStrategy, this.injectionStrategy, this.lambdaInstrumentationStrategy, this.descriptionStrategy, this.fallbackStrategy, this.classFileBufferStrategy, installationListener, this.ignoreMatcher, this.transformations, this.circularityLock);
        }

        @Override // net.bytebuddy.agent.builder.AgentBuilder
        public ResettableClassFileTransformer installOn(Instrumentation instrumentation) {
            if (!this.circularityLock.acquire()) {
                throw new IllegalStateException("Could not acquire the circularity lock upon installation.");
            }
            try {
                return doInstall(instrumentation, new Transformation.SimpleMatcher(this.ignoreMatcher, this.transformations));
            } finally {
                this.circularityLock.release();
            }
        }

        @Override // net.bytebuddy.agent.builder.AgentBuilder
        public ResettableClassFileTransformer installOnByteBuddyAgent() {
            try {
                return installOn((Instrumentation) ClassLoader.getSystemClassLoader().loadClass(INSTALLER_TYPE).getMethod(INSTRUMENTATION_GETTER, new Class[0]).invoke(STATIC_MEMBER, new Object[0]));
            } catch (RuntimeException exception) {
                throw exception;
            } catch (Exception exception2) {
                throw new IllegalStateException("The Byte Buddy agent is not installed or not accessible", exception2);
            }
        }

        @Override // net.bytebuddy.agent.builder.AgentBuilder
        public ResettableClassFileTransformer patchOn(Instrumentation instrumentation, ResettableClassFileTransformer classFileTransformer) {
            if (!this.circularityLock.acquire()) {
                throw new IllegalStateException("Could not acquire the circularity lock upon installation.");
            }
            try {
                if (!classFileTransformer.reset(instrumentation, RedefinitionStrategy.DISABLED)) {
                    throw new IllegalArgumentException("Cannot patch unregistered class file transformer: " + classFileTransformer);
                }
                ResettableClassFileTransformer doInstall = doInstall(instrumentation, new Transformation.DifferentialMatcher(this.ignoreMatcher, this.transformations, classFileTransformer));
                this.circularityLock.release();
                return doInstall;
            } catch (Throwable th) {
                this.circularityLock.release();
                throw th;
            }
        }

        @Override // net.bytebuddy.agent.builder.AgentBuilder
        public ResettableClassFileTransformer patchOnByteBuddyAgent(ResettableClassFileTransformer classFileTransformer) {
            try {
                return patchOn((Instrumentation) ClassLoader.getSystemClassLoader().loadClass(INSTALLER_TYPE).getMethod(INSTRUMENTATION_GETTER, new Class[0]).invoke(STATIC_MEMBER, new Object[0]), classFileTransformer);
            } catch (RuntimeException exception) {
                throw exception;
            } catch (Exception exception2) {
                throw new IllegalStateException("The Byte Buddy agent is not installed or not accessible", exception2);
            }
        }

        private ResettableClassFileTransformer doInstall(Instrumentation instrumentation, RawMatcher matcher) {
            Throwable throwable;
            IllegalStateException illegalStateException;
            RedefinitionStrategy.ResubmissionStrategy.Installation installation = this.redefinitionResubmissionStrategy.apply(instrumentation, this.locationStrategy, this.listener, this.installationListener, this.circularityLock, new Transformation.SimpleMatcher(this.ignoreMatcher, this.transformations), this.redefinitionStrategy, this.redefinitionBatchAllocator, this.redefinitionListener);
            ResettableClassFileTransformer classFileTransformer = this.transformerDecorator.decorate(makeRaw(installation.getListener(), installation.getInstallationListener()));
            installation.getInstallationListener().onBeforeInstall(instrumentation, classFileTransformer);
            try {
                DISPATCHER.addTransformer(instrumentation, classFileTransformer, this.redefinitionStrategy.isRetransforming());
                this.nativeMethodStrategy.apply(instrumentation, classFileTransformer);
                this.lambdaInstrumentationStrategy.apply(this.byteBuddy, instrumentation, classFileTransformer);
                this.redefinitionStrategy.apply(instrumentation, installation.getListener(), this.circularityLock, this.poolStrategy, this.locationStrategy, this.redefinitionDiscoveryStrategy, this.redefinitionBatchAllocator, this.redefinitionListener, this.lambdaInstrumentationStrategy, this.descriptionStrategy, this.fallbackStrategy, matcher);
            } finally {
                if (throwable != null) {
                }
                installation.getInstallationListener().onInstall(instrumentation, classFileTransformer);
                return classFileTransformer;
            }
            installation.getInstallationListener().onInstall(instrumentation, classFileTransformer);
            return classFileTransformer;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Default$Dispatcher.class */
        public interface Dispatcher {
            boolean isNativeMethodPrefixSupported(Instrumentation instrumentation);

            void setNativeMethodPrefix(Instrumentation instrumentation, ClassFileTransformer classFileTransformer, String str);

            void addTransformer(Instrumentation instrumentation, ClassFileTransformer classFileTransformer, boolean z);

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Default$Dispatcher$CreationAction.class */
            public enum CreationAction implements PrivilegedAction<Dispatcher> {
                INSTANCE;

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public Dispatcher run() {
                    try {
                        return new ForJava6CapableVm(Instrumentation.class.getMethod("isNativeMethodPrefixSupported", new Class[0]), Instrumentation.class.getMethod("setNativeMethodPrefix", ClassFileTransformer.class, String.class), Instrumentation.class.getMethod("addTransformer", ClassFileTransformer.class, Boolean.TYPE));
                    } catch (NoSuchMethodException e) {
                        return ForLegacyVm.INSTANCE;
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Default$Dispatcher$ForLegacyVm.class */
            public enum ForLegacyVm implements Dispatcher {
                INSTANCE;

                @Override // net.bytebuddy.agent.builder.AgentBuilder.Default.Dispatcher
                public boolean isNativeMethodPrefixSupported(Instrumentation instrumentation) {
                    return false;
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.Default.Dispatcher
                public void setNativeMethodPrefix(Instrumentation instrumentation, ClassFileTransformer classFileTransformer, String prefix) {
                    throw new UnsupportedOperationException("The current VM does not support native method prefixes");
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.Default.Dispatcher
                public void addTransformer(Instrumentation instrumentation, ClassFileTransformer classFileTransformer, boolean canRetransform) {
                    if (canRetransform) {
                        throw new UnsupportedOperationException("The current VM does not support retransformation");
                    }
                    instrumentation.addTransformer(classFileTransformer);
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Default$Dispatcher$ForJava6CapableVm.class */
            public static class ForJava6CapableVm implements Dispatcher {
                private final Method isNativeMethodPrefixSupported;
                private final Method setNativeMethodPrefix;
                private final Method addTransformer;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.isNativeMethodPrefixSupported.equals(((ForJava6CapableVm) obj).isNativeMethodPrefixSupported) && this.setNativeMethodPrefix.equals(((ForJava6CapableVm) obj).setNativeMethodPrefix) && this.addTransformer.equals(((ForJava6CapableVm) obj).addTransformer);
                }

                public int hashCode() {
                    return (((((17 * 31) + this.isNativeMethodPrefixSupported.hashCode()) * 31) + this.setNativeMethodPrefix.hashCode()) * 31) + this.addTransformer.hashCode();
                }

                protected ForJava6CapableVm(Method isNativeMethodPrefixSupported, Method setNativeMethodPrefix, Method addTransformer) {
                    this.isNativeMethodPrefixSupported = isNativeMethodPrefixSupported;
                    this.setNativeMethodPrefix = setNativeMethodPrefix;
                    this.addTransformer = addTransformer;
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.Default.Dispatcher
                public boolean isNativeMethodPrefixSupported(Instrumentation instrumentation) {
                    try {
                        return ((Boolean) this.isNativeMethodPrefixSupported.invoke(instrumentation, new Object[0])).booleanValue();
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.instrument.Instrumentation#isNativeMethodPrefixSupported", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.instrument.Instrumentation#isNativeMethodPrefixSupported", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.Default.Dispatcher
                public void setNativeMethodPrefix(Instrumentation instrumentation, ClassFileTransformer classFileTransformer, String prefix) {
                    try {
                        this.setNativeMethodPrefix.invoke(instrumentation, classFileTransformer, prefix);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.instrument.Instrumentation#setNativeMethodPrefix", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.instrument.Instrumentation#setNativeMethodPrefix", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.Default.Dispatcher
                public void addTransformer(Instrumentation instrumentation, ClassFileTransformer classFileTransformer, boolean canRetransform) {
                    try {
                        this.addTransformer.invoke(instrumentation, classFileTransformer, Boolean.valueOf(canRetransform));
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.instrument.Instrumentation#addTransformer", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.instrument.Instrumentation#addTransformer", exception2.getCause());
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Default$NativeMethodStrategy.class */
        public interface NativeMethodStrategy {
            MethodNameTransformer resolve();

            void apply(Instrumentation instrumentation, ClassFileTransformer classFileTransformer);

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Default$NativeMethodStrategy$Disabled.class */
            public enum Disabled implements NativeMethodStrategy {
                INSTANCE;

                @Override // net.bytebuddy.agent.builder.AgentBuilder.Default.NativeMethodStrategy
                public MethodNameTransformer resolve() {
                    return MethodNameTransformer.Suffixing.withRandomSuffix();
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.Default.NativeMethodStrategy
                public void apply(Instrumentation instrumentation, ClassFileTransformer classFileTransformer) {
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Default$NativeMethodStrategy$ForPrefix.class */
            public static class ForPrefix implements NativeMethodStrategy {
                private final String prefix;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.prefix.equals(((ForPrefix) obj).prefix);
                }

                public int hashCode() {
                    return (17 * 31) + this.prefix.hashCode();
                }

                protected ForPrefix(String prefix) {
                    this.prefix = prefix;
                }

                protected static NativeMethodStrategy of(String prefix) {
                    if (prefix.length() == 0) {
                        throw new IllegalArgumentException("A method name prefix must not be the empty string");
                    }
                    return new ForPrefix(prefix);
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.Default.NativeMethodStrategy
                public MethodNameTransformer resolve() {
                    return new MethodNameTransformer.Prefixing(this.prefix);
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.Default.NativeMethodStrategy
                public void apply(Instrumentation instrumentation, ClassFileTransformer classFileTransformer) {
                    if (Default.DISPATCHER.isNativeMethodPrefixSupported(instrumentation)) {
                        Default.DISPATCHER.setNativeMethodPrefix(instrumentation, classFileTransformer, this.prefix);
                        return;
                    }
                    throw new IllegalArgumentException("A prefix for native methods is not supported: " + instrumentation);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Default$Transformation.class */
        public static class Transformation {
            private static final byte[] NONE = null;
            private final RawMatcher matcher;
            private final List<Transformer> transformers;
            private final boolean terminal;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.terminal == ((Transformation) obj).terminal && this.matcher.equals(((Transformation) obj).matcher) && this.transformers.equals(((Transformation) obj).transformers);
            }

            public int hashCode() {
                return (((((17 * 31) + this.matcher.hashCode()) * 31) + this.transformers.hashCode()) * 31) + (this.terminal ? 1 : 0);
            }

            protected Transformation(RawMatcher matcher, List<Transformer> transformers, boolean terminal) {
                this.matcher = matcher;
                this.transformers = transformers;
                this.terminal = terminal;
            }

            protected RawMatcher getMatcher() {
                return this.matcher;
            }

            protected List<Transformer> getTransformers() {
                return this.transformers;
            }

            protected boolean isTerminal() {
                return this.terminal;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Default$Transformation$SimpleMatcher.class */
            public static class SimpleMatcher implements RawMatcher {
                private final RawMatcher ignoreMatcher;
                private final List<Transformation> transformations;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.ignoreMatcher.equals(((SimpleMatcher) obj).ignoreMatcher) && this.transformations.equals(((SimpleMatcher) obj).transformations);
                }

                public int hashCode() {
                    return (((17 * 31) + this.ignoreMatcher.hashCode()) * 31) + this.transformations.hashCode();
                }

                protected SimpleMatcher(RawMatcher ignoreMatcher, List<Transformation> transformations) {
                    this.ignoreMatcher = ignoreMatcher;
                    this.transformations = transformations;
                }

                @Override // net.bytebuddy.agent.builder.AgentBuilder.RawMatcher
                public boolean matches(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, Class<?> classBeingRedefined, ProtectionDomain protectionDomain) {
                    if (this.ignoreMatcher.matches(typeDescription, classLoader, module, classBeingRedefined, protectionDomain)) {
                        return false;
                    }
                    for (Transformation transformation : this.transformations) {
                        if (transformation.getMatcher().matches(typeDescription, classLoader, module, classBeingRedefined, protectionDomain)) {
                            return true;
                        }
                    }
                    return false;
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Default$Transformation$DifferentialMatcher.class */
            public static class DifferentialMatcher implements RawMatcher {
                private final RawMatcher ignoreMatcher;
                private final List<Transformation> transformations;
                private final ResettableClassFileTransformer classFileTransformer;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.ignoreMatcher.equals(((DifferentialMatcher) obj).ignoreMatcher) && this.transformations.equals(((DifferentialMatcher) obj).transformations) && this.classFileTransformer.equals(((DifferentialMatcher) obj).classFileTransformer);
                }

                public int hashCode() {
                    return (((((17 * 31) + this.ignoreMatcher.hashCode()) * 31) + this.transformations.hashCode()) * 31) + this.classFileTransformer.hashCode();
                }

                protected DifferentialMatcher(RawMatcher ignoreMatcher, List<Transformation> transformations, ResettableClassFileTransformer classFileTransformer) {
                    this.ignoreMatcher = ignoreMatcher;
                    this.transformations = transformations;
                    this.classFileTransformer = classFileTransformer;
                }

                /* JADX WARN: Removed duplicated region for block: B:14:0x0078  */
                @Override // net.bytebuddy.agent.builder.AgentBuilder.RawMatcher
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct add '--show-bad-code' argument
                */
                public boolean matches(net.bytebuddy.description.type.TypeDescription r8, java.lang.ClassLoader r9, net.bytebuddy.utility.JavaModule r10, java.lang.Class<?> r11, java.security.ProtectionDomain r12) {
                    /*
                        r7 = this;
                        r0 = r7
                        net.bytebuddy.agent.builder.ResettableClassFileTransformer r0 = r0.classFileTransformer
                        r1 = r8
                        r2 = r9
                        r3 = r10
                        r4 = r11
                        r5 = r12
                        java.util.Iterator r0 = r0.iterator(r1, r2, r3, r4, r5)
                        r13 = r0
                        r0 = r7
                        net.bytebuddy.agent.builder.AgentBuilder$RawMatcher r0 = r0.ignoreMatcher
                        r1 = r8
                        r2 = r9
                        r3 = r10
                        r4 = r11
                        r5 = r12
                        boolean r0 = r0.matches(r1, r2, r3, r4, r5)
                        if (r0 == 0) goto L2d
                        r0 = r13
                        boolean r0 = r0.hasNext()
                        return r0
                    L2d:
                        r0 = r7
                        java.util.List<net.bytebuddy.agent.builder.AgentBuilder$Default$Transformation> r0 = r0.transformations
                        java.util.Iterator r0 = r0.iterator()
                        r14 = r0
                    L38:
                        r0 = r14
                        boolean r0 = r0.hasNext()
                        if (r0 == 0) goto La8
                        r0 = r14
                        java.lang.Object r0 = r0.next()
                        net.bytebuddy.agent.builder.AgentBuilder$Default$Transformation r0 = (net.bytebuddy.agent.builder.AgentBuilder.Default.Transformation) r0
                        r15 = r0
                        r0 = r15
                        net.bytebuddy.agent.builder.AgentBuilder$RawMatcher r0 = r0.getMatcher()
                        r1 = r8
                        r2 = r9
                        r3 = r10
                        r4 = r11
                        r5 = r12
                        boolean r0 = r0.matches(r1, r2, r3, r4, r5)
                        if (r0 == 0) goto La5
                        r0 = r15
                        java.util.List r0 = r0.getTransformers()
                        java.util.Iterator r0 = r0.iterator()
                        r16 = r0
                    L6e:
                        r0 = r16
                        boolean r0 = r0.hasNext()
                        if (r0 == 0) goto La5
                        r0 = r16
                        java.lang.Object r0 = r0.next()
                        net.bytebuddy.agent.builder.AgentBuilder$Transformer r0 = (net.bytebuddy.agent.builder.AgentBuilder.Transformer) r0
                        r17 = r0
                        r0 = r13
                        boolean r0 = r0.hasNext()
                        if (r0 == 0) goto La0
                        r0 = r13
                        java.lang.Object r0 = r0.next()
                        net.bytebuddy.agent.builder.AgentBuilder$Transformer r0 = (net.bytebuddy.agent.builder.AgentBuilder.Transformer) r0
                        r1 = r17
                        boolean r0 = r0.equals(r1)
                        if (r0 != 0) goto La2
                    La0:
                        r0 = 1
                        return r0
                    La2:
                        goto L6e
                    La5:
                        goto L38
                    La8:
                        r0 = r13
                        boolean r0 = r0.hasNext()
                        return r0
                    */
                    throw new UnsupportedOperationException("Method not decompiled: net.bytebuddy.agent.builder.AgentBuilder.Default.Transformation.DifferentialMatcher.matches(net.bytebuddy.description.type.TypeDescription, java.lang.ClassLoader, net.bytebuddy.utility.JavaModule, java.lang.Class, java.security.ProtectionDomain):boolean");
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Default$Transformation$TransformerIterator.class */
            protected static class TransformerIterator implements Iterator<Transformer> {
                private final TypeDescription typeDescription;
                private final ClassLoader classLoader;
                private final JavaModule module;
                private final Class<?> classBeingRedefined;
                private final ProtectionDomain protectionDomain;
                private final Iterator<Transformation> transformations;
                private Iterator<Transformer> transformers;

                protected TransformerIterator(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, List<Transformation> transformations) {
                    this.typeDescription = typeDescription;
                    this.classLoader = classLoader;
                    this.module = module;
                    this.classBeingRedefined = classBeingRedefined;
                    this.protectionDomain = protectionDomain;
                    this.transformations = transformations.iterator();
                    this.transformers = Collections.emptySet().iterator();
                    while (!this.transformers.hasNext() && this.transformations.hasNext()) {
                        Transformation transformation = this.transformations.next();
                        if (transformation.getMatcher().matches(typeDescription, classLoader, module, classBeingRedefined, protectionDomain)) {
                            this.transformers = transformation.getTransformers().iterator();
                        }
                    }
                }

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return this.transformers.hasNext();
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.Iterator
                public Transformer next() {
                    boolean hasNext;
                    boolean hasNext2;
                    try {
                        while (true) {
                            if (!hasNext) {
                                if (!hasNext2) {
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                        return this.transformers.next();
                    } finally {
                        while (!this.transformers.hasNext() && this.transformations.hasNext()) {
                            Transformation transformation = this.transformations.next();
                            if (transformation.getMatcher().matches(this.typeDescription, this.classLoader, this.module, this.classBeingRedefined, this.protectionDomain)) {
                                this.transformers = transformation.getTransformers().iterator();
                            }
                        }
                    }
                }

                @Override // java.util.Iterator
                public void remove() {
                    throw new UnsupportedOperationException("remove");
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Default$ExecutingTransformer.class */
        public static class ExecutingTransformer extends ResettableClassFileTransformer.AbstractBase {
            protected static final Factory FACTORY = (Factory) AccessController.doPrivileged(Factory.CreationAction.INSTANCE);
            private final ByteBuddy byteBuddy;
            private final PoolStrategy poolStrategy;
            private final TypeStrategy typeStrategy;
            private final Listener listener;
            private final NativeMethodStrategy nativeMethodStrategy;
            private final InitializationStrategy initializationStrategy;
            private final InjectionStrategy injectionStrategy;
            private final LambdaInstrumentationStrategy lambdaInstrumentationStrategy;
            private final DescriptionStrategy descriptionStrategy;
            private final LocationStrategy locationStrategy;
            private final FallbackStrategy fallbackStrategy;
            private final ClassFileBufferStrategy classFileBufferStrategy;
            private final InstallationListener installationListener;
            private final RawMatcher ignoreMatcher;
            private final List<Transformation> transformations;
            private final CircularityLock circularityLock;
            private final AccessControlContext accessControlContext = AccessController.getContext();

            public ExecutingTransformer(ByteBuddy byteBuddy, Listener listener, PoolStrategy poolStrategy, TypeStrategy typeStrategy, LocationStrategy locationStrategy, NativeMethodStrategy nativeMethodStrategy, InitializationStrategy initializationStrategy, InjectionStrategy injectionStrategy, LambdaInstrumentationStrategy lambdaInstrumentationStrategy, DescriptionStrategy descriptionStrategy, FallbackStrategy fallbackStrategy, ClassFileBufferStrategy classFileBufferStrategy, InstallationListener installationListener, RawMatcher ignoreMatcher, List<Transformation> transformations, CircularityLock circularityLock) {
                this.byteBuddy = byteBuddy;
                this.typeStrategy = typeStrategy;
                this.poolStrategy = poolStrategy;
                this.locationStrategy = locationStrategy;
                this.listener = listener;
                this.nativeMethodStrategy = nativeMethodStrategy;
                this.initializationStrategy = initializationStrategy;
                this.injectionStrategy = injectionStrategy;
                this.lambdaInstrumentationStrategy = lambdaInstrumentationStrategy;
                this.descriptionStrategy = descriptionStrategy;
                this.fallbackStrategy = fallbackStrategy;
                this.classFileBufferStrategy = classFileBufferStrategy;
                this.installationListener = installationListener;
                this.ignoreMatcher = ignoreMatcher;
                this.transformations = transformations;
                this.circularityLock = circularityLock;
            }

            public byte[] transform(ClassLoader classLoader, String internalTypeName, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] binaryRepresentation) {
                if (!this.circularityLock.acquire()) {
                    return Default.NO_TRANSFORMATION;
                }
                try {
                    byte[] bArr = (byte[]) AccessController.doPrivileged(new LegacyVmDispatcher(classLoader, internalTypeName, classBeingRedefined, protectionDomain, binaryRepresentation), this.accessControlContext);
                    this.circularityLock.release();
                    return bArr;
                } catch (Throwable th) {
                    this.circularityLock.release();
                    throw th;
                }
            }

            protected byte[] transform(Object rawModule, ClassLoader classLoader, String internalTypeName, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] binaryRepresentation) {
                if (!this.circularityLock.acquire()) {
                    return Default.NO_TRANSFORMATION;
                }
                try {
                    byte[] bArr = (byte[]) AccessController.doPrivileged(new Java9CapableVmDispatcher(rawModule, classLoader, internalTypeName, classBeingRedefined, protectionDomain, binaryRepresentation), this.accessControlContext);
                    this.circularityLock.release();
                    return bArr;
                } catch (Throwable th) {
                    this.circularityLock.release();
                    throw th;
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public byte[] transform(JavaModule module, ClassLoader classLoader, String internalTypeName, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] binaryRepresentation) {
                if (internalTypeName == null || !this.lambdaInstrumentationStrategy.isInstrumented(classBeingRedefined)) {
                    return Default.NO_TRANSFORMATION;
                }
                String typeName = internalTypeName.replace('/', '.');
                try {
                    this.listener.onDiscovery(typeName, classLoader, module, classBeingRedefined != null);
                    ClassFileLocator classFileLocator = new ClassFileLocator.Compound(this.classFileBufferStrategy.resolve(typeName, binaryRepresentation, classLoader, module, protectionDomain), this.locationStrategy.classFileLocator(classLoader, module));
                    TypePool typePool = this.poolStrategy.typePool(classFileLocator, classLoader);
                    byte[] doTransform = doTransform(module, classLoader, typeName, classBeingRedefined, classBeingRedefined != null, protectionDomain, typePool, classFileLocator);
                    this.listener.onComplete(typeName, classLoader, module, classBeingRedefined != null);
                    return doTransform;
                }
            }

            private byte[] doTransform(JavaModule module, ClassLoader classLoader, String typeName, Class<?> classBeingRedefined, boolean loaded, ProtectionDomain protectionDomain, TypePool typePool, ClassFileLocator classFileLocator) {
                TypeDescription typeDescription = this.descriptionStrategy.apply(typeName, classBeingRedefined, typePool, this.circularityLock, classLoader, module);
                List<Transformer> transformers = new ArrayList<>();
                if (!this.ignoreMatcher.matches(typeDescription, classLoader, module, classBeingRedefined, protectionDomain)) {
                    for (Transformation transformation : this.transformations) {
                        if (transformation.getMatcher().matches(typeDescription, classLoader, module, classBeingRedefined, protectionDomain)) {
                            transformers.addAll(transformation.getTransformers());
                            if (transformation.isTerminal()) {
                                break;
                            }
                        }
                    }
                }
                if (transformers.isEmpty()) {
                    this.listener.onIgnored(typeDescription, classLoader, module, loaded);
                    return Transformation.NONE;
                }
                DynamicType.Builder<?> builder = this.typeStrategy.builder(typeDescription, this.byteBuddy, classFileLocator, this.nativeMethodStrategy.resolve(), classLoader, module, protectionDomain);
                InitializationStrategy.Dispatcher dispatcher = this.initializationStrategy.dispatcher();
                for (Transformer transformer : transformers) {
                    builder = transformer.transform(builder, typeDescription, classLoader, module);
                }
                DynamicType.Unloaded<?> dynamicType = dispatcher.apply(builder).make(TypeResolutionStrategy.Disabled.INSTANCE, typePool);
                dispatcher.register(dynamicType, classLoader, protectionDomain, this.injectionStrategy);
                this.listener.onTransformation(typeDescription, classLoader, module, loaded, dynamicType);
                return dynamicType.getBytes();
            }

            @Override // net.bytebuddy.agent.builder.ResettableClassFileTransformer
            public Iterator<Transformer> iterator(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, Class<?> classBeingRedefined, ProtectionDomain protectionDomain) {
                return this.ignoreMatcher.matches(typeDescription, classLoader, module, classBeingRedefined, protectionDomain) ? Collections.emptySet().iterator() : new Transformation.TransformerIterator(typeDescription, classLoader, module, classBeingRedefined, protectionDomain, this.transformations);
            }

            @Override // net.bytebuddy.agent.builder.ResettableClassFileTransformer
            public synchronized boolean reset(Instrumentation instrumentation, ResettableClassFileTransformer classFileTransformer, RedefinitionStrategy redefinitionStrategy, RedefinitionStrategy.DiscoveryStrategy redefinitionDiscoveryStrategy, RedefinitionStrategy.BatchAllocator redefinitionBatchAllocator, RedefinitionStrategy.Listener redefinitionListener) {
                if (instrumentation.removeTransformer(classFileTransformer)) {
                    redefinitionStrategy.apply(instrumentation, Listener.NoOp.INSTANCE, CircularityLock.Inactive.INSTANCE, this.poolStrategy, this.locationStrategy, redefinitionDiscoveryStrategy, redefinitionBatchAllocator, redefinitionListener, this.lambdaInstrumentationStrategy, this.descriptionStrategy, this.fallbackStrategy, new Transformation.SimpleMatcher(this.ignoreMatcher, this.transformations));
                    this.installationListener.onReset(instrumentation, classFileTransformer);
                    return true;
                }
                return false;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Default$ExecutingTransformer$Factory.class */
            public interface Factory {
                ResettableClassFileTransformer make(ByteBuddy byteBuddy, Listener listener, PoolStrategy poolStrategy, TypeStrategy typeStrategy, LocationStrategy locationStrategy, NativeMethodStrategy nativeMethodStrategy, InitializationStrategy initializationStrategy, InjectionStrategy injectionStrategy, LambdaInstrumentationStrategy lambdaInstrumentationStrategy, DescriptionStrategy descriptionStrategy, FallbackStrategy fallbackStrategy, ClassFileBufferStrategy classFileBufferStrategy, InstallationListener installationListener, RawMatcher rawMatcher, List<Transformation> list, CircularityLock circularityLock);

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Default$ExecutingTransformer$Factory$CreationAction.class */
                public enum CreationAction implements PrivilegedAction<Factory> {
                    INSTANCE;

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    @SuppressFBWarnings(value = {"REC_CATCH_EXCEPTION"}, justification = "Exception should not be rethrown but trigger a fallback")
                    public Factory run() {
                        try {
                            return new ForJava9CapableVm(new ByteBuddy().with(TypeValidation.DISABLED).subclass(ExecutingTransformer.class).name(ExecutingTransformer.class.getName() + "$ByteBuddy$ModuleSupport").method(ElementMatchers.named("transform").and(ElementMatchers.takesArgument(0, JavaType.MODULE.load()))).intercept(MethodCall.invoke(ExecutingTransformer.class.getDeclaredMethod("transform", Object.class, ClassLoader.class, String.class, Class.class, ProtectionDomain.class, byte[].class)).onSuper().withAllArguments()).make().load(ExecutingTransformer.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER_PERSISTENT.with(ExecutingTransformer.class.getProtectionDomain())).getLoaded().getDeclaredConstructor(ByteBuddy.class, Listener.class, PoolStrategy.class, TypeStrategy.class, LocationStrategy.class, NativeMethodStrategy.class, InitializationStrategy.class, InjectionStrategy.class, LambdaInstrumentationStrategy.class, DescriptionStrategy.class, FallbackStrategy.class, ClassFileBufferStrategy.class, InstallationListener.class, RawMatcher.class, List.class, CircularityLock.class));
                        } catch (Exception e) {
                            return ForLegacyVm.INSTANCE;
                        }
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Default$ExecutingTransformer$Factory$ForJava9CapableVm.class */
                public static class ForJava9CapableVm implements Factory {
                    private final Constructor<? extends ResettableClassFileTransformer> executingTransformer;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.executingTransformer.equals(((ForJava9CapableVm) obj).executingTransformer);
                    }

                    public int hashCode() {
                        return (17 * 31) + this.executingTransformer.hashCode();
                    }

                    protected ForJava9CapableVm(Constructor<? extends ResettableClassFileTransformer> executingTransformer) {
                        this.executingTransformer = executingTransformer;
                    }

                    @Override // net.bytebuddy.agent.builder.AgentBuilder.Default.ExecutingTransformer.Factory
                    public ResettableClassFileTransformer make(ByteBuddy byteBuddy, Listener listener, PoolStrategy poolStrategy, TypeStrategy typeStrategy, LocationStrategy locationStrategy, NativeMethodStrategy nativeMethodStrategy, InitializationStrategy initializationStrategy, InjectionStrategy injectionStrategy, LambdaInstrumentationStrategy lambdaInstrumentationStrategy, DescriptionStrategy descriptionStrategy, FallbackStrategy fallbackStrategy, ClassFileBufferStrategy classFileBufferStrategy, InstallationListener installationListener, RawMatcher ignoreMatcher, List<Transformation> transformations, CircularityLock circularityLock) {
                        try {
                            return this.executingTransformer.newInstance(byteBuddy, listener, poolStrategy, typeStrategy, locationStrategy, nativeMethodStrategy, initializationStrategy, injectionStrategy, lambdaInstrumentationStrategy, descriptionStrategy, fallbackStrategy, classFileBufferStrategy, installationListener, ignoreMatcher, transformations, circularityLock);
                        } catch (IllegalAccessException exception) {
                            throw new IllegalStateException("Cannot access " + this.executingTransformer, exception);
                        } catch (InstantiationException exception2) {
                            throw new IllegalStateException("Cannot instantiate " + this.executingTransformer.getDeclaringClass(), exception2);
                        } catch (InvocationTargetException exception3) {
                            throw new IllegalStateException("Cannot invoke " + this.executingTransformer, exception3.getCause());
                        }
                    }
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Default$ExecutingTransformer$Factory$ForLegacyVm.class */
                public enum ForLegacyVm implements Factory {
                    INSTANCE;

                    @Override // net.bytebuddy.agent.builder.AgentBuilder.Default.ExecutingTransformer.Factory
                    public ResettableClassFileTransformer make(ByteBuddy byteBuddy, Listener listener, PoolStrategy poolStrategy, TypeStrategy typeStrategy, LocationStrategy locationStrategy, NativeMethodStrategy nativeMethodStrategy, InitializationStrategy initializationStrategy, InjectionStrategy injectionStrategy, LambdaInstrumentationStrategy lambdaInstrumentationStrategy, DescriptionStrategy descriptionStrategy, FallbackStrategy fallbackStrategy, ClassFileBufferStrategy classFileBufferStrategy, InstallationListener installationListener, RawMatcher ignoreMatcher, List<Transformation> transformations, CircularityLock circularityLock) {
                        return new ExecutingTransformer(byteBuddy, listener, poolStrategy, typeStrategy, locationStrategy, nativeMethodStrategy, initializationStrategy, injectionStrategy, lambdaInstrumentationStrategy, descriptionStrategy, fallbackStrategy, classFileBufferStrategy, installationListener, ignoreMatcher, transformations, circularityLock);
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Default$ExecutingTransformer$LegacyVmDispatcher.class */
            protected class LegacyVmDispatcher implements PrivilegedAction<byte[]> {
                private final ClassLoader classLoader;
                private final String internalTypeName;
                private final Class<?> classBeingRedefined;
                private final ProtectionDomain protectionDomain;
                private final byte[] binaryRepresentation;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.internalTypeName.equals(((LegacyVmDispatcher) obj).internalTypeName) && this.classLoader.equals(((LegacyVmDispatcher) obj).classLoader) && this.classBeingRedefined.equals(((LegacyVmDispatcher) obj).classBeingRedefined) && this.protectionDomain.equals(((LegacyVmDispatcher) obj).protectionDomain) && Arrays.equals(this.binaryRepresentation, ((LegacyVmDispatcher) obj).binaryRepresentation) && ExecutingTransformer.this.equals(ExecutingTransformer.this);
                }

                public int hashCode() {
                    return (((((((((((17 * 31) + this.classLoader.hashCode()) * 31) + this.internalTypeName.hashCode()) * 31) + this.classBeingRedefined.hashCode()) * 31) + this.protectionDomain.hashCode()) * 31) + Arrays.hashCode(this.binaryRepresentation)) * 31) + ExecutingTransformer.this.hashCode();
                }

                protected LegacyVmDispatcher(ClassLoader classLoader, String internalTypeName, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] binaryRepresentation) {
                    this.classLoader = classLoader;
                    this.internalTypeName = internalTypeName;
                    this.classBeingRedefined = classBeingRedefined;
                    this.protectionDomain = protectionDomain;
                    this.binaryRepresentation = binaryRepresentation;
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public byte[] run() {
                    return ExecutingTransformer.this.transform(JavaModule.UNSUPPORTED, this.classLoader, this.internalTypeName, this.classBeingRedefined, this.protectionDomain, this.binaryRepresentation);
                }
            }

            @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Default$ExecutingTransformer$Java9CapableVmDispatcher.class */
            protected class Java9CapableVmDispatcher implements PrivilegedAction<byte[]> {
                private final Object rawModule;
                private final ClassLoader classLoader;
                private final String internalTypeName;
                private final Class<?> classBeingRedefined;
                private final ProtectionDomain protectionDomain;
                private final byte[] binaryRepresentation;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.internalTypeName.equals(((Java9CapableVmDispatcher) obj).internalTypeName) && this.rawModule.equals(((Java9CapableVmDispatcher) obj).rawModule) && this.classLoader.equals(((Java9CapableVmDispatcher) obj).classLoader) && this.classBeingRedefined.equals(((Java9CapableVmDispatcher) obj).classBeingRedefined) && this.protectionDomain.equals(((Java9CapableVmDispatcher) obj).protectionDomain) && Arrays.equals(this.binaryRepresentation, ((Java9CapableVmDispatcher) obj).binaryRepresentation) && ExecutingTransformer.this.equals(ExecutingTransformer.this);
                }

                public int hashCode() {
                    return (((((((((((((17 * 31) + this.rawModule.hashCode()) * 31) + this.classLoader.hashCode()) * 31) + this.internalTypeName.hashCode()) * 31) + this.classBeingRedefined.hashCode()) * 31) + this.protectionDomain.hashCode()) * 31) + Arrays.hashCode(this.binaryRepresentation)) * 31) + ExecutingTransformer.this.hashCode();
                }

                protected Java9CapableVmDispatcher(Object rawModule, ClassLoader classLoader, String internalTypeName, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] binaryRepresentation) {
                    this.rawModule = rawModule;
                    this.classLoader = classLoader;
                    this.internalTypeName = internalTypeName;
                    this.classBeingRedefined = classBeingRedefined;
                    this.protectionDomain = protectionDomain;
                    this.binaryRepresentation = binaryRepresentation;
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public byte[] run() {
                    return ExecutingTransformer.this.transform(JavaModule.of(this.rawModule), this.classLoader, this.internalTypeName, this.classBeingRedefined, this.protectionDomain, this.binaryRepresentation);
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Default$Delegator.class */
        protected abstract class Delegator<T extends Matchable<T>> extends Matchable.AbstractBase<T> implements AgentBuilder {
            protected abstract AgentBuilder materialize();

            protected Delegator() {
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder
            public AgentBuilder with(ByteBuddy byteBuddy) {
                return materialize().with(byteBuddy);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder
            public AgentBuilder with(Listener listener) {
                return materialize().with(listener);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder
            public AgentBuilder with(CircularityLock circularityLock) {
                return materialize().with(circularityLock);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder
            public AgentBuilder with(TypeStrategy typeStrategy) {
                return materialize().with(typeStrategy);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder
            public AgentBuilder with(PoolStrategy poolStrategy) {
                return materialize().with(poolStrategy);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder
            public AgentBuilder with(LocationStrategy locationStrategy) {
                return materialize().with(locationStrategy);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder
            public AgentBuilder with(InitializationStrategy initializationStrategy) {
                return materialize().with(initializationStrategy);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder
            public RedefinitionListenable.WithoutBatchStrategy with(RedefinitionStrategy redefinitionStrategy) {
                return materialize().with(redefinitionStrategy);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder
            public AgentBuilder with(LambdaInstrumentationStrategy lambdaInstrumentationStrategy) {
                return materialize().with(lambdaInstrumentationStrategy);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder
            public AgentBuilder with(DescriptionStrategy descriptionStrategy) {
                return materialize().with(descriptionStrategy);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder
            public AgentBuilder with(FallbackStrategy fallbackStrategy) {
                return materialize().with(fallbackStrategy);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder
            public AgentBuilder with(ClassFileBufferStrategy classFileBufferStrategy) {
                return materialize().with(classFileBufferStrategy);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder
            public AgentBuilder with(InstallationListener installationListener) {
                return materialize().with(installationListener);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder
            public AgentBuilder with(InjectionStrategy injectionStrategy) {
                return materialize().with(injectionStrategy);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder
            public AgentBuilder with(TransformerDecorator transformerDecorator) {
                return materialize().with(transformerDecorator);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder
            public AgentBuilder enableNativeMethodPrefix(String prefix) {
                return materialize().enableNativeMethodPrefix(prefix);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder
            public AgentBuilder disableNativeMethodPrefix() {
                return materialize().disableNativeMethodPrefix();
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder
            public AgentBuilder disableClassFormatChanges() {
                return materialize().disableClassFormatChanges();
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder
            public AgentBuilder assureReadEdgeTo(Instrumentation instrumentation, Class<?>... type) {
                return materialize().assureReadEdgeTo(instrumentation, type);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder
            public AgentBuilder assureReadEdgeTo(Instrumentation instrumentation, JavaModule... module) {
                return materialize().assureReadEdgeTo(instrumentation, module);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder
            public AgentBuilder assureReadEdgeTo(Instrumentation instrumentation, Collection<? extends JavaModule> modules) {
                return materialize().assureReadEdgeTo(instrumentation, modules);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder
            public AgentBuilder assureReadEdgeFromAndTo(Instrumentation instrumentation, Class<?>... type) {
                return materialize().assureReadEdgeFromAndTo(instrumentation, type);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder
            public AgentBuilder assureReadEdgeFromAndTo(Instrumentation instrumentation, JavaModule... module) {
                return materialize().assureReadEdgeFromAndTo(instrumentation, module);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder
            public AgentBuilder assureReadEdgeFromAndTo(Instrumentation instrumentation, Collection<? extends JavaModule> modules) {
                return materialize().assureReadEdgeFromAndTo(instrumentation, modules);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder
            public Identified.Narrowable type(ElementMatcher<? super TypeDescription> typeMatcher) {
                return materialize().type(typeMatcher);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder
            public Identified.Narrowable type(ElementMatcher<? super TypeDescription> typeMatcher, ElementMatcher<? super ClassLoader> classLoaderMatcher) {
                return materialize().type(typeMatcher, classLoaderMatcher);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder
            public Identified.Narrowable type(ElementMatcher<? super TypeDescription> typeMatcher, ElementMatcher<? super ClassLoader> classLoaderMatcher, ElementMatcher<? super JavaModule> moduleMatcher) {
                return materialize().type(typeMatcher, classLoaderMatcher, moduleMatcher);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder
            public Identified.Narrowable type(RawMatcher matcher) {
                return materialize().type(matcher);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder
            public Ignored ignore(ElementMatcher<? super TypeDescription> ignoredTypes) {
                return materialize().ignore(ignoredTypes);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder
            public Ignored ignore(ElementMatcher<? super TypeDescription> ignoredTypes, ElementMatcher<? super ClassLoader> ignoredClassLoaders) {
                return materialize().ignore(ignoredTypes, ignoredClassLoaders);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder
            public Ignored ignore(ElementMatcher<? super TypeDescription> typeMatcher, ElementMatcher<? super ClassLoader> classLoaderMatcher, ElementMatcher<? super JavaModule> moduleMatcher) {
                return materialize().ignore(typeMatcher, classLoaderMatcher, moduleMatcher);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder
            public Ignored ignore(RawMatcher rawMatcher) {
                return materialize().ignore(rawMatcher);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder
            public ClassFileTransformer makeRaw() {
                return materialize().makeRaw();
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder
            public ResettableClassFileTransformer installOn(Instrumentation instrumentation) {
                return materialize().installOn(instrumentation);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder
            public ResettableClassFileTransformer installOnByteBuddyAgent() {
                return materialize().installOnByteBuddyAgent();
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder
            public ResettableClassFileTransformer patchOn(Instrumentation instrumentation, ResettableClassFileTransformer classFileTransformer) {
                return materialize().patchOn(instrumentation, classFileTransformer);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder
            public ResettableClassFileTransformer patchOnByteBuddyAgent(ResettableClassFileTransformer classFileTransformer) {
                return materialize().patchOnByteBuddyAgent(classFileTransformer);
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Default$Ignoring.class */
        public class Ignoring extends Delegator<Ignored> implements Ignored {
            private final RawMatcher rawMatcher;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.rawMatcher.equals(((Ignoring) obj).rawMatcher) && Default.this.equals(Default.this);
            }

            public int hashCode() {
                return (((17 * 31) + this.rawMatcher.hashCode()) * 31) + Default.this.hashCode();
            }

            protected Ignoring(RawMatcher rawMatcher) {
                super();
                this.rawMatcher = rawMatcher;
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Default.Delegator
            protected AgentBuilder materialize() {
                return new Default(Default.this.byteBuddy, Default.this.listener, Default.this.circularityLock, Default.this.poolStrategy, Default.this.typeStrategy, Default.this.locationStrategy, Default.this.nativeMethodStrategy, Default.this.transformerDecorator, Default.this.initializationStrategy, Default.this.redefinitionStrategy, Default.this.redefinitionDiscoveryStrategy, Default.this.redefinitionBatchAllocator, Default.this.redefinitionListener, Default.this.redefinitionResubmissionStrategy, Default.this.injectionStrategy, Default.this.lambdaInstrumentationStrategy, Default.this.descriptionStrategy, Default.this.fallbackStrategy, Default.this.classFileBufferStrategy, Default.this.installationListener, this.rawMatcher, Default.this.transformations);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // net.bytebuddy.agent.builder.AgentBuilder.Matchable
            public Ignored and(RawMatcher rawMatcher) {
                return new Ignoring(new RawMatcher.Conjunction(this.rawMatcher, rawMatcher));
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // net.bytebuddy.agent.builder.AgentBuilder.Matchable
            public Ignored or(RawMatcher rawMatcher) {
                return new Ignoring(new RawMatcher.Disjunction(this.rawMatcher, rawMatcher));
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Default$Redefining.class */
        protected static class Redefining extends Default implements RedefinitionListenable.WithoutBatchStrategy {
            @Override // net.bytebuddy.agent.builder.AgentBuilder.Default, net.bytebuddy.agent.builder.AgentBuilder
            public /* bridge */ /* synthetic */ ClassFileTransformer makeRaw() {
                return super.makeRaw();
            }

            protected Redefining(ByteBuddy byteBuddy, Listener listener, CircularityLock circularityLock, PoolStrategy poolStrategy, TypeStrategy typeStrategy, LocationStrategy locationStrategy, NativeMethodStrategy nativeMethodStrategy, TransformerDecorator transformerDecorator, InitializationStrategy initializationStrategy, RedefinitionStrategy redefinitionStrategy, RedefinitionStrategy.DiscoveryStrategy redefinitionDiscoveryStrategy, RedefinitionStrategy.BatchAllocator redefinitionBatchAllocator, RedefinitionStrategy.Listener redefinitionListener, RedefinitionStrategy.ResubmissionStrategy redefinitionResubmissionStrategy, InjectionStrategy injectionStrategy, LambdaInstrumentationStrategy lambdaInstrumentationStrategy, DescriptionStrategy descriptionStrategy, FallbackStrategy fallbackStrategy, ClassFileBufferStrategy classFileBufferStrategy, InstallationListener installationListener, RawMatcher ignoreMatcher, List<Transformation> transformations) {
                super(byteBuddy, listener, circularityLock, poolStrategy, typeStrategy, locationStrategy, nativeMethodStrategy, transformerDecorator, initializationStrategy, redefinitionStrategy, redefinitionDiscoveryStrategy, redefinitionBatchAllocator, redefinitionListener, redefinitionResubmissionStrategy, injectionStrategy, lambdaInstrumentationStrategy, descriptionStrategy, fallbackStrategy, classFileBufferStrategy, installationListener, ignoreMatcher, transformations);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionListenable.WithoutBatchStrategy
            public RedefinitionListenable.WithImplicitDiscoveryStrategy with(RedefinitionStrategy.BatchAllocator redefinitionBatchAllocator) {
                if (!this.redefinitionStrategy.isEnabled()) {
                    throw new IllegalStateException("Cannot set redefinition batch allocator when redefinition is disabled");
                }
                return new Redefining(this.byteBuddy, this.listener, this.circularityLock, this.poolStrategy, this.typeStrategy, this.locationStrategy, this.nativeMethodStrategy, this.transformerDecorator, this.initializationStrategy, this.redefinitionStrategy, this.redefinitionDiscoveryStrategy, redefinitionBatchAllocator, this.redefinitionListener, this.redefinitionResubmissionStrategy, this.injectionStrategy, this.lambdaInstrumentationStrategy, this.descriptionStrategy, this.fallbackStrategy, this.classFileBufferStrategy, this.installationListener, this.ignoreMatcher, this.transformations);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionListenable.WithImplicitDiscoveryStrategy
            public RedefinitionListenable redefineOnly(Class<?>... type) {
                return with(new RedefinitionStrategy.DiscoveryStrategy.Explicit(type));
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionListenable.WithImplicitDiscoveryStrategy
            public RedefinitionListenable with(RedefinitionStrategy.DiscoveryStrategy redefinitionDiscoveryStrategy) {
                if (!this.redefinitionStrategy.isEnabled()) {
                    throw new IllegalStateException("Cannot set redefinition discovery strategy when redefinition is disabled");
                }
                return new Redefining(this.byteBuddy, this.listener, this.circularityLock, this.poolStrategy, this.typeStrategy, this.locationStrategy, this.nativeMethodStrategy, this.transformerDecorator, this.initializationStrategy, this.redefinitionStrategy, redefinitionDiscoveryStrategy, this.redefinitionBatchAllocator, this.redefinitionListener, this.redefinitionResubmissionStrategy, this.injectionStrategy, this.lambdaInstrumentationStrategy, this.descriptionStrategy, this.fallbackStrategy, this.classFileBufferStrategy, this.installationListener, this.ignoreMatcher, this.transformations);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionListenable
            public RedefinitionListenable with(RedefinitionStrategy.Listener redefinitionListener) {
                if (!this.redefinitionStrategy.isEnabled()) {
                    throw new IllegalStateException("Cannot set redefinition listener when redefinition is disabled");
                }
                return new Redefining(this.byteBuddy, this.listener, this.circularityLock, this.poolStrategy, this.typeStrategy, this.locationStrategy, this.nativeMethodStrategy, this.transformerDecorator, this.initializationStrategy, this.redefinitionStrategy, this.redefinitionDiscoveryStrategy, this.redefinitionBatchAllocator, new RedefinitionStrategy.Listener.Compound(this.redefinitionListener, redefinitionListener), this.redefinitionResubmissionStrategy, this.injectionStrategy, this.lambdaInstrumentationStrategy, this.descriptionStrategy, this.fallbackStrategy, this.classFileBufferStrategy, this.installationListener, this.ignoreMatcher, this.transformations);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionListenable
            public AgentBuilder withResubmission(RedefinitionStrategy.ResubmissionScheduler resubmissionScheduler) {
                return withResubmission(resubmissionScheduler, ElementMatchers.any());
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionListenable
            public AgentBuilder withResubmission(RedefinitionStrategy.ResubmissionScheduler resubmissionScheduler, ElementMatcher<? super Throwable> matcher) {
                return withResubmission(resubmissionScheduler, ElementMatchers.any(), ElementMatchers.none());
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.RedefinitionListenable
            public AgentBuilder withResubmission(RedefinitionStrategy.ResubmissionScheduler resubmissionScheduler, ElementMatcher<? super Throwable> matcher, ElementMatcher<? super ClassLoader> classLoaderFilter) {
                if (!this.redefinitionStrategy.isEnabled()) {
                    throw new IllegalStateException("Cannot enable redefinition resubmission when redefinition is disabled");
                }
                return new Redefining(this.byteBuddy, this.listener, this.circularityLock, this.poolStrategy, this.typeStrategy, this.locationStrategy, this.nativeMethodStrategy, this.transformerDecorator, this.initializationStrategy, this.redefinitionStrategy, this.redefinitionDiscoveryStrategy, this.redefinitionBatchAllocator, this.redefinitionListener, new RedefinitionStrategy.ResubmissionStrategy.Enabled(resubmissionScheduler, matcher, classLoaderFilter), this.injectionStrategy, this.lambdaInstrumentationStrategy, this.descriptionStrategy, this.fallbackStrategy, this.classFileBufferStrategy, this.installationListener, this.ignoreMatcher, this.transformations);
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/AgentBuilder$Default$Transforming.class */
        public class Transforming extends Delegator<Identified.Narrowable> implements Identified.Extendable, Identified.Narrowable {
            private final RawMatcher rawMatcher;
            private final List<Transformer> transformers;
            private final boolean terminal;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.terminal == ((Transforming) obj).terminal && this.rawMatcher.equals(((Transforming) obj).rawMatcher) && this.transformers.equals(((Transforming) obj).transformers) && Default.this.equals(Default.this);
            }

            public int hashCode() {
                return (((((((17 * 31) + this.rawMatcher.hashCode()) * 31) + this.transformers.hashCode()) * 31) + (this.terminal ? 1 : 0)) * 31) + Default.this.hashCode();
            }

            protected Transforming(RawMatcher rawMatcher, List<Transformer> transformers, boolean terminal) {
                super();
                this.rawMatcher = rawMatcher;
                this.transformers = transformers;
                this.terminal = terminal;
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Default.Delegator
            protected AgentBuilder materialize() {
                return new Default(Default.this.byteBuddy, Default.this.listener, Default.this.circularityLock, Default.this.poolStrategy, Default.this.typeStrategy, Default.this.locationStrategy, Default.this.nativeMethodStrategy, Default.this.transformerDecorator, Default.this.initializationStrategy, Default.this.redefinitionStrategy, Default.this.redefinitionDiscoveryStrategy, Default.this.redefinitionBatchAllocator, Default.this.redefinitionListener, Default.this.redefinitionResubmissionStrategy, Default.this.injectionStrategy, Default.this.lambdaInstrumentationStrategy, Default.this.descriptionStrategy, Default.this.fallbackStrategy, Default.this.classFileBufferStrategy, Default.this.installationListener, Default.this.ignoreMatcher, CompoundList.of(Default.this.transformations, new Transformation(this.rawMatcher, this.transformers, this.terminal)));
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Identified
            public Identified.Extendable transform(Transformer transformer) {
                return new Transforming(this.rawMatcher, CompoundList.of(this.transformers, transformer), this.terminal);
            }

            @Override // net.bytebuddy.agent.builder.AgentBuilder.Identified.Extendable
            public AgentBuilder asTerminalTransformation() {
                return new Transforming(this.rawMatcher, this.transformers, true);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // net.bytebuddy.agent.builder.AgentBuilder.Matchable
            public Identified.Narrowable and(RawMatcher rawMatcher) {
                return new Transforming(new RawMatcher.Conjunction(this.rawMatcher, rawMatcher), this.transformers, this.terminal);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // net.bytebuddy.agent.builder.AgentBuilder.Matchable
            public Identified.Narrowable or(RawMatcher rawMatcher) {
                return new Transforming(new RawMatcher.Disjunction(this.rawMatcher, rawMatcher), this.transformers, this.terminal);
            }
        }
    }
}
