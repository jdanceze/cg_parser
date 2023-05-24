package org.objenesis.strategy;

import org.objenesis.instantiator.ObjectInstantiator;
/* loaded from: gencallgraphv3.jar:objenesis-3.1.jar:org/objenesis/strategy/InstantiatorStrategy.class */
public interface InstantiatorStrategy {
    <T> ObjectInstantiator<T> newInstantiatorOf(Class<T> cls);
}
