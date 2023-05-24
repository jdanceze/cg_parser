package soot.jimple.infoflow.collect;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/collect/ConcurrentCountingMap.class */
public class ConcurrentCountingMap<T> implements ConcurrentMap<T, Integer> {
    private final ConcurrentMap<T, AtomicInteger> map;
    private final ReentrantLock lock;
    private LockingMode lockingMode;
    private AtomicInteger changeCounter;
    private static volatile /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$collect$ConcurrentCountingMap$LockingMode;

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/collect/ConcurrentCountingMap$LockingMode.class */
    public enum LockingMode {
        NoLocking,
        Fast,
        Safe;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static LockingMode[] valuesCustom() {
            LockingMode[] valuesCustom = values();
            int length = valuesCustom.length;
            LockingMode[] lockingModeArr = new LockingMode[length];
            System.arraycopy(valuesCustom, 0, lockingModeArr, 0, length);
            return lockingModeArr;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.concurrent.ConcurrentMap, java.util.Map
    public /* bridge */ /* synthetic */ Object replace(Object obj, Object obj2) {
        return replace((ConcurrentCountingMap<T>) obj, (Integer) obj2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.concurrent.ConcurrentMap, java.util.Map
    public /* bridge */ /* synthetic */ boolean replace(Object obj, Object obj2, Object obj3) {
        return replace((ConcurrentCountingMap<T>) obj, (Integer) obj2, (Integer) obj3);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.concurrent.ConcurrentMap, java.util.Map
    public /* bridge */ /* synthetic */ Object putIfAbsent(Object obj, Object obj2) {
        return putIfAbsent((ConcurrentCountingMap<T>) obj, (Integer) obj2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Map
    public /* bridge */ /* synthetic */ Object put(Object obj, Object obj2) {
        return put((ConcurrentCountingMap<T>) obj, (Integer) obj2);
    }

    static /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$collect$ConcurrentCountingMap$LockingMode() {
        int[] iArr = $SWITCH_TABLE$soot$jimple$infoflow$collect$ConcurrentCountingMap$LockingMode;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[LockingMode.valuesCustom().length];
        try {
            iArr2[LockingMode.Fast.ordinal()] = 2;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[LockingMode.NoLocking.ordinal()] = 1;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[LockingMode.Safe.ordinal()] = 3;
        } catch (NoSuchFieldError unused3) {
        }
        $SWITCH_TABLE$soot$jimple$infoflow$collect$ConcurrentCountingMap$LockingMode = iArr2;
        return iArr2;
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/collect/ConcurrentCountingMap$Entry.class */
    public class Entry implements Map.Entry<T, Integer> {
        private final Map.Entry<T, AtomicInteger> parentEntry;
        private static volatile /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$collect$ConcurrentCountingMap$LockingMode;

        static /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$collect$ConcurrentCountingMap$LockingMode() {
            int[] iArr = $SWITCH_TABLE$soot$jimple$infoflow$collect$ConcurrentCountingMap$LockingMode;
            if (iArr != null) {
                return iArr;
            }
            int[] iArr2 = new int[LockingMode.valuesCustom().length];
            try {
                iArr2[LockingMode.Fast.ordinal()] = 2;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr2[LockingMode.NoLocking.ordinal()] = 1;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr2[LockingMode.Safe.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            $SWITCH_TABLE$soot$jimple$infoflow$collect$ConcurrentCountingMap$LockingMode = iArr2;
            return iArr2;
        }

        private Entry(Map.Entry<T, AtomicInteger> parentEntry) {
            this.parentEntry = parentEntry;
        }

        /* synthetic */ Entry(ConcurrentCountingMap concurrentCountingMap, Map.Entry entry, Entry entry2) {
            this(entry);
        }

        @Override // java.util.Map.Entry
        public T getKey() {
            return this.parentEntry.getKey();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Map.Entry
        public Integer getValue() {
            AtomicInteger i = this.parentEntry.getValue();
            return Integer.valueOf(i == null ? 0 : i.get());
        }

        @Override // java.util.Map.Entry
        public Integer setValue(Integer value) {
            try {
                switch ($SWITCH_TABLE$soot$jimple$infoflow$collect$ConcurrentCountingMap$LockingMode()[ConcurrentCountingMap.this.lockingMode.ordinal()]) {
                    case 2:
                        if (ConcurrentCountingMap.this.lock.isLocked()) {
                            ConcurrentCountingMap.this.lock.lock();
                            break;
                        }
                        break;
                    case 3:
                        ConcurrentCountingMap.this.lock.lock();
                        break;
                }
                AtomicInteger i = this.parentEntry.setValue(new AtomicInteger(value.intValue()));
                return Integer.valueOf(i == null ? 0 : i.get());
            } finally {
                if (ConcurrentCountingMap.this.lock.isHeldByCurrentThread()) {
                    ConcurrentCountingMap.this.lock.unlock();
                }
            }
        }
    }

    public ConcurrentCountingMap() {
        this.lock = new ReentrantLock();
        this.lockingMode = LockingMode.NoLocking;
        this.changeCounter = new AtomicInteger();
        this.map = new ConcurrentHashMap();
    }

    public ConcurrentCountingMap(int size) {
        this.lock = new ReentrantLock();
        this.lockingMode = LockingMode.NoLocking;
        this.changeCounter = new AtomicInteger();
        this.map = new ConcurrentHashMap(size);
    }

    public ConcurrentCountingMap(Map<T, AtomicInteger> map) {
        this.lock = new ReentrantLock();
        this.lockingMode = LockingMode.NoLocking;
        this.changeCounter = new AtomicInteger();
        this.map = new ConcurrentHashMap(map);
    }

    @Override // java.util.Map
    public int size() {
        return this.map.size();
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override // java.util.Map
    public boolean containsKey(Object key) {
        return this.map.containsKey(key);
    }

    @Override // java.util.Map
    public boolean containsValue(Object value) {
        if (value instanceof Integer) {
            AtomicInteger i = new AtomicInteger(((Integer) value).intValue());
            return this.map.containsValue(i);
        }
        return false;
    }

    @Override // java.util.Map
    public Integer get(Object key) {
        AtomicInteger i = this.map.get(key);
        return Integer.valueOf(i == null ? 0 : i.get());
    }

    public Integer put(T key, Integer value) {
        try {
            switch ($SWITCH_TABLE$soot$jimple$infoflow$collect$ConcurrentCountingMap$LockingMode()[this.lockingMode.ordinal()]) {
                case 2:
                    if (this.lock.isLocked()) {
                        this.lock.lock();
                        break;
                    }
                    break;
                case 3:
                    this.lock.lock();
                    break;
            }
            AtomicInteger old = this.map.put(key, value == null ? null : new AtomicInteger(value.intValue()));
            this.changeCounter.incrementAndGet();
            return Integer.valueOf(old == null ? 0 : old.get());
        } finally {
            if (this.lock.isHeldByCurrentThread()) {
                this.lock.unlock();
            }
        }
    }

    @Override // java.util.Map
    public Integer remove(Object key) {
        try {
            switch ($SWITCH_TABLE$soot$jimple$infoflow$collect$ConcurrentCountingMap$LockingMode()[this.lockingMode.ordinal()]) {
                case 2:
                    if (this.lock.isLocked()) {
                        this.lock.lock();
                        break;
                    }
                    break;
                case 3:
                    this.lock.lock();
                    break;
            }
            AtomicInteger old = this.map.remove(key);
            this.changeCounter.incrementAndGet();
            return Integer.valueOf(old == null ? 0 : old.get());
        } finally {
            if (this.lock.isHeldByCurrentThread()) {
                this.lock.unlock();
            }
        }
    }

    @Override // java.util.Map
    public void putAll(Map<? extends T, ? extends Integer> m) {
        try {
            switch ($SWITCH_TABLE$soot$jimple$infoflow$collect$ConcurrentCountingMap$LockingMode()[this.lockingMode.ordinal()]) {
                case 2:
                    if (this.lock.isLocked()) {
                        this.lock.lock();
                        break;
                    }
                    break;
                case 3:
                    this.lock.lock();
                    break;
            }
            for (T t : m.keySet()) {
                Integer i = m.get(t);
                this.map.put(t, i == null ? null : new AtomicInteger(i.intValue()));
                this.changeCounter.incrementAndGet();
            }
        } finally {
            if (this.lock.isHeldByCurrentThread()) {
                this.lock.unlock();
            }
        }
    }

    @Override // java.util.Map
    public void clear() {
        try {
            switch ($SWITCH_TABLE$soot$jimple$infoflow$collect$ConcurrentCountingMap$LockingMode()[this.lockingMode.ordinal()]) {
                case 2:
                    if (this.lock.isLocked()) {
                        this.lock.lock();
                        break;
                    }
                    break;
                case 3:
                    this.lock.lock();
                    break;
            }
            this.map.clear();
            this.changeCounter.incrementAndGet();
        } finally {
            if (this.lock.isHeldByCurrentThread()) {
                this.lock.unlock();
            }
        }
    }

    @Override // java.util.Map
    public Set<T> keySet() {
        return this.map.keySet();
    }

    @Override // java.util.Map
    public Collection<Integer> values() {
        return (Collection) this.map.values().stream().map(i -> {
            return Integer.valueOf(i == null ? 0 : i.get());
        }).collect(Collectors.toSet());
    }

    @Override // java.util.Map
    public Set<Map.Entry<T, Integer>> entrySet() {
        return (Set) this.map.entrySet().stream().map(entry -> {
            return new Entry(this, entry, null);
        }).collect(Collectors.toSet());
    }

    public Integer putIfAbsent(T key, Integer value) {
        try {
            switch ($SWITCH_TABLE$soot$jimple$infoflow$collect$ConcurrentCountingMap$LockingMode()[this.lockingMode.ordinal()]) {
                case 2:
                    if (this.lock.isLocked()) {
                        this.lock.lock();
                        break;
                    }
                    break;
                case 3:
                    this.lock.lock();
                    break;
            }
            AtomicInteger i = this.map.computeIfAbsent(key, obj -> {
                return new AtomicInteger(value.intValue());
            });
            if (i == null) {
                this.changeCounter.incrementAndGet();
                return 0;
            }
            Integer valueOf = Integer.valueOf(i.get());
            if (this.lock.isHeldByCurrentThread()) {
                this.lock.unlock();
            }
            return valueOf;
        } finally {
            if (this.lock.isHeldByCurrentThread()) {
                this.lock.unlock();
            }
        }
    }

    @Override // java.util.concurrent.ConcurrentMap, java.util.Map
    public boolean remove(Object key, Object value) {
        try {
            switch ($SWITCH_TABLE$soot$jimple$infoflow$collect$ConcurrentCountingMap$LockingMode()[this.lockingMode.ordinal()]) {
                case 2:
                    if (this.lock.isLocked()) {
                        this.lock.lock();
                        break;
                    }
                    break;
                case 3:
                    this.lock.lock();
                    break;
            }
            if (value instanceof Integer) {
                boolean res = this.map.remove(key, new AtomicInteger(((Integer) value).intValue()));
                this.changeCounter.incrementAndGet();
                return res;
            } else if (!this.lock.isHeldByCurrentThread()) {
                return false;
            } else {
                this.lock.unlock();
                return false;
            }
        } finally {
            if (this.lock.isHeldByCurrentThread()) {
                this.lock.unlock();
            }
        }
    }

    public boolean replace(T key, Integer oldValue, Integer newValue) {
        try {
            switch ($SWITCH_TABLE$soot$jimple$infoflow$collect$ConcurrentCountingMap$LockingMode()[this.lockingMode.ordinal()]) {
                case 2:
                    if (this.lock.isLocked()) {
                        this.lock.lock();
                        break;
                    }
                    break;
                case 3:
                    this.lock.lock();
                    break;
            }
            if (oldValue != null && newValue != null) {
                boolean res = this.map.replace(key, new AtomicInteger(oldValue.intValue()), new AtomicInteger(newValue.intValue()));
                this.changeCounter.incrementAndGet();
                return res;
            } else if (!this.lock.isHeldByCurrentThread()) {
                return false;
            } else {
                this.lock.unlock();
                return false;
            }
        } finally {
            if (this.lock.isHeldByCurrentThread()) {
                this.lock.unlock();
            }
        }
    }

    public Integer replace(T key, Integer value) {
        try {
            switch ($SWITCH_TABLE$soot$jimple$infoflow$collect$ConcurrentCountingMap$LockingMode()[this.lockingMode.ordinal()]) {
                case 2:
                    if (this.lock.isLocked()) {
                        this.lock.lock();
                        break;
                    }
                    break;
                case 3:
                    this.lock.lock();
                    break;
            }
            if (value == null) {
            }
            AtomicInteger i = this.map.replace(key, new AtomicInteger(value.intValue()));
            this.changeCounter.incrementAndGet();
            Integer valueOf = Integer.valueOf(i == null ? 0 : i.get());
            if (this.lock.isHeldByCurrentThread()) {
                this.lock.unlock();
            }
            return valueOf;
        } finally {
            if (this.lock.isHeldByCurrentThread()) {
                this.lock.unlock();
            }
        }
    }

    public int increment(T key) {
        return increment(key, 1);
    }

    public int increment(T key, int delta) {
        try {
            switch ($SWITCH_TABLE$soot$jimple$infoflow$collect$ConcurrentCountingMap$LockingMode()[this.lockingMode.ordinal()]) {
                case 2:
                    if (this.lock.isLocked()) {
                        this.lock.lock();
                        break;
                    }
                    break;
                case 3:
                    this.lock.lock();
                    break;
            }
            AtomicInteger i = this.map.computeIfAbsent(key, obj -> {
                return new AtomicInteger(0);
            });
            this.changeCounter.incrementAndGet();
            int val = 0;
            for (int j = 0; j < delta; j++) {
                val = i.incrementAndGet();
            }
            return val;
        } finally {
            if (this.lock.isHeldByCurrentThread()) {
                this.lock.unlock();
            }
        }
    }

    public int decrement(T key) {
        try {
            switch ($SWITCH_TABLE$soot$jimple$infoflow$collect$ConcurrentCountingMap$LockingMode()[this.lockingMode.ordinal()]) {
                case 2:
                    if (this.lock.isLocked()) {
                        this.lock.lock();
                        break;
                    }
                    break;
                case 3:
                    this.lock.lock();
                    break;
            }
            AtomicInteger i = this.map.get(key);
            if (i == null) {
            }
            int res = i.decrementAndGet();
            this.changeCounter.incrementAndGet();
            if (this.lock.isHeldByCurrentThread()) {
                this.lock.unlock();
            }
            return res;
        } finally {
            if (this.lock.isHeldByCurrentThread()) {
                this.lock.unlock();
            }
        }
    }

    public Set<T> getByValue(int value) {
        Set<T> set = new HashSet<>();
        for (Map.Entry<T, AtomicInteger> e : this.map.entrySet()) {
            AtomicInteger atomicInt = e.getValue();
            if (atomicInt != null && atomicInt.get() == value) {
                set.add(e.getKey());
            }
        }
        return set;
    }

    public void setLockingMode(LockingMode lockingMode) {
        this.lockingMode = lockingMode;
    }

    public ConcurrentCountingMap<T> snapshot() {
        try {
            this.lock.lock();
            ConcurrentCountingMap<T> snapshot = new ConcurrentCountingMap<>();
            for (T key : this.map.keySet()) {
                snapshot.put((ConcurrentCountingMap<T>) key, Integer.valueOf(this.map.get(key).get()));
            }
            return snapshot;
        } finally {
            this.lock.unlock();
        }
    }

    public ConcurrentCountingMap<T> snapshot(Collection<T> subset) {
        try {
            this.lock.lock();
            ConcurrentCountingMap<T> snapshot = new ConcurrentCountingMap<>(subset.size());
            for (T key : subset) {
                AtomicInteger atomic = this.map.get(key);
                if (atomic != null) {
                    snapshot.put((ConcurrentCountingMap<T>) key, Integer.valueOf(atomic.get()));
                }
            }
            return snapshot;
        } finally {
            this.lock.unlock();
        }
    }

    public int getChangeCounter() {
        return this.changeCounter.get();
    }
}
