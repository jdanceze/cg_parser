package com.sun.xml.txw2;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:txw2-2.4.0-b180830.0438.jar:com/sun/xml/txw2/Content.class */
public abstract class Content {
    private Content next;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean concludesPendingStartTag();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void accept(ContentVisitor contentVisitor);

    static {
        $assertionsDisabled = !Content.class.desiredAssertionStatus();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Content getNext() {
        return this.next;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void setNext(Document doc, Content next) {
        if (!$assertionsDisabled && next == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && this.next != null) {
            throw new AssertionError("next of " + this + " is already set to " + this.next);
        }
        this.next = next;
        doc.run();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isReadyToCommit() {
        return true;
    }

    public void written() {
    }
}
