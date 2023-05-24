package jas;

import java.io.DataOutputStream;
import java.io.IOException;
/* compiled from: InsnOperand.java */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/LookupswitchOperand.class */
class LookupswitchOperand extends InsnOperand {
    Label dflt;
    Insn source;
    int[] match;
    Label[] jmp;

    /* JADX INFO: Access modifiers changed from: package-private */
    public LookupswitchOperand(Insn s, Label def, int[] m, Label[] j) {
        this.dflt = def;
        this.jmp = j;
        this.match = m;
        this.source = s;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.InsnOperand
    public void resolve(ClassEnv e) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.InsnOperand
    public int size(ClassEnv ce, CodeAttr code) throws jasError {
        int sz = 8;
        int source_pc = code.getPc(this.source);
        if ((source_pc + 1) % 4 != 0) {
            sz = 8 + (4 - ((source_pc + 1) % 4));
        }
        if (this.jmp != null) {
            sz += 8 * this.jmp.length;
        }
        return sz;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.InsnOperand
    public void write(ClassEnv e, CodeAttr ce, DataOutputStream out) throws IOException, jasError {
        int source_pc = ce.getPc(this.source);
        if ((source_pc + 1) % 4 != 0) {
            int pad = 4 - ((source_pc + 1) % 4);
            for (int x = 0; x < pad; x++) {
                out.writeByte(0);
            }
        }
        this.dflt.writeWideOffset(ce, this.source, out);
        if (this.jmp == null) {
            out.writeInt(0);
            return;
        }
        out.writeInt(this.jmp.length);
        for (int x2 = 0; x2 < this.jmp.length; x2++) {
            out.writeInt(this.match[x2]);
            this.jmp[x2].writeWideOffset(ce, this.source, out);
        }
    }
}
