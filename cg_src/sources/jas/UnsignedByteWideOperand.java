package jas;

import java.io.DataOutputStream;
import java.io.IOException;
/* compiled from: InsnOperand.java */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/UnsignedByteWideOperand.class */
class UnsignedByteWideOperand extends InsnOperand implements RuntimeConstants {
    int val;

    /* JADX INFO: Access modifiers changed from: package-private */
    public UnsignedByteWideOperand(int n) {
        this.val = n;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.InsnOperand
    public int size(ClassEnv ce, CodeAttr code) {
        return this.val >= 256 ? 3 : 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.InsnOperand
    public void writePrefix(ClassEnv e, CodeAttr ce, DataOutputStream out) throws IOException {
        if (this.val > 255) {
            out.writeByte(-60);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.InsnOperand
    public void write(ClassEnv e, CodeAttr ce, DataOutputStream out) throws IOException {
        if (this.val > 255) {
            out.writeShort((short) (65535 & this.val));
        } else {
            out.writeByte((byte) (this.val & 255));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.InsnOperand
    public void resolve(ClassEnv e) {
    }
}
