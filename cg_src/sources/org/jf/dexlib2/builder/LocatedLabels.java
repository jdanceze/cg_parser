package org.jf.dexlib2.builder;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/builder/LocatedLabels.class */
public class LocatedLabels extends LocatedItems<Label> {
    @Override // org.jf.dexlib2.builder.LocatedItems
    protected String getAddLocatedItemError() {
        return "Cannot add a label that is already placed.You must remove it from its current location first.";
    }
}
