package org.apache.tools.zip;

import android.widget.ExpandableListView;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/zip/ZipFile.class */
public class ZipFile implements Closeable {
    private static final int HASH_SIZE = 509;
    static final int NIBLET_MASK = 15;
    static final int BYTE_SHIFT = 8;
    private static final int POS_0 = 0;
    private static final int POS_1 = 1;
    private static final int POS_2 = 2;
    private static final int POS_3 = 3;
    private final List<ZipEntry> entries;
    private final Map<String, LinkedList<ZipEntry>> nameMap;
    private final String encoding;
    private final ZipEncoding zipEncoding;
    private final String archiveName;
    private final RandomAccessFile archive;
    private final boolean useUnicodeExtraFields;
    private volatile boolean closed;
    private final byte[] DWORD_BUF;
    private final byte[] WORD_BUF;
    private final byte[] CFH_BUF;
    private final byte[] SHORT_BUF;
    private static final int CFH_LEN = 42;
    private static final long CFH_SIG = ZipLong.getValue(ZipOutputStream.CFH_SIG);
    private static final int MIN_EOCD_SIZE = 22;
    private static final int MAX_EOCD_SIZE = 65557;
    private static final int CFD_LOCATOR_OFFSET = 16;
    private static final int ZIP64_EOCDL_LENGTH = 20;
    private static final int ZIP64_EOCDL_LOCATOR_OFFSET = 8;
    private static final int ZIP64_EOCD_CFD_LOCATOR_OFFSET = 48;
    private static final long LFH_OFFSET_FOR_FILENAME_LENGTH = 26;
    private final Comparator<ZipEntry> OFFSET_COMPARATOR;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/zip/ZipFile$OffsetEntry.class */
    public static final class OffsetEntry {
        private long headerOffset;
        private long dataOffset;

        private OffsetEntry() {
            this.headerOffset = -1L;
            this.dataOffset = -1L;
        }
    }

    public ZipFile(File f) throws IOException {
        this(f, (String) null);
    }

    public ZipFile(String name) throws IOException {
        this(new File(name), (String) null);
    }

    public ZipFile(String name, String encoding) throws IOException {
        this(new File(name), encoding, true);
    }

    public ZipFile(File f, String encoding) throws IOException {
        this(f, encoding, true);
    }

    public ZipFile(File f, String encoding, boolean useUnicodeExtraFields) throws IOException {
        this.entries = new LinkedList();
        this.nameMap = new HashMap((int) HASH_SIZE);
        this.DWORD_BUF = new byte[8];
        this.WORD_BUF = new byte[4];
        this.CFH_BUF = new byte[42];
        this.SHORT_BUF = new byte[2];
        this.OFFSET_COMPARATOR = e1, e2 -> {
            if (e1 == e2) {
                return 0;
            }
            Entry ent1 = e1 instanceof Entry ? (Entry) e1 : null;
            Entry ent2 = e2 instanceof Entry ? (Entry) e2 : null;
            if (ent1 == null) {
                return 1;
            }
            if (ent2 != null) {
                long val = ent1.getOffsetEntry().headerOffset - ent2.getOffsetEntry().headerOffset;
                if (val == 0) {
                    return 0;
                }
                return val < 0 ? -1 : 1;
            }
            return -1;
        };
        this.archiveName = f.getAbsolutePath();
        this.encoding = encoding;
        this.zipEncoding = ZipEncodingHelper.getZipEncoding(encoding);
        this.useUnicodeExtraFields = useUnicodeExtraFields;
        this.archive = new RandomAccessFile(f, "r");
        boolean success = false;
        try {
            Map<ZipEntry, NameAndComment> entriesWithoutUTF8Flag = populateFromCentralDirectory();
            resolveLocalFileHeaderData(entriesWithoutUTF8Flag);
            success = true;
            this.closed = 1 == 0;
            if (1 == 0) {
                try {
                    this.archive.close();
                } catch (IOException e) {
                }
            }
        } catch (Throwable th) {
            this.closed = !success;
            if (!success) {
                try {
                    this.archive.close();
                } catch (IOException e3) {
                }
            }
            throw th;
        }
    }

    public String getEncoding() {
        return this.encoding;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.closed = true;
        this.archive.close();
    }

    public static void closeQuietly(ZipFile zipfile) {
        if (zipfile != null) {
            try {
                zipfile.close();
            } catch (IOException e) {
            }
        }
    }

    public Enumeration<ZipEntry> getEntries() {
        return Collections.enumeration(this.entries);
    }

    public Enumeration<ZipEntry> getEntriesInPhysicalOrder() {
        return (Enumeration) this.entries.stream().sorted(this.OFFSET_COMPARATOR).collect(Collectors.collectingAndThen(Collectors.toList(), (v0) -> {
            return Collections.enumeration(v0);
        }));
    }

    public ZipEntry getEntry(String name) {
        LinkedList<ZipEntry> entriesOfThatName = this.nameMap.get(name);
        if (entriesOfThatName != null) {
            return entriesOfThatName.getFirst();
        }
        return null;
    }

    public Iterable<ZipEntry> getEntries(String name) {
        List<ZipEntry> entriesOfThatName = this.nameMap.get(name);
        return entriesOfThatName != null ? entriesOfThatName : Collections.emptyList();
    }

    public Iterable<ZipEntry> getEntriesInPhysicalOrder(String name) {
        if (this.nameMap.containsKey(name)) {
            return (Iterable) this.nameMap.get(name).stream().sorted(this.OFFSET_COMPARATOR).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public boolean canReadEntryData(ZipEntry ze) {
        return ZipUtil.canHandleEntryData(ze);
    }

    public InputStream getInputStream(ZipEntry ze) throws IOException, ZipException {
        if (!(ze instanceof Entry)) {
            return null;
        }
        OffsetEntry offsetEntry = ((Entry) ze).getOffsetEntry();
        ZipUtil.checkRequestedFeatures(ze);
        long start = offsetEntry.dataOffset;
        BoundedInputStream bis = new BoundedInputStream(start, ze.getCompressedSize());
        switch (ze.getMethod()) {
            case 0:
                return bis;
            case 8:
                bis.addDummy();
                final Inflater inflater = new Inflater(true);
                return new InflaterInputStream(bis, inflater) { // from class: org.apache.tools.zip.ZipFile.1
                    @Override // java.util.zip.InflaterInputStream, java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
                    public void close() throws IOException {
                        super.close();
                        inflater.end();
                    }
                };
            default:
                throw new ZipException("Found unsupported compression method " + ze.getMethod());
        }
    }

    public String getName() {
        return this.archiveName;
    }

    protected void finalize() throws Throwable {
        try {
            if (!this.closed) {
                System.err.printf("Cleaning up unclosed %s for archive %s%n", getClass().getSimpleName(), this.archiveName);
                close();
            }
        } finally {
            super.finalize();
        }
    }

    private Map<ZipEntry, NameAndComment> populateFromCentralDirectory() throws IOException {
        Map<ZipEntry, NameAndComment> noUTF8Flag = new HashMap<>();
        positionAtCentralDirectory();
        this.archive.readFully(this.WORD_BUF);
        long sig = ZipLong.getValue(this.WORD_BUF);
        if (sig != CFH_SIG && startsWithLocalFileHeader()) {
            throw new IOException("central directory is empty, can't expand corrupt archive.");
        }
        while (sig == CFH_SIG) {
            readCentralDirectoryEntry(noUTF8Flag);
            this.archive.readFully(this.WORD_BUF);
            sig = ZipLong.getValue(this.WORD_BUF);
        }
        return noUTF8Flag;
    }

    private void readCentralDirectoryEntry(Map<ZipEntry, NameAndComment> noUTF8Flag) throws IOException {
        this.archive.readFully(this.CFH_BUF);
        OffsetEntry offset = new OffsetEntry();
        Entry ze = new Entry(offset);
        int versionMadeBy = ZipShort.getValue(this.CFH_BUF, 0);
        ze.setPlatform((versionMadeBy >> 8) & 15);
        int off = 0 + 2 + 2;
        GeneralPurposeBit gpFlag = GeneralPurposeBit.parse(this.CFH_BUF, off);
        boolean hasUTF8Flag = gpFlag.usesUTF8ForNames();
        ZipEncoding entryEncoding = hasUTF8Flag ? ZipEncodingHelper.UTF8_ZIP_ENCODING : this.zipEncoding;
        ze.setGeneralPurposeBit(gpFlag);
        int off2 = off + 2;
        ze.setMethod(ZipShort.getValue(this.CFH_BUF, off2));
        int off3 = off2 + 2;
        long time = ZipUtil.dosToJavaTime(ZipLong.getValue(this.CFH_BUF, off3));
        ze.setTime(time);
        int off4 = off3 + 4;
        ze.setCrc(ZipLong.getValue(this.CFH_BUF, off4));
        int off5 = off4 + 4;
        ze.setCompressedSize(ZipLong.getValue(this.CFH_BUF, off5));
        int off6 = off5 + 4;
        ze.setSize(ZipLong.getValue(this.CFH_BUF, off6));
        int off7 = off6 + 4;
        int fileNameLen = ZipShort.getValue(this.CFH_BUF, off7);
        int off8 = off7 + 2;
        int extraLen = ZipShort.getValue(this.CFH_BUF, off8);
        int off9 = off8 + 2;
        int commentLen = ZipShort.getValue(this.CFH_BUF, off9);
        int off10 = off9 + 2;
        int diskStart = ZipShort.getValue(this.CFH_BUF, off10);
        int off11 = off10 + 2;
        ze.setInternalAttributes(ZipShort.getValue(this.CFH_BUF, off11));
        int off12 = off11 + 2;
        ze.setExternalAttributes(ZipLong.getValue(this.CFH_BUF, off12));
        int off13 = off12 + 4;
        if (this.archive.length() - this.archive.getFilePointer() < fileNameLen) {
            throw new EOFException();
        }
        byte[] fileName = new byte[fileNameLen];
        this.archive.readFully(fileName);
        ze.setName(entryEncoding.decode(fileName), fileName);
        offset.headerOffset = ZipLong.getValue(this.CFH_BUF, off13);
        this.entries.add(ze);
        if (this.archive.length() - this.archive.getFilePointer() < extraLen) {
            throw new EOFException();
        }
        byte[] cdExtraData = new byte[extraLen];
        this.archive.readFully(cdExtraData);
        ze.setCentralDirectoryExtra(cdExtraData);
        setSizesAndOffsetFromZip64Extra(ze, offset, diskStart);
        if (this.archive.length() - this.archive.getFilePointer() < commentLen) {
            throw new EOFException();
        }
        byte[] comment = new byte[commentLen];
        this.archive.readFully(comment);
        ze.setComment(entryEncoding.decode(comment));
        if (!hasUTF8Flag && this.useUnicodeExtraFields) {
            noUTF8Flag.put(ze, new NameAndComment(fileName, comment));
        }
    }

    private void setSizesAndOffsetFromZip64Extra(ZipEntry ze, OffsetEntry offset, int diskStart) throws IOException {
        Zip64ExtendedInformationExtraField z64 = (Zip64ExtendedInformationExtraField) ze.getExtraField(Zip64ExtendedInformationExtraField.HEADER_ID);
        if (z64 != null) {
            boolean hasUncompressedSize = ze.getSize() == ExpandableListView.PACKED_POSITION_VALUE_NULL;
            boolean hasCompressedSize = ze.getCompressedSize() == ExpandableListView.PACKED_POSITION_VALUE_NULL;
            boolean hasRelativeHeaderOffset = offset.headerOffset == ExpandableListView.PACKED_POSITION_VALUE_NULL;
            z64.reparseCentralDirectoryData(hasUncompressedSize, hasCompressedSize, hasRelativeHeaderOffset, diskStart == 65535);
            if (hasUncompressedSize) {
                ze.setSize(z64.getSize().getLongValue());
            } else if (hasCompressedSize) {
                z64.setSize(new ZipEightByteInteger(ze.getSize()));
            }
            if (hasCompressedSize) {
                ze.setCompressedSize(z64.getCompressedSize().getLongValue());
            } else if (hasUncompressedSize) {
                z64.setCompressedSize(new ZipEightByteInteger(ze.getCompressedSize()));
            }
            if (hasRelativeHeaderOffset) {
                offset.headerOffset = z64.getRelativeHeaderOffset().getLongValue();
            }
        }
    }

    private void positionAtCentralDirectory() throws IOException {
        positionAtEndOfCentralDirectoryRecord();
        boolean found = false;
        boolean searchedForZip64EOCD = this.archive.getFilePointer() > 20;
        if (searchedForZip64EOCD) {
            this.archive.seek(this.archive.getFilePointer() - 20);
            this.archive.readFully(this.WORD_BUF);
            found = Arrays.equals(ZipOutputStream.ZIP64_EOCD_LOC_SIG, this.WORD_BUF);
        }
        if (!found) {
            if (searchedForZip64EOCD) {
                skipBytes(16);
            }
            positionAtCentralDirectory32();
            return;
        }
        positionAtCentralDirectory64();
    }

    private void positionAtCentralDirectory64() throws IOException {
        skipBytes(4);
        this.archive.readFully(this.DWORD_BUF);
        this.archive.seek(ZipEightByteInteger.getLongValue(this.DWORD_BUF));
        this.archive.readFully(this.WORD_BUF);
        if (!Arrays.equals(this.WORD_BUF, ZipOutputStream.ZIP64_EOCD_SIG)) {
            throw new ZipException("archive's ZIP64 end of central directory locator is corrupt.");
        }
        skipBytes(44);
        this.archive.readFully(this.DWORD_BUF);
        this.archive.seek(ZipEightByteInteger.getLongValue(this.DWORD_BUF));
    }

    private void positionAtCentralDirectory32() throws IOException {
        skipBytes(16);
        this.archive.readFully(this.WORD_BUF);
        this.archive.seek(ZipLong.getValue(this.WORD_BUF));
    }

    private void positionAtEndOfCentralDirectoryRecord() throws IOException {
        boolean found = tryToLocateSignature(22L, 65557L, ZipOutputStream.EOCD_SIG);
        if (!found) {
            throw new ZipException("archive is not a ZIP archive");
        }
    }

    private boolean tryToLocateSignature(long minDistanceFromEnd, long maxDistanceFromEnd, byte[] sig) throws IOException {
        boolean found = false;
        long off = this.archive.length() - minDistanceFromEnd;
        long stopSearching = Math.max(0L, this.archive.length() - maxDistanceFromEnd);
        if (off >= 0) {
            while (true) {
                if (off < stopSearching) {
                    break;
                }
                this.archive.seek(off);
                int curr = this.archive.read();
                if (curr == -1) {
                    break;
                } else if (curr != sig[0] || this.archive.read() != sig[1] || this.archive.read() != sig[2] || this.archive.read() != sig[3]) {
                    off--;
                } else {
                    found = true;
                    break;
                }
            }
        }
        if (found) {
            this.archive.seek(off);
        }
        return found;
    }

    private void skipBytes(int count) throws IOException {
        int i = 0;
        while (true) {
            int totalSkipped = i;
            if (totalSkipped < count) {
                int skippedNow = this.archive.skipBytes(count - totalSkipped);
                if (skippedNow <= 0) {
                    throw new EOFException();
                }
                i = totalSkipped + skippedNow;
            } else {
                return;
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x00a7, code lost:
        if ((r6.archive.length() - r6.archive.getFilePointer()) >= r0) goto L17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x00b1, code lost:
        throw new java.io.EOFException();
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x00b2, code lost:
        r0 = new byte[r0];
        r6.archive.readFully(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x00c1, code lost:
        r0.setExtra(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x00cb, code lost:
        r18 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x00cd, code lost:
        r0 = new java.util.zip.ZipException("Invalid extra data in entry " + r0.getName());
        r0.initCause(r18);
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x00f8, code lost:
        throw r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x00f9, code lost:
        r0.dataOffset = ((((r0 + org.apache.tools.zip.ZipFile.LFH_OFFSET_FOR_FILENAME_LENGTH) + 2) + 2) + r0) + r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x011d, code lost:
        if (r7.containsKey(r0) == false) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0120, code lost:
        r0 = r7.get(r0);
        org.apache.tools.zip.ZipUtil.setNameAndCommentFromExtraFields(r0, r0.name, r0.comment);
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x013c, code lost:
        r0 = r0.getName();
        r0 = r6.nameMap.computeIfAbsent(r0, (v0) -> { // java.util.function.Function.apply(java.lang.Object):java.lang.Object
            return lambda$resolveLocalFileHeaderData$0(v0);
        });
        r0.addLast(r0);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void resolveLocalFileHeaderData(java.util.Map<org.apache.tools.zip.ZipEntry, org.apache.tools.zip.ZipFile.NameAndComment> r7) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 355
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.tools.zip.ZipFile.resolveLocalFileHeaderData(java.util.Map):void");
    }

    private boolean startsWithLocalFileHeader() throws IOException {
        this.archive.seek(0L);
        this.archive.readFully(this.WORD_BUF);
        return Arrays.equals(this.WORD_BUF, ZipOutputStream.LFH_SIG);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/zip/ZipFile$BoundedInputStream.class */
    public class BoundedInputStream extends InputStream {
        private long remaining;
        private long loc;
        private boolean addDummyByte = false;

        BoundedInputStream(long start, long remaining) {
            this.remaining = remaining;
            this.loc = start;
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            int read;
            long j = this.remaining;
            this.remaining = j - 1;
            if (j > 0) {
                synchronized (ZipFile.this.archive) {
                    RandomAccessFile randomAccessFile = ZipFile.this.archive;
                    long j2 = this.loc;
                    this.loc = j2 + 1;
                    randomAccessFile.seek(j2);
                    read = ZipFile.this.archive.read();
                }
                return read;
            } else if (this.addDummyByte) {
                this.addDummyByte = false;
                return 0;
            } else {
                return -1;
            }
        }

        @Override // java.io.InputStream
        public int read(byte[] b, int off, int len) throws IOException {
            int ret;
            if (this.remaining <= 0) {
                if (this.addDummyByte) {
                    this.addDummyByte = false;
                    b[off] = 0;
                    return 1;
                }
                return -1;
            } else if (len <= 0) {
                return 0;
            } else {
                if (len > this.remaining) {
                    len = (int) this.remaining;
                }
                synchronized (ZipFile.this.archive) {
                    ZipFile.this.archive.seek(this.loc);
                    ret = ZipFile.this.archive.read(b, off, len);
                }
                if (ret > 0) {
                    this.loc += ret;
                    this.remaining -= ret;
                }
                return ret;
            }
        }

        void addDummy() {
            this.addDummyByte = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/zip/ZipFile$NameAndComment.class */
    public static final class NameAndComment {
        private final byte[] name;
        private final byte[] comment;

        private NameAndComment(byte[] name, byte[] comment) {
            this.name = name;
            this.comment = comment;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/zip/ZipFile$Entry.class */
    public static class Entry extends ZipEntry {
        private final OffsetEntry offsetEntry;

        Entry(OffsetEntry offset) {
            this.offsetEntry = offset;
        }

        OffsetEntry getOffsetEntry() {
            return this.offsetEntry;
        }

        @Override // org.apache.tools.zip.ZipEntry, java.util.zip.ZipEntry
        public int hashCode() {
            return (3 * super.hashCode()) + ((int) (this.offsetEntry.headerOffset % 2147483647L));
        }

        @Override // org.apache.tools.zip.ZipEntry
        public boolean equals(Object other) {
            if (super.equals(other)) {
                Entry otherEntry = (Entry) other;
                return this.offsetEntry.headerOffset == otherEntry.offsetEntry.headerOffset && this.offsetEntry.dataOffset == otherEntry.offsetEntry.dataOffset;
            }
            return false;
        }
    }
}
