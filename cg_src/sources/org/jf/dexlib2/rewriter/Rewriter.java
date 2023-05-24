package org.jf.dexlib2.rewriter;

import javax.annotation.Nonnull;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/rewriter/Rewriter.class */
public interface Rewriter<T> {
    @Nonnull
    T rewrite(@Nonnull T t);
}
