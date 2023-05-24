package javax.management;

import com.sun.jmx.mbeanserver.GetPropertyAction;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Date;
import java.util.EventObject;
import soot.JavaBasicTypes;
import soot.jimple.infoflow.rifl.RIFLConstants;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/Notification.class */
public class Notification extends EventObject {
    private static final long oldSerialVersionUID = 1716977971058914352L;
    private static final long newSerialVersionUID = -7516092053498031989L;
    private static final ObjectStreamField[] oldSerialPersistentFields;
    private static final ObjectStreamField[] newSerialPersistentFields;
    private static final long serialVersionUID;
    private static final ObjectStreamField[] serialPersistentFields;
    private static boolean compat;
    private String type;
    private long sequenceNumber;
    private long timeStamp;
    private Object userData;
    private String message;
    protected Object source;
    static Class class$java$lang$String;
    static Class class$java$lang$Object;
    static Class class$javax$management$ObjectName;

    static {
        Class cls;
        Class cls2;
        Class cls3;
        Class cls4;
        Class cls5;
        Class cls6;
        Class cls7;
        Class cls8;
        Class cls9;
        ObjectStreamField[] objectStreamFieldArr = new ObjectStreamField[7];
        if (class$java$lang$String == null) {
            cls = class$("java.lang.String");
            class$java$lang$String = cls;
        } else {
            cls = class$java$lang$String;
        }
        objectStreamFieldArr[0] = new ObjectStreamField("message", cls);
        objectStreamFieldArr[1] = new ObjectStreamField("sequenceNumber", Long.TYPE);
        if (class$java$lang$Object == null) {
            cls2 = class$(JavaBasicTypes.JAVA_LANG_OBJECT);
            class$java$lang$Object = cls2;
        } else {
            cls2 = class$java$lang$Object;
        }
        objectStreamFieldArr[2] = new ObjectStreamField(RIFLConstants.SOURCE_TAG, cls2);
        if (class$javax$management$ObjectName == null) {
            cls3 = class$("javax.management.ObjectName");
            class$javax$management$ObjectName = cls3;
        } else {
            cls3 = class$javax$management$ObjectName;
        }
        objectStreamFieldArr[3] = new ObjectStreamField("sourceObjectName", cls3);
        objectStreamFieldArr[4] = new ObjectStreamField("timeStamp", Long.TYPE);
        if (class$java$lang$String == null) {
            cls4 = class$("java.lang.String");
            class$java$lang$String = cls4;
        } else {
            cls4 = class$java$lang$String;
        }
        objectStreamFieldArr[5] = new ObjectStreamField("type", cls4);
        if (class$java$lang$Object == null) {
            cls5 = class$(JavaBasicTypes.JAVA_LANG_OBJECT);
            class$java$lang$Object = cls5;
        } else {
            cls5 = class$java$lang$Object;
        }
        objectStreamFieldArr[6] = new ObjectStreamField("userData", cls5);
        oldSerialPersistentFields = objectStreamFieldArr;
        ObjectStreamField[] objectStreamFieldArr2 = new ObjectStreamField[6];
        if (class$java$lang$String == null) {
            cls6 = class$("java.lang.String");
            class$java$lang$String = cls6;
        } else {
            cls6 = class$java$lang$String;
        }
        objectStreamFieldArr2[0] = new ObjectStreamField("message", cls6);
        objectStreamFieldArr2[1] = new ObjectStreamField("sequenceNumber", Long.TYPE);
        if (class$java$lang$Object == null) {
            cls7 = class$(JavaBasicTypes.JAVA_LANG_OBJECT);
            class$java$lang$Object = cls7;
        } else {
            cls7 = class$java$lang$Object;
        }
        objectStreamFieldArr2[2] = new ObjectStreamField(RIFLConstants.SOURCE_TAG, cls7);
        objectStreamFieldArr2[3] = new ObjectStreamField("timeStamp", Long.TYPE);
        if (class$java$lang$String == null) {
            cls8 = class$("java.lang.String");
            class$java$lang$String = cls8;
        } else {
            cls8 = class$java$lang$String;
        }
        objectStreamFieldArr2[4] = new ObjectStreamField("type", cls8);
        if (class$java$lang$Object == null) {
            cls9 = class$(JavaBasicTypes.JAVA_LANG_OBJECT);
            class$java$lang$Object = cls9;
        } else {
            cls9 = class$java$lang$Object;
        }
        objectStreamFieldArr2[5] = new ObjectStreamField("userData", cls9);
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

    public Notification(String str, Object obj, long j) {
        super(obj);
        this.userData = null;
        this.message = "";
        this.source = null;
        this.source = obj;
        this.type = str;
        this.sequenceNumber = j;
        this.timeStamp = new Date().getTime();
    }

    public Notification(String str, Object obj, long j, String str2) {
        super(obj);
        this.userData = null;
        this.message = "";
        this.source = null;
        this.source = obj;
        this.type = str;
        this.sequenceNumber = j;
        this.timeStamp = new Date().getTime();
        this.message = str2;
    }

    public Notification(String str, Object obj, long j, long j2) {
        super(obj);
        this.userData = null;
        this.message = "";
        this.source = null;
        this.source = obj;
        this.type = str;
        this.sequenceNumber = j;
        this.timeStamp = j2;
    }

    public Notification(String str, Object obj, long j, long j2, String str2) {
        super(obj);
        this.userData = null;
        this.message = "";
        this.source = null;
        this.source = obj;
        this.type = str;
        this.sequenceNumber = j;
        this.timeStamp = j2;
        this.message = str2;
    }

    public void setSource(Object obj) {
        ((EventObject) this).source = obj;
        this.source = obj;
    }

    public long getSequenceNumber() {
        return this.sequenceNumber;
    }

    public void setSequenceNumber(long j) {
        this.sequenceNumber = j;
    }

    public String getType() {
        return this.type;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(long j) {
        this.timeStamp = j;
    }

    public String getMessage() {
        return this.message;
    }

    public Object getUserData() {
        return this.userData;
    }

    public void setUserData(Object obj) {
        this.userData = obj;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        ((EventObject) this).source = this.source;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (compat) {
            ObjectOutputStream.PutField putFields = objectOutputStream.putFields();
            putFields.put("type", this.type);
            putFields.put("sequenceNumber", this.sequenceNumber);
            putFields.put("timeStamp", this.timeStamp);
            putFields.put("userData", this.userData);
            putFields.put("message", this.message);
            putFields.put(RIFLConstants.SOURCE_TAG, this.source);
            objectOutputStream.writeFields();
            return;
        }
        objectOutputStream.defaultWriteObject();
    }
}
