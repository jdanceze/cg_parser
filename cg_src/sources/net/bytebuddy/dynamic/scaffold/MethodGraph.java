package net.bytebuddy.dynamic.scaffold;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.matcher.FilterableList;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodGraph.class */
public interface MethodGraph {
    Node locate(MethodDescription.SignatureToken signatureToken);

    NodeList listNodes();

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodGraph$Empty.class */
    public enum Empty implements Linked, Compiler {
        INSTANCE;

        @Override // net.bytebuddy.dynamic.scaffold.MethodGraph
        public Node locate(MethodDescription.SignatureToken token) {
            return Node.Unresolved.INSTANCE;
        }

        @Override // net.bytebuddy.dynamic.scaffold.MethodGraph
        public NodeList listNodes() {
            return new NodeList(Collections.emptyList());
        }

        @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Linked
        public MethodGraph getSuperClassGraph() {
            return this;
        }

        @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Linked
        public MethodGraph getInterfaceGraph(TypeDescription typeDescription) {
            return this;
        }

        @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Compiler
        public Linked compile(TypeDescription typeDescription) {
            return this;
        }

        @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Compiler
        public Linked compile(TypeDefinition typeDefinition, TypeDescription viewPoint) {
            return this;
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodGraph$Linked.class */
    public interface Linked extends MethodGraph {
        MethodGraph getSuperClassGraph();

        MethodGraph getInterfaceGraph(TypeDescription typeDescription);

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodGraph$Linked$Delegation.class */
        public static class Delegation implements Linked {
            private final MethodGraph methodGraph;
            private final MethodGraph superClassGraph;
            private final Map<TypeDescription, MethodGraph> interfaceGraphs;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.methodGraph.equals(((Delegation) obj).methodGraph) && this.superClassGraph.equals(((Delegation) obj).superClassGraph) && this.interfaceGraphs.equals(((Delegation) obj).interfaceGraphs);
            }

            public int hashCode() {
                return (((((17 * 31) + this.methodGraph.hashCode()) * 31) + this.superClassGraph.hashCode()) * 31) + this.interfaceGraphs.hashCode();
            }

            public Delegation(MethodGraph methodGraph, MethodGraph superClassGraph, Map<TypeDescription, MethodGraph> interfaceGraphs) {
                this.methodGraph = methodGraph;
                this.superClassGraph = superClassGraph;
                this.interfaceGraphs = interfaceGraphs;
            }

            @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Linked
            public MethodGraph getSuperClassGraph() {
                return this.superClassGraph;
            }

            @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Linked
            public MethodGraph getInterfaceGraph(TypeDescription typeDescription) {
                MethodGraph interfaceGraph = this.interfaceGraphs.get(typeDescription);
                return interfaceGraph == null ? Empty.INSTANCE : interfaceGraph;
            }

            @Override // net.bytebuddy.dynamic.scaffold.MethodGraph
            public Node locate(MethodDescription.SignatureToken token) {
                return this.methodGraph.locate(token);
            }

            @Override // net.bytebuddy.dynamic.scaffold.MethodGraph
            public NodeList listNodes() {
                return this.methodGraph.listNodes();
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodGraph$Node.class */
    public interface Node {
        Sort getSort();

        MethodDescription getRepresentative();

        Set<MethodDescription.TypeToken> getMethodTypes();

        Visibility getVisibility();

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodGraph$Node$Sort.class */
        public enum Sort {
            VISIBLE(true, true, true),
            RESOLVED(true, true, false),
            AMBIGUOUS(true, false, false),
            UNRESOLVED(false, false, false);
            
            private final boolean resolved;
            private final boolean unique;
            private final boolean madeVisible;

            Sort(boolean resolved, boolean unique, boolean madeVisible) {
                this.resolved = resolved;
                this.unique = unique;
                this.madeVisible = madeVisible;
            }

            public boolean isResolved() {
                return this.resolved;
            }

            public boolean isUnique() {
                return this.unique;
            }

            public boolean isMadeVisible() {
                return this.madeVisible;
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodGraph$Node$Unresolved.class */
        public enum Unresolved implements Node {
            INSTANCE;

            @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Node
            public Sort getSort() {
                return Sort.UNRESOLVED;
            }

            @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Node
            public MethodDescription getRepresentative() {
                throw new IllegalStateException("Cannot resolve the method of an illegal node");
            }

            @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Node
            public Set<MethodDescription.TypeToken> getMethodTypes() {
                throw new IllegalStateException("Cannot resolve bridge method of an illegal node");
            }

            @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Node
            public Visibility getVisibility() {
                throw new IllegalStateException("Cannot resolve visibility of an illegal node");
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodGraph$Node$Simple.class */
        public static class Simple implements Node {
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

            @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Node
            public Sort getSort() {
                return Sort.RESOLVED;
            }

            @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Node
            public MethodDescription getRepresentative() {
                return this.methodDescription;
            }

            @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Node
            public Set<MethodDescription.TypeToken> getMethodTypes() {
                return Collections.emptySet();
            }

            @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Node
            public Visibility getVisibility() {
                return this.methodDescription.getVisibility();
            }
        }
    }

    @SuppressFBWarnings(value = {"IC_SUPERCLASS_USES_SUBCLASS_DURING_INITIALIZATION"}, justification = "Safe initialization is implied")
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodGraph$Compiler.class */
    public interface Compiler {
        public static final Compiler DEFAULT = Default.forJavaHierarchy();

        Linked compile(TypeDescription typeDescription);

        Linked compile(TypeDefinition typeDefinition, TypeDescription typeDescription);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodGraph$Compiler$ForDeclaredMethods.class */
        public enum ForDeclaredMethods implements Compiler {
            INSTANCE;

            @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Compiler
            public Linked compile(TypeDescription typeDescription) {
                return compile(typeDescription, typeDescription);
            }

            @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Compiler
            public Linked compile(TypeDefinition typeDefinition, TypeDescription viewPoint) {
                LinkedHashMap<MethodDescription.SignatureToken, Node> nodes = new LinkedHashMap<>();
                for (MethodDescription methodDescription : typeDefinition.getDeclaredMethods().filter(ElementMatchers.isVirtual().and(ElementMatchers.not(ElementMatchers.isBridge())).and(ElementMatchers.isVisibleTo(viewPoint)))) {
                    nodes.put(methodDescription.asSignatureToken(), new Node.Simple(methodDescription));
                }
                return new Linked.Delegation(new Simple(nodes), Empty.INSTANCE, Collections.emptyMap());
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodGraph$Compiler$AbstractBase.class */
        public static abstract class AbstractBase implements Compiler {
            @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Compiler
            public Linked compile(TypeDescription typeDescription) {
                return compile(typeDescription, typeDescription);
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodGraph$Compiler$Default.class */
        public static class Default<T> extends AbstractBase {
            private final Harmonizer<T> harmonizer;
            private final Merger merger;
            private final TypeDescription.Generic.Visitor<? extends TypeDescription.Generic> visitor;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.harmonizer.equals(((Default) obj).harmonizer) && this.merger.equals(((Default) obj).merger) && this.visitor.equals(((Default) obj).visitor);
            }

            public int hashCode() {
                return (((((17 * 31) + this.harmonizer.hashCode()) * 31) + this.merger.hashCode()) * 31) + this.visitor.hashCode();
            }

            protected Default(Harmonizer<T> harmonizer, Merger merger, TypeDescription.Generic.Visitor<? extends TypeDescription.Generic> visitor) {
                this.harmonizer = harmonizer;
                this.merger = merger;
                this.visitor = visitor;
            }

            public static <S> Compiler of(Harmonizer<S> harmonizer, Merger merger) {
                return new Default(harmonizer, merger, TypeDescription.Generic.Visitor.Reifying.INITIATING);
            }

            public static <S> Compiler of(Harmonizer<S> harmonizer, Merger merger, TypeDescription.Generic.Visitor<? extends TypeDescription.Generic> visitor) {
                return new Default(harmonizer, merger, visitor);
            }

            public static Compiler forJavaHierarchy() {
                return of(Harmonizer.ForJavaMethod.INSTANCE, Merger.Directional.LEFT);
            }

            public static Compiler forJVMHierarchy() {
                return of(Harmonizer.ForJVMMethod.INSTANCE, Merger.Directional.LEFT);
            }

            @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Compiler
            public Linked compile(TypeDefinition typeDefinition, TypeDescription viewPoint) {
                Map<TypeDefinition, Key.Store<T>> snapshots = new HashMap<>();
                Key.Store<T> doAnalyze = doAnalyze(typeDefinition, snapshots, ElementMatchers.isVirtual().and(ElementMatchers.isVisibleTo(viewPoint)));
                TypeDescription.Generic superClass = typeDefinition.getSuperClass();
                List<TypeDescription.Generic> interfaceTypes = typeDefinition.getInterfaces();
                Map<TypeDescription, MethodGraph> interfaceGraphs = new HashMap<>();
                for (TypeDescription.Generic interfaceType : interfaceTypes) {
                    interfaceGraphs.put(interfaceType.asErasure(), snapshots.get(interfaceType).asGraph(this.merger));
                }
                return new Linked.Delegation(doAnalyze.asGraph(this.merger), superClass == null ? Empty.INSTANCE : snapshots.get(superClass).asGraph(this.merger), interfaceGraphs);
            }

            protected Key.Store<T> analyze(TypeDefinition typeDefinition, TypeDefinition key, Map<TypeDefinition, Key.Store<T>> snapshots, ElementMatcher<? super MethodDescription> relevanceMatcher) {
                Key.Store<T> store = snapshots.get(key);
                if (store == null) {
                    store = doAnalyze(typeDefinition, snapshots, relevanceMatcher);
                    snapshots.put(key, store);
                }
                return store;
            }

            protected Key.Store<T> analyzeNullable(TypeDescription.Generic typeDescription, Map<TypeDefinition, Key.Store<T>> snapshots, ElementMatcher<? super MethodDescription> relevanceMatcher) {
                return typeDescription == null ? new Key.Store<>() : analyze((TypeDefinition) typeDescription.accept(this.visitor), typeDescription, snapshots, relevanceMatcher);
            }

            /* JADX WARN: Multi-variable type inference failed */
            protected Key.Store<T> doAnalyze(TypeDefinition typeDefinition, Map<TypeDefinition, Key.Store<T>> snapshots, ElementMatcher<? super MethodDescription> relevanceMatcher) {
                Key.Store<T> store = analyzeNullable(typeDefinition.getSuperClass(), snapshots, relevanceMatcher);
                Key.Store<T> interfaceStore = new Key.Store<>();
                for (TypeDescription.Generic interfaceType : typeDefinition.getInterfaces()) {
                    interfaceStore = interfaceStore.combineWith(analyze((TypeDefinition) interfaceType.accept(this.visitor), interfaceType, snapshots, relevanceMatcher));
                }
                return store.inject(interfaceStore).registerTopLevel(typeDefinition.getDeclaredMethods().filter(relevanceMatcher), this.harmonizer);
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodGraph$Compiler$Default$Harmonizer.class */
            public interface Harmonizer<S> {
                S harmonize(MethodDescription.TypeToken typeToken);

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodGraph$Compiler$Default$Harmonizer$ForJavaMethod.class */
                public enum ForJavaMethod implements Harmonizer<Token> {
                    INSTANCE;

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Compiler.Default.Harmonizer
                    public Token harmonize(MethodDescription.TypeToken typeToken) {
                        return new Token(typeToken);
                    }

                    /* JADX INFO: Access modifiers changed from: protected */
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodGraph$Compiler$Default$Harmonizer$ForJavaMethod$Token.class */
                    public static class Token {
                        private final MethodDescription.TypeToken typeToken;
                        private final int hashCode;

                        protected Token(MethodDescription.TypeToken typeToken) {
                            this.typeToken = typeToken;
                            this.hashCode = typeToken.getParameterTypes().hashCode();
                        }

                        public int hashCode() {
                            return this.hashCode;
                        }

                        public boolean equals(Object other) {
                            return this == other || ((other instanceof Token) && this.typeToken.getParameterTypes().equals(((Token) other).typeToken.getParameterTypes()));
                        }

                        public String toString() {
                            return this.typeToken.getParameterTypes().toString();
                        }
                    }
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodGraph$Compiler$Default$Harmonizer$ForJVMMethod.class */
                public enum ForJVMMethod implements Harmonizer<Token> {
                    INSTANCE;

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Compiler.Default.Harmonizer
                    public Token harmonize(MethodDescription.TypeToken typeToken) {
                        return new Token(typeToken);
                    }

                    /* JADX INFO: Access modifiers changed from: protected */
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodGraph$Compiler$Default$Harmonizer$ForJVMMethod$Token.class */
                    public static class Token {
                        private final MethodDescription.TypeToken typeToken;
                        private final int hashCode;

                        public Token(MethodDescription.TypeToken typeToken) {
                            this.typeToken = typeToken;
                            this.hashCode = typeToken.getReturnType().hashCode() + (31 * typeToken.getParameterTypes().hashCode());
                        }

                        public int hashCode() {
                            return this.hashCode;
                        }

                        public boolean equals(Object other) {
                            if (this == other) {
                                return true;
                            }
                            if (!(other instanceof Token)) {
                                return false;
                            }
                            Token token = (Token) other;
                            return this.typeToken.getReturnType().equals(token.typeToken.getReturnType()) && this.typeToken.getParameterTypes().equals(token.typeToken.getParameterTypes());
                        }

                        public String toString() {
                            return this.typeToken.toString();
                        }
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodGraph$Compiler$Default$Merger.class */
            public interface Merger {
                MethodDescription merge(MethodDescription methodDescription, MethodDescription methodDescription2);

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodGraph$Compiler$Default$Merger$Directional.class */
                public enum Directional implements Merger {
                    LEFT(true),
                    RIGHT(false);
                    
                    private final boolean left;

                    Directional(boolean left) {
                        this.left = left;
                    }

                    @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Compiler.Default.Merger
                    public MethodDescription merge(MethodDescription left, MethodDescription right) {
                        return this.left ? left : right;
                    }
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodGraph$Compiler$Default$Key.class */
            public static abstract class Key<S> {
                protected final String internalName;
                protected final int parameterCount;

                protected abstract Set<S> getIdentifiers();

                protected Key(String internalName, int parameterCount) {
                    this.internalName = internalName;
                    this.parameterCount = parameterCount;
                }

                public int hashCode() {
                    return this.internalName.hashCode() + (31 * this.parameterCount);
                }

                public boolean equals(Object other) {
                    if (this == other) {
                        return true;
                    }
                    if (!(other instanceof Key)) {
                        return false;
                    }
                    Key key = (Key) other;
                    return this.internalName.equals(key.internalName) && this.parameterCount == key.parameterCount && !Collections.disjoint(getIdentifiers(), key.getIdentifiers());
                }

                /* JADX INFO: Access modifiers changed from: protected */
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodGraph$Compiler$Default$Key$Harmonized.class */
                public static class Harmonized<V> extends Key<V> {
                    private final Map<V, Set<MethodDescription.TypeToken>> identifiers;

                    protected Harmonized(String internalName, int parameterCount, Map<V, Set<MethodDescription.TypeToken>> identifiers) {
                        super(internalName, parameterCount);
                        this.identifiers = identifiers;
                    }

                    protected static <Q> Harmonized<Q> of(MethodDescription methodDescription, Harmonizer<Q> harmonizer) {
                        MethodDescription.TypeToken typeToken = methodDescription.asTypeToken();
                        return new Harmonized<>(methodDescription.getInternalName(), methodDescription.getParameters().size(), Collections.singletonMap(harmonizer.harmonize(typeToken), Collections.emptySet()));
                    }

                    protected Detached detach(MethodDescription.TypeToken typeToken) {
                        Set<MethodDescription.TypeToken> identifiers = new HashSet<>();
                        for (Set<MethodDescription.TypeToken> typeTokens : this.identifiers.values()) {
                            identifiers.addAll(typeTokens);
                        }
                        identifiers.add(typeToken);
                        return new Detached(this.internalName, this.parameterCount, identifiers);
                    }

                    protected Harmonized<V> combineWith(Harmonized<V> key) {
                        Map<V, Set<MethodDescription.TypeToken>> identifiers = new HashMap<>((Map<? extends V, ? extends Set<MethodDescription.TypeToken>>) this.identifiers);
                        for (Map.Entry<V, Set<MethodDescription.TypeToken>> entry : key.identifiers.entrySet()) {
                            Set<MethodDescription.TypeToken> typeTokens = identifiers.get(entry.getKey());
                            if (typeTokens == null) {
                                identifiers.put(entry.getKey(), entry.getValue());
                            } else {
                                Set<MethodDescription.TypeToken> typeTokens2 = new HashSet<>(typeTokens);
                                typeTokens2.addAll(entry.getValue());
                                identifiers.put(entry.getKey(), typeTokens2);
                            }
                        }
                        return new Harmonized<>(this.internalName, this.parameterCount, identifiers);
                    }

                    protected Harmonized<V> extend(MethodDescription.InDefinedShape methodDescription, Harmonizer<V> harmonizer) {
                        Map<V, Set<MethodDescription.TypeToken>> identifiers = new HashMap<>((Map<? extends V, ? extends Set<MethodDescription.TypeToken>>) this.identifiers);
                        MethodDescription.TypeToken typeToken = methodDescription.asTypeToken();
                        V identifier = harmonizer.harmonize(typeToken);
                        Set<MethodDescription.TypeToken> typeTokens = identifiers.get(identifier);
                        if (typeTokens == null) {
                            identifiers.put(identifier, Collections.singleton(typeToken));
                        } else {
                            Set<MethodDescription.TypeToken> typeTokens2 = new HashSet<>(typeTokens);
                            typeTokens2.add(typeToken);
                            identifiers.put(identifier, typeTokens2);
                        }
                        return new Harmonized<>(this.internalName, this.parameterCount, identifiers);
                    }

                    @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Compiler.Default.Key
                    protected Set<V> getIdentifiers() {
                        return this.identifiers.keySet();
                    }
                }

                /* JADX INFO: Access modifiers changed from: protected */
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodGraph$Compiler$Default$Key$Detached.class */
                public static class Detached extends Key<MethodDescription.TypeToken> {
                    private final Set<MethodDescription.TypeToken> identifiers;

                    protected Detached(String internalName, int parameterCount, Set<MethodDescription.TypeToken> identifiers) {
                        super(internalName, parameterCount);
                        this.identifiers = identifiers;
                    }

                    protected static Detached of(MethodDescription.SignatureToken token) {
                        return new Detached(token.getName(), token.getParameterTypes().size(), Collections.singleton(token.asTypeToken()));
                    }

                    @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Compiler.Default.Key
                    protected Set<MethodDescription.TypeToken> getIdentifiers() {
                        return this.identifiers;
                    }
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodGraph$Compiler$Default$Key$Store.class */
                public static class Store<V> {
                    private final LinkedHashMap<Harmonized<V>, Entry<V>> entries;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.entries.equals(((Store) obj).entries);
                    }

                    public int hashCode() {
                        return (17 * 31) + this.entries.hashCode();
                    }

                    protected Store() {
                        this(new LinkedHashMap());
                    }

                    private Store(LinkedHashMap<Harmonized<V>, Entry<V>> entries) {
                        this.entries = entries;
                    }

                    private static <W> Entry<W> combine(Entry<W> left, Entry<W> right) {
                        Set<MethodDescription> leftMethods = left.getCandidates();
                        Set<MethodDescription> rightMethods = right.getCandidates();
                        LinkedHashSet<MethodDescription> combined = new LinkedHashSet<>();
                        combined.addAll(leftMethods);
                        combined.addAll(rightMethods);
                        for (MethodDescription leftMethod : leftMethods) {
                            TypeDescription leftType = leftMethod.getDeclaringType().asErasure();
                            Iterator<MethodDescription> it = rightMethods.iterator();
                            while (true) {
                                if (it.hasNext()) {
                                    MethodDescription rightMethod = it.next();
                                    TypeDescription rightType = rightMethod.getDeclaringType().asErasure();
                                    if (!leftType.equals(rightType)) {
                                        if (leftType.isAssignableTo(rightType)) {
                                            combined.remove(rightMethod);
                                            break;
                                        } else if (leftType.isAssignableFrom(rightType)) {
                                            combined.remove(leftMethod);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        Harmonized<W> key = left.getKey().combineWith(right.getKey());
                        Visibility visibility = left.getVisibility().expandTo(right.getVisibility());
                        return combined.size() == 1 ? new Entry.Resolved(key, combined.iterator().next(), visibility, false) : new Entry.Ambiguous(key, combined, visibility);
                    }

                    protected Store<V> registerTopLevel(List<? extends MethodDescription> methodDescriptions, Harmonizer<V> harmonizer) {
                        if (methodDescriptions.isEmpty()) {
                            return this;
                        }
                        LinkedHashMap<Harmonized<V>, Entry<V>> entries = new LinkedHashMap<>(this.entries);
                        for (MethodDescription methodDescription : methodDescriptions) {
                            Harmonized<V> key = Harmonized.of(methodDescription, harmonizer);
                            Entry<V> currentEntry = entries.remove(key);
                            Entry<V> extendedEntry = (currentEntry == null ? new Entry.Initial<>(key) : currentEntry).extendBy(methodDescription, harmonizer);
                            entries.put(extendedEntry.getKey(), extendedEntry);
                        }
                        return new Store<>(entries);
                    }

                    protected Store<V> combineWith(Store<V> store) {
                        if (this.entries.isEmpty()) {
                            return store;
                        }
                        if (store.entries.isEmpty()) {
                            return this;
                        }
                        LinkedHashMap<Harmonized<V>, Entry<V>> entries = new LinkedHashMap<>(this.entries);
                        for (Entry<V> entry : store.entries.values()) {
                            Entry<V> previousEntry = entries.remove(entry.getKey());
                            Entry<V> injectedEntry = previousEntry == null ? entry : combine(previousEntry, entry);
                            entries.put(injectedEntry.getKey(), injectedEntry);
                        }
                        return new Store<>(entries);
                    }

                    protected Store<V> inject(Store<V> store) {
                        if (this.entries.isEmpty()) {
                            return store;
                        }
                        if (store.entries.isEmpty()) {
                            return this;
                        }
                        LinkedHashMap<Harmonized<V>, Entry<V>> entries = new LinkedHashMap<>(this.entries);
                        for (Entry<V> entry : store.entries.values()) {
                            Entry<V> dominantEntry = entries.remove(entry.getKey());
                            Entry<V> injectedEntry = dominantEntry == null ? entry : dominantEntry.inject(entry.getKey(), entry.getVisibility());
                            entries.put(injectedEntry.getKey(), injectedEntry);
                        }
                        return new Store<>(entries);
                    }

                    protected MethodGraph asGraph(Merger merger) {
                        LinkedHashMap<Key<MethodDescription.TypeToken>, Node> entries = new LinkedHashMap<>();
                        for (Entry<V> entry : this.entries.values()) {
                            Node node = entry.asNode(merger);
                            entries.put(entry.getKey().detach(node.getRepresentative().asTypeToken()), node);
                        }
                        return new Graph(entries);
                    }

                    /* JADX INFO: Access modifiers changed from: protected */
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodGraph$Compiler$Default$Key$Store$Entry.class */
                    public interface Entry<W> {
                        Harmonized<W> getKey();

                        Set<MethodDescription> getCandidates();

                        Visibility getVisibility();

                        Entry<W> extendBy(MethodDescription methodDescription, Harmonizer<W> harmonizer);

                        Entry<W> inject(Harmonized<W> harmonized, Visibility visibility);

                        Node asNode(Merger merger);

                        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodGraph$Compiler$Default$Key$Store$Entry$Initial.class */
                        public static class Initial<U> implements Entry<U> {
                            private final Harmonized<U> key;

                            protected Initial(Harmonized<U> key) {
                                this.key = key;
                            }

                            @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Compiler.Default.Key.Store.Entry
                            public Harmonized<U> getKey() {
                                throw new IllegalStateException("Cannot extract key from initial entry:" + this);
                            }

                            @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Compiler.Default.Key.Store.Entry
                            public Set<MethodDescription> getCandidates() {
                                throw new IllegalStateException("Cannot extract method from initial entry:" + this);
                            }

                            @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Compiler.Default.Key.Store.Entry
                            public Visibility getVisibility() {
                                throw new IllegalStateException("Cannot extract visibility from initial entry:" + this);
                            }

                            @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Compiler.Default.Key.Store.Entry
                            public Entry<U> extendBy(MethodDescription methodDescription, Harmonizer<U> harmonizer) {
                                return new Resolved(this.key.extend(methodDescription.asDefined(), harmonizer), methodDescription, methodDescription.getVisibility(), false);
                            }

                            @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Compiler.Default.Key.Store.Entry
                            public Entry<U> inject(Harmonized<U> key, Visibility visibility) {
                                throw new IllegalStateException("Cannot inject into initial entry without a registered method: " + this);
                            }

                            @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Compiler.Default.Key.Store.Entry
                            public Node asNode(Merger merger) {
                                throw new IllegalStateException("Cannot transform initial entry without a registered method: " + this);
                            }

                            public int hashCode() {
                                return this.key.hashCode();
                            }

                            public boolean equals(Object other) {
                                if (this == other) {
                                    return true;
                                }
                                if (other == null || getClass() != other.getClass()) {
                                    return false;
                                }
                                Initial<?> initial = (Initial) other;
                                return this.key.equals(initial.key);
                            }
                        }

                        @HashCodeAndEqualsPlugin.Enhance
                        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodGraph$Compiler$Default$Key$Store$Entry$Resolved.class */
                        public static class Resolved<U> implements Entry<U> {
                            private static final int MADE_VISIBLE = 5;
                            private static final boolean NOT_MADE_VISIBLE = false;
                            private final Harmonized<U> key;
                            private final MethodDescription methodDescription;
                            private final Visibility visibility;
                            private final boolean madeVisible;

                            public boolean equals(Object obj) {
                                if (this == obj) {
                                    return true;
                                }
                                return obj != null && getClass() == obj.getClass() && this.madeVisible == ((Resolved) obj).madeVisible && this.visibility.equals(((Resolved) obj).visibility) && this.key.equals(((Resolved) obj).key) && this.methodDescription.equals(((Resolved) obj).methodDescription);
                            }

                            public int hashCode() {
                                return (((((((17 * 31) + this.key.hashCode()) * 31) + this.methodDescription.hashCode()) * 31) + this.visibility.hashCode()) * 31) + (this.madeVisible ? 1 : 0);
                            }

                            protected Resolved(Harmonized<U> key, MethodDescription methodDescription, Visibility visibility, boolean madeVisible) {
                                this.key = key;
                                this.methodDescription = methodDescription;
                                this.visibility = visibility;
                                this.madeVisible = madeVisible;
                            }

                            private static <V> Entry<V> of(Harmonized<V> key, MethodDescription override, MethodDescription original, Visibility visibility) {
                                Visibility visibility2 = visibility.expandTo(original.getVisibility()).expandTo(override.getVisibility());
                                if (override.isBridge()) {
                                    return new Resolved(key, original, visibility2, (original.getDeclaringType().getModifiers() & 5) == 0);
                                }
                                return new Resolved(key, override, visibility2, false);
                            }

                            @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Compiler.Default.Key.Store.Entry
                            public Harmonized<U> getKey() {
                                return this.key;
                            }

                            @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Compiler.Default.Key.Store.Entry
                            public Set<MethodDescription> getCandidates() {
                                return Collections.singleton(this.methodDescription);
                            }

                            @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Compiler.Default.Key.Store.Entry
                            public Visibility getVisibility() {
                                return this.visibility;
                            }

                            @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Compiler.Default.Key.Store.Entry
                            public Entry<U> extendBy(MethodDescription methodDescription, Harmonizer<U> harmonizer) {
                                Harmonized<U> key = this.key.extend(methodDescription.asDefined(), harmonizer);
                                Visibility visibility = this.visibility.expandTo(methodDescription.getVisibility());
                                if (methodDescription.getDeclaringType().equals(this.methodDescription.getDeclaringType())) {
                                    return Ambiguous.of(key, methodDescription, this.methodDescription, visibility);
                                }
                                return of(key, methodDescription, this.methodDescription, visibility);
                            }

                            @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Compiler.Default.Key.Store.Entry
                            public Entry<U> inject(Harmonized<U> key, Visibility visibility) {
                                return new Resolved(this.key.combineWith(key), this.methodDescription, this.visibility.expandTo(visibility), this.madeVisible);
                            }

                            @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Compiler.Default.Key.Store.Entry
                            public Node asNode(Merger merger) {
                                return new Node(this.key.detach(this.methodDescription.asTypeToken()), this.methodDescription, this.visibility, this.madeVisible);
                            }

                            @HashCodeAndEqualsPlugin.Enhance
                            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodGraph$Compiler$Default$Key$Store$Entry$Resolved$Node.class */
                            protected static class Node implements Node {
                                private final Detached key;
                                private final MethodDescription methodDescription;
                                private final Visibility visibility;
                                private final boolean visible;

                                public boolean equals(Object obj) {
                                    if (this == obj) {
                                        return true;
                                    }
                                    return obj != null && getClass() == obj.getClass() && this.visible == ((Node) obj).visible && this.visibility.equals(((Node) obj).visibility) && this.key.equals(((Node) obj).key) && this.methodDescription.equals(((Node) obj).methodDescription);
                                }

                                public int hashCode() {
                                    return (((((((17 * 31) + this.key.hashCode()) * 31) + this.methodDescription.hashCode()) * 31) + this.visibility.hashCode()) * 31) + (this.visible ? 1 : 0);
                                }

                                protected Node(Detached key, MethodDescription methodDescription, Visibility visibility, boolean visible) {
                                    this.key = key;
                                    this.methodDescription = methodDescription;
                                    this.visibility = visibility;
                                    this.visible = visible;
                                }

                                @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Node
                                public Node.Sort getSort() {
                                    return this.visible ? Node.Sort.VISIBLE : Node.Sort.RESOLVED;
                                }

                                @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Node
                                public MethodDescription getRepresentative() {
                                    return this.methodDescription;
                                }

                                @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Node
                                public Set<MethodDescription.TypeToken> getMethodTypes() {
                                    return this.key.getIdentifiers();
                                }

                                @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Node
                                public Visibility getVisibility() {
                                    return this.visibility;
                                }
                            }
                        }

                        @HashCodeAndEqualsPlugin.Enhance
                        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodGraph$Compiler$Default$Key$Store$Entry$Ambiguous.class */
                        public static class Ambiguous<U> implements Entry<U> {
                            private final Harmonized<U> key;
                            private final LinkedHashSet<MethodDescription> methodDescriptions;
                            private final Visibility visibility;

                            public boolean equals(Object obj) {
                                if (this == obj) {
                                    return true;
                                }
                                return obj != null && getClass() == obj.getClass() && this.visibility.equals(((Ambiguous) obj).visibility) && this.key.equals(((Ambiguous) obj).key) && this.methodDescriptions.equals(((Ambiguous) obj).methodDescriptions);
                            }

                            public int hashCode() {
                                return (((((17 * 31) + this.key.hashCode()) * 31) + this.methodDescriptions.hashCode()) * 31) + this.visibility.hashCode();
                            }

                            protected Ambiguous(Harmonized<U> key, LinkedHashSet<MethodDescription> methodDescriptions, Visibility visibility) {
                                this.key = key;
                                this.methodDescriptions = methodDescriptions;
                                this.visibility = visibility;
                            }

                            protected static <Q> Entry<Q> of(Harmonized<Q> key, MethodDescription left, MethodDescription right, Visibility visibility) {
                                Visibility visibility2 = visibility.expandTo(left.getVisibility()).expandTo(right.getVisibility());
                                if (left.isBridge() ^ right.isBridge()) {
                                    return new Resolved(key, left.isBridge() ? right : left, visibility2, false);
                                }
                                return new Ambiguous(key, new LinkedHashSet(Arrays.asList(left, right)), visibility2);
                            }

                            @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Compiler.Default.Key.Store.Entry
                            public Harmonized<U> getKey() {
                                return this.key;
                            }

                            @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Compiler.Default.Key.Store.Entry
                            public Set<MethodDescription> getCandidates() {
                                return this.methodDescriptions;
                            }

                            @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Compiler.Default.Key.Store.Entry
                            public Visibility getVisibility() {
                                return this.visibility;
                            }

                            @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Compiler.Default.Key.Store.Entry
                            public Entry<U> extendBy(MethodDescription methodDescription, Harmonizer<U> harmonizer) {
                                Harmonized<U> key = this.key.extend(methodDescription.asDefined(), harmonizer);
                                LinkedHashSet<MethodDescription> methodDescriptions = new LinkedHashSet<>();
                                TypeDescription declaringType = methodDescription.getDeclaringType().asErasure();
                                boolean bridge = methodDescription.isBridge();
                                Visibility visibility = this.visibility;
                                Iterator<MethodDescription> it = this.methodDescriptions.iterator();
                                while (it.hasNext()) {
                                    MethodDescription extendedMethod = it.next();
                                    if (extendedMethod.getDeclaringType().asErasure().equals(declaringType)) {
                                        if (extendedMethod.isBridge() ^ bridge) {
                                            methodDescriptions.add(bridge ? extendedMethod : methodDescription);
                                        } else {
                                            methodDescriptions.add(methodDescription);
                                            methodDescriptions.add(extendedMethod);
                                        }
                                    }
                                    visibility = visibility.expandTo(extendedMethod.getVisibility());
                                }
                                if (methodDescriptions.isEmpty()) {
                                    return new Resolved(key, methodDescription, visibility, bridge);
                                }
                                if (methodDescriptions.size() == 1) {
                                    return new Resolved(key, methodDescriptions.iterator().next(), visibility, false);
                                }
                                return new Ambiguous(key, methodDescriptions, visibility);
                            }

                            @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Compiler.Default.Key.Store.Entry
                            public Entry<U> inject(Harmonized<U> key, Visibility visibility) {
                                return new Ambiguous(this.key.combineWith(key), this.methodDescriptions, this.visibility.expandTo(visibility));
                            }

                            @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Compiler.Default.Key.Store.Entry
                            public Node asNode(Merger merger) {
                                Iterator<MethodDescription> iterator = this.methodDescriptions.iterator();
                                MethodDescription next = iterator.next();
                                while (true) {
                                    MethodDescription methodDescription = next;
                                    if (iterator.hasNext()) {
                                        next = merger.merge(methodDescription, iterator.next());
                                    } else {
                                        return new Node(this.key.detach(methodDescription.asTypeToken()), methodDescription, this.visibility);
                                    }
                                }
                            }

                            @HashCodeAndEqualsPlugin.Enhance
                            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodGraph$Compiler$Default$Key$Store$Entry$Ambiguous$Node.class */
                            protected static class Node implements Node {
                                private final Detached key;
                                private final MethodDescription methodDescription;
                                private final Visibility visibility;

                                public boolean equals(Object obj) {
                                    if (this == obj) {
                                        return true;
                                    }
                                    return obj != null && getClass() == obj.getClass() && this.visibility.equals(((Node) obj).visibility) && this.key.equals(((Node) obj).key) && this.methodDescription.equals(((Node) obj).methodDescription);
                                }

                                public int hashCode() {
                                    return (((((17 * 31) + this.key.hashCode()) * 31) + this.methodDescription.hashCode()) * 31) + this.visibility.hashCode();
                                }

                                protected Node(Detached key, MethodDescription methodDescription, Visibility visibility) {
                                    this.key = key;
                                    this.methodDescription = methodDescription;
                                    this.visibility = visibility;
                                }

                                @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Node
                                public Node.Sort getSort() {
                                    return Node.Sort.AMBIGUOUS;
                                }

                                @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Node
                                public MethodDescription getRepresentative() {
                                    return this.methodDescription;
                                }

                                @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Node
                                public Set<MethodDescription.TypeToken> getMethodTypes() {
                                    return this.key.getIdentifiers();
                                }

                                @Override // net.bytebuddy.dynamic.scaffold.MethodGraph.Node
                                public Visibility getVisibility() {
                                    return this.visibility;
                                }
                            }
                        }
                    }

                    /* JADX INFO: Access modifiers changed from: protected */
                    @HashCodeAndEqualsPlugin.Enhance
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodGraph$Compiler$Default$Key$Store$Graph.class */
                    public static class Graph implements MethodGraph {
                        private final LinkedHashMap<Key<MethodDescription.TypeToken>, Node> entries;

                        public boolean equals(Object obj) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.entries.equals(((Graph) obj).entries);
                        }

                        public int hashCode() {
                            return (17 * 31) + this.entries.hashCode();
                        }

                        protected Graph(LinkedHashMap<Key<MethodDescription.TypeToken>, Node> entries) {
                            this.entries = entries;
                        }

                        @Override // net.bytebuddy.dynamic.scaffold.MethodGraph
                        public Node locate(MethodDescription.SignatureToken token) {
                            Node node = this.entries.get(Detached.of(token));
                            return node == null ? Node.Unresolved.INSTANCE : node;
                        }

                        @Override // net.bytebuddy.dynamic.scaffold.MethodGraph
                        public NodeList listNodes() {
                            return new NodeList(new ArrayList(this.entries.values()));
                        }
                    }
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodGraph$NodeList.class */
    public static class NodeList extends FilterableList.AbstractBase<Node, NodeList> {
        private final List<? extends Node> nodes;

        public NodeList(List<? extends Node> nodes) {
            this.nodes = nodes;
        }

        @Override // java.util.AbstractList, java.util.List
        public Node get(int index) {
            return this.nodes.get(index);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.nodes.size();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // net.bytebuddy.matcher.FilterableList.AbstractBase
        public NodeList wrap(List<Node> values) {
            return new NodeList(values);
        }

        public MethodList<?> asMethodList() {
            List<MethodDescription> methodDescriptions = new ArrayList<>(size());
            for (Node node : this.nodes) {
                methodDescriptions.add(node.getRepresentative());
            }
            return new MethodList.Explicit(methodDescriptions);
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/MethodGraph$Simple.class */
    public static class Simple implements MethodGraph {
        private final LinkedHashMap<MethodDescription.SignatureToken, Node> nodes;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.nodes.equals(((Simple) obj).nodes);
        }

        public int hashCode() {
            return (17 * 31) + this.nodes.hashCode();
        }

        public Simple(LinkedHashMap<MethodDescription.SignatureToken, Node> nodes) {
            this.nodes = nodes;
        }

        public static MethodGraph of(List<? extends MethodDescription> methodDescriptions) {
            LinkedHashMap<MethodDescription.SignatureToken, Node> nodes = new LinkedHashMap<>();
            for (MethodDescription methodDescription : methodDescriptions) {
                nodes.put(methodDescription.asSignatureToken(), new Node.Simple(methodDescription));
            }
            return new Simple(nodes);
        }

        @Override // net.bytebuddy.dynamic.scaffold.MethodGraph
        public Node locate(MethodDescription.SignatureToken token) {
            Node node = this.nodes.get(token);
            return node == null ? Node.Unresolved.INSTANCE : node;
        }

        @Override // net.bytebuddy.dynamic.scaffold.MethodGraph
        public NodeList listNodes() {
            return new NodeList(new ArrayList(this.nodes.values()));
        }
    }
}
