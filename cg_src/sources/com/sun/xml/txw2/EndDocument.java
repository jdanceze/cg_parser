package com.sun.xml.txw2;
/* loaded from: gencallgraphv3.jar:txw2-2.4.0-b180830.0438.jar:com/sun/xml/txw2/EndDocument.class */
final class EndDocument extends Content {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sun.xml.txw2.Content
    public boolean concludesPendingStartTag() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sun.xml.txw2.Content
    public void accept(ContentVisitor visitor) {
        visitor.onEndDocument();
    }
}
