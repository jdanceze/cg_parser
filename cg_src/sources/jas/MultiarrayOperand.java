package jas;

import java.io.DataOutputStream;
import java.io.IOException;
/* compiled from: InsnOperand.java */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/MultiarrayOperand.class */
class MultiarrayOperand extends InsnOperand {
    CP cpe;
    int sz;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MultiarrayOperand(CP cpe, int sz) {
        this.cpe = cpe;
        this.sz = sz;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.InsnOperand
    public void resolve(ClassEnv e) {
        e.addCPItem(this.cpe);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.InsnOperand
    public int size(ClassEnv ce, CodeAttr code) {
        return 3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.InsnOperand
    public void write(ClassEnv e, CodeAttr ce, DataOutputStream out) throws IOException, jasError {
        out.writeShort(e.getCPIndex(this.cpe));
        out.writeByte((byte) (255 & this.sz));
    }
}
