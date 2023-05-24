package org.apache.tools.zip;

import java.util.zip.ZipException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/zip/ZipExtraField.class */
public interface ZipExtraField {
    ZipShort getHeaderId();

    ZipShort getLocalFileDataLength();

    ZipShort getCentralDirectoryLength();

    byte[] getLocalFileDataData();

    byte[] getCentralDirectoryData();

    void parseFromLocalFileData(byte[] bArr, int i, int i2) throws ZipException;
}
