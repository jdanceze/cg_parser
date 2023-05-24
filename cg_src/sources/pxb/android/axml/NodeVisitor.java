package pxb.android.axml;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:axml-2.1.0-SNAPSHOT.jar:pxb/android/axml/NodeVisitor.class
 */
/* loaded from: gencallgraphv3.jar:axml-2.1.3.jar:pxb/android/axml/NodeVisitor.class */
public abstract class NodeVisitor {
    public static final int TYPE_FIRST_INT = 16;
    public static final int TYPE_INT_BOOLEAN = 18;
    public static final int TYPE_INT_HEX = 17;
    public static final int TYPE_REFERENCE = 1;
    public static final int TYPE_STRING = 3;
    protected NodeVisitor nv;

    public NodeVisitor() {
    }

    public NodeVisitor(NodeVisitor nv) {
        this.nv = nv;
    }

    public void attr(String ns, String name, int resourceId, int type, Object obj) {
        if (this.nv != null) {
            this.nv.attr(ns, name, resourceId, type, obj);
        }
    }

    public NodeVisitor child(String ns, String name) {
        if (this.nv != null) {
            return this.nv.child(ns, name);
        }
        return null;
    }

    public void end() {
        if (this.nv != null) {
            this.nv.end();
        }
    }

    public void line(int ln) {
        if (this.nv != null) {
            this.nv.line(ln);
        }
    }

    public void text(int lineNumber, String value) {
        if (this.nv != null) {
            this.nv.text(lineNumber, value);
        }
    }
}
