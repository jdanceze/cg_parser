package jas;

import java.io.DataOutputStream;
import java.io.IOException;
/* compiled from: InsnOperand.java */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/LabelOperand.class */
class LabelOperand extends InsnOperand {
    Label target;
    Insn source;
    boolean wide;

    /* JADX INFO: Access modifiers changed from: package-private */
    public LabelOperand(Label l, Insn source) {
        this.target = l;
        this.source = source;
        this.wide = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LabelOperand(Label l, Insn source, boolean wide) {
        this.target = l;
        this.source = source;
        this.wide = wide;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.InsnOperand
    public int size(ClassEnv ce, CodeAttr code) {
        return this.wide ? 4 : 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.InsnOperand
    public void resolve(ClassEnv e) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.InsnOperand
    public void write(ClassEnv e, CodeAttr ce, DataOutputStream out) throws IOException, jasError {
        if (this.wide) {
            this.target.writeWideOffset(ce, this.source, out);
        } else {
            this.target.writeOffset(ce, this.source, out);
        }
    }
}
