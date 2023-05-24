package ppg.util;

import java.io.IOException;
import java.io.Writer;
import soot.coffi.Instruction;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: CodeWriter.java */
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:ppg/util/Newline.class */
public class Newline extends AllowBreak {
    /* JADX INFO: Access modifiers changed from: package-private */
    public Newline(int n_) {
        super(n_, "");
    }

    @Override // ppg.util.AllowBreak, ppg.util.Item
    int formatN(int lmargin, int pos, int rmargin, int fin, boolean can_break, boolean nofail) throws Overrun {
        this.broken = true;
        if (can_break) {
            return format(this.next, lmargin, lmargin + this.indent, rmargin, fin, can_break, nofail);
        }
        throw new Overrun(1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // ppg.util.AllowBreak, ppg.util.Item
    public int sendOutput(Writer o, int lmargin, int pos) throws IOException {
        o.write("\r\n");
        for (int i = 0; i < lmargin + this.indent; i++) {
            o.write(Instruction.argsep);
        }
        return lmargin + this.indent;
    }
}
