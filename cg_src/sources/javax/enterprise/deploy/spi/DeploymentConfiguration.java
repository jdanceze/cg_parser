package javax.enterprise.deploy.spi;

import java.io.InputStream;
import java.io.OutputStream;
import javax.enterprise.deploy.model.DDBeanRoot;
import javax.enterprise.deploy.model.DeployableObject;
import javax.enterprise.deploy.spi.exceptions.BeanNotFoundException;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/enterprise/deploy/spi/DeploymentConfiguration.class */
public interface DeploymentConfiguration {
    DeployableObject getDeployableObject();

    DConfigBeanRoot getDConfigBeanRoot(DDBeanRoot dDBeanRoot) throws ConfigurationException;

    void removeDConfigBean(DConfigBeanRoot dConfigBeanRoot) throws BeanNotFoundException;

    DConfigBeanRoot restoreDConfigBean(InputStream inputStream, DDBeanRoot dDBeanRoot) throws ConfigurationException;

    void saveDConfigBean(OutputStream outputStream, DConfigBeanRoot dConfigBeanRoot) throws ConfigurationException;

    void restore(InputStream inputStream) throws ConfigurationException;

    void save(OutputStream outputStream) throws ConfigurationException;
}
