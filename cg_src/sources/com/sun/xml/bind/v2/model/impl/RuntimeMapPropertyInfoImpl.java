package com.sun.xml.bind.v2.model.impl;

import com.sun.xml.bind.v2.model.core.NonElement;
import com.sun.xml.bind.v2.model.core.TypeInfo;
import com.sun.xml.bind.v2.model.impl.RuntimeClassInfoImpl;
import com.sun.xml.bind.v2.model.runtime.RuntimeMapPropertyInfo;
import com.sun.xml.bind.v2.model.runtime.RuntimeNonElement;
import com.sun.xml.bind.v2.runtime.reflect.Accessor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/impl/RuntimeMapPropertyInfoImpl.class */
class RuntimeMapPropertyInfoImpl extends MapPropertyInfoImpl<Type, Class, Field, Method> implements RuntimeMapPropertyInfo {
    private final Accessor acc;

    @Override // com.sun.xml.bind.v2.model.impl.PropertyInfoImpl, com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo
    public /* bridge */ /* synthetic */ Type getIndividualType() {
        return (Type) super.getIndividualType();
    }

    @Override // com.sun.xml.bind.v2.model.impl.PropertyInfoImpl, com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo
    public /* bridge */ /* synthetic */ Type getRawType() {
        return (Type) super.getRawType();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RuntimeMapPropertyInfoImpl(RuntimeClassInfoImpl classInfo, PropertySeed<Type, Class, Field, Method> seed) {
        super(classInfo, seed);
        this.acc = ((RuntimeClassInfoImpl.RuntimePropertySeed) seed).getAccessor();
    }

    @Override // com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo
    public Accessor getAccessor() {
        return this.acc;
    }

    @Override // com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo
    public boolean elementOnlyContent() {
        return true;
    }

    @Override // com.sun.xml.bind.v2.model.impl.MapPropertyInfoImpl, com.sun.xml.bind.v2.model.core.MapPropertyInfo
    /* renamed from: getKeyType */
    public NonElement<Type, Class> getKeyType2() {
        return (RuntimeNonElement) super.getKeyType();
    }

    @Override // com.sun.xml.bind.v2.model.impl.MapPropertyInfoImpl, com.sun.xml.bind.v2.model.core.MapPropertyInfo
    /* renamed from: getValueType */
    public NonElement<Type, Class> getValueType2() {
        return (RuntimeNonElement) super.getValueType();
    }

    @Override // com.sun.xml.bind.v2.model.impl.MapPropertyInfoImpl, com.sun.xml.bind.v2.model.core.PropertyInfo
    /* renamed from: ref */
    public Collection<? extends TypeInfo<Type, Class>> ref2() {
        return (List) super.ref();
    }
}
