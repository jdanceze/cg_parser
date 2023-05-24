package pxb.android.axml;

import java.util.HashMap;
import java.util.Map;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:axml-2.1.0-SNAPSHOT.jar:pxb/android/axml/DumpAdapter.class
 */
/* loaded from: gencallgraphv3.jar:axml-2.1.3.jar:pxb/android/axml/DumpAdapter.class */
public class DumpAdapter extends AxmlVisitor {
    protected int deep;
    protected Map<String, String> nses;

    public DumpAdapter() {
        this(null);
    }

    public DumpAdapter(NodeVisitor nv) {
        this(nv, 0, new HashMap());
    }

    public DumpAdapter(NodeVisitor nv, int x, Map<String, String> nses) {
        super(nv);
        this.deep = x;
        this.nses = nses;
    }

    @Override // pxb.android.axml.NodeVisitor
    public void attr(String ns, String name, int resourceId, int type, Object obj) {
        for (int i = 0; i < this.deep; i++) {
            System.out.print("  ");
        }
        if (ns != null) {
            System.out.print(String.format("%s:", getPrefix(ns)));
        }
        System.out.print(name);
        if (resourceId != -1) {
            System.out.print(String.format("(%08x)", Integer.valueOf(resourceId)));
        }
        if (obj instanceof String) {
            System.out.print(String.format("=[%08x]\"%s\"", Integer.valueOf(type), obj));
        } else if (obj instanceof Boolean) {
            System.out.print(String.format("=[%08x]\"%b\"", Integer.valueOf(type), obj));
        } else if (obj instanceof ValueWrapper) {
            ValueWrapper w = (ValueWrapper) obj;
            System.out.print(String.format("=[%08x]@%08x, raw: \"%s\"", Integer.valueOf(type), Integer.valueOf(w.ref), w.raw));
        } else if (type == 1) {
            System.out.print(String.format("=[%08x]@%08x", Integer.valueOf(type), obj));
        } else {
            System.out.print(String.format("=[%08x]%08x", Integer.valueOf(type), obj));
        }
        System.out.println();
        super.attr(ns, name, resourceId, type, obj);
    }

    @Override // pxb.android.axml.NodeVisitor
    public NodeVisitor child(String ns, String name) {
        for (int i = 0; i < this.deep; i++) {
            System.out.print("  ");
        }
        System.out.print("<");
        if (ns != null) {
            System.out.print(getPrefix(ns) + ":");
        }
        System.out.println(name);
        NodeVisitor nv = super.child(ns, name);
        if (nv != null) {
            return new DumpAdapter(nv, this.deep + 1, this.nses);
        }
        return null;
    }

    protected String getPrefix(String uri) {
        String prefix;
        if (this.nses != null && (prefix = this.nses.get(uri)) != null) {
            return prefix;
        }
        return uri;
    }

    @Override // pxb.android.axml.AxmlVisitor
    public void ns(String prefix, String uri, int ln) {
        System.out.println(prefix + "=" + uri);
        this.nses.put(uri, prefix);
        super.ns(prefix, uri, ln);
    }

    @Override // pxb.android.axml.NodeVisitor
    public void text(int ln, String value) {
        for (int i = 0; i < this.deep + 1; i++) {
            System.out.print("  ");
        }
        System.out.print("T: ");
        System.out.println(value);
        super.text(ln, value);
    }
}
