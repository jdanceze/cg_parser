package org.jf.dexlib2.builder;

import org.jf.dexlib2.iface.debug.DebugItem;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/builder/BuilderDebugItem.class */
public abstract class BuilderDebugItem extends ItemWithLocation implements DebugItem {
    @Override // org.jf.dexlib2.iface.debug.DebugItem
    public int getCodeAddress() {
        if (this.location == null) {
            throw new IllegalStateException("Cannot get the address of a BuilderDebugItem that isn't associated with a method.");
        }
        return this.location.getCodeAddress();
    }
}
