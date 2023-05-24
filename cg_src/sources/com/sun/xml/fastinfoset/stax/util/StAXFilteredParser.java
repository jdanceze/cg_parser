package com.sun.xml.fastinfoset.stax.util;

import com.sun.xml.fastinfoset.CommonResourceBundle;
import javax.xml.stream.StreamFilter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/stax/util/StAXFilteredParser.class */
public class StAXFilteredParser extends StAXParserWrapper {
    private StreamFilter _filter;

    public StAXFilteredParser() {
    }

    public StAXFilteredParser(XMLStreamReader reader, StreamFilter filter) {
        super(reader);
        this._filter = filter;
    }

    public void setFilter(StreamFilter filter) {
        this._filter = filter;
    }

    @Override // com.sun.xml.fastinfoset.stax.util.StAXParserWrapper
    public int next() throws XMLStreamException {
        if (hasNext()) {
            return super.next();
        }
        throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.noMoreItems"));
    }

    @Override // com.sun.xml.fastinfoset.stax.util.StAXParserWrapper
    public boolean hasNext() throws XMLStreamException {
        while (super.hasNext()) {
            if (this._filter.accept(getReader())) {
                return true;
            }
            super.next();
        }
        return false;
    }
}
