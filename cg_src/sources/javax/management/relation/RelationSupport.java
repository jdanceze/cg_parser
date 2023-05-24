package javax.management.relation;

import com.sun.jmx.trace.Trace;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import soot.JavaBasicTypes;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/relation/RelationSupport.class */
public class RelationSupport implements RelationSupportMBean, MBeanRegistration {
    private String myRelId = null;
    private ObjectName myRelServiceName = null;
    private MBeanServer myRelServiceMBeanServer = null;
    private String myRelTypeName = null;
    private HashMap myRoleName2ValueMap = new HashMap();
    private Boolean myInRelServFlg = null;
    private static String localClassName = "RelationSupport";

    public RelationSupport(String str, ObjectName objectName, String str2, RoleList roleList) throws InvalidRoleValueException, IllegalArgumentException {
        if (isTraceOn()) {
            trace("Constructor: entering", null);
        }
        initMembers(str, objectName, null, str2, roleList);
        if (isTraceOn()) {
            trace("Constructor: exiting", null);
        }
    }

    public RelationSupport(String str, ObjectName objectName, MBeanServer mBeanServer, String str2, RoleList roleList) throws InvalidRoleValueException, IllegalArgumentException {
        if (mBeanServer == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("Constructor: entering", null);
        }
        initMembers(str, objectName, mBeanServer, str2, roleList);
        if (isTraceOn()) {
            trace("Constructor: exiting", null);
        }
    }

    @Override // javax.management.relation.Relation
    public List getRole(String str) throws IllegalArgumentException, RoleNotFoundException, RelationServiceNotRegisteredException {
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("getRole: entering", str);
        }
        ArrayList arrayList = (ArrayList) getRoleInt(str, false, null, false);
        if (isTraceOn()) {
            trace("getRole: exiting", null);
        }
        return arrayList;
    }

    @Override // javax.management.relation.Relation
    public RoleResult getRoles(String[] strArr) throws IllegalArgumentException, RelationServiceNotRegisteredException {
        if (strArr == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("getRoles: entering", null);
        }
        RoleResult rolesInt = getRolesInt(strArr, false, null);
        if (isTraceOn()) {
            trace("getRoles: exiting", null);
        }
        return rolesInt;
    }

    @Override // javax.management.relation.Relation
    public RoleResult getAllRoles() throws RelationServiceNotRegisteredException {
        if (isTraceOn()) {
            trace("getAllRoles: entering", null);
        }
        RoleResult roleResult = null;
        try {
            roleResult = getAllRolesInt(false, null);
        } catch (IllegalArgumentException e) {
        }
        if (isTraceOn()) {
            trace("getAllRoles: exiting", null);
        }
        return roleResult;
    }

    @Override // javax.management.relation.Relation
    public RoleList retrieveAllRoles() {
        RoleList roleList;
        if (isTraceOn()) {
            trace("retrieveAllRoles: entering", null);
        }
        synchronized (this.myRoleName2ValueMap) {
            roleList = new RoleList(new ArrayList(this.myRoleName2ValueMap.values()));
        }
        if (isTraceOn()) {
            trace("retrieveAllRoles: exiting", null);
        }
        return roleList;
    }

    @Override // javax.management.relation.Relation
    public Integer getRoleCardinality(String str) throws IllegalArgumentException, RoleNotFoundException {
        Role role;
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("getRoleCardinality: entering", str);
        }
        synchronized (this.myRoleName2ValueMap) {
            role = (Role) this.myRoleName2ValueMap.get(str);
        }
        if (role == null) {
            try {
                RelationService.throwRoleProblemException(1, str);
            } catch (InvalidRoleValueException e) {
            }
        }
        ArrayList arrayList = (ArrayList) role.getRoleValue();
        if (isTraceOn()) {
            trace("getRoleCardinality: exiting", null);
        }
        return new Integer(arrayList.size());
    }

    @Override // javax.management.relation.Relation
    public void setRole(Role role) throws IllegalArgumentException, RoleNotFoundException, RelationTypeNotFoundException, InvalidRoleValueException, RelationServiceNotRegisteredException, RelationNotFoundException {
        if (role == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("setRole: entering", role.toString());
        }
        setRoleInt(role, false, null, false);
        if (isTraceOn()) {
            trace("setRole: exiting", null);
        }
    }

    @Override // javax.management.relation.Relation
    public RoleResult setRoles(RoleList roleList) throws IllegalArgumentException, RelationServiceNotRegisteredException, RelationTypeNotFoundException, RelationNotFoundException {
        if (roleList == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("setRoles: entering", roleList.toString());
        }
        RoleResult rolesInt = setRolesInt(roleList, false, null);
        if (isTraceOn()) {
            trace("setRoles: exiting", null);
        }
        return rolesInt;
    }

    @Override // javax.management.relation.Relation
    public void handleMBeanUnregistration(ObjectName objectName, String str) throws IllegalArgumentException, RoleNotFoundException, InvalidRoleValueException, RelationServiceNotRegisteredException, RelationTypeNotFoundException, RelationNotFoundException {
        if (objectName == null || str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("handleMBeanUnregistration: entering", new StringBuffer().append("theObjName ").append(objectName).append(", theRoleName ").append(str).toString());
        }
        handleMBeanUnregistrationInt(objectName, str, false, null);
        if (isTraceOn()) {
            trace("handleMBeanUnregistration: exiting", null);
        }
    }

    @Override // javax.management.relation.Relation
    public Map getReferencedMBeans() {
        if (isTraceOn()) {
            trace("getReferencedMBeans: entering", null);
        }
        HashMap hashMap = new HashMap();
        synchronized (this.myRoleName2ValueMap) {
            for (Role role : this.myRoleName2ValueMap.values()) {
                String roleName = role.getRoleName();
                Iterator it = ((ArrayList) role.getRoleValue()).iterator();
                while (it.hasNext()) {
                    ObjectName objectName = (ObjectName) it.next();
                    ArrayList arrayList = (ArrayList) hashMap.get(objectName);
                    boolean z = false;
                    if (arrayList == null) {
                        z = true;
                        arrayList = new ArrayList();
                    }
                    arrayList.add(roleName);
                    if (z) {
                        hashMap.put(objectName, arrayList);
                    }
                }
            }
        }
        if (isTraceOn()) {
            trace("getReferencedMBeans: exiting", null);
        }
        return hashMap;
    }

    @Override // javax.management.relation.Relation
    public String getRelationTypeName() {
        return this.myRelTypeName;
    }

    @Override // javax.management.relation.Relation
    public ObjectName getRelationServiceName() {
        return this.myRelServiceName;
    }

    @Override // javax.management.relation.Relation
    public String getRelationId() {
        return this.myRelId;
    }

    @Override // javax.management.MBeanRegistration
    public ObjectName preRegister(MBeanServer mBeanServer, ObjectName objectName) throws Exception {
        this.myRelServiceMBeanServer = mBeanServer;
        return objectName;
    }

    @Override // javax.management.MBeanRegistration
    public void postRegister(Boolean bool) {
    }

    @Override // javax.management.MBeanRegistration
    public void preDeregister() throws Exception {
    }

    @Override // javax.management.MBeanRegistration
    public void postDeregister() {
    }

    @Override // javax.management.relation.RelationSupportMBean
    public Boolean isInRelationService() {
        Boolean bool;
        synchronized (this.myInRelServFlg) {
            bool = new Boolean(this.myInRelServFlg.booleanValue());
        }
        return bool;
    }

    @Override // javax.management.relation.RelationSupportMBean
    public void setRelationServiceManagementFlag(Boolean bool) throws IllegalArgumentException {
        if (bool == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        synchronized (this.myInRelServFlg) {
            this.myInRelServFlg = new Boolean(bool.booleanValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Object getRoleInt(String str, boolean z, RelationService relationService, boolean z2) throws IllegalArgumentException, RoleNotFoundException, RelationServiceNotRegisteredException {
        Role role;
        Integer checkRoleReading;
        int intValue;
        Object roleUnresolved;
        if (str == null || (z && relationService == null)) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isDebugOn()) {
            debug("getRoleInt: entering", new StringBuffer().append("theRoleName ").append(str).toString());
        }
        synchronized (this.myRoleName2ValueMap) {
            role = (Role) this.myRoleName2ValueMap.get(str);
        }
        if (role == null) {
            intValue = 1;
        } else {
            if (z) {
                try {
                    checkRoleReading = relationService.checkRoleReading(str, this.myRelTypeName);
                } catch (RelationTypeNotFoundException e) {
                    throw new RuntimeException(e.getMessage());
                }
            } else {
                try {
                    checkRoleReading = (Integer) this.myRelServiceMBeanServer.invoke(this.myRelServiceName, "checkRoleReading", new Object[]{str, this.myRelTypeName}, new String[]{"java.lang.String", "java.lang.String"});
                } catch (InstanceNotFoundException e2) {
                    throw new RelationServiceNotRegisteredException(e2.getMessage());
                } catch (MBeanException e3) {
                    throw new RuntimeException("incorrect relation type");
                } catch (ReflectionException e4) {
                    throw new RuntimeException(e4.getMessage());
                }
            }
            intValue = checkRoleReading.intValue();
        }
        if (intValue == 0) {
            if (!z2) {
                roleUnresolved = (ArrayList) ((ArrayList) role.getRoleValue()).clone();
            } else {
                roleUnresolved = (Role) role.clone();
            }
        } else if (!z2) {
            try {
                RelationService.throwRoleProblemException(intValue, str);
                return null;
            } catch (InvalidRoleValueException e5) {
                throw new RuntimeException(e5.getMessage());
            }
        } else {
            roleUnresolved = new RoleUnresolved(str, null, intValue);
        }
        if (isDebugOn()) {
            debug("getRoleInt: exiting", null);
        }
        return roleUnresolved;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RoleResult getRolesInt(String[] strArr, boolean z, RelationService relationService) throws IllegalArgumentException, RelationServiceNotRegisteredException {
        if (strArr == null || (z && relationService == null)) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isDebugOn()) {
            debug("getRolesInt: entering", null);
        }
        RoleList roleList = new RoleList();
        RoleUnresolvedList roleUnresolvedList = new RoleUnresolvedList();
        for (String str : strArr) {
            try {
                Object roleInt = getRoleInt(str, z, relationService, true);
                if (roleInt instanceof Role) {
                    try {
                        roleList.add((Role) roleInt);
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                } else if (roleInt instanceof RoleUnresolved) {
                    try {
                        roleUnresolvedList.add((RoleUnresolved) roleInt);
                    } catch (IllegalArgumentException e2) {
                        throw new RuntimeException(e2.getMessage());
                    }
                } else {
                    continue;
                }
            } catch (RoleNotFoundException e3) {
                return null;
            }
        }
        RoleResult roleResult = new RoleResult(roleList, roleUnresolvedList);
        if (isDebugOn()) {
            debug("getRolesInt: exiting", null);
        }
        return roleResult;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RoleResult getAllRolesInt(boolean z, RelationService relationService) throws IllegalArgumentException, RelationServiceNotRegisteredException {
        ArrayList arrayList;
        if (z && relationService == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isDebugOn()) {
            debug("getAllRolesInt: entering", null);
        }
        synchronized (this.myRoleName2ValueMap) {
            arrayList = new ArrayList(this.myRoleName2ValueMap.keySet());
        }
        String[] strArr = new String[arrayList.size()];
        int i = 0;
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            strArr[i] = (String) it.next();
            i++;
        }
        RoleResult rolesInt = getRolesInt(strArr, z, relationService);
        if (isDebugOn()) {
            debug("getAllRolesInt: exiting", null);
        }
        return rolesInt;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Object setRoleInt(Role role, boolean z, RelationService relationService, boolean z2) throws IllegalArgumentException, RoleNotFoundException, InvalidRoleValueException, RelationServiceNotRegisteredException, RelationTypeNotFoundException, RelationNotFoundException {
        Role role2;
        Boolean bool;
        ArrayList arrayList;
        Integer num;
        if (role == null || (z && relationService == null)) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isDebugOn()) {
            debug("setRoleInt: entering", new StringBuffer().append("theRole ").append(role).append(", theRelServCallFlg ").append(z).append(", theMultiRoleFlg ").append(z2).toString());
        }
        String roleName = role.getRoleName();
        synchronized (this.myRoleName2ValueMap) {
            role2 = (Role) this.myRoleName2ValueMap.get(roleName);
        }
        if (role2 == null) {
            bool = new Boolean(true);
            arrayList = new ArrayList();
        } else {
            bool = new Boolean(false);
            arrayList = (ArrayList) role2.getRoleValue();
        }
        try {
            if (z) {
                num = relationService.checkRoleWriting(role, this.myRelTypeName, bool);
            } else {
                num = (Integer) this.myRelServiceMBeanServer.invoke(this.myRelServiceName, "checkRoleWriting", new Object[]{role, this.myRelTypeName, bool}, new String[]{"javax.management.relation.Role", "java.lang.String", JavaBasicTypes.JAVA_LANG_BOOLEAN});
            }
            int intValue = num.intValue();
            Object obj = null;
            if (intValue == 0) {
                if (!bool.booleanValue()) {
                    sendRoleUpdateNotification(role, arrayList, z, relationService);
                    updateRelationServiceMap(role, arrayList, z, relationService);
                }
                synchronized (this.myRoleName2ValueMap) {
                    this.myRoleName2ValueMap.put(roleName, (Role) role.clone());
                }
                if (z2) {
                    obj = role;
                }
            } else if (!z2) {
                RelationService.throwRoleProblemException(intValue, roleName);
                return null;
            } else {
                obj = new RoleUnresolved(roleName, role.getRoleValue(), intValue);
            }
            if (isDebugOn()) {
                debug("setRoleInt: exiting", null);
            }
            return obj;
        } catch (InstanceNotFoundException e) {
            throw new RelationServiceNotRegisteredException(e.getMessage());
        } catch (MBeanException e2) {
            Exception targetException = e2.getTargetException();
            if (targetException instanceof RelationTypeNotFoundException) {
                throw ((RelationTypeNotFoundException) targetException);
            }
            throw new RuntimeException(targetException.getMessage());
        } catch (ReflectionException e3) {
            throw new RuntimeException(e3.getMessage());
        } catch (RelationTypeNotFoundException e4) {
            throw new RuntimeException(e4.getMessage());
        }
    }

    private void sendRoleUpdateNotification(Role role, List list, boolean z, RelationService relationService) throws IllegalArgumentException, RelationServiceNotRegisteredException, RelationNotFoundException {
        if (role == null || list == null || (z && relationService == null)) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isDebugOn()) {
            debug("sendRoleUpdateNotification: entering", new StringBuffer().append("theNewRole ").append(role).append(", theOldRoleValue ").append(list).append(", theRelServCallFlg ").append(z).toString());
        }
        if (z) {
            try {
                relationService.sendRoleUpdateNotification(this.myRelId, role, list);
            } catch (RelationNotFoundException e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            try {
                this.myRelServiceMBeanServer.invoke(this.myRelServiceName, "sendRoleUpdateNotification", new Object[]{this.myRelId, role, (ArrayList) list}, new String[]{"java.lang.String", "javax.management.relation.Role", "java.util.List"});
            } catch (InstanceNotFoundException e2) {
                throw new RelationServiceNotRegisteredException(e2.getMessage());
            } catch (MBeanException e3) {
                Exception targetException = e3.getTargetException();
                if (targetException instanceof RelationNotFoundException) {
                    throw ((RelationNotFoundException) targetException);
                }
                throw new RuntimeException(targetException.getMessage());
            } catch (ReflectionException e4) {
                throw new RuntimeException(e4.getMessage());
            }
        }
        if (isDebugOn()) {
            debug("sendRoleUpdateNotification: exiting", null);
        }
    }

    private void updateRelationServiceMap(Role role, List list, boolean z, RelationService relationService) throws IllegalArgumentException, RelationServiceNotRegisteredException, RelationNotFoundException {
        if (role == null || list == null || (z && relationService == null)) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isDebugOn()) {
            debug("updateRelationServiceMap: entering", new StringBuffer().append("theNewRole ").append(role).append(", theOldRoleValue ").append(list).append(", theRelServCallFlg ").append(z).toString());
        }
        if (z) {
            try {
                relationService.updateRoleMap(this.myRelId, role, list);
            } catch (RelationNotFoundException e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            try {
                this.myRelServiceMBeanServer.invoke(this.myRelServiceName, "updateRoleMap", new Object[]{this.myRelId, role, list}, new String[]{"java.lang.String", "javax.management.relation.Role", "java.util.List"});
            } catch (InstanceNotFoundException e2) {
                throw new RelationServiceNotRegisteredException(e2.getMessage());
            } catch (MBeanException e3) {
                Exception targetException = e3.getTargetException();
                if (targetException instanceof RelationNotFoundException) {
                    throw ((RelationNotFoundException) targetException);
                }
                throw new RuntimeException(targetException.getMessage());
            } catch (ReflectionException e4) {
                throw new RuntimeException(e4.getMessage());
            }
        }
        if (isDebugOn()) {
            debug("updateRelationServiceMap: exiting", null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RoleResult setRolesInt(RoleList roleList, boolean z, RelationService relationService) throws IllegalArgumentException, RelationServiceNotRegisteredException, RelationTypeNotFoundException, RelationNotFoundException {
        if (roleList == null || (z && relationService == null)) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isDebugOn()) {
            debug("setRolesInt: entering", new StringBuffer().append("theRoleList ").append(roleList).append(", theRelServCallFlg ").append(z).toString());
        }
        RoleList roleList2 = new RoleList();
        RoleUnresolvedList roleUnresolvedList = new RoleUnresolvedList();
        Iterator it = roleList.iterator();
        while (it.hasNext()) {
            Role role = null;
            try {
                role = setRoleInt((Role) it.next(), z, relationService, true);
            } catch (InvalidRoleValueException e) {
            } catch (RoleNotFoundException e2) {
            }
            if (role instanceof Role) {
                try {
                    roleList2.add(role);
                } catch (IllegalArgumentException e3) {
                    throw new RuntimeException(e3.getMessage());
                }
            } else if (role instanceof RoleUnresolved) {
                try {
                    roleUnresolvedList.add((RoleUnresolved) role);
                } catch (IllegalArgumentException e4) {
                    throw new RuntimeException(e4.getMessage());
                }
            } else {
                continue;
            }
        }
        RoleResult roleResult = new RoleResult(roleList2, roleUnresolvedList);
        if (isDebugOn()) {
            debug("setRolesInt: exiting", null);
        }
        return roleResult;
    }

    private void initMembers(String str, ObjectName objectName, MBeanServer mBeanServer, String str2, RoleList roleList) throws InvalidRoleValueException, IllegalArgumentException {
        if (str == null || objectName == null || str2 == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isDebugOn()) {
            StringBuffer stringBuffer = new StringBuffer(new StringBuffer().append("theRelId ").append(str).append(", theRelServiceName ").append(objectName.toString()).append(", theRelTypeName ").append(str2).toString());
            if (roleList != null) {
                stringBuffer.append(new StringBuffer().append(", theRoleList ").append(roleList.toString()).toString());
            }
            debug("initMembers: entering", stringBuffer.toString());
        }
        this.myRelId = str;
        this.myRelServiceName = objectName;
        this.myRelServiceMBeanServer = mBeanServer;
        this.myRelTypeName = str2;
        initRoleMap(roleList);
        this.myInRelServFlg = new Boolean(false);
        if (isDebugOn()) {
            debug("initMembers: exiting", null);
        }
    }

    private void initRoleMap(RoleList roleList) throws InvalidRoleValueException {
        if (roleList == null) {
            return;
        }
        if (isDebugOn()) {
            debug("initRoleMap: entering", roleList.toString());
        }
        synchronized (this.myRoleName2ValueMap) {
            Iterator it = roleList.iterator();
            while (it.hasNext()) {
                Role role = (Role) it.next();
                String roleName = role.getRoleName();
                if (this.myRoleName2ValueMap.containsKey(roleName)) {
                    StringBuffer stringBuffer = new StringBuffer("Role name ");
                    stringBuffer.append(roleName);
                    stringBuffer.append(" used for two roles.");
                    throw new InvalidRoleValueException(stringBuffer.toString());
                }
                this.myRoleName2ValueMap.put(roleName, (Role) role.clone());
            }
        }
        if (isDebugOn()) {
            debug("initRoleMap: exiting", null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleMBeanUnregistrationInt(ObjectName objectName, String str, boolean z, RelationService relationService) throws IllegalArgumentException, RoleNotFoundException, InvalidRoleValueException, RelationServiceNotRegisteredException, RelationTypeNotFoundException, RelationNotFoundException {
        Role role;
        if (objectName == null || str == null || (z && relationService == null)) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isDebugOn()) {
            debug("handleMBeanUnregistrationInt: entering", new StringBuffer().append("theObjName ").append(objectName).append(", theRoleName ").append(str).append(", theRelServCallFlg ").append(z).toString());
        }
        synchronized (this.myRoleName2ValueMap) {
            role = (Role) this.myRoleName2ValueMap.get(str);
        }
        if (role == null) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("No role with name ");
            stringBuffer.append(str);
            throw new RoleNotFoundException(stringBuffer.toString());
        }
        ArrayList arrayList = (ArrayList) ((ArrayList) role.getRoleValue()).clone();
        arrayList.remove(objectName);
        setRoleInt(new Role(str, arrayList), z, relationService, false);
        if (isDebugOn()) {
            debug("handleMBeanUnregistrationInt: exiting", null);
        }
    }

    private boolean isTraceOn() {
        return Trace.isSelected(1, 64);
    }

    private void trace(String str, String str2) {
        Trace.send(1, 64, localClassName, str, str2);
        Trace.send(1, 64, "", "", "\n");
    }

    private boolean isDebugOn() {
        return Trace.isSelected(2, 64);
    }

    private void debug(String str, String str2) {
        Trace.send(2, 64, localClassName, str, str2);
        Trace.send(2, 64, "", "", "\n");
    }
}
