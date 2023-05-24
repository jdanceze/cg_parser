package org.apache.tools.zip;

import com.sun.xml.fastinfoset.EncodingConstants;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.CRC32;
import org.apache.tools.zip.UnsupportedZipFeatureException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/zip/ZipUtil.class */
public abstract class ZipUtil {
    private static final byte[] DOS_TIME_MIN = ZipLong.getBytes(8448);

    public static ZipLong toDosTime(Date time) {
        return new ZipLong(toDosTime(time.getTime()));
    }

    public static byte[] toDosTime(long t) {
        byte[] result = new byte[4];
        toDosTime(t, result, 0);
        return result;
    }

    public static void toDosTime(long t, byte[] buf, int offset) {
        toDosTime(Calendar.getInstance(), t, buf, offset);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void toDosTime(Calendar c, long t, byte[] buf, int offset) {
        c.setTimeInMillis(t);
        int year = c.get(1);
        if (year < 1980) {
            System.arraycopy(DOS_TIME_MIN, 0, buf, offset, DOS_TIME_MIN.length);
            return;
        }
        int month = c.get(2) + 1;
        long value = ((year - 1980) << 25) | (month << 21) | (c.get(5) << 16) | (c.get(11) << 11) | (c.get(12) << 5) | (c.get(13) >> 1);
        ZipLong.putLong(value, buf, offset);
    }

    public static long adjustToLong(int i) {
        if (i < 0) {
            return EncodingConstants.OCTET_STRING_MAXIMUM_LENGTH + i;
        }
        return i;
    }

    public static Date fromDosTime(ZipLong zipDosTime) {
        long dosTime = zipDosTime.getValue();
        return new Date(dosToJavaTime(dosTime));
    }

    public static long dosToJavaTime(long dosTime) {
        Calendar cal = Calendar.getInstance();
        cal.set(1, ((int) ((dosTime >> 25) & 127)) + 1980);
        cal.set(2, ((int) ((dosTime >> 21) & 15)) - 1);
        cal.set(5, ((int) (dosTime >> 16)) & 31);
        cal.set(11, ((int) (dosTime >> 11)) & 31);
        cal.set(12, ((int) (dosTime >> 5)) & 63);
        cal.set(13, ((int) (dosTime << 1)) & 62);
        cal.set(14, 0);
        return cal.getTime().getTime();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setNameAndCommentFromExtraFields(ZipEntry ze, byte[] originalNameBytes, byte[] commentBytes) {
        UnicodePathExtraField name = (UnicodePathExtraField) ze.getExtraField(UnicodePathExtraField.UPATH_ID);
        String originalName = ze.getName();
        String newName = getUnicodeStringIfOriginalMatches(name, originalNameBytes);
        if (newName != null && !originalName.equals(newName)) {
            ze.setName(newName);
        }
        if (commentBytes != null && commentBytes.length > 0) {
            UnicodeCommentExtraField cmt = (UnicodeCommentExtraField) ze.getExtraField(UnicodeCommentExtraField.UCOM_ID);
            String newComment = getUnicodeStringIfOriginalMatches(cmt, commentBytes);
            if (newComment != null) {
                ze.setComment(newComment);
            }
        }
    }

    private static String getUnicodeStringIfOriginalMatches(AbstractUnicodeExtraField f, byte[] orig) {
        if (f != null) {
            CRC32 crc32 = new CRC32();
            crc32.update(orig);
            long origCRC32 = crc32.getValue();
            if (origCRC32 == f.getNameCRC32()) {
                try {
                    return ZipEncodingHelper.UTF8_ZIP_ENCODING.decode(f.getUnicodeName());
                } catch (IOException e) {
                    return null;
                }
            }
            return null;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] copy(byte[] from) {
        if (from != null) {
            byte[] to = new byte[from.length];
            System.arraycopy(from, 0, to, 0, to.length);
            return to;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean canHandleEntryData(ZipEntry entry) {
        return supportsEncryptionOf(entry) && supportsMethodOf(entry);
    }

    private static boolean supportsEncryptionOf(ZipEntry entry) {
        return !entry.getGeneralPurposeBit().usesEncryption();
    }

    private static boolean supportsMethodOf(ZipEntry entry) {
        return entry.getMethod() == 0 || entry.getMethod() == 8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkRequestedFeatures(ZipEntry ze) throws UnsupportedZipFeatureException {
        if (!supportsEncryptionOf(ze)) {
            throw new UnsupportedZipFeatureException(UnsupportedZipFeatureException.Feature.ENCRYPTION, ze);
        }
        if (!supportsMethodOf(ze)) {
            throw new UnsupportedZipFeatureException(UnsupportedZipFeatureException.Feature.METHOD, ze);
        }
    }
}
