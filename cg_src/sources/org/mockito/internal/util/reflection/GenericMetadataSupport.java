package org.mockito.internal.util.reflection;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.Checks;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/reflection/GenericMetadataSupport.class */
public abstract class GenericMetadataSupport {
    protected Map<TypeVariable<?>, Type> contextualActualTypeParameters = new HashMap();

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/reflection/GenericMetadataSupport$BoundedType.class */
    public interface BoundedType extends Type {
        Type firstBound();

        Type[] interfaceBounds();
    }

    public abstract Class<?> rawType();

    protected void registerAllTypeVariables(Type classType) {
        Queue<Type> typesToRegister = new LinkedList<>();
        Set<Type> registeredTypes = new HashSet<>();
        typesToRegister.add(classType);
        while (!typesToRegister.isEmpty()) {
            Type typeToRegister = typesToRegister.poll();
            if (typeToRegister != null && !registeredTypes.contains(typeToRegister)) {
                registerTypeVariablesOn(typeToRegister);
                registeredTypes.add(typeToRegister);
                Class<?> rawType = extractRawTypeOf(typeToRegister);
                typesToRegister.add(rawType.getGenericSuperclass());
                typesToRegister.addAll(Arrays.asList(rawType.getGenericInterfaces()));
            }
        }
    }

    protected Class<?> extractRawTypeOf(Type type) {
        if (type instanceof Class) {
            return (Class) type;
        }
        if (type instanceof ParameterizedType) {
            return (Class) ((ParameterizedType) type).getRawType();
        }
        if (type instanceof BoundedType) {
            return extractRawTypeOf(((BoundedType) type).firstBound());
        }
        if (type instanceof TypeVariable) {
            return extractRawTypeOf(this.contextualActualTypeParameters.get(type));
        }
        throw new MockitoException("Raw extraction not supported for : '" + type + "'");
    }

    protected void registerTypeVariablesOn(Type classType) {
        int i;
        if (!(classType instanceof ParameterizedType)) {
            return;
        }
        ParameterizedType parameterizedType = (ParameterizedType) classType;
        TypeVariable<?>[] typeParameters = ((Class) parameterizedType.getRawType()).getTypeParameters();
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        for (i = 0; i < actualTypeArguments.length; i = i + 1) {
            TypeVariable<?> typeParameter = typeParameters[i];
            Type actualTypeArgument = actualTypeArguments[i];
            if (actualTypeArgument instanceof TypeVariable) {
                registerTypeVariableIfNotPresent((TypeVariable) actualTypeArgument);
                i = this.contextualActualTypeParameters.containsKey(typeParameter) ? i + 1 : 0;
            }
            if (actualTypeArgument instanceof WildcardType) {
                this.contextualActualTypeParameters.put(typeParameter, boundsOf((WildcardType) actualTypeArgument));
            } else if (typeParameter != actualTypeArgument) {
                this.contextualActualTypeParameters.put(typeParameter, actualTypeArgument);
            }
        }
    }

    protected void registerTypeParametersOn(TypeVariable<?>[] typeParameters) {
        for (TypeVariable<?> type : typeParameters) {
            registerTypeVariableIfNotPresent(type);
        }
    }

    private void registerTypeVariableIfNotPresent(TypeVariable<?> typeVariable) {
        if (!this.contextualActualTypeParameters.containsKey(typeVariable)) {
            this.contextualActualTypeParameters.put(typeVariable, boundsOf(typeVariable));
        }
    }

    private BoundedType boundsOf(TypeVariable<?> typeParameter) {
        if (typeParameter.getBounds()[0] instanceof TypeVariable) {
            return boundsOf((TypeVariable) typeParameter.getBounds()[0]);
        }
        return new TypeVarBoundedType(typeParameter);
    }

    private BoundedType boundsOf(WildcardType wildCard) {
        WildCardBoundedType wildCardBoundedType = new WildCardBoundedType(wildCard);
        if (wildCardBoundedType.firstBound() instanceof TypeVariable) {
            return boundsOf((TypeVariable) wildCardBoundedType.firstBound());
        }
        return wildCardBoundedType;
    }

    public List<Type> extraInterfaces() {
        return Collections.emptyList();
    }

    public Class<?>[] rawExtraInterfaces() {
        return new Class[0];
    }

    public boolean hasRawExtraInterfaces() {
        return rawExtraInterfaces().length > 0;
    }

    public Map<TypeVariable<?>, Type> actualTypeArguments() {
        TypeVariable<?>[] typeParameters = rawType().getTypeParameters();
        LinkedHashMap<TypeVariable<?>, Type> actualTypeArguments = new LinkedHashMap<>();
        for (TypeVariable<?> typeParameter : typeParameters) {
            Type actualType = getActualTypeArgumentFor(typeParameter);
            actualTypeArguments.put(typeParameter, actualType);
        }
        return actualTypeArguments;
    }

    protected Type getActualTypeArgumentFor(TypeVariable<?> typeParameter) {
        Type type = this.contextualActualTypeParameters.get(typeParameter);
        if (type instanceof TypeVariable) {
            TypeVariable<?> typeVariable = (TypeVariable) type;
            return getActualTypeArgumentFor(typeVariable);
        }
        return type;
    }

    public GenericMetadataSupport resolveGenericReturnType(Method method) {
        Type genericReturnType = method.getGenericReturnType();
        int arity = 0;
        while (genericReturnType instanceof GenericArrayType) {
            arity++;
            genericReturnType = ((GenericArrayType) genericReturnType).getGenericComponentType();
        }
        GenericMetadataSupport genericMetadataSupport = resolveGenericType(genericReturnType, method);
        if (arity == 0) {
            return genericMetadataSupport;
        }
        return new GenericArrayReturnType(genericMetadataSupport, arity);
    }

    private GenericMetadataSupport resolveGenericType(Type type, Method method) {
        if (type instanceof Class) {
            return new NotGenericReturnTypeSupport(this, type);
        }
        if (type instanceof ParameterizedType) {
            return new ParameterizedReturnType(this, method.getTypeParameters(), (ParameterizedType) type);
        }
        if (type instanceof TypeVariable) {
            return new TypeVariableReturnType(this, method.getTypeParameters(), (TypeVariable) type);
        }
        throw new MockitoException("Ouch, it shouldn't happen, type '" + type.getClass().getCanonicalName() + "' on method : '" + method.toGenericString() + "' is not supported : " + type);
    }

    public static GenericMetadataSupport inferFrom(Type type) {
        Checks.checkNotNull(type, "type");
        if (type instanceof Class) {
            return new FromClassGenericMetadataSupport((Class) type);
        }
        if (type instanceof ParameterizedType) {
            return new FromParameterizedTypeGenericMetadataSupport((ParameterizedType) type);
        }
        throw new MockitoException("Type meta-data for this Type (" + type.getClass().getCanonicalName() + ") is not supported : " + type);
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/reflection/GenericMetadataSupport$FromClassGenericMetadataSupport.class */
    private static class FromClassGenericMetadataSupport extends GenericMetadataSupport {
        private final Class<?> clazz;

        public FromClassGenericMetadataSupport(Class<?> clazz) {
            this.clazz = clazz;
            registerTypeParametersOn(clazz.getTypeParameters());
            registerAllTypeVariables(clazz);
        }

        @Override // org.mockito.internal.util.reflection.GenericMetadataSupport
        public Class<?> rawType() {
            return this.clazz;
        }
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/reflection/GenericMetadataSupport$FromParameterizedTypeGenericMetadataSupport.class */
    private static class FromParameterizedTypeGenericMetadataSupport extends GenericMetadataSupport {
        private final ParameterizedType parameterizedType;

        public FromParameterizedTypeGenericMetadataSupport(ParameterizedType parameterizedType) {
            this.parameterizedType = parameterizedType;
            readActualTypeParameters();
        }

        private void readActualTypeParameters() {
            registerAllTypeVariables(this.parameterizedType);
        }

        @Override // org.mockito.internal.util.reflection.GenericMetadataSupport
        public Class<?> rawType() {
            return (Class) this.parameterizedType.getRawType();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/reflection/GenericMetadataSupport$ParameterizedReturnType.class */
    public static class ParameterizedReturnType extends GenericMetadataSupport {
        private final ParameterizedType parameterizedType;
        private final TypeVariable<?>[] typeParameters;

        public ParameterizedReturnType(GenericMetadataSupport source, TypeVariable<?>[] typeParameters, ParameterizedType parameterizedType) {
            this.parameterizedType = parameterizedType;
            this.typeParameters = typeParameters;
            this.contextualActualTypeParameters = source.contextualActualTypeParameters;
            readTypeParameters();
            readTypeVariables();
        }

        private void readTypeParameters() {
            registerTypeParametersOn(this.typeParameters);
        }

        private void readTypeVariables() {
            registerTypeVariablesOn(this.parameterizedType);
        }

        @Override // org.mockito.internal.util.reflection.GenericMetadataSupport
        public Class<?> rawType() {
            return (Class) this.parameterizedType.getRawType();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/reflection/GenericMetadataSupport$TypeVariableReturnType.class */
    public static class TypeVariableReturnType extends GenericMetadataSupport {
        private final TypeVariable<?> typeVariable;
        private final TypeVariable<?>[] typeParameters;
        private Class<?> rawType;
        private List<Type> extraInterfaces;

        public TypeVariableReturnType(GenericMetadataSupport source, TypeVariable<?>[] typeParameters, TypeVariable<?> typeVariable) {
            this.typeParameters = typeParameters;
            this.typeVariable = typeVariable;
            this.contextualActualTypeParameters = source.contextualActualTypeParameters;
            readTypeParameters();
            readTypeVariables();
        }

        private void readTypeParameters() {
            registerTypeParametersOn(this.typeParameters);
        }

        private void readTypeVariables() {
            Type[] bounds;
            for (Type type : this.typeVariable.getBounds()) {
                registerTypeVariablesOn(type);
            }
            registerTypeParametersOn(new TypeVariable[]{this.typeVariable});
            registerTypeVariablesOn(getActualTypeArgumentFor(this.typeVariable));
        }

        @Override // org.mockito.internal.util.reflection.GenericMetadataSupport
        public Class<?> rawType() {
            if (this.rawType == null) {
                this.rawType = extractRawTypeOf(this.typeVariable);
            }
            return this.rawType;
        }

        @Override // org.mockito.internal.util.reflection.GenericMetadataSupport
        public List<Type> extraInterfaces() {
            if (this.extraInterfaces != null) {
                return this.extraInterfaces;
            }
            Type type = extractActualBoundedTypeOf(this.typeVariable);
            if (type instanceof BoundedType) {
                List<Type> asList = Arrays.asList(((BoundedType) type).interfaceBounds());
                this.extraInterfaces = asList;
                return asList;
            } else if (type instanceof ParameterizedType) {
                List<Type> singletonList = Collections.singletonList(type);
                this.extraInterfaces = singletonList;
                return singletonList;
            } else if (type instanceof Class) {
                List<Type> emptyList = Collections.emptyList();
                this.extraInterfaces = emptyList;
                return emptyList;
            } else {
                throw new MockitoException("Cannot extract extra-interfaces from '" + this.typeVariable + "' : '" + type + "'");
            }
        }

        @Override // org.mockito.internal.util.reflection.GenericMetadataSupport
        public Class<?>[] rawExtraInterfaces() {
            List<Type> extraInterfaces = extraInterfaces();
            List<Class<?>> rawExtraInterfaces = new ArrayList<>();
            for (Type extraInterface : extraInterfaces) {
                Class<?> rawInterface = extractRawTypeOf(extraInterface);
                if (!rawType().equals(rawInterface)) {
                    rawExtraInterfaces.add(rawInterface);
                }
            }
            return (Class[]) rawExtraInterfaces.toArray(new Class[rawExtraInterfaces.size()]);
        }

        private Type extractActualBoundedTypeOf(Type type) {
            if (type instanceof TypeVariable) {
                return extractActualBoundedTypeOf(this.contextualActualTypeParameters.get(type));
            }
            if (type instanceof BoundedType) {
                Type actualFirstBound = extractActualBoundedTypeOf(((BoundedType) type).firstBound());
                if (!(actualFirstBound instanceof BoundedType)) {
                    return type;
                }
                return actualFirstBound;
            }
            return type;
        }
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/reflection/GenericMetadataSupport$GenericArrayReturnType.class */
    private static class GenericArrayReturnType extends GenericMetadataSupport {
        private final GenericMetadataSupport genericArrayType;
        private final int arity;

        public GenericArrayReturnType(GenericMetadataSupport genericArrayType, int arity) {
            this.genericArrayType = genericArrayType;
            this.arity = arity;
        }

        @Override // org.mockito.internal.util.reflection.GenericMetadataSupport
        public Class<?> rawType() {
            Class<?> rawComponentType = this.genericArrayType.rawType();
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < this.arity; i++) {
                stringBuilder.append("[");
            }
            try {
                return Class.forName(stringBuilder.append("L").append(rawComponentType.getName()).append(";").toString(), false, rawComponentType.getClassLoader());
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("This was not supposed to happen.", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/reflection/GenericMetadataSupport$NotGenericReturnTypeSupport.class */
    public static class NotGenericReturnTypeSupport extends GenericMetadataSupport {
        private final Class<?> returnType;

        public NotGenericReturnTypeSupport(GenericMetadataSupport source, Type genericReturnType) {
            this.returnType = (Class) genericReturnType;
            this.contextualActualTypeParameters = source.contextualActualTypeParameters;
            registerAllTypeVariables(this.returnType);
        }

        @Override // org.mockito.internal.util.reflection.GenericMetadataSupport
        public Class<?> rawType() {
            return this.returnType;
        }
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/reflection/GenericMetadataSupport$TypeVarBoundedType.class */
    public static class TypeVarBoundedType implements BoundedType {
        private final TypeVariable<?> typeVariable;

        public TypeVarBoundedType(TypeVariable<?> typeVariable) {
            this.typeVariable = typeVariable;
        }

        @Override // org.mockito.internal.util.reflection.GenericMetadataSupport.BoundedType
        public Type firstBound() {
            return this.typeVariable.getBounds()[0];
        }

        @Override // org.mockito.internal.util.reflection.GenericMetadataSupport.BoundedType
        public Type[] interfaceBounds() {
            Type[] interfaceBounds = new Type[this.typeVariable.getBounds().length - 1];
            System.arraycopy(this.typeVariable.getBounds(), 1, interfaceBounds, 0, this.typeVariable.getBounds().length - 1);
            return interfaceBounds;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            return this.typeVariable.equals(((TypeVarBoundedType) o).typeVariable);
        }

        public int hashCode() {
            return this.typeVariable.hashCode();
        }

        public String toString() {
            return "{firstBound=" + firstBound() + ", interfaceBounds=" + Arrays.deepToString(interfaceBounds()) + '}';
        }

        public TypeVariable<?> typeVariable() {
            return this.typeVariable;
        }
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/reflection/GenericMetadataSupport$WildCardBoundedType.class */
    public static class WildCardBoundedType implements BoundedType {
        private final WildcardType wildcard;

        public WildCardBoundedType(WildcardType wildcard) {
            this.wildcard = wildcard;
        }

        @Override // org.mockito.internal.util.reflection.GenericMetadataSupport.BoundedType
        public Type firstBound() {
            Type[] lowerBounds = this.wildcard.getLowerBounds();
            Type[] upperBounds = this.wildcard.getUpperBounds();
            return lowerBounds.length != 0 ? lowerBounds[0] : upperBounds[0];
        }

        @Override // org.mockito.internal.util.reflection.GenericMetadataSupport.BoundedType
        public Type[] interfaceBounds() {
            return new Type[0];
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            return this.wildcard.equals(((TypeVarBoundedType) o).typeVariable);
        }

        public int hashCode() {
            return this.wildcard.hashCode();
        }

        public String toString() {
            return "{firstBound=" + firstBound() + ", interfaceBounds=[]}";
        }

        public WildcardType wildCard() {
            return this.wildcard;
        }
    }
}
