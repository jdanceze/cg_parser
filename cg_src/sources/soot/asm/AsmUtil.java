package soot.asm;

import com.google.common.base.Optional;
import soot.ArrayType;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.LongType;
import soot.ModuleRefType;
import soot.ModuleUtil;
import soot.RefLikeType;
import soot.RefType;
import soot.ShortType;
import soot.SootClass;
import soot.Type;
import soot.Unit;
import soot.jimple.AssignStmt;
/* loaded from: gencallgraphv3.jar:soot/asm/AsmUtil.class */
public class AsmUtil {
    private static RefType makeRefType(String className, Optional<String> moduleName) {
        if (ModuleUtil.module_mode()) {
            return ModuleRefType.v(className, moduleName);
        }
        return RefType.v(className);
    }

    public static boolean isDWord(Type type) {
        return (type instanceof LongType) || (type instanceof DoubleType);
    }

    public static Type toBaseType(String internal, Optional<String> moduleName) {
        if (internal.charAt(0) == '[') {
            internal = internal.substring(internal.lastIndexOf(91) + 1);
        }
        if (internal.charAt(internal.length() - 1) == ';') {
            String internal2 = internal.substring(0, internal.length() - 1);
            if (internal2.charAt(0) == 'L') {
                internal2 = internal2.substring(1);
            }
            return makeRefType(toQualifiedName(internal2), moduleName);
        }
        switch (internal.charAt(0)) {
            case 'B':
                return ByteType.v();
            case 'C':
                return CharType.v();
            case 'D':
                return DoubleType.v();
            case 'F':
                return FloatType.v();
            case 'I':
                return IntType.v();
            case 'J':
                return LongType.v();
            case 'S':
                return ShortType.v();
            case 'Z':
                return BooleanType.v();
            default:
                return makeRefType(toQualifiedName(internal), moduleName);
        }
    }

    public static String toQualifiedName(String internal) {
        return internal.replace('/', '.');
    }

    public static String toInternalName(String qual) {
        return qual.replace('.', '/');
    }

    public static String toInternalName(SootClass cls) {
        return toInternalName(cls.getName());
    }

    public static Type toJimpleRefType(String desc, Optional<String> moduleName) {
        return desc.charAt(0) == '[' ? toJimpleType(desc, moduleName) : makeRefType(toQualifiedName(desc), moduleName);
    }

    public static Type toJimpleType(String desc, Optional<String> moduleName) {
        Type baseType;
        int idx = desc.lastIndexOf(91);
        int nrDims = idx + 1;
        if (nrDims > 0) {
            if (desc.charAt(0) != '[') {
                throw new AssertionError("Invalid array descriptor: " + desc);
            }
            desc = desc.substring(idx + 1);
        }
        switch (desc.charAt(0)) {
            case 'B':
                baseType = ByteType.v();
                break;
            case 'C':
                baseType = CharType.v();
                break;
            case 'D':
                baseType = DoubleType.v();
                break;
            case 'F':
                baseType = FloatType.v();
                break;
            case 'I':
                baseType = IntType.v();
                break;
            case 'J':
                baseType = LongType.v();
                break;
            case 'L':
                if (desc.charAt(desc.length() - 1) != ';') {
                    throw new AssertionError("Invalid reference descriptor: " + desc);
                }
                String name = desc.substring(1, desc.length() - 1);
                baseType = makeRefType(toQualifiedName(name), moduleName);
                break;
            case 'S':
                baseType = ShortType.v();
                break;
            case 'Z':
                baseType = BooleanType.v();
                break;
            default:
                throw new AssertionError("Unknown descriptor: " + desc);
        }
        if ((baseType instanceof RefLikeType) || desc.length() <= 1) {
            return nrDims > 0 ? ArrayType.v(baseType, nrDims) : baseType;
        }
        throw new AssertionError("Invalid primitive type descriptor: " + desc);
    }

    /* JADX WARN: Code restructure failed: missing block: B:26:0x013b, code lost:
        if (r12 == null) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0140, code lost:
        if (r11 <= 0) goto L16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0143, code lost:
        r0.add(soot.ArrayType.v(r12, r11));
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0152, code lost:
        r0.add(r12);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.util.List<soot.Type> toJimpleDesc(java.lang.String r6, com.google.common.base.Optional<java.lang.String> r7) {
        /*
            Method dump skipped, instructions count: 353
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.asm.AsmUtil.toJimpleDesc(java.lang.String, com.google.common.base.Optional):java.util.List");
    }

    public static String baseTypeName(String s) {
        int index = s.indexOf("[");
        if (index < 0) {
            return s;
        }
        return s.substring(0, index);
    }

    private AsmUtil() {
    }

    public static int byteCodeToJavaVersion(int bytecodeVersion) {
        int javaVersion;
        switch (bytecodeVersion) {
            case 49:
                javaVersion = 6;
                break;
            case 50:
                javaVersion = 7;
                break;
            case 51:
                javaVersion = 8;
                break;
            case 52:
                javaVersion = 9;
                break;
            case 53:
                javaVersion = 10;
                break;
            case 54:
                javaVersion = 11;
                break;
            case 55:
                javaVersion = 12;
                break;
            case 56:
                javaVersion = 13;
                break;
            default:
                javaVersion = 1;
                break;
        }
        return javaVersion;
    }

    public static int javaToBytecodeVersion(int javaVersion) {
        int bytecodeVersion;
        switch (javaVersion) {
            case 2:
                bytecodeVersion = 196653;
                break;
            case 3:
                bytecodeVersion = 46;
                break;
            case 4:
                bytecodeVersion = 47;
                break;
            case 5:
                bytecodeVersion = 48;
                break;
            case 6:
                bytecodeVersion = 49;
                break;
            case 7:
                bytecodeVersion = 50;
                break;
            case 8:
                bytecodeVersion = 51;
                break;
            case 9:
                bytecodeVersion = 52;
                break;
            case 10:
                bytecodeVersion = 53;
                break;
            case 11:
                bytecodeVersion = 54;
                break;
            case 12:
                bytecodeVersion = 55;
                break;
            case 13:
                bytecodeVersion = 56;
                break;
            default:
                bytecodeVersion = 51;
                break;
        }
        return bytecodeVersion;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean alreadyExists(Unit prev, Object left, Object right) {
        if (prev instanceof AssignStmt) {
            AssignStmt prevAsign = (AssignStmt) prev;
            if (prevAsign.getLeftOp().equivTo(left) && prevAsign.getRightOp().equivTo(right)) {
                return true;
            }
            return false;
        }
        return false;
    }
}
