package jas;

import java.io.DataOutputStream;
import java.io.IOException;
/* compiled from: InsnOperand.java */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/UnsignedByteOperand.class */
class UnsignedByteOperand extends InsnOperand {
    int val;

    /* JADX INFO: Access modifiers changed from: package-private */
    public UnsignedByteOperand(int n) {
        this.val = n;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.InsnOperand
    public int size(ClassEnv ce, CodeAttr code) {
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.InsnOperand
    public void write(ClassEnv e, CodeAttr ce, DataOutputStream out) throws IOException, jasError {
        if (this.val >= 256) {
            throw new jasError("Operand is too large (" + this.val + ") for this instruction");
        }
        out.writeByte((byte) (255 & this.val));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.InsnOperand
    public void resolve(ClassEnv e) {
    }
}
