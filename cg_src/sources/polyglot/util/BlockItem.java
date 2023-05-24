package polyglot.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: CodeWriter.java */
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/BlockItem.class */
public class BlockItem extends Item {
    BlockItem parent;
    int indent;
    Map containsBreaks = new HashMap();
    Item last = null;
    Item first = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BlockItem(BlockItem parent_, int indent_) {
        this.parent = parent_;
        this.indent = indent_;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void add(Item it) {
        if (this.first == null) {
            this.first = it;
        } else if ((it instanceof TextItem) && (this.last instanceof TextItem)) {
            TextItem lasts = (TextItem) this.last;
            lasts.appendTextItem((TextItem) it);
            return;
        } else {
            this.last.next = it;
        }
        this.last = it;
    }

    @Override // polyglot.util.Item
    FormatResult formatN(int lmargin, int pos, int rmargin, int fin, MaxLevels m, int minLevel, int minLevelUnified) throws Overrun {
        int childfin = fin;
        if (childfin + getMinPosWidth(this.next, m) > rmargin) {
            childfin = rmargin - getMinPosWidth(this.next, m);
        }
        while (true) {
            FormatResult fr = format(this.first, pos + this.indent, pos, rmargin, childfin, new MaxLevels(m.maxLevelInner, m.maxLevelInner), 0, 0);
            int minLevel2 = Math.max(minLevel, fr.minLevel);
            int minLevelU2 = Math.max(minLevelUnified, fr.minLevel);
            try {
                return format(this.next, lmargin, fr.pos, rmargin, fin, m, minLevel2, minLevelU2);
            } catch (Overrun o) {
                if (o.type == 1) {
                    o.type = 2;
                    throw o;
                }
                childfin -= o.amount;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // polyglot.util.Item
    public int sendOutput(PrintWriter o, int lmargin, int pos, boolean success, Item last) throws IOException {
        int lmargin2 = pos + this.indent;
        for (Item it = this.first; it != null; it = it.next) {
            pos = it.sendOutput(o, lmargin2, pos, success, last);
            if (last != null && it == last) {
                throw new IOException();
            }
        }
        return pos;
    }

    @Override // polyglot.util.Item
    int selfMinWidth(MaxLevels m) {
        return getMinWidth(this.first, new MaxLevels(m.maxLevelInner, m.maxLevelInner));
    }

    @Override // polyglot.util.Item
    int selfMinPosWidth(MaxLevels m) {
        return getMinPosWidth(this.first, new MaxLevels(m.maxLevelInner, m.maxLevelInner));
    }

    @Override // polyglot.util.Item
    int selfMinIndent(MaxLevels m) {
        return getMinIndent(this.first, new MaxLevels(m.maxLevelInner, m.maxLevelInner));
    }

    @Override // polyglot.util.Item
    boolean selfContainsBreaks(MaxLevels m) {
        if (this.containsBreaks.containsKey(m)) {
            return this.containsBreaks.get(m) != null;
        }
        boolean result = containsBreaks(this.first, new MaxLevels(m.maxLevelInner, m.maxLevelInner));
        this.containsBreaks.put(m, result ? m : null);
        return result;
    }

    @Override // polyglot.util.Item
    String selfToString() {
        return this.indent == 0 ? new StringBuffer().append("[").append(this.first).append("]").toString() : new StringBuffer().append("[").append(this.indent).append(this.first).append("]").toString();
    }
}
