package org.jf.dexlib2;

import java.util.HashMap;
import soot.coffi.Instruction;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/AccessFlags.class */
public enum AccessFlags {
    PUBLIC(1, Jimple.PUBLIC, true, true, true),
    PRIVATE(2, Jimple.PRIVATE, true, true, true),
    PROTECTED(4, Jimple.PROTECTED, true, true, true),
    STATIC(8, Jimple.STATIC, true, true, true),
    FINAL(16, Jimple.FINAL, true, true, true),
    SYNCHRONIZED(32, Jimple.SYNCHRONIZED, false, true, false),
    VOLATILE(64, Jimple.VOLATILE, false, false, true),
    BRIDGE(64, "bridge", false, true, false),
    TRANSIENT(128, Jimple.TRANSIENT, false, false, true),
    VARARGS(128, "varargs", false, true, false),
    NATIVE(256, Jimple.NATIVE, false, true, false),
    INTERFACE(512, "interface", true, false, false),
    ABSTRACT(1024, Jimple.ABSTRACT, true, true, false),
    STRICTFP(2048, Jimple.STRICTFP, false, true, false),
    SYNTHETIC(4096, "synthetic", true, true, true),
    ANNOTATION(8192, Jimple.ANNOTATION, true, false, false),
    ENUM(16384, "enum", true, false, true),
    CONSTRUCTOR(65536, "constructor", false, true, false),
    DECLARED_SYNCHRONIZED(131072, "declared-synchronized", false, true, false);
    
    private int value;
    private String accessFlagName;
    private boolean validForClass;
    private boolean validForMethod;
    private boolean validForField;
    private static final AccessFlags[] allFlags = values();
    private static HashMap<String, AccessFlags> accessFlagsByName = new HashMap<>();

    static {
        AccessFlags[] accessFlagsArr;
        for (AccessFlags accessFlag : allFlags) {
            accessFlagsByName.put(accessFlag.accessFlagName, accessFlag);
        }
    }

    AccessFlags(int value, String accessFlagName, boolean validForClass, boolean validForMethod, boolean validForField) {
        this.value = value;
        this.accessFlagName = accessFlagName;
        this.validForClass = validForClass;
        this.validForMethod = validForMethod;
        this.validForField = validForField;
    }

    public boolean isSet(int accessFlags) {
        return (this.value & accessFlags) != 0;
    }

    public static AccessFlags[] getAccessFlagsForClass(int accessFlagValue) {
        AccessFlags[] accessFlagsArr;
        AccessFlags[] accessFlagsArr2;
        int size = 0;
        for (AccessFlags accessFlag : allFlags) {
            if (accessFlag.validForClass && (accessFlagValue & accessFlag.value) != 0) {
                size++;
            }
        }
        AccessFlags[] accessFlags = new AccessFlags[size];
        int accessFlagsPosition = 0;
        for (AccessFlags accessFlag2 : allFlags) {
            if (accessFlag2.validForClass && (accessFlagValue & accessFlag2.value) != 0) {
                int i = accessFlagsPosition;
                accessFlagsPosition++;
                accessFlags[i] = accessFlag2;
            }
        }
        return accessFlags;
    }

    private static String formatAccessFlags(AccessFlags[] accessFlags) {
        int size = 0;
        for (AccessFlags accessFlag : accessFlags) {
            size += accessFlag.toString().length() + 1;
        }
        StringBuilder sb = new StringBuilder(size);
        for (AccessFlags accessFlag2 : accessFlags) {
            sb.append(accessFlag2.toString());
            sb.append(Instruction.argsep);
        }
        if (accessFlags.length > 0) {
            sb.delete(sb.length() - 1, sb.length());
        }
        return sb.toString();
    }

    public static String formatAccessFlagsForClass(int accessFlagValue) {
        return formatAccessFlags(getAccessFlagsForClass(accessFlagValue));
    }

    public static AccessFlags[] getAccessFlagsForMethod(int accessFlagValue) {
        AccessFlags[] accessFlagsArr;
        AccessFlags[] accessFlagsArr2;
        int size = 0;
        for (AccessFlags accessFlag : allFlags) {
            if (accessFlag.validForMethod && (accessFlagValue & accessFlag.value) != 0) {
                size++;
            }
        }
        AccessFlags[] accessFlags = new AccessFlags[size];
        int accessFlagsPosition = 0;
        for (AccessFlags accessFlag2 : allFlags) {
            if (accessFlag2.validForMethod && (accessFlagValue & accessFlag2.value) != 0) {
                int i = accessFlagsPosition;
                accessFlagsPosition++;
                accessFlags[i] = accessFlag2;
            }
        }
        return accessFlags;
    }

    public static String formatAccessFlagsForMethod(int accessFlagValue) {
        return formatAccessFlags(getAccessFlagsForMethod(accessFlagValue));
    }

    public static AccessFlags[] getAccessFlagsForField(int accessFlagValue) {
        AccessFlags[] accessFlagsArr;
        AccessFlags[] accessFlagsArr2;
        int size = 0;
        for (AccessFlags accessFlag : allFlags) {
            if (accessFlag.validForField && (accessFlagValue & accessFlag.value) != 0) {
                size++;
            }
        }
        AccessFlags[] accessFlags = new AccessFlags[size];
        int accessFlagsPosition = 0;
        for (AccessFlags accessFlag2 : allFlags) {
            if (accessFlag2.validForField && (accessFlagValue & accessFlag2.value) != 0) {
                int i = accessFlagsPosition;
                accessFlagsPosition++;
                accessFlags[i] = accessFlag2;
            }
        }
        return accessFlags;
    }

    public static String formatAccessFlagsForField(int accessFlagValue) {
        return formatAccessFlags(getAccessFlagsForField(accessFlagValue));
    }

    public static AccessFlags getAccessFlag(String accessFlag) {
        return accessFlagsByName.get(accessFlag);
    }

    public int getValue() {
        return this.value;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.accessFlagName;
    }
}
