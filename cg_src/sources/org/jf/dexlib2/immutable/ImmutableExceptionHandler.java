package org.jf.dexlib2.immutable;

import com.google.common.collect.ImmutableList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.BaseExceptionHandler;
import org.jf.dexlib2.iface.ExceptionHandler;
import org.jf.util.ImmutableConverter;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/immutable/ImmutableExceptionHandler.class */
public class ImmutableExceptionHandler extends BaseExceptionHandler implements ExceptionHandler {
    @Nullable
    protected final String exceptionType;
    protected final int handlerCodeAddress;
    private static final ImmutableConverter<ImmutableExceptionHandler, ExceptionHandler> CONVERTER = new ImmutableConverter<ImmutableExceptionHandler, ExceptionHandler>() { // from class: org.jf.dexlib2.immutable.ImmutableExceptionHandler.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.jf.util.ImmutableConverter
        public boolean isImmutable(@Nonnull ExceptionHandler item) {
            return item instanceof ImmutableExceptionHandler;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.jf.util.ImmutableConverter
        @Nonnull
        public ImmutableExceptionHandler makeImmutable(@Nonnull ExceptionHandler item) {
            return ImmutableExceptionHandler.of(item);
        }
    };

    public ImmutableExceptionHandler(@Nullable String exceptionType, int handlerCodeAddress) {
        this.exceptionType = exceptionType;
        this.handlerCodeAddress = handlerCodeAddress;
    }

    public static ImmutableExceptionHandler of(ExceptionHandler exceptionHandler) {
        if (exceptionHandler instanceof ImmutableExceptionHandler) {
            return (ImmutableExceptionHandler) exceptionHandler;
        }
        return new ImmutableExceptionHandler(exceptionHandler.getExceptionType(), exceptionHandler.getHandlerCodeAddress());
    }

    @Override // org.jf.dexlib2.iface.ExceptionHandler
    @Nullable
    public String getExceptionType() {
        return this.exceptionType;
    }

    @Override // org.jf.dexlib2.iface.ExceptionHandler
    public int getHandlerCodeAddress() {
        return this.handlerCodeAddress;
    }

    @Nonnull
    public static ImmutableList<ImmutableExceptionHandler> immutableListOf(@Nullable Iterable<? extends ExceptionHandler> list) {
        return CONVERTER.toList(list);
    }
}
