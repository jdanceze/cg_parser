package net.bytebuddy.description.type;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.bytebuddy.build.CachedReturnPlugin;
import net.bytebuddy.description.ByteCodeElement;
import net.bytebuddy.description.TypeVariableSource;
import net.bytebuddy.description.annotation.AnnotationList;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.bytecode.StackSize;
import net.bytebuddy.jar.asm.Type;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.FilterableList;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeList.class */
public interface TypeList extends FilterableList<TypeDescription, TypeList> {
    @SuppressFBWarnings(value = {"MS_MUTABLE_ARRAY", "MS_OOI_PKGPROTECT"}, justification = "Value is null")
    public static final String[] NO_INTERFACES = null;

    String[] toInternalNames();

    int getStackSize();

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeList$AbstractBase.class */
    public static abstract class AbstractBase extends FilterableList.AbstractBase<TypeDescription, TypeList> implements TypeList {
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // net.bytebuddy.matcher.FilterableList.AbstractBase
        public TypeList wrap(List<TypeDescription> values) {
            return new Explicit(values);
        }

        @Override // net.bytebuddy.description.type.TypeList
        public int getStackSize() {
            return StackSize.of(this);
        }

        @Override // net.bytebuddy.description.type.TypeList
        public String[] toInternalNames() {
            String[] internalNames = new String[size()];
            int i = 0;
            Iterator it = iterator();
            while (it.hasNext()) {
                TypeDescription typeDescription = (TypeDescription) it.next();
                int i2 = i;
                i++;
                internalNames[i2] = typeDescription.getInternalName();
            }
            return internalNames.length == 0 ? NO_INTERFACES : internalNames;
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeList$ForLoadedTypes.class */
    public static class ForLoadedTypes extends AbstractBase {
        private final List<? extends Class<?>> types;

        public ForLoadedTypes(Class<?>... type) {
            this(Arrays.asList(type));
        }

        public ForLoadedTypes(List<? extends Class<?>> types) {
            this.types = types;
        }

        @Override // java.util.AbstractList, java.util.List
        public TypeDescription get(int index) {
            return TypeDescription.ForLoadedType.of(this.types.get(index));
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.types.size();
        }

        @Override // net.bytebuddy.description.type.TypeList.AbstractBase, net.bytebuddy.description.type.TypeList
        public String[] toInternalNames() {
            String[] internalNames = new String[this.types.size()];
            int i = 0;
            for (Class<?> type : this.types) {
                int i2 = i;
                i++;
                internalNames[i2] = Type.getInternalName(type);
            }
            return internalNames.length == 0 ? NO_INTERFACES : internalNames;
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeList$Explicit.class */
    public static class Explicit extends AbstractBase {
        private final List<? extends TypeDescription> typeDescriptions;

        public Explicit(TypeDescription... typeDescription) {
            this(Arrays.asList(typeDescription));
        }

        public Explicit(List<? extends TypeDescription> typeDescriptions) {
            this.typeDescriptions = typeDescriptions;
        }

        @Override // java.util.AbstractList, java.util.List
        public TypeDescription get(int index) {
            return this.typeDescriptions.get(index);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.typeDescriptions.size();
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeList$Empty.class */
    public static class Empty extends FilterableList.Empty<TypeDescription, TypeList> implements TypeList {
        @Override // net.bytebuddy.description.type.TypeList
        @SuppressFBWarnings(value = {"EI_EXPOSE_REP"}, justification = "Value is null")
        public String[] toInternalNames() {
            return NO_INTERFACES;
        }

        @Override // net.bytebuddy.description.type.TypeList
        public int getStackSize() {
            return 0;
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeList$Generic.class */
    public interface Generic extends FilterableList<TypeDescription.Generic, Generic> {
        TypeList asErasures();

        Generic asRawTypes();

        ByteCodeElement.Token.TokenList<TypeVariableToken> asTokenList(ElementMatcher<? super TypeDescription> elementMatcher);

        Generic accept(TypeDescription.Generic.Visitor<? extends TypeDescription.Generic> visitor);

        int getStackSize();

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeList$Generic$AbstractBase.class */
        public static abstract class AbstractBase extends FilterableList.AbstractBase<TypeDescription.Generic, Generic> implements Generic {
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // net.bytebuddy.matcher.FilterableList.AbstractBase
            public Generic wrap(List<TypeDescription.Generic> values) {
                return new Explicit(values);
            }

            @Override // net.bytebuddy.description.type.TypeList.Generic
            public Generic accept(TypeDescription.Generic.Visitor<? extends TypeDescription.Generic> visitor) {
                ArrayList arrayList = new ArrayList(size());
                Iterator it = iterator();
                while (it.hasNext()) {
                    TypeDescription.Generic typeDescription = (TypeDescription.Generic) it.next();
                    arrayList.add(typeDescription.accept(visitor));
                }
                return new Explicit(arrayList);
            }

            @Override // net.bytebuddy.description.type.TypeList.Generic
            public ByteCodeElement.Token.TokenList<TypeVariableToken> asTokenList(ElementMatcher<? super TypeDescription> matcher) {
                List<TypeVariableToken> tokens = new ArrayList<>(size());
                Iterator it = iterator();
                while (it.hasNext()) {
                    TypeDescription.Generic typeVariable = (TypeDescription.Generic) it.next();
                    tokens.add(TypeVariableToken.of(typeVariable, matcher));
                }
                return new ByteCodeElement.Token.TokenList<>(tokens);
            }

            @Override // net.bytebuddy.description.type.TypeList.Generic
            public int getStackSize() {
                int stackSize = 0;
                Iterator it = iterator();
                while (it.hasNext()) {
                    TypeDescription.Generic typeDescription = (TypeDescription.Generic) it.next();
                    stackSize += typeDescription.getStackSize().getSize();
                }
                return stackSize;
            }

            @Override // net.bytebuddy.description.type.TypeList.Generic
            public TypeList asErasures() {
                List<TypeDescription> typeDescriptions = new ArrayList<>(size());
                Iterator it = iterator();
                while (it.hasNext()) {
                    TypeDescription.Generic typeDescription = (TypeDescription.Generic) it.next();
                    typeDescriptions.add(typeDescription.asErasure());
                }
                return new Explicit(typeDescriptions);
            }

            @Override // net.bytebuddy.description.type.TypeList.Generic
            public Generic asRawTypes() {
                List<TypeDescription.Generic> typeDescriptions = new ArrayList<>(size());
                Iterator it = iterator();
                while (it.hasNext()) {
                    TypeDescription.Generic typeDescription = (TypeDescription.Generic) it.next();
                    typeDescriptions.add(typeDescription.asRawType());
                }
                return new Explicit(typeDescriptions);
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeList$Generic$Explicit.class */
        public static class Explicit extends AbstractBase {
            private final List<? extends TypeDefinition> typeDefinitions;

            public Explicit(TypeDefinition... typeDefinition) {
                this(Arrays.asList(typeDefinition));
            }

            public Explicit(List<? extends TypeDefinition> typeDefinitions) {
                this.typeDefinitions = typeDefinitions;
            }

            @Override // java.util.AbstractList, java.util.List
            public TypeDescription.Generic get(int index) {
                return this.typeDefinitions.get(index).asGenericType();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return this.typeDefinitions.size();
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeList$Generic$ForLoadedTypes.class */
        public static class ForLoadedTypes extends AbstractBase {
            private final List<? extends java.lang.reflect.Type> types;

            public ForLoadedTypes(java.lang.reflect.Type... type) {
                this(Arrays.asList(type));
            }

            public ForLoadedTypes(List<? extends java.lang.reflect.Type> types) {
                this.types = types;
            }

            @Override // java.util.AbstractList, java.util.List
            public TypeDescription.Generic get(int index) {
                return TypeDefinition.Sort.describe(this.types.get(index));
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return this.types.size();
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeList$Generic$ForLoadedTypes$OfTypeVariables.class */
            public static class OfTypeVariables extends AbstractBase {
                private final List<TypeVariable<?>> typeVariables;

                protected OfTypeVariables(TypeVariable<?>... typeVariable) {
                    this(Arrays.asList(typeVariable));
                }

                protected OfTypeVariables(List<TypeVariable<?>> typeVariables) {
                    this.typeVariables = typeVariables;
                }

                public static Generic of(GenericDeclaration genericDeclaration) {
                    return new OfTypeVariables(genericDeclaration.getTypeParameters());
                }

                @Override // java.util.AbstractList, java.util.List
                public TypeDescription.Generic get(int index) {
                    TypeVariable<?> typeVariable = this.typeVariables.get(index);
                    return TypeDefinition.Sort.describe(typeVariable, TypeDescription.Generic.AnnotationReader.DISPATCHER.resolveTypeVariable(typeVariable));
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
                public int size() {
                    return this.typeVariables.size();
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeList$Generic$ForDetachedTypes.class */
        public static class ForDetachedTypes extends AbstractBase {
            private final List<? extends TypeDescription.Generic> detachedTypes;
            private final TypeDescription.Generic.Visitor<? extends TypeDescription.Generic> visitor;

            public ForDetachedTypes(List<? extends TypeDescription.Generic> detachedTypes, TypeDescription.Generic.Visitor<? extends TypeDescription.Generic> visitor) {
                this.detachedTypes = detachedTypes;
                this.visitor = visitor;
            }

            public static Generic attachVariables(TypeDescription typeDescription, List<? extends TypeVariableToken> detachedTypeVariables) {
                return new OfTypeVariables(typeDescription, detachedTypeVariables, TypeDescription.Generic.Visitor.Substitutor.ForAttachment.of(typeDescription));
            }

            public static Generic attach(FieldDescription fieldDescription, List<? extends TypeDescription.Generic> detachedTypes) {
                return new ForDetachedTypes(detachedTypes, TypeDescription.Generic.Visitor.Substitutor.ForAttachment.of(fieldDescription));
            }

            public static Generic attach(MethodDescription methodDescription, List<? extends TypeDescription.Generic> detachedTypes) {
                return new ForDetachedTypes(detachedTypes, TypeDescription.Generic.Visitor.Substitutor.ForAttachment.of(methodDescription));
            }

            public static Generic attachVariables(MethodDescription methodDescription, List<? extends TypeVariableToken> detachedTypeVariables) {
                return new OfTypeVariables(methodDescription, detachedTypeVariables, TypeDescription.Generic.Visitor.Substitutor.ForAttachment.of(methodDescription));
            }

            public static Generic attach(ParameterDescription parameterDescription, List<? extends TypeDescription.Generic> detachedTypes) {
                return new ForDetachedTypes(detachedTypes, TypeDescription.Generic.Visitor.Substitutor.ForAttachment.of(parameterDescription));
            }

            @Override // java.util.AbstractList, java.util.List
            public TypeDescription.Generic get(int index) {
                return (TypeDescription.Generic) this.detachedTypes.get(index).accept(this.visitor);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return this.detachedTypes.size();
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeList$Generic$ForDetachedTypes$WithResolvedErasure.class */
            public static class WithResolvedErasure extends AbstractBase {
                private final List<? extends TypeDescription.Generic> detachedTypes;
                private final TypeDescription.Generic.Visitor<? extends TypeDescription.Generic> visitor;

                public WithResolvedErasure(List<? extends TypeDescription.Generic> detachedTypes, TypeDescription.Generic.Visitor<? extends TypeDescription.Generic> visitor) {
                    this.detachedTypes = detachedTypes;
                    this.visitor = visitor;
                }

                @Override // java.util.AbstractList, java.util.List
                public TypeDescription.Generic get(int index) {
                    return new TypeDescription.Generic.LazyProjection.WithResolvedErasure(this.detachedTypes.get(index), this.visitor);
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
                public int size() {
                    return this.detachedTypes.size();
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeList$Generic$ForDetachedTypes$OfTypeVariables.class */
            public static class OfTypeVariables extends AbstractBase {
                private final TypeVariableSource typeVariableSource;
                private final List<? extends TypeVariableToken> detachedTypeVariables;
                private final TypeDescription.Generic.Visitor<? extends TypeDescription.Generic> visitor;

                public OfTypeVariables(TypeVariableSource typeVariableSource, List<? extends TypeVariableToken> detachedTypeVariables, TypeDescription.Generic.Visitor<? extends TypeDescription.Generic> visitor) {
                    this.typeVariableSource = typeVariableSource;
                    this.detachedTypeVariables = detachedTypeVariables;
                    this.visitor = visitor;
                }

                @Override // java.util.AbstractList, java.util.List
                public TypeDescription.Generic get(int index) {
                    return new AttachedTypeVariable(this.typeVariableSource, this.detachedTypeVariables.get(index), this.visitor);
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
                public int size() {
                    return this.detachedTypeVariables.size();
                }

                /* JADX INFO: Access modifiers changed from: protected */
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeList$Generic$ForDetachedTypes$OfTypeVariables$AttachedTypeVariable.class */
                public static class AttachedTypeVariable extends TypeDescription.Generic.OfTypeVariable {
                    private final TypeVariableSource typeVariableSource;
                    private final TypeVariableToken typeVariableToken;
                    private final TypeDescription.Generic.Visitor<? extends TypeDescription.Generic> visitor;

                    protected AttachedTypeVariable(TypeVariableSource typeVariableSource, TypeVariableToken typeVariableToken, TypeDescription.Generic.Visitor<? extends TypeDescription.Generic> visitor) {
                        this.typeVariableSource = typeVariableSource;
                        this.typeVariableToken = typeVariableToken;
                        this.visitor = visitor;
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic
                    public Generic getUpperBounds() {
                        return this.typeVariableToken.getBounds().accept(this.visitor);
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic
                    public TypeVariableSource getTypeVariableSource() {
                        return this.typeVariableSource;
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic
                    public String getSymbol() {
                        return this.typeVariableToken.getSymbol();
                    }

                    @Override // net.bytebuddy.description.annotation.AnnotationSource
                    public AnnotationList getDeclaredAnnotations() {
                        return this.typeVariableToken.getAnnotations();
                    }
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeList$Generic$OfLoadedInterfaceTypes.class */
        public static class OfLoadedInterfaceTypes extends AbstractBase {
            private final Class<?> type;

            public OfLoadedInterfaceTypes(Class<?> type) {
                this.type = type;
            }

            @Override // java.util.AbstractList, java.util.List
            public TypeDescription.Generic get(int index) {
                return new TypeProjection(this.type, index, this.type.getInterfaces());
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return this.type.getInterfaces().length;
            }

            @Override // net.bytebuddy.description.type.TypeList.Generic.AbstractBase, net.bytebuddy.description.type.TypeList.Generic
            public TypeList asErasures() {
                return new ForLoadedTypes(this.type.getInterfaces());
            }

            /* JADX INFO: Access modifiers changed from: private */
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeList$Generic$OfLoadedInterfaceTypes$TypeProjection.class */
            public static class TypeProjection extends TypeDescription.Generic.LazyProjection.WithLazyNavigation.OfAnnotatedElement {
                private final Class<?> type;
                private final int index;
                private final Class<?>[] erasure;
                private transient /* synthetic */ TypeDescription.Generic resolved;

                private TypeProjection(Class<?> type, int index, Class<?>[] erasure) {
                    this.type = type;
                    this.index = index;
                    this.erasure = erasure;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection
                @CachedReturnPlugin.Enhance("resolved")
                protected TypeDescription.Generic resolve() {
                    TypeDescription.Generic asRawType;
                    if (this.resolved != null) {
                        asRawType = null;
                    } else {
                        java.lang.reflect.Type[] type = this.type.getGenericInterfaces();
                        if (this.erasure.length == type.length) {
                            asRawType = TypeDefinition.Sort.describe(type[this.index], getAnnotationReader());
                        } else {
                            asRawType = asRawType();
                        }
                    }
                    TypeDescription.Generic generic = asRawType;
                    if (generic == null) {
                        generic = this.resolved;
                    } else {
                        this.resolved = generic;
                    }
                    return generic;
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public TypeDescription asErasure() {
                    return TypeDescription.ForLoadedType.of(this.erasure[this.index]);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection.WithLazyNavigation.OfAnnotatedElement
                protected TypeDescription.Generic.AnnotationReader getAnnotationReader() {
                    return TypeDescription.Generic.AnnotationReader.DISPATCHER.resolveInterfaceType(this.type, this.index);
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeList$Generic$OfConstructorExceptionTypes.class */
        public static class OfConstructorExceptionTypes extends AbstractBase {
            private final Constructor<?> constructor;

            public OfConstructorExceptionTypes(Constructor<?> constructor) {
                this.constructor = constructor;
            }

            @Override // java.util.AbstractList, java.util.List
            public TypeDescription.Generic get(int index) {
                return new TypeProjection(this.constructor, index, this.constructor.getExceptionTypes());
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return this.constructor.getExceptionTypes().length;
            }

            @Override // net.bytebuddy.description.type.TypeList.Generic.AbstractBase, net.bytebuddy.description.type.TypeList.Generic
            public TypeList asErasures() {
                return new ForLoadedTypes(this.constructor.getExceptionTypes());
            }

            /* JADX INFO: Access modifiers changed from: private */
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeList$Generic$OfConstructorExceptionTypes$TypeProjection.class */
            public static class TypeProjection extends TypeDescription.Generic.LazyProjection.WithEagerNavigation.OfAnnotatedElement {
                private final Constructor<?> constructor;
                private final int index;
                private final Class<?>[] erasure;
                private transient /* synthetic */ TypeDescription.Generic resolved;

                private TypeProjection(Constructor<?> constructor, int index, Class<?>[] erasure) {
                    this.constructor = constructor;
                    this.index = index;
                    this.erasure = erasure;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection
                @CachedReturnPlugin.Enhance("resolved")
                protected TypeDescription.Generic resolve() {
                    TypeDescription.Generic asRawType;
                    if (this.resolved != null) {
                        asRawType = null;
                    } else {
                        java.lang.reflect.Type[] type = this.constructor.getGenericExceptionTypes();
                        if (this.erasure.length == type.length) {
                            asRawType = TypeDefinition.Sort.describe(type[this.index], getAnnotationReader());
                        } else {
                            asRawType = asRawType();
                        }
                    }
                    TypeDescription.Generic generic = asRawType;
                    if (generic == null) {
                        generic = this.resolved;
                    } else {
                        this.resolved = generic;
                    }
                    return generic;
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public TypeDescription asErasure() {
                    return TypeDescription.ForLoadedType.of(this.erasure[this.index]);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection.WithEagerNavigation.OfAnnotatedElement
                protected TypeDescription.Generic.AnnotationReader getAnnotationReader() {
                    return TypeDescription.Generic.AnnotationReader.DISPATCHER.resolveExceptionType(this.constructor, this.index);
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeList$Generic$OfMethodExceptionTypes.class */
        public static class OfMethodExceptionTypes extends AbstractBase {
            private final Method method;

            public OfMethodExceptionTypes(Method method) {
                this.method = method;
            }

            @Override // java.util.AbstractList, java.util.List
            public TypeDescription.Generic get(int index) {
                return new TypeProjection(this.method, index, this.method.getExceptionTypes());
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return this.method.getExceptionTypes().length;
            }

            @Override // net.bytebuddy.description.type.TypeList.Generic.AbstractBase, net.bytebuddy.description.type.TypeList.Generic
            public TypeList asErasures() {
                return new ForLoadedTypes(this.method.getExceptionTypes());
            }

            /* JADX INFO: Access modifiers changed from: private */
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeList$Generic$OfMethodExceptionTypes$TypeProjection.class */
            public static class TypeProjection extends TypeDescription.Generic.LazyProjection.WithEagerNavigation.OfAnnotatedElement {
                private final Method method;
                private final int index;
                private final Class<?>[] erasure;
                private transient /* synthetic */ TypeDescription.Generic resolved;

                public TypeProjection(Method method, int index, Class<?>[] erasure) {
                    this.method = method;
                    this.index = index;
                    this.erasure = erasure;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection
                @CachedReturnPlugin.Enhance("resolved")
                protected TypeDescription.Generic resolve() {
                    TypeDescription.Generic asRawType;
                    if (this.resolved != null) {
                        asRawType = null;
                    } else {
                        java.lang.reflect.Type[] type = this.method.getGenericExceptionTypes();
                        if (this.erasure.length == type.length) {
                            asRawType = TypeDefinition.Sort.describe(type[this.index], getAnnotationReader());
                        } else {
                            asRawType = asRawType();
                        }
                    }
                    TypeDescription.Generic generic = asRawType;
                    if (generic == null) {
                        generic = this.resolved;
                    } else {
                        this.resolved = generic;
                    }
                    return generic;
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public TypeDescription asErasure() {
                    return TypeDescription.ForLoadedType.of(this.erasure[this.index]);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection.WithEagerNavigation.OfAnnotatedElement
                protected TypeDescription.Generic.AnnotationReader getAnnotationReader() {
                    return TypeDescription.Generic.AnnotationReader.DISPATCHER.resolveExceptionType(this.method, this.index);
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeList$Generic$Empty.class */
        public static class Empty extends FilterableList.Empty<TypeDescription.Generic, Generic> implements Generic {
            @Override // net.bytebuddy.description.type.TypeList.Generic
            public TypeList asErasures() {
                return new Empty();
            }

            @Override // net.bytebuddy.description.type.TypeList.Generic
            public Generic asRawTypes() {
                return this;
            }

            @Override // net.bytebuddy.description.type.TypeList.Generic
            public Generic accept(TypeDescription.Generic.Visitor<? extends TypeDescription.Generic> visitor) {
                return new Empty();
            }

            @Override // net.bytebuddy.description.type.TypeList.Generic
            public ByteCodeElement.Token.TokenList<TypeVariableToken> asTokenList(ElementMatcher<? super TypeDescription> matcher) {
                return new ByteCodeElement.Token.TokenList<>(new TypeVariableToken[0]);
            }

            @Override // net.bytebuddy.description.type.TypeList.Generic
            public int getStackSize() {
                return 0;
            }
        }
    }
}
