package jas;

import java.io.DataOutputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/Label.class */
public class Label extends Insn implements RuntimeConstants {
    String id;

    public Label(String tag) {
        this.id = tag.intern();
        this.opc = -1;
        this.operand = null;
    }

    @Override // jas.Insn
    void write(ClassEnv e, CodeAttr ce, DataOutputStream out) {
    }

    @Override // jas.Insn
    int size(ClassEnv e, CodeAttr ce) {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void writeOffset(CodeAttr ce, Insn source, DataOutputStream out) throws jasError, IOException {
        int tpc;
        int pc = ce.getPc(this);
        if (source == null) {
            tpc = 0;
        } else {
            tpc = ce.getPc(source);
        }
        short offset = (short) (pc - tpc);
        out.writeShort(offset);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void writeWideOffset(CodeAttr ce, Insn source, DataOutputStream out) throws IOException, jasError {
        int tpc;
        int pc = ce.getPc(this);
        if (source == null) {
            tpc = 0;
        } else {
            tpc = ce.getPc(source);
        }
        out.writeInt(pc - tpc);
    }

    public String toString() {
        return "Label: " + this.id;
    }
}
