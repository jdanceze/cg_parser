package com.google.common.xml;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.escape.Escaper;
import com.google.common.escape.Escapers;
@Beta
@GwtCompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/xml/XmlEscapers.class */
public class XmlEscapers {
    private static final char MIN_ASCII_CONTROL_CHAR = 0;
    private static final char MAX_ASCII_CONTROL_CHAR = 31;
    private static final Escaper XML_ESCAPER;
    private static final Escaper XML_CONTENT_ESCAPER;
    private static final Escaper XML_ATTRIBUTE_ESCAPER;

    private XmlEscapers() {
    }

    public static Escaper xmlContentEscaper() {
        return XML_CONTENT_ESCAPER;
    }

    public static Escaper xmlAttributeEscaper() {
        return XML_ATTRIBUTE_ESCAPER;
    }

    static {
        Escapers.Builder builder = Escapers.builder();
        builder.setSafeRange((char) 0, (char) 65533);
        builder.setUnsafeReplacement("�");
        char c = 0;
        while (true) {
            char c2 = c;
            if (c2 <= 31) {
                if (c2 != '\t' && c2 != '\n' && c2 != '\r') {
                    builder.addEscape(c2, "�");
                }
                c = (char) (c2 + 1);
            } else {
                builder.addEscape('&', "&amp;");
                builder.addEscape('<', "&lt;");
                builder.addEscape('>', "&gt;");
                XML_CONTENT_ESCAPER = builder.build();
                builder.addEscape('\'', "&apos;");
                builder.addEscape('\"', "&quot;");
                XML_ESCAPER = builder.build();
                builder.addEscape('\t', "&#x9;");
                builder.addEscape('\n', "&#xA;");
                builder.addEscape('\r', "&#xD;");
                XML_ATTRIBUTE_ESCAPER = builder.build();
                return;
            }
        }
    }
}
