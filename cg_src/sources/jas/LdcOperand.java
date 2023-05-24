package jas;

import java.io.DataOutputStream;
import java.io.IOException;
/* compiled from: InsnOperand.java */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/LdcOperand.class */
class LdcOperand extends InsnOperand implements RuntimeConstants {
    CP cpe;
    Insn source;
    boolean wide;

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.InsnOperand
    public int size(ClassEnv ce, CodeAttr code) throws jasError {
        if (this.wide) {
            return 2;
        }
        int idx = ce.getCPIndex(this.cpe);
        if (idx > 255) {
            this.wide = true;
            this.source.opc = 19;
            return 2;
        }
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LdcOperand(Insn s, CP cpe) {
        this.source = s;
        this.cpe = cpe;
        this.wide = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LdcOperand(Insn s, CP cpe, boolean wide) {
        this.source = s;
        this.cpe = cpe;
        this.wide = wide;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.InsnOperand
    public void resolve(ClassEnv e) {
        e.addCPItem(this.cpe);
        if (this.cpe instanceof ClassCP) {
            e.requireJava5();
        }
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
