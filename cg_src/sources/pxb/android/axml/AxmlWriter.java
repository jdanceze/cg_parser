package pxb.android.axml;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import pxb.android.Item;
import pxb.android.StringItem;
import pxb.android.StringItems;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:axml-2.1.0-SNAPSHOT.jar:pxb/android/axml/AxmlWriter.class
 */
/* loaded from: gencallgraphv3.jar:axml-2.1.3.jar:pxb/android/axml/AxmlWriter.class */
public class AxmlWriter extends AxmlVisitor {
    static final Comparator<Attr> ATTR_CMP = new Comparator<Attr>() { // from class: pxb.android.axml.AxmlWriter.1
        @Override // java.util.Comparator
        public int compare(Attr a, Attr b) {
            int x = a.resourceId - b.resourceId;
            if (x == 0) {
                x = a.name.data.compareTo(b.name.data);
                if (x == 0) {
                    boolean aNsIsnull = a.ns == null;
                    boolean bNsIsnull = b.ns == null;
                    if (aNsIsnull) {
                        if (bNsIsnull) {
                            x = 0;
                        } else {
                            x = -1;
                        }
                    } else if (bNsIsnull) {
                        x = 1;
                    } else {
                        x = a.ns.data.compareTo(b.ns.data);
                    }
                }
            }
            return x;
        }
    };
    private List<NodeImpl> firsts = new ArrayList(3);
    private Map<String, Ns> nses = new HashMap();
    private List<StringItem> otherString = new ArrayList();
    private Map<String, StringItem> resourceId2Str = new HashMap();
    private List<Integer> resourceIds = new ArrayList();
    private List<StringItem> resourceString = new ArrayList();
    private StringItems stringItems = new StringItems();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:axml-2.1.0-SNAPSHOT.jar:pxb/android/axml/AxmlWriter$Attr.class
     */
    /* loaded from: gencallgraphv3.jar:axml-2.1.3.jar:pxb/android/axml/AxmlWriter$Attr.class */
    public static class Attr {
        public int index;
        public StringItem name;
        public StringItem ns;
        public int resourceId;
        public int type;
        public Object value;
        public StringItem raw;

        public Attr(StringItem ns, StringItem name, int resourceId) {
            this.ns = ns;
            this.name = name;
            this.resourceId = resourceId;
        }

        public void prepare(AxmlWriter axmlWriter) {
            this.ns = axmlWriter.updateNs(this.ns);
            if (this.name != null) {
                if (this.resourceId != -1) {
                    this.name = axmlWriter.updateWithResourceId(this.name, this.resourceId);
                } else {
                    this.name = axmlWriter.update(this.name);
                }
            }
            if (this.value instanceof StringItem) {
                this.value = axmlWriter.update((StringItem) this.value);
            }
            if (this.raw != null) {
                this.raw = axmlWriter.update(this.raw);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:axml-2.1.0-SNAPSHOT.jar:pxb/android/axml/AxmlWriter$NodeImpl.class
     */
    /* loaded from: gencallgraphv3.jar:axml-2.1.3.jar:pxb/android/axml/AxmlWriter$NodeImpl.class */
    public static class NodeImpl extends NodeVisitor {
        private Set<Attr> attrs;
        private List<NodeImpl> children;
        private int line;
        private StringItem name;
        private StringItem ns;
        private StringItem text;
        private int textLineNumber;
        Attr id;
        Attr style;
        Attr clz;

        public NodeImpl(String ns, String name) {
            super(null);
            this.attrs = new TreeSet(AxmlWriter.ATTR_CMP);
            this.children = new ArrayList();
            this.ns = ns == null ? null : new StringItem(ns);
            this.name = name == null ? null : new StringItem(name);
        }

        @Override // pxb.android.axml.NodeVisitor
        public void attr(String ns, String name, int resourceId, int type, Object value) {
            if (name == null) {
                throw new RuntimeException("name can't be null");
            }
            Attr a = new Attr(ns == null ? null : new StringItem(ns), new StringItem(name), resourceId);
            a.type = type;
            if (value instanceof ValueWrapper) {
                ValueWrapper valueWrapper = (ValueWrapper) value;
                if (valueWrapper.raw != null) {
                    a.raw = new StringItem(valueWrapper.raw);
                }
                a.value = Integer.valueOf(valueWrapper.ref);
                switch (valueWrapper.type) {
                    case 1:
                        this.id = a;
                        break;
                    case 2:
                        this.style = a;
                        break;
                    case 3:
                        this.clz = a;
                        break;
                }
            } else if (type == 3) {
                StringItem raw = new StringItem((String) value);
                a.raw = raw;
                a.value = raw;
            } else {
                a.raw = null;
                a.value = value;
            }
            this.attrs.add(a);
        }

        @Override // pxb.android.axml.NodeVisitor
        public NodeVisitor child(String ns, String name) {
            NodeImpl child = new NodeImpl(ns, name);
            this.children.add(child);
            return child;
        }

        @Override // pxb.android.axml.NodeVisitor
        public void end() {
        }

        @Override // pxb.android.axml.NodeVisitor
        public void line(int ln) {
            this.line = ln;
        }

        public int prepare(AxmlWriter axmlWriter) {
            this.ns = axmlWriter.updateNs(this.ns);
            this.name = axmlWriter.update(this.name);
            int attrIndex = 0;
            for (Attr attr : this.attrs) {
                int i = attrIndex;
                attrIndex++;
                attr.index = i;
                attr.prepare(axmlWriter);
            }
            this.text = axmlWriter.update(this.text);
            int size = 60 + (this.attrs.size() * 20);
            for (NodeImpl child : this.children) {
                size += child.prepare(axmlWriter);
            }
            if (this.text != null) {
                size += 28;
            }
            return size;
        }

        @Override // pxb.android.axml.NodeVisitor
        public void text(int ln, String value) {
            this.text = new StringItem(value);
            this.textLineNumber = ln;
        }

        void write(ByteBuffer out) throws IOException {
            out.putInt(1048834);
            out.putInt(36 + (this.attrs.size() * 20));
            out.putInt(this.line);
            out.putInt(-1);
            out.putInt(this.ns != null ? this.ns.index : -1);
            out.putInt(this.name.index);
            out.putInt(1310740);
            out.putShort((short) this.attrs.size());
            out.putShort((short) (this.id == null ? 0 : this.id.index + 1));
            out.putShort((short) (this.clz == null ? 0 : this.clz.index + 1));
            out.putShort((short) (this.style == null ? 0 : this.style.index + 1));
            for (Attr attr : this.attrs) {
                out.putInt(attr.ns == null ? -1 : attr.ns.index);
                out.putInt(attr.name.index);
                out.putInt(attr.raw != null ? attr.raw.index : -1);
                out.putInt((attr.type << 24) | 8);
                Object v = attr.value;
                if (v instanceof Item) {
                    ((Item) attr.value).writeout(out);
                } else if (v instanceof Boolean) {
                    out.putInt(Boolean.TRUE.equals(v) ? -1 : 0);
                } else if (v instanceof Float) {
                    out.putInt(Float.floatToIntBits(((Float) v).floatValue()));
                } else {
                    out.putInt(((Integer) attr.value).intValue());
                }
            }
            if (this.text != null) {
                out.putInt(1048836);
                out.putInt(28);
                out.putInt(this.textLineNumber);
                out.putInt(-1);
                out.putInt(this.text.index);
                out.putInt(8);
                out.putInt(0);
            }
            for (NodeImpl child : this.children) {
                child.write(out);
            }
            out.putInt(1048835);
            out.putInt(24);
            out.putInt(-1);
            out.putInt(-1);
            out.putInt(this.ns != null ? this.ns.index : -1);
            out.putInt(this.name.index);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:axml-2.1.0-SNAPSHOT.jar:pxb/android/axml/AxmlWriter$Ns.class
     */
    /* loaded from: gencallgraphv3.jar:axml-2.1.3.jar:pxb/android/axml/AxmlWriter$Ns.class */
    public static class Ns {
        int ln;
        StringItem prefix;
        StringItem uri;

        public Ns(StringItem prefix, StringItem uri, int ln) {
            this.prefix = prefix;
            this.uri = uri;
            this.ln = ln;
        }
    }

    @Override // pxb.android.axml.NodeVisitor
    public NodeVisitor child(String ns, String name) {
        NodeImpl first = new NodeImpl(ns, name);
        this.firsts.add(first);
        return first;
    }

    @Override // pxb.android.axml.NodeVisitor
    public void end() {
    }

    @Override // pxb.android.axml.AxmlVisitor
    public void ns(String prefix, String uri, int ln) {
        this.nses.put(uri, new Ns(prefix == null ? null : new StringItem(prefix), new StringItem(uri), ln));
    }

    private int prepare() throws IOException {
        int size = 0;
        for (NodeImpl first : this.firsts) {
            size += first.prepare(this);
        }
        int a = 0;
        for (Map.Entry<String, Ns> e : this.nses.entrySet()) {
            Ns ns = e.getValue();
            if (ns == null) {
                ns = new Ns(null, new StringItem(e.getKey()), 0);
                e.setValue(ns);
            }
            if (ns.prefix == null) {
                int i = a;
                a++;
                ns.prefix = new StringItem(String.format("axml_auto_%02d", Integer.valueOf(i)));
            }
            ns.prefix = update(ns.prefix);
            ns.uri = update(ns.uri);
        }
        int size2 = size + (this.nses.size() * 24 * 2);
        this.stringItems.addAll(this.resourceString);
        this.resourceString = null;
        this.stringItems.addAll(this.otherString);
        this.otherString = null;
        this.stringItems.prepare();
        int stringSize = this.stringItems.getSize();
        if (stringSize % 4 != 0) {
            stringSize += 4 - (stringSize % 4);
        }
        return size2 + 8 + stringSize + 8 + (this.resourceIds.size() * 4);
    }

    public byte[] toByteArray() throws IOException {
        int size = 8 + prepare();
        ByteBuffer out = ByteBuffer.allocate(size).order(ByteOrder.LITTLE_ENDIAN);
        out.putInt(524291);
        out.putInt(size);
        int stringSize = this.stringItems.getSize();
        int padding = 0;
        if (stringSize % 4 != 0) {
            padding = 4 - (stringSize % 4);
        }
        out.putInt(1835009);
        out.putInt(stringSize + padding + 8);
        this.stringItems.write(out);
        out.put(new byte[padding]);
        out.putInt(524672);
        out.putInt(8 + (this.resourceIds.size() * 4));
        for (Integer i : this.resourceIds) {
            out.putInt(i.intValue());
        }
        Stack<Ns> stack = new Stack<>();
        for (Map.Entry<String, Ns> e : this.nses.entrySet()) {
            Ns ns = e.getValue();
            stack.push(ns);
            out.putInt(1048832);
            out.putInt(24);
            out.putInt(-1);
            out.putInt(-1);
            out.putInt(ns.prefix.index);
            out.putInt(ns.uri.index);
        }
        for (NodeImpl first : this.firsts) {
            first.write(out);
        }
        while (stack.size() > 0) {
            Ns ns2 = stack.pop();
            out.putInt(1048833);
            out.putInt(24);
            out.putInt(ns2.ln);
            out.putInt(-1);
            out.putInt(ns2.prefix.index);
            out.putInt(ns2.uri.index);
        }
        return out.array();
    }

    StringItem update(StringItem item) {
        if (item == null) {
            return null;
        }
        int i = this.otherString.indexOf(item);
        if (i < 0) {
            StringItem copy = new StringItem(item.data);
            this.otherString.add(copy);
            return copy;
        }
        return this.otherString.get(i);
    }

    StringItem updateNs(StringItem item) {
        if (item == null) {
            return null;
        }
        String ns = item.data;
        if (!this.nses.containsKey(ns)) {
            this.nses.put(ns, null);
        }
        return update(item);
    }

    StringItem updateWithResourceId(StringItem name, int resourceId) {
        String key = name.data + resourceId;
        StringItem item = this.resourceId2Str.get(key);
        if (item != null) {
            return item;
        }
        StringItem copy = new StringItem(name.data);
        this.resourceIds.add(Integer.valueOf(resourceId));
        this.resourceString.add(copy);
        this.resourceId2Str.put(key, copy);
        return copy;
    }
}
