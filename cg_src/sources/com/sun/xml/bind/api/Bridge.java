package com.sun.xml.bind.api;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import com.sun.xml.bind.v2.runtime.BridgeContextImpl;
import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.bind.v2.runtime.MarshallerImpl;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.attachment.AttachmentMarshaller;
import javax.xml.bind.attachment.AttachmentUnmarshaller;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/api/Bridge.class */
public abstract class Bridge<T> {
    protected final JAXBContextImpl context;

    public abstract void marshal(@NotNull Marshaller marshaller, T t, XMLStreamWriter xMLStreamWriter) throws JAXBException;

    public abstract void marshal(@NotNull Marshaller marshaller, T t, OutputStream outputStream, NamespaceContext namespaceContext) throws JAXBException;

    public abstract void marshal(@NotNull Marshaller marshaller, T t, Node node) throws JAXBException;

    public abstract void marshal(@NotNull Marshaller marshaller, T t, ContentHandler contentHandler) throws JAXBException;

    public abstract void marshal(@NotNull Marshaller marshaller, T t, Result result) throws JAXBException;

    @NotNull
    public abstract T unmarshal(@NotNull Unmarshaller unmarshaller, @NotNull XMLStreamReader xMLStreamReader) throws JAXBException;

    @NotNull
    public abstract T unmarshal(@NotNull Unmarshaller unmarshaller, @NotNull Source source) throws JAXBException;

    @NotNull
    public abstract T unmarshal(@NotNull Unmarshaller unmarshaller, @NotNull InputStream inputStream) throws JAXBException;

    @NotNull
    public abstract T unmarshal(@NotNull Unmarshaller unmarshaller, @NotNull Node node) throws JAXBException;

    public abstract TypeReference getTypeReference();

    /* JADX INFO: Access modifiers changed from: protected */
    public Bridge(JAXBContextImpl context) {
        this.context = context;
    }

    @NotNull
    public JAXBRIContext getContext() {
        return this.context;
    }

    public final void marshal(T object, XMLStreamWriter output) throws JAXBException {
        marshal((Bridge<T>) object, output, (AttachmentMarshaller) null);
    }

    public final void marshal(T object, XMLStreamWriter output, AttachmentMarshaller am) throws JAXBException {
        Marshaller m = this.context.marshallerPool.take();
        m.setAttachmentMarshaller(am);
        marshal(m, (Marshaller) object, output);
        m.setAttachmentMarshaller(null);
        this.context.marshallerPool.recycle(m);
    }

    public final void marshal(@NotNull BridgeContext context, T object, XMLStreamWriter output) throws JAXBException {
        marshal((Marshaller) ((BridgeContextImpl) context).marshaller, (MarshallerImpl) object, output);
    }

    public void marshal(T object, OutputStream output, NamespaceContext nsContext) throws JAXBException {
        marshal((Bridge<T>) object, output, nsContext, (AttachmentMarshaller) null);
    }

    public void marshal(T object, OutputStream output, NamespaceContext nsContext, AttachmentMarshaller am) throws JAXBException {
        Marshaller m = this.context.marshallerPool.take();
        m.setAttachmentMarshaller(am);
        marshal(m, (Marshaller) object, output, nsContext);
        m.setAttachmentMarshaller(null);
        this.context.marshallerPool.recycle(m);
    }

    public final void marshal(@NotNull BridgeContext context, T object, OutputStream output, NamespaceContext nsContext) throws JAXBException {
        marshal((Marshaller) ((BridgeContextImpl) context).marshaller, (MarshallerImpl) object, output, nsContext);
    }

    public final void marshal(T object, Node output) throws JAXBException {
        Marshaller m = this.context.marshallerPool.take();
        marshal(m, (Marshaller) object, output);
        this.context.marshallerPool.recycle(m);
    }

    public final void marshal(@NotNull BridgeContext context, T object, Node output) throws JAXBException {
        marshal((Marshaller) ((BridgeContextImpl) context).marshaller, (MarshallerImpl) object, output);
    }

    public final void marshal(T object, ContentHandler contentHandler) throws JAXBException {
        marshal((Bridge<T>) object, contentHandler, (AttachmentMarshaller) null);
    }

    public final void marshal(T object, ContentHandler contentHandler, AttachmentMarshaller am) throws JAXBException {
        Marshaller m = this.context.marshallerPool.take();
        m.setAttachmentMarshaller(am);
        marshal(m, (Marshaller) object, contentHandler);
        m.setAttachmentMarshaller(null);
        this.context.marshallerPool.recycle(m);
    }

    public final void marshal(@NotNull BridgeContext context, T object, ContentHandler contentHandler) throws JAXBException {
        marshal((Marshaller) ((BridgeContextImpl) context).marshaller, (MarshallerImpl) object, contentHandler);
    }

    public final void marshal(T object, Result result) throws JAXBException {
        Marshaller m = this.context.marshallerPool.take();
        marshal(m, (Marshaller) object, result);
        this.context.marshallerPool.recycle(m);
    }

    public final void marshal(@NotNull BridgeContext context, T object, Result result) throws JAXBException {
        marshal((Marshaller) ((BridgeContextImpl) context).marshaller, (MarshallerImpl) object, result);
    }

    private T exit(T r, Unmarshaller u) {
        u.setAttachmentUnmarshaller(null);
        this.context.unmarshallerPool.recycle(u);
        return r;
    }

    @NotNull
    public final T unmarshal(@NotNull XMLStreamReader in) throws JAXBException {
        return unmarshal(in, (AttachmentUnmarshaller) null);
    }

    @NotNull
    public final T unmarshal(@NotNull XMLStreamReader in, @Nullable AttachmentUnmarshaller au) throws JAXBException {
        Unmarshaller u = this.context.unmarshallerPool.take();
        u.setAttachmentUnmarshaller(au);
        return exit(unmarshal(u, in), u);
    }

    @NotNull
    public final T unmarshal(@NotNull BridgeContext context, @NotNull XMLStreamReader in) throws JAXBException {
        return unmarshal(((BridgeContextImpl) context).unmarshaller, in);
    }

    @NotNull
    public final T unmarshal(@NotNull Source in) throws JAXBException {
        return unmarshal(in, (AttachmentUnmarshaller) null);
    }

    @NotNull
    public final T unmarshal(@NotNull Source in, @Nullable AttachmentUnmarshaller au) throws JAXBException {
        Unmarshaller u = this.context.unmarshallerPool.take();
        u.setAttachmentUnmarshaller(au);
        return exit(unmarshal(u, in), u);
    }

    @NotNull
    public final T unmarshal(@NotNull BridgeContext context, @NotNull Source in) throws JAXBException {
        return unmarshal(((BridgeContextImpl) context).unmarshaller, in);
    }

    @NotNull
    public final T unmarshal(@NotNull InputStream in) throws JAXBException {
        Unmarshaller u = this.context.unmarshallerPool.take();
        return exit(unmarshal(u, in), u);
    }

    @NotNull
    public final T unmarshal(@NotNull BridgeContext context, @NotNull InputStream in) throws JAXBException {
        return unmarshal(((BridgeContextImpl) context).unmarshaller, in);
    }

    @NotNull
    public final T unmarshal(@NotNull Node n) throws JAXBException {
        return unmarshal(n, (AttachmentUnmarshaller) null);
    }

    @NotNull
    public final T unmarshal(@NotNull Node n, @Nullable AttachmentUnmarshaller au) throws JAXBException {
        Unmarshaller u = this.context.unmarshallerPool.take();
        u.setAttachmentUnmarshaller(au);
        return exit(unmarshal(u, n), u);
    }

    @NotNull
    public final T unmarshal(@NotNull BridgeContext context, @NotNull Node n) throws JAXBException {
        return unmarshal(((BridgeContextImpl) context).unmarshaller, n);
    }
}
