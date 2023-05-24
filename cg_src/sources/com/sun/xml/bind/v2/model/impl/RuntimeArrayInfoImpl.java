package com.sun.xml.bind.v2.model.impl;

import com.sun.xml.bind.v2.model.annotation.Locatable;
import com.sun.xml.bind.v2.model.core.NonElement;
import com.sun.xml.bind.v2.model.runtime.RuntimeArrayInfo;
import com.sun.xml.bind.v2.model.runtime.RuntimeNonElement;
import com.sun.xml.bind.v2.runtime.Transducer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/impl/RuntimeArrayInfoImpl.class */
public final class RuntimeArrayInfoImpl extends ArrayInfoImpl<Type, Class, Field, Method> implements RuntimeArrayInfo {
    /* JADX INFO: Access modifiers changed from: package-private */
    public RuntimeArrayInfoImpl(RuntimeModelBuilder builder, Locatable upstream, Class arrayType) {
        super(builder, upstream, arrayType);
    }

    @Override // com.sun.xml.bind.v2.model.impl.ArrayInfoImpl, com.sun.xml.bind.v2.model.core.TypeInfo
    /* renamed from: getType */
    public Type getType2() {
        return (Class) super.getType();
    }

    @Override // com.sun.xml.bind.v2.model.impl.ArrayInfoImpl, com.sun.xml.bind.v2.model.core.ArrayInfo
    /* renamed from: getItemType */
    public NonElement<Type, Class> getItemType2() {
        return (RuntimeNonElement) super.getItemType();
    }

    @Override // com.sun.xml.bind.v2.model.runtime.RuntimeNonElement
    public <V> Transducer<V> getTransducer() {
        return null;
    }
}
