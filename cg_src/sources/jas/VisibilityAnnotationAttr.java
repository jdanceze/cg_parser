package jas;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/VisibilityAnnotationAttr.class */
public class VisibilityAnnotationAttr {
    AsciiCP attr;
    ArrayList list;
    protected String kind;

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resolve(ClassEnv e) {
        e.addCPItem(this.attr);
        if (this.list != null) {
            Iterator it = this.list.iterator();
            while (it.hasNext()) {
                ((AnnotationAttr) it.next()).resolve(e);
            }
        }
    }

    public VisibilityAnnotationAttr(String kind, ArrayList annotations) {
        this.list = new ArrayList();
        this.attr = new AsciiCP(kind + "Annotations");
        this.list = annotations;
        this.kind = kind;
    }

    public VisibilityAnnotationAttr() {
        this.list = new ArrayList();
    }

    public void setKind(String k) {
        this.kind = k;
        this.attr = new AsciiCP(k + "Annotations");
    }

    public void addAnnotation(AnnotationAttr annot) {
        this.list.add(annot);
    }

    public ArrayList getList() {
        return this.list;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int size() {
        int i = 2;
        if (this.list != null) {
            Iterator it = this.list.iterator();
            while (it.hasNext()) {
                i += ((AnnotationAttr) it.next()).size();
            }
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
        out.writeShort(e.getCPIndex(this.attr));
        out.writeInt(size());
        if (this.list == null) {
            out.writeShort(0);
        } else {
            out.writeShort(this.list.size());
        }
        if (this.list != null) {
            Iterator it = this.list.iterator();
            while (it.hasNext()) {
                ((AnnotationAttr) it.next()).write(e, out);
            }
        }
    }

    public String getKind() {
        return this.kind;
    }
}
