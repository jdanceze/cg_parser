package com.sun.xml.bind.v2.runtime.reflect;

import com.sun.xml.bind.api.AccessorException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/NullSafeAccessor.class */
public class NullSafeAccessor<B, V, P> extends Accessor<B, V> {
    private final Accessor<B, V> core;
    private final Lister<B, V, ?, P> lister;

    public NullSafeAccessor(Accessor<B, V> core, Lister<B, V, ?, P> lister) {
        super(core.getValueType());
        this.core = core;
        this.lister = lister;
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.Accessor
    public V get(B bean) throws AccessorException {
        V v = this.core.get(bean);
        if (v == null) {
            P pack = this.lister.startPacking(bean, this.core);
            this.lister.endPacking(pack, bean, this.core);
            v = this.core.get(bean);
        }
        return v;
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.Accessor
    public void set(B bean, V value) throws AccessorException {
        this.core.set(bean, value);
    }
}
