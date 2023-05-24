package javax.security.jacc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.security.Permission;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/security/jacc/EJBRoleRefPermission.class */
public final class EJBRoleRefPermission extends Permission implements Serializable {
    private final String actions;
    private transient int hashCodeValue;
    private static final long serialVersionUID = 1;
    private static final ObjectStreamField[] serialPersistentFields;
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
        objectStreamFieldArr[0] = new ObjectStreamField("actions", cls);
        serialPersistentFields = objectStreamFieldArr;
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public EJBRoleRefPermission(String name, String actions) {
        super(name);
        this.hashCodeValue = 0;
        this.actions = actions;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof EJBRoleRefPermission)) {
            return false;
        }
        EJBRoleRefPermission that = (EJBRoleRefPermission) o;
        if (getName().equals(that.getName())) {
            return this.actions.equals(that.actions);
        }
        return false;
    }

    @Override // java.security.Permission
    public String getActions() {
        return this.actions;
    }

    public int hashCode() {
        if (this.hashCodeValue == 0) {
            String hashInput = new String(new StringBuffer().append(getName()).append(Instruction.argsep).append(this.actions).toString());
            this.hashCodeValue = hashInput.hashCode();
        }
        return this.hashCodeValue;
    }

    @Override // java.security.Permission
    public boolean implies(Permission permission) {
        return equals(permission);
    }

    private synchronized void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
    }

    private synchronized void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
    }
}
