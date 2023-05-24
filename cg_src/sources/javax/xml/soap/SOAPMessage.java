package javax.xml.soap;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import javax.activation.DataHandler;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/soap/SOAPMessage.class */
public abstract class SOAPMessage {
    public static final String CHARACTER_SET_ENCODING = "javax.xml.soap.character-set-encoding";
    public static final String WRITE_XML_DECLARATION = "javax.xml.soap.write-xml-declaration";

    public abstract void setContentDescription(String str);

    public abstract String getContentDescription();

    public abstract SOAPPart getSOAPPart();

    public abstract SOAPBody getSOAPBody() throws SOAPException;

    public abstract SOAPHeader getSOAPHeader() throws SOAPException;

    public abstract void removeAllAttachments();

    public abstract int countAttachments();

    public abstract Iterator getAttachments();

    public abstract Iterator getAttachments(MimeHeaders mimeHeaders);

    public abstract void addAttachmentPart(AttachmentPart attachmentPart);

    public abstract AttachmentPart createAttachmentPart();

    public AttachmentPart createAttachmentPart(DataHandler dataHandler) {
        AttachmentPart createAttachmentPart = createAttachmentPart();
        createAttachmentPart.setDataHandler(dataHandler);
        return createAttachmentPart;
    }

    public abstract MimeHeaders getMimeHeaders();

    public AttachmentPart createAttachmentPart(Object obj, String str) {
        AttachmentPart createAttachmentPart = createAttachmentPart();
        createAttachmentPart.setContent(obj, str);
        return createAttachmentPart;
    }

    public abstract void saveChanges() throws SOAPException;

    public abstract boolean saveRequired();

    public abstract void writeTo(OutputStream outputStream) throws SOAPException, IOException;

    public abstract void setProperty(String str, Object obj) throws SOAPException;

    public abstract Object getProperty(String str) throws SOAPException;
}
