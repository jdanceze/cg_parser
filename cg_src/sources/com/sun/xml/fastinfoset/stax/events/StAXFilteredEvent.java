package com.sun.xml.fastinfoset.stax.events;

import com.sun.xml.fastinfoset.CommonResourceBundle;
import javax.xml.stream.EventFilter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/stax/events/StAXFilteredEvent.class */
public class StAXFilteredEvent implements XMLEventReader {
    private XMLEventReader eventReader;
    private EventFilter _filter;

    public StAXFilteredEvent() {
    }

    public StAXFilteredEvent(XMLEventReader reader, EventFilter filter) throws XMLStreamException {
        this.eventReader = reader;
        this._filter = filter;
    }

    public void setEventReader(XMLEventReader reader) {
        this.eventReader = reader;
    }

    public void setFilter(EventFilter filter) {
        this._filter = filter;
    }

    public Object next() {
        try {
            return nextEvent();
        } catch (XMLStreamException e) {
            return null;
        }
    }

    public XMLEvent nextEvent() throws XMLStreamException {
        if (hasNext()) {
            return this.eventReader.nextEvent();
        }
        return null;
    }

    public String getElementText() throws XMLStreamException {
        StringBuffer buffer = new StringBuffer();
        XMLEvent e = nextEvent();
        if (!e.isStartElement()) {
            throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.mustBeOnSTART_ELEMENT"));
        }
        while (hasNext()) {
            Characters nextEvent = nextEvent();
            if (nextEvent.isStartElement()) {
                throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.getElementTextExpectTextOnly"));
            }
            if (nextEvent.isCharacters()) {
                buffer.append(nextEvent.getData());
            }
            if (nextEvent.isEndElement()) {
                return buffer.toString();
            }
        }
        throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.END_ELEMENTnotFound"));
    }

    /* JADX WARN: Removed duplicated region for block: B:4:0x0007  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public javax.xml.stream.events.XMLEvent nextTag() throws javax.xml.stream.XMLStreamException {
        /*
            r5 = this;
        L0:
            r0 = r5
            boolean r0 = r0.hasNext()
            if (r0 == 0) goto L23
            r0 = r5
            javax.xml.stream.events.XMLEvent r0 = r0.nextEvent()
            r6 = r0
            r0 = r6
            boolean r0 = r0.isStartElement()
            if (r0 != 0) goto L1e
            r0 = r6
            boolean r0 = r0.isEndElement()
            if (r0 == 0) goto L20
        L1e:
            r0 = r6
            return r0
        L20:
            goto L0
        L23:
            javax.xml.stream.XMLStreamException r0 = new javax.xml.stream.XMLStreamException
            r1 = r0
            com.sun.xml.fastinfoset.CommonResourceBundle r2 = com.sun.xml.fastinfoset.CommonResourceBundle.getInstance()
            java.lang.String r3 = "message.startOrEndNotFound"
            java.lang.String r2 = r2.getString(r3)
            r1.<init>(r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.xml.fastinfoset.stax.events.StAXFilteredEvent.nextTag():javax.xml.stream.events.XMLEvent");
    }

    public boolean hasNext() {
        while (this.eventReader.hasNext()) {
            try {
                if (this._filter.accept(this.eventReader.peek())) {
                    return true;
                }
                this.eventReader.nextEvent();
            } catch (XMLStreamException e) {
                return false;
            }
        }
        return false;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public XMLEvent peek() throws XMLStreamException {
        if (hasNext()) {
            return this.eventReader.peek();
        }
        return null;
    }

    public void close() throws XMLStreamException {
        this.eventReader.close();
    }

    public Object getProperty(String name) {
        return this.eventReader.getProperty(name);
    }
}
