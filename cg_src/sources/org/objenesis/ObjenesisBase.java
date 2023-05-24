package org.objenesis;

import java.util.concurrent.ConcurrentHashMap;
import org.objenesis.instantiator.ObjectInstantiator;
import org.objenesis.strategy.InstantiatorStrategy;
/* loaded from: gencallgraphv3.jar:objenesis-3.1.jar:org/objenesis/ObjenesisBase.class */
public class ObjenesisBase implements Objenesis {
    protected final InstantiatorStrategy strategy;
    protected ConcurrentHashMap<String, ObjectInstantiator<?>> cache;

    public ObjenesisBase(InstantiatorStrategy strategy) {
        this(strategy, true);
    }

    public ObjenesisBase(InstantiatorStrategy strategy, boolean useCache) {
        if (strategy == null) {
            throw new IllegalArgumentException("A strategy can't be null");
        }
        this.strategy = strategy;
        this.cache = useCache ? new ConcurrentHashMap<>() : null;
    }

    public String toString() {
        return getClass().getName() + " using " + this.strategy.getClass().getName() + (this.cache == null ? " without" : " with") + " caching";
    }

    @Override // org.objenesis.Objenesis
    public <T> T newInstance(Class<T> clazz) {
        return getInstantiatorOf(clazz).newInstance();
    }

    @Override // org.objenesis.Objenesis
    public <T> ObjectInstantiator<T> getInstantiatorOf(Class<T> clazz) {
        if (clazz.isPrimitive()) {
            throw new IllegalArgumentException("Primitive types can't be instantiated in Java");
        }
        if (this.cache == null) {
            return this.strategy.newInstantiatorOf(clazz);
        }
        ObjectInstantiator<?> instantiator = this.cache.get(clazz.getName());
        if (instantiator == null) {
            ObjectInstantiator<?> newInstantiator = this.strategy.newInstantiatorOf(clazz);
            instantiator = this.cache.putIfAbsent(clazz.getName(), newInstantiator);
            if (instantiator == null) {
                instantiator = newInstantiator;
            }
        }
        return (ObjectInstantiator<T>) instantiator;
    }
}
