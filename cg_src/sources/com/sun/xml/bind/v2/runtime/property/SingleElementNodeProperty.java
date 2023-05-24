package com.sun.xml.bind.v2.runtime.property;

import com.sun.xml.bind.api.AccessorException;
import com.sun.xml.bind.v2.model.core.PropertyKind;
import com.sun.xml.bind.v2.model.core.TypeRef;
import com.sun.xml.bind.v2.model.runtime.RuntimeElementPropertyInfo;
import com.sun.xml.bind.v2.model.runtime.RuntimeTypeInfo;
import com.sun.xml.bind.v2.model.runtime.RuntimeTypeRef;
import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.bind.v2.runtime.JaxBeanInfo;
import com.sun.xml.bind.v2.runtime.Name;
import com.sun.xml.bind.v2.runtime.XMLSerializer;
import com.sun.xml.bind.v2.runtime.reflect.Accessor;
import com.sun.xml.bind.v2.runtime.unmarshaller.ChildLoader;
import com.sun.xml.bind.v2.runtime.unmarshaller.DefaultValueLoaderDecorator;
import com.sun.xml.bind.v2.runtime.unmarshaller.Loader;
import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader;
import com.sun.xml.bind.v2.util.QNameMap;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/property/SingleElementNodeProperty.class */
final class SingleElementNodeProperty<BeanT, ValueT> extends PropertyImpl<BeanT> {
    private final Accessor<BeanT, ValueT> acc;
    private final boolean nillable;
    private final QName[] acceptedElements;
    private final Map<Class, TagAndType> typeNames;
    private RuntimeElementPropertyInfo prop;
    private final Name nullTagName;

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v18, types: [com.sun.xml.bind.v2.model.runtime.RuntimeNonElement, com.sun.xml.bind.v2.model.runtime.RuntimeTypeInfo] */
    public SingleElementNodeProperty(JAXBContextImpl context, RuntimeElementPropertyInfo prop) {
        super(context, prop);
        this.typeNames = new HashMap();
        this.acc = prop.getAccessor().optimize(context);
        this.prop = prop;
        QName nt = null;
        boolean nil = false;
        this.acceptedElements = new QName[prop.getTypes().size()];
        for (int i = 0; i < this.acceptedElements.length; i++) {
            this.acceptedElements[i] = ((RuntimeTypeRef) prop.getTypes().get(i)).getTagName();
        }
        Iterator<? extends TypeRef<Type, Class>> it = prop.getTypes().iterator();
        while (it.hasNext()) {
            RuntimeTypeRef e = (RuntimeTypeRef) it.next();
            JaxBeanInfo beanInfo = context.getOrCreate((RuntimeTypeInfo) e.getTarget());
            if (nt == null) {
                nt = e.getTagName();
            }
            this.typeNames.put(beanInfo.jaxbType, new TagAndType(context.nameBuilder.createElementName(e.getTagName()), beanInfo));
            nil |= e.isNillable();
        }
        this.nullTagName = context.nameBuilder.createElementName(nt);
        this.nillable = nil;
    }

    @Override // com.sun.xml.bind.v2.runtime.property.PropertyImpl, com.sun.xml.bind.v2.runtime.property.Property
    public void wrapUp() {
        super.wrapUp();
        this.prop = null;
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
        if (v == null) {
            if (this.nillable) {
                w.startElement(this.nullTagName, null);
                w.writeXsiNilTrue();
                w.endElement();
                return;
            }
            return;
        }
        Class vtype = v.getClass();
        TagAndType tt = this.typeNames.get(vtype);
        if (tt == null) {
            Iterator<Map.Entry<Class, TagAndType>> it = this.typeNames.entrySet().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Map.Entry<Class, TagAndType> e = it.next();
                if (e.getKey().isAssignableFrom(vtype)) {
                    tt = e.getValue();
                    break;
                }
            }
        }
        boolean addNilDecl = (o instanceof JAXBElement) && ((JAXBElement) o).isNil();
        if (tt == null) {
            w.startElement(this.typeNames.values().iterator().next().tagName, null);
            w.childAsXsiType(v, this.fieldName, w.grammar.getBeanInfo(Object.class), addNilDecl && this.nillable);
        } else {
            w.startElement(tt.tagName, null);
            w.childAsXsiType(v, this.fieldName, tt.beanInfo, addNilDecl && this.nillable);
        }
        w.endElement();
    }

    @Override // com.sun.xml.bind.v2.runtime.property.StructureLoaderBuilder
    public void buildChildElementUnmarshallers(UnmarshallerChain chain, QNameMap<ChildLoader> handlers) {
        JAXBContextImpl context = chain.context;
        for (TypeRef<Type, Class> e : this.prop.getTypes()) {
            JaxBeanInfo bi = context.getOrCreate((RuntimeTypeInfo) e.getTarget());
            Loader l = bi.getLoader(context, !Modifier.isFinal(bi.jaxbType.getModifiers()));
            if (e.getDefaultValue() != null) {
                l = new DefaultValueLoaderDecorator(l, e.getDefaultValue());
            }
            if (this.nillable || chain.context.allNillable) {
                l = new XsiNilLoader.Single(l, this.acc);
            }
            handlers.put(e.getTagName(), (QName) new ChildLoader(l, this.acc));
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.property.Property
    public PropertyKind getKind() {
        return PropertyKind.ELEMENT;
    }

    @Override // com.sun.xml.bind.v2.runtime.property.PropertyImpl, com.sun.xml.bind.v2.runtime.property.Property
    public Accessor getElementPropertyAccessor(String nsUri, String localName) {
        QName[] qNameArr;
        for (QName n : this.acceptedElements) {
            if (n.getNamespaceURI().equals(nsUri) && n.getLocalPart().equals(localName)) {
                return this.acc;
            }
        }
        return null;
    }
}
