package javax.xml.bind.attachment;

import javax.activation.DataHandler;
/* loaded from: gencallgraphv3.jar:jaxb-api-2.4.0-b180725.0427.jar:javax/xml/bind/attachment/AttachmentUnmarshaller.class */
public abstract class AttachmentUnmarshaller {
    public abstract DataHandler getAttachmentAsDataHandler(String str);

    public abstract byte[] getAttachmentAsByteArray(String str);

    public boolean isXOPPackage() {
        return false;
    }
}
