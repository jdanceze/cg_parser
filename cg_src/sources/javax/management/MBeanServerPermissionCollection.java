package javax.management;

import java.security.Permission;
import java.security.PermissionCollection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;
/* compiled from: MBeanServerPermission.java */
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/MBeanServerPermissionCollection.class */
class MBeanServerPermissionCollection extends PermissionCollection {
    private MBeanServerPermission collectionPermission;
    private static final long serialVersionUID = -5661980843569388590L;

    @Override // java.security.PermissionCollection
    public synchronized void add(Permission permission) {
        if (!(permission instanceof MBeanServerPermission)) {
            throw new IllegalArgumentException(new StringBuffer().append("Permission not an MBeanServerPermission: ").append(permission).toString());
        }
        if (isReadOnly()) {
            throw new SecurityException("Read-only permission collection");
        }
        MBeanServerPermission mBeanServerPermission = (MBeanServerPermission) permission;
        if (this.collectionPermission == null) {
            this.collectionPermission = mBeanServerPermission;
        } else if (!this.collectionPermission.implies(permission)) {
            this.collectionPermission = new MBeanServerPermission(this.collectionPermission.mask | mBeanServerPermission.mask);
        }
    }

    @Override // java.security.PermissionCollection
    public synchronized boolean implies(Permission permission) {
        return this.collectionPermission != null && this.collectionPermission.implies(permission);
    }

    @Override // java.security.PermissionCollection
    public synchronized Enumeration elements() {
        Set singleton;
        if (this.collectionPermission == null) {
            singleton = Collections.EMPTY_SET;
        } else {
            singleton = Collections.singleton(this.collectionPermission);
        }
        return Collections.enumeration(singleton);
    }
}
