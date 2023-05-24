package org.apache.tools.zip;

import java.util.zip.CRC32;
import java.util.zip.ZipException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/zip/AsiExtraField.class */
public class AsiExtraField implements ZipExtraField, UnixStat, Cloneable {
    private static final ZipShort HEADER_ID = new ZipShort(30062);
    private static final int WORD = 4;
    private int mode = 0;
    private int uid = 0;
    private int gid = 0;
    private String link = "";
    private boolean dirFlag = false;
    private CRC32 crc = new CRC32();

    @Override // org.apache.tools.zip.ZipExtraField
    public ZipShort getHeaderId() {
        return HEADER_ID;
    }

    @Override // org.apache.tools.zip.ZipExtraField
    public ZipShort getLocalFileDataLength() {
        return new ZipShort(14 + getLinkedFile().getBytes().length);
    }

    @Override // org.apache.tools.zip.ZipExtraField
    public ZipShort getCentralDirectoryLength() {
        return getLocalFileDataLength();
    }

    @Override // org.apache.tools.zip.ZipExtraField
    public byte[] getLocalFileDataData() {
        byte[] data = new byte[getLocalFileDataLength().getValue() - 4];
        System.arraycopy(ZipShort.getBytes(getMode()), 0, data, 0, 2);
        byte[] linkArray = getLinkedFile().getBytes();
        System.arraycopy(ZipLong.getBytes(linkArray.length), 0, data, 2, 4);
        System.arraycopy(ZipShort.getBytes(getUserId()), 0, data, 6, 2);
        System.arraycopy(ZipShort.getBytes(getGroupId()), 0, data, 8, 2);
        System.arraycopy(linkArray, 0, data, 10, linkArray.length);
        this.crc.reset();
        this.crc.update(data);
        long checksum = this.crc.getValue();
        byte[] result = new byte[data.length + 4];
        System.arraycopy(ZipLong.getBytes(checksum), 0, result, 0, 4);
        System.arraycopy(data, 0, result, 4, data.length);
        return result;
    }

    @Override // org.apache.tools.zip.ZipExtraField
    public byte[] getCentralDirectoryData() {
        return getLocalFileDataData();
    }

    public void setUserId(int uid) {
        this.uid = uid;
    }

    public int getUserId() {
        return this.uid;
    }

    public void setGroupId(int gid) {
        this.gid = gid;
    }

    public int getGroupId() {
        return this.gid;
    }

    public void setLinkedFile(String name) {
        this.link = name;
        this.mode = getMode(this.mode);
    }

    public String getLinkedFile() {
        return this.link;
    }

    public boolean isLink() {
        return !getLinkedFile().isEmpty();
    }

    public void setMode(int mode) {
        this.mode = getMode(mode);
    }

    public int getMode() {
        return this.mode;
    }

    public void setDirectory(boolean dirFlag) {
        this.dirFlag = dirFlag;
        this.mode = getMode(this.mode);
    }

    public boolean isDirectory() {
        return this.dirFlag && !isLink();
    }

    @Override // org.apache.tools.zip.ZipExtraField
    public void parseFromLocalFileData(byte[] data, int offset, int length) throws ZipException {
        long givenChecksum = ZipLong.getValue(data, offset);
        byte[] tmp = new byte[length - 4];
        System.arraycopy(data, offset + 4, tmp, 0, length - 4);
        this.crc.reset();
        this.crc.update(tmp);
        long realChecksum = this.crc.getValue();
        if (givenChecksum != realChecksum) {
            throw new ZipException("bad CRC checksum " + Long.toHexString(givenChecksum) + " instead of " + Long.toHexString(realChecksum));
        }
        int newMode = ZipShort.getValue(tmp, 0);
        int linkArrayLength = (int) ZipLong.getValue(tmp, 2);
        if (linkArrayLength < 0 || linkArrayLength > tmp.length - 10) {
            throw new ZipException("Bad symbolic link name length " + linkArrayLength + " in ASI extra field");
        }
        this.uid = ZipShort.getValue(tmp, 6);
        this.gid = ZipShort.getValue(tmp, 8);
        if (linkArrayLength == 0) {
            this.link = "";
        } else {
            byte[] linkArray = new byte[linkArrayLength];
            System.arraycopy(tmp, 10, linkArray, 0, linkArrayLength);
            this.link = new String(linkArray);
        }
        setDirectory((newMode & 16384) != 0);
        setMode(newMode);
    }

    protected int getMode(int mode) {
        int type = 32768;
        if (isLink()) {
            type = 40960;
        } else if (isDirectory()) {
            type = 16384;
        }
        return type | (mode & 4095);
    }

    public Object clone() {
        try {
            AsiExtraField cloned = (AsiExtraField) super.clone();
            cloned.crc = new CRC32();
            return cloned;
        } catch (CloneNotSupportedException cnfe) {
            throw new RuntimeException(cnfe);
        }
    }
}
