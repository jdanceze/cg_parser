package javax.xml.bind.attachment;

import javax.activation.DataHandler;
/* loaded from: gencallgraphv3.jar:jaxb-api-2.4.0-b180725.0427.jar:javax/xml/bind/attachment/AttachmentMarshaller.class */
public abstract class AttachmentMarshaller {
    public abstract String addMtomAttachment(DataHandler dataHandler, String str, String str2);

    public abstract String addMtomAttachment(byte[] bArr, int i, int i2, String str, String str2, String str3);

    public abstract String addSwaRefAttachment(DataHandler dataHandler);

    public boolean isXOPPackage() {
        return false;
    }
}
