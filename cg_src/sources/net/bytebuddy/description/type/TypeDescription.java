package net.bytebuddy.description.type;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.GenericSignatureFormatError;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.build.CachedReturnPlugin;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.ByteCodeElement;
import net.bytebuddy.description.ModifierReviewable;
import net.bytebuddy.description.TypeVariableSource;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.annotation.AnnotationList;
import net.bytebuddy.description.annotation.AnnotationSource;
import net.bytebuddy.description.enumeration.EnumerationDescription;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.field.FieldList;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.type.PackageDescription;
import net.bytebuddy.description.type.RecordComponentDescription;
import net.bytebuddy.description.type.RecordComponentList;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeList;
import net.bytebuddy.dynamic.TargetType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bytecode.StackSize;
import net.bytebuddy.jar.asm.signature.SignatureVisitor;
import net.bytebuddy.jar.asm.signature.SignatureWriter;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.CompoundList;
import net.bytebuddy.utility.JavaType;
import net.bytebuddy.utility.privilege.GetSystemPropertyAction;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription.class */
public interface TypeDescription extends TypeDefinition, ByteCodeElement, TypeVariableSource {
    public static final TypeDescription OBJECT = new ForLoadedType(Object.class);
    public static final TypeDescription STRING = new ForLoadedType(String.class);
    public static final TypeDescription CLASS = new ForLoadedType(Class.class);
    public static final TypeDescription THROWABLE = new ForLoadedType(Throwable.class);
    public static final TypeDescription VOID = new ForLoadedType(Void.TYPE);
    public static final TypeList.Generic ARRAY_INTERFACES = new TypeList.Generic.ForLoadedTypes(Cloneable.class, Serializable.class);
    public static final TypeDescription UNDEFINED = null;

    @Override // net.bytebuddy.description.type.TypeDefinition
    FieldList<FieldDescription.InDefinedShape> getDeclaredFields();

    @Override // net.bytebuddy.description.type.TypeDefinition
    MethodList<MethodDescription.InDefinedShape> getDeclaredMethods();

    @Override // net.bytebuddy.description.type.TypeDefinition
    RecordComponentList<RecordComponentDescription.InDefinedShape> getRecordComponents();

    boolean isInstance(Object obj);

    boolean isAssignableFrom(Class<?> cls);

    boolean isAssignableFrom(TypeDescription typeDescription);

    boolean isAssignableTo(Class<?> cls);

    boolean isAssignableTo(TypeDescription typeDescription);

    boolean isInHierarchyWith(Class<?> cls);

    boolean isInHierarchyWith(TypeDescription typeDescription);

    @Override // net.bytebuddy.description.type.TypeDefinition
    TypeDescription getComponentType();

    @Override // net.bytebuddy.description.DeclaredByType, net.bytebuddy.description.field.FieldDescription.InDefinedShape
    TypeDescription getDeclaringType();

    TypeList getDeclaredTypes();

    MethodDescription.InDefinedShape getEnclosingMethod();

    TypeDescription getEnclosingType();

    int getActualModifiers(boolean z);

    String getSimpleName();

    String getCanonicalName();

    boolean isAnonymousType();

    boolean isLocalType();

    boolean isMemberType();

    PackageDescription getPackage();

    AnnotationList getInheritedAnnotations();

    boolean isSamePackage(TypeDescription typeDescription);

    boolean isPrimitiveWrapper();

    boolean isAnnotationReturnType();

    boolean isAnnotationValue();

    boolean isAnnotationValue(Object obj);

    boolean isPackageType();

    int getInnerClassCount();

    boolean isInnerClass();

    boolean isNestedClass();

    TypeDescription asBoxed();

    TypeDescription asUnboxed();

    Object getDefaultValue();

    TypeDescription getNestHost();

    TypeList getNestMembers();

    boolean isNestHost();

    boolean isNestMateOf(Class<?> cls);

    boolean isNestMateOf(TypeDescription typeDescription);

    boolean isCompileTimeConstant();

    TypeList getPermittedSubclasses();

    boolean isSealed();

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic.class */
    public interface Generic extends TypeDefinition, AnnotationSource {
        public static final Generic OBJECT = new OfNonGenericType.ForLoadedType(Object.class);
        public static final Generic CLASS = new OfNonGenericType.ForLoadedType(Class.class);
        public static final Generic VOID = new OfNonGenericType.ForLoadedType(Void.TYPE);
        public static final Generic ANNOTATION = new OfNonGenericType.ForLoadedType(Annotation.class);
        public static final Generic UNDEFINED = null;

        Generic asRawType();

        TypeList.Generic getUpperBounds();

        TypeList.Generic getLowerBounds();

        TypeList.Generic getTypeArguments();

        Generic getOwnerType();

        Generic findBindingOf(Generic generic);

        TypeVariableSource getTypeVariableSource();

        String getSymbol();

        @Override // net.bytebuddy.description.type.TypeDefinition
        Generic getComponentType();

        @Override // net.bytebuddy.description.type.TypeDefinition
        FieldList<FieldDescription.InGenericShape> getDeclaredFields();

        @Override // net.bytebuddy.description.type.TypeDefinition
        MethodList<MethodDescription.InGenericShape> getDeclaredMethods();

        @Override // net.bytebuddy.description.type.TypeDefinition
        RecordComponentList<RecordComponentDescription.InGenericShape> getRecordComponents();

        <T> T accept(Visitor<T> visitor);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$Visitor.class */
        public interface Visitor<T> {
            T onGenericArray(Generic generic);

            T onWildcard(Generic generic);

            T onParameterizedType(Generic generic);

            T onTypeVariable(Generic generic);

            T onNonGenericType(Generic generic);

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$Visitor$NoOp.class */
            public enum NoOp implements Visitor<Generic> {
                INSTANCE;

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Generic onGenericArray(Generic genericArray) {
                    return genericArray;
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Generic onWildcard(Generic wildcard) {
                    return wildcard;
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Generic onParameterizedType(Generic parameterizedType) {
                    return parameterizedType;
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Generic onTypeVariable(Generic typeVariable) {
                    return typeVariable;
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Generic onNonGenericType(Generic typeDescription) {
                    return typeDescription;
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$Visitor$TypeErasing.class */
            public enum TypeErasing implements Visitor<Generic> {
                INSTANCE;

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Generic onGenericArray(Generic genericArray) {
                    return genericArray.asRawType();
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Generic onWildcard(Generic wildcard) {
                    throw new IllegalArgumentException("Cannot erase a wildcard type: " + wildcard);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Generic onParameterizedType(Generic parameterizedType) {
                    return parameterizedType.asRawType();
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Generic onTypeVariable(Generic typeVariable) {
                    return typeVariable.asRawType();
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Generic onNonGenericType(Generic typeDescription) {
                    return typeDescription.asRawType();
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$Visitor$AnnotationStripper.class */
            public enum AnnotationStripper implements Visitor<Generic> {
                INSTANCE;

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Generic onGenericArray(Generic genericArray) {
                    return new OfGenericArray.Latent((Generic) genericArray.getComponentType().accept(this), AnnotationSource.Empty.INSTANCE);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Generic onWildcard(Generic wildcard) {
                    return new OfWildcardType.Latent(wildcard.getUpperBounds().accept(this), wildcard.getLowerBounds().accept(this), AnnotationSource.Empty.INSTANCE);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Generic onParameterizedType(Generic parameterizedType) {
                    Generic ownerType = parameterizedType.getOwnerType();
                    return new OfParameterizedType.Latent(parameterizedType.asErasure(), ownerType == null ? Generic.UNDEFINED : (Generic) ownerType.accept(this), parameterizedType.getTypeArguments().accept(this), AnnotationSource.Empty.INSTANCE);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Generic onTypeVariable(Generic typeVariable) {
                    return new NonAnnotatedTypeVariable(typeVariable);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Generic onNonGenericType(Generic typeDescription) {
                    if (typeDescription.isArray()) {
                        return new OfGenericArray.Latent(onNonGenericType(typeDescription.getComponentType()), AnnotationSource.Empty.INSTANCE);
                    }
                    return new OfNonGenericType.Latent(typeDescription.asErasure(), AnnotationSource.Empty.INSTANCE);
                }

                /* JADX INFO: Access modifiers changed from: protected */
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$Visitor$AnnotationStripper$NonAnnotatedTypeVariable.class */
                public static class NonAnnotatedTypeVariable extends OfTypeVariable {
                    private final Generic typeVariable;

                    protected NonAnnotatedTypeVariable(Generic typeVariable) {
                        this.typeVariable = typeVariable;
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic
                    public TypeList.Generic getUpperBounds() {
                        return this.typeVariable.getUpperBounds();
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic
                    public TypeVariableSource getTypeVariableSource() {
                        return this.typeVariable.getTypeVariableSource();
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic
                    public String getSymbol() {
                        return this.typeVariable.getSymbol();
                    }

                    @Override // net.bytebuddy.description.annotation.AnnotationSource
                    public AnnotationList getDeclaredAnnotations() {
                        return new AnnotationList.Empty();
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$Visitor$Assigner.class */
            public enum Assigner implements Visitor<Dispatcher> {
                INSTANCE;

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Dispatcher onGenericArray(Generic genericArray) {
                    return new Dispatcher.ForGenericArray(genericArray);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Dispatcher onWildcard(Generic wildcard) {
                    throw new IllegalArgumentException("A wildcard is not a first level type: " + this);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Dispatcher onParameterizedType(Generic parameterizedType) {
                    return new Dispatcher.ForParameterizedType(parameterizedType);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Dispatcher onTypeVariable(Generic typeVariable) {
                    return new Dispatcher.ForTypeVariable(typeVariable);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Dispatcher onNonGenericType(Generic typeDescription) {
                    return new Dispatcher.ForNonGenericType(typeDescription.asErasure());
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$Visitor$Assigner$Dispatcher.class */
                public interface Dispatcher {
                    boolean isAssignableFrom(Generic generic);

                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$Visitor$Assigner$Dispatcher$AbstractBase.class */
                    public static abstract class AbstractBase implements Dispatcher, Visitor<Boolean> {
                        @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Assigner.Dispatcher
                        public boolean isAssignableFrom(Generic typeDescription) {
                            return ((Boolean) typeDescription.accept(this)).booleanValue();
                        }
                    }

                    @HashCodeAndEqualsPlugin.Enhance
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$Visitor$Assigner$Dispatcher$ForNonGenericType.class */
                    public static class ForNonGenericType extends AbstractBase {
                        private final TypeDescription typeDescription;

                        public boolean equals(Object obj) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.typeDescription.equals(((ForNonGenericType) obj).typeDescription);
                        }

                        public int hashCode() {
                            return (17 * 31) + this.typeDescription.hashCode();
                        }

                        protected ForNonGenericType(TypeDescription typeDescription) {
                            this.typeDescription = typeDescription;
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                        public Boolean onGenericArray(Generic genericArray) {
                            boolean z;
                            if (this.typeDescription.isArray()) {
                                z = ((Boolean) genericArray.getComponentType().accept(new ForNonGenericType(this.typeDescription.getComponentType()))).booleanValue();
                            } else {
                                z = this.typeDescription.represents(Object.class) || TypeDescription.ARRAY_INTERFACES.contains(this.typeDescription.asGenericType());
                            }
                            return Boolean.valueOf(z);
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                        public Boolean onWildcard(Generic wildcard) {
                            throw new IllegalArgumentException("A wildcard is not a first-level type: " + wildcard);
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                        public Boolean onParameterizedType(Generic parameterizedType) {
                            if (this.typeDescription.equals(parameterizedType.asErasure())) {
                                return true;
                            }
                            Generic superClass = parameterizedType.getSuperClass();
                            if (superClass != null && isAssignableFrom(superClass)) {
                                return true;
                            }
                            for (Generic interfaceType : parameterizedType.getInterfaces()) {
                                if (isAssignableFrom(interfaceType)) {
                                    return true;
                                }
                            }
                            return Boolean.valueOf(this.typeDescription.represents(Object.class));
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                        public Boolean onTypeVariable(Generic typeVariable) {
                            for (Generic upperBound : typeVariable.getUpperBounds()) {
                                if (isAssignableFrom(upperBound)) {
                                    return true;
                                }
                            }
                            return false;
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                        public Boolean onNonGenericType(Generic typeDescription) {
                            return Boolean.valueOf(this.typeDescription.isAssignableFrom(typeDescription.asErasure()));
                        }
                    }

                    @HashCodeAndEqualsPlugin.Enhance
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$Visitor$Assigner$Dispatcher$ForTypeVariable.class */
                    public static class ForTypeVariable extends AbstractBase {
                        private final Generic typeVariable;

                        public boolean equals(Object obj) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.typeVariable.equals(((ForTypeVariable) obj).typeVariable);
                        }

                        public int hashCode() {
                            return (17 * 31) + this.typeVariable.hashCode();
                        }

                        protected ForTypeVariable(Generic typeVariable) {
                            this.typeVariable = typeVariable;
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                        public Boolean onGenericArray(Generic genericArray) {
                            return false;
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                        public Boolean onWildcard(Generic wildcard) {
                            throw new IllegalArgumentException("A wildcard is not a first-level type: " + wildcard);
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                        public Boolean onParameterizedType(Generic parameterizedType) {
                            return false;
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                        public Boolean onTypeVariable(Generic typeVariable) {
                            if (typeVariable.equals(this.typeVariable)) {
                                return true;
                            }
                            for (Generic upperBound : typeVariable.getUpperBounds()) {
                                if (isAssignableFrom(upperBound)) {
                                    return true;
                                }
                            }
                            return false;
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                        public Boolean onNonGenericType(Generic typeDescription) {
                            return false;
                        }
                    }

                    @HashCodeAndEqualsPlugin.Enhance
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$Visitor$Assigner$Dispatcher$ForParameterizedType.class */
                    public static class ForParameterizedType extends AbstractBase {
                        private final Generic parameterizedType;

                        public boolean equals(Object obj) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.parameterizedType.equals(((ForParameterizedType) obj).parameterizedType);
                        }

                        public int hashCode() {
                            return (17 * 31) + this.parameterizedType.hashCode();
                        }

                        protected ForParameterizedType(Generic parameterizedType) {
                            this.parameterizedType = parameterizedType;
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                        public Boolean onGenericArray(Generic genericArray) {
                            return false;
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                        public Boolean onWildcard(Generic wildcard) {
                            throw new IllegalArgumentException("A wildcard is not a first-level type: " + wildcard);
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                        public Boolean onParameterizedType(Generic parameterizedType) {
                            if (this.parameterizedType.asErasure().equals(parameterizedType.asErasure())) {
                                Generic fromOwner = this.parameterizedType.getOwnerType();
                                Generic toOwner = parameterizedType.getOwnerType();
                                if (fromOwner != null && toOwner != null && !((Dispatcher) fromOwner.accept(Assigner.INSTANCE)).isAssignableFrom(toOwner)) {
                                    return false;
                                }
                                TypeList.Generic fromArguments = this.parameterizedType.getTypeArguments();
                                TypeList.Generic toArguments = parameterizedType.getTypeArguments();
                                if (fromArguments.size() == toArguments.size()) {
                                    for (int index = 0; index < fromArguments.size(); index++) {
                                        if (!((Dispatcher) ((Generic) fromArguments.get(index)).accept(ParameterAssigner.INSTANCE)).isAssignableFrom((Generic) toArguments.get(index))) {
                                            return false;
                                        }
                                    }
                                    return true;
                                }
                                throw new IllegalArgumentException("Incompatible generic types: " + parameterizedType + " and " + this.parameterizedType);
                            }
                            Generic superClass = parameterizedType.getSuperClass();
                            if (superClass != null && isAssignableFrom(superClass)) {
                                return true;
                            }
                            for (Generic interfaceType : parameterizedType.getInterfaces()) {
                                if (isAssignableFrom(interfaceType)) {
                                    return true;
                                }
                            }
                            return false;
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                        public Boolean onTypeVariable(Generic typeVariable) {
                            for (Generic upperBound : typeVariable.getUpperBounds()) {
                                if (isAssignableFrom(upperBound)) {
                                    return true;
                                }
                            }
                            return false;
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                        public Boolean onNonGenericType(Generic typeDescription) {
                            if (this.parameterizedType.asErasure().equals(typeDescription.asErasure())) {
                                return true;
                            }
                            Generic superClass = typeDescription.getSuperClass();
                            if (superClass != null && isAssignableFrom(superClass)) {
                                return true;
                            }
                            for (Generic interfaceType : typeDescription.getInterfaces()) {
                                if (isAssignableFrom(interfaceType)) {
                                    return true;
                                }
                            }
                            return false;
                        }

                        /* JADX INFO: Access modifiers changed from: protected */
                        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$Visitor$Assigner$Dispatcher$ForParameterizedType$ParameterAssigner.class */
                        public enum ParameterAssigner implements Visitor<Dispatcher> {
                            INSTANCE;

                            /* JADX WARN: Can't rename method to resolve collision */
                            @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                            public Dispatcher onGenericArray(Generic genericArray) {
                                return new InvariantBinding(genericArray);
                            }

                            /* JADX WARN: Can't rename method to resolve collision */
                            @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                            public Dispatcher onWildcard(Generic wildcard) {
                                TypeList.Generic lowerBounds = wildcard.getLowerBounds();
                                if (lowerBounds.isEmpty()) {
                                    return new CovariantBinding(wildcard.getUpperBounds().getOnly());
                                }
                                return new ContravariantBinding(lowerBounds.getOnly());
                            }

                            /* JADX WARN: Can't rename method to resolve collision */
                            @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                            public Dispatcher onParameterizedType(Generic parameterizedType) {
                                return new InvariantBinding(parameterizedType);
                            }

                            /* JADX WARN: Can't rename method to resolve collision */
                            @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                            public Dispatcher onTypeVariable(Generic typeVariable) {
                                return new InvariantBinding(typeVariable);
                            }

                            /* JADX WARN: Can't rename method to resolve collision */
                            @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                            public Dispatcher onNonGenericType(Generic typeDescription) {
                                return new InvariantBinding(typeDescription);
                            }

                            /* JADX INFO: Access modifiers changed from: protected */
                            @HashCodeAndEqualsPlugin.Enhance
                            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$Visitor$Assigner$Dispatcher$ForParameterizedType$ParameterAssigner$InvariantBinding.class */
                            public static class InvariantBinding implements Dispatcher {
                                private final Generic typeDescription;

                                public boolean equals(Object obj) {
                                    if (this == obj) {
                                        return true;
                                    }
                                    return obj != null && getClass() == obj.getClass() && this.typeDescription.equals(((InvariantBinding) obj).typeDescription);
                                }

                                public int hashCode() {
                                    return (17 * 31) + this.typeDescription.hashCode();
                                }

                                protected InvariantBinding(Generic typeDescription) {
                                    this.typeDescription = typeDescription;
                                }

                                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Assigner.Dispatcher
                                public boolean isAssignableFrom(Generic typeDescription) {
                                    return typeDescription.equals(this.typeDescription);
                                }
                            }

                            /* JADX INFO: Access modifiers changed from: protected */
                            @HashCodeAndEqualsPlugin.Enhance
                            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$Visitor$Assigner$Dispatcher$ForParameterizedType$ParameterAssigner$CovariantBinding.class */
                            public static class CovariantBinding implements Dispatcher {
                                private final Generic upperBound;

                                public boolean equals(Object obj) {
                                    if (this == obj) {
                                        return true;
                                    }
                                    return obj != null && getClass() == obj.getClass() && this.upperBound.equals(((CovariantBinding) obj).upperBound);
                                }

                                public int hashCode() {
                                    return (17 * 31) + this.upperBound.hashCode();
                                }

                                protected CovariantBinding(Generic upperBound) {
                                    this.upperBound = upperBound;
                                }

                                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Assigner.Dispatcher
                                public boolean isAssignableFrom(Generic typeDescription) {
                                    if (typeDescription.getSort().isWildcard()) {
                                        return typeDescription.getLowerBounds().isEmpty() && ((Dispatcher) this.upperBound.accept(Assigner.INSTANCE)).isAssignableFrom(typeDescription.getUpperBounds().getOnly());
                                    }
                                    return ((Dispatcher) this.upperBound.accept(Assigner.INSTANCE)).isAssignableFrom(typeDescription);
                                }
                            }

                            /* JADX INFO: Access modifiers changed from: protected */
                            @HashCodeAndEqualsPlugin.Enhance
                            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$Visitor$Assigner$Dispatcher$ForParameterizedType$ParameterAssigner$ContravariantBinding.class */
                            public static class ContravariantBinding implements Dispatcher {
                                private final Generic lowerBound;

                                public boolean equals(Object obj) {
                                    if (this == obj) {
                                        return true;
                                    }
                                    return obj != null && getClass() == obj.getClass() && this.lowerBound.equals(((ContravariantBinding) obj).lowerBound);
                                }

                                public int hashCode() {
                                    return (17 * 31) + this.lowerBound.hashCode();
                                }

                                protected ContravariantBinding(Generic lowerBound) {
                                    this.lowerBound = lowerBound;
                                }

                                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Assigner.Dispatcher
                                public boolean isAssignableFrom(Generic typeDescription) {
                                    if (!typeDescription.getSort().isWildcard()) {
                                        return typeDescription.getSort().isWildcard() || ((Dispatcher) typeDescription.accept(Assigner.INSTANCE)).isAssignableFrom(this.lowerBound);
                                    }
                                    TypeList.Generic lowerBounds = typeDescription.getLowerBounds();
                                    return !lowerBounds.isEmpty() && ((Dispatcher) lowerBounds.getOnly().accept(Assigner.INSTANCE)).isAssignableFrom(this.lowerBound);
                                }
                            }
                        }
                    }

                    @HashCodeAndEqualsPlugin.Enhance
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$Visitor$Assigner$Dispatcher$ForGenericArray.class */
                    public static class ForGenericArray extends AbstractBase {
                        private final Generic genericArray;

                        public boolean equals(Object obj) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.genericArray.equals(((ForGenericArray) obj).genericArray);
                        }

                        public int hashCode() {
                            return (17 * 31) + this.genericArray.hashCode();
                        }

                        protected ForGenericArray(Generic genericArray) {
                            this.genericArray = genericArray;
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                        public Boolean onGenericArray(Generic genericArray) {
                            return Boolean.valueOf(((Dispatcher) this.genericArray.getComponentType().accept(Assigner.INSTANCE)).isAssignableFrom(genericArray.getComponentType()));
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                        public Boolean onWildcard(Generic wildcard) {
                            throw new IllegalArgumentException("A wildcard is not a first-level type: " + wildcard);
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                        public Boolean onParameterizedType(Generic parameterizedType) {
                            return false;
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                        public Boolean onTypeVariable(Generic typeVariable) {
                            return false;
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                        public Boolean onNonGenericType(Generic typeDescription) {
                            return Boolean.valueOf(typeDescription.isArray() && ((Dispatcher) this.genericArray.getComponentType().accept(Assigner.INSTANCE)).isAssignableFrom(typeDescription.getComponentType()));
                        }
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$Visitor$Validator.class */
            public enum Validator implements Visitor<Boolean> {
                SUPER_CLASS(false, false, false, false) { // from class: net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Validator.1
                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Validator, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public /* bridge */ /* synthetic */ Boolean onTypeVariable(Generic generic) {
                        return super.onTypeVariable(generic);
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Validator, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public /* bridge */ /* synthetic */ Boolean onWildcard(Generic generic) {
                        return super.onWildcard(generic);
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Validator, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public /* bridge */ /* synthetic */ Boolean onGenericArray(Generic generic) {
                        return super.onGenericArray(generic);
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Validator, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public Boolean onNonGenericType(Generic typeDescription) {
                        return Boolean.valueOf(super.onNonGenericType(typeDescription).booleanValue() && !typeDescription.isInterface());
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Validator, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public Boolean onParameterizedType(Generic parameterizedType) {
                        return Boolean.valueOf(!parameterizedType.isInterface());
                    }
                },
                INTERFACE(false, false, false, false) { // from class: net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Validator.2
                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Validator, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public /* bridge */ /* synthetic */ Boolean onTypeVariable(Generic generic) {
                        return super.onTypeVariable(generic);
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Validator, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public /* bridge */ /* synthetic */ Boolean onWildcard(Generic generic) {
                        return super.onWildcard(generic);
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Validator, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public /* bridge */ /* synthetic */ Boolean onGenericArray(Generic generic) {
                        return super.onGenericArray(generic);
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Validator, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public Boolean onNonGenericType(Generic typeDescription) {
                        return Boolean.valueOf(super.onNonGenericType(typeDescription).booleanValue() && typeDescription.isInterface());
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Validator, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public Boolean onParameterizedType(Generic parameterizedType) {
                        return Boolean.valueOf(parameterizedType.isInterface());
                    }
                },
                TYPE_VARIABLE(false, false, true, false),
                FIELD(true, true, true, false),
                METHOD_RETURN(true, true, true, true),
                METHOD_PARAMETER(true, true, true, false),
                EXCEPTION(false, false, true, false) { // from class: net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Validator.3
                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Validator, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public /* bridge */ /* synthetic */ Boolean onWildcard(Generic generic) {
                        return super.onWildcard(generic);
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Validator, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public /* bridge */ /* synthetic */ Boolean onGenericArray(Generic generic) {
                        return super.onGenericArray(generic);
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Validator, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public Boolean onParameterizedType(Generic parameterizedType) {
                        return false;
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Validator, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public Boolean onTypeVariable(Generic typeVariable) {
                        for (Generic bound : typeVariable.getUpperBounds()) {
                            if (((Boolean) bound.accept(this)).booleanValue()) {
                                return true;
                            }
                        }
                        return false;
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Validator, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public Boolean onNonGenericType(Generic typeDescription) {
                        return Boolean.valueOf(typeDescription.asErasure().isAssignableTo(Throwable.class));
                    }
                },
                RECEIVER(false, false, false, false);
                
                private final boolean acceptsArray;
                private final boolean acceptsPrimitive;
                private final boolean acceptsVariable;
                private final boolean acceptsVoid;

                Validator(boolean acceptsArray, boolean acceptsPrimitive, boolean acceptsVariable, boolean acceptsVoid) {
                    this.acceptsArray = acceptsArray;
                    this.acceptsPrimitive = acceptsPrimitive;
                    this.acceptsVariable = acceptsVariable;
                    this.acceptsVoid = acceptsVoid;
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Boolean onGenericArray(Generic genericArray) {
                    return Boolean.valueOf(this.acceptsArray);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Boolean onWildcard(Generic wildcard) {
                    return false;
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Boolean onParameterizedType(Generic parameterizedType) {
                    return true;
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Boolean onTypeVariable(Generic typeVariable) {
                    return Boolean.valueOf(this.acceptsVariable);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Boolean onNonGenericType(Generic typeDescription) {
                    return Boolean.valueOf((this.acceptsArray || !typeDescription.isArray()) && (this.acceptsPrimitive || !typeDescription.isPrimitive()) && (this.acceptsVoid || !typeDescription.represents(Void.TYPE)));
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$Visitor$Validator$ForTypeAnnotations.class */
                public enum ForTypeAnnotations implements Visitor<Boolean> {
                    INSTANCE;
                    
                    private final ElementType typeUse;
                    private final ElementType typeParameter;

                    ForTypeAnnotations() {
                        ElementType typeUse;
                        ElementType typeParameter;
                        try {
                            typeUse = (ElementType) Enum.valueOf(ElementType.class, "TYPE_USE");
                            typeParameter = (ElementType) Enum.valueOf(ElementType.class, "TYPE_PARAMETER");
                        } catch (IllegalArgumentException e) {
                            typeUse = null;
                            typeParameter = null;
                        }
                        this.typeUse = typeUse;
                        this.typeParameter = typeParameter;
                    }

                    /* JADX WARN: Removed duplicated region for block: B:5:0x001d  */
                    /*
                        Code decompiled incorrectly, please refer to instructions dump.
                        To view partially-correct add '--show-bad-code' argument
                    */
                    public static boolean ofFormalTypeVariable(net.bytebuddy.description.type.TypeDescription.Generic r3) {
                        /*
                            java.util.HashSet r0 = new java.util.HashSet
                            r1 = r0
                            r1.<init>()
                            r4 = r0
                            r0 = r3
                            net.bytebuddy.description.annotation.AnnotationList r0 = r0.getDeclaredAnnotations()
                            java.util.Iterator r0 = r0.iterator()
                            r5 = r0
                        L14:
                            r0 = r5
                            boolean r0 = r0.hasNext()
                            if (r0 == 0) goto L4f
                            r0 = r5
                            java.lang.Object r0 = r0.next()
                            net.bytebuddy.description.annotation.AnnotationDescription r0 = (net.bytebuddy.description.annotation.AnnotationDescription) r0
                            r6 = r0
                            r0 = r6
                            java.util.Set r0 = r0.getElementTypes()
                            net.bytebuddy.description.type.TypeDescription$Generic$Visitor$Validator$ForTypeAnnotations r1 = net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Validator.ForTypeAnnotations.INSTANCE
                            java.lang.annotation.ElementType r1 = r1.typeParameter
                            boolean r0 = r0.contains(r1)
                            if (r0 == 0) goto L4a
                            r0 = r4
                            r1 = r6
                            net.bytebuddy.description.type.TypeDescription r1 = r1.getAnnotationType()
                            boolean r0 = r0.add(r1)
                            if (r0 != 0) goto L4c
                        L4a:
                            r0 = 0
                            return r0
                        L4c:
                            goto L14
                        L4f:
                            r0 = 1
                            return r0
                        */
                        throw new UnsupportedOperationException("Method not decompiled: net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Validator.ForTypeAnnotations.ofFormalTypeVariable(net.bytebuddy.description.type.TypeDescription$Generic):boolean");
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public Boolean onGenericArray(Generic genericArray) {
                        return Boolean.valueOf(isValid(genericArray) && ((Boolean) genericArray.getComponentType().accept(this)).booleanValue());
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public Boolean onWildcard(Generic wildcard) {
                        if (!isValid(wildcard)) {
                            return false;
                        }
                        TypeList.Generic lowerBounds = wildcard.getLowerBounds();
                        return (Boolean) (lowerBounds.isEmpty() ? wildcard.getUpperBounds() : lowerBounds).getOnly().accept(this);
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public Boolean onParameterizedType(Generic parameterizedType) {
                        if (!isValid(parameterizedType)) {
                            return false;
                        }
                        Generic ownerType = parameterizedType.getOwnerType();
                        if (ownerType != null && !((Boolean) ownerType.accept(this)).booleanValue()) {
                            return false;
                        }
                        for (Generic typeArgument : parameterizedType.getTypeArguments()) {
                            if (!((Boolean) typeArgument.accept(this)).booleanValue()) {
                                return false;
                            }
                        }
                        return true;
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public Boolean onTypeVariable(Generic typeVariable) {
                        return Boolean.valueOf(isValid(typeVariable));
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public Boolean onNonGenericType(Generic typeDescription) {
                        return Boolean.valueOf(isValid(typeDescription) && (!typeDescription.isArray() || ((Boolean) typeDescription.getComponentType().accept(this)).booleanValue()));
                    }

                    /* JADX WARN: Removed duplicated region for block: B:5:0x001d  */
                    /*
                        Code decompiled incorrectly, please refer to instructions dump.
                        To view partially-correct add '--show-bad-code' argument
                    */
                    private boolean isValid(net.bytebuddy.description.type.TypeDescription.Generic r4) {
                        /*
                            r3 = this;
                            java.util.HashSet r0 = new java.util.HashSet
                            r1 = r0
                            r1.<init>()
                            r5 = r0
                            r0 = r4
                            net.bytebuddy.description.annotation.AnnotationList r0 = r0.getDeclaredAnnotations()
                            java.util.Iterator r0 = r0.iterator()
                            r6 = r0
                        L14:
                            r0 = r6
                            boolean r0 = r0.hasNext()
                            if (r0 == 0) goto L50
                            r0 = r6
                            java.lang.Object r0 = r0.next()
                            net.bytebuddy.description.annotation.AnnotationDescription r0 = (net.bytebuddy.description.annotation.AnnotationDescription) r0
                            r7 = r0
                            r0 = r7
                            java.util.Set r0 = r0.getElementTypes()
                            r1 = r3
                            java.lang.annotation.ElementType r1 = r1.typeUse
                            boolean r0 = r0.contains(r1)
                            if (r0 == 0) goto L4b
                            r0 = r5
                            r1 = r7
                            net.bytebuddy.description.type.TypeDescription r1 = r1.getAnnotationType()
                            boolean r0 = r0.add(r1)
                            if (r0 != 0) goto L4d
                        L4b:
                            r0 = 0
                            return r0
                        L4d:
                            goto L14
                        L50:
                            r0 = 1
                            return r0
                        */
                        throw new UnsupportedOperationException("Method not decompiled: net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Validator.ForTypeAnnotations.isValid(net.bytebuddy.description.type.TypeDescription$Generic):boolean");
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$Visitor$Reifying.class */
            public enum Reifying implements Visitor<Generic> {
                INITIATING { // from class: net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Reifying.1
                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Reifying, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public /* bridge */ /* synthetic */ Generic onNonGenericType(Generic generic) {
                        return super.onNonGenericType(generic);
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Reifying, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public /* bridge */ /* synthetic */ Generic onTypeVariable(Generic generic) {
                        return super.onTypeVariable(generic);
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Reifying, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public /* bridge */ /* synthetic */ Generic onWildcard(Generic generic) {
                        return super.onWildcard(generic);
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Reifying, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public /* bridge */ /* synthetic */ Generic onGenericArray(Generic generic) {
                        return super.onGenericArray(generic);
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public Generic onParameterizedType(Generic parameterizedType) {
                        return parameterizedType;
                    }
                },
                INHERITING { // from class: net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Reifying.2
                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Reifying, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public /* bridge */ /* synthetic */ Generic onNonGenericType(Generic generic) {
                        return super.onNonGenericType(generic);
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Reifying, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public /* bridge */ /* synthetic */ Generic onTypeVariable(Generic generic) {
                        return super.onTypeVariable(generic);
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Reifying, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public /* bridge */ /* synthetic */ Generic onWildcard(Generic generic) {
                        return super.onWildcard(generic);
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Reifying, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public /* bridge */ /* synthetic */ Generic onGenericArray(Generic generic) {
                        return super.onGenericArray(generic);
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public Generic onParameterizedType(Generic parameterizedType) {
                        return new OfParameterizedType.ForReifiedType(parameterizedType);
                    }
                };

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Generic onGenericArray(Generic genericArray) {
                    throw new IllegalArgumentException("Cannot reify a generic array: " + genericArray);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Generic onWildcard(Generic wildcard) {
                    throw new IllegalArgumentException("Cannot reify a wildcard: " + wildcard);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Generic onTypeVariable(Generic typeVariable) {
                    throw new IllegalArgumentException("Cannot reify a type variable: " + typeVariable);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Generic onNonGenericType(Generic typeDescription) {
                    TypeDescription erasure = typeDescription.asErasure();
                    return erasure.isGenerified() ? new OfNonGenericType.ForReifiedErasure(erasure) : typeDescription;
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$Visitor$ForSignatureVisitor.class */
            public static class ForSignatureVisitor implements Visitor<SignatureVisitor> {
                private static final int ONLY_CHARACTER = 0;
                protected final SignatureVisitor signatureVisitor;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.signatureVisitor.equals(((ForSignatureVisitor) obj).signatureVisitor);
                }

                public int hashCode() {
                    return (17 * 31) + this.signatureVisitor.hashCode();
                }

                public ForSignatureVisitor(SignatureVisitor signatureVisitor) {
                    this.signatureVisitor = signatureVisitor;
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public SignatureVisitor onGenericArray(Generic genericArray) {
                    genericArray.getComponentType().accept(new ForSignatureVisitor(this.signatureVisitor.visitArrayType()));
                    return this.signatureVisitor;
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public SignatureVisitor onWildcard(Generic wildcard) {
                    throw new IllegalStateException("Unexpected wildcard: " + wildcard);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public SignatureVisitor onParameterizedType(Generic parameterizedType) {
                    onOwnableType(parameterizedType);
                    this.signatureVisitor.visitEnd();
                    return this.signatureVisitor;
                }

                private void onOwnableType(Generic ownableType) {
                    Generic ownerType = ownableType.getOwnerType();
                    if (ownerType != null && ownerType.getSort().isParameterized()) {
                        onOwnableType(ownerType);
                        this.signatureVisitor.visitInnerClassType(ownableType.asErasure().getSimpleName());
                    } else {
                        this.signatureVisitor.visitClassType(ownableType.asErasure().getInternalName());
                    }
                    for (Generic typeArgument : ownableType.getTypeArguments()) {
                        typeArgument.accept(new OfTypeArgument(this.signatureVisitor));
                    }
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public SignatureVisitor onTypeVariable(Generic typeVariable) {
                    this.signatureVisitor.visitTypeVariable(typeVariable.getSymbol());
                    return this.signatureVisitor;
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public SignatureVisitor onNonGenericType(Generic typeDescription) {
                    if (typeDescription.isArray()) {
                        typeDescription.getComponentType().accept(new ForSignatureVisitor(this.signatureVisitor.visitArrayType()));
                    } else if (typeDescription.isPrimitive()) {
                        this.signatureVisitor.visitBaseType(typeDescription.asErasure().getDescriptor().charAt(0));
                    } else {
                        this.signatureVisitor.visitClassType(typeDescription.asErasure().getInternalName());
                        this.signatureVisitor.visitEnd();
                    }
                    return this.signatureVisitor;
                }

                /* JADX INFO: Access modifiers changed from: protected */
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$Visitor$ForSignatureVisitor$OfTypeArgument.class */
                public static class OfTypeArgument extends ForSignatureVisitor {
                    protected OfTypeArgument(SignatureVisitor signatureVisitor) {
                        super(signatureVisitor);
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.ForSignatureVisitor, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public SignatureVisitor onWildcard(Generic wildcard) {
                        TypeList.Generic upperBounds = wildcard.getUpperBounds();
                        TypeList.Generic lowerBounds = wildcard.getLowerBounds();
                        if (lowerBounds.isEmpty() && upperBounds.getOnly().represents(Object.class)) {
                            this.signatureVisitor.visitTypeArgument();
                        } else if (!lowerBounds.isEmpty()) {
                            lowerBounds.getOnly().accept(new ForSignatureVisitor(this.signatureVisitor.visitTypeArgument('-')));
                        } else {
                            upperBounds.getOnly().accept(new ForSignatureVisitor(this.signatureVisitor.visitTypeArgument('+')));
                        }
                        return this.signatureVisitor;
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.ForSignatureVisitor, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public SignatureVisitor onGenericArray(Generic genericArray) {
                        genericArray.accept(new ForSignatureVisitor(this.signatureVisitor.visitTypeArgument('=')));
                        return this.signatureVisitor;
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.ForSignatureVisitor, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public SignatureVisitor onParameterizedType(Generic parameterizedType) {
                        parameterizedType.accept(new ForSignatureVisitor(this.signatureVisitor.visitTypeArgument('=')));
                        return this.signatureVisitor;
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.ForSignatureVisitor, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public SignatureVisitor onTypeVariable(Generic typeVariable) {
                        typeVariable.accept(new ForSignatureVisitor(this.signatureVisitor.visitTypeArgument('=')));
                        return this.signatureVisitor;
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.ForSignatureVisitor, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public SignatureVisitor onNonGenericType(Generic typeDescription) {
                        typeDescription.accept(new ForSignatureVisitor(this.signatureVisitor.visitTypeArgument('=')));
                        return this.signatureVisitor;
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$Visitor$Substitutor.class */
            public static abstract class Substitutor implements Visitor<Generic> {
                protected abstract Generic onSimpleType(Generic generic);

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Generic onParameterizedType(Generic parameterizedType) {
                    Generic ownerType = parameterizedType.getOwnerType();
                    ArrayList arrayList = new ArrayList(parameterizedType.getTypeArguments().size());
                    for (Generic typeArgument : parameterizedType.getTypeArguments()) {
                        arrayList.add(typeArgument.accept(this));
                    }
                    return new OfParameterizedType.Latent(((Generic) parameterizedType.asRawType().accept(this)).asErasure(), ownerType == null ? Generic.UNDEFINED : (Generic) ownerType.accept(this), arrayList, parameterizedType);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Generic onGenericArray(Generic genericArray) {
                    return new OfGenericArray.Latent((Generic) genericArray.getComponentType().accept(this), genericArray);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Generic onWildcard(Generic wildcard) {
                    return new OfWildcardType.Latent(wildcard.getUpperBounds().accept(this), wildcard.getLowerBounds().accept(this), wildcard);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Generic onNonGenericType(Generic typeDescription) {
                    if (typeDescription.isArray()) {
                        return new OfGenericArray.Latent((Generic) typeDescription.getComponentType().accept(this), typeDescription);
                    }
                    return onSimpleType(typeDescription);
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$Visitor$Substitutor$WithoutTypeSubstitution.class */
                public static abstract class WithoutTypeSubstitution extends Substitutor {
                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Substitutor, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public /* bridge */ /* synthetic */ Generic onParameterizedType(Generic generic) {
                        return super.onParameterizedType(generic);
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Substitutor, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public /* bridge */ /* synthetic */ Generic onWildcard(Generic generic) {
                        return super.onWildcard(generic);
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Substitutor, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public /* bridge */ /* synthetic */ Generic onGenericArray(Generic generic) {
                        return super.onGenericArray(generic);
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Substitutor, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public Generic onNonGenericType(Generic typeDescription) {
                        return typeDescription;
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Substitutor
                    protected Generic onSimpleType(Generic typeDescription) {
                        return typeDescription;
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$Visitor$Substitutor$ForAttachment.class */
                public static class ForAttachment extends Substitutor {
                    private final TypeDescription declaringType;
                    private final TypeVariableSource typeVariableSource;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.declaringType.equals(((ForAttachment) obj).declaringType) && this.typeVariableSource.equals(((ForAttachment) obj).typeVariableSource);
                    }

                    public int hashCode() {
                        return (((17 * 31) + this.declaringType.hashCode()) * 31) + this.typeVariableSource.hashCode();
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Substitutor, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public /* bridge */ /* synthetic */ Generic onNonGenericType(Generic generic) {
                        return super.onNonGenericType(generic);
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Substitutor, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public /* bridge */ /* synthetic */ Generic onParameterizedType(Generic generic) {
                        return super.onParameterizedType(generic);
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Substitutor, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public /* bridge */ /* synthetic */ Generic onWildcard(Generic generic) {
                        return super.onWildcard(generic);
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Substitutor, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public /* bridge */ /* synthetic */ Generic onGenericArray(Generic generic) {
                        return super.onGenericArray(generic);
                    }

                    protected ForAttachment(TypeDefinition declaringType, TypeVariableSource typeVariableSource) {
                        this(declaringType.asErasure(), typeVariableSource);
                    }

                    protected ForAttachment(TypeDescription declaringType, TypeVariableSource typeVariableSource) {
                        this.declaringType = declaringType;
                        this.typeVariableSource = typeVariableSource;
                    }

                    public static ForAttachment of(TypeDescription typeDescription) {
                        return new ForAttachment(typeDescription, (TypeVariableSource) typeDescription);
                    }

                    public static ForAttachment of(FieldDescription fieldDescription) {
                        return new ForAttachment(fieldDescription.getDeclaringType(), fieldDescription.getDeclaringType().asErasure());
                    }

                    public static ForAttachment of(MethodDescription methodDescription) {
                        return new ForAttachment(methodDescription.getDeclaringType(), methodDescription);
                    }

                    public static ForAttachment of(ParameterDescription parameterDescription) {
                        return new ForAttachment(parameterDescription.getDeclaringMethod().getDeclaringType(), parameterDescription.getDeclaringMethod());
                    }

                    public static ForAttachment of(RecordComponentDescription recordComponentDescription) {
                        return new ForAttachment(recordComponentDescription.getDeclaringType(), recordComponentDescription.getDeclaringType().asErasure());
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public Generic onTypeVariable(Generic typeVariable) {
                        Generic attachedVariable = this.typeVariableSource.findVariable(typeVariable.getSymbol());
                        if (attachedVariable == null) {
                            throw new IllegalArgumentException("Cannot attach undefined variable: " + typeVariable);
                        }
                        return new OfTypeVariable.WithAnnotationOverlay(attachedVariable, typeVariable);
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Substitutor
                    protected Generic onSimpleType(Generic typeDescription) {
                        return typeDescription.represents(TargetType.class) ? new OfNonGenericType.Latent(this.declaringType, typeDescription) : typeDescription;
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$Visitor$Substitutor$ForDetachment.class */
                public static class ForDetachment extends Substitutor {
                    private final ElementMatcher<? super TypeDescription> typeMatcher;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.typeMatcher.equals(((ForDetachment) obj).typeMatcher);
                    }

                    public int hashCode() {
                        return (17 * 31) + this.typeMatcher.hashCode();
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Substitutor, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public /* bridge */ /* synthetic */ Generic onNonGenericType(Generic generic) {
                        return super.onNonGenericType(generic);
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Substitutor, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public /* bridge */ /* synthetic */ Generic onParameterizedType(Generic generic) {
                        return super.onParameterizedType(generic);
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Substitutor, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public /* bridge */ /* synthetic */ Generic onWildcard(Generic generic) {
                        return super.onWildcard(generic);
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Substitutor, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public /* bridge */ /* synthetic */ Generic onGenericArray(Generic generic) {
                        return super.onGenericArray(generic);
                    }

                    public ForDetachment(ElementMatcher<? super TypeDescription> typeMatcher) {
                        this.typeMatcher = typeMatcher;
                    }

                    public static Visitor<Generic> of(TypeDefinition typeDefinition) {
                        return new ForDetachment(ElementMatchers.is(typeDefinition));
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public Generic onTypeVariable(Generic typeVariable) {
                        return new OfTypeVariable.Symbolic(typeVariable.getSymbol(), typeVariable);
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Substitutor
                    protected Generic onSimpleType(Generic typeDescription) {
                        return this.typeMatcher.matches(typeDescription.asErasure()) ? new OfNonGenericType.Latent(TargetType.DESCRIPTION, typeDescription.getOwnerType(), typeDescription) : typeDescription;
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$Visitor$Substitutor$ForTypeVariableBinding.class */
                public static class ForTypeVariableBinding extends WithoutTypeSubstitution {
                    private final Generic parameterizedType;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.parameterizedType.equals(((ForTypeVariableBinding) obj).parameterizedType);
                    }

                    public int hashCode() {
                        return (17 * 31) + this.parameterizedType.hashCode();
                    }

                    protected ForTypeVariableBinding(Generic parameterizedType) {
                        this.parameterizedType = parameterizedType;
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public Generic onTypeVariable(Generic typeVariable) {
                        return (Generic) typeVariable.getTypeVariableSource().accept(new TypeVariableSubstitutor(typeVariable));
                    }

                    /* JADX INFO: Access modifiers changed from: protected */
                    @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$Visitor$Substitutor$ForTypeVariableBinding$TypeVariableSubstitutor.class */
                    public class TypeVariableSubstitutor implements TypeVariableSource.Visitor<Generic> {
                        private final Generic typeVariable;

                        public boolean equals(Object obj) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.typeVariable.equals(((TypeVariableSubstitutor) obj).typeVariable) && ForTypeVariableBinding.this.equals(ForTypeVariableBinding.this);
                        }

                        public int hashCode() {
                            return (((17 * 31) + this.typeVariable.hashCode()) * 31) + ForTypeVariableBinding.this.hashCode();
                        }

                        protected TypeVariableSubstitutor(Generic typeVariable) {
                            this.typeVariable = typeVariable;
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // net.bytebuddy.description.TypeVariableSource.Visitor
                        public Generic onType(TypeDescription typeDescription) {
                            Generic typeArgument = ForTypeVariableBinding.this.parameterizedType.findBindingOf(this.typeVariable);
                            return typeArgument == null ? this.typeVariable.asRawType() : typeArgument;
                        }

                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // net.bytebuddy.description.TypeVariableSource.Visitor
                        public Generic onMethod(MethodDescription.InDefinedShape methodDescription) {
                            return new RetainedMethodTypeVariable(this.typeVariable);
                        }
                    }

                    /* JADX INFO: Access modifiers changed from: protected */
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$Visitor$Substitutor$ForTypeVariableBinding$RetainedMethodTypeVariable.class */
                    public class RetainedMethodTypeVariable extends OfTypeVariable {
                        private final Generic typeVariable;

                        protected RetainedMethodTypeVariable(Generic typeVariable) {
                            this.typeVariable = typeVariable;
                        }

                        @Override // net.bytebuddy.description.type.TypeDescription.Generic
                        public TypeList.Generic getUpperBounds() {
                            return this.typeVariable.getUpperBounds().accept(ForTypeVariableBinding.this);
                        }

                        @Override // net.bytebuddy.description.type.TypeDescription.Generic
                        public TypeVariableSource getTypeVariableSource() {
                            return this.typeVariable.getTypeVariableSource();
                        }

                        @Override // net.bytebuddy.description.type.TypeDescription.Generic
                        public String getSymbol() {
                            return this.typeVariable.getSymbol();
                        }

                        @Override // net.bytebuddy.description.annotation.AnnotationSource
                        public AnnotationList getDeclaredAnnotations() {
                            return this.typeVariable.getDeclaredAnnotations();
                        }
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$Visitor$Substitutor$ForTokenNormalization.class */
                public static class ForTokenNormalization extends Substitutor {
                    private final TypeDescription typeDescription;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.typeDescription.equals(((ForTokenNormalization) obj).typeDescription);
                    }

                    public int hashCode() {
                        return (17 * 31) + this.typeDescription.hashCode();
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Substitutor, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public /* bridge */ /* synthetic */ Generic onNonGenericType(Generic generic) {
                        return super.onNonGenericType(generic);
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Substitutor, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public /* bridge */ /* synthetic */ Generic onParameterizedType(Generic generic) {
                        return super.onParameterizedType(generic);
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Substitutor, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public /* bridge */ /* synthetic */ Generic onWildcard(Generic generic) {
                        return super.onWildcard(generic);
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Substitutor, net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public /* bridge */ /* synthetic */ Generic onGenericArray(Generic generic) {
                        return super.onGenericArray(generic);
                    }

                    public ForTokenNormalization(TypeDescription typeDescription) {
                        this.typeDescription = typeDescription;
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Substitutor
                    protected Generic onSimpleType(Generic typeDescription) {
                        return typeDescription.represents(TargetType.class) ? new OfNonGenericType.Latent(this.typeDescription, typeDescription) : typeDescription;
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                    public Generic onTypeVariable(Generic typeVariable) {
                        return new OfTypeVariable.Symbolic(typeVariable.getSymbol(), typeVariable);
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$Visitor$ForRawType.class */
            public static class ForRawType implements Visitor<Generic> {
                private final TypeDescription declaringType;

                public ForRawType(TypeDescription declaringType) {
                    this.declaringType = declaringType;
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Generic onGenericArray(Generic genericArray) {
                    return this.declaringType.isGenerified() ? new OfNonGenericType.Latent(genericArray.asErasure(), genericArray) : genericArray;
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Generic onWildcard(Generic wildcard) {
                    throw new IllegalStateException("Did not expect wildcard on top-level: " + wildcard);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Generic onParameterizedType(Generic parameterizedType) {
                    return this.declaringType.isGenerified() ? new OfNonGenericType.Latent(parameterizedType.asErasure(), parameterizedType) : parameterizedType;
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Generic onTypeVariable(Generic typeVariable) {
                    return this.declaringType.isGenerified() ? new OfNonGenericType.Latent(typeVariable.asErasure(), typeVariable) : typeVariable;
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public Generic onNonGenericType(Generic typeDescription) {
                    return typeDescription;
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$Visitor$Reducing.class */
            public static class Reducing implements Visitor<TypeDescription> {
                private final TypeDescription declaringType;
                private final List<? extends TypeVariableToken> typeVariableTokens;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.declaringType.equals(((Reducing) obj).declaringType) && this.typeVariableTokens.equals(((Reducing) obj).typeVariableTokens);
                }

                public int hashCode() {
                    return (((17 * 31) + this.declaringType.hashCode()) * 31) + this.typeVariableTokens.hashCode();
                }

                public Reducing(TypeDescription declaringType, TypeVariableToken... typeVariableToken) {
                    this(declaringType, Arrays.asList(typeVariableToken));
                }

                public Reducing(TypeDescription declaringType, List<? extends TypeVariableToken> typeVariableTokens) {
                    this.declaringType = declaringType;
                    this.typeVariableTokens = typeVariableTokens;
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public TypeDescription onGenericArray(Generic genericArray) {
                    TypeDescription asErasure;
                    Generic targetType = genericArray;
                    int arity = 0;
                    do {
                        targetType = targetType.getComponentType();
                        arity++;
                    } while (targetType.isArray());
                    if (targetType.getSort().isTypeVariable()) {
                        asErasure = ArrayProjection.of(this.declaringType.findVariable(targetType.getSymbol()).asErasure(), arity);
                    } else {
                        asErasure = genericArray.asErasure();
                    }
                    return TargetType.resolve(asErasure, this.declaringType);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public TypeDescription onWildcard(Generic wildcard) {
                    throw new IllegalStateException("A wildcard cannot be a top-level type: " + wildcard);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public TypeDescription onParameterizedType(Generic parameterizedType) {
                    return TargetType.resolve(parameterizedType.asErasure(), this.declaringType);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public TypeDescription onTypeVariable(Generic typeVariable) {
                    for (TypeVariableToken typeVariableToken : this.typeVariableTokens) {
                        if (typeVariable.getSymbol().equals(typeVariableToken.getSymbol())) {
                            return (TypeDescription) ((Generic) typeVariableToken.getBounds().get(0)).accept(this);
                        }
                    }
                    return TargetType.resolve(this.declaringType.findVariable(typeVariable.getSymbol()).asErasure(), this.declaringType);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Visitor
                public TypeDescription onNonGenericType(Generic typeDescription) {
                    return TargetType.resolve(typeDescription.asErasure(), this.declaringType);
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$AnnotationReader.class */
        public interface AnnotationReader {
            public static final Dispatcher DISPATCHER = (Dispatcher) AccessController.doPrivileged(Dispatcher.CreationAction.INSTANCE);

            AnnotatedElement resolve();

            AnnotationList asList();

            AnnotationReader ofWildcardUpperBoundType(int i);

            AnnotationReader ofWildcardLowerBoundType(int i);

            AnnotationReader ofTypeVariableBoundType(int i);

            AnnotationReader ofTypeArgument(int i);

            AnnotationReader ofOwnerType();

            AnnotationReader ofOuterClass();

            AnnotationReader ofComponentType();

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$AnnotationReader$Dispatcher.class */
            public interface Dispatcher {
                public static final Object[] NO_ARGUMENTS = new Object[0];

                AnnotationReader resolveTypeVariable(TypeVariable<?> typeVariable);

                AnnotationReader resolveSuperClassType(Class<?> cls);

                AnnotationReader resolveInterfaceType(Class<?> cls, int i);

                AnnotationReader resolveFieldType(Field field);

                AnnotationReader resolveReturnType(Method method);

                AnnotationReader resolveParameterType(AccessibleObject accessibleObject, int i);

                AnnotationReader resolveExceptionType(AccessibleObject accessibleObject, int i);

                Generic resolveReceiverType(AccessibleObject accessibleObject);

                Generic resolve(AnnotatedElement annotatedElement);

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$AnnotationReader$Dispatcher$CreationAction.class */
                public enum CreationAction implements PrivilegedAction<Dispatcher> {
                    INSTANCE;

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    public Dispatcher run() {
                        try {
                            return new ForJava8CapableVm(Class.class.getMethod("getAnnotatedSuperclass", new Class[0]), Class.class.getMethod("getAnnotatedInterfaces", new Class[0]), Field.class.getMethod("getAnnotatedType", new Class[0]), Method.class.getMethod("getAnnotatedReturnType", new Class[0]), Class.forName("java.lang.reflect.Executable").getMethod("getAnnotatedParameterTypes", new Class[0]), Class.forName("java.lang.reflect.Executable").getMethod("getAnnotatedExceptionTypes", new Class[0]), Class.forName("java.lang.reflect.Executable").getMethod("getAnnotatedReceiverType", new Class[0]), Class.forName("java.lang.reflect.AnnotatedType").getMethod("getType", new Class[0]));
                        } catch (RuntimeException exception) {
                            throw exception;
                        } catch (Exception e) {
                            return ForLegacyVm.INSTANCE;
                        }
                    }
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$AnnotationReader$Dispatcher$ForLegacyVm.class */
                public enum ForLegacyVm implements Dispatcher {
                    INSTANCE;

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Dispatcher
                    public AnnotationReader resolveTypeVariable(TypeVariable<?> typeVariable) {
                        return NoOp.INSTANCE;
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Dispatcher
                    public AnnotationReader resolveSuperClassType(Class<?> type) {
                        return NoOp.INSTANCE;
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Dispatcher
                    public AnnotationReader resolveInterfaceType(Class<?> type, int index) {
                        return NoOp.INSTANCE;
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Dispatcher
                    public AnnotationReader resolveFieldType(Field field) {
                        return NoOp.INSTANCE;
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Dispatcher
                    public AnnotationReader resolveReturnType(Method method) {
                        return NoOp.INSTANCE;
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Dispatcher
                    public AnnotationReader resolveParameterType(AccessibleObject executable, int index) {
                        return NoOp.INSTANCE;
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Dispatcher
                    public AnnotationReader resolveExceptionType(AccessibleObject executable, int index) {
                        return NoOp.INSTANCE;
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Dispatcher
                    public Generic resolveReceiverType(AccessibleObject executable) {
                        return Generic.UNDEFINED;
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Dispatcher
                    public Generic resolve(AnnotatedElement annotatedType) {
                        throw new UnsupportedOperationException("Loaded annotated type cannot be represented on this VM");
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$AnnotationReader$Dispatcher$ForJava8CapableVm.class */
                public static class ForJava8CapableVm implements Dispatcher {
                    private final Method getAnnotatedSuperclass;
                    private final Method getAnnotatedInterfaces;
                    private final Method getAnnotatedType;
                    private final Method getAnnotatedReturnType;
                    private final Method getAnnotatedParameterTypes;
                    private final Method getAnnotatedExceptionTypes;
                    private final Method getAnnotatedReceiverType;
                    private final Method getType;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.getAnnotatedSuperclass.equals(((ForJava8CapableVm) obj).getAnnotatedSuperclass) && this.getAnnotatedInterfaces.equals(((ForJava8CapableVm) obj).getAnnotatedInterfaces) && this.getAnnotatedType.equals(((ForJava8CapableVm) obj).getAnnotatedType) && this.getAnnotatedReturnType.equals(((ForJava8CapableVm) obj).getAnnotatedReturnType) && this.getAnnotatedParameterTypes.equals(((ForJava8CapableVm) obj).getAnnotatedParameterTypes) && this.getAnnotatedExceptionTypes.equals(((ForJava8CapableVm) obj).getAnnotatedExceptionTypes) && this.getAnnotatedReceiverType.equals(((ForJava8CapableVm) obj).getAnnotatedReceiverType) && this.getType.equals(((ForJava8CapableVm) obj).getType);
                    }

                    public int hashCode() {
                        return (((((((((((((((17 * 31) + this.getAnnotatedSuperclass.hashCode()) * 31) + this.getAnnotatedInterfaces.hashCode()) * 31) + this.getAnnotatedType.hashCode()) * 31) + this.getAnnotatedReturnType.hashCode()) * 31) + this.getAnnotatedParameterTypes.hashCode()) * 31) + this.getAnnotatedExceptionTypes.hashCode()) * 31) + this.getAnnotatedReceiverType.hashCode()) * 31) + this.getType.hashCode();
                    }

                    protected ForJava8CapableVm(Method getAnnotatedSuperclass, Method getAnnotatedInterfaces, Method getAnnotatedType, Method getAnnotatedReturnType, Method getAnnotatedParameterTypes, Method getAnnotatedExceptionTypes, Method getAnnotatedReceiverType, Method getType) {
                        this.getAnnotatedSuperclass = getAnnotatedSuperclass;
                        this.getAnnotatedInterfaces = getAnnotatedInterfaces;
                        this.getAnnotatedType = getAnnotatedType;
                        this.getAnnotatedReturnType = getAnnotatedReturnType;
                        this.getAnnotatedParameterTypes = getAnnotatedParameterTypes;
                        this.getAnnotatedExceptionTypes = getAnnotatedExceptionTypes;
                        this.getAnnotatedReceiverType = getAnnotatedReceiverType;
                        this.getType = getType;
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Dispatcher
                    public AnnotationReader resolveTypeVariable(TypeVariable<?> typeVariable) {
                        return new AnnotatedTypeVariableType(typeVariable);
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Dispatcher
                    public AnnotationReader resolveSuperClassType(Class<?> type) {
                        return new AnnotatedSuperClass(type);
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Dispatcher
                    public AnnotationReader resolveInterfaceType(Class<?> type, int index) {
                        return new AnnotatedInterfaceType(type, index);
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Dispatcher
                    public AnnotationReader resolveFieldType(Field field) {
                        return new AnnotatedFieldType(field);
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Dispatcher
                    public AnnotationReader resolveReturnType(Method method) {
                        return new AnnotatedReturnType(method);
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Dispatcher
                    public AnnotationReader resolveParameterType(AccessibleObject executable, int index) {
                        return new AnnotatedParameterizedType(executable, index);
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Dispatcher
                    public AnnotationReader resolveExceptionType(AccessibleObject executable, int index) {
                        return new AnnotatedExceptionType(executable, index);
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Dispatcher
                    public Generic resolveReceiverType(AccessibleObject executable) {
                        try {
                            return resolve((AnnotatedElement) this.getAnnotatedReceiverType.invoke(executable, NO_ARGUMENTS));
                        } catch (IllegalAccessException exception) {
                            throw new IllegalStateException("Cannot access java.lang.reflect.Executable#getAnnotatedReceiverType", exception);
                        } catch (InvocationTargetException exception2) {
                            throw new IllegalStateException("Error invoking java.lang.reflect.Executable#getAnnotatedReceiverType", exception2.getCause());
                        }
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Dispatcher
                    public Generic resolve(AnnotatedElement annotatedType) {
                        try {
                            return annotatedType == null ? Generic.UNDEFINED : TypeDefinition.Sort.describe((Type) this.getType.invoke(annotatedType, NO_ARGUMENTS), new Resolved(annotatedType));
                        } catch (IllegalAccessException exception) {
                            throw new IllegalStateException("Cannot access java.lang.reflect.AnnotatedType#getType", exception);
                        } catch (InvocationTargetException exception2) {
                            throw new IllegalStateException("Error invoking java.lang.reflect.AnnotatedType#getType", exception2.getCause());
                        }
                    }

                    /* JADX INFO: Access modifiers changed from: protected */
                    @HashCodeAndEqualsPlugin.Enhance
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$AnnotationReader$Dispatcher$ForJava8CapableVm$Resolved.class */
                    public static class Resolved extends Delegator {
                        private final AnnotatedElement annotatedElement;

                        public boolean equals(Object obj) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.annotatedElement.equals(((Resolved) obj).annotatedElement);
                        }

                        public int hashCode() {
                            return (17 * 31) + this.annotatedElement.hashCode();
                        }

                        protected Resolved(AnnotatedElement annotatedElement) {
                            this.annotatedElement = annotatedElement;
                        }

                        @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader
                        public AnnotatedElement resolve() {
                            return this.annotatedElement;
                        }
                    }

                    @HashCodeAndEqualsPlugin.Enhance
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$AnnotationReader$Dispatcher$ForJava8CapableVm$AnnotatedTypeVariableType.class */
                    protected static class AnnotatedTypeVariableType extends Delegator {
                        private final TypeVariable<?> typeVariable;

                        public boolean equals(Object obj) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.typeVariable.equals(((AnnotatedTypeVariableType) obj).typeVariable);
                        }

                        public int hashCode() {
                            return (17 * 31) + this.typeVariable.hashCode();
                        }

                        protected AnnotatedTypeVariableType(TypeVariable<?> typeVariable) {
                            this.typeVariable = typeVariable;
                        }

                        /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.reflect.TypeVariable<?>, java.lang.reflect.AnnotatedElement] */
                        @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader
                        public AnnotatedElement resolve() {
                            return this.typeVariable;
                        }

                        @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Delegator, net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader
                        public AnnotationReader ofTypeVariableBoundType(int index) {
                            return new ForTypeVariableBoundType.OfFormalTypeVariable(this.typeVariable, index);
                        }
                    }

                    @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$AnnotationReader$Dispatcher$ForJava8CapableVm$AnnotatedSuperClass.class */
                    protected class AnnotatedSuperClass extends Delegator {
                        private final Class<?> type;

                        public boolean equals(Object obj) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.type.equals(((AnnotatedSuperClass) obj).type) && ForJava8CapableVm.this.equals(ForJava8CapableVm.this);
                        }

                        public int hashCode() {
                            return (((17 * 31) + this.type.hashCode()) * 31) + ForJava8CapableVm.this.hashCode();
                        }

                        protected AnnotatedSuperClass(Class<?> type) {
                            this.type = type;
                        }

                        @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader
                        public AnnotatedElement resolve() {
                            try {
                                return (AnnotatedElement) ForJava8CapableVm.this.getAnnotatedSuperclass.invoke(this.type, NO_ARGUMENTS);
                            } catch (IllegalAccessException exception) {
                                throw new IllegalStateException("Cannot access java.lang.Class#getAnnotatedSuperclass", exception);
                            } catch (InvocationTargetException exception2) {
                                throw new IllegalStateException("Error invoking java.lang.Class#getAnnotatedSuperclass", exception2.getCause());
                            }
                        }
                    }

                    @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$AnnotationReader$Dispatcher$ForJava8CapableVm$AnnotatedInterfaceType.class */
                    protected class AnnotatedInterfaceType extends Delegator {
                        private final Class<?> type;
                        private final int index;

                        public boolean equals(Object obj) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.index == ((AnnotatedInterfaceType) obj).index && this.type.equals(((AnnotatedInterfaceType) obj).type) && ForJava8CapableVm.this.equals(ForJava8CapableVm.this);
                        }

                        public int hashCode() {
                            return (((((17 * 31) + this.type.hashCode()) * 31) + this.index) * 31) + ForJava8CapableVm.this.hashCode();
                        }

                        protected AnnotatedInterfaceType(Class<?> type, int index) {
                            this.type = type;
                            this.index = index;
                        }

                        @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader
                        public AnnotatedElement resolve() {
                            try {
                                return (AnnotatedElement) Array.get(ForJava8CapableVm.this.getAnnotatedInterfaces.invoke(this.type, new Object[0]), this.index);
                            } catch (IllegalAccessException exception) {
                                throw new IllegalStateException("Cannot access java.lang.Class#getAnnotatedInterfaces", exception);
                            } catch (InvocationTargetException exception2) {
                                throw new IllegalStateException("Error invoking java.lang.Class#getAnnotatedInterfaces", exception2.getCause());
                            }
                        }
                    }

                    @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$AnnotationReader$Dispatcher$ForJava8CapableVm$AnnotatedFieldType.class */
                    protected class AnnotatedFieldType extends Delegator {
                        private final Field field;

                        public boolean equals(Object obj) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.field.equals(((AnnotatedFieldType) obj).field) && ForJava8CapableVm.this.equals(ForJava8CapableVm.this);
                        }

                        public int hashCode() {
                            return (((17 * 31) + this.field.hashCode()) * 31) + ForJava8CapableVm.this.hashCode();
                        }

                        protected AnnotatedFieldType(Field field) {
                            this.field = field;
                        }

                        @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader
                        public AnnotatedElement resolve() {
                            try {
                                return (AnnotatedElement) ForJava8CapableVm.this.getAnnotatedType.invoke(this.field, NO_ARGUMENTS);
                            } catch (IllegalAccessException exception) {
                                throw new IllegalStateException("Cannot access java.lang.reflect.Field#getAnnotatedType", exception);
                            } catch (InvocationTargetException exception2) {
                                throw new IllegalStateException("Error invoking java.lang.reflect.Field#getAnnotatedType", exception2.getCause());
                            }
                        }
                    }

                    @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$AnnotationReader$Dispatcher$ForJava8CapableVm$AnnotatedReturnType.class */
                    protected class AnnotatedReturnType extends Delegator {
                        private final Method method;

                        public boolean equals(Object obj) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.method.equals(((AnnotatedReturnType) obj).method) && ForJava8CapableVm.this.equals(ForJava8CapableVm.this);
                        }

                        public int hashCode() {
                            return (((17 * 31) + this.method.hashCode()) * 31) + ForJava8CapableVm.this.hashCode();
                        }

                        protected AnnotatedReturnType(Method method) {
                            this.method = method;
                        }

                        @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader
                        public AnnotatedElement resolve() {
                            try {
                                return (AnnotatedElement) ForJava8CapableVm.this.getAnnotatedReturnType.invoke(this.method, NO_ARGUMENTS);
                            } catch (IllegalAccessException exception) {
                                throw new IllegalStateException("Cannot access java.lang.reflect.Method#getAnnotatedReturnType", exception);
                            } catch (InvocationTargetException exception2) {
                                throw new IllegalStateException("Error invoking java.lang.reflect.Method#getAnnotatedReturnType", exception2.getCause());
                            }
                        }
                    }

                    @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$AnnotationReader$Dispatcher$ForJava8CapableVm$AnnotatedParameterizedType.class */
                    protected class AnnotatedParameterizedType extends Delegator {
                        private final AccessibleObject executable;
                        private final int index;

                        public boolean equals(Object obj) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.index == ((AnnotatedParameterizedType) obj).index && this.executable.equals(((AnnotatedParameterizedType) obj).executable) && ForJava8CapableVm.this.equals(ForJava8CapableVm.this);
                        }

                        public int hashCode() {
                            return (((((17 * 31) + this.executable.hashCode()) * 31) + this.index) * 31) + ForJava8CapableVm.this.hashCode();
                        }

                        protected AnnotatedParameterizedType(AccessibleObject executable, int index) {
                            this.executable = executable;
                            this.index = index;
                        }

                        @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader
                        public AnnotatedElement resolve() {
                            try {
                                return (AnnotatedElement) Array.get(ForJava8CapableVm.this.getAnnotatedParameterTypes.invoke(this.executable, NO_ARGUMENTS), this.index);
                            } catch (IllegalAccessException exception) {
                                throw new IllegalStateException("Cannot access java.lang.reflect.Executable#getAnnotatedParameterTypes", exception);
                            } catch (InvocationTargetException exception2) {
                                throw new IllegalStateException("Error invoking java.lang.reflect.Executable#getAnnotatedParameterTypes", exception2.getCause());
                            }
                        }
                    }

                    @HashCodeAndEqualsPlugin.Enhance(includeSyntheticFields = true)
                    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$AnnotationReader$Dispatcher$ForJava8CapableVm$AnnotatedExceptionType.class */
                    protected class AnnotatedExceptionType extends Delegator {
                        private final AccessibleObject executable;
                        private final int index;

                        public boolean equals(Object obj) {
                            if (this == obj) {
                                return true;
                            }
                            return obj != null && getClass() == obj.getClass() && this.index == ((AnnotatedExceptionType) obj).index && this.executable.equals(((AnnotatedExceptionType) obj).executable) && ForJava8CapableVm.this.equals(ForJava8CapableVm.this);
                        }

                        public int hashCode() {
                            return (((((17 * 31) + this.executable.hashCode()) * 31) + this.index) * 31) + ForJava8CapableVm.this.hashCode();
                        }

                        protected AnnotatedExceptionType(AccessibleObject executable, int index) {
                            this.executable = executable;
                            this.index = index;
                        }

                        @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader
                        public AnnotatedElement resolve() {
                            try {
                                return (AnnotatedElement) Array.get(ForJava8CapableVm.this.getAnnotatedExceptionTypes.invoke(this.executable, NO_ARGUMENTS), this.index);
                            } catch (IllegalAccessException exception) {
                                throw new IllegalStateException("Cannot access java.lang.reflect.Executable#getAnnotatedExceptionTypes", exception);
                            } catch (InvocationTargetException exception2) {
                                throw new IllegalStateException("Error invoking java.lang.reflect.Executable#getAnnotatedExceptionTypes", exception2.getCause());
                            }
                        }
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$AnnotationReader$NoOp.class */
            public enum NoOp implements AnnotationReader, AnnotatedElement {
                INSTANCE;

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader
                public AnnotatedElement resolve() {
                    return this;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader
                public AnnotationList asList() {
                    return new AnnotationList.Empty();
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader
                public AnnotationReader ofWildcardUpperBoundType(int index) {
                    return this;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader
                public AnnotationReader ofWildcardLowerBoundType(int index) {
                    return this;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader
                public AnnotationReader ofTypeVariableBoundType(int index) {
                    return this;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader
                public AnnotationReader ofTypeArgument(int index) {
                    return this;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader
                public AnnotationReader ofOwnerType() {
                    return this;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader
                public AnnotationReader ofOuterClass() {
                    return this;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader
                public AnnotationReader ofComponentType() {
                    return this;
                }

                @Override // java.lang.reflect.AnnotatedElement
                public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
                    throw new IllegalStateException("Cannot resolve annotations for no-op reader: " + this);
                }

                @Override // java.lang.reflect.AnnotatedElement
                public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
                    throw new IllegalStateException("Cannot resolve annotations for no-op reader: " + this);
                }

                @Override // java.lang.reflect.AnnotatedElement
                public Annotation[] getAnnotations() {
                    throw new IllegalStateException("Cannot resolve annotations for no-op reader: " + this);
                }

                @Override // java.lang.reflect.AnnotatedElement
                public Annotation[] getDeclaredAnnotations() {
                    return new Annotation[0];
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$AnnotationReader$Delegator.class */
            public static abstract class Delegator implements AnnotationReader {
                protected static final Object[] NO_ARGUMENTS = new Object[0];

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader
                public AnnotationReader ofWildcardUpperBoundType(int index) {
                    return new ForWildcardUpperBoundType(this, index);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader
                public AnnotationReader ofWildcardLowerBoundType(int index) {
                    return new ForWildcardLowerBoundType(this, index);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader
                public AnnotationReader ofTypeVariableBoundType(int index) {
                    return new ForTypeVariableBoundType(this, index);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader
                public AnnotationReader ofTypeArgument(int index) {
                    return new ForTypeArgument(this, index);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader
                public AnnotationReader ofOwnerType() {
                    return ForOwnerType.of(this);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader
                public AnnotationReader ofOuterClass() {
                    return ForOwnerType.of(this);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader
                public AnnotationReader ofComponentType() {
                    return new ForComponentType(this);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader
                public AnnotationList asList() {
                    return new AnnotationList.ForLoadedAnnotations(resolve().getDeclaredAnnotations());
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$AnnotationReader$Delegator$Chained.class */
                protected static abstract class Chained extends Delegator {
                    protected static final Method NOT_AVAILABLE = null;
                    protected final AnnotationReader annotationReader;

                    protected abstract AnnotatedElement resolve(AnnotatedElement annotatedElement);

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.annotationReader.equals(((Chained) obj).annotationReader);
                    }

                    public int hashCode() {
                        return (17 * 31) + this.annotationReader.hashCode();
                    }

                    protected Chained(AnnotationReader annotationReader) {
                        this.annotationReader = annotationReader;
                    }

                    @SuppressFBWarnings(value = {"REC_CATCH_EXCEPTION"}, justification = "Exception should not be rethrown but trigger a fallback")
                    protected static Method of(String typeName, String methodName) {
                        try {
                            return Class.forName(typeName).getMethod(methodName, new Class[0]);
                        } catch (Exception e) {
                            return NOT_AVAILABLE;
                        }
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader
                    public AnnotatedElement resolve() {
                        return resolve(this.annotationReader.resolve());
                    }
                }

                /* JADX INFO: Access modifiers changed from: protected */
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$AnnotationReader$Delegator$ForRecordComponent.class */
                public static class ForRecordComponent extends Delegator {
                    private final Object recordComponent;

                    protected ForRecordComponent(Object recordComponent) {
                        this.recordComponent = recordComponent;
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader
                    public AnnotatedElement resolve() {
                        return RecordComponentDescription.ForLoadedRecordComponent.DISPATCHER.getAnnotatedType(this.recordComponent);
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$AnnotationReader$ForWildcardUpperBoundType.class */
            public static class ForWildcardUpperBoundType extends Delegator.Chained {
                private static final Method GET_ANNOTATED_UPPER_BOUNDS = of("java.lang.reflect.AnnotatedWildcardType", "getAnnotatedUpperBounds");
                private final int index;

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Delegator.Chained
                public boolean equals(Object obj) {
                    if (super.equals(obj)) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.index == ((ForWildcardUpperBoundType) obj).index;
                    }
                    return false;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Delegator.Chained
                public int hashCode() {
                    return (super.hashCode() * 31) + this.index;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Delegator.Chained, net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader
                public /* bridge */ /* synthetic */ AnnotatedElement resolve() {
                    return super.resolve();
                }

                protected ForWildcardUpperBoundType(AnnotationReader annotationReader, int index) {
                    super(annotationReader);
                    this.index = index;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Delegator.Chained
                protected AnnotatedElement resolve(AnnotatedElement annotatedElement) {
                    if (!GET_ANNOTATED_UPPER_BOUNDS.getDeclaringClass().isInstance(annotatedElement)) {
                        return NoOp.INSTANCE;
                    }
                    try {
                        Object annotatedUpperBounds = GET_ANNOTATED_UPPER_BOUNDS.invoke(annotatedElement, NO_ARGUMENTS);
                        return Array.getLength(annotatedUpperBounds) == 0 ? NoOp.INSTANCE : (AnnotatedElement) Array.get(annotatedUpperBounds, this.index);
                    } catch (ClassCastException e) {
                        return NoOp.INSTANCE;
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.reflect.AnnotatedWildcardType#getAnnotatedUpperBounds", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.reflect.AnnotatedWildcardType#getAnnotatedUpperBounds", exception2.getCause());
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$AnnotationReader$ForWildcardLowerBoundType.class */
            public static class ForWildcardLowerBoundType extends Delegator.Chained {
                private static final Method GET_ANNOTATED_LOWER_BOUNDS = of("java.lang.reflect.AnnotatedWildcardType", "getAnnotatedLowerBounds");
                private final int index;

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Delegator.Chained
                public boolean equals(Object obj) {
                    if (super.equals(obj)) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.index == ((ForWildcardLowerBoundType) obj).index;
                    }
                    return false;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Delegator.Chained
                public int hashCode() {
                    return (super.hashCode() * 31) + this.index;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Delegator.Chained, net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader
                public /* bridge */ /* synthetic */ AnnotatedElement resolve() {
                    return super.resolve();
                }

                protected ForWildcardLowerBoundType(AnnotationReader annotationReader, int index) {
                    super(annotationReader);
                    this.index = index;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Delegator.Chained
                protected AnnotatedElement resolve(AnnotatedElement annotatedElement) {
                    if (!GET_ANNOTATED_LOWER_BOUNDS.getDeclaringClass().isInstance(annotatedElement)) {
                        return NoOp.INSTANCE;
                    }
                    try {
                        return (AnnotatedElement) Array.get(GET_ANNOTATED_LOWER_BOUNDS.invoke(annotatedElement, NO_ARGUMENTS), this.index);
                    } catch (ClassCastException e) {
                        return NoOp.INSTANCE;
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.reflect.AnnotatedWildcardType#getAnnotatedLowerBounds", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.reflect.AnnotatedWildcardType#getAnnotatedLowerBounds", exception2.getCause());
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$AnnotationReader$ForTypeVariableBoundType.class */
            public static class ForTypeVariableBoundType extends Delegator.Chained {
                private static final Method GET_ANNOTATED_BOUNDS = of("java.lang.reflect.AnnotatedTypeVariable", "getAnnotatedBounds");
                private final int index;

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Delegator.Chained
                public boolean equals(Object obj) {
                    if (super.equals(obj)) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.index == ((ForTypeVariableBoundType) obj).index;
                    }
                    return false;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Delegator.Chained
                public int hashCode() {
                    return (super.hashCode() * 31) + this.index;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Delegator.Chained, net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader
                public /* bridge */ /* synthetic */ AnnotatedElement resolve() {
                    return super.resolve();
                }

                protected ForTypeVariableBoundType(AnnotationReader annotationReader, int index) {
                    super(annotationReader);
                    this.index = index;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Delegator.Chained
                protected AnnotatedElement resolve(AnnotatedElement annotatedElement) {
                    if (!GET_ANNOTATED_BOUNDS.getDeclaringClass().isInstance(annotatedElement)) {
                        return NoOp.INSTANCE;
                    }
                    try {
                        return (AnnotatedElement) Array.get(GET_ANNOTATED_BOUNDS.invoke(annotatedElement, NO_ARGUMENTS), this.index);
                    } catch (ClassCastException e) {
                        return NoOp.INSTANCE;
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.reflect.AnnotatedTypeVariable#getAnnotatedBounds", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.reflect.AnnotatedTypeVariable#getAnnotatedBounds", exception2.getCause());
                    }
                }

                @HashCodeAndEqualsPlugin.Enhance
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$AnnotationReader$ForTypeVariableBoundType$OfFormalTypeVariable.class */
                protected static class OfFormalTypeVariable extends Delegator {
                    private static final Method GET_ANNOTATED_BOUNDS = Delegator.Chained.of(TypeVariable.class.getName(), "getAnnotatedBounds");
                    private final TypeVariable<?> typeVariable;
                    private final int index;

                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.index == ((OfFormalTypeVariable) obj).index && this.typeVariable.equals(((OfFormalTypeVariable) obj).typeVariable);
                    }

                    public int hashCode() {
                        return (((17 * 31) + this.typeVariable.hashCode()) * 31) + this.index;
                    }

                    protected OfFormalTypeVariable(TypeVariable<?> typeVariable, int index) {
                        this.typeVariable = typeVariable;
                        this.index = index;
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader
                    public AnnotatedElement resolve() {
                        try {
                            return (AnnotatedElement) Array.get(GET_ANNOTATED_BOUNDS.invoke(this.typeVariable, NO_ARGUMENTS), this.index);
                        } catch (ClassCastException e) {
                            return NoOp.INSTANCE;
                        } catch (IllegalAccessException exception) {
                            throw new IllegalStateException("Cannot access java.lang.reflect.TypeVariable#getAnnotatedBounds", exception);
                        } catch (InvocationTargetException exception2) {
                            throw new IllegalStateException("Error invoking java.lang.reflect.TypeVariable#getAnnotatedBounds", exception2.getCause());
                        }
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$AnnotationReader$ForTypeArgument.class */
            public static class ForTypeArgument extends Delegator.Chained {
                private static final Method GET_ANNOTATED_ACTUAL_TYPE_ARGUMENTS = of("java.lang.reflect.AnnotatedParameterizedType", "getAnnotatedActualTypeArguments");
                private final int index;

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Delegator.Chained
                public boolean equals(Object obj) {
                    if (super.equals(obj)) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.index == ((ForTypeArgument) obj).index;
                    }
                    return false;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Delegator.Chained
                public int hashCode() {
                    return (super.hashCode() * 31) + this.index;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Delegator.Chained, net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader
                public /* bridge */ /* synthetic */ AnnotatedElement resolve() {
                    return super.resolve();
                }

                protected ForTypeArgument(AnnotationReader annotationReader, int index) {
                    super(annotationReader);
                    this.index = index;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Delegator.Chained
                protected AnnotatedElement resolve(AnnotatedElement annotatedElement) {
                    if (!GET_ANNOTATED_ACTUAL_TYPE_ARGUMENTS.getDeclaringClass().isInstance(annotatedElement)) {
                        return NoOp.INSTANCE;
                    }
                    try {
                        return (AnnotatedElement) Array.get(GET_ANNOTATED_ACTUAL_TYPE_ARGUMENTS.invoke(annotatedElement, NO_ARGUMENTS), this.index);
                    } catch (ClassCastException e) {
                        return NoOp.INSTANCE;
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.reflect.AnnotatedParameterizedType#getAnnotatedActualTypeArguments", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.reflect.AnnotatedParameterizedType#getAnnotatedActualTypeArguments", exception2.getCause());
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$AnnotationReader$ForComponentType.class */
            public static class ForComponentType extends Delegator.Chained {
                private static final Method GET_ANNOTATED_GENERIC_COMPONENT_TYPE = of("java.lang.reflect.AnnotatedArrayType", "getAnnotatedGenericComponentType");

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Delegator.Chained, net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader
                public /* bridge */ /* synthetic */ AnnotatedElement resolve() {
                    return super.resolve();
                }

                protected ForComponentType(AnnotationReader annotationReader) {
                    super(annotationReader);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Delegator.Chained
                protected AnnotatedElement resolve(AnnotatedElement annotatedElement) {
                    if (!GET_ANNOTATED_GENERIC_COMPONENT_TYPE.getDeclaringClass().isInstance(annotatedElement)) {
                        return NoOp.INSTANCE;
                    }
                    try {
                        return (AnnotatedElement) GET_ANNOTATED_GENERIC_COMPONENT_TYPE.invoke(annotatedElement, NO_ARGUMENTS);
                    } catch (ClassCastException e) {
                        return NoOp.INSTANCE;
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.reflect.AnnotatedArrayType#getAnnotatedGenericComponentType", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.reflect.AnnotatedArrayType#getAnnotatedGenericComponentType", exception2.getCause());
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$AnnotationReader$ForOwnerType.class */
            public static class ForOwnerType extends Delegator.Chained {
                private static final Method GET_ANNOTATED_OWNER_TYPE = of("java.lang.reflect.AnnotatedType", "getAnnotatedOwnerType");

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Delegator.Chained, net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader
                public /* bridge */ /* synthetic */ AnnotatedElement resolve() {
                    return super.resolve();
                }

                /* JADX INFO: Access modifiers changed from: private */
                public static AnnotationReader of(AnnotationReader annotationReader) {
                    return GET_ANNOTATED_OWNER_TYPE == null ? NoOp.INSTANCE : new ForOwnerType(annotationReader);
                }

                protected ForOwnerType(AnnotationReader annotationReader) {
                    super(annotationReader);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AnnotationReader.Delegator.Chained
                protected AnnotatedElement resolve(AnnotatedElement annotatedElement) {
                    if (!GET_ANNOTATED_OWNER_TYPE.getDeclaringClass().isInstance(annotatedElement)) {
                        return NoOp.INSTANCE;
                    }
                    try {
                        AnnotatedElement annotatedOwnerType = (AnnotatedElement) GET_ANNOTATED_OWNER_TYPE.invoke(annotatedElement, NO_ARGUMENTS);
                        return annotatedOwnerType == null ? NoOp.INSTANCE : annotatedOwnerType;
                    } catch (ClassCastException e) {
                        return NoOp.INSTANCE;
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.reflect.AnnotatedType#getAnnotatedOwnerType", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.reflect.AnnotatedType#getAnnotatedOwnerType", exception2.getCause());
                    }
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$AbstractBase.class */
        public static abstract class AbstractBase extends ModifierReviewable.AbstractBase implements Generic {
            @Override // net.bytebuddy.description.ModifierReviewable
            public int getModifiers() {
                return asErasure().getModifiers();
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public Generic asGenericType() {
                return this;
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public Generic asRawType() {
                return asErasure().asGenericType();
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public boolean represents(Type type) {
                return equals(TypeDefinition.Sort.describe(type));
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$OfNonGenericType.class */
        public static abstract class OfNonGenericType extends AbstractBase {
            private transient /* synthetic */ int hashCode_Eo1xB4L4;

            @Override // net.bytebuddy.description.type.TypeDefinition
            public TypeDefinition.Sort getSort() {
                return TypeDefinition.Sort.NON_GENERIC;
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public Generic getSuperClass() {
                TypeDescription erasure = asErasure();
                Generic superClass = erasure.getSuperClass();
                if (AbstractBase.RAW_TYPES) {
                    return superClass;
                }
                return superClass == null ? Generic.UNDEFINED : new LazyProjection.WithResolvedErasure(superClass, new Visitor.ForRawType(erasure), AnnotationSource.Empty.INSTANCE);
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public TypeList.Generic getInterfaces() {
                TypeDescription erasure = asErasure();
                if (AbstractBase.RAW_TYPES) {
                    return erasure.getInterfaces();
                }
                return new TypeList.Generic.ForDetachedTypes.WithResolvedErasure(erasure.getInterfaces(), new Visitor.ForRawType(erasure));
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic, net.bytebuddy.description.type.TypeDefinition
            public FieldList<FieldDescription.InGenericShape> getDeclaredFields() {
                TypeDescription erasure = asErasure();
                return new FieldList.TypeSubstituting(this, erasure.getDeclaredFields(), AbstractBase.RAW_TYPES ? Visitor.NoOp.INSTANCE : new Visitor.ForRawType(erasure));
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic, net.bytebuddy.description.type.TypeDefinition
            public MethodList<MethodDescription.InGenericShape> getDeclaredMethods() {
                TypeDescription erasure = asErasure();
                return new MethodList.TypeSubstituting(this, erasure.getDeclaredMethods(), AbstractBase.RAW_TYPES ? Visitor.NoOp.INSTANCE : new Visitor.ForRawType(erasure));
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic, net.bytebuddy.description.type.TypeDefinition
            public RecordComponentList<RecordComponentDescription.InGenericShape> getRecordComponents() {
                TypeDescription erasure = asErasure();
                return new RecordComponentList.TypeSubstituting(this, erasure.getRecordComponents(), AbstractBase.RAW_TYPES ? Visitor.NoOp.INSTANCE : new Visitor.ForRawType(erasure));
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public TypeList.Generic getTypeArguments() {
                throw new IllegalStateException("A non-generic type does not imply type arguments: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public Generic findBindingOf(Generic typeVariable) {
                throw new IllegalStateException("A non-generic type does not imply type arguments: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public <T> T accept(Visitor<T> visitor) {
                return visitor.onNonGenericType(this);
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public String getTypeName() {
                return asErasure().getTypeName();
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public TypeList.Generic getUpperBounds() {
                throw new IllegalStateException("A non-generic type does not imply upper type bounds: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public TypeList.Generic getLowerBounds() {
                throw new IllegalStateException("A non-generic type does not imply lower type bounds: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public TypeVariableSource getTypeVariableSource() {
                throw new IllegalStateException("A non-generic type does not imply a type variable source: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public String getSymbol() {
                throw new IllegalStateException("A non-generic type does not imply a symbol: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public StackSize getStackSize() {
                return asErasure().getStackSize();
            }

            @Override // net.bytebuddy.description.NamedElement
            public String getActualName() {
                return asErasure().getActualName();
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public boolean isArray() {
                return asErasure().isArray();
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public boolean isPrimitive() {
                return asErasure().isPrimitive();
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public boolean isRecord() {
                return asErasure().isRecord();
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic.AbstractBase, net.bytebuddy.description.type.TypeDefinition
            public boolean represents(Type type) {
                return asErasure().represents(type);
            }

            @Override // java.lang.Iterable
            public Iterator<TypeDefinition> iterator() {
                return new TypeDefinition.SuperClassIterator(this);
            }

            @CachedReturnPlugin.Enhance
            public int hashCode() {
                int hashCode = this.hashCode_Eo1xB4L4 != 0 ? 0 : asErasure().hashCode();
                if (hashCode == 0) {
                    hashCode = this.hashCode_Eo1xB4L4;
                } else {
                    this.hashCode_Eo1xB4L4 = hashCode;
                }
                return hashCode;
            }

            @SuppressFBWarnings(value = {"EQ_CHECK_FOR_OPERAND_NOT_COMPATIBLE_WITH_THIS"}, justification = "Type check is performed by erasure implementation")
            public boolean equals(Object other) {
                return this == other || asErasure().equals(other);
            }

            public String toString() {
                return asErasure().toString();
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$OfNonGenericType$ForLoadedType.class */
            public static class ForLoadedType extends OfNonGenericType {
                @SuppressFBWarnings(value = {"MS_MUTABLE_COLLECTION_PKGPROTECT"}, justification = "This collection is not exposed.")
                private static final Map<Class<?>, Generic> TYPE_CACHE = new HashMap();
                private final Class<?> type;
                private final AnnotationReader annotationReader;

                static {
                    TYPE_CACHE.put(TargetType.class, new ForLoadedType(TargetType.class));
                    TYPE_CACHE.put(Object.class, new ForLoadedType(Object.class));
                    TYPE_CACHE.put(String.class, new ForLoadedType(String.class));
                    TYPE_CACHE.put(Boolean.class, new ForLoadedType(Boolean.class));
                    TYPE_CACHE.put(Byte.class, new ForLoadedType(Byte.class));
                    TYPE_CACHE.put(Short.class, new ForLoadedType(Short.class));
                    TYPE_CACHE.put(Character.class, new ForLoadedType(Character.class));
                    TYPE_CACHE.put(Integer.class, new ForLoadedType(Integer.class));
                    TYPE_CACHE.put(Long.class, new ForLoadedType(Long.class));
                    TYPE_CACHE.put(Float.class, new ForLoadedType(Float.class));
                    TYPE_CACHE.put(Double.class, new ForLoadedType(Double.class));
                    TYPE_CACHE.put(Void.TYPE, new ForLoadedType(Void.TYPE));
                    TYPE_CACHE.put(Boolean.TYPE, new ForLoadedType(Boolean.TYPE));
                    TYPE_CACHE.put(Byte.TYPE, new ForLoadedType(Byte.TYPE));
                    TYPE_CACHE.put(Short.TYPE, new ForLoadedType(Short.TYPE));
                    TYPE_CACHE.put(Character.TYPE, new ForLoadedType(Character.TYPE));
                    TYPE_CACHE.put(Integer.TYPE, new ForLoadedType(Integer.TYPE));
                    TYPE_CACHE.put(Long.TYPE, new ForLoadedType(Long.TYPE));
                    TYPE_CACHE.put(Float.TYPE, new ForLoadedType(Float.TYPE));
                    TYPE_CACHE.put(Double.TYPE, new ForLoadedType(Double.TYPE));
                }

                public ForLoadedType(Class<?> type) {
                    this(type, AnnotationReader.NoOp.INSTANCE);
                }

                /* JADX INFO: Access modifiers changed from: protected */
                public ForLoadedType(Class<?> type, AnnotationReader annotationReader) {
                    this.type = type;
                    this.annotationReader = annotationReader;
                }

                public static Generic of(Class<?> type) {
                    Generic typeDescription = TYPE_CACHE.get(type);
                    return typeDescription == null ? new ForLoadedType(type) : typeDescription;
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public TypeDescription asErasure() {
                    return ForLoadedType.of(this.type);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic
                public Generic getOwnerType() {
                    Class<?> declaringClass = this.type.getDeclaringClass();
                    return declaringClass == null ? Generic.UNDEFINED : new ForLoadedType(declaringClass, this.annotationReader.ofOuterClass());
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public Generic getComponentType() {
                    Class<?> componentType = this.type.getComponentType();
                    return componentType == null ? Generic.UNDEFINED : new ForLoadedType(componentType, this.annotationReader.ofComponentType());
                }

                @Override // net.bytebuddy.description.annotation.AnnotationSource
                public AnnotationList getDeclaredAnnotations() {
                    return this.annotationReader.asList();
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.OfNonGenericType, net.bytebuddy.description.type.TypeDescription.Generic.AbstractBase, net.bytebuddy.description.type.TypeDefinition
                public boolean represents(Type type) {
                    return this.type == type || super.represents(type);
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$OfNonGenericType$ForErasure.class */
            public static class ForErasure extends OfNonGenericType {
                private final TypeDescription typeDescription;

                public ForErasure(TypeDescription typeDescription) {
                    this.typeDescription = typeDescription;
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public TypeDescription asErasure() {
                    return this.typeDescription;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic
                public Generic getOwnerType() {
                    TypeDescription declaringType = this.typeDescription.getDeclaringType();
                    return declaringType == null ? Generic.UNDEFINED : declaringType.asGenericType();
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public Generic getComponentType() {
                    TypeDescription componentType = this.typeDescription.getComponentType();
                    return componentType == null ? Generic.UNDEFINED : componentType.asGenericType();
                }

                @Override // net.bytebuddy.description.annotation.AnnotationSource
                public AnnotationList getDeclaredAnnotations() {
                    return new AnnotationList.Empty();
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$OfNonGenericType$Latent.class */
            public static class Latent extends OfNonGenericType {
                private final TypeDescription typeDescription;
                private final Generic declaringType;
                private final AnnotationSource annotationSource;

                public Latent(TypeDescription typeDescription, AnnotationSource annotationSource) {
                    this(typeDescription, typeDescription.getDeclaringType(), annotationSource);
                }

                private Latent(TypeDescription typeDescription, TypeDescription declaringType, AnnotationSource annotationSource) {
                    this(typeDescription, declaringType == null ? Generic.UNDEFINED : declaringType.asGenericType(), annotationSource);
                }

                protected Latent(TypeDescription typeDescription, Generic declaringType, AnnotationSource annotationSource) {
                    this.typeDescription = typeDescription;
                    this.declaringType = declaringType;
                    this.annotationSource = annotationSource;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic
                public Generic getOwnerType() {
                    return this.declaringType;
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public Generic getComponentType() {
                    TypeDescription componentType = this.typeDescription.getComponentType();
                    return componentType == null ? Generic.UNDEFINED : componentType.asGenericType();
                }

                @Override // net.bytebuddy.description.annotation.AnnotationSource
                public AnnotationList getDeclaredAnnotations() {
                    return this.annotationSource.getDeclaredAnnotations();
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public TypeDescription asErasure() {
                    return this.typeDescription;
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$OfNonGenericType$ForReifiedErasure.class */
            public static class ForReifiedErasure extends OfNonGenericType {
                private final TypeDescription typeDescription;

                protected ForReifiedErasure(TypeDescription typeDescription) {
                    this.typeDescription = typeDescription;
                }

                protected static Generic of(TypeDescription typeDescription) {
                    return typeDescription.isGenerified() ? new ForReifiedErasure(typeDescription) : new ForErasure(typeDescription);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.OfNonGenericType, net.bytebuddy.description.type.TypeDefinition
                public Generic getSuperClass() {
                    Generic superClass = this.typeDescription.getSuperClass();
                    return superClass == null ? Generic.UNDEFINED : new LazyProjection.WithResolvedErasure(superClass, Visitor.Reifying.INHERITING);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.OfNonGenericType, net.bytebuddy.description.type.TypeDefinition
                public TypeList.Generic getInterfaces() {
                    return new TypeList.Generic.ForDetachedTypes.WithResolvedErasure(this.typeDescription.getInterfaces(), Visitor.Reifying.INHERITING);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.OfNonGenericType, net.bytebuddy.description.type.TypeDescription.Generic, net.bytebuddy.description.type.TypeDefinition
                public FieldList<FieldDescription.InGenericShape> getDeclaredFields() {
                    return new FieldList.TypeSubstituting(this, this.typeDescription.getDeclaredFields(), Visitor.TypeErasing.INSTANCE);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.OfNonGenericType, net.bytebuddy.description.type.TypeDescription.Generic, net.bytebuddy.description.type.TypeDefinition
                public MethodList<MethodDescription.InGenericShape> getDeclaredMethods() {
                    return new MethodList.TypeSubstituting(this, this.typeDescription.getDeclaredMethods(), Visitor.TypeErasing.INSTANCE);
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public TypeDescription asErasure() {
                    return this.typeDescription;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic
                public Generic getOwnerType() {
                    TypeDescription declaringType = this.typeDescription.getDeclaringType();
                    return declaringType == null ? Generic.UNDEFINED : of(declaringType);
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public Generic getComponentType() {
                    TypeDescription componentType = this.typeDescription.getComponentType();
                    return componentType == null ? Generic.UNDEFINED : of(componentType);
                }

                @Override // net.bytebuddy.description.annotation.AnnotationSource
                public AnnotationList getDeclaredAnnotations() {
                    return new AnnotationList.Empty();
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$OfGenericArray.class */
        public static abstract class OfGenericArray extends AbstractBase {
            private transient /* synthetic */ int hashCode_Dbfo4ZSc;

            @Override // net.bytebuddy.description.type.TypeDefinition
            public TypeDefinition.Sort getSort() {
                return getComponentType().getSort().isNonGeneric() ? TypeDefinition.Sort.NON_GENERIC : TypeDefinition.Sort.GENERIC_ARRAY;
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public TypeDescription asErasure() {
                return ArrayProjection.of(getComponentType().asErasure(), 1);
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public Generic getSuperClass() {
                return Generic.OBJECT;
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public TypeList.Generic getInterfaces() {
                return TypeDescription.ARRAY_INTERFACES;
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic, net.bytebuddy.description.type.TypeDefinition
            public FieldList<FieldDescription.InGenericShape> getDeclaredFields() {
                return new FieldList.Empty();
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic, net.bytebuddy.description.type.TypeDefinition
            public MethodList<MethodDescription.InGenericShape> getDeclaredMethods() {
                return new MethodList.Empty();
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic, net.bytebuddy.description.type.TypeDefinition
            public RecordComponentList<RecordComponentDescription.InGenericShape> getRecordComponents() {
                return new RecordComponentList.Empty();
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public TypeList.Generic getUpperBounds() {
                throw new IllegalStateException("A generic array type does not imply upper type bounds: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public TypeList.Generic getLowerBounds() {
                throw new IllegalStateException("A generic array type does not imply lower type bounds: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public TypeVariableSource getTypeVariableSource() {
                throw new IllegalStateException("A generic array type does not imply a type variable source: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public TypeList.Generic getTypeArguments() {
                throw new IllegalStateException("A generic array type does not imply type arguments: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public Generic findBindingOf(Generic typeVariable) {
                throw new IllegalStateException("A generic array type does not imply type arguments: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public Generic getOwnerType() {
                return Generic.UNDEFINED;
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public String getSymbol() {
                throw new IllegalStateException("A generic array type does not imply a symbol: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public String getTypeName() {
                if (getSort().isNonGeneric()) {
                    return asErasure().getTypeName();
                }
                return toString();
            }

            @Override // net.bytebuddy.description.NamedElement
            public String getActualName() {
                if (getSort().isNonGeneric()) {
                    return asErasure().getActualName();
                }
                return toString();
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public boolean isArray() {
                return true;
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public boolean isPrimitive() {
                return false;
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public boolean isRecord() {
                return false;
            }

            @Override // java.lang.Iterable
            public Iterator<TypeDefinition> iterator() {
                return new TypeDefinition.SuperClassIterator(this);
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public <T> T accept(Visitor<T> visitor) {
                if (getSort().isNonGeneric()) {
                    return visitor.onNonGenericType(this);
                }
                return visitor.onGenericArray(this);
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public StackSize getStackSize() {
                return StackSize.SINGLE;
            }

            @CachedReturnPlugin.Enhance
            public int hashCode() {
                int hashCode;
                if (this.hashCode_Dbfo4ZSc != 0) {
                    hashCode = 0;
                } else if (getSort().isNonGeneric()) {
                    hashCode = asErasure().hashCode();
                } else {
                    hashCode = getComponentType().hashCode();
                }
                int i = hashCode;
                if (i == 0) {
                    i = this.hashCode_Dbfo4ZSc;
                } else {
                    this.hashCode_Dbfo4ZSc = i;
                }
                return i;
            }

            @SuppressFBWarnings(value = {"EQ_CHECK_FOR_OPERAND_NOT_COMPATIBLE_WITH_THIS"}, justification = "Type check is performed by erasure implementation")
            public boolean equals(Object other) {
                if (this == other) {
                    return true;
                }
                if (getSort().isNonGeneric()) {
                    return asErasure().equals(other);
                }
                if (!(other instanceof Generic)) {
                    return false;
                }
                Generic typeDescription = (Generic) other;
                return typeDescription.getSort().isGenericArray() && getComponentType().equals(typeDescription.getComponentType());
            }

            public String toString() {
                if (getSort().isNonGeneric()) {
                    return asErasure().toString();
                }
                return getComponentType().getTypeName() + "[]";
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$OfGenericArray$ForLoadedType.class */
            public static class ForLoadedType extends OfGenericArray {
                private final GenericArrayType genericArrayType;
                private final AnnotationReader annotationReader;

                public ForLoadedType(GenericArrayType genericArrayType) {
                    this(genericArrayType, AnnotationReader.NoOp.INSTANCE);
                }

                /* JADX INFO: Access modifiers changed from: protected */
                public ForLoadedType(GenericArrayType genericArrayType, AnnotationReader annotationReader) {
                    this.genericArrayType = genericArrayType;
                    this.annotationReader = annotationReader;
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public Generic getComponentType() {
                    return TypeDefinition.Sort.describe(this.genericArrayType.getGenericComponentType(), this.annotationReader.ofComponentType());
                }

                @Override // net.bytebuddy.description.annotation.AnnotationSource
                public AnnotationList getDeclaredAnnotations() {
                    return this.annotationReader.asList();
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AbstractBase, net.bytebuddy.description.type.TypeDefinition
                public boolean represents(Type type) {
                    return this.genericArrayType == type || super.represents(type);
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$OfGenericArray$Latent.class */
            public static class Latent extends OfGenericArray {
                private final Generic componentType;
                private final AnnotationSource annotationSource;

                public Latent(Generic componentType, AnnotationSource annotationSource) {
                    this.componentType = componentType;
                    this.annotationSource = annotationSource;
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public Generic getComponentType() {
                    return this.componentType;
                }

                @Override // net.bytebuddy.description.annotation.AnnotationSource
                public AnnotationList getDeclaredAnnotations() {
                    return this.annotationSource.getDeclaredAnnotations();
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$OfWildcardType.class */
        public static abstract class OfWildcardType extends AbstractBase {
            public static final String SYMBOL = "?";
            private transient /* synthetic */ int hashCode_mA5GtJ0J;

            @Override // net.bytebuddy.description.type.TypeDefinition
            public TypeDefinition.Sort getSort() {
                return TypeDefinition.Sort.WILDCARD;
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public TypeDescription asErasure() {
                throw new IllegalStateException("A wildcard does not represent an erasable type: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public Generic getSuperClass() {
                throw new IllegalStateException("A wildcard does not imply a super type definition: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public TypeList.Generic getInterfaces() {
                throw new IllegalStateException("A wildcard does not imply an interface type definition: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic, net.bytebuddy.description.type.TypeDefinition
            public FieldList<FieldDescription.InGenericShape> getDeclaredFields() {
                throw new IllegalStateException("A wildcard does not imply field definitions: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic, net.bytebuddy.description.type.TypeDefinition
            public MethodList<MethodDescription.InGenericShape> getDeclaredMethods() {
                throw new IllegalStateException("A wildcard does not imply method definitions: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic, net.bytebuddy.description.type.TypeDefinition
            public RecordComponentList<RecordComponentDescription.InGenericShape> getRecordComponents() {
                throw new IllegalStateException("A wildcard does not imply record component definitions: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public Generic getComponentType() {
                throw new IllegalStateException("A wildcard does not imply a component type: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public TypeVariableSource getTypeVariableSource() {
                throw new IllegalStateException("A wildcard does not imply a type variable source: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public TypeList.Generic getTypeArguments() {
                throw new IllegalStateException("A wildcard does not imply type arguments: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public Generic findBindingOf(Generic typeVariable) {
                throw new IllegalStateException("A wildcard does not imply type arguments: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public Generic getOwnerType() {
                throw new IllegalStateException("A wildcard does not imply an owner type: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public String getSymbol() {
                throw new IllegalStateException("A wildcard does not imply a symbol: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public String getTypeName() {
                return toString();
            }

            @Override // net.bytebuddy.description.NamedElement
            public String getActualName() {
                return toString();
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public boolean isPrimitive() {
                return false;
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public boolean isArray() {
                return false;
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public boolean isRecord() {
                return false;
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic.AbstractBase, net.bytebuddy.description.type.TypeDefinition
            public boolean represents(Type type) {
                return equals(TypeDefinition.Sort.describe(type));
            }

            @Override // java.lang.Iterable
            public Iterator<TypeDefinition> iterator() {
                throw new IllegalStateException("A wildcard does not imply a super type definition: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public <T> T accept(Visitor<T> visitor) {
                return visitor.onWildcard(this);
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public StackSize getStackSize() {
                throw new IllegalStateException("A wildcard does not imply an operand stack size: " + this);
            }

            @CachedReturnPlugin.Enhance
            public int hashCode() {
                int i;
                if (this.hashCode_mA5GtJ0J != 0) {
                    i = 0;
                } else {
                    int lowerHash = 1;
                    int upperHash = 1;
                    for (Generic lowerBound : getLowerBounds()) {
                        lowerHash = (31 * lowerHash) + lowerBound.hashCode();
                    }
                    for (Generic upperBound : getUpperBounds()) {
                        upperHash = (31 * upperHash) + upperBound.hashCode();
                    }
                    i = lowerHash ^ upperHash;
                }
                int i2 = i;
                if (i2 == 0) {
                    i2 = this.hashCode_mA5GtJ0J;
                } else {
                    this.hashCode_mA5GtJ0J = i2;
                }
                return i2;
            }

            public boolean equals(Object other) {
                if (this == other) {
                    return true;
                }
                if (!(other instanceof Generic)) {
                    return false;
                }
                Generic typeDescription = (Generic) other;
                return typeDescription.getSort().isWildcard() && getUpperBounds().equals(typeDescription.getUpperBounds()) && getLowerBounds().equals(typeDescription.getLowerBounds());
            }

            public String toString() {
                StringBuilder stringBuilder = new StringBuilder(SYMBOL);
                TypeList.Generic bounds = getLowerBounds();
                if (!bounds.isEmpty()) {
                    stringBuilder.append(" super ");
                } else {
                    bounds = getUpperBounds();
                    if (bounds.getOnly().equals(Generic.OBJECT)) {
                        return SYMBOL;
                    }
                    stringBuilder.append(" extends ");
                }
                return stringBuilder.append(bounds.getOnly().getTypeName()).toString();
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$OfWildcardType$ForLoadedType.class */
            public static class ForLoadedType extends OfWildcardType {
                private final WildcardType wildcardType;
                private final AnnotationReader annotationReader;

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.OfWildcardType, net.bytebuddy.description.type.TypeDefinition
                public /* bridge */ /* synthetic */ TypeDefinition getComponentType() {
                    return super.getComponentType();
                }

                public ForLoadedType(WildcardType wildcardType) {
                    this(wildcardType, AnnotationReader.NoOp.INSTANCE);
                }

                /* JADX INFO: Access modifiers changed from: protected */
                public ForLoadedType(WildcardType wildcardType, AnnotationReader annotationReader) {
                    this.wildcardType = wildcardType;
                    this.annotationReader = annotationReader;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic
                public TypeList.Generic getUpperBounds() {
                    return new WildcardUpperBoundTypeList(this.wildcardType.getUpperBounds(), this.annotationReader);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic
                public TypeList.Generic getLowerBounds() {
                    return new WildcardLowerBoundTypeList(this.wildcardType.getLowerBounds(), this.annotationReader);
                }

                @Override // net.bytebuddy.description.annotation.AnnotationSource
                public AnnotationList getDeclaredAnnotations() {
                    return this.annotationReader.asList();
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.OfWildcardType, net.bytebuddy.description.type.TypeDescription.Generic.AbstractBase, net.bytebuddy.description.type.TypeDefinition
                public boolean represents(Type type) {
                    return this.wildcardType == type || super.represents(type);
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$OfWildcardType$ForLoadedType$WildcardUpperBoundTypeList.class */
                protected static class WildcardUpperBoundTypeList extends TypeList.Generic.AbstractBase {
                    private final Type[] upperBound;
                    private final AnnotationReader annotationReader;

                    protected WildcardUpperBoundTypeList(Type[] upperBound, AnnotationReader annotationReader) {
                        this.upperBound = upperBound;
                        this.annotationReader = annotationReader;
                    }

                    @Override // java.util.AbstractList, java.util.List
                    public Generic get(int index) {
                        return TypeDefinition.Sort.describe(this.upperBound[index], this.annotationReader.ofWildcardUpperBoundType(index));
                    }

                    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
                    public int size() {
                        return this.upperBound.length;
                    }
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$OfWildcardType$ForLoadedType$WildcardLowerBoundTypeList.class */
                protected static class WildcardLowerBoundTypeList extends TypeList.Generic.AbstractBase {
                    private final Type[] lowerBound;
                    private final AnnotationReader annotationReader;

                    protected WildcardLowerBoundTypeList(Type[] lowerBound, AnnotationReader annotationReader) {
                        this.lowerBound = lowerBound;
                        this.annotationReader = annotationReader;
                    }

                    @Override // java.util.AbstractList, java.util.List
                    public Generic get(int index) {
                        return TypeDefinition.Sort.describe(this.lowerBound[index], this.annotationReader.ofWildcardLowerBoundType(index));
                    }

                    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
                    public int size() {
                        return this.lowerBound.length;
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$OfWildcardType$Latent.class */
            public static class Latent extends OfWildcardType {
                private final List<? extends Generic> upperBounds;
                private final List<? extends Generic> lowerBounds;
                private final AnnotationSource annotationSource;

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.OfWildcardType, net.bytebuddy.description.type.TypeDefinition
                public /* bridge */ /* synthetic */ TypeDefinition getComponentType() {
                    return super.getComponentType();
                }

                protected Latent(List<? extends Generic> upperBounds, List<? extends Generic> lowerBounds, AnnotationSource annotationSource) {
                    this.upperBounds = upperBounds;
                    this.lowerBounds = lowerBounds;
                    this.annotationSource = annotationSource;
                }

                public static Generic unbounded(AnnotationSource annotationSource) {
                    return new Latent(Collections.singletonList(Generic.OBJECT), Collections.emptyList(), annotationSource);
                }

                public static Generic boundedAbove(Generic upperBound, AnnotationSource annotationSource) {
                    return new Latent(Collections.singletonList(upperBound), Collections.emptyList(), annotationSource);
                }

                public static Generic boundedBelow(Generic lowerBound, AnnotationSource annotationSource) {
                    return new Latent(Collections.singletonList(Generic.OBJECT), Collections.singletonList(lowerBound), annotationSource);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic
                public TypeList.Generic getUpperBounds() {
                    return new TypeList.Generic.Explicit(this.upperBounds);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic
                public TypeList.Generic getLowerBounds() {
                    return new TypeList.Generic.Explicit(this.lowerBounds);
                }

                @Override // net.bytebuddy.description.annotation.AnnotationSource
                public AnnotationList getDeclaredAnnotations() {
                    return this.annotationSource.getDeclaredAnnotations();
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$OfParameterizedType.class */
        public static abstract class OfParameterizedType extends AbstractBase {
            private transient /* synthetic */ int hashCode_RkCESJQg;

            @Override // net.bytebuddy.description.type.TypeDefinition
            public TypeDefinition.Sort getSort() {
                return TypeDefinition.Sort.PARAMETERIZED;
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public Generic getSuperClass() {
                Generic superClass = asErasure().getSuperClass();
                return superClass == null ? Generic.UNDEFINED : new LazyProjection.WithResolvedErasure(superClass, new Visitor.Substitutor.ForTypeVariableBinding(this));
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public TypeList.Generic getInterfaces() {
                return new TypeList.Generic.ForDetachedTypes.WithResolvedErasure(asErasure().getInterfaces(), new Visitor.Substitutor.ForTypeVariableBinding(this));
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic, net.bytebuddy.description.type.TypeDefinition
            public FieldList<FieldDescription.InGenericShape> getDeclaredFields() {
                return new FieldList.TypeSubstituting(this, asErasure().getDeclaredFields(), new Visitor.Substitutor.ForTypeVariableBinding(this));
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic, net.bytebuddy.description.type.TypeDefinition
            public MethodList<MethodDescription.InGenericShape> getDeclaredMethods() {
                return new MethodList.TypeSubstituting(this, asErasure().getDeclaredMethods(), new Visitor.Substitutor.ForTypeVariableBinding(this));
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic, net.bytebuddy.description.type.TypeDefinition
            public RecordComponentList<RecordComponentDescription.InGenericShape> getRecordComponents() {
                return new RecordComponentList.TypeSubstituting(this, asErasure().getRecordComponents(), new Visitor.Substitutor.ForTypeVariableBinding(this));
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public Generic findBindingOf(Generic typeVariable) {
                Generic typeDescription = this;
                do {
                    TypeList.Generic typeArguments = typeDescription.getTypeArguments();
                    TypeList.Generic typeVariables = typeDescription.asErasure().getTypeVariables();
                    for (int index = 0; index < Math.min(typeArguments.size(), typeVariables.size()); index++) {
                        if (typeVariable.equals(typeVariables.get(index))) {
                            return (Generic) typeArguments.get(index);
                        }
                    }
                    typeDescription = typeDescription.getOwnerType();
                    if (typeDescription == null) {
                        break;
                    }
                } while (typeDescription.getSort().isParameterized());
                return Generic.UNDEFINED;
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public TypeList.Generic getUpperBounds() {
                throw new IllegalStateException("A parameterized type does not imply upper bounds: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public TypeList.Generic getLowerBounds() {
                throw new IllegalStateException("A parameterized type does not imply lower bounds: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public Generic getComponentType() {
                throw new IllegalStateException("A parameterized type does not imply a component type: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public TypeVariableSource getTypeVariableSource() {
                throw new IllegalStateException("A parameterized type does not imply a type variable source: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public String getSymbol() {
                throw new IllegalStateException("A parameterized type does not imply a symbol: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public String getTypeName() {
                return toString();
            }

            @Override // net.bytebuddy.description.NamedElement
            public String getActualName() {
                return toString();
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public boolean isPrimitive() {
                return false;
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public boolean isArray() {
                return false;
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public boolean isRecord() {
                return asErasure().isRecord();
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic.AbstractBase, net.bytebuddy.description.type.TypeDefinition
            public boolean represents(Type type) {
                return equals(TypeDefinition.Sort.describe(type));
            }

            @Override // java.lang.Iterable
            public Iterator<TypeDefinition> iterator() {
                return new TypeDefinition.SuperClassIterator(this);
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public <T> T accept(Visitor<T> visitor) {
                return visitor.onParameterizedType(this);
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public StackSize getStackSize() {
                return StackSize.SINGLE;
            }

            @CachedReturnPlugin.Enhance
            public int hashCode() {
                int hashCode;
                int i;
                if (this.hashCode_RkCESJQg != 0) {
                    i = 0;
                } else {
                    int result = 1;
                    for (Generic typeArgument : getTypeArguments()) {
                        result = (31 * result) + typeArgument.hashCode();
                    }
                    Generic ownerType = getOwnerType();
                    int i2 = result;
                    if (ownerType == null) {
                        hashCode = asErasure().hashCode();
                    } else {
                        hashCode = ownerType.hashCode();
                    }
                    i = i2 ^ hashCode;
                }
                int i3 = i;
                if (i3 == 0) {
                    i3 = this.hashCode_RkCESJQg;
                } else {
                    this.hashCode_RkCESJQg = i3;
                }
                return i3;
            }

            public boolean equals(Object other) {
                if (this == other) {
                    return true;
                }
                if (!(other instanceof Generic)) {
                    return false;
                }
                Generic typeDescription = (Generic) other;
                if (!typeDescription.getSort().isParameterized()) {
                    return false;
                }
                Generic ownerType = getOwnerType();
                Generic otherOwnerType = typeDescription.getOwnerType();
                return asErasure().equals(typeDescription.asErasure()) && (ownerType != null || otherOwnerType == null) && ((ownerType == null || ownerType.equals(otherOwnerType)) && getTypeArguments().equals(typeDescription.getTypeArguments()));
            }

            public String toString() {
                StringBuilder stringBuilder = new StringBuilder();
                RenderingDelegate.CURRENT.apply(stringBuilder, asErasure(), getOwnerType());
                TypeList.Generic<Generic> typeArguments = getTypeArguments();
                if (!typeArguments.isEmpty()) {
                    stringBuilder.append('<');
                    boolean multiple = false;
                    for (Generic typeArgument : typeArguments) {
                        if (multiple) {
                            stringBuilder.append(", ");
                        }
                        stringBuilder.append(typeArgument.getTypeName());
                        multiple = true;
                    }
                    stringBuilder.append('>');
                }
                return stringBuilder.toString();
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$OfParameterizedType$RenderingDelegate.class */
            public enum RenderingDelegate {
                FOR_LEGACY_VM { // from class: net.bytebuddy.description.type.TypeDescription.Generic.OfParameterizedType.RenderingDelegate.1
                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.OfParameterizedType.RenderingDelegate
                    protected void apply(StringBuilder stringBuilder, TypeDescription erasure, Generic ownerType) {
                        String name;
                        if (ownerType != null) {
                            StringBuilder append = stringBuilder.append(ownerType.getTypeName()).append('.');
                            if (ownerType.getSort().isParameterized()) {
                                name = erasure.getSimpleName();
                            } else {
                                name = erasure.getName();
                            }
                            append.append(name);
                            return;
                        }
                        stringBuilder.append(erasure.getName());
                    }
                },
                FOR_JAVA_8_CAPABLE_VM { // from class: net.bytebuddy.description.type.TypeDescription.Generic.OfParameterizedType.RenderingDelegate.2
                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.OfParameterizedType.RenderingDelegate
                    protected void apply(StringBuilder stringBuilder, TypeDescription erasure, Generic ownerType) {
                        if (ownerType != null) {
                            stringBuilder.append(ownerType.getTypeName()).append('$');
                            if (ownerType.getSort().isParameterized()) {
                                stringBuilder.append(erasure.getName().replace(ownerType.asErasure().getName() + "$", ""));
                                return;
                            } else {
                                stringBuilder.append(erasure.getSimpleName());
                                return;
                            }
                        }
                        stringBuilder.append(erasure.getName());
                    }
                };
                
                protected static final RenderingDelegate CURRENT;

                protected abstract void apply(StringBuilder sb, TypeDescription typeDescription, Generic generic);

                static {
                    CURRENT = ClassFileVersion.ofThisVm(ClassFileVersion.JAVA_V6).isAtLeast(ClassFileVersion.JAVA_V8) ? FOR_JAVA_8_CAPABLE_VM : FOR_LEGACY_VM;
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$OfParameterizedType$ForLoadedType.class */
            public static class ForLoadedType extends OfParameterizedType {
                private final ParameterizedType parameterizedType;
                private final AnnotationReader annotationReader;

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.OfParameterizedType, net.bytebuddy.description.type.TypeDefinition
                public /* bridge */ /* synthetic */ TypeDefinition getComponentType() {
                    return super.getComponentType();
                }

                public ForLoadedType(ParameterizedType parameterizedType) {
                    this(parameterizedType, AnnotationReader.NoOp.INSTANCE);
                }

                /* JADX INFO: Access modifiers changed from: protected */
                public ForLoadedType(ParameterizedType parameterizedType, AnnotationReader annotationReader) {
                    this.parameterizedType = parameterizedType;
                    this.annotationReader = annotationReader;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic
                public TypeList.Generic getTypeArguments() {
                    return new ParameterArgumentTypeList(this.parameterizedType.getActualTypeArguments(), this.annotationReader);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic
                public Generic getOwnerType() {
                    Type ownerType = this.parameterizedType.getOwnerType();
                    return ownerType == null ? Generic.UNDEFINED : TypeDefinition.Sort.describe(ownerType, this.annotationReader.ofOwnerType());
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public TypeDescription asErasure() {
                    return ForLoadedType.of((Class) this.parameterizedType.getRawType());
                }

                @Override // net.bytebuddy.description.annotation.AnnotationSource
                public AnnotationList getDeclaredAnnotations() {
                    return this.annotationReader.asList();
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.OfParameterizedType, net.bytebuddy.description.type.TypeDescription.Generic.AbstractBase, net.bytebuddy.description.type.TypeDefinition
                public boolean represents(Type type) {
                    return this.parameterizedType == type || super.represents(type);
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$OfParameterizedType$ForLoadedType$ParameterArgumentTypeList.class */
                protected static class ParameterArgumentTypeList extends TypeList.Generic.AbstractBase {
                    private final Type[] argumentType;
                    private final AnnotationReader annotationReader;

                    protected ParameterArgumentTypeList(Type[] argumentType, AnnotationReader annotationReader) {
                        this.argumentType = argumentType;
                        this.annotationReader = annotationReader;
                    }

                    @Override // java.util.AbstractList, java.util.List
                    public Generic get(int index) {
                        return TypeDefinition.Sort.describe(this.argumentType[index], this.annotationReader.ofTypeArgument(index));
                    }

                    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
                    public int size() {
                        return this.argumentType.length;
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$OfParameterizedType$Latent.class */
            public static class Latent extends OfParameterizedType {
                private final TypeDescription rawType;
                private final Generic ownerType;
                private final List<? extends Generic> parameters;
                private final AnnotationSource annotationSource;

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.OfParameterizedType, net.bytebuddy.description.type.TypeDefinition
                public /* bridge */ /* synthetic */ TypeDefinition getComponentType() {
                    return super.getComponentType();
                }

                public Latent(TypeDescription rawType, Generic ownerType, List<? extends Generic> parameters, AnnotationSource annotationSource) {
                    this.rawType = rawType;
                    this.ownerType = ownerType;
                    this.parameters = parameters;
                    this.annotationSource = annotationSource;
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public TypeDescription asErasure() {
                    return this.rawType;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic
                public Generic getOwnerType() {
                    return this.ownerType;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic
                public TypeList.Generic getTypeArguments() {
                    return new TypeList.Generic.Explicit(this.parameters);
                }

                @Override // net.bytebuddy.description.annotation.AnnotationSource
                public AnnotationList getDeclaredAnnotations() {
                    return this.annotationSource.getDeclaredAnnotations();
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$OfParameterizedType$ForReifiedType.class */
            public static class ForReifiedType extends OfParameterizedType {
                private final Generic parameterizedType;

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.OfParameterizedType, net.bytebuddy.description.type.TypeDefinition
                public /* bridge */ /* synthetic */ TypeDefinition getComponentType() {
                    return super.getComponentType();
                }

                protected ForReifiedType(Generic parameterizedType) {
                    this.parameterizedType = parameterizedType;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.OfParameterizedType, net.bytebuddy.description.type.TypeDefinition
                public Generic getSuperClass() {
                    Generic superClass = super.getSuperClass();
                    return superClass == null ? Generic.UNDEFINED : new LazyProjection.WithResolvedErasure(superClass, Visitor.Reifying.INHERITING);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.OfParameterizedType, net.bytebuddy.description.type.TypeDefinition
                public TypeList.Generic getInterfaces() {
                    return new TypeList.Generic.ForDetachedTypes.WithResolvedErasure(super.getInterfaces(), Visitor.Reifying.INHERITING);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.OfParameterizedType, net.bytebuddy.description.type.TypeDescription.Generic, net.bytebuddy.description.type.TypeDefinition
                public FieldList<FieldDescription.InGenericShape> getDeclaredFields() {
                    return new FieldList.TypeSubstituting(this, super.getDeclaredFields(), Visitor.TypeErasing.INSTANCE);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.OfParameterizedType, net.bytebuddy.description.type.TypeDescription.Generic, net.bytebuddy.description.type.TypeDefinition
                public MethodList<MethodDescription.InGenericShape> getDeclaredMethods() {
                    return new MethodList.TypeSubstituting(this, super.getDeclaredMethods(), Visitor.TypeErasing.INSTANCE);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic
                public TypeList.Generic getTypeArguments() {
                    return new TypeList.Generic.ForDetachedTypes(this.parameterizedType.getTypeArguments(), Visitor.TypeErasing.INSTANCE);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic
                public Generic getOwnerType() {
                    Generic ownerType = this.parameterizedType.getOwnerType();
                    return ownerType == null ? Generic.UNDEFINED : (Generic) ownerType.accept(Visitor.Reifying.INHERITING);
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public TypeDescription asErasure() {
                    return this.parameterizedType.asErasure();
                }

                @Override // net.bytebuddy.description.annotation.AnnotationSource
                public AnnotationList getDeclaredAnnotations() {
                    return new AnnotationList.Empty();
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$OfParameterizedType$ForGenerifiedErasure.class */
            public static class ForGenerifiedErasure extends OfParameterizedType {
                private final TypeDescription typeDescription;

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.OfParameterizedType, net.bytebuddy.description.type.TypeDefinition
                public /* bridge */ /* synthetic */ TypeDefinition getComponentType() {
                    return super.getComponentType();
                }

                protected ForGenerifiedErasure(TypeDescription typeDescription) {
                    this.typeDescription = typeDescription;
                }

                public static Generic of(TypeDescription typeDescription) {
                    return typeDescription.isGenerified() ? new ForGenerifiedErasure(typeDescription) : new OfNonGenericType.ForErasure(typeDescription);
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public TypeDescription asErasure() {
                    return this.typeDescription;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic
                public TypeList.Generic getTypeArguments() {
                    return new TypeList.Generic.ForDetachedTypes(this.typeDescription.getTypeVariables(), Visitor.AnnotationStripper.INSTANCE);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic
                public Generic getOwnerType() {
                    TypeDescription declaringType = this.typeDescription.getDeclaringType();
                    return declaringType == null ? Generic.UNDEFINED : of(declaringType);
                }

                @Override // net.bytebuddy.description.annotation.AnnotationSource
                public AnnotationList getDeclaredAnnotations() {
                    return new AnnotationList.Empty();
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$OfTypeVariable.class */
        public static abstract class OfTypeVariable extends AbstractBase {
            private transient /* synthetic */ int hashCode_XPX3X54f;

            @Override // net.bytebuddy.description.type.TypeDefinition
            public TypeDefinition.Sort getSort() {
                return TypeDefinition.Sort.VARIABLE;
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public TypeDescription asErasure() {
                TypeList.Generic upperBounds = getUpperBounds();
                return upperBounds.isEmpty() ? TypeDescription.OBJECT : ((Generic) upperBounds.get(0)).asErasure();
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public Generic getSuperClass() {
                throw new IllegalStateException("A type variable does not imply a super type definition: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public TypeList.Generic getInterfaces() {
                throw new IllegalStateException("A type variable does not imply an interface type definition: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic, net.bytebuddy.description.type.TypeDefinition
            public FieldList<FieldDescription.InGenericShape> getDeclaredFields() {
                throw new IllegalStateException("A type variable does not imply field definitions: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic, net.bytebuddy.description.type.TypeDefinition
            public MethodList<MethodDescription.InGenericShape> getDeclaredMethods() {
                throw new IllegalStateException("A type variable does not imply method definitions: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic, net.bytebuddy.description.type.TypeDefinition
            public RecordComponentList<RecordComponentDescription.InGenericShape> getRecordComponents() {
                throw new IllegalStateException("A type variable does not imply record component definitions: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public Generic getComponentType() {
                throw new IllegalStateException("A type variable does not imply a component type: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public TypeList.Generic getTypeArguments() {
                throw new IllegalStateException("A type variable does not imply type arguments: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public Generic findBindingOf(Generic typeVariable) {
                throw new IllegalStateException("A type variable does not imply type arguments: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public TypeList.Generic getLowerBounds() {
                throw new IllegalStateException("A type variable does not imply lower bounds: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public Generic getOwnerType() {
                throw new IllegalStateException("A type variable does not imply an owner type: " + this);
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public String getTypeName() {
                return toString();
            }

            @Override // net.bytebuddy.description.NamedElement
            public String getActualName() {
                return getSymbol();
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public <T> T accept(Visitor<T> visitor) {
                return visitor.onTypeVariable(this);
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public StackSize getStackSize() {
                return StackSize.SINGLE;
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public boolean isArray() {
                return false;
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public boolean isPrimitive() {
                return false;
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public boolean isRecord() {
                return false;
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic.AbstractBase, net.bytebuddy.description.type.TypeDefinition
            public boolean represents(Type type) {
                return equals(TypeDefinition.Sort.describe(type));
            }

            @Override // java.lang.Iterable
            public Iterator<TypeDefinition> iterator() {
                throw new IllegalStateException("A type variable does not imply a super type definition: " + this);
            }

            @CachedReturnPlugin.Enhance
            public int hashCode() {
                int hashCode = this.hashCode_XPX3X54f != 0 ? 0 : getTypeVariableSource().hashCode() ^ getSymbol().hashCode();
                if (hashCode == 0) {
                    hashCode = this.hashCode_XPX3X54f;
                } else {
                    this.hashCode_XPX3X54f = hashCode;
                }
                return hashCode;
            }

            public boolean equals(Object other) {
                if (this == other) {
                    return true;
                }
                if (!(other instanceof Generic)) {
                    return false;
                }
                Generic typeDescription = (Generic) other;
                return typeDescription.getSort().isTypeVariable() && getSymbol().equals(typeDescription.getSymbol()) && getTypeVariableSource().equals(typeDescription.getTypeVariableSource());
            }

            public String toString() {
                return getSymbol();
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$OfTypeVariable$Symbolic.class */
            public static class Symbolic extends AbstractBase {
                private final String symbol;
                private final AnnotationSource annotationSource;

                public Symbolic(String symbol, AnnotationSource annotationSource) {
                    this.symbol = symbol;
                    this.annotationSource = annotationSource;
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public TypeDefinition.Sort getSort() {
                    return TypeDefinition.Sort.VARIABLE_SYMBOLIC;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic
                public String getSymbol() {
                    return this.symbol;
                }

                @Override // net.bytebuddy.description.annotation.AnnotationSource
                public AnnotationList getDeclaredAnnotations() {
                    return this.annotationSource.getDeclaredAnnotations();
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public TypeDescription asErasure() {
                    throw new IllegalStateException("A symbolic type variable does not imply an erasure: " + this);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic
                public TypeList.Generic getUpperBounds() {
                    throw new IllegalStateException("A symbolic type variable does not imply an upper type bound: " + this);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic
                public TypeVariableSource getTypeVariableSource() {
                    throw new IllegalStateException("A symbolic type variable does not imply a variable source: " + this);
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public Generic getSuperClass() {
                    throw new IllegalStateException("A symbolic type variable does not imply a super type definition: " + this);
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public TypeList.Generic getInterfaces() {
                    throw new IllegalStateException("A symbolic type variable does not imply an interface type definition: " + this);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic, net.bytebuddy.description.type.TypeDefinition
                public FieldList<FieldDescription.InGenericShape> getDeclaredFields() {
                    throw new IllegalStateException("A symbolic type variable does not imply field definitions: " + this);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic, net.bytebuddy.description.type.TypeDefinition
                public MethodList<MethodDescription.InGenericShape> getDeclaredMethods() {
                    throw new IllegalStateException("A symbolic type variable does not imply method definitions: " + this);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic, net.bytebuddy.description.type.TypeDefinition
                public RecordComponentList<RecordComponentDescription.InGenericShape> getRecordComponents() {
                    throw new IllegalStateException("A symbolic type variable does not imply record component definitions: " + this);
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public Generic getComponentType() {
                    throw new IllegalStateException("A symbolic type variable does not imply a component type: " + this);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic
                public TypeList.Generic getTypeArguments() {
                    throw new IllegalStateException("A symbolic type variable does not imply type arguments: " + this);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic
                public Generic findBindingOf(Generic typeVariable) {
                    throw new IllegalStateException("A symbolic type variable does not imply type arguments: " + this);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic
                public TypeList.Generic getLowerBounds() {
                    throw new IllegalStateException("A symbolic type variable does not imply lower bounds: " + this);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic
                public Generic getOwnerType() {
                    throw new IllegalStateException("A symbolic type variable does not imply an owner type: " + this);
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public String getTypeName() {
                    return toString();
                }

                @Override // net.bytebuddy.description.NamedElement
                public String getActualName() {
                    return getSymbol();
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic
                public <T> T accept(Visitor<T> visitor) {
                    return visitor.onTypeVariable(this);
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public StackSize getStackSize() {
                    return StackSize.SINGLE;
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public boolean isArray() {
                    return false;
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public boolean isPrimitive() {
                    return false;
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public boolean isRecord() {
                    return false;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.AbstractBase, net.bytebuddy.description.type.TypeDefinition
                public boolean represents(Type type) {
                    if (type == null) {
                        throw new NullPointerException();
                    }
                    return false;
                }

                @Override // java.lang.Iterable
                public Iterator<TypeDefinition> iterator() {
                    throw new IllegalStateException("A symbolic type variable does not imply a super type definition: " + this);
                }

                public int hashCode() {
                    return this.symbol.hashCode();
                }

                public boolean equals(Object other) {
                    if (this == other) {
                        return true;
                    }
                    if (!(other instanceof Generic)) {
                        return false;
                    }
                    Generic typeDescription = (Generic) other;
                    return typeDescription.getSort().isTypeVariable() && getSymbol().equals(typeDescription.getSymbol());
                }

                public String toString() {
                    return getSymbol();
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$OfTypeVariable$ForLoadedType.class */
            public static class ForLoadedType extends OfTypeVariable {
                private final TypeVariable<?> typeVariable;
                private final AnnotationReader annotationReader;

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.OfTypeVariable, net.bytebuddy.description.type.TypeDefinition
                public /* bridge */ /* synthetic */ TypeDefinition getComponentType() {
                    return super.getComponentType();
                }

                public ForLoadedType(TypeVariable<?> typeVariable) {
                    this(typeVariable, AnnotationReader.NoOp.INSTANCE);
                }

                /* JADX INFO: Access modifiers changed from: protected */
                public ForLoadedType(TypeVariable<?> typeVariable, AnnotationReader annotationReader) {
                    this.typeVariable = typeVariable;
                    this.annotationReader = annotationReader;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic
                public TypeVariableSource getTypeVariableSource() {
                    Object genericDeclaration = this.typeVariable.getGenericDeclaration();
                    if (genericDeclaration instanceof Class) {
                        return ForLoadedType.of((Class) genericDeclaration);
                    }
                    if (genericDeclaration instanceof Method) {
                        return new MethodDescription.ForLoadedMethod((Method) genericDeclaration);
                    }
                    if (genericDeclaration instanceof Constructor) {
                        return new MethodDescription.ForLoadedConstructor((Constructor) genericDeclaration);
                    }
                    throw new IllegalStateException("Unknown declaration: " + genericDeclaration);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic
                public TypeList.Generic getUpperBounds() {
                    return new TypeVariableBoundList(this.typeVariable.getBounds(), this.annotationReader);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic
                public String getSymbol() {
                    return this.typeVariable.getName();
                }

                @Override // net.bytebuddy.description.annotation.AnnotationSource
                public AnnotationList getDeclaredAnnotations() {
                    return this.annotationReader.asList();
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.OfTypeVariable, net.bytebuddy.description.type.TypeDescription.Generic.AbstractBase, net.bytebuddy.description.type.TypeDefinition
                public boolean represents(Type type) {
                    return this.typeVariable == type || super.represents(type);
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$OfTypeVariable$ForLoadedType$TypeVariableBoundList.class */
                protected static class TypeVariableBoundList extends TypeList.Generic.AbstractBase {
                    private final Type[] bound;
                    private final AnnotationReader annotationReader;

                    protected TypeVariableBoundList(Type[] bound, AnnotationReader annotationReader) {
                        this.bound = bound;
                        this.annotationReader = annotationReader;
                    }

                    @Override // java.util.AbstractList, java.util.List
                    public Generic get(int index) {
                        return TypeDefinition.Sort.describe(this.bound[index], this.annotationReader.ofTypeVariableBoundType(index));
                    }

                    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
                    public int size() {
                        return this.bound.length;
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$OfTypeVariable$WithAnnotationOverlay.class */
            public static class WithAnnotationOverlay extends OfTypeVariable {
                private final Generic typeVariable;
                private final AnnotationSource annotationSource;

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.OfTypeVariable, net.bytebuddy.description.type.TypeDefinition
                public /* bridge */ /* synthetic */ TypeDefinition getComponentType() {
                    return super.getComponentType();
                }

                public WithAnnotationOverlay(Generic typeVariable, AnnotationSource annotationSource) {
                    this.typeVariable = typeVariable;
                    this.annotationSource = annotationSource;
                }

                @Override // net.bytebuddy.description.annotation.AnnotationSource
                public AnnotationList getDeclaredAnnotations() {
                    return this.annotationSource.getDeclaredAnnotations();
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic
                public TypeList.Generic getUpperBounds() {
                    return this.typeVariable.getUpperBounds();
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic
                public TypeVariableSource getTypeVariableSource() {
                    return this.typeVariable.getTypeVariableSource();
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic
                public String getSymbol() {
                    return this.typeVariable.getSymbol();
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$LazyProjection.class */
        public static abstract class LazyProjection extends AbstractBase {
            private transient /* synthetic */ int hashCode_3rk3u4iO;

            protected abstract Generic resolve();

            @Override // net.bytebuddy.description.type.TypeDefinition
            public TypeDefinition.Sort getSort() {
                return resolve().getSort();
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic, net.bytebuddy.description.type.TypeDefinition
            public FieldList<FieldDescription.InGenericShape> getDeclaredFields() {
                return resolve().getDeclaredFields();
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic, net.bytebuddy.description.type.TypeDefinition
            public MethodList<MethodDescription.InGenericShape> getDeclaredMethods() {
                return resolve().getDeclaredMethods();
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic, net.bytebuddy.description.type.TypeDefinition
            public RecordComponentList<RecordComponentDescription.InGenericShape> getRecordComponents() {
                return resolve().getRecordComponents();
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public TypeList.Generic getUpperBounds() {
                return resolve().getUpperBounds();
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public TypeList.Generic getLowerBounds() {
                return resolve().getLowerBounds();
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public Generic getComponentType() {
                return resolve().getComponentType();
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public TypeList.Generic getTypeArguments() {
                return resolve().getTypeArguments();
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public Generic findBindingOf(Generic typeVariable) {
                return resolve().findBindingOf(typeVariable);
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public TypeVariableSource getTypeVariableSource() {
                return resolve().getTypeVariableSource();
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public Generic getOwnerType() {
                return resolve().getOwnerType();
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public String getTypeName() {
                return resolve().getTypeName();
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public String getSymbol() {
                return resolve().getSymbol();
            }

            @Override // net.bytebuddy.description.NamedElement
            public String getActualName() {
                return resolve().getActualName();
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic
            public <T> T accept(Visitor<T> visitor) {
                return (T) resolve().accept(visitor);
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public StackSize getStackSize() {
                return asErasure().getStackSize();
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public boolean isArray() {
                return asErasure().isArray();
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public boolean isPrimitive() {
                return asErasure().isPrimitive();
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public boolean isRecord() {
                return asErasure().isRecord();
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic.AbstractBase, net.bytebuddy.description.type.TypeDefinition
            public boolean represents(Type type) {
                return resolve().represents(type);
            }

            @CachedReturnPlugin.Enhance
            public int hashCode() {
                int hashCode = this.hashCode_3rk3u4iO != 0 ? 0 : resolve().hashCode();
                if (hashCode == 0) {
                    hashCode = this.hashCode_3rk3u4iO;
                } else {
                    this.hashCode_3rk3u4iO = hashCode;
                }
                return hashCode;
            }

            public boolean equals(Object other) {
                return this == other || ((other instanceof TypeDefinition) && resolve().equals(other));
            }

            public String toString() {
                return resolve().toString();
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$LazyProjection$WithLazyNavigation.class */
            public static abstract class WithLazyNavigation extends LazyProjection {
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection, net.bytebuddy.description.type.TypeDefinition
                public /* bridge */ /* synthetic */ TypeDefinition getComponentType() {
                    return super.getComponentType();
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public Generic getSuperClass() {
                    return LazySuperClass.of(this);
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public TypeList.Generic getInterfaces() {
                    return LazyInterfaceList.of((LazyProjection) this);
                }

                @Override // java.lang.Iterable
                public Iterator<TypeDefinition> iterator() {
                    return new TypeDefinition.SuperClassIterator(this);
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$LazyProjection$WithLazyNavigation$LazySuperClass.class */
                protected static class LazySuperClass extends WithLazyNavigation {
                    private final LazyProjection delegate;
                    private transient /* synthetic */ Generic resolved;

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection.WithLazyNavigation, net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection, net.bytebuddy.description.type.TypeDefinition
                    public /* bridge */ /* synthetic */ TypeDefinition getComponentType() {
                        return super.getComponentType();
                    }

                    protected LazySuperClass(LazyProjection delegate) {
                        this.delegate = delegate;
                    }

                    protected static Generic of(LazyProjection delegate) {
                        return delegate.asErasure().getSuperClass() == null ? Generic.UNDEFINED : new LazySuperClass(delegate);
                    }

                    @Override // net.bytebuddy.description.annotation.AnnotationSource
                    public AnnotationList getDeclaredAnnotations() {
                        return resolve().getDeclaredAnnotations();
                    }

                    @Override // net.bytebuddy.description.type.TypeDefinition
                    public TypeDescription asErasure() {
                        return this.delegate.asErasure().getSuperClass().asErasure();
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection
                    @CachedReturnPlugin.Enhance("resolved")
                    protected Generic resolve() {
                        Generic superClass = this.resolved != null ? null : this.delegate.resolve().getSuperClass();
                        if (superClass == null) {
                            superClass = this.resolved;
                        } else {
                            this.resolved = superClass;
                        }
                        return superClass;
                    }
                }

                /* JADX INFO: Access modifiers changed from: protected */
                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$LazyProjection$WithLazyNavigation$LazyInterfaceType.class */
                public static class LazyInterfaceType extends WithLazyNavigation {
                    private final LazyProjection delegate;
                    private final int index;
                    private final Generic rawInterface;
                    private transient /* synthetic */ Generic resolved;

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection.WithLazyNavigation, net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection, net.bytebuddy.description.type.TypeDefinition
                    public /* bridge */ /* synthetic */ TypeDefinition getComponentType() {
                        return super.getComponentType();
                    }

                    protected LazyInterfaceType(LazyProjection delegate, int index, Generic rawInterface) {
                        this.delegate = delegate;
                        this.index = index;
                        this.rawInterface = rawInterface;
                    }

                    @Override // net.bytebuddy.description.annotation.AnnotationSource
                    public AnnotationList getDeclaredAnnotations() {
                        return resolve().getDeclaredAnnotations();
                    }

                    @Override // net.bytebuddy.description.type.TypeDefinition
                    public TypeDescription asErasure() {
                        return this.rawInterface.asErasure();
                    }

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection
                    @CachedReturnPlugin.Enhance("resolved")
                    protected Generic resolve() {
                        Generic generic = this.resolved != null ? null : (Generic) this.delegate.resolve().getInterfaces().get(this.index);
                        if (generic == null) {
                            generic = this.resolved;
                        } else {
                            this.resolved = generic;
                        }
                        return generic;
                    }
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$LazyProjection$WithLazyNavigation$LazyInterfaceList.class */
                protected static class LazyInterfaceList extends TypeList.Generic.AbstractBase {
                    private final LazyProjection delegate;
                    private final TypeList.Generic rawInterfaces;

                    protected LazyInterfaceList(LazyProjection delegate, TypeList.Generic rawInterfaces) {
                        this.delegate = delegate;
                        this.rawInterfaces = rawInterfaces;
                    }

                    protected static TypeList.Generic of(LazyProjection delegate) {
                        return new LazyInterfaceList(delegate, delegate.asErasure().getInterfaces());
                    }

                    @Override // java.util.AbstractList, java.util.List
                    public Generic get(int index) {
                        return new LazyInterfaceType(this.delegate, index, (Generic) this.rawInterfaces.get(index));
                    }

                    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
                    public int size() {
                        return this.rawInterfaces.size();
                    }
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$LazyProjection$WithLazyNavigation$OfAnnotatedElement.class */
                protected static abstract class OfAnnotatedElement extends WithLazyNavigation {
                    protected abstract AnnotationReader getAnnotationReader();

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection.WithLazyNavigation, net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection, net.bytebuddy.description.type.TypeDefinition
                    public /* bridge */ /* synthetic */ TypeDefinition getComponentType() {
                        return super.getComponentType();
                    }

                    public AnnotationList getDeclaredAnnotations() {
                        return getAnnotationReader().asList();
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$LazyProjection$WithEagerNavigation.class */
            public static abstract class WithEagerNavigation extends LazyProjection {
                @Override // net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection, net.bytebuddy.description.type.TypeDefinition
                public /* bridge */ /* synthetic */ TypeDefinition getComponentType() {
                    return super.getComponentType();
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public Generic getSuperClass() {
                    return resolve().getSuperClass();
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public TypeList.Generic getInterfaces() {
                    return resolve().getInterfaces();
                }

                @Override // java.lang.Iterable
                public Iterator<TypeDefinition> iterator() {
                    return resolve().iterator();
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$LazyProjection$WithEagerNavigation$OfAnnotatedElement.class */
                protected static abstract class OfAnnotatedElement extends WithEagerNavigation {
                    protected abstract AnnotationReader getAnnotationReader();

                    @Override // net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection.WithEagerNavigation, net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection, net.bytebuddy.description.type.TypeDefinition
                    public /* bridge */ /* synthetic */ TypeDefinition getComponentType() {
                        return super.getComponentType();
                    }

                    public AnnotationList getDeclaredAnnotations() {
                        return getAnnotationReader().asList();
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$LazyProjection$ForLoadedSuperClass.class */
            public static class ForLoadedSuperClass extends WithLazyNavigation.OfAnnotatedElement {
                private final Class<?> type;
                private transient /* synthetic */ Generic resolved;

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection.WithLazyNavigation.OfAnnotatedElement, net.bytebuddy.description.annotation.AnnotationSource
                public /* bridge */ /* synthetic */ AnnotationList getDeclaredAnnotations() {
                    return super.getDeclaredAnnotations();
                }

                public ForLoadedSuperClass(Class<?> type) {
                    this.type = type;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection
                @CachedReturnPlugin.Enhance("resolved")
                protected Generic resolve() {
                    Generic describe;
                    if (this.resolved != null) {
                        describe = null;
                    } else {
                        Type superClass = this.type.getGenericSuperclass();
                        describe = superClass == null ? Generic.UNDEFINED : TypeDefinition.Sort.describe(superClass, getAnnotationReader());
                    }
                    Generic generic = describe;
                    if (generic == null) {
                        generic = this.resolved;
                    } else {
                        this.resolved = generic;
                    }
                    return generic;
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public TypeDescription asErasure() {
                    Class<?> superClass = this.type.getSuperclass();
                    return superClass == null ? TypeDescription.UNDEFINED : ForLoadedType.of(superClass);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection.WithLazyNavigation.OfAnnotatedElement
                protected AnnotationReader getAnnotationReader() {
                    return AnnotationReader.DISPATCHER.resolveSuperClassType(this.type);
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$LazyProjection$ForLoadedFieldType.class */
            public static class ForLoadedFieldType extends WithEagerNavigation.OfAnnotatedElement {
                private final Field field;
                private transient /* synthetic */ Generic resolved;

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection.WithEagerNavigation.OfAnnotatedElement, net.bytebuddy.description.annotation.AnnotationSource
                public /* bridge */ /* synthetic */ AnnotationList getDeclaredAnnotations() {
                    return super.getDeclaredAnnotations();
                }

                public ForLoadedFieldType(Field field) {
                    this.field = field;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection
                @CachedReturnPlugin.Enhance("resolved")
                protected Generic resolve() {
                    Generic describe = this.resolved != null ? null : TypeDefinition.Sort.describe(this.field.getGenericType(), getAnnotationReader());
                    if (describe == null) {
                        describe = this.resolved;
                    } else {
                        this.resolved = describe;
                    }
                    return describe;
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public TypeDescription asErasure() {
                    return ForLoadedType.of(this.field.getType());
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection.WithEagerNavigation.OfAnnotatedElement
                protected AnnotationReader getAnnotationReader() {
                    return AnnotationReader.DISPATCHER.resolveFieldType(this.field);
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$LazyProjection$ForLoadedReturnType.class */
            public static class ForLoadedReturnType extends WithEagerNavigation.OfAnnotatedElement {
                private final Method method;
                private transient /* synthetic */ Generic resolved;

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection.WithEagerNavigation.OfAnnotatedElement, net.bytebuddy.description.annotation.AnnotationSource
                public /* bridge */ /* synthetic */ AnnotationList getDeclaredAnnotations() {
                    return super.getDeclaredAnnotations();
                }

                public ForLoadedReturnType(Method method) {
                    this.method = method;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection
                @CachedReturnPlugin.Enhance("resolved")
                protected Generic resolve() {
                    Generic describe = this.resolved != null ? null : TypeDefinition.Sort.describe(this.method.getGenericReturnType(), getAnnotationReader());
                    if (describe == null) {
                        describe = this.resolved;
                    } else {
                        this.resolved = describe;
                    }
                    return describe;
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public TypeDescription asErasure() {
                    return ForLoadedType.of(this.method.getReturnType());
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection.WithEagerNavigation.OfAnnotatedElement
                protected AnnotationReader getAnnotationReader() {
                    return AnnotationReader.DISPATCHER.resolveReturnType(this.method);
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$LazyProjection$OfConstructorParameter.class */
            public static class OfConstructorParameter extends WithEagerNavigation.OfAnnotatedElement {
                private final Constructor<?> constructor;
                private final int index;
                private final Class<?>[] erasure;
                private transient /* synthetic */ Generic delegate;

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection.WithEagerNavigation.OfAnnotatedElement, net.bytebuddy.description.annotation.AnnotationSource
                public /* bridge */ /* synthetic */ AnnotationList getDeclaredAnnotations() {
                    return super.getDeclaredAnnotations();
                }

                @SuppressFBWarnings(value = {"EI_EXPOSE_REP2"}, justification = "The array is never exposed outside of the class")
                public OfConstructorParameter(Constructor<?> constructor, int index, Class<?>[] erasure) {
                    this.constructor = constructor;
                    this.index = index;
                    this.erasure = erasure;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection
                @CachedReturnPlugin.Enhance(MethodDelegation.ImplementationDelegate.FIELD_NAME_PREFIX)
                protected Generic resolve() {
                    Generic of;
                    if (this.delegate != null) {
                        of = null;
                    } else {
                        Type[] type = this.constructor.getGenericParameterTypes();
                        if (this.erasure.length == type.length) {
                            of = TypeDefinition.Sort.describe(type[this.index], getAnnotationReader());
                        } else {
                            of = OfNonGenericType.ForLoadedType.of(this.erasure[this.index]);
                        }
                    }
                    Generic generic = of;
                    if (generic == null) {
                        generic = this.delegate;
                    } else {
                        this.delegate = generic;
                    }
                    return generic;
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public TypeDescription asErasure() {
                    return ForLoadedType.of(this.erasure[this.index]);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection.WithEagerNavigation.OfAnnotatedElement
                protected AnnotationReader getAnnotationReader() {
                    return AnnotationReader.DISPATCHER.resolveParameterType(this.constructor, this.index);
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$LazyProjection$OfMethodParameter.class */
            public static class OfMethodParameter extends WithEagerNavigation.OfAnnotatedElement {
                private final Method method;
                private final int index;
                private final Class<?>[] erasure;
                private transient /* synthetic */ Generic resolved;

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection.WithEagerNavigation.OfAnnotatedElement, net.bytebuddy.description.annotation.AnnotationSource
                public /* bridge */ /* synthetic */ AnnotationList getDeclaredAnnotations() {
                    return super.getDeclaredAnnotations();
                }

                @SuppressFBWarnings(value = {"EI_EXPOSE_REP2"}, justification = "The array is never exposed outside of the class")
                public OfMethodParameter(Method method, int index, Class<?>[] erasure) {
                    this.method = method;
                    this.index = index;
                    this.erasure = erasure;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection
                @CachedReturnPlugin.Enhance("resolved")
                protected Generic resolve() {
                    Generic of;
                    if (this.resolved != null) {
                        of = null;
                    } else {
                        Type[] type = this.method.getGenericParameterTypes();
                        if (this.erasure.length == type.length) {
                            of = TypeDefinition.Sort.describe(type[this.index], getAnnotationReader());
                        } else {
                            of = OfNonGenericType.ForLoadedType.of(this.erasure[this.index]);
                        }
                    }
                    Generic generic = of;
                    if (generic == null) {
                        generic = this.resolved;
                    } else {
                        this.resolved = generic;
                    }
                    return generic;
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public TypeDescription asErasure() {
                    return ForLoadedType.of(this.erasure[this.index]);
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection.WithEagerNavigation.OfAnnotatedElement
                protected AnnotationReader getAnnotationReader() {
                    return AnnotationReader.DISPATCHER.resolveParameterType(this.method, this.index);
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$LazyProjection$OfRecordComponent.class */
            public static class OfRecordComponent extends WithEagerNavigation.OfAnnotatedElement {
                private final Object recordComponent;
                private transient /* synthetic */ Generic resolved;

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection.WithEagerNavigation.OfAnnotatedElement, net.bytebuddy.description.annotation.AnnotationSource
                public /* bridge */ /* synthetic */ AnnotationList getDeclaredAnnotations() {
                    return super.getDeclaredAnnotations();
                }

                /* JADX INFO: Access modifiers changed from: protected */
                public OfRecordComponent(Object recordComponent) {
                    this.recordComponent = recordComponent;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection
                @CachedReturnPlugin.Enhance("resolved")
                protected Generic resolve() {
                    Generic describe = this.resolved != null ? null : TypeDefinition.Sort.describe(RecordComponentDescription.ForLoadedRecordComponent.DISPATCHER.getGenericType(this.recordComponent), getAnnotationReader());
                    if (describe == null) {
                        describe = this.resolved;
                    } else {
                        this.resolved = describe;
                    }
                    return describe;
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public TypeDescription asErasure() {
                    return ForLoadedType.of(RecordComponentDescription.ForLoadedRecordComponent.DISPATCHER.getType(this.recordComponent));
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection.WithEagerNavigation.OfAnnotatedElement
                protected AnnotationReader getAnnotationReader() {
                    return new AnnotationReader.Delegator.ForRecordComponent(this.recordComponent);
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$LazyProjection$WithResolvedErasure.class */
            public static class WithResolvedErasure extends WithEagerNavigation {
                private final Generic delegate;
                private final Visitor<? extends Generic> visitor;
                private final AnnotationSource annotationSource;
                private transient /* synthetic */ Generic resolved;

                public WithResolvedErasure(Generic delegate, Visitor<? extends Generic> visitor) {
                    this(delegate, visitor, delegate);
                }

                public WithResolvedErasure(Generic delegate, Visitor<? extends Generic> visitor, AnnotationSource annotationSource) {
                    this.delegate = delegate;
                    this.visitor = visitor;
                    this.annotationSource = annotationSource;
                }

                @Override // net.bytebuddy.description.annotation.AnnotationSource
                public AnnotationList getDeclaredAnnotations() {
                    return this.annotationSource.getDeclaredAnnotations();
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public TypeDescription asErasure() {
                    return this.delegate.asErasure();
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection
                @CachedReturnPlugin.Enhance("resolved")
                protected Generic resolve() {
                    Generic generic = this.resolved != null ? null : (Generic) this.delegate.accept(this.visitor);
                    if (generic == null) {
                        generic = this.resolved;
                    } else {
                        this.resolved = generic;
                    }
                    return generic;
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$Builder.class */
        public static abstract class Builder {
            private static final Type UNDEFINED = null;
            protected final List<? extends AnnotationDescription> annotations;

            protected abstract Builder doAnnotate(List<? extends AnnotationDescription> list);

            protected abstract Generic doBuild();

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.annotations.equals(((Builder) obj).annotations);
            }

            public int hashCode() {
                return (17 * 31) + this.annotations.hashCode();
            }

            protected Builder(List<? extends AnnotationDescription> annotations) {
                this.annotations = annotations;
            }

            public static Builder rawType(Class<?> type) {
                return rawType(ForLoadedType.of(type));
            }

            public static Builder rawType(TypeDescription type) {
                return new OfNonGenericType(type);
            }

            public static Builder rawType(Class<?> type, Generic ownerType) {
                return rawType(ForLoadedType.of(type), ownerType);
            }

            public static Builder rawType(TypeDescription type, Generic ownerType) {
                TypeDescription declaringType = type.getDeclaringType();
                if (declaringType == null && ownerType != null) {
                    throw new IllegalArgumentException(type + " does not have a declaring type: " + ownerType);
                }
                if (declaringType != null && (ownerType == null || !declaringType.equals(ownerType.asErasure()))) {
                    throw new IllegalArgumentException(ownerType + " is not the declaring type of " + type);
                }
                return new OfNonGenericType(type, ownerType);
            }

            public static Generic unboundWildcard() {
                return unboundWildcard(Collections.emptySet());
            }

            public static Generic unboundWildcard(Annotation... annotation) {
                return unboundWildcard((List<? extends Annotation>) Arrays.asList(annotation));
            }

            public static Generic unboundWildcard(List<? extends Annotation> annotations) {
                return unboundWildcard((Collection<? extends AnnotationDescription>) new AnnotationList.ForLoadedAnnotations(annotations));
            }

            public static Generic unboundWildcard(AnnotationDescription... annotation) {
                return unboundWildcard((Collection<? extends AnnotationDescription>) Arrays.asList(annotation));
            }

            public static Generic unboundWildcard(Collection<? extends AnnotationDescription> annotations) {
                return OfWildcardType.Latent.unbounded(new AnnotationSource.Explicit(new ArrayList(annotations)));
            }

            public static Builder typeVariable(String symbol) {
                return new OfTypeVariable(symbol);
            }

            public static Builder parameterizedType(Class<?> rawType, Type... parameter) {
                return parameterizedType(rawType, Arrays.asList(parameter));
            }

            public static Builder parameterizedType(Class<?> rawType, List<? extends Type> parameters) {
                return parameterizedType(rawType, UNDEFINED, parameters);
            }

            public static Builder parameterizedType(Class<?> rawType, Type ownerType, List<? extends Type> parameters) {
                return parameterizedType(ForLoadedType.of(rawType), ownerType == null ? null : TypeDefinition.Sort.describe(ownerType), new TypeList.Generic.ForLoadedTypes(parameters));
            }

            public static Builder parameterizedType(TypeDescription rawType, TypeDefinition... parameter) {
                return parameterizedType(rawType, Arrays.asList(parameter));
            }

            public static Builder parameterizedType(TypeDescription rawType, Collection<? extends TypeDefinition> parameters) {
                return parameterizedType(rawType, Generic.UNDEFINED, parameters);
            }

            public static Builder parameterizedType(TypeDescription rawType, Generic ownerType, Collection<? extends TypeDefinition> parameters) {
                TypeDescription declaringType = rawType.getDeclaringType();
                if (ownerType == null && declaringType != null && rawType.isStatic()) {
                    ownerType = declaringType.asGenericType();
                }
                if (!rawType.represents(TargetType.class)) {
                    if (!rawType.isGenerified()) {
                        throw new IllegalArgumentException(rawType + " is not a parameterized type");
                    }
                    if (ownerType == null && declaringType != null && !rawType.isStatic()) {
                        throw new IllegalArgumentException(rawType + " requires an owner type");
                    }
                    if (ownerType != null && !ownerType.asErasure().equals(declaringType)) {
                        throw new IllegalArgumentException(ownerType + " does not represent required owner for " + rawType);
                    }
                    if (ownerType != null && (rawType.isStatic() ^ ownerType.getSort().isNonGeneric())) {
                        throw new IllegalArgumentException(ownerType + " does not define the correct parameters for owning " + rawType);
                    }
                    if (rawType.getTypeVariables().size() != parameters.size()) {
                        throw new IllegalArgumentException(parameters + " does not contain number of required parameters for " + rawType);
                    }
                }
                return new OfParameterizedType(rawType, ownerType, new TypeList.Generic.Explicit(new ArrayList(parameters)));
            }

            public Generic asWildcardUpperBound() {
                return asWildcardUpperBound(Collections.emptySet());
            }

            public Generic asWildcardUpperBound(Annotation... annotation) {
                return asWildcardUpperBound(Arrays.asList(annotation));
            }

            public Generic asWildcardUpperBound(List<? extends Annotation> annotations) {
                return asWildcardUpperBound((Collection<? extends AnnotationDescription>) new AnnotationList.ForLoadedAnnotations(annotations));
            }

            public Generic asWildcardUpperBound(AnnotationDescription... annotation) {
                return asWildcardUpperBound((Collection<? extends AnnotationDescription>) Arrays.asList(annotation));
            }

            public Generic asWildcardUpperBound(Collection<? extends AnnotationDescription> annotations) {
                return OfWildcardType.Latent.boundedAbove(build(), new AnnotationSource.Explicit(new ArrayList(annotations)));
            }

            public Generic asWildcardLowerBound() {
                return asWildcardLowerBound(Collections.emptySet());
            }

            public Generic asWildcardLowerBound(Annotation... annotation) {
                return asWildcardLowerBound(Arrays.asList(annotation));
            }

            public Generic asWildcardLowerBound(List<? extends Annotation> annotations) {
                return asWildcardLowerBound((Collection<? extends AnnotationDescription>) new AnnotationList.ForLoadedAnnotations(annotations));
            }

            public Generic asWildcardLowerBound(AnnotationDescription... annotation) {
                return asWildcardLowerBound((Collection<? extends AnnotationDescription>) Arrays.asList(annotation));
            }

            public Generic asWildcardLowerBound(Collection<? extends AnnotationDescription> annotations) {
                return OfWildcardType.Latent.boundedBelow(build(), new AnnotationSource.Explicit(new ArrayList(annotations)));
            }

            public Builder asArray() {
                return asArray(1);
            }

            public Builder asArray(int arity) {
                if (arity < 1) {
                    throw new IllegalArgumentException("Cannot define an array of a non-positive arity: " + arity);
                }
                Generic build = build();
                while (true) {
                    Generic typeDescription = build;
                    arity--;
                    if (arity > 0) {
                        build = new OfGenericArray.Latent(typeDescription, AnnotationSource.Empty.INSTANCE);
                    } else {
                        return new OfGenericArrayType(typeDescription);
                    }
                }
            }

            public Builder annotate(Annotation... annotation) {
                return annotate(Arrays.asList(annotation));
            }

            public Builder annotate(List<? extends Annotation> annotations) {
                return annotate((Collection<? extends AnnotationDescription>) new AnnotationList.ForLoadedAnnotations(annotations));
            }

            public Builder annotate(AnnotationDescription... annotation) {
                return annotate((Collection<? extends AnnotationDescription>) Arrays.asList(annotation));
            }

            public Builder annotate(Collection<? extends AnnotationDescription> annotations) {
                return doAnnotate(new ArrayList(annotations));
            }

            public Generic build() {
                return doBuild();
            }

            public Generic build(Annotation... annotation) {
                return build(Arrays.asList(annotation));
            }

            public Generic build(List<? extends Annotation> annotations) {
                return build((Collection<? extends AnnotationDescription>) new AnnotationList.ForLoadedAnnotations(annotations));
            }

            public Generic build(AnnotationDescription... annotation) {
                return build((Collection<? extends AnnotationDescription>) Arrays.asList(annotation));
            }

            public Generic build(Collection<? extends AnnotationDescription> annotations) {
                return doAnnotate(new ArrayList(annotations)).doBuild();
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$Builder$OfNonGenericType.class */
            public static class OfNonGenericType extends Builder {
                private final TypeDescription typeDescription;
                @HashCodeAndEqualsPlugin.ValueHandling(HashCodeAndEqualsPlugin.ValueHandling.Sort.REVERSE_NULLABILITY)
                private final Generic ownerType;

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Builder
                public boolean equals(Object obj) {
                    if (super.equals(obj)) {
                        if (this == obj) {
                            return true;
                        }
                        if (obj != null && getClass() == obj.getClass() && this.typeDescription.equals(((OfNonGenericType) obj).typeDescription)) {
                            Generic generic = this.ownerType;
                            Generic generic2 = ((OfNonGenericType) obj).ownerType;
                            return generic2 != null ? generic != null && generic.equals(generic2) : generic == null;
                        }
                        return false;
                    }
                    return false;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Builder
                public int hashCode() {
                    int hashCode = ((super.hashCode() * 31) + this.typeDescription.hashCode()) * 31;
                    Generic generic = this.ownerType;
                    return generic != null ? hashCode + generic.hashCode() : hashCode;
                }

                protected OfNonGenericType(TypeDescription typeDescription) {
                    this(typeDescription, typeDescription.getDeclaringType());
                }

                private OfNonGenericType(TypeDescription typeDescription, TypeDescription ownerType) {
                    this(typeDescription, ownerType == null ? Generic.UNDEFINED : ownerType.asGenericType());
                }

                protected OfNonGenericType(TypeDescription typeDescription, Generic ownerType) {
                    this(typeDescription, ownerType, Collections.emptyList());
                }

                protected OfNonGenericType(TypeDescription typeDescription, Generic ownerType, List<? extends AnnotationDescription> annotations) {
                    super(annotations);
                    this.ownerType = ownerType;
                    this.typeDescription = typeDescription;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Builder
                protected Builder doAnnotate(List<? extends AnnotationDescription> annotations) {
                    return new OfNonGenericType(this.typeDescription, this.ownerType, CompoundList.of((List) this.annotations, (List) annotations));
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Builder
                protected Generic doBuild() {
                    if (this.typeDescription.represents(Void.TYPE) && !this.annotations.isEmpty()) {
                        throw new IllegalArgumentException("The void non-type cannot be annotated");
                    }
                    return new OfNonGenericType.Latent(this.typeDescription, this.ownerType, new AnnotationSource.Explicit(this.annotations));
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$Builder$OfParameterizedType.class */
            public static class OfParameterizedType extends Builder {
                private final TypeDescription rawType;
                private final Generic ownerType;
                private final List<? extends Generic> parameterTypes;

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Builder
                public boolean equals(Object obj) {
                    if (super.equals(obj)) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.rawType.equals(((OfParameterizedType) obj).rawType) && this.ownerType.equals(((OfParameterizedType) obj).ownerType) && this.parameterTypes.equals(((OfParameterizedType) obj).parameterTypes);
                    }
                    return false;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Builder
                public int hashCode() {
                    return (((((super.hashCode() * 31) + this.rawType.hashCode()) * 31) + this.ownerType.hashCode()) * 31) + this.parameterTypes.hashCode();
                }

                protected OfParameterizedType(TypeDescription rawType, Generic ownerType, List<? extends Generic> parameterTypes) {
                    this(rawType, ownerType, parameterTypes, Collections.emptyList());
                }

                protected OfParameterizedType(TypeDescription rawType, Generic ownerType, List<? extends Generic> parameterTypes, List<? extends AnnotationDescription> annotations) {
                    super(annotations);
                    this.rawType = rawType;
                    this.ownerType = ownerType;
                    this.parameterTypes = parameterTypes;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Builder
                protected Builder doAnnotate(List<? extends AnnotationDescription> annotations) {
                    return new OfParameterizedType(this.rawType, this.ownerType, this.parameterTypes, CompoundList.of((List) this.annotations, (List) annotations));
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Builder
                protected Generic doBuild() {
                    return new OfParameterizedType.Latent(this.rawType, this.ownerType, this.parameterTypes, new AnnotationSource.Explicit(this.annotations));
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$Builder$OfGenericArrayType.class */
            public static class OfGenericArrayType extends Builder {
                private final Generic componentType;

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Builder
                public boolean equals(Object obj) {
                    if (super.equals(obj)) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.componentType.equals(((OfGenericArrayType) obj).componentType);
                    }
                    return false;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Builder
                public int hashCode() {
                    return (super.hashCode() * 31) + this.componentType.hashCode();
                }

                protected OfGenericArrayType(Generic componentType) {
                    this(componentType, Collections.emptyList());
                }

                protected OfGenericArrayType(Generic componentType, List<? extends AnnotationDescription> annotations) {
                    super(annotations);
                    this.componentType = componentType;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Builder
                protected Builder doAnnotate(List<? extends AnnotationDescription> annotations) {
                    return new OfGenericArrayType(this.componentType, CompoundList.of((List) this.annotations, (List) annotations));
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Builder
                protected Generic doBuild() {
                    return new OfGenericArray.Latent(this.componentType, new AnnotationSource.Explicit(this.annotations));
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Generic$Builder$OfTypeVariable.class */
            protected static class OfTypeVariable extends Builder {
                private final String symbol;

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Builder
                public boolean equals(Object obj) {
                    if (super.equals(obj)) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.symbol.equals(((OfTypeVariable) obj).symbol);
                    }
                    return false;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Builder
                public int hashCode() {
                    return (super.hashCode() * 31) + this.symbol.hashCode();
                }

                protected OfTypeVariable(String symbol) {
                    this(symbol, Collections.emptyList());
                }

                protected OfTypeVariable(String symbol, List<? extends AnnotationDescription> annotations) {
                    super(annotations);
                    this.symbol = symbol;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Builder
                protected Builder doAnnotate(List<? extends AnnotationDescription> annotations) {
                    return new OfTypeVariable(this.symbol, CompoundList.of((List) this.annotations, (List) annotations));
                }

                @Override // net.bytebuddy.description.type.TypeDescription.Generic.Builder
                protected Generic doBuild() {
                    return new OfTypeVariable.Symbolic(this.symbol, new AnnotationSource.Explicit(this.annotations));
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$AbstractBase.class */
    public static abstract class AbstractBase extends TypeVariableSource.AbstractBase implements TypeDescription {
        public static final boolean RAW_TYPES;
        private transient /* synthetic */ int hashCode_J6JZZ80F;

        static {
            boolean rawTypes;
            try {
                rawTypes = Boolean.parseBoolean((String) AccessController.doPrivileged(new GetSystemPropertyAction(TypeDefinition.RAW_TYPES_PROPERTY)));
            } catch (Exception e) {
                rawTypes = false;
            }
            RAW_TYPES = rawTypes;
        }

        private static boolean isAssignable(TypeDescription sourceType, TypeDescription targetType) {
            if (sourceType.equals(targetType)) {
                return true;
            }
            if (targetType.isArray()) {
                if (sourceType.isArray()) {
                    return isAssignable(sourceType.getComponentType(), targetType.getComponentType());
                }
                return sourceType.represents(Object.class) || ARRAY_INTERFACES.contains(sourceType.asGenericType());
            } else if (sourceType.represents(Object.class)) {
                return !targetType.isPrimitive();
            } else {
                Generic superClass = targetType.getSuperClass();
                if (superClass != null && sourceType.isAssignableFrom(superClass.asErasure())) {
                    return true;
                }
                if (sourceType.isInterface()) {
                    for (TypeDescription interfaceType : targetType.getInterfaces().asErasures()) {
                        if (sourceType.isAssignableFrom(interfaceType)) {
                            return true;
                        }
                    }
                    return false;
                }
                return false;
            }
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public boolean isAssignableFrom(Class<?> type) {
            return isAssignableFrom(ForLoadedType.of(type));
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public boolean isAssignableFrom(TypeDescription typeDescription) {
            return isAssignable(this, typeDescription);
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public boolean isAssignableTo(Class<?> type) {
            return isAssignableTo(ForLoadedType.of(type));
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public boolean isAssignableTo(TypeDescription typeDescription) {
            return isAssignable(typeDescription, this);
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public boolean isInHierarchyWith(Class<?> type) {
            return isAssignableTo(type) || isAssignableFrom(type);
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public boolean isInHierarchyWith(TypeDescription typeDescription) {
            return isAssignableTo(typeDescription) || isAssignableFrom(typeDescription);
        }

        @Override // net.bytebuddy.description.type.TypeDefinition
        public TypeDescription asErasure() {
            return this;
        }

        @Override // net.bytebuddy.description.type.TypeDefinition
        public Generic asGenericType() {
            return new Generic.OfNonGenericType.ForErasure(this);
        }

        @Override // net.bytebuddy.description.type.TypeDefinition
        public TypeDefinition.Sort getSort() {
            return TypeDefinition.Sort.NON_GENERIC;
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public boolean isInstance(Object value) {
            return isAssignableFrom(value.getClass());
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public boolean isAnnotationValue(Object value) {
            EnumerationDescription[] enumerationDescriptionArr;
            AnnotationDescription[] annotationDescriptionArr;
            if (!represents(Class.class) || !(value instanceof TypeDescription)) {
                if (!(value instanceof AnnotationDescription) || !((AnnotationDescription) value).getAnnotationType().equals(this)) {
                    if (!(value instanceof EnumerationDescription) || !((EnumerationDescription) value).getEnumerationType().equals(this)) {
                        if (!represents(String.class) || !(value instanceof String)) {
                            if (!represents(Boolean.TYPE) || !(value instanceof Boolean)) {
                                if (!represents(Byte.TYPE) || !(value instanceof Byte)) {
                                    if (!represents(Short.TYPE) || !(value instanceof Short)) {
                                        if (!represents(Character.TYPE) || !(value instanceof Character)) {
                                            if (!represents(Integer.TYPE) || !(value instanceof Integer)) {
                                                if (!represents(Long.TYPE) || !(value instanceof Long)) {
                                                    if (!represents(Float.TYPE) || !(value instanceof Float)) {
                                                        if (!represents(Double.TYPE) || !(value instanceof Double)) {
                                                            if (!represents(String[].class) || !(value instanceof String[])) {
                                                                if (!represents(boolean[].class) || !(value instanceof boolean[])) {
                                                                    if (!represents(byte[].class) || !(value instanceof byte[])) {
                                                                        if (!represents(short[].class) || !(value instanceof short[])) {
                                                                            if (!represents(char[].class) || !(value instanceof char[])) {
                                                                                if (!represents(int[].class) || !(value instanceof int[])) {
                                                                                    if (!represents(long[].class) || !(value instanceof long[])) {
                                                                                        if (!represents(float[].class) || !(value instanceof float[])) {
                                                                                            if (!represents(double[].class) || !(value instanceof double[])) {
                                                                                                if (represents(Class[].class) && (value instanceof TypeDescription[])) {
                                                                                                    return true;
                                                                                                }
                                                                                                if (isAssignableTo(Annotation[].class) && (value instanceof AnnotationDescription[])) {
                                                                                                    for (AnnotationDescription annotationDescription : (AnnotationDescription[]) value) {
                                                                                                        if (!annotationDescription.getAnnotationType().equals(getComponentType())) {
                                                                                                            return false;
                                                                                                        }
                                                                                                    }
                                                                                                    return true;
                                                                                                } else if (isAssignableTo(Enum[].class) && (value instanceof EnumerationDescription[])) {
                                                                                                    for (EnumerationDescription enumerationDescription : (EnumerationDescription[]) value) {
                                                                                                        if (!enumerationDescription.getEnumerationType().equals(getComponentType())) {
                                                                                                            return false;
                                                                                                        }
                                                                                                    }
                                                                                                    return true;
                                                                                                } else {
                                                                                                    return false;
                                                                                                }
                                                                                            }
                                                                                            return true;
                                                                                        }
                                                                                        return true;
                                                                                    }
                                                                                    return true;
                                                                                }
                                                                                return true;
                                                                            }
                                                                            return true;
                                                                        }
                                                                        return true;
                                                                    }
                                                                    return true;
                                                                }
                                                                return true;
                                                            }
                                                            return true;
                                                        }
                                                        return true;
                                                    }
                                                    return true;
                                                }
                                                return true;
                                            }
                                            return true;
                                        }
                                        return true;
                                    }
                                    return true;
                                }
                                return true;
                            }
                            return true;
                        }
                        return true;
                    }
                    return true;
                }
                return true;
            }
            return true;
        }

        @Override // net.bytebuddy.description.NamedElement.WithRuntimeName
        public String getInternalName() {
            return getName().replace('.', '/');
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public int getActualModifiers(boolean superFlag) {
            int actualModifiers = getModifiers() | (getDeclaredAnnotations().isAnnotationPresent(Deprecated.class) ? 131072 : 0) | (isRecord() ? 65536 : 0) | (superFlag ? 32 : 0);
            if (isPrivate()) {
                return actualModifiers & (-11);
            }
            if (isProtected()) {
                return (actualModifiers & (-13)) | 1;
            }
            return actualModifiers & (-9);
        }

        @Override // net.bytebuddy.description.NamedElement.WithDescriptor
        public String getGenericSignature() {
            SignatureVisitor visitClassBound;
            try {
                SignatureWriter signatureWriter = new SignatureWriter();
                boolean generic = false;
                for (Generic typeVariable : getTypeVariables()) {
                    signatureWriter.visitFormalTypeParameter(typeVariable.getSymbol());
                    for (Generic upperBound : typeVariable.getUpperBounds()) {
                        if (upperBound.asErasure().isInterface()) {
                            visitClassBound = signatureWriter.visitInterfaceBound();
                        } else {
                            visitClassBound = signatureWriter.visitClassBound();
                        }
                        upperBound.accept(new Generic.Visitor.ForSignatureVisitor(visitClassBound));
                    }
                    generic = true;
                }
                Generic superClass = getSuperClass();
                if (superClass == null) {
                    superClass = Generic.OBJECT;
                }
                superClass.accept(new Generic.Visitor.ForSignatureVisitor(signatureWriter.visitSuperclass()));
                boolean generic2 = generic || !superClass.getSort().isNonGeneric();
                for (Generic interfaceType : getInterfaces()) {
                    interfaceType.accept(new Generic.Visitor.ForSignatureVisitor(signatureWriter.visitInterface()));
                    generic2 = generic2 || !interfaceType.getSort().isNonGeneric();
                }
                return generic2 ? signatureWriter.toString() : NON_GENERIC_SIGNATURE;
            } catch (GenericSignatureFormatError e) {
                return NON_GENERIC_SIGNATURE;
            }
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public boolean isSamePackage(TypeDescription typeDescription) {
            PackageDescription thisPackage = getPackage();
            PackageDescription otherPackage = typeDescription.getPackage();
            if (thisPackage == null || otherPackage == null) {
                return thisPackage == otherPackage;
            }
            return thisPackage.equals(otherPackage);
        }

        @Override // net.bytebuddy.description.ByteCodeElement
        public boolean isVisibleTo(TypeDescription typeDescription) {
            return isPrimitive() || (!isArray() ? !(isPublic() || isProtected() || isSamePackage(typeDescription)) : !getComponentType().isVisibleTo(typeDescription));
        }

        @Override // net.bytebuddy.description.ByteCodeElement
        public boolean isAccessibleTo(TypeDescription typeDescription) {
            return isPrimitive() || (!isArray() ? !(isPublic() || isSamePackage(typeDescription)) : !getComponentType().isVisibleTo(typeDescription));
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public AnnotationList getInheritedAnnotations() {
            Generic superClass = getSuperClass();
            AnnotationList<AnnotationDescription> declaredAnnotations = getDeclaredAnnotations();
            if (superClass == null) {
                return declaredAnnotations;
            }
            Set<TypeDescription> annotationTypes = new HashSet<>();
            for (AnnotationDescription annotationDescription : declaredAnnotations) {
                annotationTypes.add(annotationDescription.getAnnotationType());
            }
            return new AnnotationList.Explicit(CompoundList.of((List) declaredAnnotations, (List) superClass.asErasure().getInheritedAnnotations().inherited(annotationTypes)));
        }

        @Override // net.bytebuddy.description.NamedElement
        public String getActualName() {
            if (isArray()) {
                TypeDescription typeDescription = this;
                int dimensions = 0;
                do {
                    dimensions++;
                    typeDescription = typeDescription.getComponentType();
                } while (typeDescription.isArray());
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(typeDescription.getActualName());
                for (int i = 0; i < dimensions; i++) {
                    stringBuilder.append("[]");
                }
                return stringBuilder.toString();
            }
            return getName();
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public boolean isPrimitiveWrapper() {
            return represents(Boolean.class) || represents(Byte.class) || represents(Short.class) || represents(Character.class) || represents(Integer.class) || represents(Long.class) || represents(Float.class) || represents(Double.class);
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public boolean isAnnotationReturnType() {
            return isPrimitive() || represents(String.class) || (isAssignableTo(Enum.class) && !represents(Enum.class)) || ((isAssignableTo(Annotation.class) && !represents(Annotation.class)) || represents(Class.class) || (isArray() && !getComponentType().isArray() && getComponentType().isAnnotationReturnType()));
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public boolean isAnnotationValue() {
            return isPrimitive() || represents(String.class) || isAssignableTo(TypeDescription.class) || isAssignableTo(AnnotationDescription.class) || isAssignableTo(EnumerationDescription.class) || (isArray() && !getComponentType().isArray() && getComponentType().isAnnotationValue());
        }

        @Override // net.bytebuddy.description.type.TypeDefinition
        @SuppressFBWarnings(value = {"EC_UNRELATED_CLASS_AND_INTERFACE"}, justification = "Fits equality contract for type definitions")
        public boolean represents(Type type) {
            return equals(TypeDefinition.Sort.describe(type));
        }

        @Override // net.bytebuddy.description.type.TypeDefinition
        public String getTypeName() {
            return getName();
        }

        @Override // net.bytebuddy.description.TypeVariableSource
        public TypeVariableSource getEnclosingSource() {
            MethodDescription enclosingMethod = getEnclosingMethod();
            return enclosingMethod == null ? isStatic() ? TypeVariableSource.UNDEFINED : getEnclosingType() : enclosingMethod;
        }

        @Override // net.bytebuddy.description.TypeVariableSource
        public boolean isInferrable() {
            return false;
        }

        @Override // net.bytebuddy.description.TypeVariableSource
        public <T> T accept(TypeVariableSource.Visitor<T> visitor) {
            return visitor.onType(this);
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public boolean isPackageType() {
            return getSimpleName().equals(PackageDescription.PACKAGE_CLASS_NAME);
        }

        @Override // net.bytebuddy.description.TypeVariableSource
        public boolean isGenerified() {
            TypeDescription declaringType;
            if (getTypeVariables().isEmpty()) {
                return (isStatic() || (declaringType = getDeclaringType()) == null || !declaringType.isGenerified()) ? false : true;
            }
            return true;
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public int getInnerClassCount() {
            TypeDescription declaringType;
            if (isStatic() || (declaringType = getDeclaringType()) == null) {
                return 0;
            }
            return declaringType.getInnerClassCount() + 1;
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public boolean isInnerClass() {
            return !isStatic() && isNestedClass();
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public boolean isNestedClass() {
            return getDeclaringType() != null;
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public TypeDescription asBoxed() {
            if (represents(Boolean.TYPE)) {
                return ForLoadedType.of(Boolean.class);
            }
            if (represents(Byte.TYPE)) {
                return ForLoadedType.of(Byte.class);
            }
            if (represents(Short.TYPE)) {
                return ForLoadedType.of(Short.class);
            }
            if (represents(Character.TYPE)) {
                return ForLoadedType.of(Character.class);
            }
            if (represents(Integer.TYPE)) {
                return ForLoadedType.of(Integer.class);
            }
            if (represents(Long.TYPE)) {
                return ForLoadedType.of(Long.class);
            }
            if (represents(Float.TYPE)) {
                return ForLoadedType.of(Float.class);
            }
            if (represents(Double.TYPE)) {
                return ForLoadedType.of(Double.class);
            }
            return this;
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public TypeDescription asUnboxed() {
            if (represents(Boolean.class)) {
                return ForLoadedType.of(Boolean.TYPE);
            }
            if (represents(Byte.class)) {
                return ForLoadedType.of(Byte.TYPE);
            }
            if (represents(Short.class)) {
                return ForLoadedType.of(Short.TYPE);
            }
            if (represents(Character.class)) {
                return ForLoadedType.of(Character.TYPE);
            }
            if (represents(Integer.class)) {
                return ForLoadedType.of(Integer.TYPE);
            }
            if (represents(Long.class)) {
                return ForLoadedType.of(Long.TYPE);
            }
            if (represents(Float.class)) {
                return ForLoadedType.of(Float.TYPE);
            }
            if (represents(Double.class)) {
                return ForLoadedType.of(Double.TYPE);
            }
            return this;
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public Object getDefaultValue() {
            if (represents(Boolean.TYPE)) {
                return false;
            }
            if (represents(Byte.TYPE)) {
                return (byte) 0;
            }
            if (represents(Short.TYPE)) {
                return (short) 0;
            }
            if (represents(Character.TYPE)) {
                return (char) 0;
            }
            if (represents(Integer.TYPE)) {
                return 0;
            }
            if (represents(Long.TYPE)) {
                return 0L;
            }
            if (represents(Float.TYPE)) {
                return Float.valueOf(0.0f);
            }
            if (represents(Double.TYPE)) {
                return Double.valueOf((double) Const.default_value_double);
            }
            return null;
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public boolean isNestHost() {
            return equals(getNestHost());
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public boolean isNestMateOf(Class<?> type) {
            return isNestMateOf(ForLoadedType.of(type));
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public boolean isNestMateOf(TypeDescription typeDescription) {
            return getNestHost().equals(typeDescription.getNestHost());
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public boolean isMemberType() {
            return (isLocalType() || isAnonymousType() || getDeclaringType() == null) ? false : true;
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public boolean isCompileTimeConstant() {
            return represents(Integer.TYPE) || represents(Long.TYPE) || represents(Float.TYPE) || represents(Double.TYPE) || represents(String.class) || represents(Class.class) || equals(JavaType.METHOD_TYPE.getTypeStub()) || equals(JavaType.METHOD_HANDLE.getTypeStub());
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public boolean isSealed() {
            return (isPrimitive() || isArray() || getPermittedSubclasses().isEmpty()) ? false : true;
        }

        @Override // java.lang.Iterable
        public Iterator<TypeDefinition> iterator() {
            return new TypeDefinition.SuperClassIterator(this);
        }

        @CachedReturnPlugin.Enhance
        public int hashCode() {
            int hashCode = this.hashCode_J6JZZ80F != 0 ? 0 : getName().hashCode();
            if (hashCode == 0) {
                hashCode = this.hashCode_J6JZZ80F;
            } else {
                this.hashCode_J6JZZ80F = hashCode;
            }
            return hashCode;
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof TypeDefinition)) {
                return false;
            }
            TypeDefinition typeDefinition = (TypeDefinition) other;
            return typeDefinition.getSort().isNonGeneric() && getName().equals(typeDefinition.asErasure().getName());
        }

        public String toString() {
            String str;
            StringBuilder sb = new StringBuilder();
            if (isPrimitive()) {
                str = "";
            } else {
                str = (isInterface() ? "interface" : "class") + Instruction.argsep;
            }
            return sb.append(str).append(getName()).toString();
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$AbstractBase$OfSimpleType.class */
        public static abstract class OfSimpleType extends AbstractBase {
            @Override // net.bytebuddy.description.type.TypeDefinition
            public boolean isPrimitive() {
                return false;
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public boolean isArray() {
                return false;
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public TypeDescription getComponentType() {
                return TypeDescription.UNDEFINED;
            }

            @Override // net.bytebuddy.description.NamedElement.WithDescriptor
            public String getDescriptor() {
                return "L" + getInternalName() + ";";
            }

            @Override // net.bytebuddy.description.type.TypeDescription
            public String getCanonicalName() {
                if (isAnonymousType() || isLocalType()) {
                    return NO_NAME;
                }
                String internalName = getInternalName();
                TypeDescription enclosingType = getEnclosingType();
                if (enclosingType != null && internalName.startsWith(enclosingType.getInternalName() + "$")) {
                    return enclosingType.getCanonicalName() + "." + internalName.substring(enclosingType.getInternalName().length() + 1);
                }
                return getName();
            }

            @Override // net.bytebuddy.description.type.TypeDescription
            public String getSimpleName() {
                int simpleNameIndex;
                String internalName = getInternalName();
                TypeDescription enclosingType = getEnclosingType();
                if (enclosingType != null && internalName.startsWith(enclosingType.getInternalName() + "$")) {
                    simpleNameIndex = enclosingType.getInternalName().length() + 1;
                } else {
                    simpleNameIndex = internalName.lastIndexOf(47);
                    if (simpleNameIndex == -1) {
                        return internalName;
                    }
                }
                while (simpleNameIndex < internalName.length() && !Character.isLetter(internalName.charAt(simpleNameIndex))) {
                    simpleNameIndex++;
                }
                return internalName.substring(simpleNameIndex);
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            public StackSize getStackSize() {
                return StackSize.SINGLE;
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$AbstractBase$OfSimpleType$WithDelegation.class */
            public static abstract class WithDelegation extends OfSimpleType {
                protected abstract TypeDescription delegate();

                @Override // net.bytebuddy.description.type.TypeDescription.AbstractBase.OfSimpleType, net.bytebuddy.description.type.TypeDefinition
                public /* bridge */ /* synthetic */ TypeDefinition getComponentType() {
                    return super.getComponentType();
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public Generic getSuperClass() {
                    return delegate().getSuperClass();
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public TypeList.Generic getInterfaces() {
                    return delegate().getInterfaces();
                }

                @Override // net.bytebuddy.description.type.TypeDescription, net.bytebuddy.description.type.TypeDefinition
                public FieldList<FieldDescription.InDefinedShape> getDeclaredFields() {
                    return delegate().getDeclaredFields();
                }

                @Override // net.bytebuddy.description.type.TypeDescription, net.bytebuddy.description.type.TypeDefinition
                public MethodList<MethodDescription.InDefinedShape> getDeclaredMethods() {
                    return delegate().getDeclaredMethods();
                }

                @Override // net.bytebuddy.description.DeclaredByType, net.bytebuddy.description.field.FieldDescription.InDefinedShape
                public TypeDescription getDeclaringType() {
                    return delegate().getDeclaringType();
                }

                @Override // net.bytebuddy.description.type.TypeDescription
                public MethodDescription.InDefinedShape getEnclosingMethod() {
                    return delegate().getEnclosingMethod();
                }

                @Override // net.bytebuddy.description.type.TypeDescription
                public TypeDescription getEnclosingType() {
                    return delegate().getEnclosingType();
                }

                @Override // net.bytebuddy.description.type.TypeDescription
                public TypeList getDeclaredTypes() {
                    return delegate().getDeclaredTypes();
                }

                @Override // net.bytebuddy.description.type.TypeDescription
                public boolean isAnonymousType() {
                    return delegate().isAnonymousType();
                }

                @Override // net.bytebuddy.description.type.TypeDescription
                public boolean isLocalType() {
                    return delegate().isLocalType();
                }

                @Override // net.bytebuddy.description.type.TypeDescription
                public PackageDescription getPackage() {
                    return delegate().getPackage();
                }

                @Override // net.bytebuddy.description.annotation.AnnotationSource
                public AnnotationList getDeclaredAnnotations() {
                    return delegate().getDeclaredAnnotations();
                }

                @Override // net.bytebuddy.description.TypeVariableSource
                public TypeList.Generic getTypeVariables() {
                    return delegate().getTypeVariables();
                }

                @Override // net.bytebuddy.description.ModifierReviewable
                public int getModifiers() {
                    return delegate().getModifiers();
                }

                @Override // net.bytebuddy.description.type.TypeDescription.AbstractBase, net.bytebuddy.description.NamedElement.WithDescriptor
                public String getGenericSignature() {
                    return delegate().getGenericSignature();
                }

                @Override // net.bytebuddy.description.type.TypeDescription.AbstractBase, net.bytebuddy.description.type.TypeDescription
                public int getActualModifiers(boolean superFlag) {
                    return delegate().getActualModifiers(superFlag);
                }

                @Override // net.bytebuddy.description.type.TypeDescription
                public TypeDescription getNestHost() {
                    return delegate().getNestHost();
                }

                @Override // net.bytebuddy.description.type.TypeDescription
                public TypeList getNestMembers() {
                    return delegate().getNestMembers();
                }

                @Override // net.bytebuddy.description.type.TypeDescription, net.bytebuddy.description.type.TypeDefinition
                public RecordComponentList<RecordComponentDescription.InDefinedShape> getRecordComponents() {
                    return delegate().getRecordComponents();
                }

                @Override // net.bytebuddy.description.type.TypeDefinition
                public boolean isRecord() {
                    return delegate().isRecord();
                }

                @Override // net.bytebuddy.description.type.TypeDescription
                public TypeList getPermittedSubclasses() {
                    return delegate().getPermittedSubclasses();
                }
            }
        }
    }

    @SuppressFBWarnings(value = {"SE_TRANSIENT_FIELD_NOT_RESTORED"}, justification = "Field is only used as a cache store and is implicitly recomputed")
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$ForLoadedType.class */
    public static class ForLoadedType extends AbstractBase implements Serializable {
        private static final long serialVersionUID = 1;
        private static final Dispatcher DISPATCHER = (Dispatcher) AccessController.doPrivileged(Dispatcher.CreationAction.INSTANCE);
        @SuppressFBWarnings(value = {"MS_MUTABLE_COLLECTION_PKGPROTECT"}, justification = "This collection is not exposed.")
        private static final Map<Class<?>, TypeDescription> TYPE_CACHE = new HashMap();
        private final Class<?> type;
        private transient /* synthetic */ FieldList declaredFields;
        private transient /* synthetic */ MethodList declaredMethods;
        private transient /* synthetic */ AnnotationList declaredAnnotations;

        static {
            TYPE_CACHE.put(TargetType.class, new ForLoadedType(TargetType.class));
            TYPE_CACHE.put(Object.class, new ForLoadedType(Object.class));
            TYPE_CACHE.put(String.class, new ForLoadedType(String.class));
            TYPE_CACHE.put(Boolean.class, new ForLoadedType(Boolean.class));
            TYPE_CACHE.put(Byte.class, new ForLoadedType(Byte.class));
            TYPE_CACHE.put(Short.class, new ForLoadedType(Short.class));
            TYPE_CACHE.put(Character.class, new ForLoadedType(Character.class));
            TYPE_CACHE.put(Integer.class, new ForLoadedType(Integer.class));
            TYPE_CACHE.put(Long.class, new ForLoadedType(Long.class));
            TYPE_CACHE.put(Float.class, new ForLoadedType(Float.class));
            TYPE_CACHE.put(Double.class, new ForLoadedType(Double.class));
            TYPE_CACHE.put(Void.TYPE, new ForLoadedType(Void.TYPE));
            TYPE_CACHE.put(Boolean.TYPE, new ForLoadedType(Boolean.TYPE));
            TYPE_CACHE.put(Byte.TYPE, new ForLoadedType(Byte.TYPE));
            TYPE_CACHE.put(Short.TYPE, new ForLoadedType(Short.TYPE));
            TYPE_CACHE.put(Character.TYPE, new ForLoadedType(Character.TYPE));
            TYPE_CACHE.put(Integer.TYPE, new ForLoadedType(Integer.TYPE));
            TYPE_CACHE.put(Long.TYPE, new ForLoadedType(Long.TYPE));
            TYPE_CACHE.put(Float.TYPE, new ForLoadedType(Float.TYPE));
            TYPE_CACHE.put(Double.TYPE, new ForLoadedType(Double.TYPE));
        }

        public ForLoadedType(Class<?> type) {
            this.type = type;
        }

        public static String getName(Class<?> type) {
            String name = type.getName();
            int anonymousLoaderIndex = name.indexOf(47);
            return anonymousLoaderIndex == -1 ? name : name.substring(0, anonymousLoaderIndex);
        }

        public static TypeDescription of(Class<?> type) {
            TypeDescription typeDescription = TYPE_CACHE.get(type);
            return typeDescription == null ? new ForLoadedType(type) : typeDescription;
        }

        @Override // net.bytebuddy.description.type.TypeDescription.AbstractBase, net.bytebuddy.description.type.TypeDescription
        public boolean isAssignableFrom(Class<?> type) {
            return this.type.isAssignableFrom(type) || super.isAssignableFrom(type);
        }

        @Override // net.bytebuddy.description.type.TypeDescription.AbstractBase, net.bytebuddy.description.type.TypeDescription
        public boolean isAssignableFrom(TypeDescription typeDescription) {
            return ((typeDescription instanceof ForLoadedType) && this.type.isAssignableFrom(((ForLoadedType) typeDescription).type)) || super.isAssignableFrom(typeDescription);
        }

        @Override // net.bytebuddy.description.type.TypeDescription.AbstractBase, net.bytebuddy.description.type.TypeDescription
        public boolean isAssignableTo(Class<?> type) {
            return type.isAssignableFrom(this.type) || super.isAssignableTo(type);
        }

        @Override // net.bytebuddy.description.type.TypeDescription.AbstractBase, net.bytebuddy.description.type.TypeDescription
        public boolean isAssignableTo(TypeDescription typeDescription) {
            return ((typeDescription instanceof ForLoadedType) && ((ForLoadedType) typeDescription).type.isAssignableFrom(this.type)) || super.isAssignableTo(typeDescription);
        }

        @Override // net.bytebuddy.description.type.TypeDescription.AbstractBase, net.bytebuddy.description.type.TypeDescription
        public boolean isInHierarchyWith(Class<?> type) {
            return type.isAssignableFrom(this.type) || this.type.isAssignableFrom(type) || super.isInHierarchyWith(type);
        }

        @Override // net.bytebuddy.description.type.TypeDescription.AbstractBase, net.bytebuddy.description.type.TypeDescription
        public boolean isInHierarchyWith(TypeDescription typeDescription) {
            return ((typeDescription instanceof ForLoadedType) && (((ForLoadedType) typeDescription).type.isAssignableFrom(this.type) || this.type.isAssignableFrom(((ForLoadedType) typeDescription).type))) || super.isInHierarchyWith(typeDescription);
        }

        @Override // net.bytebuddy.description.type.TypeDescription.AbstractBase, net.bytebuddy.description.type.TypeDefinition
        public boolean represents(Type type) {
            return type == this.type || super.represents(type);
        }

        @Override // net.bytebuddy.description.type.TypeDefinition
        public TypeDescription getComponentType() {
            Class<?> componentType = this.type.getComponentType();
            return componentType == null ? TypeDescription.UNDEFINED : of(componentType);
        }

        @Override // net.bytebuddy.description.type.TypeDefinition
        public boolean isArray() {
            return this.type.isArray();
        }

        @Override // net.bytebuddy.description.type.TypeDefinition
        public boolean isPrimitive() {
            return this.type.isPrimitive();
        }

        @Override // net.bytebuddy.description.ModifierReviewable.AbstractBase, net.bytebuddy.description.ModifierReviewable.ForTypeDefinition
        public boolean isAnnotation() {
            return this.type.isAnnotation();
        }

        @Override // net.bytebuddy.description.type.TypeDefinition
        public Generic getSuperClass() {
            return RAW_TYPES ? this.type.getSuperclass() == null ? Generic.UNDEFINED : Generic.OfNonGenericType.ForLoadedType.of(this.type.getSuperclass()) : this.type.getSuperclass() == null ? Generic.UNDEFINED : new Generic.LazyProjection.ForLoadedSuperClass(this.type);
        }

        @Override // net.bytebuddy.description.type.TypeDefinition
        public TypeList.Generic getInterfaces() {
            return RAW_TYPES ? isArray() ? ARRAY_INTERFACES : new TypeList.Generic.ForLoadedTypes(this.type.getInterfaces()) : isArray() ? ARRAY_INTERFACES : new TypeList.Generic.OfLoadedInterfaceTypes(this.type);
        }

        @Override // net.bytebuddy.description.DeclaredByType, net.bytebuddy.description.field.FieldDescription.InDefinedShape
        public TypeDescription getDeclaringType() {
            Class<?> declaringType = this.type.getDeclaringClass();
            return declaringType == null ? TypeDescription.UNDEFINED : of(declaringType);
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public MethodDescription.InDefinedShape getEnclosingMethod() {
            Method enclosingMethod = this.type.getEnclosingMethod();
            Constructor<?> enclosingConstructor = this.type.getEnclosingConstructor();
            if (enclosingMethod != null) {
                return new MethodDescription.ForLoadedMethod(enclosingMethod);
            }
            if (enclosingConstructor != null) {
                return new MethodDescription.ForLoadedConstructor(enclosingConstructor);
            }
            return MethodDescription.UNDEFINED;
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public TypeDescription getEnclosingType() {
            Class<?> enclosingType = this.type.getEnclosingClass();
            return enclosingType == null ? TypeDescription.UNDEFINED : of(enclosingType);
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public TypeList getDeclaredTypes() {
            return new TypeList.ForLoadedTypes(this.type.getDeclaredClasses());
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public String getSimpleName() {
            String simpleName = this.type.getSimpleName();
            int anonymousLoaderIndex = simpleName.indexOf(47);
            if (anonymousLoaderIndex == -1) {
                return simpleName;
            }
            StringBuilder normalized = new StringBuilder(simpleName.substring(0, anonymousLoaderIndex));
            Class<?> cls = this.type;
            while (true) {
                Class<?> type = cls;
                if (type.isArray()) {
                    normalized.append("[]");
                    cls = type.getComponentType();
                } else {
                    return normalized.toString();
                }
            }
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public boolean isAnonymousType() {
            return this.type.isAnonymousClass();
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public boolean isLocalType() {
            return this.type.isLocalClass();
        }

        @Override // net.bytebuddy.description.type.TypeDescription.AbstractBase, net.bytebuddy.description.type.TypeDescription
        public boolean isMemberType() {
            return this.type.isMemberClass();
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v12, types: [net.bytebuddy.description.field.FieldList] */
        @Override // net.bytebuddy.description.type.TypeDescription, net.bytebuddy.description.type.TypeDefinition
        @CachedReturnPlugin.Enhance("declaredFields")
        public FieldList<FieldDescription.InDefinedShape> getDeclaredFields() {
            FieldList.ForLoadedFields forLoadedFields = this.declaredFields != null ? null : new FieldList.ForLoadedFields(this.type.getDeclaredFields());
            if (forLoadedFields == null) {
                forLoadedFields = this.declaredFields;
            } else {
                this.declaredFields = forLoadedFields;
            }
            return forLoadedFields;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v12, types: [net.bytebuddy.description.method.MethodList] */
        @Override // net.bytebuddy.description.type.TypeDescription, net.bytebuddy.description.type.TypeDefinition
        @CachedReturnPlugin.Enhance("declaredMethods")
        public MethodList<MethodDescription.InDefinedShape> getDeclaredMethods() {
            MethodList.ForLoadedMethods forLoadedMethods = this.declaredMethods != null ? null : new MethodList.ForLoadedMethods(this.type);
            if (forLoadedMethods == null) {
                forLoadedMethods = this.declaredMethods;
            } else {
                this.declaredMethods = forLoadedMethods;
            }
            return forLoadedMethods;
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public PackageDescription getPackage() {
            if (this.type.isArray() || this.type.isPrimitive()) {
                return PackageDescription.UNDEFINED;
            }
            Package aPackage = this.type.getPackage();
            if (aPackage == null) {
                String name = this.type.getName();
                int index = name.lastIndexOf(46);
                return index == -1 ? new PackageDescription.Simple("") : new PackageDescription.Simple(name.substring(0, index));
            }
            return new PackageDescription.ForLoadedPackage(aPackage);
        }

        @Override // net.bytebuddy.description.type.TypeDefinition
        public StackSize getStackSize() {
            return StackSize.of(this.type);
        }

        @Override // net.bytebuddy.description.NamedElement.WithRuntimeName
        public String getName() {
            return getName(this.type);
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public String getCanonicalName() {
            String canonicalName = this.type.getCanonicalName();
            if (canonicalName == null) {
                return NO_NAME;
            }
            int anonymousLoaderIndex = canonicalName.indexOf(47);
            if (anonymousLoaderIndex == -1) {
                return canonicalName;
            }
            StringBuilder normalized = new StringBuilder(canonicalName.substring(0, anonymousLoaderIndex));
            Class<?> cls = this.type;
            while (true) {
                Class<?> type = cls;
                if (type.isArray()) {
                    normalized.append("[]");
                    cls = type.getComponentType();
                } else {
                    return normalized.toString();
                }
            }
        }

        @Override // net.bytebuddy.description.NamedElement.WithDescriptor
        public String getDescriptor() {
            String name = this.type.getName();
            int anonymousLoaderIndex = name.indexOf(47);
            if (anonymousLoaderIndex == -1) {
                return net.bytebuddy.jar.asm.Type.getDescriptor(this.type);
            }
            return "L" + name.substring(0, anonymousLoaderIndex).replace('.', '/') + ";";
        }

        @Override // net.bytebuddy.description.ModifierReviewable
        public int getModifiers() {
            return this.type.getModifiers();
        }

        @Override // net.bytebuddy.description.TypeVariableSource
        public TypeList.Generic getTypeVariables() {
            if (RAW_TYPES) {
                return new TypeList.Generic.Empty();
            }
            return TypeList.Generic.ForLoadedTypes.OfTypeVariables.of((GenericDeclaration) this.type);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v12, types: [net.bytebuddy.description.annotation.AnnotationList] */
        @Override // net.bytebuddy.description.annotation.AnnotationSource
        @CachedReturnPlugin.Enhance("declaredAnnotations")
        public AnnotationList getDeclaredAnnotations() {
            AnnotationList.ForLoadedAnnotations forLoadedAnnotations = this.declaredAnnotations != null ? null : new AnnotationList.ForLoadedAnnotations(this.type.getDeclaredAnnotations());
            if (forLoadedAnnotations == null) {
                forLoadedAnnotations = this.declaredAnnotations;
            } else {
                this.declaredAnnotations = forLoadedAnnotations;
            }
            return forLoadedAnnotations;
        }

        @Override // net.bytebuddy.description.type.TypeDescription.AbstractBase, net.bytebuddy.description.type.TypeDefinition
        public Generic asGenericType() {
            return Generic.OfNonGenericType.ForLoadedType.of(this.type);
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public TypeDescription getNestHost() {
            return of(DISPATCHER.getNestHost(this.type));
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public TypeList getNestMembers() {
            return new TypeList.ForLoadedTypes(DISPATCHER.getNestMembers(this.type));
        }

        @Override // net.bytebuddy.description.type.TypeDescription.AbstractBase, net.bytebuddy.description.type.TypeDescription
        public boolean isNestHost() {
            return DISPATCHER.getNestHost(this.type) == this.type;
        }

        @Override // net.bytebuddy.description.type.TypeDescription.AbstractBase, net.bytebuddy.description.type.TypeDescription
        public boolean isNestMateOf(Class<?> type) {
            return DISPATCHER.isNestmateOf(this.type, type) || super.isNestMateOf(of(type));
        }

        @Override // net.bytebuddy.description.type.TypeDescription.AbstractBase, net.bytebuddy.description.type.TypeDescription
        public boolean isNestMateOf(TypeDescription typeDescription) {
            return ((typeDescription instanceof ForLoadedType) && DISPATCHER.isNestmateOf(this.type, ((ForLoadedType) typeDescription).type)) || super.isNestMateOf(typeDescription);
        }

        @Override // net.bytebuddy.description.type.TypeDescription, net.bytebuddy.description.type.TypeDefinition
        public RecordComponentList<RecordComponentDescription.InDefinedShape> getRecordComponents() {
            Object[] recordComponent = RecordComponentDescription.ForLoadedRecordComponent.DISPATCHER.getRecordComponents(this.type);
            return recordComponent == null ? new RecordComponentList.Empty<>() : new RecordComponentList.ForLoadedRecordComponents(new Object[0]);
        }

        @Override // net.bytebuddy.description.type.TypeDefinition
        public boolean isRecord() {
            return RecordComponentDescription.ForLoadedRecordComponent.DISPATCHER.isRecord(this.type);
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public TypeList getPermittedSubclasses() {
            return new ClassDescriptionTypeList(this.type.getClassLoader(), DISPATCHER.getPermittedSubclasses(this.type));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$ForLoadedType$Dispatcher.class */
        public interface Dispatcher {
            Class<?> getNestHost(Class<?> cls);

            Class<?>[] getNestMembers(Class<?> cls);

            boolean isNestmateOf(Class<?> cls, Class<?> cls2);

            Object[] getPermittedSubclasses(Class<?> cls);

            String getInternalName(Object obj);

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$ForLoadedType$Dispatcher$CreationAction.class */
            public enum CreationAction implements PrivilegedAction<Dispatcher> {
                INSTANCE;

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                @SuppressFBWarnings(value = {"REC_CATCH_EXCEPTION"}, justification = "Exception should not be rethrown but trigger a fallback")
                public Dispatcher run() {
                    try {
                        return new ForJava16CapableVm(Class.class.getMethod("getNestHost", new Class[0]), Class.class.getMethod("getNestMembers", new Class[0]), Class.class.getMethod("isNestmateOf", Class.class), Class.class.getMethod("permittedSubclasses", new Class[0]), Class.forName("java.lang.constant.ClassDesc").getMethod("descriptorString", new Class[0]));
                    } catch (Exception e) {
                        try {
                            return new ForJava11CapableVm(Class.class.getMethod("getNestHost", new Class[0]), Class.class.getMethod("getNestMembers", new Class[0]), Class.class.getMethod("isNestmateOf", Class.class));
                        } catch (NoSuchMethodException e2) {
                            return ForLegacyVm.INSTANCE;
                        }
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$ForLoadedType$Dispatcher$ForLegacyVm.class */
            public enum ForLegacyVm implements Dispatcher {
                INSTANCE;

                @Override // net.bytebuddy.description.type.TypeDescription.ForLoadedType.Dispatcher
                public Class<?> getNestHost(Class<?> type) {
                    return type;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.ForLoadedType.Dispatcher
                public Class<?>[] getNestMembers(Class<?> type) {
                    return new Class[]{type};
                }

                @Override // net.bytebuddy.description.type.TypeDescription.ForLoadedType.Dispatcher
                public boolean isNestmateOf(Class<?> type, Class<?> candidate) {
                    return type == candidate;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.ForLoadedType.Dispatcher
                public Object[] getPermittedSubclasses(Class<?> type) {
                    return new Object[0];
                }

                @Override // net.bytebuddy.description.type.TypeDescription.ForLoadedType.Dispatcher
                public String getInternalName(Object permittedSubclass) {
                    throw new IllegalStateException("Not supported on the current VM");
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$ForLoadedType$Dispatcher$ForJava11CapableVm.class */
            public static class ForJava11CapableVm implements Dispatcher {
                private final Method getNestHost;
                private final Method getNestMembers;
                private final Method isNestmateOf;

                protected ForJava11CapableVm(Method getNestHost, Method getNestMembers, Method isNestmateOf) {
                    this.getNestHost = getNestHost;
                    this.getNestMembers = getNestMembers;
                    this.isNestmateOf = isNestmateOf;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.ForLoadedType.Dispatcher
                public Class<?> getNestHost(Class<?> type) {
                    try {
                        return (Class) this.getNestHost.invoke(type, new Object[0]);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Could not access Class::getNestHost", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Could not invoke Class::getNestHost", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.description.type.TypeDescription.ForLoadedType.Dispatcher
                public Class<?>[] getNestMembers(Class<?> type) {
                    try {
                        return (Class[]) this.getNestMembers.invoke(type, new Object[0]);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Could not access Class::getNestMembers", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Could not invoke Class::getNestMembers", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.description.type.TypeDescription.ForLoadedType.Dispatcher
                public boolean isNestmateOf(Class<?> type, Class<?> candidate) {
                    try {
                        return ((Boolean) this.isNestmateOf.invoke(type, candidate)).booleanValue();
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Could not access Class::isNestmateOf", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Could not invoke Class::isNestmateOf", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.description.type.TypeDescription.ForLoadedType.Dispatcher
                public Object[] getPermittedSubclasses(Class<?> type) {
                    return new Object[0];
                }

                @Override // net.bytebuddy.description.type.TypeDescription.ForLoadedType.Dispatcher
                public String getInternalName(Object permittedSubclass) {
                    throw new IllegalStateException("Not supported on the current VM");
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$ForLoadedType$Dispatcher$ForJava16CapableVm.class */
            public static class ForJava16CapableVm implements Dispatcher {
                private final Method getNestHost;
                private final Method getNestMembers;
                private final Method isNestmateOf;
                private final Method permittedSubclasses;
                private final Method descriptorString;

                protected ForJava16CapableVm(Method getNestHost, Method getNestMembers, Method isNestmateOf, Method permittedSubclasses, Method descriptorString) {
                    this.getNestHost = getNestHost;
                    this.getNestMembers = getNestMembers;
                    this.isNestmateOf = isNestmateOf;
                    this.permittedSubclasses = permittedSubclasses;
                    this.descriptorString = descriptorString;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.ForLoadedType.Dispatcher
                public Class<?> getNestHost(Class<?> type) {
                    try {
                        return (Class) this.getNestHost.invoke(type, new Object[0]);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Could not access Class::getNestHost", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Could not invoke Class::getNestHost", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.description.type.TypeDescription.ForLoadedType.Dispatcher
                public Class<?>[] getNestMembers(Class<?> type) {
                    try {
                        return (Class[]) this.getNestMembers.invoke(type, new Object[0]);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Could not access Class::getNestMembers", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Could not invoke Class::getNestMembers", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.description.type.TypeDescription.ForLoadedType.Dispatcher
                public boolean isNestmateOf(Class<?> type, Class<?> candidate) {
                    try {
                        return ((Boolean) this.isNestmateOf.invoke(type, candidate)).booleanValue();
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Could not access Class::isNestmateOf", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Could not invoke Class::isNestmateOf", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.description.type.TypeDescription.ForLoadedType.Dispatcher
                public Object[] getPermittedSubclasses(Class<?> type) {
                    try {
                        return (Object[]) this.permittedSubclasses.invoke(type, new Object[0]);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Could not access Class::permittedSubclasses", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Could not invoke Class::permittedSubclasses", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.description.type.TypeDescription.ForLoadedType.Dispatcher
                public String getInternalName(Object permittedSubclass) {
                    try {
                        return (String) this.descriptorString.invoke(permittedSubclass, new Object[0]);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Could not access ClassDesc::descriptorString", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Could not invoke ClassDesc::descriptorString", exception2.getCause());
                    }
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$ForLoadedType$ClassDescriptionTypeList.class */
        protected static class ClassDescriptionTypeList extends TypeList.AbstractBase {
            private final ClassLoader classLoader;
            private final Object[] permittedSubclass;

            protected ClassDescriptionTypeList(ClassLoader classLoader, Object[] permittedSubclass) {
                this.classLoader = classLoader;
                this.permittedSubclass = permittedSubclass;
            }

            @Override // java.util.AbstractList, java.util.List
            public TypeDescription get(int index) {
                return new InternalNameLazyType(this.classLoader, ForLoadedType.DISPATCHER.getInternalName(this.permittedSubclass[index]));
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return this.permittedSubclass.length;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$ForLoadedType$ClassDescriptionTypeList$InternalNameLazyType.class */
            public static class InternalNameLazyType extends AbstractBase.OfSimpleType.WithDelegation {
                private final ClassLoader classLoader;
                private final String internalName;
                private transient /* synthetic */ TypeDescription delegate_D9CNVRug;

                protected InternalNameLazyType(ClassLoader classLoader, String internalName) {
                    this.classLoader = classLoader;
                    this.internalName = internalName;
                }

                @Override // net.bytebuddy.description.type.TypeDescription.AbstractBase.OfSimpleType.WithDelegation
                @CachedReturnPlugin.Enhance
                protected TypeDescription delegate() {
                    TypeDescription of;
                    if (this.delegate_D9CNVRug != null) {
                        of = null;
                    } else {
                        try {
                            of = ForLoadedType.of(Class.forName(getName(), false, this.classLoader));
                        } catch (ClassNotFoundException exception) {
                            throw new IllegalStateException(exception);
                        }
                    }
                    TypeDescription typeDescription = of;
                    if (typeDescription == null) {
                        typeDescription = this.delegate_D9CNVRug;
                    } else {
                        this.delegate_D9CNVRug = typeDescription;
                    }
                    return typeDescription;
                }

                @Override // net.bytebuddy.description.NamedElement.WithRuntimeName
                public String getName() {
                    return this.internalName.replace('/', '.');
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$ArrayProjection.class */
    public static class ArrayProjection extends AbstractBase {
        private static final int ARRAY_IMPLIED = 1040;
        private static final int ARRAY_EXCLUDED = 8712;
        private final TypeDescription componentType;
        private final int arity;

        protected ArrayProjection(TypeDescription componentType, int arity) {
            this.componentType = componentType;
            this.arity = arity;
        }

        public static TypeDescription of(TypeDescription componentType) {
            return of(componentType, 1);
        }

        public static TypeDescription of(TypeDescription componentType, int arity) {
            if (arity < 0) {
                throw new IllegalArgumentException("Arrays cannot have a negative arity");
            }
            while (componentType.isArray()) {
                componentType = componentType.getComponentType();
                arity++;
            }
            return arity == 0 ? componentType : new ArrayProjection(componentType, arity);
        }

        @Override // net.bytebuddy.description.type.TypeDefinition
        public boolean isArray() {
            return true;
        }

        @Override // net.bytebuddy.description.type.TypeDefinition
        public TypeDescription getComponentType() {
            return this.arity == 1 ? this.componentType : new ArrayProjection(this.componentType, this.arity - 1);
        }

        @Override // net.bytebuddy.description.type.TypeDefinition
        public boolean isPrimitive() {
            return false;
        }

        @Override // net.bytebuddy.description.type.TypeDefinition
        public Generic getSuperClass() {
            return Generic.OBJECT;
        }

        @Override // net.bytebuddy.description.type.TypeDefinition
        public TypeList.Generic getInterfaces() {
            return ARRAY_INTERFACES;
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public MethodDescription.InDefinedShape getEnclosingMethod() {
            return MethodDescription.UNDEFINED;
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public TypeDescription getEnclosingType() {
            return TypeDescription.UNDEFINED;
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public TypeList getDeclaredTypes() {
            return new TypeList.Empty();
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public String getSimpleName() {
            StringBuilder stringBuilder = new StringBuilder(this.componentType.getSimpleName());
            for (int i = 0; i < this.arity; i++) {
                stringBuilder.append("[]");
            }
            return stringBuilder.toString();
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public String getCanonicalName() {
            String canonicalName = this.componentType.getCanonicalName();
            if (canonicalName == null) {
                return NO_NAME;
            }
            StringBuilder stringBuilder = new StringBuilder(canonicalName);
            for (int i = 0; i < this.arity; i++) {
                stringBuilder.append("[]");
            }
            return stringBuilder.toString();
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public boolean isAnonymousType() {
            return false;
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public boolean isLocalType() {
            return false;
        }

        @Override // net.bytebuddy.description.type.TypeDescription.AbstractBase, net.bytebuddy.description.type.TypeDescription
        public boolean isMemberType() {
            return false;
        }

        @Override // net.bytebuddy.description.type.TypeDescription, net.bytebuddy.description.type.TypeDefinition
        public FieldList<FieldDescription.InDefinedShape> getDeclaredFields() {
            return new FieldList.Empty();
        }

        @Override // net.bytebuddy.description.type.TypeDescription, net.bytebuddy.description.type.TypeDefinition
        public MethodList<MethodDescription.InDefinedShape> getDeclaredMethods() {
            return new MethodList.Empty();
        }

        @Override // net.bytebuddy.description.type.TypeDefinition
        public StackSize getStackSize() {
            return StackSize.SINGLE;
        }

        @Override // net.bytebuddy.description.annotation.AnnotationSource
        public AnnotationList getDeclaredAnnotations() {
            return new AnnotationList.Empty();
        }

        @Override // net.bytebuddy.description.type.TypeDescription.AbstractBase, net.bytebuddy.description.type.TypeDescription
        public AnnotationList getInheritedAnnotations() {
            return new AnnotationList.Empty();
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public PackageDescription getPackage() {
            return PackageDescription.UNDEFINED;
        }

        @Override // net.bytebuddy.description.NamedElement.WithRuntimeName
        public String getName() {
            String descriptor = this.componentType.getDescriptor();
            StringBuilder stringBuilder = new StringBuilder(descriptor.length() + this.arity);
            for (int index = 0; index < this.arity; index++) {
                stringBuilder.append('[');
            }
            for (int index2 = 0; index2 < descriptor.length(); index2++) {
                char character = descriptor.charAt(index2);
                stringBuilder.append(character == '/' ? '.' : character);
            }
            return stringBuilder.toString();
        }

        @Override // net.bytebuddy.description.NamedElement.WithDescriptor
        public String getDescriptor() {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < this.arity; i++) {
                stringBuilder.append('[');
            }
            return stringBuilder.append(this.componentType.getDescriptor()).toString();
        }

        @Override // net.bytebuddy.description.DeclaredByType, net.bytebuddy.description.field.FieldDescription.InDefinedShape
        public TypeDescription getDeclaringType() {
            return TypeDescription.UNDEFINED;
        }

        @Override // net.bytebuddy.description.ModifierReviewable
        public int getModifiers() {
            return (getComponentType().getModifiers() & (-8713)) | 1040;
        }

        @Override // net.bytebuddy.description.TypeVariableSource
        public TypeList.Generic getTypeVariables() {
            return new TypeList.Generic.Empty();
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public TypeDescription getNestHost() {
            return this;
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public TypeList getNestMembers() {
            return new TypeList.Explicit(this);
        }

        @Override // net.bytebuddy.description.type.TypeDescription, net.bytebuddy.description.type.TypeDefinition
        public RecordComponentList<RecordComponentDescription.InDefinedShape> getRecordComponents() {
            return new RecordComponentList.Empty();
        }

        @Override // net.bytebuddy.description.type.TypeDefinition
        public boolean isRecord() {
            return false;
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public TypeList getPermittedSubclasses() {
            return new TypeList.Empty();
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$Latent.class */
    public static class Latent extends AbstractBase.OfSimpleType {
        private final String name;
        private final int modifiers;
        private final Generic superClass;
        private final List<? extends Generic> interfaces;

        public Latent(String name, int modifiers, Generic superClass, Generic... anInterface) {
            this(name, modifiers, superClass, Arrays.asList(anInterface));
        }

        public Latent(String name, int modifiers, Generic superClass, List<? extends Generic> interfaces) {
            this.name = name;
            this.modifiers = modifiers;
            this.superClass = superClass;
            this.interfaces = interfaces;
        }

        @Override // net.bytebuddy.description.type.TypeDefinition
        public Generic getSuperClass() {
            return this.superClass;
        }

        @Override // net.bytebuddy.description.type.TypeDefinition
        public TypeList.Generic getInterfaces() {
            return new TypeList.Generic.Explicit(this.interfaces);
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public MethodDescription.InDefinedShape getEnclosingMethod() {
            throw new IllegalStateException("Cannot resolve enclosing method of a latent type description: " + this);
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public TypeDescription getEnclosingType() {
            throw new IllegalStateException("Cannot resolve enclosing type of a latent type description: " + this);
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public TypeList getDeclaredTypes() {
            throw new IllegalStateException("Cannot resolve inner types of a latent type description: " + this);
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public boolean isAnonymousType() {
            throw new IllegalStateException("Cannot resolve anonymous type property of a latent type description: " + this);
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public boolean isLocalType() {
            throw new IllegalStateException("Cannot resolve local class property of a latent type description: " + this);
        }

        @Override // net.bytebuddy.description.type.TypeDescription, net.bytebuddy.description.type.TypeDefinition
        public FieldList<FieldDescription.InDefinedShape> getDeclaredFields() {
            throw new IllegalStateException("Cannot resolve declared fields of a latent type description: " + this);
        }

        @Override // net.bytebuddy.description.type.TypeDescription, net.bytebuddy.description.type.TypeDefinition
        public MethodList<MethodDescription.InDefinedShape> getDeclaredMethods() {
            throw new IllegalStateException("Cannot resolve declared methods of a latent type description: " + this);
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public PackageDescription getPackage() {
            String name = getName();
            int index = name.lastIndexOf(46);
            return new PackageDescription.Simple(index == -1 ? "" : name.substring(0, index));
        }

        @Override // net.bytebuddy.description.annotation.AnnotationSource
        public AnnotationList getDeclaredAnnotations() {
            throw new IllegalStateException("Cannot resolve declared annotations of a latent type description: " + this);
        }

        @Override // net.bytebuddy.description.DeclaredByType, net.bytebuddy.description.field.FieldDescription.InDefinedShape
        public TypeDescription getDeclaringType() {
            throw new IllegalStateException("Cannot resolve declared type of a latent type description: " + this);
        }

        @Override // net.bytebuddy.description.ModifierReviewable
        public int getModifiers() {
            return this.modifiers;
        }

        @Override // net.bytebuddy.description.NamedElement.WithRuntimeName
        public String getName() {
            return this.name;
        }

        @Override // net.bytebuddy.description.TypeVariableSource
        public TypeList.Generic getTypeVariables() {
            throw new IllegalStateException("Cannot resolve type variables of a latent type description: " + this);
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public TypeDescription getNestHost() {
            throw new IllegalStateException("Cannot resolve nest host of a latent type description: " + this);
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public TypeList getNestMembers() {
            throw new IllegalStateException("Cannot resolve nest mates of a latent type description: " + this);
        }

        @Override // net.bytebuddy.description.type.TypeDescription, net.bytebuddy.description.type.TypeDefinition
        public RecordComponentList<RecordComponentDescription.InDefinedShape> getRecordComponents() {
            throw new IllegalStateException("Cannot resolve record components of a latent type description: " + this);
        }

        @Override // net.bytebuddy.description.type.TypeDefinition
        public boolean isRecord() {
            throw new IllegalStateException("Cannot resolve record attribute of a latent type description: " + this);
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public TypeList getPermittedSubclasses() {
            throw new IllegalStateException("Cannot resolve permitted subclasses of a latent type description: " + this);
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$ForPackageDescription.class */
    public static class ForPackageDescription extends AbstractBase.OfSimpleType {
        private final PackageDescription packageDescription;

        public ForPackageDescription(PackageDescription packageDescription) {
            this.packageDescription = packageDescription;
        }

        @Override // net.bytebuddy.description.type.TypeDefinition
        public Generic getSuperClass() {
            return Generic.OBJECT;
        }

        @Override // net.bytebuddy.description.type.TypeDefinition
        public TypeList.Generic getInterfaces() {
            return new TypeList.Generic.Empty();
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public MethodDescription.InDefinedShape getEnclosingMethod() {
            return MethodDescription.UNDEFINED;
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public TypeDescription getEnclosingType() {
            return TypeDescription.UNDEFINED;
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public boolean isAnonymousType() {
            return false;
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public boolean isLocalType() {
            return false;
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public TypeList getDeclaredTypes() {
            return new TypeList.Empty();
        }

        @Override // net.bytebuddy.description.type.TypeDescription, net.bytebuddy.description.type.TypeDefinition
        public FieldList<FieldDescription.InDefinedShape> getDeclaredFields() {
            return new FieldList.Empty();
        }

        @Override // net.bytebuddy.description.type.TypeDescription, net.bytebuddy.description.type.TypeDefinition
        public MethodList<MethodDescription.InDefinedShape> getDeclaredMethods() {
            return new MethodList.Empty();
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public PackageDescription getPackage() {
            return this.packageDescription;
        }

        @Override // net.bytebuddy.description.annotation.AnnotationSource
        public AnnotationList getDeclaredAnnotations() {
            return this.packageDescription.getDeclaredAnnotations();
        }

        @Override // net.bytebuddy.description.DeclaredByType, net.bytebuddy.description.field.FieldDescription.InDefinedShape
        public TypeDescription getDeclaringType() {
            return TypeDescription.UNDEFINED;
        }

        @Override // net.bytebuddy.description.TypeVariableSource
        public TypeList.Generic getTypeVariables() {
            return new TypeList.Generic.Empty();
        }

        @Override // net.bytebuddy.description.ModifierReviewable
        public int getModifiers() {
            return 5632;
        }

        @Override // net.bytebuddy.description.NamedElement.WithRuntimeName
        public String getName() {
            return this.packageDescription.getName() + "." + PackageDescription.PACKAGE_CLASS_NAME;
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public TypeDescription getNestHost() {
            return this;
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public TypeList getNestMembers() {
            return new TypeList.Explicit(this);
        }

        @Override // net.bytebuddy.description.type.TypeDescription, net.bytebuddy.description.type.TypeDefinition
        public RecordComponentList<RecordComponentDescription.InDefinedShape> getRecordComponents() {
            return new RecordComponentList.Empty();
        }

        @Override // net.bytebuddy.description.type.TypeDefinition
        public boolean isRecord() {
            return false;
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public TypeList getPermittedSubclasses() {
            return new TypeList.Empty();
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$SuperTypeLoading.class */
    public static class SuperTypeLoading extends AbstractBase {
        private final TypeDescription delegate;
        private final ClassLoader classLoader;
        private final ClassLoadingDelegate classLoadingDelegate;

        public SuperTypeLoading(TypeDescription delegate, ClassLoader classLoader) {
            this(delegate, classLoader, ClassLoadingDelegate.Simple.INSTANCE);
        }

        public SuperTypeLoading(TypeDescription delegate, ClassLoader classLoader, ClassLoadingDelegate classLoadingDelegate) {
            this.delegate = delegate;
            this.classLoader = classLoader;
            this.classLoadingDelegate = classLoadingDelegate;
        }

        @Override // net.bytebuddy.description.annotation.AnnotationSource
        public AnnotationList getDeclaredAnnotations() {
            return this.delegate.getDeclaredAnnotations();
        }

        @Override // net.bytebuddy.description.ModifierReviewable
        public int getModifiers() {
            return this.delegate.getModifiers();
        }

        @Override // net.bytebuddy.description.TypeVariableSource
        public TypeList.Generic getTypeVariables() {
            return this.delegate.getTypeVariables();
        }

        @Override // net.bytebuddy.description.NamedElement.WithDescriptor
        public String getDescriptor() {
            return this.delegate.getDescriptor();
        }

        @Override // net.bytebuddy.description.NamedElement.WithRuntimeName
        public String getName() {
            return this.delegate.getName();
        }

        @Override // net.bytebuddy.description.type.TypeDefinition
        public Generic getSuperClass() {
            Generic superClass = this.delegate.getSuperClass();
            return superClass == null ? Generic.UNDEFINED : new ClassLoadingTypeProjection(superClass, this.classLoader, this.classLoadingDelegate);
        }

        @Override // net.bytebuddy.description.type.TypeDefinition
        public TypeList.Generic getInterfaces() {
            return new ClassLoadingTypeList(this.delegate.getInterfaces(), this.classLoader, this.classLoadingDelegate);
        }

        @Override // net.bytebuddy.description.type.TypeDescription, net.bytebuddy.description.type.TypeDefinition
        public FieldList<FieldDescription.InDefinedShape> getDeclaredFields() {
            return this.delegate.getDeclaredFields();
        }

        @Override // net.bytebuddy.description.type.TypeDescription, net.bytebuddy.description.type.TypeDefinition
        public MethodList<MethodDescription.InDefinedShape> getDeclaredMethods() {
            return this.delegate.getDeclaredMethods();
        }

        @Override // net.bytebuddy.description.type.TypeDefinition
        public StackSize getStackSize() {
            return this.delegate.getStackSize();
        }

        @Override // net.bytebuddy.description.type.TypeDefinition
        public boolean isArray() {
            return this.delegate.isArray();
        }

        @Override // net.bytebuddy.description.type.TypeDefinition
        public boolean isPrimitive() {
            return this.delegate.isPrimitive();
        }

        @Override // net.bytebuddy.description.type.TypeDefinition
        public TypeDescription getComponentType() {
            return this.delegate.getComponentType();
        }

        @Override // net.bytebuddy.description.DeclaredByType, net.bytebuddy.description.field.FieldDescription.InDefinedShape
        public TypeDescription getDeclaringType() {
            return this.delegate.getDeclaringType();
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public TypeList getDeclaredTypes() {
            return this.delegate.getDeclaredTypes();
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public MethodDescription.InDefinedShape getEnclosingMethod() {
            return this.delegate.getEnclosingMethod();
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public TypeDescription getEnclosingType() {
            return this.delegate.getEnclosingType();
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public String getSimpleName() {
            return this.delegate.getSimpleName();
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public String getCanonicalName() {
            return this.delegate.getCanonicalName();
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public boolean isAnonymousType() {
            return this.delegate.isAnonymousType();
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public boolean isLocalType() {
            return this.delegate.isLocalType();
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public PackageDescription getPackage() {
            return this.delegate.getPackage();
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public TypeDescription getNestHost() {
            return this.delegate.getNestHost();
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public TypeList getNestMembers() {
            return this.delegate.getNestMembers();
        }

        @Override // net.bytebuddy.description.type.TypeDescription, net.bytebuddy.description.type.TypeDefinition
        public RecordComponentList<RecordComponentDescription.InDefinedShape> getRecordComponents() {
            return this.delegate.getRecordComponents();
        }

        @Override // net.bytebuddy.description.type.TypeDefinition
        public boolean isRecord() {
            return this.delegate.isRecord();
        }

        @Override // net.bytebuddy.description.type.TypeDescription
        public TypeList getPermittedSubclasses() {
            return this.delegate.getPermittedSubclasses();
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$SuperTypeLoading$ClassLoadingDelegate.class */
        public interface ClassLoadingDelegate {
            Class<?> load(String str, ClassLoader classLoader) throws ClassNotFoundException;

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$SuperTypeLoading$ClassLoadingDelegate$Simple.class */
            public enum Simple implements ClassLoadingDelegate {
                INSTANCE;

                @Override // net.bytebuddy.description.type.TypeDescription.SuperTypeLoading.ClassLoadingDelegate
                public Class<?> load(String name, ClassLoader classLoader) throws ClassNotFoundException {
                    return Class.forName(name, false, classLoader);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$SuperTypeLoading$ClassLoadingTypeProjection.class */
        public static class ClassLoadingTypeProjection extends Generic.LazyProjection {
            private final Generic delegate;
            private final ClassLoader classLoader;
            private final ClassLoadingDelegate classLoadingDelegate;
            private transient /* synthetic */ TypeDescription erasure;
            private transient /* synthetic */ Generic superClass;
            private transient /* synthetic */ TypeList.Generic interfaces;

            protected ClassLoadingTypeProjection(Generic delegate, ClassLoader classLoader, ClassLoadingDelegate classLoadingDelegate) {
                this.delegate = delegate;
                this.classLoader = classLoader;
                this.classLoadingDelegate = classLoadingDelegate;
            }

            @Override // net.bytebuddy.description.annotation.AnnotationSource
            public AnnotationList getDeclaredAnnotations() {
                return this.delegate.getDeclaredAnnotations();
            }

            /* JADX WARN: Type inference failed for: r0v3, types: [net.bytebuddy.description.type.TypeDescription$SuperTypeLoading$ClassLoadingTypeProjection] */
            /* JADX WARN: Type inference failed for: r0v4 */
            /* JADX WARN: Type inference failed for: r0v8, types: [net.bytebuddy.description.type.TypeDescription] */
            @Override // net.bytebuddy.description.type.TypeDefinition
            @CachedReturnPlugin.Enhance("erasure")
            public TypeDescription asErasure() {
                TypeDescription typeDescription;
                if (this.erasure != null) {
                    typeDescription = null;
                } else {
                    ?? r0 = this;
                    try {
                        r0 = ForLoadedType.of(r0.classLoadingDelegate.load(r0.delegate.asErasure().getName(), r0.classLoader));
                        typeDescription = r0;
                    } catch (ClassNotFoundException e) {
                        r0.delegate.asErasure();
                        typeDescription = r0;
                    }
                }
                TypeDescription typeDescription2 = typeDescription;
                if (typeDescription2 == null) {
                    typeDescription2 = this.erasure;
                } else {
                    this.erasure = typeDescription2;
                }
                return typeDescription2;
            }

            @Override // net.bytebuddy.description.type.TypeDescription.Generic.LazyProjection
            protected Generic resolve() {
                return this.delegate;
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            @CachedReturnPlugin.Enhance("superClass")
            public Generic getSuperClass() {
                Generic generic;
                if (this.superClass != null) {
                    generic = null;
                } else {
                    Generic superClass = this.delegate.getSuperClass();
                    generic = superClass;
                    if (generic == null) {
                        generic = Generic.UNDEFINED;
                    } else {
                        try {
                            generic = new ClassLoadingTypeProjection(superClass, this.classLoadingDelegate.load(this.delegate.asErasure().getName(), this.classLoader).getClassLoader(), this.classLoadingDelegate);
                        } catch (ClassNotFoundException e) {
                        }
                    }
                }
                Generic generic2 = generic;
                if (generic2 == null) {
                    generic2 = this.superClass;
                } else {
                    this.superClass = generic2;
                }
                return generic2;
            }

            @Override // net.bytebuddy.description.type.TypeDefinition
            @CachedReturnPlugin.Enhance("interfaces")
            public TypeList.Generic getInterfaces() {
                TypeList.Generic interfaces;
                if (this.interfaces != null) {
                    interfaces = null;
                } else {
                    interfaces = this.delegate.getInterfaces();
                    try {
                        interfaces = new ClassLoadingTypeList(interfaces, this.classLoadingDelegate.load(this.delegate.asErasure().getName(), this.classLoader).getClassLoader(), this.classLoadingDelegate);
                    } catch (ClassNotFoundException e) {
                    }
                }
                TypeList.Generic generic = interfaces;
                if (generic == null) {
                    generic = this.interfaces;
                } else {
                    this.interfaces = generic;
                }
                return generic;
            }

            @Override // java.lang.Iterable
            public Iterator<TypeDefinition> iterator() {
                return new TypeDefinition.SuperClassIterator(this);
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDescription$SuperTypeLoading$ClassLoadingTypeList.class */
        protected static class ClassLoadingTypeList extends TypeList.Generic.AbstractBase {
            private final TypeList.Generic delegate;
            private final ClassLoader classLoader;
            private final ClassLoadingDelegate classLoadingDelegate;

            protected ClassLoadingTypeList(TypeList.Generic delegate, ClassLoader classLoader, ClassLoadingDelegate classLoadingDelegate) {
                this.delegate = delegate;
                this.classLoader = classLoader;
                this.classLoadingDelegate = classLoadingDelegate;
            }

            @Override // java.util.AbstractList, java.util.List
            public Generic get(int index) {
                return new ClassLoadingTypeProjection((Generic) this.delegate.get(index), this.classLoader, this.classLoadingDelegate);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return this.delegate.size();
            }
        }
    }
}
