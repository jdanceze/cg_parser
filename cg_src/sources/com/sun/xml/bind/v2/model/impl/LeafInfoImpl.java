package com.sun.xml.bind.v2.model.impl;

import com.sun.xml.bind.v2.model.annotation.Locatable;
import com.sun.xml.bind.v2.model.core.LeafInfo;
import com.sun.xml.bind.v2.runtime.Location;
import javax.xml.namespace.QName;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/impl/LeafInfoImpl.class */
abstract class LeafInfoImpl<TypeT, ClassDeclT> implements LeafInfo<TypeT, ClassDeclT>, Location {
    private final TypeT type;
    private final QName typeName;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !LeafInfoImpl.class.desiredAssertionStatus();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public LeafInfoImpl(TypeT type, QName typeName) {
        if (!$assertionsDisabled && type == null) {
            throw new AssertionError();
        }
        this.type = type;
        this.typeName = typeName;
    }

    @Override // com.sun.xml.bind.v2.model.core.TypeInfo
    public TypeT getType() {
        return this.type;
    }

    @Override // com.sun.xml.bind.v2.model.core.TypeInfo
    public final boolean canBeReferencedByIDREF() {
        return false;
    }

    @Override // com.sun.xml.bind.v2.model.core.NonElement
    public QName getTypeName() {
        return this.typeName;
    }

    @Override // com.sun.xml.bind.v2.model.annotation.Locatable
    public Locatable getUpstream() {
        return null;
    }

    @Override // com.sun.xml.bind.v2.model.annotation.Locatable
    public Location getLocation() {
        return this;
    }

    @Override // com.sun.xml.bind.v2.model.core.NonElement
    public boolean isSimpleType() {
        return true;
    }

    public String toString() {
        return this.type.toString();
    }
}
