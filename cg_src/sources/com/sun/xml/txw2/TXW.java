package com.sun.xml.txw2;

import com.sun.xml.txw2.annotation.XmlElement;
import com.sun.xml.txw2.annotation.XmlNamespace;
import com.sun.xml.txw2.output.TXWSerializer;
import com.sun.xml.txw2.output.XmlSerializer;
import javax.xml.namespace.QName;
/* loaded from: gencallgraphv3.jar:txw2-2.4.0-b180830.0438.jar:com/sun/xml/txw2/TXW.class */
public abstract class TXW {
    private TXW() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static QName getTagName(Class<?> c) {
        Package pkg;
        XmlNamespace xn;
        String localName = "";
        String nsUri = "##default";
        XmlElement xe = (XmlElement) c.getAnnotation(XmlElement.class);
        if (xe != null) {
            localName = xe.value();
            nsUri = xe.ns();
        }
        if (localName.length() == 0) {
            String localName2 = c.getName();
            int idx = localName2.lastIndexOf(46);
            if (idx >= 0) {
                localName2 = localName2.substring(idx + 1);
            }
            localName = Character.toLowerCase(localName2.charAt(0)) + localName2.substring(1);
        }
        if (nsUri.equals("##default") && (pkg = c.getPackage()) != null && (xn = (XmlNamespace) pkg.getAnnotation(XmlNamespace.class)) != null) {
            nsUri = xn.value();
        }
        if (nsUri.equals("##default")) {
            nsUri = "";
        }
        return new QName(nsUri, localName);
    }

    public static <T extends TypedXmlWriter> T create(Class<T> rootElement, XmlSerializer out) {
        if (out instanceof TXWSerializer) {
            TXWSerializer txws = (TXWSerializer) out;
            return (T) txws.txw._element(rootElement);
        }
        Document doc = new Document(out);
        QName n = getTagName(rootElement);
        return (T) new ContainerElement(doc, null, n.getNamespaceURI(), n.getLocalPart())._cast(rootElement);
    }

    public static <T extends TypedXmlWriter> T create(QName tagName, Class<T> rootElement, XmlSerializer out) {
        if (out instanceof TXWSerializer) {
            TXWSerializer txws = (TXWSerializer) out;
            return (T) txws.txw._element(tagName, rootElement);
        }
        return (T) new ContainerElement(new Document(out), null, tagName.getNamespaceURI(), tagName.getLocalPart())._cast(rootElement);
    }
}
