package soot.util.backend;

import java.util.List;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ByteVector;
import org.objectweb.asm.ClassWriter;
import soot.ArrayType;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.LongType;
import soot.RefType;
import soot.ShortType;
import soot.SootClass;
import soot.SootField;
import soot.SootMethodRef;
import soot.Type;
import soot.TypeSwitch;
import soot.VoidType;
import soot.baf.DoubleWordType;
import soot.tagkit.DoubleConstantValueTag;
import soot.tagkit.FloatConstantValueTag;
import soot.tagkit.IntegerConstantValueTag;
import soot.tagkit.LongConstantValueTag;
import soot.tagkit.StringConstantValueTag;
import soot.tagkit.Tag;
/* loaded from: gencallgraphv3.jar:soot/util/backend/ASMBackendUtils.class */
public class ASMBackendUtils {
    public static String slashify(String s) {
        if (s == null) {
            return null;
        }
        return s.replace('.', '/');
    }

    public static String toTypeDesc(SootMethodRef m) {
        return toTypeDesc(m.parameterTypes(), m.returnType());
    }

    public static String toTypeDesc(List<Type> parameterTypes, Type returnType) {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        for (Type t : parameterTypes) {
            sb.append(toTypeDesc(t));
        }
        sb.append(')');
        sb.append(toTypeDesc(returnType));
        return sb.toString();
    }

    public static String toTypeDesc(Type type) {
        final StringBuilder sb = new StringBuilder(1);
        type.apply(new TypeSwitch() { // from class: soot.util.backend.ASMBackendUtils.1
            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void defaultCase(Type t) {
                throw new RuntimeException("Invalid type " + t.toString());
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseDoubleType(DoubleType t) {
                sb.append('D');
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseFloatType(FloatType t) {
                sb.append('F');
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseIntType(IntType t) {
                sb.append('I');
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseByteType(ByteType t) {
                sb.append('B');
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseShortType(ShortType t) {
                sb.append('S');
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseCharType(CharType t) {
                sb.append('C');
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseBooleanType(BooleanType t) {
                sb.append('Z');
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseLongType(LongType t) {
                sb.append('J');
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseArrayType(ArrayType t) {
                sb.append('[');
                t.getElementType().apply(this);
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseRefType(RefType t) {
                sb.append('L');
                sb.append(ASMBackendUtils.slashify(t.getClassName()));
                sb.append(';');
            }

            @Override // soot.TypeSwitch, soot.ITypeSwitch
            public void caseVoidType(VoidType t) {
                sb.append('V');
            }
        });
        return sb.toString();
    }

    public static Object getDefaultValue(SootField field) {
        for (Tag t : field.getTags()) {
            String name = t.getName();
            switch (name.hashCode()) {
                case -1381833090:
                    if (name.equals(StringConstantValueTag.NAME) && acceptsStringInitialValue(field)) {
                        return ((StringConstantValueTag) t).getStringValue();
                    }
                    break;
                case -890200343:
                    if (!name.equals(LongConstantValueTag.NAME)) {
                        break;
                    } else {
                        return Long.valueOf(((LongConstantValueTag) t).getLongValue());
                    }
                case 897661291:
                    if (!name.equals(IntegerConstantValueTag.NAME)) {
                        break;
                    } else {
                        return Integer.valueOf(((IntegerConstantValueTag) t).getIntValue());
                    }
                case 1312183945:
                    if (!name.equals(FloatConstantValueTag.NAME)) {
                        break;
                    } else {
                        return Float.valueOf(((FloatConstantValueTag) t).getFloatValue());
                    }
                case 2039841342:
                    if (!name.equals(DoubleConstantValueTag.NAME)) {
                        break;
                    } else {
                        return Double.valueOf(((DoubleConstantValueTag) t).getDoubleValue());
                    }
            }
        }
        return null;
    }

    public static boolean acceptsStringInitialValue(SootField field) {
        if (field.getType() instanceof RefType) {
            SootClass fieldClass = ((RefType) field.getType()).getSootClass();
            return fieldClass.getName().equals("java.lang.String");
        }
        return false;
    }

    public static int sizeOfType(Type t) {
        if ((t instanceof DoubleWordType) || (t instanceof LongType) || (t instanceof DoubleType)) {
            return 2;
        }
        if (t instanceof VoidType) {
            return 0;
        }
        return 1;
    }

    public static Attribute createASMAttribute(final soot.tagkit.Attribute attr) {
        return new Attribute(attr.getName()) { // from class: soot.util.backend.ASMBackendUtils.2
            @Override // org.objectweb.asm.Attribute
            protected ByteVector write(ClassWriter cw, byte[] code, int len, int maxStack, int maxLocals) {
                ByteVector result = new ByteVector();
                result.putByteArray(attr.getValue(), 0, attr.getValue().length);
                return result;
            }
        };
    }

    public static String translateJavaVersion(int javaVersion) {
        if (javaVersion == 1) {
            return "1.0";
        }
        return "1." + (javaVersion - 1);
    }
}
