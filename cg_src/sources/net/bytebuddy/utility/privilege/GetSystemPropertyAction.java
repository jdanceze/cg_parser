package net.bytebuddy.utility.privilege;

import java.security.PrivilegedAction;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
@HashCodeAndEqualsPlugin.Enhance
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/utility/privilege/GetSystemPropertyAction.class */
public class GetSystemPropertyAction implements PrivilegedAction<String> {
    private final String key;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.key.equals(((GetSystemPropertyAction) obj).key);
    }

    public int hashCode() {
        return (17 * 31) + this.key.hashCode();
    }

    public GetSystemPropertyAction(String key) {
        this.key = key;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.security.PrivilegedAction
    public String run() {
        return System.getProperty(this.key);
    }
}
