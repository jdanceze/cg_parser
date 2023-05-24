package org.apache.tools.zip;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/zip/UnparseableExtraFieldData.class */
public final class UnparseableExtraFieldData implements CentralDirectoryParsingZipExtraField {
    private static final ZipShort HEADER_ID = new ZipShort(44225);
    private byte[] localFileData;
    private byte[] centralDirectoryData;

    @Override // org.apache.tools.zip.ZipExtraField
    public ZipShort getHeaderId() {
        return HEADER_ID;
    }

    @Override // org.apache.tools.zip.ZipExtraField
    public ZipShort getLocalFileDataLength() {
        return new ZipShort(this.localFileData == null ? 0 : this.localFileData.length);
    }

    @Override // org.apache.tools.zip.ZipExtraField
    public ZipShort getCentralDirectoryLength() {
        if (this.centralDirectoryData == null) {
            return getLocalFileDataLength();
        }
        return new ZipShort(this.centralDirectoryData.length);
    }

    @Override // org.apache.tools.zip.ZipExtraField
    public byte[] getLocalFileDataData() {
        return ZipUtil.copy(this.localFileData);
    }

    @Override // org.apache.tools.zip.ZipExtraField
    public byte[] getCentralDirectoryData() {
        return this.centralDirectoryData == null ? getLocalFileDataData() : ZipUtil.copy(this.centralDirectoryData);
    }

    @Override // org.apache.tools.zip.ZipExtraField
    public void parseFromLocalFileData(byte[] buffer, int offset, int length) {
        this.localFileData = new byte[length];
        System.arraycopy(buffer, offset, this.localFileData, 0, length);
    }

    @Override // org.apache.tools.zip.CentralDirectoryParsingZipExtraField
    public void parseFromCentralDirectoryData(byte[] buffer, int offset, int length) {
        this.centralDirectoryData = new byte[length];
        System.arraycopy(buffer, offset, this.centralDirectoryData, 0, length);
        if (this.localFileData == null) {
            parseFromLocalFileData(buffer, offset, length);
        }
    }
}
