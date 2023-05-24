package com.sun.xml.bind.marshaller;

import java.io.IOException;
import java.io.Writer;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/marshaller/MinimumEscapeHandler.class */
public class MinimumEscapeHandler implements CharacterEscapeHandler {
    public static final CharacterEscapeHandler theInstance = new MinimumEscapeHandler();

    private MinimumEscapeHandler() {
    }

    @Override // com.sun.xml.bind.marshaller.CharacterEscapeHandler
    public void escape(char[] ch, int start, int length, boolean isAttVal, Writer out) throws IOException {
        int limit = start + length;
        for (int i = start; i < limit; i++) {
            char c = ch[i];
            if (c == '&' || c == '<' || c == '>' || c == '\r' || ((c == '\n' && isAttVal) || (c == '\"' && isAttVal))) {
                if (i != start) {
                    out.write(ch, start, i - start);
                }
                start = i + 1;
                switch (ch[i]) {
                    case '\n':
                    case '\r':
                        out.write("&#");
                        out.write(Integer.toString(c));
                        out.write(59);
                        continue;
                    case '\"':
                        out.write("&quot;");
                        continue;
                    case '&':
                        out.write("&amp;");
                        continue;
                    case '<':
                        out.write("&lt;");
                        continue;
                    case '>':
                        out.write("&gt;");
                        continue;
                    default:
                        throw new IllegalArgumentException("Cannot escape: '" + c + "'");
                }
            }
        }
        if (start != limit) {
            out.write(ch, start, limit - start);
        }
    }
}
