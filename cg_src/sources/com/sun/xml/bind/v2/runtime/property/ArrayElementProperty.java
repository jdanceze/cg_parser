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
import com.sun.xml.bind.v2.runtime.RuntimeUtil;
import com.sun.xml.bind.v2.runtime.Transducer;
import com.sun.xml.bind.v2.runtime.XMLSerializer;
import com.sun.xml.bind.v2.runtime.property.ArrayERProperty;
import com.sun.xml.bind.v2.runtime.reflect.Accessor;
import com.sun.xml.bind.v2.runtime.reflect.ListIterator;
import com.sun.xml.bind.v2.runtime.reflect.Lister;
import com.sun.xml.bind.v2.runtime.reflect.NullSafeAccessor;
import com.sun.xml.bind.v2.runtime.unmarshaller.ChildLoader;
import com.sun.xml.bind.v2.runtime.unmarshaller.DefaultValueLoaderDecorator;
import com.sun.xml.bind.v2.runtime.unmarshaller.Loader;
import com.sun.xml.bind.v2.runtime.unmarshaller.Receiver;
import com.sun.xml.bind.v2.runtime.unmarshaller.TextLoader;
import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader;
import com.sun.xml.bind.v2.util.QNameMap;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/property/ArrayElementProperty.class */
abstract class ArrayElementProperty<BeanT, ListT, ItemT> extends ArrayERProperty<BeanT, ListT, ItemT> {
    private final Map<Class, TagAndType> typeMap;
    private Map<TypeRef<Type, Class>, JaxBeanInfo> refs;
    protected RuntimeElementPropertyInfo prop;
    private final Name nillableTagName;

    protected abstract void serializeItem(JaxBeanInfo jaxBeanInfo, ItemT itemt, XMLSerializer xMLSerializer) throws SAXException, AccessorException, IOException, XMLStreamException;

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v6, types: [com.sun.xml.bind.v2.model.runtime.RuntimeNonElement, com.sun.xml.bind.v2.model.runtime.RuntimeTypeInfo] */
    public ArrayElementProperty(JAXBContextImpl grammar, RuntimeElementPropertyInfo prop) {
        super(grammar, prop, prop.getXmlName(), prop.isCollectionNillable());
        this.typeMap = new HashMap();
        this.refs = new HashMap();
        this.prop = prop;
        Name n = null;
        Iterator<? extends TypeRef<Type, Class>> it = prop.getTypes().iterator();
        while (it.hasNext()) {
            RuntimeTypeRef typeRef = (RuntimeTypeRef) it.next();
            Class type = (Class) typeRef.getTarget().getType();
            type = type.isPrimitive() ? RuntimeUtil.primitiveToBox.get(type) : type;
            JaxBeanInfo beanInfo = grammar.getOrCreate((RuntimeTypeInfo) typeRef.getTarget());
            TagAndType tt = new TagAndType(grammar.nameBuilder.createElementName(typeRef.getTagName()), beanInfo);
            this.typeMap.put(type, tt);
            this.refs.put(typeRef, beanInfo);
            if (typeRef.isNillable() && n == null) {
                n = tt.tagName;
            }
        }
        this.nillableTagName = n;
    }

    @Override // com.sun.xml.bind.v2.runtime.property.PropertyImpl, com.sun.xml.bind.v2.runtime.property.Property
    public void wrapUp() {
        super.wrapUp();
        this.refs = null;
        this.prop = null;
    }

    @Override // com.sun.xml.bind.v2.runtime.property.ArrayERProperty
    protected void serializeListBody(BeanT beanT, XMLSerializer w, ListT list) throws IOException, XMLStreamException, SAXException, AccessorException {
        ListIterator<ItemT> itr = this.lister.iterator(list, w);
        boolean isIdref = itr instanceof Lister.IDREFSIterator;
        while (itr.hasNext()) {
            try {
                ItemT item = itr.next();
                if (item != null) {
                    Class itemType = item.getClass();
                    if (isIdref) {
                        itemType = ((Lister.IDREFSIterator) itr).last().getClass();
                    }
                    TagAndType tt = this.typeMap.get(itemType);
                    Class itemType2 = itemType;
                    while (tt == null && itemType2 != null) {
                        Class itemType3 = itemType2.getSuperclass();
                        tt = this.typeMap.get(itemType3);
                        itemType2 = itemType3;
                    }
                    if (tt == null) {
                        w.startElement(this.typeMap.values().iterator().next().tagName, null);
                        w.childAsXsiType(item, this.fieldName, w.grammar.getBeanInfo(Object.class), false);
                    } else {
                        w.startElement(tt.tagName, null);
                        serializeItem(tt.beanInfo, item, w);
                    }
                    w.endElement();
                } else if (this.nillableTagName != null) {
                    w.startElement(this.nillableTagName, null);
                    w.writeXsiNilTrue();
                    w.endElement();
                }
            } catch (JAXBException e) {
                w.reportError(this.fieldName, e);
            }
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.property.ArrayERProperty
    public void createBodyUnmarshaller(UnmarshallerChain chain, QNameMap<ChildLoader> loaders) {
        int offset = chain.allocateOffset();
        Receiver recv = new ArrayERProperty.ReceiverImpl(offset);
        Iterator<? extends TypeRef<Type, Class>> it = this.prop.getTypes().iterator();
        while (it.hasNext()) {
            RuntimeTypeRef typeRef = (RuntimeTypeRef) it.next();
            Name tagName = chain.context.nameBuilder.createElementName(typeRef.getTagName());
            Loader item = createItemUnmarshaller(chain, typeRef);
            if (typeRef.isNillable() || chain.context.allNillable) {
                item = new XsiNilLoader.Array(item);
            }
            if (typeRef.getDefaultValue() != null) {
                item = new DefaultValueLoaderDecorator(item, typeRef.getDefaultValue());
            }
            loaders.put(tagName, (Name) new ChildLoader(item, recv));
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.property.Property
    public final PropertyKind getKind() {
        return PropertyKind.ELEMENT;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo] */
    private Loader createItemUnmarshaller(UnmarshallerChain chain, RuntimeTypeRef typeRef) {
        if (PropertyFactory.isLeaf(typeRef.getSource())) {
            Transducer xducer = typeRef.getTransducer();
            return new TextLoader(xducer);
        }
        return this.refs.get(typeRef).getLoader(chain.context, true);
    }

    @Override // com.sun.xml.bind.v2.runtime.property.PropertyImpl, com.sun.xml.bind.v2.runtime.property.Property
    public Accessor getElementPropertyAccessor(String nsUri, String localName) {
        if (this.wrapperTagName != null) {
            if (this.wrapperTagName.equals(nsUri, localName)) {
                return this.acc;
            }
            return null;
        }
        for (TagAndType tt : this.typeMap.values()) {
            if (tt.tagName.equals(nsUri, localName)) {
                return new NullSafeAccessor(this.acc, this.lister);
            }
        }
        return null;
    }
}
