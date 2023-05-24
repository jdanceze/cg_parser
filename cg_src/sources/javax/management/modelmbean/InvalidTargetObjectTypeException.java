package javax.management.modelmbean;

import com.sun.jmx.mbeanserver.GetPropertyAction;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.security.AccessController;
import java.security.PrivilegedAction;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/modelmbean/InvalidTargetObjectTypeException.class */
public class InvalidTargetObjectTypeException extends Exception {
    private static final long oldSerialVersionUID = 3711724570458346634L;
    private static final long newSerialVersionUID = 1190536278266811217L;
    private static final ObjectStreamField[] oldSerialPersistentFields;
    private static final ObjectStreamField[] newSerialPersistentFields;
    private static final long serialVersionUID;
    private static final ObjectStreamField[] serialPersistentFields;
    private static boolean compat;
    Exception exception;
    static Class class$java$lang$String;
    static Class class$java$lang$Exception;

    static {
        Class cls;
        Class cls2;
        Class cls3;
        ObjectStreamField[] objectStreamFieldArr = new ObjectStreamField[2];
        if (class$java$lang$String == null) {
            cls = class$("java.lang.String");
            class$java$lang$String = cls;
        } else {
            cls = class$java$lang$String;
        }
        objectStreamFieldArr[0] = new ObjectStreamField("msgStr", cls);
        if (class$java$lang$Exception == null) {
            cls2 = class$("java.lang.Exception");
            class$java$lang$Exception = cls2;
        } else {
            cls2 = class$java$lang$Exception;
        }
        objectStreamFieldArr[1] = new ObjectStreamField("relatedExcept", cls2);
        oldSerialPersistentFields = objectStreamFieldArr;
        ObjectStreamField[] objectStreamFieldArr2 = new ObjectStreamField[1];
        if (class$java$lang$Exception == null) {
            cls3 = class$("java.lang.Exception");
            class$java$lang$Exception = cls3;
        } else {
            cls3 = class$java$lang$Exception;
        }
        objectStreamFieldArr2[0] = new ObjectStreamField("exception", cls3);
        newSerialPersistentFields = objectStreamFieldArr2;
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

    public InvalidTargetObjectTypeException() {
        super("InvalidTargetObjectTypeException: ");
        this.exception = null;
    }

    public InvalidTargetObjectTypeException(String str) {
        super(new StringBuffer().append("InvalidTargetObjectTypeException: ").append(str).toString());
        this.exception = null;
    }

    public InvalidTargetObjectTypeException(Exception exc, String str) {
        super(new StringBuffer().append("InvalidTargetObjectTypeException: ").append(str).append(exc != null ? new StringBuffer().append("\n\t triggered by:").append(exc.toString()).toString() : "").toString());
        this.exception = exc;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        if (compat) {
            ObjectInputStream.GetField readFields = objectInputStream.readFields();
            this.exception = (Exception) readFields.get("relatedExcept", (Object) null);
            if (readFields.defaulted("relatedExcept")) {
                throw new NullPointerException("relatedExcept");
            }
            return;
        }
        objectInputStream.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (compat) {
            ObjectOutputStream.PutField putFields = objectOutputStream.putFields();
            putFields.put("relatedExcept", this.exception);
            putFields.put("msgStr", this.exception != null ? this.exception.getMessage() : "");
            objectOutputStream.writeFields();
            return;
        }
        objectOutputStream.defaultWriteObject();
    }
}
