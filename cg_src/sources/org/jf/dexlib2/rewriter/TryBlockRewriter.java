package org.jf.dexlib2.rewriter;

import java.util.List;
import javax.annotation.Nonnull;
import org.jf.dexlib2.base.BaseTryBlock;
import org.jf.dexlib2.iface.ExceptionHandler;
import org.jf.dexlib2.iface.TryBlock;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/rewriter/TryBlockRewriter.class */
public class TryBlockRewriter implements Rewriter<TryBlock<? extends ExceptionHandler>> {
    @Nonnull
    protected final Rewriters rewriters;

    public TryBlockRewriter(@Nonnull Rewriters rewriters) {
        this.rewriters = rewriters;
    }

    @Override // org.jf.dexlib2.rewriter.Rewriter
    @Nonnull
    public TryBlock<? extends ExceptionHandler> rewrite(@Nonnull TryBlock<? extends ExceptionHandler> tryBlock) {
        return new RewrittenTryBlock(tryBlock);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/rewriter/TryBlockRewriter$RewrittenTryBlock.class */
    public class RewrittenTryBlock extends BaseTryBlock<ExceptionHandler> {
        @Nonnull
        protected TryBlock<? extends ExceptionHandler> tryBlock;

        public RewrittenTryBlock(@Nonnull TryBlock<? extends ExceptionHandler> tryBlock) {
            this.tryBlock = tryBlock;
        }

        @Override // org.jf.dexlib2.iface.TryBlock
        public int getStartCodeAddress() {
            return this.tryBlock.getStartCodeAddress();
        }

        @Override // org.jf.dexlib2.iface.TryBlock
        public int getCodeUnitCount() {
            return this.tryBlock.getCodeUnitCount();
        }

        @Override // org.jf.dexlib2.iface.TryBlock
        @Nonnull
        public List<? extends ExceptionHandler> getExceptionHandlers() {
            return RewriterUtils.rewriteList(TryBlockRewriter.this.rewriters.getExceptionHandlerRewriter(), this.tryBlock.getExceptionHandlers());
        }
    }
}
