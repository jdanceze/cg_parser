package ppg.util;

import java.io.IOException;
import java.io.Writer;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: CodeWriter.java */
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:ppg/util/Block.class */
public class Block extends Item {
    Block parent;
    int indent;
    Item last = null;
    Item first = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Block(Block parent_, int indent_) {
        this.parent = parent_;
        this.indent = indent_;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void add(Item it) {
        if (this.first == null) {
            this.first = it;
        } else if ((it instanceof StringItem) && (this.last instanceof StringItem)) {
            StringItem lasts = (StringItem) this.last;
            lasts.appendString(((StringItem) it).s);
            return;
        } else {
            this.last.next = it;
        }
        this.last = it;
    }

    @Override // ppg.util.Item
    int formatN(int lmargin, int pos, int rmargin, int fin, boolean can_break, boolean nofail) throws Overrun {
        int this_fin = rmargin;
        boolean this_nofail = false;
        boolean this_break = false;
        while (true) {
            try {
                int next_pos = format(this.first, pos + this.indent, pos, rmargin, this_fin, this_break, this_nofail && this_break);
                try {
                    return format(this.next, lmargin, next_pos, rmargin, fin, can_break, nofail);
                } catch (Overrun o) {
                    if (!can_break) {
                        throw o;
                    }
                    if (this.next instanceof AllowBreak) {
                        throw o;
                    }
                    this_break = true;
                    if (next_pos > this_fin) {
                        next_pos = this_fin;
                    }
                    this_fin = next_pos - o.amount;
                }
            } catch (Overrun o2) {
                if (!can_break) {
                    throw o2;
                }
                if (!this_break) {
                    this_break = true;
                } else if (!nofail) {
                    throw o2;
                } else {
                    this_nofail = true;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // ppg.util.Item
    public int sendOutput(Writer o, int lmargin, int pos) throws IOException {
        int lmargin2 = pos + this.indent;
        for (Item it = this.first; it != null; it = it.next) {
            pos = it.sendOutput(o, lmargin2, pos);
        }
        return pos;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // ppg.util.Item
    public void free() {
        super.free();
        this.parent = null;
        if (this.first != null) {
            this.first.free();
        }
        this.last = null;
    }
}
