package org.jf.dexlib2.util;

import java.util.Collection;
import java.util.Iterator;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.VerificationError;
import org.jf.dexlib2.iface.instruction.SwitchElement;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.iface.reference.Reference;
import org.jf.dexlib2.iface.reference.StringReference;
import org.jf.dexlib2.iface.reference.TypeReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/util/Preconditions.class */
public class Preconditions {
    public static void checkFormat(Opcode opcode, Format expectedFormat) {
        if (opcode.format != expectedFormat) {
            throw new IllegalArgumentException(String.format("Invalid opcode %s for %s", opcode.name, expectedFormat.name()));
        }
    }

    public static int checkNibbleRegister(int register) {
        if ((register & (-16)) != 0) {
            throw new IllegalArgumentException(String.format("Invalid register: v%d. Must be between v0 and v15, inclusive.", Integer.valueOf(register)));
        }
        return register;
    }

    public static int checkByteRegister(int register) {
        if ((register & (-256)) != 0) {
            throw new IllegalArgumentException(String.format("Invalid register: v%d. Must be between v0 and v255, inclusive.", Integer.valueOf(register)));
        }
        return register;
    }

    public static int checkShortRegister(int register) {
        if ((register & (-65536)) != 0) {
            throw new IllegalArgumentException(String.format("Invalid register: v%d. Must be between v0 and v65535, inclusive.", Integer.valueOf(register)));
        }
        return register;
    }

    public static int checkNibbleLiteral(int literal) {
        if (literal < -8 || literal > 7) {
            throw new IllegalArgumentException(String.format("Invalid literal value: %d. Must be between -8 and 7, inclusive.", Integer.valueOf(literal)));
        }
        return literal;
    }

    public static int checkByteLiteral(int literal) {
        if (literal < -128 || literal > 127) {
            throw new IllegalArgumentException(String.format("Invalid literal value: %d. Must be between -128 and 127, inclusive.", Integer.valueOf(literal)));
        }
        return literal;
    }

    public static int checkShortLiteral(int literal) {
        if (literal < -32768 || literal > 32767) {
            throw new IllegalArgumentException(String.format("Invalid literal value: %d. Must be between -32768 and 32767, inclusive.", Integer.valueOf(literal)));
        }
        return literal;
    }

    public static int checkIntegerHatLiteral(int literal) {
        if ((literal & 65535) != 0) {
            throw new IllegalArgumentException(String.format("Invalid literal value: %d. Low 16 bits must be zeroed out.", Integer.valueOf(literal)));
        }
        return literal;
    }

    public static long checkLongHatLiteral(long literal) {
        if ((literal & 281474976710655L) != 0) {
            throw new IllegalArgumentException(String.format("Invalid literal value: %d. Low 48 bits must be zeroed out.", Long.valueOf(literal)));
        }
        return literal;
    }

    public static int checkByteCodeOffset(int offset) {
        if (offset < -128 || offset > 127) {
            throw new IllegalArgumentException(String.format("Invalid code offset: %d. Must be between -128 and 127, inclusive.", Integer.valueOf(offset)));
        }
        return offset;
    }

    public static int checkShortCodeOffset(int offset) {
        if (offset < -32768 || offset > 32767) {
            throw new IllegalArgumentException(String.format("Invalid code offset: %d. Must be between -32768 and 32767, inclusive.", Integer.valueOf(offset)));
        }
        return offset;
    }

    public static int check35cAnd45ccRegisterCount(int registerCount) {
        if (registerCount < 0 || registerCount > 5) {
            throw new IllegalArgumentException(String.format("Invalid register count: %d. Must be between 0 and 5, inclusive.", Integer.valueOf(registerCount)));
        }
        return registerCount;
    }

    public static int checkRegisterRangeCount(int registerCount) {
        if ((registerCount & (-256)) != 0) {
            throw new IllegalArgumentException(String.format("Invalid register count: %d. Must be between 0 and 255, inclusive.", Integer.valueOf(registerCount)));
        }
        return registerCount;
    }

    public static void checkValueArg(int valueArg, int maxValue) {
        if (valueArg > maxValue) {
            if (maxValue == 0) {
                throw new IllegalArgumentException(String.format("Invalid value_arg value %d for an encoded_value. Expecting 0", Integer.valueOf(valueArg)));
            }
            throw new IllegalArgumentException(String.format("Invalid value_arg value %d for an encoded_value. Expecting 0..%d, inclusive", Integer.valueOf(valueArg), Integer.valueOf(maxValue)));
        }
    }

    public static int checkFieldOffset(int fieldOffset) {
        if (fieldOffset < 0 || fieldOffset > 65535) {
            throw new IllegalArgumentException(String.format("Invalid field offset: 0x%x. Must be between 0x0000 and 0xFFFF inclusive", Integer.valueOf(fieldOffset)));
        }
        return fieldOffset;
    }

    public static int checkVtableIndex(int vtableIndex) {
        if (vtableIndex < 0 || vtableIndex > 65535) {
            throw new IllegalArgumentException(String.format("Invalid vtable index: %d. Must be between 0 and 65535, inclusive", Integer.valueOf(vtableIndex)));
        }
        return vtableIndex;
    }

    public static int checkInlineIndex(int inlineIndex) {
        if (inlineIndex < 0 || inlineIndex > 65535) {
            throw new IllegalArgumentException(String.format("Invalid inline index: %d. Must be between 0 and 65535, inclusive", Integer.valueOf(inlineIndex)));
        }
        return inlineIndex;
    }

    public static int checkVerificationError(int verificationError) {
        if (!VerificationError.isValidVerificationError(verificationError)) {
            throw new IllegalArgumentException(String.format("Invalid verification error value: %d. Must be between 1 and 9, inclusive", Integer.valueOf(verificationError)));
        }
        return verificationError;
    }

    public static <C extends Collection<? extends SwitchElement>> C checkSequentialOrderedKeys(C elements) {
        Integer previousKey = null;
        Iterator it = elements.iterator();
        while (it.hasNext()) {
            SwitchElement element = (SwitchElement) it.next();
            int key = element.getKey();
            if (previousKey != null && previousKey.intValue() + 1 != key) {
                throw new IllegalArgumentException("SwitchElement set is not sequential and ordered");
            }
            previousKey = Integer.valueOf(key);
        }
        return elements;
    }

    public static int checkArrayPayloadElementWidth(int elementWidth) {
        switch (elementWidth) {
            case 1:
            case 2:
            case 4:
            case 8:
                return elementWidth;
            case 3:
            case 5:
            case 6:
            case 7:
            default:
                throw new IllegalArgumentException(String.format("Not a valid element width: %d", Integer.valueOf(elementWidth)));
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:5:0x0023  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static <L extends java.util.List<? extends java.lang.Number>> L checkArrayPayloadElements(int r9, L r10) {
        /*
            r0 = 1
            r1 = 8
            r2 = r9
            int r1 = r1 * r2
            r2 = 1
            int r1 = r1 - r2
            long r0 = r0 << r1
            r1 = 1
            long r0 = r0 - r1
            r11 = r0
            r0 = r11
            long r0 = -r0
            r1 = 1
            long r0 = r0 - r1
            r13 = r0
            r0 = r10
            java.util.Iterator r0 = r0.iterator()
            r15 = r0
        L19:
            r0 = r15
            boolean r0 = r0.hasNext()
            if (r0 == 0) goto L6a
            r0 = r15
            java.lang.Object r0 = r0.next()
            java.lang.Number r0 = (java.lang.Number) r0
            r16 = r0
            r0 = r16
            long r0 = r0.longValue()
            r1 = r13
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 < 0) goto L44
            r0 = r16
            long r0 = r0.longValue()
            r1 = r11
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 <= 0) goto L67
        L44:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            r1 = r0
            java.lang.String r2 = "%d does not fit into a %d-byte signed integer"
            r3 = 2
            java.lang.Object[] r3 = new java.lang.Object[r3]
            r4 = r3
            r5 = 0
            r6 = r16
            long r6 = r6.longValue()
            java.lang.Long r6 = java.lang.Long.valueOf(r6)
            r4[r5] = r6
            r4 = r3
            r5 = 1
            r6 = r9
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)
            r4[r5] = r6
            java.lang.String r2 = java.lang.String.format(r2, r3)
            r1.<init>(r2)
            throw r0
        L67:
            goto L19
        L6a:
            r0 = r10
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jf.dexlib2.util.Preconditions.checkArrayPayloadElements(int, java.util.List):java.util.List");
    }

    public static <T extends Reference> T checkReference(int referenceType, T reference) {
        switch (referenceType) {
            case 0:
                if (!(reference instanceof StringReference)) {
                    throw new IllegalArgumentException("Invalid reference type, expecting a string reference");
                }
                break;
            case 1:
                if (!(reference instanceof TypeReference)) {
                    throw new IllegalArgumentException("Invalid reference type, expecting a type reference");
                }
                break;
            case 2:
                if (!(reference instanceof FieldReference)) {
                    throw new IllegalArgumentException("Invalid reference type, expecting a field reference");
                }
                break;
            case 3:
                if (!(reference instanceof MethodReference)) {
                    throw new IllegalArgumentException("Invalid reference type, expecting a method reference");
                }
                break;
            default:
                throw new IllegalArgumentException(String.format("Not a valid reference type: %d", Integer.valueOf(referenceType)));
        }
        return reference;
    }
}
