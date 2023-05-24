package net.bytebuddy.dynamic.scaffold;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.annotation.AnnotationValue;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.Transformer;
import net.bytebuddy.dynamic.VisibilityBridgeStrategy;
import net.bytebuddy.dynamic.scaffold.InstrumentedType;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import net.bytebuddy.dynamic.scaffold.TypeWriter;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.LoadedTypeInitializer;
import net.bytebuddy.implementation.attribute.MethodAttributeAppender;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.matcher.LatentMatcher;
import net.bytebuddy.utility.CompoundList;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodRegistry.class */
public interface MethodRegistry {

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodRegistry$Compiled.class */
    public interface Compiled extends TypeWriter.MethodPool {
        TypeDescription getInstrumentedType();

        MethodList<?> getMethods();

        MethodList<?> getInstrumentedMethods();

        LoadedTypeInitializer getLoadedTypeInitializer();

        TypeInitializer getTypeInitializer();
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodRegistry$Prepared.class */
    public interface Prepared {
        TypeDescription getInstrumentedType();

        MethodList<?> getMethods();

        MethodList<?> getInstrumentedMethods();

        LoadedTypeInitializer getLoadedTypeInitializer();

        TypeInitializer getTypeInitializer();

        Compiled compile(Implementation.Target.Factory factory, ClassFileVersion classFileVersion);
    }

    MethodRegistry prepend(LatentMatcher<? super MethodDescription> latentMatcher, Handler handler, MethodAttributeAppender.Factory factory, Transformer<MethodDescription> transformer);

    MethodRegistry append(LatentMatcher<? super MethodDescription> latentMatcher, Handler handler, MethodAttributeAppender.Factory factory, Transformer<MethodDescription> transformer);

    Prepared prepare(InstrumentedType instrumentedType, MethodGraph.Compiler compiler, TypeValidation typeValidation, VisibilityBridgeStrategy visibilityBridgeStrategy, LatentMatcher<? super MethodDescription> latentMatcher);

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodRegistry$Handler.class */
    public interface Handler extends InstrumentedType.Prepareable {

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodRegistry$Handler$Compiled.class */
        public interface Compiled {
            TypeWriter.MethodPool.Record assemble(MethodDescription methodDescription, MethodAttributeAppender methodAttributeAppender, Visibility visibility);
        }

        Compiled compile(Implementation.Target target);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodRegistry$Handler$ForAbstractMethod.class */
        public enum ForAbstractMethod implements Handler, Compiled {
            INSTANCE;

            @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
            public InstrumentedType prepare(InstrumentedType instrumentedType) {
                return instrumentedType;
            }

            @Override // net.bytebuddy.dynamic.scaffold.MethodRegistry.Handler
            public Compiled compile(Implementation.Target implementationTarget) {
                return this;
            }

            @Override // net.bytebuddy.dynamic.scaffold.MethodRegistry.Handler.Compiled
            public TypeWriter.MethodPool.Record assemble(MethodDescription methodDescription, MethodAttributeAppender attributeAppender, Visibility visibility) {
                return new TypeWriter.MethodPool.Record.ForDefinedMethod.WithoutBody(methodDescription, attributeAppender, visibility);
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodRegistry$Handler$ForVisibilityBridge.class */
        public enum ForVisibilityBridge implements Handler {
            INSTANCE;

            @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
            public InstrumentedType prepare(InstrumentedType instrumentedType) {
                throw new IllegalStateException("A visibility bridge handler must not apply any preparations");
            }

            @Override // net.bytebuddy.dynamic.scaffold.MethodRegistry.Handler
            public Compiled compile(Implementation.Target implementationTarget) {
                return new Compiled(implementationTarget.getInstrumentedType());
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodRegistry$Handler$ForVisibilityBridge$Compiled.class */
            public static class Compiled implements Compiled {
                private final TypeDescription instrumentedType;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.instrumentedType.equals(((Compiled) obj).instrumentedType);
                }

                public int hashCode() {
                    return (17 * 31) + this.instrumentedType.hashCode();
                }

                protected Compiled(TypeDescription instrumentedType) {
                    this.instrumentedType = instrumentedType;
                }

                @Override // net.bytebuddy.dynamic.scaffold.MethodRegistry.Handler.Compiled
                public TypeWriter.MethodPool.Record assemble(MethodDescription methodDescription, MethodAttributeAppender attributeAppender, Visibility visibility) {
                    return TypeWriter.MethodPool.Record.ForDefinedMethod.OfVisibilityBridge.of(this.instrumentedType, methodDescription, attributeAppender);
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodRegistry$Handler$ForImplementation.class */
        public static class ForImplementation implements Handler {
            private final Implementation implementation;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.implementation.equals(((ForImplementation) obj).implementation);
            }

            public int hashCode() {
                return (17 * 31) + this.implementation.hashCode();
            }

            public ForImplementation(Implementation implementation) {
                this.implementation = implementation;
            }

            @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
            public InstrumentedType prepare(InstrumentedType instrumentedType) {
                return this.implementation.prepare(instrumentedType);
            }

            @Override // net.bytebuddy.dynamic.scaffold.MethodRegistry.Handler
            public Compiled compile(Implementation.Target implementationTarget) {
                return new Compiled(this.implementation.appender(implementationTarget));
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodRegistry$Handler$ForImplementation$Compiled.class */
            public static class Compiled implements Compiled {
                private final ByteCodeAppender byteCodeAppender;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.byteCodeAppender.equals(((Compiled) obj).byteCodeAppender);
                }

                public int hashCode() {
                    return (17 * 31) + this.byteCodeAppender.hashCode();
                }

                protected Compiled(ByteCodeAppender byteCodeAppender) {
                    this.byteCodeAppender = byteCodeAppender;
                }

                @Override // net.bytebuddy.dynamic.scaffold.MethodRegistry.Handler.Compiled
                public TypeWriter.MethodPool.Record assemble(MethodDescription methodDescription, MethodAttributeAppender attributeAppender, Visibility visibility) {
                    return new TypeWriter.MethodPool.Record.ForDefinedMethod.WithBody(methodDescription, this.byteCodeAppender, attributeAppender, visibility);
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodRegistry$Handler$ForAnnotationValue.class */
        public static class ForAnnotationValue implements Handler, Compiled {
            private final AnnotationValue<?, ?> annotationValue;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.annotationValue.equals(((ForAnnotationValue) obj).annotationValue);
            }

            public int hashCode() {
                return (17 * 31) + this.annotationValue.hashCode();
            }

            public ForAnnotationValue(AnnotationValue<?, ?> annotationValue) {
                this.annotationValue = annotationValue;
            }

            @Override // net.bytebuddy.dynamic.scaffold.InstrumentedType.Prepareable
            public InstrumentedType prepare(InstrumentedType instrumentedType) {
                return instrumentedType;
            }

            @Override // net.bytebuddy.dynamic.scaffold.MethodRegistry.Handler
            public Compiled compile(Implementation.Target implementationTarget) {
                return this;
            }

            @Override // net.bytebuddy.dynamic.scaffold.MethodRegistry.Handler.Compiled
            public TypeWriter.MethodPool.Record assemble(MethodDescription methodDescription, MethodAttributeAppender attributeAppender, Visibility visibility) {
                return new TypeWriter.MethodPool.Record.ForDefinedMethod.WithAnnotationDefaultValue(methodDescription, this.annotationValue, attributeAppender);
            }
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodRegistry$Default.class */
    public static class Default implements MethodRegistry {
        private final List<Entry> entries;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.entries.equals(((Default) obj).entries);
        }

        public int hashCode() {
            return (17 * 31) + this.entries.hashCode();
        }

        public Default() {
            this.entries = Collections.emptyList();
        }

        private Default(List<Entry> entries) {
            this.entries = entries;
        }

        @Override // net.bytebuddy.dynamic.scaffold.MethodRegistry
        public MethodRegistry prepend(LatentMatcher<? super MethodDescription> matcher, Handler handler, MethodAttributeAppender.Factory attributeAppenderFactory, Transformer<MethodDescription> transformer) {
            return new Default(CompoundList.of(new Entry(matcher, handler, attributeAppenderFactory, transformer), this.entries));
        }

        @Override // net.bytebuddy.dynamic.scaffold.MethodRegistry
        public MethodRegistry append(LatentMatcher<? super MethodDescription> matcher, Handler handler, MethodAttributeAppender.Factory attributeAppenderFactory, Transformer<MethodDescription> transformer) {
            return new Default(CompoundList.of(this.entries, new Entry(matcher, handler, attributeAppenderFactory, transformer)));
        }

        @Override // net.bytebuddy.dynamic.scaffold.MethodRegistry
        public Prepared prepare(InstrumentedType instrumentedType, MethodGraph.Compiler methodGraphCompiler, TypeValidation typeValidation, VisibilityBridgeStrategy visibilityBridgeStrategy, LatentMatcher<? super MethodDescription> ignoredMethods) {
            InstrumentedType typeDescription;
            LinkedHashMap<MethodDescription, Prepared.Entry> implementations = new LinkedHashMap<>();
            Set<Handler> handlers = new HashSet<>();
            Set<MethodDescription> declaredMethods = new HashSet<>(instrumentedType.getDeclaredMethods());
            for (Entry entry : this.entries) {
                if (handlers.add(entry.getHandler()) && instrumentedType != (typeDescription = entry.getHandler().prepare(instrumentedType))) {
                    for (MethodDescription methodDescription : typeDescription.getDeclaredMethods()) {
                        if (!declaredMethods.contains(methodDescription)) {
                            implementations.put(methodDescription, entry.asSupplementaryEntry(methodDescription));
                            declaredMethods.add(methodDescription);
                        }
                    }
                    instrumentedType = typeDescription;
                }
            }
            MethodGraph.Linked methodGraph = methodGraphCompiler.compile(instrumentedType);
            ElementMatcher.Junction and = ElementMatchers.not(ElementMatchers.anyOf(implementations.keySet())).and(ElementMatchers.returns(ElementMatchers.isVisibleTo(instrumentedType))).and(ElementMatchers.hasParameters(ElementMatchers.whereNone(ElementMatchers.hasType(ElementMatchers.not(ElementMatchers.isVisibleTo(instrumentedType)))))).and(ignoredMethods.resolve(instrumentedType));
            List<MethodDescription> methods = new ArrayList<>();
            Iterator it = methodGraph.listNodes().iterator();
            while (it.hasNext()) {
                MethodGraph.Node node = (MethodGraph.Node) it.next();
                MethodDescription methodDescription2 = node.getRepresentative();
                boolean visibilityBridge = instrumentedType.isPublic() && !instrumentedType.isInterface();
                if (and.matches(methodDescription2)) {
                    Iterator<Entry> it2 = this.entries.iterator();
                    while (true) {
                        if (!it2.hasNext()) {
                            break;
                        }
                        Entry entry2 = it2.next();
                        if (entry2.resolve(instrumentedType).matches(methodDescription2)) {
                            implementations.put(methodDescription2, entry2.asPreparedEntry(instrumentedType, methodDescription2, node.getMethodTypes(), node.getVisibility()));
                            visibilityBridge = false;
                            break;
                        }
                    }
                }
                if (visibilityBridge && !node.getSort().isMadeVisible() && methodDescription2.isPublic() && !methodDescription2.isAbstract() && !methodDescription2.isFinal() && methodDescription2.getDeclaringType().isPackagePrivate() && visibilityBridgeStrategy.generateVisibilityBridge(methodDescription2)) {
                    implementations.put(methodDescription2, Prepared.Entry.forVisibilityBridge(methodDescription2, node.getVisibility()));
                }
                methods.add(methodDescription2);
            }
            for (MethodDescription methodDescription3 : CompoundList.of(instrumentedType.getDeclaredMethods().filter(ElementMatchers.not(ElementMatchers.isVirtual()).and(and)), new MethodDescription.Latent.TypeInitializer(instrumentedType))) {
                Iterator<Entry> it3 = this.entries.iterator();
                while (true) {
                    if (it3.hasNext()) {
                        Entry entry3 = it3.next();
                        if (entry3.resolve(instrumentedType).matches(methodDescription3)) {
                            implementations.put(methodDescription3, entry3.asPreparedEntry(instrumentedType, methodDescription3, methodDescription3.getVisibility()));
                            break;
                        }
                    }
                }
                methods.add(methodDescription3);
            }
            return new Prepared(implementations, instrumentedType.getLoadedTypeInitializer(), instrumentedType.getTypeInitializer(), typeValidation.isEnabled() ? instrumentedType.validated() : instrumentedType, methodGraph, new MethodList.Explicit(methods));
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodRegistry$Default$Entry.class */
        protected static class Entry implements LatentMatcher<MethodDescription> {
            private final LatentMatcher<? super MethodDescription> matcher;
            private final Handler handler;
            private final MethodAttributeAppender.Factory attributeAppenderFactory;
            private final Transformer<MethodDescription> transformer;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.matcher.equals(((Entry) obj).matcher) && this.handler.equals(((Entry) obj).handler) && this.attributeAppenderFactory.equals(((Entry) obj).attributeAppenderFactory) && this.transformer.equals(((Entry) obj).transformer);
            }

            public int hashCode() {
                return (((((((17 * 31) + this.matcher.hashCode()) * 31) + this.handler.hashCode()) * 31) + this.attributeAppenderFactory.hashCode()) * 31) + this.transformer.hashCode();
            }

            protected Entry(LatentMatcher<? super MethodDescription> matcher, Handler handler, MethodAttributeAppender.Factory attributeAppenderFactory, Transformer<MethodDescription> transformer) {
                this.matcher = matcher;
                this.handler = handler;
                this.attributeAppenderFactory = attributeAppenderFactory;
                this.transformer = transformer;
            }

            protected Prepared.Entry asPreparedEntry(TypeDescription instrumentedType, MethodDescription methodDescription, Visibility visibility) {
                return asPreparedEntry(instrumentedType, methodDescription, Collections.emptySet(), visibility);
            }

            protected Prepared.Entry asPreparedEntry(TypeDescription instrumentedType, MethodDescription methodDescription, Set<MethodDescription.TypeToken> methodTypes, Visibility visibility) {
                return new Prepared.Entry(this.handler, this.attributeAppenderFactory, this.transformer.transform(instrumentedType, methodDescription), methodTypes, visibility, false);
            }

            protected Prepared.Entry asSupplementaryEntry(MethodDescription methodDescription) {
                return new Prepared.Entry(this.handler, MethodAttributeAppender.Explicit.of(methodDescription), methodDescription, Collections.emptySet(), methodDescription.getVisibility(), false);
            }

            protected Handler getHandler() {
                return this.handler;
            }

            @Override // net.bytebuddy.matcher.LatentMatcher
            public ElementMatcher<? super MethodDescription> resolve(TypeDescription typeDescription) {
                return this.matcher.resolve(typeDescription);
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodRegistry$Default$Prepared.class */
        protected static class Prepared implements Prepared {
            private final LinkedHashMap<MethodDescription, Entry> implementations;
            private final LoadedTypeInitializer loadedTypeInitializer;
            private final TypeInitializer typeInitializer;
            private final TypeDescription instrumentedType;
            private final MethodGraph.Linked methodGraph;
            private final MethodList<?> methods;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.implementations.equals(((Prepared) obj).implementations) && this.loadedTypeInitializer.equals(((Prepared) obj).loadedTypeInitializer) && this.typeInitializer.equals(((Prepared) obj).typeInitializer) && this.instrumentedType.equals(((Prepared) obj).instrumentedType) && this.methodGraph.equals(((Prepared) obj).methodGraph) && this.methods.equals(((Prepared) obj).methods);
            }

            public int hashCode() {
                return (((((((((((17 * 31) + this.implementations.hashCode()) * 31) + this.loadedTypeInitializer.hashCode()) * 31) + this.typeInitializer.hashCode()) * 31) + this.instrumentedType.hashCode()) * 31) + this.methodGraph.hashCode()) * 31) + this.methods.hashCode();
            }

            protected Prepared(LinkedHashMap<MethodDescription, Entry> implementations, LoadedTypeInitializer loadedTypeInitializer, TypeInitializer typeInitializer, TypeDescription instrumentedType, MethodGraph.Linked methodGraph, MethodList<?> methods) {
                this.implementations = implementations;
                this.loadedTypeInitializer = loadedTypeInitializer;
                this.typeInitializer = typeInitializer;
                this.instrumentedType = instrumentedType;
                this.methodGraph = methodGraph;
                this.methods = methods;
            }

            @Override // net.bytebuddy.dynamic.scaffold.MethodRegistry.Prepared
            public TypeDescription getInstrumentedType() {
                return this.instrumentedType;
            }

            @Override // net.bytebuddy.dynamic.scaffold.MethodRegistry.Prepared
            public LoadedTypeInitializer getLoadedTypeInitializer() {
                return this.loadedTypeInitializer;
            }

            @Override // net.bytebuddy.dynamic.scaffold.MethodRegistry.Prepared
            public TypeInitializer getTypeInitializer() {
                return this.typeInitializer;
            }

            @Override // net.bytebuddy.dynamic.scaffold.MethodRegistry.Prepared
            public MethodList<?> getMethods() {
                return this.methods;
            }

            @Override // net.bytebuddy.dynamic.scaffold.MethodRegistry.Prepared
            public MethodList<?> getInstrumentedMethods() {
                return (MethodList) new MethodList.Explicit(new ArrayList(this.implementations.keySet())).filter(ElementMatchers.not(ElementMatchers.isTypeInitializer()));
            }

            @Override // net.bytebuddy.dynamic.scaffold.MethodRegistry.Prepared
            public Compiled compile(Implementation.Target.Factory implementationTargetFactory, ClassFileVersion classFileVersion) {
                Map<Handler, Handler.Compiled> compilationCache = new HashMap<>();
                Map<MethodAttributeAppender.Factory, MethodAttributeAppender> attributeAppenderCache = new HashMap<>();
                LinkedHashMap<MethodDescription, Compiled.Entry> entries = new LinkedHashMap<>();
                Implementation.Target implementationTarget = implementationTargetFactory.make(this.instrumentedType, this.methodGraph, classFileVersion);
                for (Map.Entry<MethodDescription, Entry> entry : this.implementations.entrySet()) {
                    Handler.Compiled cachedHandler = compilationCache.get(entry.getValue().getHandler());
                    if (cachedHandler == null) {
                        cachedHandler = entry.getValue().getHandler().compile(implementationTarget);
                        compilationCache.put(entry.getValue().getHandler(), cachedHandler);
                    }
                    MethodAttributeAppender cachedAttributeAppender = attributeAppenderCache.get(entry.getValue().getAppenderFactory());
                    if (cachedAttributeAppender == null) {
                        cachedAttributeAppender = entry.getValue().getAppenderFactory().make(this.instrumentedType);
                        attributeAppenderCache.put(entry.getValue().getAppenderFactory(), cachedAttributeAppender);
                    }
                    entries.put(entry.getKey(), new Compiled.Entry(cachedHandler, cachedAttributeAppender, entry.getValue().getMethodDescription(), entry.getValue().resolveBridgeTypes(), entry.getValue().getVisibility(), entry.getValue().isBridgeMethod()));
                }
                return new Compiled(this.instrumentedType, this.loadedTypeInitializer, this.typeInitializer, this.methods, entries, classFileVersion.isAtLeast(ClassFileVersion.JAVA_V5));
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodRegistry$Default$Prepared$Entry.class */
            public static class Entry {
                private final Handler handler;
                private final MethodAttributeAppender.Factory attributeAppenderFactory;
                private final MethodDescription methodDescription;
                private final Set<MethodDescription.TypeToken> typeTokens;
                private Visibility visibility;
                private final boolean bridgeMethod;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.bridgeMethod == ((Entry) obj).bridgeMethod && this.visibility.equals(((Entry) obj).visibility) && this.handler.equals(((Entry) obj).handler) && this.attributeAppenderFactory.equals(((Entry) obj).attributeAppenderFactory) && this.methodDescription.equals(((Entry) obj).methodDescription) && this.typeTokens.equals(((Entry) obj).typeTokens);
                }

                public int hashCode() {
                    return (((((((((((17 * 31) + this.handler.hashCode()) * 31) + this.attributeAppenderFactory.hashCode()) * 31) + this.methodDescription.hashCode()) * 31) + this.typeTokens.hashCode()) * 31) + this.visibility.hashCode()) * 31) + (this.bridgeMethod ? 1 : 0);
                }

                protected Entry(Handler handler, MethodAttributeAppender.Factory attributeAppenderFactory, MethodDescription methodDescription, Set<MethodDescription.TypeToken> typeTokens, Visibility visibility, boolean bridgeMethod) {
                    this.handler = handler;
                    this.attributeAppenderFactory = attributeAppenderFactory;
                    this.methodDescription = methodDescription;
                    this.typeTokens = typeTokens;
                    this.visibility = visibility;
                    this.bridgeMethod = bridgeMethod;
                }

                protected static Entry forVisibilityBridge(MethodDescription bridgeTarget, Visibility visibility) {
                    return new Entry(Handler.ForVisibilityBridge.INSTANCE, MethodAttributeAppender.Explicit.of(bridgeTarget), bridgeTarget, Collections.emptySet(), visibility, true);
                }

                protected Handler getHandler() {
                    return this.handler;
                }

                protected MethodAttributeAppender.Factory getAppenderFactory() {
                    return this.attributeAppenderFactory;
                }

                protected MethodDescription getMethodDescription() {
                    return this.methodDescription;
                }

                protected Set<MethodDescription.TypeToken> resolveBridgeTypes() {
                    HashSet<MethodDescription.TypeToken> typeTokens = new HashSet<>(this.typeTokens);
                    typeTokens.remove(this.methodDescription.asTypeToken());
                    return typeTokens;
                }

                protected Visibility getVisibility() {
                    return this.visibility;
                }

                protected boolean isBridgeMethod() {
                    return this.bridgeMethod;
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodRegistry$Default$Compiled.class */
        protected static class Compiled implements Compiled {
            private final TypeDescription instrumentedType;
            private final LoadedTypeInitializer loadedTypeInitializer;
            private final TypeInitializer typeInitializer;
            private final MethodList<?> methods;
            private final LinkedHashMap<MethodDescription, Entry> implementations;
            private final boolean supportsBridges;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.supportsBridges == ((Compiled) obj).supportsBridges && this.instrumentedType.equals(((Compiled) obj).instrumentedType) && this.loadedTypeInitializer.equals(((Compiled) obj).loadedTypeInitializer) && this.typeInitializer.equals(((Compiled) obj).typeInitializer) && this.methods.equals(((Compiled) obj).methods) && this.implementations.equals(((Compiled) obj).implementations);
            }

            public int hashCode() {
                return (((((((((((17 * 31) + this.instrumentedType.hashCode()) * 31) + this.loadedTypeInitializer.hashCode()) * 31) + this.typeInitializer.hashCode()) * 31) + this.methods.hashCode()) * 31) + this.implementations.hashCode()) * 31) + (this.supportsBridges ? 1 : 0);
            }

            protected Compiled(TypeDescription instrumentedType, LoadedTypeInitializer loadedTypeInitializer, TypeInitializer typeInitializer, MethodList<?> methods, LinkedHashMap<MethodDescription, Entry> implementations, boolean supportsBridges) {
                this.instrumentedType = instrumentedType;
                this.loadedTypeInitializer = loadedTypeInitializer;
                this.typeInitializer = typeInitializer;
                this.methods = methods;
                this.implementations = implementations;
                this.supportsBridges = supportsBridges;
            }

            @Override // net.bytebuddy.dynamic.scaffold.MethodRegistry.Compiled
            public TypeDescription getInstrumentedType() {
                return this.instrumentedType;
            }

            @Override // net.bytebuddy.dynamic.scaffold.MethodRegistry.Compiled
            public LoadedTypeInitializer getLoadedTypeInitializer() {
                return this.loadedTypeInitializer;
            }

            @Override // net.bytebuddy.dynamic.scaffold.MethodRegistry.Compiled
            public TypeInitializer getTypeInitializer() {
                return this.typeInitializer;
            }

            @Override // net.bytebuddy.dynamic.scaffold.MethodRegistry.Compiled
            public MethodList<?> getMethods() {
                return this.methods;
            }

            @Override // net.bytebuddy.dynamic.scaffold.MethodRegistry.Compiled
            public MethodList<?> getInstrumentedMethods() {
                return (MethodList) new MethodList.Explicit(new ArrayList(this.implementations.keySet())).filter(ElementMatchers.not(ElementMatchers.isTypeInitializer()));
            }

            @Override // net.bytebuddy.dynamic.scaffold.TypeWriter.MethodPool
            public TypeWriter.MethodPool.Record target(MethodDescription methodDescription) {
                Entry entry = this.implementations.get(methodDescription);
                return entry == null ? new TypeWriter.MethodPool.Record.ForNonImplementedMethod(methodDescription) : entry.bind(this.instrumentedType, this.supportsBridges);
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodRegistry$Default$Compiled$Entry.class */
            protected static class Entry {
                private final Handler.Compiled handler;
                private final MethodAttributeAppender attributeAppender;
                private final MethodDescription methodDescription;
                private final Set<MethodDescription.TypeToken> bridgeTypes;
                private final Visibility visibility;
                private final boolean bridgeMethod;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.bridgeMethod == ((Entry) obj).bridgeMethod && this.visibility.equals(((Entry) obj).visibility) && this.handler.equals(((Entry) obj).handler) && this.attributeAppender.equals(((Entry) obj).attributeAppender) && this.methodDescription.equals(((Entry) obj).methodDescription) && this.bridgeTypes.equals(((Entry) obj).bridgeTypes);
                }

                public int hashCode() {
                    return (((((((((((17 * 31) + this.handler.hashCode()) * 31) + this.attributeAppender.hashCode()) * 31) + this.methodDescription.hashCode()) * 31) + this.bridgeTypes.hashCode()) * 31) + this.visibility.hashCode()) * 31) + (this.bridgeMethod ? 1 : 0);
                }

                protected Entry(Handler.Compiled handler, MethodAttributeAppender attributeAppender, MethodDescription methodDescription, Set<MethodDescription.TypeToken> bridgeTypes, Visibility visibility, boolean bridgeMethod) {
                    this.handler = handler;
                    this.attributeAppender = attributeAppender;
                    this.methodDescription = methodDescription;
                    this.bridgeTypes = bridgeTypes;
                    this.visibility = visibility;
                    this.bridgeMethod = bridgeMethod;
                }

                protected TypeWriter.MethodPool.Record bind(TypeDescription instrumentedType, boolean supportsBridges) {
                    if (this.bridgeMethod && !supportsBridges) {
                        return new TypeWriter.MethodPool.Record.ForNonImplementedMethod(this.methodDescription);
                    }
                    TypeWriter.MethodPool.Record record = this.handler.assemble(this.methodDescription, this.attributeAppender, this.visibility);
                    return supportsBridges ? TypeWriter.MethodPool.Record.AccessBridgeWrapper.of(record, instrumentedType, this.methodDescription, this.bridgeTypes, this.attributeAppender) : record;
                }
            }
        }
    }
}
