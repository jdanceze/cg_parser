package com.sun.xml.txw2;
/* loaded from: gencallgraphv3.jar:txw2-2.4.0-b180830.0438.jar:com/sun/xml/txw2/Pcdata.class */
final class Pcdata extends Text {
    /* JADX INFO: Access modifiers changed from: package-private */
    public Pcdata(Document document, NamespaceResolver nsResolver, Object obj) {
        super(document, nsResolver, obj);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sun.xml.txw2.Content
    public void accept(ContentVisitor visitor) {
        visitor.onPcdata(this.buffer);
    }
}
