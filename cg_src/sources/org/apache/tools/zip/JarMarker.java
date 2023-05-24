package org.apache.tools.zip;

import java.util.zip.ZipException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/zip/JarMarker.class */
public final class JarMarker implements ZipExtraField {
    private static final ZipShort ID = new ZipShort(51966);
    private static final ZipShort NULL = new ZipShort(0);
    private static final byte[] NO_BYTES = new byte[0];
    private static final JarMarker DEFAULT = new JarMarker();

    public static JarMarker getInstance() {
        return DEFAULT;
    }

    @Override // org.apache.tools.zip.ZipExtraField
    public ZipShort getHeaderId() {
        return ID;
    }

    @Override // org.apache.tools.zip.ZipExtraField
    public ZipShort getLocalFileDataLength() {
        return NULL;
    }

    @Override // org.apache.tools.zip.ZipExtraField
    public ZipShort getCentralDirectoryLength() {
        return NULL;
    }

    @Override // org.apache.tools.zip.ZipExtraField
    public byte[] getLocalFileDataData() {
        return NO_BYTES;
    }

    @Override // org.apache.tools.zip.ZipExtraField
    public byte[] getCentralDirectoryData() {
        return NO_BYTES;
    }

    @Override // org.apache.tools.zip.ZipExtraField
    public void parseFromLocalFileData(byte[] data, int offset, int length) throws ZipException {
        if (length != 0) {
            throw new ZipException("JarMarker doesn't expect any data");
        }
    }
}
