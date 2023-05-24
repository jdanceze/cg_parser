package org.apache.tools.zip;

import java.util.zip.ZipException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/zip/CentralDirectoryParsingZipExtraField.class */
public interface CentralDirectoryParsingZipExtraField extends ZipExtraField {
    void parseFromCentralDirectoryData(byte[] bArr, int i, int i2) throws ZipException;
}
