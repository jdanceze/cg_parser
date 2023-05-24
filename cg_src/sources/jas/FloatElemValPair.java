package jas;

import java.io.DataOutputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/FloatElemValPair.class */
public class FloatElemValPair extends ElemValPair {
    FloatCP val;

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.ElemValPair
    public void resolve(ClassEnv e) {
        super.resolve(e);
        e.addCPItem(this.val);
    }

    public FloatElemValPair(String name, char kind, float val) {
        super(name, kind);
        this.val = new FloatCP(val);
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
        out.writeShort(e.getCPIndex(this.val));
    }
}
