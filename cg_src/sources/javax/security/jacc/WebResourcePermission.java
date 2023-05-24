package javax.security.jacc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.security.Permission;
import javax.servlet.http.HttpServletRequest;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/security/jacc/WebResourcePermission.class */
public final class WebResourcePermission extends Permission implements Serializable {
    private transient int methodSet;
    private transient URLPatternSpec urlPatternSpec;
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

    public WebResourcePermission(String name, String actions) {
        super(name);
        this.urlPatternSpec = null;
        this.hashCodeValue = 0;
        this.urlPatternSpec = new URLPatternSpec(name);
        this.methodSet = HttpMethodSpec.getMethodSet(actions);
    }

    public WebResourcePermission(String urlPatternSpec, String[] HTTPMethods) {
        super(urlPatternSpec);
        this.urlPatternSpec = null;
        this.hashCodeValue = 0;
        this.urlPatternSpec = new URLPatternSpec(urlPatternSpec);
        this.methodSet = HttpMethodSpec.getMethodSet(HTTPMethods);
    }

    public WebResourcePermission(HttpServletRequest request) {
        super(request.getServletPath());
        this.urlPatternSpec = null;
        this.hashCodeValue = 0;
        this.urlPatternSpec = new URLPatternSpec(super.getName());
        this.methodSet = HttpMethodSpec.getMethodSet(request.getMethod());
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof WebResourcePermission)) {
            return false;
        }
        WebResourcePermission that = (WebResourcePermission) o;
        if (this.methodSet != that.methodSet) {
            return false;
        }
        return this.urlPatternSpec.equals(that.urlPatternSpec);
    }

    @Override // java.security.Permission
    public String getActions() {
        return HttpMethodSpec.getActions(this.methodSet);
    }

    public int hashCode() {
        if (this.hashCodeValue == 0) {
            String hashInput = new String(new StringBuffer().append(this.urlPatternSpec.toString()).append(Instruction.argsep).append(this.methodSet).toString());
            this.hashCodeValue = hashInput.hashCode();
        }
        return this.hashCodeValue;
    }

    @Override // java.security.Permission
    public boolean implies(Permission permission) {
        if (permission == null || !(permission instanceof WebResourcePermission)) {
            return false;
        }
        WebResourcePermission that = (WebResourcePermission) permission;
        if (!HttpMethodSpec.implies(this.methodSet, that.methodSet)) {
            return false;
        }
        return this.urlPatternSpec.implies(that.urlPatternSpec);
    }

    private synchronized void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        this.methodSet = HttpMethodSpec.getMethodSet((String) s.readFields().get("actions", (Object) null));
        this.urlPatternSpec = new URLPatternSpec(super.getName());
    }

    private synchronized void writeObject(ObjectOutputStream s) throws IOException {
        s.putFields().put("actions", getActions());
        s.writeFields();
    }
}
