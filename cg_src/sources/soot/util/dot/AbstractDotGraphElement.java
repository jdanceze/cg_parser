package soot.util.dot;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
/* loaded from: gencallgraphv3.jar:soot/util/dot/AbstractDotGraphElement.class */
public abstract class AbstractDotGraphElement {
    private LinkedHashMap<String, DotGraphAttribute> attributes;

    public void setAttribute(String id, String value) {
        setAttribute(new DotGraphAttribute(id, value));
    }

    public void setAttribute(DotGraphAttribute attr) {
        LinkedHashMap<String, DotGraphAttribute> attrs = this.attributes;
        if (attrs == null) {
            LinkedHashMap<String, DotGraphAttribute> linkedHashMap = new LinkedHashMap<>();
            attrs = linkedHashMap;
            this.attributes = linkedHashMap;
        }
        attrs.put(attr.getID(), attr);
    }

    public Collection<DotGraphAttribute> getAttributes() {
        LinkedHashMap<String, DotGraphAttribute> attrs = this.attributes;
        return attrs == null ? Collections.emptyList() : Collections.unmodifiableCollection(attrs.values());
    }

    public DotGraphAttribute getAttribute(String id) {
        LinkedHashMap<String, DotGraphAttribute> attrs = this.attributes;
        if (attrs == null) {
            return null;
        }
        return attrs.get(id);
    }

    public String getAttributeValue(String id) {
        DotGraphAttribute attr = getAttribute(id);
        if (attr == null) {
            return null;
        }
        return attr.getValue();
    }

    public void removeAttribute(String id) {
        LinkedHashMap<String, DotGraphAttribute> attrs = this.attributes;
        if (attrs != null) {
            attrs.remove(id);
        }
    }

    public void removeAttribute(DotGraphAttribute attr) {
        LinkedHashMap<String, DotGraphAttribute> attrs = this.attributes;
        if (attrs != null) {
            attrs.remove(attr.getID(), attr);
        }
    }

    public void removeAllAttributes() {
        this.attributes = null;
    }

    public void setLabel(String label) {
        setAttribute("label", "\"" + DotGraphUtility.replaceReturns(DotGraphUtility.replaceQuotes(label)) + "\"");
    }

    public String getLabel() {
        return getAttributeValue("label");
    }
}
