package com.sun.xml.bind.v2.runtime.reflect;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import com.sun.istack.SAXException2;
import com.sun.xml.bind.WhiteSpaceProcessor;
import com.sun.xml.bind.api.AccessorException;
import com.sun.xml.bind.v2.model.core.ID;
import com.sun.xml.bind.v2.model.impl.RuntimeModelBuilder;
import com.sun.xml.bind.v2.model.runtime.RuntimeNonElementRef;
import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.bind.v2.runtime.Name;
import com.sun.xml.bind.v2.runtime.Transducer;
import com.sun.xml.bind.v2.runtime.XMLSerializer;
import com.sun.xml.bind.v2.runtime.unmarshaller.LocatorEx;
import com.sun.xml.bind.v2.runtime.unmarshaller.Patcher;
import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import java.io.IOException;
import java.util.concurrent.Callable;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/TransducedAccessor.class */
public abstract class TransducedAccessor<BeanT> {
    @Nullable
    public abstract CharSequence print(@NotNull BeanT beant) throws AccessorException, SAXException;

    public abstract void parse(BeanT beant, CharSequence charSequence) throws AccessorException, SAXException;

    public abstract boolean hasValue(BeanT beant) throws AccessorException;

    public abstract void writeLeafElement(XMLSerializer xMLSerializer, Name name, BeanT beant, String str) throws SAXException, AccessorException, IOException, XMLStreamException;

    public abstract void writeText(XMLSerializer xMLSerializer, BeanT beant, String str) throws AccessorException, SAXException, IOException, XMLStreamException;

    public boolean useNamespace() {
        return false;
    }

    public void declareNamespace(BeanT o, XMLSerializer w) throws AccessorException, SAXException {
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo] */
    public static <T> TransducedAccessor<T> get(JAXBContextImpl context, RuntimeNonElementRef ref) {
        Transducer xducer = RuntimeModelBuilder.createTransducer(ref);
        ?? source = ref.getSource();
        if (source.isCollection()) {
            return new ListTransducedAccessorImpl(xducer, source.getAccessor(), Lister.create(Utils.REFLECTION_NAVIGATOR.erasure(source.getRawType()), source.id(), source.getAdapter()));
        }
        if (source.id() == ID.IDREF) {
            return new IDREFTransducedAccessorImpl(source.getAccessor());
        }
        if (xducer.useNamespace()) {
            return new CompositeContextDependentTransducedAccessorImpl(context, xducer, source.getAccessor());
        }
        return new CompositeTransducedAccessorImpl(context, xducer, source.getAccessor());
    }

    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/TransducedAccessor$CompositeContextDependentTransducedAccessorImpl.class */
    static class CompositeContextDependentTransducedAccessorImpl<BeanT, ValueT> extends CompositeTransducedAccessorImpl<BeanT, ValueT> {
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !TransducedAccessor.class.desiredAssertionStatus();
        }

        public CompositeContextDependentTransducedAccessorImpl(JAXBContextImpl context, Transducer<ValueT> xducer, Accessor<BeanT, ValueT> acc) {
            super(context, xducer, acc);
            if (!$assertionsDisabled && !xducer.useNamespace()) {
                throw new AssertionError();
            }
        }

        @Override // com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor
        public boolean useNamespace() {
            return true;
        }

        @Override // com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor
        public void declareNamespace(BeanT bean, XMLSerializer w) throws AccessorException {
            ValueT o = this.acc.get(bean);
            if (o != null) {
                this.xducer.declareNamespace(o, w);
            }
        }

        @Override // com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor.CompositeTransducedAccessorImpl, com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor
        public void writeLeafElement(XMLSerializer w, Name tagName, BeanT o, String fieldName) throws SAXException, AccessorException, IOException, XMLStreamException {
            w.startElement(tagName, null);
            declareNamespace(o, w);
            w.endNamespaceDecls(null);
            w.endAttributes();
            this.xducer.writeText(w, this.acc.get(o), fieldName);
            w.endElement();
        }
    }

    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/TransducedAccessor$CompositeTransducedAccessorImpl.class */
    public static class CompositeTransducedAccessorImpl<BeanT, ValueT> extends TransducedAccessor<BeanT> {
        protected final Transducer<ValueT> xducer;
        protected final Accessor<BeanT, ValueT> acc;

        public CompositeTransducedAccessorImpl(JAXBContextImpl context, Transducer<ValueT> xducer, Accessor<BeanT, ValueT> acc) {
            this.xducer = xducer;
            this.acc = acc.optimize(context);
        }

        @Override // com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor
        public CharSequence print(BeanT bean) throws AccessorException {
            ValueT o = this.acc.get(bean);
            if (o == null) {
                return null;
            }
            return this.xducer.print(o);
        }

        @Override // com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor
        public void parse(BeanT bean, CharSequence lexical) throws AccessorException, SAXException {
            this.acc.set(bean, this.xducer.parse(lexical));
        }

        @Override // com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor
        public boolean hasValue(BeanT bean) throws AccessorException {
            return this.acc.getUnadapted(bean) != null;
        }

        @Override // com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor
        public void writeLeafElement(XMLSerializer w, Name tagName, BeanT o, String fieldName) throws SAXException, AccessorException, IOException, XMLStreamException {
            this.xducer.writeLeafElement(w, tagName, this.acc.get(o), fieldName);
        }

        @Override // com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor
        public void writeText(XMLSerializer w, BeanT o, String fieldName) throws AccessorException, SAXException, IOException, XMLStreamException {
            this.xducer.writeText(w, this.acc.get(o), fieldName);
        }
    }

    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/TransducedAccessor$IDREFTransducedAccessorImpl.class */
    private static final class IDREFTransducedAccessorImpl<BeanT, TargetT> extends DefaultTransducedAccessor<BeanT> {
        private final Accessor<BeanT, TargetT> acc;
        private final Class<TargetT> targetType;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.sun.xml.bind.v2.runtime.reflect.DefaultTransducedAccessor, com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor
        public /* bridge */ /* synthetic */ CharSequence print(Object obj) throws AccessorException, SAXException {
            return print((IDREFTransducedAccessorImpl<BeanT, TargetT>) obj);
        }

        public IDREFTransducedAccessorImpl(Accessor<BeanT, TargetT> acc) {
            this.acc = acc;
            this.targetType = acc.getValueType();
        }

        @Override // com.sun.xml.bind.v2.runtime.reflect.DefaultTransducedAccessor, com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor
        public String print(BeanT bean) throws AccessorException, SAXException {
            TargetT target = this.acc.get(bean);
            if (target == null) {
                return null;
            }
            XMLSerializer w = XMLSerializer.getInstance();
            try {
                String id = w.grammar.getBeanInfo((Object) target, true).getId(target, w);
                if (id == null) {
                    w.errorMissingId(target);
                }
                return id;
            } catch (JAXBException e) {
                w.reportError(null, e);
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void assign(BeanT bean, TargetT t, UnmarshallingContext context) throws AccessorException {
            if (!this.targetType.isInstance(t)) {
                context.handleError(Messages.UNASSIGNABLE_TYPE.format(this.targetType, t.getClass()));
            } else {
                this.acc.set(bean, t);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor
        public void parse(final BeanT bean, CharSequence lexical) throws AccessorException, SAXException {
            final String idref = WhiteSpaceProcessor.trim(lexical).toString();
            final UnmarshallingContext context = UnmarshallingContext.getInstance();
            final Callable callable = context.getObjectFromId(idref, this.acc.valueType);
            if (callable == null) {
                context.errorUnresolvedIDREF(bean, idref, context.getLocator());
                return;
            }
            try {
                Object call = callable.call();
                if (call != null) {
                    assign(bean, call, context);
                    return;
                }
                final LocatorEx loc = new LocatorEx.Snapshot(context.getLocator());
                context.addPatcher(new Patcher() { // from class: com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor.IDREFTransducedAccessorImpl.1
                    @Override // com.sun.xml.bind.v2.runtime.unmarshaller.Patcher
                    public void run() throws SAXException {
                        try {
                            Object call2 = callable.call();
                            if (call2 != null) {
                                IDREFTransducedAccessorImpl.this.assign(bean, call2, context);
                            } else {
                                context.errorUnresolvedIDREF(bean, idref, loc);
                            }
                        } catch (AccessorException e) {
                            context.handleError(e);
                        } catch (RuntimeException e2) {
                            throw e2;
                        } catch (SAXException e3) {
                            throw e3;
                        } catch (Exception e4) {
                            throw new SAXException2(e4);
                        }
                    }
                });
            } catch (RuntimeException e) {
                throw e;
            } catch (SAXException e2) {
                throw e2;
            } catch (Exception e3) {
                throw new SAXException2(e3);
            }
        }

        @Override // com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor
        public boolean hasValue(BeanT bean) throws AccessorException {
            return this.acc.get(bean) != null;
        }
    }
}
