package com.sun.xml.bind.marshaller;

import java.io.IOException;
import java.io.Writer;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/marshaller/CharacterEscapeHandler.class */
public interface CharacterEscapeHandler {
    void escape(char[] cArr, int i, int i2, boolean z, Writer writer) throws IOException;
}
