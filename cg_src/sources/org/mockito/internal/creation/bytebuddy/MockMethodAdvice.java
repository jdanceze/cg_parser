package org.mockito.internal.creation.bytebuddy;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.SoftReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.field.FieldList;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.This;
import net.bytebuddy.implementation.bytecode.StackSize;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import net.bytebuddy.jar.asm.Label;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.jar.asm.Opcodes;
import net.bytebuddy.jar.asm.Type;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.pool.TypePool;
import net.bytebuddy.utility.OpenedClassReader;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.creation.bytebuddy.inject.MockMethodDispatcher;
import org.mockito.internal.debugging.LocationImpl;
import org.mockito.internal.exceptions.stacktrace.ConditionalStackTraceFilter;
import org.mockito.internal.invocation.RealMethod;
import org.mockito.internal.invocation.SerializableMethod;
import org.mockito.internal.invocation.mockref.MockReference;
import org.mockito.internal.invocation.mockref.MockWeakReference;
import org.mockito.internal.util.concurrent.DetachedThreadLocal;
import org.mockito.internal.util.concurrent.WeakConcurrentMap;
import org.mockito.plugins.MemberAccessor;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/MockMethodAdvice.class */
public class MockMethodAdvice extends MockMethodDispatcher {
    private final WeakConcurrentMap<Object, MockMethodInterceptor> interceptors;
    private final DetachedThreadLocal<Map<Class<?>, MockMethodInterceptor>> mockedStatics;
    private final String identifier;
    private final SelfCallInfo selfCallInfo = new SelfCallInfo();
    private final MethodGraph.Compiler compiler = MethodGraph.Compiler.Default.forJavaHierarchy();
    private final WeakConcurrentMap<Class<?>, SoftReference<MethodGraph>> graphs = new WeakConcurrentMap.WithInlinedExpunction();
    private final Predicate<Class<?>> isMockConstruction;
    private final ConstructionCallback onConstruction;

    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/MockMethodAdvice$Identifier.class */
    @interface Identifier {
    }

    public MockMethodAdvice(WeakConcurrentMap<Object, MockMethodInterceptor> interceptors, DetachedThreadLocal<Map<Class<?>, MockMethodInterceptor>> mockedStatics, String identifier, Predicate<Class<?>> isMockConstruction, ConstructionCallback onConstruction) {
        this.interceptors = interceptors;
        this.mockedStatics = mockedStatics;
        this.onConstruction = onConstruction;
        this.identifier = identifier;
        this.isMockConstruction = isMockConstruction;
    }

    @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
    private static Callable<?> enter(@Identifier String identifier, @Advice.This Object mock, @Advice.Origin Method origin, @Advice.AllArguments Object[] arguments) throws Throwable {
        MockMethodDispatcher dispatcher = MockMethodDispatcher.get(identifier, mock);
        if (dispatcher == null || !dispatcher.isMocked(mock) || dispatcher.isOverridden(mock, origin)) {
            return null;
        }
        return dispatcher.handle(mock, origin, arguments);
    }

    @Advice.OnMethodExit
    private static void exit(@Advice.Return(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object returned, @Advice.Enter Callable<?> mocked) throws Throwable {
        if (mocked != null) {
            mocked.call();
        }
    }

    static Throwable hideRecursiveCall(Throwable throwable, int current, Class<?> targetType) {
        StackTraceElement next;
        try {
            StackTraceElement[] stack = throwable.getStackTrace();
            int skip = 0;
            do {
                skip++;
                next = stack[(stack.length - current) - skip];
            } while (!next.getClassName().equals(targetType.getName()));
            int top = (stack.length - current) - skip;
            StackTraceElement[] cleared = new StackTraceElement[stack.length - skip];
            System.arraycopy(stack, 0, cleared, 0, top);
            System.arraycopy(stack, top + skip, cleared, top, current);
            throwable.setStackTrace(cleared);
            return throwable;
        } catch (RuntimeException e) {
            return throwable;
        }
    }

    @Override // org.mockito.internal.creation.bytebuddy.inject.MockMethodDispatcher
    public Callable<?> handle(Object instance, Method origin, Object[] arguments) throws Throwable {
        RealMethod realMethod;
        MockMethodInterceptor interceptor = this.interceptors.get(instance);
        if (interceptor == null) {
            return null;
        }
        if (instance instanceof Serializable) {
            realMethod = new SerializableRealMethodCall(this.identifier, origin, instance, arguments);
        } else {
            realMethod = new RealMethodCall(this.selfCallInfo, origin, instance, arguments);
        }
        return new ReturnValueWrapper(interceptor.doIntercept(instance, origin, arguments, realMethod, new LocationImpl(new Throwable(), true)));
    }

    @Override // org.mockito.internal.creation.bytebuddy.inject.MockMethodDispatcher
    public Callable<?> handleStatic(Class<?> type, Method origin, Object[] arguments) throws Throwable {
        Map<Class<?>, MockMethodInterceptor> interceptors = this.mockedStatics.get();
        if (interceptors == null || !interceptors.containsKey(type)) {
            return null;
        }
        return new ReturnValueWrapper(interceptors.get(type).doIntercept(type, origin, arguments, new StaticMethodCall(this.selfCallInfo, type, origin, arguments), new LocationImpl(new Throwable(), true)));
    }

    @Override // org.mockito.internal.creation.bytebuddy.inject.MockMethodDispatcher
    public Object handleConstruction(Class<?> type, Object object, Object[] arguments, String[] parameterTypeNames) {
        return this.onConstruction.apply(type, object, arguments, parameterTypeNames);
    }

    @Override // org.mockito.internal.creation.bytebuddy.inject.MockMethodDispatcher
    public boolean isMock(Object instance) {
        return instance != this.interceptors.target && this.interceptors.containsKey(instance);
    }

    @Override // org.mockito.internal.creation.bytebuddy.inject.MockMethodDispatcher
    public boolean isMocked(Object instance) {
        return this.selfCallInfo.checkSelfCall(instance) && isMock(instance);
    }

    @Override // org.mockito.internal.creation.bytebuddy.inject.MockMethodDispatcher
    public boolean isMockedStatic(Class<?> type) {
        Map<Class<?>, ?> interceptors;
        return this.selfCallInfo.checkSelfCall(type) && (interceptors = this.mockedStatics.get()) != null && interceptors.containsKey(type);
    }

    @Override // org.mockito.internal.creation.bytebuddy.inject.MockMethodDispatcher
    public boolean isOverridden(Object instance, Method origin) {
        SoftReference<MethodGraph> reference = this.graphs.get(instance.getClass());
        MethodGraph methodGraph = reference == null ? null : reference.get();
        if (methodGraph == null) {
            methodGraph = this.compiler.compile(new TypeDescription.ForLoadedType(instance.getClass()));
            this.graphs.put(instance.getClass(), new SoftReference<>(methodGraph));
        }
        MethodGraph.Node node = methodGraph.locate(new MethodDescription.ForLoadedMethod(origin).asSignatureToken());
        return (node.getSort().isResolved() && node.getRepresentative().asDefined().getDeclaringType().represents(origin.getDeclaringClass())) ? false : true;
    }

    @Override // org.mockito.internal.creation.bytebuddy.inject.MockMethodDispatcher
    public boolean isConstructorMock(Class<?> type) {
        return this.isMockConstruction.test(type);
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/MockMethodAdvice$RealMethodCall.class */
    private static class RealMethodCall implements RealMethod {
        private final SelfCallInfo selfCallInfo;
        private final Method origin;
        private final MockWeakReference<Object> instanceRef;
        private final Object[] arguments;

        private RealMethodCall(SelfCallInfo selfCallInfo, Method origin, Object instance, Object[] arguments) {
            this.selfCallInfo = selfCallInfo;
            this.origin = origin;
            this.instanceRef = new MockWeakReference<>(instance);
            this.arguments = arguments;
        }

        @Override // org.mockito.internal.invocation.RealMethod
        public boolean isInvokable() {
            return true;
        }

        @Override // org.mockito.internal.invocation.RealMethod
        public Object invoke() throws Throwable {
            this.selfCallInfo.set(this.instanceRef.get());
            return MockMethodAdvice.tryInvoke(this.origin, this.instanceRef.get(), this.arguments);
        }
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/MockMethodAdvice$SerializableRealMethodCall.class */
    private static class SerializableRealMethodCall implements RealMethod {
        private final String identifier;
        private final SerializableMethod origin;
        private final MockReference<Object> instanceRef;
        private final Object[] arguments;

        private SerializableRealMethodCall(String identifier, Method origin, Object instance, Object[] arguments) {
            this.origin = new SerializableMethod(origin);
            this.identifier = identifier;
            this.instanceRef = new MockWeakReference(instance);
            this.arguments = arguments;
        }

        @Override // org.mockito.internal.invocation.RealMethod
        public boolean isInvokable() {
            return true;
        }

        @Override // org.mockito.internal.invocation.RealMethod
        public Object invoke() throws Throwable {
            Method method = this.origin.getJavaMethod();
            MockMethodDispatcher mockMethodDispatcher = MockMethodDispatcher.get(this.identifier, this.instanceRef.get());
            if (mockMethodDispatcher instanceof MockMethodAdvice) {
                Object previous = ((MockMethodAdvice) mockMethodDispatcher).selfCallInfo.replace(this.instanceRef.get());
                try {
                    Object tryInvoke = MockMethodAdvice.tryInvoke(method, this.instanceRef.get(), this.arguments);
                    ((MockMethodAdvice) mockMethodDispatcher).selfCallInfo.set(previous);
                    return tryInvoke;
                } catch (Throwable th) {
                    ((MockMethodAdvice) mockMethodDispatcher).selfCallInfo.set(previous);
                    throw th;
                }
            }
            throw new MockitoException("Unexpected dispatcher for advice-based super call");
        }
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/MockMethodAdvice$StaticMethodCall.class */
    private static class StaticMethodCall implements RealMethod {
        private final SelfCallInfo selfCallInfo;
        private final Class<?> type;
        private final Method origin;
        private final Object[] arguments;

        private StaticMethodCall(SelfCallInfo selfCallInfo, Class<?> type, Method origin, Object[] arguments) {
            this.selfCallInfo = selfCallInfo;
            this.type = type;
            this.origin = origin;
            this.arguments = arguments;
        }

        @Override // org.mockito.internal.invocation.RealMethod
        public boolean isInvokable() {
            return true;
        }

        @Override // org.mockito.internal.invocation.RealMethod
        public Object invoke() throws Throwable {
            this.selfCallInfo.set(this.type);
            return MockMethodAdvice.tryInvoke(this.origin, null, this.arguments);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Object tryInvoke(Method origin, Object instance, Object[] arguments) throws Throwable {
        MemberAccessor accessor = Plugins.getMemberAccessor();
        try {
            return accessor.invoke(origin, instance, arguments);
        } catch (InvocationTargetException exception) {
            Throwable cause = exception.getCause();
            new ConditionalStackTraceFilter().filter(hideRecursiveCall(cause, new Throwable().getStackTrace().length, origin.getDeclaringClass()));
            throw cause;
        }
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/MockMethodAdvice$ReturnValueWrapper.class */
    private static class ReturnValueWrapper implements Callable<Object> {
        private final Object returned;

        private ReturnValueWrapper(Object returned) {
            this.returned = returned;
        }

        @Override // java.util.concurrent.Callable
        public Object call() {
            return this.returned;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/MockMethodAdvice$SelfCallInfo.class */
    public static class SelfCallInfo extends ThreadLocal<Object> {
        private SelfCallInfo() {
        }

        Object replace(Object value) {
            Object current = get();
            set(value);
            return current;
        }

        boolean checkSelfCall(Object value) {
            if (value == get()) {
                set(null);
                return false;
            }
            return true;
        }
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/MockMethodAdvice$ConstructorShortcut.class */
    static class ConstructorShortcut implements AsmVisitorWrapper.ForDeclaredMethods.MethodVisitorWrapper {
        private final String identifier;

        /* JADX INFO: Access modifiers changed from: package-private */
        public ConstructorShortcut(String identifier) {
            this.identifier = identifier;
        }

        @Override // net.bytebuddy.asm.AsmVisitorWrapper.ForDeclaredMethods.MethodVisitorWrapper
        public MethodVisitor wrap(final TypeDescription instrumentedType, final MethodDescription instrumentedMethod, MethodVisitor methodVisitor, final Implementation.Context implementationContext, TypePool typePool, int writerFlags, int readerFlags) {
            if (instrumentedMethod.isConstructor() && !instrumentedType.represents(Object.class)) {
                MethodList<MethodDescription.InDefinedShape> constructors = instrumentedType.getSuperClass().asErasure().getDeclaredMethods().filter(ElementMatchers.isConstructor().and(ElementMatchers.not(ElementMatchers.isPrivate())));
                int arguments = Integer.MAX_VALUE;
                boolean packagePrivate = true;
                MethodDescription.InDefinedShape current = null;
                for (MethodDescription.InDefinedShape constructor : constructors) {
                    if (constructor.getParameters().size() < arguments && (packagePrivate || !constructor.isPackagePrivate())) {
                        arguments = constructor.getParameters().size();
                        packagePrivate = constructor.isPackagePrivate();
                        current = constructor;
                    }
                }
                if (current != null) {
                    final MethodDescription.InDefinedShape selected = current;
                    return new MethodVisitor(OpenedClassReader.ASM_API, methodVisitor) { // from class: org.mockito.internal.creation.bytebuddy.MockMethodAdvice.ConstructorShortcut.1
                        @Override // net.bytebuddy.jar.asm.MethodVisitor
                        public void visitCode() {
                            int i;
                            super.visitCode();
                            Label label = new Label();
                            super.visitLdcInsn(ConstructorShortcut.this.identifier);
                            if (implementationContext.getClassFileVersion().isAtLeast(ClassFileVersion.JAVA_V5)) {
                                super.visitLdcInsn(Type.getType(instrumentedType.getDescriptor()));
                            } else {
                                super.visitLdcInsn(instrumentedType.getName());
                                super.visitMethodInsn(184, Type.getInternalName(Class.class), "forName", Type.getMethodDescriptor(Type.getType(Class.class), Type.getType(String.class)), false);
                            }
                            super.visitMethodInsn(184, Type.getInternalName(MockMethodDispatcher.class), "isConstructorMock", Type.getMethodDescriptor(Type.BOOLEAN_TYPE, Type.getType(String.class), Type.getType(Class.class)), false);
                            super.visitInsn(3);
                            super.visitJumpInsn(159, label);
                            super.visitVarInsn(25, 0);
                            for (TypeDescription type : selected.getParameters().asTypeList().asErasures()) {
                                if (type.represents(Boolean.TYPE) || type.represents(Byte.TYPE) || type.represents(Short.TYPE) || type.represents(Character.TYPE) || type.represents(Integer.TYPE)) {
                                    super.visitInsn(3);
                                } else if (type.represents(Long.TYPE)) {
                                    super.visitInsn(9);
                                } else if (type.represents(Float.TYPE)) {
                                    super.visitInsn(11);
                                } else if (type.represents(Double.TYPE)) {
                                    super.visitInsn(14);
                                } else {
                                    super.visitInsn(1);
                                }
                            }
                            super.visitMethodInsn(183, selected.getDeclaringType().getInternalName(), selected.getInternalName(), selected.getDescriptor(), false);
                            super.visitLdcInsn(ConstructorShortcut.this.identifier);
                            if (implementationContext.getClassFileVersion().isAtLeast(ClassFileVersion.JAVA_V5)) {
                                super.visitLdcInsn(Type.getType(instrumentedType.getDescriptor()));
                            } else {
                                super.visitLdcInsn(instrumentedType.getName());
                                super.visitMethodInsn(184, Type.getInternalName(Class.class), "forName", Type.getMethodDescriptor(Type.getType(Class.class), Type.getType(String.class)), false);
                            }
                            super.visitVarInsn(25, 0);
                            super.visitLdcInsn(Integer.valueOf(instrumentedMethod.getParameters().size()));
                            super.visitTypeInsn(189, Type.getInternalName(Object.class));
                            int index = 0;
                            Iterator it = instrumentedMethod.getParameters().iterator();
                            while (it.hasNext()) {
                                ParameterDescription parameter = (ParameterDescription) it.next();
                                super.visitInsn(89);
                                int i2 = index;
                                index++;
                                super.visitLdcInsn(Integer.valueOf(i2));
                                Type type2 = Type.getType(parameter.getType().asErasure().getDescriptor());
                                super.visitVarInsn(type2.getOpcode(21), parameter.getOffset());
                                if (parameter.getType().isPrimitive()) {
                                    Type wrapper = Type.getType(parameter.getType().asErasure().asBoxed().getDescriptor());
                                    super.visitMethodInsn(184, wrapper.getInternalName(), "valueOf", Type.getMethodDescriptor(wrapper, type2), false);
                                }
                                super.visitInsn(83);
                            }
                            int index2 = 0;
                            super.visitLdcInsn(Integer.valueOf(instrumentedMethod.getParameters().size()));
                            super.visitTypeInsn(189, Type.getInternalName(String.class));
                            for (TypeDescription typeDescription : instrumentedMethod.getParameters().asTypeList().asErasures()) {
                                super.visitInsn(89);
                                int i3 = index2;
                                index2++;
                                super.visitLdcInsn(Integer.valueOf(i3));
                                super.visitLdcInsn(typeDescription.getName());
                                super.visitInsn(83);
                            }
                            super.visitMethodInsn(184, Type.getInternalName(MockMethodDispatcher.class), "handleConstruction", Type.getMethodDescriptor(Type.getType(Object.class), Type.getType(String.class), Type.getType(Class.class), Type.getType(Object.class), Type.getType(Object[].class), Type.getType(String[].class)), false);
                            FieldList<FieldDescription> fields = instrumentedType.getDeclaredFields().filter(ElementMatchers.not(ElementMatchers.isStatic()));
                            super.visitTypeInsn(192, instrumentedType.getInternalName());
                            super.visitInsn(89);
                            Label noSpy = new Label();
                            super.visitJumpInsn(198, noSpy);
                            for (FieldDescription field : fields) {
                                super.visitInsn(89);
                                super.visitFieldInsn(180, instrumentedType.getInternalName(), field.getInternalName(), field.getDescriptor());
                                super.visitVarInsn(25, 0);
                                if (field.getType().getStackSize() == StackSize.DOUBLE) {
                                    i = 91;
                                } else {
                                    i = 90;
                                }
                                super.visitInsn(i);
                                super.visitInsn(87);
                                super.visitFieldInsn(181, instrumentedType.getInternalName(), field.getInternalName(), field.getDescriptor());
                            }
                            super.visitLabel(noSpy);
                            if (implementationContext.getClassFileVersion().isAtLeast(ClassFileVersion.JAVA_V6)) {
                                Object[] locals = ConstructorShortcut.toFrames(instrumentedType.getInternalName(), instrumentedMethod.getParameters().asTypeList().asErasures());
                                super.visitFrame(0, locals.length, locals, 1, new Object[]{instrumentedType.getInternalName()});
                            }
                            super.visitInsn(87);
                            super.visitInsn(177);
                            super.visitLabel(label);
                            if (implementationContext.getClassFileVersion().isAtLeast(ClassFileVersion.JAVA_V6)) {
                                Object[] locals2 = ConstructorShortcut.toFrames(Opcodes.UNINITIALIZED_THIS, instrumentedMethod.getParameters().asTypeList().asErasures());
                                super.visitFrame(0, locals2.length, locals2, 0, new Object[0]);
                            }
                        }

                        @Override // net.bytebuddy.jar.asm.MethodVisitor
                        public void visitMaxs(int maxStack, int maxLocals) {
                            int prequel = Math.max(5, selected.getStackSize());
                            Iterator it = instrumentedMethod.getParameters().iterator();
                            while (it.hasNext()) {
                                ParameterDescription parameter = (ParameterDescription) it.next();
                                prequel = Math.max(Math.max(prequel, 6 + parameter.getType().getStackSize().getSize()), 8);
                            }
                            super.visitMaxs(Math.max(maxStack, prequel), maxLocals);
                        }
                    };
                }
            }
            return methodVisitor;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static Object[] toFrames(Object self, List<TypeDescription> types) {
            Object obj;
            Object[] frames = new Object[1 + types.size()];
            frames[0] = self;
            int index = 0;
            for (TypeDescription type : types) {
                if (type.represents(Boolean.TYPE) || type.represents(Byte.TYPE) || type.represents(Short.TYPE) || type.represents(Character.TYPE) || type.represents(Integer.TYPE)) {
                    obj = Opcodes.INTEGER;
                } else if (type.represents(Long.TYPE)) {
                    obj = Opcodes.LONG;
                } else if (type.represents(Float.TYPE)) {
                    obj = Opcodes.FLOAT;
                } else if (type.represents(Double.TYPE)) {
                    obj = Opcodes.DOUBLE;
                } else {
                    obj = type.getInternalName();
                }
                Object frame = obj;
                index++;
                frames[index] = frame;
            }
            return frames;
        }
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/MockMethodAdvice$ForHashCode.class */
    static class ForHashCode {
        ForHashCode() {
        }

        @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
        private static boolean enter(@Identifier String id, @Advice.This Object self) {
            MockMethodDispatcher dispatcher = MockMethodDispatcher.get(id, self);
            return dispatcher != null && dispatcher.isMock(self);
        }

        @Advice.OnMethodExit
        private static void enter(@Advice.This Object self, @Advice.Return(readOnly = false) int hashCode, @Advice.Enter boolean skipped) {
            if (skipped) {
                System.identityHashCode(self);
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/MockMethodAdvice$ForEquals.class */
    static class ForEquals {
        ForEquals() {
        }

        @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
        private static boolean enter(@Identifier String identifier, @Advice.This Object self) {
            MockMethodDispatcher dispatcher = MockMethodDispatcher.get(identifier, self);
            return dispatcher != null && dispatcher.isMock(self);
        }

        @Advice.OnMethodExit
        private static void enter(@Advice.This Object self, @Advice.Argument(0) Object other, @Advice.Return(readOnly = false) boolean equals, @Advice.Enter boolean skipped) {
            if (skipped) {
                boolean z = self == other;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/MockMethodAdvice$ForStatic.class */
    static class ForStatic {
        ForStatic() {
        }

        @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
        private static Callable<?> enter(@Identifier String identifier, @Advice.Origin Class<?> type, @Advice.Origin Method origin, @Advice.AllArguments Object[] arguments) throws Throwable {
            MockMethodDispatcher dispatcher = MockMethodDispatcher.getStatic(identifier, type);
            if (dispatcher == null || !dispatcher.isMockedStatic(type)) {
                return null;
            }
            return dispatcher.handleStatic(type, origin, arguments);
        }

        @Advice.OnMethodExit
        private static void exit(@Advice.Return(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object returned, @Advice.Enter Callable<?> mocked) throws Throwable {
            if (mocked != null) {
                mocked.call();
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/MockMethodAdvice$ForReadObject.class */
    public static class ForReadObject {
        public static void doReadObject(@Identifier String identifier, @This MockAccess thiz, @Argument(0) ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            objectInputStream.defaultReadObject();
            MockMethodAdvice mockMethodAdvice = (MockMethodAdvice) MockMethodDispatcher.get(identifier, thiz);
            if (mockMethodAdvice != null) {
                mockMethodAdvice.interceptors.put(thiz, thiz.getMockitoInterceptor());
            }
        }
    }
}
