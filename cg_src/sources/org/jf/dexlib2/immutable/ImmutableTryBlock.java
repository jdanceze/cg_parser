package org.jf.dexlib2.immutable;

import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.BaseTryBlock;
import org.jf.dexlib2.iface.ExceptionHandler;
import org.jf.dexlib2.iface.TryBlock;
import org.jf.util.ImmutableConverter;
import org.jf.util.ImmutableUtils;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/immutable/ImmutableTryBlock.class */
public class ImmutableTryBlock extends BaseTryBlock<ImmutableExceptionHandler> {
    protected final int startCodeAddress;
    protected final int codeUnitCount;
    @Nonnull
    protected final ImmutableList<? extends ImmutableExceptionHandler> exceptionHandlers;
    private static final ImmutableConverter<ImmutableTryBlock, TryBlock<? extends ExceptionHandler>> CONVERTER = new ImmutableConverter<ImmutableTryBlock, TryBlock<? extends ExceptionHandler>>() { // from class: org.jf.dexlib2.immutable.ImmutableTryBlock.1
        @Override // org.jf.util.ImmutableConverter
        protected /* bridge */ /* synthetic */ boolean isImmutable(@Nonnull TryBlock<? extends ExceptionHandler> tryBlock) {
            return isImmutable2((TryBlock) tryBlock);
        }

        /* renamed from: isImmutable  reason: avoid collision after fix types in other method */
        protected boolean isImmutable2(@Nonnull TryBlock item) {
            return item instanceof ImmutableTryBlock;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.jf.util.ImmutableConverter
        @Nonnull
        public ImmutableTryBlock makeImmutable(@Nonnull TryBlock<? extends ExceptionHandler> item) {
            return ImmutableTryBlock.of(item);
        }
    };

    public ImmutableTryBlock(int startCodeAddress, int codeUnitCount, @Nullable List<? extends ExceptionHandler> exceptionHandlers) {
        this.startCodeAddress = startCodeAddress;
        this.codeUnitCount = codeUnitCount;
        this.exceptionHandlers = ImmutableExceptionHandler.immutableListOf(exceptionHandlers);
    }

    public ImmutableTryBlock(int startCodeAddress, int codeUnitCount, @Nullable ImmutableList<? extends ImmutableExceptionHandler> exceptionHandlers) {
        this.startCodeAddress = startCodeAddress;
        this.codeUnitCount = codeUnitCount;
        this.exceptionHandlers = ImmutableUtils.nullToEmptyList(exceptionHandlers);
    }

    public static ImmutableTryBlock of(TryBlock<? extends ExceptionHandler> tryBlock) {
        if (tryBlock instanceof ImmutableTryBlock) {
            return (ImmutableTryBlock) tryBlock;
        }
        return new ImmutableTryBlock(tryBlock.getStartCodeAddress(), tryBlock.getCodeUnitCount(), (List<? extends ExceptionHandler>) tryBlock.getExceptionHandlers());
    }

    @Override // org.jf.dexlib2.iface.TryBlock
    public int getStartCodeAddress() {
        return this.startCodeAddress;
    }

    @Override // org.jf.dexlib2.iface.TryBlock
    public int getCodeUnitCount() {
        return this.codeUnitCount;
    }

    @Override // org.jf.dexlib2.iface.TryBlock
    @Nonnull
    public ImmutableList<? extends ImmutableExceptionHandler> getExceptionHandlers() {
        return this.exceptionHandlers;
    }

    @Nonnull
    public static ImmutableList<ImmutableTryBlock> immutableListOf(@Nullable List<? extends TryBlock<? extends ExceptionHandler>> list) {
        return CONVERTER.toList(list);
    }
}
