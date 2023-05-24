package ppg.util;

import java.io.IOException;
import java.io.Writer;
/* compiled from: CodeWriter.java */
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:ppg/util/Item.class */
abstract class Item {
    Item next = null;

    abstract int formatN(int i, int i2, int i3, int i4, boolean z, boolean z2) throws Overrun;

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract int sendOutput(Writer writer, int i, int i2) throws IOException;

    /* JADX INFO: Access modifiers changed from: package-private */
    public void free() {
        if (this.next != null) {
            this.next.free();
            this.next = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int format(Item it, int lmargin, int pos, int rmargin, int fin, boolean can_break, boolean nofail) throws Overrun {
        if (!nofail && pos > rmargin) {
            throw new Overrun(pos - rmargin);
        }
        if (it == null) {
            if (nofail || pos <= fin) {
                return pos;
            }
            throw new Overrun(pos - fin);
        }
        return it.formatN(lmargin, pos, rmargin, fin, can_break, nofail);
    }
}
