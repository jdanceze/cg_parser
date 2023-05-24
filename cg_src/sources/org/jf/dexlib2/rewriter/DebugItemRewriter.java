package org.jf.dexlib2.rewriter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.debug.DebugItem;
import org.jf.dexlib2.iface.debug.EndLocal;
import org.jf.dexlib2.iface.debug.LocalInfo;
import org.jf.dexlib2.iface.debug.RestartLocal;
import org.jf.dexlib2.iface.debug.StartLocal;
import org.jf.dexlib2.iface.reference.StringReference;
import org.jf.dexlib2.iface.reference.TypeReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/rewriter/DebugItemRewriter.class */
public class DebugItemRewriter implements Rewriter<DebugItem> {
    @Nonnull
    protected final Rewriters rewriters;

    public DebugItemRewriter(@Nonnull Rewriters rewriters) {
        this.rewriters = rewriters;
    }

    @Override // org.jf.dexlib2.rewriter.Rewriter
    @Nonnull
    public DebugItem rewrite(@Nonnull DebugItem value) {
        switch (value.getDebugItemType()) {
            case 3:
                return new RewrittenStartLocal((StartLocal) value);
            case 4:
            default:
                return value;
            case 5:
                return new RewrittenEndLocal((EndLocal) value);
            case 6:
                return new RewrittenRestartLocal((RestartLocal) value);
        }
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/rewriter/DebugItemRewriter$BaseRewrittenLocalInfoDebugItem.class */
    protected class BaseRewrittenLocalInfoDebugItem<T extends DebugItem & LocalInfo> implements DebugItem, LocalInfo {
        @Nonnull
        protected T debugItem;

        public BaseRewrittenLocalInfoDebugItem(@Nonnull T debugItem) {
            this.debugItem = debugItem;
        }

        @Override // org.jf.dexlib2.iface.debug.DebugItem
        public int getDebugItemType() {
            return this.debugItem.getDebugItemType();
        }

        @Override // org.jf.dexlib2.iface.debug.DebugItem
        public int getCodeAddress() {
            return this.debugItem.getCodeAddress();
        }

        @Override // org.jf.dexlib2.iface.debug.LocalInfo
        @Nullable
        public String getName() {
            return this.debugItem.getName();
        }

        @Override // org.jf.dexlib2.iface.debug.LocalInfo
        @Nullable
        public String getType() {
            return (String) RewriterUtils.rewriteNullable(DebugItemRewriter.this.rewriters.getTypeRewriter(), this.debugItem.getType());
        }

        @Override // org.jf.dexlib2.iface.debug.LocalInfo
        @Nullable
        public String getSignature() {
            return this.debugItem.getSignature();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/rewriter/DebugItemRewriter$RewrittenStartLocal.class */
    public class RewrittenStartLocal extends BaseRewrittenLocalInfoDebugItem<StartLocal> implements StartLocal {
        public RewrittenStartLocal(@Nonnull StartLocal debugItem) {
            super(debugItem);
        }

        @Override // org.jf.dexlib2.iface.debug.StartLocal
        public int getRegister() {
            return ((StartLocal) this.debugItem).getRegister();
        }

        @Override // org.jf.dexlib2.iface.debug.StartLocal
        @Nullable
        public StringReference getNameReference() {
            return ((StartLocal) this.debugItem).getNameReference();
        }

        @Override // org.jf.dexlib2.iface.debug.StartLocal
        @Nullable
        public TypeReference getTypeReference() {
            TypeReference typeReference = ((StartLocal) this.debugItem).getTypeReference();
            if (typeReference == null) {
                return null;
            }
            return RewriterUtils.rewriteTypeReference(DebugItemRewriter.this.rewriters.getTypeRewriter(), typeReference);
        }

        @Override // org.jf.dexlib2.iface.debug.StartLocal
        @Nullable
        public StringReference getSignatureReference() {
            return ((StartLocal) this.debugItem).getSignatureReference();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/rewriter/DebugItemRewriter$RewrittenEndLocal.class */
    public class RewrittenEndLocal extends BaseRewrittenLocalInfoDebugItem<EndLocal> implements EndLocal {
        public RewrittenEndLocal(@Nonnull EndLocal instruction) {
            super(instruction);
        }

        @Override // org.jf.dexlib2.iface.debug.EndLocal
        public int getRegister() {
            return ((EndLocal) this.debugItem).getRegister();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/rewriter/DebugItemRewriter$RewrittenRestartLocal.class */
    public class RewrittenRestartLocal extends BaseRewrittenLocalInfoDebugItem<RestartLocal> implements RestartLocal {
        public RewrittenRestartLocal(@Nonnull RestartLocal instruction) {
            super(instruction);
        }

        @Override // org.jf.dexlib2.iface.debug.RestartLocal
        public int getRegister() {
            return ((RestartLocal) this.debugItem).getRegister();
        }
    }
}
