package javax.management.relation;

import com.sun.jmx.trace.Trace;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.MBeanServerNotification;
import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.ReflectionException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/relation/RelationService.class */
public class RelationService extends NotificationBroadcasterSupport implements RelationServiceMBean, MBeanRegistration, NotificationListener {
    private HashMap myRelId2ObjMap = new HashMap();
    private HashMap myRelId2RelTypeMap = new HashMap();
    private HashMap myRelMBeanObjName2RelIdMap = new HashMap();
    private HashMap myRelType2ObjMap = new HashMap();
    private HashMap myRelType2RelIdsMap = new HashMap();
    private HashMap myRefedMBeanObjName2RelIdsMap = new HashMap();
    private boolean myPurgeFlg = true;
    private Long myNtfSeqNbrCounter = new Long(0);
    private ObjectName myObjName = null;
    private MBeanServer myMBeanServer = null;
    private MBeanServerNotificationFilter myUnregNtfFilter = null;
    private ArrayList myUnregNtfList = new ArrayList();
    private static String localClassName = "RelationService";

    public RelationService(boolean z) {
        if (isTraceOn()) {
            trace("Constructor: entering", null);
        }
        setPurgeFlag(z);
        if (isTraceOn()) {
            trace("Constructor: exiting", null);
        }
    }

    @Override // javax.management.relation.RelationServiceMBean
    public void isActive() throws RelationServiceNotRegisteredException {
        if (this.myMBeanServer == null) {
            throw new RelationServiceNotRegisteredException("Relation Service not registered in the MBean Server.");
        }
    }

    @Override // javax.management.MBeanRegistration
    public ObjectName preRegister(MBeanServer mBeanServer, ObjectName objectName) throws Exception {
        this.myMBeanServer = mBeanServer;
        this.myObjName = objectName;
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

    @Override // javax.management.relation.RelationServiceMBean
    public boolean getPurgeFlag() {
        return this.myPurgeFlg;
    }

    @Override // javax.management.relation.RelationServiceMBean
    public void setPurgeFlag(boolean z) {
        this.myPurgeFlg = z;
    }

    private Long getNotificationSequenceNumber() {
        Long l;
        synchronized (this.myNtfSeqNbrCounter) {
            l = new Long(this.myNtfSeqNbrCounter.longValue() + 1);
            this.myNtfSeqNbrCounter = new Long(l.longValue());
        }
        return l;
    }

    @Override // javax.management.relation.RelationServiceMBean
    public void createRelationType(String str, RoleInfo[] roleInfoArr) throws IllegalArgumentException, InvalidRelationTypeException {
        if (str == null || roleInfoArr == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("createRelationType: entering", str);
        }
        addRelationTypeInt(new RelationTypeSupport(str, roleInfoArr));
        if (isTraceOn()) {
            trace("createRelationType: exiting", null);
        }
    }

    @Override // javax.management.relation.RelationServiceMBean
    public void addRelationType(RelationType relationType) throws IllegalArgumentException, InvalidRelationTypeException {
        if (relationType == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("addRelationType: entering", null);
        }
        List<RoleInfo> roleInfos = relationType.getRoleInfos();
        if (roleInfos == null) {
            throw new InvalidRelationTypeException("No role info provided.");
        }
        RoleInfo[] roleInfoArr = new RoleInfo[roleInfos.size()];
        int i = 0;
        for (RoleInfo roleInfo : roleInfos) {
            roleInfoArr[i] = roleInfo;
            i++;
        }
        RelationTypeSupport.checkRoleInfos(roleInfoArr);
        addRelationTypeInt(relationType);
        if (isTraceOn()) {
            trace("addRelationType: exiting", null);
        }
    }

    @Override // javax.management.relation.RelationServiceMBean
    public List getAllRelationTypeNames() {
        ArrayList arrayList;
        synchronized (this.myRelType2ObjMap) {
            arrayList = new ArrayList(this.myRelType2ObjMap.keySet());
        }
        return arrayList;
    }

    @Override // javax.management.relation.RelationServiceMBean
    public List getRoleInfos(String str) throws IllegalArgumentException, RelationTypeNotFoundException {
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("getRoleInfos: entering", str);
        }
        RelationType relationType = getRelationType(str);
        if (isTraceOn()) {
            trace("getRoleInfos: exiting", null);
        }
        return relationType.getRoleInfos();
    }

    @Override // javax.management.relation.RelationServiceMBean
    public RoleInfo getRoleInfo(String str, String str2) throws IllegalArgumentException, RelationTypeNotFoundException, RoleInfoNotFoundException {
        if (str == null || str2 == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("getRoleInfo: entering", new StringBuffer().append("theRelTypeName ").append(str).append(", theRoleInfoName ").append(str2).toString());
        }
        RoleInfo roleInfo = getRelationType(str).getRoleInfo(str2);
        if (isTraceOn()) {
            trace("getRoleInfo: exiting", null);
        }
        return roleInfo;
    }

    @Override // javax.management.relation.RelationServiceMBean
    public void removeRelationType(String str) throws RelationServiceNotRegisteredException, IllegalArgumentException, RelationTypeNotFoundException {
        isActive();
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("removeRelationType: entering", str);
        }
        getRelationType(str);
        ArrayList arrayList = null;
        synchronized (this.myRelType2RelIdsMap) {
            ArrayList arrayList2 = (ArrayList) this.myRelType2RelIdsMap.get(str);
            if (arrayList2 != null) {
                arrayList = (ArrayList) arrayList2.clone();
            }
        }
        synchronized (this.myRelType2ObjMap) {
            this.myRelType2ObjMap.remove(str);
        }
        synchronized (this.myRelType2RelIdsMap) {
            this.myRelType2RelIdsMap.remove(str);
        }
        if (arrayList != null) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                try {
                    removeRelation((String) it.next());
                } catch (RelationNotFoundException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
        if (isTraceOn()) {
            trace("removeRelationType: exiting", null);
        }
    }

    @Override // javax.management.relation.RelationServiceMBean
    public void createRelation(String str, String str2, RoleList roleList) throws RelationServiceNotRegisteredException, IllegalArgumentException, RoleNotFoundException, InvalidRelationIdException, RelationTypeNotFoundException, InvalidRoleValueException {
        isActive();
        if (str == null || str2 == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            StringBuffer stringBuffer = new StringBuffer(new StringBuffer().append("theRelId ").append(str).append(", theRelTypeName ").append(str2).toString());
            if (roleList != null) {
                stringBuffer.append(new StringBuffer().append(", theRoleList ").append(roleList.toString()).toString());
            }
            trace("createRelation: entering", stringBuffer.toString());
        }
        addRelationInt(true, new RelationSupport(str, this.myObjName, str2, roleList), null, str, str2, roleList);
        if (isTraceOn()) {
            trace("createRelation: exiting", null);
        }
    }

    @Override // javax.management.relation.RelationServiceMBean
    public void addRelation(ObjectName objectName) throws IllegalArgumentException, RelationServiceNotRegisteredException, NoSuchMethodException, InvalidRelationIdException, InstanceNotFoundException, InvalidRelationServiceException, RelationTypeNotFoundException, RoleNotFoundException, InvalidRoleValueException {
        if (objectName == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("addRelation: entering", objectName.toString());
        }
        isActive();
        if (!this.myMBeanServer.isInstanceOf(objectName, "javax.management.relation.Relation")) {
            throw new NoSuchMethodException("This MBean does not implement the Relation interface.");
        }
        try {
            String str = (String) this.myMBeanServer.getAttribute(objectName, "RelationId");
            if (str == null) {
                throw new InvalidRelationIdException("This MBean does not provide a relation id.");
            }
            try {
                ObjectName objectName2 = (ObjectName) this.myMBeanServer.getAttribute(objectName, "RelationServiceName");
                boolean z = false;
                if (objectName2 == null) {
                    z = true;
                } else if (!objectName2.equals(this.myObjName)) {
                    z = true;
                }
                if (z) {
                    throw new InvalidRelationServiceException("The Relation Service referenced in the MBean is not the current one.");
                }
                try {
                    String str2 = (String) this.myMBeanServer.getAttribute(objectName, "RelationTypeName");
                    if (str2 == null) {
                        throw new RelationTypeNotFoundException("No relation type provided.");
                    }
                    try {
                        addRelationInt(false, null, objectName, str, str2, (RoleList) this.myMBeanServer.invoke(objectName, "retrieveAllRoles", null, null));
                        synchronized (this.myRelMBeanObjName2RelIdMap) {
                            this.myRelMBeanObjName2RelIdMap.put(objectName, str);
                        }
                        try {
                            this.myMBeanServer.setAttribute(objectName, new Attribute("RelationServiceManagementFlag", new Boolean(true)));
                        } catch (Exception e) {
                        }
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(objectName);
                        updateUnregistrationListener(arrayList, null);
                        if (isTraceOn()) {
                            trace("addRelation: exiting", null);
                        }
                    } catch (MBeanException e2) {
                        throw new RuntimeException(e2.getTargetException().getMessage());
                    } catch (ReflectionException e3) {
                        throw new RuntimeException(e3.getMessage());
                    }
                } catch (AttributeNotFoundException e4) {
                    throw new RuntimeException(e4.getMessage());
                } catch (MBeanException e5) {
                    throw new RuntimeException(e5.getTargetException().getMessage());
                } catch (ReflectionException e6) {
                    throw new RuntimeException(e6.getMessage());
                }
            } catch (AttributeNotFoundException e7) {
                throw new RuntimeException(e7.getMessage());
            } catch (MBeanException e8) {
                throw new RuntimeException(e8.getTargetException().getMessage());
            } catch (ReflectionException e9) {
                throw new RuntimeException(e9.getMessage());
            }
        } catch (AttributeNotFoundException e10) {
            throw new RuntimeException(e10.getMessage());
        } catch (MBeanException e11) {
            throw new RuntimeException(e11.getTargetException().getMessage());
        } catch (ReflectionException e12) {
            throw new RuntimeException(e12.getMessage());
        }
    }

    @Override // javax.management.relation.RelationServiceMBean
    public ObjectName isRelationMBean(String str) throws IllegalArgumentException, RelationNotFoundException {
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("isRelationMBean", str);
        }
        Object relation = getRelation(str);
        if (relation instanceof ObjectName) {
            return (ObjectName) relation;
        }
        return null;
    }

    @Override // javax.management.relation.RelationServiceMBean
    public String isRelation(ObjectName objectName) throws IllegalArgumentException {
        if (objectName == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("isRelation", objectName.toString());
        }
        String str = null;
        synchronized (this.myRelMBeanObjName2RelIdMap) {
            String str2 = (String) this.myRelMBeanObjName2RelIdMap.get(objectName);
            if (str2 != null) {
                str = str2;
            }
        }
        return str;
    }

    @Override // javax.management.relation.RelationServiceMBean
    public Boolean hasRelation(String str) throws IllegalArgumentException {
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("hasRelation", str);
        }
        try {
            getRelation(str);
            return new Boolean(true);
        } catch (RelationNotFoundException e) {
            return new Boolean(false);
        }
    }

    @Override // javax.management.relation.RelationServiceMBean
    public List getAllRelationIds() {
        ArrayList arrayList;
        synchronized (this.myRelId2ObjMap) {
            arrayList = new ArrayList(this.myRelId2ObjMap.keySet());
        }
        return arrayList;
    }

    @Override // javax.management.relation.RelationServiceMBean
    public Integer checkRoleReading(String str, String str2) throws IllegalArgumentException, RelationTypeNotFoundException {
        Integer num;
        if (str == null || str2 == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("checkRoleReading: entering", new StringBuffer().append("theRoleName ").append(str).append(", theRelTypeName ").append(str2).toString());
        }
        try {
            num = checkRoleInt(1, str, null, getRelationType(str2).getRoleInfo(str), false);
        } catch (RoleInfoNotFoundException e) {
            num = new Integer(1);
        }
        if (isTraceOn()) {
            trace("checkRoleReading: exiting", null);
        }
        return num;
    }

    @Override // javax.management.relation.RelationServiceMBean
    public Integer checkRoleWriting(Role role, String str, Boolean bool) throws IllegalArgumentException, RelationTypeNotFoundException {
        if (role == null || str == null || bool == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("checkRoleWriting: entering", new String(new StringBuffer().append("theRole ").append(role.toString()).append(", theRelTypeName ").append(str).append(", theInitFlg ").append(bool).toString()));
        }
        RelationType relationType = getRelationType(str);
        String roleName = role.getRoleName();
        ArrayList arrayList = (ArrayList) role.getRoleValue();
        boolean z = true;
        if (bool.booleanValue()) {
            z = false;
        }
        try {
            Integer checkRoleInt = checkRoleInt(2, roleName, arrayList, relationType.getRoleInfo(roleName), z);
            if (isTraceOn()) {
                trace("checkRoleWriting: exiting", null);
            }
            return checkRoleInt;
        } catch (RoleInfoNotFoundException e) {
            if (isTraceOn()) {
                trace("checkRoleWriting: exiting", null);
            }
            return new Integer(1);
        }
    }

    @Override // javax.management.relation.RelationServiceMBean
    public void sendRelationCreationNotification(String str) throws IllegalArgumentException, RelationNotFoundException {
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("sendRelationCreationNotification: entering", str);
        }
        StringBuffer stringBuffer = new StringBuffer("Creation of relation ");
        stringBuffer.append(str);
        sendNotificationInt(1, stringBuffer.toString(), str, null, null, null, null);
        if (isTraceOn()) {
            trace("sendRelationCreationNotification: exiting", null);
        }
    }

    @Override // javax.management.relation.RelationServiceMBean
    public void sendRoleUpdateNotification(String str, Role role, List list) throws IllegalArgumentException, RelationNotFoundException {
        if (str == null || role == null || list == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("sendRoleUpdateNotification: entering", new String(new StringBuffer().append("theRelId ").append(str).append(", theNewRole ").append(role.toString()).append(", theOldRoleValue ").append(list.toString()).toString()));
        }
        String roleName = role.getRoleName();
        ArrayList arrayList = (ArrayList) role.getRoleValue();
        String roleValueToString = Role.roleValueToString(arrayList);
        String roleValueToString2 = Role.roleValueToString(list);
        StringBuffer stringBuffer = new StringBuffer("Value of role ");
        stringBuffer.append(roleName);
        stringBuffer.append(" has changed\nOld value:\n");
        stringBuffer.append(roleValueToString2);
        stringBuffer.append("\nNew value:\n");
        stringBuffer.append(roleValueToString);
        sendNotificationInt(2, stringBuffer.toString(), str, null, roleName, arrayList, list);
        if (isTraceOn()) {
            trace("sendRoleUpdateNotification: exiting", null);
        }
    }

    @Override // javax.management.relation.RelationServiceMBean
    public void sendRelationRemovalNotification(String str, List list) throws IllegalArgumentException, RelationNotFoundException {
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            StringBuffer stringBuffer = new StringBuffer(new StringBuffer().append("theRelId ").append(str).toString());
            if (list != null) {
                stringBuffer.append(new StringBuffer().append(", theUnregMBeanList ").append(list.toString()).toString());
            }
            trace("sendRelationRemovalNotification: entering", stringBuffer.toString());
        }
        StringBuffer stringBuffer2 = new StringBuffer("Removal of relation ");
        stringBuffer2.append(str);
        sendNotificationInt(3, stringBuffer2.toString(), str, list, null, null, null);
        if (isTraceOn()) {
            trace("sendRelationRemovalNotification: exiting", null);
        }
    }

    @Override // javax.management.relation.RelationServiceMBean
    public void updateRoleMap(String str, Role role, List list) throws IllegalArgumentException, RelationServiceNotRegisteredException, RelationNotFoundException {
        if (str == null || role == null || list == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("updateRoleMap: entering", new String(new StringBuffer().append("theRelId ").append(str).append(", theNewRole ").append(role.toString()).append(", theOldRoleValue ").append(list.toString()).toString()));
        }
        isActive();
        getRelation(str);
        String roleName = role.getRoleName();
        ArrayList arrayList = (ArrayList) role.getRoleValue();
        ArrayList arrayList2 = (ArrayList) ((ArrayList) list).clone();
        ArrayList arrayList3 = new ArrayList();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ObjectName objectName = (ObjectName) it.next();
            int indexOf = arrayList2.indexOf(objectName);
            if (indexOf == -1) {
                if (addNewMBeanReference(objectName, str, roleName)) {
                    arrayList3.add(objectName);
                }
            } else {
                arrayList2.remove(indexOf);
            }
        }
        ArrayList arrayList4 = new ArrayList();
        Iterator it2 = arrayList2.iterator();
        while (it2.hasNext()) {
            ObjectName objectName2 = (ObjectName) it2.next();
            if (removeMBeanReference(objectName2, str, roleName, false)) {
                arrayList4.add(objectName2);
            }
        }
        updateUnregistrationListener(arrayList3, arrayList4);
        if (isTraceOn()) {
            trace("updateRoleMap: exiting", null);
        }
    }

    @Override // javax.management.relation.RelationServiceMBean
    public void removeRelation(String str) throws RelationServiceNotRegisteredException, IllegalArgumentException, RelationNotFoundException {
        String str2;
        isActive();
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("removeRelation: entering", str);
        }
        Object relation = getRelation(str);
        if (relation instanceof ObjectName) {
            ArrayList arrayList = new ArrayList();
            arrayList.add((ObjectName) relation);
            updateUnregistrationListener(null, arrayList);
        }
        sendRelationRemovalNotification(str, null);
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        synchronized (this.myRefedMBeanObjName2RelIdsMap) {
            for (ObjectName objectName : this.myRefedMBeanObjName2RelIdsMap.keySet()) {
                HashMap hashMap = (HashMap) this.myRefedMBeanObjName2RelIdsMap.get(objectName);
                if (hashMap.containsKey(str)) {
                    hashMap.remove(str);
                    arrayList2.add(objectName);
                }
                if (hashMap.isEmpty()) {
                    arrayList3.add(objectName);
                }
            }
            Iterator it = arrayList3.iterator();
            while (it.hasNext()) {
                this.myRefedMBeanObjName2RelIdsMap.remove((ObjectName) it.next());
            }
        }
        synchronized (this.myRelId2ObjMap) {
            this.myRelId2ObjMap.remove(str);
        }
        if (relation instanceof ObjectName) {
            synchronized (this.myRelMBeanObjName2RelIdMap) {
                this.myRelMBeanObjName2RelIdMap.remove((ObjectName) relation);
            }
        }
        synchronized (this.myRelId2RelTypeMap) {
            str2 = (String) this.myRelId2RelTypeMap.get(str);
            this.myRelId2RelTypeMap.remove(str);
        }
        synchronized (this.myRelType2RelIdsMap) {
            ArrayList arrayList4 = (ArrayList) this.myRelType2RelIdsMap.get(str2);
            if (arrayList4 != null) {
                arrayList4.remove(str);
                if (arrayList4.isEmpty()) {
                    this.myRelType2RelIdsMap.remove(str2);
                }
            }
        }
        if (isTraceOn()) {
            trace("removeRelation: exiting", null);
        }
    }

    @Override // javax.management.relation.RelationServiceMBean
    public void purgeRelations() throws RelationServiceNotRegisteredException {
        ArrayList arrayList;
        if (isTraceOn()) {
            trace("purgeRelations: entering", null);
        }
        isActive();
        synchronized (this.myUnregNtfList) {
            arrayList = (ArrayList) this.myUnregNtfList.clone();
            this.myUnregNtfList = new ArrayList();
        }
        ArrayList arrayList2 = new ArrayList();
        HashMap hashMap = new HashMap();
        synchronized (this.myRefedMBeanObjName2RelIdsMap) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                ObjectName mBeanName = ((MBeanServerNotification) it.next()).getMBeanName();
                arrayList2.add(mBeanName);
                hashMap.put(mBeanName, (HashMap) this.myRefedMBeanObjName2RelIdsMap.get(mBeanName));
                this.myRefedMBeanObjName2RelIdsMap.remove(mBeanName);
            }
        }
        updateUnregistrationListener(null, arrayList2);
        Iterator it2 = arrayList.iterator();
        while (it2.hasNext()) {
            ObjectName mBeanName2 = ((MBeanServerNotification) it2.next()).getMBeanName();
            HashMap hashMap2 = (HashMap) hashMap.get(mBeanName2);
            for (String str : hashMap2.keySet()) {
                try {
                    handleReferenceUnregistration(str, mBeanName2, (ArrayList) hashMap2.get(str));
                } catch (RelationNotFoundException e) {
                    throw new RuntimeException(e.getMessage());
                } catch (RoleNotFoundException e2) {
                    throw new RuntimeException(e2.getMessage());
                }
            }
        }
        if (isTraceOn()) {
            trace("purgeRelations: exiting", null);
        }
    }

    @Override // javax.management.relation.RelationServiceMBean
    public Map findReferencingRelations(ObjectName objectName, String str, String str2) throws IllegalArgumentException {
        ArrayList arrayList;
        String str3;
        if (objectName == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("findReferencingRelations: entering", new String(new StringBuffer().append("theMBeanName ").append(objectName.toString()).append(", theRelTypeName ").append(str).append(", theRoleName ").append(str2).toString()));
        }
        HashMap hashMap = new HashMap();
        synchronized (this.myRefedMBeanObjName2RelIdsMap) {
            HashMap hashMap2 = (HashMap) this.myRefedMBeanObjName2RelIdsMap.get(objectName);
            if (hashMap2 != null) {
                Set<String> keySet = hashMap2.keySet();
                if (str == null) {
                    arrayList = new ArrayList(keySet);
                } else {
                    arrayList = new ArrayList();
                    for (String str4 : keySet) {
                        synchronized (this.myRelId2RelTypeMap) {
                            str3 = (String) this.myRelId2RelTypeMap.get(str4);
                        }
                        if (str3.equals(str)) {
                            arrayList.add(str4);
                        }
                    }
                }
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    String str5 = (String) it.next();
                    ArrayList arrayList2 = (ArrayList) hashMap2.get(str5);
                    if (str2 == null) {
                        hashMap.put(str5, (ArrayList) arrayList2.clone());
                    } else if (arrayList2.contains(str2)) {
                        ArrayList arrayList3 = new ArrayList();
                        arrayList3.add(str2);
                        hashMap.put(str5, arrayList3);
                    }
                }
            }
        }
        if (isTraceOn()) {
            trace("findReferencingRelations: exiting", null);
        }
        return hashMap;
    }

    @Override // javax.management.relation.RelationServiceMBean
    public Map findAssociatedMBeans(ObjectName objectName, String str, String str2) throws IllegalArgumentException {
        if (objectName == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("findAssociatedMBeans: entering", new String(new StringBuffer().append("theMBeanName ").append(objectName.toString()).append(", theRelTypeName ").append(str).append(", theRoleName ").append(str2).toString()));
        }
        HashMap hashMap = (HashMap) findReferencingRelations(objectName, str, str2);
        HashMap hashMap2 = new HashMap();
        for (String str3 : hashMap.keySet()) {
            try {
                for (ObjectName objectName2 : ((HashMap) getReferencedMBeans(str3)).keySet()) {
                    if (!objectName2.equals(objectName)) {
                        ArrayList arrayList = (ArrayList) hashMap2.get(objectName2);
                        if (arrayList == null) {
                            ArrayList arrayList2 = new ArrayList();
                            arrayList2.add(str3);
                            hashMap2.put(objectName2, arrayList2);
                        } else {
                            arrayList.add(str3);
                        }
                    }
                }
            } catch (RelationNotFoundException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        if (isTraceOn()) {
            trace("findReferencingRelations: exiting", null);
        }
        return hashMap2;
    }

    @Override // javax.management.relation.RelationServiceMBean
    public List findRelationsOfType(String str) throws IllegalArgumentException, RelationTypeNotFoundException {
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("findRelationsOfType: entering", str);
        }
        getRelationType(str);
        ArrayList arrayList = new ArrayList();
        synchronized (this.myRelType2RelIdsMap) {
            ArrayList arrayList2 = (ArrayList) this.myRelType2RelIdsMap.get(str);
            if (arrayList2 != null) {
                arrayList = (ArrayList) arrayList2.clone();
            }
        }
        if (isTraceOn()) {
            trace("findRelationsOfType: exiting", null);
        }
        return arrayList;
    }

    @Override // javax.management.relation.RelationServiceMBean
    public List getRole(String str, String str2) throws RelationServiceNotRegisteredException, IllegalArgumentException, RelationNotFoundException, RoleNotFoundException {
        ArrayList arrayList;
        if (str == null || str2 == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("getRole: entering", new StringBuffer().append("theRelId ").append(str).append(", theRoleName ").append(str2).toString());
        }
        isActive();
        Object relation = getRelation(str);
        if (relation instanceof RelationSupport) {
            arrayList = (ArrayList) ((RelationSupport) relation).getRoleInt(str2, true, this, false);
        } else {
            try {
                arrayList = (ArrayList) this.myMBeanServer.invoke((ObjectName) relation, "getRole", new Object[]{str2}, new String[]{"java.lang.String"});
            } catch (InstanceNotFoundException e) {
                throw new RuntimeException(e.getMessage());
            } catch (MBeanException e2) {
                Exception targetException = e2.getTargetException();
                if (targetException instanceof RoleNotFoundException) {
                    throw ((RoleNotFoundException) targetException);
                }
                throw new RuntimeException(targetException.getMessage());
            } catch (ReflectionException e3) {
                throw new RuntimeException(e3.getMessage());
            }
        }
        if (isTraceOn()) {
            trace("getRole: exiting", null);
        }
        return arrayList;
    }

    @Override // javax.management.relation.RelationServiceMBean
    public RoleResult getRoles(String str, String[] strArr) throws RelationServiceNotRegisteredException, IllegalArgumentException, RelationNotFoundException {
        RoleResult roleResult;
        if (str == null || strArr == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("getRoles: entering", str);
        }
        isActive();
        Object relation = getRelation(str);
        if (relation instanceof RelationSupport) {
            roleResult = ((RelationSupport) relation).getRolesInt(strArr, true, this);
        } else {
            Object[] objArr = {strArr};
            String[] strArr2 = new String[1];
            try {
                strArr2[0] = strArr.getClass().getName();
            } catch (Exception e) {
            }
            try {
                roleResult = (RoleResult) this.myMBeanServer.invoke((ObjectName) relation, "getRoles", objArr, strArr2);
            } catch (InstanceNotFoundException e2) {
                throw new RuntimeException(e2.getMessage());
            } catch (MBeanException e3) {
                throw new RuntimeException(e3.getTargetException().getMessage());
            } catch (ReflectionException e4) {
                throw new RuntimeException(e4.getMessage());
            }
        }
        if (isTraceOn()) {
            trace("getRoles: exiting", null);
        }
        return roleResult;
    }

    @Override // javax.management.relation.RelationServiceMBean
    public RoleResult getAllRoles(String str) throws IllegalArgumentException, RelationNotFoundException, RelationServiceNotRegisteredException {
        RoleResult roleResult;
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("getAllRoles: entering", str);
        }
        Object relation = getRelation(str);
        if (relation instanceof RelationSupport) {
            roleResult = ((RelationSupport) relation).getAllRolesInt(true, this);
        } else {
            try {
                roleResult = (RoleResult) this.myMBeanServer.getAttribute((ObjectName) relation, "AllRoles");
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        if (isTraceOn()) {
            trace("getAllRoles: exiting", null);
        }
        return roleResult;
    }

    @Override // javax.management.relation.RelationServiceMBean
    public Integer getRoleCardinality(String str, String str2) throws IllegalArgumentException, RelationNotFoundException, RoleNotFoundException {
        Integer num;
        if (str == null || str2 == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("getRoleCardinality: entering", new StringBuffer().append("theRelId ").append(str).append(", theRoleName ").append(str2).toString());
        }
        Object relation = getRelation(str);
        if (relation instanceof RelationSupport) {
            num = ((RelationSupport) relation).getRoleCardinality(str2);
        } else {
            try {
                num = (Integer) this.myMBeanServer.invoke((ObjectName) relation, "getRoleCardinality", new Object[]{str2}, new String[]{"java.lang.String"});
            } catch (InstanceNotFoundException e) {
                throw new RuntimeException(e.getMessage());
            } catch (MBeanException e2) {
                Exception targetException = e2.getTargetException();
                if (targetException instanceof RoleNotFoundException) {
                    throw ((RoleNotFoundException) targetException);
                }
                throw new RuntimeException(targetException.getMessage());
            } catch (ReflectionException e3) {
                throw new RuntimeException(e3.getMessage());
            }
        }
        if (isTraceOn()) {
            trace("getRoleCardinality: exiting", null);
        }
        return num;
    }

    @Override // javax.management.relation.RelationServiceMBean
    public void setRole(String str, Role role) throws RelationServiceNotRegisteredException, IllegalArgumentException, RelationNotFoundException, RoleNotFoundException, InvalidRoleValueException {
        if (str == null || role == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("setRole: entering", new String(new StringBuffer().append("theRelId ").append(str).append(", theRole ").append(role.toString()).toString()));
        }
        isActive();
        Object relation = getRelation(str);
        if (relation instanceof RelationSupport) {
            try {
                ((RelationSupport) relation).setRoleInt(role, true, this, false);
            } catch (RelationTypeNotFoundException e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            new Object[1][0] = role;
            new String[1][0] = "javax.management.relation.Role";
            try {
                this.myMBeanServer.setAttribute((ObjectName) relation, new Attribute("Role", role));
            } catch (AttributeNotFoundException e2) {
                throw new RuntimeException(e2.getMessage());
            } catch (InstanceNotFoundException e3) {
                throw new RuntimeException(e3.getMessage());
            } catch (InvalidAttributeValueException e4) {
                throw new RuntimeException(e4.getMessage());
            } catch (MBeanException e5) {
                Exception targetException = e5.getTargetException();
                if (targetException instanceof RoleNotFoundException) {
                    throw ((RoleNotFoundException) targetException);
                }
                if (targetException instanceof InvalidRoleValueException) {
                    throw ((InvalidRoleValueException) targetException);
                }
                throw new RuntimeException(targetException.getMessage());
            } catch (ReflectionException e6) {
                throw new RuntimeException(e6.getMessage());
            }
        }
        if (isTraceOn()) {
            trace("setRole: exiting", null);
        }
    }

    @Override // javax.management.relation.RelationServiceMBean
    public RoleResult setRoles(String str, RoleList roleList) throws RelationServiceNotRegisteredException, IllegalArgumentException, RelationNotFoundException {
        RoleResult rolesInt;
        if (str == null || roleList == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("setRoles: entering", new String(new StringBuffer().append("theRelId ").append(str).append(", theRoleList ").append(roleList.toString()).toString()));
        }
        isActive();
        Object relation = getRelation(str);
        if (relation instanceof RelationSupport) {
            try {
                rolesInt = ((RelationSupport) relation).setRolesInt(roleList, true, this);
            } catch (RelationTypeNotFoundException e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            try {
                rolesInt = (RoleResult) this.myMBeanServer.invoke((ObjectName) relation, "setRoles", new Object[]{roleList}, new String[]{"javax.management.relation.RoleList"});
            } catch (InstanceNotFoundException e2) {
                throw new RuntimeException(e2.getMessage());
            } catch (MBeanException e3) {
                throw new RuntimeException(e3.getTargetException().getMessage());
            } catch (ReflectionException e4) {
                throw new RuntimeException(e4.getMessage());
            }
        }
        if (isTraceOn()) {
            trace("setRoles: exiting", null);
        }
        return rolesInt;
    }

    @Override // javax.management.relation.RelationServiceMBean
    public Map getReferencedMBeans(String str) throws IllegalArgumentException, RelationNotFoundException {
        HashMap hashMap;
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("getReferencedMBeans: entering", str);
        }
        Object relation = getRelation(str);
        if (relation instanceof RelationSupport) {
            hashMap = (HashMap) ((RelationSupport) relation).getReferencedMBeans();
        } else {
            try {
                hashMap = (HashMap) this.myMBeanServer.getAttribute((ObjectName) relation, "ReferencedMBeans");
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        if (isTraceOn()) {
            trace("getReferencedMBeans: exiting", null);
        }
        return hashMap;
    }

    @Override // javax.management.relation.RelationServiceMBean
    public String getRelationTypeName(String str) throws IllegalArgumentException, RelationNotFoundException {
        String str2;
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("getRelationTypeName: entering", str);
        }
        Object relation = getRelation(str);
        if (relation instanceof RelationSupport) {
            str2 = ((RelationSupport) relation).getRelationTypeName();
        } else {
            try {
                str2 = (String) this.myMBeanServer.getAttribute((ObjectName) relation, "RelationTypeName");
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        if (isTraceOn()) {
            trace("getRelationTypeName: exiting", null);
        }
        return str2;
    }

    @Override // javax.management.NotificationListener
    public void handleNotification(Notification notification, Object obj) {
        String str;
        if (notification == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isTraceOn()) {
            trace("handleNotification: entering", notification.toString());
        }
        if ((notification instanceof MBeanServerNotification) && notification.getType().equals(MBeanServerNotification.UNREGISTRATION_NOTIFICATION)) {
            ObjectName mBeanName = ((MBeanServerNotification) notification).getMBeanName();
            boolean z = false;
            synchronized (this.myRefedMBeanObjName2RelIdsMap) {
                if (this.myRefedMBeanObjName2RelIdsMap.containsKey(mBeanName)) {
                    synchronized (this.myUnregNtfList) {
                        this.myUnregNtfList.add(notification);
                    }
                    z = true;
                }
                if (z && this.myPurgeFlg) {
                    try {
                        purgeRelations();
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }
            }
            synchronized (this.myRelMBeanObjName2RelIdMap) {
                str = (String) this.myRelMBeanObjName2RelIdMap.get(mBeanName);
            }
            if (str != null) {
                try {
                    removeRelation(str);
                } catch (Exception e2) {
                    throw new RuntimeException(e2.getMessage());
                }
            }
        }
        if (isTraceOn()) {
            trace("handleNotification: exiting", null);
        }
    }

    @Override // javax.management.NotificationBroadcasterSupport, javax.management.NotificationBroadcaster
    public MBeanNotificationInfo[] getNotificationInfo() {
        if (isTraceOn()) {
            trace("getNotificationInfo: entering", null);
        }
        MBeanNotificationInfo[] mBeanNotificationInfoArr = {new MBeanNotificationInfo(new String[]{RelationNotification.RELATION_BASIC_CREATION, RelationNotification.RELATION_MBEAN_CREATION, RelationNotification.RELATION_BASIC_UPDATE, RelationNotification.RELATION_MBEAN_UPDATE, RelationNotification.RELATION_BASIC_REMOVAL, RelationNotification.RELATION_MBEAN_REMOVAL}, "RelationNotification", "Sent when a relation is created, updated or deleted.")};
        if (isTraceOn()) {
            trace("getNotificationInfo: exiting", null);
        }
        return mBeanNotificationInfoArr;
    }

    private void addRelationTypeInt(RelationType relationType) throws IllegalArgumentException, InvalidRelationTypeException {
        if (relationType == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isDebugOn()) {
            debug("addRelationTypeInt: entering", null);
        }
        String relationTypeName = relationType.getRelationTypeName();
        if (getRelationType(relationTypeName) != null) {
            StringBuffer stringBuffer = new StringBuffer("There is already a relation type in the Relation Service with name ");
            stringBuffer.append(relationTypeName);
            throw new InvalidRelationTypeException(stringBuffer.toString());
        }
        synchronized (this.myRelType2ObjMap) {
            this.myRelType2ObjMap.put(relationTypeName, relationType);
        }
        if (relationType instanceof RelationTypeSupport) {
            ((RelationTypeSupport) relationType).setRelationServiceFlag(true);
        }
        if (isDebugOn()) {
            debug("addRelationTypeInt: exiting", null);
        }
    }

    RelationType getRelationType(String str) throws IllegalArgumentException, RelationTypeNotFoundException {
        RelationType relationType;
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isDebugOn()) {
            debug("getRelationType: entering", str);
        }
        synchronized (this.myRelType2ObjMap) {
            relationType = (RelationType) this.myRelType2ObjMap.get(str);
        }
        if (relationType == null) {
            StringBuffer stringBuffer = new StringBuffer("No relation type created in the Relation Service with the name ");
            stringBuffer.append(str);
            throw new RelationTypeNotFoundException(stringBuffer.toString());
        }
        if (isDebugOn()) {
            debug("getRelationType: exiting", null);
        }
        return relationType;
    }

    Object getRelation(String str) throws IllegalArgumentException, RelationNotFoundException {
        Object obj;
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isDebugOn()) {
            debug("getRelation: entering", str);
        }
        synchronized (this.myRelId2ObjMap) {
            obj = this.myRelId2ObjMap.get(str);
        }
        if (obj == null) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("No relation associated to relation id ");
            stringBuffer.append(str);
            throw new RelationNotFoundException(stringBuffer.toString());
        }
        if (isDebugOn()) {
            debug("getRelation: exiting", null);
        }
        return obj;
    }

    private boolean addNewMBeanReference(ObjectName objectName, String str, String str2) throws IllegalArgumentException {
        if (objectName == null || str == null || str2 == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isDebugOn()) {
            debug("addNewMBeanReference: entering", new String(new StringBuffer().append("theObjName ").append(objectName.toString()).append(", theRelId ").append(str).append(", theRoleName ").append(str2).toString()));
        }
        boolean z = false;
        synchronized (this.myRefedMBeanObjName2RelIdsMap) {
            HashMap hashMap = (HashMap) this.myRefedMBeanObjName2RelIdsMap.get(objectName);
            if (hashMap == null) {
                z = true;
                ArrayList arrayList = new ArrayList();
                arrayList.add(str2);
                HashMap hashMap2 = new HashMap();
                hashMap2.put(str, arrayList);
                this.myRefedMBeanObjName2RelIdsMap.put(objectName, hashMap2);
            } else {
                ArrayList arrayList2 = (ArrayList) hashMap.get(str);
                if (arrayList2 == null) {
                    ArrayList arrayList3 = new ArrayList();
                    arrayList3.add(str2);
                    hashMap.put(str, arrayList3);
                } else {
                    arrayList2.add(str2);
                }
            }
        }
        if (isDebugOn()) {
            debug("addNewMBeanReference: exiting", null);
        }
        return z;
    }

    private boolean removeMBeanReference(ObjectName objectName, String str, String str2, boolean z) throws IllegalArgumentException {
        if (objectName == null || str == null || str2 == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isDebugOn()) {
            debug("removeMBeanReference: entering", new String(new StringBuffer().append("theObjName ").append(objectName.toString()).append(", theRelId ").append(str).append(", theRoleName ").append(str2).append(", theAllRolesFlg ").append(z).toString()));
        }
        boolean z2 = false;
        synchronized (this.myRefedMBeanObjName2RelIdsMap) {
            HashMap hashMap = (HashMap) this.myRefedMBeanObjName2RelIdsMap.get(objectName);
            if (hashMap == null) {
                if (isDebugOn()) {
                    debug("removeMBeanReference: exiting", null);
                }
                return true;
            }
            ArrayList arrayList = new ArrayList();
            if (!z) {
                arrayList = (ArrayList) hashMap.get(str);
                int indexOf = arrayList.indexOf(str2);
                if (indexOf != -1) {
                    arrayList.remove(indexOf);
                }
            }
            if (arrayList.isEmpty() || z) {
                hashMap.remove(str);
            }
            if (hashMap.isEmpty()) {
                this.myRefedMBeanObjName2RelIdsMap.remove(objectName);
                z2 = true;
            }
            if (isDebugOn()) {
                debug("removeMBeanReference: exiting", null);
            }
            return z2;
        }
    }

    private void updateUnregistrationListener(List list, List list2) throws RelationServiceNotRegisteredException {
        if (list != null && list2 != null && list.isEmpty() && list2.isEmpty()) {
            return;
        }
        if (isDebugOn()) {
            StringBuffer stringBuffer = new StringBuffer();
            if (list != null) {
                stringBuffer.append(new StringBuffer().append("theNewRefList ").append(list.toString()).toString());
            }
            if (list2 != null) {
                stringBuffer.append(new StringBuffer().append(", theObsRefList").append(list2.toString()).toString());
            }
            debug("updateUnregistrationListener: entering", stringBuffer.toString());
        }
        isActive();
        if (list != null || list2 != null) {
            boolean z = false;
            if (this.myUnregNtfFilter == null) {
                this.myUnregNtfFilter = new MBeanServerNotificationFilter();
                z = true;
            }
            synchronized (this.myUnregNtfFilter) {
                if (list != null) {
                    Iterator it = list.iterator();
                    while (it.hasNext()) {
                        this.myUnregNtfFilter.enableObjectName((ObjectName) it.next());
                    }
                }
                if (list2 != null) {
                    Iterator it2 = list2.iterator();
                    while (it2.hasNext()) {
                        this.myUnregNtfFilter.disableObjectName((ObjectName) it2.next());
                    }
                }
                ObjectName objectName = null;
                try {
                    objectName = new ObjectName("JMImplementation:type=MBeanServerDelegate");
                } catch (MalformedObjectNameException e) {
                }
                if (z) {
                    try {
                        this.myMBeanServer.addNotificationListener(objectName, this, this.myUnregNtfFilter, (Object) null);
                    } catch (InstanceNotFoundException e2) {
                        throw new RelationServiceNotRegisteredException(e2.getMessage());
                    }
                }
            }
        }
        if (isDebugOn()) {
            debug("updateUnregistrationListener: exiting", null);
        }
    }

    private void addRelationInt(boolean z, RelationSupport relationSupport, ObjectName objectName, String str, String str2, RoleList roleList) throws IllegalArgumentException, RelationServiceNotRegisteredException, RoleNotFoundException, InvalidRelationIdException, RelationTypeNotFoundException, InvalidRoleValueException {
        if (str == null || str2 == null || ((z && (relationSupport == null || objectName != null)) || (!z && (objectName == null || relationSupport != null)))) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isDebugOn()) {
            StringBuffer stringBuffer = new StringBuffer(new StringBuffer().append("theRelBaseFlg ").append(z).append(", theRelId ").append(str).append(", theRelTypeName ").append(str2).toString());
            if (objectName != null) {
                stringBuffer.append(new StringBuffer().append(",  theRelObjName ").append(objectName.toString()).toString());
            }
            if (roleList != null) {
                stringBuffer.append(new StringBuffer().append(", theRoleList ").append(roleList.toString()).toString());
            }
            debug("addRelationInt: entering", stringBuffer.toString());
        }
        isActive();
        if (getRelation(str) != null) {
            StringBuffer stringBuffer2 = new StringBuffer("There is already a relation with id ");
            stringBuffer2.append(str);
            throw new InvalidRelationIdException(stringBuffer2.toString());
        }
        RelationType relationType = getRelationType(str2);
        ArrayList arrayList = (ArrayList) ((ArrayList) relationType.getRoleInfos()).clone();
        if (roleList != null) {
            Iterator it = roleList.iterator();
            while (it.hasNext()) {
                Role role = (Role) it.next();
                String roleName = role.getRoleName();
                List list = (ArrayList) role.getRoleValue();
                try {
                    RoleInfo roleInfo = relationType.getRoleInfo(roleName);
                    int intValue = checkRoleInt(2, roleName, list, roleInfo, false).intValue();
                    if (intValue != 0) {
                        throwRoleProblemException(intValue, roleName);
                    }
                    arrayList.remove(arrayList.indexOf(roleInfo));
                } catch (RoleInfoNotFoundException e) {
                    throw new RoleNotFoundException(e.getMessage());
                }
            }
        }
        initialiseMissingRoles(z, relationSupport, objectName, str, str2, arrayList);
        synchronized (this.myRelId2ObjMap) {
            if (z) {
                this.myRelId2ObjMap.put(str, relationSupport);
            } else {
                this.myRelId2ObjMap.put(str, objectName);
            }
        }
        synchronized (this.myRelId2RelTypeMap) {
            this.myRelId2RelTypeMap.put(str, str2);
        }
        synchronized (this.myRelType2RelIdsMap) {
            ArrayList arrayList2 = (ArrayList) this.myRelType2RelIdsMap.get(str2);
            boolean z2 = false;
            if (arrayList2 == null) {
                z2 = true;
                arrayList2 = new ArrayList();
            }
            arrayList2.add(str);
            if (z2) {
                this.myRelType2RelIdsMap.put(str2, arrayList2);
            }
        }
        Iterator it2 = roleList.iterator();
        while (it2.hasNext()) {
            try {
                updateRoleMap(str, (Role) it2.next(), new ArrayList());
            } catch (RelationNotFoundException e2) {
            }
        }
        try {
            sendRelationCreationNotification(str);
        } catch (RelationNotFoundException e3) {
        }
        if (isDebugOn()) {
            debug("addRelationInt: exiting", null);
        }
    }

    private Integer checkRoleInt(int i, String str, List list, RoleInfo roleInfo, boolean z) throws IllegalArgumentException {
        if (str == null || roleInfo == null || (i == 2 && list == null)) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isDebugOn()) {
            StringBuffer stringBuffer = new StringBuffer(new StringBuffer().append("theChkType ").append(i).append(", theRoleName ").append(str).append(", theRoleInfo ").append(roleInfo.toString()).append(", theWriteChkFlg ").append(z).toString());
            if (list != null) {
                stringBuffer.append(new StringBuffer().append(", theRoleValue ").append(list.toString()).toString());
            }
            debug("checkRoleInt: entering", stringBuffer.toString());
        }
        if (!str.equals(roleInfo.getName())) {
            if (isDebugOn()) {
                debug("checkRoleInt: exiting", null);
            }
            return new Integer(1);
        } else if (i == 1) {
            if (!roleInfo.isReadable()) {
                if (isDebugOn()) {
                    debug("checkRoleInt: exiting", null);
                }
                return new Integer(2);
            }
            if (isDebugOn()) {
                debug("checkRoleInt: exiting", null);
            }
            return new Integer(0);
        } else if (z && !roleInfo.isWritable()) {
            if (isDebugOn()) {
                debug("checkRoleInt: exiting", null);
            }
            return new Integer(3);
        } else {
            int size = list.size();
            if (!roleInfo.checkMinDegree(size)) {
                if (isDebugOn()) {
                    debug("checkRoleInt: exiting", null);
                }
                return new Integer(4);
            } else if (!roleInfo.checkMaxDegree(size)) {
                if (isDebugOn()) {
                    debug("checkRoleInt: exiting", null);
                }
                return new Integer(5);
            } else {
                String refMBeanClassName = roleInfo.getRefMBeanClassName();
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    ObjectName objectName = (ObjectName) it.next();
                    if (objectName == null) {
                        if (isDebugOn()) {
                            debug("checkRoleInt: exiting", null);
                        }
                        return new Integer(7);
                    }
                    try {
                        if (!this.myMBeanServer.isInstanceOf(objectName, refMBeanClassName)) {
                            if (isDebugOn()) {
                                debug("checkRoleInt: exiting", null);
                            }
                            return new Integer(6);
                        }
                    } catch (InstanceNotFoundException e) {
                        if (isDebugOn()) {
                            debug("checkRoleInt: exiting", null);
                        }
                        return new Integer(7);
                    }
                }
                if (isDebugOn()) {
                    debug("checkRoleInt: exiting", null);
                }
                return new Integer(0);
            }
        }
    }

    private void initialiseMissingRoles(boolean z, RelationSupport relationSupport, ObjectName objectName, String str, String str2, List list) throws IllegalArgumentException, RelationServiceNotRegisteredException, InvalidRoleValueException {
        if ((z && (relationSupport == null || objectName != null)) || ((!z && (objectName == null || relationSupport != null)) || str == null || str2 == null || list == null)) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isDebugOn()) {
            StringBuffer stringBuffer = new StringBuffer(new StringBuffer().append("theRelBaseFlg ").append(z).append(", theRelId ").append(str).append(", theRelTypeName ").append(str2).append(", theRoleInfoList ").append(list).toString());
            if (objectName != null) {
                stringBuffer.append(objectName.toString());
            }
            debug("initialiseMissingRoles: entering", stringBuffer.toString());
        }
        isActive();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Role role = new Role(((RoleInfo) it.next()).getName(), new ArrayList());
            if (z) {
                try {
                    relationSupport.setRoleInt(role, true, this, false);
                } catch (RelationNotFoundException e) {
                    throw new RuntimeException(e.getMessage());
                } catch (RelationTypeNotFoundException e2) {
                    throw new RuntimeException(e2.getMessage());
                } catch (RoleNotFoundException e3) {
                    throw new RuntimeException(e3.getMessage());
                }
            } else {
                new Object[1][0] = role;
                new String[1][0] = "javax.management.relation.Role";
                try {
                    this.myMBeanServer.setAttribute(objectName, new Attribute("Role", role));
                } catch (AttributeNotFoundException e4) {
                    throw new RuntimeException(e4.getMessage());
                } catch (InstanceNotFoundException e5) {
                    throw new RuntimeException(e5.getMessage());
                } catch (InvalidAttributeValueException e6) {
                    throw new RuntimeException(e6.getMessage());
                } catch (MBeanException e7) {
                    Exception targetException = e7.getTargetException();
                    if (targetException instanceof InvalidRoleValueException) {
                        throw ((InvalidRoleValueException) targetException);
                    }
                    throw new RuntimeException(targetException.getMessage());
                } catch (ReflectionException e8) {
                    throw new RuntimeException(e8.getMessage());
                }
            }
        }
        if (isDebugOn()) {
            debug("initializeMissingRoles: exiting", null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void throwRoleProblemException(int i, String str) throws IllegalArgumentException, RoleNotFoundException, InvalidRoleValueException {
        if (str == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        boolean z = false;
        String str2 = null;
        switch (i) {
            case 1:
                str2 = " does not exist in relation.";
                z = true;
                break;
            case 2:
                str2 = " is not readable.";
                z = true;
                break;
            case 3:
                str2 = " is not writable.";
                z = true;
                break;
            case 4:
                str2 = " has a number of MBean references less than the expected minimum degree.";
                z = true;
                break;
            case 5:
                str2 = " has a number of MBean references greater than the expected maximum degree.";
                z = true;
                break;
            case 6:
                str2 = " has an MBean reference to an MBean not of the expected class of references for that role.";
                z = true;
                break;
            case 7:
                str2 = " has a reference to null or to an MBean not registered.";
                z = true;
                break;
        }
        StringBuffer stringBuffer = new StringBuffer(str);
        stringBuffer.append(str2);
        String stringBuffer2 = stringBuffer.toString();
        if (z) {
            throw new RoleNotFoundException(stringBuffer2);
        }
        if (z) {
            throw new InvalidRoleValueException(stringBuffer2);
        }
    }

    private void sendNotificationInt(int i, String str, String str2, List list, String str3, List list2, List list3) throws IllegalArgumentException, RelationNotFoundException {
        String str4;
        if (str == null || str2 == null || ((i != 3 && list != null) || (i == 2 && (str3 == null || list2 == null || list3 == null)))) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isDebugOn()) {
            StringBuffer stringBuffer = new StringBuffer(new StringBuffer().append("theIntNtfType ").append(i).append(", theMsg ").append(str).append(", theRelId ").append(str2).toString());
            if (list != null) {
                stringBuffer.append(new StringBuffer().append(", theUnregMBeanList ").append(list.toString()).toString());
            }
            if (str3 != null) {
                stringBuffer.append(new StringBuffer().append(", theRoleName ").append(str3).toString());
            }
            if (list2 != null) {
                stringBuffer.append(new StringBuffer().append(", theRoleNewValue ").append(list2.toString()).toString());
            }
            if (list3 != null) {
                stringBuffer.append(new StringBuffer().append(", theOldRoleValue ").append(list3.toString()).toString());
            }
            debug("sendNotificationInt: entering", stringBuffer.toString());
        }
        synchronized (this.myRelId2RelTypeMap) {
            str4 = (String) this.myRelId2RelTypeMap.get(str2);
        }
        ObjectName isRelationMBean = isRelationMBean(str2);
        String str5 = null;
        if (isRelationMBean != null) {
            switch (i) {
                case 1:
                    str5 = RelationNotification.RELATION_MBEAN_CREATION;
                    break;
                case 2:
                    str5 = RelationNotification.RELATION_MBEAN_UPDATE;
                    break;
                case 3:
                    str5 = RelationNotification.RELATION_MBEAN_REMOVAL;
                    break;
            }
        } else {
            switch (i) {
                case 1:
                    str5 = RelationNotification.RELATION_BASIC_CREATION;
                    break;
                case 2:
                    str5 = RelationNotification.RELATION_BASIC_UPDATE;
                    break;
                case 3:
                    str5 = RelationNotification.RELATION_BASIC_REMOVAL;
                    break;
            }
        }
        Long notificationSequenceNumber = getNotificationSequenceNumber();
        long time = new Date().getTime();
        RelationNotification relationNotification = null;
        if (str5.equals(RelationNotification.RELATION_BASIC_CREATION) || str5.equals(RelationNotification.RELATION_MBEAN_CREATION) || str5.equals(RelationNotification.RELATION_BASIC_REMOVAL) || str5.equals(RelationNotification.RELATION_MBEAN_REMOVAL)) {
            relationNotification = new RelationNotification(str5, this, notificationSequenceNumber.longValue(), time, str, str2, str4, isRelationMBean, list);
        } else if (str5.equals(RelationNotification.RELATION_BASIC_UPDATE) || str5.equals(RelationNotification.RELATION_MBEAN_UPDATE)) {
            relationNotification = new RelationNotification(str5, this, notificationSequenceNumber.longValue(), time, str, str2, str4, isRelationMBean, str3, list2, list3);
        }
        sendNotification(relationNotification);
        if (isDebugOn()) {
            debug("sendNotificationInt: exiting", null);
        }
    }

    private void handleReferenceUnregistration(String str, ObjectName objectName, List list) throws IllegalArgumentException, RelationServiceNotRegisteredException, RelationNotFoundException, RoleNotFoundException {
        if (str == null || list == null || objectName == null) {
            throw new IllegalArgumentException("Invalid parameter.");
        }
        if (isDebugOn()) {
            debug("handleReferenceUnregistration: entering", new String(new StringBuffer().append("theRelId ").append(str).append(", theRoleNameList ").append(list.toString()).append("theObjName ").append(objectName.toString()).toString()));
        }
        isActive();
        String relationTypeName = getRelationTypeName(str);
        Object relation = getRelation(str);
        boolean z = false;
        Iterator it = list.iterator();
        while (it.hasNext() && !z) {
            String str2 = (String) it.next();
            try {
                if (!getRoleInfo(relationTypeName, str2).checkMinDegree(getRoleCardinality(str, str2).intValue() - 1)) {
                    z = true;
                }
            } catch (RelationTypeNotFoundException e) {
                throw new RuntimeException(e.getMessage());
            } catch (RoleInfoNotFoundException e2) {
                throw new RuntimeException(e2.getMessage());
            }
        }
        if (z) {
            removeRelation(str);
        } else {
            Iterator it2 = list.iterator();
            while (it2.hasNext()) {
                String str3 = (String) it2.next();
                if (relation instanceof RelationSupport) {
                    try {
                        ((RelationSupport) relation).handleMBeanUnregistrationInt(objectName, str3, true, this);
                    } catch (InvalidRoleValueException e3) {
                        throw new RuntimeException(e3.getMessage());
                    } catch (RelationTypeNotFoundException e4) {
                        throw new RuntimeException(e4.getMessage());
                    }
                } else {
                    try {
                        this.myMBeanServer.invoke((ObjectName) relation, "handleMBeanUnregistration", new Object[]{objectName, str3}, new String[]{"javax.management.ObjectName", "java.lang.String"});
                    } catch (InstanceNotFoundException e5) {
                        throw new RuntimeException(e5.getMessage());
                    } catch (MBeanException e6) {
                        throw new RuntimeException(e6.getTargetException().getMessage());
                    } catch (ReflectionException e7) {
                        throw new RuntimeException(e7.getMessage());
                    }
                }
            }
        }
        if (isDebugOn()) {
            debug("handleReferenceUnregistration: exiting", null);
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
