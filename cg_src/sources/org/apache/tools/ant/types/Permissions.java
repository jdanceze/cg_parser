package org.apache.tools.ant.types;

import java.net.SocketPermission;
import java.security.UnresolvedPermission;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PropertyPermission;
import java.util.Set;
import java.util.StringTokenizer;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ExitException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/Permissions.class */
public class Permissions {
    private final List<Permission> grantedPermissions;
    private final List<Permission> revokedPermissions;
    private java.security.Permissions granted;
    private SecurityManager origSm;
    private boolean active;
    private final boolean delegateToOldSM;
    private static final Class<?>[] PARAMS = {String.class, String.class};

    public Permissions() {
        this(false);
    }

    public Permissions(boolean delegateToOldSM) {
        this.grantedPermissions = new LinkedList();
        this.revokedPermissions = new LinkedList();
        this.granted = null;
        this.origSm = null;
        this.active = false;
        this.delegateToOldSM = delegateToOldSM;
    }

    public void addConfiguredGrant(Permission perm) {
        this.grantedPermissions.add(perm);
    }

    public void addConfiguredRevoke(Permission perm) {
        this.revokedPermissions.add(perm);
    }

    public synchronized void setSecurityManager() throws BuildException {
        this.origSm = System.getSecurityManager();
        init();
        System.setSecurityManager(new MySM());
        this.active = true;
    }

    private void init() throws BuildException {
        this.granted = new java.security.Permissions();
        for (Permission p : this.revokedPermissions) {
            if (p.getClassName() == null) {
                throw new BuildException("Revoked permission " + p + " does not contain a class.");
            }
        }
        for (Permission p2 : this.grantedPermissions) {
            if (p2.getClassName() == null) {
                throw new BuildException("Granted permission " + p2 + " does not contain a class.");
            }
            java.security.Permission perm = createPermission(p2);
            this.granted.add(perm);
        }
        this.granted.add(new SocketPermission("localhost:1024-", "listen"));
        this.granted.add(new PropertyPermission("java.version", "read"));
        this.granted.add(new PropertyPermission("java.vendor", "read"));
        this.granted.add(new PropertyPermission("java.vendor.url", "read"));
        this.granted.add(new PropertyPermission("java.class.version", "read"));
        this.granted.add(new PropertyPermission("os.name", "read"));
        this.granted.add(new PropertyPermission("os.version", "read"));
        this.granted.add(new PropertyPermission("os.arch", "read"));
        this.granted.add(new PropertyPermission("file.encoding", "read"));
        this.granted.add(new PropertyPermission("file.separator", "read"));
        this.granted.add(new PropertyPermission("path.separator", "read"));
        this.granted.add(new PropertyPermission("line.separator", "read"));
        this.granted.add(new PropertyPermission("java.specification.version", "read"));
        this.granted.add(new PropertyPermission("java.specification.vendor", "read"));
        this.granted.add(new PropertyPermission("java.specification.name", "read"));
        this.granted.add(new PropertyPermission("java.vm.specification.version", "read"));
        this.granted.add(new PropertyPermission("java.vm.specification.vendor", "read"));
        this.granted.add(new PropertyPermission("java.vm.specification.name", "read"));
        this.granted.add(new PropertyPermission("java.vm.version", "read"));
        this.granted.add(new PropertyPermission("java.vm.vendor", "read"));
        this.granted.add(new PropertyPermission("java.vm.name", "read"));
    }

    private java.security.Permission createPermission(Permission permission) {
        try {
            Class<? extends U> asSubclass = Class.forName(permission.getClassName()).asSubclass(java.security.Permission.class);
            String name = permission.getName();
            String actions = permission.getActions();
            return (java.security.Permission) asSubclass.getConstructor(PARAMS).newInstance(name, actions);
        } catch (Exception e) {
            return new UnresolvedPermission(permission.getClassName(), permission.getName(), permission.getActions(), null);
        }
    }

    public synchronized void restoreSecurityManager() {
        this.active = false;
        System.setSecurityManager(this.origSm);
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/Permissions$MySM.class */
    private class MySM extends SecurityManager {
        private MySM() {
        }

        @Override // java.lang.SecurityManager
        public void checkExit(int status) {
            java.security.Permission perm = new RuntimePermission("exitVM", null);
            try {
                checkPermission(perm);
            } catch (SecurityException e) {
                throw new ExitException(e.getMessage(), status);
            }
        }

        @Override // java.lang.SecurityManager
        public void checkPermission(java.security.Permission perm) {
            if (Permissions.this.active) {
                if (!Permissions.this.delegateToOldSM || perm.getName().equals("exitVM")) {
                    if (!Permissions.this.granted.implies(perm)) {
                        throw new SecurityException("Permission " + perm + " was not granted.");
                    }
                    checkRevoked(perm);
                    return;
                }
                boolean permOK = false;
                if (Permissions.this.granted.implies(perm)) {
                    permOK = true;
                }
                checkRevoked(perm);
                if (!permOK && Permissions.this.origSm != null) {
                    Permissions.this.origSm.checkPermission(perm);
                }
            }
        }

        private void checkRevoked(java.security.Permission perm) {
            for (Permission revoked : Permissions.this.revokedPermissions) {
                if (revoked.matches(perm)) {
                    throw new SecurityException("Permission " + perm + " was revoked.");
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/Permissions$Permission.class */
    public static class Permission {
        private String className;
        private String name;
        private String actionString;
        private Set<String> actions;

        public void setClass(String aClass) {
            this.className = aClass.trim();
        }

        public String getClassName() {
            return this.className;
        }

        public void setName(String aName) {
            this.name = aName.trim();
        }

        public String getName() {
            return this.name;
        }

        public void setActions(String actions) {
            this.actionString = actions;
            if (!actions.isEmpty()) {
                this.actions = parseActions(actions);
            }
        }

        public String getActions() {
            return this.actionString;
        }

        boolean matches(java.security.Permission perm) {
            if (!this.className.equals(perm.getClass().getName())) {
                return false;
            }
            if (this.name != null) {
                if (this.name.endsWith("*")) {
                    if (!perm.getName().startsWith(this.name.substring(0, this.name.length() - 1))) {
                        return false;
                    }
                } else if (!this.name.equals(perm.getName())) {
                    return false;
                }
            }
            if (this.actions != null) {
                Set<String> as = parseActions(perm.getActions());
                int size = as.size();
                as.removeAll(this.actions);
                return as.size() != size;
            }
            return true;
        }

        private Set<String> parseActions(String actions) {
            Set<String> result = new HashSet<>();
            StringTokenizer tk = new StringTokenizer(actions, ",");
            while (tk.hasMoreTokens()) {
                String item = tk.nextToken().trim();
                if (!item.isEmpty()) {
                    result.add(item);
                }
            }
            return result;
        }

        public String toString() {
            return "Permission: " + this.className + " (\"" + this.name + "\", \"" + this.actions + "\")";
        }
    }
}
