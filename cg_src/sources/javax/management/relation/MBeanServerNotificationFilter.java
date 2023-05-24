package javax.management.relation;

import com.sun.jmx.mbeanserver.GetPropertyAction;
import com.sun.jmx.trace.Trace;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;
import java.util.Vector;
import javax.management.MBeanServerNotification;
import javax.management.Notification;
import javax.management.NotificationFilterSupport;
import javax.management.ObjectName;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/relation/MBeanServerNotificationFilter.class */
public class MBeanServerNotificationFilter extends NotificationFilterSupport {
    private static final long oldSerialVersionUID = 6001782699077323605L;
    private static final long newSerialVersionUID = 2605900539589789736L;
    private static final ObjectStreamField[] oldSerialPersistentFields;
    private static final ObjectStreamField[] newSerialPersistentFields;
    private static final long serialVersionUID;
    private static final ObjectStreamField[] serialPersistentFields;
    private static boolean compat;
    private List selectedNames = new Vector();
    private List deselectedNames = null;
    private static String localClassName;
    static Class class$java$util$Vector;
    static Class class$java$util$List;

    static {
        Class cls;
        Class cls2;
        Class cls3;
        Class cls4;
        ObjectStreamField[] objectStreamFieldArr = new ObjectStreamField[2];
        if (class$java$util$Vector == null) {
            cls = class$("java.util.Vector");
            class$java$util$Vector = cls;
        } else {
            cls = class$java$util$Vector;
        }
        objectStreamFieldArr[0] = new ObjectStreamField("mySelectObjNameList", cls);
        if (class$java$util$Vector == null) {
            cls2 = class$("java.util.Vector");
            class$java$util$Vector = cls2;
        } else {
            cls2 = class$java$util$Vector;
        }
        objectStreamFieldArr[1] = new ObjectStreamField("myDeselectObjNameList", cls2);
        oldSerialPersistentFields = objectStreamFieldArr;
        ObjectStreamField[] objectStreamFieldArr2 = new ObjectStreamField[2];
        if (class$java$util$List == null) {
            cls3 = class$("java.util.List");
            class$java$util$List = cls3;
        } else {
            cls3 = class$java$util$List;
        }
        objectStreamFieldArr2[0] = new ObjectStreamField("selectedNames", cls3);
        if (class$java$util$List == null) {
            cls4 = class$("java.util.List");
            class$java$util$List = cls4;
        } else {
            cls4 = class$java$util$List;
        }
        objectStreamFieldArr2[1] = new ObjectStreamField("deselectedNames", cls4);
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
        } else {
            serialPersistentFields = newSerialPersistentFields;
            serialVersionUID = newSerialVersionUID;
        }
        localClassName = "MBeanServerNotificationFilter";
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    public MBeanServerNotificationFilter() {
        if (isTraceOn()) {
            trace("Constructor: entering", null);
        }
        enableType(MBeanServerNotification.REGISTRATION_NOTIFICATION);
        enableType(MBeanServerNotification.UNREGISTRATION_NOTIFICATION);
        if (isTraceOn()) {
            trace("Constructor: exiting", null);
        }
    }

    public synchronized void disableAllObjectNames() {
        if (isTraceOn()) {
            trace("disableAllObjectNames: entering", null);
        }
        this.selectedNames = new Vector();
        this.deselectedNames = null;
        if (isTraceOn()) {
            trace("disableAllObjectNames: exiting", null);
        }
    }

    public synchronized void disableObjectName(ObjectName objectName) throws IllegalArgumentException {
        if (objectName == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("disableObjectName: entering", objectName.toString());
        }
        if (this.selectedNames != null && this.selectedNames.size() != 0) {
            this.selectedNames.remove(objectName);
        }
        if (this.deselectedNames != null && !this.deselectedNames.contains(objectName)) {
            this.deselectedNames.add(objectName);
        }
        if (isTraceOn()) {
            trace("disableObjectName: exiting", null);
        }
    }

    public synchronized void enableAllObjectNames() {
        if (isTraceOn()) {
            trace("enableAllObjectNames: entering", null);
        }
        this.selectedNames = null;
        this.deselectedNames = new Vector();
        if (isTraceOn()) {
            trace("enableAllObjectNames: exiting", null);
        }
    }

    public synchronized void enableObjectName(ObjectName objectName) throws IllegalArgumentException {
        if (objectName == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("enableObjectName: entering", objectName.toString());
        }
        if (this.deselectedNames != null && this.deselectedNames.size() != 0) {
            this.deselectedNames.remove(objectName);
        }
        if (this.selectedNames != null && !this.selectedNames.contains(objectName)) {
            this.selectedNames.add(objectName);
        }
        if (isTraceOn()) {
            trace("enableObjectName: exiting", null);
        }
    }

    public synchronized Vector getEnabledObjectNames() {
        if (this.selectedNames != null) {
            return (Vector) ((Vector) this.selectedNames).clone();
        }
        return null;
    }

    public synchronized Vector getDisabledObjectNames() {
        if (this.deselectedNames != null) {
            return (Vector) ((Vector) this.deselectedNames).clone();
        }
        return null;
    }

    @Override // javax.management.NotificationFilterSupport, javax.management.NotificationFilter
    public synchronized boolean isNotificationEnabled(Notification notification) throws IllegalArgumentException {
        if (notification == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("isNotificationEnabled: entering", notification.toString());
        }
        if (!getEnabledTypes().contains(notification.getType())) {
            if (isTraceOn()) {
                trace("isNotificationEnabled: type not selected, exiting", null);
                return false;
            }
            return false;
        }
        ObjectName mBeanName = ((MBeanServerNotification) notification).getMBeanName();
        boolean z = false;
        if (this.selectedNames != null) {
            if (this.selectedNames.size() == 0) {
                if (isTraceOn()) {
                    trace("isNotificationEnabled: no ObjectNames selected, exiting", null);
                    return false;
                }
                return false;
            }
            z = this.selectedNames.contains(mBeanName);
            if (!z) {
                if (isTraceOn()) {
                    trace("isNotificationEnabled: ObjectName not in selected list, exiting", null);
                    return false;
                }
                return false;
            }
        }
        if (!z) {
            if (this.deselectedNames == null) {
                if (isTraceOn()) {
                    trace("isNotificationEnabled: ObjectName not selected and all deselectedm, exiting", null);
                    return false;
                }
                return false;
            } else if (this.deselectedNames.contains(mBeanName)) {
                if (isTraceOn()) {
                    trace("isNotificationEnabled: ObjectName explicitly not selected, exiting", null);
                    return false;
                }
                return false;
            }
        }
        if (isTraceOn()) {
            trace("isNotificationEnabled: ObjectName selected, exiting", null);
            return true;
        }
        return true;
    }

    private boolean isTraceOn() {
        return Trace.isSelected(1, 64);
    }

    private void trace(String str, String str2) {
        Trace.send(1, 64, localClassName, str, str2);
        Trace.send(1, 64, "", "", "\n");
    }

    private boolean isDebugOn() {
        return Trace.isSelected(2, 64);
    }

    private void debug(String str, String str2) {
        Trace.send(2, 64, localClassName, str, str2);
        Trace.send(2, 64, "", "", "\n");
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        if (compat) {
            ObjectInputStream.GetField readFields = objectInputStream.readFields();
            this.selectedNames = (List) readFields.get("mySelectObjNameList", (Object) null);
            if (readFields.defaulted("mySelectObjNameList")) {
                throw new NullPointerException("mySelectObjNameList");
            }
            this.deselectedNames = (List) readFields.get("myDeselectObjNameList", (Object) null);
            if (readFields.defaulted("myDeselectObjNameList")) {
                throw new NullPointerException("myDeselectObjNameList");
            }
            return;
        }
        objectInputStream.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (compat) {
            ObjectOutputStream.PutField putFields = objectOutputStream.putFields();
            putFields.put("mySelectObjNameList", (Vector) this.selectedNames);
            putFields.put("myDeselectObjNameList", (Vector) this.deselectedNames);
            objectOutputStream.writeFields();
            return;
        }
        objectOutputStream.defaultWriteObject();
    }
}
