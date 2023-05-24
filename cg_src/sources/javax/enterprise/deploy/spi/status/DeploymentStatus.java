package javax.enterprise.deploy.spi.status;

import javax.enterprise.deploy.shared.ActionType;
import javax.enterprise.deploy.shared.CommandType;
import javax.enterprise.deploy.shared.StateType;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/enterprise/deploy/spi/status/DeploymentStatus.class */
public interface DeploymentStatus {
    StateType getState();

    CommandType getCommand();

    ActionType getAction();

    String getMessage();

    boolean isCompleted();

    boolean isFailed();

    boolean isRunning();
}
