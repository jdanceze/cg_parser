package org.jf.dexlib2.builder;

import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.BaseTryBlock;
import org.jf.dexlib2.iface.reference.TypeReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/builder/BuilderTryBlock.class */
public class BuilderTryBlock extends BaseTryBlock<BuilderExceptionHandler> {
    @Nonnull
    public final BuilderExceptionHandler exceptionHandler;
    @Nonnull
    public final Label start;
    @Nonnull
    public final Label end;

    public BuilderTryBlock(@Nonnull Label start, @Nonnull Label end, @Nullable String exceptionType, @Nonnull Label handler) {
        this.start = start;
        this.end = end;
        this.exceptionHandler = BuilderExceptionHandler.newExceptionHandler(exceptionType, handler);
    }

    public BuilderTryBlock(@Nonnull Label start, @Nonnull Label end, @Nullable TypeReference exceptionType, @Nonnull Label handler) {
        this.start = start;
        this.end = end;
        this.exceptionHandler = BuilderExceptionHandler.newExceptionHandler(exceptionType, handler);
    }

    public BuilderTryBlock(@Nonnull Label start, @Nonnull Label end, @Nonnull Label handler) {
        this.start = start;
        this.end = end;
        this.exceptionHandler = BuilderExceptionHandler.newExceptionHandler(handler);
    }

    @Override // org.jf.dexlib2.iface.TryBlock
    public int getStartCodeAddress() {
        return this.start.getCodeAddress();
    }

    @Override // org.jf.dexlib2.iface.TryBlock
    public int getCodeUnitCount() {
        return this.end.getCodeAddress() - this.start.getCodeAddress();
    }

    @Override // org.jf.dexlib2.iface.TryBlock
    @Nonnull
    public List<? extends BuilderExceptionHandler> getExceptionHandlers() {
        return ImmutableList.of(this.exceptionHandler);
    }
}
