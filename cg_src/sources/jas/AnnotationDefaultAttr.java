package jas;

import java.io.DataOutputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/AnnotationDefaultAttr.class */
public class AnnotationDefaultAttr {
    static CP attr = new AsciiCP("AnnotationDefault");
    ElemValPair elem;

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resolve(ClassEnv e) {
        e.addCPItem(attr);
        this.elem.resolve(e);
    }

    public AnnotationDefaultAttr(ElemValPair s) {
        this.elem = s;
    }

    int size() {
        return this.elem.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
        out.writeShort(e.getCPIndex(attr));
        out.writeInt(size());
        this.elem.write(e, out);
    }
}
