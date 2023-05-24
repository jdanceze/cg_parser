package com.sun.xml.bind.v2.runtime.unmarshaller;

import com.sun.xml.bind.v2.runtime.Name;
import javax.xml.namespace.QName;
import org.xml.sax.Attributes;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/unmarshaller/TagName.class */
public abstract class TagName {
    public String uri;
    public String local;
    public Attributes atts;

    public abstract String getQname();

    public final boolean matches(String nsUri, String local) {
        return this.uri == nsUri && this.local == local;
    }

    public final boolean matches(Name name) {
        return this.local == name.localName && this.uri == name.nsUri;
    }

    public String toString() {
        return '{' + this.uri + '}' + this.local;
    }

    public String getPrefix() {
        String qname = getQname();
        int idx = qname.indexOf(58);
        return idx < 0 ? "" : qname.substring(0, idx);
    }

    public QName createQName() {
        return new QName(this.uri, this.local, getPrefix());
    }
}
