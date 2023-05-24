package com.sun.xml.bind.v2.runtime;

import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import javax.activation.DataHandler;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.attachment.AttachmentMarshaller;
import javax.xml.bind.attachment.AttachmentUnmarshaller;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/SwaRefAdapter.class */
public final class SwaRefAdapter extends XmlAdapter<String, DataHandler> {
    @Override // javax.xml.bind.annotation.adapters.XmlAdapter
    public DataHandler unmarshal(String cid) {
        AttachmentUnmarshaller au = UnmarshallingContext.getInstance().parent.getAttachmentUnmarshaller();
        return au.getAttachmentAsDataHandler(cid);
    }

    @Override // javax.xml.bind.annotation.adapters.XmlAdapter
    public String marshal(DataHandler data) {
        if (data == null) {
            return null;
        }
        AttachmentMarshaller am = XMLSerializer.getInstance().attachmentMarshaller;
        return am.addSwaRefAttachment(data);
    }
}
