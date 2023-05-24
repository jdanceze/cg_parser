package soot.dava.internal.javaRep;

import javax.resource.spi.work.WorkException;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.Type;
import soot.jimple.IntConstant;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/javaRep/DIntConstant.class */
public class DIntConstant extends IntConstant {
    public Type type;

    private DIntConstant(int value, Type type) {
        super(value);
        this.type = type;
    }

    public static DIntConstant v(int value, Type type) {
        return new DIntConstant(value, type);
    }

    @Override // soot.jimple.IntConstant
    public String toString() {
        String ch;
        if (this.type != null) {
            if (this.type instanceof BooleanType) {
                return this.value == 0 ? "false" : "true";
            } else if (this.type instanceof CharType) {
                switch (this.value) {
                    case 8:
                        ch = "\\b";
                        break;
                    case 9:
                        ch = "\\t";
                        break;
                    case 10:
                        ch = "\\n";
                        break;
                    case 12:
                        ch = "\\f";
                        break;
                    case 13:
                        ch = "\\r";
                        break;
                    case 34:
                        ch = "\\\"";
                        break;
                    case 39:
                        ch = "\\'";
                        break;
                    case 92:
                        ch = "\\\\";
                        break;
                    default:
                        if (this.value > 31 && this.value < 127) {
                            ch = new Character((char) this.value).toString();
                            break;
                        } else {
                            String hexString = Integer.toHexString(this.value);
                            while (true) {
                                String ch2 = hexString;
                                if (ch2.length() < 4) {
                                    hexString = WorkException.UNDEFINED + ch2;
                                } else {
                                    if (ch2.length() > 4) {
                                        ch2 = ch2.substring(ch2.length() - 4);
                                    }
                                    ch = "\\u" + ch2;
                                    break;
                                }
                            }
                        }
                        break;
                }
                return "'" + ch + "'";
            } else if (this.type instanceof ByteType) {
                return "(byte) " + Integer.toString(this.value);
            }
        }
        return Integer.toString(this.value);
    }
}
