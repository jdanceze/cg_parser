package org.jf.dexlib2.dexbacked;

import javax.annotation.Nonnull;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/DexBackedTypedExceptionHandler.class */
public class DexBackedTypedExceptionHandler extends DexBackedExceptionHandler {
    @Nonnull
    private final DexBackedDexFile dexFile;
    private final int typeId;
    private final int handlerCodeAddress;

    public DexBackedTypedExceptionHandler(@Nonnull DexBackedDexFile dexFile, @Nonnull DexReader reader) {
        this.dexFile = dexFile;
        this.typeId = reader.readSmallUleb128();
        this.handlerCodeAddress = reader.readSmallUleb128();
    }

    @Override // org.jf.dexlib2.iface.ExceptionHandler
    @Nonnull
    public String getExceptionType() {
        return (String) this.dexFile.getTypeSection().get(this.typeId);
    }

    @Override // org.jf.dexlib2.iface.ExceptionHandler
    public int getHandlerCodeAddress() {
        return this.handlerCodeAddress;
    }
}
