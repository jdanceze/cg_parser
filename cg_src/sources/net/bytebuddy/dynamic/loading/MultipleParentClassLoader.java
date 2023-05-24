package net.bytebuddy.dynamic.loading;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/MultipleParentClassLoader.class */
public class MultipleParentClassLoader extends InjectionClassLoader {
    private final List<? extends ClassLoader> parents;

    public MultipleParentClassLoader(List<? extends ClassLoader> parents) {
        this(ClassLoadingStrategy.BOOTSTRAP_LOADER, parents);
    }

    public MultipleParentClassLoader(ClassLoader parent, List<? extends ClassLoader> parents) {
        this(parent, parents, true);
    }

    public MultipleParentClassLoader(ClassLoader parent, List<? extends ClassLoader> parents, boolean sealed) {
        super(parent, sealed);
        this.parents = parents;
    }

    @Override // java.lang.ClassLoader
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        for (ClassLoader parent : this.parents) {
            try {
                Class<?> type = parent.loadClass(name);
                if (resolve) {
                    resolveClass(type);
                }
                return type;
            } catch (ClassNotFoundException e) {
            }
        }
        return super.loadClass(name, resolve);
    }

    @Override // java.lang.ClassLoader
    public URL getResource(String name) {
        for (ClassLoader parent : this.parents) {
            URL url = parent.getResource(name);
            if (url != null) {
                return url;
            }
        }
        return super.getResource(name);
    }

    @Override // java.lang.ClassLoader
    public Enumeration<URL> getResources(String name) throws IOException {
        List<Enumeration<URL>> enumerations = new ArrayList<>(this.parents.size() + 1);
        for (ClassLoader parent : this.parents) {
            enumerations.add(parent.getResources(name));
        }
        enumerations.add(super.getResources(name));
        return new CompoundEnumeration(enumerations);
    }

    @Override // net.bytebuddy.dynamic.loading.InjectionClassLoader
    protected Map<String, Class<?>> doDefineClasses(Map<String, byte[]> typeDefinitions) {
        Map<String, Class<?>> types = new HashMap<>();
        for (Map.Entry<String, byte[]> entry : typeDefinitions.entrySet()) {
            types.put(entry.getKey(), defineClass(entry.getKey(), entry.getValue(), 0, entry.getValue().length));
        }
        return types;
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/MultipleParentClassLoader$CompoundEnumeration.class */
    protected static class CompoundEnumeration implements Enumeration<URL> {
        private static final int FIRST = 0;
        private final List<Enumeration<URL>> enumerations;
        private Enumeration<URL> currentEnumeration;

        protected CompoundEnumeration(List<Enumeration<URL>> enumerations) {
            this.enumerations = enumerations;
        }

        @Override // java.util.Enumeration
        public boolean hasMoreElements() {
            if (this.currentEnumeration != null && this.currentEnumeration.hasMoreElements()) {
                return true;
            }
            if (!this.enumerations.isEmpty()) {
                this.currentEnumeration = this.enumerations.remove(0);
                return hasMoreElements();
            }
            return false;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Enumeration
        @SuppressFBWarnings(value = {"UWF_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR"}, justification = "Null reference is impossible due to element check")
        public URL nextElement() {
            if (hasMoreElements()) {
                return this.currentEnumeration.nextElement();
            }
            throw new NoSuchElementException();
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/loading/MultipleParentClassLoader$Builder.class */
    public static class Builder {
        private static final int ONLY = 0;
        private final boolean sealed;
        private final List<? extends ClassLoader> classLoaders;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.sealed == ((Builder) obj).sealed && this.classLoaders.equals(((Builder) obj).classLoaders);
        }

        public int hashCode() {
            return (((17 * 31) + (this.sealed ? 1 : 0)) * 31) + this.classLoaders.hashCode();
        }

        public Builder() {
            this(true);
        }

        public Builder(boolean sealed) {
            this(Collections.emptyList(), sealed);
        }

        private Builder(List<? extends ClassLoader> classLoaders, boolean sealed) {
            this.classLoaders = classLoaders;
            this.sealed = sealed;
        }

        public Builder append(Class<?>... type) {
            return append((Collection<? extends Class<?>>) Arrays.asList(type));
        }

        public Builder append(Collection<? extends Class<?>> types) {
            List<ClassLoader> classLoaders = new ArrayList<>(types.size());
            for (Class<?> type : types) {
                classLoaders.add(type.getClassLoader());
            }
            return append((List<? extends ClassLoader>) classLoaders);
        }

        public Builder append(ClassLoader... classLoader) {
            return append(Arrays.asList(classLoader));
        }

        public Builder append(List<? extends ClassLoader> classLoaders) {
            List<ClassLoader> filtered = new ArrayList<>(this.classLoaders.size() + classLoaders.size());
            filtered.addAll(this.classLoaders);
            Set<ClassLoader> registered = new HashSet<>(this.classLoaders);
            for (ClassLoader classLoader : classLoaders) {
                if (classLoader != null && registered.add(classLoader)) {
                    filtered.add(classLoader);
                }
            }
            return new Builder(filtered, this.sealed);
        }

        public Builder appendMostSpecific(Class<?>... type) {
            return appendMostSpecific((Collection<? extends Class<?>>) Arrays.asList(type));
        }

        public Builder appendMostSpecific(Collection<? extends Class<?>> types) {
            List<ClassLoader> classLoaders = new ArrayList<>(types.size());
            for (Class<?> type : types) {
                classLoaders.add(type.getClassLoader());
            }
            return appendMostSpecific((List<? extends ClassLoader>) classLoaders);
        }

        public Builder appendMostSpecific(ClassLoader... classLoader) {
            return appendMostSpecific(Arrays.asList(classLoader));
        }

        public Builder appendMostSpecific(List<? extends ClassLoader> classLoaders) {
            ClassLoader parent;
            List<ClassLoader> filtered = new ArrayList<>(this.classLoaders.size() + classLoaders.size());
            filtered.addAll(this.classLoaders);
            for (ClassLoader classLoader : classLoaders) {
                if (classLoader != null) {
                    ClassLoader candidate = classLoader;
                    do {
                        Iterator<ClassLoader> iterator = filtered.iterator();
                        while (iterator.hasNext()) {
                            ClassLoader previous = iterator.next();
                            if (previous.equals(candidate)) {
                                iterator.remove();
                            }
                        }
                        parent = candidate.getParent();
                        candidate = parent;
                    } while (parent != null);
                    Iterator<ClassLoader> it = filtered.iterator();
                    while (true) {
                        if (it.hasNext()) {
                            ClassLoader previous2 = it.next();
                            while (!previous2.equals(classLoader)) {
                                ClassLoader parent2 = previous2.getParent();
                                previous2 = parent2;
                                if (parent2 == null) {
                                    break;
                                }
                            }
                        } else {
                            filtered.add(classLoader);
                            break;
                        }
                    }
                }
            }
            return new Builder(filtered, this.sealed);
        }

        public Builder filter(ElementMatcher<? super ClassLoader> matcher) {
            List<ClassLoader> classLoaders = new ArrayList<>(this.classLoaders.size());
            for (ClassLoader classLoader : this.classLoaders) {
                if (matcher.matches(classLoader)) {
                    classLoaders.add(classLoader);
                }
            }
            return new Builder(classLoaders, this.sealed);
        }

        @SuppressFBWarnings(value = {"DP_CREATE_CLASSLOADER_INSIDE_DO_PRIVILEGED"}, justification = "Privilege is explicit user responsibility")
        public ClassLoader build() {
            return this.classLoaders.size() == 1 ? this.classLoaders.get(0) : new MultipleParentClassLoader(this.classLoaders);
        }

        public ClassLoader build(ClassLoader parent) {
            return (this.classLoaders.isEmpty() || (this.classLoaders.size() == 1 && this.classLoaders.contains(parent))) ? parent : filter(ElementMatchers.not(ElementMatchers.is(parent))).doBuild(parent);
        }

        @SuppressFBWarnings(value = {"DP_CREATE_CLASSLOADER_INSIDE_DO_PRIVILEGED"}, justification = "Privilege is explicit user responsibility")
        private ClassLoader doBuild(ClassLoader parent) {
            return new MultipleParentClassLoader(parent, this.classLoaders, this.sealed);
        }
    }
}
