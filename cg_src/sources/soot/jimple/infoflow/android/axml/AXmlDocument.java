package soot.jimple.infoflow.android.axml;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/axml/AXmlDocument.class */
public class AXmlDocument {
    private AXmlNode rootNode;
    Map<String, AXmlNamespace> namespaces = null;

    public AXmlNode getRootNode() {
        return this.rootNode;
    }

    public void setRootNode(AXmlNode rootNode) {
        this.rootNode = rootNode;
    }

    public void addNamespace(AXmlNamespace ns) {
        if (ns.getUri() == null || ns.getUri().isEmpty()) {
            return;
        }
        if (this.namespaces == null) {
            this.namespaces = new HashMap();
        }
        this.namespaces.put(ns.getPrefix(), ns);
    }

    public Collection<AXmlNamespace> getNamespaces() {
        return this.namespaces == null ? Collections.emptySet() : this.namespaces.values();
    }
}
