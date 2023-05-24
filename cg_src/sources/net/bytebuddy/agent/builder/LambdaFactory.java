package net.bytebuddy.agent.builder;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.lang.instrument.ClassFileTransformer;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.loading.ClassInjector;
import net.bytebuddy.implementation.auxiliary.TypeProxy;
@HashCodeAndEqualsPlugin.Enhance
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/agent/builder/LambdaFactory.class */
public class LambdaFactory {
    private static final String FIELD_NAME = "CLASS_FILE_TRANSFORMERS";
    @SuppressFBWarnings(value = {"MS_MUTABLE_COLLECTION_PKGPROTECT"}, justification = "The field must be accessible by different class loader instances")
    public static final Map<ClassFileTransformer, LambdaFactory> CLASS_FILE_TRANSFORMERS = new ConcurrentHashMap();
    private final Object target;
    private final Method dispatcher;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.target.equals(((LambdaFactory) obj).target) && this.dispatcher.equals(((LambdaFactory) obj).dispatcher);
    }

    public int hashCode() {
        return (((17 * 31) + this.target.hashCode()) * 31) + this.dispatcher.hashCode();
    }

    public LambdaFactory(Object target, Method dispatcher) {
        this.target = target;
        this.dispatcher = dispatcher;
    }

    public static boolean register(ClassFileTransformer classFileTransformer, Object classFileFactory) {
        boolean isEmpty;
        try {
            TypeDescription typeDescription = TypeDescription.ForLoadedType.of(LambdaFactory.class);
            Class<?> lambdaFactory = ClassInjector.UsingReflection.ofSystemClassLoader().inject(Collections.singletonMap(typeDescription, ClassFileLocator.ForClassLoader.read(LambdaFactory.class))).get(typeDescription);
            Map<ClassFileTransformer, Object> classFileTransformers = (Map) lambdaFactory.getField(FIELD_NAME).get(null);
            synchronized (classFileTransformers) {
                try {
                    isEmpty = classFileTransformers.isEmpty();
                    classFileTransformers.put(classFileTransformer, lambdaFactory.getConstructor(Object.class, Method.class).newInstance(classFileFactory, classFileFactory.getClass().getMethod(TypeProxy.REFLECTION_METHOD, Object.class, String.class, Object.class, Object.class, Object.class, Object.class, Boolean.TYPE, List.class, List.class, Collection.class)));
                } catch (Throwable th) {
                    classFileTransformers.put(classFileTransformer, lambdaFactory.getConstructor(Object.class, Method.class).newInstance(classFileFactory, classFileFactory.getClass().getMethod(TypeProxy.REFLECTION_METHOD, Object.class, String.class, Object.class, Object.class, Object.class, Object.class, Boolean.TYPE, List.class, List.class, Collection.class)));
                    throw th;
                }
            }
            return isEmpty;
        } catch (RuntimeException exception) {
            throw exception;
        } catch (Exception exception2) {
            throw new IllegalStateException("Could not register class file transformer", exception2);
        }
    }

    public static boolean release(ClassFileTransformer classFileTransformer) {
        boolean z;
        try {
            Map<ClassFileTransformer, ?> classFileTransformers = (Map) ClassLoader.getSystemClassLoader().loadClass(LambdaFactory.class.getName()).getField(FIELD_NAME).get(null);
            synchronized (classFileTransformers) {
                z = classFileTransformers.remove(classFileTransformer) != null && classFileTransformers.isEmpty();
            }
            return z;
        } catch (RuntimeException exception) {
            throw exception;
        } catch (Exception exception2) {
            throw new IllegalStateException("Could not release class file transformer", exception2);
        }
    }

    private byte[] invoke(Object caller, String invokedName, Object invokedType, Object samMethodType, Object implMethod, Object instantiatedMethodType, boolean serializable, List<Class<?>> markerInterfaces, List<?> additionalBridges, Collection<ClassFileTransformer> classFileTransformers) {
        try {
            return (byte[]) this.dispatcher.invoke(this.target, caller, invokedName, invokedType, samMethodType, implMethod, instantiatedMethodType, Boolean.valueOf(serializable), markerInterfaces, additionalBridges, classFileTransformers);
        } catch (RuntimeException exception) {
            throw exception;
        } catch (Exception exception2) {
            throw new IllegalStateException("Cannot create class for lambda expression", exception2);
        }
    }

    public static byte[] make(Object caller, String invokedName, Object invokedType, Object samMethodType, Object implMethod, Object instantiatedMethodType, boolean serializable, List<Class<?>> markerInterfaces, List<?> additionalBridges) {
        return CLASS_FILE_TRANSFORMERS.values().iterator().next().invoke(caller, invokedName, invokedType, samMethodType, implMethod, instantiatedMethodType, serializable, markerInterfaces, additionalBridges, CLASS_FILE_TRANSFORMERS.keySet());
    }
}
