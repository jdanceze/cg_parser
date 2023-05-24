package com.sun.xml.bind.v2.runtime.property;

import com.sun.xml.bind.api.AccessorException;
import com.sun.xml.bind.v2.ClassFactory;
import com.sun.xml.bind.v2.model.core.Element;
import com.sun.xml.bind.v2.model.core.PropertyKind;
import com.sun.xml.bind.v2.model.core.WildcardMode;
import com.sun.xml.bind.v2.model.runtime.RuntimeElement;
import com.sun.xml.bind.v2.model.runtime.RuntimeReferencePropertyInfo;
import com.sun.xml.bind.v2.runtime.ElementBeanInfoImpl;
import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.bind.v2.runtime.JaxBeanInfo;
import com.sun.xml.bind.v2.runtime.XMLSerializer;
import com.sun.xml.bind.v2.runtime.reflect.Accessor;
import com.sun.xml.bind.v2.runtime.unmarshaller.ChildLoader;
import com.sun.xml.bind.v2.runtime.unmarshaller.WildcardLoader;
import com.sun.xml.bind.v2.util.QNameMap;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Iterator;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.DomHandler;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/property/SingleReferenceNodeProperty.class */
final class SingleReferenceNodeProperty<BeanT, ValueT> extends PropertyImpl<BeanT> {
    private final Accessor<BeanT, ValueT> acc;
    private final QNameMap<JaxBeanInfo> expectedElements;
    private final DomHandler domHandler;
    private final WildcardMode wcMode;

    public SingleReferenceNodeProperty(JAXBContextImpl context, RuntimeReferencePropertyInfo prop) {
        super(context, prop);
        this.expectedElements = new QNameMap<>();
        this.acc = prop.getAccessor().optimize(context);
        Iterator<? extends Element<Type, Class>> it = prop.getElements().iterator();
        while (it.hasNext()) {
            RuntimeElement e = (RuntimeElement) it.next();
            this.expectedElements.put(e.getElementName(), (QName) context.getOrCreate(e));
        }
        if (prop.getWildcard() != null) {
            this.domHandler = (DomHandler) ClassFactory.create(prop.getDOMHandler());
            this.wcMode = prop.getWildcard();
            return;
        }
        this.domHandler = null;
        this.wcMode = null;
    }

    @Override // com.sun.xml.bind.v2.runtime.property.Property
    public void reset(BeanT bean) throws AccessorException {
        this.acc.set(bean, null);
    }

    @Override // com.sun.xml.bind.v2.runtime.property.Property
    public String getIdValue(BeanT beanT) {
        return null;
    }

    @Override // com.sun.xml.bind.v2.runtime.property.PropertyImpl, com.sun.xml.bind.v2.runtime.property.Property
    public void serializeBody(BeanT o, XMLSerializer w, Object outerPeer) throws SAXException, AccessorException, IOException, XMLStreamException {
        ValueT v = this.acc.get(o);
        if (v != null) {
            try {
                JaxBeanInfo bi = w.grammar.getBeanInfo((Object) v, true);
                if (bi.jaxbType == Object.class && this.domHandler != null) {
                    w.writeDom(v, this.domHandler, o, this.fieldName);
                } else {
                    bi.serializeRoot(v, w);
                }
            } catch (JAXBException e) {
                w.reportError(this.fieldName, e);
            }
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.property.StructureLoaderBuilder
    public void buildChildElementUnmarshallers(UnmarshallerChain chain, QNameMap<ChildLoader> handlers) {
        for (QNameMap.Entry<JaxBeanInfo> n : this.expectedElements.entrySet()) {
            handlers.put(n.nsUri, n.localName, new ChildLoader(n.getValue().getLoader(chain.context, true), this.acc));
        }
        if (this.domHandler != null) {
            handlers.put(CATCH_ALL, (QName) new ChildLoader(new WildcardLoader(this.domHandler, this.wcMode), this.acc));
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.property.Property
    public PropertyKind getKind() {
        return PropertyKind.REFERENCE;
    }

    @Override // com.sun.xml.bind.v2.runtime.property.PropertyImpl, com.sun.xml.bind.v2.runtime.property.Property
    public Accessor getElementPropertyAccessor(String nsUri, String localName) {
        JaxBeanInfo bi = this.expectedElements.get(nsUri, localName);
        if (bi != null) {
            if (bi instanceof ElementBeanInfoImpl) {
                final ElementBeanInfoImpl ebi = (ElementBeanInfoImpl) bi;
                return new Accessor<BeanT, Object>(ebi.expectedType) { // from class: com.sun.xml.bind.v2.runtime.property.SingleReferenceNodeProperty.1
                    @Override // com.sun.xml.bind.v2.runtime.reflect.Accessor
                    public Object get(BeanT bean) throws AccessorException {
                        Object obj = SingleReferenceNodeProperty.this.acc.get(bean);
                        if (obj instanceof JAXBElement) {
                            return ((JAXBElement) obj).getValue();
                        }
                        return obj;
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // com.sun.xml.bind.v2.runtime.reflect.Accessor
                    public void set(BeanT bean, Object value) throws AccessorException {
                        if (value != null) {
                            try {
                                value = ebi.createInstanceFromValue(value);
                            } catch (IllegalAccessException e) {
                                throw new AccessorException(e);
                            } catch (InstantiationException e2) {
                                throw new AccessorException(e2);
                            } catch (InvocationTargetException e3) {
                                throw new AccessorException(e3);
                            }
                        }
                        SingleReferenceNodeProperty.this.acc.set(bean, value);
                    }
                };
            }
            return this.acc;
        }
        return null;
    }
}
