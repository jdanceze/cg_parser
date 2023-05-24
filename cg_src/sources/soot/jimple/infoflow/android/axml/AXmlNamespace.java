package soot.jimple.infoflow.android.axml;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/axml/AXmlNamespace.class */
public class AXmlNamespace {
    protected String prefix;
    protected String uri;
    protected int line;

    public AXmlNamespace(String prefix, String uri, int line) {
        this.prefix = prefix;
        this.uri = uri;
        this.line = line;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getUri() {
        return this.uri;
    }

    public int getLine() {
        return this.line;
    }
}
