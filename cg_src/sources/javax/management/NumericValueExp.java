package javax.management;

import com.sun.jmx.mbeanserver.GetPropertyAction;
import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.security.AccessController;
import java.security.PrivilegedAction;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/NumericValueExp.class */
class NumericValueExp extends QueryEval implements ValueExp {
    private static final long oldSerialVersionUID = -6227876276058904000L;
    private static final long newSerialVersionUID = -4679739485102359104L;
    private static final ObjectStreamField[] oldSerialPersistentFields = {new ObjectStreamField("longVal", Long.TYPE), new ObjectStreamField("doubleVal", Double.TYPE), new ObjectStreamField("valIsLong", Boolean.TYPE)};
    private static final ObjectStreamField[] newSerialPersistentFields;
    private static final long serialVersionUID;
    private static final ObjectStreamField[] serialPersistentFields;
    private static boolean compat;
    private Number val;
    static Class class$java$lang$Number;

    static {
        Class cls;
        ObjectStreamField[] objectStreamFieldArr = new ObjectStreamField[1];
        if (class$java$lang$Number == null) {
            cls = class$("java.lang.Number");
            class$java$lang$Number = cls;
        } else {
            cls = class$java$lang$Number;
        }
        objectStreamFieldArr[0] = new ObjectStreamField("val", cls);
        newSerialPersistentFields = objectStreamFieldArr;
        compat = false;
        try {
            String str = (String) AccessController.doPrivileged((PrivilegedAction<Object>) new GetPropertyAction("jmx.serial.form"));
            compat = str != null && str.equals("1.0");
        } catch (Exception e) {
        }
        if (compat) {
            serialPersistentFields = oldSerialPersistentFields;
            serialVersionUID = oldSerialVersionUID;
            return;
        }
        serialPersistentFields = newSerialPersistentFields;
        serialVersionUID = newSerialVersionUID;
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    public NumericValueExp() {
        this.val = new Double((double) Const.default_value_double);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public NumericValueExp(Number number) {
        this.val = new Double((double) Const.default_value_double);
        this.val = number;
    }

    public double doubleValue() {
        if ((this.val instanceof Long) || (this.val instanceof Integer)) {
            return this.val.longValue();
        }
        return this.val.doubleValue();
    }

    public long longValue() {
        if ((this.val instanceof Long) || (this.val instanceof Integer)) {
            return this.val.longValue();
        }
        return (long) this.val.doubleValue();
    }

    public boolean isLong() {
        return (this.val instanceof Long) || (this.val instanceof Integer);
    }

    public String toString() {
        if ((this.val instanceof Long) || (this.val instanceof Integer)) {
            return String.valueOf(this.val.longValue());
        }
        return String.valueOf(this.val.doubleValue());
    }

    @Override // javax.management.ValueExp
    public ValueExp apply(ObjectName objectName) throws BadStringOperationException, BadBinaryOpValueExpException, BadAttributeValueExpException, InvalidApplicationException {
        return this;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        if (compat) {
            ObjectInputStream.GetField readFields = objectInputStream.readFields();
            double d = readFields.get("doubleVal", Const.default_value_double);
            if (readFields.defaulted("doubleVal")) {
                throw new NullPointerException("doubleVal");
            }
            long j = readFields.get("longVal", 0L);
            if (readFields.defaulted("longVal")) {
                throw new NullPointerException("longVal");
            }
            boolean z = readFields.get("valIsLong", false);
            if (readFields.defaulted("valIsLong")) {
                throw new NullPointerException("valIsLong");
            }
            if (z) {
                this.val = new Long(j);
                return;
            } else {
                this.val = new Double(d);
                return;
            }
        }
        objectInputStream.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (compat) {
            ObjectOutputStream.PutField putFields = objectOutputStream.putFields();
            putFields.put("doubleVal", doubleValue());
            putFields.put("longVal", longValue());
            putFields.put("valIsLong", isLong());
            objectOutputStream.writeFields();
            return;
        }
        objectOutputStream.defaultWriteObject();
    }
}
