package org.jf.dexlib2.rewriter;

import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.BaseMethodParameter;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.MethodParameter;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/rewriter/MethodParameterRewriter.class */
public class MethodParameterRewriter implements Rewriter<MethodParameter> {
    @Nonnull
    protected final Rewriters rewriters;

    public MethodParameterRewriter(@Nonnull Rewriters rewriters) {
        this.rewriters = rewriters;
    }

    @Override // org.jf.dexlib2.rewriter.Rewriter
    @Nonnull
    public MethodParameter rewrite(@Nonnull MethodParameter methodParameter) {
        return new RewrittenMethodParameter(methodParameter);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/rewriter/MethodParameterRewriter$RewrittenMethodParameter.class */
    public class RewrittenMethodParameter extends BaseMethodParameter {
        @Nonnull
        protected MethodParameter methodParameter;

        public RewrittenMethodParameter(@Nonnull MethodParameter methodParameter) {
            this.methodParameter = methodParameter;
        }

        @Override // org.jf.dexlib2.iface.reference.TypeReference, org.jf.dexlib2.iface.ClassDef
        @Nonnull
        public String getType() {
            return MethodParameterRewriter.this.rewriters.getTypeRewriter().rewrite(this.methodParameter.getType());
        }

        @Override // org.jf.dexlib2.iface.MethodParameter
        @Nonnull
        public Set<? extends Annotation> getAnnotations() {
            return RewriterUtils.rewriteSet(MethodParameterRewriter.this.rewriters.getAnnotationRewriter(), this.methodParameter.getAnnotations());
        }

        @Override // org.jf.dexlib2.iface.MethodParameter, org.jf.dexlib2.iface.debug.LocalInfo
        @Nullable
        public String getName() {
            return this.methodParameter.getName();
        }

        @Override // org.jf.dexlib2.base.BaseMethodParameter, org.jf.dexlib2.iface.MethodParameter, org.jf.dexlib2.iface.debug.LocalInfo
        @Nullable
        public String getSignature() {
            return this.methodParameter.getSignature();
        }
    }
}
