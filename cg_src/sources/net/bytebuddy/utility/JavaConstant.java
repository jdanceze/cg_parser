package net.bytebuddy.utility;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.enumeration.EnumerationDescription;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.description.type.TypeList;
import net.bytebuddy.jar.asm.ConstantDynamic;
import net.bytebuddy.jar.asm.Handle;
import net.bytebuddy.jar.asm.Type;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/utility/JavaConstant.class */
public interface JavaConstant {
    Object asConstantPoolValue();

    TypeDescription getType();

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/utility/JavaConstant$MethodType.class */
    public static class MethodType implements JavaConstant {
        private static final Dispatcher DISPATCHER = (Dispatcher) AccessController.doPrivileged(Dispatcher.CreationAction.INSTANCE);
        private final TypeDescription returnType;
        private final List<? extends TypeDescription> parameterTypes;

        protected MethodType(TypeDescription returnType, List<? extends TypeDescription> parameterTypes) {
            this.returnType = returnType;
            this.parameterTypes = parameterTypes;
        }

        public static MethodType ofLoaded(Object methodType) {
            if (!JavaType.METHOD_TYPE.isInstance(methodType)) {
                throw new IllegalArgumentException("Expected method type object: " + methodType);
            }
            return of(DISPATCHER.returnType(methodType), DISPATCHER.parameterArray(methodType));
        }

        public static MethodType of(Class<?> returnType, Class<?>... parameterType) {
            return of(TypeDescription.ForLoadedType.of(returnType), new TypeList.ForLoadedTypes(parameterType));
        }

        public static MethodType of(TypeDescription returnType, List<? extends TypeDescription> parameterTypes) {
            return new MethodType(returnType, parameterTypes);
        }

        public static MethodType of(Method method) {
            return of(new MethodDescription.ForLoadedMethod(method));
        }

        public static MethodType of(Constructor<?> constructor) {
            return of(new MethodDescription.ForLoadedConstructor(constructor));
        }

        public static MethodType of(MethodDescription methodDescription) {
            return new MethodType(methodDescription.getReturnType().asErasure(), methodDescription.getParameters().asTypeList().asErasures());
        }

        public static MethodType ofSetter(Field field) {
            return ofSetter(new FieldDescription.ForLoadedField(field));
        }

        public static MethodType ofSetter(FieldDescription fieldDescription) {
            return new MethodType(TypeDescription.VOID, Collections.singletonList(fieldDescription.getType().asErasure()));
        }

        public static MethodType ofGetter(Field field) {
            return ofGetter(new FieldDescription.ForLoadedField(field));
        }

        public static MethodType ofGetter(FieldDescription fieldDescription) {
            return new MethodType(fieldDescription.getType().asErasure(), Collections.emptyList());
        }

        public static MethodType ofConstant(Object instance) {
            return ofConstant(instance.getClass());
        }

        public static MethodType ofConstant(Class<?> type) {
            return ofConstant(TypeDescription.ForLoadedType.of(type));
        }

        public static MethodType ofConstant(TypeDescription typeDescription) {
            return new MethodType(typeDescription, Collections.emptyList());
        }

        public TypeDescription getReturnType() {
            return this.returnType;
        }

        public TypeList getParameterTypes() {
            return new TypeList.Explicit(this.parameterTypes);
        }

        public String getDescriptor() {
            StringBuilder stringBuilder = new StringBuilder("(");
            for (TypeDescription parameterType : this.parameterTypes) {
                stringBuilder.append(parameterType.getDescriptor());
            }
            return stringBuilder.append(')').append(this.returnType.getDescriptor()).toString();
        }

        @Override // net.bytebuddy.utility.JavaConstant
        public Object asConstantPoolValue() {
            StringBuilder stringBuilder = new StringBuilder().append('(');
            for (TypeDescription parameterType : getParameterTypes()) {
                stringBuilder.append(parameterType.getDescriptor());
            }
            return Type.getMethodType(stringBuilder.append(')').append(getReturnType().getDescriptor()).toString());
        }

        @Override // net.bytebuddy.utility.JavaConstant
        public TypeDescription getType() {
            return JavaType.METHOD_TYPE.getTypeStub();
        }

        public int hashCode() {
            int result = this.returnType.hashCode();
            return (31 * result) + this.parameterTypes.hashCode();
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof MethodType)) {
                return false;
            }
            MethodType methodType = (MethodType) other;
            return this.parameterTypes.equals(methodType.parameterTypes) && this.returnType.equals(methodType.returnType);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/utility/JavaConstant$MethodType$Dispatcher.class */
        public interface Dispatcher {
            Class<?> returnType(Object obj);

            Class<?>[] parameterArray(Object obj);

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/utility/JavaConstant$MethodType$Dispatcher$CreationAction.class */
            public enum CreationAction implements PrivilegedAction<Dispatcher> {
                INSTANCE;

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                @SuppressFBWarnings(value = {"REC_CATCH_EXCEPTION"}, justification = "Exception should not be rethrown but trigger a fallback")
                public Dispatcher run() {
                    try {
                        Class<?> methodType = JavaType.METHOD_TYPE.load();
                        return new ForJava7CapableVm(methodType.getMethod("returnType", new Class[0]), methodType.getMethod("parameterArray", new Class[0]));
                    } catch (Exception e) {
                        return ForLegacyVm.INSTANCE;
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/utility/JavaConstant$MethodType$Dispatcher$ForJava7CapableVm.class */
            public static class ForJava7CapableVm implements Dispatcher {
                private static final Object[] NO_ARGUMENTS = new Object[0];
                private final Method returnType;
                private final Method parameterArray;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.returnType.equals(((ForJava7CapableVm) obj).returnType) && this.parameterArray.equals(((ForJava7CapableVm) obj).parameterArray);
                }

                public int hashCode() {
                    return (((17 * 31) + this.returnType.hashCode()) * 31) + this.parameterArray.hashCode();
                }

                protected ForJava7CapableVm(Method returnType, Method parameterArray) {
                    this.returnType = returnType;
                    this.parameterArray = parameterArray;
                }

                @Override // net.bytebuddy.utility.JavaConstant.MethodType.Dispatcher
                public Class<?> returnType(Object methodType) {
                    try {
                        return (Class) this.returnType.invoke(methodType, NO_ARGUMENTS);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.invoke.MethodType#returnType", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.invoke.MethodType#returnType", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.utility.JavaConstant.MethodType.Dispatcher
                public Class<?>[] parameterArray(Object methodType) {
                    try {
                        return (Class[]) this.parameterArray.invoke(methodType, NO_ARGUMENTS);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.invoke.MethodType#parameterArray", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.invoke.MethodType#parameterArray", exception2.getCause());
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/utility/JavaConstant$MethodType$Dispatcher$ForLegacyVm.class */
            public enum ForLegacyVm implements Dispatcher {
                INSTANCE;

                @Override // net.bytebuddy.utility.JavaConstant.MethodType.Dispatcher
                public Class<?> returnType(Object methodType) {
                    throw new UnsupportedOperationException("Unsupported type for the current JVM: java.lang.invoke.MethodType");
                }

                @Override // net.bytebuddy.utility.JavaConstant.MethodType.Dispatcher
                public Class<?>[] parameterArray(Object methodType) {
                    throw new UnsupportedOperationException("Unsupported type for the current JVM: java.lang.invoke.MethodType");
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/utility/JavaConstant$MethodHandle.class */
    public static class MethodHandle implements JavaConstant {
        private static final Dispatcher.Initializable DISPATCHER = (Dispatcher.Initializable) AccessController.doPrivileged(Dispatcher.CreationAction.INSTANCE);
        private final HandleType handleType;
        private final TypeDescription ownerType;
        private final String name;
        private final TypeDescription returnType;
        private final List<? extends TypeDescription> parameterTypes;

        protected MethodHandle(HandleType handleType, TypeDescription ownerType, String name, TypeDescription returnType, List<? extends TypeDescription> parameterTypes) {
            this.handleType = handleType;
            this.ownerType = ownerType;
            this.name = name;
            this.returnType = returnType;
            this.parameterTypes = parameterTypes;
        }

        public static MethodHandle ofLoaded(Object methodHandle) {
            return ofLoaded(methodHandle, DISPATCHER.publicLookup());
        }

        public static MethodHandle ofLoaded(Object methodHandle, Object lookup) {
            if (!JavaType.METHOD_HANDLE.isInstance(methodHandle)) {
                throw new IllegalArgumentException("Expected method handle object: " + methodHandle);
            }
            if (!JavaType.METHOD_HANDLES_LOOKUP.isInstance(lookup)) {
                throw new IllegalArgumentException("Expected method handle lookup object: " + lookup);
            }
            Dispatcher dispatcher = DISPATCHER.initialize();
            Object methodHandleInfo = dispatcher.reveal(lookup, methodHandle);
            Object methodType = dispatcher.getMethodType(methodHandleInfo);
            return new MethodHandle(HandleType.of(dispatcher.getReferenceKind(methodHandleInfo)), TypeDescription.ForLoadedType.of(dispatcher.getDeclaringClass(methodHandleInfo)), dispatcher.getName(methodHandleInfo), TypeDescription.ForLoadedType.of(dispatcher.returnType(methodType)), new TypeList.ForLoadedTypes(dispatcher.parameterArray(methodType)));
        }

        public static MethodHandle of(Method method) {
            return of(new MethodDescription.ForLoadedMethod(method));
        }

        public static MethodHandle of(Constructor<?> constructor) {
            return of(new MethodDescription.ForLoadedConstructor(constructor));
        }

        public static MethodHandle of(MethodDescription.InDefinedShape methodDescription) {
            return new MethodHandle(HandleType.of(methodDescription), methodDescription.getDeclaringType().asErasure(), methodDescription.getInternalName(), methodDescription.getReturnType().asErasure(), methodDescription.getParameters().asTypeList().asErasures());
        }

        public static MethodHandle ofSpecial(Method method, Class<?> type) {
            return ofSpecial(new MethodDescription.ForLoadedMethod(method), TypeDescription.ForLoadedType.of(type));
        }

        public static MethodHandle ofSpecial(MethodDescription.InDefinedShape methodDescription, TypeDescription typeDescription) {
            if (!methodDescription.isSpecializableFor(typeDescription)) {
                throw new IllegalArgumentException("Cannot specialize " + methodDescription + " for " + typeDescription);
            }
            return new MethodHandle(HandleType.ofSpecial(methodDescription), typeDescription, methodDescription.getInternalName(), methodDescription.getReturnType().asErasure(), methodDescription.getParameters().asTypeList().asErasures());
        }

        public static MethodHandle ofGetter(Field field) {
            return ofGetter(new FieldDescription.ForLoadedField(field));
        }

        public static MethodHandle ofGetter(FieldDescription.InDefinedShape fieldDescription) {
            return new MethodHandle(HandleType.ofGetter(fieldDescription), fieldDescription.getDeclaringType().asErasure(), fieldDescription.getInternalName(), fieldDescription.getType().asErasure(), Collections.emptyList());
        }

        public static MethodHandle ofSetter(Field field) {
            return ofSetter(new FieldDescription.ForLoadedField(field));
        }

        public static MethodHandle ofSetter(FieldDescription.InDefinedShape fieldDescription) {
            return new MethodHandle(HandleType.ofSetter(fieldDescription), fieldDescription.getDeclaringType().asErasure(), fieldDescription.getInternalName(), TypeDescription.VOID, Collections.singletonList(fieldDescription.getType().asErasure()));
        }

        @Override // net.bytebuddy.utility.JavaConstant
        public Object asConstantPoolValue() {
            return new Handle(getHandleType().getIdentifier(), getOwnerType().getInternalName(), getName(), getDescriptor(), getOwnerType().isInterface());
        }

        @Override // net.bytebuddy.utility.JavaConstant
        public TypeDescription getType() {
            return JavaType.METHOD_HANDLE.getTypeStub();
        }

        public HandleType getHandleType() {
            return this.handleType;
        }

        public TypeDescription getOwnerType() {
            return this.ownerType;
        }

        public String getName() {
            return this.name;
        }

        public TypeDescription getReturnType() {
            return this.returnType;
        }

        public TypeList getParameterTypes() {
            return new TypeList.Explicit(this.parameterTypes);
        }

        public String getDescriptor() {
            if (this.handleType.isField()) {
                return this.returnType.getDescriptor();
            }
            StringBuilder stringBuilder = new StringBuilder().append('(');
            for (TypeDescription parameterType : this.parameterTypes) {
                stringBuilder.append(parameterType.getDescriptor());
            }
            return stringBuilder.append(')').append(this.returnType.getDescriptor()).toString();
        }

        public int hashCode() {
            int result = this.handleType.hashCode();
            return (31 * ((31 * ((31 * ((31 * result) + this.ownerType.hashCode())) + this.name.hashCode())) + this.returnType.hashCode())) + this.parameterTypes.hashCode();
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof MethodHandle)) {
                return false;
            }
            MethodHandle methodHandle = (MethodHandle) other;
            return this.handleType == methodHandle.handleType && this.name.equals(methodHandle.name) && this.ownerType.equals(methodHandle.ownerType) && this.parameterTypes.equals(methodHandle.parameterTypes) && this.returnType.equals(methodHandle.returnType);
        }

        public static Class<?> lookupType(Object callerClassLookup) {
            return DISPATCHER.lookupType(callerClassLookup);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/utility/JavaConstant$MethodHandle$Dispatcher.class */
        public interface Dispatcher {

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/utility/JavaConstant$MethodHandle$Dispatcher$Initializable.class */
            public interface Initializable {
                Dispatcher initialize();

                Object publicLookup();

                Class<?> lookupType(Object obj);
            }

            Object reveal(Object obj, Object obj2);

            Object getMethodType(Object obj);

            int getReferenceKind(Object obj);

            Class<?> getDeclaringClass(Object obj);

            String getName(Object obj);

            Class<?> returnType(Object obj);

            List<? extends Class<?>> parameterArray(Object obj);

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/utility/JavaConstant$MethodHandle$Dispatcher$CreationAction.class */
            public enum CreationAction implements PrivilegedAction<Initializable> {
                INSTANCE;

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                @SuppressFBWarnings(value = {"REC_CATCH_EXCEPTION"}, justification = "Exception should not be rethrown but trigger a fallback")
                public Initializable run() {
                    try {
                        return new ForJava8CapableVm(Class.forName("java.lang.invoke.MethodHandles").getMethod("publicLookup", new Class[0]), Class.forName("java.lang.invoke.MethodHandleInfo").getMethod("getName", new Class[0]), Class.forName("java.lang.invoke.MethodHandleInfo").getMethod("getDeclaringClass", new Class[0]), Class.forName("java.lang.invoke.MethodHandleInfo").getMethod("getReferenceKind", new Class[0]), Class.forName("java.lang.invoke.MethodHandleInfo").getMethod("getMethodType", new Class[0]), JavaType.METHOD_TYPE.load().getMethod("returnType", new Class[0]), JavaType.METHOD_TYPE.load().getMethod("parameterArray", new Class[0]), JavaType.METHOD_HANDLES_LOOKUP.load().getMethod("lookupClass", new Class[0]), JavaType.METHOD_HANDLES_LOOKUP.load().getMethod("revealDirect", JavaType.METHOD_HANDLE.load()));
                    } catch (Exception e) {
                        try {
                            return new ForJava7CapableVm(Class.forName("java.lang.invoke.MethodHandles").getMethod("publicLookup", new Class[0]), Class.forName("java.lang.invoke.MethodHandleInfo").getMethod("getName", new Class[0]), Class.forName("java.lang.invoke.MethodHandleInfo").getMethod("getDeclaringClass", new Class[0]), Class.forName("java.lang.invoke.MethodHandleInfo").getMethod("getReferenceKind", new Class[0]), Class.forName("java.lang.invoke.MethodHandleInfo").getMethod("getMethodType", new Class[0]), JavaType.METHOD_TYPE.load().getMethod("returnType", new Class[0]), JavaType.METHOD_TYPE.load().getMethod("parameterArray", new Class[0]), JavaType.METHOD_HANDLES_LOOKUP.load().getMethod("lookupClass", new Class[0]), Class.forName("java.lang.invoke.MethodHandleInfo").getConstructor(JavaType.METHOD_HANDLE.load()));
                        } catch (Exception e2) {
                            return ForLegacyVm.INSTANCE;
                        }
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/utility/JavaConstant$MethodHandle$Dispatcher$AbstractBase.class */
            public static abstract class AbstractBase implements Dispatcher, Initializable {
                private static final Object[] NO_ARGUMENTS = new Object[0];
                protected final Method publicLookup;
                protected final Method getName;
                protected final Method getDeclaringClass;
                protected final Method getReferenceKind;
                protected final Method getMethodType;
                protected final Method returnType;
                protected final Method parameterArray;
                protected final Method lookupClass;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.publicLookup.equals(((AbstractBase) obj).publicLookup) && this.getName.equals(((AbstractBase) obj).getName) && this.getDeclaringClass.equals(((AbstractBase) obj).getDeclaringClass) && this.getReferenceKind.equals(((AbstractBase) obj).getReferenceKind) && this.getMethodType.equals(((AbstractBase) obj).getMethodType) && this.returnType.equals(((AbstractBase) obj).returnType) && this.parameterArray.equals(((AbstractBase) obj).parameterArray) && this.lookupClass.equals(((AbstractBase) obj).lookupClass);
                }

                public int hashCode() {
                    return (((((((((((((((17 * 31) + this.publicLookup.hashCode()) * 31) + this.getName.hashCode()) * 31) + this.getDeclaringClass.hashCode()) * 31) + this.getReferenceKind.hashCode()) * 31) + this.getMethodType.hashCode()) * 31) + this.returnType.hashCode()) * 31) + this.parameterArray.hashCode()) * 31) + this.lookupClass.hashCode();
                }

                protected AbstractBase(Method publicLookup, Method getName, Method getDeclaringClass, Method getReferenceKind, Method getMethodType, Method returnType, Method parameterArray, Method lookupClass) {
                    this.publicLookup = publicLookup;
                    this.getName = getName;
                    this.getDeclaringClass = getDeclaringClass;
                    this.getReferenceKind = getReferenceKind;
                    this.getMethodType = getMethodType;
                    this.returnType = returnType;
                    this.parameterArray = parameterArray;
                    this.lookupClass = lookupClass;
                }

                @Override // net.bytebuddy.utility.JavaConstant.MethodHandle.Dispatcher.Initializable
                public Object publicLookup() {
                    try {
                        return this.publicLookup.invoke(null, NO_ARGUMENTS);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.invoke.MethodHandles#publicLookup", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.invoke.MethodHandles#publicLookup", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.utility.JavaConstant.MethodHandle.Dispatcher
                public Object getMethodType(Object methodHandleInfo) {
                    try {
                        return this.getMethodType.invoke(methodHandleInfo, NO_ARGUMENTS);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.invoke.MethodHandleInfo#getMethodType", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.invoke.MethodHandleInfo#getMethodType", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.utility.JavaConstant.MethodHandle.Dispatcher
                public int getReferenceKind(Object methodHandleInfo) {
                    try {
                        return ((Integer) this.getReferenceKind.invoke(methodHandleInfo, NO_ARGUMENTS)).intValue();
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.invoke.MethodHandleInfo#getReferenceKind", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.invoke.MethodHandleInfo#getReferenceKind", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.utility.JavaConstant.MethodHandle.Dispatcher
                public Class<?> getDeclaringClass(Object methodHandleInfo) {
                    try {
                        return (Class) this.getDeclaringClass.invoke(methodHandleInfo, NO_ARGUMENTS);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.invoke.MethodHandleInfo#getDeclaringClass", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.invoke.MethodHandleInfo#getDeclaringClass", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.utility.JavaConstant.MethodHandle.Dispatcher
                public String getName(Object methodHandleInfo) {
                    try {
                        return (String) this.getName.invoke(methodHandleInfo, NO_ARGUMENTS);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.invoke.MethodHandleInfo#getName", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.invoke.MethodHandleInfo#getName", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.utility.JavaConstant.MethodHandle.Dispatcher
                public Class<?> returnType(Object methodType) {
                    try {
                        return (Class) this.returnType.invoke(methodType, NO_ARGUMENTS);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.invoke.MethodType#returnType", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.reflect.MethodType#returnType", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.utility.JavaConstant.MethodHandle.Dispatcher
                public List<? extends Class<?>> parameterArray(Object methodType) {
                    try {
                        return Arrays.asList((Class[]) this.parameterArray.invoke(methodType, NO_ARGUMENTS));
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.reflect.MethodType#parameterArray", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.reflect.MethodType#parameterArray", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.utility.JavaConstant.MethodHandle.Dispatcher.Initializable
                public Class<?> lookupType(Object lookup) {
                    try {
                        return (Class) this.lookupClass.invoke(lookup, NO_ARGUMENTS);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.reflect.MethodHandles.Lookup#lookupClass", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.reflect.MethodHandles.Lookup#lookupClass", exception2.getCause());
                    }
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/utility/JavaConstant$MethodHandle$Dispatcher$ForJava8CapableVm.class */
            public static class ForJava8CapableVm extends AbstractBase {
                private final Method revealDirect;

                @Override // net.bytebuddy.utility.JavaConstant.MethodHandle.Dispatcher.AbstractBase
                public boolean equals(Object obj) {
                    if (super.equals(obj)) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.revealDirect.equals(((ForJava8CapableVm) obj).revealDirect);
                    }
                    return false;
                }

                @Override // net.bytebuddy.utility.JavaConstant.MethodHandle.Dispatcher.AbstractBase
                public int hashCode() {
                    return (super.hashCode() * 31) + this.revealDirect.hashCode();
                }

                protected ForJava8CapableVm(Method publicLookup, Method getName, Method getDeclaringClass, Method getReferenceKind, Method getMethodType, Method returnType, Method parameterArray, Method lookupClass, Method revealDirect) {
                    super(publicLookup, getName, getDeclaringClass, getReferenceKind, getMethodType, returnType, parameterArray, lookupClass);
                    this.revealDirect = revealDirect;
                }

                @Override // net.bytebuddy.utility.JavaConstant.MethodHandle.Dispatcher
                public Object reveal(Object lookup, Object methodHandle) {
                    try {
                        return this.revealDirect.invoke(lookup, methodHandle);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.invoke.MethodHandles.Lookup#revealDirect", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error invoking java.lang.invoke.MethodHandles.Lookup#revealDirect", exception2.getCause());
                    }
                }

                @Override // net.bytebuddy.utility.JavaConstant.MethodHandle.Dispatcher.Initializable
                public Dispatcher initialize() {
                    return this;
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/utility/JavaConstant$MethodHandle$Dispatcher$ForJava7CapableVm.class */
            public static class ForJava7CapableVm extends AbstractBase implements PrivilegedAction<Dispatcher> {
                private final Constructor<?> methodInfo;

                @Override // net.bytebuddy.utility.JavaConstant.MethodHandle.Dispatcher.AbstractBase
                public boolean equals(Object obj) {
                    if (super.equals(obj)) {
                        if (this == obj) {
                            return true;
                        }
                        return obj != null && getClass() == obj.getClass() && this.methodInfo.equals(((ForJava7CapableVm) obj).methodInfo);
                    }
                    return false;
                }

                @Override // net.bytebuddy.utility.JavaConstant.MethodHandle.Dispatcher.AbstractBase
                public int hashCode() {
                    return (super.hashCode() * 31) + this.methodInfo.hashCode();
                }

                protected ForJava7CapableVm(Method publicLookup, Method getName, Method getDeclaringClass, Method getReferenceKind, Method getMethodType, Method returnType, Method parameterArray, Method lookupClass, Constructor<?> methodInfo) {
                    super(publicLookup, getName, getDeclaringClass, getReferenceKind, getMethodType, returnType, parameterArray, lookupClass);
                    this.methodInfo = methodInfo;
                }

                @Override // net.bytebuddy.utility.JavaConstant.MethodHandle.Dispatcher.Initializable
                public Dispatcher initialize() {
                    return (Dispatcher) AccessController.doPrivileged(this);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public Dispatcher run() {
                    this.methodInfo.setAccessible(true);
                    this.getName.setAccessible(true);
                    this.getDeclaringClass.setAccessible(true);
                    this.getReferenceKind.setAccessible(true);
                    this.getMethodType.setAccessible(true);
                    return this;
                }

                @Override // net.bytebuddy.utility.JavaConstant.MethodHandle.Dispatcher
                public Object reveal(Object lookup, Object methodHandle) {
                    try {
                        return this.methodInfo.newInstance(methodHandle);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access java.lang.invoke.MethodInfo()", exception);
                    } catch (InstantiationException exception2) {
                        throw new IllegalStateException("Error constructing java.lang.invoke.MethodInfo", exception2);
                    } catch (InvocationTargetException exception3) {
                        throw new IllegalStateException("Error invoking java.lang.invoke.MethodInfo()", exception3.getCause());
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/utility/JavaConstant$MethodHandle$Dispatcher$ForLegacyVm.class */
            public enum ForLegacyVm implements Initializable {
                INSTANCE;

                @Override // net.bytebuddy.utility.JavaConstant.MethodHandle.Dispatcher.Initializable
                public Dispatcher initialize() {
                    throw new UnsupportedOperationException("Unsupported type on current JVM: java.lang.invoke.MethodHandle");
                }

                @Override // net.bytebuddy.utility.JavaConstant.MethodHandle.Dispatcher.Initializable
                public Object publicLookup() {
                    throw new UnsupportedOperationException("Unsupported type on current JVM: java.lang.invoke.MethodHandle");
                }

                @Override // net.bytebuddy.utility.JavaConstant.MethodHandle.Dispatcher.Initializable
                public Class<?> lookupType(Object lookup) {
                    throw new UnsupportedOperationException("Unsupported type on current JVM: java.lang.invoke.MethodHandle");
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/utility/JavaConstant$MethodHandle$HandleType.class */
        public enum HandleType {
            INVOKE_VIRTUAL(5, false),
            INVOKE_STATIC(6, false),
            INVOKE_SPECIAL(7, false),
            INVOKE_INTERFACE(9, false),
            INVOKE_SPECIAL_CONSTRUCTOR(8, false),
            PUT_FIELD(3, true),
            GET_FIELD(1, true),
            PUT_STATIC_FIELD(4, true),
            GET_STATIC_FIELD(2, true);
            
            private final int identifier;
            private final boolean field;

            HandleType(int identifier, boolean field) {
                this.identifier = identifier;
                this.field = field;
            }

            protected static HandleType of(MethodDescription.InDefinedShape methodDescription) {
                if (methodDescription.isTypeInitializer()) {
                    throw new IllegalArgumentException("Cannot create handle of type initializer " + methodDescription);
                }
                if (methodDescription.isStatic()) {
                    return INVOKE_STATIC;
                }
                if (methodDescription.isConstructor()) {
                    return INVOKE_SPECIAL_CONSTRUCTOR;
                }
                if (methodDescription.isPrivate()) {
                    return INVOKE_SPECIAL;
                }
                if (methodDescription.getDeclaringType().isInterface()) {
                    return INVOKE_INTERFACE;
                }
                return INVOKE_VIRTUAL;
            }

            protected static HandleType of(int identifier) {
                HandleType[] values;
                for (HandleType handleType : values()) {
                    if (handleType.getIdentifier() == identifier) {
                        return handleType;
                    }
                }
                throw new IllegalArgumentException("Unknown handle type: " + identifier);
            }

            protected static HandleType ofSpecial(MethodDescription.InDefinedShape methodDescription) {
                if (methodDescription.isStatic() || methodDescription.isAbstract()) {
                    throw new IllegalArgumentException("Cannot invoke " + methodDescription + " via invokespecial");
                }
                return methodDescription.isConstructor() ? INVOKE_SPECIAL_CONSTRUCTOR : INVOKE_SPECIAL;
            }

            protected static HandleType ofGetter(FieldDescription.InDefinedShape fieldDescription) {
                return fieldDescription.isStatic() ? GET_STATIC_FIELD : GET_FIELD;
            }

            protected static HandleType ofSetter(FieldDescription.InDefinedShape fieldDescription) {
                return fieldDescription.isStatic() ? PUT_STATIC_FIELD : PUT_FIELD;
            }

            public int getIdentifier() {
                return this.identifier;
            }

            public boolean isField() {
                return this.field;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/utility/JavaConstant$Dynamic.class */
    public static class Dynamic implements JavaConstant {
        private static final String CONSTANT_BOOTSTRAPS = "java/lang/invoke/ConstantBootstraps";
        private final ConstantDynamic value;
        private final TypeDescription typeDescription;

        protected Dynamic(ConstantDynamic value, TypeDescription typeDescription) {
            this.value = value;
            this.typeDescription = typeDescription;
        }

        public static Dynamic ofNullConstant() {
            return new Dynamic(new ConstantDynamic("nullConstant", TypeDescription.OBJECT.getDescriptor(), new Handle(6, CONSTANT_BOOTSTRAPS, "nullConstant", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;", false), new Object[0]), TypeDescription.OBJECT);
        }

        public static JavaConstant ofPrimitiveType(Class<?> type) {
            return ofPrimitiveType(TypeDescription.ForLoadedType.of(type));
        }

        public static JavaConstant ofPrimitiveType(TypeDescription typeDescription) {
            if (!typeDescription.isPrimitive()) {
                throw new IllegalArgumentException("Not a primitive type: " + typeDescription);
            }
            return new Dynamic(new ConstantDynamic(typeDescription.getDescriptor(), TypeDescription.CLASS.getDescriptor(), new Handle(6, CONSTANT_BOOTSTRAPS, "primitiveClass", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/Class;", false), new Object[0]), TypeDescription.CLASS);
        }

        public static JavaConstant ofEnumeration(Enum<?> enumeration) {
            return ofEnumeration(new EnumerationDescription.ForLoadedEnumeration(enumeration));
        }

        public static JavaConstant ofEnumeration(EnumerationDescription enumerationDescription) {
            return new Dynamic(new ConstantDynamic(enumerationDescription.getValue(), enumerationDescription.getEnumerationType().getDescriptor(), new Handle(6, CONSTANT_BOOTSTRAPS, "enumConstant", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/Enum;", false), new Object[0]), enumerationDescription.getEnumerationType());
        }

        public static Dynamic ofField(Field field) {
            return ofField(new FieldDescription.ForLoadedField(field));
        }

        public static Dynamic ofField(FieldDescription.InDefinedShape fieldDescription) {
            boolean equals;
            if (!fieldDescription.isStatic() || !fieldDescription.isFinal()) {
                throw new IllegalArgumentException("Field must be static and final: " + fieldDescription);
            }
            if (fieldDescription.getType().isPrimitive()) {
                equals = fieldDescription.getType().asErasure().asBoxed().equals(fieldDescription.getType().asErasure());
            } else {
                equals = fieldDescription.getDeclaringType().equals(fieldDescription.getType().asErasure());
            }
            boolean selfDeclared = equals;
            return new Dynamic(new ConstantDynamic(fieldDescription.getInternalName(), fieldDescription.getDescriptor(), new Handle(6, CONSTANT_BOOTSTRAPS, "getStaticFinal", selfDeclared ? "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;" : "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Class;)Ljava/lang/Object;", false), selfDeclared ? new Object[0] : new Object[]{Type.getType(fieldDescription.getDeclaringType().getDescriptor())}), fieldDescription.getType().asErasure());
        }

        public static Dynamic ofInvocation(Method method, Object... constant) {
            return ofInvocation(method, Arrays.asList(constant));
        }

        public static Dynamic ofInvocation(Method method, List<?> constants) {
            return ofInvocation(new MethodDescription.ForLoadedMethod(method), constants);
        }

        public static Dynamic ofInvocation(Constructor<?> constructor, Object... constant) {
            return ofInvocation(constructor, Arrays.asList(constant));
        }

        public static Dynamic ofInvocation(Constructor<?> constructor, List<?> constants) {
            return ofInvocation(new MethodDescription.ForLoadedConstructor(constructor), constants);
        }

        public static Dynamic ofInvocation(MethodDescription.InDefinedShape methodDescription, Object... constant) {
            return ofInvocation(methodDescription, Arrays.asList(constant));
        }

        public static Dynamic ofInvocation(MethodDescription.InDefinedShape methodDescription, List<?> constants) {
            List asErasures;
            TypeDescription asErasure;
            TypeDescription typeDescription;
            if (methodDescription.isConstructor() || !methodDescription.getReturnType().represents(Void.TYPE)) {
                if (methodDescription.getParameters().size() + ((methodDescription.isStatic() || methodDescription.isConstructor()) ? 0 : 1) != constants.size()) {
                    throw new IllegalArgumentException("Cannot assign " + constants + " to " + methodDescription);
                }
                List<Object> arguments = new ArrayList<>(constants.size());
                arguments.add(new Handle(methodDescription.isConstructor() ? 8 : 6, methodDescription.getDeclaringType().getInternalName(), methodDescription.getInternalName(), methodDescription.getDescriptor(), false));
                if (methodDescription.isStatic() || methodDescription.isConstructor()) {
                    asErasures = methodDescription.getParameters().asTypeList().asErasures();
                } else {
                    asErasures = CompoundList.of(methodDescription.getDeclaringType(), methodDescription.getParameters().asTypeList().asErasures());
                }
                Iterator<TypeDescription> iterator = asErasures.iterator();
                for (Object constant : constants) {
                    if (constant instanceof JavaConstant) {
                        arguments.add(((JavaConstant) constant).asConstantPoolValue());
                        typeDescription = ((JavaConstant) constant).getType();
                    } else if (constant instanceof TypeDescription) {
                        arguments.add(Type.getType(((TypeDescription) constant).getDescriptor()));
                        typeDescription = TypeDescription.CLASS;
                    } else {
                        arguments.add(constant);
                        typeDescription = TypeDescription.ForLoadedType.of(constant.getClass()).asUnboxed();
                        if (JavaType.METHOD_TYPE.isInstance(constant) || JavaType.METHOD_HANDLE.isInstance(constant)) {
                            throw new IllegalArgumentException("Must be represented as a JavaConstant instance: " + constant);
                        }
                        if (constant instanceof Class) {
                            throw new IllegalArgumentException("Must be represented as a TypeDescription instance: " + constant);
                        }
                        if (!typeDescription.isCompileTimeConstant()) {
                            throw new IllegalArgumentException("Not a compile-time constant: " + constant);
                        }
                    }
                    if (!typeDescription.isAssignableTo(iterator.next())) {
                        throw new IllegalArgumentException("Cannot assign " + constants + " to " + methodDescription);
                    }
                }
                if (methodDescription.isConstructor()) {
                    asErasure = methodDescription.getDeclaringType();
                } else {
                    asErasure = methodDescription.getReturnType().asErasure();
                }
                return new Dynamic(new ConstantDynamic("invoke", asErasure.getDescriptor(), new Handle(6, CONSTANT_BOOTSTRAPS, "invoke", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/Class;Ljava/lang/invoke/MethodHandle;[Ljava/lang/Object;)Ljava/lang/Object;", false), arguments.toArray()), methodDescription.isConstructor() ? methodDescription.getDeclaringType() : methodDescription.getReturnType().asErasure());
            }
            throw new IllegalArgumentException("Bootstrap method is no constructor or non-void static factory: " + methodDescription);
        }

        public static JavaConstant ofVarHandle(Field field) {
            return ofVarHandle(new FieldDescription.ForLoadedField(field));
        }

        public static JavaConstant ofVarHandle(FieldDescription.InDefinedShape fieldDescription) {
            return new Dynamic(new ConstantDynamic(fieldDescription.getInternalName(), JavaType.VAR_HANDLE.getTypeStub().getDescriptor(), new Handle(6, CONSTANT_BOOTSTRAPS, fieldDescription.isStatic() ? "staticFieldVarHandle" : "fieldVarHandle", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Class;Ljava/lang/Class;)Ljava/lang/invoke/VarHandle;", false), Type.getType(fieldDescription.getDeclaringType().getDescriptor()), Type.getType(fieldDescription.getType().asErasure().getDescriptor())), JavaType.VAR_HANDLE.getTypeStub());
        }

        public static JavaConstant ofArrayVarHandle(Class<?> type) {
            return ofArrayVarHandle(TypeDescription.ForLoadedType.of(type));
        }

        public static JavaConstant ofArrayVarHandle(TypeDescription typeDescription) {
            if (!typeDescription.isArray()) {
                throw new IllegalArgumentException("Not an array type: " + typeDescription);
            }
            return new Dynamic(new ConstantDynamic("arrayVarHandle", JavaType.VAR_HANDLE.getTypeStub().getDescriptor(), new Handle(6, CONSTANT_BOOTSTRAPS, "arrayVarHandle", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Class;)Ljava/lang/invoke/VarHandle;", false), Type.getType(typeDescription.getDescriptor())), JavaType.VAR_HANDLE.getTypeStub());
        }

        public static Dynamic bootstrap(String name, Method method, Object... constant) {
            return bootstrap(name, method, Arrays.asList(constant));
        }

        public static Dynamic bootstrap(String name, Method method, List<?> constants) {
            return bootstrap(name, new MethodDescription.ForLoadedMethod(method), constants);
        }

        public static Dynamic bootstrap(String name, Constructor<?> constructor, Object... constant) {
            return bootstrap(name, constructor, Arrays.asList(constant));
        }

        public static Dynamic bootstrap(String name, Constructor<?> constructor, List<?> constants) {
            return bootstrap(name, new MethodDescription.ForLoadedConstructor(constructor), constants);
        }

        public static Dynamic bootstrap(String name, MethodDescription.InDefinedShape bootstrapMethod, Object... constant) {
            return bootstrap(name, bootstrapMethod, Arrays.asList(constant));
        }

        public static Dynamic bootstrap(String name, MethodDescription.InDefinedShape bootstrap, List<?> constants) {
            TypeDescription asErasure;
            TypeDescription asErasure2;
            if (name.length() == 0 || name.contains(".")) {
                throw new IllegalArgumentException("Not a valid field name: " + name);
            }
            List<Object> arguments = new ArrayList<>(constants.size());
            List<TypeDescription> types = new ArrayList<>(constants.size());
            for (Object constant : constants) {
                if (constant instanceof JavaConstant) {
                    arguments.add(((JavaConstant) constant).asConstantPoolValue());
                    types.add(((JavaConstant) constant).getType());
                } else if (constant instanceof TypeDescription) {
                    arguments.add(Type.getType(((TypeDescription) constant).getDescriptor()));
                    types.add(TypeDescription.CLASS);
                } else {
                    arguments.add(constant);
                    TypeDescription typeDescription = TypeDescription.ForLoadedType.of(constant.getClass()).asUnboxed();
                    types.add(typeDescription);
                    if (JavaType.METHOD_TYPE.isInstance(constant) || JavaType.METHOD_HANDLE.isInstance(constant)) {
                        throw new IllegalArgumentException("Must be represented as a JavaConstant instance: " + constant);
                    }
                    if (constant instanceof Class) {
                        throw new IllegalArgumentException("Must be represented as a TypeDescription instance: " + constant);
                    }
                    if (!typeDescription.isCompileTimeConstant()) {
                        throw new IllegalArgumentException("Not a compile-time constant: " + constant);
                    }
                }
            }
            if (!bootstrap.isConstantBootstrap(types)) {
                throw new IllegalArgumentException("Not a valid bootstrap method " + bootstrap + " for " + arguments);
            }
            if (bootstrap.isConstructor()) {
                asErasure = bootstrap.getDeclaringType();
            } else {
                asErasure = bootstrap.getReturnType().asErasure();
            }
            ConstantDynamic constantDynamic = new ConstantDynamic(name, asErasure.getDescriptor(), new Handle(bootstrap.isConstructor() ? 8 : 6, bootstrap.getDeclaringType().getInternalName(), bootstrap.getInternalName(), bootstrap.getDescriptor(), false), arguments.toArray());
            if (bootstrap.isConstructor()) {
                asErasure2 = bootstrap.getDeclaringType();
            } else {
                asErasure2 = bootstrap.getReturnType().asErasure();
            }
            return new Dynamic(constantDynamic, asErasure2);
        }

        public JavaConstant withType(Class<?> type) {
            return withType(TypeDescription.ForLoadedType.of(type));
        }

        public JavaConstant withType(TypeDescription typeDescription) {
            if (typeDescription.represents(Void.TYPE)) {
                throw new IllegalArgumentException("Constant value cannot represent void");
            }
            if (!this.value.getBootstrapMethod().getName().equals("<init>") ? !typeDescription.asBoxed().isInHierarchyWith(this.typeDescription.asBoxed()) : !this.typeDescription.isAssignableTo(typeDescription)) {
                throw new IllegalArgumentException(typeDescription + " is not compatible with bootstrapped type " + this.typeDescription);
            }
            Object[] bootstrapMethodArgument = new Object[this.value.getBootstrapMethodArgumentCount()];
            for (int index = 0; index < this.value.getBootstrapMethodArgumentCount(); index++) {
                bootstrapMethodArgument[index] = this.value.getBootstrapMethodArgument(index);
            }
            return new Dynamic(new ConstantDynamic(this.value.getName(), typeDescription.getDescriptor(), this.value.getBootstrapMethod(), bootstrapMethodArgument), typeDescription);
        }

        @Override // net.bytebuddy.utility.JavaConstant
        public Object asConstantPoolValue() {
            return this.value;
        }

        @Override // net.bytebuddy.utility.JavaConstant
        public TypeDescription getType() {
            return this.typeDescription;
        }

        public int hashCode() {
            int result = this.value.hashCode();
            return (31 * result) + this.typeDescription.hashCode();
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || getClass() != other.getClass()) {
                return false;
            }
            Dynamic dynamic = (Dynamic) other;
            return this.value.equals(dynamic.value) && this.typeDescription.equals(dynamic.typeDescription);
        }
    }
}
