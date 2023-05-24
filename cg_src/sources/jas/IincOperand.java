package jas;

import java.io.DataOutputStream;
import java.io.IOException;
/* compiled from: InsnOperand.java */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/IincOperand.class */
class IincOperand extends InsnOperand implements RuntimeConstants {
    int vindex;
    int constt;

    /* JADX INFO: Access modifiers changed from: package-private */
    public IincOperand(int vindex, int constt) {
        this.vindex = vindex;
        this.constt = constt;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.InsnOperand
    public int size(ClassEnv ce, CodeAttr code) {
        if (this.vindex > 255 || this.constt > 127 || this.constt < -128) {
            return 5;
        }
        return 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.InsnOperand
    public void resolve(ClassEnv e) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.InsnOperand
    public void writePrefix(ClassEnv e, CodeAttr ce, DataOutputStream out) throws IOException {
        if (this.vindex > 255 || this.constt > 127 || this.constt < -128) {
            out.writeByte(-60);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.InsnOperand
    public void write(ClassEnv e, CodeAttr ce, DataOutputStream out) throws IOException {
        if (this.vindex > 255 || this.constt > 127 || this.constt < -128) {
            out.writeShort((short) (65535 & this.vindex));
            out.writeShort((short) (65535 & this.constt));
            return;
        }
        out.writeByte((byte) (255 & this.vindex));
        out.writeByte((byte) (255 & this.constt));
    }
}
