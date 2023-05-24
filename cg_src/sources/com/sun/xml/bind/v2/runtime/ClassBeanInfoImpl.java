package com.sun.xml.bind.v2.runtime;

import com.sun.istack.FinalArrayList;
import com.sun.xml.bind.Util;
import com.sun.xml.bind.api.AccessorException;
import com.sun.xml.bind.v2.ClassFactory;
import com.sun.xml.bind.v2.model.core.ID;
import com.sun.xml.bind.v2.model.runtime.RuntimeClassInfo;
import com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo;
import com.sun.xml.bind.v2.model.runtime.RuntimeTypeInfo;
import com.sun.xml.bind.v2.runtime.property.AttributeProperty;
import com.sun.xml.bind.v2.runtime.property.Property;
import com.sun.xml.bind.v2.runtime.property.PropertyFactory;
import com.sun.xml.bind.v2.runtime.reflect.Accessor;
import com.sun.xml.bind.v2.runtime.unmarshaller.Loader;
import com.sun.xml.bind.v2.runtime.unmarshaller.StructureLoader;
import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import com.sun.xml.bind.v2.runtime.unmarshaller.XsiTypeLoader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.helpers.ValidationEventImpl;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.LocatorImpl;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/ClassBeanInfoImpl.class */
public final class ClassBeanInfoImpl<BeanT> extends JaxBeanInfo<BeanT> implements AttributeAccessor<BeanT> {
    public final Property<BeanT>[] properties;
    private Property<? super BeanT> idProperty;
    private Loader loader;
    private Loader loaderWithTypeSubst;
    private RuntimeClassInfo ci;
    private final Accessor<? super BeanT, Map<QName, String>> inheritedAttWildcard;
    private final Transducer<BeanT> xducer;
    public final ClassBeanInfoImpl<? super BeanT> superClazz;
    private final Accessor<? super BeanT, Locator> xmlLocatorField;
    private final Name tagName;
    private boolean retainPropertyInfo;
    private AttributeProperty<BeanT>[] attributeProperties;
    private Property<BeanT>[] uriProperties;
    private final Method factoryMethod;
    private static final AttributeProperty[] EMPTY_PROPERTIES = new AttributeProperty[0];
    private static final Logger logger = Util.getClassLogger();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v2, types: [com.sun.xml.bind.v2.model.runtime.RuntimeClassInfo] */
    public ClassBeanInfoImpl(JAXBContextImpl owner, RuntimeClassInfo ci) {
        super(owner, (RuntimeTypeInfo) ci, ci.getClazz(), ci.getTypeName(), ci.isElement(), false, true);
        this.retainPropertyInfo = false;
        this.ci = ci;
        this.inheritedAttWildcard = ci.getAttributeWildcard();
        this.xducer = ci.getTransducer();
        this.factoryMethod = ci.getFactoryMethod();
        this.retainPropertyInfo = owner.retainPropertyInfo;
        if (this.factoryMethod != null) {
            int classMod = this.factoryMethod.getDeclaringClass().getModifiers();
            if (!Modifier.isPublic(classMod) || !Modifier.isPublic(this.factoryMethod.getModifiers())) {
                try {
                    this.factoryMethod.setAccessible(true);
                } catch (SecurityException e) {
                    logger.log(Level.FINE, "Unable to make the method of " + this.factoryMethod + " accessible", (Throwable) e);
                    throw e;
                }
            }
        }
        if (ci.getBaseClass() == null) {
            this.superClazz = null;
        } else {
            this.superClazz = owner.getOrCreate((RuntimeClassInfo) ci.getBaseClass());
        }
        if (this.superClazz != null && this.superClazz.xmlLocatorField != null) {
            this.xmlLocatorField = (Accessor<? super Object, Locator>) this.superClazz.xmlLocatorField;
        } else {
            this.xmlLocatorField = ci.getLocatorField();
        }
        Collection<? extends RuntimePropertyInfo> ps = ci.getProperties();
        this.properties = new Property[ps.size()];
        int idx = 0;
        boolean elementOnly = true;
        for (RuntimePropertyInfo info : ps) {
            Property p = PropertyFactory.create(owner, info);
            if (info.id() == ID.ID) {
                this.idProperty = p;
            }
            int i = idx;
            idx++;
            this.properties[i] = p;
            elementOnly &= info.elementOnlyContent();
            checkOverrideProperties(p);
        }
        hasElementOnlyContentModel(elementOnly);
        if (ci.isElement()) {
            this.tagName = owner.nameBuilder.createElementName(ci.getElementName());
        } else {
            this.tagName = null;
        }
        setLifecycleFlags();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void checkOverrideProperties(Property p) {
        Property[] props;
        String spName;
        ClassBeanInfoImpl bi = this;
        while (true) {
            ClassBeanInfoImpl classBeanInfoImpl = (ClassBeanInfoImpl<? super BeanT>) bi.superClazz;
            bi = classBeanInfoImpl;
            if (classBeanInfoImpl == null || (props = bi.properties) == null) {
                return;
            }
            for (Property superProperty : props) {
                if (superProperty != null && (spName = superProperty.getFieldName()) != null && spName.equals(p.getFieldName())) {
                    superProperty.setHiddenByOverride(true);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public void link(JAXBContextImpl grammar) {
        if (this.uriProperties != null) {
            return;
        }
        super.link(grammar);
        if (this.superClazz != null) {
            this.superClazz.link(grammar);
        }
        getLoader(grammar, true);
        if (this.superClazz != null) {
            if (this.idProperty == null) {
                this.idProperty = (Property<? super Object>) this.superClazz.idProperty;
            }
            if (!this.superClazz.hasElementOnlyContentModel()) {
                hasElementOnlyContentModel(false);
            }
        }
        List<AttributeProperty> attProps = new FinalArrayList<>();
        List<Property> uriProps = new FinalArrayList<>();
        ClassBeanInfoImpl classBeanInfoImpl = this;
        while (true) {
            ClassBeanInfoImpl bi = classBeanInfoImpl;
            if (bi == null) {
                break;
            }
            for (int i = 0; i < bi.properties.length; i++) {
                Property p = bi.properties[i];
                if (p instanceof AttributeProperty) {
                    attProps.add((AttributeProperty) p);
                }
                if (p.hasSerializeURIAction()) {
                    uriProps.add(p);
                }
            }
            classBeanInfoImpl = bi.superClazz;
        }
        if (grammar.c14nSupport) {
            Collections.sort(attProps);
        }
        if (attProps.isEmpty()) {
            this.attributeProperties = EMPTY_PROPERTIES;
        } else {
            this.attributeProperties = (AttributeProperty[]) attProps.toArray(new AttributeProperty[attProps.size()]);
        }
        if (uriProps.isEmpty()) {
            this.uriProperties = EMPTY_PROPERTIES;
        } else {
            this.uriProperties = (Property[]) uriProps.toArray(new Property[uriProps.size()]);
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public void wrapUp() {
        Property[] propertyArr;
        for (Property p : this.properties) {
            p.wrapUp();
        }
        this.ci = null;
        super.wrapUp();
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public String getElementNamespaceURI(BeanT bean) {
        return this.tagName.nsUri;
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public String getElementLocalName(BeanT bean) {
        return this.tagName.localName;
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public BeanT createInstance(UnmarshallingContext context) throws IllegalAccessException, InvocationTargetException, InstantiationException, SAXException {
        Object obj;
        if (this.factoryMethod == null) {
            obj = ClassFactory.create0(this.jaxbType);
        } else {
            Object o = ClassFactory.create(this.factoryMethod);
            if (this.jaxbType.isInstance(o)) {
                obj = o;
            } else {
                throw new InstantiationException("The factory method didn't return a correct object");
            }
        }
        if (this.xmlLocatorField != null) {
            try {
                this.xmlLocatorField.set(obj, new LocatorImpl(context.getLocator()));
            } catch (AccessorException e) {
                context.handleError(e);
            }
        }
        return (BeanT) obj;
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public boolean reset(BeanT bean, UnmarshallingContext context) throws SAXException {
        Property<BeanT>[] propertyArr;
        try {
            if (this.superClazz != null) {
                this.superClazz.reset(bean, context);
            }
            for (Property<BeanT> p : this.properties) {
                p.reset(bean);
            }
            return true;
        } catch (AccessorException e) {
            context.handleError(e);
            return false;
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public String getId(BeanT bean, XMLSerializer target) throws SAXException {
        if (this.idProperty != null) {
            try {
                return this.idProperty.getIdValue(bean);
            } catch (AccessorException e) {
                target.reportError(null, e);
                return null;
            }
        }
        return null;
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public void serializeRoot(BeanT bean, XMLSerializer target) throws SAXException, IOException, XMLStreamException {
        String message;
        if (this.tagName == null) {
            Class beanClass = bean.getClass();
            if (beanClass.isAnnotationPresent(XmlRootElement.class)) {
                message = Messages.UNABLE_TO_MARSHAL_UNBOUND_CLASS.format(beanClass.getName());
            } else {
                message = Messages.UNABLE_TO_MARSHAL_NON_ELEMENT.format(beanClass.getName());
            }
            target.reportError(new ValidationEventImpl(1, message, null, null));
            return;
        }
        target.startElement(this.tagName, bean);
        target.childAsSoleContent(bean, null);
        target.endElement();
        if (this.retainPropertyInfo) {
            target.currentProperty.remove();
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public void serializeBody(BeanT bean, XMLSerializer target) throws SAXException, IOException, XMLStreamException {
        Property<BeanT>[] propertyArr;
        if (this.superClazz != null) {
            this.superClazz.serializeBody(bean, target);
        }
        try {
            for (Property<BeanT> p : this.properties) {
                if (this.retainPropertyInfo) {
                    target.currentProperty.set(p);
                }
                boolean isThereAnOverridingProperty = p.isHiddenByOverride();
                if (!isThereAnOverridingProperty || bean.getClass().equals(this.jaxbType)) {
                    p.serializeBody(bean, target, null);
                } else if (isThereAnOverridingProperty) {
                    Class beanClass = bean.getClass();
                    if (Utils.REFLECTION_NAVIGATOR.getDeclaredField(beanClass, p.getFieldName()) == null) {
                        p.serializeBody(bean, target, null);
                    }
                }
            }
        } catch (AccessorException e) {
            target.reportError(null, e);
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public void serializeAttributes(BeanT bean, XMLSerializer target) throws SAXException, IOException, XMLStreamException {
        AttributeProperty<BeanT>[] attributePropertyArr;
        for (AttributeProperty<BeanT> p : this.attributeProperties) {
            try {
                if (this.retainPropertyInfo) {
                    Property parentProperty = target.getCurrentProperty();
                    target.currentProperty.set(p);
                    p.serializeAttributes(bean, target);
                    target.currentProperty.set(parentProperty);
                } else {
                    p.serializeAttributes(bean, target);
                }
                if (p.attName.equals("http://www.w3.org/2001/XMLSchema-instance", "nil")) {
                    this.isNilIncluded = true;
                }
            } catch (AccessorException e) {
                target.reportError(null, e);
            }
        }
        try {
            if (this.inheritedAttWildcard != null) {
                Map<QName, String> map = this.inheritedAttWildcard.get(bean);
                target.attWildcardAsAttributes(map, null);
            }
        } catch (AccessorException e2) {
            target.reportError(null, e2);
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public void serializeURIs(BeanT bean, XMLSerializer target) throws SAXException {
        Property<BeanT>[] propertyArr;
        try {
            if (this.retainPropertyInfo) {
                Property parentProperty = target.getCurrentProperty();
                for (Property<BeanT> p : this.uriProperties) {
                    target.currentProperty.set(p);
                    p.serializeURIs(bean, target);
                }
                target.currentProperty.set(parentProperty);
            } else {
                for (Property<BeanT> p2 : this.uriProperties) {
                    p2.serializeURIs(bean, target);
                }
            }
            if (this.inheritedAttWildcard != null) {
                Map<QName, String> map = this.inheritedAttWildcard.get(bean);
                target.attWildcardAsURIs(map, null);
            }
        } catch (AccessorException e) {
            target.reportError(null, e);
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public Loader getLoader(JAXBContextImpl context, boolean typeSubstitutionCapable) {
        if (this.loader == null) {
            StructureLoader sl = new StructureLoader(this);
            this.loader = sl;
            if (this.ci.hasSubClasses()) {
                this.loaderWithTypeSubst = new XsiTypeLoader(this);
            } else {
                this.loaderWithTypeSubst = this.loader;
            }
            sl.init(context, this, this.ci.getAttributeWildcard());
        }
        if (typeSubstitutionCapable) {
            return this.loaderWithTypeSubst;
        }
        return this.loader;
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public Transducer<BeanT> getTransducer() {
        return this.xducer;
    }
}
