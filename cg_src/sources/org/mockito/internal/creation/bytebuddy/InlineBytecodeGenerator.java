package org.mockito.internal.creation.bytebuddy;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.field.FieldList;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder;
import net.bytebuddy.jar.asm.ClassVisitor;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.pool.TypePool;
import net.bytebuddy.utility.OpenedClassReader;
import net.bytebuddy.utility.RandomString;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.SuppressSignatureCheck;
import org.mockito.internal.creation.bytebuddy.MockMethodAdvice;
import org.mockito.internal.creation.bytebuddy.inject.MockMethodDispatcher;
import org.mockito.internal.util.StringUtil;
import org.mockito.internal.util.concurrent.DetachedThreadLocal;
import org.mockito.internal.util.concurrent.WeakConcurrentMap;
import org.mockito.internal.util.concurrent.WeakConcurrentSet;
import org.mockito.mock.SerializableMode;
@SuppressSignatureCheck
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/InlineBytecodeGenerator.class */
public class InlineBytecodeGenerator implements BytecodeGenerator, ClassFileTransformer {
    private static final String PRELOAD = "org.mockito.inline.preload";
    static final Set<Class<?>> EXCLUDES = new HashSet(Arrays.asList(Class.class, Boolean.class, Byte.class, Short.class, Character.class, Integer.class, Long.class, Float.class, Double.class, String.class));
    private final Instrumentation instrumentation;
    private final ByteBuddy byteBuddy;
    private final WeakConcurrentSet<Class<?>> mocked;
    private final WeakConcurrentSet<Class<?>> flatMocked;
    private final BytecodeGenerator subclassEngine;
    private final AsmVisitorWrapper mockTransformer;
    private final Method getModule;
    private final Method canRead;
    private final Method redefineModule;
    private volatile Throwable lastException;

    public InlineBytecodeGenerator(Instrumentation instrumentation, WeakConcurrentMap<Object, MockMethodInterceptor> mocks, DetachedThreadLocal<Map<Class<?>, MockMethodInterceptor>> mockedStatics, Predicate<Class<?>> isMockConstruction, ConstructionCallback onConstruction) {
        Method getModule;
        Method canRead;
        Method redefineModule;
        preload();
        this.instrumentation = instrumentation;
        this.byteBuddy = new ByteBuddy().with(TypeValidation.DISABLED).with(Implementation.Context.Disabled.Factory.INSTANCE).with(MethodGraph.Compiler.ForDeclaredMethods.INSTANCE).ignore(ElementMatchers.isSynthetic().and(ElementMatchers.not(ElementMatchers.isConstructor())).or(ElementMatchers.isDefaultFinalizer()));
        this.mocked = new WeakConcurrentSet<>(WeakConcurrentSet.Cleaner.INLINE);
        this.flatMocked = new WeakConcurrentSet<>(WeakConcurrentSet.Cleaner.INLINE);
        String identifier = RandomString.make();
        this.subclassEngine = new TypeCachingBytecodeGenerator(new SubclassBytecodeGenerator(MethodDelegation.withDefaultConfiguration().withBinders(TargetMethodAnnotationDrivenBinder.ParameterBinder.ForFixedValue.OfConstant.of(MockMethodAdvice.Identifier.class, identifier)).to(MockMethodAdvice.ForReadObject.class), ElementMatchers.isAbstract().or(ElementMatchers.isNative()).or(ElementMatchers.isToString())), false);
        this.mockTransformer = new AsmVisitorWrapper.ForDeclaredMethods().method(ElementMatchers.isVirtual().and(ElementMatchers.not(ElementMatchers.isBridge().or(ElementMatchers.isHashCode()).or(ElementMatchers.isEquals()).or(ElementMatchers.isDefaultFinalizer()))).and(ElementMatchers.not(ElementMatchers.isDeclaredBy(ElementMatchers.nameStartsWith("java.")).and(ElementMatchers.isPackagePrivate()))), Advice.withCustomMapping().bind(MockMethodAdvice.Identifier.class, identifier).to(MockMethodAdvice.class)).method(ElementMatchers.isStatic(), Advice.withCustomMapping().bind(MockMethodAdvice.Identifier.class, identifier).to(MockMethodAdvice.ForStatic.class)).constructor(ElementMatchers.any(), new MockMethodAdvice.ConstructorShortcut(identifier)).method(ElementMatchers.isHashCode(), Advice.withCustomMapping().bind(MockMethodAdvice.Identifier.class, identifier).to(MockMethodAdvice.ForHashCode.class)).method(ElementMatchers.isEquals(), Advice.withCustomMapping().bind(MockMethodAdvice.Identifier.class, identifier).to(MockMethodAdvice.ForEquals.class));
        try {
            getModule = Class.class.getMethod("getModule", new Class[0]);
            canRead = getModule.getReturnType().getMethod("canRead", getModule.getReturnType());
            redefineModule = Instrumentation.class.getMethod("redefineModule", getModule.getReturnType(), Set.class, Map.class, Map.class, Set.class, Map.class);
        } catch (Exception e) {
            getModule = null;
            canRead = null;
            redefineModule = null;
        }
        this.getModule = getModule;
        this.canRead = canRead;
        this.redefineModule = redefineModule;
        MockMethodDispatcher.set(identifier, new MockMethodAdvice(mocks, mockedStatics, identifier, isMockConstruction, onConstruction));
        instrumentation.addTransformer(this, true);
    }

    private static void preload() {
        String[] split;
        String preloads = System.getProperty(PRELOAD);
        if (preloads == null) {
            preloads = "java.lang.WeakPairMap,java.lang.WeakPairMap$Pair,java.lang.WeakPairMap$Pair$Weak";
        }
        for (String preload : preloads.split(",")) {
            try {
                Class.forName(preload, false, null);
            } catch (ClassNotFoundException e) {
            }
        }
    }

    @Override // org.mockito.internal.creation.bytebuddy.BytecodeGenerator
    public <T> Class<? extends T> mockClass(MockFeatures<T> features) {
        boolean subclassingRequired = (features.interfaces.isEmpty() && features.serializableMode == SerializableMode.NONE && !Modifier.isAbstract(features.mockedType.getModifiers())) ? false : true;
        checkSupportedCombination(subclassingRequired, features);
        Set<Class<?>> types = new HashSet<>();
        types.add(features.mockedType);
        types.addAll(features.interfaces);
        synchronized (this) {
            triggerRetransformation(types, false);
        }
        return subclassingRequired ? this.subclassEngine.mockClass(features) : (Class<T>) features.mockedType;
    }

    @Override // org.mockito.internal.creation.bytebuddy.BytecodeGenerator
    public synchronized void mockClassStatic(Class<?> type) {
        triggerRetransformation(Collections.singleton(type), true);
    }

    @Override // org.mockito.internal.creation.bytebuddy.BytecodeGenerator
    public synchronized void mockClassConstruction(Class<?> type) {
        triggerRetransformation(Collections.singleton(type), false);
    }

    private static void assureInitialization(Class<?> type) {
        try {
            Class.forName(type.getName(), true, type.getClassLoader());
        } catch (Throwable th) {
        }
    }

    private <T> void triggerRetransformation(Set<Class<?>> types, boolean flat) {
        Set<Class<?>> targets = new HashSet<>();
        Iterator<Class<?>> it = types.iterator();
        while (it.hasNext()) {
            Class<?> type = it.next();
            if (flat) {
                if (!this.mocked.contains(type) && this.flatMocked.add(type)) {
                    assureInitialization(type);
                    targets.add(type);
                }
            } else {
                do {
                    if (this.mocked.add(type)) {
                        assureInitialization(type);
                        if (!this.flatMocked.remove(type)) {
                            targets.add(type);
                        }
                        addInterfaces(targets, type.getInterfaces());
                    }
                    type = type.getSuperclass();
                } while (type != null);
            }
        }
        if (!targets.isEmpty()) {
            try {
                try {
                    assureCanReadMockito(targets);
                    this.instrumentation.retransformClasses((Class[]) targets.toArray(new Class[targets.size()]));
                    Throwable throwable = this.lastException;
                    if (throwable != null) {
                        throw new IllegalStateException(StringUtil.join("Byte Buddy could not instrument all classes within the mock's type hierarchy", "", "This problem should never occur for javac-compiled classes. This problem has been observed for classes that are:", " - Compiled by older versions of scalac", " - Classes that are part of the Android distribution"), throwable);
                    }
                } catch (Exception exception) {
                    for (Class<?> failed : targets) {
                        this.mocked.remove(failed);
                        this.flatMocked.remove(failed);
                    }
                    throw new MockitoException("Could not modify all classes " + targets, exception);
                }
            } finally {
                this.lastException = null;
            }
        }
    }

    private void assureCanReadMockito(Set<Class<?>> types) {
        if (this.redefineModule == null) {
            return;
        }
        Set<Object> modules = new HashSet<>();
        try {
            Object target = this.getModule.invoke(Class.forName("org.mockito.internal.creation.bytebuddy.inject.MockMethodDispatcher", false, null), new Object[0]);
            for (Class<?> type : types) {
                Object module = this.getModule.invoke(type, new Object[0]);
                if (!modules.contains(module) && !((Boolean) this.canRead.invoke(module, target)).booleanValue()) {
                    modules.add(module);
                }
            }
            for (Object module2 : modules) {
                this.redefineModule.invoke(this.instrumentation, module2, Collections.singleton(target), Collections.emptyMap(), Collections.emptyMap(), Collections.emptySet(), Collections.emptyMap());
            }
        } catch (Exception e) {
            throw new IllegalStateException(StringUtil.join("Could not adjust module graph to make the mock instance dispatcher visible to some classes", "", "At least one of those modules: " + modules + " is not reading the unnamed module of the bootstrap loader", "Without such a read edge, the classes that are redefined to become mocks cannot access the mock dispatcher.", "To circumvent this, Mockito attempted to add a read edge to this module what failed for an unexpected reason"), e);
        }
    }

    private <T> void checkSupportedCombination(boolean subclassingRequired, MockFeatures<T> features) {
        if (subclassingRequired && !features.mockedType.isArray() && !features.mockedType.isPrimitive() && Modifier.isFinal(features.mockedType.getModifiers())) {
            throw new MockitoException("Unsupported settings with this type '" + features.mockedType.getName() + "'");
        }
    }

    private void addInterfaces(Set<Class<?>> types, Class<?>[] interfaces) {
        for (Class<?> type : interfaces) {
            if (this.mocked.add(type)) {
                if (!this.flatMocked.remove(type)) {
                    types.add(type);
                }
                addInterfaces(types, type.getInterfaces());
            }
        }
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        if (classBeingRedefined != null) {
            if ((!this.mocked.contains(classBeingRedefined) && !this.flatMocked.contains(classBeingRedefined)) || EXCLUDES.contains(classBeingRedefined)) {
                return null;
            }
            try {
                return this.byteBuddy.redefine(classBeingRedefined, ClassFileLocator.Simple.of(classBeingRedefined.getName(), classfileBuffer)).visit(new ParameterWritingVisitorWrapper(classBeingRedefined)).visit(this.mockTransformer).make().getBytes();
            } catch (Throwable throwable) {
                this.lastException = throwable;
                return null;
            }
        }
        return null;
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/InlineBytecodeGenerator$ParameterWritingVisitorWrapper.class */
    private static class ParameterWritingVisitorWrapper extends AsmVisitorWrapper.AbstractBase {
        private final Class<?> type;

        private ParameterWritingVisitorWrapper(Class<?> type) {
            this.type = type;
        }

        @Override // net.bytebuddy.asm.AsmVisitorWrapper
        public ClassVisitor wrap(TypeDescription instrumentedType, ClassVisitor classVisitor, Implementation.Context implementationContext, TypePool typePool, FieldList<FieldDescription.InDefinedShape> fields, MethodList<?> methods, int writerFlags, int readerFlags) {
            if (implementationContext.getClassFileVersion().isAtLeast(ClassFileVersion.JAVA_V8)) {
                return new ParameterAddingClassVisitor(classVisitor, new TypeDescription.ForLoadedType(this.type));
            }
            return classVisitor;
        }

        /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/InlineBytecodeGenerator$ParameterWritingVisitorWrapper$ParameterAddingClassVisitor.class */
        private static class ParameterAddingClassVisitor extends ClassVisitor {
            private final TypeDescription typeDescription;

            private ParameterAddingClassVisitor(ClassVisitor cv, TypeDescription typeDescription) {
                super(OpenedClassReader.ASM_API, cv);
                this.typeDescription = typeDescription;
            }

            @Override // net.bytebuddy.jar.asm.ClassVisitor
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                ElementMatcher.Junction named;
                MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
                MethodList<MethodDescription.InDefinedShape> declaredMethods = this.typeDescription.getDeclaredMethods();
                if (name.equals("<init>")) {
                    named = ElementMatchers.isConstructor();
                } else {
                    named = ElementMatchers.named(name);
                }
                MethodList methodList = declaredMethods.filter(named.and(ElementMatchers.hasDescriptor(desc)));
                if (methodList.size() == 1 && ((MethodDescription) methodList.getOnly()).getParameters().hasExplicitMetaData()) {
                    Iterator it = ((MethodDescription) methodList.getOnly()).getParameters().iterator();
                    while (it.hasNext()) {
                        ParameterDescription parameterDescription = (ParameterDescription) it.next();
                        methodVisitor.visitParameter(parameterDescription.getName(), parameterDescription.getModifiers());
                    }
                    return new MethodParameterStrippingMethodVisitor(methodVisitor);
                }
                return methodVisitor;
            }
        }

        /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/InlineBytecodeGenerator$ParameterWritingVisitorWrapper$MethodParameterStrippingMethodVisitor.class */
        private static class MethodParameterStrippingMethodVisitor extends MethodVisitor {
            public MethodParameterStrippingMethodVisitor(MethodVisitor mv) {
                super(OpenedClassReader.ASM_API, mv);
            }

            @Override // net.bytebuddy.jar.asm.MethodVisitor
            public void visitParameter(String name, int access) {
            }
        }
    }
}
