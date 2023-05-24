package polyglot.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: CodeWriter.java */
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/Item.class */
public abstract class Item {
    static final int NO_WIDTH = -9999;
    boolean contains_brks;
    Map min_widths = new HashMap();
    Map min_indents = new HashMap();
    Map min_pos_width = new HashMap();
    boolean cb_init = false;
    Item next = null;

    abstract FormatResult formatN(int i, int i2, int i3, int i4, MaxLevels maxLevels, int i5, int i6) throws Overrun;

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract int sendOutput(PrintWriter printWriter, int i, int i2, boolean z, Item item) throws IOException;

    abstract String selfToString();

    abstract int selfMinIndent(MaxLevels maxLevels);

    abstract int selfMinWidth(MaxLevels maxLevels);

    abstract int selfMinPosWidth(MaxLevels maxLevels);

    abstract boolean selfContainsBreaks(MaxLevels maxLevels);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static FormatResult format(Item it, int lmargin, int pos, int rmargin, int fin, MaxLevels m, int minLevel, int minLevelUnified) throws Overrun {
        CodeWriter.format_calls++;
        if (it == null) {
            if (pos > fin) {
                throw Overrun.overrun(pos - fin, 2);
            }
            return new FormatResult(pos, minLevelUnified);
        }
        int amount2 = (lmargin + getMinWidth(it, m)) - rmargin;
        if (amount2 > 0) {
            throw Overrun.overrun(amount2, 1);
        }
        int amount = (pos + getMinPosWidth(it, m)) - rmargin;
        if (amount > 0) {
            throw Overrun.overrun(amount, 0);
        }
        int amount3 = (lmargin + getMinIndent(it, m)) - fin;
        if (amount3 > 0) {
            throw Overrun.overrun(amount3, 2);
        }
        return it.formatN(lmargin, pos, rmargin, fin, m, minLevel, minLevelUnified);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getMinWidth(Item it, MaxLevels m) {
        if (it == null) {
            return NO_WIDTH;
        }
        if (it.min_widths.containsKey(m)) {
            return ((Integer) it.min_widths.get(m)).intValue();
        }
        int p1 = it.selfMinWidth(m);
        int p2 = it.selfMinIndent(m);
        int p3 = p2 != NO_WIDTH ? getMinPosWidth(it.next, m) + p2 : NO_WIDTH;
        int p4 = getMinWidth(it.next, m);
        int result = Math.max(Math.max(p1, p3), p4);
        it.min_widths.put(m, new Integer(result));
        return result;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getMinPosWidth(Item it, MaxLevels m) {
        int result;
        if (it == null) {
            return 0;
        }
        if (it.min_pos_width.containsKey(m)) {
            return ((Integer) it.min_pos_width.get(m)).intValue();
        }
        int p1 = it.selfMinPosWidth(m);
        if (it.next == null || it.selfContainsBreaks(m)) {
            result = p1;
        } else {
            result = p1 + getMinPosWidth(it.next, m);
        }
        it.min_pos_width.put(m, new Integer(result));
        return result;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getMinIndent(Item it, MaxLevels m) {
        int result;
        if (it == null) {
            return NO_WIDTH;
        }
        if (it.min_indents.containsKey(m)) {
            return ((Integer) it.min_indents.get(m)).intValue();
        }
        int p1 = it.selfMinIndent(m);
        if (it.next == null) {
            return p1;
        }
        if (containsBreaks(it.next, m)) {
            result = getMinIndent(it.next, m);
        } else {
            result = getMinPosWidth(it.next, m);
        }
        it.min_indents.put(m, new Integer(result));
        return result;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean containsBreaks(Item it, MaxLevels m) {
        if (it == null) {
            return false;
        }
        if (it.cb_init) {
            return it.contains_brks;
        }
        if (it.selfContainsBreaks(m)) {
            it.contains_brks = true;
            it.cb_init = true;
            return true;
        } else if (it.next == null) {
            return false;
        } else {
            it.contains_brks = containsBreaks(it.next, m);
            it.cb_init = true;
            return it.contains_brks;
        }
    }

    public String summarize(String s) {
        return s.length() <= 79 ? s : new StringBuffer().append(s.substring(0, 76)).append("...").toString();
    }

    public String toString() {
        return this.next == null ? summarize(selfToString()) : summarize(new StringBuffer().append(selfToString()).append(this.next.toString()).toString());
    }
}
