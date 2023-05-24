package javax.xml.bind.annotation;

import javax.xml.bind.ValidationEventHandler;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
/* loaded from: gencallgraphv3.jar:jaxb-api-2.4.0-b180725.0427.jar:javax/xml/bind/annotation/W3CDomHandler.class */
public class W3CDomHandler implements DomHandler<Element, DOMResult> {
    private DocumentBuilder builder;

    public W3CDomHandler() {
        this.builder = null;
    }

    public W3CDomHandler(DocumentBuilder builder) {
        if (builder == null) {
            throw new IllegalArgumentException();
        }
        this.builder = builder;
    }

    public DocumentBuilder getBuilder() {
        return this.builder;
    }

    public void setBuilder(DocumentBuilder builder) {
        this.builder = builder;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javax.xml.bind.annotation.DomHandler
    public DOMResult createUnmarshaller(ValidationEventHandler errorHandler) {
        if (this.builder == null) {
            return new DOMResult();
        }
        return new DOMResult(this.builder.newDocument());
    }

    @Override // javax.xml.bind.annotation.DomHandler
    public Element getElement(DOMResult r) {
        Node n = r.getNode();
        if (n instanceof Document) {
            return ((Document) n).getDocumentElement();
        }
        if (n instanceof Element) {
            return (Element) n;
        }
        if (n instanceof DocumentFragment) {
            return (Element) n.getChildNodes().item(0);
        }
        throw new IllegalStateException(n.toString());
    }

    @Override // javax.xml.bind.annotation.DomHandler
    public Source marshal(Element element, ValidationEventHandler errorHandler) {
        return new DOMSource(element);
    }
}
