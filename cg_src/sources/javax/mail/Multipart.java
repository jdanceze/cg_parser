package javax.mail;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/Multipart.class */
public abstract class Multipart {
    protected Vector parts = new Vector();
    protected String contentType = "multipart/mixed";
    protected Part parent;

    public abstract void writeTo(OutputStream outputStream) throws IOException, MessagingException;

    /* JADX INFO: Access modifiers changed from: protected */
    public void setMultipartDataSource(MultipartDataSource mp) throws MessagingException {
        this.contentType = mp.getContentType();
        int count = mp.getCount();
        for (int i = 0; i < count; i++) {
            addBodyPart(mp.getBodyPart(i));
        }
    }

    public String getContentType() {
        return this.contentType;
    }

    public int getCount() throws MessagingException {
        if (this.parts == null) {
            return 0;
        }
        return this.parts.size();
    }

    public BodyPart getBodyPart(int index) throws MessagingException {
        if (this.parts == null) {
            throw new IndexOutOfBoundsException("No such BodyPart");
        }
        return (BodyPart) this.parts.elementAt(index);
    }

    public boolean removeBodyPart(BodyPart part) throws MessagingException {
        if (this.parts == null) {
            throw new MessagingException("No such body part");
        }
        boolean ret = this.parts.removeElement(part);
        part.setParent(null);
        return ret;
    }

    public void removeBodyPart(int index) throws MessagingException {
        if (this.parts == null) {
            throw new IndexOutOfBoundsException("No such BodyPart");
        }
        BodyPart part = (BodyPart) this.parts.elementAt(index);
        this.parts.removeElementAt(index);
        part.setParent(null);
    }

    public synchronized void addBodyPart(BodyPart part) throws MessagingException {
        if (this.parts == null) {
            this.parts = new Vector();
        }
        this.parts.addElement(part);
        part.setParent(this);
    }

    public synchronized void addBodyPart(BodyPart part, int index) throws MessagingException {
        if (this.parts == null) {
            this.parts = new Vector();
        }
        this.parts.insertElementAt(part, index);
        part.setParent(this);
    }

    public Part getParent() {
        return this.parent;
    }

    public void setParent(Part parent) {
        this.parent = parent;
    }
}
