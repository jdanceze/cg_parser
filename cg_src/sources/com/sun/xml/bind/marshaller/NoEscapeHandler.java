package com.sun.xml.bind.marshaller;

import java.io.IOException;
import java.io.Writer;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/marshaller/NoEscapeHandler.class */
public class NoEscapeHandler implements CharacterEscapeHandler {
    public static final NoEscapeHandler theInstance = new NoEscapeHandler();

    @Override // com.sun.xml.bind.marshaller.CharacterEscapeHandler
    public void escape(char[] ch, int start, int length, boolean isAttVal, Writer out) throws IOException {
        out.write(ch, start, length);
    }
}
