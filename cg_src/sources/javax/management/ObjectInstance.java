package javax.management;

import java.io.Serializable;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/ObjectInstance.class */
public class ObjectInstance implements Serializable {
    private static final long serialVersionUID = -4099952623687795850L;
    private ObjectName name;
    private String className;

    public ObjectInstance(String str, String str2) throws MalformedObjectNameException {
        this(new ObjectName(str), str2);
    }

    public ObjectInstance(ObjectName objectName, String str) {
        if (objectName.isPattern()) {
            new RuntimeOperationsException(new IllegalArgumentException(new StringBuffer().append("Invalid name->").append(objectName.toString()).toString()));
        }
        this.name = objectName;
        this.className = str;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ObjectInstance)) {
            return false;
        }
        ObjectInstance objectInstance = (ObjectInstance) obj;
        return this.name.equals(objectInstance.getObjectName()) && this.className.equals(objectInstance.getClassName());
    }

    public int hashCode() {
        return this.name.hashCode() ^ this.className.hashCode();
    }

    public ObjectName getObjectName() {
        return this.name;
    }

    public String getClassName() {
        return this.className;
    }
}
