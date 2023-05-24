package com.sun.xml.bind.v2.model.impl;

import com.sun.xml.bind.api.impl.NameConverter;
import com.sun.xml.bind.v2.model.core.AttributePropertyInfo;
import com.sun.xml.bind.v2.model.core.PropertyKind;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.namespace.QName;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/impl/AttributePropertyInfoImpl.class */
public class AttributePropertyInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> extends SingleTypePropertyInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> implements AttributePropertyInfo<TypeT, ClassDeclT> {
    private final QName xmlName;
    private final boolean isRequired;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !AttributePropertyInfoImpl.class.desiredAssertionStatus();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AttributePropertyInfoImpl(ClassInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> parent, PropertySeed<TypeT, ClassDeclT, FieldT, MethodT> seed) {
        super(parent, seed);
        XmlAttribute att = (XmlAttribute) seed.readAnnotation(XmlAttribute.class);
        if (!$assertionsDisabled && att == null) {
            throw new AssertionError();
        }
        if (att.required()) {
            this.isRequired = true;
        } else {
            this.isRequired = nav().isPrimitive(getIndividualType());
        }
        this.xmlName = calcXmlName(att);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private QName calcXmlName(XmlAttribute att) {
        String uri = att.namespace();
        String local = att.name();
        if (local.equals("##default")) {
            local = NameConverter.standard.toVariableName(getName());
        }
        if (uri.equals("##default")) {
            XmlSchema xs = (XmlSchema) reader().getPackageAnnotation(XmlSchema.class, this.parent.getClazz(), this);
            if (xs != null) {
                switch (xs.attributeFormDefault()) {
                    case QUALIFIED:
                        uri = this.parent.getTypeName().getNamespaceURI();
                        if (uri.length() == 0) {
                            uri = this.parent.builder.defaultNsUri;
                            break;
                        }
                        break;
                    case UNQUALIFIED:
                    case UNSET:
                        uri = "";
                        break;
                }
            } else {
                uri = "";
            }
        }
        return new QName(uri.intern(), local.intern());
    }

    @Override // com.sun.xml.bind.v2.model.core.AttributePropertyInfo
    public boolean isRequired() {
        return this.isRequired;
    }

    @Override // com.sun.xml.bind.v2.model.core.AttributePropertyInfo
    public final QName getXmlName() {
        return this.xmlName;
    }

    @Override // com.sun.xml.bind.v2.model.core.PropertyInfo
    public final PropertyKind kind() {
        return PropertyKind.ATTRIBUTE;
    }
}
