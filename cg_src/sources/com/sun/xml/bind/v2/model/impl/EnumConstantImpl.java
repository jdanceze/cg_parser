package com.sun.xml.bind.v2.model.impl;

import com.sun.xml.bind.v2.model.core.EnumConstant;
import com.sun.xml.bind.v2.model.core.EnumLeafInfo;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/impl/EnumConstantImpl.class */
public class EnumConstantImpl<T, C, F, M> implements EnumConstant<T, C> {
    protected final String lexical;
    protected final EnumLeafInfoImpl<T, C, F, M> owner;
    protected final String name;
    protected final EnumConstantImpl<T, C, F, M> next;

    public EnumConstantImpl(EnumLeafInfoImpl<T, C, F, M> owner, String name, String lexical, EnumConstantImpl<T, C, F, M> next) {
        this.lexical = lexical;
        this.owner = owner;
        this.name = name;
        this.next = next;
    }

    @Override // com.sun.xml.bind.v2.model.core.EnumConstant
    public EnumLeafInfo<T, C> getEnclosingClass() {
        return this.owner;
    }

    @Override // com.sun.xml.bind.v2.model.core.EnumConstant
    public final String getLexicalValue() {
        return this.lexical;
    }

    @Override // com.sun.xml.bind.v2.model.core.EnumConstant
    public final String getName() {
        return this.name;
    }
}
