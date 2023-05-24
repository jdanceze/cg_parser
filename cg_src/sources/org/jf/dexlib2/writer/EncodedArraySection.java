package org.jf.dexlib2.writer;

import java.util.List;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/EncodedArraySection.class */
public interface EncodedArraySection<EncodedArrayKey, EncodedValue> extends OffsetSection<EncodedArrayKey> {
    List<? extends EncodedValue> getEncodedValueList(EncodedArrayKey encodedarraykey);
}
