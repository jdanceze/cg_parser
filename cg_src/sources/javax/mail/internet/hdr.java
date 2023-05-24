package javax.mail.internet;
/* compiled from: InternetHeaders.java */
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/internet/hdr.class */
class hdr {
    String name;
    String line;

    /* JADX INFO: Access modifiers changed from: package-private */
    public hdr(String l) {
        int i = l.indexOf(58);
        if (i < 0) {
            this.name = l.trim();
        } else {
            this.name = l.substring(0, i).trim();
        }
        this.line = l;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public hdr(String n, String v) {
        this.name = n;
        if (v != null) {
            this.line = new StringBuffer().append(n).append(": ").append(v).toString();
        } else {
            this.line = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getName() {
        return this.name;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getValue() {
        char c;
        int i = this.line.indexOf(58);
        if (i < 0) {
            return this.line;
        }
        int j = i + 1;
        while (j < this.line.length() && ((c = this.line.charAt(j)) == ' ' || c == '\t' || c == '\r' || c == '\n')) {
            j++;
        }
        return this.line.substring(j);
    }
}
