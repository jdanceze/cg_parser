package javax.mail;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/Provider.class */
public class Provider {
    private Type type;
    private String protocol;
    private String className;
    private String vendor;
    private String version;

    /* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/Provider$Type.class */
    public static class Type {
        public static final Type STORE = new Type("Store");
        public static final Type TRANSPORT = new Type("Transport");
        private String type;

        private Type(String type) {
            this.type = type;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Provider(Type type, String protocol, String classname, String vendor, String version) {
        this.type = type;
        this.protocol = protocol;
        this.className = classname;
        this.vendor = vendor;
        this.version = version;
    }

    public Type getType() {
        return this.type;
    }

    public String getProtocol() {
        return this.protocol;
    }

    public String getClassName() {
        return this.className;
    }

    public String getVendor() {
        return this.vendor;
    }

    public String getVersion() {
        return this.version;
    }

    public String toString() {
        String s = "javax.mail.Provider[";
        if (this.type == Type.STORE) {
            s = new StringBuffer().append(s).append("STORE,").toString();
        } else if (this.type == Type.TRANSPORT) {
            s = new StringBuffer().append(s).append("TRANSPORT,").toString();
        }
        String s2 = new StringBuffer().append(s).append(this.protocol).append(",").append(this.className).toString();
        if (this.vendor != null) {
            s2 = new StringBuffer().append(s2).append(",").append(this.vendor).toString();
        }
        if (this.version != null) {
            s2 = new StringBuffer().append(s2).append(",").append(this.version).toString();
        }
        return new StringBuffer().append(s2).append("]").toString();
    }
}
