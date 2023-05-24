package soot.coffi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.G;
import soot.Value;
import soot.jimple.StringConstant;
/* loaded from: gencallgraphv3.jar:soot/coffi/CONSTANT_Utf8_info.class */
public class CONSTANT_Utf8_info extends cp_info {
    private static final Logger logger = LoggerFactory.getLogger(CONSTANT_Utf8_info.class);
    private int sHashCode;
    private String s;
    private final byte[] bytes;

    public CONSTANT_Utf8_info(DataInputStream d) throws IOException {
        int len = d.readUnsignedShort();
        this.bytes = new byte[len + 2];
        this.bytes[0] = (byte) (len >> 8);
        this.bytes[1] = (byte) (len & 255);
        if (len > 0) {
            for (int j = 0; j < len; j++) {
                this.bytes[j + 2] = (byte) d.readUnsignedByte();
            }
        }
    }

    public void writeBytes(DataOutputStream dd) throws IOException {
        int len = this.bytes.length;
        dd.writeShort(len - 2);
        dd.write(this.bytes, 2, len - 2);
    }

    public int length() {
        return ((this.bytes[0] & 255) << 8) + (this.bytes[1] & 255);
    }

    @Override // soot.coffi.cp_info
    public int size() {
        return length() + 3;
    }

    public String convert() {
        if (this.s == null) {
            try {
                ByteArrayInputStream bs = new ByteArrayInputStream(this.bytes);
                DataInputStream d = new DataInputStream(bs);
                String buf = d.readUTF();
                this.sHashCode = buf.hashCode();
                return buf;
            } catch (IOException e) {
                return "!!IOException!!";
            }
        }
        return this.s;
    }

    public void fixConversion(String rep) {
        if (this.sHashCode != rep.hashCode()) {
            throw new RuntimeException("bad use of fixConversion!");
        }
        if (this.s == null) {
            this.s = rep;
        }
    }

    public boolean equals(CONSTANT_Utf8_info cu) {
        int j = this.bytes.length;
        if (j != cu.bytes.length) {
            return false;
        }
        for (int i = 0; i < j; i++) {
            if (this.bytes[i] != cu.bytes[i]) {
                return false;
            }
        }
        return true;
    }

    @Override // soot.coffi.cp_info
    public int compareTo(cp_info[] constant_pool, cp_info cp, cp_info[] cp_constant_pool) {
        return compareTo(cp);
    }

    public int compareTo(cp_info cp) {
        if (this.tag != cp.tag) {
            return this.tag - cp.tag;
        }
        CONSTANT_Utf8_info cu = (CONSTANT_Utf8_info) cp;
        G.v().coffi_CONSTANT_Utf8_info_e1.reset(this.bytes);
        G.v().coffi_CONSTANT_Utf8_info_e2.reset(cu.bytes);
        while (G.v().coffi_CONSTANT_Utf8_info_e1.hasMoreElements() && G.v().coffi_CONSTANT_Utf8_info_e2.hasMoreElements()) {
            G.v().coffi_CONSTANT_Utf8_info_e1.nextElement();
            G.v().coffi_CONSTANT_Utf8_info_e2.nextElement();
            if (G.v().coffi_CONSTANT_Utf8_info_e1.c < G.v().coffi_CONSTANT_Utf8_info_e2.c) {
                return -1;
            }
            if (G.v().coffi_CONSTANT_Utf8_info_e2.c < G.v().coffi_CONSTANT_Utf8_info_e1.c) {
                return 1;
            }
        }
        if (G.v().coffi_CONSTANT_Utf8_info_e1.hasMoreElements()) {
            return -1;
        }
        if (G.v().coffi_CONSTANT_Utf8_info_e2.hasMoreElements()) {
            return 1;
        }
        return 0;
    }

    public static byte[] toUtf8(String s) {
        try {
            ByteArrayOutputStream bs = new ByteArrayOutputStream(s.length());
            DataOutputStream d = new DataOutputStream(bs);
            d.writeUTF(s);
            return bs.toByteArray();
        } catch (IOException e) {
            logger.debug("Some sort of IO exception in toUtf8 with " + s);
            return null;
        }
    }

    @Override // soot.coffi.cp_info
    public String toString(cp_info[] constant_pool) {
        return convert();
    }

    @Override // soot.coffi.cp_info
    public String typeName() {
        return "utf8";
    }

    @Override // soot.coffi.cp_info
    public Value createJimpleConstantValue(cp_info[] constant_pool) {
        return StringConstant.v(convert());
    }
}
