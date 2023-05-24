package org.jf.dexlib2.iface.debug;

import javax.annotation.Nullable;
import org.jf.dexlib2.iface.reference.StringReference;
import org.jf.dexlib2.iface.reference.TypeReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/iface/debug/StartLocal.class */
public interface StartLocal extends DebugItem, LocalInfo {
    int getRegister();

    @Nullable
    StringReference getNameReference();

    @Nullable
    TypeReference getTypeReference();

    @Nullable
    StringReference getSignatureReference();
}
