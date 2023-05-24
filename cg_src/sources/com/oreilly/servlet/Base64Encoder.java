package com.oreilly.servlet;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
/* loaded from: gencallgraphv3.jar:cos.jar:com/oreilly/servlet/Base64Encoder.class */
public class Base64Encoder extends FilterOutputStream {
    private static final char[] chars = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    private int charCount;
    private int carryOver;

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: JavaClassParseException: Unknown opcode: 0xa8 in method: com.oreilly.servlet.Base64Encoder.main(java.lang.String[]):void, file: gencallgraphv3.jar:cos.jar:com/oreilly/servlet/Base64Encoder.class
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
        // Can't load method instructions: Load method exception: JavaClassParseException: Unknown opcode: 0xa8 in method: com.oreilly.servlet.Base64Encoder.main(java.lang.String[]):void, file: gencallgraphv3.jar:cos.jar:com/oreilly/servlet/Base64Encoder.class
        */
        throw new UnsupportedOperationException("Method not decompiled: com.oreilly.servlet.Base64Encoder.main(java.lang.String[]):void");
    }

    public Base64Encoder(OutputStream out) {
        super(out);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(int b) throws IOException {
        if (b < 0) {
            b += 256;
        }
        if (this.charCount % 3 == 0) {
            int lookup = b >> 2;
            this.carryOver = b & 3;
            ((FilterOutputStream) this).out.write(chars[lookup]);
        } else if (this.charCount % 3 == 1) {
            int lookup2 = ((this.carryOver << 4) + (b >> 4)) & 63;
            this.carryOver = b & 15;
            ((FilterOutputStream) this).out.write(chars[lookup2]);
        } else if (this.charCount % 3 == 2) {
            int lookup3 = ((this.carryOver << 2) + (b >> 6)) & 63;
            ((FilterOutputStream) this).out.write(chars[lookup3]);
            int lookup4 = b & 63;
            ((FilterOutputStream) this).out.write(chars[lookup4]);
            this.carryOver = 0;
        }
        this.charCount++;
        if (this.charCount % 57 == 0) {
            ((FilterOutputStream) this).out.write(10);
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] buf, int off, int len) throws IOException {
        for (int i = 0; i < len; i++) {
            write(buf[off + i]);
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.charCount % 3 == 1) {
            int lookup = (this.carryOver << 4) & 63;
            ((FilterOutputStream) this).out.write(chars[lookup]);
            ((FilterOutputStream) this).out.write(61);
            ((FilterOutputStream) this).out.write(61);
        } else if (this.charCount % 3 == 2) {
            int lookup2 = (this.carryOver << 2) & 63;
            ((FilterOutputStream) this).out.write(chars[lookup2]);
            ((FilterOutputStream) this).out.write(61);
        }
        super.close();
    }

    public static String encode(String unencoded) {
        byte[] bytes = null;
        try {
            bytes = unencoded.getBytes("8859_1");
        } catch (UnsupportedEncodingException e) {
        }
        return encode(bytes);
    }

    public static String encode(byte[] bytes) {
        ByteArrayOutputStream out = new ByteArrayOutputStream((int) (bytes.length * 1.37d));
        Base64Encoder encodedOut = new Base64Encoder(out);
        try {
            encodedOut.write(bytes);
            encodedOut.close();
            return out.toString("8859_1");
        } catch (IOException e) {
            return null;
        }
    }
}
