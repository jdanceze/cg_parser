package ppg.util;

import java.io.IOException;
import java.io.Writer;
import soot.coffi.Instruction;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: CodeWriter.java */
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:ppg/util/AllowBreak.class */
public class AllowBreak extends Item {
    int indent;
    boolean broken = true;
    String alt;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AllowBreak(int n_, String alt_) {
        this.indent = n_;
        this.alt = alt_;
    }

    @Override // ppg.util.Item
    int formatN(int lmargin, int pos, int rmargin, int fin, boolean can_break, boolean nofail) throws Overrun {
        int pos2;
        if (can_break) {
            pos2 = lmargin + this.indent;
            this.broken = true;
        } else {
            pos2 = pos + this.alt.length();
            this.broken = false;
        }
        return format(this.next, lmargin, pos2, rmargin, fin, can_break, nofail);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // ppg.util.Item
    public int sendOutput(Writer o, int lmargin, int pos) throws IOException {
        if (this.broken) {
            o.write("\r\n");
            for (int i = 0; i < lmargin + this.indent; i++) {
                o.write(Instruction.argsep);
            }
            return lmargin + this.indent;
        }
        o.write(this.alt);
        return pos + this.alt.length();
    }
}
