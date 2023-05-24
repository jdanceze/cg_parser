package jas;

import java.io.DataOutputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/Insn.class */
public class Insn implements RuntimeConstants {
    int opc;
    InsnOperand operand;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Insn() {
    }

    public Insn(int opc) throws jasError {
        if (opcLengths[opc] == 1) {
            this.operand = null;
            this.opc = opc;
            return;
        }
        throw new jasError(opcNames[opc] + " cannot be used without more parameters");
    }

    public Insn(int opc, int val) throws jasError {
        this.opc = opc;
        switch (opc) {
            case 16:
                this.operand = new ByteOperand(val);
                return;
            case 17:
                this.operand = new ShortOperand(val);
                return;
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 169:
                this.operand = new UnsignedByteWideOperand(val);
                return;
            case 188:
                this.operand = new UnsignedByteOperand(val);
                return;
            default:
                throw new jasError(opcNames[opc] + " does not take a numeric argument");
        }
    }

    public Insn(int opc, Label target) throws jasError {
        this.opc = opc;
        switch (opc) {
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164:
            case 165:
            case 166:
            case 167:
            case 168:
            case 198:
            case 199:
                this.operand = new LabelOperand(target, this);
                return;
            case 169:
            case 170:
            case 171:
            case 172:
            case 173:
            case 174:
            case 175:
            case 176:
            case 177:
            case 178:
            case 179:
            case 180:
            case 181:
            case 182:
            case 183:
            case 184:
            case 185:
            case 186:
            case 187:
            case 188:
            case 189:
            case 190:
            case 191:
            case 192:
            case 193:
            case 194:
            case 195:
            case 196:
            case 197:
            default:
                throw new jasError(opcNames[opc] + " does not take a label as its argument");
            case 200:
            case 201:
                this.operand = new LabelOperand(target, this, true);
                return;
        }
    }

    public Insn(int opc, CP arg) throws jasError {
        this.opc = opc;
        switch (opc) {
            case 18:
                this.operand = new LdcOperand(this, arg, false);
                return;
            case 19:
            case 20:
                this.operand = new LdcOperand(this, arg);
                return;
            case 178:
            case 179:
            case 180:
            case 181:
            case 182:
            case 183:
            case 184:
            case 187:
            case 189:
            case 192:
            case 193:
                this.operand = new CPOperand(arg);
                return;
            case 186:
                this.operand = new PaddedCPOperand(arg);
                return;
            default:
                throw new jasError(opcNames[opc] + " does not take a CP item as an argument");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resolve(ClassEnv e) {
        if (this.operand != null) {
            this.operand.resolve(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void write(ClassEnv e, CodeAttr ce, DataOutputStream out) throws IOException, jasError {
        if (this.operand != null) {
            this.operand.writePrefix(e, ce, out);
        }
        out.writeByte((byte) this.opc);
        if (this.operand != null) {
            this.operand.write(e, ce, out);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int size(ClassEnv e, CodeAttr ce) throws jasError {
        if (this.operand == null) {
            return 1;
        }
        return 1 + this.operand.size(e, ce);
    }
}
