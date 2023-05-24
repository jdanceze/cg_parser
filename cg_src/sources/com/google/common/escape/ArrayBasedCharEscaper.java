package com.google.common.escape;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Map;
@Beta
@GwtCompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/escape/ArrayBasedCharEscaper.class */
public abstract class ArrayBasedCharEscaper extends CharEscaper {
    private final char[][] replacements;
    private final int replacementsLength;
    private final char safeMin;
    private final char safeMax;

    protected abstract char[] escapeUnsafe(char c);

    /* JADX INFO: Access modifiers changed from: protected */
    public ArrayBasedCharEscaper(Map<Character, String> replacementMap, char safeMin, char safeMax) {
        this(ArrayBasedEscaperMap.create(replacementMap), safeMin, safeMax);
    }

    protected ArrayBasedCharEscaper(ArrayBasedEscaperMap escaperMap, char safeMin, char safeMax) {
        Preconditions.checkNotNull(escaperMap);
        this.replacements = escaperMap.getReplacementArray();
        this.replacementsLength = this.replacements.length;
        if (safeMax < safeMin) {
            safeMax = 0;
            safeMin = 65535;
        }
        this.safeMin = safeMin;
        this.safeMax = safeMax;
    }

    @Override // com.google.common.escape.CharEscaper, com.google.common.escape.Escaper
    public final String escape(String s) {
        Preconditions.checkNotNull(s);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if ((c < this.replacementsLength && this.replacements[c] != null) || c > this.safeMax || c < this.safeMin) {
                return escapeSlow(s, i);
            }
        }
        return s;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.common.escape.CharEscaper
    public final char[] escape(char c) {
        char[] chars;
        if (c < this.replacementsLength && (chars = this.replacements[c]) != null) {
            return chars;
        }
        if (c >= this.safeMin && c <= this.safeMax) {
            return null;
        }
        return escapeUnsafe(c);
    }
}
