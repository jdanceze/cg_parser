package polyglot.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: CodeWriter.java */
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/TextItem.class */
public class TextItem extends Item {
    String s;
    int length;

    /* JADX INFO: Access modifiers changed from: package-private */
    public TextItem(String s_, int length_) {
        this.s = s_;
        this.length = length_;
    }

    @Override // polyglot.util.Item
    FormatResult formatN(int lmargin, int pos, int rmargin, int fin, MaxLevels m, int minLevel, int minLevelUnified) throws Overrun {
        return format(this.next, lmargin, pos + this.length, rmargin, fin, m, minLevel, minLevelUnified);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // polyglot.util.Item
    public int sendOutput(PrintWriter o, int lm, int pos, boolean success, Item last) throws IOException {
        o.write(this.s);
        return pos + this.length;
    }

    @Override // polyglot.util.Item
    boolean selfContainsBreaks(MaxLevels m) {
        return false;
    }

    @Override // polyglot.util.Item
    int selfMinIndent(MaxLevels m) {
        return -9999;
    }

    @Override // polyglot.util.Item
    int selfMinWidth(MaxLevels m) {
        return -9999;
    }

    @Override // polyglot.util.Item
    int selfMinPosWidth(MaxLevels m) {
        return this.length;
    }

    @Override // polyglot.util.Item
    String selfToString() {
        StringWriter sw = new StringWriter();
        for (int i = 0; i < this.s.length(); i++) {
            char c = this.s.charAt(i);
            if (c == ' ') {
                sw.write("\\ ");
            } else {
                sw.write(c);
            }
        }
        return sw.toString();
    }

    public void appendTextItem(TextItem item) {
        this.s = new StringBuffer().append(this.s).append(item.s).toString();
        this.length += item.length;
    }
}
