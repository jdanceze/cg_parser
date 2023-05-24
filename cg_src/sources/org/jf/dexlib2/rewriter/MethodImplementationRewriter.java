package org.jf.dexlib2.rewriter;

import java.util.List;
import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.ExceptionHandler;
import org.jf.dexlib2.iface.MethodImplementation;
import org.jf.dexlib2.iface.TryBlock;
import org.jf.dexlib2.iface.debug.DebugItem;
import org.jf.dexlib2.iface.instruction.Instruction;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/rewriter/MethodImplementationRewriter.class */
public class MethodImplementationRewriter implements Rewriter<MethodImplementation> {
    @Nonnull
    protected final Rewriters rewriters;

    public MethodImplementationRewriter(@Nonnull Rewriters rewriters) {
        this.rewriters = rewriters;
    }

    @Override // org.jf.dexlib2.rewriter.Rewriter
    @Nonnull
    public MethodImplementation rewrite(@Nonnull MethodImplementation methodImplementation) {
        return new RewrittenMethodImplementation(methodImplementation);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/rewriter/MethodImplementationRewriter$RewrittenMethodImplementation.class */
    public class RewrittenMethodImplementation implements MethodImplementation {
        @Nonnull
        protected MethodImplementation methodImplementation;

        public RewrittenMethodImplementation(@Nonnull MethodImplementation methodImplementation) {
            this.methodImplementation = methodImplementation;
        }

        @Override // org.jf.dexlib2.iface.MethodImplementation
        public int getRegisterCount() {
            return this.methodImplementation.getRegisterCount();
        }

        @Override // org.jf.dexlib2.iface.MethodImplementation
        @Nonnull
        public Iterable<? extends Instruction> getInstructions() {
            return RewriterUtils.rewriteIterable(MethodImplementationRewriter.this.rewriters.getInstructionRewriter(), this.methodImplementation.getInstructions());
        }

        @Override // org.jf.dexlib2.iface.MethodImplementation
        @Nonnull
        public List<? extends TryBlock<? extends ExceptionHandler>> getTryBlocks() {
            return RewriterUtils.rewriteList(MethodImplementationRewriter.this.rewriters.getTryBlockRewriter(), this.methodImplementation.getTryBlocks());
        }

        @Override // org.jf.dexlib2.iface.MethodImplementation
        @Nonnull
        public Iterable<? extends DebugItem> getDebugItems() {
            return RewriterUtils.rewriteIterable(MethodImplementationRewriter.this.rewriters.getDebugItemRewriter(), this.methodImplementation.getDebugItems());
        }
    }
}
