package org.apache.tools.zip;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/zip/UnicodePathExtraField.class */
public class UnicodePathExtraField extends AbstractUnicodeExtraField {
    public static final ZipShort UPATH_ID = new ZipShort(28789);

    public UnicodePathExtraField() {
    }

    public UnicodePathExtraField(String text, byte[] bytes, int off, int len) {
        super(text, bytes, off, len);
    }

    public UnicodePathExtraField(String name, byte[] bytes) {
        super(name, bytes);
    }

    @Override // org.apache.tools.zip.ZipExtraField
    public ZipShort getHeaderId() {
        return UPATH_ID;
    }
}
