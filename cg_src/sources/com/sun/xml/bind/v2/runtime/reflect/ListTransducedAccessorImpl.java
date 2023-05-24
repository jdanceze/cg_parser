package com.sun.xml.bind.v2.runtime.reflect;

import com.sun.xml.bind.WhiteSpaceProcessor;
import com.sun.xml.bind.api.AccessorException;
import com.sun.xml.bind.v2.runtime.Transducer;
import com.sun.xml.bind.v2.runtime.XMLSerializer;
import javax.xml.bind.JAXBException;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/ListTransducedAccessorImpl.class */
public final class ListTransducedAccessorImpl<BeanT, ListT, ItemT, PackT> extends DefaultTransducedAccessor<BeanT> {
    private final Transducer<ItemT> xducer;
    private final Lister<BeanT, ListT, ItemT, PackT> lister;
    private final Accessor<BeanT, ListT> acc;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.bind.v2.runtime.reflect.DefaultTransducedAccessor, com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor
    public /* bridge */ /* synthetic */ CharSequence print(Object obj) throws AccessorException, SAXException {
        return print((ListTransducedAccessorImpl<BeanT, ListT, ItemT, PackT>) obj);
    }

    public ListTransducedAccessorImpl(Transducer<ItemT> xducer, Accessor<BeanT, ListT> acc, Lister<BeanT, ListT, ItemT, PackT> lister) {
        this.xducer = xducer;
        this.lister = lister;
        this.acc = acc;
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor
    public boolean useNamespace() {
        return this.xducer.useNamespace();
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor
    public void declareNamespace(BeanT bean, XMLSerializer w) throws AccessorException, SAXException {
        ListT list = this.acc.get(bean);
        if (list != null) {
            ListIterator<ItemT> itr = this.lister.iterator(list, w);
            while (itr.hasNext()) {
                try {
                    ItemT item = itr.next();
                    if (item != null) {
                        this.xducer.declareNamespace(item, w);
                    }
                } catch (JAXBException e) {
                    w.reportError(null, e);
                }
            }
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.DefaultTransducedAccessor, com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor
    public String print(BeanT o) throws AccessorException, SAXException {
        ListT list = this.acc.get(o);
        if (list == null) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        XMLSerializer w = XMLSerializer.getInstance();
        ListIterator<ItemT> itr = this.lister.iterator(list, w);
        while (itr.hasNext()) {
            try {
                ItemT item = itr.next();
                if (item != null) {
                    if (buf.length() > 0) {
                        buf.append(' ');
                    }
                    buf.append(this.xducer.print(item));
                }
            } catch (JAXBException e) {
                w.reportError(null, e);
            }
        }
        return buf.toString();
    }

    private void processValue(BeanT bean, CharSequence s) throws AccessorException, SAXException {
        PackT pack = this.lister.startPacking(bean, this.acc);
        int idx = 0;
        int len = s.length();
        while (true) {
            int p = idx;
            while (p < len && !WhiteSpaceProcessor.isWhiteSpace(s.charAt(p))) {
                p++;
            }
            CharSequence token = s.subSequence(idx, p);
            if (!token.equals("")) {
                this.lister.addToPack(pack, this.xducer.parse(token));
            }
            if (p == len) {
                break;
            }
            while (p < len && WhiteSpaceProcessor.isWhiteSpace(s.charAt(p))) {
                p++;
            }
            if (p == len) {
                break;
            }
            idx = p;
        }
        this.lister.endPacking(pack, bean, this.acc);
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor
    public void parse(BeanT bean, CharSequence lexical) throws AccessorException, SAXException {
        processValue(bean, lexical);
    }

    @Override // com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor
    public boolean hasValue(BeanT bean) throws AccessorException {
        return this.acc.get(bean) != null;
    }
}
