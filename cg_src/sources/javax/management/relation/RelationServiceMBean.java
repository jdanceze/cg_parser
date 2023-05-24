package javax.management.relation;

import java.util.List;
import java.util.Map;
import javax.management.InstanceNotFoundException;
import javax.management.ObjectName;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/relation/RelationServiceMBean.class */
public interface RelationServiceMBean {
    void isActive() throws RelationServiceNotRegisteredException;

    boolean getPurgeFlag();

    void setPurgeFlag(boolean z);

    void createRelationType(String str, RoleInfo[] roleInfoArr) throws IllegalArgumentException, InvalidRelationTypeException;

    void addRelationType(RelationType relationType) throws IllegalArgumentException, InvalidRelationTypeException;

    List getAllRelationTypeNames();

    List getRoleInfos(String str) throws IllegalArgumentException, RelationTypeNotFoundException;

    RoleInfo getRoleInfo(String str, String str2) throws IllegalArgumentException, RelationTypeNotFoundException, RoleInfoNotFoundException;

    void removeRelationType(String str) throws RelationServiceNotRegisteredException, IllegalArgumentException, RelationTypeNotFoundException;

    void createRelation(String str, String str2, RoleList roleList) throws RelationServiceNotRegisteredException, IllegalArgumentException, RoleNotFoundException, InvalidRelationIdException, RelationTypeNotFoundException, InvalidRoleValueException;

    void addRelation(ObjectName objectName) throws IllegalArgumentException, RelationServiceNotRegisteredException, NoSuchMethodException, InvalidRelationIdException, InstanceNotFoundException, InvalidRelationServiceException, RelationTypeNotFoundException, RoleNotFoundException, InvalidRoleValueException;

    ObjectName isRelationMBean(String str) throws IllegalArgumentException, RelationNotFoundException;

    String isRelation(ObjectName objectName) throws IllegalArgumentException;

    Boolean hasRelation(String str) throws IllegalArgumentException;

    List getAllRelationIds();

    Integer checkRoleReading(String str, String str2) throws IllegalArgumentException, RelationTypeNotFoundException;

    Integer checkRoleWriting(Role role, String str, Boolean bool) throws IllegalArgumentException, RelationTypeNotFoundException;

    void sendRelationCreationNotification(String str) throws IllegalArgumentException, RelationNotFoundException;

    void sendRoleUpdateNotification(String str, Role role, List list) throws IllegalArgumentException, RelationNotFoundException;

    void sendRelationRemovalNotification(String str, List list) throws IllegalArgumentException, RelationNotFoundException;

    void updateRoleMap(String str, Role role, List list) throws IllegalArgumentException, RelationServiceNotRegisteredException, RelationNotFoundException;

    void removeRelation(String str) throws RelationServiceNotRegisteredException, IllegalArgumentException, RelationNotFoundException;

    void purgeRelations() throws RelationServiceNotRegisteredException;

    Map findReferencingRelations(ObjectName objectName, String str, String str2) throws IllegalArgumentException;

    Map findAssociatedMBeans(ObjectName objectName, String str, String str2) throws IllegalArgumentException;

    List findRelationsOfType(String str) throws IllegalArgumentException, RelationTypeNotFoundException;

    List getRole(String str, String str2) throws RelationServiceNotRegisteredException, IllegalArgumentException, RelationNotFoundException, RoleNotFoundException;

    RoleResult getRoles(String str, String[] strArr) throws RelationServiceNotRegisteredException, IllegalArgumentException, RelationNotFoundException;

    RoleResult getAllRoles(String str) throws IllegalArgumentException, RelationNotFoundException, RelationServiceNotRegisteredException;

    Integer getRoleCardinality(String str, String str2) throws IllegalArgumentException, RelationNotFoundException, RoleNotFoundException;

    void setRole(String str, Role role) throws RelationServiceNotRegisteredException, IllegalArgumentException, RelationNotFoundException, RoleNotFoundException, InvalidRoleValueException, RelationTypeNotFoundException;

    RoleResult setRoles(String str, RoleList roleList) throws RelationServiceNotRegisteredException, IllegalArgumentException, RelationNotFoundException;

    Map getReferencedMBeans(String str) throws IllegalArgumentException, RelationNotFoundException;

    String getRelationTypeName(String str) throws IllegalArgumentException, RelationNotFoundException;
}
