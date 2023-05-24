package com.sun.xml.bind.v2.runtime.output;

import com.sun.xml.bind.marshaller.CharacterEscapeHandler;
import com.sun.xml.bind.v2.runtime.Name;
import java.io.IOException;
import java.io.OutputStream;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/output/IndentingUTF8XmlOutput.class */
public final class IndentingUTF8XmlOutput extends UTF8XmlOutput {
    private final Encoded indent8;
    private final int unitLen;
    private int depth;
    private boolean seenText;

    public IndentingUTF8XmlOutput(OutputStream out, String indentStr, Encoded[] localNames, CharacterEscapeHandler escapeHandler) {
        super(out, localNames, escapeHandler);
        this.depth = 0;
        this.seenText = false;
        if (indentStr != null) {
            Encoded e = new Encoded(indentStr);
            this.indent8 = new Encoded();
            this.indent8.ensureSize(e.len * 8);
            this.unitLen = e.len;
            for (int i = 0; i < 8; i++) {
                System.arraycopy(e.buf, 0, this.indent8.buf, this.unitLen * i, this.unitLen);
            }
            return;
        }
        this.indent8 = null;
        this.unitLen = 0;
    }

    @Override // com.sun.xml.bind.v2.runtime.output.UTF8XmlOutput, com.sun.xml.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.bind.v2.runtime.output.XmlOutput
    public void beginStartTag(int prefix, String localName) throws IOException {
        indentStartTag();
        super.beginStartTag(prefix, localName);
    }

    @Override // com.sun.xml.bind.v2.runtime.output.UTF8XmlOutput, com.sun.xml.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.bind.v2.runtime.output.XmlOutput
    public void beginStartTag(Name name) throws IOException {
        indentStartTag();
        super.beginStartTag(name);
    }

    private void indentStartTag() throws IOException {
        closeStartTag();
        if (!this.seenText) {
            printIndent();
        }
        this.depth++;
        this.seenText = false;
    }

    @Override // com.sun.xml.bind.v2.runtime.output.UTF8XmlOutput, com.sun.xml.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.bind.v2.runtime.output.XmlOutput
    public void endTag(Name name) throws IOException {
        indentEndTag();
        super.endTag(name);
    }

    @Override // com.sun.xml.bind.v2.runtime.output.UTF8XmlOutput, com.sun.xml.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.bind.v2.runtime.output.XmlOutput
    public void endTag(int prefix, String localName) throws IOException {
        indentEndTag();
        super.endTag(prefix, localName);
    }

    private void indentEndTag() throws IOException {
        this.depth--;
        if (!this.closeStartTagPending && !this.seenText) {
            printIndent();
        }
        this.seenText = false;
    }

    private void printIndent() throws IOException {
        write(10);
        int i = this.depth % 8;
        write(this.indent8.buf, 0, i * this.unitLen);
        for (int i2 = i >> 3; i2 > 0; i2--) {
            this.indent8.write(this);
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.output.UTF8XmlOutput, com.sun.xml.bind.v2.runtime.output.XmlOutput
    public void text(String value, boolean needSP) throws IOException {
        this.seenText = true;
        super.text(value, needSP);
    }

    @Override // com.sun.xml.bind.v2.runtime.output.UTF8XmlOutput, com.sun.xml.bind.v2.runtime.output.XmlOutput
    public void text(Pcdata value, boolean needSP) throws IOException {
        this.seenText = true;
        super.text(value, needSP);
    }

    @Override // com.sun.xml.bind.v2.runtime.output.UTF8XmlOutput, com.sun.xml.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.bind.v2.runtime.output.XmlOutput
    public void endDocument(boolean fragment) throws IOException, SAXException, XMLStreamException {
        write(10);
        super.endDocument(fragment);
    }
}
