package com.sun.xml.bind.v2.schemagen;

import com.sun.xml.bind.v2.schemagen.xmlschema.LocalAttribute;
import com.sun.xml.bind.v2.schemagen.xmlschema.LocalElement;
import com.sun.xml.bind.v2.schemagen.xmlschema.Schema;
import com.sun.xml.txw2.TypedXmlWriter;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.namespace.QName;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/schemagen/Form.class */
public enum Form {
    QUALIFIED(XmlNsForm.QUALIFIED, true) { // from class: com.sun.xml.bind.v2.schemagen.Form.1
        @Override // com.sun.xml.bind.v2.schemagen.Form
        void declare(String attName, Schema schema) {
            schema._attribute(attName, "qualified");
        }
    },
    UNQUALIFIED(XmlNsForm.UNQUALIFIED, false) { // from class: com.sun.xml.bind.v2.schemagen.Form.2
        @Override // com.sun.xml.bind.v2.schemagen.Form
        void declare(String attName, Schema schema) {
            schema._attribute(attName, "unqualified");
        }
    },
    UNSET(XmlNsForm.UNSET, false) { // from class: com.sun.xml.bind.v2.schemagen.Form.3
        @Override // com.sun.xml.bind.v2.schemagen.Form
        void declare(String attName, Schema schema) {
        }
    };
    
    private final XmlNsForm xnf;
    public final boolean isEffectivelyQualified;

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void declare(String str, Schema schema);

    Form(XmlNsForm xnf, boolean effectivelyQualified) {
        this.xnf = xnf;
        this.isEffectivelyQualified = effectivelyQualified;
    }

    public void writeForm(LocalElement e, QName tagName) {
        _writeForm(e, tagName);
    }

    public void writeForm(LocalAttribute a, QName tagName) {
        _writeForm(a, tagName);
    }

    private void _writeForm(TypedXmlWriter e, QName tagName) {
        boolean qualified = tagName.getNamespaceURI().length() > 0;
        if (qualified && this != QUALIFIED) {
            e._attribute("form", "qualified");
        } else if (!qualified && this == QUALIFIED) {
            e._attribute("form", "unqualified");
        }
    }

    public static Form get(XmlNsForm xnf) {
        Form[] values;
        for (Form v : values()) {
            if (v.xnf == xnf) {
                return v;
            }
        }
        throw new IllegalArgumentException();
    }
}
