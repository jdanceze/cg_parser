package com.sun.xml.txw2.output;

import com.sun.xml.txw2.TypedXmlWriter;
import javax.xml.transform.Result;
/* loaded from: gencallgraphv3.jar:txw2-2.4.0-b180830.0438.jar:com/sun/xml/txw2/output/TXWResult.class */
public class TXWResult implements Result {
    private String systemId;
    private TypedXmlWriter writer;

    public TXWResult(TypedXmlWriter writer) {
        this.writer = writer;
    }

    public TypedXmlWriter getWriter() {
        return this.writer;
    }

    public void setWriter(TypedXmlWriter writer) {
        this.writer = writer;
    }

    @Override // javax.xml.transform.Result
    public String getSystemId() {
        return this.systemId;
    }

    @Override // javax.xml.transform.Result
    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }
}
