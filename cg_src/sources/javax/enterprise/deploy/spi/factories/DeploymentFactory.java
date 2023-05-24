package javax.enterprise.deploy.spi.factories;

import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.exceptions.DeploymentManagerCreationException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/enterprise/deploy/spi/factories/DeploymentFactory.class */
public interface DeploymentFactory {
    boolean handlesURI(String str);

    DeploymentManager getDeploymentManager(String str, String str2, String str3) throws DeploymentManagerCreationException;

    DeploymentManager getDisconnectedDeploymentManager(String str) throws DeploymentManagerCreationException;

    String getDisplayName();

    String getProductVersion();
}
