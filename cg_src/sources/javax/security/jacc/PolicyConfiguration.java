package javax.security.jacc;

import java.security.Permission;
import java.security.PermissionCollection;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/security/jacc/PolicyConfiguration.class */
public interface PolicyConfiguration {
    String getContextID() throws PolicyContextException;

    void addToRole(String str, PermissionCollection permissionCollection) throws PolicyContextException;

    void addToRole(String str, Permission permission) throws PolicyContextException;

    void addToUncheckedPolicy(PermissionCollection permissionCollection) throws PolicyContextException;

    void addToUncheckedPolicy(Permission permission) throws PolicyContextException;

    void addToExcludedPolicy(PermissionCollection permissionCollection) throws PolicyContextException;

    void addToExcludedPolicy(Permission permission) throws PolicyContextException;

    void removeRole(String str) throws PolicyContextException;

    void removeUncheckedPolicy() throws PolicyContextException;

    void removeExcludedPolicy() throws PolicyContextException;

    void linkConfiguration(PolicyConfiguration policyConfiguration) throws PolicyContextException;

    void delete() throws PolicyContextException;

    void commit() throws PolicyContextException;

    boolean inService() throws PolicyContextException;
}
