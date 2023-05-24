package com.sun.xml.bind.v2.runtime;

import com.sun.xml.bind.api.BridgeContext;
import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallerImpl;
import javax.xml.bind.JAXBException;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.attachment.AttachmentMarshaller;
import javax.xml.bind.attachment.AttachmentUnmarshaller;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/BridgeContextImpl.class */
public final class BridgeContextImpl extends BridgeContext {
    public final UnmarshallerImpl unmarshaller;
    public final MarshallerImpl marshaller;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BridgeContextImpl(JAXBContextImpl context) {
        this.unmarshaller = context.createUnmarshaller();
        this.marshaller = context.createMarshaller();
    }

    @Override // com.sun.xml.bind.api.BridgeContext
    public void setErrorHandler(ValidationEventHandler handler) {
        try {
            this.unmarshaller.setEventHandler(handler);
            this.marshaller.setEventHandler(handler);
        } catch (JAXBException e) {
            throw new Error(e);
        }
    }

    @Override // com.sun.xml.bind.api.BridgeContext
    public void setAttachmentMarshaller(AttachmentMarshaller m) {
        this.marshaller.setAttachmentMarshaller(m);
    }

    @Override // com.sun.xml.bind.api.BridgeContext
    public void setAttachmentUnmarshaller(AttachmentUnmarshaller u) {
        this.unmarshaller.setAttachmentUnmarshaller(u);
    }

    @Override // com.sun.xml.bind.api.BridgeContext
    public AttachmentMarshaller getAttachmentMarshaller() {
        return this.marshaller.getAttachmentMarshaller();
    }

    @Override // com.sun.xml.bind.api.BridgeContext
    public AttachmentUnmarshaller getAttachmentUnmarshaller() {
        return this.unmarshaller.getAttachmentUnmarshaller();
    }
}
