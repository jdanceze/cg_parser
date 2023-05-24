package com.sun.xml.bind.v2.model.impl;

import com.sun.xml.bind.v2.model.core.MapPropertyInfo;
import com.sun.xml.bind.v2.model.core.NonElement;
import com.sun.xml.bind.v2.model.core.PropertyKind;
import com.sun.xml.bind.v2.model.core.TypeInfo;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.namespace.QName;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/impl/MapPropertyInfoImpl.class */
public class MapPropertyInfoImpl<T, C, F, M> extends PropertyInfoImpl<T, C, F, M> implements MapPropertyInfo<T, C> {
    private final QName xmlName;
    private boolean nil;
    private final T keyType;
    private final T valueType;
    private NonElement<T, C> keyTypeInfo;
    private NonElement<T, C> valueTypeInfo;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !MapPropertyInfoImpl.class.desiredAssertionStatus();
    }

    public MapPropertyInfoImpl(ClassInfoImpl<T, C, F, M> ci, PropertySeed<T, C, F, M> seed) {
        super(ci, seed);
        XmlElementWrapper xe = (XmlElementWrapper) seed.readAnnotation(XmlElementWrapper.class);
        this.xmlName = calcXmlName(xe);
        this.nil = xe != null && xe.nillable();
        T raw = getRawType();
        T bt = nav().getBaseClass(raw, nav().asDecl(Map.class));
        if (!$assertionsDisabled && bt == null) {
            throw new AssertionError();
        }
        if (nav().isParameterizedType(bt)) {
            this.keyType = nav().getTypeArgument(bt, 0);
            this.valueType = nav().getTypeArgument(bt, 1);
            return;
        }
        T ref = nav().ref(Object.class);
        this.valueType = ref;
        this.keyType = ref;
    }

    @Override // com.sun.xml.bind.v2.model.core.PropertyInfo
    public Collection<? extends TypeInfo<T, C>> ref() {
        return Arrays.asList(getKeyType(), getValueType());
    }

    @Override // com.sun.xml.bind.v2.model.core.PropertyInfo
    public final PropertyKind kind() {
        return PropertyKind.MAP;
    }

    @Override // com.sun.xml.bind.v2.model.core.MapPropertyInfo
    public QName getXmlName() {
        return this.xmlName;
    }

    @Override // com.sun.xml.bind.v2.model.core.MapPropertyInfo
    public boolean isCollectionNillable() {
        return this.nil;
    }

    @Override // com.sun.xml.bind.v2.model.core.MapPropertyInfo
    public NonElement<T, C> getKeyType() {
        if (this.keyTypeInfo == null) {
            this.keyTypeInfo = getTarget(this.keyType);
        }
        return this.keyTypeInfo;
    }

    @Override // com.sun.xml.bind.v2.model.core.MapPropertyInfo
    public NonElement<T, C> getValueType() {
        if (this.valueTypeInfo == null) {
            this.valueTypeInfo = getTarget(this.valueType);
        }
        return this.valueTypeInfo;
    }

    public NonElement<T, C> getTarget(T type) {
        if ($assertionsDisabled || this.parent.builder != null) {
            return this.parent.builder.getTypeInfo(type, this);
        }
        throw new AssertionError("this method must be called during the build stage");
    }
}
