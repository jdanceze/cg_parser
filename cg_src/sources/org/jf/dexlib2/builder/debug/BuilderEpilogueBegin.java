package org.jf.dexlib2.builder.debug;

import org.jf.dexlib2.builder.BuilderDebugItem;
import org.jf.dexlib2.iface.debug.EpilogueBegin;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/builder/debug/BuilderEpilogueBegin.class */
public class BuilderEpilogueBegin extends BuilderDebugItem implements EpilogueBegin {
    @Override // org.jf.dexlib2.iface.debug.DebugItem
    public int getDebugItemType() {
        return 8;
    }
}
