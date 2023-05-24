package javax.security.jacc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.security.Permission;
import java.util.HashMap;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/security/jacc/EJBMethodPermission.class */
public final class EJBMethodPermission extends Permission implements Serializable {
    private static final String[] interfaceKeys = {"Local", "LocalHome", "Remote", "Home", "ServiceEndpoint"};
    private static HashMap interfaceHash = new HashMap();
    private transient int methodInterface;
    private transient String methodName;
    private transient String methodParams;
    private transient String actions;
    private transient int hashCodeValue;
    private static final long serialVersionUID = 1;
    private static final ObjectStreamField[] serialPersistentFields;
    static Class class$java$lang$String;

    static {
        Class cls;
        for (int i = 0; i < interfaceKeys.length; i++) {
            interfaceHash.put(interfaceKeys[i], new Integer(i));
        }
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

    public EJBMethodPermission(String name, String actions) {
        super(name);
        this.hashCodeValue = 0;
        setMethodSpec(actions);
    }

    public EJBMethodPermission(String EJBName, String methodName, String methodInterface, String[] methodParams) {
        super(EJBName);
        this.hashCodeValue = 0;
        setMethodSpec(methodName, methodInterface, methodParams);
    }

    public EJBMethodPermission(String EJBName, String methodInterface, Method method) {
        super(EJBName);
        this.hashCodeValue = 0;
        setMethodSpec(methodInterface, method);
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof EJBMethodPermission)) {
            return false;
        }
        EJBMethodPermission that = (EJBMethodPermission) o;
        if (getName().equals(that.getName())) {
            if (this.methodName != null) {
                if (that.methodName == null || !this.methodName.equals(that.methodName)) {
                    return false;
                }
            } else if (that.methodName != null) {
                return false;
            }
            if (this.methodInterface != that.methodInterface) {
                return false;
            }
            if (this.methodParams == null) {
                return that.methodParams == null;
            } else if (that.methodParams == null || !this.methodParams.equals(that.methodParams)) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override // java.security.Permission
    public String getActions() {
        if (this.actions == null) {
            if (this.methodName == null) {
                if (this.methodInterface < 0) {
                    if (this.methodParams != null) {
                        this.actions = new StringBuffer().append(",").append(this.methodParams).toString();
                    }
                } else if (this.methodParams == null) {
                    this.actions = new StringBuffer().append(",").append(interfaceKeys[this.methodInterface]).toString();
                } else {
                    this.actions = new StringBuffer().append(",").append(interfaceKeys[this.methodInterface]).append(this.methodParams).toString();
                }
            } else if (this.methodInterface < 0) {
                if (this.methodParams == null) {
                    this.actions = this.methodName;
                } else {
                    this.actions = new StringBuffer().append(this.methodName).append(",").append(this.methodParams).toString();
                }
            } else if (this.methodParams == null) {
                this.actions = new StringBuffer().append(this.methodName).append(",").append(interfaceKeys[this.methodInterface]).toString();
            } else {
                this.actions = new StringBuffer().append(this.methodName).append(",").append(interfaceKeys[this.methodInterface]).append(this.methodParams).toString();
            }
        }
        return this.actions;
    }

    public int hashCode() {
        if (this.hashCodeValue == 0) {
            String actions = getActions();
            String hashInput = actions == null ? getName() : new String(new StringBuffer().append(getName()).append(Instruction.argsep).append(actions).toString());
            this.hashCodeValue = hashInput.hashCode();
        }
        return this.hashCodeValue;
    }

    @Override // java.security.Permission
    public boolean implies(Permission permission) {
        if (permission == null || !(permission instanceof EJBMethodPermission)) {
            return false;
        }
        EJBMethodPermission that = (EJBMethodPermission) permission;
        if (getName().equals(that.getName())) {
            if (this.methodName != null && (that.methodName == null || !this.methodName.equals(that.methodName))) {
                return false;
            }
            if (this.methodInterface != -1 && (that.methodInterface == -1 || this.methodInterface != that.methodInterface)) {
                return false;
            }
            if (this.methodParams != null) {
                if (that.methodParams == null || !this.methodParams.equals(that.methodParams)) {
                    return false;
                }
                return true;
            }
            return true;
        }
        return false;
    }

    private synchronized void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        setMethodSpec((String) s.readFields().get("actions", (Object) null));
    }

    private synchronized void writeObject(ObjectOutputStream s) throws IOException {
        s.putFields().put("actions", getActions());
        s.writeFields();
    }

    private void setMethodSpec(String actions) {
        String mInterface = null;
        if (actions != null) {
            int i = actions.indexOf(44);
            if (i < 0) {
                this.methodName = actions;
            } else if (i > 0) {
                this.methodName = actions.substring(0, i);
                int j = actions.substring(i + 1).indexOf(44);
                if (j < 0) {
                    mInterface = actions.substring(i + 1);
                } else {
                    if (j > 0) {
                        mInterface = actions.substring(i + 1, i + j + 1);
                    }
                    this.methodParams = actions.substring(i + j + 2);
                    if (this.methodParams.length() > 1 && this.methodParams.endsWith(",")) {
                        throw new IllegalArgumentException("illegal methodParam");
                    }
                }
            }
        }
        this.methodInterface = validateInterface(mInterface);
        this.actions = actions;
    }

    private void setMethodSpec(String methodName, String methodInterface, String[] methodParams) {
        if (methodName != null && methodName.indexOf(44) >= 0) {
            throw new IllegalArgumentException("illegal methodName");
        }
        this.methodInterface = validateInterface(methodInterface);
        if (methodParams != null) {
            StringBuffer mParams = new StringBuffer(",");
            for (int i = 0; i < methodParams.length; i++) {
                if (methodParams[i] == null || methodParams[i].indexOf(44) >= 0) {
                    throw new IllegalArgumentException("illegal methodParam");
                }
                if (i == 0) {
                    mParams.append(methodParams[i]);
                } else {
                    mParams.append(new StringBuffer().append(",").append(methodParams[i]).toString());
                }
            }
            this.methodParams = mParams.toString();
        }
        this.methodName = methodName;
    }

    private void setMethodSpec(String methodInterface, Method method) {
        this.methodInterface = validateInterface(methodInterface);
        this.methodName = method.getName();
        Class[] params = method.getParameterTypes();
        StringBuffer mParams = new StringBuffer(",");
        for (int i = 0; i < params.length; i++) {
            if (i == 0) {
                mParams.append(params[i].getName());
            }
            mParams.append(new StringBuffer().append(",").append(params[i].getName()).toString());
        }
        this.methodParams = mParams.toString();
    }

    private static int validateInterface(String methodInterface) {
        int result = -1;
        if (methodInterface != null) {
            Integer i = (Integer) interfaceHash.get(methodInterface);
            if (i == null) {
                throw new IllegalArgumentException("illegal methodInterface");
            }
            result = i.intValue();
        }
        return result;
    }
}
