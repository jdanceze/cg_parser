package com.sun.xml.bind.v2.runtime.unmarshaller;

import com.sun.xml.bind.v2.runtime.unmarshaller.XmlVisitor;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.bind.helpers.ValidationEventLocatorImpl;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/unmarshaller/StAXConnector.class */
public abstract class StAXConnector {
    protected final XmlVisitor visitor;
    protected final UnmarshallingContext context;
    protected final XmlVisitor.TextPredictor predictor;
    protected final TagName tagName = new TagNameImpl();

    public abstract void bridge() throws XMLStreamException;

    protected abstract Location getCurrentLocation();

    protected abstract String getCurrentQName();

    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/unmarshaller/StAXConnector$TagNameImpl.class */
    private final class TagNameImpl extends TagName {
        private TagNameImpl() {
        }

        @Override // com.sun.xml.bind.v2.runtime.unmarshaller.TagName
        public String getQname() {
            return StAXConnector.this.getCurrentQName();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public StAXConnector(XmlVisitor visitor) {
        this.visitor = visitor;
        this.context = visitor.getContext();
        this.predictor = visitor.getPredictor();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void handleStartDocument(NamespaceContext nsc) throws SAXException {
        this.visitor.startDocument(new LocatorEx() { // from class: com.sun.xml.bind.v2.runtime.unmarshaller.StAXConnector.1
            @Override // com.sun.xml.bind.v2.runtime.unmarshaller.LocatorEx
            public ValidationEventLocator getLocation() {
                return new ValidationEventLocatorImpl((Locator) this);
            }

            @Override // org.xml.sax.Locator
            public int getColumnNumber() {
                return StAXConnector.this.getCurrentLocation().getColumnNumber();
            }

            @Override // org.xml.sax.Locator
            public int getLineNumber() {
                return StAXConnector.this.getCurrentLocation().getLineNumber();
            }

            @Override // org.xml.sax.Locator
            public String getPublicId() {
                return StAXConnector.this.getCurrentLocation().getPublicId();
            }

            @Override // org.xml.sax.Locator
            public String getSystemId() {
                return StAXConnector.this.getCurrentLocation().getSystemId();
            }
        }, nsc);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void handleEndDocument() throws SAXException {
        this.visitor.endDocument();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static String fixNull(String s) {
        return s == null ? "" : s;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final String getQName(String prefix, String localName) {
        if (prefix == null || prefix.length() == 0) {
            return localName;
        }
        return prefix + ':' + localName;
    }
}
