package net.bytebuddy.dynamic;

import android.content.ContentResolver;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Collections;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.loading.ClassInjector;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.LoadedTypeInitializer;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;
import net.bytebuddy.implementation.bytecode.Removal;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.collection.ArrayFactory;
import net.bytebuddy.implementation.bytecode.constant.ClassConstant;
import net.bytebuddy.implementation.bytecode.constant.IntegerConstant;
import net.bytebuddy.implementation.bytecode.constant.NullConstant;
import net.bytebuddy.implementation.bytecode.constant.TextConstant;
import net.bytebuddy.implementation.bytecode.member.MethodInvocation;
import net.bytebuddy.jar.asm.MethodVisitor;
@HashCodeAndEqualsPlugin.Enhance
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/NexusAccessor.class */
public class NexusAccessor {
    private static final Dispatcher DISPATCHER = (Dispatcher) AccessController.doPrivileged(Dispatcher.CreationAction.INSTANCE);
    private static final ReferenceQueue<ClassLoader> NO_QUEUE = null;
    @HashCodeAndEqualsPlugin.ValueHandling(HashCodeAndEqualsPlugin.ValueHandling.Sort.REVERSE_NULLABILITY)
    private final ReferenceQueue<? super ClassLoader> referenceQueue;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            ReferenceQueue<? super ClassLoader> referenceQueue = this.referenceQueue;
            ReferenceQueue<? super ClassLoader> referenceQueue2 = ((NexusAccessor) obj).referenceQueue;
            return referenceQueue2 != null ? referenceQueue != null && referenceQueue.equals(referenceQueue2) : referenceQueue == null;
        }
        return false;
    }

    public int hashCode() {
        int i = 17 * 31;
        ReferenceQueue<? super ClassLoader> referenceQueue = this.referenceQueue;
        return referenceQueue != null ? i + referenceQueue.hashCode() : i;
    }

    public NexusAccessor() {
        this(NO_QUEUE);
    }

    public NexusAccessor(ReferenceQueue<? super ClassLoader> referenceQueue) {
        this.referenceQueue = referenceQueue;
    }

    public static boolean isAlive() {
        return DISPATCHER.isAlive();
    }

    public static void clean(Reference<? extends ClassLoader> reference) {
        DISPATCHER.clean(reference);
    }

    public void register(String name, ClassLoader classLoader, int identification, LoadedTypeInitializer loadedTypeInitializer) {
        if (loadedTypeInitializer.isAlive()) {
            DISPATCHER.register(name, classLoader, this.referenceQueue, identification, loadedTypeInitializer);
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/NexusAccessor$InitializationAppender.class */
    public static class InitializationAppender implements ByteCodeAppender {
        private final int identification;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.identification == ((InitializationAppender) obj).identification;
        }

        public int hashCode() {
            return (17 * 31) + this.identification;
        }

        public InitializationAppender(int identification) {
            this.identification = identification;
        }

        @Override // net.bytebuddy.implementation.bytecode.ByteCodeAppender
        public ByteCodeAppender.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodDescription instrumentedMethod) {
            try {
                return new ByteCodeAppender.Simple(new StackManipulation.Compound(MethodInvocation.invoke((MethodDescription.InDefinedShape) new MethodDescription.ForLoadedMethod(ClassLoader.class.getMethod("getSystemClassLoader", new Class[0]))), new TextConstant(Nexus.class.getName()), MethodInvocation.invoke((MethodDescription.InDefinedShape) new MethodDescription.ForLoadedMethod(ClassLoader.class.getMethod("loadClass", String.class))), new TextConstant(ContentResolver.SYNC_EXTRAS_INITIALIZE), ArrayFactory.forType(TypeDescription.Generic.CLASS).withValues(Arrays.asList(ClassConstant.of(TypeDescription.CLASS), ClassConstant.of(TypeDescription.ForLoadedType.of(Integer.TYPE)))), MethodInvocation.invoke((MethodDescription.InDefinedShape) new MethodDescription.ForLoadedMethod(Class.class.getMethod("getMethod", String.class, Class[].class))), NullConstant.INSTANCE, ArrayFactory.forType(TypeDescription.Generic.OBJECT).withValues(Arrays.asList(ClassConstant.of(instrumentedMethod.getDeclaringType().asErasure()), new StackManipulation.Compound(IntegerConstant.forValue(this.identification), MethodInvocation.invoke((MethodDescription.InDefinedShape) new MethodDescription.ForLoadedMethod(Integer.class.getMethod("valueOf", Integer.TYPE)))))), MethodInvocation.invoke((MethodDescription.InDefinedShape) new MethodDescription.ForLoadedMethod(Method.class.getMethod("invoke", Object.class, Object[].class))), Removal.SINGLE)).apply(methodVisitor, implementationContext, instrumentedMethod);
            } catch (NoSuchMethodException exception) {
                throw new IllegalStateException("Cannot locate method", exception);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/NexusAccessor$Dispatcher.class */
    public interface Dispatcher {
        boolean isAlive();

        void clean(Reference<? extends ClassLoader> reference);

        void register(String str, ClassLoader classLoader, ReferenceQueue<? super ClassLoader> referenceQueue, int i, LoadedTypeInitializer loadedTypeInitializer);

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/NexusAccessor$Dispatcher$CreationAction.class */
        public enum CreationAction implements PrivilegedAction<Dispatcher> {
            INSTANCE;

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            @SuppressFBWarnings(value = {"REC_CATCH_EXCEPTION"}, justification = "Exception should not be rethrown but trigger a fallback")
            public Dispatcher run() {
                if (Boolean.getBoolean(Nexus.PROPERTY)) {
                    return new Unavailable("Nexus injection was explicitly disabled");
                }
                try {
                    Class<?> nexusType = new ClassInjector.UsingReflection(ClassLoader.getSystemClassLoader(), ClassLoadingStrategy.NO_PROTECTION_DOMAIN).inject(Collections.singletonMap(TypeDescription.ForLoadedType.of(Nexus.class), ClassFileLocator.ForClassLoader.read(Nexus.class))).get(TypeDescription.ForLoadedType.of(Nexus.class));
                    return new Available(nexusType.getMethod("register", String.class, ClassLoader.class, ReferenceQueue.class, Integer.TYPE, Object.class), nexusType.getMethod("clean", Reference.class));
                } catch (Exception exception) {
                    try {
                        Class<?> nexusType2 = ClassLoader.getSystemClassLoader().loadClass(Nexus.class.getName());
                        return new Available(nexusType2.getMethod("register", String.class, ClassLoader.class, ReferenceQueue.class, Integer.TYPE, Object.class), nexusType2.getMethod("clean", Reference.class));
                    } catch (Exception e) {
                        return new Unavailable(exception.toString());
                    }
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/NexusAccessor$Dispatcher$Available.class */
        public static class Available implements Dispatcher {
            private static final Object STATIC_METHOD = null;
            private final Method register;
            private final Method clean;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.register.equals(((Available) obj).register) && this.clean.equals(((Available) obj).clean);
            }

            public int hashCode() {
                return (((17 * 31) + this.register.hashCode()) * 31) + this.clean.hashCode();
            }

            protected Available(Method register, Method clean) {
                this.register = register;
                this.clean = clean;
            }

            @Override // net.bytebuddy.dynamic.NexusAccessor.Dispatcher
            public boolean isAlive() {
                return true;
            }

            @Override // net.bytebuddy.dynamic.NexusAccessor.Dispatcher
            public void clean(Reference<? extends ClassLoader> reference) {
                try {
                    this.clean.invoke(STATIC_METHOD, reference);
                } catch (IllegalAccessException exception) {
                    throw new IllegalStateException("Cannot access: " + this.clean, exception);
                } catch (InvocationTargetException exception2) {
                    throw new IllegalStateException("Cannot invoke: " + this.clean, exception2.getCause());
                }
            }

            @Override // net.bytebuddy.dynamic.NexusAccessor.Dispatcher
            public void register(String name, ClassLoader classLoader, ReferenceQueue<? super ClassLoader> referenceQueue, int identification, LoadedTypeInitializer loadedTypeInitializer) {
                try {
                    this.register.invoke(STATIC_METHOD, name, classLoader, referenceQueue, Integer.valueOf(identification), loadedTypeInitializer);
                } catch (IllegalAccessException exception) {
                    throw new IllegalStateException("Cannot access: " + this.register, exception);
                } catch (InvocationTargetException exception2) {
                    throw new IllegalStateException("Cannot invoke: " + this.register, exception2.getCause());
                }
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/NexusAccessor$Dispatcher$Unavailable.class */
        public static class Unavailable implements Dispatcher {
            private final String message;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.message.equals(((Unavailable) obj).message);
            }

            public int hashCode() {
                return (17 * 31) + this.message.hashCode();
            }

            protected Unavailable(String message) {
                this.message = message;
            }

            @Override // net.bytebuddy.dynamic.NexusAccessor.Dispatcher
            public boolean isAlive() {
                return false;
            }

            @Override // net.bytebuddy.dynamic.NexusAccessor.Dispatcher
            public void clean(Reference<? extends ClassLoader> reference) {
                throw new UnsupportedOperationException("Could not initialize Nexus accessor: " + this.message);
            }

            @Override // net.bytebuddy.dynamic.NexusAccessor.Dispatcher
            public void register(String name, ClassLoader classLoader, ReferenceQueue<? super ClassLoader> referenceQueue, int identification, LoadedTypeInitializer loadedTypeInitializer) {
                throw new UnsupportedOperationException("Could not initialize Nexus accessor: " + this.message);
            }
        }
    }
}
