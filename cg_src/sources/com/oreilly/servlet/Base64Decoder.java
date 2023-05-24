package com.oreilly.servlet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
/* loaded from: gencallgraphv3.jar:cos.jar:com/oreilly/servlet/Base64Decoder.class */
public class Base64Decoder extends FilterInputStream {
    private static final char[] chars = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    private static final int[] ints = new int[128];
    private int charCount;
    private int carryOver;

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: JavaClassParseException: Unknown opcode: 0xa8 in method: com.oreilly.servlet.Base64Decoder.main(java.lang.String[]):void, file: gencallgraphv3.jar:cos.jar:com/oreilly/servlet/Base64Decoder.class
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
    public static void main(java.lang.String[] r0) throws java.lang.Exception {
        /*
        // Can't load method instructions: Load method exception: JavaClassParseException: Unknown opcode: 0xa8 in method: com.oreilly.servlet.Base64Decoder.main(java.lang.String[]):void, file: gencallgraphv3.jar:cos.jar:com/oreilly/servlet/Base64Decoder.class
        */
        throw new UnsupportedOperationException("Method not decompiled: com.oreilly.servlet.Base64Decoder.main(java.lang.String[]):void");
    }

    static {
        for (int i = 0; i < 64; i++) {
            ints[chars[i]] = i;
        }
    }

    public Base64Decoder(InputStream in) {
        super(in);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        int x;
        do {
            x = ((FilterInputStream) this).in.read();
            if (x == -1) {
                return -1;
            }
        } while (Character.isWhitespace((char) x));
        this.charCount++;
        if (x == 61) {
            return -1;
        }
        int x2 = ints[x];
        int mode = (this.charCount - 1) % 4;
        if (mode == 0) {
            this.carryOver = x2 & 63;
            return read();
        } else if (mode == 1) {
            int decoded = ((this.carryOver << 2) + (x2 >> 4)) & 255;
            this.carryOver = x2 & 15;
            return decoded;
        } else if (mode == 2) {
            int decoded2 = ((this.carryOver << 4) + (x2 >> 2)) & 255;
            this.carryOver = x2 & 3;
            return decoded2;
        } else if (mode == 3) {
            int decoded3 = ((this.carryOver << 6) + x2) & 255;
            return decoded3;
        } else {
            return -1;
        }
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] buf, int off, int len) throws IOException {
        if (buf.length < (len + off) - 1) {
            throw new IOException(new StringBuffer().append("The input buffer is too small: ").append(len).append(" bytes requested starting at offset ").append(off).append(" while the buffer ").append(" is only ").append(buf.length).append(" bytes long.").toString());
        }
        int i = 0;
        while (i < len) {
            int x = read();
            if (x == -1 && i == 0) {
                return -1;
            }
            if (x == -1) {
                break;
            }
            buf[off + i] = (byte) x;
            i++;
        }
        return i;
    }

    public static String decode(String encoded) {
        return new String(decodeToBytes(encoded));
    }

    public static byte[] decodeToBytes(String encoded) {
        byte[] bytes = null;
        try {
            bytes = encoded.getBytes("8859_1");
        } catch (UnsupportedEncodingException e) {
        }
        Base64Decoder in = new Base64Decoder(new ByteArrayInputStream(bytes));
        ByteArrayOutputStream out = new ByteArrayOutputStream((int) (bytes.length * 0.67d));
        try {
            byte[] buf = new byte[4096];
            while (true) {
                int read = in.read(buf);
                if (read != -1) {
                    out.write(buf, 0, read);
                } else {
                    out.close();
                    return out.toByteArray();
                }
            }
        } catch (IOException e2) {
            return null;
        }
    }
}
