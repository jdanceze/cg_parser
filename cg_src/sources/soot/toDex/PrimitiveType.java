package soot.toDex;

import java.util.Locale;
/* loaded from: gencallgraphv3.jar:soot/toDex/PrimitiveType.class */
public enum PrimitiveType {
    BOOLEAN,
    BYTE,
    CHAR,
    SHORT,
    INT,
    LONG,
    FLOAT,
    DOUBLE;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static PrimitiveType[] valuesCustom() {
        PrimitiveType[] valuesCustom = values();
        int length = valuesCustom.length;
        PrimitiveType[] primitiveTypeArr = new PrimitiveType[length];
        System.arraycopy(valuesCustom, 0, primitiveTypeArr, 0, length);
        return primitiveTypeArr;
    }

    public String getName() {
        return name().toLowerCase(Locale.ENGLISH);
    }

    public static PrimitiveType getByName(String name) {
        PrimitiveType[] valuesCustom;
        for (PrimitiveType p : valuesCustom()) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        throw new RuntimeException("not found: " + name);
    }
}
