package soot.coffi;

import java.util.StringTokenizer;
import javax.resource.spi.work.WorkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Value;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/cp_info.class */
public abstract class cp_info {
    public static final byte CONSTANT_Utf8 = 1;
    public static final byte CONSTANT_Integer = 3;
    public static final byte CONSTANT_Float = 4;
    public static final byte CONSTANT_Long = 5;
    public static final byte CONSTANT_Double = 6;
    public static final byte CONSTANT_Class = 7;
    public static final byte CONSTANT_String = 8;
    public static final byte CONSTANT_Fieldref = 9;
    public static final byte CONSTANT_Methodref = 10;
    public static final byte CONSTANT_InterfaceMethodref = 11;
    public static final byte CONSTANT_NameAndType = 12;
    public static final byte CONSTANT_MethodHandle = 15;
    public static final byte CONSTANT_MethodType = 16;
    public static final byte CONSTANT_InvokeDynamic = 18;
    public static final byte REF_getField = 1;
    public static final byte REF_getStatic = 2;
    public static final byte REF_putField = 3;
    public static final byte REF_putStatic = 4;
    public static final byte REF_invokeVirtual = 5;
    public static final byte REF_invokeStatic = 6;
    public static final byte REF_invokeSpecial = 7;
    public static final byte REF_newInvokeSpecial = 8;
    public static final byte REF_invokeInterface = 9;
    public byte tag;
    private static final Logger logger = LoggerFactory.getLogger(cp_info.class);
    public static final byte[] REF_TO_CONSTANT = {-1, 9, 9, 9, 9, 10, 10, 10, 10, 11};

    public abstract int size();

    public abstract String toString(cp_info[] cp_infoVarArr);

    public abstract String typeName();

    public abstract int compareTo(cp_info[] cp_infoVarArr, cp_info cp_infoVar, cp_info[] cp_infoVarArr2);

    public abstract Value createJimpleConstantValue(cp_info[] cp_infoVarArr);

    public static long ints2long(long high, long low) {
        return (high << 32) + low;
    }

    public static String printBits(int i) {
        String str;
        String s = "";
        int k = 1;
        for (int j = 0; j < 32; j++) {
            if ((i & k) != 0) {
                str = WorkException.START_TIMED_OUT + s;
            } else {
                str = WorkException.UNDEFINED + s;
            }
            s = str;
            k <<= 1;
        }
        return s;
    }

    public static String printBits(long i) {
        String str;
        String s = "";
        long k = 1;
        long j = 0;
        while (true) {
            long j2 = j;
            if (j2 < 64) {
                if ((i & k) != 0) {
                    str = WorkException.START_TIMED_OUT + s;
                } else {
                    str = WorkException.UNDEFINED + s;
                }
                s = str;
                k <<= 1;
                j = j2 + 1;
            } else {
                return s;
            }
        }
    }

    public static String getClassname(cp_info[] constant_pool, int i) {
        cp_info c = constant_pool[i];
        switch (c.tag) {
            case 7:
                return c.toString(constant_pool);
            case 8:
            default:
                logger.debug("Request for classname for non-class object!");
                return "Can't find classname. Sorry.";
            case 9:
                return getClassname(constant_pool, ((CONSTANT_Fieldref_info) c).class_index);
            case 10:
                return getClassname(constant_pool, ((CONSTANT_Methodref_info) c).class_index);
            case 11:
                return getClassname(constant_pool, ((CONSTANT_InterfaceMethodref_info) c).class_index);
        }
    }

    public static String getName(cp_info[] constant_pool, int i) {
        cp_info c = constant_pool[i];
        switch (c.tag) {
            case 1:
                return c.toString(constant_pool);
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            default:
                logger.debug("Request for name for non-named object!");
                return "Can't find name of that object. Sorry.";
            case 9:
                return getName(constant_pool, ((CONSTANT_Fieldref_info) c).name_and_type_index);
            case 10:
                return getName(constant_pool, ((CONSTANT_Methodref_info) c).name_and_type_index);
            case 11:
                return getName(constant_pool, ((CONSTANT_InterfaceMethodref_info) c).name_and_type_index);
            case 12:
                return getName(constant_pool, ((CONSTANT_NameAndType_info) c).name_index);
        }
    }

    public static int countParams(cp_info[] constant_pool, int m) {
        String s = getTypeDescr(constant_pool, m);
        StringTokenizer st = new StringTokenizer(ClassFile.parseMethodDesc_params(s), ",", false);
        return st.countTokens();
    }

    public static String getTypeDescr(cp_info[] constant_pool, int i) {
        cp_info c = constant_pool[i];
        if (c instanceof CONSTANT_Utf8_info) {
            return c.toString(constant_pool);
        }
        if (c instanceof CONSTANT_NameAndType_info) {
            return getTypeDescr(constant_pool, ((CONSTANT_NameAndType_info) c).descriptor_index);
        }
        if (c instanceof CONSTANT_Methodref_info) {
            return getTypeDescr(constant_pool, ((CONSTANT_Methodref_info) c).name_and_type_index);
        }
        if (c instanceof CONSTANT_InterfaceMethodref_info) {
            return getTypeDescr(constant_pool, ((CONSTANT_InterfaceMethodref_info) c).name_and_type_index);
        }
        if (c instanceof CONSTANT_Fieldref_info) {
            return getTypeDescr(constant_pool, ((CONSTANT_Fieldref_info) c).name_and_type_index);
        }
        logger.debug("Invalid request for type descr!");
        return "Invalid type descriptor request.";
    }

    public static String fieldType(cp_info[] constant_pool, int i) {
        return ClassFile.parseDesc(getTypeDescr(constant_pool, i), "");
    }
}
