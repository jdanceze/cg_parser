package javax.enterprise.deploy.spi.status;

import java.util.EventObject;
import javax.enterprise.deploy.spi.TargetModuleID;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/enterprise/deploy/spi/status/ProgressEvent.class */
public class ProgressEvent extends EventObject {
    private DeploymentStatus statuscode;
    private TargetModuleID targetModuleID;

    public ProgressEvent(Object source, TargetModuleID targetModuleID, DeploymentStatus sCode) {
        super(source);
        this.statuscode = sCode;
        this.targetModuleID = targetModuleID;
    }

    public TargetModuleID getTargetModuleID() {
        return this.targetModuleID;
    }

    public DeploymentStatus getDeploymentStatus() {
        return this.statuscode;
    }
}
