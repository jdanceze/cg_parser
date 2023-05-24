package net.bytebuddy.dynamic;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.NexusAccessor;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.scaffold.TypeInitializer;
import net.bytebuddy.implementation.LoadedTypeInitializer;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/TypeResolutionStrategy.class */
public interface TypeResolutionStrategy {

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/TypeResolutionStrategy$Resolved.class */
    public interface Resolved {
        TypeInitializer injectedInto(TypeInitializer typeInitializer);

        <S extends ClassLoader> Map<TypeDescription, Class<?>> initialize(DynamicType dynamicType, S s, ClassLoadingStrategy<? super S> classLoadingStrategy);
    }

    Resolved resolve();

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/TypeResolutionStrategy$Passive.class */
    public enum Passive implements TypeResolutionStrategy, Resolved {
        INSTANCE;

        @Override // net.bytebuddy.dynamic.TypeResolutionStrategy
        public Resolved resolve() {
            return this;
        }

        @Override // net.bytebuddy.dynamic.TypeResolutionStrategy.Resolved
        public TypeInitializer injectedInto(TypeInitializer typeInitializer) {
            return typeInitializer;
        }

        @Override // net.bytebuddy.dynamic.TypeResolutionStrategy.Resolved
        public <S extends ClassLoader> Map<TypeDescription, Class<?>> initialize(DynamicType dynamicType, S classLoader, ClassLoadingStrategy<? super S> classLoadingStrategy) {
            Map<TypeDescription, Class<?>> types = classLoadingStrategy.load(classLoader, dynamicType.getAllTypes());
            for (Map.Entry<TypeDescription, LoadedTypeInitializer> entry : dynamicType.getLoadedTypeInitializers().entrySet()) {
                entry.getValue().onLoad(types.get(entry.getKey()));
            }
            return new HashMap(types);
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/TypeResolutionStrategy$Active.class */
    public static class Active implements TypeResolutionStrategy {
        private final NexusAccessor nexusAccessor;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.nexusAccessor.equals(((Active) obj).nexusAccessor);
        }

        public int hashCode() {
            return (17 * 31) + this.nexusAccessor.hashCode();
        }

        public Active() {
            this(new NexusAccessor());
        }

        public Active(NexusAccessor nexusAccessor) {
            this.nexusAccessor = nexusAccessor;
        }

        @Override // net.bytebuddy.dynamic.TypeResolutionStrategy
        @SuppressFBWarnings(value = {"DMI_RANDOM_USED_ONLY_ONCE"}, justification = "Avoid thread-contention")
        public Resolved resolve() {
            return new Resolved(this.nexusAccessor, new Random().nextInt());
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/TypeResolutionStrategy$Active$Resolved.class */
        protected static class Resolved implements Resolved {
            private final NexusAccessor nexusAccessor;
            private final int identification;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.identification == ((Resolved) obj).identification && this.nexusAccessor.equals(((Resolved) obj).nexusAccessor);
            }

            public int hashCode() {
                return (((17 * 31) + this.nexusAccessor.hashCode()) * 31) + this.identification;
            }

            protected Resolved(NexusAccessor nexusAccessor, int identification) {
                this.nexusAccessor = nexusAccessor;
                this.identification = identification;
            }

            @Override // net.bytebuddy.dynamic.TypeResolutionStrategy.Resolved
            public TypeInitializer injectedInto(TypeInitializer typeInitializer) {
                return typeInitializer.expandWith(new NexusAccessor.InitializationAppender(this.identification));
            }

            @Override // net.bytebuddy.dynamic.TypeResolutionStrategy.Resolved
            public <S extends ClassLoader> Map<TypeDescription, Class<?>> initialize(DynamicType dynamicType, S classLoader, ClassLoadingStrategy<? super S> classLoadingStrategy) {
                Map<TypeDescription, LoadedTypeInitializer> loadedTypeInitializers = new HashMap<>(dynamicType.getLoadedTypeInitializers());
                TypeDescription instrumentedType = dynamicType.getTypeDescription();
                Map<TypeDescription, Class<?>> types = classLoadingStrategy.load(classLoader, dynamicType.getAllTypes());
                this.nexusAccessor.register(instrumentedType.getName(), types.get(instrumentedType).getClassLoader(), this.identification, loadedTypeInitializers.remove(instrumentedType));
                for (Map.Entry<TypeDescription, LoadedTypeInitializer> entry : loadedTypeInitializers.entrySet()) {
                    entry.getValue().onLoad(types.get(entry.getKey()));
                }
                return types;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/TypeResolutionStrategy$Lazy.class */
    public enum Lazy implements TypeResolutionStrategy, Resolved {
        INSTANCE;

        @Override // net.bytebuddy.dynamic.TypeResolutionStrategy
        public Resolved resolve() {
            return this;
        }

        @Override // net.bytebuddy.dynamic.TypeResolutionStrategy.Resolved
        public TypeInitializer injectedInto(TypeInitializer typeInitializer) {
            return typeInitializer;
        }

        @Override // net.bytebuddy.dynamic.TypeResolutionStrategy.Resolved
        public <S extends ClassLoader> Map<TypeDescription, Class<?>> initialize(DynamicType dynamicType, S classLoader, ClassLoadingStrategy<? super S> classLoadingStrategy) {
            return classLoadingStrategy.load(classLoader, dynamicType.getAllTypes());
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/TypeResolutionStrategy$Disabled.class */
    public enum Disabled implements TypeResolutionStrategy, Resolved {
        INSTANCE;

        @Override // net.bytebuddy.dynamic.TypeResolutionStrategy
        public Resolved resolve() {
            return this;
        }

        @Override // net.bytebuddy.dynamic.TypeResolutionStrategy.Resolved
        public TypeInitializer injectedInto(TypeInitializer typeInitializer) {
            return typeInitializer;
        }

        @Override // net.bytebuddy.dynamic.TypeResolutionStrategy.Resolved
        public <S extends ClassLoader> Map<TypeDescription, Class<?>> initialize(DynamicType dynamicType, S classLoader, ClassLoadingStrategy<? super S> classLoadingStrategy) {
            throw new IllegalStateException("Cannot initialize a dynamic type for a disabled type resolution strategy");
        }
    }
}
