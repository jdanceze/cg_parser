package org.jvnet.staxex.util;

import com.sun.xml.fastinfoset.EncodingConstants;
import java.io.IOException;
import javax.xml.bind.attachment.AttachmentMarshaller;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.jvnet.staxex.Base64Data;
import org.jvnet.staxex.XMLStreamReaderEx;
import org.jvnet.staxex.XMLStreamWriterEx;
/* loaded from: gencallgraphv3.jar:stax-ex-1.8.jar:org/jvnet/staxex/util/XMLStreamReaderToXMLStreamWriter.class */
public class XMLStreamReaderToXMLStreamWriter {
    private static final int BUF_SIZE = 4096;
    protected XMLStreamReader in;
    protected XMLStreamWriter out;
    private char[] buf;
    boolean optimizeBase64Data = false;
    AttachmentMarshaller mtomAttachmentMarshaller;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !XMLStreamReaderToXMLStreamWriter.class.desiredAssertionStatus();
    }

    /* loaded from: gencallgraphv3.jar:stax-ex-1.8.jar:org/jvnet/staxex/util/XMLStreamReaderToXMLStreamWriter$Breakpoint.class */
    public static class Breakpoint {
        protected XMLStreamReader reader;
        protected XMLStreamWriter writer;

        public Breakpoint(XMLStreamReader r, XMLStreamWriter w) {
            this.reader = r;
            this.writer = w;
        }

        public XMLStreamReader reader() {
            return this.reader;
        }

        public XMLStreamWriter writer() {
            return this.writer;
        }

        public boolean proceedBeforeStartElement() {
            return true;
        }

        public boolean proceedAfterStartElement() {
            return true;
        }
    }

    public void bridge(XMLStreamReader in, XMLStreamWriter out) throws XMLStreamException {
        bridge(in, out, null);
    }

    public void bridge(Breakpoint breakPoint) throws XMLStreamException {
        bridge(breakPoint.reader(), breakPoint.writer(), breakPoint);
    }

    private void bridge(XMLStreamReader in, XMLStreamWriter out, Breakpoint breakPoint) throws XMLStreamException {
        if (!$assertionsDisabled && (in == null || out == null)) {
            throw new AssertionError();
        }
        this.in = in;
        this.out = out;
        this.optimizeBase64Data = in instanceof XMLStreamReaderEx;
        if ((out instanceof XMLStreamWriterEx) && (out instanceof MtomStreamWriter)) {
            this.mtomAttachmentMarshaller = ((MtomStreamWriter) out).getAttachmentMarshaller();
        }
        int depth = 0;
        this.buf = new char[4096];
        int event = getEventType();
        if (event != 1) {
            throw new IllegalStateException("The current event is not START_ELEMENT\n but " + event);
        }
        do {
            switch (event) {
                case 1:
                    if (breakPoint != null && !breakPoint.proceedBeforeStartElement()) {
                        return;
                    }
                    depth++;
                    handleStartElement();
                    if (breakPoint != null && !breakPoint.proceedAfterStartElement()) {
                        return;
                    }
                    break;
                case 2:
                    handleEndElement();
                    depth--;
                    if (depth == 0) {
                        return;
                    }
                    break;
                case 3:
                    handlePI();
                    break;
                case 4:
                    handleCharacters();
                    break;
                case 5:
                    handleComment();
                    break;
                case 6:
                    handleSpace();
                    break;
                case 7:
                case 10:
                default:
                    throw new XMLStreamException("Cannot process event: " + event);
                case 8:
                    throw new XMLStreamException("Malformed XML at depth=" + depth + ", Reached EOF. Event=" + event);
                case 9:
                    handleEntityReference();
                    break;
                case 11:
                    handleDTD();
                    break;
                case 12:
                    handleCDATA();
                    break;
            }
            event = getNextEvent();
        } while (depth != 0);
    }

    protected void handlePI() throws XMLStreamException {
        this.out.writeProcessingInstruction(this.in.getPITarget(), this.in.getPIData());
    }

    protected void handleCharacters() throws XMLStreamException {
        CharSequence c = null;
        if (this.optimizeBase64Data) {
            c = ((XMLStreamReaderEx) this.in).getPCDATA();
        }
        if (c != null && (c instanceof Base64Data)) {
            if (this.mtomAttachmentMarshaller != null) {
                Base64Data b64d = (Base64Data) c;
                ((XMLStreamWriterEx) this.out).writeBinary(b64d.getDataHandler());
                return;
            }
            try {
                ((Base64Data) c).writeTo(this.out);
                return;
            } catch (IOException e) {
                throw new XMLStreamException(e);
            }
        }
        int start = 0;
        int read = this.buf.length;
        while (read == this.buf.length) {
            read = this.in.getTextCharacters(start, this.buf, 0, this.buf.length);
            this.out.writeCharacters(this.buf, 0, read);
            start += this.buf.length;
        }
    }

    protected void handleEndElement() throws XMLStreamException {
        this.out.writeEndElement();
    }

    protected void handleStartElement() throws XMLStreamException {
        String nsUri = this.in.getNamespaceURI();
        if (nsUri == null) {
            this.out.writeStartElement(this.in.getLocalName());
        } else {
            this.out.writeStartElement(fixNull(this.in.getPrefix()), this.in.getLocalName(), nsUri);
        }
        int nsCount = this.in.getNamespaceCount();
        for (int i = 0; i < nsCount; i++) {
            this.out.writeNamespace(fixNull(this.in.getNamespacePrefix(i)), fixNull(this.in.getNamespaceURI(i)));
        }
        int attCount = this.in.getAttributeCount();
        for (int i2 = 0; i2 < attCount; i2++) {
            handleAttribute(i2);
        }
    }

    protected void handleAttribute(int i) throws XMLStreamException {
        String nsUri = this.in.getAttributeNamespace(i);
        String prefix = this.in.getAttributePrefix(i);
        if (fixNull(nsUri).equals(EncodingConstants.XMLNS_NAMESPACE_NAME)) {
            return;
        }
        if (nsUri == null || prefix == null || prefix.equals("")) {
            this.out.writeAttribute(this.in.getAttributeLocalName(i), this.in.getAttributeValue(i));
        } else {
            this.out.writeAttribute(prefix, nsUri, this.in.getAttributeLocalName(i), this.in.getAttributeValue(i));
        }
    }

    protected void handleDTD() throws XMLStreamException {
        this.out.writeDTD(this.in.getText());
    }

    protected void handleComment() throws XMLStreamException {
        this.out.writeComment(this.in.getText());
    }

    protected void handleEntityReference() throws XMLStreamException {
        this.out.writeEntityRef(this.in.getText());
    }

    protected void handleSpace() throws XMLStreamException {
        handleCharacters();
    }

    protected void handleCDATA() throws XMLStreamException {
        this.out.writeCData(this.in.getText());
    }

    private static String fixNull(String s) {
        return s == null ? "" : s;
    }

    private int getEventType() throws XMLStreamException {
        int event = this.in.getEventType();
        if (event == 7) {
            while (!this.in.isStartElement()) {
                event = this.in.next();
                if (event == 5) {
                    handleComment();
                }
            }
        }
        return event;
    }

    private int getNextEvent() throws XMLStreamException {
        this.in.next();
        return getEventType();
    }
}
