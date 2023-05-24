package com.sun.xml.txw2;
/* loaded from: gencallgraphv3.jar:txw2-2.4.0-b180830.0438.jar:com/sun/xml/txw2/Text.class */
abstract class Text extends Content {
    protected final StringBuilder buffer = new StringBuilder();

    /* JADX INFO: Access modifiers changed from: protected */
    public Text(Document document, NamespaceResolver nsResolver, Object obj) {
        document.writeValue(obj, nsResolver, this.buffer);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sun.xml.txw2.Content
    public boolean concludesPendingStartTag() {
        return false;
    }
}
