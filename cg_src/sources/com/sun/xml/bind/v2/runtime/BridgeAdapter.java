package com.sun.xml.bind.v2.runtime;

import com.sun.istack.NotNull;
import com.sun.xml.bind.api.TypeReference;
import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallerImpl;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/BridgeAdapter.class */
public final class BridgeAdapter<OnWire, InMemory> extends InternalBridge<InMemory> {
    private final InternalBridge<OnWire> core;
    private final Class<? extends XmlAdapter<OnWire, InMemory>> adapter;

    public BridgeAdapter(InternalBridge<OnWire> core, Class<? extends XmlAdapter<OnWire, InMemory>> adapter) {
        super(core.getContext());
        this.core = core;
        this.adapter = adapter;
    }

    @Override // com.sun.xml.bind.api.Bridge
    public void marshal(Marshaller m, InMemory inMemory, XMLStreamWriter output) throws JAXBException {
        this.core.marshal(m, (Marshaller) adaptM(m, inMemory), output);
    }

    @Override // com.sun.xml.bind.api.Bridge
    public void marshal(Marshaller m, InMemory inMemory, OutputStream output, NamespaceContext nsc) throws JAXBException {
        this.core.marshal(m, (Marshaller) adaptM(m, inMemory), output, nsc);
    }

    @Override // com.sun.xml.bind.api.Bridge
    public void marshal(Marshaller m, InMemory inMemory, Node output) throws JAXBException {
        this.core.marshal(m, (Marshaller) adaptM(m, inMemory), output);
    }

    @Override // com.sun.xml.bind.api.Bridge
    public void marshal(Marshaller context, InMemory inMemory, ContentHandler contentHandler) throws JAXBException {
        this.core.marshal(context, (Marshaller) adaptM(context, inMemory), contentHandler);
    }

    @Override // com.sun.xml.bind.api.Bridge
    public void marshal(Marshaller context, InMemory inMemory, Result result) throws JAXBException {
        this.core.marshal(context, (Marshaller) adaptM(context, inMemory), result);
    }

    private OnWire adaptM(Marshaller m, InMemory v) throws JAXBException {
        XMLSerializer serializer = ((MarshallerImpl) m).serializer;
        serializer.pushCoordinator();
        try {
            OnWire _adaptM = _adaptM(serializer, v);
            serializer.popCoordinator();
            return _adaptM;
        } catch (Throwable th) {
            serializer.popCoordinator();
            throw th;
        }
    }

    private OnWire _adaptM(XMLSerializer serializer, InMemory v) throws MarshalException {
        XmlAdapter<OnWire, InMemory> a = serializer.getAdapter(this.adapter);
        try {
            return a.marshal(v);
        } catch (Exception e) {
            serializer.handleError(e, v, null);
            throw new MarshalException(e);
        }
    }

    @Override // com.sun.xml.bind.api.Bridge
    @NotNull
    public InMemory unmarshal(Unmarshaller u, XMLStreamReader in) throws JAXBException {
        return adaptU(u, this.core.unmarshal(u, in));
    }

    @Override // com.sun.xml.bind.api.Bridge
    @NotNull
    public InMemory unmarshal(Unmarshaller u, Source in) throws JAXBException {
        return adaptU(u, this.core.unmarshal(u, in));
    }

    @Override // com.sun.xml.bind.api.Bridge
    @NotNull
    public InMemory unmarshal(Unmarshaller u, InputStream in) throws JAXBException {
        return adaptU(u, this.core.unmarshal(u, in));
    }

    @Override // com.sun.xml.bind.api.Bridge
    @NotNull
    public InMemory unmarshal(Unmarshaller u, Node n) throws JAXBException {
        return adaptU(u, this.core.unmarshal(u, n));
    }

    @Override // com.sun.xml.bind.api.Bridge
    public TypeReference getTypeReference() {
        return this.core.getTypeReference();
    }

    @NotNull
    private InMemory adaptU(Unmarshaller _u, OnWire v) throws JAXBException {
        UnmarshallerImpl u = (UnmarshallerImpl) _u;
        XmlAdapter<OnWire, InMemory> a = u.coordinator.getAdapter(this.adapter);
        u.coordinator.pushCoordinator();
        try {
            try {
                InMemory unmarshal = a.unmarshal(v);
                u.coordinator.popCoordinator();
                return unmarshal;
            } catch (Exception e) {
                throw new UnmarshalException(e);
            }
        } catch (Throwable th) {
            u.coordinator.popCoordinator();
            throw th;
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.InternalBridge
    void marshal(InMemory o, XMLSerializer out) throws IOException, SAXException, XMLStreamException {
        try {
            this.core.marshal((InternalBridge<OnWire>) _adaptM(XMLSerializer.getInstance(), o), out);
        } catch (MarshalException e) {
        }
    }
}
