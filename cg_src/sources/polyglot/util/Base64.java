package polyglot.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/Base64.class */
public class Base64 {
    public static final int NO_OPTIONS = 0;
    public static final int ENCODE = 1;
    public static final int DECODE = 0;
    public static final int GZIP = 2;
    public static final int DONT_BREAK_LINES = 8;
    private static final int MAX_LINE_LENGTH = 76;
    private static final byte EQUALS_SIGN = 61;
    private static final byte NEW_LINE = 10;
    private static final String PREFERRED_ENCODING = "UTF-8";
    private static final byte[] ALPHABET;
    private static final byte[] _NATIVE_ALPHABET = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
    private static final byte[] DECODABET;
    private static final byte WHITE_SPACE_ENC = -5;
    private static final byte EQUALS_SIGN_ENC = -1;

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: JavaClassParseException: Unknown opcode: 0xa8 in method: polyglot.util.Base64.encodeObject(java.io.Serializable, int):java.lang.String, file: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/Base64.class
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:413)
        	at jadx.core.ProcessClass.process(ProcessClass.java:67)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:115)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:387)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:335)
        Caused by: jadx.plugins.input.java.utils.JavaClassParseException: Unknown opcode: 0xa8
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:71)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:48)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:148)
        	... 5 more
        */
    public static java.lang.String encodeObject(java.io.Serializable r0, int r1) {
        /*
        // Can't load method instructions: Load method exception: JavaClassParseException: Unknown opcode: 0xa8 in method: polyglot.util.Base64.encodeObject(java.io.Serializable, int):java.lang.String, file: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/Base64.class
        */
        throw new UnsupportedOperationException("Method not decompiled: polyglot.util.Base64.encodeObject(java.io.Serializable, int):java.lang.String");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: JavaClassParseException: Unknown opcode: 0xa8 in method: polyglot.util.Base64.encodeBytes(byte[], int, int, int):java.lang.String, file: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/Base64.class
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:413)
        	at jadx.core.ProcessClass.process(ProcessClass.java:67)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:115)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:387)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:335)
        Caused by: jadx.plugins.input.java.utils.JavaClassParseException: Unknown opcode: 0xa8
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:71)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:48)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:148)
        	... 5 more
        */
    public static java.lang.String encodeBytes(byte[] r0, int r1, int r2, int r3) {
        /*
        // Can't load method instructions: Load method exception: JavaClassParseException: Unknown opcode: 0xa8 in method: polyglot.util.Base64.encodeBytes(byte[], int, int, int):java.lang.String, file: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/Base64.class
        */
        throw new UnsupportedOperationException("Method not decompiled: polyglot.util.Base64.encodeBytes(byte[], int, int, int):java.lang.String");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: JavaClassParseException: Unknown opcode: 0xa8 in method: polyglot.util.Base64.decode(java.lang.String):byte[], file: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/Base64.class
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:413)
        	at jadx.core.ProcessClass.process(ProcessClass.java:67)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:115)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:387)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:335)
        Caused by: jadx.plugins.input.java.utils.JavaClassParseException: Unknown opcode: 0xa8
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:71)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:48)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:148)
        	... 5 more
        */
    public static byte[] decode(java.lang.String r0) {
        /*
        // Can't load method instructions: Load method exception: JavaClassParseException: Unknown opcode: 0xa8 in method: polyglot.util.Base64.decode(java.lang.String):byte[], file: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/Base64.class
        */
        throw new UnsupportedOperationException("Method not decompiled: polyglot.util.Base64.decode(java.lang.String):byte[]");
    }

    static {
        byte[] __bytes;
        try {
            __bytes = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            __bytes = _NATIVE_ALPHABET;
        }
        ALPHABET = __bytes;
        DECODABET = new byte[]{-9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, -9, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, -9, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9};
    }

    private Base64() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static byte[] encode3to4(byte[] b4, byte[] threeBytes, int numSigBytes) {
        encode3to4(threeBytes, 0, numSigBytes, b4, 0);
        return b4;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static byte[] encode3to4(byte[] source, int srcOffset, int numSigBytes, byte[] destination, int destOffset) {
        int inBuff = (numSigBytes > 0 ? (source[srcOffset] << 24) >>> 8 : 0) | (numSigBytes > 1 ? (source[srcOffset + 1] << 24) >>> 16 : 0) | (numSigBytes > 2 ? (source[srcOffset + 2] << 24) >>> 24 : 0);
        switch (numSigBytes) {
            case 1:
                destination[destOffset] = ALPHABET[inBuff >>> 18];
                destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 63];
                destination[destOffset + 2] = 61;
                destination[destOffset + 3] = 61;
                return destination;
            case 2:
                destination[destOffset] = ALPHABET[inBuff >>> 18];
                destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 63];
                destination[destOffset + 2] = ALPHABET[(inBuff >>> 6) & 63];
                destination[destOffset + 3] = 61;
                return destination;
            case 3:
                destination[destOffset] = ALPHABET[inBuff >>> 18];
                destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 63];
                destination[destOffset + 2] = ALPHABET[(inBuff >>> 6) & 63];
                destination[destOffset + 3] = ALPHABET[inBuff & 63];
                return destination;
            default:
                return destination;
        }
    }

    public static String encodeObject(Serializable serializableObject) {
        return encodeObject(serializableObject, 0);
    }

    public static String encodeBytes(byte[] source) {
        return encodeBytes(source, 0, source.length, 0);
    }

    public static String encodeBytes(byte[] source, int options) {
        return encodeBytes(source, 0, source.length, options);
    }

    public static String encodeBytes(byte[] source, int off, int len) {
        return encodeBytes(source, off, len, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int decode4to3(byte[] source, int srcOffset, byte[] destination, int destOffset) {
        if (source[srcOffset + 2] == 61) {
            destination[destOffset] = (byte) ((((DECODABET[source[srcOffset]] & 255) << 18) | ((DECODABET[source[srcOffset + 1]] & 255) << 12)) >>> 16);
            return 1;
        } else if (source[srcOffset + 3] == 61) {
            int outBuff = ((DECODABET[source[srcOffset]] & 255) << 18) | ((DECODABET[source[srcOffset + 1]] & 255) << 12) | ((DECODABET[source[srcOffset + 2]] & 255) << 6);
            destination[destOffset] = (byte) (outBuff >>> 16);
            destination[destOffset + 1] = (byte) (outBuff >>> 8);
            return 2;
        } else {
            try {
                int outBuff2 = ((DECODABET[source[srcOffset]] & 255) << 18) | ((DECODABET[source[srcOffset + 1]] & 255) << 12) | ((DECODABET[source[srcOffset + 2]] & 255) << 6) | (DECODABET[source[srcOffset + 3]] & 255);
                destination[destOffset] = (byte) (outBuff2 >> 16);
                destination[destOffset + 1] = (byte) (outBuff2 >> 8);
                destination[destOffset + 2] = (byte) outBuff2;
                return 3;
            } catch (Exception e) {
                System.out.println(new StringBuffer().append("").append((int) source[srcOffset]).append(": ").append((int) DECODABET[source[srcOffset]]).toString());
                System.out.println(new StringBuffer().append("").append((int) source[srcOffset + 1]).append(": ").append((int) DECODABET[source[srcOffset + 1]]).toString());
                System.out.println(new StringBuffer().append("").append((int) source[srcOffset + 2]).append(": ").append((int) DECODABET[source[srcOffset + 2]]).toString());
                System.out.println(new StringBuffer().append("").append((int) source[srcOffset + 3]).append(": ").append((int) DECODABET[source[srcOffset + 3]]).toString());
                return -1;
            }
        }
    }

    public static byte[] decode(byte[] source, int off, int len) {
        int len34 = (len * 3) / 4;
        byte[] outBuff = new byte[len34];
        int outBuffPosn = 0;
        byte[] b4 = new byte[4];
        int b4Posn = 0;
        for (int i = off; i < off + len; i++) {
            byte sbiCrop = (byte) (source[i] & Byte.MAX_VALUE);
            byte sbiDecode = DECODABET[sbiCrop];
            if (sbiDecode >= -5) {
                if (sbiDecode >= -1) {
                    int i2 = b4Posn;
                    b4Posn++;
                    b4[i2] = sbiCrop;
                    if (b4Posn > 3) {
                        outBuffPosn += decode4to3(b4, 0, outBuff, outBuffPosn);
                        b4Posn = 0;
                        if (sbiCrop == 61) {
                            break;
                        }
                    } else {
                        continue;
                    }
                }
            } else {
                System.err.println(new StringBuffer().append("Bad Base64 input character at ").append(i).append(": ").append((int) source[i]).append("(decimal)").toString());
                return null;
            }
        }
        byte[] out = new byte[outBuffPosn];
        System.arraycopy(outBuff, 0, out, 0, outBuffPosn);
        return out;
    }

    public static Object decodeToObject(String encodedObject) {
        Object obj;
        byte[] objBytes = decode(encodedObject);
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            try {
                bais = new ByteArrayInputStream(objBytes);
                ois = new ObjectInputStream(bais);
                obj = ois.readObject();
                try {
                    bais.close();
                } catch (Exception e) {
                }
                try {
                    ois.close();
                } catch (Exception e2) {
                }
            } catch (Throwable th) {
                try {
                    bais.close();
                } catch (Exception e3) {
                }
                try {
                    ois.close();
                } catch (Exception e4) {
                }
                throw th;
            }
        } catch (IOException e5) {
            e5.printStackTrace();
            obj = null;
            try {
                bais.close();
            } catch (Exception e6) {
            }
            try {
                ois.close();
            } catch (Exception e7) {
            }
        } catch (ClassNotFoundException e8) {
            e8.printStackTrace();
            obj = null;
            try {
                bais.close();
            } catch (Exception e9) {
            }
            try {
                ois.close();
            } catch (Exception e10) {
            }
        }
        return obj;
    }

    public static boolean encodeToFile(byte[] dataToEncode, String filename) {
        boolean success;
        OutputStream bos = null;
        try {
            bos = new OutputStream(new FileOutputStream(filename), 1);
            bos.write(dataToEncode);
            success = true;
            try {
                bos.close();
            } catch (Exception e) {
            }
        } catch (IOException e2) {
            success = false;
            try {
                bos.close();
            } catch (Exception e3) {
            }
        } catch (Throwable th) {
            try {
                bos.close();
            } catch (Exception e4) {
            }
            throw th;
        }
        return success;
    }

    public static boolean decodeToFile(String dataToDecode, String filename) {
        boolean success;
        OutputStream bos = null;
        try {
            bos = new OutputStream(new FileOutputStream(filename), 0);
            bos.write(dataToDecode.getBytes("UTF-8"));
            success = true;
            try {
                bos.close();
            } catch (Exception e) {
            }
        } catch (IOException e2) {
            success = false;
            try {
                bos.close();
            } catch (Exception e3) {
            }
        } catch (Throwable th) {
            try {
                bos.close();
            } catch (Exception e4) {
            }
            throw th;
        }
        return success;
    }

    public static byte[] decodeFromFile(String filename) {
        File file;
        int length;
        byte[] decodedData = null;
        InputStream bis = null;
        try {
            try {
                file = new File(filename);
                length = 0;
            } catch (IOException e) {
                System.err.println(new StringBuffer().append("Error decoding from file ").append(filename).toString());
                try {
                    bis.close();
                } catch (Exception e2) {
                }
            }
            if (file.length() > 2147483647L) {
                System.err.println(new StringBuffer().append("File is too big for this convenience method (").append(file.length()).append(" bytes).").toString());
                try {
                    bis.close();
                } catch (Exception e3) {
                }
                return null;
            }
            byte[] buffer = new byte[(int) file.length()];
            InputStream bis2 = new InputStream(new BufferedInputStream(new FileInputStream(file)), 0);
            while (true) {
                int numBytes = bis2.read(buffer, length, 4096);
                if (numBytes < 0) {
                    break;
                }
                length += numBytes;
            }
            decodedData = new byte[length];
            System.arraycopy(buffer, 0, decodedData, 0, length);
            try {
                bis2.close();
            } catch (Exception e4) {
            }
            return decodedData;
        } catch (Throwable th) {
            try {
                bis.close();
            } catch (Exception e5) {
            }
            throw th;
        }
    }

    public static String encodeFromFile(String filename) {
        String encodedData = null;
        InputStream bis = null;
        try {
            try {
                File file = new File(filename);
                byte[] buffer = new byte[(int) (file.length() * 1.4d)];
                int length = 0;
                bis = new InputStream(new BufferedInputStream(new FileInputStream(file)), 1);
                while (true) {
                    int numBytes = bis.read(buffer, length, 4096);
                    if (numBytes < 0) {
                        break;
                    }
                    length += numBytes;
                }
                encodedData = new String(buffer, 0, length, "UTF-8");
                try {
                    bis.close();
                } catch (Exception e) {
                }
            } catch (Throwable th) {
                try {
                    bis.close();
                } catch (Exception e2) {
                }
                throw th;
            }
        } catch (IOException e3) {
            System.err.println(new StringBuffer().append("Error encoding from file ").append(filename).toString());
            try {
                bis.close();
            } catch (Exception e4) {
            }
        }
        return encodedData;
    }

    public static char[] encode(byte[] data) {
        return encodeBytes(data).toCharArray();
    }

    public static byte[] decode(char[] data) {
        return decode(new String(data));
    }

    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/Base64$InputStream.class */
    public static class InputStream extends FilterInputStream {
        private boolean encode;
        private int position;
        private byte[] buffer;
        private int bufferLength;
        private int numSigBytes;
        private int lineLength;
        private boolean breakLines;

        public InputStream(java.io.InputStream in) {
            this(in, 0);
        }

        public InputStream(java.io.InputStream in, int options) {
            super(in);
            this.breakLines = (options & 8) != 8;
            this.encode = (options & 1) == 1;
            this.bufferLength = this.encode ? 4 : 3;
            this.buffer = new byte[this.bufferLength];
            this.position = -1;
            this.lineLength = 0;
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read() throws IOException {
            int b;
            if (this.position < 0) {
                if (this.encode) {
                    byte[] b3 = new byte[3];
                    int numBinaryBytes = 0;
                    for (int i = 0; i < 3; i++) {
                        try {
                            int b2 = this.in.read();
                            if (b2 >= 0) {
                                b3[i] = (byte) b2;
                                numBinaryBytes++;
                            }
                        } catch (IOException e) {
                            if (i == 0) {
                                throw e;
                            }
                        }
                    }
                    if (numBinaryBytes > 0) {
                        Base64.encode3to4(b3, 0, numBinaryBytes, this.buffer, 0);
                        this.position = 0;
                        this.numSigBytes = 4;
                    } else {
                        return -1;
                    }
                } else {
                    byte[] b4 = new byte[4];
                    int i2 = 0;
                    while (i2 < 4) {
                        do {
                            b = this.in.read();
                            if (b < 0) {
                                break;
                            }
                        } while (Base64.DECODABET[b & 127] <= -5);
                        if (b < 0) {
                            break;
                        }
                        b4[i2] = (byte) b;
                        i2++;
                    }
                    if (i2 == 4) {
                        this.numSigBytes = Base64.decode4to3(b4, 0, this.buffer, 0);
                        this.position = 0;
                    } else if (i2 == 0) {
                        return -1;
                    } else {
                        throw new IOException("Improperly padded Base64 input.");
                    }
                }
            }
            if (this.position >= 0) {
                if (this.position >= this.numSigBytes) {
                    return -1;
                }
                if (this.encode && this.breakLines && this.lineLength >= 76) {
                    this.lineLength = 0;
                    return 10;
                }
                this.lineLength++;
                byte[] bArr = this.buffer;
                int i3 = this.position;
                this.position = i3 + 1;
                byte b5 = bArr[i3];
                if (this.position >= this.bufferLength) {
                    this.position = -1;
                }
                return b5 & 255;
            }
            throw new IOException("Error in Base64 code reading stream.");
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read(byte[] dest, int off, int len) throws IOException {
            int i = 0;
            while (true) {
                if (i >= len) {
                    break;
                }
                int b = read();
                if (b >= 0) {
                    dest[off + i] = (byte) b;
                    i++;
                } else if (i == 0) {
                    return -1;
                }
            }
            return i;
        }
    }

    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/Base64$OutputStream.class */
    public static class OutputStream extends FilterOutputStream {
        private boolean encode;
        private int position;
        private byte[] buffer;
        private int bufferLength;
        private int lineLength;
        private boolean breakLines;
        private byte[] b4;
        private boolean suspendEncoding;

        public OutputStream(java.io.OutputStream out) {
            this(out, 1);
        }

        public OutputStream(java.io.OutputStream out, int options) {
            super(out);
            this.breakLines = (options & 8) != 8;
            this.encode = (options & 1) == 1;
            this.bufferLength = this.encode ? 3 : 4;
            this.buffer = new byte[this.bufferLength];
            this.position = 0;
            this.lineLength = 0;
            this.suspendEncoding = false;
            this.b4 = new byte[4];
        }

        @Override // java.io.FilterOutputStream, java.io.OutputStream
        public void write(int theByte) throws IOException {
            if (this.suspendEncoding) {
                ((FilterOutputStream) this).out.write(theByte);
            } else if (!this.encode) {
                if (Base64.DECODABET[theByte & 127] <= -5) {
                    if (Base64.DECODABET[theByte & 127] != -5) {
                        throw new IOException("Invalid character in Base64 data.");
                    }
                    return;
                }
                byte[] bArr = this.buffer;
                int i = this.position;
                this.position = i + 1;
                bArr[i] = (byte) theByte;
                if (this.position >= this.bufferLength) {
                    int len = Base64.decode4to3(this.buffer, 0, this.b4, 0);
                    this.out.write(this.b4, 0, len);
                    this.position = 0;
                }
            } else {
                byte[] bArr2 = this.buffer;
                int i2 = this.position;
                this.position = i2 + 1;
                bArr2[i2] = (byte) theByte;
                if (this.position >= this.bufferLength) {
                    this.out.write(Base64.encode3to4(this.b4, this.buffer, this.bufferLength));
                    this.lineLength += 4;
                    if (this.breakLines && this.lineLength >= 76) {
                        this.out.write(10);
                        this.lineLength = 0;
                    }
                    this.position = 0;
                }
            }
        }

        @Override // java.io.FilterOutputStream, java.io.OutputStream
        public void write(byte[] theBytes, int off, int len) throws IOException {
            if (this.suspendEncoding) {
                ((FilterOutputStream) this).out.write(theBytes, off, len);
                return;
            }
            for (int i = 0; i < len; i++) {
                write(theBytes[off + i]);
            }
        }

        public void flushBase64() throws IOException {
            if (this.position > 0) {
                if (this.encode) {
                    this.out.write(Base64.encode3to4(this.b4, this.buffer, this.position));
                    this.position = 0;
                    return;
                }
                throw new IOException("Base64 input not properly padded.");
            }
        }

        @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            flushBase64();
            super.close();
            this.buffer = null;
            this.out = null;
        }

        public void suspendEncoding() throws IOException {
            flushBase64();
            this.suspendEncoding = true;
        }

        public void resumeEncoding() {
            this.suspendEncoding = false;
        }
    }
}
