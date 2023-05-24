package org.jf.dexlib2.dexbacked;

import com.google.common.io.ByteStreams;
import java.io.IOException;
import java.io.InputStream;
import java.util.AbstractList;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.dexbacked.raw.HeaderItem;
import org.jf.dexlib2.dexbacked.raw.ItemType;
import org.jf.dexlib2.dexbacked.raw.MapItem;
import org.jf.dexlib2.dexbacked.reference.DexBackedCallSiteReference;
import org.jf.dexlib2.dexbacked.reference.DexBackedFieldReference;
import org.jf.dexlib2.dexbacked.reference.DexBackedMethodHandleReference;
import org.jf.dexlib2.dexbacked.reference.DexBackedMethodProtoReference;
import org.jf.dexlib2.dexbacked.reference.DexBackedMethodReference;
import org.jf.dexlib2.dexbacked.reference.DexBackedStringReference;
import org.jf.dexlib2.dexbacked.reference.DexBackedTypeReference;
import org.jf.dexlib2.dexbacked.util.FixedSizeList;
import org.jf.dexlib2.dexbacked.util.FixedSizeSet;
import org.jf.dexlib2.iface.DexFile;
import org.jf.dexlib2.iface.reference.Reference;
import org.jf.dexlib2.util.DexUtil;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/DexBackedDexFile.class */
public class DexBackedDexFile implements DexFile {
    private final DexBuffer dexBuffer;
    private final DexBuffer dataBuffer;
    @Nonnull
    private final Opcodes opcodes;
    private final int stringCount;
    private final int stringStartOffset;
    private final int typeCount;
    private final int typeStartOffset;
    private final int protoCount;
    private final int protoStartOffset;
    private final int fieldCount;
    private final int fieldStartOffset;
    private final int methodCount;
    private final int methodStartOffset;
    private final int classCount;
    private final int classStartOffset;
    private final int mapOffset;
    private final int hiddenApiRestrictionsOffset;
    private OptionalIndexedSection<String> stringSection;
    private OptionalIndexedSection<String> typeSection;
    private IndexedSection<DexBackedFieldReference> fieldSection;
    private IndexedSection<DexBackedMethodReference> methodSection;
    private IndexedSection<DexBackedMethodProtoReference> protoSection;
    private IndexedSection<DexBackedClassDef> classSection;
    private IndexedSection<DexBackedCallSiteReference> callSiteSection;
    private IndexedSection<DexBackedMethodHandleReference> methodHandleSection;

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/DexBackedDexFile$IndexedSection.class */
    public static abstract class IndexedSection<T> extends AbstractList<T> {
        public abstract int getOffset(int i);
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/DexBackedDexFile$OptionalIndexedSection.class */
    public static abstract class OptionalIndexedSection<T> extends IndexedSection<T> {
        @Nullable
        public abstract T getOptional(int i);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public DexBackedDexFile(@Nullable Opcodes opcodes, @Nonnull byte[] buf, int offset, boolean verifyMagic) {
        this.stringSection = new OptionalIndexedSection<String>() { // from class: org.jf.dexlib2.dexbacked.DexBackedDexFile.5
            @Override // java.util.AbstractList, java.util.List
            public String get(int index) {
                int stringOffset = getOffset(index);
                int stringDataOffset = DexBackedDexFile.this.dexBuffer.readSmallUint(stringOffset);
                DexReader reader = DexBackedDexFile.this.dataBuffer.readerAt(stringDataOffset);
                int utf16Length = reader.readSmallUleb128();
                return reader.readString(utf16Length);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return DexBackedDexFile.this.stringCount;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.jf.dexlib2.dexbacked.DexBackedDexFile.OptionalIndexedSection
            @Nullable
            public String getOptional(int index) {
                if (index == -1) {
                    return null;
                }
                return get(index);
            }

            @Override // org.jf.dexlib2.dexbacked.DexBackedDexFile.IndexedSection
            public int getOffset(int index) {
                if (index >= 0 && index < size()) {
                    return DexBackedDexFile.this.stringStartOffset + (index * 4);
                }
                throw new IndexOutOfBoundsException(String.format("Invalid string index %d, not in [0, %d)", Integer.valueOf(index), Integer.valueOf(size())));
            }
        };
        this.typeSection = new OptionalIndexedSection<String>() { // from class: org.jf.dexlib2.dexbacked.DexBackedDexFile.6
            @Override // java.util.AbstractList, java.util.List
            public String get(int index) {
                int typeOffset = getOffset(index);
                int stringIndex = DexBackedDexFile.this.dexBuffer.readSmallUint(typeOffset);
                return (String) DexBackedDexFile.this.getStringSection().get(stringIndex);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return DexBackedDexFile.this.typeCount;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.jf.dexlib2.dexbacked.DexBackedDexFile.OptionalIndexedSection
            @Nullable
            public String getOptional(int index) {
                if (index == -1) {
                    return null;
                }
                return get(index);
            }

            @Override // org.jf.dexlib2.dexbacked.DexBackedDexFile.IndexedSection
            public int getOffset(int index) {
                if (index >= 0 && index < size()) {
                    return DexBackedDexFile.this.typeStartOffset + (index * 4);
                }
                throw new IndexOutOfBoundsException(String.format("Invalid type index %d, not in [0, %d)", Integer.valueOf(index), Integer.valueOf(size())));
            }
        };
        this.fieldSection = new IndexedSection<DexBackedFieldReference>() { // from class: org.jf.dexlib2.dexbacked.DexBackedDexFile.7
            @Override // java.util.AbstractList, java.util.List
            public DexBackedFieldReference get(int index) {
                return new DexBackedFieldReference(DexBackedDexFile.this, index);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return DexBackedDexFile.this.fieldCount;
            }

            @Override // org.jf.dexlib2.dexbacked.DexBackedDexFile.IndexedSection
            public int getOffset(int index) {
                if (index >= 0 && index < size()) {
                    return DexBackedDexFile.this.fieldStartOffset + (index * 8);
                }
                throw new IndexOutOfBoundsException(String.format("Invalid field index %d, not in [0, %d)", Integer.valueOf(index), Integer.valueOf(size())));
            }
        };
        this.methodSection = new IndexedSection<DexBackedMethodReference>() { // from class: org.jf.dexlib2.dexbacked.DexBackedDexFile.8
            @Override // java.util.AbstractList, java.util.List
            public DexBackedMethodReference get(int index) {
                return new DexBackedMethodReference(DexBackedDexFile.this, index);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return DexBackedDexFile.this.methodCount;
            }

            @Override // org.jf.dexlib2.dexbacked.DexBackedDexFile.IndexedSection
            public int getOffset(int index) {
                if (index >= 0 && index < size()) {
                    return DexBackedDexFile.this.methodStartOffset + (index * 8);
                }
                throw new IndexOutOfBoundsException(String.format("Invalid method index %d, not in [0, %d)", Integer.valueOf(index), Integer.valueOf(size())));
            }
        };
        this.protoSection = new IndexedSection<DexBackedMethodProtoReference>() { // from class: org.jf.dexlib2.dexbacked.DexBackedDexFile.9
            @Override // java.util.AbstractList, java.util.List
            public DexBackedMethodProtoReference get(int index) {
                return new DexBackedMethodProtoReference(DexBackedDexFile.this, index);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return DexBackedDexFile.this.protoCount;
            }

            @Override // org.jf.dexlib2.dexbacked.DexBackedDexFile.IndexedSection
            public int getOffset(int index) {
                if (index >= 0 && index < size()) {
                    return DexBackedDexFile.this.protoStartOffset + (index * 12);
                }
                throw new IndexOutOfBoundsException(String.format("Invalid proto index %d, not in [0, %d)", Integer.valueOf(index), Integer.valueOf(size())));
            }
        };
        this.classSection = new IndexedSection<DexBackedClassDef>() { // from class: org.jf.dexlib2.dexbacked.DexBackedDexFile.10
            @Override // java.util.AbstractList, java.util.List
            public DexBackedClassDef get(int index) {
                return new DexBackedClassDef(DexBackedDexFile.this, getOffset(index), DexBackedDexFile.this.readHiddenApiRestrictionsOffset(index));
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return DexBackedDexFile.this.classCount;
            }

            @Override // org.jf.dexlib2.dexbacked.DexBackedDexFile.IndexedSection
            public int getOffset(int index) {
                if (index >= 0 && index < size()) {
                    return DexBackedDexFile.this.classStartOffset + (index * 32);
                }
                throw new IndexOutOfBoundsException(String.format("Invalid class index %d, not in [0, %d)", Integer.valueOf(index), Integer.valueOf(size())));
            }
        };
        this.callSiteSection = new IndexedSection<DexBackedCallSiteReference>() { // from class: org.jf.dexlib2.dexbacked.DexBackedDexFile.11
            @Override // java.util.AbstractList, java.util.List
            public DexBackedCallSiteReference get(int index) {
                return new DexBackedCallSiteReference(DexBackedDexFile.this, index);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                MapItem mapItem = DexBackedDexFile.this.getMapItemForSection(7);
                if (mapItem == null) {
                    return 0;
                }
                return mapItem.getItemCount();
            }

            @Override // org.jf.dexlib2.dexbacked.DexBackedDexFile.IndexedSection
            public int getOffset(int index) {
                MapItem mapItem = DexBackedDexFile.this.getMapItemForSection(7);
                if (index < 0 || index >= size()) {
                    throw new IndexOutOfBoundsException(String.format("Invalid callsite index %d, not in [0, %d)", Integer.valueOf(index), Integer.valueOf(size())));
                }
                return mapItem.getOffset() + (index * 4);
            }
        };
        this.methodHandleSection = new IndexedSection<DexBackedMethodHandleReference>() { // from class: org.jf.dexlib2.dexbacked.DexBackedDexFile.12
            @Override // java.util.AbstractList, java.util.List
            public DexBackedMethodHandleReference get(int index) {
                return new DexBackedMethodHandleReference(DexBackedDexFile.this, index);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                MapItem mapItem = DexBackedDexFile.this.getMapItemForSection(8);
                if (mapItem == null) {
                    return 0;
                }
                return mapItem.getItemCount();
            }

            @Override // org.jf.dexlib2.dexbacked.DexBackedDexFile.IndexedSection
            public int getOffset(int index) {
                MapItem mapItem = DexBackedDexFile.this.getMapItemForSection(8);
                if (index < 0 || index >= size()) {
                    throw new IndexOutOfBoundsException(String.format("Invalid method handle index %d, not in [0, %d)", Integer.valueOf(index), Integer.valueOf(size())));
                }
                return mapItem.getOffset() + (index * 8);
            }
        };
        this.dexBuffer = new DexBuffer(buf, offset);
        this.dataBuffer = new DexBuffer(buf, offset + getBaseDataOffset());
        int dexVersion = getVersion(buf, offset, verifyMagic);
        if (opcodes == null) {
            this.opcodes = getDefaultOpcodes(dexVersion);
        } else {
            this.opcodes = opcodes;
        }
        this.stringCount = this.dexBuffer.readSmallUint(56);
        this.stringStartOffset = this.dexBuffer.readSmallUint(60);
        this.typeCount = this.dexBuffer.readSmallUint(64);
        this.typeStartOffset = this.dexBuffer.readSmallUint(68);
        this.protoCount = this.dexBuffer.readSmallUint(72);
        this.protoStartOffset = this.dexBuffer.readSmallUint(76);
        this.fieldCount = this.dexBuffer.readSmallUint(80);
        this.fieldStartOffset = this.dexBuffer.readSmallUint(84);
        this.methodCount = this.dexBuffer.readSmallUint(88);
        this.methodStartOffset = this.dexBuffer.readSmallUint(92);
        this.classCount = this.dexBuffer.readSmallUint(96);
        this.classStartOffset = this.dexBuffer.readSmallUint(100);
        this.mapOffset = this.dexBuffer.readSmallUint(52);
        MapItem mapItem = getMapItemForSection(ItemType.HIDDENAPI_CLASS_DATA_ITEM);
        if (mapItem != null) {
            this.hiddenApiRestrictionsOffset = mapItem.getOffset();
        } else {
            this.hiddenApiRestrictionsOffset = 0;
        }
    }

    protected DexBackedDexFile(@Nullable Opcodes opcodes, @Nonnull DexBuffer dexBuffer, @Nonnull DexBuffer dataBuffer, int offset, boolean verifyMagic) {
        this.stringSection = new OptionalIndexedSection<String>() { // from class: org.jf.dexlib2.dexbacked.DexBackedDexFile.5
            @Override // java.util.AbstractList, java.util.List
            public String get(int index) {
                int stringOffset = getOffset(index);
                int stringDataOffset = DexBackedDexFile.this.dexBuffer.readSmallUint(stringOffset);
                DexReader reader = DexBackedDexFile.this.dataBuffer.readerAt(stringDataOffset);
                int utf16Length = reader.readSmallUleb128();
                return reader.readString(utf16Length);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return DexBackedDexFile.this.stringCount;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.jf.dexlib2.dexbacked.DexBackedDexFile.OptionalIndexedSection
            @Nullable
            public String getOptional(int index) {
                if (index == -1) {
                    return null;
                }
                return get(index);
            }

            @Override // org.jf.dexlib2.dexbacked.DexBackedDexFile.IndexedSection
            public int getOffset(int index) {
                if (index >= 0 && index < size()) {
                    return DexBackedDexFile.this.stringStartOffset + (index * 4);
                }
                throw new IndexOutOfBoundsException(String.format("Invalid string index %d, not in [0, %d)", Integer.valueOf(index), Integer.valueOf(size())));
            }
        };
        this.typeSection = new OptionalIndexedSection<String>() { // from class: org.jf.dexlib2.dexbacked.DexBackedDexFile.6
            @Override // java.util.AbstractList, java.util.List
            public String get(int index) {
                int typeOffset = getOffset(index);
                int stringIndex = DexBackedDexFile.this.dexBuffer.readSmallUint(typeOffset);
                return (String) DexBackedDexFile.this.getStringSection().get(stringIndex);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return DexBackedDexFile.this.typeCount;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.jf.dexlib2.dexbacked.DexBackedDexFile.OptionalIndexedSection
            @Nullable
            public String getOptional(int index) {
                if (index == -1) {
                    return null;
                }
                return get(index);
            }

            @Override // org.jf.dexlib2.dexbacked.DexBackedDexFile.IndexedSection
            public int getOffset(int index) {
                if (index >= 0 && index < size()) {
                    return DexBackedDexFile.this.typeStartOffset + (index * 4);
                }
                throw new IndexOutOfBoundsException(String.format("Invalid type index %d, not in [0, %d)", Integer.valueOf(index), Integer.valueOf(size())));
            }
        };
        this.fieldSection = new IndexedSection<DexBackedFieldReference>() { // from class: org.jf.dexlib2.dexbacked.DexBackedDexFile.7
            @Override // java.util.AbstractList, java.util.List
            public DexBackedFieldReference get(int index) {
                return new DexBackedFieldReference(DexBackedDexFile.this, index);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return DexBackedDexFile.this.fieldCount;
            }

            @Override // org.jf.dexlib2.dexbacked.DexBackedDexFile.IndexedSection
            public int getOffset(int index) {
                if (index >= 0 && index < size()) {
                    return DexBackedDexFile.this.fieldStartOffset + (index * 8);
                }
                throw new IndexOutOfBoundsException(String.format("Invalid field index %d, not in [0, %d)", Integer.valueOf(index), Integer.valueOf(size())));
            }
        };
        this.methodSection = new IndexedSection<DexBackedMethodReference>() { // from class: org.jf.dexlib2.dexbacked.DexBackedDexFile.8
            @Override // java.util.AbstractList, java.util.List
            public DexBackedMethodReference get(int index) {
                return new DexBackedMethodReference(DexBackedDexFile.this, index);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return DexBackedDexFile.this.methodCount;
            }

            @Override // org.jf.dexlib2.dexbacked.DexBackedDexFile.IndexedSection
            public int getOffset(int index) {
                if (index >= 0 && index < size()) {
                    return DexBackedDexFile.this.methodStartOffset + (index * 8);
                }
                throw new IndexOutOfBoundsException(String.format("Invalid method index %d, not in [0, %d)", Integer.valueOf(index), Integer.valueOf(size())));
            }
        };
        this.protoSection = new IndexedSection<DexBackedMethodProtoReference>() { // from class: org.jf.dexlib2.dexbacked.DexBackedDexFile.9
            @Override // java.util.AbstractList, java.util.List
            public DexBackedMethodProtoReference get(int index) {
                return new DexBackedMethodProtoReference(DexBackedDexFile.this, index);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return DexBackedDexFile.this.protoCount;
            }

            @Override // org.jf.dexlib2.dexbacked.DexBackedDexFile.IndexedSection
            public int getOffset(int index) {
                if (index >= 0 && index < size()) {
                    return DexBackedDexFile.this.protoStartOffset + (index * 12);
                }
                throw new IndexOutOfBoundsException(String.format("Invalid proto index %d, not in [0, %d)", Integer.valueOf(index), Integer.valueOf(size())));
            }
        };
        this.classSection = new IndexedSection<DexBackedClassDef>() { // from class: org.jf.dexlib2.dexbacked.DexBackedDexFile.10
            @Override // java.util.AbstractList, java.util.List
            public DexBackedClassDef get(int index) {
                return new DexBackedClassDef(DexBackedDexFile.this, getOffset(index), DexBackedDexFile.this.readHiddenApiRestrictionsOffset(index));
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return DexBackedDexFile.this.classCount;
            }

            @Override // org.jf.dexlib2.dexbacked.DexBackedDexFile.IndexedSection
            public int getOffset(int index) {
                if (index >= 0 && index < size()) {
                    return DexBackedDexFile.this.classStartOffset + (index * 32);
                }
                throw new IndexOutOfBoundsException(String.format("Invalid class index %d, not in [0, %d)", Integer.valueOf(index), Integer.valueOf(size())));
            }
        };
        this.callSiteSection = new IndexedSection<DexBackedCallSiteReference>() { // from class: org.jf.dexlib2.dexbacked.DexBackedDexFile.11
            @Override // java.util.AbstractList, java.util.List
            public DexBackedCallSiteReference get(int index) {
                return new DexBackedCallSiteReference(DexBackedDexFile.this, index);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                MapItem mapItem = DexBackedDexFile.this.getMapItemForSection(7);
                if (mapItem == null) {
                    return 0;
                }
                return mapItem.getItemCount();
            }

            @Override // org.jf.dexlib2.dexbacked.DexBackedDexFile.IndexedSection
            public int getOffset(int index) {
                MapItem mapItem = DexBackedDexFile.this.getMapItemForSection(7);
                if (index < 0 || index >= size()) {
                    throw new IndexOutOfBoundsException(String.format("Invalid callsite index %d, not in [0, %d)", Integer.valueOf(index), Integer.valueOf(size())));
                }
                return mapItem.getOffset() + (index * 4);
            }
        };
        this.methodHandleSection = new IndexedSection<DexBackedMethodHandleReference>() { // from class: org.jf.dexlib2.dexbacked.DexBackedDexFile.12
            @Override // java.util.AbstractList, java.util.List
            public DexBackedMethodHandleReference get(int index) {
                return new DexBackedMethodHandleReference(DexBackedDexFile.this, index);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                MapItem mapItem = DexBackedDexFile.this.getMapItemForSection(8);
                if (mapItem == null) {
                    return 0;
                }
                return mapItem.getItemCount();
            }

            @Override // org.jf.dexlib2.dexbacked.DexBackedDexFile.IndexedSection
            public int getOffset(int index) {
                MapItem mapItem = DexBackedDexFile.this.getMapItemForSection(8);
                if (index < 0 || index >= size()) {
                    throw new IndexOutOfBoundsException(String.format("Invalid method handle index %d, not in [0, %d)", Integer.valueOf(index), Integer.valueOf(size())));
                }
                return mapItem.getOffset() + (index * 8);
            }
        };
        this.dexBuffer = dexBuffer;
        this.dataBuffer = dataBuffer;
        byte[] headerBuf = dexBuffer.readByteRange(offset, 112);
        int dexVersion = getVersion(headerBuf, offset, verifyMagic);
        if (opcodes == null) {
            this.opcodes = getDefaultOpcodes(dexVersion);
        } else {
            this.opcodes = opcodes;
        }
        this.stringCount = dexBuffer.readSmallUint(56);
        this.stringStartOffset = dexBuffer.readSmallUint(60);
        this.typeCount = dexBuffer.readSmallUint(64);
        this.typeStartOffset = dexBuffer.readSmallUint(68);
        this.protoCount = dexBuffer.readSmallUint(72);
        this.protoStartOffset = dexBuffer.readSmallUint(76);
        this.fieldCount = dexBuffer.readSmallUint(80);
        this.fieldStartOffset = dexBuffer.readSmallUint(84);
        this.methodCount = dexBuffer.readSmallUint(88);
        this.methodStartOffset = dexBuffer.readSmallUint(92);
        this.classCount = dexBuffer.readSmallUint(96);
        this.classStartOffset = dexBuffer.readSmallUint(100);
        this.mapOffset = dexBuffer.readSmallUint(52);
        MapItem mapItem = getMapItemForSection(ItemType.HIDDENAPI_CLASS_DATA_ITEM);
        if (mapItem != null) {
            this.hiddenApiRestrictionsOffset = mapItem.getOffset();
        } else {
            this.hiddenApiRestrictionsOffset = 0;
        }
    }

    public int getBaseDataOffset() {
        return 0;
    }

    protected int getVersion(byte[] buf, int offset, boolean verifyMagic) {
        if (verifyMagic) {
            return DexUtil.verifyDexHeader(buf, offset);
        }
        return HeaderItem.getVersion(buf, offset);
    }

    protected Opcodes getDefaultOpcodes(int version) {
        return Opcodes.forDexVersion(version);
    }

    public DexBuffer getBuffer() {
        return this.dexBuffer;
    }

    public DexBuffer getDataBuffer() {
        return this.dataBuffer;
    }

    public DexBackedDexFile(@Nullable Opcodes opcodes, @Nonnull DexBuffer buf) {
        this(opcodes, buf.buf, buf.baseOffset);
    }

    public DexBackedDexFile(@Nullable Opcodes opcodes, @Nonnull byte[] buf, int offset) {
        this(opcodes, buf, offset, false);
    }

    public DexBackedDexFile(@Nullable Opcodes opcodes, @Nonnull byte[] buf) {
        this(opcodes, buf, 0, true);
    }

    @Nonnull
    public static DexBackedDexFile fromInputStream(@Nullable Opcodes opcodes, @Nonnull InputStream is) throws IOException {
        DexUtil.verifyDexHeader(is);
        byte[] buf = ByteStreams.toByteArray(is);
        return new DexBackedDexFile(opcodes, buf, 0, false);
    }

    @Override // org.jf.dexlib2.iface.DexFile
    @Nonnull
    public Opcodes getOpcodes() {
        return this.opcodes;
    }

    public boolean supportsOptimizedOpcodes() {
        return false;
    }

    @Override // org.jf.dexlib2.iface.DexFile
    @Nonnull
    public Set<? extends DexBackedClassDef> getClasses() {
        return new FixedSizeSet<DexBackedClassDef>() { // from class: org.jf.dexlib2.dexbacked.DexBackedDexFile.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.jf.dexlib2.dexbacked.util.FixedSizeSet
            @Nonnull
            public DexBackedClassDef readItem(int index) {
                return DexBackedDexFile.this.getClassSection().get(index);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return DexBackedDexFile.this.classCount;
            }
        };
    }

    public List<DexBackedStringReference> getStringReferences() {
        return new AbstractList<DexBackedStringReference>() { // from class: org.jf.dexlib2.dexbacked.DexBackedDexFile.2
            @Override // java.util.AbstractList, java.util.List
            public DexBackedStringReference get(int index) {
                if (index < 0 || index >= DexBackedDexFile.this.getStringSection().size()) {
                    throw new IndexOutOfBoundsException();
                }
                return new DexBackedStringReference(DexBackedDexFile.this, index);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return DexBackedDexFile.this.getStringSection().size();
            }
        };
    }

    public List<DexBackedTypeReference> getTypeReferences() {
        return new AbstractList<DexBackedTypeReference>() { // from class: org.jf.dexlib2.dexbacked.DexBackedDexFile.3
            @Override // java.util.AbstractList, java.util.List
            public DexBackedTypeReference get(int index) {
                if (index < 0 || index >= DexBackedDexFile.this.getTypeSection().size()) {
                    throw new IndexOutOfBoundsException();
                }
                return new DexBackedTypeReference(DexBackedDexFile.this, index);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return DexBackedDexFile.this.getTypeSection().size();
            }
        };
    }

    public List<? extends Reference> getReferences(int referenceType) {
        switch (referenceType) {
            case 0:
                return getStringReferences();
            case 1:
                return getTypeReferences();
            case 2:
                return getFieldSection();
            case 3:
                return getMethodSection();
            default:
                throw new IllegalArgumentException(String.format("Invalid reference type: %d", Integer.valueOf(referenceType)));
        }
    }

    public List<MapItem> getMapItems() {
        final int mapSize = this.dataBuffer.readSmallUint(this.mapOffset);
        return new FixedSizeList<MapItem>() { // from class: org.jf.dexlib2.dexbacked.DexBackedDexFile.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.jf.dexlib2.dexbacked.util.FixedSizeList
            public MapItem readItem(int index) {
                int mapItemOffset = DexBackedDexFile.this.mapOffset + 4 + (index * 12);
                return new MapItem(DexBackedDexFile.this, mapItemOffset);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return mapSize;
            }
        };
    }

    @Nullable
    public MapItem getMapItemForSection(int itemType) {
        for (MapItem mapItem : getMapItems()) {
            if (mapItem.getType() == itemType) {
                return mapItem;
            }
        }
        return null;
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/DexBackedDexFile$NotADexFile.class */
    public static class NotADexFile extends RuntimeException {
        public NotADexFile() {
        }

        public NotADexFile(Throwable cause) {
            super(cause);
        }

        public NotADexFile(String message) {
            super(message);
        }

        public NotADexFile(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public OptionalIndexedSection<String> getStringSection() {
        return this.stringSection;
    }

    public OptionalIndexedSection<String> getTypeSection() {
        return this.typeSection;
    }

    public IndexedSection<DexBackedFieldReference> getFieldSection() {
        return this.fieldSection;
    }

    public IndexedSection<DexBackedMethodReference> getMethodSection() {
        return this.methodSection;
    }

    public IndexedSection<DexBackedMethodProtoReference> getProtoSection() {
        return this.protoSection;
    }

    public IndexedSection<DexBackedClassDef> getClassSection() {
        return this.classSection;
    }

    public IndexedSection<DexBackedCallSiteReference> getCallSiteSection() {
        return this.callSiteSection;
    }

    public IndexedSection<DexBackedMethodHandleReference> getMethodHandleSection() {
        return this.methodHandleSection;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public DexBackedMethodImplementation createMethodImplementation(@Nonnull DexBackedDexFile dexFile, @Nonnull DexBackedMethod method, int codeOffset) {
        return new DexBackedMethodImplementation(dexFile, method, codeOffset);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int readHiddenApiRestrictionsOffset(int classIndex) {
        int offset;
        if (this.hiddenApiRestrictionsOffset == 0 || (offset = this.dexBuffer.readInt(this.hiddenApiRestrictionsOffset + 4 + (classIndex * 4))) == 0) {
            return 0;
        }
        return this.hiddenApiRestrictionsOffset + offset;
    }
}
