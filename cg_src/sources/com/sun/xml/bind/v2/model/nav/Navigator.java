package com.sun.xml.bind.v2.model.nav;

import com.sun.xml.bind.v2.runtime.Location;
import java.util.Collection;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/nav/Navigator.class */
public interface Navigator<T, C, F, M> {
    C getSuperClass(C c);

    T getBaseClass(T t, C c);

    String getClassName(C c);

    String getTypeName(T t);

    String getClassShortName(C c);

    Collection<? extends F> getDeclaredFields(C c);

    F getDeclaredField(C c, String str);

    Collection<? extends M> getDeclaredMethods(C c);

    C getDeclaringClassForField(F f);

    C getDeclaringClassForMethod(M m);

    T getFieldType(F f);

    String getFieldName(F f);

    String getMethodName(M m);

    T getReturnType(M m);

    T[] getMethodParameters(M m);

    boolean isStaticMethod(M m);

    boolean isSubClassOf(T t, T t2);

    T ref(Class cls);

    T use(C c);

    C asDecl(T t);

    C asDecl(Class cls);

    boolean isArray(T t);

    boolean isArrayButNotByteArray(T t);

    T getComponentType(T t);

    T getTypeArgument(T t, int i);

    boolean isParameterizedType(T t);

    boolean isPrimitive(T t);

    T getPrimitive(Class cls);

    Location getClassLocation(C c);

    Location getFieldLocation(F f);

    Location getMethodLocation(M m);

    boolean hasDefaultConstructor(C c);

    boolean isStaticField(F f);

    boolean isPublicMethod(M m);

    boolean isFinalMethod(M m);

    boolean isPublicField(F f);

    boolean isEnum(C c);

    <P> T erasure(T t);

    boolean isAbstract(C c);

    boolean isFinal(C c);

    F[] getEnumConstants(C c);

    T getVoidType();

    String getPackageName(C c);

    C loadObjectFactory(C c, String str);

    boolean isBridgeMethod(M m);

    boolean isOverriding(M m, C c);

    boolean isInterface(C c);

    boolean isTransient(F f);

    boolean isInnerClass(C c);

    boolean isSameType(T t, T t2);
}
