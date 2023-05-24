package javax.mail;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/BodyPart.class */
public abstract class BodyPart implements Part {
    protected Multipart parent;

    public Multipart getParent() {
        return this.parent;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setParent(Multipart parent) {
        this.parent = parent;
    }
}
