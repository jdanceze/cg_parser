package org.jf.dexlib2.dexbacked.raw.util;

import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.io.Writer;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.dexbacked.CDexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.raw.AnnotationDirectoryItem;
import org.jf.dexlib2.dexbacked.raw.AnnotationItem;
import org.jf.dexlib2.dexbacked.raw.AnnotationSetItem;
import org.jf.dexlib2.dexbacked.raw.AnnotationSetRefList;
import org.jf.dexlib2.dexbacked.raw.CallSiteIdItem;
import org.jf.dexlib2.dexbacked.raw.CdexDebugOffsetTable;
import org.jf.dexlib2.dexbacked.raw.ClassDataItem;
import org.jf.dexlib2.dexbacked.raw.ClassDefItem;
import org.jf.dexlib2.dexbacked.raw.CodeItem;
import org.jf.dexlib2.dexbacked.raw.DebugInfoItem;
import org.jf.dexlib2.dexbacked.raw.EncodedArrayItem;
import org.jf.dexlib2.dexbacked.raw.FieldIdItem;
import org.jf.dexlib2.dexbacked.raw.HeaderItem;
import org.jf.dexlib2.dexbacked.raw.HiddenApiClassDataItem;
import org.jf.dexlib2.dexbacked.raw.ItemType;
import org.jf.dexlib2.dexbacked.raw.MapItem;
import org.jf.dexlib2.dexbacked.raw.MethodHandleItem;
import org.jf.dexlib2.dexbacked.raw.MethodIdItem;
import org.jf.dexlib2.dexbacked.raw.ProtoIdItem;
import org.jf.dexlib2.dexbacked.raw.SectionAnnotator;
import org.jf.dexlib2.dexbacked.raw.StringDataItem;
import org.jf.dexlib2.dexbacked.raw.StringIdItem;
import org.jf.dexlib2.dexbacked.raw.TypeIdItem;
import org.jf.dexlib2.dexbacked.raw.TypeListItem;
import org.jf.dexlib2.util.AnnotatedBytes;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/raw/util/DexAnnotator.class */
public class DexAnnotator extends AnnotatedBytes {
    @Nonnull
    public final DexBackedDexFile dexFile;
    private final Map<Integer, SectionAnnotator> annotators;
    private static final Map<Integer, Integer> sectionAnnotationOrder = Maps.newHashMap();

    static {
        int[] sectionOrder = {4096, 0, 1, 2, 3, 4, 5, 7, 8, 6, 8192, ItemType.CODE_ITEM, ItemType.DEBUG_INFO_ITEM, 4097, 4098, 4099, 8194, ItemType.ANNOTATION_ITEM, ItemType.ENCODED_ARRAY_ITEM, ItemType.ANNOTATION_DIRECTORY_ITEM, ItemType.HIDDENAPI_CLASS_DATA_ITEM};
        for (int i = 0; i < sectionOrder.length; i++) {
            sectionAnnotationOrder.put(Integer.valueOf(sectionOrder[i]), Integer.valueOf(i));
        }
    }

    public DexAnnotator(@Nonnull DexBackedDexFile dexFile, int width) {
        super(width);
        this.annotators = Maps.newHashMap();
        this.dexFile = dexFile;
        for (MapItem mapItem : dexFile.getMapItems()) {
            switch (mapItem.getType()) {
                case 0:
                    this.annotators.put(Integer.valueOf(mapItem.getType()), HeaderItem.makeAnnotator(this, mapItem));
                    break;
                case 1:
                    this.annotators.put(Integer.valueOf(mapItem.getType()), StringIdItem.makeAnnotator(this, mapItem));
                    break;
                case 2:
                    this.annotators.put(Integer.valueOf(mapItem.getType()), TypeIdItem.makeAnnotator(this, mapItem));
                    break;
                case 3:
                    this.annotators.put(Integer.valueOf(mapItem.getType()), ProtoIdItem.makeAnnotator(this, mapItem));
                    break;
                case 4:
                    this.annotators.put(Integer.valueOf(mapItem.getType()), FieldIdItem.makeAnnotator(this, mapItem));
                    break;
                case 5:
                    this.annotators.put(Integer.valueOf(mapItem.getType()), MethodIdItem.makeAnnotator(this, mapItem));
                    break;
                case 6:
                    this.annotators.put(Integer.valueOf(mapItem.getType()), ClassDefItem.makeAnnotator(this, mapItem));
                    break;
                case 7:
                    this.annotators.put(Integer.valueOf(mapItem.getType()), CallSiteIdItem.makeAnnotator(this, mapItem));
                    break;
                case 8:
                    this.annotators.put(Integer.valueOf(mapItem.getType()), MethodHandleItem.makeAnnotator(this, mapItem));
                    break;
                case 4096:
                    this.annotators.put(Integer.valueOf(mapItem.getType()), MapItem.makeAnnotator(this, mapItem));
                    break;
                case 4097:
                    this.annotators.put(Integer.valueOf(mapItem.getType()), TypeListItem.makeAnnotator(this, mapItem));
                    break;
                case 4098:
                    this.annotators.put(Integer.valueOf(mapItem.getType()), AnnotationSetRefList.makeAnnotator(this, mapItem));
                    break;
                case 4099:
                    this.annotators.put(Integer.valueOf(mapItem.getType()), AnnotationSetItem.makeAnnotator(this, mapItem));
                    break;
                case 8192:
                    this.annotators.put(Integer.valueOf(mapItem.getType()), ClassDataItem.makeAnnotator(this, mapItem));
                    break;
                case ItemType.CODE_ITEM /* 8193 */:
                    this.annotators.put(Integer.valueOf(mapItem.getType()), CodeItem.makeAnnotator(this, mapItem));
                    break;
                case 8194:
                    this.annotators.put(Integer.valueOf(mapItem.getType()), StringDataItem.makeAnnotator(this, mapItem));
                    break;
                case ItemType.DEBUG_INFO_ITEM /* 8195 */:
                    this.annotators.put(Integer.valueOf(mapItem.getType()), DebugInfoItem.makeAnnotator(this, mapItem));
                    break;
                case ItemType.ANNOTATION_ITEM /* 8196 */:
                    this.annotators.put(Integer.valueOf(mapItem.getType()), AnnotationItem.makeAnnotator(this, mapItem));
                    break;
                case ItemType.ENCODED_ARRAY_ITEM /* 8197 */:
                    this.annotators.put(Integer.valueOf(mapItem.getType()), EncodedArrayItem.makeAnnotator(this, mapItem));
                    break;
                case ItemType.ANNOTATION_DIRECTORY_ITEM /* 8198 */:
                    this.annotators.put(Integer.valueOf(mapItem.getType()), AnnotationDirectoryItem.makeAnnotator(this, mapItem));
                    break;
                case ItemType.HIDDENAPI_CLASS_DATA_ITEM /* 61440 */:
                    this.annotators.put(Integer.valueOf(mapItem.getType()), HiddenApiClassDataItem.makeAnnotator(this, mapItem));
                    break;
                default:
                    throw new RuntimeException(String.format("Unrecognized item type: 0x%x", Integer.valueOf(mapItem.getType())));
            }
        }
    }

    public void writeAnnotations(Writer out) throws IOException {
        List<MapItem> mapItems = this.dexFile.getMapItems();
        Ordering<MapItem> ordering = Ordering.from(new Comparator<MapItem>() { // from class: org.jf.dexlib2.dexbacked.raw.util.DexAnnotator.1
            @Override // java.util.Comparator
            public int compare(MapItem o1, MapItem o2) {
                return Ints.compare(((Integer) DexAnnotator.sectionAnnotationOrder.get(Integer.valueOf(o1.getType()))).intValue(), ((Integer) DexAnnotator.sectionAnnotationOrder.get(Integer.valueOf(o2.getType()))).intValue());
            }
        });
        List<MapItem> mapItems2 = ordering.immutableSortedCopy(mapItems);
        try {
            if (this.dexFile instanceof CDexBackedDexFile) {
                moveTo(this.dexFile.getBaseDataOffset() + ((CDexBackedDexFile) this.dexFile).getDebugInfoOffsetsPos());
                CdexDebugOffsetTable.annotate(this, this.dexFile.getBuffer());
            }
            for (MapItem mapItem : mapItems2) {
                try {
                    SectionAnnotator annotator = this.annotators.get(Integer.valueOf(mapItem.getType()));
                    annotator.annotateSection(this);
                } catch (Exception ex) {
                    System.err.println(String.format("There was an error while dumping the %s section", ItemType.getItemTypeName(mapItem.getType())));
                    ex.printStackTrace(System.err);
                }
            }
        } finally {
            writeAnnotations(out, this.dexFile.getBuffer().getBuf(), this.dexFile.getBuffer().getBaseOffset());
        }
    }

    @Nullable
    public SectionAnnotator getAnnotator(int itemType) {
        return this.annotators.get(Integer.valueOf(itemType));
    }
}
