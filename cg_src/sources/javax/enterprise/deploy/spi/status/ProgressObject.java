package javax.enterprise.deploy.spi.status;

import javax.enterprise.deploy.spi.TargetModuleID;
import javax.enterprise.deploy.spi.exceptions.OperationUnsupportedException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/enterprise/deploy/spi/status/ProgressObject.class */
public interface ProgressObject {
    DeploymentStatus getDeploymentStatus();

    TargetModuleID[] getResultTargetModuleIDs();

    ClientConfiguration getClientConfiguration(TargetModuleID targetModuleID);

    boolean isCancelSupported();

    void cancel() throws OperationUnsupportedException;

    boolean isStopSupported();

    void stop() throws OperationUnsupportedException;

    void addProgressListener(ProgressListener progressListener);

    void removeProgressListener(ProgressListener progressListener);
}
