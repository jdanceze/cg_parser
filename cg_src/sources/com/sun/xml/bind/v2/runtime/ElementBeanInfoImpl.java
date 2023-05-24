package com.sun.xml.bind.v2.runtime;

import com.sun.xml.bind.api.AccessorException;
import com.sun.xml.bind.v2.model.core.PropertyKind;
import com.sun.xml.bind.v2.model.runtime.RuntimeElementInfo;
import com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo;
import com.sun.xml.bind.v2.runtime.property.Property;
import com.sun.xml.bind.v2.runtime.property.PropertyFactory;
import com.sun.xml.bind.v2.runtime.property.UnmarshallerChain;
import com.sun.xml.bind.v2.runtime.reflect.Accessor;
import com.sun.xml.bind.v2.runtime.unmarshaller.ChildLoader;
import com.sun.xml.bind.v2.runtime.unmarshaller.Discarder;
import com.sun.xml.bind.v2.runtime.unmarshaller.Intercepter;
import com.sun.xml.bind.v2.runtime.unmarshaller.Loader;
import com.sun.xml.bind.v2.runtime.unmarshaller.TagName;
import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import com.sun.xml.bind.v2.util.QNameMap;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/ElementBeanInfoImpl.class */
public final class ElementBeanInfoImpl extends JaxBeanInfo<JAXBElement> {
    private Loader loader;
    private final Property property;
    private final QName tagName;
    public final Class expectedType;
    private final Class scope;
    private final Constructor<? extends JAXBElement> constructor;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Type inference failed for: r2v2, types: [com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo, com.sun.xml.bind.v2.model.runtime.RuntimeElementPropertyInfo] */
    public ElementBeanInfoImpl(JAXBContextImpl grammar, RuntimeElementInfo rei) {
        super(grammar, rei, rei.getType(), true, false, true);
        this.property = PropertyFactory.create(grammar, rei.getProperty());
        this.tagName = rei.getElementName();
        this.expectedType = (Class) Utils.REFLECTION_NAVIGATOR.erasure(rei.getContentInMemoryType());
        this.scope = rei.getScope() == null ? JAXBElement.GlobalScope.class : rei.getScope().getClazz();
        Class type = (Class) Utils.REFLECTION_NAVIGATOR.erasure(rei.getType());
        if (type == JAXBElement.class) {
            this.constructor = null;
            return;
        }
        try {
            this.constructor = type.getConstructor(this.expectedType);
        } catch (NoSuchMethodException e) {
            NoSuchMethodError x = new NoSuchMethodError("Failed to find the constructor for " + type + " with " + this.expectedType);
            x.initCause(e);
            throw x;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ElementBeanInfoImpl(final JAXBContextImpl grammar) {
        super(grammar, null, JAXBElement.class, true, false, true);
        this.tagName = null;
        this.expectedType = null;
        this.scope = null;
        this.constructor = null;
        this.property = new Property<JAXBElement>() { // from class: com.sun.xml.bind.v2.runtime.ElementBeanInfoImpl.1
            @Override // com.sun.xml.bind.v2.runtime.property.Property
            public void reset(JAXBElement o) {
                throw new UnsupportedOperationException();
            }

            @Override // com.sun.xml.bind.v2.runtime.property.Property
            public void serializeBody(JAXBElement e, XMLSerializer target, Object outerPeer) throws SAXException, IOException, XMLStreamException {
                Class scope = e.getScope();
                if (e.isGlobalScope()) {
                    scope = null;
                }
                QName n = e.getName();
                ElementBeanInfoImpl bi = grammar.getElement(scope, n);
                if (bi != null) {
                    try {
                        bi.property.serializeBody(e, target, e);
                        return;
                    } catch (AccessorException x) {
                        target.reportError(null, x);
                        return;
                    }
                }
                try {
                    JaxBeanInfo tbi = grammar.getBeanInfo(e.getDeclaredType(), true);
                    Object value = e.getValue();
                    target.startElement(n.getNamespaceURI(), n.getLocalPart(), n.getPrefix(), null);
                    if (value == null) {
                        target.writeXsiNilTrue();
                    } else {
                        target.childAsXsiType(value, "value", tbi, false);
                    }
                    target.endElement();
                } catch (JAXBException x2) {
                    target.reportError(null, x2);
                }
            }

            @Override // com.sun.xml.bind.v2.runtime.property.Property
            public void serializeURIs(JAXBElement o, XMLSerializer target) {
            }

            @Override // com.sun.xml.bind.v2.runtime.property.Property
            public boolean hasSerializeURIAction() {
                return false;
            }

            @Override // com.sun.xml.bind.v2.runtime.property.Property
            public String getIdValue(JAXBElement o) {
                return null;
            }

            @Override // com.sun.xml.bind.v2.runtime.property.Property
            public PropertyKind getKind() {
                return PropertyKind.ELEMENT;
            }

            @Override // com.sun.xml.bind.v2.runtime.property.StructureLoaderBuilder
            public void buildChildElementUnmarshallers(UnmarshallerChain chain, QNameMap<ChildLoader> handlers) {
            }

            @Override // com.sun.xml.bind.v2.runtime.property.Property
            public Accessor getElementPropertyAccessor(String nsUri, String localName) {
                throw new UnsupportedOperationException();
            }

            @Override // com.sun.xml.bind.v2.runtime.property.Property
            public void wrapUp() {
            }

            @Override // com.sun.xml.bind.v2.runtime.property.Property
            public RuntimePropertyInfo getInfo() {
                return ElementBeanInfoImpl.this.property.getInfo();
            }

            @Override // com.sun.xml.bind.v2.runtime.property.Property
            public boolean isHiddenByOverride() {
                return false;
            }

            @Override // com.sun.xml.bind.v2.runtime.property.Property
            public void setHiddenByOverride(boolean hidden) {
                throw new UnsupportedOperationException("Not supported on jaxbelements.");
            }

            @Override // com.sun.xml.bind.v2.runtime.property.Property
            public String getFieldName() {
                return null;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/ElementBeanInfoImpl$IntercepterLoader.class */
    public final class IntercepterLoader extends Loader implements Intercepter {
        private final Loader core;

        public IntercepterLoader(Loader core) {
            this.core = core;
        }

        @Override // com.sun.xml.bind.v2.runtime.unmarshaller.Loader
        public final void startElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
            state.setLoader(this.core);
            state.setIntercepter(this);
            UnmarshallingContext context = state.getContext();
            Object child = context.getOuterPeer();
            if (child != null && ElementBeanInfoImpl.this.jaxbType != child.getClass()) {
                child = null;
            }
            if (child != null) {
                ElementBeanInfoImpl.this.reset((JAXBElement) child, context);
            }
            if (child == null) {
                child = context.createInstance(ElementBeanInfoImpl.this);
            }
            fireBeforeUnmarshal(ElementBeanInfoImpl.this, child, state);
            context.recordOuterPeer(child);
            UnmarshallingContext.State p = state.getPrev();
            p.setBackup(p.getTarget());
            p.setTarget(child);
            this.core.startElement(state, ea);
        }

        @Override // com.sun.xml.bind.v2.runtime.unmarshaller.Intercepter
        public Object intercept(UnmarshallingContext.State state, Object o) throws SAXException {
            JAXBElement e = (JAXBElement) state.getTarget();
            state.setTarget(state.getBackup());
            state.setBackup(null);
            if (state.isNil()) {
                e.setNil(true);
                state.setNil(false);
            }
            if (o != null) {
                e.setValue(o);
            }
            fireAfterUnmarshal(ElementBeanInfoImpl.this, e, state);
            return e;
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public String getElementNamespaceURI(JAXBElement e) {
        return e.getName().getNamespaceURI();
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public String getElementLocalName(JAXBElement e) {
        return e.getName().getLocalPart();
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public Loader getLoader(JAXBContextImpl context, boolean typeSubstitutionCapable) {
        if (this.loader == null) {
            UnmarshallerChain c = new UnmarshallerChain(context);
            QNameMap<ChildLoader> result = new QNameMap<>();
            this.property.buildChildElementUnmarshallers(c, result);
            if (result.size() == 1) {
                this.loader = new IntercepterLoader(result.getOne().getValue().loader);
            } else {
                this.loader = Discarder.INSTANCE;
            }
        }
        return this.loader;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public final JAXBElement createInstance(UnmarshallingContext context) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return createInstanceFromValue(null);
    }

    public final JAXBElement createInstanceFromValue(Object o) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return this.constructor == null ? new JAXBElement(this.tagName, this.expectedType, this.scope, o) : this.constructor.newInstance(o);
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public boolean reset(JAXBElement e, UnmarshallingContext context) {
        e.setValue(null);
        return true;
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public String getId(JAXBElement e, XMLSerializer target) {
        Object o = e.getValue();
        if (o instanceof String) {
            return (String) o;
        }
        return null;
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public void serializeBody(JAXBElement element, XMLSerializer target) throws SAXException, IOException, XMLStreamException {
        try {
            this.property.serializeBody(element, target, null);
        } catch (AccessorException x) {
            target.reportError(null, x);
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public void serializeRoot(JAXBElement e, XMLSerializer target) throws SAXException, IOException, XMLStreamException {
        serializeBody(e, target);
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public void serializeAttributes(JAXBElement e, XMLSerializer target) {
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public void serializeURIs(JAXBElement e, XMLSerializer target) {
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public final Transducer<JAXBElement> getTransducer() {
        return null;
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public void wrapUp() {
        super.wrapUp();
        this.property.wrapUp();
    }

    @Override // com.sun.xml.bind.v2.runtime.JaxBeanInfo
    public void link(JAXBContextImpl grammar) {
        super.link(grammar);
        getLoader(grammar, true);
    }
}
