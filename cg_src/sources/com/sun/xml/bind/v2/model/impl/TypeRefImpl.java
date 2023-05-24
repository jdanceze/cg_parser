package com.sun.xml.bind.v2.model.impl;

import com.sun.xml.bind.v2.model.core.NonElement;
import com.sun.xml.bind.v2.model.core.PropertyInfo;
import com.sun.xml.bind.v2.model.core.TypeRef;
import javax.xml.namespace.QName;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/impl/TypeRefImpl.class */
public class TypeRefImpl<TypeT, ClassDeclT> implements TypeRef<TypeT, ClassDeclT> {
    private final QName elementName;
    private final TypeT type;
    protected final ElementPropertyInfoImpl<TypeT, ClassDeclT, ?, ?> owner;
    private NonElement<TypeT, ClassDeclT> ref;
    private final boolean isNillable;
    private String defaultValue;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !TypeRefImpl.class.desiredAssertionStatus();
    }

    public TypeRefImpl(ElementPropertyInfoImpl<TypeT, ClassDeclT, ?, ?> owner, QName elementName, TypeT type, boolean isNillable, String defaultValue) {
        this.owner = owner;
        this.elementName = elementName;
        this.type = type;
        this.isNillable = isNillable;
        this.defaultValue = defaultValue;
        if (!$assertionsDisabled && owner == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && elementName == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && type == null) {
            throw new AssertionError();
        }
    }

    @Override // com.sun.xml.bind.v2.model.core.NonElementRef
    public NonElement<TypeT, ClassDeclT> getTarget() {
        if (this.ref == null) {
            calcRef();
        }
        return this.ref;
    }

    @Override // com.sun.xml.bind.v2.model.core.TypeRef
    public QName getTagName() {
        return this.elementName;
    }

    @Override // com.sun.xml.bind.v2.model.core.TypeRef
    public boolean isNillable() {
        return this.isNillable;
    }

    @Override // com.sun.xml.bind.v2.model.core.TypeRef
    public String getDefaultValue() {
        return this.defaultValue;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void link() {
        calcRef();
    }

    private void calcRef() {
        this.ref = this.owner.parent.builder.getTypeInfo(this.type, this.owner);
        if (!$assertionsDisabled && this.ref == null) {
            throw new AssertionError();
        }
    }

    @Override // com.sun.xml.bind.v2.model.core.NonElementRef
    public PropertyInfo<TypeT, ClassDeclT> getSource() {
        return this.owner;
    }
}
