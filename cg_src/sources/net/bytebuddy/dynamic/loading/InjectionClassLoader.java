package net.bytebuddy.dynamic.loading;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import net.bytebuddy.description.type.TypeDescription;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/InjectionClassLoader.class */
public abstract class InjectionClassLoader extends ClassLoader {
    private final boolean sealed;

    protected abstract Map<String, Class<?>> doDefineClasses(Map<String, byte[]> map) throws ClassNotFoundException;

    /* JADX INFO: Access modifiers changed from: protected */
    public InjectionClassLoader(ClassLoader parent, boolean sealed) {
        super(parent);
        this.sealed = sealed;
    }

    public boolean isSealed() {
        return this.sealed;
    }

    public Class<?> defineClass(String name, byte[] binaryRepresentation) throws ClassNotFoundException {
        return defineClasses(Collections.singletonMap(name, binaryRepresentation)).get(name);
    }

    public Map<String, Class<?>> defineClasses(Map<String, byte[]> typeDefinitions) throws ClassNotFoundException {
        if (this.sealed) {
            throw new IllegalStateException("Cannot inject classes into a sealed class loader");
        }
        return doDefineClasses(typeDefinitions);
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/InjectionClassLoader$Strategy.class */
    public enum Strategy implements ClassLoadingStrategy<InjectionClassLoader> {
        INSTANCE;

        @Override // net.bytebuddy.dynamic.loading.ClassLoadingStrategy
        public /* bridge */ /* synthetic */ Map load(InjectionClassLoader injectionClassLoader, Map map) {
            return load2(injectionClassLoader, (Map<TypeDescription, byte[]>) map);
        }

        /* renamed from: load  reason: avoid collision after fix types in other method */
        public Map<TypeDescription, Class<?>> load2(InjectionClassLoader classLoader, Map<TypeDescription, byte[]> types) {
            if (classLoader == null) {
                throw new IllegalArgumentException("Cannot add types to bootstrap class loader: " + types);
            }
            Map<String, byte[]> typeDefinitions = new LinkedHashMap<>();
            Map<String, TypeDescription> typeDescriptions = new HashMap<>();
            for (Map.Entry<TypeDescription, byte[]> entry : types.entrySet()) {
                typeDefinitions.put(entry.getKey().getName(), entry.getValue());
                typeDescriptions.put(entry.getKey().getName(), entry.getKey());
            }
            Map<TypeDescription, Class<?>> loadedTypes = new HashMap<>();
            try {
                for (Map.Entry<String, Class<?>> entry2 : classLoader.defineClasses(typeDefinitions).entrySet()) {
                    loadedTypes.put(typeDescriptions.get(entry2.getKey()), entry2.getValue());
                }
                return loadedTypes;
            } catch (ClassNotFoundException exception) {
                throw new IllegalStateException("Cannot load classes: " + types, exception);
            }
        }
    }
}
