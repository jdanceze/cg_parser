package jas;

import java.io.DataOutputStream;
import java.io.IOException;
/* compiled from: InsnOperand.java */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/CPOperand.class */
class CPOperand extends InsnOperand {
    CP cpe;
    boolean wide;

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.InsnOperand
    public int size(ClassEnv ce, CodeAttr code) {
        return this.wide ? 2 : 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CPOperand(CP cpe) {
        this.cpe = cpe;
        this.wide = true;
    }

    CPOperand(CP cpe, boolean wide) {
        this.cpe = cpe;
        this.wide = wide;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.InsnOperand
    public void resolve(ClassEnv e) {
        e.addCPItem(this.cpe);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.InsnOperand
    public void write(ClassEnv e, CodeAttr ce, DataOutputStream out) throws IOException, jasError {
        int idx = e.getCPIndex(this.cpe);
        if (this.wide) {
            out.writeShort((short) idx);
        } else if (idx > 255) {
            throw new jasError("exceeded size for small cpidx" + this.cpe);
        } else {
            out.writeByte((byte) (255 & idx));
        }
    }
}
