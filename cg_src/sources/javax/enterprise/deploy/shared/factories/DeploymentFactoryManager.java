package javax.enterprise.deploy.shared.factories;

import java.util.Vector;
import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.exceptions.DeploymentManagerCreationException;
import javax.enterprise.deploy.spi.factories.DeploymentFactory;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/enterprise/deploy/shared/factories/DeploymentFactoryManager.class */
public final class DeploymentFactoryManager {
    private Vector deploymentFactories;
    private static DeploymentFactoryManager deploymentFactoryManager = null;

    private DeploymentFactoryManager() {
        this.deploymentFactories = null;
        this.deploymentFactories = new Vector();
    }

    public static DeploymentFactoryManager getInstance() {
        if (deploymentFactoryManager == null) {
            deploymentFactoryManager = new DeploymentFactoryManager();
        }
        return deploymentFactoryManager;
    }

    public DeploymentFactory[] getDeploymentFactories() {
        Vector deploymentFactoriesSnapShot;
        synchronized (this) {
            deploymentFactoriesSnapShot = (Vector) this.deploymentFactories.clone();
        }
        DeploymentFactory[] factoriesArray = new DeploymentFactory[deploymentFactoriesSnapShot.size()];
        deploymentFactoriesSnapShot.copyInto(factoriesArray);
        return factoriesArray;
    }

    public DeploymentManager getDeploymentManager(String uri, String username, String password) throws DeploymentManagerCreationException {
        try {
            DeploymentFactory[] factories = getDeploymentFactories();
            for (int factoryIndex = 0; factoryIndex < factories.length; factoryIndex++) {
                if (factories[factoryIndex].handlesURI(uri)) {
                    return factories[factoryIndex].getDeploymentManager(uri, username, password);
                }
            }
            throw new DeploymentManagerCreationException(new StringBuffer().append("URL [").append(uri).append("] not supported by any available factories").toString());
        } catch (Throwable th) {
            throw new DeploymentManagerCreationException("Could not get DeploymentManager");
        }
    }

    public void registerDeploymentFactory(DeploymentFactory factory) {
        this.deploymentFactories.add(factory);
    }

    public DeploymentManager getDisconnectedDeploymentManager(String uri) throws DeploymentManagerCreationException {
        try {
            DeploymentFactory[] factories = getDeploymentFactories();
            for (int factoryIndex = 0; factoryIndex < factories.length; factoryIndex++) {
                if (factories[factoryIndex].handlesURI(uri)) {
                    return factories[factoryIndex].getDisconnectedDeploymentManager(uri);
                }
            }
            throw new DeploymentManagerCreationException(new StringBuffer().append("URL [").append(uri).append("] not supported by any available factories").toString());
        } catch (Throwable th) {
            throw new DeploymentManagerCreationException("Could not get DeploymentManager");
        }
    }
}
