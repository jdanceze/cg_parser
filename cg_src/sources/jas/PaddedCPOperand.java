package jas;

import java.io.DataOutputStream;
import java.io.IOException;
/* compiled from: InsnOperand.java */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/PaddedCPOperand.class */
class PaddedCPOperand extends CPOperand {
    /* JADX INFO: Access modifiers changed from: package-private */
    public PaddedCPOperand(CP cpe) {
        super(cpe);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.CPOperand, jas.InsnOperand
    public void write(ClassEnv e, CodeAttr ce, DataOutputStream out) throws IOException, jasError {
        super.write(e, ce, out);
        out.writeShort(0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.CPOperand, jas.InsnOperand
    public int size(ClassEnv ce, CodeAttr code) {
        return super.size(ce, code) + 2;
    }
}
