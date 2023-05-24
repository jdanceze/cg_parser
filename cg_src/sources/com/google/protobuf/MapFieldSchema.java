package com.google.protobuf;

import com.google.protobuf.MapEntryLite;
import java.util.Map;
@CheckReturnValue
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/MapFieldSchema.class */
interface MapFieldSchema {
    Map<?, ?> forMutableMapData(Object obj);

    Map<?, ?> forMapData(Object obj);

    boolean isImmutable(Object obj);

    Object toImmutable(Object obj);

    Object newMapField(Object obj);

    MapEntryLite.Metadata<?, ?> forMapMetadata(Object obj);

    @CanIgnoreReturnValue
    Object mergeFrom(Object obj, Object obj2);

    int getSerializedSize(int i, Object obj, Object obj2);
}
