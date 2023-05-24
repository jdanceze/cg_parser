package soot.dava.toolkits.base.AST.structuredAnalysis;

import soot.BooleanType;
import soot.Value;
import soot.dava.internal.javaRep.DIntConstant;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IntConstant;
import soot.jimple.LongConstant;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/structuredAnalysis/CPHelper.class */
public class CPHelper {
    public static Object wrapperClassCloner(Object value) {
        if (value instanceof Double) {
            return new Double(((Double) value).doubleValue());
        }
        if (value instanceof Float) {
            return new Float(((Float) value).floatValue());
        }
        if (value instanceof Long) {
            return new Long(((Long) value).longValue());
        }
        if (value instanceof Boolean) {
            return new Boolean(((Boolean) value).booleanValue());
        }
        if (value instanceof Integer) {
            return new Integer(((Integer) value).intValue());
        }
        return null;
    }

    public static Object isAConstantValue(Value toCheck) {
        Object value = null;
        if (toCheck instanceof LongConstant) {
            value = new Long(((LongConstant) toCheck).value);
        } else if (toCheck instanceof DoubleConstant) {
            value = new Double(((DoubleConstant) toCheck).value);
        } else if (toCheck instanceof FloatConstant) {
            value = new Float(((FloatConstant) toCheck).value);
        } else if (toCheck instanceof IntConstant) {
            int val = ((IntConstant) toCheck).value;
            value = new Integer(val);
        }
        return value;
    }

    public static Value createConstant(Object toConvert) {
        if (toConvert instanceof Long) {
            return LongConstant.v(((Long) toConvert).longValue());
        }
        if (toConvert instanceof Double) {
            return DoubleConstant.v(((Double) toConvert).doubleValue());
        }
        if (toConvert instanceof Boolean) {
            boolean val = ((Boolean) toConvert).booleanValue();
            if (val) {
                return DIntConstant.v(1, BooleanType.v());
            }
            return DIntConstant.v(0, BooleanType.v());
        } else if (toConvert instanceof Float) {
            return FloatConstant.v(((Float) toConvert).floatValue());
        } else {
            if (toConvert instanceof Integer) {
                return IntConstant.v(((Integer) toConvert).intValue());
            }
            return null;
        }
    }
}
