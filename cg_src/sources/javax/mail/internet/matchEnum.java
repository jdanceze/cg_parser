package javax.mail.internet;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;
import javax.mail.Header;
/* compiled from: InternetHeaders.java */
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/internet/matchEnum.class */
class matchEnum implements Enumeration {
    private Enumeration e;
    private String[] names;
    private boolean match;
    private boolean want_line;
    private hdr next_header = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    public matchEnum(Vector v, String[] n, boolean m, boolean l) {
        this.e = v.elements();
        this.names = n;
        this.match = m;
        this.want_line = l;
    }

    @Override // java.util.Enumeration
    public boolean hasMoreElements() {
        if (this.next_header == null) {
            this.next_header = nextMatch();
        }
        return this.next_header != null;
    }

    @Override // java.util.Enumeration
    public Object nextElement() {
        if (this.next_header == null) {
            this.next_header = nextMatch();
        }
        if (this.next_header == null) {
            throw new NoSuchElementException("No more headers");
        }
        hdr h = this.next_header;
        this.next_header = null;
        if (this.want_line) {
            return h.line;
        }
        return new Header(h.getName(), h.getValue());
    }

    private hdr nextMatch() {
        while (this.e.hasMoreElements()) {
            hdr h = (hdr) this.e.nextElement();
            if (h.line != null) {
                if (this.names == null) {
                    if (this.match) {
                        return null;
                    }
                    return h;
                }
                int i = 0;
                while (true) {
                    if (i < this.names.length) {
                        if (!this.names[i].equalsIgnoreCase(h.name)) {
                            i++;
                        } else if (this.match) {
                            return h;
                        }
                    } else if (!this.match) {
                        return h;
                    }
                }
            }
        }
        return null;
    }
}
