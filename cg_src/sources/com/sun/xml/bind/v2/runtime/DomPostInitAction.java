package com.sun.xml.bind.v2.runtime;

import com.sun.xml.fastinfoset.EncodingConstants;
import java.util.HashSet;
import java.util.Set;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/DomPostInitAction.class */
public final class DomPostInitAction implements Runnable {
    private final Node node;
    private final XMLSerializer serializer;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DomPostInitAction(Node node, XMLSerializer serializer) {
        this.node = node;
        this.serializer = serializer;
    }

    @Override // java.lang.Runnable
    public void run() {
        Set<String> declaredPrefixes = new HashSet<>();
        Node node = this.node;
        while (true) {
            Node n = node;
            if (n != null && n.getNodeType() == 1) {
                NamedNodeMap atts = n.getAttributes();
                if (atts != null) {
                    for (int i = 0; i < atts.getLength(); i++) {
                        Attr a = (Attr) atts.item(i);
                        String nsUri = a.getNamespaceURI();
                        if (nsUri != null && nsUri.equals(EncodingConstants.XMLNS_NAMESPACE_NAME)) {
                            String prefix = a.getLocalName();
                            if (prefix != null) {
                                if (prefix.equals(EncodingConstants.XMLNS_NAMESPACE_PREFIX)) {
                                    prefix = "";
                                }
                                String value = a.getValue();
                                if (value != null && declaredPrefixes.add(prefix)) {
                                    this.serializer.addInscopeBinding(value, prefix);
                                }
                            }
                        }
                    }
                }
                node = n.getParentNode();
            } else {
                return;
            }
        }
    }
}
