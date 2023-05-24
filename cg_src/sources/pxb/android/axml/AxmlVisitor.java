package pxb.android.axml;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:axml-2.1.0-SNAPSHOT.jar:pxb/android/axml/AxmlVisitor.class
 */
/* loaded from: gencallgraphv3.jar:axml-2.1.3.jar:pxb/android/axml/AxmlVisitor.class */
public class AxmlVisitor extends NodeVisitor {
    public AxmlVisitor() {
    }

    public AxmlVisitor(NodeVisitor av) {
        super(av);
    }

    public void ns(String prefix, String uri, int ln) {
        if (this.nv != null && (this.nv instanceof AxmlVisitor)) {
            ((AxmlVisitor) this.nv).ns(prefix, uri, ln);
        }
    }
}
