package com.sun.xml.bind.v2.runtime.reflect;

import com.sun.xml.bind.api.AccessorException;
import com.sun.xml.bind.v2.runtime.Coordinator;
import com.sun.xml.bind.v2.runtime.XMLSerializer;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.xml.sax.SAXException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/AdaptedLister.class */
public final class AdaptedLister<BeanT, PropT, InMemItemT, OnWireItemT, PackT> extends Lister<BeanT, PropT, OnWireItemT, PackT> {
    private final Lister<BeanT, PropT, InMemItemT, PackT> core;
    private final Class<? extends XmlAdapter<OnWireItemT, InMemItemT>> adapter;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AdaptedLister(Lister<BeanT, PropT, InMemItemT, PackT> core, Class<? extends XmlAdapter<OnWireItemT, InMemItemT>> adapter) {
        this.core = core;
        this.adapter = adapter;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public XmlAdapter<OnWireItemT, InMemItemT> getAdapter() {
        return Coordinator._getInstance().getAdapter(this.adapter);
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
    public ListIterator<OnWireItemT> iterator(PropT prop, XMLSerializer context) {
        return new ListIteratorImpl(this.core.iterator(prop, context), context);
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
    public PackT startPacking(BeanT bean, Accessor<BeanT, PropT> accessor) throws AccessorException {
        return this.core.startPacking(bean, accessor);
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
    public void addToPack(PackT pack, OnWireItemT item) throws AccessorException {
        try {
            InMemItemT r = getAdapter().unmarshal(item);
            this.core.addToPack(pack, r);
        } catch (Exception e) {
            throw new AccessorException(e);
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
    public void endPacking(PackT pack, BeanT bean, Accessor<BeanT, PropT> accessor) throws AccessorException {
        this.core.endPacking(pack, bean, accessor);
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.Lister
    public void reset(BeanT bean, Accessor<BeanT, PropT> accessor) throws AccessorException {
        this.core.reset(bean, accessor);
    }

    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/AdaptedLister$ListIteratorImpl.class */
    private final class ListIteratorImpl implements ListIterator<OnWireItemT> {
        private final ListIterator<InMemItemT> core;
        private final XMLSerializer serializer;

        public ListIteratorImpl(ListIterator<InMemItemT> core, XMLSerializer serializer) {
            this.core = core;
            this.serializer = serializer;
        }

        @Override // com.sun.xml.bind.v2.runtime.reflect.ListIterator
        public boolean hasNext() {
            return this.core.hasNext();
        }

        @Override // com.sun.xml.bind.v2.runtime.reflect.ListIterator
        public OnWireItemT next() throws SAXException, JAXBException {
            InMemItemT next = this.core.next();
            try {
                return (OnWireItemT) AdaptedLister.this.getAdapter().marshal(next);
            } catch (Exception e) {
                this.serializer.reportError(null, e);
                return null;
            }
        }
    }
}
