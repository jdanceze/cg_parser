package com.sun.xml.txw2.output;

import java.io.IOException;
import java.io.Writer;
/* loaded from: gencallgraphv3.jar:txw2-2.4.0-b180830.0438.jar:com/sun/xml/txw2/output/DumbEscapeHandler.class */
public class DumbEscapeHandler implements CharacterEscapeHandler {
    public static final CharacterEscapeHandler theInstance = new DumbEscapeHandler();

    private DumbEscapeHandler() {
    }

    @Override // com.sun.xml.txw2.output.CharacterEscapeHandler
    public void escape(char[] ch, int start, int length, boolean isAttVal, Writer out) throws IOException {
        int limit = start + length;
        for (int i = start; i < limit; i++) {
            switch (ch[i]) {
                case '\"':
                    if (isAttVal) {
                        out.write("&quot;");
                        break;
                    } else {
                        out.write(34);
                        break;
                    }
                case '&':
                    out.write("&amp;");
                    break;
                case '<':
                    out.write("&lt;");
                    break;
                case '>':
                    out.write("&gt;");
                    break;
                default:
                    if (ch[i] > 127) {
                        out.write("&#");
                        out.write(Integer.toString(ch[i]));
                        out.write(59);
                        break;
                    } else {
                        out.write(ch[i]);
                        break;
                    }
            }
        }
    }
}
