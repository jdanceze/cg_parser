package com.sun.xml.txw2.output;

import java.io.IOException;
import java.io.Writer;
/* loaded from: gencallgraphv3.jar:txw2-2.4.0-b180830.0438.jar:com/sun/xml/txw2/output/CharacterEscapeHandler.class */
public interface CharacterEscapeHandler {
    void escape(char[] cArr, int i, int i2, boolean z, Writer writer) throws IOException;
}
