package com.sun.xml.bind.v2.runtime.property;

import com.sun.xml.bind.api.AccessorException;
import com.sun.xml.bind.v2.model.core.PropertyKind;
import com.sun.xml.bind.v2.model.runtime.RuntimeElementPropertyInfo;
import com.sun.xml.bind.v2.model.runtime.RuntimeTypeRef;
import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.bind.v2.runtime.Name;
import com.sun.xml.bind.v2.runtime.Transducer;
import com.sun.xml.bind.v2.runtime.XMLSerializer;
import com.sun.xml.bind.v2.runtime.reflect.Accessor;
import com.sun.xml.bind.v2.runtime.reflect.ListTransducedAccessorImpl;
import com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor;
import com.sun.xml.bind.v2.runtime.unmarshaller.ChildLoader;
import com.sun.xml.bind.v2.runtime.unmarshaller.DefaultValueLoaderDecorator;
import com.sun.xml.bind.v2.runtime.unmarshaller.LeafPropertyLoader;
import com.sun.xml.bind.v2.runtime.unmarshaller.Loader;
import com.sun.xml.bind.v2.util.QNameMap;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/property/ListElementProperty.class */
public final class ListElementProperty<BeanT, ListT, ItemT> extends ArrayProperty<BeanT, ListT, ItemT> {
    private final Name tagName;
    private final String defaultValue;
    private final TransducedAccessor<BeanT> xacc;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ListElementProperty.class.desiredAssertionStatus();
    }

    public ListElementProperty(JAXBContextImpl grammar, RuntimeElementPropertyInfo prop) {
        super(grammar, prop);
        if (!$assertionsDisabled && !prop.isValueList()) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && prop.getTypes().size() != 1) {
            throw new AssertionError();
        }
        RuntimeTypeRef ref = (RuntimeTypeRef) prop.getTypes().get(0);
        this.tagName = grammar.nameBuilder.createElementName(ref.getTagName());
        this.defaultValue = ref.getDefaultValue();
        Transducer xducer = ref.getTransducer();
        this.xacc = new ListTransducedAccessorImpl(xducer, this.acc, this.lister);
    }

    @Override // com.sun.xml.bind.v2.runtime.property.Property
    public PropertyKind getKind() {
        return PropertyKind.ELEMENT;
    }

    @Override // com.sun.xml.bind.v2.runtime.property.StructureLoaderBuilder
    public void buildChildElementUnmarshallers(UnmarshallerChain chain, QNameMap<ChildLoader> handlers) {
        Loader l = new LeafPropertyLoader(this.xacc);
        handlers.put(this.tagName, (Name) new ChildLoader(new DefaultValueLoaderDecorator(l, this.defaultValue), null));
    }

    @Override // com.sun.xml.bind.v2.runtime.property.PropertyImpl, com.sun.xml.bind.v2.runtime.property.Property
    public void serializeBody(BeanT o, XMLSerializer w, Object outerPeer) throws SAXException, AccessorException, IOException, XMLStreamException {
        ListT list = this.acc.get(o);
        if (list != null) {
            if (this.xacc.useNamespace()) {
                w.startElement(this.tagName, null);
                this.xacc.declareNamespace(o, w);
                w.endNamespaceDecls(list);
                w.endAttributes();
                this.xacc.writeText(w, o, this.fieldName);
                w.endElement();
                return;
            }
            this.xacc.writeLeafElement(w, this.tagName, o, this.fieldName);
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.property.PropertyImpl, com.sun.xml.bind.v2.runtime.property.Property
    public Accessor getElementPropertyAccessor(String nsUri, String localName) {
        if (this.tagName != null && this.tagName.equals(nsUri, localName)) {
            return this.acc;
        }
        return null;
    }
}
