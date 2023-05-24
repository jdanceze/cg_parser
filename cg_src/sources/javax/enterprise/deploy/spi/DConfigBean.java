package javax.enterprise.deploy.spi;

import java.beans.PropertyChangeListener;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.model.XpathEvent;
import javax.enterprise.deploy.spi.exceptions.BeanNotFoundException;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/enterprise/deploy/spi/DConfigBean.class */
public interface DConfigBean {
    DDBean getDDBean();

    String[] getXpaths();

    DConfigBean getDConfigBean(DDBean dDBean) throws ConfigurationException;

    void removeDConfigBean(DConfigBean dConfigBean) throws BeanNotFoundException;

    void notifyDDChange(XpathEvent xpathEvent);

    void addPropertyChangeListener(PropertyChangeListener propertyChangeListener);

    void removePropertyChangeListener(PropertyChangeListener propertyChangeListener);
}
