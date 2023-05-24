package org.jf.dexlib2.immutable.util;

import com.google.common.collect.ImmutableList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.util.ImmutableConverter;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/immutable/util/CharSequenceConverter.class */
public final class CharSequenceConverter {
    private static final ImmutableConverter<String, CharSequence> CONVERTER = new ImmutableConverter<String, CharSequence>() { // from class: org.jf.dexlib2.immutable.util.CharSequenceConverter.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.jf.util.ImmutableConverter
        public boolean isImmutable(@Nonnull CharSequence item) {
            return item instanceof String;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.jf.util.ImmutableConverter
        @Nonnull
        public String makeImmutable(@Nonnull CharSequence item) {
            return item.toString();
        }
    };

    private CharSequenceConverter() {
    }

    @Nonnull
    public static ImmutableList<String> immutableStringList(@Nullable Iterable<? extends CharSequence> iterable) {
        return CONVERTER.toList(iterable);
    }
}
