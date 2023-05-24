package com.sun.xml.bind.v2.model.nav;

import com.sun.xml.bind.v2.runtime.Location;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Collection;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/nav/ReflectionNavigator.class */
public final class ReflectionNavigator implements Navigator<Type, Class, Field, Method> {
    private static final ReflectionNavigator INSTANCE;
    private static final TypeVisitor<Type, Class> baseClassFinder;
    private static final TypeVisitor<Type, BinderArg> binder;
    private static final TypeVisitor<Class, Void> eraser;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ReflectionNavigator.class.desiredAssertionStatus();
        INSTANCE = new ReflectionNavigator();
        baseClassFinder = new TypeVisitor<Type, Class>() { // from class: com.sun.xml.bind.v2.model.nav.ReflectionNavigator.1
            @Override // com.sun.xml.bind.v2.model.nav.TypeVisitor
            public Type onClass(Class c, Class sup) {
                Type[] genericInterfaces;
                Type r;
                if (sup == c) {
                    return sup;
                }
                Type sc = c.getGenericSuperclass();
                if (sc != null && (r = visit(sc, sup)) != null) {
                    return r;
                }
                for (Type i : c.getGenericInterfaces()) {
                    Type r2 = visit(i, sup);
                    if (r2 != null) {
                        return r2;
                    }
                }
                return null;
            }

            @Override // com.sun.xml.bind.v2.model.nav.TypeVisitor
            public Type onParameterizdType(ParameterizedType p, Class sup) {
                Type[] genericInterfaces;
                Class raw = (Class) p.getRawType();
                if (raw == sup) {
                    return p;
                }
                Type r = raw.getGenericSuperclass();
                if (r != null) {
                    r = visit(bind(r, raw, p), sup);
                }
                if (r != null) {
                    return r;
                }
                for (Type i : raw.getGenericInterfaces()) {
                    Type r2 = visit(bind(i, raw, p), sup);
                    if (r2 != null) {
                        return r2;
                    }
                }
                return null;
            }

            @Override // com.sun.xml.bind.v2.model.nav.TypeVisitor
            public Type onGenericArray(GenericArrayType g, Class sup) {
                return null;
            }

            @Override // com.sun.xml.bind.v2.model.nav.TypeVisitor
            public Type onVariable(TypeVariable v, Class sup) {
                return visit(v.getBounds()[0], sup);
            }

            @Override // com.sun.xml.bind.v2.model.nav.TypeVisitor
            public Type onWildcard(WildcardType w, Class sup) {
                return null;
            }

            private Type bind(Type t, GenericDeclaration decl, ParameterizedType args) {
                return (Type) ReflectionNavigator.binder.visit(t, new BinderArg(decl, args.getActualTypeArguments()));
            }
        };
        binder = new TypeVisitor<Type, BinderArg>() { // from class: com.sun.xml.bind.v2.model.nav.ReflectionNavigator.2
            @Override // com.sun.xml.bind.v2.model.nav.TypeVisitor
            public Type onClass(Class c, BinderArg args) {
                return c;
            }

            @Override // com.sun.xml.bind.v2.model.nav.TypeVisitor
            public Type onParameterizdType(ParameterizedType p, BinderArg args) {
                Type[] params = p.getActualTypeArguments();
                boolean different = false;
                for (int i = 0; i < params.length; i++) {
                    Type t = params[i];
                    params[i] = visit(t, args);
                    different |= t != params[i];
                }
                Type newOwner = p.getOwnerType();
                if (newOwner != null) {
                    newOwner = visit(newOwner, args);
                }
                if (!(different | (p.getOwnerType() != newOwner))) {
                    return p;
                }
                return new ParameterizedTypeImpl((Class) p.getRawType(), params, newOwner);
            }

            @Override // com.sun.xml.bind.v2.model.nav.TypeVisitor
            public Type onGenericArray(GenericArrayType g, BinderArg types) {
                Type c = visit(g.getGenericComponentType(), types);
                if (c == g.getGenericComponentType()) {
                    return g;
                }
                return new GenericArrayTypeImpl(c);
            }

            @Override // com.sun.xml.bind.v2.model.nav.TypeVisitor
            public Type onVariable(TypeVariable v, BinderArg types) {
                return types.replace(v);
            }

            @Override // com.sun.xml.bind.v2.model.nav.TypeVisitor
            public Type onWildcard(WildcardType w, BinderArg types) {
                Type[] lb = w.getLowerBounds();
                Type[] ub = w.getUpperBounds();
                boolean diff = false;
                for (int i = 0; i < lb.length; i++) {
                    Type t = lb[i];
                    lb[i] = visit(t, types);
                    diff |= t != lb[i];
                }
                for (int i2 = 0; i2 < ub.length; i2++) {
                    Type t2 = ub[i2];
                    ub[i2] = visit(t2, types);
                    diff |= t2 != ub[i2];
                }
                if (!diff) {
                    return w;
                }
                return new WildcardTypeImpl(lb, ub);
            }
        };
        eraser = new TypeVisitor<Class, Void>() { // from class: com.sun.xml.bind.v2.model.nav.ReflectionNavigator.6
            @Override // com.sun.xml.bind.v2.model.nav.TypeVisitor
            public Class onClass(Class c, Void v) {
                return c;
            }

            @Override // com.sun.xml.bind.v2.model.nav.TypeVisitor
            public Class onParameterizdType(ParameterizedType p, Void v) {
                return visit(p.getRawType(), null);
            }

            @Override // com.sun.xml.bind.v2.model.nav.TypeVisitor
            public Class onGenericArray(GenericArrayType g, Void v) {
                return Array.newInstance(visit(g.getGenericComponentType(), null), 0).getClass();
            }

            @Override // com.sun.xml.bind.v2.model.nav.TypeVisitor
            public Class onVariable(TypeVariable tv, Void v) {
                return visit(tv.getBounds()[0], null);
            }

            @Override // com.sun.xml.bind.v2.model.nav.TypeVisitor
            public Class onWildcard(WildcardType w, Void v) {
                return visit(w.getUpperBounds()[0], null);
            }
        };
    }

    static ReflectionNavigator getInstance() {
        return INSTANCE;
    }

    private ReflectionNavigator() {
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public Class getSuperClass(Class clazz) {
        if (clazz == Object.class) {
            return null;
        }
        Class sc = clazz.getSuperclass();
        if (sc == null) {
            sc = Object.class;
        }
        return sc;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/nav/ReflectionNavigator$BinderArg.class */
    public static class BinderArg {
        final TypeVariable[] params;
        final Type[] args;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !ReflectionNavigator.class.desiredAssertionStatus();
        }

        BinderArg(TypeVariable[] params, Type[] args) {
            this.params = params;
            this.args = args;
            if (!$assertionsDisabled && params.length != args.length) {
                throw new AssertionError();
            }
        }

        public BinderArg(GenericDeclaration decl, Type[] args) {
            this(decl.getTypeParameters(), args);
        }

        Type replace(TypeVariable v) {
            for (int i = 0; i < this.params.length; i++) {
                if (this.params[i].equals(v)) {
                    return this.args[i];
                }
            }
            return v;
        }
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public Type getBaseClass(Type t, Class sup) {
        return baseClassFinder.visit(t, sup);
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public String getClassName(Class clazz) {
        return clazz.getName();
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public String getTypeName(Type type) {
        if (type instanceof Class) {
            Class c = (Class) type;
            if (c.isArray()) {
                return getTypeName((Type) c.getComponentType()) + "[]";
            }
            return c.getName();
        }
        return type.toString();
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public String getClassShortName(Class clazz) {
        return clazz.getSimpleName();
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public Collection<? extends Field> getDeclaredFields(final Class clazz) {
        Field[] fields = (Field[]) AccessController.doPrivileged(new PrivilegedAction<Field[]>() { // from class: com.sun.xml.bind.v2.model.nav.ReflectionNavigator.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Field[] run() {
                return clazz.getDeclaredFields();
            }
        });
        return Arrays.asList(fields);
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public Field getDeclaredField(final Class clazz, final String fieldName) {
        return (Field) AccessController.doPrivileged(new PrivilegedAction<Field>() { // from class: com.sun.xml.bind.v2.model.nav.ReflectionNavigator.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Field run() {
                try {
                    return clazz.getDeclaredField(fieldName);
                } catch (NoSuchFieldException e) {
                    return null;
                }
            }
        });
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public Collection<? extends Method> getDeclaredMethods(final Class clazz) {
        Method[] methods = (Method[]) AccessController.doPrivileged(new PrivilegedAction<Method[]>() { // from class: com.sun.xml.bind.v2.model.nav.ReflectionNavigator.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Method[] run() {
                return clazz.getDeclaredMethods();
            }
        });
        return Arrays.asList(methods);
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public Class getDeclaringClassForField(Field field) {
        return field.getDeclaringClass();
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public Class getDeclaringClassForMethod(Method method) {
        return method.getDeclaringClass();
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public Type getFieldType(Field field) {
        if (field.getType().isArray()) {
            Class c = field.getType().getComponentType();
            if (c.isPrimitive()) {
                return Array.newInstance(c, 0).getClass();
            }
        }
        return fix(field.getGenericType());
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public String getFieldName(Field field) {
        return field.getName();
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public String getMethodName(Method method) {
        return method.getName();
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public Type getReturnType(Method method) {
        return fix(method.getGenericReturnType());
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public Type[] getMethodParameters(Method method) {
        return method.getGenericParameterTypes();
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public boolean isStaticMethod(Method method) {
        return Modifier.isStatic(method.getModifiers());
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public boolean isFinalMethod(Method method) {
        return Modifier.isFinal(method.getModifiers());
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public boolean isSubClassOf(Type sub, Type sup) {
        return erasure(sup).isAssignableFrom(erasure(sub));
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    /* renamed from: ref */
    public Type ref2(Class c) {
        return c;
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public Class use(Class c) {
        return c;
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public Class asDecl(Type t) {
        return erasure(t);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public Class asDecl(Class c) {
        return c;
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public <T> Class<T> erasure(Type t) {
        return eraser.visit(t, null);
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public boolean isAbstract(Class clazz) {
        return Modifier.isAbstract(clazz.getModifiers());
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public boolean isFinal(Class clazz) {
        return Modifier.isFinal(clazz.getModifiers());
    }

    public Type createParameterizedType(Class rawType, Type... arguments) {
        return new ParameterizedTypeImpl(rawType, arguments, null);
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public boolean isArray(Type t) {
        if (t instanceof Class) {
            Class c = (Class) t;
            return c.isArray();
        } else if (t instanceof GenericArrayType) {
            return true;
        } else {
            return false;
        }
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public boolean isArrayButNotByteArray(Type t) {
        if (!(t instanceof Class)) {
            return (t instanceof GenericArrayType) && ((GenericArrayType) t).getGenericComponentType() != Byte.TYPE;
        }
        Class c = (Class) t;
        return c.isArray() && c != byte[].class;
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public Type getComponentType(Type t) {
        if (t instanceof Class) {
            Class c = (Class) t;
            return c.getComponentType();
        } else if (t instanceof GenericArrayType) {
            return ((GenericArrayType) t).getGenericComponentType();
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public Type getTypeArgument(Type type, int i) {
        if (type instanceof ParameterizedType) {
            ParameterizedType p = (ParameterizedType) type;
            return fix(p.getActualTypeArguments()[i]);
        }
        throw new IllegalArgumentException();
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public boolean isParameterizedType(Type type) {
        return type instanceof ParameterizedType;
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public boolean isPrimitive(Type type) {
        if (type instanceof Class) {
            Class c = (Class) type;
            return c.isPrimitive();
        }
        return false;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public Type getPrimitive(Class primitiveType) {
        if ($assertionsDisabled || primitiveType.isPrimitive()) {
            return primitiveType;
        }
        throw new AssertionError();
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public Location getClassLocation(final Class clazz) {
        return new Location() { // from class: com.sun.xml.bind.v2.model.nav.ReflectionNavigator.7
            @Override // com.sun.xml.bind.v2.runtime.Location
            public String toString() {
                return clazz.getName();
            }
        };
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public Location getFieldLocation(final Field field) {
        return new Location() { // from class: com.sun.xml.bind.v2.model.nav.ReflectionNavigator.8
            @Override // com.sun.xml.bind.v2.runtime.Location
            public String toString() {
                return field.toString();
            }
        };
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public Location getMethodLocation(final Method method) {
        return new Location() { // from class: com.sun.xml.bind.v2.model.nav.ReflectionNavigator.9
            @Override // com.sun.xml.bind.v2.runtime.Location
            public String toString() {
                return method.toString();
            }
        };
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public boolean hasDefaultConstructor(Class c) {
        try {
            c.getDeclaredConstructor(new Class[0]);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public boolean isStaticField(Field field) {
        return Modifier.isStatic(field.getModifiers());
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public boolean isPublicMethod(Method method) {
        return Modifier.isPublic(method.getModifiers());
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public boolean isPublicField(Field field) {
        return Modifier.isPublic(field.getModifiers());
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public boolean isEnum(Class c) {
        return Enum.class.isAssignableFrom(c);
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public Field[] getEnumConstants(Class clazz) {
        try {
            Object[] values = clazz.getEnumConstants();
            Field[] fields = new Field[values.length];
            for (int i = 0; i < values.length; i++) {
                fields[i] = clazz.getField(((Enum) values[i]).name());
            }
            return fields;
        } catch (NoSuchFieldException e) {
            throw new NoSuchFieldError(e.getMessage());
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public Type getVoidType() {
        return Void.class;
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public String getPackageName(Class clazz) {
        String name = clazz.getName();
        int idx = name.lastIndexOf(46);
        if (idx < 0) {
            return "";
        }
        return name.substring(0, idx);
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public Class loadObjectFactory(Class referencePoint, String pkg) {
        ClassLoader cl = SecureLoader.getClassClassLoader(referencePoint);
        if (cl == null) {
            cl = SecureLoader.getSystemClassLoader();
        }
        try {
            return cl.loadClass(pkg + ".ObjectFactory");
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public boolean isBridgeMethod(Method method) {
        return method.isBridge();
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public boolean isOverriding(Method method, final Class base) {
        final String name = method.getName();
        final Class[] params = method.getParameterTypes();
        return ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: com.sun.xml.bind.v2.model.nav.ReflectionNavigator.10
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Boolean run() {
                Method m;
                Class cls = base;
                while (true) {
                    Class clazz = cls;
                    if (clazz != null) {
                        try {
                            m = clazz.getDeclaredMethod(name, params);
                        } catch (NoSuchMethodException e) {
                        }
                        if (m != null) {
                            return Boolean.TRUE;
                        }
                        continue;
                        cls = clazz.getSuperclass();
                    } else {
                        return Boolean.FALSE;
                    }
                }
            }
        })).booleanValue();
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public boolean isInterface(Class clazz) {
        return clazz.isInterface();
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public boolean isTransient(Field f) {
        return Modifier.isTransient(f.getModifiers());
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public boolean isInnerClass(Class clazz) {
        return (clazz.getEnclosingClass() == null || Modifier.isStatic(clazz.getModifiers())) ? false : true;
    }

    @Override // com.sun.xml.bind.v2.model.nav.Navigator
    public boolean isSameType(Type t1, Type t2) {
        return t1.equals(t2);
    }

    private Type fix(Type t) {
        if (!(t instanceof GenericArrayType)) {
            return t;
        }
        GenericArrayType gat = (GenericArrayType) t;
        if (gat.getGenericComponentType() instanceof Class) {
            Class c = (Class) gat.getGenericComponentType();
            return Array.newInstance(c, 0).getClass();
        }
        return t;
    }
}
