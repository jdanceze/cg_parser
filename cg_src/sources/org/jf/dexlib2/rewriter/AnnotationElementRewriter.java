package org.jf.dexlib2.rewriter;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.BaseAnnotationElement;
import org.jf.dexlib2.iface.AnnotationElement;
import org.jf.dexlib2.iface.value.EncodedValue;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/rewriter/AnnotationElementRewriter.class */
public class AnnotationElementRewriter implements Rewriter<AnnotationElement> {
    @Nonnull
    protected final Rewriters rewriters;

    public AnnotationElementRewriter(@Nonnull Rewriters rewriters) {
        this.rewriters = rewriters;
    }

    @Override // org.jf.dexlib2.rewriter.Rewriter
    @Nonnull
    public AnnotationElement rewrite(@Nonnull AnnotationElement annotationElement) {
        return new RewrittenAnnotationElement(annotationElement);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/rewriter/AnnotationElementRewriter$RewrittenAnnotationElement.class */
    public class RewrittenAnnotationElement extends BaseAnnotationElement {
        @Nonnull
        protected AnnotationElement annotationElement;

        public RewrittenAnnotationElement(@Nonnull AnnotationElement annotationElement) {
            this.annotationElement = annotationElement;
        }

        @Override // org.jf.dexlib2.iface.AnnotationElement
        @Nonnull
        public String getName() {
            return this.annotationElement.getName();
        }

        @Override // org.jf.dexlib2.iface.AnnotationElement
        @Nonnull
        public EncodedValue getValue() {
            return AnnotationElementRewriter.this.rewriters.getEncodedValueRewriter().rewrite(this.annotationElement.getValue());
        }
    }
}
