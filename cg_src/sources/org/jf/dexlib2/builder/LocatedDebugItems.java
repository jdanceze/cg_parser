package org.jf.dexlib2.builder;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/builder/LocatedDebugItems.class */
public class LocatedDebugItems extends LocatedItems<BuilderDebugItem> {
    @Override // org.jf.dexlib2.builder.LocatedItems
    protected String getAddLocatedItemError() {
        return "Cannot add a debug item that has already been added to a method.You must remove it from its current location first.";
    }
}
