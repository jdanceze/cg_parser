package org.jf.dexlib2.iface.debug;

import javax.annotation.Nullable;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/iface/debug/LocalInfo.class */
public interface LocalInfo {
    @Nullable
    String getName();

    @Nullable
    String getType();

    @Nullable
    String getSignature();
}
