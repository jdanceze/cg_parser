package org.jf.dexlib2.writer;

import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.reference.StringReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/StringSection.class */
public interface StringSection<StringKey, StringRef extends StringReference> extends NullableIndexSection<StringKey> {
    int getItemIndex(@Nonnull StringRef stringref);

    boolean hasJumboIndexes();
}
