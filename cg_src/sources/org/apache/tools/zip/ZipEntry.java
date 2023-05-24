package org.apache.tools.zip;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.zip.ZipException;
import org.apache.tools.zip.ExtraFieldUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/zip/ZipEntry.class */
public class ZipEntry extends java.util.zip.ZipEntry implements Cloneable {
    public static final int PLATFORM_UNIX = 3;
    public static final int PLATFORM_FAT = 0;
    public static final int CRC_UNKNOWN = -1;
    private static final int SHORT_MASK = 65535;
    private static final int SHORT_SHIFT = 16;
    private int method;
    private long size;
    private int internalAttributes;
    private int platform;
    private long externalAttributes;
    private ZipExtraField[] extraFields;
    private UnparseableExtraFieldData unparseableExtra;
    private String name;
    private byte[] rawName;
    private GeneralPurposeBit gpb;
    private static final byte[] EMPTY = new byte[0];
    private static final ZipExtraField[] noExtraFields = new ZipExtraField[0];

    public ZipEntry(String name) {
        super(name);
        this.method = -1;
        this.size = -1L;
        this.internalAttributes = 0;
        this.platform = 0;
        this.externalAttributes = 0L;
        this.unparseableExtra = null;
        this.name = null;
        this.rawName = null;
        this.gpb = new GeneralPurposeBit();
        setName(name);
    }

    public ZipEntry(java.util.zip.ZipEntry entry) throws ZipException {
        super(entry);
        this.method = -1;
        this.size = -1L;
        this.internalAttributes = 0;
        this.platform = 0;
        this.externalAttributes = 0L;
        this.unparseableExtra = null;
        this.name = null;
        this.rawName = null;
        this.gpb = new GeneralPurposeBit();
        setName(entry.getName());
        byte[] extra = entry.getExtra();
        if (extra != null) {
            setExtraFields(ExtraFieldUtils.parse(extra, true, ExtraFieldUtils.UnparseableExtraField.READ));
        } else {
            setExtra();
        }
        setMethod(entry.getMethod());
        this.size = entry.getSize();
    }

    public ZipEntry(ZipEntry entry) throws ZipException {
        this((java.util.zip.ZipEntry) entry);
        setInternalAttributes(entry.getInternalAttributes());
        setExternalAttributes(entry.getExternalAttributes());
        setExtraFields(getAllExtraFieldsNoCopy());
        setPlatform(entry.getPlatform());
        GeneralPurposeBit other = entry.getGeneralPurposeBit();
        setGeneralPurposeBit(other == null ? null : (GeneralPurposeBit) other.clone());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ZipEntry() {
        this("");
    }

    public ZipEntry(File inputFile, String entryName) {
        this((!inputFile.isDirectory() || entryName.endsWith("/")) ? entryName : entryName + "/");
        if (inputFile.isFile()) {
            setSize(inputFile.length());
        }
        setTime(inputFile.lastModified());
    }

    @Override // java.util.zip.ZipEntry
    public Object clone() {
        ZipEntry e = (ZipEntry) super.clone();
        e.setInternalAttributes(getInternalAttributes());
        e.setExternalAttributes(getExternalAttributes());
        e.setExtraFields(getAllExtraFieldsNoCopy());
        return e;
    }

    @Override // java.util.zip.ZipEntry
    public int getMethod() {
        return this.method;
    }

    @Override // java.util.zip.ZipEntry
    public void setMethod(int method) {
        if (method < 0) {
            throw new IllegalArgumentException("ZIP compression method can not be negative: " + method);
        }
        this.method = method;
    }

    public int getInternalAttributes() {
        return this.internalAttributes;
    }

    public void setInternalAttributes(int value) {
        this.internalAttributes = value;
    }

    public long getExternalAttributes() {
        return this.externalAttributes;
    }

    public void setExternalAttributes(long value) {
        this.externalAttributes = value;
    }

    public void setUnixMode(int mode) {
        setExternalAttributes((mode << 16) | ((mode & 128) == 0 ? 1 : 0) | (isDirectory() ? 16 : 0));
        this.platform = 3;
    }

    public int getUnixMode() {
        if (this.platform != 3) {
            return 0;
        }
        return (int) ((getExternalAttributes() >> 16) & 65535);
    }

    public int getPlatform() {
        return this.platform;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public void setExtraFields(ZipExtraField[] fields) {
        List<ZipExtraField> newFields = new ArrayList<>();
        for (ZipExtraField field : fields) {
            if (field instanceof UnparseableExtraFieldData) {
                this.unparseableExtra = (UnparseableExtraFieldData) field;
            } else {
                newFields.add(field);
            }
        }
        this.extraFields = (ZipExtraField[]) newFields.toArray(new ZipExtraField[newFields.size()]);
        setExtra();
    }

    public ZipExtraField[] getExtraFields() {
        return getParseableExtraFields();
    }

    public ZipExtraField[] getExtraFields(boolean includeUnparseable) {
        return includeUnparseable ? getAllExtraFields() : getParseableExtraFields();
    }

    private ZipExtraField[] getParseableExtraFieldsNoCopy() {
        if (this.extraFields == null) {
            return noExtraFields;
        }
        return this.extraFields;
    }

    private ZipExtraField[] getParseableExtraFields() {
        ZipExtraField[] parseableExtraFields = getParseableExtraFieldsNoCopy();
        return parseableExtraFields == this.extraFields ? copyOf(parseableExtraFields) : parseableExtraFields;
    }

    private ZipExtraField[] copyOf(ZipExtraField[] src) {
        return copyOf(src, src.length);
    }

    private ZipExtraField[] copyOf(ZipExtraField[] src, int length) {
        ZipExtraField[] cpy = new ZipExtraField[length];
        System.arraycopy(src, 0, cpy, 0, Math.min(src.length, length));
        return cpy;
    }

    private ZipExtraField[] getMergedFields() {
        ZipExtraField[] zipExtraFields = copyOf(this.extraFields, this.extraFields.length + 1);
        zipExtraFields[this.extraFields.length] = this.unparseableExtra;
        return zipExtraFields;
    }

    private ZipExtraField[] getUnparseableOnly() {
        return this.unparseableExtra == null ? noExtraFields : new ZipExtraField[]{this.unparseableExtra};
    }

    private ZipExtraField[] getAllExtraFields() {
        ZipExtraField[] allExtraFieldsNoCopy = getAllExtraFieldsNoCopy();
        return allExtraFieldsNoCopy == this.extraFields ? copyOf(allExtraFieldsNoCopy) : allExtraFieldsNoCopy;
    }

    private ZipExtraField[] getAllExtraFieldsNoCopy() {
        if (this.extraFields == null) {
            return getUnparseableOnly();
        }
        return this.unparseableExtra != null ? getMergedFields() : this.extraFields;
    }

    public void addExtraField(ZipExtraField ze) {
        if (ze instanceof UnparseableExtraFieldData) {
            this.unparseableExtra = (UnparseableExtraFieldData) ze;
        } else if (this.extraFields == null) {
            this.extraFields = new ZipExtraField[]{ze};
        } else {
            if (getExtraField(ze.getHeaderId()) != null) {
                removeExtraField(ze.getHeaderId());
            }
            ZipExtraField[] zipExtraFields = copyOf(this.extraFields, this.extraFields.length + 1);
            zipExtraFields[this.extraFields.length] = ze;
            this.extraFields = zipExtraFields;
        }
        setExtra();
    }

    public void addAsFirstExtraField(ZipExtraField ze) {
        if (ze instanceof UnparseableExtraFieldData) {
            this.unparseableExtra = (UnparseableExtraFieldData) ze;
        } else {
            if (getExtraField(ze.getHeaderId()) != null) {
                removeExtraField(ze.getHeaderId());
            }
            ZipExtraField[] copy = this.extraFields;
            int newLen = this.extraFields != null ? this.extraFields.length + 1 : 1;
            this.extraFields = new ZipExtraField[newLen];
            this.extraFields[0] = ze;
            if (copy != null) {
                System.arraycopy(copy, 0, this.extraFields, 1, this.extraFields.length - 1);
            }
        }
        setExtra();
    }

    public void removeExtraField(ZipShort type) {
        ZipExtraField[] zipExtraFieldArr;
        if (this.extraFields == null) {
            throw new NoSuchElementException();
        }
        List<ZipExtraField> newResult = new ArrayList<>();
        for (ZipExtraField extraField : this.extraFields) {
            if (!type.equals(extraField.getHeaderId())) {
                newResult.add(extraField);
            }
        }
        if (this.extraFields.length == newResult.size()) {
            throw new NoSuchElementException();
        }
        this.extraFields = (ZipExtraField[]) newResult.toArray(new ZipExtraField[newResult.size()]);
        setExtra();
    }

    public void removeUnparseableExtraFieldData() {
        if (this.unparseableExtra == null) {
            throw new NoSuchElementException();
        }
        this.unparseableExtra = null;
        setExtra();
    }

    public ZipExtraField getExtraField(ZipShort type) {
        ZipExtraField[] zipExtraFieldArr;
        if (this.extraFields != null) {
            for (ZipExtraField extraField : this.extraFields) {
                if (type.equals(extraField.getHeaderId())) {
                    return extraField;
                }
            }
            return null;
        }
        return null;
    }

    public UnparseableExtraFieldData getUnparseableExtraFieldData() {
        return this.unparseableExtra;
    }

    @Override // java.util.zip.ZipEntry
    public void setExtra(byte[] extra) throws RuntimeException {
        try {
            ZipExtraField[] local = ExtraFieldUtils.parse(extra, true, ExtraFieldUtils.UnparseableExtraField.READ);
            mergeExtraFields(local, true);
        } catch (ZipException e) {
            throw new RuntimeException("Error parsing extra fields for entry: " + getName() + " - " + e.getMessage(), e);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setExtra() {
        super.setExtra(ExtraFieldUtils.mergeLocalFileDataData(getExtraFields(true)));
    }

    public void setCentralDirectoryExtra(byte[] b) {
        try {
            ZipExtraField[] central = ExtraFieldUtils.parse(b, false, ExtraFieldUtils.UnparseableExtraField.READ);
            mergeExtraFields(central, false);
        } catch (ZipException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public byte[] getLocalFileDataExtra() {
        byte[] extra = getExtra();
        return extra != null ? extra : EMPTY;
    }

    public byte[] getCentralDirectoryExtra() {
        return ExtraFieldUtils.mergeCentralDirectoryData(getExtraFields(true));
    }

    @Deprecated
    public void setComprSize(long size) {
        setCompressedSize(size);
    }

    @Override // java.util.zip.ZipEntry
    public String getName() {
        return this.name == null ? super.getName() : this.name;
    }

    @Override // java.util.zip.ZipEntry
    public boolean isDirectory() {
        return getName().endsWith("/");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setName(String name) {
        if (name != null && getPlatform() == 0 && !name.contains("/")) {
            name = name.replace('\\', '/');
        }
        this.name = name;
    }

    @Override // java.util.zip.ZipEntry
    public long getSize() {
        return this.size;
    }

    @Override // java.util.zip.ZipEntry
    public void setSize(long size) {
        if (size < 0) {
            throw new IllegalArgumentException("invalid entry size");
        }
        this.size = size;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setName(String name, byte[] rawName) {
        setName(name);
        this.rawName = rawName;
    }

    public byte[] getRawName() {
        if (this.rawName != null) {
            byte[] b = new byte[this.rawName.length];
            System.arraycopy(this.rawName, 0, b, 0, this.rawName.length);
            return b;
        }
        return null;
    }

    @Override // java.util.zip.ZipEntry
    public int hashCode() {
        return getName().hashCode();
    }

    public GeneralPurposeBit getGeneralPurposeBit() {
        return this.gpb;
    }

    public void setGeneralPurposeBit(GeneralPurposeBit b) {
        this.gpb = b;
    }

    private void mergeExtraFields(ZipExtraField[] f, boolean local) throws ZipException {
        ZipExtraField existing;
        if (this.extraFields == null) {
            setExtraFields(f);
            return;
        }
        for (ZipExtraField element : f) {
            if (element instanceof UnparseableExtraFieldData) {
                existing = this.unparseableExtra;
            } else {
                existing = getExtraField(element.getHeaderId());
            }
            if (existing == null) {
                addExtraField(element);
            } else if (local || !(existing instanceof CentralDirectoryParsingZipExtraField)) {
                byte[] b = element.getLocalFileDataData();
                existing.parseFromLocalFileData(b, 0, b.length);
            } else {
                byte[] b2 = element.getCentralDirectoryData();
                ((CentralDirectoryParsingZipExtraField) existing).parseFromCentralDirectoryData(b2, 0, b2.length);
            }
        }
        setExtra();
    }

    public Date getLastModifiedDate() {
        return new Date(getTime());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ZipEntry other = (ZipEntry) obj;
        String myName = getName();
        String otherName = other.getName();
        if (myName == null) {
            if (otherName != null) {
                return false;
            }
        } else if (!myName.equals(otherName)) {
            return false;
        }
        String myComment = getComment();
        String otherComment = other.getComment();
        if (myComment == null) {
            myComment = "";
        }
        if (otherComment == null) {
            otherComment = "";
        }
        return getTime() == other.getTime() && myComment.equals(otherComment) && getInternalAttributes() == other.getInternalAttributes() && getPlatform() == other.getPlatform() && getExternalAttributes() == other.getExternalAttributes() && getMethod() == other.getMethod() && getSize() == other.getSize() && getCrc() == other.getCrc() && getCompressedSize() == other.getCompressedSize() && Arrays.equals(getCentralDirectoryExtra(), other.getCentralDirectoryExtra()) && Arrays.equals(getLocalFileDataExtra(), other.getLocalFileDataExtra()) && this.gpb.equals(other.gpb);
    }
}
