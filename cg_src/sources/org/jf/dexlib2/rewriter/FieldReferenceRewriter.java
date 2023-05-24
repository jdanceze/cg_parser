package org.jf.dexlib2.rewriter;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.reference.BaseFieldReference;
import org.jf.dexlib2.iface.reference.FieldReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/rewriter/FieldReferenceRewriter.class */
public class FieldReferenceRewriter implements Rewriter<FieldReference> {
    @Nonnull
    protected final Rewriters rewriters;

    public FieldReferenceRewriter(@Nonnull Rewriters rewriters) {
        this.rewriters = rewriters;
    }

    @Override // org.jf.dexlib2.rewriter.Rewriter
    @Nonnull
    public FieldReference rewrite(@Nonnull FieldReference fieldReference) {
        return new RewrittenFieldReference(fieldReference);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/rewriter/FieldReferenceRewriter$RewrittenFieldReference.class */
    public class RewrittenFieldReference extends BaseFieldReference {
        @Nonnull
        protected FieldReference fieldReference;

        public RewrittenFieldReference(@Nonnull FieldReference fieldReference) {
            this.fieldReference = fieldReference;
        }

        @Override // org.jf.dexlib2.iface.reference.FieldReference, org.jf.dexlib2.iface.Field, org.jf.dexlib2.iface.Member
        @Nonnull
        public String getDefiningClass() {
            return FieldReferenceRewriter.this.rewriters.getTypeRewriter().rewrite(this.fieldReference.getDefiningClass());
        }

        @Override // org.jf.dexlib2.iface.reference.FieldReference, org.jf.dexlib2.iface.Field, org.jf.dexlib2.iface.Member
        @Nonnull
        public String getName() {
            return this.fieldReference.getName();
        }

        @Override // org.jf.dexlib2.iface.reference.FieldReference, org.jf.dexlib2.iface.Field
        @Nonnull
        public String getType() {
            return FieldReferenceRewriter.this.rewriters.getTypeRewriter().rewrite(this.fieldReference.getType());
        }
    }
}
