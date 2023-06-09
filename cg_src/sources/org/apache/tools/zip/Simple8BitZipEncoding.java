package org.apache.tools.zip;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/zip/Simple8BitZipEncoding.class */
public class Simple8BitZipEncoding implements ZipEncoding {
    private final char[] highChars;
    private final List<Simple8BitChar> reverseMapping;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/zip/Simple8BitZipEncoding$Simple8BitChar.class */
    public static final class Simple8BitChar implements Comparable<Simple8BitChar> {
        public final char unicode;
        public final byte code;

        Simple8BitChar(byte code, char unicode) {
            this.code = code;
            this.unicode = unicode;
        }

        @Override // java.lang.Comparable
        public int compareTo(Simple8BitChar a) {
            return this.unicode - a.unicode;
        }

        public String toString() {
            return "0x" + Integer.toHexString(65535 & this.unicode) + "->0x" + Integer.toHexString(255 & this.code);
        }

        public boolean equals(Object o) {
            if (o instanceof Simple8BitChar) {
                Simple8BitChar other = (Simple8BitChar) o;
                return this.unicode == other.unicode && this.code == other.code;
            }
            return false;
        }

        public int hashCode() {
            return this.unicode;
        }
    }

    public Simple8BitZipEncoding(char[] highChars) {
        char[] cArr;
        this.highChars = (char[]) highChars.clone();
        List<Simple8BitChar> temp = new ArrayList<>(this.highChars.length);
        byte code = Byte.MAX_VALUE;
        for (char highChar : this.highChars) {
            code = (byte) (code + 1);
            temp.add(new Simple8BitChar(code, highChar));
        }
        Collections.sort(temp);
        this.reverseMapping = Collections.unmodifiableList(temp);
    }

    public char decodeByte(byte b) {
        if (b >= 0) {
            return (char) b;
        }
        return this.highChars[128 + b];
    }

    public boolean canEncodeChar(char c) {
        if (c >= 0 && c < 128) {
            return true;
        }
        Simple8BitChar r = encodeHighChar(c);
        return r != null;
    }

    public boolean pushEncodedChar(ByteBuffer bb, char c) {
        if (c >= 0 && c < 128) {
            bb.put((byte) c);
            return true;
        }
        Simple8BitChar r = encodeHighChar(c);
        if (r == null) {
            return false;
        }
        bb.put(r.code);
        return true;
    }

    private Simple8BitChar encodeHighChar(char c) {
        int i0 = 0;
        int i1 = this.reverseMapping.size();
        while (i1 > i0) {
            int i = i0 + ((i1 - i0) / 2);
            Simple8BitChar m = this.reverseMapping.get(i);
            if (m.unicode == c) {
                return m;
            }
            if (m.unicode < c) {
                i0 = i + 1;
            } else {
                i1 = i;
            }
        }
        if (i0 >= this.reverseMapping.size()) {
            return null;
        }
        Simple8BitChar r = this.reverseMapping.get(i0);
        if (r.unicode != c) {
            return null;
        }
        return r;
    }

    @Override // org.apache.tools.zip.ZipEncoding
    public boolean canEncode(String name) {
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (!canEncodeChar(c)) {
                return false;
            }
        }
        return true;
    }

    @Override // org.apache.tools.zip.ZipEncoding
    public ByteBuffer encode(String name) {
        ByteBuffer out = ByteBuffer.allocate(name.length() + 6 + ((name.length() + 1) / 2));
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (out.remaining() < 6) {
                out = ZipEncodingHelper.growBuffer(out, out.position() + 6);
            }
            if (!pushEncodedChar(out, c)) {
                ZipEncodingHelper.appendSurrogate(out, c);
            }
        }
        ZipEncodingHelper.prepareBufferForRead(out);
        return out;
    }

    @Override // org.apache.tools.zip.ZipEncoding
    public String decode(byte[] data) throws IOException {
        char[] ret = new char[data.length];
        for (int i = 0; i < data.length; i++) {
            ret[i] = decodeByte(data[i]);
        }
        return new String(ret);
    }
}
