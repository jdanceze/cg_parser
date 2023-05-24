package javax.management;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/NotificationFilterSupport.class */
public class NotificationFilterSupport implements NotificationFilter, Serializable {
    private static final long serialVersionUID = 6579080007561786969L;
    private List enabledTypes = new Vector();

    @Override // javax.management.NotificationFilter
    public synchronized boolean isNotificationEnabled(Notification notification) {
        String type = notification.getType();
        if (type == null) {
            return false;
        }
        try {
            for (String str : this.enabledTypes) {
                if (type.startsWith(str)) {
                    return true;
                }
            }
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public synchronized void enableType(String str) throws IllegalArgumentException {
        if (str == null) {
            throw new IllegalArgumentException("The prefix cannot be null.");
        }
        if (!this.enabledTypes.contains(str)) {
            this.enabledTypes.add(str);
        }
    }

    public synchronized void disableType(String str) {
        this.enabledTypes.remove(str);
    }

    public synchronized void disableAllTypes() {
        this.enabledTypes.clear();
    }

    public synchronized Vector getEnabledTypes() {
        return (Vector) this.enabledTypes;
    }
}
