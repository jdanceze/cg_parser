package com.google.protobuf;

import com.google.protobuf.MapEntryLite;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/MapFieldSchemaFull.class */
class MapFieldSchemaFull implements MapFieldSchema {
    MapFieldSchemaFull() {
    }

    @Override // com.google.protobuf.MapFieldSchema
    public Map<?, ?> forMutableMapData(Object mapField) {
        return ((MapField) mapField).getMutableMap();
    }

    @Override // com.google.protobuf.MapFieldSchema
    public Map<?, ?> forMapData(Object mapField) {
        return ((MapField) mapField).getMap();
    }

    @Override // com.google.protobuf.MapFieldSchema
    public boolean isImmutable(Object mapField) {
        return !((MapField) mapField).isMutable();
    }

    @Override // com.google.protobuf.MapFieldSchema
    public Object toImmutable(Object mapField) {
        ((MapField) mapField).makeImmutable();
        return mapField;
    }

    @Override // com.google.protobuf.MapFieldSchema
    public Object newMapField(Object mapDefaultEntry) {
        return MapField.newMapField((MapEntry) mapDefaultEntry);
    }

    @Override // com.google.protobuf.MapFieldSchema
    public MapEntryLite.Metadata<?, ?> forMapMetadata(Object mapDefaultEntry) {
        return ((MapEntry) mapDefaultEntry).getMetadata();
    }

    @Override // com.google.protobuf.MapFieldSchema
    public Object mergeFrom(Object destMapField, Object srcMapField) {
        return mergeFromFull(destMapField, srcMapField);
    }

    private static <K, V> Object mergeFromFull(Object destMapField, Object srcMapField) {
        MapField<K, V> mine = (MapField) destMapField;
        MapField<K, V> other = (MapField) srcMapField;
        if (!mine.isMutable()) {
            mine.copy();
        }
        mine.mergeFrom(other);
        return mine;
    }

    @Override // com.google.protobuf.MapFieldSchema
    public int getSerializedSize(int number, Object mapField, Object mapDefaultEntry) {
        return getSerializedSizeFull(number, mapField, mapDefaultEntry);
    }

    private static <K, V> int getSerializedSizeFull(int number, Object mapField, Object defaultEntryObject) {
        if (mapField == null) {
            return 0;
        }
        Map<K, V> map = ((MapField) mapField).getMap();
        MapEntry<K, V> defaultEntry = (MapEntry) defaultEntryObject;
        if (map.isEmpty()) {
            return 0;
        }
        int size = 0;
        for (Map.Entry<K, V> entry : map.entrySet()) {
            size += CodedOutputStream.computeTagSize(number) + CodedOutputStream.computeLengthDelimitedFieldSize(MapEntryLite.computeSerializedSize(defaultEntry.getMetadata(), entry.getKey(), entry.getValue()));
        }
        return size;
    }
}
