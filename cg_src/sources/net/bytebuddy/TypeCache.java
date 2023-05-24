package net.bytebuddy;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import net.bytebuddy.build.CachedReturnPlugin;
import net.bytebuddy.utility.CompoundList;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/TypeCache.class */
public class TypeCache<T> extends ReferenceQueue<ClassLoader> {
    private static final Class<?> NOT_FOUND = null;
    protected final Sort sort;
    protected final ConcurrentMap<StorageKey, ConcurrentMap<T, Reference<Class<?>>>> cache = new ConcurrentHashMap();

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/TypeCache$Sort.class */
    public enum Sort {
        WEAK { // from class: net.bytebuddy.TypeCache.Sort.1
            @Override // net.bytebuddy.TypeCache.Sort
            protected Reference<Class<?>> wrap(Class<?> type) {
                return new WeakReference(type);
            }
        },
        SOFT { // from class: net.bytebuddy.TypeCache.Sort.2
            @Override // net.bytebuddy.TypeCache.Sort
            protected Reference<Class<?>> wrap(Class<?> type) {
                return new SoftReference(type);
            }
        };

        protected abstract Reference<Class<?>> wrap(Class<?> cls);
    }

    public TypeCache(Sort sort) {
        this.sort = sort;
    }

    @SuppressFBWarnings(value = {"GC_UNRELATED_TYPES"}, justification = "Cross-comparison is intended")
    public Class<?> find(ClassLoader classLoader, T key) {
        ConcurrentMap<T, Reference<Class<?>>> storage = this.cache.get(new LookupKey(classLoader));
        if (storage == null) {
            return NOT_FOUND;
        }
        Reference<Class<?>> reference = storage.get(key);
        if (reference == null) {
            return NOT_FOUND;
        }
        return reference.get();
    }

    @SuppressFBWarnings(value = {"GC_UNRELATED_TYPES"}, justification = "Cross-comparison is intended")
    public Class<?> insert(ClassLoader classLoader, T key, Class<?> type) {
        ConcurrentMap<T, Reference<Class<?>>> storage = this.cache.get(new LookupKey(classLoader));
        if (storage == null) {
            storage = new ConcurrentHashMap<>();
            ConcurrentMap<T, Reference<Class<?>>> previous = this.cache.putIfAbsent(new StorageKey(classLoader, this), storage);
            if (previous != null) {
                storage = previous;
            }
        }
        Reference<Class<?>> reference = this.sort.wrap(type);
        Reference<Class<?>> previous2 = storage.putIfAbsent(key, reference);
        while (previous2 != null) {
            Class<?> previousType = previous2.get();
            if (previousType != null) {
                return previousType;
            }
            if (storage.remove(key, previous2)) {
                previous2 = storage.putIfAbsent(key, reference);
            } else {
                previous2 = storage.get(key);
                if (previous2 == null) {
                    previous2 = storage.putIfAbsent(key, reference);
                }
            }
        }
        return type;
    }

    public Class<?> findOrInsert(ClassLoader classLoader, T key, Callable<Class<?>> lazy) {
        Class<?> type = find(classLoader, key);
        if (type != null) {
            return type;
        }
        try {
            return insert(classLoader, key, lazy.call());
        } catch (Throwable throwable) {
            throw new IllegalArgumentException("Could not create type", throwable);
        }
    }

    public Class<?> findOrInsert(ClassLoader classLoader, T key, Callable<Class<?>> lazy, Object monitor) {
        Class<?> findOrInsert;
        Class<?> type = find(classLoader, key);
        if (type != null) {
            return type;
        }
        synchronized (monitor) {
            findOrInsert = findOrInsert(classLoader, key, lazy);
        }
        return findOrInsert;
    }

    public void expungeStaleEntries() {
        while (true) {
            Reference<?> reference = poll();
            if (reference != null) {
                this.cache.remove(reference);
            } else {
                return;
            }
        }
    }

    public void clear() {
        this.cache.clear();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/TypeCache$LookupKey.class */
    public static class LookupKey {
        private final ClassLoader classLoader;
        private final int hashCode;

        protected LookupKey(ClassLoader classLoader) {
            this.classLoader = classLoader;
            this.hashCode = System.identityHashCode(classLoader);
        }

        public int hashCode() {
            return this.hashCode;
        }

        @SuppressFBWarnings(value = {"EQ_CHECK_FOR_OPERAND_NOT_COMPATIBLE_WITH_THIS"}, justification = "Cross-comparison is intended")
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (other instanceof LookupKey) {
                return this.classLoader == ((LookupKey) other).classLoader;
            } else if (other instanceof StorageKey) {
                StorageKey storageKey = (StorageKey) other;
                return this.hashCode == storageKey.hashCode && this.classLoader == storageKey.get();
            } else {
                return false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/TypeCache$StorageKey.class */
    public static class StorageKey extends WeakReference<ClassLoader> {
        private final int hashCode;

        protected StorageKey(ClassLoader classLoader, ReferenceQueue<? super ClassLoader> referenceQueue) {
            super(classLoader, referenceQueue);
            this.hashCode = System.identityHashCode(classLoader);
        }

        public int hashCode() {
            return this.hashCode;
        }

        @SuppressFBWarnings(value = {"EQ_CHECK_FOR_OPERAND_NOT_COMPATIBLE_WITH_THIS"}, justification = "Cross-comparison is intended")
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (other instanceof LookupKey) {
                LookupKey lookupKey = (LookupKey) other;
                return this.hashCode == lookupKey.hashCode && get() == lookupKey.classLoader;
            } else if (other instanceof StorageKey) {
                StorageKey storageKey = (StorageKey) other;
                return this.hashCode == storageKey.hashCode && get() == storageKey.get();
            } else {
                return false;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/TypeCache$WithInlineExpunction.class */
    public static class WithInlineExpunction<S> extends TypeCache<S> {
        public WithInlineExpunction(Sort sort) {
            super(sort);
        }

        @Override // net.bytebuddy.TypeCache
        public Class<?> find(ClassLoader classLoader, S key) {
            try {
                Class<?> find = super.find(classLoader, key);
                expungeStaleEntries();
                return find;
            } catch (Throwable th) {
                expungeStaleEntries();
                throw th;
            }
        }

        @Override // net.bytebuddy.TypeCache
        public Class<?> insert(ClassLoader classLoader, S key, Class<?> type) {
            try {
                Class<?> insert = super.insert(classLoader, key, type);
                expungeStaleEntries();
                return insert;
            } catch (Throwable th) {
                expungeStaleEntries();
                throw th;
            }
        }

        @Override // net.bytebuddy.TypeCache
        public Class<?> findOrInsert(ClassLoader classLoader, S key, Callable<Class<?>> builder) {
            try {
                Class<?> findOrInsert = super.findOrInsert(classLoader, key, builder);
                expungeStaleEntries();
                return findOrInsert;
            } catch (Throwable th) {
                expungeStaleEntries();
                throw th;
            }
        }

        @Override // net.bytebuddy.TypeCache
        public Class<?> findOrInsert(ClassLoader classLoader, S key, Callable<Class<?>> builder, Object monitor) {
            try {
                Class<?> findOrInsert = super.findOrInsert(classLoader, key, builder, monitor);
                expungeStaleEntries();
                return findOrInsert;
            } catch (Throwable th) {
                expungeStaleEntries();
                throw th;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/TypeCache$SimpleKey.class */
    public static class SimpleKey {
        private final Set<String> types;
        private transient /* synthetic */ int hashCode_NvlhFsMA;

        public SimpleKey(Class<?> type, Class<?>... additionalType) {
            this(type, Arrays.asList(additionalType));
        }

        public SimpleKey(Class<?> type, Collection<? extends Class<?>> additionalTypes) {
            this(CompoundList.of(type, new ArrayList(additionalTypes)));
        }

        public SimpleKey(Collection<? extends Class<?>> types) {
            this.types = new HashSet();
            for (Class<?> type : types) {
                this.types.add(type.getName());
            }
        }

        @CachedReturnPlugin.Enhance
        public int hashCode() {
            int hashCode = this.hashCode_NvlhFsMA != 0 ? 0 : this.types.hashCode();
            if (hashCode == 0) {
                hashCode = this.hashCode_NvlhFsMA;
            } else {
                this.hashCode_NvlhFsMA = hashCode;
            }
            return hashCode;
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || getClass() != other.getClass()) {
                return false;
            }
            SimpleKey simpleKey = (SimpleKey) other;
            return this.types.equals(simpleKey.types);
        }
    }
}
