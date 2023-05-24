package org.apache.tools.ant.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Vector;
import java.util.stream.Collectors;
@Deprecated
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/CollectionUtils.class */
public class CollectionUtils {
    @Deprecated
    public static final List EMPTY_LIST = Collections.EMPTY_LIST;

    @Deprecated
    public static boolean equals(Vector<?> v1, Vector<?> v2) {
        return Objects.equals(v1, v2);
    }

    @Deprecated
    public static boolean equals(Dictionary<?, ?> d1, Dictionary<?, ?> d2) {
        if (d1 == d2) {
            return true;
        }
        if (d1 == null || d2 == null || d1.size() != d2.size()) {
            return false;
        }
        return StreamUtils.enumerationAsStream(d1.keys()).allMatch(key -> {
            return d1.get(key).equals(d2.get(key));
        });
    }

    @Deprecated
    public static String flattenToString(Collection<?> c) {
        return (String) c.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    @Deprecated
    public static <K, V> void putAll(Dictionary<? super K, ? super V> m1, Dictionary<? extends K, ? extends V> m2) {
        StreamUtils.enumerationAsStream(m2.keys()).forEach(key -> {
            m1.put(key, m2.get(key));
        });
    }

    @Deprecated
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/CollectionUtils$EmptyEnumeration.class */
    public static final class EmptyEnumeration<E> implements Enumeration<E> {
        @Override // java.util.Enumeration
        public boolean hasMoreElements() {
            return false;
        }

        @Override // java.util.Enumeration
        public E nextElement() throws NoSuchElementException {
            throw new NoSuchElementException();
        }
    }

    @Deprecated
    public static <E> Enumeration<E> append(Enumeration<E> e1, Enumeration<E> e2) {
        return new CompoundEnumeration(e1, e2);
    }

    @Deprecated
    public static <E> Enumeration<E> asEnumeration(final Iterator<E> iter) {
        return new Enumeration<E>() { // from class: org.apache.tools.ant.util.CollectionUtils.1
            @Override // java.util.Enumeration
            public boolean hasMoreElements() {
                return iter.hasNext();
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [E, java.lang.Object] */
            @Override // java.util.Enumeration
            public E nextElement() {
                return iter.next();
            }
        };
    }

    @Deprecated
    public static <E> Iterator<E> asIterator(final Enumeration<E> e) {
        return new Iterator<E>() { // from class: org.apache.tools.ant.util.CollectionUtils.2
            @Override // java.util.Iterator
            public boolean hasNext() {
                return e.hasMoreElements();
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [E, java.lang.Object] */
            @Override // java.util.Iterator
            public E next() {
                return e.nextElement();
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Deprecated
    public static <T> Collection<T> asCollection(Iterator<? extends T> iter) {
        List<T> l = new ArrayList<>();
        Objects.requireNonNull(l);
        iter.forEachRemaining(this::add);
        return l;
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/CollectionUtils$CompoundEnumeration.class */
    private static final class CompoundEnumeration<E> implements Enumeration<E> {
        private final Enumeration<E> e1;
        private final Enumeration<E> e2;

        public CompoundEnumeration(Enumeration<E> e1, Enumeration<E> e2) {
            this.e1 = e1;
            this.e2 = e2;
        }

        @Override // java.util.Enumeration
        public boolean hasMoreElements() {
            return this.e1.hasMoreElements() || this.e2.hasMoreElements();
        }

        @Override // java.util.Enumeration
        public E nextElement() throws NoSuchElementException {
            if (this.e1.hasMoreElements()) {
                return this.e1.nextElement();
            }
            return this.e2.nextElement();
        }
    }

    @Deprecated
    public static int frequency(Collection<?> c, Object o) {
        if (c == null) {
            return 0;
        }
        return Collections.frequency(c, o);
    }

    private CollectionUtils() {
    }
}
