package soot.jimple.infoflow.values;

import soot.SootMethod;
import soot.Value;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IntConstant;
import soot.jimple.LongConstant;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/values/SimpleConstantValueProvider.class */
public class SimpleConstantValueProvider implements IValueProvider {
    @Override // soot.jimple.infoflow.values.IValueProvider
    public Object getValue(SootMethod sm, Stmt stmt, Value value, Class type) {
        if (type == Integer.TYPE || type == Integer.class) {
            if (value instanceof IntConstant) {
                return Integer.valueOf(((IntConstant) value).value);
            }
            return null;
        } else if (type == Long.TYPE || type == Long.class) {
            if (value instanceof LongConstant) {
                return Long.valueOf(((LongConstant) value).value);
            }
            return null;
        } else if (type == Float.TYPE || type == Float.class) {
            if (value instanceof FloatConstant) {
                return Float.valueOf(((FloatConstant) value).value);
            }
            return null;
        } else if (type == Double.TYPE || type == Double.class) {
            if (value instanceof DoubleConstant) {
                return Double.valueOf(((DoubleConstant) value).value);
            }
            return null;
        } else if (type == String.class && (value instanceof StringConstant)) {
            return ((StringConstant) value).value;
        } else {
            return null;
        }
    }
}
