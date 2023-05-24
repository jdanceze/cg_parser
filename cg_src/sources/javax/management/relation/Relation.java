package javax.management.relation;

import java.util.List;
import java.util.Map;
import javax.management.ObjectName;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/relation/Relation.class */
public interface Relation {
    List getRole(String str) throws IllegalArgumentException, RoleNotFoundException, RelationServiceNotRegisteredException;

    RoleResult getRoles(String[] strArr) throws IllegalArgumentException, RelationServiceNotRegisteredException;

    Integer getRoleCardinality(String str) throws IllegalArgumentException, RoleNotFoundException;

    RoleResult getAllRoles() throws RelationServiceNotRegisteredException;

    RoleList retrieveAllRoles();

    void setRole(Role role) throws IllegalArgumentException, RoleNotFoundException, RelationTypeNotFoundException, InvalidRoleValueException, RelationServiceNotRegisteredException, RelationNotFoundException;

    RoleResult setRoles(RoleList roleList) throws IllegalArgumentException, RelationServiceNotRegisteredException, RelationTypeNotFoundException, RelationNotFoundException;

    void handleMBeanUnregistration(ObjectName objectName, String str) throws IllegalArgumentException, RoleNotFoundException, InvalidRoleValueException, RelationServiceNotRegisteredException, RelationTypeNotFoundException, RelationNotFoundException;

    Map getReferencedMBeans();

    String getRelationTypeName();

    ObjectName getRelationServiceName();

    String getRelationId();
}
