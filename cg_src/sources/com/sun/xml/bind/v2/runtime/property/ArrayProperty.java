package com.sun.xml.bind.v2.runtime.property;

import com.sun.xml.bind.api.AccessorException;
import com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo;
import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.bind.v2.runtime.reflect.Accessor;
import com.sun.xml.bind.v2.runtime.reflect.Lister;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/property/ArrayProperty.class */
abstract class ArrayProperty<BeanT, ListT, ItemT> extends PropertyImpl<BeanT> {
    protected final Accessor<BeanT, ListT> acc;
    protected final Lister<BeanT, ListT, ItemT, Object> lister;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ArrayProperty.class.desiredAssertionStatus();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ArrayProperty(JAXBContextImpl context, RuntimePropertyInfo prop) {
        super(context, prop);
        if (!$assertionsDisabled && !prop.isCollection()) {
            throw new AssertionError();
        }
        this.lister = Lister.create(Utils.REFLECTION_NAVIGATOR.erasure(prop.getRawType()), prop.id(), prop.getAdapter());
        if (!$assertionsDisabled && this.lister == null) {
            throw new AssertionError();
        }
        this.acc = prop.getAccessor().optimize(context);
        if (!$assertionsDisabled && this.acc == null) {
            throw new AssertionError();
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.property.Property
    public void reset(BeanT o) throws AccessorException {
        this.lister.reset(o, this.acc);
    }

    @Override // com.sun.xml.bind.v2.runtime.property.Property
    public final String getIdValue(BeanT bean) {
        return null;
    }
}
