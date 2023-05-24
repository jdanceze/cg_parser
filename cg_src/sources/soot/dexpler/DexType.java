package soot.dexpler;

import org.jf.dexlib2.iface.reference.TypeReference;
import org.jf.dexlib2.immutable.reference.ImmutableTypeReference;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.LongType;
import soot.RefType;
import soot.ShortType;
import soot.Type;
import soot.UnknownType;
import soot.VoidType;
/* loaded from: gencallgraphv3.jar:soot/dexpler/DexType.class */
public class DexType {
    protected String name;
    protected TypeReference type;

    public DexType(TypeReference type) {
        if (type == null) {
            throw new RuntimeException("error: type ref is null!");
        }
        this.type = type;
        this.name = type.getType();
    }

    public DexType(String type) {
        if (type == null) {
            throw new RuntimeException("error: type is null!");
        }
        this.type = new ImmutableTypeReference(type);
        this.name = type;
    }

    public String getName() {
        return this.name;
    }

    public boolean overwriteEquivalent(DexType field) {
        return this.name.equals(field.getName());
    }

    public TypeReference getType() {
        return this.type;
    }

    public Type toSoot() {
        return toSoot(this.type.getType(), 0);
    }

    public static Type toSoot(TypeReference type) {
        return toSoot(type.getType(), 0);
    }

    public static Type toSoot(String type) {
        return toSoot(type, 0);
    }

    public static boolean isWide(TypeReference typeReference) {
        String t = typeReference.getType();
        return isWide(t);
    }

    public static boolean isWide(String type) {
        return type.startsWith("J") || type.startsWith("D");
    }

    private static Type toSoot(String typeDescriptor, int pos) {
        Type type;
        char typeDesignator = typeDescriptor.charAt(pos);
        switch (typeDesignator) {
            case 'B':
                type = ByteType.v();
                break;
            case 'C':
                type = CharType.v();
                break;
            case 'D':
                type = DoubleType.v();
                break;
            case 'E':
            case 'G':
            case 'H':
            case 'K':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'T':
            case 'U':
            case 'W':
            case 'X':
            case 'Y':
            default:
                type = UnknownType.v();
                break;
            case 'F':
                type = FloatType.v();
                break;
            case 'I':
                type = IntType.v();
                break;
            case 'J':
                type = LongType.v();
                break;
            case 'L':
                type = RefType.v(Util.dottedClassName(typeDescriptor));
                break;
            case 'S':
                type = ShortType.v();
                break;
            case 'V':
                type = VoidType.v();
                break;
            case 'Z':
                type = BooleanType.v();
                break;
            case '[':
                type = toSoot(typeDescriptor, pos + 1).makeArrayType();
                break;
        }
        return type;
    }

    public static String toSootICAT(String type) {
        String r = "";
        String[] split1 = type.replace(".", "/").split(";");
        int length = split1.length;
        for (int i = 0; i < length; i++) {
            String s = split1[i];
            if (s.startsWith("L")) {
                s = s.replaceFirst("L", "");
            }
            if (s.startsWith("<L")) {
                s = s.replaceFirst("<L", "<");
            }
            r = String.valueOf(r) + s;
        }
        return r;
    }

    public static String toDalvikICAT(String type) {
        String type2 = ("L" + type.replaceAll("<", "<L").replaceAll(">", ">;")).replaceAll("L\\*;", "*");
        if (!type2.endsWith(";")) {
            type2 = String.valueOf(type2) + ";";
        }
        return type2;
    }

    public static String toSootAT(String type) {
        return type;
    }

    public String toString() {
        return this.name;
    }
}
