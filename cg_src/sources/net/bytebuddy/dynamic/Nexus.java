package net.bytebuddy.dynamic;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/Nexus.class */
public class Nexus extends WeakReference<ClassLoader> {
    public static final String PROPERTY = "net.bytebuddy.nexus.disabled";
    private static final ReferenceQueue<ClassLoader> NO_QUEUE = null;
    private static final ConcurrentMap<Nexus, Object> TYPE_INITIALIZERS = new ConcurrentHashMap();
    private final String name;
    private final int classLoaderHashCode;
    private final int identification;

    private Nexus(Class<?> type, int identification) {
        this(nonAnonymous(type.getName()), type.getClassLoader(), NO_QUEUE, identification);
    }

    private Nexus(String name, ClassLoader classLoader, ReferenceQueue<? super ClassLoader> referenceQueue, int identification) {
        super(classLoader, classLoader == null ? null : referenceQueue);
        this.name = name;
        this.classLoaderHashCode = System.identityHashCode(classLoader);
        this.identification = identification;
    }

    private static String nonAnonymous(String typeName) {
        int anonymousLoaderIndex = typeName.indexOf(47);
        return anonymousLoaderIndex == -1 ? typeName : typeName.substring(0, anonymousLoaderIndex);
    }

    public static void initialize(Class<?> type, int identification) throws Exception {
        Object typeInitializer = TYPE_INITIALIZERS.remove(new Nexus(type, identification));
        if (typeInitializer != null) {
            typeInitializer.getClass().getMethod("onLoad", Class.class).invoke(typeInitializer, type);
        }
    }

    public static void register(String name, ClassLoader classLoader, ReferenceQueue<? super ClassLoader> referenceQueue, int identification, Object typeInitializer) {
        TYPE_INITIALIZERS.put(new Nexus(name, classLoader, referenceQueue, identification), typeInitializer);
    }

    public static void clean(Reference<? super ClassLoader> reference) {
        TYPE_INITIALIZERS.remove(reference);
    }

    public int hashCode() {
        int result = this.name.hashCode();
        return (31 * ((31 * result) + this.classLoaderHashCode)) + this.identification;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Nexus nexus = (Nexus) other;
        return this.classLoaderHashCode == nexus.classLoaderHashCode && this.identification == nexus.identification && this.name.equals(nexus.name) && get() == nexus.get();
    }

    public String toString() {
        return "Nexus{name='" + this.name + "', classLoaderHashCode=" + this.classLoaderHashCode + ", identification=" + this.identification + ", classLoader=" + get() + '}';
    }
}
