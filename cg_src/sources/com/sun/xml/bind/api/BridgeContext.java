package com.sun.xml.bind.api;

import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.attachment.AttachmentMarshaller;
import javax.xml.bind.attachment.AttachmentUnmarshaller;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/api/BridgeContext.class */
public abstract class BridgeContext {
    public abstract void setErrorHandler(ValidationEventHandler validationEventHandler);

    public abstract void setAttachmentMarshaller(AttachmentMarshaller attachmentMarshaller);

    public abstract void setAttachmentUnmarshaller(AttachmentUnmarshaller attachmentUnmarshaller);

    public abstract AttachmentMarshaller getAttachmentMarshaller();

    public abstract AttachmentUnmarshaller getAttachmentUnmarshaller();
}
