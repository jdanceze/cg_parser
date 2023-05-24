package polyglot.util;

import java.io.IOException;
import java.io.PrintWriter;
import soot.coffi.Instruction;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: CodeWriter.java */
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/AllowBreak.class */
public class AllowBreak extends Item {
    final int indent;
    final int level;
    final boolean unified;
    final String alt;
    final int altlen;
    boolean broken = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AllowBreak(int n_, int level_, String alt_, int altlen_, boolean u) {
        this.indent = n_;
        this.alt = alt_;
        this.altlen = altlen_;
        this.level = level_;
        this.unified = u;
    }

    @Override // polyglot.util.Item
    FormatResult formatN(int lmargin, int pos, int rmargin, int fin, MaxLevels m, int minLevel, int minLevelUnified) throws Overrun {
        if (canLeaveUnbroken(minLevel, minLevelUnified)) {
            try {
                this.broken = false;
                return format(this.next, lmargin, pos + this.altlen, rmargin, fin, new MaxLevels(Math.min(this.unified ? this.level - 1 : this.level, m.maxLevel), Math.min(this.level - 1, m.maxLevelInner)), minLevel, minLevelUnified);
            } catch (Overrun o) {
                if (this.level > m.maxLevel) {
                    throw o;
                }
            }
        }
        if (canBreak(m)) {
            this.broken = true;
            try {
                return format(this.next, lmargin, lmargin + this.indent, rmargin, fin, m, Math.max(this.level - 1, minLevel), Math.max(this.level, minLevelUnified));
            } catch (Overrun o2) {
                o2.type = 1;
                throw o2;
            }
        }
        throw new IllegalArgumentException("could not either break or not break");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // polyglot.util.Item
    public int sendOutput(PrintWriter o, int lmargin, int pos, boolean success, Item last) throws IOException {
        if (this.broken || !success) {
            o.println();
            for (int i = 0; i < lmargin + this.indent; i++) {
                o.print(Instruction.argsep);
            }
            return lmargin + this.indent;
        }
        o.print(this.alt);
        return pos + this.altlen;
    }

    boolean canBreak(MaxLevels m) {
        return this.level <= m.maxLevel;
    }

    boolean canLeaveUnbroken(int minLevel, int minLevelUnified) {
        return this.level > minLevelUnified || (!this.unified && this.level > minLevel);
    }

    @Override // polyglot.util.Item
    int selfMinIndent(MaxLevels m) {
        if (canBreak(m)) {
            return this.indent;
        }
        return -9999;
    }

    @Override // polyglot.util.Item
    int selfMinPosWidth(MaxLevels m) {
        if (canBreak(m)) {
            return 0;
        }
        return this.altlen;
    }

    @Override // polyglot.util.Item
    int selfMinWidth(MaxLevels m) {
        if (canBreak(m)) {
            return this.indent;
        }
        return -9999;
    }

    @Override // polyglot.util.Item
    boolean selfContainsBreaks(MaxLevels m) {
        return canBreak(m);
    }

    @Override // polyglot.util.Item
    String selfToString() {
        return this.indent == 0 ? Instruction.argsep : new StringBuffer().append("^").append(this.indent).toString();
    }
}
