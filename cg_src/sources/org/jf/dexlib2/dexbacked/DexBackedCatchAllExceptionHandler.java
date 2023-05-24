package org.jf.dexlib2.dexbacked;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/DexBackedCatchAllExceptionHandler.class */
public class DexBackedCatchAllExceptionHandler extends DexBackedExceptionHandler {
    private final int handlerCodeAddress;

    public DexBackedCatchAllExceptionHandler(@Nonnull DexReader reader) {
        this.handlerCodeAddress = reader.readSmallUleb128();
    }

    @Override // org.jf.dexlib2.iface.ExceptionHandler
    @Nullable
    public String getExceptionType() {
        return null;
    }

    @Override // org.jf.dexlib2.iface.ExceptionHandler
    public int getHandlerCodeAddress() {
        return this.handlerCodeAddress;
    }
}
