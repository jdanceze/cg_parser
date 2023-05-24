package javax.management.relation;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/relation/RelationSupportMBean.class */
public interface RelationSupportMBean extends Relation {
    Boolean isInRelationService();

    void setRelationServiceManagementFlag(Boolean bool) throws IllegalArgumentException;
}
