package com.sun.xml.bind.v2.runtime;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamWriter;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/StAXPostInitAction.class */
final class StAXPostInitAction implements Runnable {
    private final XMLStreamWriter xsw;
    private final XMLEventWriter xew;
    private final NamespaceContext nsc;
    private final XMLSerializer serializer;

    /* JADX INFO: Access modifiers changed from: package-private */
    public StAXPostInitAction(XMLStreamWriter xsw, XMLSerializer serializer) {
        this.xsw = xsw;
        this.xew = null;
        this.nsc = null;
        this.serializer = serializer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public StAXPostInitAction(XMLEventWriter xew, XMLSerializer serializer) {
        this.xsw = null;
        this.xew = xew;
        this.nsc = null;
        this.serializer = serializer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public StAXPostInitAction(NamespaceContext nsc, XMLSerializer serializer) {
        this.xsw = null;
        this.xew = null;
        this.nsc = nsc;
        this.serializer = serializer;
    }

    @Override // java.lang.Runnable
    public void run() {
        String[] strArr;
        NamespaceContext ns = this.nsc;
        if (this.xsw != null) {
            ns = this.xsw.getNamespaceContext();
        }
        if (this.xew != null) {
            ns = this.xew.getNamespaceContext();
        }
        if (ns == null) {
            return;
        }
        for (String nsUri : this.serializer.grammar.nameList.namespaceURIs) {
            String p = ns.getPrefix(nsUri);
            if (p != null) {
                this.serializer.addInscopeBinding(nsUri, p);
            }
        }
    }
}
