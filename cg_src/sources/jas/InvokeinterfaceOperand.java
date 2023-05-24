package jas;

import java.io.DataOutputStream;
import java.io.IOException;
/* compiled from: InsnOperand.java */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/InvokeinterfaceOperand.class */
class InvokeinterfaceOperand extends InsnOperand {
    CP cpe;
    int nargs;

    /* JADX INFO: Access modifiers changed from: package-private */
    public InvokeinterfaceOperand(CP cpe, int nargs) {
        this.cpe = cpe;
        this.nargs = nargs;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.InsnOperand
    public int size(ClassEnv ce, CodeAttr code) {
        return 4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.InsnOperand
    public void resolve(ClassEnv e) {
        e.addCPItem(this.cpe);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.InsnOperand
    public void write(ClassEnv e, CodeAttr ce, DataOutputStream out) throws IOException, jasError {
        out.writeShort(e.getCPIndex(this.cpe));
        out.writeByte((byte) (255 & this.nargs));
        out.writeByte(0);
    }
}
