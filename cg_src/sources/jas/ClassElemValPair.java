package jas;

import java.io.DataOutputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/ClassElemValPair.class */
public class ClassElemValPair extends ElemValPair {
    AsciiCP cval;

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.ElemValPair
    public void resolve(ClassEnv e) {
        super.resolve(e);
        e.addCPItem(this.cval);
    }

    public ClassElemValPair(String name, char kind, String cval) {
        super(name, kind);
        this.cval = new AsciiCP(cval);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.ElemValPair
    public int size() {
        return super.size() + 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.ElemValPair
    public void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
        super.write(e, out);
        out.writeShort(e.getCPIndex(this.cval));
    }
}
