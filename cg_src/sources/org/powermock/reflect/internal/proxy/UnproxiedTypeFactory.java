package org.powermock.reflect.internal.proxy;

import java.util.ArrayList;
import java.util.List;
/* loaded from: gencallgraphv3.jar:powermock-reflect-2.0.9.jar:org/powermock/reflect/internal/proxy/UnproxiedTypeFactory.class */
class UnproxiedTypeFactory {
    private static final String[] RESTRICTED_INTERFACES = {"cglib.proxy.Factory"};

    /* JADX INFO: Access modifiers changed from: package-private */
    public UnproxiedType createFromInterfaces(Class<?>[] interfaces) {
        Class<?>[] filteredInterfaces = filterInterfaces(interfaces);
        if (filteredInterfaces.length == 0) {
            return new UnproxiedTypeImpl(Object.class);
        }
        if (filteredInterfaces.length == 1) {
            return new UnproxiedTypeImpl(filteredInterfaces[0]);
        }
        return new UnproxiedTypeImpl(filteredInterfaces);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public UnproxiedType createFromSuperclassAndInterfaces(Class<?> superclass, Class<?>[] interfaces) {
        if (Object.class.equals(superclass)) {
            return createFromInterfaces(interfaces);
        }
        Class<?>[] filteredInterfaces = filterInterfaces(interfaces);
        return new UnproxiedTypeImpl(superclass, filteredInterfaces);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public UnproxiedType createFromType(Class<?> type) {
        return new UnproxiedTypeImpl(type);
    }

    private Class<?>[] filterInterfaces(Class<?>[] interfaces) {
        List<Class<?>> filtered = new ArrayList<>();
        for (Class<?> anInterface : interfaces) {
            if (!isMockFrameworkInterface(anInterface)) {
                filtered.add(anInterface);
            }
        }
        return (Class[]) filtered.toArray(new Class[filtered.size()]);
    }

    private boolean isMockFrameworkInterface(Class<?> anInterface) {
        String[] strArr;
        String name = anInterface.getName();
        for (String restrictedInterface : RESTRICTED_INTERFACES) {
            if (name.contains(restrictedInterface)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:powermock-reflect-2.0.9.jar:org/powermock/reflect/internal/proxy/UnproxiedTypeFactory$UnproxiedTypeImpl.class */
    public static class UnproxiedTypeImpl implements UnproxiedType {
        private final Class<?> type;
        private final Class<?>[] interfaces;

        private UnproxiedTypeImpl(Class<?> type) {
            this.type = type;
            this.interfaces = new Class[0];
        }

        private UnproxiedTypeImpl(Class<?>[] interfaces) {
            this.type = null;
            this.interfaces = interfaces;
        }

        private UnproxiedTypeImpl(Class<?> type, Class<?>[] interfaces) {
            this.type = type;
            this.interfaces = interfaces;
        }

        @Override // org.powermock.reflect.internal.proxy.UnproxiedType
        public Class<?> getOriginalType() {
            return this.type;
        }

        @Override // org.powermock.reflect.internal.proxy.UnproxiedType
        public Class<?>[] getInterfaces() {
            return this.interfaces;
        }
    }
}
