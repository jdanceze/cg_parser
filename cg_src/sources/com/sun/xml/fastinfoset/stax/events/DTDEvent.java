package com.sun.xml.fastinfoset.stax.events;

import java.util.List;
import javax.xml.stream.events.DTD;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/stax/events/DTDEvent.class */
public class DTDEvent extends EventBase implements DTD {
    private String _dtd;
    private List _notations;
    private List _entities;

    public DTDEvent() {
        setEventType(11);
    }

    public DTDEvent(String dtd) {
        setEventType(11);
        this._dtd = dtd;
    }

    public String getDocumentTypeDeclaration() {
        return this._dtd;
    }

    public void setDTD(String dtd) {
        this._dtd = dtd;
    }

    public List getEntities() {
        return this._entities;
    }

    public List getNotations() {
        return this._notations;
    }

    public Object getProcessedDTD() {
        return null;
    }

    public void setEntities(List entites) {
        this._entities = entites;
    }

    public void setNotations(List notations) {
        this._notations = notations;
    }

    public String toString() {
        return this._dtd;
    }
}
