package com.google.common.escape;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.HashMap;
import java.util.Map;
@Beta
@GwtCompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/escape/CharEscaperBuilder.class */
public final class CharEscaperBuilder {
    private int max = -1;
    private final Map<Character, String> map = new HashMap();

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/escape/CharEscaperBuilder$CharArrayDecorator.class */
    private static class CharArrayDecorator extends CharEscaper {
        private final char[][] replacements;
        private final int replaceLength;

        CharArrayDecorator(char[][] replacements) {
            this.replacements = replacements;
            this.replaceLength = replacements.length;
        }

        @Override // com.google.common.escape.CharEscaper, com.google.common.escape.Escaper
        public String escape(String s) {
            int slen = s.length();
            for (int index = 0; index < slen; index++) {
                char c = s.charAt(index);
                if (c < this.replacements.length && this.replacements[c] != null) {
                    return escapeSlow(s, index);
                }
            }
            return s;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.escape.CharEscaper
        public char[] escape(char c) {
            if (c < this.replaceLength) {
                return this.replacements[c];
            }
            return null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @CanIgnoreReturnValue
    public CharEscaperBuilder addEscape(char c, String r) {
        this.map.put(Character.valueOf(c), Preconditions.checkNotNull(r));
        if (c > this.max) {
            this.max = c;
        }
        return this;
    }

    @CanIgnoreReturnValue
    public CharEscaperBuilder addEscapes(char[] cs, String r) {
        Preconditions.checkNotNull(r);
        for (char c : cs) {
            addEscape(c, r);
        }
        return this;
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [char[], char[][]] */
    public char[][] toArray() {
        ?? r0 = new char[this.max + 1];
        for (Map.Entry<Character, String> entry : this.map.entrySet()) {
            r0[entry.getKey().charValue()] = entry.getValue().toCharArray();
        }
        return r0;
    }

    public Escaper toEscaper() {
        return new CharArrayDecorator(toArray());
    }
}
