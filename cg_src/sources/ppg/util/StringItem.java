package ppg.util;

import java.io.IOException;
import java.io.Writer;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: CodeWriter.java */
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:ppg/util/StringItem.class */
public class StringItem extends Item {
    String s;

    /* JADX INFO: Access modifiers changed from: package-private */
    public StringItem(String s_) {
        this.s = s_;
    }

    @Override // ppg.util.Item
    int formatN(int lmargin, int pos, int rmargin, int fin, boolean can_break, boolean nofail) throws Overrun {
        return format(this.next, lmargin, pos + this.s.length(), rmargin, fin, can_break, nofail);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // ppg.util.Item
    public int sendOutput(Writer o, int lm, int pos) throws IOException {
        o.write(this.s);
        return pos + this.s.length();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void appendString(String s) {
        this.s = new StringBuffer().append(this.s).append(s).toString();
    }
}
