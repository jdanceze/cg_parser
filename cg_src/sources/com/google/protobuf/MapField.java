package com.google.protobuf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/MapField.class */
public class MapField<K, V> implements MutabilityOracle {
    private volatile boolean isMutable;
    private volatile StorageMode mode;
    private MutabilityAwareMap<K, V> mapData;
    private List<Message> listData;
    private final Converter<K, V> converter;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/MapField$Converter.class */
    public interface Converter<K, V> {
        Message convertKeyAndValueToMessage(K k, V v);

        void convertMessageToKeyAndValue(Message message, Map<K, V> map);

        Message getMessageDefaultInstance();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/MapField$StorageMode.class */
    public enum StorageMode {
        MAP,
        LIST,
        BOTH
    }

    /* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/MapField$ImmutableMessageConverter.class */
    private static class ImmutableMessageConverter<K, V> implements Converter<K, V> {
        private final MapEntry<K, V> defaultEntry;

        public ImmutableMessageConverter(MapEntry<K, V> defaultEntry) {
            this.defaultEntry = defaultEntry;
        }

        @Override // com.google.protobuf.MapField.Converter
        public Message convertKeyAndValueToMessage(K key, V value) {
            return this.defaultEntry.newBuilderForType().setKey(key).setValue(value).buildPartial();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.protobuf.MapField.Converter
        public void convertMessageToKeyAndValue(Message message, Map<K, V> map) {
            MapEntry<K, V> entry = (MapEntry) message;
            map.put(entry.getKey(), entry.getValue());
        }

        @Override // com.google.protobuf.MapField.Converter
        public Message getMessageDefaultInstance() {
            return this.defaultEntry;
        }
    }

    private MapField(Converter<K, V> converter, StorageMode mode, Map<K, V> mapData) {
        this.converter = converter;
        this.isMutable = true;
        this.mode = mode;
        this.mapData = new MutabilityAwareMap<>(this, mapData);
        this.listData = null;
    }

    private MapField(MapEntry<K, V> defaultEntry, StorageMode mode, Map<K, V> mapData) {
        this(new ImmutableMessageConverter(defaultEntry), mode, mapData);
    }

    public static <K, V> MapField<K, V> emptyMapField(MapEntry<K, V> defaultEntry) {
        return new MapField<>(defaultEntry, StorageMode.MAP, Collections.emptyMap());
    }

    public static <K, V> MapField<K, V> newMapField(MapEntry<K, V> defaultEntry) {
        return new MapField<>(defaultEntry, StorageMode.MAP, new LinkedHashMap());
    }

    private Message convertKeyAndValueToMessage(K key, V value) {
        return this.converter.convertKeyAndValueToMessage(key, value);
    }

    private void convertMessageToKeyAndValue(Message message, Map<K, V> map) {
        this.converter.convertMessageToKeyAndValue(message, map);
    }

    private List<Message> convertMapToList(MutabilityAwareMap<K, V> mapData) {
        List<Message> listData = new ArrayList<>();
        for (Map.Entry<K, V> entry : mapData.entrySet()) {
            listData.add(convertKeyAndValueToMessage(entry.getKey(), entry.getValue()));
        }
        return listData;
    }

    private MutabilityAwareMap<K, V> convertListToMap(List<Message> listData) {
        Map<K, V> mapData = new LinkedHashMap<>();
        for (Message item : listData) {
            convertMessageToKeyAndValue(item, mapData);
        }
        return new MutabilityAwareMap<>(this, mapData);
    }

    public Map<K, V> getMap() {
        if (this.mode == StorageMode.LIST) {
            synchronized (this) {
                if (this.mode == StorageMode.LIST) {
                    this.mapData = convertListToMap(this.listData);
                    this.mode = StorageMode.BOTH;
                }
            }
        }
        return Collections.unmodifiableMap(this.mapData);
    }

    public Map<K, V> getMutableMap() {
        if (this.mode != StorageMode.MAP) {
            if (this.mode == StorageMode.LIST) {
                this.mapData = convertListToMap(this.listData);
            }
            this.listData = null;
            this.mode = StorageMode.MAP;
        }
        return this.mapData;
    }

    public void mergeFrom(MapField<K, V> other) {
        getMutableMap().putAll(MapFieldLite.copy((Map) other.getMap()));
    }

    public void clear() {
        this.mapData = new MutabilityAwareMap<>(this, new LinkedHashMap());
        this.mode = StorageMode.MAP;
    }

    public boolean equals(Object object) {
        if (!(object instanceof MapField)) {
            return false;
        }
        MapField<K, V> other = (MapField) object;
        return MapFieldLite.equals((Map) getMap(), (Map) other.getMap());
    }

    public int hashCode() {
        return MapFieldLite.calculateHashCodeForMap(getMap());
    }

    public MapField<K, V> copy() {
        return new MapField<>(this.converter, StorageMode.MAP, MapFieldLite.copy((Map) getMap()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<Message> getList() {
        if (this.mode == StorageMode.MAP) {
            synchronized (this) {
                if (this.mode == StorageMode.MAP) {
                    this.listData = convertMapToList(this.mapData);
                    this.mode = StorageMode.BOTH;
                }
            }
        }
        return Collections.unmodifiableList(this.listData);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<Message> getMutableList() {
        if (this.mode != StorageMode.LIST) {
            if (this.mode == StorageMode.MAP) {
                this.listData = convertMapToList(this.mapData);
            }
            this.mapData = null;
            this.mode = StorageMode.LIST;
        }
        return this.listData;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Message getMapEntryMessageDefaultInstance() {
        return this.converter.getMessageDefaultInstance();
    }

    public void makeImmutable() {
        this.isMutable = false;
    }

    public boolean isMutable() {
        return this.isMutable;
    }

    @Override // com.google.protobuf.MutabilityOracle
    public void ensureMutable() {
        if (!isMutable()) {
            throw new UnsupportedOperationException();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/MapField$MutabilityAwareMap.class */
    public static class MutabilityAwareMap<K, V> implements Map<K, V> {
        private final MutabilityOracle mutabilityOracle;
        private final Map<K, V> delegate;

        MutabilityAwareMap(MutabilityOracle mutabilityOracle, Map<K, V> delegate) {
            this.mutabilityOracle = mutabilityOracle;
            this.delegate = delegate;
        }

        @Override // java.util.Map
        public int size() {
            return this.delegate.size();
        }

        @Override // java.util.Map
        public boolean isEmpty() {
            return this.delegate.isEmpty();
        }

        @Override // java.util.Map
        public boolean containsKey(Object key) {
            return this.delegate.containsKey(key);
        }

        @Override // java.util.Map
        public boolean containsValue(Object value) {
            return this.delegate.containsValue(value);
        }

        @Override // java.util.Map
        public V get(Object key) {
            return this.delegate.get(key);
        }

        @Override // java.util.Map
        public V put(K key, V value) {
            this.mutabilityOracle.ensureMutable();
            Internal.checkNotNull(key);
            Internal.checkNotNull(value);
            return this.delegate.put(key, value);
        }

        @Override // java.util.Map
        public V remove(Object key) {
            this.mutabilityOracle.ensureMutable();
            return this.delegate.remove(key);
        }

        @Override // java.util.Map
        public void putAll(Map<? extends K, ? extends V> m) {
            this.mutabilityOracle.ensureMutable();
            for (K key : m.keySet()) {
                Internal.checkNotNull(key);
                Internal.checkNotNull(m.get(key));
            }
            this.delegate.putAll(m);
        }

        @Override // java.util.Map
        public void clear() {
            this.mutabilityOracle.ensureMutable();
            this.delegate.clear();
        }

        @Override // java.util.Map
        public Set<K> keySet() {
            return new MutabilityAwareSet(this.mutabilityOracle, this.delegate.keySet());
        }

        @Override // java.util.Map
        public Collection<V> values() {
            return new MutabilityAwareCollection(this.mutabilityOracle, this.delegate.values());
        }

        @Override // java.util.Map
        public Set<Map.Entry<K, V>> entrySet() {
            return new MutabilityAwareSet(this.mutabilityOracle, this.delegate.entrySet());
        }

        @Override // java.util.Map
        public boolean equals(Object o) {
            return this.delegate.equals(o);
        }

        @Override // java.util.Map
        public int hashCode() {
            return this.delegate.hashCode();
        }

        public String toString() {
            return this.delegate.toString();
        }

        /* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/MapField$MutabilityAwareMap$MutabilityAwareCollection.class */
        private static class MutabilityAwareCollection<E> implements Collection<E> {
            private final MutabilityOracle mutabilityOracle;
            private final Collection<E> delegate;

            MutabilityAwareCollection(MutabilityOracle mutabilityOracle, Collection<E> delegate) {
                this.mutabilityOracle = mutabilityOracle;
                this.delegate = delegate;
            }

            @Override // java.util.Collection
            public int size() {
                return this.delegate.size();
            }

            @Override // java.util.Collection
            public boolean isEmpty() {
                return this.delegate.isEmpty();
            }

            @Override // java.util.Collection
            public boolean contains(Object o) {
                return this.delegate.contains(o);
            }

            @Override // java.util.Collection, java.lang.Iterable
            public Iterator<E> iterator() {
                return new MutabilityAwareIterator(this.mutabilityOracle, this.delegate.iterator());
            }

            @Override // java.util.Collection
            public Object[] toArray() {
                return this.delegate.toArray();
            }

            @Override // java.util.Collection
            public <T> T[] toArray(T[] a) {
                return (T[]) this.delegate.toArray(a);
            }

            @Override // java.util.Collection
            public boolean add(E e) {
                throw new UnsupportedOperationException();
            }

            @Override // java.util.Collection
            public boolean remove(Object o) {
                this.mutabilityOracle.ensureMutable();
                return this.delegate.remove(o);
            }

            @Override // java.util.Collection
            public boolean containsAll(Collection<?> c) {
                return this.delegate.containsAll(c);
            }

            @Override // java.util.Collection
            public boolean addAll(Collection<? extends E> c) {
                throw new UnsupportedOperationException();
            }

            @Override // java.util.Collection
            public boolean removeAll(Collection<?> c) {
                this.mutabilityOracle.ensureMutable();
                return this.delegate.removeAll(c);
            }

            @Override // java.util.Collection
            public boolean retainAll(Collection<?> c) {
                this.mutabilityOracle.ensureMutable();
                return this.delegate.retainAll(c);
            }

            @Override // java.util.Collection
            public void clear() {
                this.mutabilityOracle.ensureMutable();
                this.delegate.clear();
            }

            @Override // java.util.Collection
            public boolean equals(Object o) {
                return this.delegate.equals(o);
            }

            @Override // java.util.Collection
            public int hashCode() {
                return this.delegate.hashCode();
            }

            public String toString() {
                return this.delegate.toString();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/MapField$MutabilityAwareMap$MutabilityAwareSet.class */
        public static class MutabilityAwareSet<E> implements Set<E> {
            private final MutabilityOracle mutabilityOracle;
            private final Set<E> delegate;

            MutabilityAwareSet(MutabilityOracle mutabilityOracle, Set<E> delegate) {
                this.mutabilityOracle = mutabilityOracle;
                this.delegate = delegate;
            }

            @Override // java.util.Set, java.util.Collection
            public int size() {
                return this.delegate.size();
            }

            @Override // java.util.Set, java.util.Collection
            public boolean isEmpty() {
                return this.delegate.isEmpty();
            }

            @Override // java.util.Set, java.util.Collection
            public boolean contains(Object o) {
                return this.delegate.contains(o);
            }

            @Override // java.util.Set, java.util.Collection, java.lang.Iterable
            public Iterator<E> iterator() {
                return new MutabilityAwareIterator(this.mutabilityOracle, this.delegate.iterator());
            }

            @Override // java.util.Set, java.util.Collection
            public Object[] toArray() {
                return this.delegate.toArray();
            }

            @Override // java.util.Set, java.util.Collection
            public <T> T[] toArray(T[] a) {
                return (T[]) this.delegate.toArray(a);
            }

            @Override // java.util.Set, java.util.Collection
            public boolean add(E e) {
                this.mutabilityOracle.ensureMutable();
                return this.delegate.add(e);
            }

            @Override // java.util.Set, java.util.Collection
            public boolean remove(Object o) {
                this.mutabilityOracle.ensureMutable();
                return this.delegate.remove(o);
            }

            @Override // java.util.Set, java.util.Collection
            public boolean containsAll(Collection<?> c) {
                return this.delegate.containsAll(c);
            }

            @Override // java.util.Set, java.util.Collection
            public boolean addAll(Collection<? extends E> c) {
                this.mutabilityOracle.ensureMutable();
                return this.delegate.addAll(c);
            }

            @Override // java.util.Set, java.util.Collection
            public boolean retainAll(Collection<?> c) {
                this.mutabilityOracle.ensureMutable();
                return this.delegate.retainAll(c);
            }

            @Override // java.util.Set, java.util.Collection
            public boolean removeAll(Collection<?> c) {
                this.mutabilityOracle.ensureMutable();
                return this.delegate.removeAll(c);
            }

            @Override // java.util.Set, java.util.Collection
            public void clear() {
                this.mutabilityOracle.ensureMutable();
                this.delegate.clear();
            }

            @Override // java.util.Set, java.util.Collection
            public boolean equals(Object o) {
                return this.delegate.equals(o);
            }

            @Override // java.util.Set, java.util.Collection
            public int hashCode() {
                return this.delegate.hashCode();
            }

            public String toString() {
                return this.delegate.toString();
            }
        }

        /* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/MapField$MutabilityAwareMap$MutabilityAwareIterator.class */
        private static class MutabilityAwareIterator<E> implements Iterator<E> {
            private final MutabilityOracle mutabilityOracle;
            private final Iterator<E> delegate;

            MutabilityAwareIterator(MutabilityOracle mutabilityOracle, Iterator<E> delegate) {
                this.mutabilityOracle = mutabilityOracle;
                this.delegate = delegate;
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.delegate.hasNext();
            }

            @Override // java.util.Iterator
            public E next() {
                return this.delegate.next();
            }

            @Override // java.util.Iterator
            public void remove() {
                this.mutabilityOracle.ensureMutable();
                this.delegate.remove();
            }

            public boolean equals(Object obj) {
                return this.delegate.equals(obj);
            }

            public int hashCode() {
                return this.delegate.hashCode();
            }

            public String toString() {
                return this.delegate.toString();
            }
        }
    }
}
