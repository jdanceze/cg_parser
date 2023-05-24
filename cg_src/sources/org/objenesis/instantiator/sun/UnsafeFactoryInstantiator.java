package org.objenesis.instantiator.sun;

import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.ObjectInstantiator;
import org.objenesis.instantiator.annotations.Instantiator;
import org.objenesis.instantiator.annotations.Typology;
import org.objenesis.instantiator.util.UnsafeUtils;
import sun.misc.Unsafe;
@Instantiator(Typology.STANDARD)
/* loaded from: gencallgraphv3.jar:objenesis-3.1.jar:org/objenesis/instantiator/sun/UnsafeFactoryInstantiator.class */
public class UnsafeFactoryInstantiator<T> implements ObjectInstantiator<T> {
    private final Unsafe unsafe = UnsafeUtils.getUnsafe();
    private final Class<T> type;

    public UnsafeFactoryInstantiator(Class<T> type) {
        this.type = type;
    }

    @Override // org.objenesis.instantiator.ObjectInstantiator
    public T newInstance() {
        try {
            return this.type.cast(this.unsafe.allocateInstance(this.type));
        } catch (InstantiationException e) {
            throw new ObjenesisException(e);
        }
    }
}
