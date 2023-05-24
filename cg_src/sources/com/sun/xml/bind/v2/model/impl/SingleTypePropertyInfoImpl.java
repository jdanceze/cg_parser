package com.sun.xml.bind.v2.model.impl;

import com.sun.xml.bind.v2.model.core.ID;
import com.sun.xml.bind.v2.model.core.NonElement;
import com.sun.xml.bind.v2.model.core.PropertyInfo;
import com.sun.xml.bind.v2.model.impl.RuntimeClassInfoImpl;
import com.sun.xml.bind.v2.model.runtime.RuntimeNonElementRef;
import com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo;
import com.sun.xml.bind.v2.runtime.IllegalAnnotationException;
import com.sun.xml.bind.v2.runtime.Transducer;
import com.sun.xml.bind.v2.runtime.reflect.Accessor;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlList;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/impl/SingleTypePropertyInfoImpl.class */
public abstract class SingleTypePropertyInfoImpl<T, C, F, M> extends PropertyInfoImpl<T, C, F, M> {
    private NonElement<T, C> type;
    private final Accessor acc;
    private Transducer xducer;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SingleTypePropertyInfoImpl.class.desiredAssertionStatus();
    }

    public SingleTypePropertyInfoImpl(ClassInfoImpl<T, C, F, M> classInfo, PropertySeed<T, C, F, M> seed) {
        super(classInfo, seed);
        if (this instanceof RuntimePropertyInfo) {
            Accessor rawAcc = ((RuntimeClassInfoImpl.RuntimePropertySeed) seed).getAccessor();
            if (getAdapter() != null && !isCollection()) {
                rawAcc = rawAcc.adapt(((RuntimePropertyInfo) this).getAdapter());
            }
            this.acc = rawAcc;
            return;
        }
        this.acc = null;
    }

    @Override // com.sun.xml.bind.v2.model.core.PropertyInfo
    public List<? extends NonElement<T, C>> ref() {
        return Collections.singletonList(getTarget());
    }

    public NonElement<T, C> getTarget() {
        if (this.type == null) {
            if (!$assertionsDisabled && this.parent.builder == null) {
                throw new AssertionError("this method must be called during the build stage");
            }
            this.type = this.parent.builder.getTypeInfo(getIndividualType(), this);
        }
        return this.type;
    }

    public PropertyInfo<T, C> getSource() {
        return this;
    }

    @Override // com.sun.xml.bind.v2.model.impl.PropertyInfoImpl
    public void link() {
        super.link();
        if (!NonElement.ANYTYPE_NAME.equals(this.type.getTypeName()) && !this.type.isSimpleType() && id() != ID.IDREF) {
            this.parent.builder.reportError(new IllegalAnnotationException(Messages.SIMPLE_TYPE_IS_REQUIRED.format(new Object[0]), this.seed));
        }
        if (!isCollection() && this.seed.hasAnnotation(XmlList.class)) {
            this.parent.builder.reportError(new IllegalAnnotationException(Messages.XMLLIST_ON_SINGLE_PROPERTY.format(new Object[0]), this));
        }
    }

    public Accessor getAccessor() {
        return this.acc;
    }

    public Transducer getTransducer() {
        if (this.xducer == null) {
            this.xducer = RuntimeModelBuilder.createTransducer((RuntimeNonElementRef) this);
            if (this.xducer == null) {
                this.xducer = RuntimeBuiltinLeafInfoImpl.STRING;
            }
        }
        return this.xducer;
    }
}
