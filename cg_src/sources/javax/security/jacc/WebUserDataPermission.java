package javax.security.jacc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.security.Permission;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/security/jacc/WebUserDataPermission.class */
public final class WebUserDataPermission extends Permission implements Serializable {
    private static String[] transportKeys = {"NONE", "INTEGRAL", "CONFIDENTIAL"};
    private static HashMap transportHash = new HashMap();
    private static int TT_NONE;
    private static int TT_CONFIDENTIAL;
    private transient URLPatternSpec urlPatternSpec;
    private transient int methodSet;
    private transient int transportType;
    private transient int hashCodeValue;
    private static final long serialVersionUID = 1;
    private static final ObjectStreamField[] serialPersistentFields;
    static Class class$java$lang$String;

    static {
        Class cls;
        for (int i = 0; i < transportKeys.length; i++) {
            transportHash.put(transportKeys[i], new Integer(i));
        }
        TT_NONE = ((Integer) transportHash.get("NONE")).intValue();
        TT_CONFIDENTIAL = ((Integer) transportHash.get("CONFIDENTIAL")).intValue();
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

    public WebUserDataPermission(String name, String actions) {
        super(name);
        this.urlPatternSpec = null;
        this.hashCodeValue = 0;
        this.urlPatternSpec = new URLPatternSpec(name);
        parseActions(actions);
    }

    public WebUserDataPermission(String urlPatternSpec, String[] HTTPMethods, String transportType) {
        super(urlPatternSpec);
        this.urlPatternSpec = null;
        this.hashCodeValue = 0;
        this.urlPatternSpec = new URLPatternSpec(urlPatternSpec);
        this.transportType = TT_NONE;
        if (transportType != null) {
            Integer bit = (Integer) transportHash.get(transportType);
            if (bit == null) {
                throw new IllegalArgumentException("illegal transport value");
            }
            this.transportType = bit.intValue();
        }
        this.methodSet = HttpMethodSpec.getMethodSet(HTTPMethods);
    }

    public WebUserDataPermission(HttpServletRequest request) {
        super(request.getServletPath());
        this.urlPatternSpec = null;
        this.hashCodeValue = 0;
        this.urlPatternSpec = new URLPatternSpec(super.getName());
        this.transportType = request.isSecure() ? TT_CONFIDENTIAL : TT_NONE;
        this.methodSet = HttpMethodSpec.getMethodSet(request.getMethod());
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof WebUserDataPermission)) {
            return false;
        }
        WebUserDataPermission that = (WebUserDataPermission) o;
        if (this.transportType == that.transportType && this.methodSet == that.methodSet) {
            return this.urlPatternSpec.equals(that.urlPatternSpec);
        }
        return false;
    }

    @Override // java.security.Permission
    public String getActions() {
        String result;
        String hActions = HttpMethodSpec.getActions(this.methodSet);
        if (this.transportType == TT_NONE && hActions == null) {
            result = null;
        } else if (this.transportType == TT_NONE) {
            result = hActions;
        } else if (hActions == null) {
            result = new StringBuffer().append(":").append(transportKeys[this.transportType]).toString();
        } else {
            result = new StringBuffer().append(hActions).append(":").append(transportKeys[this.transportType]).toString();
        }
        return result;
    }

    public int hashCode() {
        if (this.hashCodeValue == 0) {
            String hashInput = new String(new StringBuffer().append(this.urlPatternSpec.toString()).append(Instruction.argsep).append(this.methodSet).append(":").append(this.transportType).toString());
            this.hashCodeValue = hashInput.hashCode();
        }
        return this.hashCodeValue;
    }

    @Override // java.security.Permission
    public boolean implies(Permission permission) {
        if (permission == null || !(permission instanceof WebUserDataPermission)) {
            return false;
        }
        WebUserDataPermission that = (WebUserDataPermission) permission;
        if ((this.transportType != 0 && this.transportType != that.transportType) || !HttpMethodSpec.implies(this.methodSet, that.methodSet)) {
            return false;
        }
        return this.urlPatternSpec.implies(that.urlPatternSpec);
    }

    private void parseActions(String actions) {
        this.transportType = TT_NONE;
        if (actions == null || actions.equals("")) {
            this.methodSet = HttpMethodSpec.getMethodSet((String) null);
            return;
        }
        int colon = actions.indexOf(58);
        if (colon < 0) {
            this.methodSet = HttpMethodSpec.getMethodSet(actions);
            return;
        }
        if (colon == 0) {
            this.methodSet = HttpMethodSpec.getMethodSet((String) null);
        } else {
            this.methodSet = HttpMethodSpec.getMethodSet(actions.substring(0, colon));
        }
        Integer bit = (Integer) transportHash.get(actions.substring(colon + 1));
        if (bit == null) {
            throw new IllegalArgumentException("illegal transport value");
        }
        this.transportType = bit.intValue();
    }

    private synchronized void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        parseActions((String) s.readFields().get("actions", (Object) null));
        this.urlPatternSpec = new URLPatternSpec(super.getName());
    }

    private synchronized void writeObject(ObjectOutputStream s) throws IOException {
        s.putFields().put("actions", getActions());
        s.writeFields();
    }
}
