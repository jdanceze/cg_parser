package com.sun.xml.fastinfoset;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/UnparsedEntity.class */
public class UnparsedEntity extends Notation {
    public final String notationName;

    public UnparsedEntity(String _name, String _systemIdentifier, String _publicIdentifier, String _notationName) {
        super(_name, _systemIdentifier, _publicIdentifier);
        this.notationName = _notationName;
    }
}
