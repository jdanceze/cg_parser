package org.objenesis;

import org.objenesis.instantiator.ObjectInstantiator;
/* loaded from: gencallgraphv3.jar:objenesis-3.1.jar:org/objenesis/Objenesis.class */
public interface Objenesis {
    <T> T newInstance(Class<T> cls);

    <T> ObjectInstantiator<T> getInstantiatorOf(Class<T> cls);
}
