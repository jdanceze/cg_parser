package com.sun.xml.bind.v2.model.impl;

import com.sun.xml.bind.api.impl.NameConverter;
import com.sun.xml.bind.v2.model.annotation.AnnotationReader;
import com.sun.xml.bind.v2.model.annotation.Locatable;
import com.sun.xml.bind.v2.model.core.TypeInfo;
import com.sun.xml.bind.v2.model.nav.Navigator;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/impl/TypeInfoImpl.class */
public abstract class TypeInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> implements TypeInfo<TypeT, ClassDeclT>, Locatable {
    private final Locatable upstream;
    protected final TypeInfoSetImpl<TypeT, ClassDeclT, FieldT, MethodT> owner;
    protected ModelBuilder<TypeT, ClassDeclT, FieldT, MethodT> builder;

    /* JADX INFO: Access modifiers changed from: protected */
    public TypeInfoImpl(ModelBuilder<TypeT, ClassDeclT, FieldT, MethodT> builder, Locatable upstream) {
        this.builder = builder;
        this.owner = builder.typeInfoSet;
        this.upstream = upstream;
    }

    @Override // com.sun.xml.bind.v2.model.annotation.Locatable
    public Locatable getUpstream() {
        return this.upstream;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void link() {
        this.builder = null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final Navigator<TypeT, ClassDeclT, FieldT, MethodT> nav() {
        return this.owner.nav;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final AnnotationReader<TypeT, ClassDeclT, FieldT, MethodT> reader() {
        return this.owner.reader;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final QName parseElementName(ClassDeclT clazz) {
        XmlRootElement e = (XmlRootElement) reader().getClassAnnotation(XmlRootElement.class, clazz, this);
        if (e == null) {
            return null;
        }
        String local = e.name();
        if (local.equals("##default")) {
            local = NameConverter.standard.toVariableName(nav().getClassShortName(clazz));
        }
        String nsUri = e.namespace();
        if (nsUri.equals("##default")) {
            XmlSchema xs = (XmlSchema) reader().getPackageAnnotation(XmlSchema.class, clazz, this);
            if (xs != null) {
                nsUri = xs.namespace();
            } else {
                nsUri = this.builder.defaultNsUri;
            }
        }
        return new QName(nsUri.intern(), local.intern());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final QName parseTypeName(ClassDeclT clazz) {
        return parseTypeName(clazz, (XmlType) reader().getClassAnnotation(XmlType.class, clazz, this));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final QName parseTypeName(ClassDeclT clazz, XmlType t) {
        String nsUri = "##default";
        String local = "##default";
        if (t != null) {
            nsUri = t.namespace();
            local = t.name();
        }
        if (local.length() == 0) {
            return null;
        }
        if (local.equals("##default")) {
            local = NameConverter.standard.toVariableName(nav().getClassShortName(clazz));
        }
        if (nsUri.equals("##default")) {
            XmlSchema xs = (XmlSchema) reader().getPackageAnnotation(XmlSchema.class, clazz, this);
            if (xs != null) {
                nsUri = xs.namespace();
            } else {
                nsUri = this.builder.defaultNsUri;
            }
        }
        return new QName(nsUri.intern(), local.intern());
    }
}
