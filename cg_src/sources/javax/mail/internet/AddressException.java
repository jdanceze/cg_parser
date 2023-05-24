package javax.mail.internet;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/internet/AddressException.class */
public class AddressException extends ParseException {
    protected String ref;
    protected int pos;

    public AddressException() {
        this.ref = null;
        this.pos = -1;
    }

    public AddressException(String s) {
        super(s);
        this.ref = null;
        this.pos = -1;
    }

    public AddressException(String s, String ref) {
        super(s);
        this.ref = null;
        this.pos = -1;
        this.ref = ref;
    }

    public AddressException(String s, String ref, int pos) {
        super(s);
        this.ref = null;
        this.pos = -1;
        this.ref = ref;
        this.pos = pos;
    }

    public String getRef() {
        return this.ref;
    }

    public int getPos() {
        return this.pos;
    }

    @Override // java.lang.Throwable
    public String toString() {
        String s = super.toString();
        if (this.ref == null) {
            return s;
        }
        String s2 = new StringBuffer().append(s).append(" in string ``").append(this.ref).append("''").toString();
        if (this.pos < 0) {
            return s2;
        }
        return new StringBuffer().append(s2).append(" at position ").append(this.pos).toString();
    }
}
