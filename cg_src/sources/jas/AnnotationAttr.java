package jas;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/AnnotationAttr.class */
public class AnnotationAttr {
    AsciiCP type;
    ArrayList list;

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resolve(ClassEnv e) {
        e.addCPItem(this.type);
        if (this.list != null) {
            Iterator it = this.list.iterator();
            while (it.hasNext()) {
                ((ElemValPair) it.next()).resolve(e);
            }
        }
    }

    public AnnotationAttr(String type, ArrayList elems) {
        this.list = new ArrayList();
        this.type = new AsciiCP(type);
        this.list = elems;
    }

    public AnnotationAttr() {
        this.list = new ArrayList();
    }

    public void setType(String type) {
        this.type = new AsciiCP(type);
    }

    public void addElemValPair(ElemValPair pair) {
        this.list.add(pair);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int size() {
        int i = 4;
        if (this.list != null) {
            Iterator it = this.list.iterator();
            while (it.hasNext()) {
                i += ((ElemValPair) it.next()).size();
            }
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
        out.writeShort(e.getCPIndex(this.type));
        if (this.list == null) {
            out.writeShort(0);
        } else {
            out.writeShort(this.list.size());
        }
        if (this.list != null) {
            Iterator it = this.list.iterator();
            while (it.hasNext()) {
                ((ElemValPair) it.next()).write(e, out);
            }
        }
    }
}
