package com.sun.xml.bind.v2.model.impl;

import com.sun.istack.FinalArrayList;
import com.sun.istack.localization.Localizable;
import com.sun.xml.bind.v2.model.core.ElementPropertyInfo;
import com.sun.xml.bind.v2.model.core.ID;
import com.sun.xml.bind.v2.model.core.PropertyKind;
import com.sun.xml.bind.v2.model.core.TypeInfo;
import com.sun.xml.bind.v2.runtime.IllegalAnnotationException;
import java.util.AbstractList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlList;
import javax.xml.namespace.QName;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/impl/ElementPropertyInfoImpl.class */
public class ElementPropertyInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> extends ERPropertyInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> implements ElementPropertyInfo<TypeT, ClassDeclT> {
    private List<TypeRefImpl<TypeT, ClassDeclT>> types;
    private final List<TypeInfo<TypeT, ClassDeclT>> ref;
    private Boolean isRequired;
    private final boolean isValueList;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ElementPropertyInfoImpl.class.desiredAssertionStatus();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ElementPropertyInfoImpl(ClassInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> parent, PropertySeed<TypeT, ClassDeclT, FieldT, MethodT> propertySeed) {
        super(parent, propertySeed);
        this.ref = new AbstractList<TypeInfo<TypeT, ClassDeclT>>() { // from class: com.sun.xml.bind.v2.model.impl.ElementPropertyInfoImpl.1
            @Override // java.util.AbstractList, java.util.List
            public TypeInfo<TypeT, ClassDeclT> get(int index) {
                return ElementPropertyInfoImpl.this.getTypes().get(index).getTarget();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return ElementPropertyInfoImpl.this.getTypes().size();
            }
        };
        this.isValueList = this.seed.hasAnnotation(XmlList.class);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.bind.v2.model.core.ElementPropertyInfo
    public List<? extends TypeRefImpl<TypeT, ClassDeclT>> getTypes() {
        XmlElement[] xmlElementArr;
        if (this.types == null) {
            this.types = new FinalArrayList();
            XmlElement[] ann = null;
            XmlElement xe = (XmlElement) this.seed.readAnnotation(XmlElement.class);
            XmlElements xes = (XmlElements) this.seed.readAnnotation(XmlElements.class);
            if (xe != null && xes != null) {
                this.parent.builder.reportError(new IllegalAnnotationException(Messages.MUTUALLY_EXCLUSIVE_ANNOTATIONS.format(nav().getClassName(this.parent.getClazz()) + '#' + this.seed.getName(), xe.annotationType().getName(), xes.annotationType().getName()), xe, xes));
            }
            this.isRequired = true;
            if (xe != null) {
                ann = new XmlElement[]{xe};
            } else if (xes != null) {
                ann = xes.value();
            }
            if (ann == null) {
                TypeT t = getIndividualType();
                if (!nav().isPrimitive(t) || isCollection()) {
                    this.isRequired = false;
                }
                this.types.add(createTypeRef(calcXmlName((XmlElement) null), t, isCollection(), null));
            } else {
                for (XmlElement item : ann) {
                    QName name = calcXmlName(item);
                    TypeT type = reader().getClassValue(item, "type");
                    if (nav().isSameType(type, nav().ref(XmlElement.DEFAULT.class))) {
                        type = getIndividualType();
                    }
                    if ((!nav().isPrimitive(type) || isCollection()) && !item.required()) {
                        this.isRequired = false;
                    }
                    this.types.add(createTypeRef(name, type, item.nillable(), getDefaultValue(item.defaultValue())));
                }
            }
            this.types = Collections.unmodifiableList(this.types);
            if (!$assertionsDisabled && this.types.contains(null)) {
                throw new AssertionError();
            }
        }
        return this.types;
    }

    private String getDefaultValue(String value) {
        if (value.equals(Localizable.NOT_LOCALIZABLE)) {
            return null;
        }
        return value;
    }

    protected TypeRefImpl<TypeT, ClassDeclT> createTypeRef(QName name, TypeT type, boolean isNillable, String defaultValue) {
        return new TypeRefImpl<>(this, name, type, isNillable, defaultValue);
    }

    @Override // com.sun.xml.bind.v2.model.core.ElementPropertyInfo
    public boolean isValueList() {
        return this.isValueList;
    }

    @Override // com.sun.xml.bind.v2.model.core.ElementPropertyInfo
    public boolean isRequired() {
        if (this.isRequired == null) {
            getTypes();
        }
        return this.isRequired.booleanValue();
    }

    @Override // com.sun.xml.bind.v2.model.core.PropertyInfo
    public List<? extends TypeInfo<TypeT, ClassDeclT>> ref() {
        return this.ref;
    }

    @Override // com.sun.xml.bind.v2.model.core.PropertyInfo
    public final PropertyKind kind() {
        return PropertyKind.ELEMENT;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.xml.bind.v2.model.impl.PropertyInfoImpl
    public void link() {
        super.link();
        for (TypeRefImpl<TypeT, ClassDeclT> ref : getTypes()) {
            ref.link();
        }
        if (isValueList()) {
            if (id() != ID.IDREF) {
                Iterator<TypeRefImpl<TypeT, ClassDeclT>> it = this.types.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    TypeRefImpl<TypeT, ClassDeclT> ref2 = it.next();
                    if (!ref2.getTarget().isSimpleType()) {
                        this.parent.builder.reportError(new IllegalAnnotationException(Messages.XMLLIST_NEEDS_SIMPLETYPE.format(nav().getTypeName(ref2.getTarget().getType())), this));
                        break;
                    }
                }
            }
            if (!isCollection()) {
                this.parent.builder.reportError(new IllegalAnnotationException(Messages.XMLLIST_ON_SINGLE_PROPERTY.format(new Object[0]), this));
            }
        }
    }
}
