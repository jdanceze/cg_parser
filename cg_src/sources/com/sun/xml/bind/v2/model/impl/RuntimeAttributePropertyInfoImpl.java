package com.sun.xml.bind.v2.model.impl;

import com.sun.xml.bind.v2.model.core.TypeInfo;
import com.sun.xml.bind.v2.model.runtime.RuntimeAttributePropertyInfo;
import com.sun.xml.bind.v2.model.runtime.RuntimeNonElement;
import com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/impl/RuntimeAttributePropertyInfoImpl.class */
class RuntimeAttributePropertyInfoImpl extends AttributePropertyInfoImpl<Type, Class, Field, Method> implements RuntimeAttributePropertyInfo {
    @Override // com.sun.xml.bind.v2.model.impl.PropertyInfoImpl, com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo
    public /* bridge */ /* synthetic */ Type getIndividualType() {
        return (Type) super.getIndividualType();
    }

    @Override // com.sun.xml.bind.v2.model.impl.PropertyInfoImpl, com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo
    public /* bridge */ /* synthetic */ Type getRawType() {
        return (Type) super.getRawType();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RuntimeAttributePropertyInfoImpl(RuntimeClassInfoImpl classInfo, PropertySeed<Type, Class, Field, Method> seed) {
        super(classInfo, seed);
    }

    @Override // com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo
    public boolean elementOnlyContent() {
        return true;
    }

    @Override // com.sun.xml.bind.v2.model.impl.SingleTypePropertyInfoImpl, com.sun.xml.bind.v2.model.core.AttributePropertyInfo, com.sun.xml.bind.v2.model.core.NonElementRef
    public RuntimeNonElement getTarget() {
        return (RuntimeNonElement) super.getTarget();
    }

    @Override // com.sun.xml.bind.v2.model.impl.SingleTypePropertyInfoImpl, com.sun.xml.bind.v2.model.core.PropertyInfo
    /* renamed from: ref */
    public Collection<? extends TypeInfo<Type, Class>> ref2() {
        return super.ref();
    }

    @Override // com.sun.xml.bind.v2.model.impl.SingleTypePropertyInfoImpl, com.sun.xml.bind.v2.model.core.NonElementRef
    public RuntimePropertyInfo getSource() {
        return this;
    }

    @Override // com.sun.xml.bind.v2.model.impl.SingleTypePropertyInfoImpl, com.sun.xml.bind.v2.model.impl.PropertyInfoImpl
    public void link() {
        getTransducer();
        super.link();
    }
}
