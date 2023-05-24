package org.apache.tools.tar;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Locale;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.zip.ZipEncoding;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/tar/TarEntry.class */
public class TarEntry implements TarConstants {
    private String name;
    private int mode;
    private long userId;
    private long groupId;
    private long size;
    private long modTime;
    private byte linkFlag;
    private String linkName;
    private String magic;
    private String version;
    private String userName;
    private String groupName;
    private int devMajor;
    private int devMinor;
    private boolean isExtended;
    private long realSize;
    private File file;
    public static final int MAX_NAMELEN = 31;
    public static final int DEFAULT_DIR_MODE = 16877;
    public static final int DEFAULT_FILE_MODE = 33188;
    public static final int MILLIS_PER_SECOND = 1000;

    private TarEntry() {
        this.magic = TarConstants.MAGIC_POSIX;
        this.version = TarConstants.VERSION_POSIX;
        this.name = "";
        this.linkName = "";
        String user = System.getProperty("user.name", "");
        user = user.length() > 31 ? user.substring(0, 31) : user;
        this.userId = 0L;
        this.groupId = 0L;
        this.userName = user;
        this.groupName = "";
        this.file = null;
    }

    public TarEntry(String name) {
        this(name, false);
    }

    public TarEntry(String name, boolean preserveLeadingSlashes) {
        this();
        String name2 = normalizeFileName(name, preserveLeadingSlashes);
        boolean isDir = name2.endsWith("/");
        this.devMajor = 0;
        this.devMinor = 0;
        this.name = name2;
        this.mode = isDir ? 16877 : 33188;
        this.linkFlag = isDir ? (byte) 53 : (byte) 48;
        this.userId = 0L;
        this.groupId = 0L;
        this.size = 0L;
        this.modTime = new Date().getTime() / 1000;
        this.linkName = "";
        this.userName = "";
        this.groupName = "";
    }

    public TarEntry(String name, byte linkFlag) {
        this(name);
        this.linkFlag = linkFlag;
        if (linkFlag == 76) {
            this.magic = TarConstants.GNU_TMAGIC;
            this.version = TarConstants.VERSION_GNU_SPACE;
        }
    }

    public TarEntry(File file) {
        this(file, file.getPath());
    }

    public TarEntry(File file, String fileName) {
        this();
        String normalizedName = normalizeFileName(fileName, false);
        this.file = file;
        this.linkName = "";
        if (file.isDirectory()) {
            this.mode = 16877;
            this.linkFlag = (byte) 53;
            int nameLength = normalizedName.length();
            if (nameLength == 0 || normalizedName.charAt(nameLength - 1) != '/') {
                this.name = normalizedName + "/";
            } else {
                this.name = normalizedName;
            }
            this.size = 0L;
        } else {
            this.mode = 33188;
            this.linkFlag = (byte) 48;
            this.size = file.length();
            this.name = normalizedName;
        }
        this.modTime = file.lastModified() / 1000;
        this.devMajor = 0;
        this.devMinor = 0;
    }

    public TarEntry(byte[] headerBuf) {
        this();
        parseTarHeader(headerBuf);
    }

    public TarEntry(byte[] headerBuf, ZipEncoding encoding) throws IOException {
        this();
        parseTarHeader(headerBuf, encoding);
    }

    public boolean equals(TarEntry it) {
        return it != null && getName().equals(it.getName());
    }

    public boolean equals(Object it) {
        return it != null && getClass() == it.getClass() && equals((TarEntry) it);
    }

    public int hashCode() {
        return getName().hashCode();
    }

    public boolean isDescendent(TarEntry desc) {
        return desc.getName().startsWith(getName());
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = normalizeFileName(name, false);
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getLinkName() {
        return this.linkName;
    }

    public void setLinkName(String link) {
        this.linkName = link;
    }

    @Deprecated
    public int getUserId() {
        return (int) this.userId;
    }

    public void setUserId(int userId) {
        setUserId(userId);
    }

    public long getLongUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Deprecated
    public int getGroupId() {
        return (int) this.groupId;
    }

    public void setGroupId(int groupId) {
        setGroupId(groupId);
    }

    public long getLongGroupId() {
        return this.groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setIds(int userId, int groupId) {
        setUserId(userId);
        setGroupId(groupId);
    }

    public void setNames(String userName, String groupName) {
        setUserName(userName);
        setGroupName(groupName);
    }

    public void setModTime(long time) {
        this.modTime = time / 1000;
    }

    public void setModTime(Date time) {
        this.modTime = time.getTime() / 1000;
    }

    public Date getModTime() {
        return new Date(this.modTime * 1000);
    }

    public File getFile() {
        return this.file;
    }

    public int getMode() {
        return this.mode;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long size) {
        if (size < 0) {
            throw new IllegalArgumentException("Size is out of range: " + size);
        }
        this.size = size;
    }

    public int getDevMajor() {
        return this.devMajor;
    }

    public void setDevMajor(int devNo) {
        if (devNo < 0) {
            throw new IllegalArgumentException("Major device number is out of range: " + devNo);
        }
        this.devMajor = devNo;
    }

    public int getDevMinor() {
        return this.devMinor;
    }

    public void setDevMinor(int devNo) {
        if (devNo < 0) {
            throw new IllegalArgumentException("Minor device number is out of range: " + devNo);
        }
        this.devMinor = devNo;
    }

    public boolean isExtended() {
        return this.isExtended;
    }

    public long getRealSize() {
        return this.realSize;
    }

    public boolean isGNUSparse() {
        return this.linkFlag == 83;
    }

    public boolean isGNULongLinkEntry() {
        return this.linkFlag == 75;
    }

    public boolean isGNULongNameEntry() {
        return this.linkFlag == 76;
    }

    public boolean isPaxHeader() {
        return this.linkFlag == 120 || this.linkFlag == 88;
    }

    public boolean isGlobalPaxHeader() {
        return this.linkFlag == 103;
    }

    public boolean isDirectory() {
        if (this.file != null) {
            return this.file.isDirectory();
        }
        return this.linkFlag == 53 || getName().endsWith("/");
    }

    public boolean isFile() {
        return this.file != null ? this.file.isFile() : this.linkFlag == 0 || this.linkFlag == 48 || !getName().endsWith("/");
    }

    public boolean isSymbolicLink() {
        return this.linkFlag == 50;
    }

    public boolean isLink() {
        return this.linkFlag == 49;
    }

    public boolean isCharacterDevice() {
        return this.linkFlag == 51;
    }

    public boolean isBlockDevice() {
        return this.linkFlag == 52;
    }

    public boolean isFIFO() {
        return this.linkFlag == 54;
    }

    public TarEntry[] getDirectoryEntries() {
        if (this.file == null || !this.file.isDirectory()) {
            return new TarEntry[0];
        }
        String[] list = this.file.list();
        TarEntry[] result = new TarEntry[list.length];
        for (int i = 0; i < list.length; i++) {
            result[i] = new TarEntry(new File(this.file, list[i]));
        }
        return result;
    }

    public void writeEntryHeader(byte[] outbuf) {
        try {
            writeEntryHeader(outbuf, TarUtils.DEFAULT_ENCODING, false);
        } catch (IOException e) {
            try {
                writeEntryHeader(outbuf, TarUtils.FALLBACK_ENCODING, false);
            } catch (IOException ex2) {
                throw new RuntimeException(ex2);
            }
        }
    }

    public void writeEntryHeader(byte[] outbuf, ZipEncoding encoding, boolean starMode) throws IOException {
        int offset = writeEntryHeaderField(this.modTime, outbuf, writeEntryHeaderField(this.size, outbuf, writeEntryHeaderField(this.groupId, outbuf, writeEntryHeaderField(this.userId, outbuf, writeEntryHeaderField(this.mode, outbuf, TarUtils.formatNameBytes(this.name, outbuf, 0, 100, encoding), 8, starMode), 8, starMode), 8, starMode), 12, starMode), 12, starMode);
        for (int c = 0; c < 8; c++) {
            int i = offset;
            offset++;
            outbuf[i] = 32;
        }
        outbuf[offset] = this.linkFlag;
        int offset2 = writeEntryHeaderField(this.devMinor, outbuf, writeEntryHeaderField(this.devMajor, outbuf, TarUtils.formatNameBytes(this.groupName, outbuf, TarUtils.formatNameBytes(this.userName, outbuf, TarUtils.formatNameBytes(this.version, outbuf, TarUtils.formatNameBytes(this.magic, outbuf, TarUtils.formatNameBytes(this.linkName, outbuf, offset + 1, 100, encoding), 6), 2), 32, encoding), 32, encoding), 8, starMode), 8, starMode);
        while (offset2 < outbuf.length) {
            int i2 = offset2;
            offset2++;
            outbuf[i2] = 0;
        }
        long chk = TarUtils.computeCheckSum(outbuf);
        TarUtils.formatCheckSumOctalBytes(chk, outbuf, offset, 8);
    }

    private int writeEntryHeaderField(long value, byte[] outbuf, int offset, int length, boolean starMode) {
        if (!starMode && (value < 0 || value >= (1 << (3 * (length - 1))))) {
            return TarUtils.formatLongOctalBytes(0L, outbuf, offset, length);
        }
        return TarUtils.formatLongOctalOrBinaryBytes(value, outbuf, offset, length);
    }

    public void parseTarHeader(byte[] header) {
        try {
            parseTarHeader(header, TarUtils.DEFAULT_ENCODING);
        } catch (IOException e) {
            try {
                parseTarHeader(header, TarUtils.DEFAULT_ENCODING, true);
            } catch (IOException ex2) {
                throw new RuntimeException(ex2);
            }
        }
    }

    public void parseTarHeader(byte[] header, ZipEncoding encoding) throws IOException {
        parseTarHeader(header, encoding, false);
    }

    private void parseTarHeader(byte[] header, ZipEncoding encoding, boolean oldStyle) throws IOException {
        String parseName;
        int offset;
        int offset2;
        if (oldStyle) {
            parseName = TarUtils.parseName(header, 0, 100);
        } else {
            parseName = TarUtils.parseName(header, 0, 100, encoding);
        }
        this.name = parseName;
        int offset3 = 0 + 100;
        this.mode = (int) TarUtils.parseOctalOrBinary(header, offset3, 8);
        this.userId = (int) TarUtils.parseOctalOrBinary(header, offset, 8);
        this.groupId = (int) TarUtils.parseOctalOrBinary(header, offset2, 8);
        int offset4 = offset3 + 8 + 8 + 8;
        this.size = TarUtils.parseOctalOrBinary(header, offset4, 12);
        int offset5 = offset4 + 12;
        this.modTime = TarUtils.parseOctalOrBinary(header, offset5, 12);
        int offset6 = offset5 + 12 + 8;
        int offset7 = offset6 + 1;
        this.linkFlag = header[offset6];
        this.linkName = oldStyle ? TarUtils.parseName(header, offset7, 100) : TarUtils.parseName(header, offset7, 100, encoding);
        int offset8 = offset7 + 100;
        this.magic = TarUtils.parseName(header, offset8, 6);
        int offset9 = offset8 + 6;
        this.version = TarUtils.parseName(header, offset9, 2);
        int offset10 = offset9 + 2;
        this.userName = oldStyle ? TarUtils.parseName(header, offset10, 32) : TarUtils.parseName(header, offset10, 32, encoding);
        int offset11 = offset10 + 32;
        this.groupName = oldStyle ? TarUtils.parseName(header, offset11, 32) : TarUtils.parseName(header, offset11, 32, encoding);
        int offset12 = offset11 + 32;
        this.devMajor = (int) TarUtils.parseOctalOrBinary(header, offset12, 8);
        int offset13 = offset12 + 8;
        this.devMinor = (int) TarUtils.parseOctalOrBinary(header, offset13, 8);
        int offset14 = offset13 + 8;
        int type = evaluateType(header);
        switch (type) {
            case 2:
                int offset15 = offset14 + 12 + 12 + 12 + 4 + 1 + 96;
                this.isExtended = TarUtils.parseBoolean(header, offset15);
                int offset16 = offset15 + 1;
                this.realSize = TarUtils.parseOctal(header, offset16, 12);
                int i = offset16 + 12;
                return;
            case 3:
            default:
                String prefix = oldStyle ? TarUtils.parseName(header, offset14, 155) : TarUtils.parseName(header, offset14, 155, encoding);
                if (isDirectory() && !this.name.endsWith("/")) {
                    this.name += "/";
                }
                if (!prefix.isEmpty()) {
                    this.name = prefix + "/" + this.name;
                    return;
                }
                return;
        }
    }

    private static String normalizeFileName(String fileName, boolean preserveLeadingSlashes) {
        String fileName2;
        int colon;
        String osname = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        if (osname != null) {
            if (osname.startsWith(Os.FAMILY_WINDOWS)) {
                if (fileName.length() > 2) {
                    char ch1 = fileName.charAt(0);
                    char ch2 = fileName.charAt(1);
                    if (ch2 == ':' && ((ch1 >= 'a' && ch1 <= 'z') || (ch1 >= 'A' && ch1 <= 'Z'))) {
                        fileName = fileName.substring(2);
                    }
                }
            } else if (osname.contains(Os.FAMILY_NETWARE) && (colon = fileName.indexOf(58)) != -1) {
                fileName = fileName.substring(colon + 1);
            }
        }
        String replace = fileName.replace(File.separatorChar, '/');
        while (true) {
            fileName2 = replace;
            if (preserveLeadingSlashes || !fileName2.startsWith("/")) {
                break;
            }
            replace = fileName2.substring(1);
        }
        return fileName2;
    }

    private int evaluateType(byte[] header) {
        if (matchAsciiBuffer(TarConstants.GNU_TMAGIC, header, 257, 6)) {
            return 2;
        }
        if (matchAsciiBuffer(TarConstants.MAGIC_POSIX, header, 257, 6)) {
            return 3;
        }
        return 0;
    }

    private static boolean matchAsciiBuffer(String expected, byte[] buffer, int offset, int length) {
        byte[] buffer1 = expected.getBytes(StandardCharsets.US_ASCII);
        return isEqual(buffer1, 0, buffer1.length, buffer, offset, length, false);
    }

    private static boolean isEqual(byte[] buffer1, int offset1, int length1, byte[] buffer2, int offset2, int length2, boolean ignoreTrailingNulls) {
        int minLen = length1 < length2 ? length1 : length2;
        for (int i = 0; i < minLen; i++) {
            if (buffer1[offset1 + i] != buffer2[offset2 + i]) {
                return false;
            }
        }
        if (length1 == length2) {
            return true;
        }
        if (ignoreTrailingNulls) {
            if (length1 > length2) {
                for (int i2 = length2; i2 < length1; i2++) {
                    if (buffer1[offset1 + i2] != 0) {
                        return false;
                    }
                }
                return true;
            }
            for (int i3 = length1; i3 < length2; i3++) {
                if (buffer2[offset2 + i3] != 0) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
