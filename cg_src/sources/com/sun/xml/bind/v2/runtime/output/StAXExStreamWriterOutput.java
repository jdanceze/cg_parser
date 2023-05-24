package com.sun.xml.bind.v2.runtime.output;

import com.sun.xml.bind.marshaller.NoEscapeHandler;
import com.sun.xml.bind.v2.runtime.unmarshaller.Base64Data;
import javax.xml.stream.XMLStreamException;
import org.jvnet.staxex.XMLStreamWriterEx;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/output/StAXExStreamWriterOutput.class */
public final class StAXExStreamWriterOutput extends XMLStreamWriterOutput {
    private final XMLStreamWriterEx out;

    public StAXExStreamWriterOutput(XMLStreamWriterEx out) {
        super(out, NoEscapeHandler.theInstance);
        this.out = out;
    }

    @Override // com.sun.xml.bind.v2.runtime.output.XMLStreamWriterOutput, com.sun.xml.bind.v2.runtime.output.XmlOutput
    public void text(Pcdata value, boolean needsSeparatingWhitespace) throws XMLStreamException {
        if (needsSeparatingWhitespace) {
            this.out.writeCharacters(Instruction.argsep);
        }
        if (!(value instanceof Base64Data)) {
            this.out.writeCharacters(value.toString());
            return;
        }
        Base64Data v = (Base64Data) value;
        this.out.writeBinary(v.getDataHandler());
    }
}
