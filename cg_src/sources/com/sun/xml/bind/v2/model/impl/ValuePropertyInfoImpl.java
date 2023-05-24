package com.sun.xml.bind.v2.model.impl;

import com.sun.xml.bind.v2.model.core.PropertyKind;
import com.sun.xml.bind.v2.model.core.ValuePropertyInfo;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/impl/ValuePropertyInfoImpl.class */
public class ValuePropertyInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> extends SingleTypePropertyInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> implements ValuePropertyInfo<TypeT, ClassDeclT> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public ValuePropertyInfoImpl(ClassInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> parent, PropertySeed<TypeT, ClassDeclT, FieldT, MethodT> seed) {
        super(parent, seed);
    }

    @Override // com.sun.xml.bind.v2.model.core.PropertyInfo
    public PropertyKind kind() {
        return PropertyKind.VALUE;
    }
}
