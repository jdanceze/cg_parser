package jas;

import java.io.DataOutputStream;
import java.io.IOException;
/* compiled from: InsnOperand.java */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/TableswitchOperand.class */
class TableswitchOperand extends InsnOperand {
    int min;
    int max;
    Label dflt;
    Label[] jmp;
    Insn source;

    /* JADX INFO: Access modifiers changed from: package-private */
    public TableswitchOperand(Insn s, int min, int max, Label def, Label[] j) {
        this.min = min;
        this.max = max;
        this.dflt = def;
        this.jmp = j;
        this.source = s;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.InsnOperand
    public void resolve(ClassEnv e) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.InsnOperand
    public int size(ClassEnv ce, CodeAttr code) throws jasError {
        int sz = 12;
        int source_pc = code.getPc(this.source);
        if ((source_pc + 1) % 4 != 0) {
            sz = 12 + (4 - ((source_pc + 1) % 4));
        }
        if (this.jmp != null) {
            sz += 4 * this.jmp.length;
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
        out.writeInt(this.min);
        out.writeInt(this.max);
        int cnt = this.jmp.length;
        for (int x2 = 0; x2 < cnt; x2++) {
            this.jmp[x2].writeWideOffset(ce, this.source, out);
        }
    }
}
