package org.jf.dexlib2.iface.debug;

import javax.annotation.Nullable;
import org.jf.dexlib2.iface.reference.StringReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/iface/debug/SetSourceFile.class */
public interface SetSourceFile extends DebugItem {
    @Nullable
    String getSourceFile();

    @Nullable
    StringReference getSourceFileReference();
}
