package org.apache.tools.tar;

import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.cookie.ClientCookie;
import org.apache.tools.zip.ZipEncoding;
import org.apache.tools.zip.ZipEncodingHelper;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/tar/TarInputStream.class */
public class TarInputStream extends FilterInputStream {
    private static final int SMALL_BUFFER_SIZE = 256;
    private static final int BUFFER_SIZE = 8192;
    private static final int LARGE_BUFFER_SIZE = 32768;
    private static final int BYTE_MASK = 255;
    private final byte[] SKIP_BUF;
    private final byte[] SMALL_BUF;
    protected boolean debug;
    protected boolean hasHitEOF;
    protected long entrySize;
    protected long entryOffset;
    protected byte[] readBuf;
    protected TarBuffer buffer;
    protected TarEntry currEntry;
    protected byte[] oneBuf;
    private final ZipEncoding encoding;

    public TarInputStream(InputStream is) {
        this(is, 10240, 512);
    }

    public TarInputStream(InputStream is, String encoding) {
        this(is, 10240, 512, encoding);
    }

    public TarInputStream(InputStream is, int blockSize) {
        this(is, blockSize, 512);
    }

    public TarInputStream(InputStream is, int blockSize, String encoding) {
        this(is, blockSize, 512, encoding);
    }

    public TarInputStream(InputStream is, int blockSize, int recordSize) {
        this(is, blockSize, recordSize, null);
    }

    public TarInputStream(InputStream is, int blockSize, int recordSize, String encoding) {
        super(is);
        this.SKIP_BUF = new byte[8192];
        this.SMALL_BUF = new byte[256];
        this.buffer = new TarBuffer(is, blockSize, recordSize);
        this.readBuf = null;
        this.oneBuf = new byte[1];
        this.debug = false;
        this.hasHitEOF = false;
        this.encoding = ZipEncodingHelper.getZipEncoding(encoding);
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
        this.buffer.setDebug(debug);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.buffer.close();
    }

    public int getRecordSize() {
        return this.buffer.getRecordSize();
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int available() throws IOException {
        if (isDirectory()) {
            return 0;
        }
        if (this.entrySize - this.entryOffset > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int) (this.entrySize - this.entryOffset);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public long skip(long numToSkip) throws IOException {
        long skip;
        if (numToSkip <= 0 || isDirectory()) {
            return 0L;
        }
        long j = numToSkip;
        while (true) {
            skip = j;
            if (skip <= 0) {
                break;
            }
            int realSkip = (int) (skip > ((long) this.SKIP_BUF.length) ? this.SKIP_BUF.length : skip);
            int numRead = read(this.SKIP_BUF, 0, realSkip);
            if (numRead == -1) {
                break;
            }
            j = skip - numRead;
        }
        return numToSkip - skip;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public boolean markSupported() {
        return false;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public void mark(int markLimit) {
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public void reset() {
    }

    public TarEntry getNextEntry() throws IOException {
        if (this.hasHitEOF) {
            return null;
        }
        if (this.currEntry != null) {
            long numToSkip = this.entrySize - this.entryOffset;
            if (this.debug) {
                System.err.println("TarInputStream: SKIP currENTRY '" + this.currEntry.getName() + "' SZ " + this.entrySize + " OFF " + this.entryOffset + "  skipping " + numToSkip + " bytes");
            }
            while (numToSkip > 0) {
                long skipped = skip(numToSkip);
                if (skipped <= 0) {
                    throw new IOException("failed to skip current tar entry");
                }
                numToSkip -= skipped;
            }
            this.readBuf = null;
        }
        byte[] headerBuf = getRecord();
        if (this.hasHitEOF) {
            this.currEntry = null;
            return null;
        }
        try {
            this.currEntry = new TarEntry(headerBuf, this.encoding);
            if (this.debug) {
                System.err.println("TarInputStream: SET CURRENTRY '" + this.currEntry.getName() + "' size = " + this.currEntry.getSize());
            }
            this.entryOffset = 0L;
            this.entrySize = this.currEntry.getSize();
            if (this.currEntry.isGNULongLinkEntry()) {
                byte[] longLinkData = getLongNameData();
                if (longLinkData == null) {
                    return null;
                }
                this.currEntry.setLinkName(this.encoding.decode(longLinkData));
            }
            if (this.currEntry.isGNULongNameEntry()) {
                byte[] longNameData = getLongNameData();
                if (longNameData == null) {
                    return null;
                }
                this.currEntry.setName(this.encoding.decode(longNameData));
            }
            if (this.currEntry.isPaxHeader()) {
                paxHeaders();
            }
            if (this.currEntry.isGNUSparse()) {
                readGNUSparse();
            }
            this.entrySize = this.currEntry.getSize();
            return this.currEntry;
        } catch (IllegalArgumentException e) {
            throw new IOException("Error detected parsing the header", e);
        }
    }

    protected byte[] getLongNameData() throws IOException {
        ByteArrayOutputStream longName = new ByteArrayOutputStream();
        while (true) {
            int length = read(this.SMALL_BUF);
            if (length < 0) {
                break;
            }
            longName.write(this.SMALL_BUF, 0, length);
        }
        getNextEntry();
        if (this.currEntry == null) {
            return null;
        }
        byte[] longNameData = longName.toByteArray();
        int length2 = longNameData.length;
        while (length2 > 0 && longNameData[length2 - 1] == 0) {
            length2--;
        }
        if (length2 != longNameData.length) {
            byte[] l = new byte[length2];
            System.arraycopy(longNameData, 0, l, 0, length2);
            longNameData = l;
        }
        return longNameData;
    }

    private byte[] getRecord() throws IOException {
        if (this.hasHitEOF) {
            return null;
        }
        byte[] headerBuf = this.buffer.readRecord();
        if (headerBuf == null) {
            if (this.debug) {
                System.err.println("READ NULL RECORD");
            }
            this.hasHitEOF = true;
        } else if (this.buffer.isEOFRecord(headerBuf)) {
            if (this.debug) {
                System.err.println("READ EOF RECORD");
            }
            this.hasHitEOF = true;
        }
        if (this.hasHitEOF) {
            return null;
        }
        return headerBuf;
    }

    private void paxHeaders() throws IOException {
        Map<String, String> headers = parsePaxHeaders(this);
        getNextEntry();
        applyPaxHeadersToCurrentEntry(headers);
    }

    Map<String, String> parsePaxHeaders(InputStream i) throws IOException {
        int ch;
        Map<String, String> headers = new HashMap<>();
        do {
            int len = 0;
            int read = 0;
            while (true) {
                int read2 = i.read();
                ch = read2;
                if (read2 == -1) {
                    break;
                }
                read++;
                if (ch == 32) {
                    ByteArrayOutputStream coll = new ByteArrayOutputStream();
                    while (true) {
                        int read3 = i.read();
                        ch = read3;
                        if (read3 == -1) {
                            break;
                        }
                        read++;
                        if (ch == 61) {
                            String keyword = coll.toString("UTF-8");
                            int restLen = len - read;
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            int got = 0;
                            while (got < restLen) {
                                int read4 = i.read();
                                ch = read4;
                                if (read4 == -1) {
                                    break;
                                }
                                bos.write((byte) ch);
                                got++;
                            }
                            bos.close();
                            if (got != restLen) {
                                throw new IOException("Failed to read Paxheader. Expected " + restLen + " bytes, read " + got);
                            }
                            byte[] rest = bos.toByteArray();
                            String value = new String(rest, 0, restLen - 1, StandardCharsets.UTF_8);
                            headers.put(keyword, value);
                        } else {
                            coll.write((byte) ch);
                        }
                    }
                } else {
                    len = (len * 10) + (ch - 48);
                }
            }
        } while (ch != -1);
        return headers;
    }

    private void applyPaxHeadersToCurrentEntry(Map<String, String> headers) {
        headers.forEach(key, val -> {
            boolean z = true;
            switch (key.hashCode()) {
                case -1916861932:
                    if (key.equals("SCHILY.devmajor")) {
                        z = true;
                        break;
                    }
                    break;
                case -1916619760:
                    if (key.equals("SCHILY.devminor")) {
                        z = true;
                        break;
                    }
                    break;
                case 102338:
                    if (key.equals("gid")) {
                        z = true;
                        break;
                    }
                    break;
                case 115792:
                    if (key.equals("uid")) {
                        z = true;
                        break;
                    }
                    break;
                case 3433509:
                    if (key.equals(ClientCookie.PATH_ATTR)) {
                        z = false;
                        break;
                    }
                    break;
                case 3530753:
                    if (key.equals("size")) {
                        z = true;
                        break;
                    }
                    break;
                case 98496370:
                    if (key.equals("gname")) {
                        z = true;
                        break;
                    }
                    break;
                case 104223930:
                    if (key.equals("mtime")) {
                        z = true;
                        break;
                    }
                    break;
                case 111425664:
                    if (key.equals("uname")) {
                        z = true;
                        break;
                    }
                    break;
                case 1195018015:
                    if (key.equals("linkpath")) {
                        z = true;
                        break;
                    }
                    break;
            }
            switch (z) {
                case false:
                    this.currEntry.setName(val);
                    return;
                case true:
                    this.currEntry.setLinkName(val);
                    return;
                case true:
                    this.currEntry.setGroupId(Long.parseLong(val));
                    return;
                case true:
                    this.currEntry.setGroupName(val);
                    return;
                case true:
                    this.currEntry.setUserId(Long.parseLong(val));
                    return;
                case true:
                    this.currEntry.setUserName(val);
                    return;
                case true:
                    this.currEntry.setSize(Long.parseLong(val));
                    return;
                case true:
                    this.currEntry.setModTime((long) (Double.parseDouble(val) * 1000.0d));
                    return;
                case true:
                    this.currEntry.setDevMinor(Integer.parseInt(val));
                    return;
                case true:
                    this.currEntry.setDevMajor(Integer.parseInt(val));
                    return;
                default:
                    return;
            }
        });
    }

    private void readGNUSparse() throws IOException {
        TarArchiveSparseEntry entry;
        if (this.currEntry.isExtended()) {
            do {
                byte[] headerBuf = getRecord();
                if (this.hasHitEOF) {
                    this.currEntry = null;
                    return;
                }
                entry = new TarArchiveSparseEntry(headerBuf);
            } while (entry.isExtended());
        }
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        int num = read(this.oneBuf, 0, 1);
        if (num == -1) {
            return -1;
        }
        return this.oneBuf[0] & 255;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] buf, int offset, int numToRead) throws IOException {
        int totalRead = 0;
        if (this.entryOffset >= this.entrySize || isDirectory()) {
            return -1;
        }
        if (numToRead + this.entryOffset > this.entrySize) {
            numToRead = (int) (this.entrySize - this.entryOffset);
        }
        if (this.readBuf != null) {
            int sz = numToRead > this.readBuf.length ? this.readBuf.length : numToRead;
            System.arraycopy(this.readBuf, 0, buf, offset, sz);
            if (sz >= this.readBuf.length) {
                this.readBuf = null;
            } else {
                int newLen = this.readBuf.length - sz;
                byte[] newBuf = new byte[newLen];
                System.arraycopy(this.readBuf, sz, newBuf, 0, newLen);
                this.readBuf = newBuf;
            }
            totalRead = 0 + sz;
            numToRead -= sz;
            offset += sz;
        }
        while (numToRead > 0) {
            byte[] rec = this.buffer.readRecord();
            if (rec == null) {
                throw new IOException("unexpected EOF with " + numToRead + " bytes unread");
            }
            int sz2 = numToRead;
            int recLen = rec.length;
            if (recLen > sz2) {
                System.arraycopy(rec, 0, buf, offset, sz2);
                this.readBuf = new byte[recLen - sz2];
                System.arraycopy(rec, sz2, this.readBuf, 0, recLen - sz2);
            } else {
                sz2 = recLen;
                System.arraycopy(rec, 0, buf, offset, recLen);
            }
            totalRead += sz2;
            numToRead -= sz2;
            offset += sz2;
        }
        this.entryOffset += totalRead;
        return totalRead;
    }

    public void copyEntryContents(OutputStream out) throws IOException {
        byte[] buf = new byte[32768];
        while (true) {
            int numRead = read(buf, 0, buf.length);
            if (numRead != -1) {
                out.write(buf, 0, numRead);
            } else {
                return;
            }
        }
    }

    public boolean canReadEntryData(TarEntry te) {
        return !te.isGNUSparse();
    }

    private boolean isDirectory() {
        return this.currEntry != null && this.currEntry.isDirectory();
    }
}
