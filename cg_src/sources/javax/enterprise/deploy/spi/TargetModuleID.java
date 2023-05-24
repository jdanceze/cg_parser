package javax.enterprise.deploy.spi;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/enterprise/deploy/spi/TargetModuleID.class */
public interface TargetModuleID {
    Target getTarget();

    String getModuleID();

    String getWebURL();

    String toString();

    TargetModuleID getParentTargetModuleID();

    TargetModuleID[] getChildTargetModuleID();
}
