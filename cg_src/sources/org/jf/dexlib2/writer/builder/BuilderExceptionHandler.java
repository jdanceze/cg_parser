package org.jf.dexlib2.writer.builder;

import javax.annotation.Nullable;
import org.jf.dexlib2.base.BaseExceptionHandler;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderExceptionHandler.class */
public class BuilderExceptionHandler extends BaseExceptionHandler {
    @Nullable
    final BuilderTypeReference exceptionType;
    final int handlerCodeAddress;

    BuilderExceptionHandler(@Nullable BuilderTypeReference exceptionType, int handlerCodeAddress) {
        this.exceptionType = exceptionType;
        this.handlerCodeAddress = handlerCodeAddress;
    }

    @Override // org.jf.dexlib2.iface.ExceptionHandler
    @Nullable
    public String getExceptionType() {
        if (this.exceptionType == null) {
            return null;
        }
        return this.exceptionType.getType();
    }

    @Override // org.jf.dexlib2.iface.ExceptionHandler
    public int getHandlerCodeAddress() {
        return this.handlerCodeAddress;
    }
}
