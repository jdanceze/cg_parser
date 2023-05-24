package org.apache.tools.zip;

import java.util.zip.ZipException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/zip/Zip64RequiredException.class */
public class Zip64RequiredException extends ZipException {
    private static final long serialVersionUID = 20110809;
    static final String ARCHIVE_TOO_BIG_MESSAGE = "archive's size exceeds the limit of 4GByte.";
    static final String TOO_MANY_ENTRIES_MESSAGE = "archive contains more than 65535 entries.";

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getEntryTooBigMessage(ZipEntry ze) {
        return ze.getName() + "'s size exceeds the limit of 4GByte.";
    }

    public Zip64RequiredException(String reason) {
        super(reason);
    }
}
