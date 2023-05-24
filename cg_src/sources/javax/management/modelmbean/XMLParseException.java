package javax.management.modelmbean;

import com.sun.jmx.mbeanserver.GetPropertyAction;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.security.AccessController;
import java.security.PrivilegedAction;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/modelmbean/XMLParseException.class */
public class XMLParseException extends Exception {
    private static final long oldSerialVersionUID = -7780049316655891976L;
    private static final long newSerialVersionUID = 3176664577895105181L;
    private static final ObjectStreamField[] oldSerialPersistentFields;
    private static final ObjectStreamField[] newSerialPersistentFields;
    private static final long serialVersionUID;
    private static final ObjectStreamField[] serialPersistentFields;
    private static boolean compat;
    static Class class$java$lang$String;

    static {
        Class cls;
        ObjectStreamField[] objectStreamFieldArr = new ObjectStreamField[1];
        if (class$java$lang$String == null) {
            cls = class$("java.lang.String");
            class$java$lang$String = cls;
        } else {
            cls = class$java$lang$String;
        }
        objectStreamFieldArr[0] = new ObjectStreamField("msgStr", cls);
        oldSerialPersistentFields = objectStreamFieldArr;
        newSerialPersistentFields = new ObjectStreamField[0];
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

    public XMLParseException() {
        super("XML Parse Exception.");
    }

    public XMLParseException(String str) {
        super(new StringBuffer().append("XML Parse Exception: ").append(str).toString());
    }

    public XMLParseException(Exception exc, String str) {
        super(new StringBuffer().append("XML Parse Exception: ").append(str).append(":").append(exc.toString()).toString());
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (compat) {
            objectOutputStream.putFields().put("msgStr", getMessage());
            objectOutputStream.writeFields();
            return;
        }
        objectOutputStream.defaultWriteObject();
    }
}
