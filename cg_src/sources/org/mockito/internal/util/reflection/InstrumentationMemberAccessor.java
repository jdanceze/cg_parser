package org.mockito.internal.util.reflection;

import android.provider.ContactsContract;
import java.lang.instrument.Instrumentation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.matcher.ElementMatchers;
import org.mockito.exceptions.base.MockitoInitializationException;
import org.mockito.internal.SuppressSignatureCheck;
import org.mockito.internal.util.StringUtil;
import org.mockito.plugins.MemberAccessor;
@SuppressSignatureCheck
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/reflection/InstrumentationMemberAccessor.class */
class InstrumentationMemberAccessor implements MemberAccessor {
    private static final Map<Class<?>, Class<?>> WRAPPERS = new HashMap();
    private static final Instrumentation INSTRUMENTATION;
    private static final Dispatcher DISPATCHER;
    private static final Throwable INITIALIZATION_ERROR;
    private final MethodHandle getModule;
    private final MethodHandle isOpen;
    private final MethodHandle redefineModule;
    private final MethodHandle privateLookupIn;

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/reflection/InstrumentationMemberAccessor$Dispatcher.class */
    public interface Dispatcher {
        MethodHandles.Lookup getLookup();

        Object getModule();

        void setAccessible(AccessibleObject accessibleObject, boolean z);

        Object invokeWithArguments(MethodHandle methodHandle, Object... objArr) throws Throwable;
    }

    static {
        Instrumentation instrumentation;
        Dispatcher dispatcher;
        Throwable throwable;
        WRAPPERS.put(Boolean.TYPE, Boolean.class);
        WRAPPERS.put(Byte.TYPE, Byte.class);
        WRAPPERS.put(Short.TYPE, Short.class);
        WRAPPERS.put(Character.TYPE, Character.class);
        WRAPPERS.put(Integer.TYPE, Integer.class);
        WRAPPERS.put(Long.TYPE, Long.class);
        WRAPPERS.put(Float.TYPE, Float.class);
        WRAPPERS.put(Double.TYPE, Double.class);
        try {
            instrumentation = ByteBuddyAgent.install();
            dispatcher = (Dispatcher) new ByteBuddy().subclass(Dispatcher.class).method(ElementMatchers.named("getLookup")).intercept(MethodCall.invoke(MethodHandles.class.getMethod(ContactsContract.ContactsColumns.LOOKUP_KEY, new Class[0]))).method(ElementMatchers.named("getModule")).intercept(MethodCall.invoke(Class.class.getMethod("getModule", new Class[0])).onMethodCall(MethodCall.invoke(Object.class.getMethod("getClass", new Class[0])))).method(ElementMatchers.named("setAccessible")).intercept(MethodCall.invoke(AccessibleObject.class.getMethod("setAccessible", Boolean.TYPE)).onArgument(0).withArgument(1)).method(ElementMatchers.named("invokeWithArguments")).intercept(MethodCall.invoke(MethodHandle.class.getMethod("invokeWithArguments", Object[].class)).onArgument(0).withArgument(1)).make().load(InstrumentationMemberAccessor.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER).getLoaded().getConstructor(new Class[0]).newInstance(new Object[0]);
            throwable = null;
        } catch (Throwable t) {
            instrumentation = null;
            dispatcher = null;
            throwable = t;
        }
        INSTRUMENTATION = instrumentation;
        DISPATCHER = dispatcher;
        INITIALIZATION_ERROR = throwable;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InstrumentationMemberAccessor() {
        if (INITIALIZATION_ERROR != null) {
            throw new MockitoInitializationException(StringUtil.join("Could not initialize the Mockito instrumentation member accessor", "", "This is unexpected on JVMs from Java 9 or later - possibly, the instrumentation API could not be resolved"), INITIALIZATION_ERROR);
        }
        try {
            Class<?> module = Class.forName("java.lang.Module");
            this.getModule = MethodHandles.publicLookup().findVirtual(Class.class, "getModule", MethodType.methodType(module));
            this.isOpen = MethodHandles.publicLookup().findVirtual(module, "isOpen", MethodType.methodType(Boolean.TYPE, String.class));
            this.redefineModule = MethodHandles.publicLookup().findVirtual(Instrumentation.class, "redefineModule", MethodType.methodType(Void.TYPE, module, Set.class, Map.class, Map.class, Set.class, Map.class));
            this.privateLookupIn = MethodHandles.publicLookup().findStatic(MethodHandles.class, "privateLookupIn", MethodType.methodType(MethodHandles.Lookup.class, Class.class, MethodHandles.Lookup.class));
        } catch (Throwable t) {
            throw new MockitoInitializationException("Could not resolve instrumentation invoker", t);
        }
    }

    @Override // org.mockito.plugins.MemberAccessor
    public Object newInstance(Constructor<?> constructor, Object... arguments) throws InstantiationException, InvocationTargetException {
        return newInstance(constructor, (v0) -> {
            return v0.newInstance();
        }, arguments);
    }

    @Override // org.mockito.plugins.MemberAccessor
    public Object newInstance(Constructor<?> constructor, MemberAccessor.OnConstruction onConstruction, Object... arguments) throws InstantiationException, InvocationTargetException {
        if (Modifier.isAbstract(constructor.getDeclaringClass().getModifiers())) {
            throw new InstantiationException("Cannot instantiate abstract " + constructor.getDeclaringClass().getTypeName());
        }
        assureArguments(constructor, null, null, arguments, constructor.getParameterTypes());
        try {
            Object module = DISPATCHER.invokeWithArguments(this.getModule.bindTo(constructor.getDeclaringClass()), new Object[0]);
            String packageName = constructor.getDeclaringClass().getPackage().getName();
            assureOpen(module, packageName);
            MethodHandle handle = ((MethodHandles.Lookup) DISPATCHER.invokeWithArguments(this.privateLookupIn, constructor.getDeclaringClass(), DISPATCHER.getLookup())).unreflectConstructor(constructor);
            AtomicBoolean thrown = new AtomicBoolean();
            Object value = onConstruction.invoke(() -> {
                try {
                    return DISPATCHER.invokeWithArguments(handle, arguments);
                } catch (Throwable throwable) {
                    thrown.set(true);
                    return throwable;
                }
            });
            if (thrown.get()) {
                throw new InvocationTargetException((Throwable) value);
            }
            return value;
        } catch (InvocationTargetException e) {
            throw e;
        } catch (Throwable t) {
            throw new IllegalStateException("Could not construct " + constructor + " with arguments " + Arrays.toString(arguments), t);
        }
    }

    @Override // org.mockito.plugins.MemberAccessor
    public Object invoke(Method method, Object target, Object... arguments) throws InvocationTargetException {
        InvocationTargetException invocationTargetException;
        assureArguments(method, Modifier.isStatic(method.getModifiers()) ? null : target, method.getDeclaringClass(), arguments, method.getParameterTypes());
        try {
            Object module = DISPATCHER.invokeWithArguments(this.getModule.bindTo(method.getDeclaringClass()), new Object[0]);
            String packageName = method.getDeclaringClass().getPackage().getName();
            assureOpen(module, packageName);
            MethodHandle handle = ((MethodHandles.Lookup) DISPATCHER.invokeWithArguments(this.privateLookupIn, method.getDeclaringClass(), DISPATCHER.getLookup())).unreflect(method);
            if (!Modifier.isStatic(method.getModifiers())) {
                handle = handle.bindTo(target);
            }
            try {
                return DISPATCHER.invokeWithArguments(handle, arguments);
            } finally {
            }
        } catch (InvocationTargetException e) {
            throw e;
        } catch (Throwable t) {
            throw new IllegalStateException("Could not invoke " + method + " on " + target + " with arguments " + Arrays.toString(arguments), t);
        }
    }

    @Override // org.mockito.plugins.MemberAccessor
    public Object get(Field field, Object target) {
        assureArguments(field, Modifier.isStatic(field.getModifiers()) ? null : target, field.getDeclaringClass(), new Object[0], new Class[0]);
        try {
            Object module = DISPATCHER.invokeWithArguments(this.getModule.bindTo(field.getDeclaringClass()), new Object[0]);
            String packageName = field.getDeclaringClass().getPackage().getName();
            assureOpen(module, packageName);
            MethodHandle handle = ((MethodHandles.Lookup) DISPATCHER.invokeWithArguments(this.privateLookupIn, field.getDeclaringClass(), DISPATCHER.getLookup())).unreflectGetter(field);
            if (!Modifier.isStatic(field.getModifiers())) {
                handle = handle.bindTo(target);
            }
            return DISPATCHER.invokeWithArguments(handle, new Object[0]);
        } catch (Throwable t) {
            throw new IllegalStateException("Could not read " + field + " on " + target, t);
        }
    }

    @Override // org.mockito.plugins.MemberAccessor
    public void set(Field field, Object target, Object value) throws IllegalAccessException {
        boolean isFinal;
        assureArguments(field, Modifier.isStatic(field.getModifiers()) ? null : target, field.getDeclaringClass(), new Object[]{value}, new Class[]{field.getType()});
        try {
            Object module = DISPATCHER.invokeWithArguments(this.getModule.bindTo(field.getDeclaringClass()), new Object[0]);
            String packageName = field.getDeclaringClass().getPackage().getName();
            assureOpen(module, packageName);
            if (Modifier.isFinal(field.getModifiers())) {
                isFinal = true;
                DISPATCHER.setAccessible(field, true);
            } else {
                isFinal = false;
            }
            MethodHandle handle = ((MethodHandles.Lookup) DISPATCHER.invokeWithArguments(this.privateLookupIn, field.getDeclaringClass(), DISPATCHER.getLookup())).unreflectSetter(field);
            if (!Modifier.isStatic(field.getModifiers())) {
                handle = handle.bindTo(target);
            }
            DISPATCHER.invokeWithArguments(handle, value);
            if (isFinal) {
                DISPATCHER.setAccessible(field, false);
            }
        } catch (Throwable t) {
            if (0 != 0) {
                throw ((IllegalAccessException) t);
            }
            throw new IllegalStateException("Could not read " + field + " on " + target, t);
        }
    }

    private void assureOpen(Object module, String packageName) throws Throwable {
        if (!((Boolean) DISPATCHER.invokeWithArguments(this.isOpen, module, packageName)).booleanValue()) {
            DISPATCHER.invokeWithArguments(this.redefineModule.bindTo(INSTRUMENTATION), module, Collections.emptySet(), Collections.emptyMap(), Collections.singletonMap(packageName, Collections.singleton(DISPATCHER.getModule())), Collections.emptySet(), Collections.emptyMap());
        }
    }

    private static void assureArguments(AccessibleObject target, Object owner, Class<?> type, Object[] values, Class<?>[] types) {
        if (owner != null && !type.isAssignableFrom(owner.getClass())) {
            throw new IllegalArgumentException("Cannot access " + target + " on " + owner);
        }
        if (types.length != values.length) {
            throw new IllegalArgumentException("Incorrect number of arguments for " + target + ": expected " + types.length + " but recevied " + values.length);
        }
        for (int index = 0; index < values.length; index++) {
            if (values[index] == null) {
                if (types[index].isPrimitive()) {
                    throw new IllegalArgumentException("Cannot assign null to primitive type " + types[index].getTypeName() + " for " + index + " parameter of " + target);
                }
            } else {
                Class<?> resolved = WRAPPERS.getOrDefault(types[index], types[index]);
                if (!resolved.isAssignableFrom(values[index].getClass())) {
                    throw new IllegalArgumentException("Cannot assign value of type " + values[index].getClass() + " to " + resolved + " for " + index + " parameter of " + target);
                }
            }
        }
    }
}
