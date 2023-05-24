package jas;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/ArrayElemValPair.class */
public class ArrayElemValPair extends ElemValPair {
    ArrayList list;

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.ElemValPair
    public void resolve(ClassEnv e) {
        super.resolve(e);
        if (this.list != null) {
            Iterator it = this.list.iterator();
            while (it.hasNext()) {
                ((ElemValPair) it.next()).resolve(e);
            }
        }
    }

    public ArrayElemValPair(String name, char kind, ArrayList list) {
        super(name, kind);
        this.list = list;
    }

    @Override // jas.ElemValPair
    public void setNoName() {
        if (this.name.uniq.equals("default")) {
            super.setNoName();
        }
        if (this.list == null) {
            return;
        }
        Iterator it = this.list.iterator();
        while (it.hasNext()) {
            ((ElemValPair) it.next()).setNoName();
        }
    }

    public ArrayElemValPair(String name, char kind) {
        super(name, kind);
    }

    public void addElemValPair(ElemValPair elem) {
        if (this.list == null) {
            this.list = new ArrayList();
        }
        this.list.add(elem);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.ElemValPair
    public int size() {
        int i = super.size() + 2;
        if (this.list != null) {
            Iterator it = this.list.iterator();
            while (it.hasNext()) {
                i += ((ElemValPair) it.next()).size();
            }
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.ElemValPair
    public void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
        super.write(e, out);
        if (this.list != null) {
            out.writeShort(this.list.size());
        } else {
            out.writeShort(0);
        }
        if (this.list != null) {
            Iterator it = this.list.iterator();
            while (it.hasNext()) {
                ((ElemValPair) it.next()).write(e, out);
            }
        }
    }
}
