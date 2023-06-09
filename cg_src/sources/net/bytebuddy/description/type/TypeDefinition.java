package net.bytebuddy.description.type;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Iterator;
import java.util.NoSuchElementException;
import net.bytebuddy.description.ModifierReviewable;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.field.FieldList;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.description.type.TypeList;
import net.bytebuddy.implementation.bytecode.StackSize;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDefinition.class */
public interface TypeDefinition extends NamedElement, ModifierReviewable.ForTypeDefinition, Iterable<TypeDefinition> {
    public static final String RAW_TYPES_PROPERTY = "net.bytebuddy.raw";

    TypeDescription.Generic asGenericType();

    TypeDescription asErasure();

    TypeDescription.Generic getSuperClass();

    TypeList.Generic getInterfaces();

    FieldList<?> getDeclaredFields();

    MethodList<?> getDeclaredMethods();

    TypeDefinition getComponentType();

    RecordComponentList<?> getRecordComponents();

    Sort getSort();

    String getTypeName();

    StackSize getStackSize();

    boolean isArray();

    boolean isRecord();

    boolean isPrimitive();

    boolean represents(Type type);

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDefinition$Sort.class */
    public enum Sort {
        NON_GENERIC,
        GENERIC_ARRAY,
        PARAMETERIZED,
        WILDCARD,
        VARIABLE,
        VARIABLE_SYMBOLIC;

        public static TypeDescription.Generic describe(Type type) {
            return describe(type, TypeDescription.Generic.AnnotationReader.NoOp.INSTANCE);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public static TypeDescription.Generic describe(Type type, TypeDescription.Generic.AnnotationReader annotationReader) {
            if (type instanceof Class) {
                return new TypeDescription.Generic.OfNonGenericType.ForLoadedType((Class) type, annotationReader);
            }
            if (type instanceof GenericArrayType) {
                return new TypeDescription.Generic.OfGenericArray.ForLoadedType((GenericArrayType) type, annotationReader);
            }
            if (type instanceof ParameterizedType) {
                return new TypeDescription.Generic.OfParameterizedType.ForLoadedType((ParameterizedType) type, annotationReader);
            }
            if (type instanceof TypeVariable) {
                return new TypeDescription.Generic.OfTypeVariable.ForLoadedType((TypeVariable) type, annotationReader);
            }
            if (type instanceof WildcardType) {
                return new TypeDescription.Generic.OfWildcardType.ForLoadedType((WildcardType) type, annotationReader);
            }
            throw new IllegalArgumentException("Unknown type: " + type);
        }

        public boolean isNonGeneric() {
            return this == NON_GENERIC;
        }

        public boolean isParameterized() {
            return this == PARAMETERIZED;
        }

        public boolean isGenericArray() {
            return this == GENERIC_ARRAY;
        }

        public boolean isWildcard() {
            return this == WILDCARD;
        }

        public boolean isTypeVariable() {
            return this == VARIABLE || this == VARIABLE_SYMBOLIC;
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeDefinition$SuperClassIterator.class */
    public static class SuperClassIterator implements Iterator<TypeDefinition> {
        private TypeDefinition nextClass;

        public SuperClassIterator(TypeDefinition initialType) {
            this.nextClass = initialType;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.nextClass != null;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public TypeDefinition next() {
            if (!hasNext()) {
                throw new NoSuchElementException("End of type hierarchy");
            }
            try {
                return this.nextClass;
            } finally {
                this.nextClass = this.nextClass.getSuperClass();
            }
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("remove");
        }
    }
}
