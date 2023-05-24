package com.sun.xml.bind.v2.model.impl;

import com.sun.xml.bind.v2.model.annotation.Locatable;
import com.sun.xml.bind.v2.model.core.ArrayInfo;
import com.sun.xml.bind.v2.model.core.NonElement;
import com.sun.xml.bind.v2.model.util.ArrayInfoUtil;
import com.sun.xml.bind.v2.runtime.IllegalAnnotationException;
import com.sun.xml.bind.v2.runtime.Location;
import javax.xml.namespace.QName;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/impl/ArrayInfoImpl.class */
public class ArrayInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> extends TypeInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> implements ArrayInfo<TypeT, ClassDeclT>, Location {
    private final NonElement<TypeT, ClassDeclT> itemType;
    private final QName typeName;
    private final TypeT arrayType;

    @Override // com.sun.xml.bind.v2.model.impl.TypeInfoImpl, com.sun.xml.bind.v2.model.annotation.Locatable
    public /* bridge */ /* synthetic */ Locatable getUpstream() {
        return super.getUpstream();
    }

    public ArrayInfoImpl(ModelBuilder<TypeT, ClassDeclT, FieldT, MethodT> builder, Locatable upstream, TypeT arrayType) {
        super(builder, upstream);
        this.arrayType = arrayType;
        TypeT componentType = nav().getComponentType(arrayType);
        this.itemType = builder.getTypeInfo(componentType, this);
        QName n = this.itemType.getTypeName();
        if (n == null) {
            builder.reportError(new IllegalAnnotationException(Messages.ANONYMOUS_ARRAY_ITEM.format(nav().getTypeName(componentType)), this));
            n = new QName("#dummy");
        }
        this.typeName = ArrayInfoUtil.calcArrayTypeName(n);
    }

    @Override // com.sun.xml.bind.v2.model.core.ArrayInfo
    public NonElement<TypeT, ClassDeclT> getItemType() {
        return this.itemType;
    }

    @Override // com.sun.xml.bind.v2.model.core.NonElement
    public QName getTypeName() {
        return this.typeName;
    }

    @Override // com.sun.xml.bind.v2.model.core.NonElement
    public boolean isSimpleType() {
        return false;
    }

    @Override // com.sun.xml.bind.v2.model.core.TypeInfo
    public TypeT getType() {
        return this.arrayType;
    }

    @Override // com.sun.xml.bind.v2.model.core.TypeInfo
    public final boolean canBeReferencedByIDREF() {
        return false;
    }

    @Override // com.sun.xml.bind.v2.model.annotation.Locatable
    public Location getLocation() {
        return this;
    }

    @Override // com.sun.xml.bind.v2.runtime.Location
    public String toString() {
        return nav().getTypeName(this.arrayType);
    }
}
