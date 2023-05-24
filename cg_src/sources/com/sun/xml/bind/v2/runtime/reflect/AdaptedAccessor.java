package com.sun.xml.bind.v2.runtime.reflect;

import com.sun.xml.bind.api.AccessorException;
import com.sun.xml.bind.v2.ClassFactory;
import com.sun.xml.bind.v2.runtime.Coordinator;
import javax.xml.bind.annotation.adapters.XmlAdapter;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/AdaptedAccessor.class */
public final class AdaptedAccessor<BeanT, InMemValueT, OnWireValueT> extends Accessor<BeanT, OnWireValueT> {
    private final Accessor<BeanT, InMemValueT> core;
    private final Class<? extends XmlAdapter<OnWireValueT, InMemValueT>> adapter;
    private XmlAdapter<OnWireValueT, InMemValueT> staticAdapter;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AdaptedAccessor(Class<OnWireValueT> targetType, Accessor<BeanT, InMemValueT> extThis, Class<? extends XmlAdapter<OnWireValueT, InMemValueT>> adapter) {
        super(targetType);
        this.core = extThis;
        this.adapter = adapter;
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.Accessor
    public boolean isAdapted() {
        return true;
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.Accessor
    public OnWireValueT get(BeanT bean) throws AccessorException {
        InMemValueT v = this.core.get(bean);
        XmlAdapter<OnWireValueT, InMemValueT> a = getAdapter();
        try {
            return a.marshal(v);
        } catch (Exception e) {
            throw new AccessorException(e);
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.Accessor
    public void set(BeanT bean, OnWireValueT o) throws AccessorException {
        XmlAdapter<OnWireValueT, InMemValueT> a = getAdapter();
        try {
            this.core.set(bean, o == null ? null : a.unmarshal(o));
        } catch (Exception e) {
            throw new AccessorException(e);
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.Accessor
    public Object getUnadapted(BeanT bean) throws AccessorException {
        return this.core.getUnadapted(bean);
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.Accessor
    public void setUnadapted(BeanT bean, Object value) throws AccessorException {
        this.core.setUnadapted(bean, value);
    }

    private XmlAdapter<OnWireValueT, InMemValueT> getAdapter() {
        Coordinator coordinator = Coordinator._getInstance();
        if (coordinator != null) {
            return coordinator.getAdapter(this.adapter);
        }
        synchronized (this) {
            if (this.staticAdapter == null) {
                this.staticAdapter = (XmlAdapter) ClassFactory.create(this.adapter);
            }
        }
        return this.staticAdapter;
    }
}
