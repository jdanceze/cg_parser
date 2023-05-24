package org.apache.tools.ant.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/UUEncoder.class */
public class UUEncoder {
    protected static final int DEFAULT_MODE = 644;
    private static final int MAX_CHARS_PER_LINE = 45;
    private static final int INPUT_BUFFER_SIZE = 4500;
    private OutputStream out;
    private String name;

    public UUEncoder(String name) {
        this.name = name;
    }

    public void encode(InputStream is, OutputStream out) throws IOException {
        int i;
        this.out = out;
        encodeBegin();
        byte[] buffer = new byte[INPUT_BUFFER_SIZE];
        while (true) {
            int read = is.read(buffer, 0, buffer.length);
            int count = read;
            if (read != -1) {
                int pos = 0;
                while (count > 0) {
                    if (count > 45) {
                        i = 45;
                    } else {
                        i = count;
                    }
                    int num = i;
                    encodeLine(buffer, pos, num, out);
                    pos += num;
                    count -= num;
                }
            } else {
                out.flush();
                encodeEnd();
                return;
            }
        }
    }

    private void encodeString(String n) {
        PrintStream writer = new PrintStream(this.out);
        writer.print(n);
        writer.flush();
    }

    private void encodeBegin() {
        encodeString("begin 644 " + this.name + "\n");
    }

    private void encodeEnd() {
        encodeString(" \nend\n");
    }

    private void encodeLine(byte[] data, int offset, int length, OutputStream out) throws IOException {
        out.write((byte) ((length & 63) + 32));
        int i = 0;
        while (i < length) {
            byte b = 1;
            byte c = 1;
            int i2 = i;
            i++;
            byte a = data[offset + i2];
            if (i < length) {
                i++;
                b = data[offset + i];
                if (i < length) {
                    i++;
                    c = data[offset + i];
                }
            }
            byte d1 = (byte) (((a >>> 2) & 63) + 32);
            byte d2 = (byte) ((((a << 4) & 48) | ((b >>> 4) & 15)) + 32);
            byte d3 = (byte) ((((b << 2) & 60) | ((c >>> 6) & 3)) + 32);
            byte d4 = (byte) ((c & 63) + 32);
            out.write(d1);
            out.write(d2);
            out.write(d3);
            out.write(d4);
        }
        out.write(10);
    }
}
