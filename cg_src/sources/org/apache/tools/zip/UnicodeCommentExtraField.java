package org.apache.tools.zip;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/zip/UnicodeCommentExtraField.class */
public class UnicodeCommentExtraField extends AbstractUnicodeExtraField {
    public static final ZipShort UCOM_ID = new ZipShort(25461);

    public UnicodeCommentExtraField() {
    }

    public UnicodeCommentExtraField(String text, byte[] bytes, int off, int len) {
        super(text, bytes, off, len);
    }

    public UnicodeCommentExtraField(String comment, byte[] bytes) {
        super(comment, bytes);
    }

    @Override // org.apache.tools.zip.ZipExtraField
    public ZipShort getHeaderId() {
        return UCOM_ID;
    }
}
