package org.mockito.internal.creation.bytebuddy;

import java.lang.ref.ReferenceQueue;
import java.util.Set;
import java.util.concurrent.Callable;
import net.bytebuddy.TypeCache;
import org.mockito.mock.SerializableMode;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/TypeCachingBytecodeGenerator.class */
public class TypeCachingBytecodeGenerator extends ReferenceQueue<ClassLoader> implements BytecodeGenerator {
    private final Object BOOTSTRAP_LOCK = new Object();
    private final BytecodeGenerator bytecodeGenerator;
    private final TypeCache<MockitoMockKey> typeCache;

    public TypeCachingBytecodeGenerator(BytecodeGenerator bytecodeGenerator, boolean weak) {
        this.bytecodeGenerator = bytecodeGenerator;
        this.typeCache = new TypeCache.WithInlineExpunction(weak ? TypeCache.Sort.WEAK : TypeCache.Sort.SOFT);
    }

    @Override // org.mockito.internal.creation.bytebuddy.BytecodeGenerator
    public <T> Class<T> mockClass(final MockFeatures<T> params) {
        try {
            ClassLoader classLoader = params.mockedType.getClassLoader();
            return (Class<T>) this.typeCache.findOrInsert(classLoader, new MockitoMockKey(params.mockedType, params.interfaces, params.serializableMode, params.stripAnnotations), new Callable<Class<?>>() { // from class: org.mockito.internal.creation.bytebuddy.TypeCachingBytecodeGenerator.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public Class<?> call() throws Exception {
                    return TypeCachingBytecodeGenerator.this.bytecodeGenerator.mockClass(params);
                }
            }, this.BOOTSTRAP_LOCK);
        } catch (IllegalArgumentException exception) {
            Throwable cause = exception.getCause();
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            }
            throw exception;
        }
    }

    @Override // org.mockito.internal.creation.bytebuddy.BytecodeGenerator
    public void mockClassStatic(Class<?> type) {
        this.bytecodeGenerator.mockClassStatic(type);
    }

    @Override // org.mockito.internal.creation.bytebuddy.BytecodeGenerator
    public void mockClassConstruction(Class<?> type) {
        this.bytecodeGenerator.mockClassConstruction(type);
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/TypeCachingBytecodeGenerator$MockitoMockKey.class */
    private static class MockitoMockKey extends TypeCache.SimpleKey {
        private final SerializableMode serializableMode;
        private final boolean stripAnnotations;

        private MockitoMockKey(Class<?> type, Set<Class<?>> additionalType, SerializableMode serializableMode, boolean stripAnnotations) {
            super(type, additionalType);
            this.serializableMode = serializableMode;
            this.stripAnnotations = stripAnnotations;
        }

        @Override // net.bytebuddy.TypeCache.SimpleKey
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object != null && getClass() == object.getClass() && super.equals(object)) {
                MockitoMockKey that = (MockitoMockKey) object;
                return this.stripAnnotations == that.stripAnnotations && this.serializableMode.equals(that.serializableMode);
            }
            return false;
        }

        @Override // net.bytebuddy.TypeCache.SimpleKey
        public int hashCode() {
            int result = super.hashCode();
            return (31 * ((31 * result) + (this.stripAnnotations ? 1 : 0))) + this.serializableMode.hashCode();
        }
    }
}
