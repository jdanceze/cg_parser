package org.jf.dexlib2.rewriter;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nonnull;
import org.jf.dexlib2.base.reference.BaseMethodReference;
import org.jf.dexlib2.iface.reference.MethodReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/rewriter/MethodReferenceRewriter.class */
public class MethodReferenceRewriter implements Rewriter<MethodReference> {
    @Nonnull
    protected final Rewriters rewriters;

    public MethodReferenceRewriter(@Nonnull Rewriters rewriters) {
        this.rewriters = rewriters;
    }

    @Override // org.jf.dexlib2.rewriter.Rewriter
    @Nonnull
    public MethodReference rewrite(@Nonnull MethodReference methodReference) {
        return new RewrittenMethodReference(methodReference);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/rewriter/MethodReferenceRewriter$RewrittenMethodReference.class */
    public class RewrittenMethodReference extends BaseMethodReference {
        @Nonnull
        protected MethodReference methodReference;

        public RewrittenMethodReference(@Nonnull MethodReference methodReference) {
            this.methodReference = methodReference;
        }

        @Override // org.jf.dexlib2.iface.reference.MethodReference, org.jf.dexlib2.iface.Method, org.jf.dexlib2.iface.Member
        @Nonnull
        public String getDefiningClass() {
            return MethodReferenceRewriter.this.rewriters.getTypeRewriter().rewrite(this.methodReference.getDefiningClass());
        }

        @Override // org.jf.dexlib2.iface.reference.MethodReference, org.jf.dexlib2.iface.Method, org.jf.dexlib2.iface.Member
        @Nonnull
        public String getName() {
            return this.methodReference.getName();
        }

        @Override // org.jf.dexlib2.iface.reference.MethodReference
        @Nonnull
        public List<? extends CharSequence> getParameterTypes() {
            return RewriterUtils.rewriteList(MethodReferenceRewriter.this.rewriters.getTypeRewriter(), Lists.transform(this.methodReference.getParameterTypes(), new Function<CharSequence, String>() { // from class: org.jf.dexlib2.rewriter.MethodReferenceRewriter.RewrittenMethodReference.1
                @Override // com.google.common.base.Function
                @Nonnull
                public String apply(CharSequence input) {
                    return input.toString();
                }
            }));
        }

        @Override // org.jf.dexlib2.iface.reference.MethodReference, org.jf.dexlib2.iface.Method
        @Nonnull
        public String getReturnType() {
            return MethodReferenceRewriter.this.rewriters.getTypeRewriter().rewrite(this.methodReference.getReturnType());
        }
    }
}
