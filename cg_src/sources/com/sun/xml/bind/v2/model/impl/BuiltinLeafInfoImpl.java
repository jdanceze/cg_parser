package com.sun.xml.bind.v2.model.impl;

import com.sun.xml.bind.v2.model.annotation.Locatable;
import com.sun.xml.bind.v2.model.core.BuiltinLeafInfo;
import com.sun.xml.bind.v2.model.core.Element;
import com.sun.xml.bind.v2.model.nav.Navigator;
import com.sun.xml.bind.v2.runtime.Location;
import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/impl/BuiltinLeafInfoImpl.class */
public class BuiltinLeafInfoImpl<TypeT, ClassDeclT> extends LeafInfoImpl<TypeT, ClassDeclT> implements BuiltinLeafInfo<TypeT, ClassDeclT> {
    private final QName[] typeNames;

    @Override // com.sun.xml.bind.v2.model.impl.LeafInfoImpl, com.sun.xml.bind.v2.runtime.Location
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    @Override // com.sun.xml.bind.v2.model.impl.LeafInfoImpl, com.sun.xml.bind.v2.model.core.NonElement
    public /* bridge */ /* synthetic */ boolean isSimpleType() {
        return super.isSimpleType();
    }

    @Override // com.sun.xml.bind.v2.model.impl.LeafInfoImpl, com.sun.xml.bind.v2.model.annotation.Locatable
    public /* bridge */ /* synthetic */ Location getLocation() {
        return super.getLocation();
    }

    @Override // com.sun.xml.bind.v2.model.impl.LeafInfoImpl, com.sun.xml.bind.v2.model.annotation.Locatable
    public /* bridge */ /* synthetic */ Locatable getUpstream() {
        return super.getUpstream();
    }

    @Override // com.sun.xml.bind.v2.model.impl.LeafInfoImpl, com.sun.xml.bind.v2.model.core.NonElement
    public /* bridge */ /* synthetic */ QName getTypeName() {
        return super.getTypeName();
    }

    @Override // com.sun.xml.bind.v2.model.impl.LeafInfoImpl, com.sun.xml.bind.v2.model.core.TypeInfo
    public /* bridge */ /* synthetic */ Object getType() {
        return super.getType();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public BuiltinLeafInfoImpl(TypeT type, QName... typeNames) {
        super(type, typeNames.length > 0 ? typeNames[0] : null);
        this.typeNames = typeNames;
    }

    public final QName[] getTypeNames() {
        return this.typeNames;
    }

    @Override // com.sun.xml.bind.v2.model.core.MaybeElement
    public final boolean isElement() {
        return false;
    }

    @Override // com.sun.xml.bind.v2.model.core.MaybeElement
    public final QName getElementName() {
        return null;
    }

    @Override // com.sun.xml.bind.v2.model.core.MaybeElement
    public final Element<TypeT, ClassDeclT> asElement() {
        return null;
    }

    public static <TypeT, ClassDeclT> Map<TypeT, BuiltinLeafInfoImpl<TypeT, ClassDeclT>> createLeaves(Navigator<TypeT, ClassDeclT, ?, ?> nav) {
        Map<TypeT, BuiltinLeafInfoImpl<TypeT, ClassDeclT>> leaves = new HashMap<>();
        for (RuntimeBuiltinLeafInfoImpl<?> leaf : RuntimeBuiltinLeafInfoImpl.builtinBeanInfos) {
            TypeT t = nav.ref(leaf.getClazz());
            leaves.put(t, new BuiltinLeafInfoImpl<>(t, leaf.getTypeNames()));
        }
        return leaves;
    }
}
