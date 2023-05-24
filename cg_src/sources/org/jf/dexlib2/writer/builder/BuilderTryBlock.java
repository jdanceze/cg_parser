package org.jf.dexlib2.writer.builder;

import java.util.List;
import javax.annotation.Nonnull;
import org.jf.dexlib2.base.BaseTryBlock;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderTryBlock.class */
public class BuilderTryBlock extends BaseTryBlock<BuilderExceptionHandler> {
    private final int startCodeAddress;
    private final int codeUnitCount;
    @Nonnull
    private final List<? extends BuilderExceptionHandler> exceptionHandlers;

    public BuilderTryBlock(int startCodeAddress, int codeUnitCount, @Nonnull List<? extends BuilderExceptionHandler> exceptionHandlers) {
        this.startCodeAddress = startCodeAddress;
        this.codeUnitCount = codeUnitCount;
        this.exceptionHandlers = exceptionHandlers;
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
    public List<? extends BuilderExceptionHandler> getExceptionHandlers() {
        return this.exceptionHandlers;
    }
}
