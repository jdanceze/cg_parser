package javax.management.relation;

import java.io.Serializable;
import java.util.List;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/relation/RelationType.class */
public interface RelationType extends Serializable {
    String getRelationTypeName();

    List getRoleInfos();

    RoleInfo getRoleInfo(String str) throws IllegalArgumentException, RoleInfoNotFoundException;
}
