package com.sun.xml.bind.v2.runtime.property;

import com.sun.xml.bind.v2.model.core.ID;
import com.sun.xml.bind.v2.model.core.PropertyKind;
import com.sun.xml.bind.v2.model.core.TypeInfo;
import com.sun.xml.bind.v2.model.runtime.RuntimeAttributePropertyInfo;
import com.sun.xml.bind.v2.model.runtime.RuntimeElementPropertyInfo;
import com.sun.xml.bind.v2.model.runtime.RuntimeNonElement;
import com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo;
import com.sun.xml.bind.v2.model.runtime.RuntimeTypeInfo;
import com.sun.xml.bind.v2.model.runtime.RuntimeValuePropertyInfo;
import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Collection;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/property/PropertyFactory.class */
public abstract class PropertyFactory {
    private static final Constructor<? extends Property>[] propImpls;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* JADX WARN: Multi-variable type inference failed */
    static {
        $assertionsDisabled = !PropertyFactory.class.desiredAssertionStatus();
        Class<? extends Property>[] implClasses = {SingleElementLeafProperty.class, null, null, ArrayElementLeafProperty.class, null, null, SingleElementNodeProperty.class, SingleReferenceNodeProperty.class, SingleMapNodeProperty.class, ArrayElementNodeProperty.class, ArrayReferenceNodeProperty.class, null};
        propImpls = new Constructor[implClasses.length];
        for (int i = 0; i < propImpls.length; i++) {
            if (implClasses[i] != null) {
                propImpls[i] = implClasses[i].getConstructors()[0];
            }
        }
    }

    private PropertyFactory() {
    }

    public static Property create(JAXBContextImpl grammar, RuntimePropertyInfo info) {
        PropertyKind kind = info.kind();
        switch (kind) {
            case ATTRIBUTE:
                return new AttributeProperty(grammar, (RuntimeAttributePropertyInfo) info);
            case VALUE:
                return new ValueProperty(grammar, (RuntimeValuePropertyInfo) info);
            case ELEMENT:
                if (((RuntimeElementPropertyInfo) info).isValueList()) {
                    return new ListElementProperty(grammar, (RuntimeElementPropertyInfo) info);
                }
                break;
            case REFERENCE:
            case MAP:
                break;
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                break;
        }
        boolean isCollection = info.isCollection();
        boolean isLeaf = isLeaf(info);
        Constructor<? extends Property> c = propImpls[(isLeaf ? 0 : 6) + (isCollection ? 3 : 0) + kind.propertyIndex];
        try {
            return c.newInstance(grammar, info);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError(e.getMessage());
        } catch (InstantiationException e2) {
            throw new InstantiationError(e2.getMessage());
        } catch (InvocationTargetException e3) {
            Throwable t = e3.getCause();
            if (t instanceof Error) {
                throw ((Error) t);
            }
            if (t instanceof RuntimeException) {
                throw ((RuntimeException) t);
            }
            throw new AssertionError(t);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isLeaf(RuntimePropertyInfo info) {
        Collection<? extends TypeInfo<Type, Class>> ref = info.ref();
        if (ref.size() != 1) {
            return false;
        }
        RuntimeTypeInfo rti = (RuntimeTypeInfo) ref.iterator().next();
        if (rti instanceof RuntimeNonElement) {
            if (info.id() == ID.IDREF) {
                return true;
            }
            if (((RuntimeNonElement) rti).getTransducer() == null || !info.getIndividualType().equals(rti.getType())) {
                return false;
            }
            return true;
        }
        return false;
    }
}
