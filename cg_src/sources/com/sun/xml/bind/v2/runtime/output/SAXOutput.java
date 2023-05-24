package com.sun.xml.bind.v2.runtime.output;

import com.sun.xml.bind.util.AttributesImpl;
import com.sun.xml.bind.v2.runtime.XMLSerializer;
import com.sun.xml.bind.v2.runtime.output.NamespaceContextImpl;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.LocatorImpl;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/output/SAXOutput.class */
public class SAXOutput extends XmlOutputAbstractImpl {
    protected final ContentHandler out;
    private String elementNsUri;
    private String elementLocalName;
    private String elementQName;
    private char[] buf = new char[256];
    private final AttributesImpl atts = new AttributesImpl();

    public SAXOutput(ContentHandler out) {
        this.out = out;
        out.setDocumentLocator(new LocatorImpl());
    }

    @Override // com.sun.xml.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.bind.v2.runtime.output.XmlOutput
    public void startDocument(XMLSerializer serializer, boolean fragment, int[] nsUriIndex2prefixIndex, NamespaceContextImpl nsContext) throws SAXException, IOException, XMLStreamException {
        super.startDocument(serializer, fragment, nsUriIndex2prefixIndex, nsContext);
        if (!fragment) {
            this.out.startDocument();
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.bind.v2.runtime.output.XmlOutput
    public void endDocument(boolean fragment) throws SAXException, IOException, XMLStreamException {
        if (!fragment) {
            this.out.endDocument();
        }
        super.endDocument(fragment);
    }

    @Override // com.sun.xml.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.bind.v2.runtime.output.XmlOutput
    public void beginStartTag(int prefix, String localName) {
        this.elementNsUri = this.nsContext.getNamespaceURI(prefix);
        this.elementLocalName = localName;
        this.elementQName = getQName(prefix, localName);
        this.atts.clear();
    }

    @Override // com.sun.xml.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.bind.v2.runtime.output.XmlOutput
    public void attribute(int prefix, String localName, String value) {
        String nsUri;
        String qname;
        if (prefix == -1) {
            nsUri = "";
            qname = localName;
        } else {
            nsUri = this.nsContext.getNamespaceURI(prefix);
            String p = this.nsContext.getPrefix(prefix);
            if (p.length() == 0) {
                qname = localName;
            } else {
                qname = p + ':' + localName;
            }
        }
        this.atts.addAttribute(nsUri, localName, qname, "CDATA", value);
    }

    @Override // com.sun.xml.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.bind.v2.runtime.output.XmlOutput
    public void endStartTag() throws SAXException {
        NamespaceContextImpl.Element ns = this.nsContext.getCurrent();
        if (ns != null) {
            int sz = ns.count();
            for (int i = 0; i < sz; i++) {
                String p = ns.getPrefix(i);
                String uri = ns.getNsUri(i);
                if (uri.length() != 0 || ns.getBase() != 1) {
                    this.out.startPrefixMapping(p, uri);
                }
            }
        }
        this.out.startElement(this.elementNsUri, this.elementLocalName, this.elementQName, this.atts);
    }

    @Override // com.sun.xml.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.bind.v2.runtime.output.XmlOutput
    public void endTag(int prefix, String localName) throws SAXException {
        this.out.endElement(this.nsContext.getNamespaceURI(prefix), localName, getQName(prefix, localName));
        NamespaceContextImpl.Element ns = this.nsContext.getCurrent();
        if (ns != null) {
            int sz = ns.count();
            for (int i = sz - 1; i >= 0; i--) {
                String p = ns.getPrefix(i);
                String uri = ns.getNsUri(i);
                if (uri.length() != 0 || ns.getBase() != 1) {
                    this.out.endPrefixMapping(p);
                }
            }
        }
    }

    private String getQName(int prefix, String localName) {
        String qname;
        String p = this.nsContext.getPrefix(prefix);
        if (p.length() == 0) {
            qname = localName;
        } else {
            qname = p + ':' + localName;
        }
        return qname;
    }

    @Override // com.sun.xml.bind.v2.runtime.output.XmlOutput
    public void text(String value, boolean needsSP) throws IOException, SAXException, XMLStreamException {
        int vlen = value.length();
        if (this.buf.length <= vlen) {
            this.buf = new char[Math.max(this.buf.length * 2, vlen + 1)];
        }
        if (needsSP) {
            value.getChars(0, vlen, this.buf, 1);
            this.buf[0] = ' ';
        } else {
            value.getChars(0, vlen, this.buf, 0);
        }
        this.out.characters(this.buf, 0, vlen + (needsSP ? 1 : 0));
    }

    @Override // com.sun.xml.bind.v2.runtime.output.XmlOutput
    public void text(Pcdata value, boolean needsSP) throws IOException, SAXException, XMLStreamException {
        int vlen = value.length();
        if (this.buf.length <= vlen) {
            this.buf = new char[Math.max(this.buf.length * 2, vlen + 1)];
        }
        if (needsSP) {
            value.writeTo(this.buf, 1);
            this.buf[0] = ' ';
        } else {
            value.writeTo(this.buf, 0);
        }
        this.out.characters(this.buf, 0, vlen + (needsSP ? 1 : 0));
    }
}
