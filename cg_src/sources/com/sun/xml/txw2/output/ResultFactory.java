package com.sun.xml.txw2.output;

import javax.xml.transform.Result;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
/* loaded from: gencallgraphv3.jar:txw2-2.4.0-b180830.0438.jar:com/sun/xml/txw2/output/ResultFactory.class */
public abstract class ResultFactory {
    private ResultFactory() {
    }

    public static XmlSerializer createSerializer(Result result) {
        if (result instanceof SAXResult) {
            return new SaxSerializer((SAXResult) result);
        }
        if (result instanceof DOMResult) {
            return new DomSerializer((DOMResult) result);
        }
        if (result instanceof StreamResult) {
            return new StreamSerializer((StreamResult) result);
        }
        if (result instanceof TXWResult) {
            return new TXWSerializer(((TXWResult) result).getWriter());
        }
        throw new UnsupportedOperationException("Unsupported Result type: " + result.getClass().getName());
    }
}
